import 'dart:async';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/modules/dashboard/controllers/dashboard_controller.dart';
import 'package:royal_q/app/modules/dashboard/tabs/controllers/currency_controller.dart';
import 'package:royal_q/app/modules/dashboard/tabs/controllers/futuretrade_controller.dart';
import 'package:royal_q/app/modules/dashboard/tabs/controllers/quantitative_controller.dart';
import 'package:royal_q/app/modules/dashboard/tabs/future/future_cell.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:royal_q/main.dart';

import '../tabs.dart';

class FutureTradeView extends GetView<FutureTradeController> {

  FutureTradeView({Key? key}) : super(key: key);

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
        Expanded(child: Obx(() => PageView(
          controller: _pageController,
          onPageChanged: (page){
            _onPageChange.add(page);
            _onCoinPageChanged(page);
          },
          children: [
            FutureTradeCell(currencyModels: _currencyController.futureUSDTListBinance.value??[],
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

