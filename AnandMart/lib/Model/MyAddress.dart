



class MyAddress{

  String adId;
  String title;
  String fname;
  String locality;
  String plotNo;
  String city;
  String cityArea;
  String pincode;
  String state;
  String landmark;
  String mobileno;
  String addresshtml;
  String addressStatus;
  bool IsCheckMark;
  String address;

  MyAddress(
      {required this.adId,
        required this.title,
        required this.fname,
        required this.locality,
        required this.plotNo,
        required this.city,
        required this.cityArea,
        required this.pincode,
        required this.state,
        required this.landmark,
        required this.mobileno,
        required this.addresshtml,
        required this.addressStatus,required this.IsCheckMark,required this.address});

   factory MyAddress.formJson(Map<String,dynamic>json){
     return MyAddress(adId: json["ad_id"], title: json["title"], fname: json["fname"], locality: json["locality"],
         plotNo: json["plot_no"],
         city: json["city"], cityArea: json["city_area"], pincode: json["pincode"], state: json["state"],
         landmark: json["landmark"],
         mobileno: json["mobileno"], addresshtml: json["addresshtml"], addressStatus: json["address_status"],
     IsCheckMark: false,address: json["address"]);
   }
}