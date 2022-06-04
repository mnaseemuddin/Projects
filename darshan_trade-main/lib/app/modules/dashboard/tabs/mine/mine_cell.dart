import 'package:flutter/material.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:royal_q/main.dart';


class MineCell extends StatefulWidget {
  final IconData icon;
  final String title;
  final Widget trailing;
  final Widget subview;
  final Function()?onPressed;
  const MineCell({Key? key, required this.icon, required this.title,
    this.trailing=const Icon(Icons.chevron_right, color: ColorConstants.white54,),
    this.subview=const SizedBox(), this.onPressed}) : super(key: key);

  @override
  _MineCellState createState() => _MineCellState();
}

class _MineCellState extends State<MineCell> {
  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      child: Container(
        child: Row(children: [
          Icon(widget.icon, color: ColorConstants.white54,),
          SizedBox(width: 16,),
          Expanded(child: Text(widget.title, style: textStyleLabel(),)),
          widget.subview,
          widget.trailing
        ],),
        padding: EdgeInsets.only(left: 32, right: 16, top: 16, bottom: 16),
        decoration: BoxDecoration(
            border: Border(bottom: BorderSide(color: ColorConstants.white24, width: 0.5))
        ),
      ),
      onTap: widget.onPressed,
    );
  }
}
