import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:ionicons/ionicons.dart';
import 'package:trading_apps/api/apis.dart';
import 'package:trading_apps/api/user_data.dart';
import 'package:trading_apps/custom_ui/common_widget.dart';
import 'package:trading_apps/models/transaction_model.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/res/constants.dart';
import 'package:trading_apps/utility/common_methods.dart';

class Transaction extends StatefulWidget {
  const Transaction({Key? key}) : super(key: key);

  @override
  _TransactionState createState() => _TransactionState();
}

class _TransactionState extends State<Transaction> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Transactions'),
      elevation: 0,),
      backgroundColor: APP_PRIMARY_COLOR,

      body: FutureBuilder(
          future: getTransactionsAPI(userModel!.data.first.userId),
          builder: (BuildContext context, AsyncSnapshot<ApiResponse>snapshot){
        if(!snapshot.hasData)return Center(child: CircularProgressIndicator(color: APP_SECONDARY_COLOR,),);
        if(!snapshot.data!.status)return Center(child: Text('Something went wrong', style: textStyleLabel(color: Colors.white),),);
        TransactionModel model = snapshot.data!.data;
        List<Trans> list = model.data;
        if(list.length==0)return Center(child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Icon(Ionicons.trash_bin_outline, color: APP_SECONDARY_COLOR, size: 100,),
            SizedBox(height: 40,),
            Text('There is no transaction', style: textStyleLabel(color: Colors.white),),
            SizedBox(height: 24,),
            TextButton.icon(onPressed: () => Navigator.of(context).pop(), icon: Icon(Icons.home, color: APP_SECONDARY_COLOR,),
                label: Text('Home', style: textStyleHeading2(color: APP_SECONDARY_COLOR),))
          ],
        ),);
        return ListView.builder(itemBuilder: (context, index){
          Trans trans = list.elementAt(index);
          return SettingItemBg(
              padding: EdgeInsets.all(8),
              child: Column(
            children: [
              Row(
                children: [
                Image.asset('assets/images/icon/${trans.coin.toLowerCase()}@2x.png', width: 36,),
                SizedBox(width: 4,),
                Text('${trans.coin}', textAlign: TextAlign.right,
                  style: textStyleHeading(color: Colors.white),),
                Expanded(child: Text('${trans.amount}\$', textAlign: TextAlign.right,
                  style: textStyleHeading(color: APP_SECONDARY_COLOR),))
              ],),
              SizedBox(height: 16,),

              Row(children: [
                SizedBox(width: 20,),
                Expanded(child: Text('Fiat amount:', style: textStyleHeading(color: Colors.white))),
                Text('${trans.fiatAmount}', textAlign: TextAlign.right,
                  style: textStyleHeading(color: Colors.white),)
              ],),

              Row(children: [
                SizedBox(width: 20,),
                Expanded(child: Text('Status:', style: textStyleHeading(color: Colors.white))),
                Text('${trans.confirms}', textAlign: TextAlign.right,
                  style: textStyleHeading(color: Colors.white),)
              ],),
              Row(children: [
                SizedBox(width: 20,),
                Expanded(child: Text('Transaction ID:', style: textStyleHeading(color: Colors.white))),
                Expanded(child: Text('${trans.txnId}', textAlign: TextAlign.right,
                  style: textStyleHeading3(color: Colors.white),))
              ],),
              Row(
                children: [
                SizedBox(width: 20,),
                Expanded(child: Text('Address:', style: textStyleHeading(color: Colors.white))),
                Expanded(child: Text('${trans.address}',overflow: TextOverflow.ellipsis, textAlign: TextAlign.right,
                  style: textStyleHeading3(color: Colors.white),))
              ],),

              Row(children: [
                Expanded(child: Container()),
                Text('${getLocalTimeStamp(trans.addedTime.substring(0, trans.addedTime.indexOf('(')).trim())}', textAlign: TextAlign.right,
                  style: textStyleLabel(color: Colors.white),)
              ],),

            ],
          ));
        }, itemCount: list.length,);
      }),
    );
  }


}
