import 'package:flutter/material.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:royal_q/main.dart';

class AppLogo extends StatelessWidget {
  const AppLogo({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    return Container(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Image.asset('assets/expgain/darklogo.png', width: 3*size.width/4, height: size.width/2,),
          // Image.asset('assets/expgain/darklogo.png', width: 3*size.width/4, height: size.width/2,),
      ],),
    );
  }
}


class AFAppLogo extends StatelessWidget {
  const AFAppLogo({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    return Center(child: Container(
      padding: EdgeInsets.all(32),
      child: Padding(padding: EdgeInsets.only(right: 20,),
        child: Image.asset('assets/expgain/darklogo.png', width: size.width/2.8, height: size.width/2.8,
          // color: Color(0xFF60F684),
        ),),
      decoration: BoxDecoration(

          gradient: LinearGradient(colors: [Color(0xFF222C35),Color(0xFF0A1319), ]),
          border: Border.all(
        color: Color(0xFFFF5555), width: 4,
      ), shape: BoxShape.circle,
        boxShadow: [BoxShadow(color: Color(0xFFFF5555), offset: Offset(4, 4), blurRadius: 6)]
      ),
    ),);
  }
}
