
import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/shared/constants/colors.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:royal_q/main.dart';
import 'package:transparent_image/transparent_image.dart';
// import 'package:trading_apps/api/apis.dart';
// import 'package:trading_apps/api/user_data.dart';
// import 'package:trading_apps/res/colors.dart';
// import 'package:trading_apps/res/constants.dart';
// import 'package:trading_apps/utility/common_methods.dart';
//
// import 'dashboard.dart';

class CurrencyDialog extends StatefulWidget {
  final List<GetcurrencyResponse>currencies;

  const CurrencyDialog({Key? key, required this.currencies}) : super(key: key);

  @override
  _CurrencyDialogState createState() => _CurrencyDialogState();
}

class _CurrencyDialogState extends State<CurrencyDialog> {

  List<GetcurrencyResponse> _tempList = [];

  @override
  void initState() {
    widget.currencies.sort((a, b) => a.currency.compareTo(b.currency));
    _tempList = widget.currencies;
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Select currency', style: textStyleHeading2(color: ColorConstants.white),),
        iconTheme: IconThemeData(color: ColorConstants.white),
        ),
      backgroundColor: Colors.transparent,

      body: Column(children: [
        Container(
          height: 50,
          margin: EdgeInsets.all(8),
          padding: EdgeInsets.all(8),
          child: TextFormField(
            style: TextStyle(color: ColorConstants.white),
            onChanged: (val){
              setState(() {
                _tempList = widget.currencies.where((element) => element.toString().contains(val)).toList();
              });
            },

            decoration: InputDecoration(
              border: InputBorder.none,
              hintText: 'Search',
              suffixIcon: Icon(Icons.search),
              labelStyle: TextStyle(color: ColorConstants.white),
                prefixStyle: TextStyle(color: ColorConstants.white),
            ),
          ),
          decoration: BoxDecoration(
            color: ColorConstants.white10,
            border: Border(bottom: BorderSide(color: ColorConstants.APP_SECONDARY_COLOR))
          ),
        ),
        Expanded(child: ListView.builder(itemBuilder: (context, index){
          return ListTile(
            // leading: Image.asset('assets/images/icon/${_tempList.elementAt(index).currency}@2x.png', width: 36,),
            leading: FadeInImage.memoryNetwork(
              placeholder: kTransparentImage,
              image: 'https://xpertgain.io/symbol/${_tempList.elementAt(index).currency.toLowerCase()}@2x.png',
              width: 40, height: 40,
              imageErrorBuilder: (context, url, error) => Image.asset('assets/images/icon_xg.png', width: 40, height: 40,),
            ),

            title: Text(_tempList.elementAt(index).currency.toString().toUpperCase(), style: textStyleHeading2(color: ColorConstants.white),),
            onTap: (){
              // baseMarket = _currencies.elementAt(index);
              // setCurrency(baseMarket);
              Navigator.of(context).pop({'val':_tempList.elementAt(index)});
            },
          );
        }, itemCount: _tempList.length,))
      ],),

    );
  }
}
