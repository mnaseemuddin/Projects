


import 'package:flutter/material.dart';
import 'package:tailor/Support/UISupport.dart';

class ProgressDialogManager extends StatefulWidget {
  const ProgressDialogManager({Key? key}) : super(key: key);

  @override
  _ProgressDialogManagerState createState() => _ProgressDialogManagerState();
}

class _ProgressDialogManagerState extends State<ProgressDialogManager> {
  @override
  Widget build(BuildContext context) {
    return Dialog(
      backgroundColor: Colors.transparent,
      elevation: 0,
      child: Row(
        children: [
          Expanded(child: Container()),
          Container(
            padding: EdgeInsets.all(16),
            child: CircularProgressIndicator(strokeWidth: 5.5, color: UISupport.APP_PRIMARY_COLOR,),
            decoration: BoxDecoration(
             color: Colors.white,
                borderRadius: BorderRadius.all(Radius.circular(16))
            ),
          ),
          Expanded(child: Container())
        ],
      ),
    );
  }
}
