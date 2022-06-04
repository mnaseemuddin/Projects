import 'package:flutter/material.dart';
import 'package:royal_q/app/shared/shared.dart';

class SuffixWidget{
  final Widget primary;
  final Widget secondary;

  SuffixWidget({required this.primary, this.secondary=const Icon(Icons.check, color: ColorConstants.APP_PRIMARY_COLOR,)});
}

class ImageText{
  final int id;
  final String image;
  final String text;

  ImageText({this.id=0, required this.image, required this.text});
}

class AcceptText{
  final bool isBtn;
  final String text;

  AcceptText({this.isBtn=false, required this.text});
}

class TitleId{
  final int id;
  final String title;

  TitleId({required this.id, required this.title});


}
