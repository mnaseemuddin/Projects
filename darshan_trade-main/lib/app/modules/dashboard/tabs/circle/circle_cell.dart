import 'dart:math';
import 'package:flutter/material.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:get/get.dart';

class CircleCell extends StatelessWidget {
  const CircleCell({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return SACellContainer(child: Column(children: [

      Row(children: [
        ClipRRect(
          borderRadius: BorderRadius.circular(8),
          child: Container(
            height: 64,
            width: 64,
            child: Image(
              image: NetworkImage(
                Random().nextInt(100)%2==0?
                'https://pyxis.nymag.com/v1/imgs/3ae/a5b/e1a1c69441d44c72a86e1120d71f297423-04-mac-miller-2.rvertical.w570.jpg'
                    :'https://static.wikia.nocookie.net/mrbean/images/4/4b/Mr_beans_holiday_ver2.jpg/revision/latest?cb=20181130033425',
              ),
              fit: BoxFit.fill,
            ),
          ),
        ),
        SizedBox(width: 8,),
        Expanded(

            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [

                Row(children: [
                  Container(
                    padding: EdgeInsets.only(top: 4, bottom: 4, left: 8, right: 8),
                    child: Text('Free'.tr, style: textStyleLabel(),),
                    decoration: BoxDecoration(
                        color: ColorConstants.blue,
                        borderRadius: BorderRadius.circular(4)
                    ),
                  ),
                  SizedBox(width: 8,),
                  Text('AUTOSULTAN', style: textStyleHeading3(),)
                ],),
                SizedBox(height: 4,),
                Row(children: [
                  Icon(Icons.sync, color: ColorConstants.blue, size: 18,),
                  Text('Sync_strategy_is_turned_on'.tr, style: textStyleCaption(color: ColorConstants.white54),)
                ],),
                SizedBox(height: 4,),
                Row(children: [
                  _createUserCircle(),
                  SizedBox(width: 8,),
                  Expanded(child: Text('people_joined'.trParams({'number': '786'}), style: textStyleLabel(),))
                ],)
              ],)),
        Container(
          // height: 20,
          padding: EdgeInsets.only(top: 4, bottom: 4, left: 16, right: 16),
          child: Text('View'.tr),
          decoration: BoxDecoration(
              color: Colors.amber[300],
              borderRadius: BorderRadius.circular(20)
          ),
        )
      ],),

      SizedBox(height: 16,),

      Container(
        width: double.infinity,
        padding: EdgeInsets.symmetric(vertical: 8, horizontal: 16),
        child: Text('🔥6K🔥 Binance and Huobi 10196% Day 58!!😀',
          style: textStyleLabel(fontSize: 12), textAlign: TextAlign.center,),
        decoration: BoxDecoration(
            color: ColorConstants.white.withOpacity(0.05),
            borderRadius: BorderRadius.circular(8)
        ),
      )

    ],));
    // return Container(
    //   padding: EdgeInsets.all(16),
    //   margin: EdgeInsets.only(bottom: 4, top: 4),
    //
    //   child: Column(children: [
    //
    //     Row(children: [
    //       ClipRRect(
    //         borderRadius: BorderRadius.circular(8),
    //         child: Container(
    //           height: 64,
    //           width: 64,
    //           child: Image(
    //             image: NetworkImage(
    //                 Random().nextInt(100)%2==0?
    //                 'https://pyxis.nymag.com/v1/imgs/3ae/a5b/e1a1c69441d44c72a86e1120d71f297423-04-mac-miller-2.rvertical.w570.jpg'
    //               :'https://static.wikia.nocookie.net/mrbean/images/4/4b/Mr_beans_holiday_ver2.jpg/revision/latest?cb=20181130033425',
    //             ),
    //             fit: BoxFit.fill,
    //           ),
    //         ),
    //       ),
    //       SizedBox(width: 8,),
    //       Expanded(
    //
    //           child: Column(
    //             crossAxisAlignment: CrossAxisAlignment.start,
    //             children: [
    //
    //            Row(children: [
    //           Container(
    //             padding: EdgeInsets.only(top: 4, bottom: 4, left: 8, right: 8),
    //             child: Text('Free', style: textStyleLabel(),),
    //             decoration: BoxDecoration(
    //               color: Colors.blue,
    //               borderRadius: BorderRadius.circular(4)
    //             ),
    //           ),
    //           SizedBox(width: 8,),
    //           Text('AUTOSULTAN', style: textStyleHeading2(),)
    //         ],),
    //            SizedBox(height: 4,),
    //               Row(children: [
    //                 Icon(Icons.sync, color: Colors.blue, size: 18,),
    //                 Text('Sync strategy is turned on', style: textStyleLabel(color: Colors.white54),)
    //               ],),
    //               SizedBox(height: 4,),
    //               Row(children: [
    //                 _createUserCircle(),
    //                 SizedBox(width: 8,),
    //                 Text('678 people joined', style: textStyleLabel(),)
    //               ],)
    //       ],)),
    //       Container(
    //         // height: 20,
    //         padding: EdgeInsets.only(top: 4, bottom: 4, left: 16, right: 16),
    //         child: Text('View'),
    //         decoration: BoxDecoration(
    //           color: Colors.amber[300],
    //           borderRadius: BorderRadius.circular(20)
    //         ),
    //       )
    //     ],),
    //
    //     SizedBox(height: 16,),
    //
    //     Container(
    //       width: double.infinity,
    //       padding: EdgeInsets.symmetric(vertical: 8, horizontal: 16),
    //       child: Text('🔥6K🔥 Binance and Huobi 10196% Day 58!!😀',
    //         style: textStyleLabel(fontSize: 14), textAlign: TextAlign.center,),
    //       decoration: BoxDecoration(
    //           color: Colors.white.withOpacity(0.05),
    //           borderRadius: BorderRadius.circular(8)
    //       ),
    //     )
    //
    //   ],),
    //
    //   decoration: BoxDecoration(
    //       color: Colors.white10.withOpacity(0.05),
    //       borderRadius: BorderRadius.circular(8)
    //   ),
    // );
  }

  Widget _createUserCircle(){
    List<Widget>list = [];
    for(int i=0; i<5; i++){
      list.add(_createCircle(i));
    }
    return Stack(
      children: list,);
  }

  Widget _createCircle(int index){
    return Padding(padding: EdgeInsets.only(left: 14*index.toDouble()),
    child: Container(
      width: 18, height: 18,
      decoration: BoxDecoration(
          image: DecorationImage(image: AssetImage('assets/icons/ic_api_bind.png')),
          shape: BoxShape.circle,
          border: Border.all(color: ColorConstants.white, width: 2)
      ),
    ),);
  }
}

