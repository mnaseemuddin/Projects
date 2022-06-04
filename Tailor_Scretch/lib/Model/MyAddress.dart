// To parse this JSON data, do
//
//     final myAddress = myAddressFromJson(jsonString);

import 'dart:convert';

import 'package:flutter/material.dart';

MyAddress myAddressFromJson(String str) => MyAddress.fromJson(json.decode(str));

String myAddressToJson(MyAddress data) => json.encode(data.toJson());

class MyAddress {
    MyAddress({
        required this.status,
      required  this.message,
       required this.data,
    });

    String status;
    String message;
    List<Datum> data;

    factory MyAddress.fromJson(Map<String, dynamic> json) => MyAddress(
        status: json["status"],
        message: json["message"],
        data: List<Datum>.from(json["data"].map((x) => Datum.fromJson(x))),
    );

    Map<String, dynamic> toJson() => {
        "status": status,
        "message": message,
        "data": List<dynamic>.from(data.map((x) => x.toJson())),
    };
}

class Datum {
    Datum({
      required  this.tblUserAddressId,
       required this.userId,
       required this.addressType,
       required this.name,
       required this.email,
       required this.gender,
       required this.mobile,
       required this.address,
       required this.city,
       required this.state,
       required this.country,
       required this.zipcode,
       required this.status,
       required this.imgString,
       required this.color,
       required this.colorBoxWidth,
       required this.IsAddressSelected
    });

    int tblUserAddressId;
    int userId;
    String addressType;
    String name;
    String email;
    String gender;
    String mobile;
    String address;
    String city;
    String state;
    dynamic country;
    int zipcode;
    int status;
    String imgString;
    Color color;
    Color colorBoxWidth;
    bool IsAddressSelected;

    factory Datum.fromJson(Map<String, dynamic> json) => Datum(
        tblUserAddressId: json["tbl_user_address_id"],
        userId: json["user_id"],
        addressType: json["address_type"] == null ? null : json["address_type"],
        name: json["name"],
        email: json["email"] == null ? "" : json["email"],
        gender: json["gender"] == null ? "" : json["gender"],
        mobile: json["mobile"],
        address: json["address"],
        city: json["city"],
        state: json["state"],
        country: json["country"]== null ? "" : json["country"],
        zipcode: json["zipcode"],
        status: json["status"],
        imgString: "",color:
        Color(0xffffffff),colorBoxWidth:
        Color(0xffffffff),IsAddressSelected: false
    );

    Map<String, dynamic> toJson() => {
        "tbl_user_address_id": tblUserAddressId,
        "user_id": userId,
        "address_type": addressType == null ? null : addressType,
        "name": name,
        "email": email == null ? null : email,
        "gender": gender == null ? null : gender,
        "mobile": mobile,
        "address": address,
        "city": city,
        "state": state,
        "country": country,
        "zipcode": zipcode,
        "status": status,
    };
}
