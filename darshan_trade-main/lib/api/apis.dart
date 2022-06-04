import 'dart:convert';

import 'package:get_storage/get_storage.dart';
import 'package:http/http.dart' as http;
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/models/response/FOrderTransationResposne.dart';
import 'package:royal_q/app/modules/AffiliateApp/model/AffLevelTeamResponse.dart';
import 'package:royal_q/app/modules/AffiliateApp/model/AffLevelWiseTeamResponse.dart';
import 'package:royal_q/app/modules/AffiliateApp/model/AffiliateProfileDetailResponse.dart';
import 'package:royal_q/app/modules/AffiliateApp/model/AfiliateLevelIncomeResposne.dart';
import 'package:royal_q/app/modules/AffiliateApp/model/AllTradeResponse.dart';
import 'package:royal_q/app/modules/AffiliateApp/model/DepositTransactionHistoryResponse.dart';
import 'package:royal_q/app/modules/AffiliateApp/model/DirectTeamResponse.dart';
import 'package:royal_q/app/modules/AffiliateApp/model/PaymentHashResposne.dart';
import 'package:royal_q/app/modules/AffiliateApp/model/ROIResponse.dart';
import 'package:royal_q/app/models/response/allfees_response.dart';
import 'package:royal_q/app/models/response/billing_details_by_coin_model.dart';
import 'package:royal_q/app/models/response/levelwise_response.dart';
import 'package:royal_q/app/models/response/robottransation_response.dart';
import 'package:royal_q/app/models/response/totalbilling_resposne.dart';
import 'package:royal_q/app/modules/AffiliateApp/model/AffiliateLoginResponse.dart';
import 'package:royal_q/app/modules/AffiliateApp/model/StakeResponse.dart';
import 'package:royal_q/app/modules/dashboard/tabs/future/model/UserFutureCurrencyResponse.dart';
import 'package:royal_q/app/modules/dashboard/tabs/home/billing_detail/billing_detail_by_coin.dart';
import 'package:royal_q/main.dart';

import '../app/models/response/TotalFeesAssetResposne.dart';
import '../app/models/response/fee_response.dart';
import '../app/modules/dashboard/tabs/future/model/fCurrencyModel.dart';
import '../app/shared/utils/navigator_helper.dart';

const String SUCCESS = 'success';
const String ERROR = 'failure';
// https://jackbot.bittgc.com/api/account/Getcurchnglist?exchangetype=exchangeValue

//Old URL  const String BASE = 'http://jackbot.bittgc.com';
const String BASE="http://rpc.darshantrade.com";
const String affiliateBaseUrl="http://3.109.111.200:5000/api/";
const String affiliateSignUpAPIName="signup";
const String affilateLoginAPIName="login";
const String affilateRIOUrl=affiliateBaseUrl+"ROI_LIST_data";
const String affiliateDirectUrl=affiliateBaseUrl+"Direct_team_list";
const String affiliateLevelWiseUrl=affiliateBaseUrl+"User_Level_data_List";
const String affiliateProfileUpdateUrl=affiliateBaseUrl+"update_profile";
const String affiliateLevelTeamIUrl=affiliateBaseUrl+"Level_team_list";
const String affiliateLevelIncomeUrl=affiliateBaseUrl+"Level_income_LIST_data";
// const String BASE = isExpertGain?'https://royalq.bittgc.com':'http://bittgc.io';
const String hashKeyCheckURL="https://api.bscscan.com/api?module=account&action=tokentx&contractaddress=0xf90dBf7FF178cc4fCfeCd2881a879675006E447f&address=0x1cAaa9a26E82Ec420681C75f8BA4fD8F3692086B&page=1&offset=5&startblock=0&endblock=999999999&sort=desc&apikey=BEC22IWQBPT9I43STQ2R3UEU1SC8SDFRYJ";
const String BASEAPI = '$BASE/api';
const String fBASE="http://frpc.darshantrade.com";
const String fBASEAPI="$fBASE/api/future";
const String messgae="message";
const String status="status";
const String profileDetailUrl=affiliateBaseUrl+"User_profile";
const String stakeListUrl=affiliateBaseUrl+"Stake_period_list";
late Map data;
final storage=GetStorage();


//
// const String URL_LIVE_TDC = 'http://new.stakingcrypto.global/api/LC/TdcLiveRate';
//
// const String BASE_WITHDRAWAL = 'http://mainbusd.liveshop.co.in/';
// const String CONT_ADDRESS = '0x6f13598e40d15228c7132b904957c80be6895cfe';
// const String TO_ADDRESS = '0xf99b296D378b4572f5e1D9fCE269fe0f9817f69A';
// const String AMOUNT = '1000000000';
// const String PKEYT = '0x14bc2b0f8d64fc93cea0bf97c4eb5a7a8406622fda8f36fc24677691cf1c4438';


Map<String, String> headers = {
  'Content-Type': 'application/json',
  'Accept': 'application/json'
};


var postHeaders = {
  'Content-Type': 'application/json'
};

Map<String, dynamic> _apiCache = {};



Future<ApiResponse>checkPaymentHashKeyAPI(String hashKey)async{
  try{
    String url="https://apilist.tronscan.org/api/transaction-info?hash=$hashKey";
    final response=await http.get(Uri.parse(url));
    Map data=json.decode(response.body);
    if(data.isNotEmpty){
      return ApiResponse(status: true,data: data,message: "");
    }else{
      return ApiResponse(status: false,message: "",data: null);
    }
  }catch(e){
    print('Error : $e');
    return ApiResponse(status: false,data: null,message: e.toString());
  }
}


