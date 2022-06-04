import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:trading_apps/api/apis.dart';
import 'package:trading_apps/api/user_data.dart';
import 'package:trading_apps/custom_ui/common_widget.dart';
import 'package:trading_apps/custom_ui/progress_dialog.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/res/constants.dart';

class VerifyGmail extends StatefulWidget {
  const VerifyGmail({Key? key}) : super(key: key);

  @override
  _VerifyGmailState createState() => _VerifyGmailState();
}

class _VerifyGmailState extends State<VerifyGmail> {
  TextEditingController _controller = TextEditingController();
  TextEditingController _controllerOTP = TextEditingController();

  bool _isOTPRequested = false;
  bool _isUpdated = false;

  @override
  void initState() {
    _controller.text = userModel!.data.first.email;
    super.initState();
  }
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(elevation: 0,),
      backgroundColor: APP_PRIMARY_COLOR,
      body: !_isUpdated?Container(
        padding: EdgeInsets.all(16),
        child: ListView(children: [
          Text('Confirm Email', style: textStyleTitle(color: Colors.white),),
          SizedBox(height: 24,),
          TextFormField(
            inputFormatters: [
              LengthLimitingTextInputFormatter(35)
            ],
            controller: _controller,
            style: textStyleHeading2(color: Colors.white),
            decoration: InputDecoration(
                labelText: 'Email *',
                labelStyle: textStyleHeading(color: Colors.white54),
                filled: true,
                fillColor: Colors.white10,
                border: InputBorder.none,
                enabledBorder: OutlineInputBorder(borderSide: BorderSide(color: APP_SECONDARY_COLOR.withOpacity(0.5), width: 1,)),
                focusedBorder: OutlineInputBorder(borderSide: BorderSide(color: Colors.white, width: 1,))
            ),
          ),
          _isOTPRequested?Column(children: [
            SizedBox(height: 16,),
            Text('In a few moments you will receive a confirmation email from us. '
                'Please check your inbox and click either on link or enter the code provided', style: textStyleHeading2(color: Colors.white54),),
            SizedBox(height: 24,),
            TextFormField(
              controller: _controllerOTP,
              style: textStyleHeading2(color: Colors.white),
              decoration: InputDecoration(
                  labelText: 'Enter Code *',
                  labelStyle: textStyleHeading(color: Colors.white54),
                  filled: true,
                  fillColor: Colors.white10,
                  border: InputBorder.none,
                  enabledBorder: OutlineInputBorder(borderSide: BorderSide(color: APP_SECONDARY_COLOR.withOpacity(0.5), width: 1,)),
                  focusedBorder: OutlineInputBorder(borderSide: BorderSide(color: Colors.white, width: 1,))
              ),
            ),
          ],):Container(),
          SizedBox(height: 40,),
          _isOTPRequested?SubmitButton(onPressed: (){
            if(_controller.text.trim().isNotEmpty){
              if(_controllerOTP.text.trim().isNotEmpty) {
                if (_controllerOTP.text.length < 5) {
                  Fluttertoast.showToast(
                      msg: 'Enter correct code', gravity: ToastGravity.BOTTOM);
                }else{
                  Map body = {
                    "user_id": userModel!.data.first.userId,
                    "email_code": _controllerOTP.text.trim()
                  };

                  _emailUpdateStatusAPI(body);
                }
              }else{
                Fluttertoast.showToast(
                    msg: 'Please enter One Time Password ', gravity: ToastGravity.BOTTOM);
              }
            }else{
              Fluttertoast.showToast(
                  msg: 'Please enter your email ', gravity: ToastGravity.BOTTOM);
            }



          }, title: 'Confirm')
          :SubmitButton(onPressed: (){
              // setState(() => _isOTPRequested = true);
            if(_controller.text.trim().isNotEmpty){
              Map body = {
                "user_id": userModel!.data.first.userId,
                "email": userModel!.data.first.email
                // "email": 'ahkykqncbvxmfuezpy@uivvn.net'
              };
              _emailVerificationAPI(body);
            }else{
              Fluttertoast.showToast(msg: "Please enter your email ",gravity: ToastGravity.BOTTOM);
            }
          }, title: 'Continue')

        ],),
      )
      :UpdateView(title: 'Email verified',
          message: 'Your email verification is updated successfully', onPressed: (){
        Navigator.of(context).pop();
      }),
    );
  }

  _emailVerificationAPI(body){

    showDialog(context: context, builder: (context) => ProgressDialog(), barrierDismissible: false).then((value){
      print(value);
    },);

    emailVerificationAPI(body).then((value){
      Navigator.of(context).pop();
      if(value.status){
        if(mounted)setState(() {
          _isOTPRequested = true;
          userModel!.data.first.name = _controller.text.trim();
          setUser(userModel!);
        });
        Fluttertoast.showToast(msg: value.data['message'], backgroundColor: Colors.green.withOpacity(0.5));
      }else{
        Fluttertoast.showToast(msg: value.data['message'], backgroundColor: Colors.red.withOpacity(0.5));
      }
    });
  }

  _emailUpdateStatusAPI(body){

    showDialog(context: context, builder: (context) => ProgressDialog(), barrierDismissible: false).then((value){
      print(value);
    },);

    emailUpdateStatusAPI(body).then((value){
      Navigator.of(context).pop();
      if(value.status){
        if(mounted)setState(() {
          _isUpdated = true;
          userModel!.data.first.emailVerify = 1;
          setUser(userModel!);
        });
        Fluttertoast.showToast(msg: value.data['message'], backgroundColor: Colors.green.withOpacity(0.5));
      }else{
        Fluttertoast.showToast(msg: value.data['message'], backgroundColor: Colors.red.withOpacity(0.5));
      }
    });
  }

}
