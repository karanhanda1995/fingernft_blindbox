// SPDX-License-Identifier: MIT
pragma solidity >=0.4.22 <0.9.0;
pragma experimental ABIEncoderV2;

import "../lib/math/SafeMath.sol";
import "../lib/interface/IERC1155.sol";
import "../lib/utils/StringLibrary.sol";
import "../lib/utils/BytesLibrary.sol";
import "../lib/interface/IERC721Mintable.sol";
import "../exchange/ERC20TransferProxy.sol";
import "../exchange/TransferProxy.sol";
import "./CopyERC721.sol";
import "./BlindState.sol";
import "./BlindDomain.sol";
import "../exchange/TransferProxyForDeprecated.sol";
import "../lib/contracts/HasSecondarySaleFees.sol";
import "../lib/utils/Ownable.sol";

contract BlindBox is Ownable, BlindDomain {
  using SafeMath for uint;
  using UintLibrary for uint;
  using StringLibrary for string;
  using BytesLibrary for bytes32;

  event Open(
    address indexed owner,
    address buyToken, uint256 buyTokenId, uint256 buyValue,
    address buyer,
    uint256 amount,
    uint256 salt
  );

  event OpenIndex(
    uint index
  );
  
  bytes4 private constant _INTERFACE_ID_FEES = 0xb7799584;
  uint256 private constant UINT256_MAX = 2 ** 256 - 1;

  address payable public beneficiary;
  address public buyerFeeSigner;
  address public constant blackHole = 0x0000000000000000000000000000000000000001;

  TransferProxy public transferProxy;
  TransferProxyForDeprecated public transferProxyForDeprecated;
  ERC20TransferProxy public erc20TransferProxy;
  BlindState public state;
  CopyERC721 copyERC721;

  constructor(
    TransferProxy _transferProxy,
    TransferProxyForDeprecated _transferProxyForDeprecated,
    ERC20TransferProxy _erc20TransferProxy,
    BlindState _state,
    CopyERC721 _copyERC721,
    address payable _beneficiary,
    address _buyerFeeSigner
  ) {
    transferProxy = _transferProxy;
    transferProxyForDeprecated = _transferProxyForDeprecated;
    erc20TransferProxy = _erc20TransferProxy;
    copyERC721 = _copyERC721;
    state = _state;
    beneficiary = _beneficiary;
    buyerFeeSigner = _buyerFeeSigner;
  }

  function setBeneficiary(address payable newBeneficiary) external onlyOwner{
    beneficiary = newBeneficiary;
  }

  function setBuyerFeeSigner(address newBuyerFeeSigner) external onlyOwner {
    buyerFeeSigner = newBuyerFeeSigner;
  }


  function open(
    BlindBox calldata blindbox,
    Sig calldata sig,
    uint buyerFee,
    Sig calldata buyerFeeSig,
    uint amount,
    address buyer
  ) payable external {
    validateTime(blindbox);
    validateBlindBoxSig(blindbox, sig);
    validateBuyerFeeSig(blindbox, buyerFee, buyerFeeSig);
    verifyState(blindbox.key, blindbox.assetAmounts, blindbox.opening, amount);
    if(buyer == address(0x0)){
      buyer = msg.sender;
    }

    compute(blindbox, amount, buyer, buyerFee);

    emitOpen(blindbox, amount, buyer);
  }


  function validateEthTransfer(uint value, uint buyerFee) internal view {
    uint256 buyerFeeValue = value.bp(buyerFee);
    require(msg.value == value.add(buyerFeeValue), "msg.value is incorrect");
  }


  function validateTime(
    BlindBox memory blindbox
  ) internal view {
    require((blindbox.startTime < block.timestamp && block.timestamp < blindbox.endTime ), "incorrect time");
  }


  function validateBlindBoxSig(
    BlindBox memory blindbox,
    Sig memory sig
  ) internal pure {
    require(prepareMessage(blindbox).recover(sig.v, sig.r, sig.s) == blindbox.key.owner, "incorrect signature");
  }


  function validateBuyerFeeSig(
    BlindBox memory blindbox,
    uint buyerFee,
    Sig memory sig
  ) internal view {
    require(prepareBuyerFeeMessage(blindbox, buyerFee).recover(sig.v, sig.r, sig.s) == buyerFeeSigner, "incorrect buyer fee signature");
  }


  function verifyState(BlindKey memory key, uint[] memory assetAmounts, uint opening, uint amount) internal view {
    uint[] memory completed = state.getCompleted(key);
    if(completed.length == 0) return;
    uint total = 0;
    uint completedTotal = 0;
    for(uint i = 0; i < assetAmounts.length; i++){
      total = total.add(assetAmounts[i]);
      completedTotal = completedTotal.add(completed[i]);
    }
    uint newCompleted = completedTotal.add( amount.mul(opening) );
    require(newCompleted <= total, "not enough stock of blindbox for buying");
  }


  function compute(BlindBox memory blindbox, uint256 amount, address buyer, uint buyerFee) internal{
    uint total = totalAmounts(blindbox);
    
    uint paying = blindbox.buying.mul(amount).div(total.div(blindbox.opening));
    if(blindbox.key.buyAsset.assetType == AssetType.ETH){
      validateEthTransfer(paying, buyerFee);
    }

    for(uint i = 0; i < amount; i++){
      (uint[] memory amounts, uint stock) = remainAmounts(blindbox);
      require(stock != 0, "incorrect stock");
      openBox(blindbox, stock, amounts, buyer, buyerFee);
    }

    if(blindbox.key.buyAsset.assetType == AssetType.ERC1155 || 
        blindbox.key.buyAsset.assetType == AssetType.ERC721 ){
      transferWithFeesPossibility(blindbox.key.buyAsset, paying, msg.sender, blackHole, true, blindbox.sellerFee, buyerFee, "");
    } else {
      transferWithFeesPossibility(blindbox.key.buyAsset, paying, msg.sender, blindbox.key.owner, true, blindbox.sellerFee, buyerFee, "");
    }
  }

  function totalAmounts(BlindBox memory blindbox) internal pure returns (uint total){
    total = 0;
    for(uint i = 0; i < blindbox.assetAmounts.length; i++){
      require(blindbox.key.sellAssets[i].assetType != AssetType.ETH && 
          blindbox.key.sellAssets[i].assetType != AssetType.ERC20, "incorrect sellAsset");
      total = total.add(blindbox.assetAmounts[i]);
    }
  }


  function remainAmounts(BlindBox memory blindbox) internal view returns (uint[] memory amounts, uint stock){
    uint[] memory completed = state.getCompleted(blindbox.key);
    amounts = new uint[](blindbox.assetAmounts.length);
    stock = 0;
    for(uint i = 0; i < blindbox.assetAmounts.length; i++){
      amounts[i] = blindbox.assetAmounts[i];
      if(completed.length > 0){
        amounts[i] = amounts[i].sub(completed[i]);
      }
      stock = stock.add(amounts[i]);
    }
  }


  function rewardIndex(uint[] memory amounts, uint stock) internal view returns (uint index) {
    uint256 luckyNumber = random(1, stock + 1, stock);
    index = 0;
    uint total = 0;
    for(uint z = 0; z < amounts.length; z++){
      total = total.add(amounts[z]);
      if(luckyNumber <= total){
        index = z;
        break;
      }
    }
 
    /*
    bool finished = false;
    for(uint z = 0; z < amounts.length; z++){
      total = total.add(amounts[z]);
      if(!finished && luckyNumber <= total){
        index = z;
        finished = true;
      }
    }
    */
  }


  function openBox(BlindBox memory blindbox, uint stock, uint[] memory amounts, address buyer, uint buyerFee) internal {
    for(uint j = 0; j < blindbox.opening; j++){
      uint index = rewardIndex(amounts, stock);
      transferBox(blindbox, index, buyer, buyerFee);
      if(blindbox.repeat){
        stock = stock.sub(1);
        amounts[index] = amounts[index].sub(1);
      }else{
        stock = stock.sub(amounts[index]);
        amounts[index] = 0;
      }
      emit OpenIndex(index);
    }
  }
  
  function transferBox(BlindBox memory blindbox, uint index, address buyer, uint buyerFee) internal {
    transferWithFeesPossibility(blindbox.key.sellAssets[index], 1, blindbox.key.owner, buyer, false, buyerFee, blindbox.sellerFee, blindbox.uris[index]);
    state.setCompleted(blindbox.key, index);
  }


  function random(uint256 from, uint256 to, uint256 salty) internal view returns (uint256){
    uint256 seed = uint256(
      keccak256(
        abi.encodePacked(
          block.timestamp + block.difficulty + block.gaslimit,
          ((uint256(keccak256(abi.encodePacked(block.coinbase)))) / (block.timestamp)) +
          block.gaslimit + 
          ((uint256(keccak256(abi.encodePacked(msg.sender)))) / (block.timestamp)) + 
          block.number + 
          salty
        )
      )
    );
    return seed.mod(to - from) + from;
  }


  function prepareBuyerFeeMessage(BlindBox memory blindbox, uint fee) public pure returns(string memory ){
    return keccak256(abi.encode(blindbox, fee)).toString();
  }


  function prepareMessage(BlindBox memory blindbox) public pure returns (string memory){
    return keccak256(abi.encode(blindbox)).toString();
  }


  function transferWithFeesPossibility(Asset memory firstType, uint value, address from, address to, bool hasFee, uint256 sellerFee, uint256 buyerFee, string memory uri) internal {
    if (!hasFee || 
        (firstType.assetType != AssetType.ETH && firstType.assetType != AssetType.ERC20)) {
      transfer(firstType, value, from, to, uri);
    } else {
      transferWithFees(firstType, value, from, to, sellerFee, buyerFee);
    }
  }


  function transfer(Asset memory asset, uint value, address from, address to, string memory uri) internal {
    if (asset.assetType == AssetType.ETH) {
      address payable toPayable = payable(to);
      toPayable.transfer(value);
    } else if (asset.assetType == AssetType.ERC20) {
      require(asset.tokenId == 0, "tokenId  be 0");
      erc20TransferProxy.erc20safeTransferFrom(IERC20(asset.token), from, to, value);
    } else if (asset.assetType == AssetType.ERC721) {
      require(value == 1, "value  be 1 for ERC-721");
      transferProxy.erc721safeTransferFrom(IERC721(asset.token), from, to, asset.tokenId);
    } else if (asset.assetType == AssetType.ERC721Deprecated) {
      require(value == 1, "value  be 1 for ERC-721");
      transferProxyForDeprecated.erc721TransferFrom(IERC721(asset.token), from, to, asset.tokenId);
    }else if(asset.assetType == AssetType.ERC721COPY){
      require(value == 1, "value be 1 for ERC-721-COPY");
      require(bytes(uri).length != 0, "uri be empty for ERC-721");
      copyERC721.safeMint(IERC721Mintable(asset.token), to, uri);
    } else {
      transferProxy.erc1155safeTransferFrom(IERC1155(asset.token), from, to, asset.tokenId, value, "");
    }
  }


  function transferWithFees(Asset memory firstType, uint value, address from, address to, uint256 sellerFee, uint256 buyerFee) internal {
    uint restValue = transferFeeToBeneficiary(firstType, from, value, sellerFee, buyerFee);
    address payable toPayable = payable(to);
    transfer(firstType, restValue, from, toPayable, "");
  }


  function transferFeeToBeneficiary(Asset memory asset, address from, uint total, uint sellerFee, uint buyerFee) internal returns (uint) {
    (uint restValue, uint sellerFeeValue) = subFeeInBp(total, total, sellerFee);
    uint buyerFeeValue = total.bp(buyerFee);
    uint beneficiaryFee = buyerFeeValue.add(sellerFeeValue);
    if (beneficiaryFee > 0) {
      transfer(asset, beneficiaryFee, from, beneficiary, "");
    }
    return restValue;
  }


  function emitOpen(BlindBox memory blindbox, uint amount, address buyer) internal {
    emit Open(
      blindbox.key.owner,
      blindbox.key.buyAsset.token,
      blindbox.key.buyAsset.tokenId, blindbox.buying,
      buyer,
      amount,
      blindbox.key.salt
    );
  }


  function subFeeInBp(uint value, uint total, uint feeInBp) internal pure returns (uint newValue, uint realFee) {
    return subFee(value, total.bp(feeInBp));
  }


  function subFee(uint value, uint fee) internal pure returns (uint newValue, uint realFee) {
    if (value > fee) {
      newValue = value - fee;
      realFee = fee;
    } else {
      newValue = 0;
      realFee = value;
    }
  }

}
