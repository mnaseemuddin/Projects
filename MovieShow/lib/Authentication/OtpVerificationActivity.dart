




import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/instance_manager.dart';
import 'package:otp_count_down/otp_count_down.dart';
import 'package:movieshow/Activity/DashBoard.dart';
import 'package:movieshow/ApplicationConfigration/ApiUrls.dart';
import 'package:movieshow/Authentication/SignInActivity.dart';
import 'package:movieshow/Support/AlertDialogManager.dart';
import 'package:movieshow/Support/SharePreferenceManager.dart';


class OTPVerfication extends StatefulWidget {

    late final String Initilized,UserID,MobileNo,ISDCode;
    OTPVerfication({Key? key,required this.Initilized,required this.
    UserID,required this.MobileNo,required this.ISDCode}):super(key: key);
    

  @override
  _OTPVerficationState createState() => _OTPVerficationState();
}

class _OTPVerficationState extends State<OTPVerfication> {
  
  
  late String timer="";
  TextEditingController editOTP1=new TextEditingController();
    TextEditingController editOTP2=new TextEditingController();
    TextEditingController editOTP3=new TextEditingController();
    TextEditingController editOTP4=new TextEditingController();
    TextEditingController editOTP5=new TextEditingController();
    TextEditingController editOTP6=new TextEditingController();
  late OTPCountDown _otpCountDown;
  final int _timesMillseconds=1000*5*12;

