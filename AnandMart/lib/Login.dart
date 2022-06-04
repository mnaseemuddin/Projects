import 'dart:async';
import 'dart:convert';
import 'dart:math';

import 'package:anandmart/Support/RandomDigit.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_svg/svg.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:http/http.dart' as http;
import 'package:google_fonts/google_fonts.dart';
import 'package:progress_dialog/progress_dialog.dart';
// import 'package:sms_otp_auto_verify/sms_otp_auto_verify.dart';

import 'Activity/AboutUsActivity.dart';
import 'OTPVerficationActivtiy.dart';



class Login extends StatefulWidget {
  const Login({Key? key}) : super(key: key);

  @override
  _LoginState createState() => _LoginState();
}

class _LoginState extends State<Login> {

  var _key=new GlobalKey<FormState>();
  TextEditingController editMobileNo=new TextEditingController();
  late String verificationId="";
  late String OTP = "";
  late Map map;

  late ProgressDialog pr;

 // late FirebaseMessaging messaging;

  @override
  void initState() {
    super.initState();
  }

  
  @override
  Widget build(BuildContext context) {


        pr=new ProgressDialog(context,isDismissible: false);
    pr.style(
        message: 'Please wait...',
        borderRadius: 10.0,
        backgroundColor: Colors.white,
        progressWidget: Padding(
          padding: const EdgeInsets.all(8.0),
          child: CircularProgressIndicator(strokeWidth: 5.0,color: Colors.black,),
        ),
        elevation: 20.0,
        insetAnimCurve: Curves.easeInOut,
        progress: 0.0,
        maxProgress: 100.0,
        progressTextStyle: TextStyle(
            color: Colors.black, fontSize: 13.0, fontWeight: FontWeight.w400),
        messageTextStyle: TextStyle(
            color: Colors.black, fontSize: 19.0, fontWeight: FontWeight.w600)
    );
    return Scaffold(
      backgroundColor: const Color(0xffffffff),
      body: ListView(
        children: [
          Stack(
            children: <Widget>[
              Form(
                key: _key,
                child: Column(
                  children: [
                    Container(
                      height: 300,
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.only(
                          bottomRight: Radius.circular(40.0),
                          bottomLeft: Radius.circular(40.0),
                        ),
                        color: const Color(0xffffffff),
                        border: Border.all(width: 1.0, color: const Color(
                            0xfff1ecec)),
                        boxShadow: [
                          BoxShadow(
                            color: const Color(0x29000000),
                            offset: Offset(4, 3),
                            blurRadius: 6,
                          ),
                        ],
                      ),
                      child: Center(child: Image.asset('drawable/logo.png',height: 150,width: 150,))
                    ),
                    Align(
                      alignment: Alignment.centerLeft,
                      child: Padding(
                        padding: const EdgeInsets.only(left: 20.0,top: 20),
                        child: Text(
                          'Mobile Number',
                          style: GoogleFonts.aBeeZee(fontSize: 16.5,fontWeight: FontWeight.normal,
                          color:Colors.black87),
                          textAlign: TextAlign.left,
                        ),
                      ),
                    ),
                    Padding(
                      padding: const EdgeInsets.only(right:15.0,left: 15,top: 10,bottom: 15),
                      child: TextFormField(
                        inputFormatters: [
                          LengthLimitingTextInputFormatter(13),
                        //  FilteringTextInputFormatter.digitsOnly
                        ],
                        keyboardType: TextInputType.phone,
                        controller: editMobileNo,
                        validator: (v){
                          if(v!.isEmpty){
                            return "Please Enter Mobile Number .";
                          }
                          return null;
                        },
                        //controller: editPassword,
                        style: GoogleFonts.aBeeZee(color: Colors.black,fontSize: 16),
                        decoration: new InputDecoration(
                          enabledBorder: OutlineInputBorder(
                            borderRadius: BorderRadius.circular(9),
                            borderSide: BorderSide(
                              color: Color(0xfff4811e),
                              width: 0.70,
                            ),
                          ),
                          suffixIcon: IconButton(
                            icon: Icon(Icons.phone,color: Colors.black,), onPressed: () {
                          },
                          ),
                          contentPadding: const EdgeInsets.only(left: 15.0),
                          filled: true,
                          fillColor: Colors.white,
                          errorStyle: TextStyle(fontSize: 14.5,color: Color(0xfff4811e),
                              fontWeight: FontWeight.w600),
                          border: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(5.0),
                              borderSide: BorderSide(color: Color(0xfff4811e))
                          ),
                          focusedBorder: OutlineInputBorder(
                            borderSide: BorderSide(width:0.70,color: Color(0xfff4811e)),
                            borderRadius: BorderRadius.circular(10.0),
                          ),
                        ),
                      ),
                    ),

                    Padding(
                      padding: const EdgeInsets.only(top:28.0,right:28,left:28,bottom: 15),
                      child: GestureDetector(
                        onTap: (){
                          if(_key.currentState!.validate()){
                         //   setState(() {
                          //    pr.show();
                            //  Timer(Duration(seconds: 1),(){
                             //   pr.hide();
                             //   Get.to(Verification(MobileNo: editMobileNo.text));
                           //   });
                          //  });
                           pr.show();
                          loginOTP();
                          }
                          },
                        child: Container(
                          height: 45,
                          width: double.infinity,
                          decoration: BoxDecoration(
                            borderRadius: BorderRadius.circular(6.0),
                            color: const Color(0xfff4811e),
                            border: Border.all(width: 1.0, color: const Color(0xfff4811e)),
                          ),
                          child: Center(
                            child: Text(
                              'Login',
                              style: GoogleFonts.montserrat(
                                fontWeight: FontWeight.w600,
                                fontSize: 16,
                                color: const Color(0xfff7f7f7),
                              ),
                              textAlign: TextAlign.left,
                            ),
                          ),
                        ),
                      ),
                    ),
                    GestureDetector(
                      onTap: (){
                        Get.to(AboutUsActivity(title: "Policy",Urls:
                        "https://anandmart.co.in/policy.html",));
                      },
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Text('By logging in you agree to our ',style: GoogleFonts.aBeeZee(color:
                          Colors.black,fontSize: 14)),
                          Text("terms and conditions",style: GoogleFonts.aBeeZee(color: Colors.red,
                              fontSize: 14),)
                        ],
                      ),
                    )
                  ],
                ),
              ),

               ],
          ),
        ],
      ),
    );
  }

  void loginOTP() async{
   
  
    String OTPCode=RandomDigits.getInteger(6).toString();
    String mobileNo="+91${editMobileNo.text}";
    String url="http://sms.hitek-solutions.co.in/domestic/sendsms/bulksms_v2.php?apikey=QU1hcnQ6UFN3Z2pOeHQ&type=TEXT&sender=ANNDTR&entityId=1701164449629748884&templateId=&mobile=$mobileNo&message=Thank%20you%20for%20downloading%20Anand%20Mart%20.%20Your%20login%20OTP%20$OTPCode%20for%20login";
    final response=await http.get(Uri.parse(url));
    if(response.statusCode==200){
      if(response.body.contains("SUCCESS")){
         setState(() {
          pr.hide();
          Get.to(Verification(mobileNo: editMobileNo.text,OTPCode: OTPCode,));
        });
      }else{
        Fluttertoast.showToast(msg: "Something Is Wrong In OTP Server...");
      }
    }else{
       Fluttertoast.showToast(msg: "Something Is Wrong In OTP Server...");
    }
  }
  
}

