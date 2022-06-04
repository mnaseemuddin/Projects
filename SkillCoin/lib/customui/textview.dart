
import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

class NormalHeadingText extends StatelessWidget {

  final String title;
 final double fontSize;
 final Color color;
  const NormalHeadingText({Key? key,required this.title,required this.fontSize,
    required this.color}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Text(title,style: TextStyle(
        fontSize: fontSize,color: color),);
  }
}


class BoldHeadingText extends StatelessWidget {

  final String title;
  final double fontSize;
  final Color color;
  const BoldHeadingText({Key? key,required this.title,required this.fontSize,
    required this.color}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Text(title,style: TextStyle(
        fontSize: fontSize,color: color,fontWeight: FontWeight.w500),);
  }
}

class GoogleFontHeadingText extends StatelessWidget {

  final String title;
  final double fontSize;
  final Color color;
  const GoogleFontHeadingText({Key? key,required this.title,required this.fontSize,
    required this.color}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Text(title,style: GoogleFonts.aBeeZee(
        fontSize: fontSize,color: color),);
  }
}
