// To parse this JSON data, do
//
//     final buySkillCoinModel = buySkillCoinModelFromJson(jsonString);

import 'dart:convert';

BuySkillCoinModel buySkillCoinModelFromJson(String str) => BuySkillCoinModel.fromJson(json.decode(str));

String buySkillCoinModelToJson(BuySkillCoinModel data) => json.encode(data.toJson());

class BuySkillCoinModel {
  BuySkillCoinModel({
    required this.block,
    required this.hash,
    required this.timestamp,
    required this.ownerAddress,
    required this.signatureAddresses,
    required this.contractType,
    required this.toAddress,
    required this.project,
    required this.confirmations,
    required this.confirmed,
    required this.revert,
    required this.contractRet,
    required this.data,
    required this.cost,
    required this.internalTransactions,
    required this.feeLimit,
    required this.tokenTransferInfo,
    required this.trc20TransferInfo,

  });

  int block;
  String hash;
  int timestamp;
  String ownerAddress;
  List<dynamic> signatureAddresses;
  int contractType;
  String toAddress;
  String project;
  int confirmations;
  bool confirmed;
  bool revert;
  String contractRet;
  String data;
  Map<String, int> cost;
  AddressTag internalTransactions;
  int feeLimit;
  TransferInfo tokenTransferInfo;
  List<TransferInfo> trc20TransferInfo;


  factory BuySkillCoinModel.fromJson(Map<String, dynamic> json) => BuySkillCoinModel(
    block: json["block"],
    hash: json["hash"],
    timestamp: json["timestamp"],
    ownerAddress: json["ownerAddress"],
    signatureAddresses: List<dynamic>.from(json["signature_addresses"].map((x) => x)),
    contractType: json["contractType"],
    toAddress: json["toAddress"],
    project: json["project"],
    confirmations: json["confirmations"],
    confirmed: json["confirmed"],
    revert: json["revert"],
    contractRet: json["contractRet"],
    data: json["data"],
    cost: Map.from(json["cost"]).map((k, v) => MapEntry<String, int>(k, v)),
    internalTransactions: AddressTag.fromJson(json["internal_transactions"]),
    feeLimit: json["fee_limit"],
    tokenTransferInfo: TransferInfo.fromJson(json["tokenTransferInfo"]),
    trc20TransferInfo: List<TransferInfo>.from(json["trc20TransferInfo"].map((x) => TransferInfo.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "block": block,
    "hash": hash,
    "timestamp": timestamp,
    "ownerAddress": ownerAddress,
    "signature_addresses": List<dynamic>.from(signatureAddresses.map((x) => x)),
    "contractType": contractType,
    "toAddress": toAddress,
    "project": project,
    "confirmations": confirmations,
    "confirmed": confirmed,
    "revert": revert,
    "contractRet": contractRet,
    "data": data,
    "cost": Map.from(cost).map((k, v) => MapEntry<String, dynamic>(k, v)),
    "internal_transactions": internalTransactions.toJson(),
    "fee_limit": feeLimit,
    "tokenTransferInfo": tokenTransferInfo.toJson(),
    "trc20TransferInfo": List<dynamic>.from(trc20TransferInfo.map((x) => x.toJson())),
  };
}

class AddressTag {
  AddressTag();

  factory AddressTag.fromJson(Map<String, dynamic> json) => AddressTag(
  );

  Map<String, dynamic> toJson() => {
  };
}








class TransferInfo {
  TransferInfo({

    required this.decimals,
    required this.name,
    required this.toAddress,
    required this.contractAddress,
    required this.type,
    required this.vip,
    required this.fromAddress,
    required this.amountStr,
  });


  int decimals;
  String name;
  String toAddress;
  String contractAddress;
  String type;
  bool vip;
  String fromAddress;
  String amountStr;

  factory TransferInfo.fromJson(Map<String, dynamic> json) => TransferInfo(
    decimals: json["decimals"],
    name: json["name"],
    toAddress: json["to_address"],
    contractAddress: json["contract_address"],
    type: json["type"],
    vip: json["vip"],
    fromAddress: json["from_address"],
    amountStr: json["amount_str"],
  );

  Map<String, dynamic> toJson() => {
    "decimals": decimals,
    "name": name,
    "to_address": toAddress,
    "contract_address": contractAddress,
    "type": type,
    "vip": vip,
    "from_address": fromAddress,
    "amount_str": amountStr,
  };
}