const String _svg_7j38my =
    '<svg viewBox="298.0 12.0 20.0 20.0" ><path transform="translate(293.5, 7.5)" d="M 8.522222518920898 13.15555477142334 C 10.12222194671631 16.29999923706055 12.69999980926514 18.86666679382324 15.84444522857666 20.4777774810791 L 18.28888893127441 18.03333282470703 C 18.58889007568359 17.73333168029785 19.0333309173584 17.63333320617676 19.42222213745117 17.76666641235352 C 20.66666793823242 18.17777824401855 22.01111030578613 18.39999961853027 23.38888740539551 18.39999961853027 C 24 18.39999961853027 24.5 18.89999771118164 24.5 19.51111030578613 L 24.5 23.38888740539551 C 24.5 24 24 24.5 23.38888740539551 24.5 C 12.9555549621582 24.5 4.5 16.04444313049316 4.5 5.611111164093018 C 4.5 5 5 4.5 5.611111164093018 4.5 L 9.5 4.5 C 10.11111068725586 4.5 10.61111068725586 5 10.61111068725586 5.611111164093018 C 10.61111068725586 7 10.83333301544189 8.333333969116211 11.24444389343262 9.577777862548828 C 11.36666584014893 9.966666221618652 11.27777767181396 10.39999866485596 10.96666622161865 10.71111011505127 L 8.522221565246582 13.15555477142334 Z" fill="#000000" stroke="none" stroke-width="1" stroke-miterlimit="4" stroke-linecap="butt" /></svg>';
