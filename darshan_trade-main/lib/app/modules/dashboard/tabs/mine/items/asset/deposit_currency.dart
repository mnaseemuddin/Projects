import 'dart:async';
import 'dart:ui';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter/services.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
// import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/modules/dashboard/controllers/dashboard_controller.dart';
import 'package:royal_q/app/shared/sawidgets/currency_selection.dart';
import 'package:royal_q/app/shared/sawidgets/progress_dialog.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:qr_flutter/qr_flutter.dart';
import 'package:royal_q/main.dart';
import 'package:share/share.dart';
import 'package:transparent_image/transparent_image.dart';


class DepositCurrency extends StatefulWidget {
  const DepositCurrency({Key? key}) : super(key: key);

  @override
  _DepositCurrencyState createState() => _DepositCurrencyState();
}

class _DepositCurrencyState extends State<DepositCurrency> {

  // String _currencyType = isExpertGain?'rdm':'eez';// = 'Select Currency';
  String? _address;// = '0xf5904bcad08fea90518ed48afac4bb2642db7b08';
  bool _isQrReceived = false;
  final TextEditingController _controller = TextEditingController();
  final TextEditingController _controllerHash = TextEditingController();
  // double _currencyValue = 0.2;
  double _dollarValue = 0;
  // double _minValue = 25;

  Timer? _timer;
  int _delayCount = 0;
  final int _delayDuration = 15;
  final int _duration = 300;

  // int _selected = 1;

  List<GetcurrencyResponse>? responseList;
  GetcurrencyResponse? _currencyResponse;// = GetcurrencyResponse(id: '1', currency: 'BTC', price: 1.0, address: 'address', minimumdeposit: 25.0);
  // GetcurrencyResponse? _currencyValueResponse;// = GetcurrencyResponse(id: '1', currency: 'BTC', price: 1.0, address: 'address', minimumdeposit: 25.0);
  final DashboardController _controllerDashboard = Get.find<DashboardController>();

  @override
  void initState() {
    _getCurrencyChanged();
    super.initState();
  }

