import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_barcode_scanner/flutter_barcode_scanner.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:ionicons/ionicons.dart';
import 'package:provider/provider.dart';
import 'package:trading_apps/api/apis.dart';
import 'package:trading_apps/api/user_data.dart';
import 'package:trading_apps/custom_ui/common_widget.dart';
import 'package:trading_apps/custom_ui/progress_dialog.dart';
import 'package:trading_apps/models/graph_model.dart';
import 'package:trading_apps/models/tdc_currency_model.dart';
import 'package:trading_apps/models/wallet_model.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/res/constants.dart';
import 'package:trading_apps/res/strings.dart';
import 'package:trading_apps/screens/currency_selection.dart';
import 'package:trading_apps/utility/common_methods.dart';
import 'package:trading_apps/utility/connectivityprovider.dart';

class Withdrawal extends StatefulWidget {
  const Withdrawal({Key? key}) : super(key: key);

  @override
  _WithdrawalState createState() => _WithdrawalState();
}

class _WithdrawalState extends State<Withdrawal> {

  String _currencyType = 'tdc';
  String _address = '0xf5904bcad08fea90518ed48afac4bb2642db7b08';
  bool _isQrReceived = false;
  TextEditingController _controller = TextEditingController();
  TextEditingController _controllerAddress = TextEditingController();
  double _currencyValue = 0;
  double _dollarValue = 0;
  double _balance = 0;
  @override
  void initState() {
    Provider.of<ConnectivityProvider>(context,listen: false).startMonitoring();
    _getBalance();
      setState(() {
        _controllerAddress.text = userModel!.data.first.withdrawaddress=="0"?"":
        userModel!.data.first.withdrawaddress;
        print("TO Address" +_controllerAddress.text);
      });
    super.initState();
  }

