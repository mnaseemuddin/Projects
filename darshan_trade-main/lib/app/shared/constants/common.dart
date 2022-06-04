import 'package:flutter/material.dart';
import 'package:royal_q/app/shared/constants/colors.dart';

class CommonConstants {
  static const String test = 'test';
  static const num testNum = 1;
  static const double largeText = 40.0;
  static const double normalText = 22.0;
  static const double smallText = 16.0;

  static const margin16  = EdgeInsets.all(16);
  static const marginT16  = EdgeInsets.only(top: 16);
  static const margin8   = EdgeInsets.all(8);
  static const padding16 = EdgeInsets.all(16);
  static const padding8  = EdgeInsets.all(8);

  static const double sizeTitle = 30;
  static const double sizeSubTitle = 20;
  static const double sizeHeading = 18;
  static const double sizeHeading2 = 16;
  static const double sizeHeading3 = 14;
  static const double sizeBody = 11;
  static const double sizeCaption = 10;
  static const double sizeHeaderFooter = 12;
  static const double sizeFootNote = 11;
  static const double sizeLabel = 14;
}


TextStyle textStyleTitle({color=ColorConstants.TEXT_COLOR, fontSize=CommonConstants.sizeTitle})             => TextStyle(fontSize: fontSize,    color: color, fontWeight: FontWeight.bold);
TextStyle textStyleSubTitle({color=ColorConstants.TEXT_COLOR, fontSize=CommonConstants.sizeSubTitle})       => TextStyle(fontSize: fontSize, color: color);
TextStyle textStyleHeading({color=ColorConstants.TEXT_COLOR, fontSize=CommonConstants.sizeHeading})        => TextStyle(fontSize: fontSize,  color: color, fontWeight: FontWeight.bold);
TextStyle textStyleHeading2({color=ColorConstants.TEXT_COLOR})       =>
TextStyle(fontSize: CommonConstants.sizeHeading2, color: color, fontWeight: FontWeight.bold);
TextStyle textStyleHeading3({color=ColorConstants.TEXT_COLOR})       => TextStyle(fontSize: CommonConstants.sizeHeading3, color: color, fontWeight: FontWeight.bold);
TextStyle textStyleBody({color=ColorConstants.TEXT_COLOR})           => TextStyle(fontSize: CommonConstants.sizeBody,     color: color);
TextStyle textStyleCaption({color=ColorConstants.TEXT_COLOR, fontSize=CommonConstants.sizeCaption})        => 
TextStyle(fontSize: CommonConstants.sizeCaption,  color: color);
TextStyle textStyleHeaderFooter({color=ColorConstants.TEXT_COLOR})   => 
TextStyle(fontSize: CommonConstants.sizeCaption,  color: color);
TextStyle textStyleFootNote({color=ColorConstants.TEXT_COLOR})       =>
 TextStyle(fontSize: CommonConstants.sizeCaption,  color: color);
TextStyle textStyleLabel({color=ColorConstants.TEXT_COLOR, fontSize=CommonConstants.sizeLabel})  => 
TextStyle(fontSize: double.parse('$fontSize'),    color: color);
