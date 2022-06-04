import 'package:flutter/material.dart';
import 'package:royal_q/main.dart';

// class ColorConstants {
//   static const Color lightScaffoldBackgroundColor = Color(0xFFF9F9F9);
//   static const Color darkScaffoldBackgroundColor = Color(0xFF00142D);//const Color(0xFF102957);//const Color(0xFF2F2E2E);// hexToColor('#2F2E2E');
//   static const Color secondaryAppColor = Color(0xFF22DDA6);// hexToColor('#22DDA6');
//   static const Color secondaryDarkAppColor = Colors.white;
//   static const Color tipColor = Color(0xFFB6B6B6);// hexToColor('#B6B6B6');
//   static const Color lightGray = Color(0xFFF6F6F6);
//   static const Color darkGray = Color(0xFF9F9F9F);
//   static const Color black = Color(0xFF000000);
//   static const Color white = Color(0xFFFFFFFF);
//   static const Color yellow = Color(0xFFFEB711);
//
//   static const Color yellow1 = Color(0xFF16d5ff);
//
//   static const Color blue = Colors.blue;
//   static const Color green = Colors.green;
//   static const Color redAccent = Colors.redAccent;
//   static const Color grey = Colors.grey;
//   static const Color orange = Colors.orange;
//
//   static const Color SCAFFOLD_LIGHT   =  Color(0xFF00142D);//isPlatformIOS?Colors.white:const Color(0xFF2A2A2A);
//   static const Color SCAFFOLD_COLOR   =  Color(0xFF00142D);///const Color(0xFF00142D);//isPlatformIOS?Colors.white:const Color(0xFF1D1D1D);
//   static const Color APP_PRIMARY_COLOR   =  Color(0xFF00142D);//isPlatformIOS?Colors.black12:const Color(0xFF222222);
//   static const Color APP_SECONDARY_COLOR = Color(0xFFFEB711);
//   static const Color TEXT_COLOR = Color(0xDDFFFFFF);
//
//   static const Color scaffoldBackgroundColor = ColorConstants.SCAFFOLD_COLOR;
//   static const Color canvasColor = ColorConstants.APP_PRIMARY_COLOR;
//   static const Color white54 = Colors.white54;
//   static const Color white70 = Colors.white70;
//   static const Color white60 = Colors.white60;
//   static const Color white38 = Colors.white38;
//   static const Color white30 = Colors.white30;
//   static const Color white24 = Colors.white24;
//
//   static const Color white12 = Colors.white12;
//   static const Color white10 = Colors.white10;
//   static const Color black87 = Colors.black87;
//
//   static const Color iconTheme = Colors.white;
//   static const Color fixedColor = APP_SECONDARY_COLOR;
//   static const Color containerColor = ColorConstants.APP_PRIMARY_COLOR;
//
//   // static const Gradient gradient =  LinearGradient(colors: [Color(0xFF00142D), Color(0xFF102957), ]);
//   // static const Gradient gradientRev =  LinearGradient(colors: [Color(0xFF102957), Color(0xFF00142D)]);
//
//   static const Gradient gradient    =  isExpertGain?LinearGradient(colors: [Color(0xFFFFFF01), Color(0xF096A128),])
//       :LinearGradient(colors: [Color(0xFF00142D), Color(0xFF102957), ]);
//
//   static const Gradient gradientRev =  isExpertGain?LinearGradient(colors: [Color(0xF096A128), Color(0xFFFFFF01),])
//   :LinearGradient(colors: [Color(0xFF102957), Color(0xFF00142D)]);
//
//   // static const MaterialColor primarySwatch = isPlatformIOS?Colors.blue:const Color(0xFF222222);
//
// }


class ColorConstants {
  static const Color secondaryAppColor = Color(0xFFEDEDED);
  static const Color black = Color(0xFF000000);
  static const Color white = Colors.black;
  static const Color whiteRev = Colors.white;
  // static const Color black = Colors.white;
  static const Color yellow = Color(0xFFFEB711);
  // static const Color blue = Color(0xFFE819A3);//Color(0xFF3E4095);
  static const Color blue = Color(0xFFF87E02);//Color(0xFF3E4095);
  static const Color green = Colors.green;
  static const Color redAccent = Colors.redAccent;
  static const Color grey = Colors.grey;
  static const Color orange = Colors.orange;


  static const Color yellow1 = Color(0xff05a9cd);

  // static const Color APP_PRIMARY_COLOR   =  Color(0xFF545454);
  static const Color APP_PRIMARY_COLOR   =  Color(0xFFEDEDED);
  // static const Color APP_PRIMARY_COLOR   =  Color(0xFFB3B2B2);
  // static const Color APP_SECONDARY_COLOR = Color(0xFFE819A3);
  static const Color APP_SECONDARY_COLOR = Color(0xFFF87E02);
  // static const Color TEXT_COLOR = Color(0xDDFFFFFF);
  static const Color TEXT_COLOR = Color(0xDD000000);

  static const Color canvasColor = ColorConstants.APP_PRIMARY_COLOR;
  // static const Color white54 = Colors.white54;
  // static const Color white70 = Colors.white70;
  // static const Color white60 = Colors.white60;
  // static const Color white24 = Colors.white24;


  static const Color white54 = Colors.black54;
  static const Color white70 = Colors.black87;
  static const Color white60 = Colors.black54;
  static const Color white38 = Colors.black38;
  static const Color white30 = Colors.black38;
  static const Color white24 = Colors.black26;
  static const Color white12 = Colors.black12;
  static const Color white10 = Colors.black12;

  static const Color black87 = Colors.white70;

  static const Color white54Rev = Colors.white54;
  static const Color white70Rev = Colors.white70;
  static const Color white60Rev = Colors.white54;
  static const Color white38Rev = Colors.white38;
  static const Color white30Rev = Colors.white30;
  static const Color white24Rev = Colors.white24;
  static const Color white12Rev = Colors.white12;
  static const Color white10Rev = Colors.white10;

  static const Color iconTheme = Colors.black;
  static const Color fixedColor = APP_SECONDARY_COLOR;
  static const Color containerColor = ColorConstants.APP_PRIMARY_COLOR;

  // static const Gradient gradient    =  LinearGradient(colors: [Color(0xFFFFFF01), Color(0xF096A128),]);
  static const Gradient gradient    =  LinearGradient(colors: [APP_PRIMARY_COLOR, APP_PRIMARY_COLOR,]);
  static const Gradient gradientRev =  LinearGradient(colors: [Colors.white, Colors.white], begin: Alignment.topCenter, end: Alignment.bottomCenter);
  // static const Gradient gradientRev =  LinearGradient(colors: [Color(0xF096A128), Color(0xFFFFFF01),]);
  static const Gradient gradientNor =  LinearGradient(colors: [whiteRev, whiteRev]);
}

Color hexToColor(String hex) {
  assert(RegExp(r'^#([0-9a-fA-F]{6})|([0-9a-fA-F]{8})$').hasMatch(hex),
      'hex color must be #rrggbb or #rrggbbaa');

  return Color(
    int.parse(hex.substring(1), radix: 16) +
        (hex.length == 7 ? 0xff000000 : 0x00000000),
  );
}

// final int _appPrimaryColor = 0xFFFEB711;
final int _appPrimaryColor = 0xFF000000;
const Color _color = Color(0xFF222222);
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
