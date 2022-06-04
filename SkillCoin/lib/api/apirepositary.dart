
import 'dart:convert';
import 'dart:io';
import 'package:flutter/cupertino.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:http/http.dart' as http;
import '../model/buyskillcoinmodel.dart';
import '../model/liveratemodel.dart';
import '../model/profiledetailsmodel.dart';
import '../model/tdccurrencymodel.dart';
import '../model/hashkeymodel.dart';
import '../model/transactionhistorymodel.dart';
import '../model/usermodel.dart';
const String SUCCESS = 'success';
const String ERROR = 'failure';
const String baseurl="https://api.skill-coin.com/api/";
const String signUpUrl=baseurl+"signup";
const String URL_LIVE_TDC = 'http://new.stakingcrypto.global/api/LC/TdcLiveRate';
const String addAmountInWalletByTDCUrl=baseurl+"deposit";
const String loginUrl=baseurl+'login';
const String otpUrl=baseurl+"submit_otp";
const String forgotOTPUrl=baseurl+"new_password";
const String resendOTPUrl=baseurl+"re_send_otp";
const String updateProfileUrl=baseurl+"update_profile";
const String forgotPasswordUrl=baseurl+"reset_password";
const String transactionHistoryUrl=baseurl+"transaction";
const String currencyLiveRateURL="https://api.coingecko.com/api/v3/coins/markets?vs_currency=inr&order=market_cap_desc";
const String apiKey="9c4d89b0-808a-4a42-8071-5b061bdd80bb";
const String hashKeyCheckURL="https://api.bscscan.com/api?module=account&action=tokentx&contractaddress=0xf90dBf7FF178cc4fCfeCd2881a879675006E447f&address=0x1cAaa9a26E82Ec420681C75f8BA4fD8F3692086B&page=1&offset=5&startblock=0&endblock=999999999&sort=desc&apikey=BEC22IWQBPT9I43STQ2R3UEU1SC8SDFRYJ";

Map<String, String> currencyHeaders = {
  'Content-Type': 'application/json',
  'key':apiKey
};


Map<String, String> headers = {
  'Content-Type': 'application/json',
};


Future<ApiResponse>hashKeyAPI()async{
  try{
    final response=await http.get(Uri.parse(hashKeyCheckURL));
    Map data=json.decode(response.body);
    if(data['status']=="1"){
      HashKeyModel hashKeyModel=hashKeyModelFromJson(response.body);
      return ApiResponse(status: true,data: hashKeyModel,message: data['message']);
    }else{
      return ApiResponse(status: false,message: data['message'],data: null);
    }
  }catch(e){
    print('Error : $e');
    return ApiResponse(status: false,data: null,message: e.toString());
  }
}


Future<ApiResponse>buySkillCoinAPI(String payHash)async{
  try{
    String url="https://apilist.tronscan.org/api/transaction-info?hash="+payHash;
    final response=await http.get(Uri.parse(url));
    Map data=json.decode(response.body);
    debugPrint("Res  : "+data.toString());
    if(data['contractRet']=="SUCCESS"){
      BuySkillCoinModel skillCoin=buySkillCoinModelFromJson(response.body);
      return ApiResponse(status: true,data: skillCoin,message: data['message']);
    }else{
      return ApiResponse(status: false,message: data['message'],data: null);
    }
  }catch(e){
    print('Error : $e');
    return ApiResponse(status: false,data: null,message: e.toString());
  }
}

Future<ApiResponse>signUpAPI(Map body)async{

  try{
    final response=await http.post(Uri.parse(signUpUrl),body: body);
    Map data=json.decode(response.body);
    print(data.toString());
    if(data['status']==1){
      return ApiResponse(status: true,data: data,message: data['message']);
    }else{
      return ApiResponse(status: false,message: data['message'],data: null);
    }
  }catch(e){
    print('Error : $e');
    return ApiResponse(status: false,data: null,message: e.toString());
  }
}