  @override
  void dispose() {
    _timer?.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    return Scaffold(
      appBar: AppBar(title: Text('Deposit', style: textStyleHeading2(),),
      iconTheme: IconThemeData(color: ColorConstants.white),
      backgroundColor: Colors.transparent,
        brightness: Brightness.light,
      elevation: 0,),
      backgroundColor: Colors.transparent,
      body: Container(
        padding: EdgeInsets.only(left: 8, right: 8),
        child: ListView(children: [
          
        !_isQrReceived?_currencyResponse==null?
            LinearProgressIndicator(color: ColorConstants.APP_SECONDARY_COLOR,)
            :Column(children: [

            ItemView(title: 'Currency_:'.tr, child: Row(children: [
            // Image.asset('assets/images/icon/$_currencyType@2x.png', width: 36,),
            // Image.asset('assets/images/icon/bnb@2x.png', width: 36,),
            //   FadeInImage.memoryNetwork(
            //     placeholder: kTransparentImage,
            //     image: 'https://xpertgain.io/symbol/${_currencyResponse!.currency.toLowerCase()}@2x.png',
            //     width: 40, height: 40,
            //     imageErrorBuilder: (context, url, error) => Image.asset('assets/images/icon_xg.png', width: 40, height: 40,),
            //   ),
              Image.asset('assets/icons/ic_usdtron.png', width: 40, height: 40,),
            SizedBox(width: 8,),
            // Expanded(child: GestureDetector(
            //   child: Column(
            //     crossAxisAlignment: CrossAxisAlignment.start,
            //     children: [
            //       Text(_currencyResponse!.currency.toUpperCase(), style: textStyleHeading2(color: ColorConstants.white),),
            //       Text('Change currency', style: textStyleLabel(color: ColorConstants.white),)],),
            //   onTap: _onChangeCurrency,
            // )),
              Expanded(child: Text(_currencyResponse!.currency.toUpperCase(), style: textStyleHeading2(color: ColorConstants.white),),),
            // IconButton(onPressed: _onChangeCurrency, icon: Icon(Icons.navigate_next, color: ColorConstants.white,))
          ],)),

          // ItemView(title:'Pay Mode:', child: Row(children: [
          //   // Image.asset('assets/images/icon/bnb@2x.png', width: 36,),
          //   Image.asset(isExpertGain?'assets/images/icon_xg.png':'assets/images/icon.png', width: 36,),
          //   SizedBox(width: 8,),
          //   // Text(_currencyType.toUpperCase(), style: textStyleHeading2(color: Colors.white),),
          //   Text('USDT', style: textStyleHeading2(color: Colors.white),),
          // ],)),
          // ItemView(title: 'Price:', child: Text('$_currencyValue', style: textStyleHeading2(color: Colors.white))),
          //     ItemView(title: 'Price_:'.tr, child: Text('${_currencyResponse!.price}', style: textStyleHeading2(color: ColorConstants.white))),
              ItemView(title: 'Amount'.tr,  child: Container(
                padding: EdgeInsets.only(left: 8),
                child: TextField(
              controller: _controller,
              style: textStyleHeading2(color: ColorConstants.white),

              keyboardType: TextInputType.numberWithOptions(decimal: true),
              inputFormatters: [
                FilteringTextInputFormatter.allow(RegExp(r"[0-9.]")),
                TextInputFormatter.withFunction((oldValue, newValue) {
                  try {
                    final text = newValue.text;
                    if (text.isNotEmpty) double.parse(text);
                    return newValue;
                  } catch (e) {
                    print(e.toString());
                  }
                  return oldValue;
                }),
              ],
              onChanged: (val){
                print('val => $val');
                if(val.isNotEmpty) {
                  setState(() {
                  _dollarValue = double.parse(val);
                  // double total = double.parse(val)/_currencyValue;
                });
                }
              },

              decoration: InputDecoration(
                  border: InputBorder.none,
                  hintText: 'Enter_amount_USDT'.tr,
                  hintStyle: TextStyle(color: ColorConstants.white30),
              ),
            ),
            decoration: BoxDecoration(
                borderRadius: BorderRadius.circular(8),
                border: Border.all(color: ColorConstants.APP_SECONDARY_COLOR, width: 1)
            ),
          )),

          // ItemView(title: 'Net Amount',  child: Text('${_dollarValue/_currencyValue} ${_currencyType.toUpperCase()}', style: textStyleHeading2(color: Colors.white),)),
              ItemView(title: 'Net_Amount'.tr,  child: Text('${_dollarValue/_currencyResponse!.price} ${_currencyResponse!.currency.toUpperCase()}', style: textStyleHeading2(color: ColorConstants.white),)),

              SizedBox(height: 20,),
              Padding(padding: EdgeInsets.symmetric(horizontal: 24, vertical: 16),
              child: SubmitButton(onPressed: (){

                if(_controller.text.isNotEmpty){
                  if(double.parse(_controller.text.toString())<_currencyResponse!.minimumdeposit){
                    EasyLoading.showToast('Minimum_value_should_be'.trParams({'amount': '${_currencyResponse!.minimumdeposit}'}));
                    // EasyLoading.showToast('Minimum value should be greater or equals to ${_currencyResponse!.minimumdeposit}');
                    return;
                  }
                  _generateNewAddress();
                  setState(() {});
              }else{
                EasyLoading.showToast('Enter_amount'.tr);
              }
          }, title: 'Pay_Now'.tr),),

              SizedBox(height: 40,),
              Divider(color: ColorConstants.white24,),
              _confirmTransaction()

        ],):Container(),
          SizedBox(height: 40,),
          _isQrReceived?_qrCodeView():Container(),
          _isQrReceived?Align(alignment: Alignment.topRight,
            child: TextButton.icon(onPressed: (){

              setState(() {
                _isQrReceived = false;
              });

            }, icon: Icon(Icons.cancel, color: ColorConstants.white,), label: Text('Cancel'.tr, style:
              TextStyle(color: ColorConstants.white),)),):Container()
        ],),
      ),
    );
  }

