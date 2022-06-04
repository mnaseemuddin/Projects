

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:skillcoin/screens/auth/login.dart';

import '../../api/apirepositary.dart';
import '../../api/user_data.dart';
import '../../customui/button.dart';
import '../../customui/textview.dart';
import '../../model/usermodel.dart';
import '../../res/color.dart';
import '../../res/routes.dart';
import '../dashboard.dart';

class FVerification extends StatefulWidget {
  final String emailAddress;
   const FVerification({Key? key,required this.emailAddress}) : super(key: key);

  @override
  _FVerificationState createState() => _FVerificationState();
}

class _FVerificationState extends State<FVerification> {
  var controllerOTP=TextEditingController();
  var controllerPassword=TextEditingController();
  bool obscureText=true;
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
        top: true,
        child: Column(children: [

          Align(
            alignment: Alignment.centerLeft,
            child: IconButton(onPressed: (){
              navPushOnBackPressed(context);
            }, icon: const Icon(Icons.arrow_back_ios_outlined,size: 20,color:PRIMARYBLACKCOLOR,)),
          ),

          const Padding(
            padding: EdgeInsets.only(left:15.0,top: 10),
            child: Align(
              alignment: Alignment.centerLeft,
              child: Text("Security verification",style: TextStyle(fontWeight: FontWeight.w800,color:
              PRIMARYBLACKCOLOR,fontSize: 25),),
            ),
          ),

          verificationCodeUI(),
          passwordTextField(),
          confirmPasswordTextField(),



          Padding(
            padding: const EdgeInsets.all(15.0),
            child: SubmitButton(onPressed: (){
              keyboardDismissed(context);
              if(controllerOTP.text.isEmpty){
                return DialogUtils.instance.edgeAlerts(context,"Please Enter Your OTP ..");
              }
              if(controllerPassword.text.isEmpty){
                return DialogUtils.instance.edgeAlerts(context,"Please Enter Your New Password ..");
              }

              if(controllerPassword.text.isEmpty){
                return DialogUtils.instance.edgeAlerts(context,"Please Enter Your Confirm Password ..");
              }


                Map<dynamic,String>body={
                  "otp":controllerOTP.text,
                  "password":controllerPassword.text
                };
                submitForgotWithOTPAPI(body).then((value){
                  if(value.status){
                    Fluttertoast.showToast(msg: value.message.toString());
                    navPushReplace(context,const Login());
                  }else{
                    DialogUtils.instance.edgeAlerts(context,value.message.toString());
                  }
                });




            }, name: 'Submit', colors: 0xfffbd536,
                textColor: PRIMARYWHITECOLOR, width: double.infinity, circular: 15),
          ),

          Padding(
            padding: const EdgeInsets.fromLTRB(25,12.0,15,0),
            child: GestureDetector(
              onTap: (){
                keyboardDismissed(context);
                resendCode();
              },
              child: const Align(
                alignment: Alignment.centerLeft,
                child: NormalHeadingText(title: "Resend verification code", fontSize: 15,
                    color: Color(0xffdec559)),
              ),
            ),
          ),

        ],),
      ),
    );
  }

  verificationCodeUI() =>Padding(
    padding: const EdgeInsets.only(top:20.0,left: 15,right: 15),
    child: TextFormField(
      keyboardType: TextInputType.number,
      controller: controllerOTP,
      inputFormatters: [
        LengthLimitingTextInputFormatter(6),
      ],
      style: const TextStyle(color: Colors.black,fontSize: 15),
      decoration: InputDecoration(
          hintText: 'Phone verification Code',
          hintStyle: const TextStyle(color: Colors.black),
          filled: true,
          fillColor: Colors.white10,
          border: InputBorder.none,
          enabledBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(12),
              borderSide: BorderSide(
                color: PRIMARYBLACKCOLOR.withOpacity(0.5), width: 1,)),
          focusedBorder:
          OutlineInputBorder(
              borderRadius: BorderRadius.circular(12),
              borderSide: const BorderSide(color: Colors.black, width: 1,))
      ),
    ),
  );
  passwordTextField() =>Padding(
    padding: const EdgeInsets.only(top:20.0,left: 18,right: 20),
    child: TextFormField(

      controller: controllerPassword,
      obscureText: obscureText,
      validator: (v) {
        if (v!.trim().isEmpty) {
          return 'Please Enter Your Password .';
        }
        return null;
      },
      textInputAction: TextInputAction.next,
      inputFormatters: [
        LengthLimitingTextInputFormatter(35),
        FilteringTextInputFormatter.deny(RegExp('[ ]')),
      ],
      decoration: InputDecoration(
          filled: true,
          fillColor: Colors.white10,
          errorBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(10),
              borderSide: const BorderSide(width: 1,color: Colors.black)
          ),
          suffixIcon:  IconButton(onPressed: (){
            setState(() {
              if(obscureText) {
                obscureText=false;
              } else {
                obscureText=true;
              }
            });
          },icon: obscureText?Icon(Icons.visibility,
            color: Colors.grey[600],):const Icon(Icons.visibility_off,
            color: PRIMARYYELLOWCOLOR,),),
          enabledBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(10),
              borderSide: const BorderSide(width: 1.0,color: Colors.black)
          ),
          border: OutlineInputBorder(
              borderRadius: BorderRadius.circular(10),
              borderSide: const BorderSide(width: 1.0,color: Colors.black)
          ),
          focusedBorder: OutlineInputBorder(
            borderRadius: BorderRadius.circular(10),
            borderSide: const BorderSide(width: 1,
                color: Colors.black),
          ),

          contentPadding: const EdgeInsets.only(top: 20,left: 15),
          hintText: 'New Password',
          hintStyle: TextStyle(color: Colors.grey[600],fontSize: 13.5),
      ),
    ),
  );
  confirmPasswordTextField() =>Padding(
    padding: const EdgeInsets.only(top:20.0,left: 18,right: 20),
    child: TextFormField(
      controller: controllerPassword,
      obscureText: obscureText,
      validator: (v) {
        if (v!.trim().isEmpty) {
          return 'Please Enter Your Confirm Password .';
        }
        return null;
      },
      textInputAction: TextInputAction.next,
      inputFormatters: [
        LengthLimitingTextInputFormatter(35),
        FilteringTextInputFormatter.deny(RegExp('[ ]')),
      ],
      decoration: InputDecoration(
          errorBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(10),
              borderSide: const BorderSide(width: 1,color: Colors.black)
          ),
          suffixIcon:  IconButton(onPressed: (){
            setState(() {
              if(obscureText) {
                obscureText=false;
              } else {
                obscureText=true;
              }
            });
          },icon: obscureText?Icon(Icons.visibility,
            color: Colors.grey[600],):const Icon(Icons.visibility_off,
            color: PRIMARYYELLOWCOLOR,),),
          enabledBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(10),
              borderSide: const BorderSide(width: 1.0,color: Colors.black)
          ),
          border: OutlineInputBorder(
              borderRadius: BorderRadius.circular(10),
              borderSide: const BorderSide(width: 1.0,color: Colors.black)
          ),
          focusedBorder: OutlineInputBorder(
            borderRadius: BorderRadius.circular(10),
            borderSide: const BorderSide(width: 1,
                color: Colors.black),
          ),

          contentPadding: const EdgeInsets.only(top: 20,left: 15),
          hintText: 'Confirm Password',
          hintStyle: TextStyle(color: Colors.grey[600],fontSize: 13.5),
        filled: true,
        fillColor: Colors.white10,
      ),
    ),
  );

  void resendCode() {
    Map<dynamic,String>body={
      "email":widget.emailAddress
    };
    resendCodeAPI(body).then((value){
      if(value.status){
        Fluttertoast.showToast(msg: value.message.toString());
      }else{
        DialogUtils.instance.edgeAlerts(context,value.message.toString());
      }
    });
  }
}
