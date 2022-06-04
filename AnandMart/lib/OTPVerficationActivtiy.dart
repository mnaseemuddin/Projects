
import 'dart:async';
import 'dart:convert';
import 'dart:io';
import 'dart:math';
import 'package:anandmart/Activity/DashBoard.dart';
import 'package:anandmart/Support/SharePreferenceManager.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:http/http.dart' as http;
//import 'package:firebase_auth/firebase_auth.dart';
// import 'package:sms_retriever/sms_retriever.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:get/get.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:otp_count_down/otp_count_down.dart';
import 'package:pin_code_fields/pin_code_fields.dart';
import 'package:platform_device_id/platform_device_id.dart';
import 'package:progress_dialog/progress_dialog.dart';
// import 'package:sms_autofill/sms_autofill.dart';
// import 'package:sms_otp_auto_verify/sms_otp_auto_verify.dart';
import 'Activity/SelectCityAndCityAreaActivity.dart';
import 'ApplicationConfigration/ApiUrls.dart';
import 'Support/AlertDialogManager.dart';
import 'Support/RandomDigit.dart';
//import 'package:otp_count_down/otp_count_down.dart';

class Verification extends StatefulWidget {

  final String mobileNo,OTPCode;
  // ignore: non_constant_identifier_names
  const Verification({Key? key,required this.mobileNo,required this.OTPCode}) : super(key: key);

  @override
  _VerificationState createState() => _VerificationState();
}

class _VerificationState extends State<Verification> {
  late String timer = "";
  final int _timesMillseconds = 1500 * 5 * 6;
  bool IsVisibilityResendOTP = false,
      IsVisiblityOTPTimer = true;

  late OTPCountDown _otpCountDown;
  late String mobileNo;

  TextEditingController editOTP = new TextEditingController();
  //late FirebaseAuth _auth=FirebaseAuth.instance;
  late Map map;

  String? deviceId;
  bool _isLoadingButton = false;
  bool _enableButton = false;
  String _otpCode = "";
  final _scaffoldKey = GlobalKey<ScaffoldState>();
  var _key=new GlobalKey<FormState>();
  TextEditingController editCoupon=new TextEditingController();
  String refferalCode="";
  late ProgressDialog pr;

  late String VerificationId="";

  String OTPCode='';

