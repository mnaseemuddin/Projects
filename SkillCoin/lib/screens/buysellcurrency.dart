
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:skillcoin/customui/button.dart';
import 'package:skillcoin/customui/container.dart';
import 'package:skillcoin/customui/textview.dart';
import 'package:skillcoin/res/color.dart';
import 'package:skillcoin/res/constants.dart';
import 'package:skillcoin/res/routes.dart';
import 'package:auto_size_text/auto_size_text.dart';
import 'package:auto_size_text_field/auto_size_text_field.dart';
import 'package:skillcoin/screens/buy.dart';
import '../api/apirepositary.dart';
import '../api/user_data.dart';
import '../customui/progressdialog.dart';
import '../model/liveratemodel.dart';
import '../model/profiledetailsmodel.dart';
import '../model/tdccurrencymodel.dart';
import 'dashboard.dart';

class BuySellCurrencyActivity extends StatefulWidget {
  final String type;
  const BuySellCurrencyActivity({Key? key,required this.type}) : super(key: key);

  @override
  _BuySellCurrencyActivityState createState() => _BuySellCurrencyActivityState();
}

class _BuySellCurrencyActivityState extends State<BuySellCurrencyActivity> {

  var amount="12,20,200.75";
  var amountController=TextEditingController();
  double _currencyValue=0.0;
  double buyCurrencyValue=0.0,totalDollor=0.0;
  var buySell="Buy Crypto";
  var tvBuySell="Buy SKL";
  ProfileDetailsModel? details;
  double _balance=0.0;

