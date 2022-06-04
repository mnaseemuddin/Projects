import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:google_fonts/google_fonts.dart';

class SHAppbar extends StatelessWidget {
  final bool isBack;
  final bool isIcon;
  final dynamic title;
  const SHAppbar({Key? key, required this.title,  this.isBack=true, this.isIcon=true}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Row(children: [
      SizedBox(width: 16,),
      if(isBack)
        GestureDetector(
          child: Image.asset('assets/icons/ic_back.png', width: 40, color: Color(0xFFFF5555),),
          onTap: () => Get.back(),
        ),
      SizedBox(width: 8,),

      Expanded(child: title is String?Text(title, style: GoogleFonts.roboto(
        fontSize: 18, fontWeight: FontWeight.w700, color: Color(0xFFFF5555)
      ),textAlign: TextAlign.center,):title),

      // Spacer(),
      // if(isIcon)
      //   Image.asset('assets/ic_img/ic_logo.png', width: 40,),
      SizedBox(width: 56,),
    ],);
  }
}
