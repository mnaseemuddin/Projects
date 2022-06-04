import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:trading_apps/api/user_data.dart';
import 'package:trading_apps/custom_ui/common_widget.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/res/constants.dart';
import 'package:trading_apps/screens/auth/change_password.dart';
import 'package:trading_apps/screens/auth/login_ui.dart';
import 'package:trading_apps/screens/settings/personal.dart';
import 'package:trading_apps/screens/settings/verify_email.dart';
import 'package:trading_apps/utility/common_methods.dart';

import '../profile.dart';

class Settings extends StatelessWidget {

  const Settings({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    return Scaffold(
      appBar: AppBar(
        title: Text('Settings'),
        elevation: 0,
      ),
      backgroundColor: APP_PRIMARY_COLOR,

      body: Container(
        padding: EdgeInsets.only(left: 16, right: 16),
        child: ListView(children: [
          SizedBox(height: 40,),
          Text('Profile', style: textStyleTitle(color: Colors.white,),),
          SizedBox(height: 8,),

          SettingItem(title: 'Personal Settings', subTitle: 'Name and contacts', leadingIcon: Icons.person, onTap: () {
            navPush(context, PersonalSettings());
          },),
          // SettingItem(title: 'Account Verification', subTitle: 'Fill check identity confirmation', leadingIcon: Icons.check_box, onTap: () {
          //
          // },),
          SettingItem(title: 'Password', subTitle: 'Keep your account secure', leadingIcon: Icons.lock, onTap: () {
            navPush(context, ChangePassword());
          },),

          SizedBox(height: size.height/3,),

          GestureDetector(
            child: Container(
              padding: EdgeInsets.all(16),
              margin: EdgeInsets.all(4),
              child: Row(children: [
                Icon(Icons.logout, color: Colors.red,),
                Expanded(child: Text('Logout', style: textStyleHeading(color: Colors.red), textAlign: TextAlign.center,))
              ],),
              decoration: BoxDecoration(
                  color: Colors.white10,
                  borderRadius: BorderRadius.circular(8)
              ),
            ),
            onTap: (){
              showDialog(
                context: context,
                builder: (BuildContext context) {
                  // return object of type Dialog
                  return AlertDialog(
                    backgroundColor: Color(0xff262424),
                    content: new Text("Do you want to logout?",style: textStyleTitle(
                        color: Colors.white,fontSize: 15.0),),
                    actions: <Widget>[
                      new TextButton(
                        child: new Text("CANCEL",style: TextStyle(color: Colors.red, fontFamily: 'Helvetica Neue',
                          fontSize: 14.899999618530273,),),
                        onPressed: () {
                          Navigator.of(context).pop();
                        },
                      ),

                      new TextButton(
                        child: new Text("OK",style: TextStyle(color: Colors.red, fontFamily: 'Helvetica Neue',
                          fontSize: 14.899999618530273,),),
                        onPressed: () {
                          setDuration(1).then((value) => null);
                          setAmount(1).then((value) => null);
                          setUser(null).then((value) => Get.offAll(LoginUI()));
                        },
                      ),
                      // usually buttons at the bottom of the dialog

                    ],
                  );
                },
              );
            },
          )

        ],),
      ),

    );
  }
}


