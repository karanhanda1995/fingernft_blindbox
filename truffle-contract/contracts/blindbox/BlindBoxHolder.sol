// SPDX-License-Identifier: MIT
pragma solidity >=0.4.22 <0.9.0;
pragma experimental ABIEncoderV2;

import "./BlindDomain.sol";
import "../lib/utils/Ownable.sol";

contract BlindBoxHolder is Ownable {
  mapping(bytes32 => BlindBoxParams) internal blindboxs;
  mapping(address => mapping(uint256 => string)) internal _tokenURIs;

  struct URIParams {
    address token;
    uint256 tokenId;
    string uri;
  }

  struct BlindBoxParams {
    uint opening;
    bool repeat;
    uint startTime;
    uint endTime;
    uint buying;
    uint[] assetAmounts;
    uint sellerFee;
  }

  function setURIs(URIParams[] calldata uris) external onlyOwner {
    for(uint256 i = 0; i < uris.length; i++){
      _tokenURIs[uris[i].token][uris[i].tokenId] = uris[i].uri;
    }
  }

  function getURI(address token, uint256 tokenId) external view returns (string memory) {
    return _tokenURIs[token][tokenId];
  }

  function add(BlindDomain.BlindBox calldata blindbox) external onlyOwner {
    // require(msg.sender == blindbox.key.owner, "blindbox could be added by owner only");
    bytes32 key = prepareKey(blindbox);
    blindboxs[key] = BlindBoxParams(
      blindbox.opening,
      blindbox.repeat,
      blindbox.startTime,
      blindbox.endTime,
      blindbox.buying,
      blindbox.assetAmounts,
      blindbox.sellerFee
    );
  }

  function exists(BlindDomain.BlindBox calldata blindbox) external view returns (bool) {
    bytes32 key = prepareKey(blindbox);
    BlindBoxParams memory params = blindboxs[key];
    validateAmounts(blindbox, params);
    return params.opening == blindbox.opening && 
          params.repeat == blindbox.repeat &&
          params.startTime == blindbox.startTime &&
          params.endTime == blindbox.endTime &&
          params.buying == blindbox.buying &&
          params.sellerFee == blindbox.sellerFee;
  }

  function validateAmounts(BlindDomain.BlindBox memory blindbox, BlindBoxParams memory params) internal pure{
    require(blindbox.assetAmounts.length == params.assetAmounts.length, "incorrect assetAmounts length");
    for(uint i = 0; i < blindbox.assetAmounts.length; i++){
      require(blindbox.assetAmounts[i] == params.assetAmounts[i], "incorrect assetAmounts value");
    }
  }

  function prepareKey(BlindDomain.BlindBox memory blindbox) internal pure returns (bytes32) {
    return keccak256(abi.encode(blindbox.key));
  }

}

