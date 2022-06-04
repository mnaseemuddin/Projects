import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:royal_q/app/modules/dashboard/controllers/dashboard_controller.dart';
import 'package:royal_q/app/modules/dashboard/tabs/controllers/quantitative_controller.dart';
import 'package:royal_q/app/shared/shared.dart';


// class RevenueDetail extends StatefulWidget {
//   const RevenueDetail({Key? key}) : super(key: key);
//
//   @override
//   _RevenueDetailState createState() => _RevenueDetailState();
// }

class RevenueDetail extends GetView<QuantitativeController> {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Revenue Detail'),
        backgroundColor: const Color(0xFFF87E02),
        elevation: 0,
        brightness: Brightness.dark,
      ),
      body: Container(
        child: Stack(children: [
          Container(height: 80, color: const Color(0xFFF87E02),),

          Padding(
              padding: EdgeInsets.only(top: 20),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.start,
                mainAxisSize: MainAxisSize.min,
                children: [
                Container(
                  child: Column(
                    children: [

                      Row(children: [
                        Expanded(child: Text('Today\'s profit(USDT)', style: textStyleLabel(color: const Color(0xFF8E5202)),), flex: 50,),
                        Expanded(child: Text('Cumulative profit(USDT)', style: textStyleLabel(color: const Color(0xFF8E5202)),), flex: 50,),
                      ],),
                      SizedBox(height: 16,),
                      Row(children: [
                        Expanded(child: Text('0000', style: textStyleHeading(),), flex: 50,),
                        Expanded(child: Text('0000', style: textStyleHeading(),), flex: 50,),
                      ],),
                      SizedBox(height: 8,),
                      Row(children: [
                        Expanded(child: Text('≅ 0.00 USD', style: textStyleLabel(),), flex: 50,),
                        Expanded(child: Text('≅ 0.00 USD', style: textStyleLabel(),), flex: 50,),
                      ],),
                      SizedBox(height: 16,),
                      Align(alignment: Alignment.bottomRight,
                      child: Text('Data_hour'.tr, style: textStyleLabel(fontSize: 12),),),
                      Align(alignment: Alignment.bottomRight,
                        child: Text('Daily_time'.trParams({'time': '(UTC+5:30)'}), style: textStyleLabel(fontSize: 12),),),
                      // SizedBox(height: 16,)
                    ],
                  ),
                  margin: EdgeInsets.symmetric(horizontal: 16),
                  padding: EdgeInsets.all(16),
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(16),
                    gradient: LinearGradient(
                        colors: [const Color(0xFFFCCB68),
                          const Color(0xFFF8B735)],
                        begin: Alignment.centerLeft, end: Alignment.centerRight)
                  ),
                ),

                  // controller.revenueResponse.isNotEmpty?Expanded(child:
                  // ListView.builder(itemBuilder: (context, index){
                  //   return ListTile(title: SACellContainer(
                  //       margin: EdgeInsets.all(0),
                  //       child: Text('2:30', style: textStyleHeading3(),)),);
                  // }, itemCount: controller.revenueResponse.length,))
                  //     :Container(
                  //       margin: EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                  //       height: 1, child: LinearProgressIndicator(color: ColorConstants.APP_SECONDARY_COLOR,),)
          ],))

        ],),
      ),
    );
  }
}
