import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:skillcoin/res/color.dart';

class NoData extends StatelessWidget {
  const NoData({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          SizedBox(height: 40,),
          Container(
            padding: EdgeInsets.all(16),
            child: Icon(FontAwesomeIcons.mehBlank, color: PRIMARYBLACKCOLOR.withOpacity(0.5),),
            decoration: BoxDecoration(
                color: Colors.white10,
                shape: BoxShape.circle
            ),
          ),
          SizedBox(height: 24,),
          Text('Your list of transaction is empty', style:
          const TextStyle(color: Colors.black,fontWeight: FontWeight.w500),),
         // Text('Go back to the chart if you want to open trades', style: TextStyle(color: Colors.white54,fontWeight: FontWeight.w500)),
        ],
      ),
    );
  }
}