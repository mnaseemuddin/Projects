




import 'package:adobe_xd/pinned.dart';
import 'package:email_validator/email_validator.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:provider/provider.dart';
import 'package:tailor/CommonUI/no_internet_connection.dart';
import 'package:tailor/Screens/DashBoardActivity.dart';
import 'package:tailor/ApiRepositary.dart';
import 'package:tailor/Auth/SignIn.dart';
import 'package:tailor/Model/SignUpModel.dart';
import 'package:tailor/Support/ProgressDialogManagers.dart';
import 'package:tailor/Support/UISupport.dart';
import 'package:tailor/Support/connectivity_provider.dart';

class SignUp extends StatefulWidget {
  const SignUp({Key? key}) : super(key: key);

  @override
  _SignUpState createState() => _SignUpState();
}

class _SignUpState extends State<SignUp> {
  var editControllerEmailId=TextEditingController();
  var editControllerPassword=TextEditingController();
  var editControllerConfirmPassword=TextEditingController();
  var _key=GlobalKey<FormState>();
  bool _passwordVisible=true;
  bool isEmail(String input) => EmailValidator.validate(input);
  bool _passwordCVisible=true;

  @override
  void initState() {
    Provider.of<ConnectivityProvider>(context,listen: false).startMonitoring();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Consumer<ConnectivityProvider>(builder: (conetext,model,child){
      if(model.isOnline!=null){
        return  Scaffold(
          backgroundColor: const Color(0xffffffff),
        resizeToAvoidBottomInset: false,
          body:model.isOnline?Form(
            key: _key,
            child: Stack(
              children: <Widget>[
                Pinned.fromPins(
                  Pin(size: 356.0, end: -104.0),
                  Pin(size: 356.0, end: -196.0),
                  child: Container(
                    decoration: BoxDecoration(
                      borderRadius:
                      BorderRadius.all(Radius.elliptical(9999.0, 9999.0)),
                      color: UISupport.APP_PRIMARY_COLOR,
                      border: Border.all(width: 1.0, color: UISupport.APP_PRIMARY_COLOR),
                    ),
                  ),
                ),
                Pinned.fromPins(
                  Pin(start: 0.0, end: 0.0),
                  Pin(size: 426.0, start: -4.0),
                  child: Container(
                    decoration: BoxDecoration(
                      color: UISupport.APP_PRIMARY_COLOR,
                      border: Border.all(width: 1.0, color: UISupport.APP_PRIMARY_COLOR),
                    ),
                  ),
                ),
                Column(children: [
                  Container(
                    height: MediaQuery.of(context).size.height*.30,
                    child: Padding(
                      padding: const EdgeInsets.all(0),
                      child: Align(
                        alignment: Alignment.bottomCenter,
                        child: Container(
                          height: 130,
                          width: 130,
                          decoration: BoxDecoration(
                            borderRadius: BorderRadius.circular(400),
                            color: UISupport.APP_PRIMARY_COLOR,
                            border: Border.all(width: 2.0, color: const Color(0xffffffff)),
                          ),
                          child: Padding(
                            padding: const EdgeInsets.all(25.0),
                            child: SvgPicture.string(
                              _svg_ip5zo5,
                              allowDrawingOutsideViewBox: true,
                            ),
                          ),
                        ),
                      ),
                    ),
                  ),
                  Container(
                    height: MediaQuery.of(context).size.height*.05,
                    child: Align(
                      alignment: Alignment.bottomCenter,
                      child: Text(
                        'Register',
                        style: TextStyle(
                          fontFamily: 'Luminari',
                          fontSize: 30,
                          color: const Color(0xffffffff),
                        ),
                        textAlign: TextAlign.left,
                      ),
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.all(20.0),
                    child: Container(
                      height: MediaQuery.of(context).size.height*.56,
                      decoration: new BoxDecoration(
                        color: Colors.white,
                        borderRadius: BorderRadius.circular(20),
                        boxShadow: [
                          BoxShadow(
                            color: const Color(0x29000000),
                            offset: Offset(4, 3),
                            blurRadius: 6,
                          ),
                        ],
                      ),
                      child: Padding(
                        padding: const EdgeInsets.all(6.0),
                        child: Scaffold(
                          body: SingleChildScrollView(
                            scrollDirection: Axis.vertical,
                            child: Column(children: [
                              Padding(
                                padding: const EdgeInsets.only(top:38.0,right: 20,left: 20),
                                child:   Center(
                                  child: TextFormField(
                                    textInputAction: TextInputAction.next,
                                    controller: editControllerEmailId,
                                    inputFormatters: [
                                      FilteringTextInputFormatter.deny(RegExp('[ ]')),
                                    ],
                                    validator:(v){
                                      if(v!.trim().isEmpty){
                                        return "Please Enter Email Address .";
                                      }
                                      return null;
                                    },
                                    decoration: InputDecoration(
                                      enabledBorder: OutlineInputBorder(borderRadius: BorderRadius.circular(25),
                                          borderSide: BorderSide(width: 1,color:Colors.black54)),
                                      focusedBorder: OutlineInputBorder(borderRadius: BorderRadius.circular(25),
                                          borderSide: BorderSide(width: 1,color:Colors.black54)),
                                      prefixIcon: Icon(Icons.email_outlined,color: Colors.black54,),
                                      contentPadding: EdgeInsets.only(left:10,top: 10),
                                      border: OutlineInputBorder(
                                          borderRadius: BorderRadius.circular(5.0),
                                          borderSide: BorderSide(color: Colors.black54)),
                                      hintText:  'Email Address',
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
                                padding: const EdgeInsets.only(top:18.0,right: 20,left: 20),
                                child:   Center(
                                  child: TextFormField(
                                    textInputAction: TextInputAction.next,
                                    onFieldSubmitted: (_) => FocusScope.of(context).nextFocus(),
                                    controller: editControllerPassword,
                                    validator: (v){
                                      if(v!.trim().isEmpty){
                                        return "Please Enter Password .";
                                      }
                                      return null;
                                    },
                                    obscureText: _passwordVisible,
                                    inputFormatters: [
                                      FilteringTextInputFormatter.deny(RegExp('[ ]')),
                                    ],
                                    decoration: InputDecoration(
                                      suffixIcon: IconButton(onPressed: (){
                                        setState(() {
                                          if(_passwordVisible)
                                            _passwordVisible=false;
                                          else
                                            _passwordVisible=true;
                                        });
                                      }, icon: Icon(_passwordVisible?Icons.visibility:Icons.visibility_off,color: Colors.grey[600],)),
                                      enabledBorder: OutlineInputBorder(borderRadius: BorderRadius.circular(25),
                                          borderSide: BorderSide(width: 1,color:Colors.black54)),
                                      focusedBorder: OutlineInputBorder(borderRadius: BorderRadius.circular(25),
                                          borderSide: BorderSide(width: 1,color:Colors.black54)),
                                      prefixIcon: Icon(Icons.lock_open,color: Colors.black54,),
                                      contentPadding: EdgeInsets.only(left:10,top: 10),
                                      border: OutlineInputBorder(
                                          borderRadius: BorderRadius.circular(5.0),
                                          borderSide: BorderSide(color: Colors.black54)),
                                      hintText:  'Password',
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
                                padding: const EdgeInsets.only(top:18.0,right: 20,left: 20),
                                child: Center(
                                  child: TextFormField(
                                    controller: editControllerConfirmPassword,
                                    inputFormatters: [
                                      FilteringTextInputFormatter.deny(RegExp('[ ]')),
                                    ],
                                    validator: (v){
                                      if(v!.trim().isEmpty){
                                        return "Please Enter Confirm Password .";
                                      }
                                      return null;
                                    },
                                    obscureText: _passwordCVisible,
                                    decoration: InputDecoration(
                                      suffixIcon: IconButton(onPressed: (){
                                        setState(() {
                                          if(_passwordCVisible)
                                            _passwordCVisible=false;
                                          else
                                            _passwordCVisible=true;
                                        });
                                      }, icon: Icon(_passwordCVisible?Icons.visibility:Icons.visibility_off,color: Colors.grey[600],)),
                                      enabledBorder: OutlineInputBorder(borderRadius: BorderRadius.circular(25),
                                          borderSide: BorderSide(width: 1,color:Colors.black54)),
                                      focusedBorder: OutlineInputBorder(borderRadius: BorderRadius.circular(25),
                                          borderSide: BorderSide(width: 1,color:Colors.black54)),
                                      prefixIcon: Icon(Icons.lock_open,color: Colors.black54,),
                                      contentPadding: EdgeInsets.only(left:10,top: 10),
                                      border: OutlineInputBorder(
                                          borderRadius: BorderRadius.circular(5.0),
                                          borderSide: BorderSide(color: Colors.black54)),
                                      hintText:  'Confirm Password',
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
                                padding: const EdgeInsets.only(top:50.0,right:50,left: 50),
                                child:   GestureDetector(
                                  onTap: (){
                                    if(_key.currentState!.validate()) {
                                      if(isEmail(editControllerEmailId.text.trim())){
                                        if(editControllerPassword.text.trim().length>6){
                                          if (editControllerPassword.text != editControllerConfirmPassword.text) {
                                            Fluttertoast.showToast(msg: "Password and Confirm Password should be same .");
                                          } else {
                                            showDialog(context: context,
                                                builder: (context) => ProgressDialogManager(),
                                                barrierDismissible: false).then((value) {
                                              print(value);
                                            });
                                            Map<String, String>mao = {
                                              'email': editControllerEmailId.text
                                                  .toLowerCase(),
                                              'password': editControllerPassword.text
                                            };
                                            signupAPI(mao).then((value) {
                                              ApiResponse response = value;
                                              if (response.status) {
                                                SignUpModel signUpModel = response.data;
                                                Fluttertoast.showToast(
                                                    msg: signUpModel.message,
                                                    gravity: ToastGravity.BOTTOM);
                                                Get.offAll(SignIn());
                                              } else {
                                                Navigator.of(context).pop();
                                                ApiResponse response = value;
                                                Fluttertoast.showToast(
                                                    msg: response.message.toString(),
                                                    gravity: ToastGravity.BOTTOM);
                                              }
                                              Fluttertoast.showToast(
                                                  msg: response.message.toString());
                                            });
                                          }
                                        }else{
                                          Fluttertoast.showToast(msg: "Password length must be greater than 6");
                                        }
                                      }else{
                                        Fluttertoast.showToast(msg: "Please Enter Valid Email Address ");
                                      }
                                    }
                                  },
                                  child: Container(
                                    height: 50,
                                    decoration: BoxDecoration(
                                        color: UISupport.APP_PRIMARY_COLOR,
                                        borderRadius: BorderRadius.circular(25)
                                    ),
                                    child:Center(
                                      child: Text(
                                        'Register',
                                        style: GoogleFonts.aBeeZee(
                                          fontSize: 20,
                                          color: const Color(0xffffffff),
                                        ),
                                        textAlign: TextAlign.left,
                                      ),
                                    ),
                                  ),
                                ),
                              ),

                              GestureDetector(
                                onTap: (){
                                  Get.offAll(SignIn());
                                },
                                child: Container(
                                  width: double.infinity,
                                  height: 70,
                                  child:   Row(
                                    mainAxisAlignment: MainAxisAlignment.center,
                                    children: [
                                      Padding(
                                        padding: const EdgeInsets.only(top:4,),
                                        child: Text(
                                          'Already have Account?',
                                          style: TextStyle(
                                            fontFamily: 'Luminari',
                                            fontSize: 15,
                                            color: const Color(0xff151414),
                                          ),
                                          textAlign: TextAlign.left,
                                        ),
                                      ),
                                      Padding(
                                        padding: const EdgeInsets.only(left:2.0,top:4,),
                                        child:   Text(
                                          'Login',
                                          style: TextStyle(
                                            fontWeight: FontWeight.w600,
                                            fontFamily: 'Luminari',
                                            fontSize: 15,
                                            color: UISupport.APP_PRIMARY_COLOR,
                                          ),
                                          textAlign: TextAlign.left,
                                        ),
                                      ),
                                    ],),
                                ),
                              )

                            ],),
                          ),),
                      ),
                    ),
                  )
                ],)
              ],
            ),
          ):InternetConnection(),
        );
      }
      return Container(
        child: Center(
          child: CircularProgressIndicator(),
        ),
      );
    });


  }
}


const String _svg_y4epyl =
    '<svg viewBox="3.0 6.0 20.0 15.0" ><path transform="translate(0.0, 0.0)" d="M 5.000000953674316 6.000001907348633 L 21.0000057220459 6.000001907348633 C 22.10000228881836 6.000001907348633 23 6.843752861022949 23 7.875000476837158 L 23 19.12499809265137 C 23 20.15625190734863 22.10000228881836 21.00000381469727 21.0000057220459 21.00000381469727 L 5.000000953674316 21.00000381469727 C 3.900000333786011 21.00000381469727 3.000000476837158 20.15625190734863 3.000000476837158 19.12499809265137 L 3.000000476837158 7.875000476837158 C 3.000000476837158 6.843752861022949 3.900000333786011 6.000001907348633 5.000000953674316 6.000001907348633 Z" fill="none" stroke="#5f5c5c" stroke-width="3" stroke-opacity="0.8" stroke-linecap="round" stroke-linejoin="round" /></svg>';
const String _svg_rk51r9 =
    '<svg viewBox="3.0 7.4 20.0 10.5" ><path transform="translate(0.0, -1.58)" d="M 23 9 L 13.00000381469727 19.5 L 3.000000476837158 9" fill="none" stroke="#5f5c5c" stroke-width="3" stroke-opacity="0.8" stroke-linecap="round" stroke-linejoin="round" /></svg>';
const String _svg_oexjtm =
    '<svg viewBox="80.8 480.0 24.0 15.0" ><path transform="translate(79.34, 471.0)" d="M 14.20909118652344 14 C 13.31454658508301 11.08749961853027 10.89272785186768 9 8.045454978942871 9 C 4.434545040130615 9 1.500000238418579 12.36249923706055 1.500000238418579 16.5 C 1.500000238418579 20.63749885559082 4.434545040130615 24 8.045454978942871 24 C 10.89272785186768 24 13.31454563140869 21.91249847412109 14.20909118652344 19 L 18.95454597473145 19 L 18.95454597473145 24 L 23.31818199157715 24 L 23.31818199157715 19 L 25.5 19 L 25.5 14 L 14.20909118652344 14 Z M 8.045454978942871 19 C 6.845455646514893 19 5.86363697052002 17.875 5.86363697052002 16.5 C 5.86363697052002 15.125 6.845455646514893 14 8.045454978942871 14 C 9.245454788208008 14 10.22727394104004 15.125 10.22727394104004 16.5 C 10.22727394104004 17.875 9.245454788208008 19 8.045454978942871 19 Z" fill="#8d8888" stroke="none" stroke-width="1" stroke-miterlimit="4" stroke-linecap="butt" /></svg>';
const String _svg_ip5zo5 =
    '<svg viewBox="163.5 130.7 60.0 40.0" ><path transform="translate(157.31, 119.5)" d="M 36.19866180419922 39.18832015991211 L 58.88567733764648 12.71491432189941 C 60.56487274169922 10.75700759887695 63.28016662597656 10.75700759887695 64.94150543212891 12.71491432189941 C 66.60283660888672 14.67282104492188 66.60282897949219 17.83879852294922 64.94150543212891 19.79670715332031 L 39.23550415039062 49.79018020629883 C 37.60990524291992 51.68560028076172 35.00178527832031 51.72726058959961 33.32259368896484 49.93598556518555 L 7.43796443939209 19.81753540039062 C 6.598366737365723 18.83858108520508 6.187499046325684 17.54719543457031 6.187499046325684 16.27664184570312 C 6.187499046325684 15.00608253479004 6.598366737365723 13.71469688415527 7.43796443939209 12.73574447631836 C 9.117159843444824 10.7778377532959 11.83245658874512 10.7778377532959 13.49378776550293 12.73574447631836 L 36.19866180419922 39.18832015991211 Z" fill="#ffffff" stroke="none" stroke-width="1" stroke-miterlimit="4" stroke-linecap="butt" /></svg>';


