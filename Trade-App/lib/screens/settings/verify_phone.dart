import 'dart:async';

import 'package:edge_alerts/edge_alerts.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:trading_apps/api/apis.dart';
import 'package:trading_apps/api/user_data.dart';
import 'package:trading_apps/custom_ui/common_widget.dart';
import 'package:trading_apps/custom_ui/country_flags.dart';
import 'package:trading_apps/custom_ui/progress_dialog.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/res/constants.dart';
import 'package:trading_apps/screens/settings/settings.dart';
import 'package:trading_apps/utility/common_methods.dart';


final FirebaseAuth _auth = FirebaseAuth.instance;


class ConfirmPhone extends StatefulWidget {
  const ConfirmPhone({Key? key}) : super(key: key);

  @override
  _ConfirmPhoneState createState() => _ConfirmPhoneState();
}

class _ConfirmPhoneState extends State<ConfirmPhone> {
  TextEditingController _controller = TextEditingController();
  TextEditingController _controllerOTP = TextEditingController();
  bool _isOTPRequested = false;
  Timer? _timer;
  int _timeLeft = 60;
  User? user;
  String? _verificationId;
  AuthCredential? _phoneAuthCredential;
  String _status = '';
  String _countryCode = '91';
  bool _isUpdated = false;

  var _title="Continue";

  String toomanyrequest='We have blocked all requests from this device due to unusual activity. Try again later.';
  String inCorrectCountryAndNumber="The format of the phone number provided is incorrect. Please enter correct country code and correct phone number.";

