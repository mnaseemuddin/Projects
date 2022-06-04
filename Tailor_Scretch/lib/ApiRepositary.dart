import 'dart:convert';
import 'dart:io';
import 'dart:typed_data';
import 'package:flutter/services.dart';
import 'package:get/get.dart';
import 'package:http/http.dart' as http;
import 'package:dio/dio.dart';
import 'package:tailor/Model/CategoryAttribute.dart';
import 'package:tailor/Model/ChooseSketchModel.dart';
import 'package:tailor/Model/MyAddress.dart';
import 'package:tailor/Model/OrderDetailsModel.dart';
import 'package:tailor/Model/OrderHistory.dart';
import 'package:tailor/Model/ProfileModel.dart';
import 'package:tailor/Model/SSketch.dart';
import 'package:tailor/Model/SignUpModel.dart';
import 'package:tailor/Model/UserModel.dart';

//

const String baseUrl = "http://3.108.177.150:5000/api";
const String SignUpApi = baseUrl + "/users";
const String LoginApi = baseUrl + "/login";
const String saveProfileImage = baseUrl + "/users_image";
const String userdetails = baseUrl + "/users_details";
const String userProfileSave = baseUrl + "/users_update_profile";
const String orderHistory = baseUrl + "/order_list";
const String deleteDesignAPI = baseUrl + "/deleteSaveDesign";
const String orderDetailsAPI=baseUrl+"/getorderDetails";
const String DeleteAddressAPI=baseUrl+"/deleteaddress";
const String addNewAddressAPI=baseUrl+"/add_user_address";
const String updateAddressAPIS=baseUrl+"/editAddress";
const String SaveSketch = baseUrl + "/confirm_design";
const String SavedSketch = baseUrl + "/savedesign";
const String PlaceorderApi = baseUrl + "/confirm_order";
const String userAddressAPIURL=baseUrl+"/useraddresslist";
const String SaveDesignConfirmOrder=baseUrl+"/confirm_order_from_save_design";
const String ChooseSketchUrl = baseUrl + "/categories_list";
const String categoryWiseAttribute = baseUrl + "/category_wise_attribute";
const String forgotPasswordAPIURL=baseUrl+"/forgot_password";
BaseOptions options = new BaseOptions(
  baseUrl: "http://3.108.177.150:5000/api",
  connectTimeout: 10000,
  receiveTimeout: 10000,
);
final request = Dio(options);
late Map data;
const String SUCCESS = "success";
const String ERROR = "failure";
const String message = "message";

Map<String, String> headers = {
  'Content-Type': 'application/json',
  'Accept': 'application/json'
};
//Manojbisht842@gmail.com
Map<String, Uint8List> _cacheMap = {};

