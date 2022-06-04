



import 'package:edge_alerts/edge_alerts.dart';
import 'package:flutter/material.dart';
import 'package:skillcoin/res/color.dart';

  navPush(context,screen)=>Navigator.push(context, MaterialPageRoute(
    builder: (context)=>screen));


navPushReplace(context,screen)=>Navigator.pushReplacement(context, MaterialPageRoute(
    builder: (context)=>screen));

  navPushOnBackPressed(context)=>Navigator.of(context).pop();

keyboardDismissed(context)=> FocusScope.of(context).unfocus();

class DialogUtils {
  static DialogUtils _instance = DialogUtils._();

  DialogUtils._();

  static DialogUtils get instance => _instance;

  void edgeAlerts(BuildContext context,String description){
    edgeAlert(context, title: 'Uh oh!', description: description.toString(),duration: 2,
        gravity: Gravity.bottom,icon: Icons.error,backgroundColor: PRIMARYYELLOWCOLOR);
  }
}