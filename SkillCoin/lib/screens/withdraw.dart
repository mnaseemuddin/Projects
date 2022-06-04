


import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:skillcoin/customui/button.dart';
import 'package:skillcoin/customui/textfields.dart';
import 'package:skillcoin/model/liveratemodel.dart';
import 'package:skillcoin/model/profiledetailsmodel.dart';
import 'package:skillcoin/res/color.dart';
import 'package:skillcoin/res/routes.dart';
import 'package:skillcoin/screens/dashboard.dart';

import '../api/apirepositary.dart';
import '../api/user_data.dart';
import '../customui/progressdialog.dart';
import '../customui/textview.dart';
import '../model/tdccurrencymodel.dart';


class Withdraw extends StatefulWidget {


  const Withdraw({Key? key}) : super(key: key);

  @override
  _WithdrawState createState() => _WithdrawState();
}

class _WithdrawState extends State<Withdraw> {


  var controllerAddress=TextEditingController();
  var controllerAmt=TextEditingController();
  double _currencyValue=0.0;
  ProfileDetailsModel? details;

  double _balance=0.0;

  double amount=0.00;


  @override
  void initState() {
    super.initState();
    _getLiveTDCAPI();
    _getWalletAPI();

  }
  void _getWalletAPI() {

    Map<dynamic,String>body={
      "email":userModel!.data.first.email,
      "token":userModel!.data.first.token
    };
    walletBalanceAPI(body).then((value){
      if(value.status){
        setState(() {
          details=value.data;
          _balance=details!.data.first.walletAmount;
          controllerAddress.text=details!.data.first.skillCoinAddress;
        });
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      top: true,
      child: Scaffold(
        backgroundColor: PRIMARYWHITECOLOR,
        body: Column(
          children: [
          Align(
            alignment: Alignment.centerLeft,
            child: IconButton(onPressed: (){
              navPushOnBackPressed(context);
            }, icon: const Icon(Icons.arrow_back_ios_outlined,size: 20,color:PRIMARYLIGHTGREYCOLOR,)),
          ),

          const Padding(
            padding: EdgeInsets.only(left: 14.0,top: 15),
            child: Align(
                alignment: Alignment.centerLeft,
                child: BoldHeadingText(title: "Send SKL", fontSize: 22, color: PRIMARYBLACKCOLOR)),
          ),


          const Align(
              alignment: Alignment.centerLeft,
              child: Padding(
                padding: EdgeInsets.only(left: 20.0,top: 18),
                child: GoogleFontHeadingText(title: "Address",fontSize: 14,color: PRIMARYLIGHTGREYCOLOR,),
              )),


          WithdrawTextField(
              hintText: 'Long press to paste',
              controller: controllerAddress, validationMsg: ""),

          const Align(
              alignment: Alignment.centerLeft,
              child: Padding(
                padding: EdgeInsets.only(left: 20.0,top: 18),
                child: GoogleFontHeadingText(title: "Amount",fontSize: 14,color: PRIMARYLIGHTGREYCOLOR,),
              )),

            Padding(
              padding: const EdgeInsets.only(top:8.0,left: 18,right: 20),
              child: TextFormField(
                style: const TextStyle(fontSize: 14.5),
                controller: controllerAmt,
                onChanged: (text){
                  setState(() {
                    amount=double.parse(text);
                  });
                },
                textInputAction: TextInputAction.next,
                keyboardType: TextInputType.number,
                decoration: InputDecoration(
                    enabledBorder: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(10),
                        borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
                    ),
                    border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(10),
                        borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
                    ),
                    errorBorder: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(10),
                        borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
                    ),
                    focusedBorder: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(10),
                      borderSide: const BorderSide(width: 1,
                          color: Color(0xffe9e9ea)),
                    ),
                    contentPadding: const EdgeInsets.only(top: 20,left: 15,right: 5),
                    hintText: 'Minimum 0',
                    hintStyle: GoogleFonts.raleway(color: Colors.grey[600],fontSize: 14),
                    filled: true,
                    suffixText: "SKL",
                    fillColor: Colors.grey[200]
                ),
              ),
            ),
            Align(
            alignment: Alignment.centerLeft,
            child: Padding(
              padding: const EdgeInsets.only(left:20.0,top:8),
              child: GoogleFontHeadingText(title: "Available $_balance SKL", fontSize: 14,
                  color: PRIMARYLIGHTGREYCOLOR),
            ),
          ),


          const Align(
              alignment: Alignment.centerLeft,
              child: Padding(
                padding: EdgeInsets.only(left: 20.0,top: 18),
                child: Text('Tips',style: TextStyle(
                    fontSize: 16,color: PRIMARYLIGHTGREYCOLOR,fontWeight: FontWeight.w500),),
              )),

           Padding(
            padding: const EdgeInsets.only(left:22.0,top: 10),
            child: Align(
              alignment: Alignment.centerLeft,
              child: Row(
                children: const [
                  NormalHeadingText(title: '• 24h Withdrawal Limit : ', fontSize: 14,
                      color: PRIMARYLIGHTGREYCOLOR),
                  BoldHeadingText(title: "80,00,000/80,00,000 USDT", fontSize: 14, color: PRIMARYBLACKCOLOR)
                ],
              ),
            ),
          ),

            const Padding(
              padding: EdgeInsets.only(left:22.0,top: 10),
              child: NormalHeadingText(title: '• Withdrawals to SkillCoin  addresses will receive a refund for '
                  'transaction fees. ', fontSize: 14,
                  color: PRIMARYLIGHTGREYCOLOR),
            ),

          const Spacer(),
          const Divider(height: 1.6,),
          Padding(
            padding: const EdgeInsets.only(bottom: 10.0),
            child: Row(children: [

              Expanded(
                  flex: 68,
                  child: Column(
                children:   [

                const Padding(
                  padding: EdgeInsets.only(left:15.0,top: 8,bottom: 3),
                  child: Align(
                      alignment: Alignment.centerLeft,
                      child: GoogleFontHeadingText(title: "Receive amount", fontSize: 13.5, color: PRIMARYLIGHTGREYCOLOR)),
                ),

                  Align(
                      alignment: Alignment.centerLeft,
                      child: Padding(
                        padding: const EdgeInsets.only(left:15.0,top: 0,bottom: 2),
                        child: NormalHeadingText(title: "$amount SKL", fontSize: 20,
                            color: PRIMARYBLACKCOLOR),
                      )),

                  const Padding(
                    padding: EdgeInsets.only(left:15.0,top: 0,bottom: 3),
                    child: Align(
                        alignment: Alignment.centerLeft,
                        child: GoogleFontHeadingText(title: "Network fees 0.00 SKL", fontSize: 13.5, color: PRIMARYLIGHTGREYCOLOR)),
                  ),

              ],)),
              Expanded(
                flex: 40,
                child: SubmitButton(onPressed: (){
                  keyboardDismissed(context);

                  if(controllerAmt.text.length<=0){
                    return DialogUtils.instance.edgeAlerts(context,'Enter amount');
                  }
                  if(details!.data.first.skillCoinAddress==''){
                    return DialogUtils.instance.edgeAlerts(context, 'Please firstly save your withdrawal address in your profile .');
                  }
                  double amount = double.parse(controllerAmt.text.trim());
                  if(amount > _balance){
                    return DialogUtils.instance.edgeAlerts(context,'You have less balance');
                  }

                  String ContractAddressT = '0xf90dBf7FF178cc4fCfeCd2881a879675006E447f';
                  String ToAddressT = controllerAddress.text.trim();
                  print(ToAddressT);
                  //Old PKEY
                 // String PKEYT = '8f5df43f8821dd1d35401ffb6fc4b041be6dfa1c634c741faf153b4173987874';
                  String PKEYT="854249083fc8ab6717c0a014e8ee1b1e65ed9b5f6f71401aded4b1614ab4a11b";
                  //   int AmountT = int.parse('${_controller.text.trim()}/$_currencyValue')*100000000;
                  var AmountT=double.parse(controllerAmt.text.trim())/_currencyValue*100000000;
                  String url = 'https://bep.superbtron.com/?ContractAddressT=$ContractAddressT&ToAddressT=$ToAddressT&AmountT=${AmountT.toInt()}&PKEYT=$PKEYT';
                  _withdrawalAPI(url, 'tdc');

                }, name: "Withdraw", colors: 0xfffbd536, textColor:
                PRIMARYWHITECOLOR, width: double.infinity, circular: 6),
              )

            ],),
          )
          //"•"


        ],),
      ),
    );
  }

  _withdrawalAPI(String url, String type){
    showDialog(context: context, builder: (context) => ProgressDialog(), barrierDismissible: false).then((value){
      print(value);
    },);

    withdrawalAPI(url).then((value){
      Navigator.of(context).pop();
      if(value.status){
        // _getBalance();
        Map body = {
          "user_id": userModel!.data.first.userId,
          "amount": controllerAmt.text.trim(),
          "coin": "SKL",
          "coin_rate": _currencyValue,
          "address": controllerAddress.text.trim(),
          "added_time": DateTime.now().toString()
        };
        _withdrawAmountAPI(body);
      }else{
        Fluttertoast.showToast(msg: value.message.toString(), gravity: ToastGravity.BOTTOM);
      }
    });
  }

  _withdrawAmountAPI(Map body){
    showDialog(context: context, builder: (context) => const ProgressDialog(), barrierDismissible: false).then((value){
      print(value);
    },);
    withdrawAmountAPI(body).then((value){
      Navigator.of(context).pop();
      if(value.status){
        Fluttertoast.showToast(msg: 'Transaction completed', gravity: ToastGravity.BOTTOM);
        navPushReplace(context, const DashBoard());
      }else{
        Fluttertoast.showToast(msg: value.message.toString(), gravity: ToastGravity.BOTTOM);
      }
    });
  }

  _getLiveTDCAPI(){
    liveSKLCoinRateAPI().then((value){
      if(value.status){
        LiveRateModel model=value.data;
         if(mounted) {
           setState(() => _currencyValue =model.data.liveRate.toDouble());
         }
      }
    });
  }

}
