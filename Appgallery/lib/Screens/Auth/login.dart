
import 'dart:async';

import 'package:appgallery/CommonUI/apptextfield.dart';
import 'package:appgallery/CommonUI/apptextview.dart';
import 'package:appgallery/Controllers/logincontroller.dart';
import 'package:appgallery/Model/usermodel.dart';
import 'package:appgallery/Resources/color.dart';
import 'package:appgallery/Resources/constants.dart';
import 'package:appgallery/Screens/Auth/register.dart';
import 'package:appgallery/Screens/BNavigationFolder/bottommenuactivity.dart';
import 'package:appgallery/Utils/dailog.dart';
import 'package:appgallery/Utils/routes.dart';
import 'package:appgallery/apis/userdata.dart';
import 'package:email_validator/email_validator.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:get_storage/get_storage.dart';
import 'package:google_fonts/google_fonts.dart';
import '../../CommonUI/progressdialog.dart';
import '../../apis/apirepositary.dart';

class Login extends StatefulWidget {
//  final String loginType;
  const Login({Key? key}) : super(key: key);

  @override
  State<Login> createState() => _LoginState();
}

class _LoginState extends State<Login> {

  var emailController=TextEditingController();
  var passwordController=TextEditingController();
  final prefs=GetStorage();
  bool obscureText=true;
  final _key=GlobalKey<FormState>();
  bool isEmailValidate(String input) => EmailValidator.validate(input);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      resizeToAvoidBottomInset: false,
      backgroundColor:appWhiteColor,
      body: Form(
        key: _key,
        child: Stack(
          children: [
            SizedBox(height: double.infinity,
            child: Image.asset(assetslbackgroundImg,fit: BoxFit.fitHeight),),
            Column(
              children: [
               // Padding(padding: EdgeInsets.only(top: 200),),
                Expanded(
                    flex: 25,
                    child:Container()),
                Expanded(
                    flex: 70,
                    child: Column(children: [
                  HeadingTextFW600(
                   title: 'Welcome back!',
                        fontSize: 24, color: Colors.black,
                  ),
                  HeadingText1(
                    title:'Log in your existent account of AppGallery',
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
                        child: passwordField()
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
                                  if(isEmailValidate(emailController.text.trim())){
                                    if(passwordController.text.trim().length>6){
                                      showDialog(context: context, builder: (context) => ProgressDialog(),
                                          barrierDismissible: false).then((value)=>null,);
                                      userloginAPI(body).then((value){
                                        if(value.status){
                                          navReturn(context);
                                          UserLoginModel user=value.data;
                                          setUserLoginDetails(user);
                                          navPushReplace(context, const BottomMenuActivity());
                                        }else{
                                          navReturn(context);
                                          DialogUtils.instance.edgeAlerts(context,
                                              value.message.toString());

                                        }
                                      });
                                    }else{
                                      DialogUtils.instance.edgeAlerts(context,
                                          'Password length must be greater than 6');
                                    }
                                  }else{
                                    DialogUtils.instance.edgeAlerts(context,
                                        'Please Enter Valid Email Address ');
                                  }
                                }
                              },
                              style: ButtonStyle(
                                overlayColor: MaterialStateProperty.all<Color>(const
                                Color(0xff134072)),
                               // shadowColor: MaterialStateProperty.all<Color>(Colors.amber),
                                animationDuration: const Duration(seconds: 2),
                                  splashFactory: InkRipple.splashFactory,
                                  backgroundColor:MaterialStateProperty.all<Color>(Color(0xff0049A0)),
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
                        padding: const EdgeInsets.fromLTRB(8.0,20,0,0),
                        child: HeadingText1(title: 'Or connect using',
                          color: Colors.grey[500],),
                      ),

                      Row(children: [
                        Expanded(
                          flex: 1,
                          child: Padding(
                            padding: const EdgeInsets.only(left:45.0,top: 15),
                            child: SizedBox(
                              height: 45,
                              child: ElevatedButton.icon(
                                onPressed: () {

                                },
                                style: ButtonStyle(
                                    overlayColor: MaterialStateProperty.all<Color>(const
                                    Color(0xff134072)),
                                    backgroundColor:MaterialStateProperty.all<Color>(Color(0xff375C92)),
                                    foregroundColor: MaterialStateProperty.all<Color>(Color(0xff375C92)),
                                    shape: MaterialStateProperty.all<RoundedRectangleBorder>(
                                        RoundedRectangleBorder(
                                            borderRadius: BorderRadius.circular(10.0),
                                            side: const BorderSide(color:Color(0xff375C92))))),
                                icon: Image.asset("assets/facebook.png",color: appWhiteColor,height: 15),
                                label: HeadingText1(title:'Facebook',color: appWhiteColor,fontSize: 14),
                              ),
                            ),
                          ),
                        ),

                        Expanded(
                          flex: 1,
                          child: Padding(
                            padding: const EdgeInsets.only(right:45.0,left: 5,top: 15),
                            child: SizedBox(
                              height: 45,
                              child: ElevatedButton.icon(
                                onPressed: () {

                                },
                                style: ButtonStyle(
                                    overlayColor: MaterialStateProperty.all<Color>(
                                        Colors.red.shade700),
                                    splashFactory: InkRipple.splashFactory,
                                    backgroundColor:MaterialStateProperty.all<Color>(Colors.red),
                                    foregroundColor: MaterialStateProperty.all<Color>(Colors.red),
                                    shape: MaterialStateProperty.all<RoundedRectangleBorder>(
                                        RoundedRectangleBorder(
                                            borderRadius: BorderRadius.circular(10.0),
                                            side: BorderSide(color:Colors.red)))),
                                icon: Image.asset("assets/google.png",color: appWhiteColor,height: 15),
                                label: HeadingText1(title:'Google',color: appWhiteColor,fontSize: 14),
                              ),
                            ),
                          ),
                        ),
                      ],),
                      const Spacer(),
                      Padding(
                        padding: const EdgeInsets.only(bottom: 8.0,top: 30),
                        child: InkWell(
                          splashColor: appSecondaryColor,
                          onTap: (){
                            navPush(context,const Register());
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
                      Expanded(
                          flex: 10,
                          child: Container())
                ],)),
              ],
            ),
          ],
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
}
