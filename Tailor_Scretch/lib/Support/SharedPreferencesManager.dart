


import 'package:shared_preferences/shared_preferences.dart';
import 'package:tailor/Model/UserModel.dart';

import 'package:shared_preferences/shared_preferences.dart';
import 'dart:async' show Future;


UserModel? userModel;

Future<bool>setEmailId(UserModel? userModel) async {
  final pref = await SharedPreferences.getInstance();
  String? data = userModel !=null?userModeltoJson(userModel):null;
  bool isFirst = await pref.setString('emailId', data??'logout');
  return isFirst;

}

Future<UserModel?>getEmailId()async{
  final pref=await SharedPreferences.getInstance();
  String? data=pref.getString("emailId")??null;
  if(data!=null&&data=='logout')return null;
  userModel=data!=null?userModelFromJson(data):null;
  return userModel;
}

Future<bool>setUserId(UserModel? userModel) async {
  final pref = await SharedPreferences.getInstance();
  String? data = userModel !=null?userModeltoJson(userModel):null;
  bool isFirst = await pref.setString('user', data??'logout');
  return isFirst;
}

Future<UserModel?>getUserId()async{
  final pref=await SharedPreferences.getInstance();
  String? data=pref.getString("user")??null;
  if(data!=null&&data=='logout')return null;
  userModel=data!=null?userModelFromJson(data):null;
}

class SharePreferenceManager{
  SharePreferenceManager._privateConstructor();
  static final SharePreferenceManager instance=SharePreferenceManager._privateConstructor();


  setEaseData(String key,String value)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.setString(key,value);
  }


  setYardageData(String key,String value)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.setString(key,value);
  }



  setDropDownFabricType(String key,String value)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.setString(key,value);
  }


  setDropDownFit(String key,String value)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.setString(key,value);
  }


  setDropDownInchOrCm(String key,String value)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.setString(key,value);
  }


  setDropDownFabricWidthCm(String key,String value)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.setString(key,value);
  }


  setDropDownFusibleinterfacingCm(String key,String value)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.setString(key,value);
  }

  setDropDownFusibleinterfacingInch(String key,String value)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.setString(key,value);
  }

  setDropDownMainFabricCm(String key,String value)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.setString(key,value);
  }

  setDropDownMainFabricInch(String key,String value)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.setString(key,value);
  }

  Future<String>getDropDownMainFabricCm(String key)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.getString(key)??"";
  }

  Future<String>getDropDownMainFabricInch(String key)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.getString(key)??"";
  }



  Future<String>getDropDownFusibleinterfacingCm(String key)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.getString(key)??"";
  }


  Future<String>getDropDownFusibleinterfacingInch(String key)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.getString(key)??"";
  }

  setDropDownFabricWidthInch(String key,String value)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.setString(key,value);
  }

  Future<String>getDropDownFabricType(String key)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.getString(key)??"";
  }

  Future<String>getDropDownFabricWidthCm(String key)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.getString(key)??"";
  }

  Future<String>getDropDownFabricWidthInch(String key)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.getString(key)??"";
  }

  Future<String>getDropDownFit(String key)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.getString(key)??"";
  }


  Future<String>getDropDownInchOrCm(String key)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.getString(key)??"";
  }

  Future<String>getEaseData(String key)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.getString(key)??"";
  }


  Future<String>getYardageData(String key)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.getString(key)??"";
  }

  setSelectedAttribute(String key,String value)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.setString(key,value);
  }

  setCategoryName(String key,String value)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.setString(key,value);
  }

  setCategoryId(String key,String value)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.setString(key,value);
  }

  setImagePath(String key,String value)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.setString(key,value);
  }

  setImageUrl(String key,String value)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.setString(key,value);
  }

  setDesignID(String key,String value)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.setString(key,value);
  }


  Future<String>getSelectedAttribute(String key)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.getString(key)??"";
  }


  Future<String>getCategoryName(String key)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.getString(key)??"";
  }

  Future<String>getCategoryId(String key)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.getString(key)??"";
  }


  Future<String>getImagePath(String key)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.getString(key)??"";
  }

  Future<String>getImageUrl(String key)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.getString(key)??"";
  }



  Future<String>getDesignID(String key)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.getString(key)??"";
  }

// ignore: non_constant_identifier_names
  void IsSharePreferenceEaseDataRemove() async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    preferences.remove("Ease");
  }


  void IsSharePreferenceFabricDropDown() async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    preferences.remove("FabricType");
  }


//---- Basic Yardage -----
  void IsSharePreferenceFabricWidthDropDown() async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    preferences.remove("FabricWidthCm");
  }

  void IsSharePreferenceFabricWidthInchDropDown() async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    preferences.remove("FabricWidthInch");
  }


  //---- End ----

  //---  Advance Yardage ---
  void IsSharePreferenceFusibleinterfacingInch() async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    preferences.remove("FusibleinterfacingInch");
  }

  void IsSharePreferenceFusibleinterfacingCm() async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    preferences.remove("FusibleinterfacingCm");
  }

  void IsSharePreferenceMainFabricCm() async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    preferences.remove("MainFabricCm");
  }
  void IsSharePreferenceMainFabricInch() async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    preferences.remove("MainFabricInch");
  }
  //---- End ---

  void IsSharePreferenceYardageDataRemove() async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    preferences.remove("Yardage");
  }

  void IsSharePreferenceFitDropDown() async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    preferences.remove("Fit");
  }

  void IsSharePreferenceInchCmDropDown() async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    preferences.remove("InchCm");
  }
  void IsSharePreferenceSelectedAttributeDataRemove() async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    preferences.remove("selectedAttribute");
  }
  void IsSharePreferenceCategoryNameDataRemove() async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    preferences.remove("CategoryName");
  }
  void IsSharePreferencCategoryIdDataRemove() async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    preferences.remove("CategoryId");
  }
  void IsSharePreferenceImagePathDataRemove() async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    preferences.remove("ImagePath");
  }

 void IsSharePreferenceImgUrlDataRemove() async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    preferences.remove("ImgUrl");
  }


 // ImgUrl

  setUserID(String key,String value)async {
    SharedPreferences preferences =await SharedPreferences.getInstance();
    return preferences.setString(key, value);
  }

  Future<String>getUserID(String key)async{
    SharedPreferences preferences=await SharedPreferences.getInstance();
    return preferences.getString(key)??"";
  }

}
