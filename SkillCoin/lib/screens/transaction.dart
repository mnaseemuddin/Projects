

import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:skillcoin/api/user_data.dart';
import 'package:skillcoin/customui/container.dart';
import 'package:skillcoin/customui/nodatafound.dart';
import 'package:skillcoin/customui/textview.dart';
import 'package:skillcoin/model/transactionhistorymodel.dart';
import 'package:skillcoin/res/color.dart';
import 'package:skillcoin/res/routes.dart';
import 'package:skillcoin/screens/transactiondetails.dart';
import 'package:intl/intl.dart';
import '../api/apirepositary.dart';
import '../res/constants.dart';


class TransactionActivity extends StatefulWidget {
  const TransactionActivity({Key? key}) : super(key: key);

  @override
  _TransactionActivityState createState() => _TransactionActivityState();
}

class _TransactionActivityState extends State<TransactionActivity> {


  TransactionHistoryModel? _history;
  Color? colors;
   String symbol="";
  String? formatDate;


 @override
  void initState() {
   getUser().then((value){
     if(value!=null){
       _transactionHistoryAPI();
     }else{
       DialogUtils.instance.edgeAlerts(context,"Please Login First For Transaction History .");
     }
   });

    super.initState();
  }


  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: PRIMARY_APP_COLOR,
      appBar: AppBar(
        leading: Container(width: 10,),
        iconTheme: const IconThemeData(color: PRIMARYLIGHTGREYCOLOR),
        title: const GoogleFontHeadingText(title: 'Transaction History',
          fontSize: 14,color: PRIMARYBLACKCOLOR,),),
      body: _history==null?const Center(child: NoData()):Column(children:  [
        Expanded(
          flex: 90,
          child: ListView.builder(
              itemCount: _history!.data.length??0,
              itemBuilder: (ctx,index){
                Datum history=_history!.data.elementAt(index);
                // final String dateTimeString = history.transactionDate;
                // var dateTime = DateFormat('dd/MM/yyyy').parse(dateTimeString);
                // formatDate=DateFormat('MMMM dd, yyyy').format(dateTime);
                if(history.type=="withdrawal"||history.type=="sell"){
                  colors=Colors.red;
                  symbol=" - ";
                }else if(history.type=="deposit"||history.type=="buy"){
                  colors=Colors.green;
                  symbol=" + ";
                }
             return Padding(
               padding: const EdgeInsets.only(top:8.0),
               child: GestureDetector(
                 onTap: (){
                   navPush(context, DetailsPage(sKLCoins: history.amount,address: history.address,
                   timeDate: history.transactionDate,paymentType: history.type, hash: history.hash,));
                 },
                 child: Container(
                   height: 101,
                   color: PRIMARYWHITECOLOR,
                   child: Row(children: [

                     Expanded(
                       flex:10,
                       child: Padding(
                         padding: const EdgeInsets.only(left:8.0),
                         child: Image.asset(imgtransactioninout,height: 24,width: 24,
                           color: PRIMARYLIGHTGREYCOLOR,),
                       ),
                     ),
                     Expanded(
                       flex: 50,
                       child: Column(
                         children: [
                          Align(
                           alignment: Alignment.centerLeft,
                           child: Padding(
                             padding: const EdgeInsets.fromLTRB(8.0,20,0,8),
                             child: NormalHeadingText(title:history.type.capitalizeFirst.toString(), fontSize: 16,
                                 color: PRIMARYBLACKCOLOR),
                           ),
                         ),

                         Align(
                           alignment: Alignment.centerLeft,
                           child: Padding(
                             padding: const EdgeInsets.fromLTRB(8.0,0,0,8),
                             child: Text(history.address,maxLines: 1,style: const TextStyle(
                                 fontSize: 14,
                                 color: PRIMARYLIGHTGREYCOLOR
                             ),),
                           ),
                         ),
                           Align(
                             alignment: Alignment.centerLeft,
                             child: Padding(
                               padding: const EdgeInsets.fromLTRB(8.0,0,0,8),
                               child: Text(history.transactionDate!,textAlign:
                               TextAlign.right,style: const TextStyle(
                                   fontSize: 14,
                                   color: PRIMARYLIGHTGREYCOLOR
                               ),),
                             ),
                           ),

                       ],),
                     ),
                     Expanded(
                         flex: 40,
                         child: Padding(
                           padding: const EdgeInsets.only(right:8.0),
                           child: Text(symbol+history.amount.toDouble().toString()+" SKL",textAlign:
                           TextAlign.right,style: TextStyle(
                             fontSize: 16,
                             color: colors,fontWeight: FontWeight.bold
                           ),),
                         ))

                   ],),
                 ),
               ),
             );
          }),
        )

      ],),
    );
  }

  void _transactionHistoryAPI() {

   Map<dynamic,String>body={
     "user_id":userModel!.data.first.userId
   };

   transactionHistoryAPI(body).then((value){
     if(mounted){
     if(value.status) {
       setState(() {
         _history = value.data;
       });
     }else{
       Fluttertoast.showToast(msg: value.message.toString());
     }
     }
   });

  }
}

class DateUtil {
  static const DATE_FORMAT = 'MMM ddd yyyy';
  String formattedDate(DateTime dateTime) {
    print('dateTime ($dateTime)');
    return DateFormat(DATE_FORMAT).format(dateTime);
  }
}
