import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:royal_q/main.dart';

import '../tabs.dart';

class APIBinding extends StatefulWidget {
  const APIBinding({Key? key}) : super(key: key);

  @override
  _APIBindingState createState() => _APIBindingState();
}

class _APIBindingState extends State<APIBinding> {

  bool _binanceEnable = false;
  bool _huobiEnable = false;

  @override
  void initState() {
    super.initState();
  }
  _initAPI(){
    getApiSecretKeyAPI({
      'id': userInfo!.id,
      'exchanetype': 1
    }).then((value){
      if(value.status){
        ApiKeyResponse data = value.data;
        if(mounted&&data.apikey.isNotEmpty) {
          setState(() {
            _binanceEnable = true;
          });
        }
      }
    });

    getApiSecretKeyAPI({
      'id': userInfo!.id,
      'exchanetype': 2
    }).then((value){
      if(value.status){
        ApiKeyResponse data = value.data;
        if(mounted&&data.apikey.isNotEmpty) {
          setState(() {
            _huobiEnable = true;
          });
        }
      }
    });
  }


  @override
  Widget build(BuildContext context) {
    _initAPI();
    return Scaffold(
      appBar: AppBar(
        title: Text('API Binding', style: textStyleHeading2(color: ColorConstants.white),),
        iconTheme: IconThemeData(color: ColorConstants.white),
        elevation: 0,
        // brightness: Brightness.dark,
        backgroundColor: Colors.transparent,
      ),
      body: Container(
        padding: EdgeInsets.all(16),
        child: ListView(children: [
          BindCell(icon: 'assets/icons/ic_binance.png', title: 'Binance',
            onPressed: () => Get.to(() =>  BindController(title: 'Binance', index: 1,))?.then((value){
              setState(() {
              });
            }),
          enable: _binanceEnable,),

          BindCell(icon: 'assets/icons/ic_huobi.png', title: 'Huobi',
            onPressed: () => Get.to(() =>  BindController(title: 'Huobi', index: 2,))?.then((value){
              setState(() {
              });
            }),
          enable: _huobiEnable,),
          SizedBox()
        ],),
      ),
    );
  }
}

class BindCell extends StatelessWidget {
  final String icon;
  final String title;
  final bool enable;
  final Function()?onPressed;
  const BindCell({Key? key, required this.icon, required this.title, this.onPressed, this.enable=false}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      child: Container(
        child: Row(children: [
          Image.asset(icon, width: 48, height: 48, color: enable?null:ColorConstants.white54,),
          SizedBox(width: 8,),
          Text(title, style: textStyleHeading(color: ColorConstants.whiteRev),),
          SizedBox(width: 8,),
          Container(padding: EdgeInsets.symmetric(vertical: 4, horizontal: 16),
            alignment: Alignment.center, child: Text(enable?'Configured':'Configure', style: textStyleLabel(color: ColorConstants.whiteRev),),
            decoration: BoxDecoration(
                color: enable?ColorConstants.green:ColorConstants.blue,
                borderRadius: BorderRadius.circular(20)
            ),),
          Expanded(child: Container()),
          Icon(Icons.chevron_right, color: ColorConstants.white70, size: 24,)
        ],),
        padding: EdgeInsets.symmetric(horizontal: 16, vertical: 32),
        margin: EdgeInsets.symmetric(vertical: 8),
        decoration: BoxDecoration(
            color: enable?const Color(0xFFF87E02):ColorConstants.grey.withOpacity(0.5),
            borderRadius: BorderRadius.circular(16)
        ),
      ),
      onTap: onPressed,
    );
  }
}
