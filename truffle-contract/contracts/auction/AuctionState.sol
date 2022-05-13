// SPDX-License-Identifier: MIT
pragma solidity >=0.4.22 <0.9.0;
pragma experimental ABIEncoderV2;

import "../exchange/ExchangeDomain.sol";
import "../exchange/OwnableOperatorRole.sol";


contract AuctionState is OwnableOperatorRole {

    // keccak256(OrderKey) => completed
    mapping(bytes32 => bool) public completed;

    function getCompleted(ExchangeDomain.OrderKey calldata key) view external returns (bool) {
        return completed[getCompletedKey(key)];
    }

    function setCompleted(ExchangeDomain.OrderKey calldata key) external onlyOperator {
        completed[getCompletedKey(key)] = true;
    }

    function getCompletedKey(ExchangeDomain.OrderKey memory key) pure public returns (bytes32) {
        return keccak256(abi.encodePacked(key.owner, key.sellAsset.token, key.sellAsset.tokenId, key.buyAsset.token, key.buyAsset.tokenId, key.salt));
    }
}
