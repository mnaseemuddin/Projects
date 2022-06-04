import 'package:flutter/cupertino.dart';
import 'package:trading_apps/models/graph_model.dart';

class ListItem{
  final IconData icon;
  final String title;

  const ListItem({required this.icon, required this.title});

}

class ListItemWithSubTitle{
  final int id;
  final IconData icon;
  final String title;
  final String subtitle;

  const ListItemWithSubTitle(this.id, {required this.icon, required this.subtitle, required this.title});

}

enum UpDownType {
  DOWN,
  UP
}

class FieldData{
  final String data;
  final bool valid;
  FieldData({required this.data, required this.valid});
}

class TradeModel{
  final int cond;
  final UpDownType? tradeType;
  final bool marketChange;
  final int addFactor;
  TradeModel({this.tradeType, required this.marketChange, this.addFactor=60, this.cond=0,});
}

class Trade{
  final GraphModel model;
  final UpDownType type;
  final int duration;
  final int bidValue;
  final String currency;
  final String uuid;
  bool isActive;

  Trade({required this.model, required this.type, required this.duration, required this.bidValue,
    required this.isActive, required this.currency, required this.uuid,});
}

