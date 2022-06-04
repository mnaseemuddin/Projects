


import 'package:appgallery/Resources/color.dart';
import 'package:flutter/material.dart';

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
          child: const CircularProgressIndicator(strokeWidth: 5.5,color: appBlackColor,),
          decoration: BoxDecoration(
              color: appWhiteColor,
              borderRadius: BorderRadius.all(Radius.circular(16))
          ),
        ),
        Expanded(child: Container()),
      ],),);
  }

}