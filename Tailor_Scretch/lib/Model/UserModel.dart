

import 'dart:convert';

UserModel userModelFromJson(String str)=>UserModel.formJson(json.decode(str));
String userModeltoJson(UserModel data)=>json.encode(data.toJson());


class UserModel{

  late String Status;
  late String Message;
 late final List<UserInfo>data;


 UserModel({required this.Status,required this.Message,required this.data});

  factory UserModel.formJson(Map<String,dynamic>json)=>
  UserModel(Status: json["status"], Message: json["message"], data:
  List<UserInfo>.from(json["data"].map((x)=>UserInfo.fromJson(x))));
   
  Map<String,dynamic>toJson()=>{
   "status":Status,
   "message":Message,
   "data":List<dynamic>.from(data.map((x) => x.toJson())),
  };

}

class UserInfo{
  late int UserId;
  late String FName;
  late String LName;
  late String EmailID;
  late String Password;
  late String ImgUrl;


  UserInfo({required this.UserId,required this.FName,required this.LName,required this.EmailID,
  required this.Password,required this.ImgUrl});

factory UserInfo.fromJson(Map<String,dynamic>json)=>
UserInfo(UserId: json["users_id"], FName: json["f_name"], LName: json["l_name"], EmailID: json["email"],
 Password: json["password"], ImgUrl: json["user_image"]);

Map<String ,dynamic>toJson()=>{
  "users_id":UserId,
  "f_name":FName,
  "l_name":LName,
  "email":EmailID,
  "password":Password,
  "user_image":ImgUrl

};

}
