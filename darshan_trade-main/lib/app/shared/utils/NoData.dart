


import 'package:flutter/material.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';

class NoData extends StatelessWidget {

  const NoData({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {

    return Container(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        mainAxisSize: MainAxisSize.max,
        children: [
          Container(
            child: Image.asset("assets/expgain/empty.png",height: 40,width: 40,),
            decoration: BoxDecoration(
                color: Colors.white10,
                shape: BoxShape.circle
            ),
          ),
          SizedBox(height: 15,),
          Text('Oops!', style: TextStyle(fontSize: 18,color: Colors.black,fontWeight: FontWeight.w500),),
          SizedBox(height: 5,),
          Text('No Data Found', style: TextStyle(fontSize: 18,color: Colors.black,fontWeight: FontWeight.bold)),
        ],
      ),
    );
  }
}