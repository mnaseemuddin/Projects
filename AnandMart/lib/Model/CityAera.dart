



class CityAera{

  String tblAreaId;
  String tblCityId;
  String areaName;
  String status;

  CityAera({required this.tblAreaId, required this.tblCityId, required this.areaName, required this.status});

  factory CityAera.formJson(Map<String,dynamic>json){
    return CityAera(tblAreaId: json["tbl_area_id"], tblCityId: json["tbl_city_id"], areaName: json["area_name"],
        status: json["status"]);
  }
}