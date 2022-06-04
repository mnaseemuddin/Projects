import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
// import 'package:flutter_barcode_scanner/flutter_barcode_scanner.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:get/get.dart';
// import 'package:fluttertoast/fluttertoast.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/modules/dashboard/controllers/dashboard_controller.dart';
import 'package:royal_q/app/routes/app_pages.dart';
import 'package:royal_q/app/shared/constants/colors.dart';
import 'package:royal_q/app/shared/sawidgets/currency_selection.dart';
import 'package:royal_q/app/shared/sawidgets/progress_dialog.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:royal_q/main.dart';
import 'package:transparent_image/transparent_image.dart';


class Withdrawal extends StatefulWidget {
  final double balance;
  const Withdrawal({Key? key, required this.balance}) : super(key: key);

  @override
  _WithdrawalState createState() => _WithdrawalState();
}

class _WithdrawalState extends State<Withdrawal> {

  final String _address = '0xf5904bcad08fea90518ed48afac4bb2642db7b08';
  final TextEditingController _controller = TextEditingController();
  final TextEditingController _controllerAddress = TextEditingController();
  final TextEditingController _controllerPass = TextEditingController();
  double _currencyValue = 1200;
  double _dollarValue = 0;
  bool _obscureText = true;

  List<GetcurrencyResponse>? responseList;
  GetcurrencyResponse? _currencyResponse;// = GetcurrencyResponse(id: '1', currency: 'BTC', price: 1.0, address: 'address', minimumdeposit: 25.0);

  final DashboardController _controllerDashboard = Get.find<DashboardController>();

  @override
  void initState() {

    super.initState();
    // _getBalance();
    _getCurrencyChanged();
  }

