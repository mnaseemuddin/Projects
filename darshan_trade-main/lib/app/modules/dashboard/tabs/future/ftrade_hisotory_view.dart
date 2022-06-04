

import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter/services.dart';
import 'package:flutter_datetime_picker/flutter_datetime_picker.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:intl/intl.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/models/response/FOrderTransationResposne.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:royal_q/app/shared/utils/common_utility.dart';
import 'package:royal_q/main.dart';
import 'package:transparent_image/transparent_image.dart';
import 'package:get/get.dart';

class FTransactionRecord extends StatefulWidget {
  final String? symbol;
  final int exchangeType;
  const FTransactionRecord({Key? key, this.symbol, required this.exchangeType}) : super(key: key);

  @override
  _TransactionRecordState createState() => _TransactionRecordState();
}

class _TransactionRecordState extends State<FTransactionRecord> {

  bool _isVisible = false;
  bool _isExpand = false;
  static final List<String>_listSymbols = [
    'BTC/USDT', '1INCH/USDT', 'AAVE/USDT', 'AAVE/USDT',
    'BTC/USDT', '1INCH/USDT', 'AAVE/USDT', 'AAVE/USDT',
    'BTC/USDT', '1INCH/USDT', 'AAVE/USDT', 'AAVE/USDT',
    'BTC/USDT', '1INCH/USDT', 'AAVE/USDT', 'AAVE/USDT',
    'BTC/USDT', '1INCH/USDT', 'AAVE/USDT', 'AAVE/USDT',
    'BTC/USDT', '1INCH/USDT', 'AAVE/USDT', 'AAVE/USDT',
    'BTC/USDT', '1INCH/USDT', 'AAVE/USDT', 'AAVE/USDT',
    'BTC/USDT', '1INCH/USDT', 'AAVE/USDT', 'AAVE/USDT',
    'BTC/USDT', '1INCH/USDT', 'AAVE/USDT', 'AAVE/USDT',
  ];

  String? _startDate;
  String? _endedDate;

  List<FOrderTransationResposne>? _list;

