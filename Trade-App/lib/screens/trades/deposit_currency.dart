import 'dart:async';
import 'dart:convert';
import 'dart:math';
import 'package:google_fonts/google_fonts.dart';
import 'package:http/http.dart'as http;
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:provider/provider.dart';
import 'package:qr_flutter/qr_flutter.dart';
import 'package:share/share.dart';
import 'package:trading_apps/api/apis.dart';
import 'package:trading_apps/api/user_data.dart';
import 'package:trading_apps/custom_ui/common_widget.dart';
import 'package:trading_apps/custom_ui/progress_dialog.dart';
import 'package:trading_apps/models/graph_model.dart';
import 'package:trading_apps/models/hashkeymodel.dart';
import 'package:trading_apps/models/tdc_currency_model.dart';
import 'package:trading_apps/models/trans_check_model.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/res/constants.dart';
import 'package:trading_apps/res/strings.dart';
import 'package:trading_apps/screens/currency_selection.dart';
import 'package:trading_apps/screens/dashboard.dart';
import 'package:trading_apps/utility/common_methods.dart';
import 'package:trading_apps/utility/connectivityprovider.dart';
import 'package:xml_parser/xml_parser.dart';

class DepositCurrency extends StatefulWidget {
  const DepositCurrency({Key? key}) : super(key: key);

  @override
  _DepositCurrencyState createState() => _DepositCurrencyState();
}

class _DepositCurrencyState extends State<DepositCurrency> {

  String _currencyType = 'bnb';// = 'Select Currency';
  String _address = '0xf5904bcad08fea90518ed48afac4bb2642db7b08';
  bool _isQrReceived = false;
  TextEditingController _controller = TextEditingController();
  double _currencyValue = 1200;
  double _dollarValue = 0,_dollerValue1=0;
  Timer? _timer;
  int _delayCount = 0;
  int _delayDuration = 15;
  int _duration = 500;

  var res;
  String txnId='';

  TextEditingController controllerPayHash=TextEditingController();

