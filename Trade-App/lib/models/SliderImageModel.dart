import 'dart:convert';
//
// SliderImageModel sliderImageModelFromJson(String str)=>SliderImageModel.fromJson(json.decode(str));
//
// String sliderImageModeltoJson(SliderImageModel data)=>json.encode(data.toJson());


class SliderImageModel {

  int tblSliderId;
  String sliderImage;


  SliderImageModel({required this.tblSliderId, required this.sliderImage});
}