Future<ApiResponse> loginAPI(Map<String, String> mao) async {
  try {
    final response = await http.post(Uri.parse(LoginApi), body: mao);
    data = json.decode(response.body);
    print(data);
    if (data["status"] != ERROR) {
      UserModel userModel = userModelFromJson(response.body);
      return ApiResponse(
          status: true, data: userModel, message: data['message']);
    } else {
      return ApiResponse(status: false, data: null, message: data['message']);
    }
  } catch (e) {
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

Future<ApiResponse> signupAPI(Map<String, String> mao) async {
  try {
    final response = await http.post(Uri.parse(SignUpApi), body: mao);
    data = json.decode(response.body);
    //   final response=await request.post(SignUpApi,queryParameters: mao,options: Options(headers: {
    //     HttpHeaders.contentTypeHeader: "application/json",
    //   }));
    //  data=json.decode(response.data);
    if (data['status'] != ERROR) {
      SignUpModel _signModel = signUpModelFromJson(response.body);
      return ApiResponse(
          status: true, message: data["message"], data: _signModel);
    } else {
      return ApiResponse(status: false, message: data["message"], data: null);
    }
  } on DioError catch (ex) {
    if (ex.type == DioErrorType.connectTimeout) {
      throw Exception("Connection  Timeout Exception");
    }
    return ApiResponse(status: false, data: null, message: ex.toString());
  }
}

Future<ApiResponse> chooseSketchAPI() async {
  try {
    final response =
        await http.get(Uri.parse(ChooseSketchUrl), headers: headers);
    data = json.decode(response.body);
    if (data["status"] != ERROR) {
      ChooseSketchModel _sketchModel = chooseSketchModelFromJson(response.body);
      return ApiResponse(
          status: true, message: data["message"], data: _sketchModel);
    } else {
      return ApiResponse(status: true, message: data["message"], data: null);
    }
  } on DioError catch (ex) {
    if (ex.type == DioErrorType.connectTimeout) {
      throw Exception("Connection  Timeout Exception");
    }
    return ApiResponse(status: false, data: null, message: ex.toString());
  }
}

Future<ApiResponse> categoryAttributeAPI(int id) async {
  try {
    final response = await http.get(Uri.parse('$categoryWiseAttribute/$id'),
        headers: headers);
    data = json.decode(response.body);
    if (data["status"] != ERROR) {
      CategoryWiseAttribute model =
          categoryWiseAttributeFromJson(response.body);
      return ApiResponse(status: true, message: data["message"], data: model);
    } else {
      return ApiResponse(status: true, message: data["message"], data: null);
    }
  } on DioError catch (ex) {
    if (ex.type == DioErrorType.connectTimeout) {
      throw Exception("Connection  Timeout Exception");
    }
    return ApiResponse(status: false, data: null, message: ex.toString());
  }
}

Future<ApiResponse> getSaveSketchAPI(int userId) async {
  try {
    final response =
        await http.get(Uri.parse('$SavedSketch/$userId'), headers: headers);
    data = json.decode(response.body);
    if (data["status"] != ERROR) {
      SSketch ssketch = sSketchFromJson(response.body);
     // print('Saved Design :'+data.toString());
      return ApiResponse(status: true, message: data['message'], data: ssketch);
    } else {
      return ApiResponse(status: false, message: data['message'], data: null);
    }
  } on DioError catch (ex) {
    if (ex.type == DioErrorType.connectTimeout) {
      throw Exception("Connection  Timeout Exception");
    }
    return ApiResponse(status: false, data: null, message: ex.toString());
  }
}

Future<ApiResponse> getUserdetails(int userId) async {
  try {
    final response = await http.get(Uri.parse('$userdetails/$userId'));
    data = json.decode(response.body);
    if (data['status'] != ERROR) {
      ProfileModel profile = profileModelFromJson(response.body);
      return ApiResponse(status: true, message: data['message'], data: profile);
    } else {
      return ApiResponse(status: false, message: data['message'], data: null);
    }
  } on DioError catch (ex) {
    if (ex.type == DioErrorType.connectTimeout) {
      throw Exception("Connection  Timeout Exception");
    }
    return ApiResponse(status: false, data: null, message: ex.toString());
  }
}

Future<ApiResponse> saveProfiledetails(Map<String, String> mao) async {
  try {
    final response = await http.post(Uri.parse(userProfileSave), body: mao);
    data = json.decode(response.body);
    if (data['status'] != ERROR) {
      return ApiResponse(status: true, data: null, message: data["message"]);
    } else {
      return ApiResponse(status: false, data: null, message: data["message"]);
    }
  } on DioError catch (ex) {
    if (ex.type == DioErrorType.connectTimeout) {
      throw Exception("Connection  Timeout Exception");
    }
    return ApiResponse(status: false, data: null, message: ex.toString());
  }
}

Future<ApiResponse> getOrderHistory(int userId) async {
  try {
    final response =
        await http.get(Uri.parse('$orderHistory/$userId'), headers: headers);
    data = json.decode(response.body);
    if (data['status'] != ERROR) {
      OrderHistory orderHistory = orderHistoryFromJson(response.body);
      return ApiResponse(
          status: true, message: data[message], data: orderHistory);
    } else {
      return ApiResponse(status: false, message: data[message], data: null);
    }
  } on DioError catch (ex) {
    if (ex.type == DioErrorType.connectTimeout) {
      throw Exception("Connection  Timeout Exception");
    }
    return ApiResponse(status: false, data: null, message: ex.toString());
  }
}

Future<ApiResponse> deleteSketch(int userId, int tblUserDesignSaveId) async {
  try {
    final response = await http.get(
        Uri.parse('$deleteDesignAPI/$userId/$tblUserDesignSaveId'),
        headers: headers);
    data = json.decode(response.body);
    if (data['status'] != ERROR) {
      return ApiResponse(status: true, message: data[message], data: null);
    } else {
      return ApiResponse(status: false, message: data[message], data: null);
    }
  } on DioError catch (ex) {
    if (ex.type == DioErrorType.connectTimeout) {
      throw Exception("Connection  Timeout Exception");
    }
    return ApiResponse(status: false, data: null, message: ex.toString());
  }
}

Future<ApiResponse>getOrderDetailAPI(int userId,int tblorderId)async{
  try {
    final response = await http.get(
        Uri.parse('$orderDetailsAPI/$userId/$tblorderId'),
        headers: headers);
    data = json.decode(response.body);
    print(data);
    if (data['status'] != ERROR) {
      OrderDeatilsModel orderDeatilsModel=orderDeatilsModelFromJson(response.body);
      return ApiResponse(status: true, message: data[message], data: orderDeatilsModel);
    } else {
      return ApiResponse(status: false, message: data[message], data: null);
    }
  } on DioError catch (ex) {
    if (ex.type == DioErrorType.connectTimeout) {
      throw Exception("Connection  Timeout Exception");
    }
    return ApiResponse(status: false, data: null, message: ex.toString());
  }
}


Future<ApiResponse>saveAddressAPI(Map <String,String>mao)async{

   try {
    final response = await http.post(
        Uri.parse(addNewAddressAPI),
        body: mao);
    data = json.decode(response.body);
    if (data['status'] != ERROR) {
      return ApiResponse(status: true, message: data[message], data: data);
    } else {
      return ApiResponse(status: false, message: data[message], data: null);
    }
  } on DioError catch (ex) {
    if (ex.type == DioErrorType.connectTimeout) {
      throw Exception("Connection  Timeout Exception");
    }
    return ApiResponse(status: false, data: null, message: ex.toString());
  }

}



Future<ApiResponse>updateAddressAPI(Map <String,String>mao)async{

   try {
    final response = await http.post(
        Uri.parse(updateAddressAPIS),
        body: mao);
    data = json.decode(response.body);
    if (data['status'] != ERROR) {
      return ApiResponse(status: true, message: data[message], data: data);
    } else {
      return ApiResponse(status: false, message: data[message], data: null);
    }
  } on DioError catch (ex) {
    if (ex.type == DioErrorType.connectTimeout) {
      throw Exception("Connection  Timeout Exception");
    }
    return ApiResponse(status: false, data: null, message: ex.toString());
  }

}

Future<ApiResponse>getAddressAPI(int userId)async{
  try {
    final response = await http.get(
        Uri.parse('$userAddressAPIURL/$userId'),
        headers: headers);
    data = json.decode(response.body);
    if (data['status'] != ERROR) {
      MyAddress address=myAddressFromJson(response.body);
      return ApiResponse(status: true, message: data[message], data: address);
    } else {
      return ApiResponse(status: false, message: data[message], data: null);
    }
  } on DioError catch (ex) {
    if (ex.type == DioErrorType.connectTimeout) {
      throw Exception("Connection  Timeout Exception");
    }
    return ApiResponse(status: false, data: null, message: ex.toString());
  }
}

Future<ApiResponse>forgotPasswordAPI(Map<String,String>mao)async{
  try{
    final response =await http.post(Uri.parse(forgotPasswordAPIURL),body: mao);
    data=json.decode(response.body);
    print(data);
    if(data["status"]!=ERROR){
      return ApiResponse(status: true,message: data[message]);
    }else{
      return ApiResponse(status: false,message: data[message]);
    }
  }on DioError catch(ex){
    if (ex.type == DioErrorType.connectTimeout) {
      throw Exception("Connection  Timeout Exception");
    }
    return ApiResponse(status: false,message: ex.toString(),data: null);
  }
}

Future<Uint8List> loadImageByteData(url) async {
  if (_cacheMap.containsKey(url)) return _cacheMap[url]!;
  http.Response response = await http.get(
    Uri.parse(url),
  );
  _cacheMap[url] = response.bodyBytes;
  return response.bodyBytes;
}

class ApiResponse {
  bool status;
  String? message;
  dynamic data;

  ApiResponse({required this.status, this.message, this.data});
}
