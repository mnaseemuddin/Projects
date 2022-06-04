
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:trading_apps/api/apis.dart';
import 'package:trading_apps/api/user_data.dart';
import 'package:trading_apps/custom_ui/common_widget.dart';
import 'package:trading_apps/models/otherwalletmodel.dart';
import 'package:trading_apps/models/tdcwalletmodel.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/res/constants.dart';


class WalletTransaction extends StatefulWidget {
  const WalletTransaction({Key? key}) : super(key: key);

  @override
  _WalletTransactionState createState() => _WalletTransactionState();
}

class _WalletTransactionState extends State<WalletTransaction> {

  int _page = 0;
  PageController _controller = PageController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('All Transactions'),
        elevation: 0,),
      backgroundColor: APP_PRIMARY_COLOR,
      body: Container(
        padding: EdgeInsets.only(left: 16, right: 16),
        child: Column(children: [
          Container(height: 40,
            child: Row(children: [
              Expanded(
                  child: GestureDetector(child: _createItem(_page==0, 'Main Wallet'), onTap: (){
                    setState(() {
                      _page = 0;
                      _controller.jumpToPage(_page);
                    });
                  },)),
              Expanded(child: GestureDetector(child: _createItem(_page==1, 'TDC Wallet'), onTap: (){
                setState(() {
                  _page = 1;
                  _controller.jumpToPage(_page);
                });
              },)),
            ],),
          ),

          Expanded(child: PageView.builder(
            controller: _controller,
            allowImplicitScrolling: false,
            //  physics: NeverScrollableScrollPhysics(),
            onPageChanged: (page){
              setState(() {
                _page = page;
              });
            },
            itemBuilder: (context, index){
              return _page==0?Container(
                color: APP_PRIMARY_COLOR,
                child: otherWalletUI(),):
              tdcWalletUI();
            }, itemCount: 2,))
        ],),
      ),
    );
  }
  Widget _createItem(bool selected, String title){
    return Container(
      child: Text(title, style: textStyleLabel(color: selected?APP_SECONDARY_COLOR:Colors.white),),
      alignment: Alignment.centerLeft,
      decoration: BoxDecoration(
          border: Border(bottom: BorderSide(color: selected?APP_SECONDARY_COLOR:Colors.white, width: 2))
      ),
    );
  }

  otherWalletUI() =>FutureBuilder(
      future: otherWalletAPI(userModel!.data.first.userId.toString()),
      builder: (context,snapshot){
        if(!snapshot.hasData)return Center(child: CircularProgressIndicator(),);
        ApiResponse response=snapshot.data as ApiResponse;
        if(!response.status)return Center(child:NoData(),);
        OtherWalletModel walletModel=response.data;
        List<Datum>otherWalletHistoryList=walletModel.data;
        if(otherWalletHistoryList.length<=0)return Center(child: NoData(),);
       return ListView.builder(itemBuilder: (context,index){
         Datum history = otherWalletHistoryList.elementAt(index);
      return Container(
        height: 90,
        margin: EdgeInsets.only(top: 8),
        padding: EdgeInsets.only(left: 16, top: 16, right: 16, bottom: 8),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(children: [
              Align(
                alignment: Alignment.centerLeft,
                child: Container(
                  margin: EdgeInsets.only(left: 10),
                  height: 40,
                  width:50,
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(6),
                    color: Colors.black,
                  ),
                  child: Icon(
                    Icons.shopping_cart,
                    color: Colors.white,
                  ),
                ),
              ),
              SizedBox(width: 16,),
              Expanded(
                  flex: 70,
                  child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text('Order #${history.orderNumber}',style: TextStyle(fontSize: 13,
                      color: Colors.white54),),
                  Padding(padding: EdgeInsets.only(top: 2)),
                  Text('Shopping', style: textStyleHeading2(
                    color: Colors.white,), textAlign: TextAlign.end,),
                  Padding(padding: EdgeInsets.only(top: 2)),
                  Text('24 Sep 2019',style: TextStyle(fontSize: 13,
                      color: Colors.white54),),
                ],)),
              Expanded(
                  flex: 25,
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.end,
                    children: [
                      Icon(Icons.remove,color: Colors.red,size: 11,),
                      Text('\$${history.otherWallet}', style: textStyleHeading2(
                          color: Colors.red),),
                    ],
                  ))
            ],),

            // Row(children: [
            //   Expanded(child: Text('Opening time', style: textStyleLabel(color: Colors.white54),),),
            //   Text('${DateFormat().format(DateTime.fromMillisecondsSinceEpoch(int.parse(history.orderDateTime)))}',
            //       style: textStyleLabel(color: Colors.white54))
            // ],),
          ],),
        decoration: BoxDecoration(
            color: Colors.white.withOpacity(0.05),
            borderRadius: BorderRadius.circular(4)
        ),
      );
    },itemCount: otherWalletHistoryList.length,);
  });

  tdcWalletUI() =>FutureBuilder(
      future: tDCWalletAPI(userModel!.data.first.userId.toString()),
      builder: (context,snapshot){
        if(!snapshot.hasData)return Center(child: CircularProgressIndicator(),);
        ApiResponse response=snapshot.data as ApiResponse;
        if(!response.status)return Center(child:NoData(),);
        TdcWalletModel walletModel=response.data;
        List<TDatum>tDCWalletHistoryList=walletModel.data;
        if(tDCWalletHistoryList.length<=0)return Center(child: NoData(),);
        return ListView.builder(itemBuilder: (context,index){
          TDatum history = tDCWalletHistoryList.elementAt(index);
          return Container(
            height: 90,
            margin: EdgeInsets.only(top: 8),
            padding: EdgeInsets.only(left: 16, top: 16, right: 16, bottom: 8),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(children: [
                  Align(
                    alignment: Alignment.centerLeft,
                    child: Container(
                      margin: EdgeInsets.only(left: 10),
                      height: 40,
                      width:50,
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(6),
                        color: Colors.black,
                      ),
                      child: Icon(
                        Icons.shopping_cart,
                        color: Colors.white,
                      ),
                    ),
                  ),
                  SizedBox(width: 16,),
                  Expanded(
                      flex: 70,
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text('Order #${history.orderNumber}',style: TextStyle(fontSize: 13,
                              color: Colors.white54),),
                          Padding(padding: EdgeInsets.only(top: 2)),
                          Text('Shopping', style: textStyleHeading2(
                            color: Colors.white,), textAlign: TextAlign.end,),
                          Padding(padding: EdgeInsets.only(top: 2)),
                          Text('24 Sep 2019',style: TextStyle(fontSize: 13,
                              color: Colors.white54),),
                        ],)),
                  Expanded(
                      flex: 25,
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.end,
                        children: [
                          Icon(Icons.remove,color: Colors.red,size: 11,),
                          Text('\$${history.allTdcDetect}', style: textStyleHeading2(
                              color: Colors.red),),
                        ],
                      ))
                ],),

                // Row(children: [
                //   Expanded(child: Text('Opening time', style: textStyleLabel(color: Colors.white54),),),
                //   Text('${DateFormat().format(DateTime.fromMillisecondsSinceEpoch(int.parse(history.orderDateTime)))}',
                //       style: textStyleLabel(color: Colors.white54))
                // ],),
              ],),
            decoration: BoxDecoration(
                color: Colors.white.withOpacity(0.05),
                borderRadius: BorderRadius.circular(4)
            ),
          );
        },itemCount: tDCWalletHistoryList.length,);
      });
}
