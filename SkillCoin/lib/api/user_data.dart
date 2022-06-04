


import 'package:skillcoin/model/usermodel.dart';
import 'package:shared_preferences/shared_preferences.dart';
UserModel? userModel;
Future<bool>setUser(UserModel? userModel)async{

  final prefs=await SharedPreferences.getInstance();
  String? data = userModel !=null?userModelToJson(userModel):null;
  bool isFirst = await prefs.setString('user', data??'logout');
  return isFirst;
}


Future<UserModel?>getUser() async {
  final pref = await SharedPreferences.getInstance();
  String? data = pref.getString('user')??null;
  if(data !=null&&data=='logout')return null;
  userModel = data!=null?userModelFromJson(data):null;
  return userModel;
}