Future<ApiResponse>resendCodeAPI(Map body)async{

  try{
    final response=await http.post(Uri.parse(resendOTPUrl),body: body);
    Map data=json.decode(response.body);
    print(data.toString());
    if(data['status']==1){
      return ApiResponse(status: true,data: data,message: data['message']);
    }else{
      return ApiResponse(status: false,message: data['message'],data: null);
    }
  }catch(e){
    print('Error : $e');
    return ApiResponse(status: false,data: null,message: e.toString());
  }
}


Future<ApiResponse>updateProfileAPI(Map body)async{

  try{
    final response=await http.post(Uri.parse(updateProfileUrl),body: body);
    Map data=json.decode(response.body);
    print(data.toString());
    if(data['status']==1){
      return ApiResponse(status: true,data: data,message: data['message']);
    }else{
      return ApiResponse(status: false,message: data['message'],data: null);
    }
  }catch(e){
    print('Error : $e');
    return ApiResponse(status: false,data: null,message: e.toString());
  }
}


Future<ApiResponse>transactionHistoryAPI(Map body)async{

  try{
    final response=await http.post(Uri.parse(transactionHistoryUrl),body: body);
    Map data=json.decode(response.body);
    print(data.toString());
    if(data['status']==1){
      TransactionHistoryModel historyModel=transactionModelFromJson(response.body);
      return ApiResponse(status: true,data: historyModel,message: data['message']);
    }else{
      return ApiResponse(status: false,message: 'No Record Found ..',data: null);
    }
  }catch(e){
    print('Error : $e');
    return ApiResponse(status: false,data: null,message: e.toString());
  }
}


Future<ApiResponse>loginAPI(Map body)async{

  try{
    final response=await http.post(Uri.parse(loginUrl),body: body);
    Map data=json.decode(response.body);
    print(data.toString());
    if(data['status']==1){
      UserModel userModel=userModelFromJson(response.body);
      return ApiResponse(status: true,data: userModel,message: data['message']);
    }else{
      return ApiResponse(status: false,message: data['message'],data: data);
    }
  }catch(e){
    print('Error : $e');
    return ApiResponse(status: false,data: null,message: e.toString());
  }
}


Future<ApiResponse>submitOTPAPI(Map body)async{

  try{
    final response=await http.post(Uri.parse(otpUrl),body: body);
    Map data=json.decode(response.body);
    print(data.toString());
    if(data['status']==1){
      UserModel userModel=userModelFromJson(response.body);
      return ApiResponse(status: true,data: userModel,message: data['message']);
    }else{
      return ApiResponse(status: false,message: data['message'],data: data);
    }
  }catch(e){
    print('Error : $e');
    return ApiResponse(status: false,data: null,message: e.toString());
  }
}



Future<ApiResponse>submitForgotWithOTPAPI(Map body)async{

  try{
    final response=await http.post(Uri.parse(forgotOTPUrl),body: body);
    Map data=json.decode(response.body);
    print(data.toString());
    if(data['status']==1){
      return ApiResponse(status: true,data: data,message: data['message']);
    }else{
      return ApiResponse(status: false,message: data['message'],data: data);
    }
  }catch(e){
    print('Error : $e');
    return ApiResponse(status: false,data: null,message: e.toString());
  }
}



