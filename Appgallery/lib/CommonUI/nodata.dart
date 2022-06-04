
import 'package:appgallery/Resources/color.dart';
import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';

class NoData extends StatelessWidget {
  String title;
   NoData({Key? key,required this.title}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      mainAxisSize: MainAxisSize.max,
      children: [
        const SizedBox(height: 40,),
        Center(
          child: Container(
            padding: const EdgeInsets.all(16),
            child: Icon(FontAwesomeIcons.mehBlank, color: appBlackColor.withOpacity(0.5),),
            decoration: const BoxDecoration(
                color: Colors.white10,
                shape: BoxShape.circle
            ),
          ),
        ),
        const SizedBox(height: 24,),
        Center(
          child: Text(title, style: const TextStyle(color: appBlackColor,
              fontWeight: FontWeight.w500),),
        ),
        /*Text('Go back to the chart if you want to open trades', style: TextStyle(
            color: APP_BLACK_COLOR,fontWeight: FontWeight.w500)),*/
      ],
    );
  }
}