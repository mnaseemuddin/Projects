import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:trading_apps/api/user_data.dart';
import 'package:trading_apps/custom_ui/progress_dialog.dart';
import 'package:trading_apps/models/HotDealsModel.dart';
import 'package:trading_apps/models/SliderImageModel.dart';
import 'package:trading_apps/models/TopCategoryModel.dart';
import 'package:trading_apps/models/TrendingModel.dart';
import 'package:trading_apps/models/country_model.dart';
import 'package:trading_apps/models/graph_model.dart';
import 'package:trading_apps/models/hashkeymodel.dart';
import 'package:trading_apps/models/history_model.dart';
import 'package:trading_apps/models/otherwalletmodel.dart';
import 'package:trading_apps/models/ques_ans_model.dart';
import 'package:trading_apps/models/support_model.dart';
import 'package:trading_apps/models/tdc_currency_model.dart';
import 'package:trading_apps/models/tdcwalletmodel.dart';
import 'package:trading_apps/models/trans_check_model.dart';
import 'package:trading_apps/models/transaction_model.dart';
import 'package:trading_apps/models/user_guide_model.dart';
import 'package:trading_apps/models/user_model.dart';
import 'package:trading_apps/models/wallet_model.dart';

const String SUCCESS = 'success';
const String ERROR = 'failure';
const String MESSAGE='message';

//Previews API
// http://3.108.210.239:5000/api/users
// http://3.108.210.239:5000/api/add_userbet/
// http://3.108.210.239:5000/api/user_bet_result/
// http://3.108.210.239:5000/api/getBethistoryBy_userid/user_id=7
// http://tradingclub.fund/api
// const String BASE = 'http://3.108.210.239:5000/';
//https://www.api.tradingclub.fund/api
const String BASE = 'https://www.api.tradingclub.fund/';
const String URL_USERS = BASE + 'api/users';
const String URL_LOGIN = BASE+'api/login';
const String URL_USER_DETAILS = BASE+'api/users_details';
const String URL_USER_IMG_UPLOAD = BASE+'api/users_image';
const String URL_ADD_BET = BASE+'api/add_userbet';
const String URL_BET_RESULT = BASE+'api/user_bet_result';
const String URL_BET_HISTORY= BASE+'api/getBethistoryBy_userid';
const String URL_USER_WALLET= BASE+'api/user_wallet_details';
const String URL_UPDATE_PASSWORD= BASE+'api/update_password';
const String URL_UPDATE_PROFILE = BASE+'api/users_update_profile';
const String URL_SAVE_WITHDRAWAL_ADDRESS=BASE+"api/Upadte_Payement_Address";
const String URL_EMAIL_VERIFICATION = BASE+'api/email_verification';
const String URL_EMAIL_UPDATE_STATUS = BASE+'api/update_email_status';
const String URL_MOBILE_UPDATE_STATUS = BASE+'api/update_mobile_verification_status';
const String URL_FORGOT_PASSWORD = BASE+'api/forgot_password';
const String URL_TRANSACTIONS = BASE+'api/transection_history';
const String URL_SUPPORT_INFO = BASE+'api/support_information';
const String URL_USER_GUIDE = BASE+'api/user_guide';
const String URL_QUESTION_ANSWERS = BASE+'api/question_answer';
const String URL_CHECK_TRANSACTION = BASE+'api/check_amount_via_address_id';
const String URL_WITHDRAW_AMOUNT = BASE+'api/withdraw_amount';
const String LeftMenuCategoryURL=BASE+"api/categories_list";
const String SliderUrl=BASE+"api/homeslider";
const String HomeCategoryUrls=BASE+"api/homecategories_list";
const String AllCategoryUrls=BASE+"api/categories_list";
const String AllItemUrls=BASE+"api/products/";
const String ProductDescriptionUrls=BASE+"api/products_details/";
const String AddToCartUrls=BASE+"api/add_to_cart";
const String CartListUrls=BASE+"api/cart_list/";
const String UpdateCartUrls=BASE+"api/update_cart_item";
const String NoOfItemsInCartsUrls=BASE+"api/get_Cart_quantity/";
const String DeleteCartItemAPI=BASE+"api/delet_cart/";
const String SaveAddressUrls=BASE+"api/add_user_address";
const String GetAddressListAPI=BASE+"api/address_list/";
const String DeleteAddressAPI=BASE+"api/delete_address/";
const String EditAddressAPI=BASE+"api/edit_user_address";
const String CheckOutAPI=BASE+"api/checkout";
const String HotDealsAndTradingUrls=BASE+"api/hometrending_hotdeal_api/";
const String SeeAllHotDealsItemsUrl=BASE+"api/hotdeal_api/";
const String SeeAllTrendingItemUrl=BASE+"api/trending_api/";
const String OrderHistoryAPI=BASE+"api/user_orders_list/";
const String OrderDetailsUrl=BASE+"api/order_details_api/";
const String URL_LIVE_TDC = 'http://new.stakingcrypto.global/api/LC/TdcLiveRate';
const String hotDealsUrl=BASE+"api/hotdeal_api";
const String trendingUrl=BASE+"api/trending_api";
const String addAmountInWalletUrl=BASE+"api/add_amount";
//http://tradingclub.fund/api/tdc_wallet_transection_history/61/0
const String otherWalletTransactionUrl=BASE+"api/normal_wallet_transection_history";
const String addAmountInWalletByTDCUrl=BASE+"api/add_amount_TDC";
const String tdcWalletTransactionUrl=BASE+"api/tdc_wallet_transection_history";
//
const String BASE_WITHDRAWAL = 'http://mainbusd.liveshop.co.in/';
const String CONT_ADDRESS = '0x6f13598e40d15228c7132b904957c80be6895cfe';
const String TO_ADDRESS = '0xf99b296D378b4572f5e1D9fCE269fe0f9817f69A';
const String AMOUNT = '1000000000';
const String PKEYT = '0x14bc2b0f8d64fc93cea0bf97c4eb5a7a8406622fda8f36fc24677691cf1c4438';
const String HashKeyCheckURL="https://api.bscscan.com/api?module=account&action=tokentx&contractaddress=0x6f13598e40d15228c7132b904957c80be6895cfe&address=0xF24e07fFfB63FCdE134cEa69A5b78674E82906b8&page=1&offset=5&startblock=0&endblock=999999999&sort=desc&apikey=BEC22IWQBPT9I43STQ2R3UEU1SC8SDFRYJ";

