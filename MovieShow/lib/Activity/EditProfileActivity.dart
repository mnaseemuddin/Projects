

import 'dart:convert';
import 'dart:io';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
// ignore: unused_import
import 'package:get/get.dart';
import 'package:http/http.dart' as http;
import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
// ignore: unused_import
import 'package:movieshow/Activity/DashBoard.dart';
import 'package:movieshow/ApplicationConfigration/ApiUrls.dart';
import 'package:movieshow/Support/AlertDialogManager.dart';
import 'package:movieshow/Support/SharePreferenceManager.dart';

class EditProfile extends StatefulWidget {
  @override
  _EditProfileState createState() => _EditProfileState();
}

class _EditProfileState extends State<EditProfile> {

  TextEditingController editName=TextEditingController();
  TextEditingController editMobileNo=TextEditingController();
  TextEditingController editEmailId=TextEditingController();
  final _key=GlobalKey<FormState>();
  late Map map,map1;
  // ignore: non_constant_identifier_names
  late String UserID;
  // ignore: non_constant_identifier_names
  late String Name,MobileNo,EmailID,UserImg,Address,City,State,
      // ignore: non_constant_identifier_names
      Country,Pincode,BounsAmount='';

  File? imgFile;
  String imgURL="";



  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.black,
      appBar: AppBar(
        backgroundColor: Colors.black,
        title: Text("My Profile",style: TextStyle(color:
                 Colors.white, fontFamily: 'Helvetica Neue',fontSize: 16.5,
                     fontWeight: FontWeight.bold)),
      ),
      body: SafeArea(
        child: Form(
          key: _key,
          child: ListView(
          children: [
                  Center(
                    child: GestureDetector(
                      onTap: (){
                        customAlertDialog();
                      },
                      child: Container(
                        height: 120,
                        width: 125,
                        child: imgFile==null||imgURL==""?CircleAvatar(
                          radius: 40,
                          child: ClipOval(
                            child: FadeInImage.assetNetwork(placeholder:
                            "drawable/user_profile.png", image: imgURL,
                              height: 120,width: 125,fit: BoxFit.fill,imageErrorBuilder:
                                (context,error,stackTrace)=>Image.asset("drawable/user_profile.png",
                                  height: 120,width: 125,),),
                          ),
                        ):
                        CircleAvatar(
                            radius: 40,
                            child: ClipOval(child: Image.file(imgFile!,
                              fit: BoxFit.fill,width: 125,height: 120,scale: 1.0,)))),
                    ),
                  ),Line(),
                 Padding(
                   padding: const EdgeInsets.only(left:25.0),
                   child: Text(
                     'Name',style: TextStyle(color:
                   Colors.white, fontFamily: 'Helvetica Neue',fontSize: 15.5,
                       fontWeight: FontWeight.bold)
                   ),
                 ),
            Padding(
              padding: const EdgeInsets.only(left:25.0),
              child: TextField(
                controller: editName,
                style: TextStyle(color: Colors.white),
                decoration: new InputDecoration(
                  hintText: "Enter Name",
                  border: InputBorder.none,
                  hintStyle: TextStyle(
                    fontFamily: 'Helvetica Neue',
                    fontSize: 15,
                    color: const Color(0xffffffff),
                    fontWeight: FontWeight.w500,
                  ),
                ),
              ),
            ),
            LineDown(),
            Padding(
              padding: const EdgeInsets.only(left:25.0),
              child: Text(
                  'Mobile No',style: TextStyle(color:
              Colors.white, fontFamily: 'Helvetica Neue',fontSize: 15.5,
                  fontWeight: FontWeight.bold)
              ),
            ),
            Padding(
              padding: const EdgeInsets.only(left:25.0),
              child: TextField(
                controller: editMobileNo,
                style: TextStyle(color: Colors.white),
                inputFormatters: [
                  LengthLimitingTextInputFormatter(13),
                  //  FilteringTextInputFormatter.digitsOnly
                ],
                decoration: new InputDecoration(
                  hintText: "Enter Mobile No",
                  border: InputBorder.none,
                  hintStyle: TextStyle(
                    fontFamily: 'Helvetica Neue',
                    fontSize: 15,
                    color: const Color(0xffffffff),
                    fontWeight: FontWeight.w500,
                  ),
                ),
              ),
            ),
            LineDown(),
            Padding(
              padding: const EdgeInsets.only(left:25.0),
              child: Text(
                  'Email ID',style: TextStyle(color:
              Colors.white, fontFamily: 'Helvetica Neue',fontSize: 15.5,
                  fontWeight: FontWeight.bold)
              ),
            ),
            Padding(
              padding: const EdgeInsets.only(left:25.0),
              child: TextFormField(
                controller: editEmailId,
                validator: (value){
                  if(value!.isEmpty){
                    return "Please Enter Email Id .";
                  }
                  return null;
                },
                style: TextStyle(color: Colors.white),
                decoration: new InputDecoration(
                  hintText: "Enter Email Id",
                  border: InputBorder.none,
                  hintStyle: TextStyle(
                    fontFamily: 'Helvetica Neue',
                    fontSize: 15,
                    color: const Color(0xffffffff),
                    fontWeight: FontWeight.w500,
                  ),
                ),
              ),
            ),
            LineDown(),

            GestureDetector(
              onTap: (){
                if(_key.currentState!.validate()) {
                  UpdateProfileDetails();
                }
              },
              child: Center(
                child: Container(
                height: 45,
                  width: 150,
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(60.0),
                      color: Colors.black,
                    border: Border.all(width: 1.1, color: const Color(0xffe61a1a)),
                  ),
                    child: Center(
                      child: Text('Update',style: TextStyle(
                        fontFamily: 'Helvetica Neue',
                        fontSize: 15,
                        color: const Color(0xffffffff),
                        fontWeight: FontWeight.w500,
                      ),),
                    ),
                ),
              ),
            )

          ],),
        ),
      ),
    );
  }
  Widget Line() {
    return Padding(
      padding: const EdgeInsets.only(left: 15.0,right: 15,top: 25,bottom: 12),
      child: Container(
        height: 0.90,
        width: double.infinity,
        color: Colors.white,
      ),
    );
  }

  Widget LineDown() {
    return Padding(
      padding: const EdgeInsets.only(left: 15.0,right: 15,top: 5,bottom: 12),
      child: Container(
        height: 0.70,
        width: double.infinity,
        color: Colors.white,
      ),
    );
  }
  @override
  void initState() {
    SharePreferenceManager.instance.getUserID("UserID").then((value){
      setState(() {
        UserID=value;
       myProfileDeatils(UserID);
      });
    });
    super.initState();
  }

  // ignore: non_constant_identifier_names
  void UpdateProfileDetails()async {
    map={
      "userid":UserID,
      'mobile':editMobileNo.text,
      'email':editEmailId.text,
      'name':editName.text
    };
    try{
      // ignore: non_constant_identifier_names
      var ApiUrls=Uri.parse(APiUrls.MyProfileUpdate);
      var response=await http.post(ApiUrls,body: map);
      if(response.statusCode==200) {
        map = json.decode(response.body);
        setState(() {
          if(map["status"]=="1"){
             showMsg(map["message"]);
          }else{
            AlertDialogManager().IsErrorAlertDialog(context, "Status", map["message"], "");
          }
        });
      }
    }catch(e){
      AlertDialogManager().IsErrorAlertDialog(context, "Status", e.toString(), "");
    }
  }
  void showMsg(String msg){
    Fluttertoast.showToast(
      msg: msg,
      toastLength: Toast.LENGTH_SHORT,
      gravity: ToastGravity.BOTTOM,
    );
  }
  void myProfileDeatils(String UserID)async{
    map={
      "userid":UserID,
    };
    try{
      // ignore: non_constant_identifier_names
      var ApiUrls=Uri.parse(APiUrls.MyProfile);
      var response=await http.post(ApiUrls,body: map);
      if(response.statusCode==200) {
        map = json.decode(response.body);
        setState(() {
          if(map["status"]=="1"){
            map1 =map['data'];
            editName.text=map1['name'];
            editMobileNo.text=map1["mobile"];
            editEmailId.text=map1["email"];
            imgURL=map1["user_image"];
          }else{
            AlertDialogManager().IsErrorAlertDialog(context, "Status", map["message"], "");
          }
        });
      }
    }catch(e){
      AlertDialogManager().IsErrorAlertDialog(context, "Status", e.toString(), "");
    }
  }

  void customAlertDialog() {
      showDialog(
        context: context,
        builder: (BuildContext context) {
          return Dialog(
            shape: RoundedRectangleBorder(
                borderRadius:
                    BorderRadius.circular(10.0)), //this right here
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
                              style: TextStyle(color: Colors.black,fontSize: 17,fontWeight: FontWeight.w800),
                            ),
                        ),
                      ),
                    ),
                    Align(
                       alignment: Alignment.centerLeft,
                      child: GestureDetector(
                        onTap: (){
                          _openCameraAndTakePicture(context);
                          Navigator.of(context).pop();
                        },
                        child: Padding(
                            padding: const EdgeInsets.all(10.0),
                            child: Container(
                              child: Text(
                                  "Take Photo ",
                                  style: TextStyle(color: Colors.black,fontSize: 17,
                                  ),
                                ),
                            ),
                          ),
                      ),
                    ),
                      Align(
                         alignment: Alignment.centerLeft,
                        child: GestureDetector(
                          onTap: (){
                            _openGallery(context);
                            Navigator.of(context).pop();
                          },
                          child: Padding(
                            padding: const EdgeInsets.all(10.0),
                            child: Container(
                              child: Text(
                                  "Choose from Gallery",
                                  style: TextStyle(color: Colors.black,fontSize: 17,
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
                          child: Container(
                            child: Text(
                                "Cancel",
                                style: TextStyle(color: Colors.black,fontSize: 17,
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

  void _openGallery(BuildContext ctx) async{
    var picture=await ImagePicker.platform.pickImage(source: ImageSource.gallery);
    this.setState(() {
      imgFile=File(picture!.path);
      _uploadProfile(imgFile);
    });
  }
  void _openCameraAndTakePicture(BuildContext ctx) async{
    var picture=await ImagePicker.platform.pickImage(source: ImageSource.camera);
    this.setState(() {
      imgFile=File(picture!.path);
      _uploadProfile(imgFile);
    });
  }

  void _uploadProfile(File? imgFile) async{
    // map={
    //   "userid":UserID,
    //   "image":imgFile
    // };
    // try{
    //   // ignore: non_constant_identifier_names
    //   var ApiUrls=Uri.parse(APiUrls.UploadProfileImg);
    //   var response=await http.post(ApiUrls,body: map);
    //   if(response.statusCode==200) {
    //     map = json.decode(response.body);
    //     setState(() {
    //       if(map["status"]=="1"){
    //         map1 =map['data'];
    //         Fluttertoast.showToast(msg: map["message"]);
    //       }else{
    //         AlertDialogManager().IsErrorAlertDialog(context, "Status", map["message"], "");
    //       }
    //     });
    //   }
    // }catch(e){
    //   AlertDialogManager().IsErrorAlertDialog(context, "Status", e.toString(), "");
    // }
    var ApiUrls=Uri.parse(APiUrls.UploadProfileImg);
    var request=http.MultipartRequest("POST",ApiUrls);
    request.fields["userid"]=UserID;
    var img=await http.MultipartFile.fromPath("image",imgFile!.path);
    request.files.add(img);
    var response = await request.send();
    if(response.statusCode==200) {
      var responsedata = await response.stream.toBytes();
      var responseString = String.fromCharCodes(responsedata);
      String res=responseString.replaceAll("{", "").replaceAll("}", "");
      List<String> res1=res.split(",");
      var message=res1[0].split(":");
      Fluttertoast.showToast(msg: message[1].replaceAll('"', '').replaceAll('"', '').toString().trim());
    }
  }


}