  @override
  void initState() {
    Provider.of<ConnectivityProvider>(context,listen: false).startMonitoring();
    _getCurrencyChanged("onStart");
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
    return Consumer<ConnectivityProvider>(builder: (ctx,data,child){
      if(data.isOnline!=null){
        return data.isOnline?Scaffold(
          appBar: AppBar(title: Text('Deposit'),
            elevation: 0,),
          backgroundColor: APP_PRIMARY_COLOR,
          body: Container(
            padding: EdgeInsets.only(left: 8, right: 8),
            child: ListView(children: [

              !_isQrReceived?Column(children: [
                ItemView(title:'Pay Mode:', child: Row(children: [
                  Image.asset('assets/images/icon/$_currencyType@2x.png', width: 36,),
                  SizedBox(width: 8,),
                  Expanded(child: GestureDetector(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Text(_currencyType.toUpperCase(), style: textStyleHeading2(color: Colors.white),),
                        Text('Change currency:', style: textStyleLabel(color: Colors.white),)],),
                    onTap: _onChangeCurrency,
                  )),
                  IconButton(onPressed: _onChangeCurrency, icon: Icon(Icons.navigate_next, color: Colors.white,))
                ],)),
                ItemView(title: 'Price(\$):', child: Text(_currencyValue.toStringAsFixed(2), style: textStyleHeading2(color: Colors.white))),

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
                          _dollerValue1=_dollarValue/_currencyValue;
                          // double total = double.parse(val)/_currencyValue;
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
//
                ItemView(title: 'Net Amount',  child: Text('${_dollerValue1.toStringAsFixed(4)} '
                    '${_currencyType.toUpperCase()}', style: textStyleHeading2(color: Colors.white),)),

                SizedBox(height: 20,),
                SubmitButton(onPressed: (){
                  if(_controller.text.length>0){
                  /*  // Map body = {
                    //   "key": "bitbk8269855",
                    //   "url": "http://tradingclub.fund",
                    //   "id": 1000,
                    //   "coin": "BTC"
                    // };
                  *//*  Map body = {
                      "key": "bitbk8269855",
                      "url": "http://tradingclub.fund",
                      "id": userModel!.data.first.userId,
                      //  "coin": "BTC"
                      "coin": _currencyType.toUpperCase()
                    };
                    _generateNewAddress(body);*//*
                    // setState(() {
                    //   // _isQrReceived = true;
                    // });
                    print(_currencyType);*/
                    if(_currencyType=="tdc"){
                      setState(() {
                        _address="0xdfa32AbaD42F7c323Ccdc835b7c32EEA7BCA5Ad8";
                        _isQrReceived=true;
                      });
                    }else {
                      _generateNewAddressAPI();
                    }

                  }else{
                    Fluttertoast.showToast(msg: 'Enter amount', gravity: ToastGravity.CENTER);
                  }
                }, title: 'Pay Now'),

              ],):Container(),
              SizedBox(height: 40,),
              _isQrReceived?_qrCodeView():Container(),
              _isQrReceived?Align(alignment: Alignment.topRight,
                child: TextButton.icon(onPressed: (){
                  setState(() {
                    _timer?.cancel();
                    _isQrReceived = false;
                  });
                }, icon: Icon(Icons.cancel, color: Colors.white,), label: Text('Cancel', style:
                TextStyle(color: Colors.white),)),):Container()
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

  Widget _qrCodeView() => Container(
    margin: EdgeInsets.only(top:16,left: 16,right: 16,bottom: 8),
    padding: EdgeInsets.only(top:16,left: 16,right: 16,bottom: 8),
    child: Column(children: [
      LinearProgressIndicator(backgroundColor: APP_SECONDARY_COLOR,),
      SizedBox(height: 8,),
      Text('Scan or copy the below address to PAY', style: textStyleHeading2(color: APP_SECONDARY_COLOR),),
      SizedBox(height: 16,),

      Text('Scan', style: textStyleHeading(color: APP_PRIMARY_COLOR),),
      QrImage(
        data: _address,
        version: QrVersions.auto,
        size: 200.0,
        embeddedImage: AssetImage('assets/images/icon/$_currencyType@2x.png'),
      ),

      Row(children: [
        Expanded(child: Text('$_address', textAlign: TextAlign.center,)),
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
                Fluttertoast.showToast(msg: 'Address copied',
                    backgroundColor: APP_SECONDARY_COLOR.withOpacity(0.5));
              }, icon: Icon(Icons.copy), label: Text('Copy')),
              Container(width: 1, color: APP_PRIMARY_COLOR,
              margin: EdgeInsets.all(4),),
              TextButton.icon(onPressed: (){
                Share.share(_address);
              }, icon: Icon(CupertinoIcons.arrowshape_turn_up_right_fill), label: Text('Share')),
          ],
        ),

        decoration: BoxDecoration(
          color: APP_SECONDARY_COLOR.withOpacity(0.5),
          borderRadius: BorderRadius.circular(25)
        ),
      ),

      _currencyType=="tdc"?Padding(
        padding: const EdgeInsets.only(top:15.0,bottom: 8),
        child: TextFormField(
          style: GoogleFonts.aBeeZee(fontSize: 14),
          controller: controllerPayHash,
          textCapitalization: TextCapitalization.sentences,
          textInputAction: TextInputAction.next,
          inputFormatters: [
            LengthLimitingTextInputFormatter(100),
          ],
          validator: (v){
            if(v!.trim().isEmpty){
              return "Please Enter Payment Hash .";
            }
            return null;
          },

          textAlign: TextAlign.left,
          decoration: InputDecoration(
            enabledBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(9),
              borderSide: BorderSide(
                color: Color(0xff000000),
                width: 0.70,
              ),
            ),
            contentPadding: EdgeInsets.only(left:10),
            errorStyle: GoogleFonts.aBeeZee(fontSize: 14.5,color:
            Colors.redAccent,fontWeight: FontWeight.w600),
            focusColor: Color(0xff000000),
            border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(5.0),
                borderSide: BorderSide(color: Color(0xff000000))
            ),
            filled: true,
            fillColor: Color(0xffffffff),
            focusedBorder: OutlineInputBorder(
              borderSide: BorderSide(color: Color(0xff000000)),
              borderRadius: BorderRadius.circular(9.0),
            ),
            hintText: "Enter Payment Hash",
            hintStyle: GoogleFonts.aBeeZee(
                fontSize: 14, color: Colors.grey[800]),
          ),
        ),
      ):Container(),

      _currencyType=='tdc'?checkHashButton():Container()

    ],),
    decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(8)
    ),
  );

