
import 'package:flutter/material.dart';

import '../res/color.dart';


class ProgressDialog extends StatelessWidget {
  const ProgressDialog({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Dialog(
      backgroundColor: Colors.transparent,
      elevation: 0,
      child: Row(children: <Widget>[
        Expanded(child: Container()),
        Container(
          padding: const EdgeInsets.all(20),
          child: const CircularProgressIndicator(strokeWidth: 5,color: PRIMARYBLACKCOLOR,),
          decoration: const BoxDecoration(
              color: PRIMARYWHITECOLOR,
              borderRadius: BorderRadius.all(Radius.circular(16))
          ),
        ),
        Expanded(child: Container()),
      ],),);
  }
}
