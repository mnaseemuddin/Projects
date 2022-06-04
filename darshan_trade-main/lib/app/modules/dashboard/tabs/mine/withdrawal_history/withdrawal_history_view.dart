import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:royal_q/app/shared/constants/colors.dart';
import 'package:royal_q/app/shared/constants/common.dart';
import 'package:royal_q/app/shared/sawidgets/sawidgets.dart';

import 'withdrawal_history_controller.dart';


class WithdrawalHistoryView extends  GetView<WithdrawalHistoryController>  {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Withdrawal History', style: TextStyle(color: ColorConstants.white),),
        centerTitle: true,
        backgroundColor: Colors.transparent,
        elevation: 0,
        iconTheme: IconThemeData(color: ColorConstants.white),
      ), 
      body: Container(
        padding: EdgeInsets.symmetric(horizontal: 8),
        child: Obx(() => ListView(children: controller.withdrawalHistoryResponse.map((element){
          return SACellContainer(child: Column(
            children: [
              _customRow('Withdraw', element.withdrawtime),
              Row(children: [
                Text('${element.quantity} USDT', style: textStyleHeading2(),),
                Expanded(child: SizedBox())
              ],),
              Divider(),
              _customRow('Quantity', Text('${element.quantity} USDT', style: textStyleLabel(fontSize: 16), textAlign: TextAlign.right)),
              _customRow('Chain name', element.chainname),
              _customRow('Withdrawal address', element.withdrawAddress),
              _customRow('Txn Hash', element.remark),
              _customRow('Withdrawal status', element.withdrawstatus),
              _customRow('Transaction fee', '${element.transactionfee} USDT'),
              _customRow('Arrival quantity', '${element.arrivalquantity} USDT'),
              _customRow('Withdrawal time', element.withdrawtime),
              _customRow('Arrival time', element.arrivaltime),
            ],
          ));
        }).toList(),)),
      ),
    );
  }

  Widget _customRow(child1, child2) => Padding(padding: EdgeInsets.symmetric(vertical: 2),
    child: Row(children: [
      Expanded(child: Text(child1),),
      SizedBox(width: 8,),
      Expanded(child: child2 is String?Text(child2, textAlign: TextAlign.right,):child2),
    ],),);

}