  void _onChangeCurrency()async {
     res = await navPush(context, CurrencyDialog(currencies: DEPOSITE_CURRENCIES,));
    if(res!=null){
      print(res);
      _currencyType = res['val'];
      setState(() {});
      if(_currencyType == 'tdc'){
        showDialog(context: context, builder: (context) => ProgressDialog(),
            barrierDismissible: false).then((value){
          print(value);
        },);
        _getLiveTDCAPI();
      }else {
        showDialog(context: context, builder: (context) => ProgressDialog(),
            barrierDismissible: false).then((value){
          print(value);
        },);
        _getCurrencyChanged("afterChanged");
      }
    }
  }

  _getCurrencyChanged(String type){
    graphDataAPI(baseMarket: _currencyType+'usdt').then((value){

      if(type!="onStart")
      Navigator.of(context).pop();
      if(value.status){
        GraphModel model = value.data;
        if(mounted)setState(() => _currencyValue = model.ticker.last);
        print(_currencyValue);
      }
    });
  }

  _getLiveTDCAPI(){
    liveTDCAPI().then((value){
      Navigator.of(context).pop();
      if(value.status){
        TdcCurrencyModel model = value.data;
        if(mounted)setState(() => _currencyValue = double.parse(model.data.first.rateDollar));
      }else{
       Fluttertoast.showToast(msg: value.message.toString());
      }
    });
  }

  _generateNewAddress(Map body){

    showDialog(context: context, builder: (context) => ProgressDialog(), barrierDismissible: false).then((value){
      print(value);
    },);

    generateNewAddressAPI(body).then((value){
      Navigator.of(context).pop();
      if(value.status){
        if(mounted){
          setState(() {
            _address = value.data['Address'];
            _isQrReceived = true;
            _initTimer();
          });
        }
      }else{
        Fluttertoast.showToast(msg: value.message.toString(),toastLength: Toast.LENGTH_LONG);
      }
    });
  }

  _initTimer(){
    _timer = Timer.periodic(Duration(seconds: _delayDuration), (timer) {
      _delayCount +=_delayDuration;
      if(_delayCount<_duration){
        _checkTransactionAPI(userModel!.data.first.userId.toString(), _address,txnId);
      }else{
        _timer?.cancel();
        Fluttertoast.showToast(msg: 'Transaction in pending we will update your wallet once it successful.', backgroundColor:
        Colors.red.withOpacity(0.5), gravity: ToastGravity.CENTER);
        Navigator.of(context).pop();
      }

    });
  }


//api.sandbox.nowpayments.io
  _checkTransactionAPI(userId, address,txnId)async{
     //txnId='4359269222';
    // checkTransactionAPI(userId, address).then((value){
    //   if(value.status){
    //     TransactionCheckModel model = value.data;
    //     if(model.data.length==0){
    //       Fluttertoast.showToast(msg: 'Waiting for transaction...');
    //     }else{
    //       Fluttertoast.showToast(msg: 'Transaction completed',
    //           backgroundColor: Colors.green.withOpacity(0.5),
    //       gravity: ToastGravity.CENTER);
    //       _timer?.cancel();
    //       Navigator.of(context).pop();
    //       // _addAmountMethod(userModel!.data.first.userId,model.data.first.amount,model.data.first.fiatAmount,
    //       //     model.data.first.txnId,model.data.first.coin,model.data.first.confirms,
    //       //     model.data.first.address);
    //     }
    //   }
    // });
    try {
      final response = await http.get(
          Uri.parse('https://api.nowpayments.io/v1/payment/$txnId'),
          headers: {'x-api-key': '2MQ30GA-NJJ4H5R-Q6NQRWR-J4KYPB9'});
      if (!mounted) return;
      setState(() {
        if (response.statusCode == 200) {
          Map data = json.decode(response.body);
          String Type="OtherWallet";
          if (data['payment_status'] == "finished") {
            String paymentStatus=data['payment_status'];
            String actullyPaidAmt=data['actually_paid'];
           _addAmountAPI(userId,_controller.text,'', txnId,
               _currencyType.toString(),'', address,paymentStatus,
               _currencyValue.toString(),actullyPaidAmt,'',Type);
          }else if(data['payment_status'] == "partially_paid") {
            String paymentStatus=data['payment_status'];
            String actullyPaidAmt=data['actually_paid'].toString();
            _addAmountAPI(userId,_controller.text,'', txnId,
                _currencyType.toUpperCase().toString(),'', address,paymentStatus,
                _currencyValue.toString(),actullyPaidAmt,'',Type);
          }else {
            Fluttertoast.showToast(msg: 'Waiting for transaction...');
          }
        }
      });
    }catch(e){
     toastMessages(e.toString());
    }
  }


//{"payment_id":"5652875883","payment_status":"waiting",
// "pay_address":"TSm2wr7WVkh4S1Eh16sCfBpQWKNaBqfDAV","price_amount":0.1,"price_currency":"usd",
// "pay_amount":"1.506795","amount_received":1.299761,"pay_currency":"TRX",
// "order_id":"TDC6320007","order_description":"Placed by 26",
// "ipn_callback_url":"https://nowpayments.io",
// "created_at":"2022-02-15T13:17:22.592Z","updated_at":"2022-02-15T13:17:22.592Z",
// "purchase_id":"4315770498",
// "smart_contract":null,"network":null,"network_precision":null,"time_limit":null}


