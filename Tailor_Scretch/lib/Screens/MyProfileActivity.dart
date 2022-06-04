import 'dart:io';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:http/http.dart' as http;
import 'package:fluttertoast/fluttertoast.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:image_picker/image_picker.dart';
import 'package:path_provider/path_provider.dart';
import 'package:provider/provider.dart';
import 'package:tailor/ApiRepositary.dart';
import 'package:tailor/CommonUI/CommonSqureTextField.dart';
import 'package:tailor/CommonUI/no_internet_connection.dart';
import 'package:tailor/Model/ProfileModel.dart';
import 'package:tailor/Screens/DashBoardActivity.dart';
import 'package:tailor/Support/ProgressDialogManagers.dart';
import 'package:tailor/Support/SharedPreferencesManager.dart';
import 'package:tailor/Support/UISupport.dart';
import 'package:tailor/Support/connectivity_provider.dart';

class MyProfileActivity extends StatefulWidget {
  const MyProfileActivity({Key? key}) : super(key: key);

  @override
  _MyProfileActivityState createState() => _MyProfileActivityState();
}

class _MyProfileActivityState extends State<MyProfileActivity> {
  File? imgFile;
  ProfileModel? _profileModel;

  var _controllerFName = TextEditingController();
  var _controllerEmail = TextEditingController();
  var _controllerLast = TextEditingController();
  var _controllerContactNo = TextEditingController();
  var _controllerAddress = TextEditingController();
  var _controllerCity = TextEditingController();
  var _controllerState = TextEditingController();
  var _controllerCountry = TextEditingController();
  var _controllerZipCode = TextEditingController();
  bool isValid(String data) => RegExp(r"^[6-9]\d{9}$").hasMatch(data);
  String? _genderRadioBtnVal='Male';

  @override
  void initState() {
    Provider.of<ConnectivityProvider>(context,listen: false).startMonitoring();
    getUserdetails(userModel!.data.first.UserId).then((value) {
      setState(() {
        if (value.status) {
          _profileModel = value.data;
          _controllerEmail.text = _profileModel!.data.first.email;
          _controllerFName.text = _profileModel!.data.first.fName;
          _controllerLast.text = _profileModel!.data.first.lName;
          _controllerContactNo.text = _profileModel!.data.first.mobile;
          _controllerAddress.text = _profileModel!.data.first.address;
          _controllerCity.text = _profileModel!.data.first.city;
          _controllerState.text = _profileModel!.data.first.state;
          _genderRadioBtnVal=_profileModel!.data.first.gender;
          _controllerCountry.text = _profileModel!.data.first.country;
          _controllerZipCode.text = _profileModel!.data.first.zipCode;
        }
      });
    });
    super.initState();
  }

  bool validateInputLength(String data){
    return data.length > 2;
  }

  bool validateInputZipCodeLength(String data){
    return data.length > 5;
  }

