

// To parse this JSON data, do
//
//     final feeResponse = feeResponseFromJson(jsonString);

import 'dart:convert';

List<FeeResponse> feeResponseFromJson(String str) => List<FeeResponse>.from(json.decode(str).map((x) => FeeResponse.fromJson(x)));

String feeResponseToJson(List<FeeResponse> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class FeeResponse {
    FeeResponse({
        required this.transdate,
        required this.fee,
        required this.commissionAsset,
    });

    String transdate;
    String fee;
    String commissionAsset;

    factory FeeResponse.fromJson(Map<String, dynamic> json) => FeeResponse(
        transdate: json["transdate"],
        fee: json["fee"],
        commissionAsset: json["commissionAsset"],
    );

    Map<String, dynamic> toJson() => {
        "transdate": transdate,
        "fee": fee,
        "commissionAsset": commissionAsset,
    };
}
