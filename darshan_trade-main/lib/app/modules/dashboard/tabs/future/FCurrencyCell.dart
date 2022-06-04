import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/shared/utils/common_utility.dart';
import 'package:royal_q/main.dart';
import 'package:get/get.dart';
import 'package:transparent_image/transparent_image.dart';

import '../../../../shared/constants/colors.dart';
import '../../../../shared/constants/common.dart';
import 'model/fCurrencyModel.dart';

class FCurrencyCell extends StatefulWidget {
  final FUSDTCurrencyModel model;
  final Function()? onPress;
  const FCurrencyCell({Key? key, required this.model, this.onPress}) : super(key: key);

  @override
  _CurrencyCellState createState() => _CurrencyCellState();
}

class _CurrencyCellState extends State<FCurrencyCell> {



  @override
  Widget build(BuildContext context) {
    String title = widget.model.symbol.replaceAll('USDT', '');//.replaceAll('DOWN', '').replaceAll('UP', '');
    return GestureDetector(
      child: Container(
        padding: EdgeInsets.all(16),
        margin: EdgeInsets.only(bottom: 8),

        child: Column(children: [

          Row(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              // FadeInImage.memoryNetwork(
              //   placeholder: kTransparentImage,
              //   imageErrorBuilder: (context, url, error) => Image.asset('assets/images/icon.png', width: 40, height: 40,),
              //   image: 'http://darshantrade.com/symbol/'
              //       '${widget.model.symbol.replaceAll('USDT', '').toLowerCase()}@2x.png',
              //   width: 40, height: 40,
              // ),

              SizedBox(
                  width: 40, height: 40,
                  child: CircleAvatar(
                    backgroundColor: Colors.grey,
                    backgroundImage: NetworkImage(
                        'http://darshantrade.com/symbol/${widget.model.symbol.replaceAll('USDT', '').toLowerCase()}@2x.png'),)),

              SizedBox(width: 8,),
              Text(title, style: textStyleHeading3(color: ColorConstants.white),),
              Text('/USDT', style: textStyleLabel(color: ColorConstants.white54),),
              SizedBox(width: 8,),
             /* Container(
                padding: EdgeInsets.all(4),
                child: Text('Cycle'.tr, style: textStyleLabel(color: ColorConstants.blue),),
                decoration: BoxDecoration(
                    color: ColorConstants.blue.withOpacity(0.1),
                    borderRadius: BorderRadius.circular(2)
                ),
              ),*/
              Expanded(child: Container()),
              Container(
                child: Text('0.00%', style: textStyleLabel(color: ColorConstants.white, fontSize: 14),),
                padding: EdgeInsets.all(8),
                alignment: Alignment.center,
                decoration: BoxDecoration(
                    color: ColorConstants.grey,
                    borderRadius: BorderRadius.circular(8)
                ),
              )
            ],),
          Divider(color: ColorConstants.white24,height: 32,),

          Row(children: [
            Text('Quantity'.tr, style: textStyleLabel(color: ColorConstants.white70),),
            SizedBox(width: 4,),
            Text(double.parse(widget.model.lastQty).toStringAsFixed(3), style: textStyleLabel(color: ColorConstants.white70),),
            SizedBox(width: 16,),
            // Text('Floating profit\t\t+${widget.model.lastPrice}', style: textStyleLabel(color: isPlatformIOS?Colors.black54:Colors.white70),),
          ],),
          SizedBox(height: 8,),
          Row(children: [
            Text('Price'.tr, style: textStyleLabel(color: ColorConstants.white70),),
            Text(double.parse(widget.model.lastPrice).toStringAsFixed(3), style: textStyleLabel(color: ColorConstants.white70),),
            SizedBox(width: 16,),
            Text('Increase'.tr, style: textStyleLabel(color: ColorConstants.white70),),
            Text('${widget.model.priceChangePercent}%', style:
            textStyleLabel(color: double.parse(widget.model.priceChangePercent)>0?
            ColorConstants.green:ColorConstants.redAccent),),

          ],)

        ],),

        decoration: BoxDecoration(
            color: ColorConstants.whiteRev,
            borderRadius: BorderRadius.circular(8)
        ),
      ),
      onTap: widget.onPress,
    );
  }



  Future<Image> _buildImage(image) async {
    // return loadImage(image, Size(24, 24));
    return rootBundle.load(image).then((value) {
      return Image.memory(value.buffer.asUint8List(), width: 24,);
    }).catchError((_) {
      return Image.asset(
        'assets/expgain/icon_splash.png',
        height: 24.0,
      );
    });
  }

  @override
  void initState() {
    print(widget.model.symbol);
    super.initState();
  }
}