  void toastMessage(String msg){
    Fluttertoast.showToast(msg: msg,
      backgroundColor: Colors.black,textColor: Colors.white,gravity: ToastGravity.BOTTOM,
    );
  }
  @override
  Widget build(BuildContext context) {
    return Consumer<ConnectivityProvider>(builder: (cCtx,data,child){
      if(data.isOnline!=null){
        return data.isOnline?Scaffold(
          resizeToAvoidBottomInset: true,
          backgroundColor: Colors.white,
          appBar: AppBar(
            brightness: Brightness.dark,
            actions: [
              Padding(
                padding: const EdgeInsets.only(right: 8.0),
                child: Align(
                    alignment: Alignment.centerRight,
                    child: IconButton(
                      icon: Icon(Icons.check),
                      onPressed: () async {
                        if (validation()) {
                          if(validateInputLength(_controllerFName.text.trim())){
                            if(validateInputLength(_controllerLast.text)){
                              if(validateInputLength(_controllerAddress.text)){
                                if(validateInputLength(_controllerCity.text)){
                                  if(validateInputLength(_controllerState.text)){
                                    if(validateInputLength(_controllerCountry.text)){
                                      if(validateInputZipCodeLength(_controllerZipCode.text)){
                                        showDialog(
                                            context: context,
                                            builder: (context) => ProgressDialogManager(),
                                            barrierDismissible: false)
                                            .then((value) {
                                          print(value);
                                        });
                                        Map<String, String> mao = {
                                          "user_id": userModel!.data.first.UserId.toString(),
                                          "f_name": _controllerFName.text,
                                          "l_name": _controllerLast.text,
                                          "gender": _genderRadioBtnVal!,
                                          "mobile": _controllerContactNo.text,
                                          "address": _controllerAddress.text,
                                          "city": _controllerCity.text,
                                          "state": _controllerState.text,
                                          "country": _controllerCountry.text,
                                          "zip_code": _controllerZipCode.text
                                        };
                                        saveProfiledetails(mao).then((value) {
                                          if (value.status) {
                                            Navigator.of(context).pop();
                                            Fluttertoast.showToast(msg: "Profile Updated Successfully . ");
                                            Get.offAll(DashBoard());
                                          } else {
                                            Navigator.of(context).pop();
                                            Fluttertoast.showToast(msg: value.message!);
                                          }
                                        });
                                      }else{
                                        toastMessage("Zipcode length should be greater than 5 digits.");
                                      }
                                    }else{
                                      toastMessage("Country Name length must be between 3 to 30 characters.");
                                    }
                                  }else{
                                    toastMessage("State length must be between 3 to 30 characters.");
                                  }
                                }else{
                                  toastMessage("City length must be between 3 to 30 characters.");
                                }
                              }else{
                                toastMessage("Address length must be between 3 to 100 characters.");
                              }
                            }else{
                              toastMessage("Last Name length must be between 3 to 30 characters.");
                            }
                          }else{
                            toastMessage("First Name length must be between 3 to 30 characters.");
                          }
                        }
                      },
                    )),
              ),
            ],
            backgroundColor: UISupport.APP_PRIMARY_COLOR,
            title: Text(
              "My Profile",
              style: GoogleFonts.aBeeZee(color: Colors.white),
            ),
          ),
          body: _profileModel == null
              ? Center(
            child: CircularProgressIndicator(
              strokeWidth: 5.5,
              color: Colors.black,
            ),
          )
              : Container(
            child: ListView(
              children: [
                Padding(
                  padding: const EdgeInsets.all(2.0),
                  child: Container(
                    height: 130,
                    decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(2),
                      color: Colors.white,
                      boxShadow: [
                        BoxShadow(
                          color: const Color(0x29000000),
                          offset: Offset(4, 3),
                          blurRadius: 6,
                        ),
                      ],
                    ),
                    width: double.infinity,
                    //  color: Colors.white,
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Container(
                            height: 130,
                            width: 130,
                            decoration: BoxDecoration(
                                borderRadius: BorderRadius.circular(100),
                                border: Border.all(
                                    width: 3, color: Color(0xffeae9e9))),
                            child: Stack(
                              children: [
                                Padding(
                                  padding: const EdgeInsets.all(0.0),
                                  child: imgFile == null
                                      ? ClipRRect(
                                    borderRadius:
                                    BorderRadius.circular(100),
                                    child: Image.network(
                                      _profileModel!
                                          .data.first.userImage,
                                      height: 130,
                                      width: 130,
                                      fit: BoxFit.cover,
                                    ),
                                  )
                                      : ClipRRect(
                                      borderRadius:
                                      BorderRadius.circular(100),
                                      child: Image.file(
                                        imgFile!,
                                        fit: BoxFit.cover,
                                        width: 130,
                                        height: 130,
                                      )),
                                ),
                                Align(
                                  alignment: Alignment.bottomRight,
                                  child: GestureDetector(
                                    onTap: () {
                                      imagePickerDailog();
                                    },
                                    child: Container(
                                      height: 40,
                                      width: 40,
                                      decoration: BoxDecoration(
                                          color: Colors.grey[600],
                                          borderRadius:
                                          BorderRadius.circular(24)),
                                      child: Icon(
                                        Icons.camera_alt,
                                        color: Colors.white,
                                        size: 20,
                                      ),
                                    ),
                                  ),
                                ),
                              ],
                            )),
                      ],
                    ),
                  ),
                ),
                CommonSqureTextField(
                  maxLength: 30,
                  labelText: "First Name *",
                  controller: _controllerFName,
                  IsEnable: true,
                  limitCharlength: 30,
                ),
                CommonSqureTextField(
                    maxLength: 30,
                    labelText: "Last Name *",
                    controller: _controllerLast,
                    limitCharlength: 30,
                    IsEnable: true),
                CommonSqureTextField(
                    maxLength: 30,
                    labelText: "Email Address",
                    limitCharlength: 30,
                    controller: _controllerEmail,
                    IsEnable: false),
                Padding(
                  padding: const EdgeInsets.all(12.0),
                  child: Text(
                    "Gender *",
                    style: GoogleFonts.aBeeZee(fontSize: 16.0),
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.only(left: 0.0, top: 4),
                  child: Row(
                    children: <Widget>[
                      Radio<String>(
                        value: "Male",
                        groupValue: _genderRadioBtnVal,
                        activeColor: Colors.black,
                        onChanged: _handleGenderChange,
                      ),
                      Text("Male"),
                      Radio<String>(
                        value: "Female",
                        activeColor: Colors.black,
                        groupValue: _genderRadioBtnVal,
                        onChanged: _handleGenderChange,
                      ),
                      Text("Female"),
                    ],
                  ),
                ),
                Padding(
                  padding:
                  const EdgeInsets.only(left: 12.0, top: 8, bottom: 10),
                  child: Align(
                      alignment: Alignment.centerLeft,
                      child: Text(
                        "Contact Information ",
                        textAlign: TextAlign.start,
                        style: GoogleFonts.aBeeZee(
                            fontSize: 20, color: Colors.black),
                      )),
                ),
                PhoneNumberTextField(
                  labelText: "Phone Number *",
                  controller: _controllerContactNo,
                  IsEnable: true,
                  Inputdigits: 10,),
                AddressTextField(
                    maxLength: 100,
                    limitCharlength: 100,
                    labelText: "Address *",
                    controller: _controllerAddress,
                    IsEnable: true),
                CommonSqureTextField(
                    maxLength: 30,
                    limitCharlength: 30,
                    labelText: "City *",
                    controller: _controllerCity,
                    IsEnable: true),
                CommonSqureTextField(
                    maxLength: 30,
                    limitCharlength: 30,
                    labelText: "State *",
                    controller: _controllerState,
                    IsEnable: true),
                CommonSqureTextField(
                    maxLength: 30,
                    limitCharlength: 30,
                    labelText: "Country *",
                    controller: _controllerCountry,
                    IsEnable: true),
                PhoneNumberTextField(
                    labelText: "Zipcode *",
                    controller: _controllerZipCode,
                    Inputdigits: 6,
                    IsEnable: true),
              ],
            ),
          ),
        ):InternetConnection();
      }
      return Container(
        child: Center(
          child: CircularProgressIndicator(),
        ),
      );
    });
  }

  void _openGallery(BuildContext ctx) async {
    var picture =
        await ImagePicker.platform.pickImage(source: ImageSource.gallery);
    this.setState(() {
      imgFile = File(picture!.path);
      _uploadProfile(imgFile);
    });
  }

  void _openCameraAndTakePicture(BuildContext ctx) async {
    var picture =
        await ImagePicker.platform.pickImage(source: ImageSource.camera);
    this.setState(() {
      imgFile = File(picture!.path);
      _uploadProfile(imgFile);
    });
  }

  void imagePickerDailog() {
    showDialog(
        context: context,
        barrierDismissible: true,
        builder: (BuildContext context) {
          return Dialog(
            shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(10.0)), //this right here
            child: Container(
              height: 200,
              child: Padding(
                padding: const EdgeInsets.all(12.0),
                child: Column(
                  children: [
                    Align(
                      alignment: Alignment.centerLeft,
                      child: Padding(
                        padding: const EdgeInsets.all(10.0),
                        child: Container(
                          child: Text(
                            "Add photo !",
                            style: TextStyle(
                                color: Colors.black,
                                fontSize: 17,
                                fontWeight: FontWeight.w800),
                          ),
                        ),
                      ),
                    ),
                    Align(
                      alignment: Alignment.centerLeft,
                      child: GestureDetector(
                        onTap: () {
                          _openCameraAndTakePicture(context);
                          Navigator.of(context).pop();
                        },
                        child: Padding(
                          padding: const EdgeInsets.all(10.0),
                          child: Container(
                            child: Text(
                              "Take Photo ",
                              style: TextStyle(
                                color: Colors.black,
                                fontSize: 17,
                              ),
                            ),
                          ),
                        ),
                      ),
                    ),
                    Align(
                      alignment: Alignment.centerLeft,
                      child: GestureDetector(
                        onTap: () {
                          _openGallery(context);
                          Navigator.of(context).pop();
                        },
                        child: Padding(
                          padding: const EdgeInsets.all(10.0),
                          child: Container(
                            child: Text(
                              "Choose from Gallery",
                              style: TextStyle(
                                color: Colors.black,
                                fontSize: 17,
                              ),
                            ),
                          ),
                        ),
                      ),
                    ),
                    Align(
                      alignment: Alignment.centerLeft,
                      child: Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: GestureDetector(
                          onTap: ()=>Navigator.of(context).pop(),
                          child: Container(
                            child: Text(
                              "Cancel",
                              style: TextStyle(
                                color: Colors.black,
                                fontSize: 17,
                              ),
                            ),
                          ),
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ),
          );
        });
  }

  bool validation() {
    if (_controllerFName.text.trim().isEmpty) {
      Fluttertoast.showToast(msg: "Please enter First Name.");
      return false;
    }
    if (_controllerLast.text.trim().isEmpty) {
      Fluttertoast.showToast(msg: "Please enter Last Name.");
      return false;
    }
    if (_controllerContactNo.text.trim().isEmpty) {
      Fluttertoast.showToast(msg: "Please enter Phone Number.");
      return false;
    }

    if(!isValid(_controllerContactNo.text)){
      Fluttertoast.showToast(msg: "Please enter valid Phone Number.");
      return false;
    }

    if (_controllerAddress.text.trim().isEmpty) {
      Fluttertoast.showToast(msg: "Please enter Address.");
      return false;
    }
    if (_controllerCity.text.trim().isEmpty) {
      Fluttertoast.showToast(msg: "Please enter City.");
      return false;
    }
    if (_controllerState.text.trim().isEmpty) {
      Fluttertoast.showToast(msg: "Please enter State.");
      return false;
    }
    if (_controllerCountry.text.trim().isEmpty) {
      Fluttertoast.showToast(msg: "Please enter Country.");
      return false;
    }
    if (_controllerZipCode.text.trim().isEmpty) {
      Fluttertoast.showToast(msg: "Please enter Zipcode.");
      return false;
    }

    return true;
  }

  void _uploadProfile(File? imgFile) async {
    try {
      var ApiUrls = Uri.parse(saveProfileImage);
      var request = await http.MultipartRequest("POST", ApiUrls);
      request.fields["user_id"] = userModel!.data.first.UserId.toString();
      var img = await http.MultipartFile.fromPath("user_image", imgFile!.path);
      request.files.add(img);
      var response = await request.send();
      var responsedata = await response.stream.toBytes();
      var responseString = String.fromCharCodes(responsedata);
      String res = responseString.replaceAll("{", "").replaceAll("}", "");
      List<String> res1 = res.split(",");
      var status = res1[0].split(":");
      var message = res1[1].split(":");
      if (status[1].replaceAll('"', '').replaceAll('"', '').toString() ==
          "success") {
        SharePreferenceManager.instance.IsSharePreferenceEaseDataRemove();
        Fluttertoast.showToast(
            msg: message[1]
                .replaceAll('"', '')
                .replaceAll('"', '')
                .toString()
                .trim());
      } else {
        Fluttertoast.showToast(
            msg: message[1]
                .replaceAll('"', '')
                .replaceAll('"', '')
                .toString()
                .trim());
      }
    } catch (error) {
      Fluttertoast.showToast(msg: error.toString());
    }
  }

  void _handleGenderChange(String? value) {
    setState(() {
      _genderRadioBtnVal = value;
    });
  }
}
