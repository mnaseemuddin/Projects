
import 'package:edge_alerts/edge_alerts.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:skillcoin/api/user_data.dart';
import 'package:skillcoin/res/color.dart';

import '../api/apirepositary.dart';
import '../customui/button.dart';
import '../res/routes.dart';


class ChangePasswordPage extends StatefulWidget {
  const ChangePasswordPage({Key? key}) : super(key: key);

  @override
  _ChangePasswordPageState createState() => _ChangePasswordPageState();
}

class _ChangePasswordPageState extends State<ChangePasswordPage> {
  var oldPasswordController = TextEditingController();
  var oldObscureText = true,obscureText=true,confirmObscureText=true;
  var passwordController=TextEditingController();
  var confirmPasswordController=TextEditingController();

  final _key=GlobalKey<FormState>();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        iconTheme: const IconThemeData(
            color: PRIMARYBLACKCOLOR
        ),
        title: Text('Change Password', style: GoogleFonts.aBeeZee(fontSize: 16.0,
            color: PRIMARYBLACKCOLOR)),
      ),
      body: Form(
        key: _key,
        child: Column(
          children: [

            oldPasswordField(),
            passwordField(),
            confirmPasswordField(),
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: SubmitButton(onPressed: (){
                keyboardDismissed(context);
                if(oldPasswordController.text.trim().isEmpty){
                  return DialogUtils.instance.edgeAlerts(context, "Please Enter Current Password .");
                }


                if(passwordController.text.trim().isEmpty){
                  return DialogUtils.instance.edgeAlerts(context, "Please Enter New Password .");
                }

                if(confirmPasswordController.text.trim().isEmpty){
                  return DialogUtils.instance.edgeAlerts(context, "Please Enter Confirm Password .");
                }

                  if(passwordController.text!=confirmPasswordController.text) {
                   return DialogUtils.instance.edgeAlerts(context, "New password and confirm password do not match.");
                  }


                  _changePassword();

              },name : 'Change Password', width: double.infinity,
                textColor: PRIMARYBLACKCOLOR, circular: 30, colors: 0xfffbd536,),
            )

          ],
        ),
      ),
    );
  }

  oldPasswordField() => Padding(
    padding: const EdgeInsets.all(8.0),
    child: SizedBox(
      height: 60,
      child: TextFormField(
        controller: oldPasswordController,
        obscureText: oldObscureText,
        textInputAction: TextInputAction.next,
        inputFormatters: [
          LengthLimitingTextInputFormatter(35),
          FilteringTextInputFormatter.deny(RegExp('[ ]')),
        ],
        decoration: InputDecoration(
            errorBorder: OutlineInputBorder(
                borderRadius: BorderRadius.circular(25),
                borderSide:
                const BorderSide(width: 1, color: Color(0xffece7e7))),
            suffixIcon: IconButton(
              onPressed: () {
                setState(() {
                  if (oldObscureText) {
                    oldObscureText = false;
                  } else {
                    oldObscureText = true;
                  }
                });
              },
              icon: oldObscureText
                  ? Icon(
                Icons.visibility,
                color: Colors.grey[600],
              )
                  : const Icon(
                Icons.visibility_off,
                color: Color(0xff84a2dc),
              ),
            ),
            enabledBorder: OutlineInputBorder(
                borderRadius: BorderRadius.circular(15),
                borderSide:
                const BorderSide(width: 1.0, color: Color(0xffece7e7))),
            border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(15),
                borderSide:
                const BorderSide(width: 1.0, color: Color(0xffece7e7))),
            focusedBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(15),
              borderSide: const BorderSide(width: 1, color: Color(0xff84a2dc)),
            ),
            contentPadding: const EdgeInsets.only(top: 20, left: 15),
            hintText: 'Current Password',
            hintStyle: TextStyle(color: Colors.grey[600], fontSize: 13.5),
            fillColor: Colors.white),
      ),
    ),
  );
  passwordField() => Padding(
    padding: const EdgeInsets.all(8.0),
    child: SizedBox(
      height: 60,
      child: TextFormField(
        controller: passwordController,
        obscureText: obscureText,
        textInputAction: TextInputAction.next,
        inputFormatters: [
          LengthLimitingTextInputFormatter(35),
          FilteringTextInputFormatter.deny(RegExp('[ ]')),
        ],
        decoration: InputDecoration(
            errorBorder: OutlineInputBorder(
                borderRadius: BorderRadius.circular(25),
                borderSide:
                const BorderSide(width: 1, color: Color(0xffece7e7))),
            suffixIcon: IconButton(
              onPressed: () {
                setState(() {
                  if (obscureText) {
                    obscureText = false;
                  } else {
                    obscureText = true;
                  }
                });
              },
              icon: obscureText
                  ? Icon(
                Icons.visibility,
                color: Colors.grey[600],
              )
                  : const Icon(
                Icons.visibility_off,
                color: Color(0xff84a2dc),
              ),
            ),
            enabledBorder: OutlineInputBorder(
                borderRadius: BorderRadius.circular(15),
                borderSide:
                const BorderSide(width: 1.0, color: Color(0xffece7e7))),
            border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(15),
                borderSide:
                const BorderSide(width: 1.0, color: Color(0xffece7e7))),
            focusedBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(15),
              borderSide: const BorderSide(width: 1, color: Color(0xff84a2dc)),
            ),
            contentPadding: const EdgeInsets.only(top: 20, left: 15),
            hintText: 'New Password',
            hintStyle: TextStyle(color: Colors.grey[600], fontSize: 13.5),
            fillColor: Colors.white),
      ),
    ),
  );
  confirmPasswordField() => Padding(
    padding: const EdgeInsets.all(8.0),
    child: SizedBox(
      height: 60,
      child: TextFormField(
        controller: confirmPasswordController,
        obscureText: confirmObscureText,
        textInputAction: TextInputAction.next,
        inputFormatters: [
          LengthLimitingTextInputFormatter(35),
          FilteringTextInputFormatter.deny(RegExp('[ ]')),
        ],
        decoration: InputDecoration(
            errorBorder: OutlineInputBorder(
                borderRadius: BorderRadius.circular(25),
                borderSide:
                const BorderSide(width: 1, color: Color(0xffece7e7))),
            suffixIcon: IconButton(
              onPressed: () {
                setState(() {
                  if (confirmObscureText) {
                    confirmObscureText = false;
                  } else {
                    confirmObscureText = true;
                  }
                });
              },
              icon: confirmObscureText
                  ? Icon(
                Icons.visibility,
                color: Colors.grey[600],
              )
                  : const Icon(
                Icons.visibility_off,
                color: Color(0xff84a2dc),
              ),
            ),
            enabledBorder: OutlineInputBorder(
                borderRadius: BorderRadius.circular(15),
                borderSide:
                const BorderSide(width: 1.0, color: Color(0xffece7e7))),
            border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(15),
                borderSide:
                const BorderSide(width: 1.0, color: Color(0xffece7e7))),
            focusedBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(15),
              borderSide: const BorderSide(width: 1, color: Color(0xff84a2dc)),
            ),

            contentPadding: const EdgeInsets.only(top: 20, left: 15),
            hintText: 'Confirm Password',
            hintStyle: TextStyle(color: Colors.grey[600], fontSize: 13.5),
            fillColor: Colors.white),
      ),
    ),
  );

  void _changePassword() {

    Map<String,dynamic>body={
      "user_id":userModel!.data.first.userId,
      "oldpassword":oldPasswordController.text,
      "newpassword":passwordController.text
    };

    changePasswordAPI(body).then((value){
      if(value.status){
        oldPasswordController.clear();
        passwordController.clear();
        confirmPasswordController.clear();
        Fluttertoast.showToast(msg: value.message.toString(),gravity: ToastGravity.BOTTOM);
      }else{
        keyboardDismissed(context);
        DialogUtils.instance.edgeAlerts(context,value.message.toString());
      }
    });
  }
}