  bool IsVisibilityResendOTP=false,IsVisiblityOTPTimer=true;
  late String verificationId;
  late FirebaseAuth _auth=FirebaseAuth.instance;
  late String OTP;
  late Map map,map1;

@override
  void initState() {

    _otpCountDown=OTPCountDown.startOTPTimer(timeInMS: _timesMillseconds, currentCountDown: (String st){
      setState(() {
    timer=st;
    if(timer=="00:00"){
     IsVisiblityOTPTimer=false;
     IsVisibilityResendOTP=true;
    }  
      });
      },
     onFinish:(){});
     late String mobileNo="+"+widget.ISDCode+widget.MobileNo;
     registerUser(mobileNo);
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
   
    return WillPopScope(
      onWillPop: (){
        Get.off(SignIn());
        return Future.value(false);
      },
      child: Scaffold(
        backgroundColor: const Color(0xff000000),
        appBar: AppBar(
          backgroundColor: Colors.black,
        ),
        body: Column(
          children: [

            Align(
                alignment: Alignment.centerLeft,
                child: Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: Icon(Icons.phone_android_rounded,size: 50,color: Colors.white,),
                )),
             SizedBox(height: 40),
            Align(
              alignment: Alignment.centerLeft,
              child: Padding(
                padding: const EdgeInsets.only(top:14.0,bottom:15),
                child: Text('Verification Code',style: TextStyle(
                  fontFamily: 'Helvetica Neue',
                  fontSize: 25,
                  color: const Color(0xffffffff),
                  fontWeight: FontWeight.w500,
                ),),
              ),
            ),
            Row(
              children: [
                Expanded(
                  child: Padding(
                    padding: const EdgeInsets.only(right:8.0,left: 10,top: 5),
                    child: Container(
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(5.0),
                        color: const Color(0xff1f3349),
                        border: Border.all(width: 1.0, color: const Color(0xff1f3349)),
                      ),
                      child: Padding(
                        padding: const EdgeInsets.only(left:15.0),
                        child: TextField(
                          keyboardType: TextInputType.number,
                          controller: editOTP1,
                          onChanged: (_) => FocusScope.of(context).nextFocus(),
                          textInputAction: TextInputAction.next,

                          inputFormatters: [
                            LengthLimitingTextInputFormatter(1),
                            FilteringTextInputFormatter.digitsOnly
                          ],
                         // controller: editPassword,
                          style: TextStyle(color: Colors.white,fontSize: 17),
                          decoration: new InputDecoration(
                           // hintText: "Enter Password",
                            border: InputBorder.none,
                           // prefixIcon: Icon(Icons.lock_open_outlined,color: Color(0xffffffff),),
                          ),
                        ),
                      ),
                    ),
                  ),
                ),
                Expanded(
                  child: Padding(
                    padding: const EdgeInsets.only(right:8.0,left: 8,top: 5),
                    child: Container(

                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(5.0),
                        color: const Color(0xff1f3349),
                        border: Border.all(width: 1.0, color: const Color(0xff1f3349)),
                      ),
                      child: Padding(
                        padding: const EdgeInsets.only(left:15.0),
                        child: TextField(
                          keyboardType: TextInputType.number,
                           controller: editOTP2,
                            onChanged: (_) => FocusScope.of(context).nextFocus(),
                           textInputAction: TextInputAction.next,
                          inputFormatters: [
                            LengthLimitingTextInputFormatter(1),
                            FilteringTextInputFormatter.digitsOnly
                          ],
                          style: TextStyle(color: Colors.white,fontSize: 17),
                          decoration: new InputDecoration(
                            // hintText: "Enter Password",
                            border: InputBorder.none,
                            // prefixIcon: Icon(Icons.lock_open_outlined,color: Color(0xffffffff),),
                          ),
                        ),
                      ),
                    ),
                  ),
                ),
                Expanded(
                  child: Padding(
                    padding: const EdgeInsets.only(right:8.0,left: 8,top: 5),
                    child: Container(
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(5.0),
                        color: const Color(0xff1f3349),
                        border: Border.all(width: 1.0, color: const Color(0xff1f3349)),
                      ),
                      child: Padding(
                        padding: const EdgeInsets.only(left:15.0),
                        child: TextField(
                          keyboardType: TextInputType.number,
                          textInputAction: TextInputAction.next,
                          controller: editOTP3,
                           onChanged: (_) => FocusScope.of(context).nextFocus(),
                          inputFormatters: [
                            LengthLimitingTextInputFormatter(1),
                            FilteringTextInputFormatter.digitsOnly
                          ],
                          // controller: editPassword,
                          style: TextStyle(color: Colors.white,fontSize: 17),
                          decoration: new InputDecoration(
                            // hintText: "Enter Password",
                            border: InputBorder.none,
                            // prefixIcon: Icon(Icons.lock_open_outlined,color: Color(0xffffffff),),
                          ),
                        ),
                      ),
                    ),
                  ),
                ),
                Expanded(
                  child: Padding(
                    padding: const EdgeInsets.only(right:8.0,left: 8,top: 5),
                    child: Container(
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(5.0),
                        color: const Color(0xff1f3349),
                        border: Border.all(width: 1.0, color: const Color(0xff1f3349)),
                      ),
                      child: Padding(
                        padding: const EdgeInsets.only(left:15.0),
                        child: TextField(
                          keyboardType: TextInputType.number,
                          controller: editOTP4,
                           onChanged: (_) => FocusScope.of(context).nextFocus(),
                          textInputAction: TextInputAction.next,
                          inputFormatters: [
                            LengthLimitingTextInputFormatter(1),
                            FilteringTextInputFormatter.digitsOnly
                          ],
                          // controller: editPassword,
                          style: TextStyle(color: Colors.white,fontSize: 17),
                          decoration: new InputDecoration(
                            // hintText: "Enter Password",
                            border: InputBorder.none,
                            // prefixIcon: Icon(Icons.lock_open_outlined,color: Color(0xffffffff),),
                          ),
                        ),
                      ),
                    ),
                  ),
                ),
                Expanded(
                  child: Padding(
                    padding: const EdgeInsets.only(right:8.0,left: 8,top: 5),
                    child: Container(
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(5.0),
                        color: const Color(0xff1f3349),
                        border: Border.all(width: 1.0, color: const Color(0xff1f3349)),
                      ),
                      child: Padding(
                        padding: const EdgeInsets.only(left:15.0),
                        child: TextField(
                          keyboardType: TextInputType.number,
                          controller: editOTP5,
                           onChanged: (_) => FocusScope.of(context).nextFocus(),
                          textInputAction: TextInputAction.next,
                          inputFormatters: [
                            LengthLimitingTextInputFormatter(1),
                            FilteringTextInputFormatter.digitsOnly
                          ],
                          // controller: editPassword,
                          style: TextStyle(color: Colors.white,fontSize: 17),
                          decoration: new InputDecoration(
                            // hintText: "Enter Password",
                            border: InputBorder.none,
                            // prefixIcon: Icon(Icons.lock_open_outlined,color: Color(0xffffffff),),
                          ),
                        ),
                      ),
                    ),
                  ),
                ),
                Expanded(
                  child: Padding(
                    padding: const EdgeInsets.only(right:8.0,left: 8,top: 5),
                    child: Container(
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(5.0),
                        color: const Color(0xff1f3349),
                        border: Border.all(width: 1.0, color: const Color(0xff1f3349)),
                      ),
                      child: Padding(
                        padding: const EdgeInsets.only(left:15.0),
                        child: TextField(
                          controller: editOTP6,
                          keyboardType: TextInputType.number,
                          textInputAction: TextInputAction.next,
                          inputFormatters: [
                            LengthLimitingTextInputFormatter(1),
                            FilteringTextInputFormatter.digitsOnly
                          ],
                          // controller: editPassword,
                          style: TextStyle(color: Colors.white,fontSize: 17),
                          decoration: new InputDecoration(
                            // hintText: "Enter Password",
                            border: InputBorder.none,
                            // prefixIcon: Icon(Icons.lock_open_outlined,color: Color(0xffffffff),),
                          ),
                        ),
                      ),
                    ),
                  ),
                ),
              ],
            ),
            Visibility(
              visible: IsVisiblityOTPTimer,
              child: Container(
                height: 60,
                child: Padding(
                  padding: const EdgeInsets.only(left:15.0,right: 15,top: 10),
                  child: Text("Didn't receive the OTP? Request for a new one in $timer sec",
                    style: TextStyle(
                    fontFamily: 'Helvetica Neue',
                    fontSize: 16,
                    color: const Color(0xffffffff),
                    fontWeight: FontWeight.w500,
                  ),),
                ),
              ),
            ),