  void _addAmountAPI(String userId, String amount, String fiatAmount,
      String txnId, String coin, String confirms, String address,String payStatus,
      String currencyRate,String actullyPaid, String hash, String type) async{

    Map body = {
      'user_id':userId,'amount':amount,'fiat_amount': fiatAmount,
      'txn_id':txnId,'coin':coin,
      'confirms':confirms,'address':address,'type':'main_wallet',
      'payment_status':payStatus,'pay_amount':amount,
      'actually_paid':actullyPaid,'live_rate':currencyRate,'payment_hash':hash
    };
    if(type!='OtherWallet'){
      addAmountInWalletByTDCAPI(body).then((value){
        if(value.status){
          print(value.status);
          Navigator.of(context).pop();
          Fluttertoast.showToast(msg: 'Transaction completed',
              backgroundColor: Colors.green.withOpacity(0.5),
              gravity: ToastGravity.CENTER);
          _timer?.cancel();
          navPush(context, Dashboard(from: 'Profile'));
        }else{
          _controller.clear();
          Navigator.of(context).pop();
          Fluttertoast.showToast(msg: value.message.toString(),
              backgroundColor: Colors.green.withOpacity(0.5),
              gravity: ToastGravity.CENTER);
        }
      });
    }else{
      addAmountInWalletAPI(body).then((value){
        if(value.status){
          print(value.status);
          Navigator.of(context).pop();
          Fluttertoast.showToast(msg: 'Transaction completed',
              backgroundColor: Colors.green.withOpacity(0.5),
              gravity: ToastGravity.CENTER);
          _timer?.cancel();
          navPush(context, Dashboard(from: 'Profile'));
        }else{
          Navigator.of(context).pop();
          Fluttertoast.showToast(msg: value.message.toString(),
              backgroundColor: Colors.green.withOpacity(0.5),
              gravity: ToastGravity.CENTER);
        }
      });
    }
  }


  void addAmountAPI(int userId, String amount, String fiatAmount,
      String txnId, String coin, String confirms, String address,String payStatus,
      String currencyRate,String actullyPaid) async{

    Map body = {
      'user_id':userId,'amount':amount,'fiat_amount': fiatAmount,
      'txn_id':txnId,'coin':coin,
      'confirms':confirms,'address':address,'type':'main_wallet',
      'payment_status':payStatus,'pay_amount':amount,
      'actually_paid':actullyPaid,'live_rate':currencyRate
    };
    addAmountInWalletAPI(body).then((value){
      if(value.status){
        _initTimer();
      }else{
        Fluttertoast.showToast(msg: value.message.toString(),
            backgroundColor: Colors.green.withOpacity(0.5),
            gravity: ToastGravity.CENTER);
      }
    });
  }

