
import 'package:flutter/material.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/models/response/billing_details_by_coin_model.dart';

import '../../../../../shared/constants/colors.dart';
import '../../../../../shared/constants/common.dart';
import '../../../../../shared/sawidgets/common_widget.dart';

class BillingDetailByViewScreen extends StatefulWidget {
  final String symbol;
  const BillingDetailByViewScreen({Key? key,required this.symbol}) : super(key: key);

  @override
  _BillingDetailByViewScreenState createState() => _BillingDetailByViewScreenState();
}

class _BillingDetailByViewScreenState extends State<BillingDetailByViewScreen> {

   List<BillingDetailsByCoinModel> billResponse=[];

  @override
  void initState() {
    coinByBillingAPI(widget.symbol+"USDT").then((value){
      setState(() {
        billResponse=value.data;
        print(billResponse..toString());
      });
    });
    super.initState();
  }


  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Billing Detail', style: textStyleHeading2(color: ColorConstants.white),),
        iconTheme: IconThemeData(color: ColorConstants.white),
        elevation: 0, centerTitle: true,
        backgroundColor: Colors.transparent,
      ),

      body: _pageBillingDetails(),);
  }
  Widget _pageBillingDetails() => ListView.builder(itemBuilder: (context, index){
    BillingDetailsByCoinModel model = billResponse.elementAt(index);
    String title = model.remark.replaceAll('USDT', '');
    String heading="";
    if(model.credit.contains("-")){
      heading="Loss";
    }else{
      heading="Profit";
    }
    return GestureDetector(
      onTap: (){
      },
      child: SACellContainer(
          margin: EdgeInsets.symmetric(vertical: 2, horizontal: 4),
          child: Row(children: [
            Expanded(child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text('$title/USDT', style: textStyleHeading2(color: ColorConstants.APP_SECONDARY_COLOR),),
                Text('Platform: ${model.side}')
              ],)), Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(children: [
                  Text('$heading: ${model.credit}${" USDT"}', style: textStyleLabel(color: heading==
                      "Profit"?Colors.green:Colors.red),),
                  //Text(' USDT', style: textStyleLabel(),),
                ],),
                SizedBox(height: 5,),
                Text(model.transdate, style: textStyleFootNote(),)
              ],)
          ],)),
    );
  }, itemCount: billResponse.length??0,);
}
