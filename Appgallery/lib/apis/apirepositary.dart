
import 'dart:convert';
import 'dart:io';

import 'package:appgallery/Model/allconsoleappsmodel.dart';
import 'package:appgallery/Model/allplaystoreapps.dart';
import 'package:appgallery/Model/appsdetailmodel.dart';
import 'package:appgallery/Model/usermodel.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:image_picker/image_picker.dart';

import 'apirepositary.dart';
import 'package:http/http.dart'as http;

late Map data;
late List? list;
const String status='status';
const String OK="success";
const String message='message';
const String imgUrl="http://3.111.16.227:8010/files/upload/app_screenshot/";
//http://3.111.16.227:5000/api/console_login/
const String baseurl="http://3.111.16.227:5000/api/";
const String csignUp=baseurl+"console_signup/";
const String usrsignUp=baseurl+"signup";
const String clogin=baseurl+"console_login/";
const String usrLoginUrl=baseurl+"login";
const String getAllAppsUrl=baseurl+"all_application_list";
const String uploadAppInfoUrl=baseurl+"Update_APK";
const String changePasswordUrl=baseurl+"account_update";
const String uploadProfileImgUrl=baseurl+"update_profile_image";
const String allPlayStoreAppsUrl=baseurl+"list_application";
const String saveProfileUrl=baseurl+"update_profile";
const String appDetailUrl=baseurl+"application_details";
const String getProfileUrl=baseurl+"get_profile";
const String submitReviewUrl=baseurl+"review";
const String updatePlayStoreProfileUrl=baseurl+"update_profile_playstore";
const String getPlayStoreProfileUrl=baseurl+"get_profile_playstore";


Map<String, String> headers = {
  'Content-Type': 'application/json',
  'Accept': 'application/json'
};

  Future<ApiResponse>signUpAPI(Map body1)async{
    try{
      final response=await http.post(Uri.parse(csignUp),body: body1);
      data=json.decode(response.body);
      print(data.toString());
      if(data[status]==1){
        return ApiResponse(status: true,data: data,message: data[message]);
      }else{
        return ApiResponse(status: false,data: data,message: data[message]);
      }
    }catch(e){
      return ApiResponse(status: false,data: null,message: e.toString());
    }
  }


Future<ApiResponse>usersignUpAPI(Map body1)async{
  try{
    final response=await http.post(Uri.parse(usrsignUp),body: body1);
    data=json.decode(response.body);
    print(data.toString());
    if(data[status]==1){
      return ApiResponse(status: true,data: data,message: data[message]);
    }else{
      return ApiResponse(status: false,data: data,message: data[message]);
    }
  }catch(e){
    return ApiResponse(status: false,data: null,message: e.toString());
  }
}

  Future<ApiResponse>loginAPI(Map body1)async{
    try{
      final response=await http.post(Uri.parse(clogin),body: body1);
      data=json.decode(response.body);
      print(data);
      if(data[status]==1){
        ConsoleLoginModel userModel=consoleModelFromJson(response.body);
        return ApiResponse(status: true,data: userModel,message: data[message]);
      }else{
        return ApiResponse(status: false,data: data,message: data[message]);
      }
    }catch(e){
      return ApiResponse(status: false,data: null,message: e.toString());
    }
  }



Future<ApiResponse>userloginAPI(Map body1)async{
  try{
    final response=await http.post(Uri.parse(usrLoginUrl),body: body1);
    data=json.decode(response.body);
    print(data);
    if(data[status]==1){
      UserLoginModel userModel=userModelFromJson(response.body);
      return ApiResponse(status: true,data: userModel,message: data[message]);
    }else{
      return ApiResponse(status: false,data: data,message: data[message]);
    }
  }catch(e){
    return ApiResponse(status: false,data: null,message: e.toString());
  }
}


Future<ApiResponse>normalUploadAppInfoAPI(Map body1)async {
  try{
    final response=await http.post(Uri.parse(uploadAppInfoUrl),body: body1);
    data=json.decode(response.body);
      return ApiResponse(status: true,data: data,message: data[message]);
  }catch(e){
    return ApiResponse(status: false,data: null,message: e.toString());
  }
}

Future<ApiResponse>changePasswordAPI(Map body1)async{
  try{
    final response=await http.post(Uri.parse(changePasswordUrl),body: body1);
    data=json.decode(response.body);
    print(data);
    if(data[status]==1){
      return ApiResponse(status: true,data: data[message],message: data[message]);
    }else{
      return ApiResponse(status: false,data: data,message: data[message]);
    }
  }catch(e){
    return ApiResponse(status: false,data: null,message: e.toString());
  }
}

Future<ApiResponse>saveProfileAPI(Map body1)async{
  try{
    final response=await http.post(Uri.parse(saveProfileUrl),body: body1);
    data=json.decode(response.body);
    print(data);
    if(data[status]==1){
      return ApiResponse(status: true,data: data[message],message: data[message]);
    }else{
      return ApiResponse(status: false,data: data,message: data[message]);
    }
  }catch(e){
    return ApiResponse(status: false,data: null,message: e.toString());
  }
}


Future<ApiResponse>getProfileAPI(Map body1)async{
  try{
    final response=await http.post(Uri.parse(getProfileUrl),body: body1);
    data=json.decode(response.body);
    print("Profile Details : "+data.toString());
    if(data[status]==1){
      return ApiResponse(status: true,data: data,message: data[message]);
    }else{
      return ApiResponse(status: false,data: data,message: data[message]);
    }
  }catch(e){
    return ApiResponse(status: false,data: null,message: e.toString());
  }
}


