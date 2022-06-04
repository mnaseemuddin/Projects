import 'package:flutter/material.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:get/get.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/modules/dashboard/controllers/dashboard_controller.dart';
import 'package:royal_q/app/modules/dashboard/tabs/mine/items/items.dart';
import 'package:royal_q/app/modules/dashboard/tabs/mine/items/asset/transfet.dart';
import 'package:royal_q/app/modules/dashboard/tabs/mine/items/asset/withdrawl.dart';
import 'package:royal_q/app/shared/shared.dart';

import 'asset/deposit_currency.dart';


class AssetsDetail extends StatefulWidget {
  const AssetsDetail({Key? key}) : super(key: key);

  @override
  _AssetsDetailState createState() => _AssetsDetailState();
}

class _AssetsDetailState extends State<AssetsDetail> {

  double _asset = 0.000;
  double _assetInDollar = 0.000;
  final DashboardController _controller = Get.find<DashboardController>();

  @override
  void initState() {
    super.initState();
    getFundBalanceAPI().then((value){
      if(mounted&&value.status){
        setState(() {
          _asset = double.parse(value.data['totalAmount']);
          _assetInDollar = double.parse(value.data['totalAmount']);
        });
      }
    });

  }

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    return Stack(children: [
      Container(color: ColorConstants.grey, width: size.width, height: size.height,),
      Scaffold(
        appBar: AppBar(
          title: Text('Transaction_Detail'.tr, style: textStyleHeading2(color: ColorConstants.white),),
          iconTheme: IconThemeData(color: ColorConstants.white),
          backgroundColor: ColorConstants.grey,
          elevation: 0,
          // brightness: Brightness.dark,
        ),
        // backgroundColor: ColorConstants.grey,
        body: Container(
          child: Stack(children: [
            Container(height: 80, color: ColorConstants.grey,),

            Padding(
                padding: EdgeInsets.only(top: 20),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.start,
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Container(
                      width: double.infinity,

                      child: Column(children: [
                        Padding(padding: EdgeInsets.only(top: 24, bottom: 40),
                          child: Row(children: [
                            Expanded(child: Text('Total_asset'.tr, style: textStyleHeading(color: const Color(0xFF8E5202)))),

                            Obx(() => Text(_controller.balanceFund.value.toStringAsFixed(3), style: textStyleHeading(color: ColorConstants.white, fontSize: 24.0))),
                            Text('USDT', style: textStyleHeading(color: ColorConstants.white70)),

                          ],),),

                      ],),
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
                    Container(
                      padding: EdgeInsets.symmetric(vertical: 16),
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                        children: [
                          DepositItem(title: 'Deposit'.tr, icon: AntDesign.arrowup,onPress: () => 
                          Get.to(DepositCurrency()),),
                          DepositItem(title: 'Withdrawal'.tr, icon: AntDesign.arrowdown, onPress: () => 
                          Get.to(Withdrawal(balance: _asset,)),),
                          DepositItem(title: 'Transfer'.tr, icon: AntDesign.swap, onPress: () => 
                          Get.to(Transfer(balance: _asset,))?.then((value){
                            setState(() {

                            });
                          }),),
                        ],),
                    ),
                    Expanded(child: FutureBuilder(
                        future: getFundListAPI(),
                        builder: (context, AsyncSnapshot<ApiResponse>snapshot){
                          if(!snapshot.hasData)return Center(child: CircularProgressIndicator(color: ColorConstants.white,),);
                          List<FundListResponse> response = snapshot.data!.data;
                          return RefreshIndicator(
                              color: ColorConstants.APP_SECONDARY_COLOR,
                              child: ListView.builder(itemBuilder: (context, index){
                                FundListResponse fundModel = response.elementAt(index);
                                return ListTile(title: SACellContainer(
                                    margin: EdgeInsets.all(0),
                                    padding: EdgeInsets.all(0),
                                    child: ListTile(title: Row(children: [
                                      Expanded(child: Text(fundModel.ttype, style: textStyleHeading3())),
                                      Text('${fundModel.credit>0?fundModel.credit:fundModel.debit}', style: textStyleHeading3(),),
                                      Text('USDT', style: textStyleHeading3(color: ColorConstants.white54),)
                                    ],), trailing: fundModel.credit>0?Text('In', style: textStyleLabel(color: ColorConstants.green),)
                                        :Text('Out', style: textStyleLabel(color: ColorConstants.redAccent),),
                                      subtitle: Text(fundModel.createDate, style: textStyleCaption(color: ColorConstants.white24),),)
                                  // child: Text('2:30', style: textStyleHeading3(),)
                                ),);
                              }, itemCount: response.length,), onRefresh: () => getFundListAPI().then((value){
                            setState(() {

                            });
                          }));
                        }))
                  ],))
          ],),
        ),
      )
    ],);
  }
  
}

class DepositItem extends StatelessWidget {
  final String title;
  final IconData icon;
  final Function()? onPress;
  const DepositItem({Key? key, required this.title, required this.icon, this.onPress}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Column(children: [
      FloatingActionButton(
        heroTag: title,
        backgroundColor: ColorConstants.APP_PRIMARY_COLOR,
        onPressed: onPress, child: Icon(icon, color: ColorConstants.yellow,size: 24,),),
      SizedBox(height: 4,),
      Text(title, style: textStyleLabel(),)
    ],);
  }
}

