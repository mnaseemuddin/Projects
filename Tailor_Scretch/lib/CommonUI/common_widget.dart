


import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:tailor/Support/UISupport.dart';

class NoData extends StatelessWidget {
  const NoData({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          Container(
            padding: EdgeInsets.all(8),
            child: Icon(FontAwesomeIcons.mehBlank, size:50,color: UISupport.APP_PRIMARY_COLOR.withOpacity(0.5),),
            decoration: BoxDecoration(
                color: Colors.white10,
                shape: BoxShape.circle
            ),
          ),
          SizedBox(height: 24,),
          Text('Your list of address is empty', style: TextStyle(color: Colors.black,fontWeight: FontWeight.w600,fontSize: 15),),
          //Text('Go back to the chart if you want to open trade', style: TextStyle(color: Colors.white54)),
        ],
      ),
    );
  }
}