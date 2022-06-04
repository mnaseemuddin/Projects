
import 'package:appgallery/CommonUI/apptextfield.dart';
import 'package:appgallery/CommonUI/commonwidget.dart';
import 'package:appgallery/Controllers/registercontroller.dart';
import 'package:appgallery/Resources/color.dart';
import 'package:appgallery/Resources/constants.dart';
import 'package:appgallery/Utils/dailog.dart';
import 'package:appgallery/Utils/routes.dart';
import 'package:email_validator/email_validator.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import '../../apis/apirepositary.dart';
import 'login.dart';

class Register extends StatefulWidget {

  const Register({Key? key}) : super(key: key);

  @override
  State<Register> createState() => _RegisterState();
}

class _RegisterState extends State<Register> {

  final RegisterController _registerController=Get.put(RegisterController());
  TextEditingController nameController=TextEditingController();
      TextEditingController emailController=TextEditingController();
      TextEditingController mobileNoController=TextEditingController();
     TextEditingController passwordController=TextEditingController();
  bool isEmailValidate(String input) => EmailValidator.validate(input);
  bool obscureText=true;
  final _key=GlobalKey<FormState>();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      resizeToAvoidBottomInset: false,
      backgroundColor: appGreyColor,
      body: Form(
        key: _key,
        child: Stack(
          children: [
            SizedBox(height: double.infinity,
              child: Image.asset(assetslbackgroundImg,fit: BoxFit.fitHeight),),
            ListView(
              children: [
                const Padding(padding: EdgeInsets.only(top: 10)),
                 Align(
                    alignment:Alignment.centerLeft,
                    child: Padding(
                      padding: const EdgeInsets.fromLTRB(20.0,10,0,0),
                      child: IconButton(icon: const Icon(Icons.arrow_back_ios,size: 20,color: appBlackColor),
                        onPressed: () {
                        Navigator.of(context).pop();
                        },),
                    )),
                const Padding(padding: EdgeInsets.only(top: 80)),
                const Align(
                  alignment: Alignment.center,
                  child: Text(
                    "Let's Get Started!",
                    textAlign: TextAlign.center,
                    style: TextStyle(
                        color: appBlackColor,
                        fontSize: 20,
                        fontWeight: FontWeight.w700),
                  ),
                ),
                const Padding(
                  padding: EdgeInsets.all(6.0),
                  child: Text(
                    'Create an account to AppGallery to get all features',
                    textAlign: TextAlign.center,
                    style: TextStyle(color: Colors.black54),
                  ),
                ),
                const Padding(
                  padding: EdgeInsets.only(top: 35),
                ),
                 Padding(
                  padding: const EdgeInsets.only(left: 24.0, right: 24.0),
                  child: NameTextField(
                    controller: nameController,
                    prefixIcon: 'assets/img.png',
                    hintText: "Full Name",
                    validationMsg: "Please Enter Name .",
                  ),
                ),
                const Padding(
                  padding: EdgeInsets.only(top: 25),
                ),
                 Padding(
                  padding: const EdgeInsets.only(left: 24.0, right: 24.0),
                  child: EmailTextField(
                    controller: emailController,
                    prefixIcon: 'assets/mail.png',
                    hintText: "Email Address",
                    validationMsg: "Please Enter Email Address .",
                  ),
                ),

                const Padding(
                  padding: EdgeInsets.only(top: 25),
                ),
                 Padding(
                  padding: const EdgeInsets.only(left: 24.0, right: 24.0),
                  child: NumberTextField(
                    controller: mobileNoController,
                    hintText: "Mobile No",
                    validationMsg: "Please Enter MobileNo .",
                    prefixIcon: 'assets/phone.png',
                  ),
                ),

                 Padding(
                   padding: const EdgeInsets.only(left: 24.0, right: 24.0,top: 25),
                 child: passwordField(),),

                const Padding(
                  padding: EdgeInsets.only(top: 30),
                ),
                SubmitButton(
                  onPressed: () {
                    Map<String,dynamic>body={
                      'name':nameController.text,
                      'email':emailController.text,
                      'mobile':mobileNoController.text,
                      'password':passwordController.text
                    };
                    if(_key.currentState!.validate()){
                      if(isEmailValidate(emailController.text.trim())){
                        if(passwordController.text.trim().length>6){
                          usersignUpAPI(body).then((value) {
                            if (value.status) {
                              Fluttertoast.showToast(msg: value.message.toString());
                              Get.offAll(const Login());
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
                  buttonName: 'Create',
                ),
                const Spacer(),
                Padding(
                  padding: const EdgeInsets.only(top:15.0),
                  child: GestureDetector(
                    onTap: (){
                      Get.offAll(const Login());
                    },
                    child: SizedBox(
                      height: 50,
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: const [
                          Text('Already have in account?',
                              style: TextStyle(
                                  color: Color(0xff9da0a1), fontWeight: FontWeight.w500)),
                          Text(
                            " Login",
                            style: TextStyle(
                                color: Color(0xff0049A0), fontWeight: FontWeight.w800),
                          ),
                        ],
                      ),
                    ),
                  ),
                )
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
        filled: false,
        fillColor: Colors.white,
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
    ),
  );
}
