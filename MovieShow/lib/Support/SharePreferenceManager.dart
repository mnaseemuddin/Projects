



import 'package:shared_preferences/shared_preferences.dart';
import 'dart:async' show Future;
class SharePreferenceManager{
  SharePreferenceManager._privateConstructor();
  static final SharePreferenceManager instance=SharePreferenceManager._privateConstructor();


 setSharePreferenceInitialized(String key,String value)async{
   SharedPreferences preferences=await SharedPreferences.getInstance();
   return preferences.setString(key,value);
 }

Future<String>IsSharePreferenceInitialized(String key)async{
  SharedPreferences preferences=await SharedPreferences.getInstance();
  return preferences.getString(key)??"";
}

// ignore: non_constant_identifier_names
void IsSharePreferenceRemove() async{
SharedPreferences preferences=await SharedPreferences.getInstance();
preferences.clear();
}

   setUserID(String key,String value)async {
   SharedPreferences preferences =await SharedPreferences.getInstance();
   return preferences.setString(key, value);
  }

  Future<String>getUserID(String key)async{
   SharedPreferences preferences=await SharedPreferences.getInstance();
   return preferences.getString(key)??"";
  }

}