//http://mainbusd.liveshop.co.in/?ContractAddressT=0x6f13598e40d15228c7132b904957c80be6895cfe&ToAddressT=0xf99b296D378b4572f5e1D9fCE269fe0f9817f69A&AmountT=1000000000&PKEYT=0x14bc2b0f8d64fc93cea0bf97c4eb5a7a8406622fda8f36fc24677691cf1c4438

Map<String, String> headers = {
  'Content-Type': 'application/json',
  'Accept': 'application/json'
};

Future<ApiResponse> loginAPI(Map<String, String> body) async {
  try{
    final response = await http.post(Uri.parse(URL_LOGIN), headers: headers, body: json.encode(body));
    Map data = jsonDecode(response.body);
    if(data['status']!=ERROR){
      UserModel model = userModelFromJson(response.body);
      return ApiResponse(status: true, data: model, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

Future<ApiResponse> signUpAPI(Map body) async {
  print(URL_USERS);
  try{
    final response = await http.post(Uri.parse(URL_USERS), headers: headers, body: json.encode(body));
      Map data = jsonDecode(response.body);
      if(data['status']!=ERROR){
        return ApiResponse(status: true, data: data, message: SUCCESS);
      }else{
        return ApiResponse(status: false, data: data, message: ERROR);
      }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

Future<ApiResponse> userDetailsAPI(userId) async {
  try{
    final String url = '$URL_USER_DETAILS/$userId';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers);
    Map data = jsonDecode(response.body);
    debugPrint('OK   :'+data.toString());
    if(data['status']!=ERROR){
      UserModel model = userModelFromJson(response.body);
      userModel = model;
      return ApiResponse(status: true, data: model, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

Future<ApiResponse>hotDealsAPI(userId)async{
  try{
    final String url='$hotDealsUrl/$userId';
    final response=await http.get(Uri.parse(url),headers: headers);
    Map data = jsonDecode(response.body);
    if(data['status']!=ERROR){
      HotDealModel model=hotDealModelFromJson(response.body);
      return ApiResponse(status: true,data: model,message: data[MESSAGE]);
    }else{
      return ApiResponse(status: false,data: null,message: data[MESSAGE]);
    }
  }catch(e){
    return ApiResponse(status: false,data: null,message: e.toString());
  }
}

Future<ApiResponse>trendingAPI(userId)async{
  try{
    final String url='$trendingUrl/$userId';
    final response=await http.get(Uri.parse(url),headers: headers);
    Map data = jsonDecode(response.body);
    if(data['status']!=ERROR){
      TrendingDealModel model=trendingDealModelFromJson(response.body);
      return ApiResponse(status: true,data: model,message: data[MESSAGE]);
    }else{
      return ApiResponse(status: false,data: null,message: data[MESSAGE]);
    }
  }catch(e){
    return ApiResponse(status: false,data: null,message: e.toString());
  }
}


Future<ApiResponse> uploadImageAPI(Map body) async {
  try{
    final String url = URL_USER_IMG_UPLOAD;
    print(url);
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
    Map data = jsonDecode(response.body);
    if(data['status']!=ERROR){
      UserModel model = userModelFromJson(response.body);
      return ApiResponse(status: true, data: model, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
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
    print('Graph View : '+data.toString());
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

Future<ApiResponse> addBetAPI(Map body) async {
  print('$URL_ADD_BET => $body');

  try{
    final response = await http.post(Uri.parse(URL_ADD_BET), headers: headers, body: json.encode(body));
    Map data = jsonDecode(response.body);
    if(data['status']!=ERROR){
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

Future<ApiResponse> betResultAPI(Map body) async {
  print(URL_BET_RESULT);
  try{
    final response = await http.post(Uri.parse(URL_BET_RESULT), headers: headers, body: json.encode(body));
    Map data = jsonDecode(response.body);
    if(data['status']!=ERROR){
      return ApiResponse(status: true, data: data, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

Future<ApiResponse> betHistoryAPI(user_id) async {
  try{
    final String url = '$URL_BET_HISTORY/$user_id';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers);
    Map data = jsonDecode(response.body);
    if(data['status']!=ERROR){
      HistoryModel model = historyModelFromJson(response.body);
      return ApiResponse(status: true, data: model, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

Future<ApiResponse> userWalletAPI(user_id) async {
  try{
    final String url = '$URL_USER_WALLET/$user_id';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers);
    Map data = jsonDecode(response.body);
    print('Response  :'+data.toString());
    if(data['status']!=ERROR){
      WalletModel model = walletModelFromJson(response.body);
      return ApiResponse(status: true, data: model, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

Future<ApiResponse> generateNewAddressAPI(Map body) async {
  print(URL_BET_RESULT);
  try{
    String url = 'https://payment.liveshop.co.in/api/ApiBlockChain/GenerateNewAddressEx';
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
    Map data = jsonDecode(response.body);
    print(data.toString());
    if(data['Success'] == '1'){
      return ApiResponse(status: true, data: data, message: data['Message']);
    }else{
      return ApiResponse(status: false, data: data, message: data['Message']);
    }

    //3A5B1nRSHypF2J7TvGSWAPJsJeHguYEXKp
  }catch(e){
    print(e.toString());
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}


Future<ApiResponse> updatePasswordAPI(Map body) async {
  print(URL_UPDATE_PASSWORD);
  try{
    String url = URL_UPDATE_PASSWORD;
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
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

Future<ApiResponse> updateProfileAPI(Map body) async {
  print(URL_UPDATE_PASSWORD);
  try{
    String url = URL_UPDATE_PROFILE;
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
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



Future<ApiResponse> saveWithdrawAddressAPI(Map body) async {
  print(URL_UPDATE_PASSWORD);
  try{
    String url = URL_SAVE_WITHDRAWAL_ADDRESS;
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
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



Future<ApiResponse> emailVerificationAPI(Map body) async {
  print(URL_UPDATE_PASSWORD);
  try{
    String url = URL_EMAIL_VERIFICATION;
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
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

Future<ApiResponse> emailUpdateStatusAPI(Map body) async {
  print(URL_UPDATE_PASSWORD);
  try{
    String url = URL_EMAIL_UPDATE_STATUS;
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
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

Future<ApiResponse> mobileUpdateStatusAPI(Map body) async {
  print(URL_MOBILE_UPDATE_STATUS);
  try{
    String url = URL_MOBILE_UPDATE_STATUS;
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
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

Future<ApiResponse> forgotPasswordAPI(Map body) async {
  print(URL_FORGOT_PASSWORD);
  try{
    String url = URL_FORGOT_PASSWORD;
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
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

Future<ApiResponse> countryFlagAPI() async {
  try{
    final String url = 'http://playbox.show/api/country_code_flag_api.php';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers);
    Map data = jsonDecode(response.body);
    if(data['status'] == '1'){
      CountryModel model = countryModelFromJson(response.body);
      return ApiResponse(status: true, data: model, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

Future<ApiResponse> getTransactionsAPI(user_id) async {
  // user_id = 3;
  try{
    final String url = '$URL_TRANSACTIONS/$user_id';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers);
    Map data = jsonDecode(response.body);
    if(data['status'] != ERROR){
      TransactionModel model = transactionModelFromJson(response.body);
      return ApiResponse(status: true, data: model, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

Future<ApiResponse> supportInfoAPI() async {
  try{
    final String url = '$URL_SUPPORT_INFO';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers);
    Map data = jsonDecode(response.body);
    if(data['status'] != ERROR){
      SupportModel model = supportModelFromJson(response.body);
      return ApiResponse(status: true, data: model, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}
Future<ApiResponse> userGuideAPI() async {
  try{
    final String url = '$URL_USER_GUIDE';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers);
    Map data = jsonDecode(response.body);
    if(data['status'] != ERROR){
      UserGuideModel model = userGuideModelFromJson(response.body);
      return ApiResponse(status: true, data: model, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}
Future<ApiResponse> questionAnswersAPI(int id) async {
  try{
    final String url = '$URL_QUESTION_ANSWERS/$id';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers);
    Map data = jsonDecode(response.body);
    if(data['status'] != ERROR){
      QuesAnsModel model = quesAnsModelFromJson(response.body);
      return ApiResponse(status: true, data: model, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);
    }
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}
Future<ApiResponse> withdrawalAPI(String url, String type) async {
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
Future<ApiResponse> checkTransactionAPI(int user_id, String address) async {
  try{
    final String url = '$URL_CHECK_TRANSACTION/$user_id/$address';
    print(url);
    final response = await http.get(Uri.parse(url), headers: headers);
    Map data = jsonDecode(response.body);
    print('Response :'+data.toString());
    if(data['status'] != ERROR){
      TransactionCheckModel model = transactionCheckModelFromJson(response.body);
      return ApiResponse(status: true, data: model, message: SUCCESS);
    }else{
      return ApiResponse(status: false, data: data, message: ERROR);}
  }catch(e){
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}

//Add Amount API
Future<ApiResponse> addAmountInWalletAPI(Map body) async {
  try{

    final response = await http.post(Uri.parse(addAmountInWalletUrl), headers: headers, body: json.encode(body));
    Map data = jsonDecode(response.body);
    if(data['status'] != ERROR){
      return ApiResponse(status: true, data: data, message: data['message'].toString());
    }else{
      return ApiResponse(status: false, data: data, message: data['message'].toString());
    }
  }catch(e){
    print(e.toString());
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}
//END


Future<ApiResponse> addAmountInWalletByTDCAPI(Map body) async {
  try{

    final response = await http.post(Uri.parse(addAmountInWalletByTDCUrl), headers: headers, body: json.encode(body));
    Map data = jsonDecode(response.body);
    print(data['status']);
    if(data['status'] != 'fail'){
      return ApiResponse(status: true, data: data, message: data['message'].toString());
    }else{
      return ApiResponse(status: false, data: data, message: data['message'].toString());
    }
  }catch(e){
    print(e.toString());
    return ApiResponse(status: false, data: null, message: e.toString());
  }
}


Future<ApiResponse> withdrawAmountAPI(Map body) async {
  print(URL_MOBILE_UPDATE_STATUS);
  try{
    String url = URL_WITHDRAW_AMOUNT;
    final response = await http.post(Uri.parse(url), headers: headers, body: json.encode(body));
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

Future<ApiResponse>hashKeyAPI()async{
  try{
    final response=await http.get(Uri.parse(HashKeyCheckURL),headers: headers);
    Map data=json.decode(response.body);
    print(data.toString());
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


Future<ApiResponse>otherWalletAPI(String userId)async{
  try{
    String url='$otherWalletTransactionUrl/$userId/0';
    print(url);
    final response=await http.get(Uri.parse(url),headers: headers);
    Map data=json.decode(response.body);
    if(data['status']!=ERROR){
      print(data.toString());
      OtherWalletModel wallet=otherWalletModelFromJson(response.body);
      return ApiResponse(status: true,data: wallet,message: data[MESSAGE]);
    }else{
      return ApiResponse(status: true,data: null,message: data[MESSAGE]);
    }
  }catch(e){
    return ApiResponse(status: false,data: null,message:e.toString());
  }
}


Future<ApiResponse>tDCWalletAPI(String userId)async{
  try{
    String url='$tdcWalletTransactionUrl/$userId/0';
    print(url);
    final response=await http.get(Uri.parse(url),headers: headers);
    Map data=json.decode(response.body);
    if(data['status']!=ERROR){
      TdcWalletModel wallet=tdcWalletModelFromJson(response.body);
      return ApiResponse(status: true,data: wallet,message: data[MESSAGE]);
    }else{
      return ApiResponse(status: true,data: null,message: data[MESSAGE]);
    }
  }catch(e){
    return ApiResponse(status: false,data: null,message:e.toString());
  }
}

// URL_LIVE_TDC
class ApiResponse{
  bool status;
  String? message;
  dynamic data;
  ApiResponse({required this.status,  this.message, this.data});

}