  @override
  void initState() {
    _auth.userChanges().listen((event){
      user = event;
      // if(mounted)setState(() => user = event);
    });
    // _controller.text = '+';
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(elevation: 0,),
      backgroundColor: APP_PRIMARY_COLOR,
      body: Container(
        padding: EdgeInsets.all(16),
        child: !_isUpdated?ListView(children: [
          Text('Confirm phone number', style: textStyleTitle(color: Colors.white),),
          SizedBox(height: 24,),

          Text('We do not spam call and do not impose hidden charges. A phone number is only necessary for the security of your account.', style: textStyleHeading2(color: Colors.white54),),

          SizedBox(height: 24,),

          Row(children: [
            GestureDetector(
              child: Container(
                padding: EdgeInsets.only(left: 16, right: 4,),
                margin: EdgeInsets.all(2),
                height: 60,
                child: Center(child: Row(children: [
                  Text('+$_countryCode', style: textStyleHeading2(color: Colors.white70),),
                  Icon(Icons.arrow_drop_down, color: APP_SECONDARY_COLOR,)
                ],),),
                decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(8),
                    border: Border.all(color: APP_SECONDARY_COLOR.withOpacity(0.5), width: 1)
                ),
              ),
              onTap: ()async{
                var res = await navPush(context, CountryFlags());
                if(res!=null){
                  String code = res['code'];
                  print('Code $code');
                  setState(() {
                    _countryCode = '$code';
                  });
                }
              },
            ),
            Expanded(child: TextFormField(
              inputFormatters: [
                LengthLimitingTextInputFormatter(15),
              ],
              controller: _controller,
              style: textStyleHeading2(color: Colors.white),
              keyboardType: TextInputType.number,
              decoration: InputDecoration(
                  hintText: 'Phone Number *',
                  labelText: 'Phone Number *',
                  labelStyle: textStyleHeading(color: Colors.white54),
                  filled: true,
                  fillColor: Colors.white10,
                  border: InputBorder.none,
                  enabledBorder: OutlineInputBorder(borderSide: BorderSide(color: APP_SECONDARY_COLOR.withOpacity(0.5), width: 1,)),
                  focusedBorder: OutlineInputBorder(borderSide: BorderSide(color: Colors.white, width: 1,))
              ),
            )),
          ],),
          SizedBox(height: 40,),

          _isOTPRequested?Column(children: [
            SizedBox(height: 16,),
            Text('In a few moments you will get an SMS with a code. '
                'Please enter the code to confirm your phone number', style: textStyleHeading2(color: Colors.white54),),
            SizedBox(height: 24,),
            TextFormField(
              keyboardType: TextInputType.number,
              controller: _controllerOTP,
              inputFormatters: [
                LengthLimitingTextInputFormatter(6),
              ],
              style: textStyleHeading2(color: Colors.white),
              decoration: InputDecoration(
                  labelText: 'Enter Code',
                  labelStyle: textStyleHeading(color: Colors.white54),
                  filled: true,
                  fillColor: Colors.white10,
                  border: InputBorder.none,
                  enabledBorder: OutlineInputBorder(borderSide: BorderSide(color: APP_SECONDARY_COLOR.withOpacity(0.5), width: 1,)),
                  focusedBorder: OutlineInputBorder(borderSide: BorderSide(color: Colors.white, width: 1,))
              ),
            ),
          ],):Container(),

          _isOTPRequested?SubmitButton(onPressed: (){
            FocusScope.of(context).unfocus();
           if(_controller.text.trim().isNotEmpty) {
             if (_controllerOTP.text.length < 5) {
               DialogUtils.instance.edgeAlerts(context, 'Enter correct code');
             } else {
               showDialog(context: context, builder: (context) => ProgressDialog(),
                   barrierDismissible: false).then((value) {
               });
               _submitOTP();
             }
           }else{
             DialogUtils.instance.edgeAlerts(context,'Please enter phone number');
           }
          }, title: 'Confirm')
              :SubmitButton(onPressed: (){
        if(_controller.text.trim().isNotEmpty){
          keyboardDismissed(context);
          bool isValid = RegExp(r"^(?:[+0]9)?[0-9]{10}$").hasMatch(_controller.text);
          if(isValid){
            showDialog(context: context, builder: (context) => ProgressDialog(), barrierDismissible: false).then((value){
              print(value);
            },);
            _auth.verifyPhoneNumber(
                phoneNumber: '+$_countryCode-${_controller.text.trim()}',
             //   phoneNumber: '+1-${_controller.text.trim()}',
                timeout: Duration(seconds: 60),
                verificationCompleted: (user){
                  print('user => $user\n${user.smsCode}');
                },
                verificationFailed: (exception){
                  Navigator.of(context).pop();
                  print('exception code=> '+exception.code);
                  if(exception.code=="too-many-requests"){
                    DialogUtils.instance.edgeAlerts(context,toomanyrequest);
                  }else if(exception.code=="invalid-phone-number"){
                    DialogUtils.instance.edgeAlerts(context,inCorrectCountryAndNumber);
                  }else{
                    DialogUtils.instance.edgeAlerts(context,"Something went wrong.");
                  }
                  print('exception => $exception');
                },
                codeSent: (verificationId, forceResendingToken){
                  Navigator.of(context).pop();
                  setState((){
                    _isOTPRequested = true;
                    _timeLeft = 60;
                    _timer = Timer.periodic(Duration(seconds: 1), (timer){
                      if(mounted)
                        setState(() {
                          _timeLeft -= 1;
                          if(_timeLeft<=0){
                            _isOTPRequested = false;
                            _title="Resend";
                            _timer!.cancel();
                          }
                        });
                    });
                  });
                  _verificationId = verificationId;
                  print('verificationId => $verificationId  forceResendingToken => $forceResendingToken');
                },
                codeAutoRetrievalTimeout: (verificationId){
                  Navigator.of(context).pop();
                  _verificationId = verificationId;
                  print('exception => $verificationId');
                }
            );
          }else{
            DialogUtils.instance.edgeAlerts(context,'Enter valid phone number');
          }

        }else{
          DialogUtils.instance.edgeAlerts(context,'Please enter phone number');
        }
          }, title: _title),
          SizedBox(height: 24,),
          _isOTPRequested?Text('Resend code in ${_formatTime(_timeLeft)}', style: textStyleHeading2
            (color: Colors.white54),):Container()

        ],)
            :UpdateView(title: 'Contact verified', message: 'Your contact number(+$_countryCode-${_controller.text}) '
            'is successfully verified\nPress button to continue', onPressed: (){
              Get.off(Settings());
        }),
      ),
    );
  }

  String _formatTime(int time){
    int min = time~/60;
    int sec = time - min*60;
    String fTime = '${min<10?'0':''}$min:${sec<10?'0':''}$sec';
    return fTime;
  }

  void _submitOTP() {
    /// get the `smsCode` from the user
    String smsCode = _controllerOTP.text.toString().trim();

    /// when used different phoneNumber other than the current (running) device
    /// we need to use OTP to get `phoneAuthCredential` which is inturn used to signIn/login

      this._phoneAuthCredential = PhoneAuthProvider.credential(
          verificationId: this._verificationId!, smsCode: smsCode);
      _login();

  }

  Future<void> _login() async {
    /// This method is used to login the user
    /// `AuthCredential`(`_phoneAuthCredential`) is needed for the signIn method
    /// After the signIn method from `AuthResult` we can get `FirebaserUser`(`_firebaseUser`)
  //  try {
      await _auth.signInWithCredential(this._phoneAuthCredential!).then((authRes) {
        user = authRes.user;
        print(user.toString());

        Map body = {
          "user_id": userModel!.data.first.userId,
          "mobile": '+$_countryCode-${_controller.text}',
          "mobile_verify": 1,
        };
        _mobileUpdateStatusAPI(body);
        Navigator.of(context).pop();

      }).catchError((e){
        Navigator.of(context).pop();
        DialogUtils.instance.edgeAlerts(context,e.toString());
      });
     // if(mounted)
      // setState(() {
      //   _status += 'Signed In\n';
      //   print('Status => $_status');
      // });
      // Map body = {
      //   "user_id": userModel!.data.first.userId,
      //   "mobile": _controller.text.trim(),
      //   "mobile_verify": 1,
      // };
      // _mobileUpdateStatusAPI(body);
   //  } catch (e) {
   //    _edgeAlert(e.toString());
   // //   _handleError(e);
   //  }
  }

  void _handleError(e) {
    print(e.message);
    if(mounted)
    setState(() {
      _status += e.message + '\n';
      print('Status => $_status');
    });
  }

  _mobileUpdateStatusAPI(Map body){

    showDialog(context: context, builder: (context) => ProgressDialog(),
        barrierDismissible: false).then((value){
      print(value);

    mobileUpdateStatusAPI(body).then((value){
        Navigator.of(context).pop();
        if(value.status){
          userModel!.data.first.mobile = '+$_countryCode-${_controller.text}';
          userModel!.data.first.mobileVerify = 1;
          setUser(userModel!);
          _isUpdated = true;
        }
      },);
    });
  }
}

