// SPDX-License-Identifier: MIT
pragma solidity >=0.4.22 <0.9.0;
pragma experimental ABIEncoderV2;

import "../exchange/ExchangeDomain.sol";


contract AuctionDomain is ExchangeDomain {

    struct AuctionOrder {
        OrderKey key;

        /* how much has owner (in wei, or UINT256_MAX if ERC-721) */
        uint selling;
        /* how much wants owner (in wei, or UINT256_MAX if ERC-721) */
        uint buying;
        uint endTime;
        uint expiredTime;
        uint encourage;   // Encourage non buyers
        /* fee for selling */
        uint sellerFee;
    }
}
