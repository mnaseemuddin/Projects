
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:skillcoin/api/apirepositary.dart';
import 'package:skillcoin/api/user_data.dart';
import 'package:skillcoin/customui/button.dart';
import 'package:skillcoin/customui/container.dart';
import 'package:skillcoin/customui/textview.dart';
import 'package:skillcoin/res/color.dart';
import 'package:skillcoin/res/constants.dart';
import 'package:skillcoin/res/routes.dart';
import 'package:skillcoin/screens/dashboard.dart';
import 'package:share/share.dart';
import 'package:qr_flutter/qr_flutter.dart';

import '../model/hashkeymodel.dart';
import '../model/liveratemodel.dart';
import '../model/tdccurrencymodel.dart';

class DepositCurrency extends StatefulWidget {
  const DepositCurrency({Key? key}) : super(key: key);

  @override
  _DepositCurrencyState createState() => _DepositCurrencyState();
}

class _DepositCurrencyState extends State<DepositCurrency> {


 final String _address="0xdfa32AbaD42F7c323Ccdc835b7c32EEA7BCA5Ad8";

  var controllerPayHash=TextEditingController();
  double _currencyValue=0.0;

  @override
  void initState() {
    _getLiveTDCAPI();
    super.initState();
  }


 _getLiveTDCAPI(){
   liveSKLCoinRateAPI().then((value){
     if(value.status){
       LiveRateModel model=value.data;
       if(mounted) {
         setState(() => _currencyValue =model.data.liveRate.toDouble());
       }
     }else{
       Fluttertoast.showToast(msg: value.message.toString());
     }
   });
 }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      top: true,
      child: Scaffold(
        backgroundColor: PRIMARYWHITECOLOR,
        body: ListView(children: [

          Align(
            alignment: Alignment.centerLeft,
            child: IconButton(onPressed: (){
              navPushOnBackPressed(context);
            }, icon: const Icon(Icons.arrow_back_ios_outlined,size: 20,color:PRIMARYLIGHTGREYCOLOR,)),
          ),

          const Padding(
            padding: EdgeInsets.only(left: 14.0,top: 10),
            child: Align(
                alignment: Alignment.centerLeft,
                child: Text("Deposit SKL",style: TextStyle(fontSize: 22, color: PRIMARYBLACKCOLOR,
                    fontWeight: FontWeight.w800),)),
          ),

           const Padding(
            padding: EdgeInsets.fromLTRB(15.0,8,15,0),
            child: LinearProgressIndicator(backgroundColor: PRIMARYBLACKCOLOR,color: PRIMARYWHITECOLOR,),
          ),

          const Padding(
            padding: EdgeInsets.only(top:15.0),
            child: Center(child: Text('Scan or copy the below address to PAY', style: TextStyle(color: PRIMARYBLACKCOLOR),)),
          ),

          Padding(
            padding: const EdgeInsets.only(left: 8.0,top: 15),
            child: Center(
              child: QrImage(
                data: _address,
                version: QrVersions.auto,
                size: 200.0,
                embeddedImage: AssetImage('assets/skl.png'),
              ),
            ),
          ),


          const Align(
            alignment: Alignment.centerLeft,
              child: Padding(
                padding: EdgeInsets.only(left: 14.0,top: 30),
                child: NormalHeadingText(title: "SKL Deposit Address", fontSize: 15, color: PRIMARYLIGHTGREYCOLOR),
              )),

           Align(
              alignment: Alignment.centerLeft,
              child: Padding(
                padding: EdgeInsets.only(left: 14.0,top: 4),
                child: Row(
                  children:  [
                     Expanded(
                      flex: 80,
                      child: NormalHeadingText(title: _address,
                          fontSize: 16, color: PRIMARYBLACKCOLOR),
                    ),

                    Expanded(
                        flex: 15,
                        child: IconButton(onPressed: (){
                          Clipboard.setData(ClipboardData(
                              text: _address
                          ));
                          Fluttertoast.showToast(msg: 'Address copied');
                        }, icon: Icon(Icons.copy)))
                  ],
                ),
              )),



          Padding(
            padding: const EdgeInsets.fromLTRB(12.0,30,12,0),
            child: ContainerBgRCircle(width: double.infinity, height: 170, color: PRIMARYLIGHTGREY1COLOR,
                circular: 10, child: Column(
                  children: [

                    Row(children:  const [

                      Padding(
                        padding: EdgeInsets.only(left:18.0,top: 8),
                        child: NormalHeadingText(title: "Minimum deposit", fontSize: 14, color: PRIMARYLIGHTGREYCOLOR),
                      ),
                      Spacer(),
                      Padding(
                        padding: EdgeInsets.only(left:15.0,right: 15,top: 8),
                        child: NormalHeadingText(title: "0.00000001 SKL", fontSize: 14,
                            color: PRIMARYBLACKCOLOR),
                      )

                    ],),

                    Padding(
                      padding: const EdgeInsets.only(top:10.0,bottom: 8,left: 15,right: 15),
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
                            borderSide: const BorderSide(
                              color: Color(0xffebecee),
                              width: 0.70,
                            ),
                          ),
                          contentPadding: const EdgeInsets.only(left:10),
                          errorStyle: GoogleFonts.aBeeZee(fontSize: 14.5,color:
                          Colors.redAccent,fontWeight: FontWeight.w600),
                          focusColor: const Color(0xffebecee),
                          border: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(5.0),
                              borderSide: BorderSide(color: Color(0xffafaeae))
                          ),
                          filled: true,
                          fillColor: const Color(0xffffffff),
                          focusedBorder: OutlineInputBorder(
                            borderSide: BorderSide(color: Color(0xffafaeae)),
                            borderRadius: BorderRadius.circular(9.0),
                          ),
                          hintText: "Enter Payment Hash",
                          hintStyle: GoogleFonts.aBeeZee(
                              fontSize: 14, color: Colors.grey[800]),
                        ),
                      ),
                    ),

                    SubmitButton(onPressed: (){

                      if(controllerPayHash.text.isNotEmpty){
                        keyboardDismissed(context);
                        hashKeyAPI().then((value){
                          if(value.status){
                            setState(() {
                              HashKeyModel model=value.data;
                              for(int i=0;i<model.result.length;i++){
                                if(controllerPayHash.text==model.result[i].hash){
                                  int value=int.parse(model.result[i].value);
                                  var actuallyPaid=value/100000000;
                                  _depositAmountInWalletAPI(userModel!.data.first.userId.toString(),
                                      actuallyPaid.toString(),
                                      '',model.result[i].blockNumber,"SKL",'',
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
                        DialogUtils.instance.edgeAlerts(context, "Please Enter Your PaymentHash");
                      }

                    }, name: "Processed to payment", colors: 0xfffbd536,
                        textColor: PRIMARYBLACKCOLOR, width: double.infinity, circular: 15),

                  ],
                )),
          ),

          const Spacer(),
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: Row(children: [
              Expanded(
                child: SubmitButton(onPressed: (){}, name: "Save as Image", colors: 0xffebecee,
                    textColor: PRIMARYBLACKCOLOR, width: double.infinity, circular: 15),
              ),

              Expanded(
                child: SubmitButton(onPressed: (){
                  Share.share(_address);
                }, name: "Share Address", colors: 0xfffbd536,
                    textColor: PRIMARYBLACKCOLOR, width: double.infinity, circular: 15),
              )

            ],),
          )

        ],),
      ),
    );
  }


 void _depositAmountInWalletAPI(String userId, String amount, String fiatAmount,
     String txnId, String coin, String confirms, String address,String payStatus,
     String currencyRate,String actullyPaid, String hash) async{
   Map body = {
     'user_id':userId,'amount':amount,'fiat_amount': "0",
     'txn_id':txnId,'coin':coin,
     'confirms':"1",'address':address,
     'payment_status':payStatus,'pay_amount':amount,
     'actually_paid':actullyPaid,'live_rate':currencyRate,'payment_hash':hash
   };
   print(body);
   depositAmountInWalletAPI(body).then((value){
     if(value.status){
       Navigator.of(context).pop();
       Fluttertoast.showToast(msg: 'Transaction completed',
           gravity: ToastGravity.BOTTOM);
       navPush(context, const DashBoard());
     }else{
       controllerPayHash.clear();
       Navigator.of(context).pop();
       Fluttertoast.showToast(msg: value.message.toString(),
           gravity: ToastGravity.BOTTOM);
     }
   });
 }
}
