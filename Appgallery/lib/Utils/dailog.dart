
import 'package:edge_alerts/edge_alerts.dart';
import 'package:flutter/material.dart';

class DialogUtils {
  static DialogUtils _instance = DialogUtils._();

  DialogUtils._();

  static DialogUtils get instance => _instance;

  void edgeAlerts(BuildContext context,String description){
    edgeAlert(context, title: 'Uh oh!', description: description.toString(),duration: 4,
        gravity: Gravity.bottom,icon: Icons.error,backgroundColor: Colors.black);
  }
}