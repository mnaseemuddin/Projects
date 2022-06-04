import 'package:appgallery/CommonUI/apptextview.dart';
import 'package:appgallery/CommonUI/commonwidget.dart';
import 'package:appgallery/Resources/color.dart';
import 'package:appgallery/Utils/dailog.dart';
import 'package:appgallery/apis/userdata.dart';
import 'package:edge_alerts/edge_alerts.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:google_fonts/google_fonts.dart';

import '../../../Utils/routes.dart';
import '../../../apis/apirepositary.dart';
import '../Core/developerconsoleactivity.dart';


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
          color: appBlackColor
        ),
        title: Text('Change Password', style: GoogleFonts.aBeeZee(fontSize: 16.0,
            color: appBlackColor)),
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
                if(_key.currentState!.validate()){
                  if(passwordController.text==confirmPasswordController.text) {
                    _changePassword(consoleUserModel!.email.toString());
                  }else{
                    DialogUtils.instance.edgeAlerts(context, "New password and confirm password do not match.");
                  }
                }
              }, buttonName: 'Change Password'),
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
            validator: (v) {
              if (v!.trim().isEmpty) {
                return 'Please Enter Current Password .';
              }
              return null;
            },
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
                prefixIcon: Padding(
                  padding: const EdgeInsets.all(15.0),
                  child: Image.asset(
                    'assets/lock.png',
                    height: 8,
                    width: 8,
                    color: Colors.blue,
                  ),
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
        validator: (v) {
          if (v!.trim().isEmpty) {
            return 'Please Enter New Password .';
          }
          return null;
        },
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
            prefixIcon: Padding(
              padding: const EdgeInsets.all(15.0),
              child: Image.asset(
                'assets/lock.png',
                height: 8,
                width: 8,
                color: Colors.blue,
              ),
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
        validator: (v) {
          if (v!.trim().isEmpty) {
            return 'Please Enter Old Password .';
          }
          return null;
        },
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
            prefixIcon: Padding(
              padding: const EdgeInsets.all(15.0),
              child: Image.asset(
                'assets/lock.png',
                height: 8,
                width: 8,
                color: Colors.blue,
              ),
            ),
            contentPadding: const EdgeInsets.only(top: 20, left: 15),
            hintText: 'Confirm Password',
            hintStyle: TextStyle(color: Colors.grey[600], fontSize: 13.5),
            fillColor: Colors.white),
      ),
    ),
  );

  void _changePassword(String email) {

    Map<String,dynamic>body={
      "email_address":email,
      "oldpassword":oldPasswordController.text,
      "newpassword":passwordController.text
    };

    changePasswordAPI(body).then((value){
      if(value.status){
        Fluttertoast.showToast(msg: value.message.toString(),gravity: ToastGravity.BOTTOM);
        navPush(context, const DeveloperConsoleActivity());
      }else{
        keyboardDismissed(context);
       DialogUtils.instance.edgeAlerts(context,value.message.toString());
      }
    });
  }
}
