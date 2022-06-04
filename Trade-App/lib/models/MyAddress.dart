import 'package:flutter/painting.dart';
//
// class MyAddress{
//
//
//   String adId;
//   String title;
//   String fname;
//   String locality;
//   String plotNo;
//   String city;
//   String cityArea;
//   String pincode;
//   String state;
//   String landmark;
//   String mobileno;
//   String addresshtml;
//   String addressStatus;
//   bool IsCheckMark;
//   String imgString;
//   Color color;
//   Color colorBoxWidth;
//   String address;
//
//   MyAddress(
//       {required this.adId,
//         required this.title,
//         required this.fname,
//         required this.locality,
//         required this.plotNo,
//         required this.city,
//         required this.cityArea,
//         required this.pincode,
//         required this.state,
//         required this.landmark,
//         required this.mobileno,
//         required this.addresshtml,
//         required this.addressStatus,required this.IsCheckMark,required this.imgString,
//         required this.colorBoxWidth,required this.color,required this.address});
//
// }


import 'package:flutter/painting.dart';

class MyAddress{

  int tblUserAddressId;
  String addressType;
  String name;
  String email;
  String mobile;
  String address;
  String city;
  String country;
  String imgString;
  Color color;
  Color colorBoxWidth;
  bool isAddressSelected;
  String state;
  int zipcode;


  MyAddress(
      {required this.tblUserAddressId,
        required this.addressType,
        required this.name,
        required this.email,
        required this.mobile,
        required this.address,
        required this.city,
        required this.country,required this.imgString,required this.color,required this.colorBoxWidth,
      required this.isAddressSelected,required this.state,required this.zipcode});

  factory MyAddress.fromJson(Map<String ,dynamic>json){
    return MyAddress(tblUserAddressId: json["tbl_user_address_id"], addressType:
    json["address_type"], name: json["name"], email: json["email"], mobile: json["mobile"], address: json["address"],
        city: json["city"], country: json["country"],
        imgString: "",color:
        Color(0xffffffff),colorBoxWidth:
        Color(0xffffffff),isAddressSelected: false,state: json['state'],zipcode: json['zipcode']);
  }

}