<script>
  export default {
    data () {
      return {
        showSaleDialog: false,
        showCancelSaleDialog: false,
        showBuyDialog: false,

        showBidDialog: false,
        showCancelBidDialog: false,
        showAcceptDialog: false,

        showTransferDialog: false,
        showBurnDialog: false,
        showAuctionDetailDialog: false,
        showSaleTypeDialog: false,
        showAuctionDialog: false,
        showAuctionCancelDialog: false,
        dialogNft: {},
        dialogOrder: {},
        dialogMyAsset: {},
        dialogToken: {},
        dialogNftURI: {},
        isAuctionType: false
      }
    },
    computed: {
      isLogin () {
        return this.$store.state.isLogin;
      },
      connected () {
        return this.$store.state.connected;
      },
    },
    methods: {
      showDialog (type, nft, order, myAsset, nftURI) {
        if (!this.$tools.needLogin(this.$route.path)) return;
        switch (type) {
          case "sale":
            this.showSaleDialog = true;
            this.dialogOrder = order;
            this.dialogNft = nft;
            this.dialogNftURI = nftURI;
            this.dialogMyAsset = myAsset;
            break;
          case "edit_sale":
            this.showSaleDialog = true;
            this.dialogNft = nft;
            this.dialogOrder = order;
            this.dialogNftURI = nftURI;
            this.dialogMyAsset = myAsset;
            break;
          case "cancel_sale":
            this.showCancelSaleDialog = true;
            this.dialogOrder = order;
            this.dialogNft = nft;
            this.dialogNftURI = nftURI;
            break;
          case "buy":
            this.showBuyDialog = true;
            this.dialogOrder = order;
            this.dialogNft = nft;
            this.dialogNftURI = nftURI;
            break;
          case "bid":
            this.showBidDialog = true;
            this.dialogOrder = order;
            this.dialogNft = nft;
            this.dialogNftURI = nftURI;
            this.dialogMyAsset = myAsset;
            break;
          case "edit_bid":
            this.showBidDialog = true;
            this.dialogOrder = order;
            this.dialogNft = nft;
            this.dialogMyAsset = myAsset;
            this.dialogNftURI = nftURI;
            break;
          case "cancel_bid":
            this.showCancelBidDialog = true;
            this.dialogOrder = order;
            this.dialogNft = nft;
            this.dialogNftURI = nftURI;
            break;
          case "accept":
            this.showAcceptDialog = true;
            this.dialogOrder = order;
            this.dialogNft = nft;
            this.dialogMyAsset = myAsset;
            this.dialogNftURI = nftURI;
            break;
          case "transfer":
            this.showTransferDialog = true;
            this.dialogOrder = order;
            this.dialogNft = nft;
            this.dialogNftURI = nftURI;
            break;
          case "burn":
            this.showBurnDialog = true;
            this.dialogOrder = order;
            this.dialogNft = nft;
            this.dialogNftURI = nftURI;
            break;
          case "auction_detail":
            this.showAuctionDetailDialog = true;
            this.dialogToken = { token: nft.address, tokenId: nft.tokenId };
            this.dialogOrder = order;
            this.dialogNft = nft;
            this.dialogNftURI = nftURI;
            break;
          case "sale_type":
            this.showSaleTypeDialog = true;
            this.dialogNft = nft;
            this.dialogNftURI = nftURI;
            this.dialogOrder = order;
            this.dialogMyAsset = myAsset
            this.isAuctionType = true;
            break;
          case "auction":
            this.showAuctionDialog = true;
            this.dialogOrder = order;
            this.dialogNft = nft;
            this.dialogNftURI = nftURI;
            this.dialogMyAsset = myAsset;
            break;
          case "edit_auction":
            this.showAuctionDialog = true;
            this.dialogOrder = order;
            this.dialogNft = nft;
            this.dialogNftURI = nftURI;
            this.dialogMyAsset = myAsset;
            break;
          case "cancel_auction":
            this.showAuctionCancelDialog = true;
            this.dialogOrder = order;
            this.dialogNftURI = nftURI;
            break;
        }
      },
      closeDialog () {
        this.showSaleDialog = false;
        this.showCancelSaleDialog = false;
        this.showBuyDialog = false;

        this.showBidDialog = false;
        this.showCancelBidDialog = false;
        this.showAcceptDialog = false;

        this.showTransferDialog = false;
        this.showBurnDialog = false;

        this.showAuctionDetailDialog = false;
        this.showSaleTypeDialog = false;
        this.showAuctionDialog = false;
        this.showAuctionCancelDialog = false;
        if (!this.isAuctionType) {
          this.dialogOrder = {};
          this.dialogNft = {};
          this.dialogNftURI = {};
          this.dialogMyAsset = {};
          this.dialogToken = {};
        };
        this.isAuctionType = false;

      },
      dialogConfirm () {
        this.closeDialog();
        this.reloadList();
      },
      dialogSelect (selectVal) {
        this.showSaleTypeDialog = false;
        this.$nextTick(() => {
          if (selectVal === 0) {
            this.showDialog("sale", this.dialogNft, this.dialogOrder, this.dialogMyAsset, this.dialogNftURI);
          } else {
            this.showDialog("auction", this.dialogNft, null, this.dialogMyAsset, this.dialogNftURI);
          }
        });
      },
    }
  }
</script>