Future<ApiResponse>uploadProfile(File? imgFile,String email) async {
  try {
    var ApiUrls = Uri.parse(uploadProfileImgUrl);
    var request = http.MultipartRequest("POST", ApiUrls);
    request.fields["email_address"] = email;
    var img = await http.MultipartFile.fromPath("user_image", imgFile!.path);
    request.files.add(img);
    var response = await request.send();
    if(response.statusCode==201) {
      var responseData = await response.stream.toBytes();
      var responseString = String.fromCharCodes(responseData);
      var res=responseString.replaceAll("{", "").replaceAll("}", "");
      var res1=res.split(",");
      var message=res1[1].split(":");
     return ApiResponse(status: true,message: message[1].replaceAll('"', '').replaceAll('"', '').toString().trim(),data: null);
    } else {
   return ApiResponse(status: false,message:  message[1]
       .replaceAll('"', '')
       .replaceAll('"', '')
       .toString()
       .trim(),data: null);
    }
  } catch (error) {
    return ApiResponse(status: false,message: error.toString(),data: null);
  }
}


// Future<ApiResponse1>getAllPlayStoreApps()async{
//     try{
//       final response=await http.post(Uri.parse(allPlayStoreAppsUrl));
//        list=json.decode(response.body);
//        print(list);
//         return ApiResponse1(status: true,data: list,message: data[message]);
//     }catch(e){
//       print("Error"+e.toString());
//       return ApiResponse1(status: false,message: e.toString(),data: null);
//     }
// }

Future<ApiResponse>playStoreAppDetailsAPI(Map body1)async{
  try{
    final response=await http.post(Uri.parse(appDetailUrl),body: body1);
    data=json.decode(response.body);
    if(data[status]==1){
      AppsDetailModel apps=appsDetailModelFromJson(response.body);
      return ApiResponse(status: true,data: apps,message: data[message]);
    }else{
      return ApiResponse(status: false,data:null,message:data[message]);
    }
  }catch(e){
    return ApiResponse(status: false,message: e.toString(),data: null);
  }
}

Future<ApiResponse>getAllConsoleAppsAPI(Map body)async{

  try{
    final response=await http.post(Uri.parse(getAllAppsUrl),body: body);
    data=json.decode(response.body);
    if(data[status]==1){
      AllConsoleAppsModel console=allAppsModelFromJson(response.body);
      return ApiResponse(status: true,data: console,message: data[message]);
    }else{
      return ApiResponse(status: false,data: null,message: "No Record Found ..");
    }
  }catch(e){
    return ApiResponse(status: false,data: null,message: e.toString());
  }
}


Future<ApiResponse>submitReviewAPI(Map body1)async{
  try{
    final response=await http.post(Uri.parse(submitReviewUrl),body: body1);
    data=json.decode(response.body);
    print(data.toString()+body1.toString());
    if(data[status]==1){
      return ApiResponse(status: true,data: data,message: data[message]);
    }else{
      return ApiResponse(status: false,data:null,message:data[message]);
    }
  }catch(e){
    return ApiResponse(status: false,message: e.toString(),data: null);
  }
}


Future<ApiResponse>updatePlayStoreProfileAPI(Map body)async{

  try{
    final response=await http.post(Uri.parse(updatePlayStoreProfileUrl),body: body);
    Map data=json.decode(response.body);
    if(data['status']==1){
      return ApiResponse(status: true,data: data,message: data['message']);
    }else{
      return ApiResponse(status: false,message: data['message'],data: null);
    }
  }catch(e){
    return ApiResponse(status: false,data: null,message: e.toString());
  }
}

Future<ApiResponse>getPlayStoreProfileDetailsAPI(Map body)async{

  try{
    final response=await http.post(Uri.parse(getPlayStoreProfileUrl),body: body);
    Map data=json.decode(response.body);
    print(data.toString());
    if(data['status']==1){
      return ApiResponse(status: true,data: data,message: data['message']);
    }else{
      return ApiResponse(status: false,message: data['message'],data: null);
    }
  }catch(e){
    return ApiResponse(status: false,data: null,message: e.toString());
  }
}


Future<ApiResponse>uploadPlayStoreProfileAPI(File? imgFile,String email) async {
  try {
    String uploadProfileImgUrl=baseurl+"update_profile_image_playstore";
    var ApiUrls = Uri.parse(uploadProfileImgUrl);
    var request = http.MultipartRequest("POST", ApiUrls);
    request.fields["email"] = email;
    var img = await http.MultipartFile.fromPath("user_image", imgFile!.path);
    request.files.add(img);
    var response = await request.send();
    if(response.statusCode==201) {
      var responseData = await response.stream.toBytes();
      var responseString = String.fromCharCodes(responseData);
      var res=responseString.replaceAll("{", "").replaceAll("}", "");
      var res1=res.split(",");
      var message=res1[1].split(":");
      return ApiResponse(status: true,message: message[1].replaceAll('"', '').replaceAll('"', '').toString().trim(),data: null);
    } else {
      var responseData = await response.stream.toBytes();
      var responseString = String.fromCharCodes(responseData);
      var res=responseString.replaceAll("{", "").replaceAll("}", "");
      var res1=res.split(",");
      var message=res1[1].split(":");
      return ApiResponse(status: false,message:message[1]
          .replaceAll('"', '')
          .replaceAll('"', '')
          .toString()
          .trim(),data: null);
    }
  } catch (error) {
    return ApiResponse(status: false,message: error.toString(),data: null);
  }
}



class ApiResponse1 {
  bool status;
  String? message;
  List? data;

  ApiResponse1({required this.status, this.message, this.data});
}

  class ApiResponse {
    bool status;
    String? message;
    dynamic data;

    ApiResponse({required this.status, this.message, this.data});
  }
