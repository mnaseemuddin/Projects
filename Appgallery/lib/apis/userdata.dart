


import 'package:appgallery/Model/usermodel.dart';
import 'package:shared_preferences/shared_preferences.dart';

ConsoleLoginModel? consoleUserModel;
UserLoginModel? userLoginModel;

Future<ConsoleLoginModel?>getUser()async{
  final prefs=await SharedPreferences.getInstance();
  String? data=prefs.getString('consoleUser')??null;
  if(data!=null&&data=='logout')return null;
  consoleUserModel=data!=null?consoleModelFromJson(data):null;
  return consoleUserModel;
}

Future<bool>setUser(ConsoleLoginModel? userModel) async {
  final pref = await SharedPreferences.getInstance();
  String? data = userModel !=null?consoleModelToJson(userModel):null;
  bool isFirst = await pref.setString('consoleUser', data??'logout');
  return isFirst;
}



//
Future<UserLoginModel?>getUserLoginDetails()async{
  final prefs=await SharedPreferences.getInstance();
  String? data=prefs.getString('user')??null;
  if(data!=null&&data=='logout')return null;
  userLoginModel=data!=null?userModelFromJson(data):null;
  return userLoginModel;
}

Future<bool>setUserLoginDetails(UserLoginModel? userModel) async {
  final pref = await SharedPreferences.getInstance();
  String? data = userModel !=null?userModelToJson1(userModel):null;
  bool isFirst = await pref.setString('user', data??'logout');
  return isFirst;
}