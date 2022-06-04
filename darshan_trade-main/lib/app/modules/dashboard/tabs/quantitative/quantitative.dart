import 'dart:async';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/modules/dashboard/controllers/dashboard_controller.dart';
import 'package:royal_q/app/modules/dashboard/tabs/controllers/currency_controller.dart';
import 'package:royal_q/app/modules/dashboard/tabs/controllers/quantitative_controller.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:royal_q/main.dart';

import '../tabs.dart';

class Quantitative extends GetView<QuantitativeController> {

  Quantitative({Key? key}) : super(key: key);

  StreamController<int> _onPageChange = StreamController<int>.broadcast();
  final CurrencyController _currencyController = Get.find<CurrencyController>();
  final DashboardController _dashboardController = Get.find<DashboardController>();
  final PageController _pageController = PageController();

  @override
  Widget build(BuildContext context) {

    if(_onPageChange.isClosed)_onPageChange = StreamController<int>.broadcast();
    return SafeArea(child: Container(
      color: Colors.transparent,
      child: Column(children: [
        Container(
          alignment: Alignment.centerLeft,
          padding: EdgeInsets.only(left: 16),
          height: 40, child: SATabview(titles: [exchangeValue==1?'Binance':'Huobi'], onPageChange: _onPageChange, onSelect: (page){
            _pageController.animateToPage(page, duration: Duration(milliseconds: 800), curve: Curves.easeIn);
            _onPageChange.add(page);
            _onCoinPageChanged(page);
        }
        //     (page){
        //   _onPageChange.add(page);
        // },
        ),),

        Expanded(child: Obx(() => PageView(
          controller: _pageController,
          onPageChanged: (page){
            _onPageChange.add(page);
            _onCoinPageChanged(page);
          },
          children: [
            QuantitativeCell(currencyModels: _currencyController.listBinance.value??[],
              exchangeType: exchangeValue,),],
        )))
      ],),
    )
    );
  }

  _onCoinPageChanged(page){
    // _onPageChange.add(page);
    print('Page => $page');
    _currencyController.exchangetype.value = page+1;
    _dashboardController.exchnagetype.value = page+1;
    _dashboardController.getXpertgainBalance();
    _currencyController.getCurrencyChanged();
  }
}

