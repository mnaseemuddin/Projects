
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:skillcoin/customui/button.dart';
import 'package:skillcoin/res/routes.dart';
import 'package:skillcoin/screens/auth/verification.dart';

import '../../api/apirepositary.dart';
import '../../customui/textfields.dart';
import '../../customui/textview.dart';
import '../../res/color.dart';
import 'fverification.dart';

class ForgotPasswordActivity extends StatefulWidget {
  const ForgotPasswordActivity({Key? key}) : super(key: key);

  @override
  _ForgotPasswordActivityState createState() => _ForgotPasswordActivityState();
}

class _ForgotPasswordActivityState extends State<ForgotPasswordActivity> {

  var controller=TextEditingController();

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      top: true,
      child: Scaffold(
        body: Column(children: [
          Align(
            alignment: Alignment.centerRight,
            child: IconButton(onPressed: (){
              navPushOnBackPressed(context);
            }, icon: const Icon(Icons.cancel,color: Colors.grey,)),
          ),

          const Padding(
            padding: EdgeInsets.only(top:35.0,left: 18),
            child: Align(
              alignment: Alignment.centerLeft,
              child: NormalHeadingText(title: "Reset Your Password", fontSize: 25,
                  color: PRIMARYBLACKCOLOR),
            ),
          ),

          Padding(
            padding: const EdgeInsets.only(top:28.0,left: 0,right: 0),
            child: EmailTextField( hintText: 'Email Address', controller: controller,
                validationMsg: "Please enter Email Address"),
          ),


          Padding(
            padding: const EdgeInsets.only(top:28.0,left: 8,right: 8),
            child: SubmitButton(onPressed: (){
              keyboardDismissed(context);
              if(controller.text.trim().isEmpty){
                return DialogUtils.instance.edgeAlerts(context,"Please Enter Your Email Address .");
              }
              _forgotPasswordAPI();

            }, name: 'Next',colors: 0xfffbd536,
                textColor: PRIMARYWHITECOLOR, width: double.infinity, circular: 10),
          )
        ],),
      ),
    );
  }

  void _forgotPasswordAPI() {
    Map<dynamic,String>body={
      "email":controller.text
    };

    forgotPasswordAPI(body).then((value){
      if(value.status){
        print("Successfully");
        Fluttertoast.showToast(msg: value.message.toString());
        navPush(context, FVerification(emailAddress: controller.text,),);
      }else{
        print("Error");
        Fluttertoast.showToast(msg: value.message.toString());
      }
    });
  }
}
