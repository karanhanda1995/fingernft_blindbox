<script>
  export default {
    computed: {
      connected () {
        return this.$store.state.connected;
      },
      user () {
        return this.$store.state.user;
      },
    },
    methods: {
      getAddressAsset (items, sales, auctions, address) {
        let item = items.get(address)
        let sale = sales ? sales.get(address) : null
        let auction = auctions ? auctions.get(address) : null
        if (item) {
          if (item.onsell && item.onsellType == 1) {
            if (sale) {
              return { ...sale, ...item }
            } else {
              return item
            }
          } else if (item.onsell && item.onsellType == 2) {
            if (auction) {
              return { ...auction, ...item }
            } else {
              return item
            }
          } else {
            return item
          }
        } else {
          return;
        }
      },
      getLowestNftAsset (sales, items) {
        let lowestAsset;
        let _this = this
        sales.forEach(function (value, key) {
          let sale = value;
          sale.singlePrice = _this.$tools.singlePrice(
            sale.buyerValue,
            sale.sellValue,
            sale.payToken
          );
          sale.singleUsdtPrice = _this.$tools.decimal(
            sale.payToken ? sale.singlePrice * sale.payToken.rate : "0"
          );
          if (!items.get(key)) return;
          if (
            !lowestAsset
          ) {
            sale = { ...sale, ...items.get(key) }
            lowestAsset = sale;
          }
        }, sales)
        return lowestAsset;
      },
      getValidAuctions (auction, owners) {
        if (auction.status) return auction;
        let owner = owners.find(owner => auction.owner.toLocaleLowerCase() == (owner.owner || owner.itemOwner).toLocaleLowerCase());
        if (!owner) return;
        if (this.$tools.BigNumberGte(owner.quantity, auction.sellValue)) {
          return auction
        }
      },
      getOwnerQuantity (auctionOwner, items) {
        let owner = items[auctionOwner]
        return owner ? owner.quantity : 0
      },
      getLowestAuctionBid (auctions, items) {
        if (!auctions) return;
        let lowestAuctionBid;
        let _this = this;
        auctions.forEach(function (value, key) {
          if (!items.get(value.owner)) return;
          let nowTime = new Date().getTime() / 1000;
          if (nowTime > parseInt(value.endTime)) return;
          let quantity = items.get(value.owner).quantity;
          if (_this.$tools.BigNumberLt(quantity, value.sellValue)) return;
          if (!lowestAuctionBid) {
            lowestAuctionBid = value;
          }
        }, auctions)
        return lowestAuctionBid;
      },
      getActiveAddressBid (bids, address) {
        let bid = bids.get(address)
        if (bid) {
          return bid
        } else {
          return;
        }
      },
      getHighestBid (bids, onlyOwner) {
        let highestBid;
        let _this = this;
        bids.forEach(function (value, key) {
          if (value.owner == onlyOwner) return;
          if (
            value.expired ||
            // bid.status != 0 ||
            value.type == _this.$sdk.valueCommonType("CANCEL_BID") ||
            value.type == _this.$sdk.valueCommonType("ACCEPT")
          )
            return;
          value.singlePrice = _this.$tools.singlePrice(
            value.sellValue,
            value.buyerValue,
            value.payToken
          );
          if (!highestBid) {
            highestBid = value;
          }
        }, bids)
        return highestBid;
      }
    },
  };
</script>
