import 'dart:io';
import 'dart:ui';

import 'package:edge_alerts/edge_alerts.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:get/get.dart';
import 'package:provider/provider.dart';
import 'package:trading_apps/api/apis.dart';
import 'package:trading_apps/api/user_data.dart';
import 'package:trading_apps/custom_ui/common_widget.dart';
import 'package:trading_apps/custom_ui/progress_dialog.dart';
import 'package:trading_apps/custom_ui/sh_textfield.dart';
import 'package:trading_apps/custom_ui/toggle_button.dart';
import 'package:trading_apps/models/common_model.dart';
import 'package:trading_apps/models/user_model.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/res/constants.dart';
import 'package:trading_apps/res/strings.dart';
import 'package:trading_apps/screens/auth/forgot_password.dart';
import 'package:trading_apps/screens/dashboard.dart';
import 'package:trading_apps/utility/common_methods.dart';
import 'package:trading_apps/utility/connectivityprovider.dart';

class LoginUI extends StatefulWidget {
  const LoginUI({Key? key}) : super(key: key);

  @override
  _LoginUIState createState() => _LoginUIState();
}

class _LoginUIState extends State<LoginUI> {

  bool      _isLogin   = true;
  FieldData _email     = FieldData(data: '', valid: false);
  FieldData _password  = FieldData(data: '', valid: false);
  TextEditingController _controllerEmail    = TextEditingController();
  TextEditingController _controllerPassword = TextEditingController();

  bool IsTermCondition=false;

  @override
  void initState() {
    Provider.of<ConnectivityProvider>(context,listen: false).startMonitoring();
  //  _scrollDown();
    super.initState();
  }

