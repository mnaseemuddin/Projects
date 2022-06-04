
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:skillcoin/api/apirepositary.dart';
import 'package:skillcoin/customui/button.dart';
import 'package:skillcoin/customui/textview.dart';

import '../../api/user_data.dart';
import '../../model/usermodel.dart';
import '../../res/color.dart';
import '../../res/routes.dart';
import '../dashboard.dart';

class Verification extends StatefulWidget {

 final String emailAddress;
  const Verification({Key? key,required this.emailAddress}) : super(key: key);

  @override
  _VerificationState createState() => _VerificationState();
}

class _VerificationState extends State<Verification> {
  final _controllerOTP=TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: PRIMARY_APP_COLOR,
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


          Padding(
            padding: const EdgeInsets.all(15.0),
            child: SubmitButton(onPressed: (){
              keyboardDismissed(context);
              if(_controllerOTP.text.isNotEmpty){
                Map<dynamic,String>body={
                  "otp":_controllerOTP.text
                };
                submitOTPAPI(body).then((value){
                  if(value.status){
                    if(value!=null){
                      UserModel user=value.data;
                      setUser(user);
                      navPush(context, const DashBoard());
                    }
                  }else{
                    DialogUtils.instance.edgeAlerts(context,value.message.toString());
                  }
                });
              }else{
                DialogUtils.instance.edgeAlerts(context,"Please Enter One Time Password ..");
              }

            }, name: 'Submit', colors: 0xfffbd536,
                textColor: PRIMARYWHITECOLOR, width: double.infinity, circular: 15),
          ),

           Padding(
            padding: const EdgeInsets.fromLTRB(25,12.0,15,0),
            child: GestureDetector(
              onTap: (){
                keyboardDismissed(context);
                _resendCodeAPI();
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
      controller: _controllerOTP,
      inputFormatters: [
        LengthLimitingTextInputFormatter(6),
      ],
      style: const TextStyle(color: Colors.black,fontSize: 15),
      decoration: InputDecoration(
          hintText: 'Email verification Code',
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
              borderSide: BorderSide(color: Colors.black, width: 1,))
      ),
    ),
  );

  void _resendCodeAPI() {
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
