
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:skillcoin/model/buyskillcoinmodel.dart';
import 'package:qr_flutter/qr_flutter.dart';
import 'package:share/share.dart';

import '../api/apirepositary.dart';
import '../api/user_data.dart';
import '../customui/button.dart';
import '../customui/container.dart';
import '../customui/textview.dart';
import '../model/hashkeymodel.dart';
import '../res/color.dart';
import '../res/routes.dart';
import 'dashboard.dart';

class BuyActivity extends StatefulWidget {
  const BuyActivity({Key? key}) : super(key: key);

  @override
  _BuyActivityState createState() => _BuyActivityState();
}

class _BuyActivityState extends State<BuyActivity> {
  var controllerPayHash=TextEditingController();
  String _address="TDZGjSSik5UfqY9xuVQqbKvyRD7ZR1Ry6r";
  double _currencyValue=0.0;

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
                child: Text("Buy SKL",style: TextStyle(fontSize: 22, color: PRIMARYBLACKCOLOR,
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
                embeddedImage: const AssetImage('assets/skl.png'),
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
                padding: const EdgeInsets.only(left: 14.0,top: 4),
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
                          Fluttertoast.showToast(msg: 'Address copied',
                              backgroundColor: PRIMARYBLACKCOLOR.withOpacity(0.5));
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
                      keyboardDismissed(context);
                      if(controllerPayHash.text.isNotEmpty){
                        keyboardDismissed(context);
                        buySkillCoinAPI(controllerPayHash.text).then((value){
                          if(value.status){
                            setState(() {
                              BuySkillCoinModel model=value.data;
                              for(int i=0;i<model.trc20TransferInfo.length;i++){
                                if(controllerPayHash.text==model.hash){
                                  int value=int.parse(model.trc20TransferInfo[i].amountStr);
                                  var actuallyPaid=value/1000000;
                                  _buyCoinAmountAPI(userModel!.data.first.userId.toString(),
                                      actuallyPaid.toString(),
                                      '',model.block.toString(),"SKL",'',
                                      model.toAddress,'finished',_currencyValue.toString(),
                                      actuallyPaid.toString(),model.hash);
                                }
                              }
                            });
                          }else{
                            DialogUtils.instance.edgeAlerts(context,value.message.toString());
                          }
                        });
                      }else{
                        DialogUtils.instance.edgeAlerts(context, "Please enter your payment hash");
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
  void _buyCoinAmountAPI(String userId, String amount, String fiatAmount,
      String txnId, String coin, String confirms, String address,String payStatus,
      String currencyRate,String actullyPaid, String hash) async{
    Map body = {
      'user_id':userId,'amount':amount,'fiat_amount': "0",
      'txn_id':txnId,'coin':coin,
      'confirms':"1",'address':address,
      'payment_status':payStatus,'pay_amount':amount,
      'actually_paid':actullyPaid,'live_rate':currencyRate,'payment_hash':hash
    };
    buySKLAmountInWalletAPI(body).then((value){
      if(value.status){
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