  @override
  void dispose() {
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Consumer<ConnectivityProvider>(builder: (ctx,data,child){
      if(data.isOnline!=null){
        return data.isOnline?Scaffold(
          appBar: AppBar(title: Text('Withdraw'),
            elevation: 0,),
          backgroundColor: APP_PRIMARY_COLOR,
          body: Container(
            padding: EdgeInsets.only(left: 8, right: 8),
            child: ListView(children: [
              ItemView(title: 'Balance:', child: Text('\$'+_balance.toStringAsFixed(2), style: textStyleHeading2(color: Colors.white))),

              ItemView(title: 'Currency:', child: Row(children: [
                Image.asset('assets/images/icon/$_currencyType@2x.png', width: 36,),
                SizedBox(width: 8,),
                Expanded(child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text(_currencyType.toUpperCase(), style: textStyleHeading2(color: Colors.white),),
                    Text('Change currency', style: textStyleLabel(color: Colors.white),)],)),
                // IconButton(onPressed: _onChangeCurrency, icon:
                // Icon(Icons.navigate_next, color: Colors.white,))
              ],)),

              ItemView(title: 'Rate(\$):', child: Text('$_currencyValue', style: textStyleHeading2(color: Colors.white))),

              ItemView(title: 'Address:',  child: Container(
                padding: EdgeInsets.only(left: 8),
                child: TextField(
                  enabled: false,
                  controller: _controllerAddress,
                  style: textStyleHeading2(color: Colors.white),
                  decoration: InputDecoration(
                    contentPadding: EdgeInsets.only(right:15),
                      border: InputBorder.none,
                      hintText: '0xFa234....',
                      /*suffixIcon: IconButton(icon: Icon(Ionicons.scan_sharp, color: APP_SECONDARY_COLOR,), onPressed: () async {
                        String barcodeScanRes = await FlutterBarcodeScanner.scanBarcode(
                            '#ff6666', 'Cancel', true, ScanMode.QR);
                        String code = barcodeScanRes.substring(barcodeScanRes.indexOf(':')+1);
                        _controllerAddress.text = code;
                        if(_controllerAddress.text=='-1'){
                          _controllerAddress.clear();
                        }
                      })*/
                  ),
                ),
                decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(8),
                    border: Border.all(color: APP_SECONDARY_COLOR, width: 1)
                ),
              )),

              ItemView(title: 'Amount(\$):',  child: Container(
                padding: EdgeInsets.only(left: 8),
                child: TextField(
                  controller: _controller,
                  style: textStyleHeading2(color: Colors.white),
                  keyboardType: TextInputType.numberWithOptions(decimal: true),
                  inputFormatters: [
                    FilteringTextInputFormatter.allow(RegExp(r"[0-9.]")),
                    TextInputFormatter.withFunction((oldValue, newValue) {
                      try {
                        final text = newValue.text;
                        if (text.isNotEmpty) double.parse(text);
                        return newValue;
                      } catch (e) {}
                      return oldValue;
                    }),
                  ],
                  onChanged: (val){
                    print('val => $val');
                    if(val.length>0)
                      setState(() {
                        _dollarValue = double.parse(val);
                        double total = double.parse(val)/_currencyValue;
                      });
                  },

                  decoration: InputDecoration(
                      border: InputBorder.none,
                      hintText: 'Enter amount(in\$)'

                  ),
                ),
                decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(8),
                    border: Border.all(color: APP_SECONDARY_COLOR, width: 1)
                ),
              )),

              SizedBox(height: 20,),
              SubmitButton(onPressed: (){
                keyboardDismissed(context);
                // if(_controllerAddress.text.length<10){
                //   return Fluttertoast.showToast(msg: 'Enter correct address', gravity: ToastGravity.CENTER);
                // }

                if(_controller.text.length<=0){
                  return DialogUtils.instance.edgeAlerts(context,'Enter amount');
                }

                  if(userModel!.data.first.withdrawaddress=='0'){
                    return DialogUtils.instance.edgeAlerts(context, 'Please firstly save your withdrawal address in your profile .');
                }

                double amount = double.parse(_controller.text.trim());
                if(amount > _balance){
                  return DialogUtils.instance.edgeAlerts(context,'You have less balance');
                }

                if(_currencyType == 'bnb'){

                  // http://mainbusd.liveshop.co.in/?
                  // ToAddressT=0xf99b296D378b4572f5e1D9fCE269fe0f9817f69A
                  // &AmountT=1000000000
                  // &PKEYT=0x14bc2b0f8d64fc93cea0bf97c4eb5a7a8406622fda8f36fc24677691cf1c4438

                  String ToAddressT = _controllerAddress.text.trim();
                  print(ToAddressT);
                  String PKEYT = '0x14bc2b0f8d64fc93cea0bf97c4eb5a7a8406622fda8f36fc24677691cf1c4438';
                  int AmountT = int.parse('${_controller.text.trim()}')*1000000;

                  String url = 'http://mainbusd.liveshop.co.in/?ToAddressT=$ToAddressT&AmountT=$AmountT&PKEYT=$PKEYT';
                  _withdrawalAPI(url, 'bnb');
                }else if(_currencyType == 'tdc'){
                  /*    http://mainbusd.liveshop.co.in/?
                  ContractAddressT=0x6f13598e40d15228c7132b904957c80be6895cfe
                  &ToAddressT=0xf99b296D378b4572f5e1D9fCE269fe0f9817f69A
                  &AmountT=1000000000
                  &PKEYT=0x14bc2b0f8d64fc93cea0bf97c4eb5a7a8406622fda8f36fc24677691cf1c4438*/
                  String ContractAddressT = '0x6f13598e40d15228c7132b904957c80be6895cfe';
                  String ToAddressT = _controllerAddress.text.trim();
                  String PKEYT = '8f5df43f8821dd1d35401ffb6fc4b041be6dfa1c634c741faf153b4173987874';
               //   int AmountT = int.parse('${_controller.text.trim()}/$_currencyValue')*100000000;
                  var AmountT=double.parse(_controller.text.trim())/_currencyValue*100000000;
                  String url = 'https://bep.superbtron.com/?ContractAddressT=$ContractAddressT&ToAddressT=$ToAddressT&AmountT=${AmountT.toInt()}&PKEYT=$PKEYT';
                  _withdrawalAPI(url, 'tdc');
                }
              }, title: 'Withdraw'),
            ],),
          ),
        ):NoInternetConnection();
      }
      return Container(
        child: Center(
          child: CircularProgressIndicator(),
        ),
      );
    });
  }

  void _onChangeCurrency() async{
    var res = await navPush(context, CurrencyDialog(currencies: WITHDRAW_CURRENCIES,));
    if(res!=null){
      print(res);
      _currencyType = res['val'];
      setState(() {});
      if(_currencyType == 'tdc'){
        _getLiveTDCAPI();
      }else {
        _getCurrencyChanged();
      }
    }
  }

  void _onCreateChangeCurrency() async{
    showDialog(context: context, builder: (context) => ProgressDialog(),
        barrierDismissible: false).then((value){
      print(value);
    },);
      //  _getCurrencyChanged();
    _getLiveTDCAPI();

  }

  _getCurrencyChanged(){
    showDialog(context: context, builder: (context) => ProgressDialog(),
        barrierDismissible: false).then((value){
      print(value);
    },);
    graphDataAPI(baseMarket: _currencyType+'usdt').then((value){
      Navigator.of(context).pop();
      if(value.status){
        GraphModel model = value.data;
        if(mounted)setState(() => _currencyValue = model.ticker.last);
      }
    });
  }



  _getBalance(){
    userWalletAPI(userModel?.data.first.userId).then((value){
      if(value.status){
        WalletModel model = value.data;
        if(mounted)setState(() => _balance = double.parse(model.data.first.walletAmount));
        _onCreateChangeCurrency();
      }
    });
  }

  _withdrawalAPI(String url, String type){
    showDialog(context: context, builder: (context) => ProgressDialog(), barrierDismissible: false).then((value){
      print(value);
    },);

    withdrawalAPI(url, type).then((value){
      Navigator.of(context).pop();
      if(value.status){
      // _getBalance();
        Map body = {
          "user_id": userModel!.data.first.userId,
          "amount": _controller.text.trim(),
          "coin": _currencyType,
          "coin_rate": _currencyValue,
          "address": _controllerAddress.text.trim(),
          "added_time": "${DateTime.now().toString()}"
        };
      _withdrawAmountAPI(body);
      }else{
        Fluttertoast.showToast(msg: 'Something went wrong', backgroundColor: Colors.red.withOpacity(0.5), gravity: ToastGravity.CENTER);
      }
    });
  }

  _withdrawAmountAPI(Map body){
    showDialog(context: context, builder: (context) => ProgressDialog(), barrierDismissible: false).then((value){
      print(value);
    },);
    withdrawAmountAPI(body).then((value){
      Navigator.of(context).pop();
      if(value.status){
        _controller.clear();
        Fluttertoast.showToast(msg: 'Transaction completed', backgroundColor: Colors.green.
        withOpacity(0.5), gravity: ToastGravity.CENTER);
        _getBalance();
      }else{
        Fluttertoast.showToast(msg: value.message.toString(), backgroundColor: Colors.green.
        withOpacity(0.5), gravity: ToastGravity.CENTER);
      }
    });
  }

  _getLiveTDCAPI(){
    liveTDCAPI().then((value){
      Navigator.of(context).pop();
      if(value.status){
        TdcCurrencyModel model = value.data;
        if(mounted)setState(() => _currencyValue = double.parse(model.data.first.rateDollar));
      }
    });
  }
}
