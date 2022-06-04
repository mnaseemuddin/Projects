//
// import 'dart:convert';
// import 'package:http/http.dart' as http;
// import 'package:royal_q/app/models/country_model.dart';
//
// const String SUCCESS = 'success';
// const String ERROR = 'failure';
//
// Map<String, String> headers = {
//   'Content-Type': 'application/json',
//   'Accept': 'application/json'
// };
//
// Future<ApiResponse> countryFlagAPI() async {
//   try{
//     final String url = 'http://playbox.show/api/country_code_flag_api.php';
//     print(url);
//     final response = await http.get(Uri.parse(url), headers: headers);
//     Map data = jsonDecode(response.body);
//     if(data['status'] == '1'){
//       CountryModel model = countryModelFromJson(response.body);
//       return ApiResponse(status: true, data: model, message: SUCCESS);
//     }else{
//       return ApiResponse(status: false, data: data, message: ERROR);
//     }
//   }catch(e){
//     return ApiResponse(status: false, data: null, message: e.toString());
//   }
// }
//
//
// class ApiResponse{
//   bool status;
//   String? message;
//   dynamic data;
//   ApiResponse({required this.status,  this.message='', this.data});
//
// }