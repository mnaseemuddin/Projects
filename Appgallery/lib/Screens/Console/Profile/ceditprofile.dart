

import 'dart:io';

import 'package:appgallery/CommonUI/apptextfield.dart';
import 'package:appgallery/CommonUI/apptextview.dart';
import 'package:appgallery/CommonUI/commonwidget.dart';
import 'package:appgallery/Resources/color.dart';
import 'package:appgallery/Resources/constants.dart';
import 'package:appgallery/Utils/routes.dart';
import 'package:appgallery/apis/userdata.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:image_picker/image_picker.dart';

import '../../../CommonUI/progressdialog.dart';
import '../../../apis/apirepositary.dart';



class EditProfileActivity extends StatefulWidget {
  const EditProfileActivity({Key? key}) : super(key: key);

  @override
  _EditProfileActivityState createState() => _EditProfileActivityState();
}

class _EditProfileActivityState extends State<EditProfileActivity> {

  var nameController=TextEditingController();
  var emailController=TextEditingController();
  var mobileController=TextEditingController();
  bool disabled=true;
  final _key=GlobalKey<FormState>();
  var imgFile;
  String photoUrl="";
  bool isLoading=true;



  @override
  void initState() {
    SystemChrome.setSystemUIOverlayStyle(const SystemUiOverlayStyle(
        statusBarColor: Colors.transparent,
        statusBarBrightness: Brightness.dark
    ));
    getProfileDetails();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: appBarColor,
      body: SafeArea(
        top: true,
        child: Form(
          key: _key,
          child: isLoading==false?ListView(
            children: [
              const SizedBox(height: 30),
            Padding(
              padding: const EdgeInsets.only(top:18.0),
              child: Center(
                child: Stack(
                  alignment: Alignment.center,
                  children: [
                    ContainerBgWithRadius(
                      height: 120,
                      width: 120,
                      circular: 60,
                      child: ClipRRect(
                          borderRadius: BorderRadius.circular(60),
                          child: imgFile==null?Image.network(
                            photoUrl,
                            fit: BoxFit.fitHeight,):Image.file(imgFile,fit: BoxFit.fitHeight,)),
                    ),
                     Padding(
                      padding: const EdgeInsets.only(top:80.0,right: 128,bottom: 20),
                      child: Align(alignment: Alignment.bottomRight,
                      child: GestureDetector(
                        onTap: (){
                          imagePickerDailog();
                        },
                        child: const ContainerBgWithRadius(
                          height: 35,
                            width: 35,
                            color: appGreyColor,
                            circular: 40,
                            child:  Icon(Icons.camera_alt_outlined,size: 22,)
                            ),
                      )),
                    )
                  ],
                ),
              ),
            ),

              Padding(
                padding: const EdgeInsets.all(15.0),
                child: labelNameTextField(
                  enabled: disabled,
                  prefixIcon: "assets/img.png",labelText: "Full Name",
                  hintText: "Full Name", controller: nameController,
                  validationMsg: 'Please enter full name .',),
              ),

              Padding(
                padding: const EdgeInsets.all(15.0),
                child: labelNameTextField(
                  enabled: false,
                  prefixIcon: "assets/mail.png",labelText: "Email Address",
                  hintText: "Email Address", controller: emailController,
                  validationMsg: 'Please enter email address .',),
              ),

              Padding(
                padding: const EdgeInsets.all(15.0),
                child: labelNumberTextField(
                  enabled: disabled,
                  prefixIcon: "assets/phone.png",
                    hintText: "Mobile Number", controller: mobileController,
                    validationMsg: "Please enter mobile number .", labelText: 'Mobile No',),
              ),

              const SizedBox(height: 50,),
              SubmitButton(onPressed: (){
                if(_key.currentState!.validate()){
                  keyboardDismissed(context);
                  saveProfile();
                }
              }, buttonName: "Save")

          ],):const Center(
            child: CircularProgressIndicator(strokeWidth: 5,
              color: appBlackColor,),
          ),
        ),
      ),
    );
  }

  void saveProfile() {
    showDialog(context: context, builder: (context) => ProgressDialog(),
        barrierDismissible: false).then((value){
      print(value);
    },);
    Map<String,dynamic>body={
      "email_address":emailController.text.trim(),
      "name":nameController.text.trim(),
      "contactno":mobileController.text
    };
    saveProfileAPI(body).then((value){
      if(value.status){
       navReturn(context);
        Fluttertoast.showToast(msg: value.message.toString());
      }else{
        Fluttertoast.showToast(msg: value.message.toString());
      }
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
  void _openGallery(BuildContext ctx) async {
    var picture =
    await ImagePicker.platform.pickImage(source: ImageSource.gallery);
    setState(() {
      imgFile = File(picture!.path);
      uploadProfile(imgFile,emailController.text).then((value){
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
    this.setState(() {
      imgFile = File(picture!.path);
      uploadProfile(imgFile,emailController.text);
    });
  }

  void getProfileDetails(){

    getUser().then((value){
      Map<String,dynamic>body={
        "email_address":value!.email,
        "token":value.token
      };
      getProfileAPI(body).then((value){
        if(value.status){
          setState(() {
            isLoading=false;
            emailController.text=value.data["email"];
            mobileController.text=value!.data["mobile"].toString();
            nameController.text=value!.data['name'];
            photoUrl= value!.data["user_image"];
          });
        }else{
          Fluttertoast.showToast(msg: value!.message.toString());
        }
      });
    });

  }
}