  Widget _confirmTransaction(){
    return Container(
      padding: EdgeInsets.symmetric(horizontal: 16),
      child: Column(children: [
        Text('Have_you_paid'.tr, style: textStyleHeading2(color: ColorConstants.APP_SECONDARY_COLOR),),
        SizedBox(height: 8,),
        Text('Confirm_transaction'.tr, style: textStyleLabel(),),
        SizedBox(height: 16,),
        TextFieldIcon(
          controller: _controllerHash,
          hintText: 'Enter_Hash_key'.tr, onChanged: (val){}, labelText: 'Hash_Key'.tr, ),
        SizedBox(height: 16,),
        SubmitButton(
          onPressed: () {

          if(_controller.text.isEmpty) {
            EasyLoading.showToast('Enter_amount'.tr);
            return;
          }

          if(_controllerHash.text.isEmpty) {
            EasyLoading.showToast('Enter_Hash_key'.tr);
            return;
          }

          Map body = {
            'userid': userInfo?.id,
            'amount': double.parse(_controller.text.trim()).toInt(),
            'price': '${_currencyResponse!.price}',
            'currencyid': int.parse(_currencyResponse!.id),
            'txthash': _controllerHash.text.trim(),
          };
          print(body);
          _depositFundAPI(body);
        }, title: 'Deposit'.tr,)
      ],),
    );
  }

