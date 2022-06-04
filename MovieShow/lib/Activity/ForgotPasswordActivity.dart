



import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:http/http.dart' as http;
import 'package:movieshow/ApplicationConfigration/ApiUrls.dart';
import 'package:movieshow/Authentication/SignInActivity.dart';
import 'package:movieshow/Authentication/SignUpActivity.dart';

class ForgotPasswordActivity extends StatefulWidget {
  const ForgotPasswordActivity({ Key? key }) : super(key: key);

  @override
  _ForgotPasswordActivityState createState() => _ForgotPasswordActivityState();
}

class _ForgotPasswordActivityState extends State<ForgotPasswordActivity> {
  var editMobileEmail=new TextEditingController();
  var _key=new GlobalKey<FormState>();
  late Map map;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Color(0xff000000),
      body: Form(
        key: _key,
        child: ListView(
          physics:NeverScrollableScrollPhysics(),
          children: [
           Container(
             height: MediaQuery.of(context).size.height*.70,
             child: Column(
               children: [
 Center(child: Padding(
             padding: const EdgeInsets.only(top:80.0),
             child: Image.asset("drawable/logo.png",height: 120,),
           )),
            Center(child: Padding(
              padding: const EdgeInsets.all(8.0),
              child: Text("Don't worry , if you forgot your password.",style:
              TextStyle(color: Colors.white,fontSize: 16),),
            )),

            Center(child: Padding(
              padding: const EdgeInsets.only(left:15.0,top: 8),
              child: Text("We just need your registered email or mobile, and we will send your passsword on that",textAlign: TextAlign.center,style:
              TextStyle(color: Colors.white,fontSize: 16),),
            )),

            Padding(
              padding: const EdgeInsets.all(15.0),
              child: TextFormField(
                                        textInputAction: TextInputAction.next,
                                        validator: (v){
                                          if(v!.isEmpty){
                                            return "Please Enter Mobile Number or Email .";
                                          }
                                          return null;
                                        },

                                        controller: editMobileEmail,
                                       // focusNode: editMobileEmailFocus,
                                        style: TextStyle(color: Colors.white),
                                        decoration: new InputDecoration(
                                          border: OutlineInputBorder(
                                            borderRadius: BorderRadius.circular(5.0),
                                           borderSide: BorderSide(color: Color(0xff1f3349))
                                          ),

                          focusedBorder: OutlineInputBorder(
                            borderSide: BorderSide(color: Color(0xff1f3349)),
                            borderRadius: BorderRadius.circular(10.0),
                          ),
                                          filled: true,
                                          fillColor: Color(0xff1f3349),
                                          errorStyle: TextStyle(fontSize: 14.5,color: Colors.redAccent,fontWeight: FontWeight.w600),
                                          focusColor: Colors.white,
                                          prefixIcon: Icon(Icons.phone_android_rounded,color: Color(0xffffffff),),
                                          hintText: "Mobile or Email",
                                          hintStyle: TextStyle(
                                            fontFamily: 'Helvetica Neue',
                                            fontSize: 15,
                                            color: Color(0xffffffff),
                                            fontWeight: FontWeight.w500,
                                          ),
                                        ),
                                      ),
            ),
            Padding(
              padding: const EdgeInsets.all(20.0),
              child: GestureDetector(
                onTap: (){
                  if(_key.currentState!.validate()) {
                    forgotPasswordAPI();
                  }
                },
                child: Container(
                                            width: double.infinity,
                                            height: 50,
                                            decoration: BoxDecoration(
                                              borderRadius: BorderRadius.circular(6.0),
                                              color: const Color(0xffe61a1a),
                                              border: Border.all(width: 1.0, color: const Color(0xff707070)),
                                            ),
                                            child:  Center(
                                              child: Text(
                                                'Submit',
                                                style: TextStyle(
                                                  fontFamily: 'Helvetica Neue',
                                                  fontSize: 15,
                                                  color: const Color(0xffffffff),
                                                  fontWeight: FontWeight.w700,
                                                ),
                                                textAlign: TextAlign.center,
                                              ),
                                            ),
                                          ),
              ),
            ),

               ],
             ),
           ),
           Container(
             height: MediaQuery.of(context).size.height*.30,
             child: Row(
                                  mainAxisAlignment: MainAxisAlignment.center,
                                  children: [
                                    Text(
                                      'Don’t have an account ? ',
                                      style: TextStyle(
                                        fontFamily: 'Helvetica Neue',
                                        fontSize: 11.899999618530273,
                                        color: const Color(0xffffffff),
                                        fontWeight: FontWeight.w700,
                                      ),
                                      textAlign: TextAlign.right,
                                    ),
                                    Padding(
                                      padding: const EdgeInsets.only(left:4.0),
                                      child: GestureDetector(
                                        onTap: (){
                                          Get.to(SignUp());
                                        },
                                        child: Text(
                                          'Sign Up',
                                          style: TextStyle(
                                            fontFamily: 'Helvetica Neue',
                                            fontSize: 11.899999618530273,
                                            color: const Color(0xffe41e18),
                                            fontWeight: FontWeight.w700,
                                          ),
                                          textAlign: TextAlign.right,
                                        ),
                                      ),
                                    ),
                                  ],
                                ),
           )
          ],
        ),
      ),
    );
  }
  forgotPasswordAPI()async{
    
    map={
      "email_or_mob":editMobileEmail.text,
    };
    var Urls=Uri.parse(APiUrls.ForgotPassword);
    var response=await http.post(Urls,body: map);
    if(response.statusCode==200){
      map=json.decode(response.body);
      setState(() {
        if(map["status"]=="1")
        Fluttertoast.showToast(msg: map["message"],gravity: ToastGravity.BOTTOM);
        Get.offAll(SignIn());
      });
    }
  }
}