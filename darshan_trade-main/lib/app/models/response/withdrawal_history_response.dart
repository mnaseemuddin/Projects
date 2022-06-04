import 'dart:convert';

List<WithdrawalHistoryResponse> withdrawalHistoryResponseFromJson(String str) => List<WithdrawalHistoryResponse>.from(json.decode(str).map((x) => WithdrawalHistoryResponse.fromJson(x)));

String withdrawalHistoryResponseToJson(List<WithdrawalHistoryResponse> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class WithdrawalHistoryResponse {
  WithdrawalHistoryResponse({
    required this.quantity,
    required this.transactionfee,
    required this.arrivalquantity,
    required this.chainname,
    required this.withdrawAddress,
    required this.remark,
    required this.withdrawtime,
    required this.withdrawstatus,
    required this.arrivaltime,
  });

  final double quantity;
  final double transactionfee;
  final double arrivalquantity;
  final String chainname;
  final String withdrawAddress;
  final String remark;
  final String withdrawtime;
  final String withdrawstatus;
  final String arrivaltime;

  factory WithdrawalHistoryResponse.fromJson(Map<String, dynamic> json) => WithdrawalHistoryResponse(
    quantity: (json["quantity"]??0.0).toDouble(),
    transactionfee: (json["transactionfee"]??0.0).toDouble(),
    arrivalquantity: (json["arrivalquantity"]??0.0).toDouble(),
    chainname: json["chainname"]??'',
    withdrawAddress: json["withdrawAddress"]??'',
    remark: json["remark"]??'',
    withdrawtime: json["withdrawtime"]??'15/36/2021 12:36:42',
    withdrawstatus: json["withdrawstatus"]??'Withdraw successful',
    arrivaltime: json["arrivaltime"]??'15/38/2021 12:38:00',
  );

  Map<String, dynamic> toJson() => {
    "quantity": quantity,
    "transactionfee": transactionfee,
    "arrivalquantity": arrivalquantity,
    "chainname": chainname,
    "withdrawAddress": withdrawAddress,
    "remark": remark,
    "withdrawtime": withdrawtime,
    "withdrawstatus": withdrawstatus,
    "arrivaltime": arrivaltime,
  };
}