Visibility(
  visible: IsVisibilityResendOTP,
  child:   GestureDetector(
      onTap: (){
        setState(() {
          IsVisibilityResendOTP=false;
          IsVisiblityOTPTimer=true;
          initState();
        });
      },
      child: Container(
                  height: 60,
                  child: Padding(
                    padding: const EdgeInsets.only(left:15.0,right: 15,top: 10),
                    child: Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: Text("Resend OTP",
                        style: TextStyle(
                        fontFamily: 'Helvetica Neue',
                        fontSize: 21,
                        color: const Color(0xffffffff),
                        fontWeight: FontWeight.w500,
                      ),),
                    ),
                  ),
                ),
  ),
),

            Padding(
              padding: const EdgeInsets.only(top:15.0,left:30,right:30),
              child: GestureDetector(
                onTap: (){
                OTP=editOTP1.text+editOTP2.text+editOTP3.text+
                editOTP4.text+editOTP5.text+editOTP6.text;
                  VerifyOTP(OTP);
                },
                child: Container(
                  width: double.infinity,
                  height:45,
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(5.0),
                    color:  Color(0xffac1414),
                  ),
                  child: Center(child: Text('Submit',style: TextStyle(
                    fontFamily: 'Helvetica Neue',
                    fontSize: 16,
                    color: const Color(0xffffffff),
                    fontWeight: FontWeight.w500,
                  ),)),
                ),
              ),
            )
          ],
        )
      ),
    );
  }


 

void focusNextTextField(BuildContext context) {
  do {
    var foundFocusNode = FocusScope.of(context).nextFocus();
    if (!foundFocusNode) return;
  } while (FocusScope.of(context).focusedChild!.context!.widget is! EditableText);
}

 Future <void>registerUser(String mobileNo) async {

  try{

   await _auth.verifyPhoneNumber(phoneNumber: mobileNo, verificationCompleted:
    verificationCompleted, verificationFailed: _onVerificationFailed, codeSent:
     _onCodeSent, codeAutoRetrievalTimeout: _onCodeTimeout);
 }catch(e){
   e.toString();
   AlertDialogManager().IsErrorAlertDialog(context, "Status",e.toString(),"");
 }
  }

  void verificationCompleted(PhoneAuthCredential phoneAuthCredential) async{
    User? _user=FirebaseAuth.instance.currentUser;
    setState(() {
      OTP=phoneAuthCredential.smsCode!;
    });
    if(phoneAuthCredential.smsCode!=null){
      try{
        UserCredential credential=await _user!.linkWithCredential(phoneAuthCredential);
        SharePreferenceManager.instance.setSharePreferenceInitialized("Initilized", "Exits");
        SharePreferenceManager.instance.setUserID("UserID",map["user_id"]);
        Get.offAll(DashBoard());
      }catch(ew){
        AlertDialogManager().IsErrorAlertDialog(context, "Status", ew.toString(), "");
      }
    }
  }
  _onVerificationFailed(FirebaseAuthException exception) {
    if (exception.code == 'invalid-phone-number') {
       AlertDialogManager().IsErrorAlertDialog(context, "Status", "The phone number entered is invalid!", "");
    }
  }
  _onCodeSent(String verificationId, int? forceResendingToken) {
    this.verificationId = verificationId;
    print(forceResendingToken);
    print("code sent");
  }
  _onCodeTimeout(String timeout) {
    return null;
  }

  void VerifyOTP(String smsCode) async{
  final AuthCredential credential=PhoneAuthProvider.credential(verificationId: verificationId,
  smsCode: smsCode);
  final User? user=(await _auth.signInWithCredential(credential)).user;
  SharePreferenceManager.instance.setSharePreferenceInitialized("Initilized", widget.Initilized);
  SharePreferenceManager.instance.setUserID("UserID",widget.UserID);
  Get.offAll(DashBoard());
  }

  // _signUp() async {
  //    map={
  //     'name':widget.Name,
  //     "email_or_mobile":widget.MobileNoEmailID,
  //     "password":widget.Password,
  //   };
  // //  8935005528
  // // {"message":"Registered Successfully.","status":"1","user_id":"290","data":
  // // {"name":"ABCD","email":"8935005528","mobile":"8935005528","password":"123456","bonus_amount":"50"}}
  //   try{
  //     // ignore: non_constant_identifier_names
  //     var ApiUrls=Uri.parse(APiUrls.SignUp);
  //     var response=await http.post(ApiUrls,body: map);
  //     if(response.statusCode==200) {
  //       map = json.decode(response.body);
  //       if(map["status"]=="1"){
  //          map1 =map['data'];
  //
  //       }else{
  //         AlertDialogManager().IsErrorAlertDialog(context, "Status", map["message"], "");
  //       }
  //     }
  //   }catch(e){
  //     AlertDialogManager().IsErrorAlertDialog(context, "Status", e.toString(), "");
  //   }
  // }
}