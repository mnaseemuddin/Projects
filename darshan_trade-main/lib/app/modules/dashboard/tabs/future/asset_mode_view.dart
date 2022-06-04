


import 'package:flutter/material.dart';
import 'package:get/get.dart';

import '../../../../shared/constants/common.dart';

class AssetModeView extends StatefulWidget {
  const AssetModeView({Key? key}) : super(key: key);

  @override
  State<AssetModeView> createState() => _AssetModeViewState();
}

class _AssetModeViewState extends State<AssetModeView> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        backgroundColor: const Color(0xFFF87E02),
        title: Text('Asset Mode',style: textStyleHeading(color: Colors.white),),
      ),
      body: Column(children: [

        Padding(
          padding: const EdgeInsets.only(top:12.0,left: 1,right: 1),
          child: Container(height: 200,
          color: Colors.grey[100],
          child: Column(children: [

            Padding(
              padding: const EdgeInsets.only(left:15.0,top: 15,right: 8),
              child: Row(children: [

                Text("Single-Asset Mode",style: textStyleHeading2(),),
                Spacer(),
                Container(width: 25,height: 25,
                decoration: BoxDecoration(borderRadius: BorderRadius.circular(40),
                color: Colors.orange),child: Icon(Icons.check,size: 15,),)

              ],),
            ),

            Padding(
              padding: const EdgeInsets.only(left:15.0,right: 40,top: 10),
              child: Text("• Supports USD-M Futures trading by only using the single margin asset of the symbol.",style: TextStyle(fontSize: 15),),
            ),

            Padding(
              padding: const EdgeInsets.only(left:15.0,right: 40,top: 10),
              child: Text("•  PNL of the same margin asset positions can be offset.",
                  style: TextStyle(fontSize: 15)),
            ),

            Padding(
              padding: const EdgeInsets.only(left:15.0,right: 40,top: 10),
              child: Text("•  Support Cross Margin Mode and Isolated Maring Mode.",
                  style: TextStyle(fontSize: 15)),
            ),

          ],),),
        )

      ],),
    );
  }
}
