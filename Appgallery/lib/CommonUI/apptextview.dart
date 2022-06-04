
import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

class HeadingText extends StatelessWidget {
  double? fontSize=14;
  int? maxLines;
  String title;
  Color? color=Colors.black;

   HeadingText({Key? key,required this.title,this.fontSize,
     this.color,this.maxLines}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Text(
      title,maxLines: maxLines,
      style: GoogleFonts.aBeeZee(
          fontSize: fontSize,
          color: color),
    );;
  }
}


class HeadingText1 extends StatelessWidget {
  double? fontSize=14;
  int? maxLines;
  String title;
  Color? color=Colors.black;

  HeadingText1({Key? key,required this.title,this.fontSize,
    this.color,this.maxLines}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Text(
      title,maxLines: maxLines,
      style: TextStyle(
          fontSize: fontSize,
          color: color),
    );;
  }
}



class HeadingTextFW600 extends StatelessWidget {
  double? fontSize=14;
  int? maxLines;
  String title;
  Color? color=Colors.black;


  HeadingTextFW600({Key? key,required this.title,this.fontSize,
    this.color,this.maxLines}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Text(
      title,maxLines: maxLines,
      style: TextStyle(
          fontSize: fontSize,
          color: color,fontWeight: FontWeight.w600),
    );;
  }
}


class HeadingTextFontWtW500 extends StatelessWidget {
  double? fontSize=14;
  int? maxLines;
  String title;
  Color? color=Colors.black;


  HeadingTextFontWtW500({Key? key,required this.title,this.fontSize,
    this.color,this.maxLines}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Text(
      title,maxLines: maxLines,
      style: TextStyle(
          fontSize: fontSize,
          color: color,fontWeight: FontWeight.w500),
    );;
  }
}


