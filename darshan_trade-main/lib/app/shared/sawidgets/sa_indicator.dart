import 'dart:async';

import 'package:flutter/material.dart';
import 'package:royal_q/app/shared/constants/colors.dart';

class SAIndicator extends StatefulWidget {
  final StreamController<int> onPageChange;
  final int len;
  final int current;
  const SAIndicator({Key? key, required this.onPageChange, required this.len, this.current=0}) : super(key: key);

  @override
  _SAIndicatorState createState() => _SAIndicatorState();
}

class _SAIndicatorState extends State<SAIndicator> {
  int _current = 0;
  @override
  void initState() {
    super.initState();
    _current = widget.current;
    widget.onPageChange.stream.listen((event) {
      if(mounted) {
        setState(() {
          _current = event;
        });
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return _createIndicator();
  }

  Widget _createIndicator(){
    List<Widget> list = [];
    for(int i=0; i<widget.len; i++){
      double radius = i==_current?10:8;
      list.add(Container(
        margin: EdgeInsets.all(4),
        width: radius, height: radius,
        decoration: BoxDecoration(
            color: i==_current?ColorConstants.white:Colors.transparent,
            shape: BoxShape.circle,
            border: Border.all(color: ColorConstants.white, width: 1,)
        ),
      ));
    }

    return Row(
      mainAxisSize: MainAxisSize.min,
      children: list,);
  }
}
