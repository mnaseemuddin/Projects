
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:skillcoin/api/apirepositary.dart';
import 'package:skillcoin/api/user_data.dart';
import 'package:skillcoin/customui/button.dart';
import 'package:skillcoin/customui/textfields.dart';
import 'package:skillcoin/customui/textview.dart';
import 'package:skillcoin/model/usermodel.dart';
import 'package:skillcoin/res/color.dart';
import 'package:skillcoin/res/routes.dart';
import 'package:skillcoin/screens/auth/forgotpassword.dart';
import 'package:skillcoin/screens/auth/verification.dart';
import 'package:skillcoin/screens/dashboard.dart';
import 'package:skillcoin/screens/market_curreny_page.dart';

import '../../customui/progressdialog.dart';
import 'register.dart';

class Login extends StatefulWidget {
  const Login({Key? key}) : super(key: key);

  @override
  _LoginState createState() => _LoginState();
}

class _LoginState extends State<Login> {
  var controller=TextEditingController();

  var passwordController=TextEditingController();
  bool obscureText=true;
  bool validEmail(emailAddress){
    bool emailValid = RegExp(r"^[a-zA-Z0-9.a-zA-Z0-9.#$%&'*+-/=?^_`{|}~]+@[a-zA-Z0-9]+\.[a-zA-Z]+").hasMatch(emailAddress);
    return emailValid;
  }
  @override
  Widget build(BuildContext context) {
    return SafeArea(
      top: true,
      child: Scaffold(
      backgroundColor: PRIMARYWHITECOLOR,
        body: Column(children:  [

          Align(
            alignment: Alignment.centerRight,
            child: IconButton(onPressed: (){
              navPushOnBackPressed(context);
            }, icon: const Icon(Icons.cancel,color: Colors.grey,)),
          ),

          const Padding(
            padding: EdgeInsets.only(top:25.0,left: 18),
            child: Align(
              alignment: Alignment.centerLeft,
              child: NormalHeadingText(title: "SkillCoin Account Login", fontSize: 25,
                  color: PRIMARYBLACKCOLOR),
            ),
          ),


          const SizedBox(height: 10,),
          EmailTextField( hintText: 'Email Address', controller: controller,
              validationMsg: "Please enter Email Address"),

          passwordTextField(),

          Padding(
            padding: const EdgeInsets.only(top:28.0,left: 8,right: 8),
            child: SubmitButton(onPressed: (){
              keyboardDismissed(context);
              if(controller.text.trim().isEmpty){
               return DialogUtils.instance.edgeAlerts(context,"Please Enter Your Email Address ..");
              }

              if(!validEmail(controller.text)){
                return DialogUtils.instance.edgeAlerts(context,"Please Enter Valid Email Address ..");
              }

                if(passwordController.text.trim().isEmpty){
                  return DialogUtils.instance.edgeAlerts(context,"Please Enter Your Password ..");
                }
              showDialog(context: context, builder: (context) => ProgressDialog(), barrierDismissible: false).then((value){
                print(value);
              },);
                  Map<dynamic,String>body={
                    "email":controller.text,
                    "password":passwordController.text
                  };
                  loginAPI(body).then((value){
                    if(value.status){
                      navPushOnBackPressed(context);
                      if(value!=null){
                        UserModel user=value.data;
                        setUser(user);
                        navPush(context, const DashBoard());
                      }
                    }else{
                      navPushOnBackPressed(context);
                      if(value.data["isverified"]==0){
                        navPush(context, Verification(emailAddress: controller.text.trim(),));
                      }
                      Fluttertoast.showToast(msg:value.message.toString());
                    }
                  });
            //  navPush(context, const Verification());
            }, name: 'Log In',
                textColor: PRIMARYBLACKCOLOR, width: double.infinity,
            circular: 6.0,colors: 0xfffbd536),
          ),

          GestureDetector(
            onTap: (){
              navPush(context,const ForgotPasswordActivity());
            },
            child: const SizedBox(
              height: 50,
              child: Align(
                alignment: Alignment.centerLeft,
                child: Padding(
                  padding: EdgeInsets.only(left:15.0,top: 20),
                  child: NormalHeadingText(title: "Forgot password?", fontSize: 16, color: Color(
                      0xffc1982e)),
                ),
              ),
            ),
          ),

           GestureDetector(
             onTap: (){
               navPush(context,const Register());
             },
             child: SizedBox(
               height: 50,
               child: Align(
                alignment: Alignment.centerLeft,
                child: Padding(
                  padding: const EdgeInsets.only(left:15.0,top: 20),
                  child: GestureDetector(
                    onTap: (){
                      navPush(context, const Register());
                    },
                    child: const NormalHeadingText(title: "Register now", fontSize: 16, color: Color(
                        0xffc1982e)),
                  ),
                ),
          ),
             ),
           ),
          
        ],),
      ),
    );
  }

  passwordTextField() =>Padding(
    padding: const EdgeInsets.only(top:20.0,left: 18,right: 20),
    child: TextFormField(
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
              borderRadius: BorderRadius.circular(10),
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
            color: PRIMARYYELLOWCOLOR,),),
          enabledBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(10),
              borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
          ),
          border: OutlineInputBorder(
              borderRadius: BorderRadius.circular(10),
              borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
          ),
          focusedBorder: OutlineInputBorder(
            borderRadius: BorderRadius.circular(10),
            borderSide: const BorderSide(width: 1,
                color: Color(0xff84a2dc)),
          ),

          contentPadding: const EdgeInsets.only(top: 20,left: 15),
          hintText: 'Password',
          hintStyle: TextStyle(color: Colors.grey[600],fontSize: 13.5),
          filled: true,
          fillColor: Colors.grey[200]
      ),
    ),
  );
}
