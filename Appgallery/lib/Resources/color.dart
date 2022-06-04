





import 'package:flutter/material.dart';

const Color appPrimaryColor   = Color(0xFF222222);
const Color appSecondaryColor = Color(0xFFFEB711);
const Color appBlackColor=Color(0xFF0F0F0F);
const Color appGreyColor= Color(0xfff4f2f2);
const  appBarColor=Color(0xfff1f3f5);
const Color appRippleColor= Color(0xff267dd4);
const Color appWhiteColor=Color(0xffffffff);



final int _appPrimaryColor = 0xFF222222;
MaterialColor appColor = MaterialColor(
  _appPrimaryColor,
  <int, Color>{
    50: Color(0xFFfce3e1),
    100: Color(0xFFf9c8c5),
    200: Color(0xFFfab0ad),
    300: Color(0xFFfc948e),
    400: Color(0xFFfc7973),
    500: Color(_appPrimaryColor),
    600: Color(0xFFe3534c),
    700: Color(0xFFc74942),
    800: Color(0xFFaa3e39),
    900: Color(0xFF842c28),
  },
);