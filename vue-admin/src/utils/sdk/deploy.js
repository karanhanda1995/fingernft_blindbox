import utils from "./utils";

export default async (abiName, args) => {
  try {
    const contract = await utils.getContract(abiName);
    if (contract.error) {
      return contract;
    }
    const account = utils.getAccount();
    const result = await contract.new(...args, {
      from: account,
    });
    return result;
  } catch (e) {
    return {
      error: e.message,
    };
  }
};
