

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:skillcoin/customui/container.dart';
import 'package:skillcoin/screens/auth/verification.dart';

import '../../api/apirepositary.dart';
import '../../customui/button.dart';
import '../../customui/progressdialog.dart';
import '../../customui/textfields.dart';
import '../../customui/textview.dart';
import '../../res/color.dart';
import '../../res/routes.dart';
import 'login.dart';


class Register extends StatefulWidget {
  const Register({Key? key}) : super(key: key);

  @override
  _RegisterState createState() => _RegisterState();
}

class _RegisterState extends State<Register> {
  var passwordController=TextEditingController();
  var nameController=TextEditingController();
  var emailIdController=TextEditingController();

  var obscureText=true;
  int emailTermCondition=-1;

  bool termCondition1=false,termCondition2=false;

  bool validEmail(emailAddress){
    bool emailValid = RegExp(r"^[a-zA-Z0-9.a-zA-Z0-9.#$%&'*+-/=?^_`{|}~]+@[a-zA-Z0-9]+\.[a-zA-Z]+").hasMatch(emailAddress);
    return emailValid;
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      top: true,
      child: Scaffold(
        resizeToAvoidBottomInset: false,
        backgroundColor: PRIMARYWHITECOLOR,
        body: SingleChildScrollView(
          scrollDirection: Axis.vertical,
          child: Column(children:  [

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
                child: NormalHeadingText(title: "Enter Account Details", fontSize: 25,
                    color: PRIMARYBLACKCOLOR),
              ),
            ),


            EmailTextField( hintText: 'Full Name', controller: nameController,
                validationMsg: "Please enter full name"),

            EmailTextField( hintText: 'Email Address', controller: emailIdController,
                validationMsg: "Please Enter Email Address"),

            passwordTextField(),

             Padding(
               padding: const EdgeInsets.only(top:28.0,left: 22,right: 10),
               child: GestureDetector(
                 onTap: (){
                   setState(() {
                     if(termCondition1==false) {
                        termCondition1 = true;
                     }else{
                       termCondition1=false;
                     }
                   });
                 },
                 child: Row(
                   children: [
                     Expanded(
                       flex: 7,
                       child: Padding(
                         padding: const EdgeInsets.all(1.0),
                         child: ContainerBgRCircle(width: 24, height: 24, color: termCondition1?
                         const Color(0xfffbd536):const Color(0xffc7c7c7),
                             circular: 15, child: const Center(child: Icon(Icons.check,
                               size: 15,color: PRIMARYWHITECOLOR,))),
                       ),
                     ),

                      const Expanded(
                        flex: 89,
                        child: Padding(
                         padding: EdgeInsets.only(left:15.0),
                         child: NormalHeadingText(title: 'I agree to receive email updates from SkillCoin',
                             fontSize: 15, color: Color(0xffc7c7c7)),
                     ),
                      ),

                   ],
                 ),
               ),
             ),

            Padding(
              padding: const EdgeInsets.only(top:28.0,left: 22,right: 10),
              child: GestureDetector(
                onTap: (){
                  setState(() {
                    if(termCondition2==false) {
                      termCondition2 = true;
                    }else{
                      termCondition2=false;
                    }
                  });
                },
                child: Row(
                  children:  [
                    Expanded(
                      flex: 7,
                      child: Padding(
                        padding: const EdgeInsets.all(1.0),
                        child: ContainerBgRCircle(width: 24, height: 24, color:
                        termCondition2?const Color(0xfffbd536):const Color(0xffc7c7c7),
                            circular: 15, child: const Center(child: Icon(Icons.check,
                              size: 15,color: PRIMARYWHITECOLOR,))),
                      ),
                    ),

                    const Expanded(
                      flex: 89,
                      child: Padding(
                        padding: EdgeInsets.only(left:15.0),
                        child: NormalHeadingText(title: 'I agree to share data for marketing purposes',
                            fontSize: 15, color: Color(0xffc7c7c7)),
                      ),
                    )

                  ],
                ),
              ),
            ),


            Padding(
              padding: const EdgeInsets.only(top:28.0,left: 8,right: 8),
              child: SubmitButton(onPressed: (){
                keyboardDismissed(context);
                if(nameController.text.isEmpty){
                 return DialogUtils.instance.edgeAlerts(context,"Please Enter Your Full Name ..");
                }
                  if(emailIdController.text.isEmpty){
                    return DialogUtils.instance.edgeAlerts(context,"Please Enter Your Email Address ..");
                  }
                if(!validEmail(emailIdController.text)){
                  return DialogUtils.instance.edgeAlerts(context,"Please Enter Valid Email Address ..");
                }
                    if(passwordController.text.isEmpty){
                      return DialogUtils.instance.edgeAlerts(context,"Please Enter Your Password ..");
                    }
                    if(!termCondition1){
                        return DialogUtils.instance.edgeAlerts(context,'Please Accept Term & Condition.');
                    }
                    if(!termCondition2){
                      return DialogUtils.instance.edgeAlerts(context,'Please Accept Term & Condition.');
                    }

                      Map<dynamic,String>body={
                        "name":nameController.text,
                        "email":emailIdController.text.toLowerCase(),
                        "password":passwordController.text
                      };
                      _signUpAPI(body);

              }, name: 'Next',
                textColor: PRIMARYBLACKCOLOR, width: double.infinity,
                circular: 6.0,colors: 0xfffbd536,),
            ),



          ],),
        ),
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

  void _signUpAPI(Map body) {
    showDialog(context: context, builder: (context) => const ProgressDialog(),
        barrierDismissible: false).then((value){print(value);
    },);
    signUpAPI(body).then((value){
      navPushOnBackPressed(context);
      if(value.status){
        navPush(context,Verification(emailAddress: emailIdController.text.trim(),
        ));
      }else{
        Fluttertoast.showToast(msg:value.message.toString());
        navPushReplace(context, const Login());
      }
    });

  }
}