  @override
  initState()  {
    setState(() {
       OTPCode= widget.OTPCode;
       print(OTPCode);
    });
    _otpCountDown = OTPCountDown.startOTPTimer(
        timeInMS: _timesMillseconds, currentCountDown: (String st) {
      setState(() {
        timer = st;
        if (timer == "00:00") {
          IsVisiblityOTPTimer = false;
          IsVisibilityResendOTP = true;
        }
      });
    }, onFinish: () {});
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

      body: Stack(
        children: [
          ListView(
            children: [
              Container(
                width: double.infinity,
                height: MediaQuery
                    .of(context)
                    .size
                    .height * 0.25,
                color: Color(0xfff4811e),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: Text("Verify Mobile", style: GoogleFonts.aBeeZee(
                          fontSize: 18, color: Colors.white),),
                    ),
                    Text("Please enter 6 digit number", style: GoogleFonts.aBeeZee(
                        fontSize: 13, color: Colors.white),)
                  ],
                ),
              ),
              Container(
                height: MediaQuery
                    .of(context)
                    .size
                    .height * 0.75,
                color: Colors.white,
                child: ListView(
                  children: [
                    Padding(
                      padding: const EdgeInsets.only(
                          left: 20.0, right: 20.0, top: 50),
                      child: Container(
                        height: 60,
                        // child: TextFieldPin(
                        //   codeLength: _otpCodeLength,
                        //   filledColor: Colors.white,
                        //   filled: true,
                        //   boxSize: 40,
                        //   filledAfterTextChange: false,
                        //   textStyle: TextStyle(fontSize: 16),
                        //   onOtpCallback: (code, isAutofill) =>
                        //       _onOtpCallBack(code, isAutofill),
                        // ),
                        child: PinCodeTextField(
                         controller: editOTP,
                          hintCharacter: "0",
                          textStyle: TextStyle(fontSize: 14),
                          backgroundColor: Colors.white,
                          length: 6,
                          keyboardType: TextInputType.number,
                          pastedTextStyle: TextStyle(
                            color: Colors.grey,
                            fontWeight: FontWeight.bold,
                          ),
                          pinTheme: PinTheme(
                            inactiveColor: Colors.grey[200],
                            shape: PinCodeFieldShape.circle,
                            borderRadius: BorderRadius.circular(15),
                            fieldHeight: 50,
                            fieldWidth: 52,
                            activeFillColor: Colors.white,
                          ),
                          appContext: context, onChanged: (String value) {  },
                        ),
                      ),
                    ),

                    Padding(
                      padding: const EdgeInsets.only(
                          top: 28.0, left: 45, right: 45),
                      child: GestureDetector(
                        onTap: (){
                          print(OTPCode);
                          if(editOTP.text.isNotEmpty) {
                             if(OTPCode==editOTP.text){
                                OTPVerification();
                                }else{
                              Fluttertoast.showToast(msg: "Please Enter Vaild OTP ..");
                                }
                          }else{
                          Fluttertoast.showToast(msg: "Please enter OTP ..",gravity:
                          ToastGravity.BOTTOM);
                          }
                        },
                        child: Container(
                          height: 45,
                          width: double.infinity,
                          decoration: BoxDecoration(
                            borderRadius: BorderRadius.circular(6.0),
                            color: const Color(0xfff4811e),
                            border: Border.all(width: 1.0, color: const Color(
                                0xfff4811e)),
                          ),
                          child: Center(
                            child: Text(
                              'Verify',
                              style: GoogleFonts.aBeeZee(
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
                    Visibility(
                      visible: IsVisiblityOTPTimer,
                      child: Container(
                        height: 60,
                        child: Padding(
                          padding: const EdgeInsets.only(
                              left: 35.0, right: 35, top: 10),
                          child: Text(
                            "Didn't receive the OTP? Request for a new one in $timer sec",
                            textAlign: TextAlign.center,
                            style: TextStyle(
                              fontFamily: 'Helvetica Neue',
                              fontSize: 15,
                              color: Colors.black,
                              fontWeight: FontWeight.w500,
                            ),),
                        ),
                      ),
                    ),
                    Visibility(
                      visible: IsVisibilityResendOTP,
                      child: GestureDetector(
                        onTap: () {
                          setState(() {
                            IsVisibilityResendOTP = false;
                            IsVisiblityOTPTimer = true;
                             resendOTP(widget.mobileNo);
                          });
                        },
                        child: Center(child: Padding(
                          padding: const EdgeInsets.only(top: 20.0),
                          child: Text("Resend Code", style: GoogleFonts.aBeeZee(
                              fontSize: 15, color: Colors.black),),
                        )),
                      ),
                    )
                  ],
                ),
              )
            ],
          ),
        ],
      ),
    );
  }

  // ignore: non_constant_identifier_names
//   void VerifyOTP(String smsCode) async {

   

// //     try{
// //  final AuthCredential credential = PhoneAuthProvider.credential(
// //         verificationId: VerificationId,
// //         smsCode: smsCode);
// //     final User? user = (await _auth.signInWithCredential(credential)).user;
// //     OTPVerification();
// //     }catch(e){
// //       pr.hide();
// //     AlertDialogManager().IsErrorAlertDialog(context, "Status", e.toString(), "");
// //     }
   
//     // SharePreferenceManager.instance.setSharePreferenceInitialized("Initilized", widget.Initilized);
//     // SharePreferenceManager.instance.setUserID("UserID",widget.UserID);
//     // Get.offAll(DashBoard());

//   }
  // ignore: non_constant_identifier_names
  void OTPVerification() async{
    map={
      "user_uid":widget.mobileNo
    };

    try{
      var Apiurls=Uri.parse(ApiUrls.Login);
      var response=await http.post(Apiurls,body: map);
      if(response.statusCode==200){
        map=json.decode(response.body);
         if(map["status"]=="1"){
           deviceId=await PlatformDeviceId.getDeviceId;
          setState(() {
            pr.hide();
 if(map["refer_code_status"]=="0"){
        showDialog(
      context: context,
      barrierDismissible: false,
      builder: (BuildContext context) {
        // return object of type Dialog
        return AlertDialog(
         title: Text("Refferal"),
          content: new Text("Do You have someone refferal code ?"),
          actions: <Widget>[
            new TextButton(
              child: new Text("No"),
              onPressed: () {
                Navigator.of(context).pop();
                _saveUserPreferance(deviceId);
              },
            ),
      
            new TextButton(
              child: new Text("Yes"),
              onPressed: () {
                Navigator.of(context).pop();
              _openReferalCodeBottomSheet(deviceId);
              },
            ),
          ],
        );
      },
    );
           }else{
             _saveUserPreferance(deviceId);
           }

          });
         }
      }
    }catch(e){
      AlertDialogManager().IsErrorAlertDialog(context, "Status",e.toString(), "");
    }

  }

  void _saveUserPreferance(String? deviceId1) {
    SharePreferenceManager.instance.setSharePreferenceInitialized("Initialize", "Yes");
          SharePreferenceManager.instance.setUserID("UserID", map["uid"]);
          SharePreferenceManager.instance.setUserName("UserName", map["username"]);
          SharePreferenceManager.instance.setDeviceID("DeviceID",deviceId1!);
         SharePreferenceManager.instance.setUserEmailAddress("UserEmailAddress", map["email_address"]);
          SharePreferenceManager.instance.setUserWalletAmt("WalletAmt", map["wallet_amt"]);
          SharePreferenceManager.instance.setUserPhoneNo("MobileNo", map["phone_no"]);
          SharePreferenceManager.instance.setRefferalCode("refer_code",map["refer_code"]);
            Get.offAll(SelctCityAndCityArea());
  }

  
   _openReferalCodeBottomSheet(String? deviceId) {
    return showModalBottomSheet(
      context:  context,
      builder: (context){
        return Form(
          key: _key,
          child: Column(
            children: [
              Container(
                width: double.infinity,
                color: Color(0xff131313),
                height: 40,
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    GestureDetector(
                      onTap: (){
                        Navigator.of(context).pop();
                        _saveUserPreferance(deviceId);
                      },
                      child: Align(
                          alignment: Alignment.centerLeft,
                          child: Image.asset("drawable/ic_remove.png",width: 20,height: 20,color: Colors.white,)),
                    ),
                  Spacer(),
                   Align(
                     alignment: Alignment.centerRight,
                      child: Text("Apply Coupon",style: GoogleFonts.aBeeZee(fontSize: 15,color:Color(
                          0xffffffff)),),
                    ),
                    Spacer(),
                  ],
                ),
              ),
              Container(
                height: 60,
                child: Row(
                  children: [
                    Padding(
                      padding: const EdgeInsets.only(left: 8.0,right: 0),
                      child: Container(
                        width: MediaQuery.of(context).size.width*0.70,
                        child: TextFormField(
                          controller: editCoupon,
                                    keyboardType: TextInputType.text,
                                    validator: (v){
                                      if(v!.isEmpty){
                                        return "Please Enter Refferal Code here ..";
                                      }
                                      return null;
                                    },
                          decoration: InputDecoration(
                            prefixIcon: Padding(
                              padding: const EdgeInsets.only(top:15.0,bottom: 15,left: 0),
                              child: Image.asset("drawable/discount1.png",width: 1,height: 1,color: Colors.black,),
                            ),
                            hintText: "Enter Refferal Code",
                            hintStyle: GoogleFonts.aBeeZee(fontSize: 15,color: Color(
                                0xff999494)),
                            enabledBorder: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(3),
                              borderSide: BorderSide(
                                color: Color(0xffc4bfbf),
                                width: 0.70,
                              ),
                            ),
                            contentPadding: EdgeInsets.only(bottom:2,top: 2),
                                        border: OutlineInputBorder(
                                            borderRadius: BorderRadius.circular(3.0),
                                            borderSide: BorderSide(color: Color(0xffc4bfbf))
                                        ),
                                        focusedBorder: OutlineInputBorder(
                                          borderSide: BorderSide(width:1.0,color: Color(0xffc4bfbf)),
                                          borderRadius: BorderRadius.circular(3.0),
                                        ),
                          ),

                        ),
                      ),
                    ),
                    Padding(
                      padding: const EdgeInsets.only(right:8.0,top:7,bottom: 7),
                      child: GestureDetector(
                        onTap: (){
                          if(_key.currentState!.validate()) {
                            setState(() {
                              pr.show();
                              String isApplied="true";
                              refferalCode = editCoupon.text;
                              applyRefferalCode(deviceId,isApplied);
                            });
                          }
                        },
                        child: Container(
                          width: MediaQuery.of(context).size.width*0.24,
                          height: 50,
                          color: Color(0xfff4811e),
                          child: Center(child: Text("Apply".toUpperCase(),style: GoogleFonts.aBeeZee(fontSize: 14,color: Colors.white),)),
                        ),
                      ),
                    )
                  ],
                ),
              )

            ],
          ),
        );
      });
  }

  void applyRefferalCode(String? deviceId, String isApplied)async{
  if(isApplied=="true"){
  map={
   "user_uid":widget.mobileNo,
   "refer_code_used":refferalCode
  };
  }else{
    map={
   "user_uid":widget.mobileNo,
  };
  }

   final response=await http.post(Uri.parse(ApiUrls.RefferalAPI),body: map);
   if(response.statusCode==200){
     map=json.decode(response.body);
     Navigator.of(context).pop();
     if(map.containsValue("ref_status")){
       pr.hide();
      if(map["ref_status"]=="refer_code_valid"){
        _saveUserPreferance(deviceId);
      }else{
        AlertDialogManager().IsErrorAlertDialog(context, "Status", "Enter Valid Refferal Code..", "");
      }
     }else{
       if(map["status"]=="pass"){
         _saveUserPreferance(deviceId);
       }
     }
   }

  }

  void resendOTP(String mobileNo1)async{

    // String randomOTP=RandomDigits.getInteger(6).toString();
    // String mobileNo="+91$mobileNo1";
    // String OtpMessage="Thank%20you%20for%20downloading%20Anand%20Mart%20.%20Your%20login%20OTP%20" + randomOTP + " for login.";
    // String url="http://sms.hitek-solutions.co.in/domestic/sendsms/bulksms_v2.php?apikey=QU1hcnQ6UFN3Z2pOeHQ&type=TEXT&sender=ANNDTR&entityId=1701164449629748884&templateId=&mobile=$mobileNo&message=$OtpMessage";
    // final response=await http.get(Uri.parse(url));
    // if(response.statusCode==200){
    //   if(response.body.contains("SUCCESS")){
    //      setState(() {
    //       pr.hide();
    //      OTPCode=randomOTP;
    //       restartOTPTimer();
    //     });
    //   }else{
    //     Fluttertoast.showToast(msg: "Something Is Wrong In OTP Server...");
    //   }
    // }else{
    //    Fluttertoast.showToast(msg: "Something Is Wrong In OTP Server...");
    // }

    String OTPCode1=RandomDigits.getInteger(6).toString();
    String mobileNo=mobileNo1;
    String url="http://sms.hitek-solutions.co.in/domestic/sendsms/bulksms_v2.php?apikey=QU1hcnQ6UFN3Z2pOeHQ&type=TEXT&sender=ANNDTR&entityId=1701164449629748884&templateId=&mobile=$mobileNo&message=Thank%20you%20for%20downloading%20Anand%20Mart%20.%20Your%20login%20OTP%20$OTPCode1%20for%20login";
    final response=await http.get(Uri.parse(url));
    if(response.statusCode==200){
      if(response.body.contains("SUCCESS")){
        setState(() {
          pr.hide();
          OTPCode=OTPCode1;
          restartOTPTimer();
        });
      }else{
        Fluttertoast.showToast(msg: "Something Is Wrong In OTP Server...");
      }
    }else{
      Fluttertoast.showToast(msg: "Something Is Wrong In OTP Server...");
    }
  }

  void restartOTPTimer() {
    _otpCountDown = OTPCountDown.startOTPTimer(
        timeInMS: _timesMillseconds, currentCountDown: (String st) {
      setState(() {
        timer = st;
        if (timer == "00:00") {
          IsVisiblityOTPTimer = false;
          IsVisibilityResendOTP = true;
        }
      });
    }, onFinish: () {
     
    });
  }
  // Future <void> _phoneAuthnatication(String mobileNo) async {
  //   try {
  //     await _auth.verifyPhoneNumber(phoneNumber: mobileNo,
  //         verificationCompleted:
  //         verificationCompleted,
  //         verificationFailed: _onVerificationFailed,
  //         codeSent: _onCodeSent,
  //         codeAutoRetrievalTimeout: _onCodeTimeout);
  //   } catch (e) {
  //     e.toString();
  //     AlertDialogManager().IsErrorAlertDialog(
  //         context, "Status", e.toString(), "");
  //   }
  // }

  // void verificationCompleted(PhoneAuthCredential phoneAuthCredential) async {
  //   User? _user = FirebaseAuth.instance.currentUser;
  //   setState(() {
  //     OTP = phoneAuthCredential.smsCode!;
  //   });
  //   if (phoneAuthCredential.smsCode != null) {
  //     try {
  //       UserCredential credential = await _user!.linkWithCredential(
  //           phoneAuthCredential);
  //     } catch (ew) {
  //       setState(() {
  //         AlertDialogManager().IsErrorAlertDialog(
  //             context, "Status", ew.toString(), "");
  //       });

  //     }
  //   }
  // }

  // _onVerificationFailed(FirebaseAuthException exception) {
  //   pr.hide();
  //   print("Error : "+exception.message.toString());
  //   AlertDialogManager().IsErrorAlertDialog(
  //       context, "Status", exception.message.toString(), "");

  // }

  // _onCodeSent(String verificationId, int? forceResendingToken) {
  //   this.VerificationId = verificationId;
  // }

  // _onCodeTimeout(String timeout) {
  //   return null;
  // }
  
}