  _generateNewAddressAPI() async {
    keyboardDismissed(context);
    showDialog(context: context, builder: (context) => ProgressDialog(), barrierDismissible: false).then((value){
      print(value);
    },);
    Random random = new Random();
    int orderNo = random.nextInt(10000000);
    String amount=_controller.text;
    int userId=userModel!.data.first.userId;
    Map<String,dynamic>body={
      'price_amount':amount,
      'price_currency':'usd',
      'pay_currency':_currencyType.toUpperCase(),
      "ipn_callback_url": "https://nowpayments.io",
      "order_id": "TDC$orderNo",
      "order_description": "  "
    };
    try{
      /*// var headers = {
      //   'x-api-key': '2MQ30GA-NJJ4H5R-Q6NQRWR-J4KYPB9',
      //   'Content-Type': 'application/json'
      // };
      // var request = http.Request('POST', Uri.parse('https://api.nowpayments.io/v1/payment'));
      // request.body = '''{\n  "price_amount": $amount,\n  "price_currency": "usd",\n  "pay_currency": "TRX",\n  "ipn_callback_url": "https://nowpayments.io",\n  "order_id": "TDC$orderNo",\n  "order_description": "Placed by $userId"\n}''';
      // request.headers.addAll(headers);
      // http.StreamedResponse response = await request.send();
      // print("API Resposne :"+response.stream.bytesToString().toString());
      // print(await response.stream.bytesToString());
      // if (response.statusCode == 200) {
      //   print(await response.stream.bytesToString());
      // } else {
      //   print(response.reasonPhrase);
      // }*/
      final response=await http.post(Uri.parse('https://api.nowpayments.io/v1/payment'),
          headers: {'x-api-key':'AX64N7T-3H849H0-KXEW0A8-Z6DNVBY'},body:body);
     setState(() {
       Map data = json.decode(response.body);
       print(data.toString());
       if(response.statusCode==201) {
         Navigator.of(context).pop();
         print(data.toString());
         txnId=data['payment_id'];
         _address = data['pay_address'];
         String paymentStatus=data['payment_status'];
         _isQrReceived = true;
         addAmountAPI(userId,_controller.text,'', txnId,
             _currencyType.toUpperCase().toString(),'', _address,paymentStatus,
             _currencyValue.toString(),'');
       }else{
         Navigator.of(context).pop();
         DialogUtils.instance.edgeAlerts(context, data['message']);
       }
     });
    }catch(e){
      Navigator.of(context).pop();
      DialogUtils.instance.edgeAlerts(context, e.toString());
    }
  }

  void toastMessages(String msg) {
    Fluttertoast.showToast(msg: msg);
  }
//150000000
  checkHashButton() =>TextButton(onPressed: (){
    keyboardDismissed(context);
    showDialog(context: context, builder: (context) => ProgressDialog(), barrierDismissible: false).then((value){
      print(value);
    },);
    if(controllerPayHash.text.isNotEmpty){
      hashKeyAPI().then((value){
        if(value.status){
         setState(() {
           HashKeyModel model=value.data;
           for(int i=0;i<model.result.length;i++){
             if(controllerPayHash.text==model.result[i].hash){
               print('Transaction Block No : '+model.result[i].blockNumber);
               int value=int.parse(model.result[i].value);
               var actuallyPaid=value/100000000;
               String Type="TDCWallet";
               _addAmountAPI(userModel!.data.first.userId.toString(),_controller.text,
               '',model.result[i].blockNumber,_currencyType.toUpperCase(),'',
                   model.result[i].to,'finished',_currencyValue.toString(),
                   actuallyPaid.toString(),model.result[i].hash,Type);
             }
           }
         });
        }else{
          DialogUtils.instance.edgeAlerts(context,value.message.toString());
        }
      });
    }else{
      DialogUtils.instance.edgeAlerts(context,"Please enter your payment hash");
    }

  }, child: Container(
    width: MediaQuery.of(context).size.width*.50,
    height: 50,
    alignment: Alignment.center,
    child: Text('Check Payment Hash', style: TextStyle(color: APP_PRIMARY_COLOR, fontSize: 14.7),),
    decoration: BoxDecoration(
        color: APP_SECONDARY_COLOR,
        borderRadius: BorderRadius.circular(25)
    ),
  ));
}

//TFcRAEfqWaSgaprezxyLyQukM5pru84m13


class TDCWalletScreens extends StatefulWidget {
  const TDCWalletScreens({Key? key}) : super(key: key);

  @override
  _TDCWalletScreensState createState() => _TDCWalletScreensState();
}

class _TDCWalletScreensState extends State<TDCWalletScreens> {


  String _currencyType = 'tdc';// = 'Select Currency';
  String _address = '0xf5904bcad08fea90518ed48afac4bb2642db7b08';
  bool _isQrReceived = false;
  TextEditingController _controller = TextEditingController();
  double _currencyValue = 1200;
  double _dollarValue = 0,_dollarValue1=0;
  Timer? _timer;
  int _delayCount = 0;
  int _delayDuration = 15;
  int _duration = 300;

  var controllerPayHash=TextEditingController();

