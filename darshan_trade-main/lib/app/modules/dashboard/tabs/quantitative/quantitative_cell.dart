import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:get/get.dart';
import 'package:royal_q/api/apis.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/modules/TradeSettings/views/currency_detail_view.dart';
import 'package:royal_q/app/modules/dashboard/tabs/controllers/currency_controller.dart';
import 'package:royal_q/app/modules/dashboard/tabs/home/trading_cell.dart';
import 'package:royal_q/app/modules/dashboard/tabs/quantitative/currency_details.dart';
import 'package:royal_q/app/routes/app_pages.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:transparent_image/transparent_image.dart';


class QuantitativeCell extends StatefulWidget {
  final List<CurrencyModel> currencyModels;
  final int exchangeType;
  final Function()?onRefresh;
  const QuantitativeCell({Key? key, required this.currencyModels, this.onRefresh, required this.exchangeType}) : super(key: key);

  @override
  _QuantitativeCellState createState() => _QuantitativeCellState();
}

class _QuantitativeCellState extends State<QuantitativeCell> {
  final StreamController<int> _onPageChange = StreamController<int>.broadcast();
  final PageController _pageController = PageController();
  final CurrencyController _currencyController = Get.find<CurrencyController>();

  List<CurrencyModel> _currencyModels = [];
  List<BoosterUserListResponse>? _boosterUserListResponse;
  int selectedPage=0;
  List<UserCurrencyResponse>? _currencyCircle=[];


