


import 'dart:io';

import 'package:appgallery/CommonUI/apptextfield.dart';
import 'package:appgallery/CommonUI/apptextview.dart';
import 'package:appgallery/CommonUI/commonwidget.dart';
import 'package:appgallery/Resources/color.dart';
import 'package:appgallery/Resources/constants.dart';
import 'package:appgallery/Utils/loadingoverlay.dart';
import 'package:appgallery/Utils/routes.dart';
import 'package:appgallery/apis/userdata.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:image_picker/image_picker.dart';

import '../../../CommonUI/progressdialog.dart';
import '../../../apis/apirepositary.dart';




class MyProfile extends StatefulWidget {
  const MyProfile({Key? key}) : super(key: key);

  @override
  _MyProfileState createState() => _MyProfileState();
}

class _MyProfileState extends State<MyProfile> {



  var nameController=TextEditingController();
  var emailController=TextEditingController();
  var mobileController=TextEditingController();
  bool disabled=true;
  final _key=GlobalKey<FormState>();
  var imgFile;
  bool isLoading=true;
  final controllerSKLAddress=TextEditingController();
  var photoUrl="";
  int? phoneNumber;


  @override
  void initState() {
    super.initState();
    getPlayStoreProfileDetails();
   }

  /*void _getWalletAPI() {

    Map<dynamic,String>body={
      "email":userModel!.data.first.email,
      "token":userModel!.data.first.token
    };
    getPlaySProfile(body).then((value){
      if(value.status){
        setState(() {
          profile=value.data;
          _balance=profile!.data.first.walletAmount;
          nameController.text=profile!.data.first.name;
          emailController.text=profile!.data.first.email;
          controllerSKLAddress.text=profile!.data.first.skillCoinAddress;
          contollerTRONAddress.text=profile!.data.first.tronAddress;
          profileImgUrl=profile!.data.first.resultsImage;
          isLoading=false;
        });
      }
    });
  }*/

  @override
  Widget build(BuildContext context) {
    return Scaffold(

      appBar: AppBar(
        iconTheme:  IconThemeData(color: Colors.black.withOpacity(0.8)),
        title:  HeadingText1(title: 'My Profile',fontSize: 15,
          color: appBlackColor,),),
      body: SafeArea(
        top: true,
        child: photoUrl==""?LoadingOverlay():Form(
          key: _key,
          child: ListView(
            children: [
              const SizedBox(height: 30),
              Padding(
                padding: const EdgeInsets.only(top:18.0),
                child: Center(
                  child: Stack(
                    alignment: Alignment.center,
                    children: [
                      GestureDetector(
                        onTap: (){
                          imagePickerDailog();
                        },
                        child: ContainerBgWithRadius(
                          height: 120,
                          width: 120,
                          circular: 60,
                          color: Colors.white,
                          child: ClipRRect(
                              borderRadius: BorderRadius.circular(60),
                              child: imgFile==null?Image.network(
                                photoUrl, fit: BoxFit.fitHeight,):
                              Image.file(imgFile,fit: BoxFit.fitHeight,)),
                        ),
                      ),
                    ],
                  ),
                ),
              ),

              Padding(
                padding: const EdgeInsets.all(15.0),
                child: NameTextField(
                  prefixIcon: assetsFullNameImg,
                  hintText: "Full Name", controller: nameController,
                  validationMsg: 'Please enter full name .',),
              ),

              Padding(
                padding: const EdgeInsets.all(15.0),
                child: EmailTextField(
                  enabled: false,
                  prefixIcon: assetsEmailIDImg,
                  hintText: "Email Address", controller: emailController,
                  validationMsg: 'Please enter email address .',),
              ),


              Padding(
                padding: const EdgeInsets.all(15.0),
                child: NumberTextField(hintText: 'Phone Number', controller: mobileController,
                  prefixIcon: assetsPhoneImg, validationMsg: '',),
              ),



              const SizedBox(height: 50,),
              SubmitButton(onPressed: (){
                if(_key.currentState!.validate()){
                  _updateProfileAPI();
                  //  saveProfile();
                }
              }, buttonName: 'Save',)

            ],)
        ),
      ),
    );
  }

  void _updateProfileAPI() {
    showDialog(context: context, builder: (context) => ProgressDialog(), barrierDismissible: false).then((value){
      print(value);
    },);
    Map<dynamic,String>body={
      "email":userLoginModel!.email,
      "name":nameController.text,
      "contactno":mobileController.text,

    };

    updatePlayStoreProfileAPI(body).then((value){
      if(value.status){
        Fluttertoast.showToast(msg: value.message.toString());
        navReturn(context);
      }else{
        Fluttertoast.showToast(msg: value.message.toString());
        navReturn(context);
      }
    });

  }
  void _openGallery(BuildContext ctx) async {
    var picture =
    await ImagePicker.platform.pickImage(source: ImageSource.gallery);
    setState(() {
      imgFile = File(picture!.path);
      uploadPlayStoreProfileAPI(imgFile,userLoginModel!.email).then((value){
        if(value.status){
          Fluttertoast.showToast(msg: value.message.toString());
        }else{
          Fluttertoast.showToast(msg: value.message.toString());
        }
      });
    });
  }

  void _openCameraAndTakePicture(BuildContext ctx) async {
    var picture =
    await ImagePicker.platform.pickImage(source: ImageSource.camera);
    setState(() {
      imgFile = File(picture!.path);
      uploadProfile(imgFile,emailController.text);
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
            child: SizedBox(
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
                          child: const Text(
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
                            child: const Text(
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
                          child: const Text(
                            "Cancel",
                            style: TextStyle(
                              color: Colors.black,
                              fontSize: 17,
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

  void getPlayStoreProfileDetails() {

    Map<dynamic,String>body={
      "email":userLoginModel!.email,
      "token":userLoginModel!.token
    };
    getPlayStoreProfileDetailsAPI(body).then((value){
      if(value.status){
        setState(() {
          emailController.text=value.data["email"];
          nameController.text=value.data["name"];
          mobileController.text=value.data["mobile"].toString();
          photoUrl=value.data["user_image"];
        });
      }else{
        toast(value.message);
      }
    });
  }

}