Future<ApiResponse> getDirectTeamAPI(id, token, refferal) async {
  try{
    // final String url = '$BASEAPI/account/GetCurrency/${isExpertGain?1:2}';
    final String url = affiliateDirectUrl;
    final response = await http.post(Uri.parse(url),body: {"referal_code":
    "$refferal", "last_id":"0"},
        headers:{ "Authorization":"Bearer $token"});
    Map data = jsonDecode(response.body);
    print(data);
    if(data[status]){
      AffDirectTeamResponse model=affdirectTeamResponseFromJson(response.body);
      return ApiResponse(status: true, data: model, message: data[messgae]);
    }else{
      return ApiResponse(status: false, data: data, message: data[messgae]);
    }
  }catch(e){
    print(e.toString());
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}



Future<ApiResponse> getLevelIncomeListAPI(id, token, refferal) async {
  try{
    // final String url = '$BASEAPI/account/GetCurrency/${isExpertGain?1:2}';
    final String url = affiliateLevelIncomeUrl;
    final response = await http.post(Uri.parse(url),body: {"referal_code":
    "$refferal", "last_id":"0"},
        headers:{ "Authorization":"Bearer $token"});
    Map data = jsonDecode(response.body);
    print("level Income : "+data.toString());
    if(data[status]){
      AffiliateLevelIncomeResponse model=affiliateLevelIncomeResponseFromJson(response.body);
      return ApiResponse(status: true, data: model, message: data[messgae]);
    }else{
      return ApiResponse(status: false, data: data, message: data[messgae]);
    }
  }catch(e){
    print(e.toString());
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}


Future<ApiResponse> getProfileDetailAPI(id,token) async {
  try{
    // final String url = '$BASEAPI/account/GetCurrency/${isExpertGain?1:2}';
    final String url = profileDetailUrl;
    final response = await http.post(Uri.parse(url),body: {"user_id":
    "$id"},
        headers:{ "Authorization":"Bearer $token"});
    Map data = jsonDecode(response.body);
    print(data);
    if(data[status]){
      AffiliateProfileDetailResponse model=affiliateProfileDetailResponseFromJson(response.body);
      return ApiResponse(status: true, data: model, message: data[messgae]);
    }else{
      return ApiResponse(status: false, data: data, message: data[messgae]);
    }
  }catch(e){
    print(e.toString());
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

Future<ApiResponse> getTransationHistoryAPI(id,token) async {
  try{
    // final String url = '$BASEAPI/account/GetCurrency/${isExpertGain?1:2}';
    final String url = affiliateBaseUrl+"Transection_history_LIST_data";
    final response = await http.post(Uri.parse(url),body: {"user_id":"$id"},
        headers:{ "Authorization":"Bearer $token"});
    Map data = jsonDecode(response.body);
    print(data);
    if(data[status]){
      DepositTransationResponse model=depositTransationResponseFromJson(response.body);
      return ApiResponse(status: true, data: model, message: data[messgae]);
    }else{
      return ApiResponse(status: false, data: data, message: data[messgae]);
    }
  }catch(e){
    print("erro");
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}


Future<ApiResponse> fundDepositAPI(token, Map hashMap) async {
  try{
    // final String url = '$BASEAPI/account/GetCurrency/${isExpertGain?1:2}';
    final String url = affiliateBaseUrl+"Add_deposite_amount";
    final response = await http.post(Uri.parse(url),body: hashMap,
        headers:{ "Authorization":"Bearer $token"});
    Map data = jsonDecode(response.body);
    print(data);
    if(data[status]){
      return ApiResponse(status: true, data: data, message: data[messgae]);
    }else{
      return ApiResponse(status: false, data: data, message: data[messgae]);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}


Future<ApiResponse> getLevelWiseTeamAPI(id, token, refferal, Map hashMap) async {
  try{
    // final String url = '$BASEAPI/account/GetCurrency/${isExpertGain?1:2}';
    final String url = affiliateLevelWiseUrl;
    print(url);
    final response = await http.post(Uri.parse(url),body: hashMap,
        headers:{ "Authorization":"Bearer $token"});
    Map data = jsonDecode(response.body);
    print(data);
    if(data[status]){
      AffLevelWiseTeamResponse model=affLevelWiseTeamResponseFromJson(response.body);
      return ApiResponse(status: true, data: model, message: data[messgae]);
    }else{
      return ApiResponse(status: false, data: data, message: data[messgae]);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}


Future<ApiResponse> saveProfileDetailAPI(Map hashMap, String? token) async {
  try{
    // final String url = '$BASEAPI/account/GetCurrency/${isExpertGain?1:2}';
    final String url = affiliateProfileUpdateUrl;
    final response = await http.post(Uri.parse(url),body: hashMap,
        headers:{ "Authorization":"Bearer $token"});
    Map data = jsonDecode(response.body);
    print(data);
    if(data[status]){
      print("Successfully .");
      return ApiResponse(status: true, data: data, message: data["message"]);
    }else{
      print("Failed ..");
      return ApiResponse(status: false, data: data, message: data["error"]);
    }
  }catch(e){
    print("Something is Error..");
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}


Future<ApiResponse> getLevelTeamAPI(id, token, refferal) async {
  try{
    // final String url = '$BASEAPI/account/GetCurrency/${isExpertGain?1:2}';
    final String url = affiliateLevelTeamIUrl;
    final response = await http.post(Uri.parse(url),body: {"referal_code":
    "$refferal"},
        headers:{ "Authorization":"Bearer $token"});
    Map data = jsonDecode(response.body);
    print(data);
    if(data[status]){
      AffLevelTeamResponse model=affLevelTeamResponseFromJson(response.body);
      return ApiResponse(status: true, data: model, message: data[messgae]);
    }else{
      return ApiResponse(status: false, data: data, message: data[messgae]);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

Future<ApiResponse> getROIListAPI(id, token) async {
  try{
    // final String url = '$BASEAPI/account/GetCurrency/${isExpertGain?1:2}';
    final String url = affilateRIOUrl;
    final response = await http.post(Uri.parse(url),body: {"user_id":id.toString()},
        headers:{ "Authorization":"Bearer $token"});
    Map data = jsonDecode(response.body);
    print(data);
    if(data[status]) {
      RoiResposne model=roiResposneFromJson(response.body);
      return ApiResponse(status: true, data: model, message: data[messgae]);
    }else{
      return ApiResponse(status: false, data: data, message: data[messgae]);
    }
  }catch(e){
    print("Error"+e.toString());
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

Future<ApiResponse> getRDMAPI(id) async {
  try{
    // final String url = '$BASEAPI/account/GetCurrency/${isExpertGain?1:2}';
    final String url = '$BASEAPI/account/GetCurrencybyid/$id';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers);
    Map data = jsonDecode(response.body);
    if(response.statusCode==200){
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

Future<ApiResponse> getFundBalanceAPI() async {
  try{
    final String url = '$BASEAPI/account/GetFundbalance/${userInfo!.id}';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers);
    Map data = jsonDecode(response.body);
    if(response.statusCode==200){
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: {"totalAmount":"75.00"}, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: {"totalAmount":"75.00"}, message: e.toString());
  }
}

Future<ApiResponse> getFundListAPI() async {
  try{
    final String url = '$BASEAPI/account/GetFundList/${userInfo!.id}';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers);
    if(response.statusCode==200){
      List<FundListResponse> data = fundListResponseFromJson(response.body);
      data.sort((a, b) => b.dateTime!.compareTo(a.dateTime!));
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: <FundListResponse>[], message: ERROR);
    }
  }catch(e){
    print(e.toString());
    return ApiResponse(status: false, data: <FundListResponse>[], message: e.toString());
  }
}

Future<ApiResponse> graphDataAPI({baseMarket='btcusdt'}) async {
  try{
    // final String url = 'https://api.wazirx.com/api/v2/market-status';
    // final String url = 'https://api.wazirx.com/api/v2/tickers';
    // final String url = 'https://api.wazirx.com/api/v2/trades?market=btcusdt';
    final String url = 'https://api.wazirx.com/api/v2/tickers/$baseMarket';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers);
    Map data = jsonDecode(response.body);
    if(response.statusCode==200){
      GraphModel model = graphModelFromJson(response.body);
      return ApiResponse(status: true, data: model, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

Future<ApiResponse> graphDatgraphDataNameAPIaNameAPI() async {
  try{
    final String url = 'https://api.wazirx.com/api/v2/market-status';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers);
    Map data = jsonDecode(response.body);
    if(response.statusCode==200){
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

Future<ApiResponse> getNewAddressAPI() async {
  try{
    String url = '$BASEAPI/account/GenerateAddress?pair=1';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers);
    Map data = jsonDecode(response.body);
    // if(data['Success'] == '1'){
    return ApiResponse(status: true, data: data, message: SUCCESS);
    // }else{
    //   return ApiResponse(status: false, data: data, message: ERROR);
    // }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

// Future<ApiResponse> getApiSecretKeyAPI(body) async {
//   String key = 'id_${body['id']}_exchanetype_${body['exchanetype']}';
//   if(_apiCache.containsKey(key)){
//     return _apiCache[key];
//   }
//   return getApiSecretKey(body);
// }

Future<ApiResponse> getApiSecretKeyAPI(body) async {
  // String key = 'id_${body['id']}_exchanetype_${body['exchanetype']}';
  try{
    String url = '$BASEAPI/account/GetApiSecretKey';
    print(url);
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
    Map data = jsonDecode(response.body);
    if(data['status'] == 'succeed'){
      ApiKeyResponse data = apiKeyResponseFromJson(response.body);
      // _apiCache[key] = ApiResponse(status: true, data: data, message: SUCCESS);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: {'message': e.toString()}, message: e.toString());
  }
}

Future<ApiResponse> depositFundAPI(Map body) async {
  try{
    String url = '$BASEAPI/account/DepositFund';
    print(url);
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
    Map data = jsonDecode(response.body);
    if(data['status'] == 'succeed'){
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

Future<ApiResponse> bindSpotTradeAPI(Map body) async {
  try{
    String url = '$BASEAPI/account/BindSpotTrade';
    print(url);
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
    Map data = jsonDecode(response.body);
    if(data['status'] == 'succeed'){
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

Future<ApiResponse> bindApiSecretKeyAPI(Map body) async {
  try{
    String url = '$BASEAPI/account/BindApiSecretKey';
    print(url);
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
    Map data = jsonDecode(response.body);
    if(data['status'] == 'succeed'){
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

Future<ApiResponse> sendEmailOTPAPI(Map body) async {
  try{
    String url = '$BASEAPI/Email/Send';
    print(url);
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
    String res = response.body;
    print(res);
    // Map data = jsonDecode(response.body);
    // if(res == '\"Success\"'){
    if(response.statusCode==200){
      return ApiResponse(status: true, data: res, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: res, message: ERROR);
    }
  }catch(e){
    print(e.toString());
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

Future<ApiResponse> getCurChngListAPI(exchangetype) async {
  try{
    // https://royalq.bittgc.com/api/account/Getcurchnglist?exchangetype=2
    String url = '$BASEAPI/account/GetCurChngList?exchangetype=$exchangeValue';
    // print(url);
    final response = await http.get(Uri.parse(url), headers: headers);
      List<CurrencyModel> data = currencyModelFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

Future<ApiResponse> getCurUserChngListAPI(exchangetype) async {
  try{
    // https://royalq.bittgc.com/api/account/Getcurchnglist?exchangetype=2
    String url = '$BASEAPI/account/GetCurUserChngList/${userInfo!.id}/$exchangeValue';

    final response = await http.get(Uri.parse(url), headers: headers);
   // print(response.body);
    List<UserCurrencyResponse> data = userCurrencyResponseFromJson(response.body);
    return ApiResponse(status: true, data: data, message: SUCCESS);
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

Future<ApiResponse> getRobotUserListAPI(exchangetype) async {
  try{
    // https://royalq.bittgc.com/api/account/Getcurchnglist?exchangetype=2
    String url = '$BASEAPI/account/GetRobotUserList/${userInfo!.id}/$exchangeValue';
    // print(url);
    final response = await http.get(Uri.parse(url), headers: headers);
    List<UserCurrencyResponse> data = userCurrencyResponseFromJson(response.body);
    return ApiResponse(status: true, data: data, message: SUCCESS);
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

Future<ApiResponse> loginAPI(Map body) async {
  try{
    String url = '$BASEAPI/account/Login';
    print(url);
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
    Map data = jsonDecode(response.body);
    print(response);
    print(data);
    if(data['status'] == 'succeed'){
      LoginResponse data = loginResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

Future<ApiResponse> activationIDAPI() async {
  try{
    String url = '$BASEAPI/account/ActivationID';
    print(url);
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode({
          "userid": userInfo!.id
        }));
    Map data = jsonDecode(response.body);
    if(data['status'] == 'succeed'){
      RegisterResponse data = registerResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

Future<ApiResponse> getProfileAPI(int id) async {
  try{
    String url = '$BASEAPI/account/GetProfile/$id';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers);
    Map data = jsonDecode(response.body);
    if(data['status'] == 'succeed'){
      UserInfo data = userInfoFromJson(response.body);
      userInfo = data;
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

Future<ApiResponse> forgotPasswordAPI(Map body) async {
  try{
    String url = '$BASEAPI/account/forgot';
    print(url);
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
    Map data = jsonDecode(response.body);
    if(data['status'] == 'succeed'){
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: {'message': e.toString()}, message: e.toString());
  }
}

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

// Future<ApiResponse> withdrawalAPI(String url, String type) async {
//   try{
//     // final String url = '$BASE_WITHDRAWAL?ContractAddressT=$CONT_ADDRESS&ToAddressT=$ToAddressT&AmountT=$AmountT&PKEYT=$PKEYT';
//     print(url);
//     final response = await http.get(Uri.parse(url), headers: headers);
//     String body = response.body;
//     if(body.contains('0x')){
//       return ApiResponse(status: true, data: body, message: SUCCESS);
//     }
//
//     Map data = jsonDecode(response.body);
//     if(data['status'] != ERROR){
//       // QuesAnsModel model = quesAnsModelFromJson(response.body);
//       return ApiResponse(status: true, data: data, message: SUCCESS);
//     }else{
//       return ApiResponse(status: false, data: data, message: ERROR);
//     }
//   }catch(e){
//     return ApiResponse(status: false, data: null, message: e.toString());
//   }
// }

// Future<ApiResponse> liveTDCAPI() async {
//   try{
//     final String url = '$URL_LIVE_TDC';
//     print(url);
//     final response = await http.get(Uri.parse(url), headers: headers);
//     Map data = jsonDecode(response.body);
//     if(data['status'] != ERROR){
//       TdcCurrencyModel model = tdcCurrencyModelFromJson(response.body);
//       return ApiResponse(status: true, data: model, message: SUCCESS);
//     }else{
//       return ApiResponse(status: false, data: data, message: ERROR);
//     }
//   }catch(e){
//     return ApiResponse(status: false, data: null, message: e.toString());
//   }
// }

Future<ApiResponse> getTradeAPI({defUser = false, symbol='INCHUSDT'}) async {
  try{
    // http://royalq.bittgc.com/api/account/GetTrade/25/INCHUSDT/1
    String url = '$BASEAPI/account/GetTrade/${defUser?0:userInfo!.id}/$symbol/$exchangeValue';

    // String url = '$BASEAPI/account/GetTrade/${defUser?0:25}/$symbol/$exchangeType';
    print("Right: "+url);
    final response = await http.get(Uri.parse(url), headers: headers);
    Map data = jsonDecode(response.body);
    print(data);
    if(data['status'] == 'succeed'){
      print(response.body);
      TradeResponse data = tradeResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    print(e.toString());
    return ApiResponse(status: false, data: {'message': e.toString()}, message: e.toString());
  }
}

Future<ApiResponse> chkBalanceAPI() async {
  try{
    String url = '$BASEAPI/Balance/chkbalance?coin=USDT&id=${userInfo!.id}&exchnagetype=$exchangeValue';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers);
    if(response.statusCode==200){
      return ApiResponse(status: true, data: response.body.replaceAll('\"', ''), message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: '0.0', message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: '0.0', message: e.toString());
  }
}

Future<ApiResponse> getRewardBalanceAPI({exchnagetype=1}) async {
  try{

    // https://royalq.bittgc.com/api/account/GetRewardBalance/ID
    String url = '$BASEAPI/account/GetRewardBalance/${userInfo!.id}';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers);
    if(response.statusCode==200){
      Map data = jsonDecode(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: {"totalAmount":0.0}, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: {"totalAmount":0.0}, message: e.toString());
  }
}

Future<ApiResponse> updateTradeSettingAPI(Map body) async {
  try{
    String url = '$BASEAPI/account/updateTradeSetting';
    print("Save Margin URL "+url);
    print(body);
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
    Map data = jsonDecode(response.body);
    print(response.body);
    if(data['status'] == 'succeed'){
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: {'message': '${data['message']}'}, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: {'message': e.toString()}, message: e.toString());
  }
}

Future<ApiResponse> updateMarginLimitAPI(Map body) async {
  try{
    // http://royalq.bittgc.com/api/account/updateMarginLimit
    String url = '$BASEAPI/account/updateMarginLimit';
    print(url);
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
    Map data = jsonDecode(response.body);
    if(data['status'] == 'succeed'){
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: {'message': e.toString()}, message: e.toString());
  }
}

Future<ApiResponse> rewardListAPI() async {
  try{
    // http://royalq.bittgc.com/api/account/RewardList/25
    String url = '$BASEAPI/account/RewardList/${userInfo!.id}';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers,);
    if(response.statusCode == 200){
      List<RewardResponse> data = rewardResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: <RewardResponse>[], message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: {'message': e.toString()}, message: e.toString());
  }
}

// Future<ApiResponse> revnueListAPI() async {
//   try{
//     // http://royalq.bittgc.com/api/account/RewardList/25
//     String url = '$BASEAPI/account/RewardList/${userInfo!.id}';
//     print(url);
//     final response = await http.get(Uri.parse(url), headers: headers,);
//     if(response.statusCode == 200){
//       List<RevenueResponse> data = revenueResponseFromJson(response.body);
//       return ApiResponse(status: true, data: data, message: SUCCESS);
//     }else{
//       return ApiResponse(status: false, data: <RevenueResponse>[], message: ERROR);
//     }
//   }catch(e){
//     return ApiResponse(status: false, data: <RevenueResponse>[], message: e.toString());
//   }
// }


Future<ApiResponse> directTeamAPI() async {
  try{
    // http://royalq.bittgc.com/api/account/RewardList/25
    String url = '$BASEAPI/account/DirectTeam/${userInfo!.id}';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers,);
    if(response.statusCode == 200){
      List<DirectTeamResponse> data = directTeamResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: <DirectTeamResponse>[], message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: {'message': e.toString()}, message: e.toString());
  }
}

Future<ApiResponse> levelTeamAPI() async {
  try{
    // http://royalq.bittgc.com/api/account/RewardList/25
    String url = '$BASEAPI/account/LevelTeam/${userInfo!.id}';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers,);
    if(response.statusCode == 200){
      List<LevelTeamResponse> data = levelTeamResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: <LevelTeamResponse>[], message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: {'message': e.toString()}, message: e.toString());
  }
}

Future<ApiResponse> rankTeamAPI() async {
  try{
    // http://royalq.bittgc.com/api/account/RewardList/25
    String url = '$BASEAPI/account/RankTeam/${userInfo!.id}';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers,);
    if(response.statusCode == 200){
      List<RankTeamResponse> data = rankTeamResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: <RankTeamResponse>[], message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: {'message': e.toString()}, message: e.toString());
  }
}

Future<ApiResponse> getLevelWiseAPI(level) async {
  try{
    // http://royalq.bittgc.com/api/account/RewardList/25
    String url = '$BASEAPI/account/GetLevelWise/${userInfo!.id}/$level';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers,);
    if(response.statusCode == 200){
      List<LevelWiseResponse> data = levelWiseResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: <RankTeamResponse>[], message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: {'message': e.toString()}, message: e.toString());
  }
}

Future<ApiResponse> getOrderTransactionAPI({symbol}) async {
  try{
    String url = '$BASEAPI/account/GetOrderTransaction?id=${userInfo!.id}&exchnagetype=$exchangeValue'
        '${symbol !=null?'&symbol=$symbol':''}';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers,);
    if(response.statusCode == 200){
      print(response.body);
      // List<DirectTeamResponse> data = directTeamResponseFromJson(response.body);
      List<OrderTransactionResponse>data =  orderTransactionResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: <DirectTeamResponse>[], message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: <DirectTeamResponse>[], message: e.toString());
  }
}

Future<ApiResponse> getAllOrderTransactionAPI(String pageCount) async {
  try{
    String url = 'http://rpc.darshantrade.com/api/account/AllTrades/$pageCount';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers,);
    if(response.statusCode == 200){
      print("Transation  API Resposne  "+response.body);
      // List<DirectTeamResponse> data = directTeamResponseFromJson(response.body);
      List<AffiliateAllOrderTransacionResponse>data = affiliateAllOrderTransacionResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: <DirectTeamResponse>[], message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}


//Robot Transation
Future<ApiResponse> getRobotTransactionAPI() async {
  try{
    String url = '$BASEAPI/account/GetOrderTransactionRobot?id=${userInfo!.id}&exchangetype=$exchangeValue';

    final response = await http.get(Uri.parse(url), headers: headers,);
    if(response.statusCode == 200){
      print(response.body);
      // List<DirectTeamResponse> data = directTeamResponseFromJson(response.body);
      List<RobotTransationModel>data =  robotTransationModelFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: <DirectTeamResponse>[], message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: <DirectTeamResponse>[], message: e.toString());
  }
}


Future<ApiResponse> getwithdrwallistAPI() async {
  try{
    // String url = 'http://api.xpertgain.io/api/account/getwithdrwallist/101';
    String url = '$BASEAPI/account/getwithdrwallist/${userInfo!.id}';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers,);
    if(response.statusCode == 200){
      List<WithdrawalHistoryResponse> data = withdrawalHistoryResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: <WithdrawalHistoryResponse>[], message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: <WithdrawalHistoryResponse>[], message: e.toString());
  }
}

Future<ApiResponse> fundTransferAPI(Map body) async {
  try{
    // // http://royalq.bittgc.com/api/account/FundTransfer

    String url = '$BASEAPI/account/FundTransfer';
    print(url);
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
    Map data = jsonDecode(response.body);
    if(data['status'] == 'succeed'){
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: {'message': e.toString()}, message: e.toString());
  }
}

Future<ApiResponse> getCurrencyAPI({method='Getcurrency'}) async {
  try{
    // https://bittgc.io/api/account/Getcurrency
    String url = '$BASEAPI/account/Getcurrency';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers,);
    if(response.statusCode == 200){
      List<GetcurrencyResponse> data = getcurrencyResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: <GetcurrencyResponse>[], message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: <GetcurrencyResponse>[], message: e.toString());
  }
}

Future<ApiResponse> getCurrencyByIdAPI(id) async {
  try{

    String url = '$BASEAPI/account/Getcurrencybyid/$id';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers,);
    if(response.statusCode == 200){
      GetcurrencyResponse data = GetcurrencyResponse.fromJson( jsonDecode(response.body));
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: <GetcurrencyResponse>[], message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: <GetcurrencyResponse>[], message: e.toString());
  }
}



Future<ApiResponse> withdrawalAPI(Map body) async {
  try{
// http://royalq.bittgc.com/api/account/Withdrawal

    String url = '$BASEAPI/account/Withdrawal';
    print(url);
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
    Map data = jsonDecode(response.body);
    if(data['status'] == 'succeed'){
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: {'message': e.toString()}, message: e.toString());
  }
}

Future<ApiResponse> updateClearModeAPI(Map body) async {
  try{
    String url = '$BASEAPI/account/UpdateClearMode';
    print(url);
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
    Map data = jsonDecode(response.body);
    if(data['status'] == 'succeed'){
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: {'message': e.toString()}, message: e.toString());
  }
}


Future<ApiResponse> updateClearAtmModeAPI(Map body) async {
  try{
    String url = '$BASEAPI/account/UpdateClearAtmMode';
    print(url);
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
    Map data = jsonDecode(response.body);
    if(data['status'] == 'succeed'){
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: {'message': e.toString()}, message: e.toString());
  }
}

Future<ApiResponse> commonPostAPI(path, Map body) async {
  try{
    String url = '$BASEAPI$path';
    print(url);
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
    Map data = jsonDecode(response.body);
    if(data['status'] == 'succeed'){
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: {'message': e.toString()}, message: e.toString());
  }
}


Future<ApiResponse> getTradeModeStatusAPI(Map body) async {
  try{
    String url = '$BASEAPI/account/GetTradeModeStatus';
    print(url);
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
    Map data = jsonDecode(response.body);
    print("Trade Status"+data.toString());
    if(data['status'] == 'succeed'){
      TradeStatusResponse data = tradeStatusResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: {'message': e.toString()}, message: e.toString());
  }
}

Future<ApiResponse> revnueListAPI() async {
  try{
    String url = '$BASEAPI/account/RevnueList/${userInfo!.id}';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers,);
    if(response.statusCode == 200){
      List<RevenueResponse> data = revenueResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: <GetcurrencyResponse>[], message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: <GetcurrencyResponse>[], message: e.toString());
  }
}

Future<ApiResponse> getCurOneChngListAPI(symbol, exchangeType) async {
  try{
    // https://royalq.bittgc.com/api/account/GetCurOneChngList/33/XECUSDT/1
    String url = '$BASEAPI/account/GetCurOneChngList/${userInfo!.id}/$symbol/$exchangeValue';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers,);
    if(response.statusCode == 200){
      CurOneChngListResponse data = curOneChngListResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: {'message': ''}, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: {'message': e.toString()}, message: e.toString());
  }
}

Future<ApiResponse> newsAPI() async {
  try{
    String url = '$BASEAPI/account/News/?id=${userInfo!.id}';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers,);
    if(response.statusCode == 200){
      List<NewsResponse> data = newsResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: <NewsResponse>[], message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: <NewsResponse>[], message: e.toString());
  }
}


Future<ApiResponse> tradingProfitAPI() async {
  try{
    String url = '$BASEAPI/account/TradingProfit';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers,);
    if(response.statusCode == 200){
      List<TradingProfitResponse> data = tradingProfitResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: <TradingProfitResponse>[], message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: <TradingProfitResponse>[], message: e.toString());
  }
}

Future<ApiResponse> getBuyModeAPI(symbol, exchnagetype, amount) async {
  try{
    // http://royalq.bittgc.io/api/account/GetBuyMode?id=1&symbol=XECUSDT&exchnagetype=1
    String url = '$BASEAPI/account/GetBuyMode?id=${userInfo!.id}&symbol=$symbol&exchnagetype=$exchnagetype&amount=$amount';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers,);
    if(response.statusCode == 200){
      GetBuyModeResponse data = getBuyModeResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: {"message": ''}, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: {"message": e.toString()}, message: e.toString());
  }
}

Future<ApiResponse> getSellModeAPI(symbol, exchnagetype) async {
  try{
    // http://royalq.bittgc.io/api/account/GetSellMode?id=1&symbol=XECUSDT&exchnagetype=1
    String url = '$BASEAPI/account/GetSellMode?id=${userInfo!.id}&symbol=$symbol&exchnagetype=$exchnagetype';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers,);
    if(response.statusCode == 200){
      GetSellModeResponse data = getSellModeResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: {"message": ''}, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: {"message": e.toString()}, message: e.toString());
  }
}

// http://royalq.bittgc.com/api/account/UpdateSellMode
Future<ApiResponse> updateBuySellModeAPI(String mode, Map body) async {
  try{
    // UpdateSellMode
    // UpdateBuyMode
    String url = '$BASEAPI/account/$mode';
    print(url);
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
    Map data = jsonDecode(response.body);
    if(data['status'] == 'succeed'){
      // TradeStatusResponse data = tradeStatusResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: {'message': e.toString()}, message: e.toString());
  }
}

Future<ApiResponse> binanceQunatityPercentAPI(symbol, exchnagetype, percent) async {
  try{
    // http://royalq.bittgc.io/api/account/GetSellMode?id=1&symbol=XECUSDT&exchnagetype=1
    // String url = '$BASEAPI/account/GetSellMode?id=${userInfo!.id}&symbol=$symbol&exchnagetype=$exchnagetype';
    String url = '$BASEAPI/account/BinanceQunatityPercent?id=${userInfo!.id}&symbol=$symbol&exchnagetype=$exchnagetype&percent=$percent';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers,);
    if(response.statusCode == 200){
      String data = response.body;
      // GetSellModeResponse data = getSellModeResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: {"message": ''}, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: {"message": e.toString()}, message: e.toString());
  }
}

Future<ApiResponse> memberCenterAPI() async {
  try{
    // String url = '$BASEAPI/account/MemberCenter/1';//${userInfo!.id}';
    String url = '$BASEAPI/account/MemberCenter/${userInfo!.id}';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers,);
    Map data = jsonDecode(response.body);
    if(data['status'] == 'succeed'){
      var data = memberResponseFromJson(response.body);
      // MemberResponse data = memberResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: {"message": ''}, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: {"message": e.toString()}, message: e.toString());
  }
}

// https://royalq.bittgc.com/api/account/ChangeStrategyMode

Future<ApiResponse> changeStrategyModeAPI(Map body) async {
  try{

    String url = '$BASEAPI/account/ChangeStrategyMode';
    print(url);
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
    Map data = jsonDecode(response.body);
    if(data['status'] == 'succeed'){
      // TradeStatusResponse data = tradeStatusResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: {'message': e.toString()}, message: e.toString());
  }
}

Future<ApiResponse> changePasswordModeAPI(Map body) async {
  try{

    String url = '$BASEAPI/account/ChangePassword';
    print(url);
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
    Map data = jsonDecode(response.body);
    if(data['status'] == 'succeed'){
      // TradeStatusResponse data = tradeStatusResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: {'message': e.toString()}, message: e.toString());
  }
}


Future<ApiResponse> changeTransPasswordAPI(Map body) async {
  try{
    String url = '$BASEAPI/account/ChangeTransPassword';
    print(url);
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
    Map data = jsonDecode(response.body);
    if(data['status'] == 'succeed'){
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: {'message': e.toString()}, message: e.toString());
  }
}

Future<ApiResponse> incomeTypeAPI() async {
  try{
    String url = '$BASEAPI/account/IncomeType';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers,);
    // Map data = jsonDecode(response.body);
    if(response.statusCode == 200){
      List<TypeResponse> data = typeResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: [], message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: [], message: e.toString());
  }
}

Future<ApiResponse> revnueListDateAPI(date) async {
  try{
    String url = '$BASEAPI/account/RevnueListDate/${userInfo!.id}/$date';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers,);
    // Map data = jsonDecode(response.body);
    if(response.statusCode == 200){
      List<RevnueListDateResponse> data = revnueListDateResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: <RevnueListDateResponse>[], message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: <RevnueListDateResponse>[], message: e.toString());
  }
}

Future<ApiResponse> updatePositionModestatusAPI(Map body) async {
  try{
    String url = '$BASEAPI/account/UpdatePositionModestatus';
    print(url);
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
    Map data = jsonDecode(response.body);
    if(data['status'] == 'succeed'){
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: {'message': e.toString()}, message: e.toString());
  }
}

Future<ApiResponse> locationListAPI() async {
  try{
    String url = '$BASEAPI/account/LocationList';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers,);
    // Map data = jsonDecode(response.body);
    if(response.statusCode == 200){
      List<CountryModel> data = countryModelFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: <CountryModel>[], message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: <CountryModel>[], message: e.toString());
  }
}

Future<ApiResponse> getcommunityAPI() async {
  try{
    // String url = '$BASEAPI/account/Getcommunity/${userInfo!.id}';
    String url = '$BASEAPI/account/Getcommunity/${userInfo!.id}';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers,);
    Map data = jsonDecode(response.body);
    if(response.statusCode == 200){
      CommunityResponse data = communityResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

Future<ApiResponse> communityListAPI() async {
  try{
    // String url = '$BASEAPI/account/Getcommunity/${userInfo!.id}';
    String url = '$BASEAPI/account/CommunityList/${userInfo!.id}';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers,);
    // Map data = jsonDecode(response.body);
    if(response.statusCode == 200){
      List<CommunityListResponse> data = communityListResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: <CommunityListResponse>[], message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: <CommunityListResponse>[], message: e.toString());
  }
}

Future<ApiResponse> billingAPI() async {
  try{
    // String url = '$BASEAPI/account/Getcommunity/${userInfo!.id}';
    String url = '$BASEAPI/account/Billing/${userInfo!.id}';
    print("Billing "+url);
    final response = await http.get(Uri.parse(url), headers: headers,);
    // Map data = jsonDecode(response.body);
    if(response.statusCode == 200){
      List<BillingResponse> data = billingResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: <BillingResponse>[], message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: <BillingResponse>[], message: e.toString());
  }
}


Future<ApiResponse> totalBillingANdLossAPI(String billType) async {
  try{
    // String url = '$BASEAPI/account/Getcommunity/${userInfo!.id}';
    String url = '$BASEAPI/account/$billType/${userInfo!.id}';
    print("Billing "+url);
    final response = await http.get(Uri.parse(url), headers: headers,);
    // Map data = jsonDecode(response.body);
    if(response.statusCode == 200){
      List<TotalBillingResponse> data = totalBillingResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: <BillingResponse>[], message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: <BillingResponse>[], message: e.toString());
  }
}



Future<ApiResponse> totalFeesAPI() async {
  try{
    // String url = '$BASEAPI/account/Getcommunity/${userInfo!.id}';
    String url = '$BASEAPI/account/Allcommission/${userInfo!.id}';
    print("Billing "+url);
    final response = await http.get(Uri.parse(url), headers: headers,);
    // Map data = jsonDecode(response.body);
    if(response.statusCode == 200){
      List<AllFeesResponse> data = allFeesResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: <BillingResponse>[], message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: <BillingResponse>[], message: e.toString());
  }
}



Future<ApiResponse> totalFeesAssetAPI() async {
  try{
    // String url = '$BASEAPI/account/Getcommunity/${userInfo!.id}';
    String url = '$BASEAPI/account/Allcommissionassest/${userInfo!.id}';
    print("Billing "+url);
    final response = await http.get(Uri.parse(url), headers: headers,);
    // Map data = jsonDecode(response.body);
    if(response.statusCode == 200){
      List<TotoalFeesResponse> data = totoalFeesResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: <BillingResponse>[], message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: <BillingResponse>[], message: e.toString());
  }
}



Future<ApiResponse>coinByBillingAPI(String symbol)async{
  
  try{

    String url="$BASEAPI/account/RevnueListSymbol/${userInfo!.id}/$symbol";
    print(url);
    final response=await http.get(Uri.parse(url),headers: headers);
    print(response.body);
    if(response.statusCode==200){
      List<BillingDetailsByCoinModel>data=billingDetailsByCoinModelFromJson(response.body);
      return ApiResponse(status: true,message: SUCCESS,data: data);
    }else{
      return ApiResponse(status: false,data: <BillingDetailsByCoinModel>[],message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false,message: e.toString(),data: <BillingDetailsByCoinModel>[]);
  }
  
}

Future<ApiResponse> totalprofitAPI(method) async {
  try{
    // String url = '$BASEAPI/account/Totalprofit/${userInfo!.id}';
    String url = '$BASEAPI/account/$method/${userInfo!.id}';

    final response = await http.get(Uri.parse(url), headers: headers,);
    // Map data = jsonDecode(response.body);
    print("Profile Header : "+response.body);
    if(response.statusCode == 200){
      List<TotalprofitResponse> data = totalprofitResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: <TotalprofitResponse>[], message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: <TotalprofitResponse>[], message: e.toString());
  }
}



Future<ApiResponse> feesAPI(method) async {
  try{
    // String url = '$BASEAPI/account/Totalprofit/${userInfo!.id}';
    String url = '$BASEAPI/account/$method/${userInfo!.id}';

    final response = await http.get(Uri.parse(url), headers: headers,);
    // Map data = jsonDecode(response.body);
    print("Profile Header : "+response.body);
    if(response.statusCode == 200){
      List<FeeResponse> data = feeResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: <TotalprofitResponse>[], message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: <TotalprofitResponse>[], message: e.toString());
  }
}


Future<ApiResponse> rankwiselistAPI(rank) async {
  try{
    // String url = '$BASEAPI/account/Totalprofit/${userInfo!.id}';
    String url = '$BASEAPI/account/rankwiselist/${userInfo!.id}/$rank';
    // String url = '$BASEAPI/account/rankwiselist/1/$rank';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers,);
    // Map data = jsonDecode(response.body);
    if(response.statusCode == 200){
      List<RankWiseResponse> data = rankWiseResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: <RankWiseResponse>[], message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: <RankWiseResponse>[], message: e.toString());
  }
}
///{"banner":"bsnner1","url":"http://jakram.jackbot.cloud/banner/658118.png"},{"banner":"b2","url":"http://jakram.jackbot.cloud/banner/418285.jpeg"}
Future<ApiResponse> bannerAPI() async {
  try{
    // String url = 'https://royalq.bittgc.com/api/account/Banner';
    String url = '$BASEAPI/account/Banner';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers,);
    print(response.body);
    if(response.statusCode == 200){
      List<BannerResponse> data = bannerResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: <BannerResponse>[], message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: <BannerResponse>[], message: e.toString());
  }
}

Future<ApiResponse> getBoosterUserListAPI() async {
  try{

    String url = '$BASEAPI/account/GetBoosterUserList/${userInfo!.id}/$exchangeValue';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers,);

    if(response.statusCode == 200){
      List<BoosterUserListResponse> data = boosterUserListResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: <BoosterUserListResponse>[], message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: <BoosterUserListResponse>[], message: e.toString());
  }
}

Future<ApiResponse> boosterModeSettingAPI(Map body, method) async {
  try{
    String url = '$BASEAPI/account/$method';
    print(url);
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
    Map data = jsonDecode(response.body);
    if(data['status'] == 'succeed'){
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: {'message': e.toString()}, message: e.toString());
  }
}

Future<ApiResponse> getUserBoosterCurAPI(symbol) async {
  try{
    String url = '$BASEAPI/account/GetUserBoosterCur/${userInfo!.id}/$symbol/1';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers,);
    if(response.statusCode == 200){
      BoosterCurResponse data = boosterCurResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: null, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

Future<ApiResponse>affiliateRegisterAPI(Map mao)async{

  try{
    print(mao);
    String url=affiliateBaseUrl+affiliateSignUpAPIName;
    final response=await http.post(Uri.parse(url),headers: postHeaders,body: json.encode(mao));
    print(response.body);
    data=jsonDecode(response.body);
     if(response.statusCode==201){
      return ApiResponse(status: true,data: data,message:data[messgae]);
    }else{
      return ApiResponse(status: false,data: null,message: data[messgae]);
    }
  }catch(ex){
    return ApiResponse(status: false,data: null,message: ex.toString());
  }
}



Future<ApiResponse>affiliateLoginAPI(Map mao)async{

  try{
    String url=affiliateBaseUrl+affilateLoginAPIName;
    final response=await http.post(Uri.parse(url),headers: postHeaders,body: json.encode(mao));
    data=jsonDecode(response.body);
    print(data);
    if(data[status]==true){
      AffiliateLoginResponse model=affiliateLoginResponseFromJson(response.body);
      return ApiResponse(status: true,data: model,message:data[messgae]);
    }else{
      return ApiResponse(status: false,data: null,message: data[messgae]);
    }
  }catch(ex){
    return ApiResponse(status: false,data: null,message: ex.toString());
  }
}


Future<ApiResponse>stakeListAPI(String? token)async{

  try{

    var zJWTToken={"Authorization":"Bearer $token"};
    String url=stakeListUrl;
    final response=await http.get(Uri.parse(url),headers: zJWTToken);
    data=jsonDecode(response.body);
    if(data[status]==true){
      StakeResponse model=stakeResponseFromJson(response.body);
      return ApiResponse(status: true,data: model,message:data[messgae]);
    }else{
      return ApiResponse(status: false,data: null,message: data[messgae]);
    }
  }catch(ex){
    print(ex);
    return ApiResponse(status: false,data: null,message: ex.toString());
  }
}



//Future Trade APIS


Future<ApiResponse> bindFSpotTradeAPI(Map hashMap, String termValue) async {
  try{
    String url ='$fBASEAPI/StartFutureTrade';
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(hashMap));
    Map data = jsonDecode(response.body);
    print("Trading Response "+data.toString());
    if(data['status'] == 'succeed'){
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){

    print(e.toString());
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}


Future<ApiResponse> getFUSDTCurChngListAPI(exchangetype) async {
  try{
    // https://royalq.bittgc.com/api/account/Getcurchnglist?exchangetype=2
    String url = '$fBASEAPI/GetCurChngList?exchangetype=$exchangeValue';
    // print(url);
    final response = await http.get(Uri.parse(url), headers: headers);
    List<FUSDTCurrencyModel> data = fCurrencyModelFromJson(response.body);
    return ApiResponse(status: true, data: data, message: SUCCESS);
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}
/*
Future<ApiResponse> getFBUSDCurChngListAPI(exchangetype) async {
  try{
    // https://royalq.bittgc.com/api/account/Getcurchnglist?exchangetype=2
    String url = '$fBASEAPI/GetCurChngList?exchangetype=$exchangeValue';
    // print(url);
    final response = await http.get(Uri.parse(url), headers: headers);
    List<FBUSDCurrencyModel> data = fBUSDCurrencyModelFromJson(response.body);
    return ApiResponse(status: true, data: data, message: SUCCESS);
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}*/


Future<ApiResponse> getPairCurrentDetailAPI(symbol) async {
  try{
    // https://royalq.bittgc.com/api/account/Getcurchnglist?exchangetype=2
    String url = '$fBASEAPI/GetCurOneChngList/$exchangeValue/$symbol/$exchangeValue';
    print("Pair Currency "+url);
    final response = await http.get(Uri.parse(url), headers: headers);
    print(response.body);
    Map data = json.decode(response.body);
    return ApiResponse(status: true, data: data, message: SUCCESS);
  }catch(e){
    print(e.toString());
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}



Future<ApiResponse> getFTradeAPI({defUser = false, symbol}) async {
  try{
    // http://royalq.bittgc.com/api/account/GetTrade/25/INCHUSDT/1
    String url = '$fBASEAPI/GetTrade/${defUser?0:userInfo!.id}/$symbol/$exchangeValue';

    // String url = '$BASEAPI/account/GetTrade/${defUser?0:25}/$symbol/$exchangeType';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers);
    Map data = jsonDecode(response.body);
    print("FTrade "+data.toString());
    if(data['status'] == 'succeed'){
      TradeResponse data = tradeResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    print("Get Trade Error"+e.toString());
    return ApiResponse(status: false, data: {'message': e.toString()}, message: e.toString());
  }
}


Future<ApiResponse> updateFTradeSettingAPI(Map body) async {
  try{
    String url = '$fBASEAPI/updateTradeSetting';
    print("Save Margin URL "+url);
    print(body);
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
    Map data = jsonDecode(response.body);
    print(response.body);
    if(data['status'] == 'succeed'){
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: {'message': '${data['message']}'}, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: {'message': e.toString()}, message: e.toString());
  }
}

Future<ApiResponse> chkUSDTBalanceAPI() async {
  try{
    String url = '$fBASE/api/balance/chkbalance?id=${userInfo?.id}&coin=USDT';
    print("Balance "+url);
    final response = await http.get(Uri.parse(url), headers: headers);
    if(response.statusCode==200){
      return ApiResponse(status: true, data: response.body.replaceAll('\"', ''), message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: '0.0', message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: '0.0', message: e.toString());
  }
}


Future<ApiResponse> getFOrderTransactionAPI({symbol}) async {
  try{
    String url = '$fBASEAPI/GetOrderTransaction?id=${userInfo!.id}${
        symbol !=null?'&symbol=$symbol':''}&exchnagetype=$exchangeValue';
    final response = await http.get(Uri.parse(url), headers: headers,);
    print(response.body);
    if(response.statusCode == 200){
      List<FOrderTransationResposne>data =  fOrderTransationResposneFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: <DirectTeamResponse>[], message: ERROR);
    }
  }catch(e){
    print("Some thing Is Error"+e.toString());
    return ApiResponse(status: false, data: <DirectTeamResponse>[], message: e.toString());
  }
}


Future<ApiResponse> saveMarginModeAPI(Map body) async {
    try{
      String url = '$fBASEAPI/UpdateMarginType';
      final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
      Map data = jsonDecode(response.body);
      print(response.body);
      if(data['status'] == 'succeed'){
        return ApiResponse(status: true, data: data, message: SUCCESS);
      }else{
        return ApiResponse(status: false, data: {'message': '${data['message']}'}, message: ERROR);
      }
    }catch(e){
      return ApiResponse(status: false, data: {'message': e.toString()}, message: e.toString());
    }

}


Future<ApiResponse> saveLeveargeAPI(Map body) async {
  try{
    String url = '$fBASEAPI/UpdateFutureLevearge';
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
    Map data = jsonDecode(response.body);
    print(response.body);
    if(data['status'] == 'succeed'){
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: {'message': '${data['message']}'}, message: ERROR);
    }
  }catch(e){
    print(e.toString());
    return ApiResponse(status: false, data: {'message': e.toString()}, message: e.toString());
  }
}

Future<ApiResponse> checkIPPermissionAPI() async {
  try{
    String url = '$fBASEAPI/GetHedgeMode/${userInfo!.id}';
    final response = await http.get(Uri.parse(url), headers: headers);
    print(response.body);
    if(response.statusCode==200){
      if(response.body!="0"){
        return ApiResponse(status: true, data: response.body.replaceAll('\"', ''), message: SUCCESS);
      }else{
        return ApiResponse(status: false, data: null, message: ERROR);
      }
    }else{
      return ApiResponse(status: false, data: null, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

Future<ApiResponse> grantedIPPermissionAPI(Map body) async {
  try{
    String url = '$fBASEAPI/UpdatePositionMode';
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
    Map data = jsonDecode(response.body);
    print(response.body);
    if(data['status'] == 'succeed'){
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: {'message': '${data['message']}'}, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: {'message': e.toString()}, message: e.toString());
  }
}



Future<ApiResponse> getFutureCurUserChngListAPI(exchangetype) async {
  try{
    // https://royalq.bittgc.com/api/account/Getcurchnglist?exchangetype=2
    String url = '$fBASEAPI/GetCurUserChngList/${userInfo!.id}/$exchangeValue';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers);
    if(response.statusCode==200){
      print("Hit");
      List<UserFutureCurrencyResponse> data = userFutureCurrencyResponseFromJson(response.body);
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      print("Not Hit ");
      return ApiResponse(status: false,message: "Running trade Api not working.");
    }
  }catch(e){
    print("Error"+e.toString());
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}
// http://jackbot.bittgc.com/api/account/GetUserBoosterCur/{id}/{pair}/{exchangetype


// http://jackbot.bittgc.com/api/account/BoosterModeSetting
// http://jackbot.bittgc.com/api/account/GetBoosterUserList/{id}/{exchnagetype}

void clearAPICache(){
  _apiCache.clear();
}

class ApiResponse{
  bool status;
  String? message;
  dynamic data;
  ApiResponse({required this.status,  this.message='', this.data});

}