  @override
  void initState() {
    super.initState();
    _currencyModels = widget.currencyModels;
    _currencyCircle=_currencyController.listBinanceHome.value;
    getBoosterUserListAPI().then((value){
      print(value.data);
      if(mounted) {
        setState(() {
        _boosterUserListResponse = value.data;
      });
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.only(left: 16, right: 16),
      child: Column(children: [
        SizedBox(height: 16,),
        SASearch(
          onChange: (val){
          setState(() {
            if(selectedPage==1){
              _currencyCircle=_currencyController.listBinanceHome.value?.
              where((element) =>element.symbol.
              replaceAll("USDT", 'replace').toLowerCase().contains(val.toLowerCase())).toList();
            }else if(selectedPage==2){
              _currencyCircle=_currencyController.listBinanceHome.value?.where((element) =>element.symbol.
              replaceAll("USDT", 'replace').toLowerCase().contains(val.toLowerCase())).toList();
            }else if(selectedPage==3){
              _currencyCircle=_currencyController.listBinanceHome.value?.where((element) =>element.symbol.
              replaceAll("USDT", 'replace').toLowerCase().contains(val.toLowerCase())).toList();
            }else if(selectedPage==0) {
              _currencyModels = widget.currencyModels.where((element) =>
                  element.symbol.replaceAll('USDT', '').toLowerCase().contains(
                      val.
                      toLowerCase())).toList();
            }
          });
        },),
        SizedBox(height: 16,),
        Container(height: 40,
          alignment: Alignment.centerLeft,
          child: SATabview(titles: ['All'.tr, 'Cycle'.tr, 'One-shot'.tr,
            'Stop_margin_call'.tr/* 'Booster'.tr*/],
            selectedColor: Colors.blueAccent, textColor: Colors.blueAccent, expand: true,
            onPageChange: _onPageChange, onSelect: (page){
              _pageController.animateToPage(page, duration: Duration(milliseconds: 200), curve: Curves.ease);
            }, isScroll: true,),),
        Container(height:1, color: ColorConstants.white10,),
        SizedBox(height: 16,),
        Expanded(child: PageView(
          controller: _pageController,
          onPageChanged: (page){
            setState(() {
              selectedPage=page;
              print(selectedPage);
            });
            _onPageChange.add(page);
          },
          physics: NeverScrollableScrollPhysics(),
          children: [

            ListView.builder(itemBuilder: (context, index){
              CurrencyModel model = _currencyModels.elementAt(index);
              return CurrencyCell(model: model,onPress: () => Get.toNamed(Routes.TRADE_SETTINGS,
                arguments: [{'model': model, 'exchangeType': exchangeValue}]),);
        }, itemCount: _currencyModels.length,),

            _createFilterList((_currencyCircle??[]).where((element)
            => element.cyclemode==1).toList()),
            _createFilterList((_currencyCircle??[]).where((element)
            => element.cyclemode==0).toList()),
            _createFilterList((_currencyCircle??[]).where((element)
            => element.marginlimit==0).toList()),

            _boosterUserListResponse!=null?_boosterUserListResponse!.isNotEmpty?
            ListView(children: _boosterUserListResponse!.map((model){
              String title = model.symbol.replaceAll('USDT', '');
              UserCurrencyResponse mModel = UserCurrencyResponse(symbol: model.symbol,
                  priceChange: model.priceChange, quantity: model.quantity,
                  price: model.price, profit: model.profit, returnprofit: model.returnprofit,
                  cyclemode: 1, marginlimit: 0, strategyMode: 0);
              return TradingCell(model: mModel, onPressed: (){
                CurrencyModel currencyModel = CurrencyModel(symbol: model.symbol, priceChangePercent: '${model.priceChange}', lastPrice: '${model.price}', lastQty: '${model.quantity}', strategyMode: 0);
                Get.toNamed(Routes.TRADE_SETTINGS, arguments: [{'model': currencyModel, 'exchangeType': exchangeValue}]);
              },padding: 0,);
              /**
              return GestureDetector(
                child: Container(
                  margin: EdgeInsets.symmetric(horizontal: 8, vertical: 4),
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
                      Expanded(child: Text(title, style: textStyleHeading2(),)),

                    ],),
                    Divider(color: ColorConstants.white24,),
                    Row(children: [
                      Expanded(child: Text('Price', style: textStyleLabel()),),
                      Text(model.price.toStringAsFixed(5), style: textStyleLabel(),),
                      Text(' USDT', style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),)
                    ],),

                    Row(children: [
                      Expanded(child: Text('Quantity', style: textStyleLabel()),),
                      Text(model.quantity.toStringAsFixed(5), style: textStyleLabel()),
                      Text(' $title', style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),)
                    ],),

                    Row(children: [
                      Expanded(child: Text('Price change', style: textStyleLabel()),),
                      Text(model.priceChange.toStringAsFixed(4), style: textStyleLabel()),
                      Text(' USDT', style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),)
                    ],),

                    Row(children: [
                      Expanded(child: Text('Quantity', style: textStyleLabel()),),
                      Text(model.quantity.toStringAsFixed(4), style: textStyleLabel()),
                      Text(' $title', style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),)
                    ],),

                    Row(children: [
                      Expanded(child: Text('Profit', style: textStyleLabel()),),
                      Text(model.price.toStringAsFixed(4), style: textStyleLabel()),
                      Text(' USDT', style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),)
                    ],),

                    Row(children: [
                      Expanded(child: Text('Change', style: textStyleLabel()),),
                      Text(model.returnprofit.toStringAsFixed(5), style: textStyleLabel()),
                    ],),
                  ],),
                  decoration: textBgWhite,
                ),
                onTap: (){
                  CurrencyModel mModel = CurrencyModel(symbol: model.symbol, priceChangePercent: '${model.priceChange}', lastPrice: '${model.price}', lastQty: '${model.quantity}', strategyMode: 0);
                  Get.toNamed(Routes.GET_BOOSTER, arguments: [{'model': mModel}]);
                  // controller.setBooster(model);
                  // _onPageChange.add(0);
                  // _pageController.animateToPage(0, duration: Duration(milliseconds: 200), curve: Curves.ease);
                },
              );
                  **/
            }).toList(),)
            :NoRecord()
                :Center(child: CircularProgressIndicator(),)

          ],
        ))
      ],),
    );
  }

  Widget _createFilterList(List<UserCurrencyResponse> list){
    return list.isNotEmpty?ListView.builder(itemBuilder: (context, index){
      UserCurrencyResponse model = list.elementAt(index);
      return TradingCell(model: model, onPressed: (){
        CurrencyModel currencyModel = CurrencyModel(symbol: model.symbol, priceChangePercent:
        '${model.priceChange}', lastPrice: '${model.price}', lastQty: '${model.quantity}',
            strategyMode: model.strategyMode);
        Get.toNamed(Routes.TRADE_SETTINGS, arguments: [{'model': currencyModel,
          'exchangeType': exchangeValue}]);
      },padding: 0,);
    }, itemCount: list.length,):NoRecord();
  }

  Future<void> _pullRefresh() async {
    widget.onRefresh!();
  }

}
