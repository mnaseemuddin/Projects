import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
// import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/routes/app_pages.dart';
import 'package:royal_q/app/shared/constants/common.dart';
import 'package:royal_q/app/shared/sawidgets/progress_dialog.dart';
import 'package:royal_q/app/shared/sawidgets/submit_button.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:royal_q/app/shared/utils/common_utility.dart';
import 'package:royal_q/main.dart';

class CurrencyDetails extends StatefulWidget {
  final CurrencyModel model;
  const CurrencyDetails({Key? key, required this.model}) : super(key: key);

  @override
  _CurrencyDetailsState createState() => _CurrencyDetailsState();
}

class _CurrencyDetailsState extends State<CurrencyDetails> {
  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    String title = widget.model.symbol.replaceAll('USDT', '').replaceAll('DOWN', '').replaceAll('UP', '');
    return Scaffold(
      appBar: AppBar(
        elevation: 0,
        brightness: Brightness.dark,
        backgroundColor: const Color(0xFFF87E02),
        actions: [
          TextButton(onPressed: (){}, child: Text('Log'.tr,style: textStyleLabel(),)),
          TextButton(onPressed: (){}, child: Text('Transaction_record'.tr,style: textStyleLabel(),)),
        ],
      ),
      body: Column(children: [
        Expanded(child: SingleChildScrollView(
          child: Stack(children: [
            Column(children: [
              Container(height: 100, color: const Color(0xFFF87E02),),
              Container(height: size.height-100,)
            ],),

            Container(
              margin: EdgeInsets.symmetric(horizontal: 16, vertical: 8),
              padding: EdgeInsets.symmetric(horizontal: 16, vertical: 16),

              child: Column(children: [
                Row(children: [
                  loadImage('assets/images/icon/${widget.model.symbol.replaceAll('USDT', '').toLowerCase()}@2x.png', Size(40, 40)),
                  SizedBox(width: 12,),
                  Text(title, style: textStyleHeading(),),
                  Text('/USDT', style: textStyleHeading3(),),
                ],),

                SizedBox(height: 40,),

                Table(
                  children: [
                    TableRow(children: [
                      TableCell(child: _labelItem('0.0', 'Position_amount'.tr)),
                      TableCell(child: _labelItem('0.0', 'Avg_price'.tr)),
                      TableCell(child: _labelItem('0.0', 'Number_of_call_margin'.tr)),
                    ]),
                    _rowSpacer,
                    TableRow(children: [
                      TableCell(child: _labelItem(double.parse(widget.model.lastQty).toStringAsFixed(3), 'Position_quantity'.trParams({'qantity': title}))),
                      // TableCell(child: _labelItem(double.parse(widget.model.lastQty).toStringAsFixed(3), 'Position quantity ($title)')),
                      TableCell(child: _labelItem(double.parse(widget.model.lastPrice).toStringAsFixed(3), 'Current_price'.tr)),
                      TableCell(child: _labelItem('${widget.model.priceChangePercent}%', 'Return_rate'.tr)),
                    ])
                  ],
                ),

                SizedBox(height: 16,)

              ],),
              decoration: BoxDecoration(
                color: const Color(0xFF102957),
                borderRadius: BorderRadius.circular(12)
              ),
            )

          ],),
        )),
        Container(
          height: 60,
          color: const Color(0xFF102957),
          child: Row(children: [
            Expanded(child: GestureDetector(
              child: Container(
                height: 60,
                alignment: Alignment.center,
                child: Text('Trade_Settings'.tr, style: textStyleHeading2(),),
              ),
              onTap: () => Get.toNamed(Routes.TRADE_SETTINGS),
            )),
            Expanded(child: GestureDetector(
              child: Container(
                height: 60,
                alignment: Alignment.center,
                color: const Color(0xFFF87E02),
                child: Text('Start'.tr, style: textStyleHeading2(),),),
              onTap: (){
                Map body = {
                  'id': userInfo?.id,
                  'Symbol': widget.model.symbol,
                  'Exchangetype': exchangeValue,
                };
                _bindSpotTradeAPI(body);
              },
            )),
            // SubmitButton(onPressed: (){}, title: 'Trade Settings'),
            // SubmitButton(onPressed: (){}, title: 'Start'),
          ],),
        )
      ],),
    );
  }

  Widget _labelItem(title, label) => Column(
    crossAxisAlignment: CrossAxisAlignment.start,
    children: [
    Text(title, style: textStyleHeading(),),
    Text(label, style: textStyleLabel(fontSize: 12, color: ColorConstants.white54),),
  ],);

  final TableRow _rowSpacer=TableRow(children: [
    TableCell(child: SizedBox(height: 16,)),
    TableCell(child: SizedBox(height: 16,)),
    TableCell(child: SizedBox(height: 16,)),
  ]);

  _bindSpotTradeAPI(Map body){
    showDialog(context: context, builder: (context) => ProgressDialog(), barrierDismissible: false).then((value){
      print(value);
    },);

    bindSpotTradeAPI(body).then((value){
      Navigator.of(context).pop();
      if(value.status){
        if(mounted){
          setState(() {
            EasyLoading.showToast('Currency_Binded'.tr);
              Get.back();
          });
        }
      }else{
        EasyLoading.showToast(value.data['message']);
      }
    });
  }
}
