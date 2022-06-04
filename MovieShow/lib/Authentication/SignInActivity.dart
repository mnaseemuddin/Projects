

import 'dart:io' show Platform;
import 'dart:convert';
import 'package:apple_sign_in/apple_sign_in.dart';
import 'package:email_validator/email_validator.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/services.dart';
import 'package:flutter_facebook_login/flutter_facebook_login.dart';
 import 'package:google_sign_in/google_sign_in.dart';
import 'package:http/http.dart' as http;
import 'dart:io';
import 'package:adobe_xd/pinned.dart';
import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:get/get.dart';
import 'package:movieshow/Activity/DashBoard.dart';
import 'package:movieshow/Activity/ForgotPasswordActivity.dart';
import 'package:movieshow/ApplicationConfigration/ApiUrls.dart';
import 'package:movieshow/Support/AlertDialogManager.dart';
import 'package:movieshow/Support/SharePreferenceManager.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:sign_in_with_apple/sign_in_with_apple.dart';

import 'SignUpActivity.dart';

class SignIn extends StatefulWidget {

  const SignIn({Key? key}) : super(key: key);

  @override
  _SignInState createState() => _SignInState();
}

class _SignInState extends State<SignIn> {

  TextEditingController editMobileEmail=new TextEditingController();
  TextEditingController editPassword=new TextEditingController();
  String LoginType="";
  FocusNode editMobileEmailFocus=FocusNode();
  var dio=Dio();
  final FirebaseAuth _auth = FirebaseAuth.instance;
  final GoogleSignIn _googleSignIn = GoogleSignIn();
  late User _user;
  var fbProfileDetails;
  var _key=new GlobalKey<FormState>();
  bool isEmail(String input) => EmailValidator.validate(input);
bool isPhone(String input) => RegExp(
  r'^[\+]?[(]?[0-9]{3}[)]?[-\s\.]?[0-9]{3}[-\s\.]?[0-9]{4,6}$'
).hasMatch(input);
bool IsVisiblityAppleLoginUI=false;

@override
  void initState() {
    setState(() {
      if(Platform.isAndroid){
        IsVisiblityAppleLoginUI=false;
      }else if(Platform.isIOS){
         IsVisiblityAppleLoginUI=true;
      }
    });
    super.initState();
  }
  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: (){
        showDialog(
          context: context,
          builder: (BuildContext context) {
            // return object of type Dialog
            return AlertDialog(
              title: Text("Exit?"),
              content: new Text("Are you sure you want to exit?"),
              actions: <Widget>[
                new FlatButton(
                  child: new Text("CANCEL",style: TextStyle(color: Colors.red, fontFamily: 'Helvetica Neue',
                    fontSize: 14.899999618530273,),),
                  onPressed: () {
                    Navigator.of(context).pop();
                  },
                ),

                new FlatButton(
                  child: new Text("OK",style: TextStyle(color: Colors.red, fontFamily: 'Helvetica Neue',
                    fontSize: 14.899999618530273,),),
                  onPressed: () {
                    exit(0);
                  },
                ),
                // usually buttons at the bottom of the dialog

              ],
            );
          },
        );
        return Future.value(false);
      },
      child: Scaffold(
          backgroundColor: Color(0xff000000),
          body:ListView(
            children: [
              Container(
                height: MediaQuery.of(context).size.height*0.85,
                child: ListView(
                  children: [
                    Stack(
                      children: <Widget>[
                        Padding(
                          padding: const EdgeInsets.only(top:48.0),
                          child: Container(
                            height: 200,
                            width: double.infinity,
                            decoration: BoxDecoration(
                            ),
                            child: Center(child: Padding(
                              padding: const EdgeInsets.all(20.0),
                              child: Image.asset('drawable/logo.png'),
                            )),
                          ),
                        ),

                        Form(
                          key: _key,
                          child: Padding(
                            padding: const EdgeInsets.only(top:258.0),
                            child: Column(
                              children: [
                                Padding(
                                  padding: const EdgeInsets.only(left:20.0,top: 20,right: 20,bottom: 20),
                                  child: TextFormField(
                                    textInputAction: TextInputAction.next,
                                    validator: (v){
                                      if(v!.isEmpty){
                                        return "Please Enter Mobile Number or Email .";
                                      }
                                      return null;
                                    },

                                    controller: editMobileEmail,
                                    focusNode: editMobileEmailFocus,
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
                                  padding: const EdgeInsets.only(right:20.0,left: 20,top: 5),
                                  child: TextFormField(
                                  validator: (v){
                                      if(v!.isEmpty){
                                        return "Please Enter Password .";
                                      }
                                      return null;
                                    },
                                    controller: editPassword,
                                    obscureText: true,
                                    style: TextStyle(color: Colors.white),

                                    decoration: new InputDecoration(
                                       errorStyle: TextStyle(fontSize: 14.5,color: 
                                       Colors.redAccent,fontWeight: FontWeight.w600),
                                      focusColor: Colors.white,
                                       border: OutlineInputBorder(
                                        borderRadius: BorderRadius.circular(5.0),
                                       borderSide: BorderSide(color: Color(0xff1f3349))
                                      ),
 filled: true,
                                      fillColor: Color(0xff1f3349),
                                      focusedBorder: OutlineInputBorder(
                        borderSide: BorderSide(color: Color(0xff1f3349)),
                        borderRadius: BorderRadius.circular(10.0),
                      ),
                                      hintText: "Enter Password",

                                      prefixIcon: Icon(Icons.lock_open_outlined,color: Color(0xffffffff),),
                                      hintStyle: TextStyle(
                                        fontFamily: 'Helvetica Neue',
                                        fontSize: 15,
                                        color: const Color(0xffffffff),
                                        fontWeight: FontWeight.w500,
                                      ),
                                    ),
                                  ),
                                ),
                                Padding(
                                  padding: const EdgeInsets.only(right:20.0,left: 20,top: 35),
                                  child: GestureDetector(
                                    onTap: (){
                                      LoginType='normal';
                                    //  if(validation()) {
                                      if(_key.currentState!.validate()) {

if(isEmail(editMobileEmail.text)||isPhone(editMobileEmail.text)){
                                        userLoginByNormal(LoginType);
                                        }else{
                                          AlertDialogManager().IsErrorAlertDialog(context, "",
                                          "Please Enter valid Mobile Number or Email .", "");
                                        }


                                      }
                                  //    }
                                      // Get.to(DashBoard());
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
                                          'Login',
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
                                Align(
                                  alignment: Alignment.centerRight,
                                  child: GestureDetector(
                                    onTap: (){
                                      Get.to(ForgotPasswordActivity());
                                    },
                                    child: Padding(
                                      padding: const EdgeInsets.only(right:20.0,top: 5),
                                      child: Text(
                                        'Forgot Password ?',
                                        style: TextStyle(
                                          fontFamily: 'Helvetica Neue',
                                          fontSize: 13,
                                          color: const Color(0xffffffff),
                                          fontWeight: FontWeight.w700,
                                        ),
                                        textAlign: TextAlign.right,
                                      ),
                                    ),
                                  ),
                                ),
                                Padding(
                                  padding: const EdgeInsets.all(18.0),
                                  child: Text(
                                    'OR',
                                    style: TextStyle(
                                      fontFamily: 'Helvetica Neue',
                                      fontSize: 13.5,
                                      color: const Color(0xffffffff),
                                      fontWeight: FontWeight.w700,
                                    ),
                                    textAlign: TextAlign.right,
                                  ),
                                ),
                                Container(
                                  height: 45,
                                  child: Row(
                                    mainAxisAlignment: MainAxisAlignment.center,
                                    children: [
                          GestureDetector(
                              onTap: (){
                                signInWithFaceBook();
                              },
                            child: Padding(
                            padding: const EdgeInsets.only(left:0.0),
                                                    child: SvgPicture.string(
                                                      _svg_7vkce0,
                                                      height: 40,width: 40,
                                                      allowDrawingOutsideViewBox: true,
                                                      fit: BoxFit.fill,
                                                    ),
                                                  ),
                          ),
                                                GestureDetector(
                                                    onTap: (){
                                                      signInWithGoogle();
                                                    },
                                                  child: Padding(
                                                    padding: const EdgeInsets.only(left:18.0),
                                                    child: SvgPicture.string(
                                                      _svg_cdmsf6,
                                                      height: 40,width: 40,
                                                      allowDrawingOutsideViewBox: true,
                                                      fit: BoxFit.fill,
                                                    ),
                                                  ),
                                                ),
                                                 Visibility(
                                      visible: IsVisiblityAppleLoginUI,
                                      child: GestureDetector(
                                      onTap: (){
                                        _signInWithApple();
                                      },
                                        child: Padding(
                                                    padding: const EdgeInsets.only(left:15.0,bottom: 4),
                                                    child: Image.asset(
                                                      "drawable/apple.png",
                                                      height: 40,width: 40,
                                                      fit: BoxFit.fill,
                                                      color: Colors.white,
                                                    ),
                                                  ),
                                      ),
                                    ),
                                      // Expanded(child: Padding(
                                      //   padding: const EdgeInsets.only(left:20.0,right: 5),
                                      //   child: GestureDetector(
                                      //     onTap:(){
                                      //       _signWithAppleID();
                                      //     },
                                      //     child: Container(
                                      //       height: double.infinity,
                                      //       decoration: BoxDecoration(
                                      //         borderRadius: BorderRadius.circular(3.0),
                                      //         color:  Colors.black,
                                      //         border: Border.all(width: 1.0, color: const Color(0xffffffff)),
                                      //       ),
                                      //       child: Row(
                                      //         mainAxisAlignment: MainAxisAlignment.center,
                                      //         children: [
                                      //           Padding(
                                      //             padding: const EdgeInsets.only(left:0.0),
                                      //             child: Image.asset(
                                      //               "drawable/apple.png",
                                      //               height: 16,width: 16,
                                      //               fit: BoxFit.fill,
                                      //               color: Colors.white,
                                      //             ),
                                      //           ),
                                      //           Padding(
                                      //             padding: const EdgeInsets.only(left: 8.0),
                                      //             child: Text(
                                      //               'Apple Login',
                                      //               style: TextStyle(
                                      //                 fontFamily: 'Helvetica Neue',
                                      //                 fontSize: 11.899999618530273,
                                      //                 color: const Color(0xffffffff),
                                      //                 fontWeight: FontWeight.w700,
                                      //               ),
                                      //               textAlign: TextAlign.right,
                                      //             ),
                                      //           ),
                                      //         ],
                                      //       ),
                                      //     ),
                                      //   ),
                                      // )),
                                      // // Expanded(child:
                                      // // SignInWithAppleButton(
                                      // //     style: SignInWithAppleButtonStyle.black,
                                      // //
                                      // //     onPressed: (){
                                      // //       _signWithAppleID();
                                      // //     }),),
                                      // Expanded(child: Padding(
                                      //   padding: const EdgeInsets.only(left:4.0,right: 5),
                                      //   child: GestureDetector(
                                      //     onTap:(){
                                      //       signInWithFaceBook();
                                      //     },
                                      //     child: Container(
                                      //       height: double.infinity,
                                      //       decoration: BoxDecoration(
                                      //         borderRadius: BorderRadius.circular(3.0),
                                      //         color: const Color(0xff3a559f),
                                      //         border: Border.all(width: 1.0, color:
                                      //         const Color(0xff3a559f)),
                                      //       ),
                                      //       child: Row(
                                      //         mainAxisAlignment: MainAxisAlignment.center,
                                      //         children: [
                                      //           Padding(
                                      //             padding: const EdgeInsets.only(left:0.0),
                                      //             child: SvgPicture.string(
                                      //               _svg_7vkce0,
                                      //               height: 16,width: 16,
                                      //               allowDrawingOutsideViewBox: true,
                                      //               fit: BoxFit.fill,
                                      //             ),
                                      //           ),
                                      //           Padding(
                                      //             padding: const EdgeInsets.only(left: 8.0),
                                      //             child: Text(
                                      //               'Facebook Login',
                                      //               style: TextStyle(
                                      //                 fontFamily: 'Helvetica Neue',
                                      //                 fontSize: 11.899999618530273,
                                      //                 color: const Color(0xffffffff),
                                      //                 fontWeight: FontWeight.w700,
                                      //               ),
                                      //               textAlign: TextAlign.right,
                                      //             ),
                                      //           ),
                                      //         ],
                                      //       ),
                                      //     ),
                                      //   ),
                                      // )),
                                      // Expanded(child: Padding(
                                      //   padding: const EdgeInsets.only(left: 4,right:20.0),
                                      //   child: GestureDetector(
                                      //     onTap: (){
                                      //       signInWithGoogle();
                                      //     },
                                      //     child: Container(
                                      //       height: double.infinity,
                                      //       decoration: BoxDecoration(
                                      //         borderRadius: BorderRadius.circular(3.0),
                                      //         color: const Color(0xffe41e18),
                                      //         border: Border.all(width: 1.0, color: const Color(0xffe41e18)),
                                      //       ),
                                      //       child: Row(
                                      //         mainAxisAlignment: MainAxisAlignment.center,
                                      //         children: [
                                      //           Padding(
                                      //             padding: const EdgeInsets.only(left:0.0),
                                      //             child: SvgPicture.string(
                                      //               _svg_cdmsf6,
                                      //               height: 15,width: 15,
                                      //               allowDrawingOutsideViewBox: true,
                                      //               fit: BoxFit.fill,
                                      //             ),
                                      //           ),
                                      //           Padding(
                                      //             padding: const EdgeInsets.only(left: 8.0),
                                      //             child: Text(
                                      //               'Google Login',
                                      //               style: TextStyle(
                                      //                 fontFamily: 'Helvetica Neue',
                                      //                 fontSize: 11.899999618530273,
                                      //                 color: const Color(0xffffffff),
                                      //                 fontWeight: FontWeight.w700,
                                      //               ),
                                      //               textAlign: TextAlign.right,
                                      //             ),
                                      //           ),
                                      //         ],
                                      //       ),
                                      //     ),
                                      //   ),
                                      // )),
                                    ],
                                  ),
                                ),
                              ],
                            ),
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
              Container(
                height: MediaQuery.of(context).size.height*0.15,
                child: Container(
                            child: Padding(
                              padding: const EdgeInsets.only(bottom: 18.0),
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
                            ),
                          ),
              )
            ],
          )
      ),
    );
  }
  void userLoginByNormal(String Types)async{

    Map map={
      "email_or_mob":editMobileEmail.text,
      "password":editPassword.text,
      "type":Types
    };
    try{
      // ignore: non_constant_identifier_names
      var ApiUrls=Uri.parse(APiUrls.Login);
      var response=await http.post(ApiUrls,body: map);
      if(response.statusCode==200) {
        map = json.decode(response.body);
        if(map["status"]=="1"){
          Map map1 =map['data'];
          SharePreferenceManager.instance.setSharePreferenceInitialized("Initilized", "Exits");
          SharePreferenceManager.instance.setUserID("UserID",map1["user_id"]);
          Get.offAll(DashBoard());
        }else{
          AlertDialogManager().IsErrorAlertDialog(context, "Status", map["message"], "");
        }
      }
    }catch(e){
      AlertDialogManager().IsErrorAlertDialog(context, "Status", e.toString(), "");
    }
  }
  // void ShowMsg(String msg){
  //   Fluttertoast.showToast(
  //     msg: msg,
  //     toastLength: Toast.LENGTH_SHORT,
  //     gravity: ToastGravity.BOTTOM,
  //   );
  // }



  Future<User?> signInWithGoogle() async{
    try {
      final GoogleSignInAccount? googleSignInAccount = await _googleSignIn
          .signIn();
      GoogleSignInAuthentication googleSignInAuthentication =
      await googleSignInAccount!.authentication;
      AuthCredential credential = GoogleAuthProvider.credential(
          accessToken: googleSignInAuthentication.accessToken,
          idToken: googleSignInAuthentication.idToken
      );
      await _auth.signInWithCredential(credential);
      UserCredential userCredential=await _auth.signInWithCredential(credential);
      _user=userCredential.user!;
      LoginType="SOCIAL";
      userLoginBySocial(LoginType,_user.email,_user.uid);
    }catch(e){print(e.toString());}
  }

  void userLoginBySocial(String loginType, String? email, String uid) async{

    Map map={
      "email_or_mob":email,
      "password":uid,
      "type":loginType
    };
    print(map.toString());
    try{
      // ignore: non_constant_identifier_names
      var ApiUrls=Uri.parse(APiUrls.Login);
      var response=await http.post(ApiUrls,body: map);
      if(response.statusCode==200) {
        map = json.decode(response.body);
        if(map["status"]=="1"){
          Map map1 =map['data'];
          SharePreferenceManager.instance.setSharePreferenceInitialized("Initilized", "Exits");
          SharePreferenceManager.instance.setUserID("UserID",map1["user_id"]);
          Get.offAll(DashBoard());
        }else{
          AlertDialogManager().IsErrorAlertDialog(context, "Status", map["message"], "");
        }
      }else{
        AlertDialogManager().IsErrorAlertDialog(context, "Status", response.body.toString(), "");
      }
    }catch(e){
      AlertDialogManager().IsErrorAlertDialog(context, "Status", e.toString(), "");
    }
  }



   signInWithFaceBook() async{
    var _login=FacebookLogin();
    var facebookLoginResult =await _login.logIn(['email', 'public_profile']);
    switch(facebookLoginResult.status){
      case FacebookLoginStatus.error:
      AlertDialogManager().IsErrorAlertDialog(context, "Status",facebookLoginResult.errorMessage, "");
      break;
      case FacebookLoginStatus.loggedIn:
      LoginType="SOCIAL";
       var ApiUrls=Uri.parse('https://graph.facebook.com/v2.12/me?fields=name,first_name,last_name,email&access_token=${facebookLoginResult
  .accessToken.token}');
      var graphResponse = await http.get(ApiUrls);
       fbProfileDetails = json.decode(graphResponse.body);
       userLoginBySocial(LoginType,fbProfileDetails['email']==null?fbProfileDetails['id']:
       fbProfileDetails['email'],
       fbProfileDetails['id']);
        break;
      case FacebookLoginStatus.cancelledByUser:
      AlertDialogManager().IsErrorAlertDialog(context, "Status","FaceBook Login Cancelled By User ..", "");
        break;
    }
  }

  //"{"name":"Inzi LogiClump","first_name":"Inzi","last_name":"LogiClump","email":"developerlogiclump\u0040gmail.com","id":"389392829479424"}"

  void _signInWithApple() async{

    final AuthorizationResult authorizationResult=await AppleSignIn.performRequests([
      AppleIdRequest(requestedScopes: [Scope.email,Scope.fullName],)
    ]);


    switch(authorizationResult.status){
      case AuthorizationStatus.authorized:
      // _auth.signInWithCredential(credential);
        AppleIdCredential appleIdCredential=authorizationResult.credential;
        final OAuthProvider oAuthProvider=new OAuthProvider("apple.com");
        final AuthCredential credential =oAuthProvider.credential(
          accessToken: String.fromCharCodes(appleIdCredential.authorizationCode),
          idToken: String.fromCharCodes(appleIdCredential.identityToken),
        );
        await _auth.signInWithCredential(credential);
        LoginType="SOCIAL";
        userLoginBySocial(LoginType,appleIdCredential.user,appleIdCredential.user);
        break;
      case AuthorizationStatus.error:
       
        break;
      case AuthorizationStatus.cancelled:
        AlertDialogManager().IsErrorAlertDialog(context, "Status","Cancelled By User ..",
            "");
        break;
    }
    // }else{
    //   print(authorizationResult.error.localizedDescription);
    //   AlertDialogManager().IsSuccessAlertDialog(context, "Status",authorizationResult.error.localizedFailureReason,
    //       "");
    // }
  }

}


const String _svg_7vkce0 =
    '<svg viewBox="15.6 9.0 25.0 25.0" ><path transform="translate(15.0, 8.44)" d="M 25.5625 13.13856506347656 C 25.5625 6.191303253173828 19.96774291992188 0.5625 13.0625 0.5625 C 6.157258033752441 0.5625 0.5625 6.191303253173828 0.5625 13.13856506347656 C 0.5625 19.41544151306152 5.133568286895752 24.6182804107666 11.109375 25.5625 L 11.109375 16.77396011352539 L 7.933971405029297 16.77396011352539 L 7.933971405029297 13.13856506347656 L 11.109375 13.13856506347656 L 11.109375 10.36777400970459 C 11.109375 7.216151237487793 12.97429370880127 5.475278854370117 15.83064460754395 5.475278854370117 C 17.19858932495117 5.475278854370117 18.62903213500977 5.720714569091797 18.62903213500977 5.720714569091797 L 18.62903213500977 8.814021110534668 L 17.05241966247559 8.814021110534668 C 15.5 8.814021110534668 15.015625 9.783595085144043 15.015625 10.77801704406738 L 15.015625 13.13856506347656 L 18.48235893249512 13.13856506347656 L 17.92792510986328 16.77396011352539 L 15.015625 16.77396011352539 L 15.015625 25.5625 C 20.99143218994141 24.6182804107666 25.5625 19.41544151306152 25.5625 13.13856506347656 Z" fill="#f7f7f7" stroke="none" stroke-width="1" stroke-miterlimit="4" stroke-linecap="butt" /></svg>';

const String _svg_cdmsf6 =
    '<svg viewBox="7.0 11.6 18.0 18.0" ><path transform="translate(7.0, 11.0)" d="M 18 9.772982597351074 C 18 14.90806484222412 14.42581748962402 18.5625 9.147540092468262 18.5625 C 4.08688497543335 18.5625 0 14.54153156280518 0 9.5625 C 0 4.583467483520508 4.08688497543335 0.5625 9.147540092468262 0.5625 C 11.61147499084473 0.5625 13.68442535400391 1.451612830162048 15.28155612945557 2.917741775512695 L 12.79180240631104 5.272983551025391 C 9.534835815429688 2.181048393249512 3.478278398513794 4.503629207611084 3.478278398513794 9.5625 C 3.478278398513794 12.70161247253418 6.02704906463623 15.24556255340576 9.147540092468262 15.24556255340576 C 12.76967144012451 15.24556255340576 14.12704849243164 12.69072437286377 14.34098243713379 11.36612796783447 L 9.147540092468262 11.36612796783447 L 9.147540092468262 8.270563125610352 L 17.85614585876465 8.270563125610352 C 17.94098281860352 8.731451034545898 18 9.174193382263184 18 9.772982597351074 Z" fill="#f7f7f7" stroke="none" stroke-width="1" stroke-miterlimit="4" stroke-linecap="butt" /></svg>';

