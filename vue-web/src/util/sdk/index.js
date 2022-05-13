import utils_web3 from "@/util/web3/index";
var Web3 = require("web3");
import types from "./types";
import utils from "./utils";
import store from "@/store";
import truffle_contract from "@truffle/contract";
import constants from "./constants";
import BigNumber from "bignumber.js";
const eth_util = require("ethereumjs-util");

export default {
  async newERC721(owner, asset) {
    let abi = utils.contractAbi("CreateNFT721");
    let contract = await utils.contractAt(abi);
    if (contract.error) return contract;

    try {
      return await contract.new(
        asset.name,
        asset.symbol,
        asset.miner,
        asset.contractURI,
        asset.uri,
        { from: owner }
      );
    } catch (e) {
      return { error: e.message };
    }
  },
  async newERC1155(owner, asset) {
    let abi = utils.contractAbi("CreateNFT1155");
    let contract = await utils.contractAt(abi);
    if (contract.error) return contract;
    try {
      return await contract.new(
        asset.name,
        asset.symbol,
        asset.miner,
        asset.contractURI,
        asset.uri,
        { from: owner }
      );
    } catch (e) {
      return { error: e.message };
    }
  },
  async totalSupply(asset) {
    asset = this.getFullAsset(asset);
    let contract = await this.getAssetContract(asset);
    if (contract.error) return contract;
    return await contract.totalSupply();
  },
  async mintToken(owner, asset) {
    asset = this.getFullAsset(asset);
    let contract = await this.getAssetContract(asset);

    if (contract.error) return contract;
    try {
      return await contract.mint(
        asset.tokenId,
        asset.signature.v,
        asset.signature.r,
        asset.signature.s,
        asset.fees,
        asset.tokenURI,
        { from: owner }
      );
    } catch (e) {
      return { error: e.message };
    }
  },
  async mintErc1155Token(owner, asset) {
    asset = this.getFullAsset(asset);
    let contract = await this.getAssetContract(asset);
    if (contract.error) return contract;
    try {
      return await contract.mint(
        asset.tokenId,
        asset.signature.v,
        asset.signature.r,
        asset.signature.s,
        asset.fees,
        asset.quantity,
        asset.tokenURI,
        { from: owner }
      );
    } catch (e) {
      return { error: e.message };
    }
  },
  async isApprovedForAll(asset, owner, operator) {
    asset = this.getFullAsset(asset);
    let contract = await this.getAssetContract(asset);
    if (contract.error) return contract;

    try {
      return await contract.isApprovedForAll(owner, operator);
    } catch (e) {
      return { error: e.message };
    }
  },
  async setApprovalForAll(asset, coinbase, operator, approved) {
    asset = this.getFullAsset(asset);
    let contract = await this.getAssetContract(asset);
    if (contract.error) return contract;

    try {
      return await contract.setApprovalForAll(operator, approved, {
        from: coinbase,
      });
    } catch (e) {
      return { error: e.message };
    }
  },
  getFullAsset(asset) {
    let type = types.keyAssetType(asset.type);
    let abi = utils.contractAbi(type);
    asset.abi = abi;
    return asset;
  },
  async getBalance(asset, owner) {
    var web3 = await utils_web3.getWeb3();
    try {
      if (asset.address == this.NULL_ADDRESS()) {
        let balance = await web3.eth.getBalance(owner);
        return web3.utils.fromWei(balance.toString(), "ether");
      } else {
        asset.type = types.valueAssetType("ERC20");
        asset = this.getFullAsset(asset);
        let contract = await this.getAssetContract(asset);
        if (contract.error) return contract;
        return await contract.balanceOf(owner);
      }
    } catch (e) {
      return { error: e.message };
    }
  },
  async getNFTBalance(asset, owner) {
    var web3 = await utils_web3.getWeb3();
    try {
      asset = this.getFullAsset(asset);
      let contract = await this.getAssetContract(asset);
      if (contract.error) return contract;
      if (types.keyAssetType(asset.type) == "ERC1155") {
        return await contract.balanceOf(owner, asset.tokenId);
      } else {
        return await contract.balanceOf(owner);
      }
    } catch (e) {
      return { error: e.message };
    }
  },
  async getAssetContract(asset) {
    asset.contractAddress = asset.address;
    return await utils.contractAt(asset.abi, asset.contractAddress);
  },
  async transferAsset(asset, from, to) {
    asset = this.getFullAsset(asset);
    let contract = await this.getAssetContract(asset);
    if (contract.error) return contract;
    try {
      return await contract.transferFrom(from, to, asset.tokenId, {
        from: from,
      });
    } catch (e) {
      return { error: e.message };
    }
  },
  async transferAsset1155(asset, from, to) {
    var web3 = await utils_web3.getWeb3();
    asset = this.getFullAsset(asset);
    let contract = await this.getAssetContract(asset);
    if (contract.error) return contract;
    try {
      let data = web3.eth.abi.encodeParameters(
        ["address", "address", "uint256", "uint256"],
        [from, to, asset.tokenId, asset.quantity]
      );
      return await contract.safeTransferFrom(
        from,
        to,
        asset.tokenId,
        asset.quantity,
        data,
        { from: from }
      );
    } catch (e) {
      return { error: e.message };
    }
  },
  async burnAsset(asset) {
    asset = this.getFullAsset(asset);
    let contract = await this.getAssetContract(asset);
    if (contract.error) return contract;

    try {
      return await contract.burn(asset.tokenId, { from: asset.owner });
    } catch (e) {
      return { error: e.message };
    }
  },
  async burnAsset1155(asset) {
    asset = this.getFullAsset(asset);
    let contract = await this.getAssetContract(asset);
    if (contract.error) return contract;
    try {
      return await contract.burn(asset.owner, asset.tokenId, asset.quantity, {
        from: asset.owner,
      });
    } catch (e) {
      return { error: e.message };
    }
  },
  async cancelOrder(owner, asset) {
    let contract = await this.getExchangeContract(asset, owner);
    if (contract.error) return contract;

    let orderKey = utils.getOrderKey(asset);
    if (!orderKey) return { error: "error Token" };
    orderKey = utils.tupleOrderKey(orderKey);
    try {
      return await contract.cancel(orderKey, { from: owner });
    } catch (e) {
      return { error: e.message };
    }
  },
  async exchangeOrder(owner, asset) {
    let contract = await this.getExchangeContract(asset);
    if (contract.error) return contract;

    let order = utils.createOrder(asset);
    if (!order) return { error: "error Token" };
    let Sig = this.parseSignatureHex(asset.signature);
    Sig = [Sig.v, Sig.r, Sig.s];
    let buyerFeeSig = asset.buyerFeeSig;
    buyerFeeSig = [buyerFeeSig.v, buyerFeeSig.r, buyerFeeSig.s];
    order = utils.tupleOrder(order);
    let value = Web3.utils.toWei(asset.value, "ether").toString();
    try {
      return await contract.exchange(
        order,
        Sig,
        asset.buyerFee,
        buyerFeeSig,
        asset.amount,
        asset.buyer,
        { from: owner, value: value }
      );
    } catch (e) {
      return { error: e.message };
    }
  },
  async exchangeAuction(owner, asset) {
    let contract = await this.getAuctionContract(asset);
    if (contract.error) return contract;
    let order = utils.createAuctionOrder(asset);
    if (!order) return { error: "error Token" };
    let ownerSig = this.parseSignatureHex(asset.signature);
    ownerSig = [ownerSig.v, ownerSig.r, ownerSig.s];
    let buyerSig = this.parseSignatureHex(asset.bidSignature);
    buyerSig = [buyerSig.v, buyerSig.r, buyerSig.s];
    let buyerFeeSig = asset.buyerFeeSig;
    buyerFeeSig = [buyerFeeSig.v, buyerFeeSig.r, buyerFeeSig.s];
    order = utils.tupleAuctionOrder(order);

    let value = Web3.utils.toWei(asset.value, "ether").toString();

    try {
      return await contract.exchange(
        order,
        ownerSig,
        asset.bidBuying,
        buyerSig,
        asset.recipients,
        asset.buyerFee,
        buyerFeeSig,
        asset.buyer,
        { from: owner, value: value }
      );
    } catch (e) {
      return { error: e.message };
    }
  },
  async openBlindBox(owner, asset) {
    let contract = await this.getBlindBoxContract(asset);
    if (contract.error) return contract;
    let order = utils.createBlindOrder(asset);
    if (!order) return { error: "error Token" };
    let Sig = this.parseSignatureHex(asset.signature);
    Sig = [Sig.v, Sig.r, Sig.s];
    let buyerFeeSig = asset.buyerFeeSig;
    buyerFeeSig = [buyerFeeSig.v, buyerFeeSig.r, buyerFeeSig.s];
    order = utils.tupleBlindOrder(order);
    let value = Web3.utils.toWei(asset.value, "ether").toString();
    try {
      return await contract
        .open(
          order,
          Sig,
          asset.buyerFee,
          buyerFeeSig,
          asset.amount,
          asset.buyer,
          { from: owner, value: value }
        )
        .on("transactionHash", function(hash) {
          store.commit("BLINDBOX_SIGNED", true);
        });
    } catch (e) {
      return { error: e.message };
    }
  },

  async getExchangeContract(asset, owner) {
    let abi = utils.contractAbi("EXCHANGE");
    asset.contractAddress = asset.exchangeAddress;
    return await utils.contractAt(abi, asset.contractAddress);
  },
  async getAuctionContract(asset) {
    let abi = utils.contractAbi("AUCTION");
    asset.contractAddress = asset.auctionAddress;
    return await utils.contractAt(abi, asset.contractAddress);
  },
  async getBlindBoxContract(asset) {
    let abi = utils.contractAbi("BLINDBOX");
    asset.contractAddress = asset.blindBoxAddress;
    return await utils.contractAt(abi, asset.contractAddress);
  },
  async allowancePayToken(asset, owner, spender) {
    asset = this.getFullAsset(asset);
    let contract = await this.getAssetContract(asset);
    if (contract.error) return contract;
    try {
      return await contract.allowance(owner, spender);
    } catch (e) {
      return { error: e.message };
    }
  },
  async approvePayToken(asset, owner, spender) {
    asset = this.getFullAsset(asset);
    let contract = await this.getAssetContract(asset);
    if (contract.error) return contract;
    try {
      return await contract.approve(spender, constants.MAX_APPROVE_AMOUNT, {
        from: owner,
      });
    } catch (e) {
      return { error: e.message };
    }
  },
  parseSignatureHex(signature) {
    if (!signature) {
      return {
        v: 0,
        r: "0x0000000000000000000000000000000000000000000000000000000000000000",
        s: "0x0000000000000000000000000000000000000000000000000000000000000000",
      };
    }
    const validVParamValues = [27, 28];
    const ecSignatureRSV = _parseSignatureHexAsRSV(signature);
    if (validVParamValues.includes(ecSignatureRSV.v)) {
      return ecSignatureRSV;
    }
    const ecSignatureVRS = _parseSignatureHexAsVRS(signature);
    if (validVParamValues.includes(ecSignatureVRS.v)) {
      return ecSignatureVRS;
    }
    throw new Error("Invalid signature");
    function _parseSignatureHexAsVRS(signatureHex) {
      const signatureBuffer = eth_util.toBuffer(signatureHex);
      let v = signatureBuffer[0];
      if (v < 27) {
        v += 27;
      }
      const r = signatureBuffer.slice(1, 33);
      const s = signatureBuffer.slice(33, 65);
      const ecSignature = {
        v,
        r: eth_util.bufferToHex(r),
        s: eth_util.bufferToHex(s),
      };
      return ecSignature;
    }

    function _parseSignatureHexAsRSV(signatureHex) {
      const { v, r, s } = eth_util.fromRpcSig(signatureHex);
      const ecSignature = {
        v,
        r: eth_util.bufferToHex(r),
        s: eth_util.bufferToHex(s),
      };
      return ecSignature;
    }
  },
  getAssetType(asset) {
    switch (asset.type) {
      case "ERC721":
        return 3;
      case "ERC1155":
        return 2;
    }
    return 0;
  },
  keyOrderType(value) {
    return types.keyOrderType(value);
  },
  valueOrderType(key) {
    return types.valueOrderType(key);
  },
  keyCommonType(value) {
    return types.keyCommonType(value);
  },
  valueCommonType(key) {
    return types.valueCommonType(key);
  },
  keyAssetType(value) {
    return types.keyAssetType(value);
  },
  valueAssetType(key) {
    return types.valueAssetType(key);
  },
  NULL_ADDRESS() {
    return constants.NULL_ADDRESS;
  },
};
