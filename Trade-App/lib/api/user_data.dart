

import 'package:shared_preferences/shared_preferences.dart';
import 'package:trading_apps/models/user_model.dart';

UserModel? userModel;
Future<bool>isFirstLaunch() async {
  final pref = await SharedPreferences.getInstance();
  bool isFirst = pref.getBool('isFirst')??true;
  return isFirst;
}

Future<bool>setFirstLaunch(bool val) async {
  final pref = await SharedPreferences.getInstance();
  bool isFirst = await pref.setBool('isFirst', val);
  return isFirst;
}

Future<UserModel?>getUser() async {
  final pref = await SharedPreferences.getInstance();
  String? data = pref.getString('user')??null;
  if(data !=null&&data=='logout')return null;
  userModel = data!=null?userModelFromJson(data):null;
  return userModel;
}

Future<bool>setUser(UserModel? userModel) async {
  final pref = await SharedPreferences.getInstance();
  String? data = userModel !=null?userModelToJson(userModel):null;
  bool isFirst = await pref.setString('user', data??'logout');
  return isFirst;
}


Future<bool>setWithdrawAddress(UserModel? userModel) async {
  final pref = await SharedPreferences.getInstance();
  String? data = userModel !=null?userModelToJson(userModel):null;
  bool isFirst = await pref.setString('withdraw', data??'0');
  return isFirst;
}

Future<String>getCurrency() async {
  final pref = await SharedPreferences.getInstance();
  String isFirst = pref.getString('currency')??'btc';
  return isFirst;
}

Future<bool>setCurrency(String val) async {
  final pref = await SharedPreferences.getInstance();
  bool isFirst = await pref.setString('currency', val);
  return isFirst;
}


Future<int>getDuration() async {
  final pref = await SharedPreferences.getInstance();
  int isFirst = pref.getInt('duration')??1;
  return isFirst;
}

Future<bool>setDuration(int val) async {
  final pref = await SharedPreferences.getInstance();
  bool isFirst = await pref.setInt('duration', val);
  return isFirst;
}

Future<int>getAmount() async {
  final pref = await SharedPreferences.getInstance();
  int isFirst = pref.getInt('amount')??1;
  return isFirst;
}

Future<bool>setAmount(int val) async {
  final pref = await SharedPreferences.getInstance();
  bool isFirst = await pref.setInt('amount', val);
  return isFirst;
}