Future<ApiResponse> liveTDCAPI() async {
  try{
    final String url = '$URL_LIVE_TDC';
    final response = await http.get(Uri.parse(url), headers: headers);
    Map data = jsonDecode(response.body);
    if(data['status'] != ERROR){
      TdcCurrencyModel model = tdcCurrencyModelFromJson(response.body);
      return ApiResponse(status: true, data: model, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}



Future<ApiResponse> liveSKLCoinRateAPI() async {
  try{
    const String url = baseurl+"liverate";
    final response = await http.post(Uri.parse(url), headers: headers);
    Map data = jsonDecode(response.body);
    if(data['status'] != 0){
      LiveRateModel model=liveRateModelFromJson(response.body);
      return ApiResponse(status: true, data: model, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

Future<ApiResponse> depositAmountInWalletAPI(Map body) async {
  try{

    final response = await http.post(Uri.parse(addAmountInWalletByTDCUrl), headers: headers, body: json.encode(body));
    Map data = jsonDecode(response.body);
    print(data.toString());
    if(data['status']==1){
      return ApiResponse(status: true, data: data, message: data['message'].toString());
    }else{
      return ApiResponse(status: false, data: data, message: data['message'].toString());
    }
  }catch(e){
    print(e.toString());
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}


Future<ApiResponse> buySKLAmountInWalletAPI(Map body) async {
  try{
    String url=baseurl+"buy";
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
    Map data = jsonDecode(response.body);
    print(data.toString());
    if(data['status']==1){
      return ApiResponse(status: true, data: data, message: data['message'].toString());
    }else{
      return ApiResponse(status: false, data: data, message: data['message'].toString());
    }
  }catch(e){
    print(e.toString());
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}


Future<ApiResponse> forgotPasswordAPI(Map body) async {
  try{

    final response = await http.post(Uri.parse(forgotPasswordUrl), headers: headers,
        body: json.encode(body));
    Map data = jsonDecode(response.body);
    print(data.toString());
    if(data['status']==1){
      return ApiResponse(status: true, data: data, message: data['message'].toString());
    }else{
      return ApiResponse(status: false, data: data, message: data['message'].toString());
    }
  }catch(e){
    print(e.toString());
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

Future<ApiResponse> withdrawalAPI(String url) async {
  try{
    // final String url = '$BASE_WITHDRAWAL?ContractAddressT=$CONT_ADDRESS&ToAddressT=$ToAddressT&AmountT=$AmountT&PKEYT=$PKEYT';
    final response = await http.get(Uri.parse(url), headers: headers);
    String body = response.body;
    Map data = jsonDecode(response.body);
    if(data['status'] != "failed"){
      return ApiResponse(status: true, data: data, message: data.toString());
    }else{
      return ApiResponse(status: false, data: data, message: data["Transactions"]);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}


Future<ApiResponse> sellSKLCoinAPI(String url) async {
  try{
    // final String url = '$BASE_WITHDRAWAL?ContractAddressT=$CONT_ADDRESS&ToAddressT=$ToAddressT&AmountT=$AmountT&PKEYT=$PKEYT';
    final response = await http.get(Uri.parse(url), headers: headers);
    String body = response.body;
    print('Response:'+body);
    if(body.contains('0x')){
      return ApiResponse(status: true, data: body, message: SUCCESS);
    }
    Map data = jsonDecode(response.body);
    if(data['status'] != ERROR){
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

Future<ApiResponse> withdrawAmountAPI(Map body) async {
  try{
    String url = baseurl+"withdrawal";
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
    Map data = jsonDecode(response.body);
    print(data.toString());
    if(data['status']==1){
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}


Future<ApiResponse> sellCoinAmountAPI(Map body) async {
  try{
    String url = baseurl+"sell";
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
    Map data = jsonDecode(response.body);
    print(data.toString());
    if(data['status']==1){
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}


Future<ApiResponse> walletBalanceAPI(Map body) async {
  try{
    String url = baseurl+"get_profile";
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
    Map data = jsonDecode(response.body);
    print(data.toString());
    if(data['status'] == 1){
      ProfileDetailsModel details=profileDetailsModelFromJson(response.body);
      return ApiResponse(status: true, data: details, message: "");
    }else{
      return ApiResponse(status: false, data: data, message: data["message"]);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

Future<ApiResponse>changePasswordAPI(Map body1)async{
  try{
    String changePasswordUrl=baseurl+"update_password";
    final response=await http.post(Uri.parse(changePasswordUrl),body: body1);
   Map data=json.decode(response.body);
    print(data);
    if(data["status"]==1){
      return ApiResponse(status: true,data: data,message: data["message"]);
    }else{
      return ApiResponse(status: false,data: data,message: data["message"]);
    }
  }catch(e){
    return ApiResponse(status: false,data: null,message: e.toString());
  }
}


Future<ApiResponse>uploadProfile(File? imgFile,String email) async {
  try {
    String uploadProfileImgUrl=baseurl+"update_profile_image";
    var ApiUrls = Uri.parse(uploadProfileImgUrl);
    var request = http.MultipartRequest("POST", ApiUrls);
    request.fields["userid"] = email;
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

class ApiResponse{
  bool status;
  String? message;
  dynamic data;

  ApiResponse({required this.status,this.message,this.data});
}