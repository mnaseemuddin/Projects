import 'package:flutter/material.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:royal_q/main.dart';
import 'package:get/get.dart';

class SASearch extends StatefulWidget {
  final String hintText;
  final bool isPrefix;
  final Function(String)onChange;
  const SASearch({Key? key, this.hintText='Search currency name', required this.onChange, this.isPrefix=true}) : super(key: key);

  @override
  _SASearchState createState() => _SASearchState();
}

class _SASearchState extends State<SASearch> {
  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.only(right: 8),
      child: TextFormField(
        onChanged: widget.onChange,
        cursorColor: ColorConstants.white70,
        style: TextStyle(color:  ColorConstants.white70),
        textAlign: widget.isPrefix?TextAlign.left:TextAlign.right,
        decoration: InputDecoration(
          hintStyle: TextStyle(color:  ColorConstants.white38),
          hintText: widget.hintText,
          border: InputBorder.none,
          prefixIcon: widget.isPrefix?Icon(Icons.search, color:  ColorConstants.white54,):null,
          suffixIcon: widget.isPrefix?null:Icon(Icons.search, color:  ColorConstants.white54,)
        ),
      ),
      decoration: BoxDecoration(
        color:  ColorConstants.whiteRev,
        borderRadius: BorderRadius.circular(30),
      ),
    );
  }
}
