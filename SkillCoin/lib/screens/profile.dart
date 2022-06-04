


import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_barcode_scanner/flutter_barcode_scanner.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:image_picker/image_picker.dart';
import 'package:ionicons/ionicons.dart';
import 'package:skillcoin/api/apirepositary.dart';
import 'package:skillcoin/customui/button.dart';
import 'package:skillcoin/customui/container.dart';
import 'package:skillcoin/customui/textview.dart';
import 'package:skillcoin/model/profiledetailsmodel.dart';
import 'package:skillcoin/res/color.dart';
import 'package:skillcoin/res/constants.dart';
import 'package:skillcoin/res/routes.dart';

import '../api/user_data.dart';
import '../customui/progressdialog.dart';
import '../customui/textfields.dart';


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
  String profileImgUrl="";
  var url="http://3.111.16.227:8010/files/upload/user_image/";
  bool isLoading=true;
  final controllerSKLAddress=TextEditingController();
  final contollerTRONAddress=TextEditingController();
  double _balance=0.0;
  ProfileDetailsModel? profile;

  @override
  void initState() {
    super.initState();
    _getWalletAPI();
  }

  void _getWalletAPI() {

    Map<dynamic,String>body={
      "email":userModel!.data.first.email,
      "token":userModel!.data.first.token
    };
    walletBalanceAPI(body).then((value){
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
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(

      appBar: AppBar(
        iconTheme:  IconThemeData(color: Colors.black.withOpacity(0.8)),
        title: const GoogleFontHeadingText(title: 'My Profile',fontSize: 15,
        color: PRIMARYBLACKCOLOR,),),
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
                GestureDetector(
                  onTap: (){
                      imagePickerDailog();
                  },
                  child: ContainerBgRCircle(
                    height: 120,
                    width: 120,
                    circular: 60,
                    color: Colors.white,
                    child: ClipRRect(
                        borderRadius: BorderRadius.circular(60),
                        child: imgFile==null?Image.network(
                          profileImgUrl,
                          fit: BoxFit.fitHeight,):Image.file(imgFile,fit: BoxFit.fitHeight,)),
                  ),
                ),
                // Padding(
                //   padding: const EdgeInsets.only(top:80.0,right: 128,bottom: 20),
                //   child: Align(alignment: Alignment.bottomRight,
                //       child: GestureDetector(
                //         onTap: (){
                //           imagePickerDailog();
                //         },
                //         child: const ContainerBgRCircle(
                //             height: 35,
                //             width: 35,
                //             color: Colors.black,
                //             circular: 40,
                //             child:  Icon(Icons.camera_alt_outlined,size: 22,color: PRIMARYWHITECOLOR,)
                //         ),
                //       )),
                // )
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
          child: TextFormField(
            style: const TextStyle(fontSize: 14.5),
            controller: controllerSKLAddress,
            textInputAction: TextInputAction.next,
            decoration: InputDecoration(
                suffixIcon: IconButton(icon: Icon(Ionicons.scan_sharp, color: PRIMARYBLACKCOLOR,), onPressed: () async {
                  String barcodeScanRes = await FlutterBarcodeScanner.scanBarcode(
                      '#ff6666', 'Cancel', true, ScanMode.QR);
                  String code = barcodeScanRes.substring(barcodeScanRes.indexOf(':')+1);
                  controllerSKLAddress.text = code;
                }),
                focusedBorder: OutlineInputBorder(
                  borderSide: const BorderSide(color: Color(0xffafaeae)),
                  borderRadius: BorderRadius.circular(9.0),
                ),
                border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(12),
                    borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
                ),
                errorBorder: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(12),
                    borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
                ),
                enabledBorder: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(12),
                    borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
                ),
                contentPadding: const EdgeInsets.only(top: 20,left: 15),
                hintText: "Address",
                labelText: "SkillCoin/BEP20 Address",
                labelStyle: TextStyle(color: Colors.grey[600],fontSize: 13.5),
                hintStyle: TextStyle(color: Colors.grey[600],fontSize: 13.5),
                filled: false,
                fillColor: Colors.white
            ),
          ),
        ),


        Padding(
          padding: const EdgeInsets.all(15.0),
          child: TextFormField(
            style: const TextStyle(fontSize: 14.5),
            controller: contollerTRONAddress,
            textInputAction: TextInputAction.next,
            decoration: InputDecoration(
                suffixIcon: IconButton(icon: Icon(Ionicons.scan_sharp, color: PRIMARYBLACKCOLOR,), onPressed: () async {
                  String barcodeScanRes = await FlutterBarcodeScanner.scanBarcode(
                      '#ff6666', 'Cancel', true, ScanMode.QR);
                  String code = barcodeScanRes.substring(barcodeScanRes.indexOf(':')+1);
                  controllerSKLAddress.text = code;
                }),
                focusedBorder: OutlineInputBorder(
                  borderSide: const BorderSide(color: Color(0xffafaeae)),
                  borderRadius: BorderRadius.circular(9.0),
                ),
                border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(12),
                    borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
                ),
                errorBorder: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(12),
                    borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
                ),
                enabledBorder: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(12),
                    borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
                ),
                contentPadding: const EdgeInsets.only(top: 20,left: 15),
                hintText: "Address",
                labelText: "TRON Address",
                labelStyle: TextStyle(color: Colors.grey[600],fontSize: 13.5),
                hintStyle: TextStyle(color: Colors.grey[600],fontSize: 13.5),
                filled: false,
                fillColor: Colors.white
            ),
          ),
        ),

        const SizedBox(height: 50,),
        SubmitButton(onPressed: (){
          if(_key.currentState!.validate()){
            _updateProfileAPI();
          //  saveProfile();
          }
        }, name: 'Save', colors: 0xfffbd536, 
          width: double.infinity, textColor: PRIMARYWHITECOLOR, circular: 10,)

      ],):const Center(
      child: CircularProgressIndicator(strokeWidth: 5,
        color: Colors.black,),
    ),
  ),
),
    );
  }

  void _updateProfileAPI() {
    showDialog(context: context, builder: (context) => ProgressDialog(), barrierDismissible: false).then((value){
      print(value);
    },);
    Map<dynamic,String>body={
      "userid":userModel!.data.first.userId,
      "name":nameController.text,
      "SkillCoinAddress":controllerSKLAddress.text,
      "TronAddress":contollerTRONAddress.text
    };

    updateProfileAPI(body).then((value){
      if(value.status){
        navPushOnBackPressed(context);
        Fluttertoast.showToast(msg: value.message.toString());
        _getWalletAPI();
      }else{
        navPushOnBackPressed(context);
        Fluttertoast.showToast(msg: value.message.toString());
      }
    });

  }
  void _openGallery(BuildContext ctx) async {
    var picture =
    await ImagePicker.platform.pickImage(source: ImageSource.gallery);
    setState(() {
      imgFile = File(picture!.path);
      uploadProfile(imgFile,userModel!.data.first.userId).then((value){
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
}
