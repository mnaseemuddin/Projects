

import 'dart:convert';
//
// City _cityModelFormdata(String str)=>City.fromJson(json.decode(str));
// String _cityModelToJson(City city)=>json.encode(city.toJson());




class City {
 
  
  String tblCityId;
  String name;
  String status;

  City({required this.tblCityId, required this.name, required this.status});
  
  factory City.formJson(Map<String,dynamic>json){
    return City(tblCityId: json["tbl_city_id"], name: json["name"], status: json["status"]);
  }
}
 