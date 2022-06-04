import 'package:edge_alerts/edge_alerts.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get_utils/src/extensions/string_extensions.dart';
import 'package:trading_apps/api/apis.dart';
import 'package:trading_apps/api/user_data.dart';
import 'package:trading_apps/custom_ui/common_widget.dart';
import 'package:trading_apps/custom_ui/progress_dialog.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/res/constants.dart';
import 'package:trading_apps/utility/common_methods.dart';

class ForgotPassword extends StatefulWidget {
  const ForgotPassword({Key? key}) : super(key: key);

  @override
  _ForgotPasswordState createState() => _ForgotPasswordState();
}

class _ForgotPasswordState extends State<ForgotPassword> {

  TextEditingController _controller = TextEditingController();
  bool _isUpdated = false;


  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(elevation: 0,),
      backgroundColor: APP_PRIMARY_COLOR,
      body: !_isUpdated?Container(
        padding: EdgeInsets.all(16),
        child: ListView(children: [
          Text('Forgot Password', style: textStyleTitle(color: Colors.white),),
          SizedBox(height: 24,),
          TextFormField(
            controller: _controller,
             textInputAction: TextInputAction.done,
            inputFormatters: [
     // LengthLimitingTextInputFormatter(38),
      FilteringTextInputFormatter.deny(RegExp('[ ]')),
    ],
            style: textStyleHeading2(color: Colors.white),
            decoration: InputDecoration(
              hintText: 'Enter your registered email',
                labelText: 'Registered Email *',
                labelStyle: textStyleHeading(color: Colors.white54),
                filled: true,
                fillColor: Colors.white10,
              border: InputBorder.none,
              enabledBorder: OutlineInputBorder(borderSide: BorderSide(color: APP_SECONDARY_COLOR.withOpacity(0.5), width: 1,)),
              focusedBorder: OutlineInputBorder(borderSide: BorderSide(color: Colors.white, width: 1,))
            ),
          ),
          SizedBox(height: 40,),

          SubmitButton(onPressed: (){
            FocusScope.of(context).unfocus();
            bool emailValid = RegExp(r"^[a-zA-Z0-9.a-zA-Z0-9.#$%&'*+-/=?^_`{|}~]+@[a-zA-Z0-9]+\.[a-zA-Z]+")
                .hasMatch(_controller.text.trim());


            if(!emailValid){
              DialogUtils.instance.edgeAlerts(context,'Your email address isn’t formatted correctly.');
              // Fluttertoast.showToast(msg: 'Enter valid email id',
              // backgroundColor: Colors.red.withOpacity(0.5),
              // gravity: ToastGravity.CENTER);
              return;
            }

            Map body = {
              "email": _controller.text.trim()
            };

            _forgotPasswordAPI(body);

          }, title: 'Recover Password')

        ],),
      )
      :UpdateView(title: 'Password sent',
          message: 'Password sent on your email: ${_controller.text.trim()}, please check your email',
          btnText: 'Login',
          onPressed: (){
        Navigator.of(context).pop();
      }),
    );
  }

  _forgotPasswordAPI(body){

    showDialog(context: context, builder: (context) => ProgressDialog(), barrierDismissible: false).then((value){
      print(value);
    },);

    forgotPasswordAPI(body).then((value){
      Navigator.of(context).pop();
      if(value.status){
         if(mounted)
        setState(() {
          _isUpdated = true;
          // userModel!.data.first.name = _controller.text.trim();
          // setUser(userModel!);
        });
        Fluttertoast.showToast(msg: value.data['message'], backgroundColor: Colors.green.withOpacity(0.5));
      }else{
        DialogUtils.instance.edgeAlerts(context,value.data['message'].toString());
     //   Fluttertoast.showToast(msg: value.data['message'], backgroundColor: Colors.red.withOpacity(0.5));
      }
    });
  }
}
