



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

setCity(String key,String value)async{
   SharedPreferences preferences=await SharedPreferences.getInstance();
   return preferences.setString(key, value);
}

   setUserID(String key,String value)async {
   SharedPreferences preferences =await SharedPreferences.getInstance();
   return preferences.setString(key, value);
  }

  Future<String> getRefferalCode(String key)async{
   SharedPreferences preferences=await SharedPreferences.getInstance();
   return preferences.getString(key)??"";
  }

  Future<String>getUserID(String key)async{
   SharedPreferences preferences=await SharedPreferences.getInstance();
   return preferences.getString(key)??"";
  }


Future<String>getMobileNo(String key)async{
   SharedPreferences preferences=await SharedPreferences.getInstance();
   return preferences.getString(key)??"";
  }

  Future<String>getDeviceID(String key)async{
   SharedPreferences preferences=await SharedPreferences.getInstance();
 return preferences.getString(key)??"";
 }


  Future<String>getCity(String key)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.getString(key)??"";
  }

  Future<String>getWalletMinimumDediction(String key)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.getString(key)??"";
  }

  Future<String>getCityAreas(String key)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.getString(key)??"";
  }

  setUserName(String key,String value)async{
   SharedPreferences preferences=await SharedPreferences.getInstance();
   return preferences.setString(key,value);
  }

  setDeviceID(String key,String value)async{
   SharedPreferences preferences=await SharedPreferences.getInstance();
   return preferences.setString(key, value);
  }

  setUserEmailAddress(String key,String value)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.setString(key,value);
  }

  setUserWalletAmt(String key,String value)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.setString(key,value);
  }

  setUserPhoneNo(String key,String value)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.setString(key,value);
  }

   setRefferalCode(String key,String value) async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.setString(key,value);
  }

  void setCityAreas(String key, String value) async{
   SharedPreferences preferences=await SharedPreferences.getInstance();
   preferences.setString(key, value);
  }

  setWalletMinimumDeduction(String key,String value) async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    preferences.setString(key, value);
  }

}