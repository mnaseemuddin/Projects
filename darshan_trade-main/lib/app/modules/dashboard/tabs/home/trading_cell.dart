import 'package:flutter/material.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/shared/constants/colors.dart';
import 'package:royal_q/app/shared/constants/common.dart';
import 'package:royal_q/app/shared/sawidgets/common_widget.dart';
import 'package:royal_q/app/shared/utils/common_utility.dart';
import 'package:royal_q/main.dart';

class TradingCell extends StatefulWidget {
  final UserCurrencyResponse model;
  final bool isConfig;
  final double? padding;
  final Function() onPressed;

  const TradingCell({Key? key, this.isConfig=false, required this.model, required this.onPressed, this.padding}) : super(key: key);

  @override
  _TradingCellState createState() => _TradingCellState();
}
// Feather.trending_up

class _TradingCellState extends State<TradingCell> {


  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return
      // GestureDetector(
      // child:
      SACellContainerGradient(
        gradient: widget.padding!=null?ColorConstants.gradientNor:null,
        margin: EdgeInsets.symmetric(horizontal: widget.padding??16, vertical: 2),
        child: GestureDetector(
          child: Column(children: [
            Row(children: [
              ClipRRect(
                borderRadius: BorderRadius.all(Radius.circular(24)),
                child: Image.network('http://darshantrade.com/symbol/${widget.model.symbol.
                replaceAll('USDT', '').toLowerCase()}@2x.png', width: 40, height: 40,
                  errorBuilder: (context, url, error) => Image.asset('assets/images/icon.png', width: 40, height: 40,),),
              ),
              SizedBox(width: 8,),
              Text(widget.model.symbol.replaceAll('USDT', ''), style: textStyleHeading2(color: ColorConstants.black),),
              Text('/USDT', style: textStyleLabel(color: ColorConstants.black),),
              SizedBox(width: 16,),
              // Chip(label: Text('Cycle'), backgroundColor: Colors.white10,),
              SACellContainer(child: Text(widget.model.cyclemode==1?'Cycle':'One-shot'), padding: EdgeInsets.all(4)),
              Expanded(child: SizedBox(),),
              Container(
                padding: EdgeInsets.symmetric(horizontal: 8, vertical: 8),
                alignment: Alignment.center,
                child: Text('${widget.model.returnprofit}%', style: textStyleHeading2(),),
                decoration: BoxDecoration(
                    color: widget.model.returnprofit>0?Colors.green:Colors.red,
                    borderRadius: BorderRadius.circular(8)
                ),
              )
            ],),
            Divider(),
            Row(children: [
              Expanded(child: Text('Quantity_:'.tr, style: textStyleLabel(
                  color: ColorConstants.black),), flex: 20,),
              Expanded(child: Text('${widget.model.quantity}', style:
              textStyleLabel(color: ColorConstants.black)), flex: 30,),
              Expanded(child: Text('Profit_:'.tr, style:
              textStyleLabel(color: ColorConstants.black)), flex: 20,),
              Expanded(child: Text('${widget.model.profit}', style:
              textStyleHeading3(color: widget.model.profit>0?Colors.green:Colors.red),),flex: 30,)
            ],),

            SizedBox(height: 8,),

            Row(children: [
              Expanded(child: Text('Price_:'.tr, style: textStyleLabel(color: ColorConstants.black)), flex: 20,),
              Expanded(child: Text('${widget.model.price}', style: textStyleLabel(color: ColorConstants.black)), flex: 30,),
              Expanded(child: Text('Change_:'.tr, style: textStyleLabel(color: ColorConstants.black)),
                flex: 20,), Expanded(child: Text('${widget.model.priceChange}%', style: textStyleHeading3(
                  color: widget.model.priceChange>0?Colors.green:Colors.red),),flex: 30,)
            ],),
          ],),
          onTap: widget.onPressed,
        ),
      );
    // ,
    //   onTap: widget.onPressed,
    // );
  }
}


//[{"symbol":"DENTUSDT","priceChange":17.5170,"quantity":278448.00000000,
// "price":0.00345500,"profit":-37.96,"returnprofit":-3.7872,"cyclemode":1},
// {"symbol":"EOSUSDT","priceChange":19.9450,"quantity":316.40000000,"price":3.06100000,
// "profit":-31.32,"returnprofit":-3.1329,"cyclemode":1},{"symbol":"XLMUSDT","priceChange":6.1480,
// "quantity":4253.00000000,"price":0.23310000,"profit":-8.51,"returnprofit":-0.8507,"cyclemode":1},
// {"symbol":"FTMUSDT","priceChange":5.1670,"quantity":684.00000000,"price":1.45730000,"profit":-3.21,
// "returnprofit":-0.3147,"cyclemode":1},{"symbol":"GALAUSDT","priceChange":6.4180,
// "quantity":3665.00000000,"price":0.26926000,"profit":-12.94,"returnprofit":-1.2940,"cyclemode":1},
// {"symbol":"MANAUSDT","priceChange":4.5720,"quantity":361.00000000,"price":2.74490000,
// "profit":-6.71,"returnprofit":-0.6731,"cyclemode":1},{"symbol":"GRTUSDT","priceChange":12.6220,
// "quantity":2118.00000000,"price":0.47200000,"profit":0.07,"returnprofit":0.0212,"cyclemode":1},{
// "symbol":"SHIBUSDT","priceChange":7.3860,"quantity":37257824.00000000,"price":0.00002646,
// "profit":-14.16,"returnprofit":-1.4158,"cyclemode":1},{"symbol":"ADAUSDT","priceChange":3.8530,
// "quantity":836.10000000,"price":1.18600000,"profit":-8.37,"returnprofit":-0.8361,"cyclemode":1},
// {"symbol":"SLPUSDT","priceChange":3.9410,"quantity":46082.00000000,"price":0.02110000,
// "profit":-27.65,"returnprofit":-2.7650,"cyclemode":1},{"symbol":"IOTAUSDT","priceChange":5.1400,
// "quantity":1105.00000000,"price":0.89590000,"profit":-9.65,"returnprofit":-0.9508,"cyclemode":1},
// {"symbol":"ENJUSDT","priceChange":8.5820,"quantity":546.10000000,"price":1.82200000,"profit":-4.92,
// "returnprofit":-0.4915,"cyclemode":1},{"symbol":"TRXUSDT","priceChange":6.1520,
// "quantity":14096.40000000,"price":0.07092000,"profit":-0.28,"returnprofit":-0.0282,"cyclemode":1},
// {"symbol":"SCUSDT","priceChange":4.0170,"quantity":80645.00000000,"price":0.01217000,
// "profit":-18.55,"returnprofit":-1.8548,"cyclemode":1},{"symbol":"ETHUSDT",
// "priceChange":5.4130,"quantity":0.30000000,"price":3324.07000000,"profit":-2.68,
// "returnprofit":-0.2679,"cyclemode":1}]

