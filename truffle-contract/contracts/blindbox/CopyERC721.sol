// SPDX-License-Identifier: MIT
pragma solidity >=0.4.22 <0.9.0;
pragma experimental ABIEncoderV2;

import "../lib/math/SafeMath.sol";
import "../lib/interface/IERC721Mintable.sol";
import "../exchange/OwnableOperatorRole.sol";


contract CopyERC721 is OwnableOperatorRole {
  using SafeMath for uint;

  mapping (address => uint256) public tokenIds;

  function safeMint(IERC721Mintable token, address to, string memory tokenURI) external onlyOperator {
    uint256 tokenId = tokenIds[address(token)].add(1);
    tokenIds[address(token)] = tokenId;
    token.safeMint(to, tokenId, tokenURI);
  }

}
