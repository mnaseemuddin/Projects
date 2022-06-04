
import 'dart:convert';

SignUpModel signUpModelFromJson(String str)=>SignUpModel.fromJson(json.decode(str));
String SignUpModeltoJson(SignUpModel data)=>json.encode(data.toJson());

class SignUpModel {
  late String status;
  late String message;


  SignUpModel({required this.status, required this.message});

  factory SignUpModel.fromJson(Map<String, dynamic> json) =>SignUpModel(status: json['status'],
      message: json['message'], );


  Map<String,dynamic>toJson()=>{
    "status":status,
    "message":message,

  };
  }
