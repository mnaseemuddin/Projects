
import 'package:royal_q/app/models/models.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../modules/AffiliateApp/model/AffiliateLoginResponse.dart';

UserInfo? userInfo;
bool isToast = false;
int exchangeValue = 1;
AffiliateLoginResponse? userModel;
Future<bool>setUser(AffiliateLoginResponse? userModel)async{

  final prefs=await SharedPreferences.getInstance();
  String? data = userModel !=null?affiliateLoginResponseToJson(userModel):null;
  bool isFirst = await prefs.setString('user', data??'logout');
  return isFirst;
}


Future<AffiliateLoginResponse?>getUser() async {
  final pref = await SharedPreferences.getInstance();
  String? data = pref.getString('user')??null;
  if(data !=null&&data=='logout')return null;
  userModel = data!=null?affiliateLoginResponseFromJson(data):null;
  return userModel;
}



Future<bool>setAccountType(String val)async{
  final prefs=await SharedPreferences.getInstance();
  bool isFirst=await prefs.setString("accountType", val);
  return isFirst;
}


Future<String>getAccountType()async{
  final prefs=await SharedPreferences.getInstance();
  String isFirst=prefs.getString("accountType")??"";
  return isFirst;
}