  @override
  void initState() {
    Provider.of<ConnectivityProvider>(context,listen: false).startMonitoring();
    _getCurrencyChanged("onStart");
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
    return Consumer<ConnectivityProvider>(builder: (ctx,data,child){
      if(data.isOnline!=null){
        return data.isOnline?Scaffold(
          appBar: AppBar(title: Text('Deposit'),
            elevation: 0,),
          backgroundColor: APP_PRIMARY_COLOR,
          body: Container(
            padding: EdgeInsets.only(left: 8, right: 8),
            child: ListView(children: [

              !_isQrReceived?Column(children: [
                ItemView(title:'Pay Mode:', child: Row(children: [
                  Image.asset('assets/images/icon/$_currencyType@2x.png', width: 36,),
                  SizedBox(width: 8,),
                  Expanded(child: GestureDetector(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Align(
                            alignment: Alignment.centerLeft,
                            child: Text(_currencyType.toUpperCase(), style: textStyleHeading2(color: Colors.white),)),
                        //Text('Change currency:', style: textStyleLabel(color: Colors.white),)
                      ],),
                    onTap: null,
                  )),
                  //  IconButton(onPressed: _onChangeCurrency, icon: Icon(Icons.navigate_next, color: Colors.white,))
                ],)),
                ItemView(title: 'Price(\$):', child: Text('$_currencyValue', style: textStyleHeading2(color: Colors.white))),

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
                          _dollarValue1=_dollarValue/_currencyValue;
                          // double total = double.parse(val)/_currencyValue;
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

                ItemView(title: 'Net Amount',  child: Text('${_dollarValue1.toStringAsFixed(4)} ${_currencyType.toUpperCase()}', style: textStyleHeading2(color: Colors.white),)),

                SizedBox(height: 20,),
                SubmitButton(onPressed: (){
                  if(_controller.text.length>0){
                  /*  // Map body = {
                    //   "key": "bitbk8269855",
                    //   "url": "http://tradingclub.fund",
                    //   "id": 1000,
                    //   "coin": "BTC"
                    // };

                    Map body = {
                      "key": "bitbk8269855",
                      "url": "http://tradingclub.fund",
                      "id": userModel!.data.first.userId,
                      "coin": "BTC"
                      // "coin": _currencyType.toUpperCase()
                    };

                    _generateNewAddress(body);
                    // setState(() {
                    //   // _isQrReceived = true;
                    // });*/
                    setState(() {
                      _address="0xdfa32AbaD42F7c323Ccdc835b7c32EEA7BCA5Ad8";
                      _isQrReceived=true;
                    });

                  }else{
                    Fluttertoast.showToast(msg: 'Enter amount', gravity: ToastGravity.CENTER);
                  }
                }, title: 'Pay Now'),

              ],):Container(),
              SizedBox(height: 40,),
              _isQrReceived?_qrCodeView():Container(),
              _isQrReceived?Align(alignment: Alignment.topRight,
                child: TextButton.icon(onPressed: (){
                  setState(() {
                    _timer?.cancel();
                    _isQrReceived = false;
                  });
                }, icon: Icon(Icons.cancel, color: Colors.white,), label: Text('Cancel', style:
                TextStyle(color: Colors.white),)),):Container()
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

  Widget _qrCodeView() => Container(
    margin: EdgeInsets.only(top:16,left: 16,right: 16,bottom: 8),
    padding: EdgeInsets.only(top:16,left: 16,right: 16,bottom: 8),
    child: Column(children: [
      LinearProgressIndicator(backgroundColor: APP_SECONDARY_COLOR,),
      SizedBox(height: 8,),
      Text('Scan or copy the below address to PAY', style: textStyleHeading2(color: APP_SECONDARY_COLOR),),
      SizedBox(height: 16,),

      Text('Scan', style: textStyleHeading(color: APP_PRIMARY_COLOR),),
      QrImage(
        data: _address,
        version: QrVersions.auto,
        size: 200.0,
        embeddedImage: AssetImage('assets/images/icon/$_currencyType@2x.png'),
      ),

      Row(children: [
        Expanded(child: Text('$_address', textAlign: TextAlign.center,)),
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
              Fluttertoast.showToast(msg: 'Address copied',
                  backgroundColor: APP_SECONDARY_COLOR.withOpacity(0.5));
            }, icon: Icon(Icons.copy), label: Text('Copy')),
            Container(width: 1, color: APP_PRIMARY_COLOR,
              margin: EdgeInsets.all(4),),
            TextButton.icon(onPressed: (){
              Share.share(_address);
            }, icon: Icon(CupertinoIcons.arrowshape_turn_up_right_fill), label: Text('Share')),
          ],
        ),

        decoration: BoxDecoration(
            color: APP_SECONDARY_COLOR.withOpacity(0.5),
            borderRadius: BorderRadius.circular(25)
        ),
      ),

      Padding(
        padding: const EdgeInsets.only(top:15.0,bottom: 8),
        child: TextFormField(
          style: GoogleFonts.aBeeZee(fontSize: 14),
          controller: controllerPayHash,
          textCapitalization: TextCapitalization.sentences,
          textInputAction: TextInputAction.next,
          inputFormatters: [
            LengthLimitingTextInputFormatter(100),
          ],
          validator: (v){
            if(v!.trim().isEmpty){
              return "Please Enter Payment Hash .";
            }
            return null;
          },

          textAlign: TextAlign.left,
          decoration: InputDecoration(
            enabledBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(9),
              borderSide: BorderSide(
                color: Color(0xff000000),
                width: 0.70,
              ),
            ),
            contentPadding: EdgeInsets.only(left:10),
            errorStyle: GoogleFonts.aBeeZee(fontSize: 14.5,color:
            Colors.redAccent,fontWeight: FontWeight.w600),
            focusColor: Color(0xff000000),
            border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(5.0),
                borderSide: BorderSide(color: Color(0xff000000))
            ),
            filled: true,
            fillColor: Color(0xffffffff),
            focusedBorder: OutlineInputBorder(
              borderSide: BorderSide(color: Color(0xff000000)),
              borderRadius: BorderRadius.circular(9.0),
            ),
            hintText: "Enter Payment Hash",
            hintStyle: GoogleFonts.aBeeZee(
                fontSize: 14, color: Colors.grey[800]),
          ),
        ),
      ),

      checkHashButton(),

    ],),
    decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(8)
    ),
  );

