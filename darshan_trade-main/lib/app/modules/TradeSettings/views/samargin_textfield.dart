import 'package:flutter/material.dart';
import 'package:royal_q/app/modules/TradeSettings/views/input_cell.dart';
import 'package:royal_q/app/shared/shared.dart';

class SAMarginTextField extends StatelessWidget {
  final String unit;
  final TextEditingController controller;
  final Function(String)?onChanged;
  final bool isInt;
  final bool isDisable;
  const SAMarginTextField({Key? key, required this.unit, required this.controller, this.onChanged, this.isInt=false, this.isDisable=false,}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Stack(children: [
      Container(
          color: ColorConstants.white10,
          padding: EdgeInsets.all(0),
          child: Row(children: [
            InputCell(controller: controller, align: TextAlign.center, onChanged: onChanged, isInt: isInt,),
            Text(unit, style: textStyleLabel(),),
            SizedBox(width: 4,)
          ],)),
      isDisable?Positioned(
          left: 0, right: 0, top: 0, bottom: 0,
          child: Container(color: ColorConstants.white12)):SizedBox(height: 0,)
    ],);
  }
}