  //final ScrollController _controller = ScrollController();

// // This is what you're looking for!
//     void _scrollDown() {
//       _controller.jumpTo(_controller.position.maxScrollExtent);
//     }


  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    return Consumer<ConnectivityProvider>(
        builder: (ctx,data,child){
          if(data.isOnline!=null){
            return data.isOnline?WillPopScope(
              onWillPop: (){
                showDialog(
                  context: context,
                  builder: (BuildContext context) {
                    // return object of type Dialog
                    return AlertDialog(
                      backgroundColor: Color(0xff262424),
                      title: Text("Exit?",style: textStyleTitle(
                      color: Colors.white,fontSize: 15.0)),
                      content: new Text("Are you sure you want to exit?",style: textStyleTitle(
                    color: Colors.white,fontSize: 15.0)),
                      actions: <Widget>[
                        new TextButton(
                          child: new Text(
                            "CANCEL",
                            style: TextStyle(
                              color: Colors.red,
                              fontFamily: 'Helvetica Neue',
                              fontSize: 14.899999618530273,
                            ),
                          ),
                          onPressed: () {
                            Navigator.of(context).pop();
                          },
                        ),

                        new TextButton(
                          child: new Text(
                            "OK",
                            style: TextStyle(
                              color: Colors.red,
                              fontFamily: 'Helvetica Neue',
                              fontSize: 14.899999618530273,
                            ),
                          ),
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
                resizeToAvoidBottomInset: true,
                backgroundColor: APP_PRIMARY_COLOR,

                body: Container(
                  padding: EdgeInsets.all(16),
                  child: ListView(
                   // controller: _controller,
                    children: [
                    SizedBox(height: size.height/10,),
                    Image.asset(tcLogoWhite, width: size.width/2, height: size.height/10,),
                    SizedBox(height: 16,),

                    ToggleButton(onSelect: (val){
                      setState((){
                        _isLogin = val;
                        _controllerEmail.clear();
                        _controllerPassword.clear();
                      });
                    }, selected: _isLogin,),

                    SizedBox(height: 16,),

                    AppTextField(controller: _controllerEmail, onChanged: (data){
                      _email = data;
                    },validate: VALIDATE.EMAIL, labelText: EMAIL),

                    AppTextField1(controller: _controllerPassword, onChanged: (data){
                      _password = data;
                    }, labelText: PASSWORD,obscureText: true,
                      icon: Icons.remove_red_eye, validate: VALIDATE.PASSWORD,hintText:
                      'At least 6 characters',),



                    !_isLogin?ListSelection(list: ['USD \$'], onSelct: (val){},):
                    Container(width: 0,),

                    _isLogin?Align(alignment: Alignment.topRight,
                      child: TextButton(onPressed: (){

                        navPush(context, ForgotPassword());

                      }, child: Text(FORGOT_PASSWORD, style: TextStyle(color: APP_SECONDARY_COLOR),)),)
                        :Container(width: 0,),

                    !_isLogin?AcceptButton(onAccept: (val){
                      IsTermCondition=val;
                    }):Container(width: 0,),

                    SizedBox(height: 24,),
                    SubmitButton(title: _isLogin?LOGIN:SIGN_UP, onPressed: (){
                      keyboardDismissed(context);
                      if(_controllerEmail.text.trim().isNotEmpty){
                        if(_email.valid){
                          if(_controllerPassword.text.trim().isNotEmpty){
                            if(_password.valid){
                              if(_isLogin==false){
                                if(IsTermCondition==false){
                                  DialogUtils.instance.edgeAlerts(context,'Please Accept Term & Condition.');
                                  //  Fluttertoast.showToast(msg: 'Please accept Term & Condition ', gravity: ToastGravity.BOTTOM);
                                }else{
                                  Map<String, String>body = {
                                    'email': _email.data.toString().toLowerCase(),
                                    'password': _password.data.toString()
                                  };
                                  _isLogin ? _signInAPI(body) : _signUpAPI(body);
                                }
                              }else{
                                Map<String, String>body = {
                                  'email': _email.data.toString().toLowerCase(),
                                  'password': _password.data.toString()
                                };
                                _isLogin ? _signInAPI(body) : _signUpAPI(body);
                              }

                            }else{
                              DialogUtils.instance.edgeAlerts(context,'Passwords length must be 5 or more characters.');
                            }
                          }else{
                            DialogUtils.instance.edgeAlerts(context,'Please enter Password.');
                          }
                        }else{
                          DialogUtils.instance.edgeAlerts(context,'Your email address isn’t formatted correctly.');
                        }
                      }else{
                        //_edgeAlert('Please enter Email address.');
                        DialogUtils.instance.edgeAlerts(context,'Please enter Email address.');
                        // Fluttertoast.showToast(msg: 'Please enter email address ', gravity: ToastGravity.BOTTOM);
                      }

                    }),

                    SizedBox(height: 40,),

                  ],),
                ),
              ),
            ):NoInternetConnection();
          }
          return Container(
            child: Center(
              child: CircularProgressIndicator(),
            ),
          );
    });
  }

  _signUpAPI(Map body){
    showDialog(context: context, builder: (context) => ProgressDialog(), barrierDismissible: false).then((value){
      print(value);
    },);

    signUpAPI(body).then((value){
      Navigator.of(context).pop();
      ApiResponse response = value;
      Map map = value.data;
      if(response.status){
        if(mounted)setState(() {
          Get.offAll(LoginUI());
        });
      }
     // DialogUtils.instance.edgeAlerts(context,map['message']);
      print(value.data.toString());
      Fluttertoast.showToast(msg:map['message'], gravity: ToastGravity.BOTTOM);
    });
  }


  _signInAPI(Map<String, String> body){
    showDialog(context: context, builder: (context) => ProgressDialog(),
        barrierDismissible: false).then((value){
      print(value);
    },);

    loginAPI(body).then((value){
      Navigator.of(context).pop();
      ApiResponse response = value;

      if(response.status){
        UserModel model = response.data;
        userModel  = model;
        if(userModel!=null)setUser(userModel!);
        Fluttertoast.showToast(msg: model.message, gravity: ToastGravity.BOTTOM);
        navPushReplace(context, Dashboard(from: '',));
      }else{
        Map map = value.data;
        DialogUtils.instance.edgeAlerts(context,map['message'].toString());
       // Fluttertoast.showToast(msg: map['message'], gravity: ToastGravity.BOTTOM);
      }
    });
  }
}