  checkHashButton() =>TextButton(onPressed: (){
    keyboardDismissed(context);
    showDialog(context: context, builder: (context) => ProgressDialog(), barrierDismissible: false).then((value){
      print(value);
    },);
    if(controllerPayHash.text.isNotEmpty){
      hashKeyAPI().then((value){
        if(value.status){
          setState(() {
            HashKeyModel model=value.data;
            for(int i=0;i<model.result.length;i++){
              if(controllerPayHash.text==model.result[i].hash){
                print('Transaction Block No : '+model.result[i].blockNumber);
                int value=int.parse(model.result[i].value);
                var actuallyPaid=value/100000000;
                _addAmountAPI(userModel!.data.first.userId.toString(),_controller.text,
                    '',model.result[i].blockNumber,_currencyType.toUpperCase(),'',
                    model.result[i].to,'finished',_currencyValue.toString(),
                    actuallyPaid.toString(),model.result[i].hash);
              }
            }
          });
        }else{
          DialogUtils.instance.edgeAlerts(context,value.message.toString());
        }
      });
    }else{
      DialogUtils.instance.edgeAlerts(context,"Please enter your payment hash");
    }

  }, child: Container(
    width: MediaQuery.of(context).size.width*.50,
    height: 50,
    alignment: Alignment.center,
    child: Text('Check Payment Hash', style: TextStyle(color: APP_PRIMARY_COLOR, fontSize: 14.7),),
    decoration: BoxDecoration(
        color: APP_SECONDARY_COLOR,
        borderRadius: BorderRadius.circular(25)
    ),
  ));


  void _onChangeCurrency()async {
  //  var res = await navPush(context, CurrencyDialog(currencies: DEPOSITE_CURRENCIES,));
    showDialog(context: context, builder: (context) => ProgressDialog(),
        barrierDismissible: false).then((value){
      print(value);
    },);
    _getLiveTDCAPI();
  /*  // if(res!=null){
    //   print(res);
    //   _currencyType = res['val'];
    //   setState(() {});
    //   if(_currencyType == 'tdc'){
    //     showDialog(context: context, builder: (context) => ProgressDialog(),
    //         barrierDismissible: false).then((value){
    //       print(value);
    //     },);
    //     _getLiveTDCAPI();
    //   }else {
    //     showDialog(context: context, builder: (context) => ProgressDialog(),
    //         barrierDismissible: false).then((value){
    //       print(value);
    //     },);
    //     _getCurrencyChanged("afterChanged");
    //   }
    // }*/
  }