  @override
  void initState() {
    super.initState();
    getFOrderTransactionAPI(symbol: widget.symbol).then((value){
      if(mounted){
        setState(() {
          _list = value.data;
          // _list!.sort((a, b) => b.transactTime>a.transactTime?1:-1);
        });
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    return Scaffold(

      appBar: AppBar(title: Text('Transaction_record'.tr, style: textStyleHeading2(color: ColorConstants.white),),
        iconTheme: IconThemeData(color: ColorConstants.white),
        elevation: 0,
        // brightness: Brightness.dark,
        systemOverlayStyle: SystemUiOverlayStyle(
          systemNavigationBarColor: ColorConstants.APP_PRIMARY_COLOR, // Navigation bar
          statusBarColor: ColorConstants.APP_PRIMARY_COLOR, // Status bar
        ),
        backgroundColor: Colors.transparent,
        actions: [
          // IconButton(onPressed: (){
          //   setState(() => _isVisible = !_isVisible);
          // }, icon: Icon(AntDesign.filter))
        ],
      ),
      body: Stack(children: [
        _list !=null?_list!.isNotEmpty?Container(
          padding: EdgeInsets.all(16),
          child: ListView.builder(itemBuilder: (context, index){
            FOrderTransationResposne model = _list!.elementAt(index);
            String title = model.symbol.replaceAll('USDT', '');
          //  DateTime convertDateTime=new DateFormat("yyyy-MM-dd hh:mm:ss").parse(model.time);
            String tradeStatus="Running";
            return Container(
              margin: EdgeInsets.all(4),
              padding: EdgeInsets.all(16),
              child: Column(children: [
                Row(children: [
                  FadeInImage.memoryNetwork(
                    placeholder: kTransparentImage,
                    image: 'https://xpertgain.io/symbol/${model.symbol.replaceAll('USDT', '').toLowerCase()}@2x.png',
                    width: 24, height: 24,
                    imageErrorBuilder: (context, url, error) => Image.asset('assets/images/icon_xg.png', width: 40, height: 40,),
                  ),

                  SizedBox(width: 8,),
                  Expanded(child: Text(title, style: textStyleLabel(),)),
                  Spacer(),
                  Container(width: 22,height: 22,decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(4),
                      color: model.side=="BUY"?Colors.green:Colors.red
                  ),child: Center(child: Text(model.side=="BUY"?"B":"S",style:
                  textStyleLabel(color: Colors.white),)),),
                  SizedBox(width: 4,),
                  tradeStatus=="Running"?Image.asset("assets/expgain/active.png",
                    height: 12,width: 12,):Container()
                ],),
                Divider(color: ColorConstants.white24,),
                // Row(children: [
                //   Expanded(child: Text('Exchange', style: textStyleLabel()),),
                //   Text(model.platform, style: textStyleLabel(),),
                // ],),

                Row(children: [
                  Expanded(child: Text('Trade Type', style: textStyleLabel()),),
                  Text(model.side, style: textStyleLabel()),
                ],),

                Row(children: [
                  Expanded(child: Text('Trade amount', style: textStyleLabel()),),
                  Text('${model.quoteQty}', style: textStyleLabel()),
                  Text(' USDT', style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),)
                ],),

                Row(children: [
                  Expanded(child: Text('Quantity', style: textStyleLabel()),),
                  Text('${model.qty}', style: textStyleLabel()),
                  Text(' $title', style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),)
                ],),

                Row(children: [
                  Expanded(child: Text('Price', style: textStyleLabel()),),
                  Text('${model.price}', style: textStyleLabel()),
                  Text(' USDT', style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),)
                ],),

                Row(children: [
                  Expanded(child: Text('Fees', style: textStyleLabel()),),
                  Text(model.commission.toString(), style: textStyleLabel()),
                  Text(model.commissionAsset.toString(), style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),)
                ],),

                Row(children: [
                  Expanded(child: Text('Time', style: textStyleLabel()),),
                  Text(model.time, style: textStyleLabel()),
                ],),
              ],),
              decoration: textBgWhite,
            );
          }, itemCount: _list!.length,),
        ):Center(child: Text('no_transaction_detail'.tr, style: textStyleLabel(),),)
            :Container(child: LinearProgressIndicator(color: ColorConstants.APP_SECONDARY_COLOR,),),

        Visibility(
            visible: _isVisible,
            child: Container(
              padding: EdgeInsets.only(left: 16, right: 16, top: 16),
              height: size.height/(_isExpand?1.5:2),
              width: double.infinity,
              color: ColorConstants.APP_PRIMARY_COLOR,
              child: Column(children: [
                Expanded(child: SingleChildScrollView(child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    SAFilter(title: 'Order_Status'.tr, list: ['All'.tr, 'Buy'.tr, 'Sell'.tr]),
                    SAFilter(title: 'Exchange'.tr, list: ['All', 'Binance', 'Huobi']),
                    Row(children: [
                      Expanded(child: Text('Symbol'.tr, style: textStyleLabel(fontSize: 16),),),
                      IconButton(onPressed: (){
                        setState(() {
                          _isExpand = !_isExpand;
                        });
                      }, icon: Icon(_isExpand?Icons.arrow_drop_up_outlined:Icons.arrow_drop_down, color: ColorConstants.white,))
                    ],),
                    // SAToggleButton(list: _isExpand?_listSymbols:_listSymbols.sublist(0,8)),
                    SAToggleButton(list: _listSymbols, isExpand: _isExpand,),

                    Divider(color: ColorConstants.white10,),
                    Text('Time'.tr, style: textStyleLabel(fontSize: 16),),
                    Row(children: [
                      Expanded(child: CalenderButton(onPressed: () async{
                        DateTime time = await _getDate();
                        print('object => $time');
                        setState(() => _startDate = '${time.year}-${time.month}-${time.day}');
                      }, text: _startDate,)),
                      Container(width:15, height: 1, color: ColorConstants.white,),
                      Expanded(child: CalenderButton(onPressed: () async{
                        DateTime time = await _getDate();
                        print('object => $time');
                        setState(() => _endedDate = '${time.year}-${time.month}-${time.day}');
                      }, text: _endedDate,)),
                    ],),
                  ],),)),
                Divider(color: ColorConstants.white10,),
                Container(
                  child: Row(
                    children: [
                      Expanded(child: TextButton(onPressed: (){}, child: Text('Reset'.tr, style: textStyleLabel(),))),
                      Container(width: 0.5, color: ColorConstants.white10, height: 40 ,),
                      Expanded(child: TextButton(onPressed: (){
                        setState(() {
                          _isVisible = false;
                        });
                      },
                          child: Text('Confirm'.tr, style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),))),
                    ],
                  ),
                )
              ],),
            ))

      ],),
    );
  }

  Future<DateTime> _getDate() async{

    DateTime today = DateTime.now();
    DateTime firstDay = today.subtract(Duration(minutes: 150*1*24*60));

    DateTime? date = await DatePicker.showDatePicker(context,
        showTitleActions: true,
        theme: DatePickerTheme(backgroundColor: ColorConstants.APP_PRIMARY_COLOR,
            itemStyle: textStyleLabel(),
            cancelStyle: textStyleLabel(),
            doneStyle: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR)
        ),
        minTime: firstDay,
        maxTime: today, onChanged: (date) {
          // print('change $date');
        }, onConfirm: (date) {
          print('confirm $date');
        }, currentTime: DateTime.now(), locale: LocaleType.en);

    return date??DateTime.now();
  }
}

class SAFilter extends StatelessWidget {
  final String title;
  final List<String>list;

  const SAFilter({Key? key, required this.title, required this.list}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(title, style: textStyleLabel(fontSize: 16),),
        SizedBox(height: 8,),
        SAToggleButton(list: list, isExpand: true,),
        Divider(color: ColorConstants.white10,)
      ],
    );
  }
}

class CalenderButton extends StatelessWidget {
  final String? text;
  final Function()onPressed;
  const CalenderButton({Key? key, this.text, required this.onPressed}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      child: Container(
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Expanded(child: Text(text !=null?text!:'YYMMDD', style: textStyleLabel(color: text !=null?ColorConstants.white:ColorConstants.white54),
              textAlign: TextAlign.center,)),
            Icon(MaterialCommunityIcons.calendar_month_outline, color: Colors.white10,)
          ],),
        padding: EdgeInsets.all(6),
        margin: EdgeInsets.all(8),

        decoration: BoxDecoration(
            color: Colors.white.withOpacity(0.1),
            borderRadius: BorderRadius.circular(4)
        ),
      ),
      onTap: onPressed,
    );
  }
}

