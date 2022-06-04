import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:intl/intl.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/shared/shared.dart';

import 'revenue_details_controller.dart';

class RevenueDetailsView extends  GetView<RevenueDetailsController>  {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Revenue_Detail'.tr),
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
                          Expanded(child: Text('Today_profit'.tr, style: textStyleLabel(color: const Color(0xFF8E5202)),), flex: 50,),
                          Expanded(child: Text('Cumulative_profit'.tr, style: textStyleLabel(color: const Color(0xFF8E5202)),), flex: 50,),
                        ],),
                        SizedBox(height: 16,),
                        Row(children: [
                          Obx(() => Expanded(child: Text('${controller.todayTotalProfit} USDT', style: textStyleHeading(),), flex: 50,)),
                          Obx(() => Expanded(child: Text('${controller.totalProfit} USDT', style: textStyleHeading(),), flex: 50,)),
                        ],),
                        SizedBox(height: 8,),
                        Row(children: [
                          Expanded(child: Text('≅ 0.00 USD', style: textStyleLabel(),), flex: 50,),
                          Expanded(child: Text('≅ ${controller.totalProfit}  USD', style: textStyleLabel(),), flex: 50,),
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

                  Obx(() => controller.revenueResponse.isNotEmpty?Expanded(child:
                  ListView.builder(itemBuilder: (context, index){
                    RevenueResponse model = controller.revenueResponse.elementAt(index);
                    return ListTile(title: SACellContainer(
                        margin: EdgeInsets.all(0),
                        child: Row(children: [
                          _formateDateTime(model.transdate),
                          SizedBox(width: 32,),
                          Expanded(child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                           Row(children: [
                             Text('${model.credit}', style: textStyleHeading2(color: ColorConstants.APP_SECONDARY_COLOR),),
                             Text(' USDT', style: textStyleHeading3(color: ColorConstants.APP_SECONDARY_COLOR.withOpacity(0.7)),)
                           ],),
                            Text(model.remark, style: textStyleCaption(color: ColorConstants.white54),)

                          ],))
                        ],)),);
                  }, itemCount: controller.revenueResponse.length,))
                      :Container(
                        margin: EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                        height: 1, child: LinearProgressIndicator(color: ColorConstants.APP_SECONDARY_COLOR,),))
                ],))

        ],),
      ),
    );
  }

   Widget _formateDateTime(time){
    // DateFormat dateFormat = DateFormat("MM/dd/yyyy HH:mm:ss");
    DateFormat dateFormat = DateFormat("MM-dd-yyyy");
    DateTime dateTime = dateFormat.parse(time);
    String d1 = DateFormat('MM-dd').format(dateTime);
    String d2 = DateFormat('YYYY').format(dateTime);
    String d3 = DateFormat('HH:mm:ss').format(dateTime);

    return Column(children: [
      Text(d1, style: textStyleHeading2(),),
      Text('${dateTime.year}', style: textStyleLabel(color: ColorConstants.white54),)
    ],);
  }
}
  