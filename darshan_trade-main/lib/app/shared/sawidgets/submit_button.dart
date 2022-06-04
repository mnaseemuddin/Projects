import 'package:flutter/material.dart';

import '../shared.dart';


class SubmitButton extends StatelessWidget {
  final String title;
  final bool isActive;
  final Function()onPressed;
  final EdgeInsets margin;
  const SubmitButton({Key? key, required this.onPressed, required this.title, this.isActive=true, this.margin= const EdgeInsets.all(0)}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      child: Container(
        margin: margin,
        width: double.infinity,
        height: 50,
        alignment: Alignment.center,
        child: Text(title, style: textStyleHeading(color: Colors.white),),

        decoration: BoxDecoration(
          gradient: isActive?LinearGradient(colors: [Color(0xFFFF5400),
            Color(0xFF8b009e),Color(0xff1f0fa1),
          ]):LinearGradient(colors: [Colors.grey,Colors.grey],begin: Alignment.centerLeft,end:
          Alignment.centerLeft),
          borderRadius: BorderRadius.circular(8),
        ),

      ),
      onTap: isActive?onPressed:null,
    );
  }
  
  
}



class AFSubmitButton extends StatelessWidget {
  final String title;
  final bool isActive;
  final Function()onPressed;
  final EdgeInsets margin;
  const AFSubmitButton({Key? key, required this.onPressed, required this.title, this.isActive=true, this.margin= const EdgeInsets.all(0)}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      child: Container(
        margin: margin,
        width: double.infinity,
        height: 50,
        alignment: Alignment.center,
        child: Text(title, style: textStyleHeading(color: Colors.white),),

        decoration: BoxDecoration(
          gradient: isActive?LinearGradient(colors: [Color(0xFFFF5555), Color(0xFFFF5555),
          ]):LinearGradient(colors: [Colors.grey,Colors.grey],begin: Alignment.centerLeft,end:
          Alignment.centerLeft),
          borderRadius: BorderRadius.circular(8),
        ),

      ),
      onTap: isActive?onPressed:null,
    );
  }


}