  void _addAmountAPI(String userId, String amount, String fiatAmount,
      String txnId, String coin, String confirms, String address,String payStatus,
      String currencyRate,String actullyPaid, String hash) async{
print(hash);
    Map body = {
      'user_id':userId,'amount':amount,'fiat_amount': fiatAmount,
      'txn_id':txnId,'coin':coin,
      'confirms':confirms,'address':address,'type':'tdc_wallet',
      'payment_status':payStatus,'pay_amount':amount,
      'actually_paid':actullyPaid,'live_rate':currencyRate,'payment_hash':hash
    };
    print(body);
    addAmountInWalletByTDCAPI(body).then((value){
      if(value.status){
        Navigator.of(context).pop();
        Fluttertoast.showToast(msg: 'Transaction completed',
            backgroundColor: Colors.green.withOpacity(0.5),
            gravity: ToastGravity.CENTER);
        _timer?.cancel();
        navPush(context, Dashboard(from: 'Profile'));
      }else{
        _controller.clear();
        Navigator.of(context).pop();
        Fluttertoast.showToast(msg: value.message.toString(),
            backgroundColor: Colors.green.withOpacity(0.5),
            gravity: ToastGravity.CENTER);
      }
    });
  }

  _getCurrencyChanged(String type){
    graphDataAPI(baseMarket: _currencyType+'usdt').then((value){

      if(type!="onStart")
        Navigator.of(context).pop();
      if(value.status){
        GraphModel model = value.data;
        if(mounted)setState(() => _currencyValue = model.ticker.last);
        _onChangeCurrency();
      }else{
         _onChangeCurrency();
      }
    });
  }

  _getLiveTDCAPI(){
    liveTDCAPI().then((value){
      Navigator.of(context).pop();
      if(value.status){
        TdcCurrencyModel model = value.data;
        if(mounted)setState(() => _currencyValue = double.parse(model.data.first.rateDollar));
      }else{
        Fluttertoast.showToast(msg: value.message.toString());
      }
    });
  }

  _generateNewAddress(Map body){

    showDialog(context: context, builder: (context) => ProgressDialog(), barrierDismissible: false).then((value){
      print(value);
    },);

    generateNewAddressAPI(body).then((value){
      Navigator.of(context).pop();
      if(value.status){
        if(mounted){
          setState(() {
            _address = value.data['Address'];
            _isQrReceived = true;
            _initTimer();
          });
        }
      }else{
        DialogUtils.instance.edgeAlerts(context, value.message.toString());
      }
    });
  }

  _initTimer(){
    _timer = Timer.periodic(Duration(seconds: _delayDuration), (timer) {
      _delayCount +=_delayDuration;
      if(_delayCount<_duration){
      //  _checkTransactionAPI(userModel!.data.first.userId, _address);
      }else{
        _timer?.cancel();
        Fluttertoast.showToast(msg: 'Transaction time out', backgroundColor: Colors.red.withOpacity(0.5), gravity: ToastGravity.CENTER);
        Navigator.of(context).pop();
      }

    });
  }

  /*_checkTransactionAPI(userId, address){
    checkTransactionAPI(userId, address).then((value){
      if(value.status){
        TransactionCheckModel model = value.data;
        if(model.data.length==0){
          Fluttertoast.showToast(msg: 'Waiting for transaction...');
        }else{
          // Fluttertoast.showToast(msg: 'Transaction completed', backgroundColor: Colors.green.withOpacity(0.5),
          //     gravity: ToastGravity.CENTER);
          // _timer?.cancel();
          // Navigator.of(context).pop();
          _addAmountMethod(userModel!.data.first.userId,model.data.first.amount,model.data.first.fiatAmount,
              model.data.first.txnId,model.data.first.coin,model.data.first.confirms,model.data.first.address);
        }
      }
    });
  }*/
  /*void _addAmountMethod(int userId, String amount, String fiatAmount, String txnId, String coin,
      String confirms, String address) async{

    Map body = {
      'user_id':userId,'amount':amount,'fiat_amount': fiatAmount,
      'txn_id':txnId,'coin':coin,
      'confirms':confirms,'address':address
    };
    addAmountInWalletAPI(body).then((value){
      if(value.status){

      }else{

      }
    });
  }*/


}



