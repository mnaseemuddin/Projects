import 'dart:convert';

import 'package:http/http.dart' as http;
import 'package:movieshow/SAModel/category_model.dart';
import 'package:movieshow/SAModel/home_data_model.dart';
import 'package:movieshow/SAModel/tabitem_model.dart';


const String SUCCESS = 'success';
const String ERROR = 'failure';

const String BASE         = 'http://show.movie/api';
const String TabItems     = BASE + "/top_bar_menu_api.php";
const String HomeAPI      = BASE + "/home_api.php";
const String TabOptionAPI = BASE +"/category_wise_data.php";
Map<String, String> headers = {
  'Content-Type': 'application/json',
  'Accept': 'application/json'
};

Future<ApiResponse> getTabItems() async {
  try{
    final String url = TabItems;
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers);
    Map data = jsonDecode(response.body);
    if(data['status']=='1'){
      TabItemModel model = tabItemModelFromJson(response.body);
      return ApiResponse(status: true, data: model, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

Future<ApiResponse> homeDataUpAPI(Map body) async {
  try{
    // final response = await http.post(Uri.parse(HomeAPI), headers: headers, body: json.encode(body));
    final response = await http.post(Uri.parse(HomeAPI), body: body);
    Map data = jsonDecode(response.body);
    if(data['status']=='1'){
      HomeDataModel model = homeDataModelFromJson(response.body);
      return ApiResponse(status: true, data: model, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

Future<ApiResponse> categoryWiseDataUpAPI(Map body) async {
  try{
    final response = await http.post(Uri.parse(TabOptionAPI), body: body);
    Map data = jsonDecode(response.body);
    if(data['status']=='1'){
      CategoryModel model = categoryModelFromJson(response.body);
      return ApiResponse(status: true, data: model, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

// category_wise_data







class ApiResponse{
  bool status;
  String? message;
  dynamic data;
  ApiResponse({required this.status,  this.message='', this.data});

}