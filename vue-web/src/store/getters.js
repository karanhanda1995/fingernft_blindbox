import sdk from '@/util/sdk/index.js';
export default {
  payToken: (state) => (address) => {
    if(!address) return;
    for(var i = 0; i < state.payTokens.length; i++){
      let payToken = state.payTokens[i];
      if(payToken.address.toLocaleLowerCase() == address.toLocaleLowerCase()){
        return payToken;
      }
    }
  },
  payTokenBySymbol: (state) => (symbol) => {
    for(var i = 0; i < state.payTokens.length; i++){
      let payToken = state.payTokens[i];
      if(payToken.symbol == symbol){
        return payToken;
      }
    }
  },
  blindPayToken: (state) => (address, tokenId) => {
    if(!address) return;
    let payTokens = tokenId > 0 ? state.blindPayTokens : state.payTokens;
    for(var i = 0; i < payTokens.length; i++){
      let payToken = payTokens[i];
      if(payToken.address == address){
        if(tokenId == 0) return payToken;
        if(tokenId == payToken.tokenId) return payToken;
      }
    }
  },
  defaultSalePayToken: (state) => () => {
    if(!state.payTokens.length) return;
    let paytoken = state.defalutPayToken;
    if(paytoken) return paytoken;
    let paytokens = state.payTokens;
    let _paytokens = paytokens.filter(function(token){
      return !token.erc20;
    });
    if(_paytokens.length) return _paytokens[0];
    return paytokens[0];
  },
  defaultBidPayToken: (state) => () => {
    let paytoken = state.defalutPayToken;
    if(paytoken && paytoken.erc20) return paytoken;
    let paytokens = state.payTokens;
    let _paytokens = paytokens.filter(function(token){
      return token.erc20;
    });
    return _paytokens[0];
  },
  getBalance: (state) => (address) => {
    if(address == sdk.NULL_ADDRESS()) return state.ethBalance;
    return state.erc20Balance[address]
  },
  getBalanceV2: (state) => (payToken) => {
    if(payToken.address == sdk.NULL_ADDRESS())  return state.ethBalance;

    if(sdk.keyAssetType(payToken.type) == "ERC721" ||
        sdk.keyAssetType(payToken.type) == "ERC1155" ){
      let key = payToken.address + ":"  + payToken.tokenId;
      return state.nftBalance[key];
    }
    return state.erc20Balance[payToken.address];
  },
  category: (state) => (categoryId) => {
    for(var i = 0; i < state.categorys.length; i++){
      let category = state.categorys[i];
      if(category.id == categoryId) return category;
    }
  },
}
