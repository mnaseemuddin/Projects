import 'package:flutter/material.dart';
import 'package:royal_q/app/models/common_model.dart';
import 'package:royal_q/main.dart';

import '../shared.dart';

class AcceptTerms extends StatefulWidget {
  final Function(bool)accept;
  final List<AcceptText>texts;
  const AcceptTerms({Key? key, required this.accept, required this.texts}) : super(key: key);

  @override
  _AcceptTermsState createState() => _AcceptTermsState();
}

class _AcceptTermsState extends State<AcceptTerms> {
  bool _isChecked = false;

  @override
  Widget build(BuildContext context) {
    return GestureDetector(child: Row(children: [
      GestureDetector(
        child: Icon(_isChecked?Icons.check_box:Icons.check_box_outline_blank, color: ColorConstants.white,),
        onTap: (){
          setState(() {
            _isChecked = !_isChecked;
            widget.accept(_isChecked);
          });

        },
      ),
      SizedBox(width: 8,),
      Expanded(child: RichText(text: TextSpan(
          children:
          widget.texts.map((e) => TextSpan(text: e.text,
              style: textStyleLabel(color: e.isBtn?ColorConstants.blue:ColorConstants.white70, fontSize: 14.0)),).toList()
      )))
    ],),
    onTap: (){

    },);
  }
}