  @override
  void initState() {
    super.initState();
    setState(() {
      buyCurrencyValue=0.0;
      totalDollor=0.0;
      if(widget.type=="Buy Crypto") {
        tvBuySell="Buy SKL";
        buySell = widget.type;
      }else{
        tvBuySell="Sell SKL";
        buySell=widget.type;
      }
    });
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
        });
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


  @override
  Widget build(BuildContext context) {
    return SafeArea(
      top: true,
      child: Scaffold(
        backgroundColor: PRIMARYWHITECOLOR,
       body: Column(children: [
         ContainerBgRCircle(width: double.infinity, height: 50, color: PRIMARYWHITECOLOR,
             circular: 0, child: Row(children: [

               IconButton(onPressed: (){
                 navPushOnBackPressed(context);
               }, icon: const Icon(Icons.arrow_back_ios_outlined)),
               const Spacer(),
               NormalHeadingText(title: buySell,fontSize: 17,color: PRIMARYBLACKCOLOR,),
               const Spacer(),
                Padding(
                 padding: const EdgeInsets.only(right:8.0),
                 child: GestureDetector(
                   onTap: (){
                     setState(() {
                       amountController.clear();
                       buyCurrencyValue=0.0;
                       totalDollor=0.0;
                       if(buySell=="Buy Crypto") {
                         tvBuySell="Sell SKL";
                         buySell = "Sell Crypto";
                       }else{
                         tvBuySell="Buy SKL";
                         buySell="Buy Crypto";
                       }
                     });
                   },
                   child: const ContainerBgRCircle(width: 45, height: 26, color: Color(0xffebecee), circular:
                   40, child: Center(child: NormalHeadingText(title: "Sell",fontSize: 14,color: PRIMARYBLACKCOLOR,))),
                 ),
               )

             ],)),
         
         
          Padding(
            padding: const EdgeInsets.only(top:18.0),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [

              ClipRRect(
                  borderRadius: BorderRadius.circular(40),
                  child: Image.asset('assets/skl.png',height: 22,width: 22,)),
                const SizedBox(width: 10,),
                Text( "1 SKL =  \$$_currencyValue",
                    style:const TextStyle(fontSize: 16,fontWeight: FontWeight.w500, color: Color(0xff797575)),)
            ],),
          ),
         const SizedBox(height: 100,),


         Align(
             alignment: Alignment.centerRight,
             child: Column(
               children: [
                 RotatedBox(
                     quarterTurns: 1,
                     child: Image.asset(imgTwoArrows,height: 30,width: 16,color: Colors.white,)),
               /*   NormalHeadingText(title: buySell=="Buy Crypto"?"SKL":"USDT",
                      fontSize: 12.5, color: PRIMARYLIGHTGREYCOLOR)*/
               ],
             )),

         Row(
           mainAxisAlignment: MainAxisAlignment.center,
           children: [
             AutoSizeTextField(
               minWidth: 50,
               stepGranularity: 5,
               fullwidth: false,
               maxFontSize: 30,
               minFontSize: 5,
               onChanged: (text){
                 setState(() {
                   if(buySell=="Buy Crypto") {
                     buyCurrencyValue = double.parse(text) / _currencyValue;
                     totalDollor = double.parse(text);
                   }else{
                     buyCurrencyValue = double.parse(text)*_currencyValue;
                     totalDollor = double.parse(text);
                   }
                 });
               },
               textAlignVertical: TextAlignVertical.center,
               textAlign: TextAlign.center,
               keyboardType: TextInputType.number,
               inputFormatters: [
                 LengthLimitingTextInputFormatter(11),
               ],
               showCursor: false,
               style: const TextStyle(fontSize: 35,fontWeight: FontWeight.bold,
                   color: PRIMARYBLACKCOLOR),
               controller: amountController,
               maxLines: 1,
               decoration:  InputDecoration(
                 suffixText: buySell=="Buy Crypto"?"USDT":"SKL",
                 suffixStyle: const TextStyle(fontSize: 15,fontWeight: FontWeight.bold),
                 isDense: true,
                   focusedBorder: InputBorder.none,
                   enabledBorder: InputBorder.none,
                   errorBorder: InputBorder.none,
                   disabledBorder: InputBorder.none,
                 border: InputBorder.none,
                 hintText: '0',
                 hintStyle: GoogleFonts.aBeeZee(fontSize: 35,fontWeight: FontWeight.bold,
                     color: PRIMARYBLACKCOLOR)
               ),
             ),

           ],
         ),
         Row(
           mainAxisAlignment: MainAxisAlignment.center,
           children: [
             const SizedBox(width: 10,),
             buySell=="Buy Crypto"?NormalHeadingText(title: "$totalDollor USDT =  $buyCurrencyValue SKL",
                 fontSize: 16, color: PRIMARYBLACKCOLOR):NormalHeadingText(title: "$totalDollor SKL =  $buyCurrencyValue USDT",
                 fontSize: 16, color: PRIMARYBLACKCOLOR),
           ],),
         const Spacer(),
         Padding(
           padding: const EdgeInsets.all(8.0),
           child: SubmitButton(onPressed: (){
             keyboardDismissed(context);
             if(buySell=="Buy Crypto"){
               if(amountController.text.length<=0){
                 return DialogUtils.instance.edgeAlerts(context,'Enter amount');
               }
               navPush(context, const BuyActivity());
             }else{
               if(amountController.text.length<=0){
                 return DialogUtils.instance.edgeAlerts(context,'Enter amount');
               }
               if(details!.data.first.tronAddress==''){
                 return DialogUtils.instance.edgeAlerts(context, 'Please firstly save your sell address in your profile .');
               }
               double amount = double.parse(amountController.text.trim());
               if(amount > _balance){
                 return DialogUtils.instance.edgeAlerts(context,'You have less balance');
               }

               String ContractAddressT = 'TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t';
               String ToAddressT = details!.data.first.tronAddress;
               //Old PKEY
               // String PKEYT = '8f5df43f8821dd1d35401ffb6fc4b041be6dfa1c634c741faf153b4173987874';
               String PKEYT="f7dbdc0a110705e6c304aa9a8147aba33a6c792494b54e4f396c1a6591cbf461";
               var AmountT=double.parse(amountController.text.trim())/_currencyValue*1000000;
               String url = 'https://tron.superbtron.com/?ContractAddressT=$ContractAddressT&ToAddressT=$ToAddressT&AmountT=${AmountT.toInt()}&PKEYT=$PKEYT';
               _sellSKLCoinAPI(url, 'tdc');
             }

           }, name: tvBuySell,
               textColor: PRIMARYBLACKCOLOR, width: double.infinity, circular: 15,colors: 0xfffbd536,
           ),
         )
       ],),
      ),
    );
  }
  _sellSKLCoinAPI(String url, String type){
    showDialog(context: context, builder: (context) => ProgressDialog(), barrierDismissible: false).then((value){
      print(value);
    },);

    sellSKLCoinAPI(url).then((value){
      Navigator.of(context).pop();
      if(value.status){
        Map data=value.data;
        // _getBalance();
        Map body = {
          "user_id": userModel!.data.first.userId,
          "amount": amountController.text.trim(),
          "coin": "SKL",
          "coin_rate": _currencyValue,
          "address": details!.data.first.tronAddress,
          "added_time": DateTime.now().toString(),
          "payment_hash":data["hash"]
        };
        _sellCoinAmountAPI(body);
      }else{
        Fluttertoast.showToast(msg: 'Something went wrong',gravity: ToastGravity.CENTER);
      }
    });
  }
  _sellCoinAmountAPI(Map body){
    showDialog(context: context, builder: (context) => const ProgressDialog(), barrierDismissible: false).then((value){
      print(value);
    },);
    sellCoinAmountAPI(body).then((value){
      Navigator.of(context).pop();
      if(value.status){
        Fluttertoast.showToast(msg: 'Transaction completed', gravity: ToastGravity.BOTTOM);
        navPushReplace(context, const DashBoard());
      }else{
        Fluttertoast.showToast(msg: value.message.toString(), gravity: ToastGravity.BOTTOM);
      }
    });
  }
}
