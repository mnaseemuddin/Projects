

import 'dart:io';

import 'package:email_validator/email_validator.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:google_fonts/google_fonts.dart';

import '../../../CommonUI/apptextfield.dart';
import '../../../CommonUI/apptextview.dart';
import '../../../CommonUI/progressdialog.dart';
import '../../../Model/usermodel.dart';
import '../../../Resources/color.dart';
import '../../../Resources/constants.dart';
import '../../../Utils/dailog.dart';
import '../../../Utils/routes.dart';
import '../../../apis/apirepositary.dart';
import '../../../apis/userdata.dart';
import '../Core/developerconsoleactivity.dart';
import 'cregister.dart';



class CLoginActivity extends StatefulWidget {
  const CLoginActivity({Key? key}) : super(key: key);

  @override
  _CLoginActivityState createState() => _CLoginActivityState();
}

class _CLoginActivityState extends State<CLoginActivity> {

  final _key=GlobalKey<FormState>();
  bool obscureText=true;
  bool isEmailValidate(String input) => EmailValidator.validate(input);
  var emailController=TextEditingController();
  var passwordController=TextEditingController();

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: (){
        _onBackPressed();
        return Future.value(false);
      },
      child: Scaffold(
        body:
        Form(
          key: _key,
          child: SingleChildScrollView(
            scrollDirection: Axis.vertical,
            child: Column(
              children: [
                const SizedBox(height: 60,),
                Center(child: Image.asset('assets/development.gif',height: 170,)),
                const SizedBox(height: 50,),
                HeadingTextFW600(
                  title: 'Console Login!',
                  fontSize: 24, color: Colors.black,
                ),
                const SizedBox(height:10,),
                HeadingText1(
                  title:'Log in your existent console account of AppGallery',
//  textAlign: TextAlign.center,
                  fontSize: 14, color: Colors.grey[500],
                ),
                Padding(
                  padding: const EdgeInsets.fromLTRB(20.0, 40, 20, 20),
                  child: EmailTextField(
                    hintText: 'Email Address',
                    prefixIcon: 'assets/img.png',
                    controller: emailController,
                    validationMsg: "Please Enter Email Address .",
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.fromLTRB(20.0, 6, 20, 0),
                  child: passwordField(),
                ),
                Padding(
                  padding: const EdgeInsets.fromLTRB(0, 15, 25, 0),
                  child: Align(
                    alignment: Alignment.centerRight,
                    child: HeadingText1(
                      title: 'Forgot Password?',
                      color: appBlackColor,
                    ),
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.fromLTRB(80.0, 15, 80, 0),
                  child: SizedBox(
                    height: 50,
                    width: 150,
                    child: ElevatedButton(
                        onPressed: (){
                          keyboardDismissed(context);
                          Map<String,dynamic>body={
                            'email':emailController.text,
                            'password':passwordController.text
                          };
                          if(_key.currentState!.validate()){
                            if(isEmailValidate(emailController.text)){
                              if(passwordController.text.trim().length>=6){
                                showDialog(context: context, builder: (context) => ProgressDialog(),
                                    barrierDismissible: false).then((value){
                                  print(value);
                                },);
                                loginAPI(body).then((value){
                                  if(value.status){
                                    navReturn(context);
                                    ConsoleLoginModel user=value.data;
                                    setUser(user);
                                    navPushReplace(context,const DeveloperConsoleActivity());
                                  }else{
                                    navReturn(context);
                                    DialogUtils.instance.edgeAlerts(context, value.message.toString());
                                  }
                                });
                              }else{
                                DialogUtils.instance.edgeAlerts(context,
                                    'Password length must be greater than 6 .');
                              }
                            }else{
                              DialogUtils.instance.edgeAlerts(context,
                                  "Please Enter Valid Email Address .");
                            }
                          }

                        },
                        style: ButtonStyle(
                            overlayColor: MaterialStateProperty.all<Color>(const
                            Color(0xff134072)),
// shadowColor: MaterialStateProperty.all<Color>(Colors.amber),
                            animationDuration: const Duration(seconds: 2),
                            splashFactory: InkRipple.splashFactory,
                            backgroundColor:MaterialStateProperty.all<Color>(const Color(0xff0049A0)),
                            foregroundColor: MaterialStateProperty.all<Color>(Color(0xff0049A0)),
                            shape: MaterialStateProperty.all<RoundedRectangleBorder>(
                                RoundedRectangleBorder(
                                    borderRadius: BorderRadius.circular(55.0),
                                    side: const BorderSide(color: Color(0xff0049A0))))),
                        child: Text(
                          'Log in'.toUpperCase(),style: GoogleFonts.adamina(color:
                        Colors.white,fontSize: 12, fontWeight: FontWeight.w600),
                        )),
                  ),
                ),


                Padding(
                  padding: const EdgeInsets.only(bottom: 8.0,top: 30),
                  child: InkWell(
                    splashColor: appSecondaryColor,
                    onTap: (){
                      navPush(context, const CRegisterActivity());
                    },
                    child: SizedBox(
                      height: 44,
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: const [
                          Text("Don't have an account?",style: TextStyle(color: Color(0xff375C92),
                              fontWeight: FontWeight.w500),),
                          Text(" Register",style: TextStyle(color: Colors.red,fontWeight: FontWeight.w800),),
                        ],
                      ),
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }


  passwordField() =>TextFormField(
    controller: passwordController,
    obscureText: obscureText,
    validator: (v) {
      if (v!.trim().isEmpty) {
        return 'Please Enter Password .';
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
            borderRadius: BorderRadius.circular(25),
            borderSide: const BorderSide(width: 1,color: Color(0xffece7e7))
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
          color: Color(0xff84a2dc),),),
        enabledBorder: OutlineInputBorder(
            borderRadius: BorderRadius.circular(25),
            borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
        ),
        border: OutlineInputBorder(
            borderRadius: BorderRadius.circular(25),
            borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
        ),
        focusedBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(25),
          borderSide: const BorderSide(width: 1,
              color: Color(0xff84a2dc)),
        ),
        prefixIcon:Padding(
          padding: const EdgeInsets.all(15.0),
          child: Image.asset('assets/lock.png',height: 8,width: 8,color: Colors.blue,),
        ),
        contentPadding: const EdgeInsets.only(top: 20,left: 15),
        hintText: 'Password',
        hintStyle: TextStyle(color: Colors.grey[600],fontSize: 13.5),
        fillColor: Colors.white
    ),
  );
  void _onBackPressed() {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        // return object of type Dialog
        return AlertDialog(
          backgroundColor: Color(0xff262424),
          title: Text("Exit?",style: textStyleTitle(
              color: Colors.white,fontSize: 15.0)),
          content: Text("Are you sure you want to exit?",style: textStyleTitle(
              color: Colors.white,fontSize: 15.0)),
          actions: <Widget>[
            FlatButton(
              child: const Text(
                "CANCEL",
                style: TextStyle(
                  color: Colors.red,
                  fontFamily: 'Helvetica Neue',
                  fontSize: 14.899999618530273,
                ),
              ),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),

            FlatButton(
              child: const Text(
                "OK",
                style: TextStyle(
                  color: Colors.red,
                  fontFamily: 'Helvetica Neue',
                  fontSize: 14.899999618530273,
                ),
              ),
              onPressed: () {
                exit(0);
              },
            ),
            // usually buttons at the bottom of the dialog
          ],
        );
      },
    );
  }
}
