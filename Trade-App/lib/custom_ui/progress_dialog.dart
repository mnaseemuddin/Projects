import 'package:flutter/material.dart';
import 'package:trading_apps/res/colors.dart';

class ProgressDialog extends StatefulWidget {
  
  @override
  _ProgressDialogState createState() => _ProgressDialogState();
}

class _ProgressDialogState extends State<ProgressDialog> {
  @override
  Widget build(BuildContext context) {
    return Dialog(
      backgroundColor: Colors.transparent,
      elevation: 0,
      child: Row(children: <Widget>[
        Expanded(child: Container()),
        Container(
          padding: EdgeInsets.all(16),
          child: CircularProgressIndicator(strokeWidth: 5.5,),
          decoration: BoxDecoration(
            color: APP_SECONDARY_COLOR,
            borderRadius: BorderRadius.all(Radius.circular(16))
          ),
        ),
        Expanded(child: Container()),
      ],),);
  }

}
