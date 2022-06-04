import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:trading_apps/api/apis.dart';
import 'package:trading_apps/api/user_data.dart';
import 'package:trading_apps/custom_ui/common_widget.dart';
import 'package:trading_apps/custom_ui/progress_dialog.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/res/constants.dart';
import 'package:trading_apps/res/strings.dart';
import 'package:trading_apps/screens/auth/login_ui.dart';
import 'package:trading_apps/utility/common_methods.dart';

class ChangePassword extends StatefulWidget {
  const ChangePassword({Key? key}) : super(key: key);

  @override
  _ChangePasswordState createState() => _ChangePasswordState();
}

class _ChangePasswordState extends State<ChangePassword> {

  TextEditingController _controllerOld = TextEditingController();
  TextEditingController _controllerNew = TextEditingController();
  TextEditingController _controllerCon = TextEditingController();

  bool _isPasswordUpdated = false;

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    return Scaffold(
      appBar: AppBar(
        elevation: 0,
        title: Text('Change Password'),
      ),
      backgroundColor: APP_PRIMARY_COLOR,
      body: Container(
        padding: EdgeInsets.all(16),
        child: ListView(
          children: [
            _Item(controller: _controllerOld, title: 'Old Password'),
            _Item(controller: _controllerNew, title: 'New Password'),
            _Item(controller: _controllerCon, title: 'Confirm Password'),
            SizedBox(height: 40,),
            SubmitButton(onPressed: (){
              keyboardDismissed(context);
              if(_controllerOld.text.length<6){
                return DialogUtils.instance.edgeAlerts(context, 'Password length must be greater then 5');
              }

              if(_controllerNew.text.length<6){
                return DialogUtils.instance.edgeAlerts(context, 'Password length must be greater then 5');
              }

              if(_controllerNew.text != _controllerCon.text){
                return DialogUtils.instance.edgeAlerts(context, 'Old Password & New Password do not matched');
                 // Fluttertoast.showToast(msg: '');
              }

              Map body = {
                "user_id": userModel!.data.first.userId,
                "old_password": _controllerOld.text.trim(),
                "new_password": _controllerNew.text.trim(),
                "confirm_password": _controllerCon.text.trim(),
              };


              print('Controller _____${_controllerOld.text}',);

              _updatePasswordAPI(body);

            }, title: 'Change Password', )
          ],
        ),
      )
    );
  }

  _updatePasswordAPI(body){

    showDialog(context: context, builder: (context) => ProgressDialog(), barrierDismissible: false).then((value){
      print(value);
    },);

    updatePasswordAPI(body).then((value){
      Navigator.of(context).pop();
      if(value.status){
        if(mounted)setState(() {
          _isPasswordUpdated = true;
         Navigator.of(context).pop();
        });
        Fluttertoast.showToast(msg: value.data['message'], backgroundColor: Colors.green.withOpacity(0.5));
      }else{
       DialogUtils.instance.edgeAlerts(context,value.data['message']);
      }
    });
  }

}

class _Item extends StatelessWidget {
  final TextEditingController controller;
  final String title,hintText;
  const _Item({Key? key, required this.controller, required this.title,this.hintText=''}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      child: TextFormField(
        controller: controller,
        obscureText: false,
        inputFormatters: [
          FilteringTextInputFormatter.deny(RegExp('[ ]')),
        ],
        style: textStyleHeading2(color: Colors.white),
        decoration: InputDecoration(
            labelText: title+" *",
            hintText: 'At least 6 characters',
            labelStyle: textStyleHeading(color: Colors.white54),
            filled: true,
            fillColor: Colors.white10,
            border: InputBorder.none,
            enabledBorder: OutlineInputBorder(borderSide: BorderSide(color: APP_SECONDARY_COLOR.withOpacity(0.5), width: 1,)),
            focusedBorder: OutlineInputBorder(borderSide: BorderSide(color: Colors.white, width: 1,))
        ),
      ),
      padding: EdgeInsets.only(bottom: 8),
    );
  }
}