  @override
  void dispose() {
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Withdrawal'.tr, style: textStyleHeading2(color: ColorConstants.white),),
        iconTheme: IconThemeData(color: ColorConstants.white),
        elevation: 0,
        actions: [
          TextButton(onPressed: () => Get.toNamed(Routes.WITHDRAWAL_HISTORY), child: Text('History'))
        ],
      backgroundColor: Colors.transparent,),
      // backgroundColor: Colors.transparent,
      body: Container(
        padding: EdgeInsets.only(left: 8, right: 8),
        child: _currencyResponse==null?
        LinearProgressIndicator(color: ColorConstants.APP_SECONDARY_COLOR,)
            :ListView(children: [

              FutureBuilder(
                  future: getRewardBalanceAPI(),
                  builder: (context, AsyncSnapshot<ApiResponse>snapshot){
                    if(!snapshot.hasData)return Container(width: 20, height: 1, child: LinearProgressIndicator(color: ColorConstants.APP_SECONDARY_COLOR,),);
                return ItemView(title: 'Balance:'.tr, child: Text('${snapshot.data!.data['totalAmount']} USDT', style: textStyleHeading2(color: ColorConstants.white)));
              }),
              // ItemView(title: 'Balance:', child: Text('${widget.balance}', style: textStyleHeading2(color: Colors.white))),

              ItemView(title: 'Currency_:'.tr, child: Row(children: [
            // Image.asset('assets/images/icon/$_currencyType@2x.png', width: 36,),
            // Image.asset('assets/images/icon/bnb@2x.png', width: 36,),
            //     FadeInImage.memoryNetwork(
            //       placeholder: kTransparentImage,
            //       imageErrorBuilder: (context, url, error) => Image.asset('assets/images/icon_xg.png', width: 40, height: 40,),
            //       image: 'https://xpertgain.io/symbol/${_currencyResponse!.currency.toLowerCase()}@2x.png',
            //       width: 40, height: 40,
            // ),
            
                Image.asset('assets/icons/ic_usdtron.png', width: 40, height: 40,),
            SizedBox(width: 8,),
            // Expanded(child: GestureDetector(
            //   child: Column(
            //     crossAxisAlignment: CrossAxisAlignment.start,
            //     children: [
            //       Text(_currencyResponse!.currency.toUpperCase(), style: textStyleHeading2(color: ColorConstants.white),),
            //       Text('Change_currency'.tr, style: textStyleLabel(color: ColorConstants.white),)],),
            //   onTap: _onChangeCurrency,
            // )),
                Expanded(child: Text(_currencyResponse!.currency.toUpperCase(), style: textStyleHeading2(color: ColorConstants.white),),),
            // IconButton(onPressed: _onChangeCurrency, icon: Icon(Icons.navigate_next, color: ColorConstants.white,))
          ],)),


              ItemView(title: 'Address:'.tr,  child: Container(
              padding: EdgeInsets.only(left: 8),
              child: TextField(
                controller: _controllerAddress,
                style: textStyleHeading2(color: ColorConstants.white),
                decoration: InputDecoration(
                    border: InputBorder.none,
                    hintText: 'Address',
                    hintStyle: TextStyle(color: ColorConstants.white30),
                  suffixIcon: IconButton(icon: Icon(Ionicons.scan_sharp, color: ColorConstants.APP_SECONDARY_COLOR,), onPressed: () async {
                    // String barcodeScanRes = await FlutterBarcodeScanner.scanBarcode(
                    //     '#ff6666', 'Cancel', true, ScanMode.QR);
                    // String code = barcodeScanRes.substring(barcodeScanRes.indexOf(':')+1);
                    // _controllerAddress.text = code;
                  })
                ),
              ),
              decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(8),
                  border: Border.all(color: ColorConstants.APP_SECONDARY_COLOR, width: 1),

              ),
            )),

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
                    } catch (e) {}
                    return oldValue;
                  }),
                ],
                onChanged: (val){
                  print('val => $val');
                  if(val.isNotEmpty) {
                    setState(() {
                      _dollarValue = double.parse(val);
                      double total = double.parse(val)/_currencyValue;
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

              ItemView(title: 'Trans Pass'.tr,  child: Container(
                padding: EdgeInsets.only(left: 8),
                child: TextField(
                  controller: _controllerPass,
                  obscureText: _obscureText,
                  style: textStyleHeading2(color: ColorConstants.white),
                  onChanged: (val){
                    print('val => $val');
                  },

              decoration: InputDecoration(
                  border: InputBorder.none,
                  hintText: 'Transaction_password'.tr,
                  hintStyle: TextStyle(color: ColorConstants.white30),
                  suffixIcon: IconButton(onPressed: (){
                    setState(() {
                      _obscureText = !_obscureText;
                    });
                  }, icon: Icon(_obscureText?Ionicons.eye_off:Ionicons.eye, color: ColorConstants.APP_SECONDARY_COLOR,))
              ),
            ),
            decoration: BoxDecoration(
                // color: ColorConstants.white30,
                borderRadius: BorderRadius.circular(8),
                border: Border.all(color: ColorConstants.APP_SECONDARY_COLOR, width: 1)
            ),
          )),
              ItemView(title: 'Withdrawal Fee', child: Text('2 USDT', style: textStyleHeading2(),)),
            SizedBox(height: 20,),
            Padding(padding: EdgeInsets.symmetric(horizontal: 24, vertical: 16),
            child: SubmitButton(onPressed: (){

              if(_controllerAddress.text.length<10){
                return EasyLoading.showToast('Enter_correct_address'.tr);
              }

              if(_controller.text.isEmpty){
                return EasyLoading.showToast('Enter_amount'.tr);
              }


              double amount = double.parse(_controller.text.trim());
              if(amount > widget.balance){
                return EasyLoading.showToast('Insufficient_balance'.tr);
              }

              if(_controllerPass.text.length<5){
                EasyLoading.showToast('Enter correct transaction password');
              }

              _controllerDashboard.generateOTPDialog(context, ' to withdrawal amount ${_controller.text}.', (){
                _withdrawalAPI({
                  'userid': userInfo!.id,
                  'Amount': double.parse(_controller.text),
                  'address': _controllerAddress.text.trim(),
                  'Transpassword': _controllerPass.text.trim()
                });
              });


              // _withdrawalAPI({
              //   'userid': userInfo!.id,
              //   'Amount': double.parse(_controller.text),
              //   'address': _controllerAddress.text.trim(),
              //   'Transpassword': _controllerPass.text.trim()
              // });


            }, title: 'Withdraw'),),
        ],),
      ),
    );
  }

  void _onChangeCurrency()async {

    var res = await navPush(context, CurrencyDialog(currencies: responseList??[],));
    if(res!=null){
      print(res);
      _currencyResponse = res['val'];
      _getCurrencyValueResponse(_currencyResponse!.id);
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
          print('$_currencyResponse');
        });
      }else{
        print('Somthing went wrong');
      }
    });
  }

  _withdrawalAPI(body) async{
    EasyLoading.show();
    ApiResponse response = await withdrawalAPI(body);
    EasyLoading.dismiss();
    if(response.status){
      Get.back();
    }

    EasyLoading.showToast(response.data['message']);
    _controllerDashboard.getFundBalance();
  }


}
