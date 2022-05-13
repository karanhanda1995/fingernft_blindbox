// SPDX-License-Identifier: MIT
pragma solidity >=0.4.22 <0.9.0;
pragma experimental ABIEncoderV2;


contract BlindDomain {

  enum AssetType {ETH, ERC20, ERC1155, ERC721, ERC721Deprecated, ERC721COPY}

  struct Asset {
    address token;
    uint tokenId;
    AssetType assetType;
  }

  struct BlindKey {
    /* who signed the order */
    address owner;
    /* random number */
    uint salt;

    Asset[] sellAssets;
    Asset buyAsset;
  }

  struct BlindBox {
    BlindKey key;

    uint opening;
    bool repeat;
    uint startTime;
    uint endTime;

    uint buying;

    uint[] assetAmounts;

    uint sellerFee;
    string[] uris;
  }

  struct Sig {
    uint8 v;
    bytes32 r;
    bytes32 s;
  }


}
