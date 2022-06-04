import 'dart:async';

import 'package:flutter/material.dart';
import 'package:royal_q/app/shared/constants/colors.dart';
import 'package:royal_q/app/shared/constants/common.dart';

class SACountDown extends StatefulWidget {
  final Function() onComplete;
  const SACountDown({Key? key, required this.onComplete}) : super(key: key);

  @override
  _SACountDownState createState() => _SACountDownState();
}

class _SACountDownState extends State<SACountDown> {
  late Timer _timer;
  int _delay = 120;

  @override
  void initState() {
    super.initState();
    _timer = Timer.periodic(Duration(seconds: 1), (timer) {
      if(mounted) {
        setState(() {
        _delay -=1;
        if(_delay<=0){
          _timer.cancel();
          widget.onComplete();
        }
      });
      }
    });
  }

  @override
  void dispose() {
    _timer.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: EdgeInsets.all(4),
      width: 40,
      height: 40,
      alignment: Alignment.center,
      child: Text('$_delay', style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),),

      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(30),
        border: Border.all(width: 1, color: ColorConstants.APP_SECONDARY_COLOR, style: BorderStyle.solid)
      ),
    );
  }
}
