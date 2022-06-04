import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/modules/dashboard/tabs/mine/reward_detail_report/reward_detail_report_controller.dart';
import 'package:royal_q/app/shared/constants/colors.dart';
import 'package:royal_q/app/shared/constants/common.dart';
import 'package:royal_q/app/shared/sawidgets/common_widget.dart';

class RewardDetailItem extends GetView<RewardDetailReportController> {
  final double total;
  final double today;
  final List<TotalprofitResponse>list;
  const RewardDetailItem({Key? key, required this.total, required this.today, required this.list, }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Column(children: [
        SACellContainer(
            margin: EdgeInsets.all(16),
            child: Row(children: [
          Expanded(child: Column(children: [
            Text('Total profit today'),
            SizedBox(height: 8,),
            Text(today.toStringAsFixed(4), style: textStyleHeading2(),),
          ],)),
          Container(color: ColorConstants.white10, height: 50, width: 1,),
          Expanded(child: Column(children: [
            Text('Cumulative profit'),
            SizedBox(height: 8,),
            Text(total.toStringAsFixed(5), style: textStyleHeading2(),),
          ],)),
        ],)),
        Expanded(child: _pageTotalProfits())
      ],),
    );
  }

  Widget _pageTotalProfits() => ListView.builder(itemBuilder: (context, index){
    TotalprofitResponse model = list.elementAt(index);
    return ListTile(title: Text(model.transdate), trailing: Text('${model.profit} USDT'),
      tileColor: index%2==0?ColorConstants.white10.withOpacity(0.05):null,);
  }, itemCount: list.length,);
}
