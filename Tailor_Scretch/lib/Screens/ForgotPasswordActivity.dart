


import 'package:email_validator/email_validator.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:tailor/ApiRepositary.dart';
import 'package:tailor/Auth/auth.dart';

class ForgotPassword extends StatelessWidget {
   ForgotPassword({Key? key}) : super(key: key);

   var editControllerEmailId=TextEditingController();
   var _key=GlobalKey<FormState>();
   bool isEmail(String input) => EmailValidator.validate(input);
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.black,
        title: Text('Forgot Password',style: TextStyle(fontSize: 15,color: Colors.white),),
      ),
      body: Form(
        key: _key,
        child: Container(
          height: double.infinity,
          child: Column(
            children: [
              Padding(padding: EdgeInsets.all(20)),
            Center(child: Padding(
              padding: const EdgeInsets.all(8.0),
              child: Text("Don't worry , if you forgot your password.",style:
              TextStyle(color: Colors.black,fontSize: 16),),
            )),

            Center(child: Padding(
              padding: const EdgeInsets.only(left:15.0,top: 8),
              child: Text("We just need your registered email, and we will send your passsword on that",textAlign: TextAlign.center,style:
              TextStyle(color: Colors.black,fontSize: 16),),
            )),

            Padding(
              padding: const EdgeInsets.only(
                  top: 38.0, right: 20, left: 20),
              child: Center(
                child: TextFormField(
                  controller: editControllerEmailId,
                  inputFormatters: [
                    FilteringTextInputFormatter.deny(RegExp('[ ]')),
                  ],
                  validator: (v) {
                    if (v!.isEmpty) {
                      return "Please Enter Email Address ..";
                    }
                    return null;
                  },
                  decoration: InputDecoration(
                    enabledBorder: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(25),
                        borderSide: BorderSide(
                            width: 1, color: Colors.black54)),
                    focusedBorder: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(25),
                        borderSide: BorderSide(
                            width: 1, color: Colors.black54)),
                    prefixIcon: Icon(
                      Icons.email_outlined,
                      color: Colors.black54,
                    ),
                    contentPadding:
                    EdgeInsets.only(left: 10, top: 10),
                    border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(5.0),
                        borderSide:
                        BorderSide(color: Colors.black54)),
                    hintText: 'Email Address',
                  ),
                  style: GoogleFonts.aBeeZee(
                    fontSize: 16,
                    color: const Color(0xff707070),
                    fontWeight: FontWeight.w300,
                  ),
                  textAlign: TextAlign.left,
                ),
              ),
            ),
            Padding(
              padding: const EdgeInsets.fromLTRB(30.0,15,30,15),
              child: SizedBox(
                height: 48,
                width: double.infinity,
                child: ElevatedButton(
                    style: ButtonStyle(
                        backgroundColor:MaterialStateProperty.all<Color>(Colors.black),
                        foregroundColor: MaterialStateProperty.all<Color>(Colors.black),
                        shape: MaterialStateProperty.all<RoundedRectangleBorder>(
                            RoundedRectangleBorder(
                                borderRadius: BorderRadius.circular(25.0),
                                side: BorderSide(color: Color(0xff0049A0))))),
                    onPressed: (){
                      if(editControllerEmailId.text.trim().isNotEmpty){
                        if(isEmail(editControllerEmailId.text.trim())){
                          Map<String,String> map={
                            'email':editControllerEmailId.text.trim().toLowerCase()
                          };
                          forgotPasswordAPI(map).then((value){
                            if(value.status){
                              Fluttertoast.showToast(msg: value.message!);
                              Get.offAll(SignIn());
                            }else{
                              Fluttertoast.showToast(msg: value.message!);
                            }
                          });
                        }else{
                          Fluttertoast.showToast(msg: "Please Enter Valid Email Address ");
                        }
                      }else{
                        Fluttertoast.showToast(msg: "Please Enter Email Address ");
                      }

                }, child: Text('Submit',style: TextStyle(fontSize: 15,color: Colors.white),)),
              ),
            ),

          ],),
        ),
      ),
    );
  }
}