  Widget _qrCodeView() => Container(
    margin: EdgeInsets.all(16),
    padding: EdgeInsets.all(16),
    child: Column(children: [
      LinearProgressIndicator(backgroundColor: ColorConstants.APP_SECONDARY_COLOR,),
      SizedBox(height: 8,),
      Text('Scan_copy'.tr, style: textStyleHeading2(color: ColorConstants.APP_SECONDARY_COLOR),),
      SizedBox(height: 16,),

      Text('Scan'.tr, style: textStyleHeading(color: ColorConstants.APP_PRIMARY_COLOR),),
      _address!=null?QrImage(
        data: _address!,
        version: QrVersions.auto,
        size: 200.0,
        // embeddedImage: AssetImage('assets/images/icon/$_currencyType@2x.png'),
        embeddedImage: AssetImage('assets/images/icon_xg.png'),
      ):Text('Address_not_received'.tr),

      Row(children: [
        Expanded(child: _address!=null?Text(_address!, textAlign: TextAlign.center, style: textStyleLabel(color: ColorConstants.black),):Text('Address_not_received'.tr)),
      ],),

      SizedBox(height: 16,),

      Container(
        height: 50,
        padding: EdgeInsets.only(left: 16, right: 16),
        // width: MediaQuery.of(context).size.width/2,
        child: Row(
          mainAxisSize: MainAxisSize.min,
          children: [
              TextButton.icon(onPressed: (){
                Clipboard.setData(ClipboardData(
                    text: _address
                ));
                EasyLoading.showToast('Address_copied'.tr);
              }, icon: Icon(Icons.copy), label: Text('Copy'.tr)),
              Container(width: 1, color: ColorConstants.APP_PRIMARY_COLOR,
              margin: EdgeInsets.all(4),),
              TextButton.icon(onPressed: (){
                _address!=null?Share.share(_address!):null;
              }, icon: Icon(CupertinoIcons.arrowshape_turn_up_right_fill), label: Text('Share'.tr)),
          ],
        ),

        decoration: BoxDecoration(
          color: ColorConstants.APP_SECONDARY_COLOR.withOpacity(0.5),
          borderRadius: BorderRadius.circular(25)
        ),
      ),

      SizedBox(height: 24,),

      Align(alignment: Alignment.topLeft, child: Text('Warning', style: textStyleHeading2(color: Colors.red),),),
      Text('Make sure to only deposit USDT on the TRC20 network, or else your funds will be lost permanently.',
      style: textStyleLabel(color: ColorConstants.white54),)

    ],),
    decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(8)
    ),
  );

  void _onChangeCurrency()async {
    // var res = await navPush(context, CurrencyDialog(currencies: DEPOSITE_CURRENCIES,));
    var res = await navPush(context, CurrencyDialog(currencies: responseList??[],));
    if(res!=null){
      print(res);

      _currencyResponse = res['val'];
      _getCurrencyValueResponse(_currencyResponse!.id);
      // _getCurrencyChanged();
      // setState(() {});
      // if(_currencyType == 'tdc'){
      //   _getLiveTDCAPI();
      // }else {
      //   _getCurrencyChanged();
      // }
    }
  }

  _getCurrencyChanged(){
    getCurrencyAPI().then((value){
      if(value.status){
        List<GetcurrencyResponse> data = value.data;
        responseList = data;
        if(data.isNotEmpty){
          _currencyResponse ??= data.first;
        }
        _getCurrencyValueResponse(_currencyResponse?.id??1);
        setState(() {});
      }
    });
  }

  _getCurrencyValueResponse(id){
    getCurrencyByIdAPI(id).then((value){
      if(value.status){
        setState(() {
          _currencyResponse = value.data!;
          _address = _currencyResponse!.address;
          print('$_currencyResponse');
        });
      }else{
        print('Somthing went wrong');
      }
    });
  }

  // _getLiveTDCAPI(){
  //   liveTDCAPI().then((value){
  //     if(value.status){
  //       TdcCurrencyModel model = value.data;
  //       if(mounted)setState(() => _currencyValue = double.parse(model.data.first.rateDollar));
  //     }
  //   });
  // }

  _generateNewAddress(){

    showDialog(context: context, builder: (context) => ProgressDialog(), barrierDismissible: false).then((value){
      print(value);
    },);

    getNewAddressAPI().then((value){
      Navigator.of(context).pop();
      if(value.status){
        if(mounted){
          setState(() {
            // _address = value.data['address'];
            _isQrReceived = true;
            // _initTimer();
          });
        }
      }
    });
  }

  _depositFundAPI(Map body){
    showDialog(context: context, builder: (context) => ProgressDialog(), barrierDismissible: false).then((value){
      print(value);
    },);
    depositFundAPI(body).then((value){
      Navigator.of(context).pop();
      if(mounted&&value.status){
        Get.back();
      }else{
        EasyLoading.showToast(value.data['message']);
      }
    });
    _controllerDashboard.getFundBalance();
  }


  // _initTimer(){
  //   _timer = Timer.periodic(Duration(seconds: _delayDuration), (timer) {
  //     _delayCount +=_delayDuration;
  //     if(_delayCount<_duration){
  //       // _checkTransactionAPI(userModel!.data.first.userId, _address);
  //     }else{
  //       _timer?.cancel();
  //       EasyLoading.showToast('Transaction_time_out'.tr);
  //       Navigator.of(context).pop();
  //     }
  //
  //   });
  // }

  // _checkTransactionAPI(userId, address){
  //   checkTransactionAPI(userId, address).then((value){
  //     if(value.status){
  //       TransactionCheckModel model = value.data;
  //       if(model.data.length==0){
  //         Fluttertoast.showToast(msg: 'Waiting for transaction...');
  //       }else{
  //         Fluttertoast.showToast(msg: 'Transaction completed', backgroundColor: Colors.green.withOpacity(0.5),
  //         gravity: ToastGravity.CENTER);
  //         _timer?.cancel();
  //         Navigator.of(context).pop();
  //       }
  //     }
  //   });
  // }

}

