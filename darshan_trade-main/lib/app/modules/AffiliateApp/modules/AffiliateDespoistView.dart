
import 'dart:convert';
import 'dart:math';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:get_storage/src/storage_impl.dart';
import 'package:qr_flutter/qr_flutter.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/modules/AffiliateApp/model/PaymentHashResposne.dart';
import 'package:royal_q/app/modules/AffiliateApp/model/StakeResponse.dart';
import 'package:royal_q/app/modules/AffiliateApp/modules/AffilateDashBoardView.dart';
import 'package:royal_q/app/modules/TradeSettings/views/samargin_textfield.dart';
import 'package:royal_q/app/shared/sawidgets/controllers/SAETextField.dart';
import 'package:royal_q/app/shared/sawidgets/sa_appbar.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:royal_q/app/shared/widgets/affliate_bg.dart';
import 'package:royal_q/app/shared/widgets/app_textbox.dart';
import 'package:share/share.dart';
import 'package:http/http.dart' as http;
import '../../dashboard/tabs/mine/items/asset/deposit_currency.dart';
import '../model/DepositTransactionHistoryResponse.dart';
import 'package:collection/collection.dart';

class AffDepositView extends StatefulWidget {
  const AffDepositView({Key? key}) : super(key: key);

  @override
  _AffDepositViewState createState() => _AffDepositViewState();
}

class _AffDepositViewState extends State<AffDepositView> {
  var controllerAmount=TextEditingController();
  // var selectedStakeValue;
  StakeResponse? stakeModel;

  String? token;
  GetStorage storage=GetStorage();

  var qrCodeUIVisibility=false;
  var depositUIVisibility=true;

  var editcontrollerHash=TextEditingController();

  // int tblStakePeriodId=0;

  var tblUserId,bToken;

  DepositTransationResponse? depositModel;

  double walletAmt=0.00;

  String _selectedLabel = 'Select stack period';

  StackData? _stackData;

  @override
  void initState() {
getUser().then((value){
  setState(() {
    tblUserId=value!.data.first.tblUserId.toString();
    bToken=value!.data.first.jwtToken;
  });
  stakeListAPI(value!.data.first.jwtToken).then((value){
    if(value.status){
      setState(() {
        stakeModel=value.data;
      });
    }else{
      toastMessage(value.message.toString());
    }
  });
  getUser().then((value){
    getTransationHistoryAPI(value!.data.first.tblUserId.toString(),
        value!.data.first.jwtToken.toString()).then((value){
      if(value.status){
        setState(() {
          depositModel=value.data;
          walletAmt=double.parse(depositModel!.totalRoiIncome);
        });
      }else{
        EasyLoading.showToast(value.message.toString(),
            toastPosition:EasyLoadingToastPosition.bottom);
      }
    });
  });
});

    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return AFBg(child: Scaffold(
      backgroundColor: Colors.transparent,
        resizeToAvoidBottomInset: false,
        // appBar: AppBar(title: Text('Deposit'.tr, style: textStyleHeading2(color: ColorConstants.white),),
        //   iconTheme: IconThemeData(color: ColorConstants.white),
        //   elevation: 0,
        //   // brightness: Brightness.dark,
        //   systemOverlayStyle: SystemUiOverlayStyle(
        //     systemNavigationBarColor: ColorConstants.APP_PRIMARY_COLOR, // Navigation bar
        //     statusBarColor: ColorConstants.APP_PRIMARY_COLOR, // Status bar
        //   ),
        //   backgroundColor: Colors.transparent,
        //   actions: [
        //     // IconButton(onPressed: (){
        //     //   setState(() => _isVisible = !_isVisible);
        //     // }, icon: Icon(AntDesign.filter))
        //   ],
        // ),
        body: SafeArea(child: Column(children: [
          SHAppbar(title: 'DEPOSIT'),
          Expanded(child: depositUIVisibility?Visibility(
            visible: depositUIVisibility,

            child: ListView(children: [
              SizedBox(height: 16,),
              AFTextBox(label: 'Amount', hint: 'Enter Amount (USDT)', isNumber: true, textEditingController: controllerAmount,),

              if(stakeModel!=null)
              AFDropBox(label: 'Select Stake Period', hint: _selectedLabel, stakeModel: stakeModel!, onTap: (){
                Get.to(() => DropDownSelect(title: 'Stack Period', list: stakeModel!.data.mapIndexed((index, element) => DropItem(index: index,
                    title: '${element.numberOfMonth} Month', )).toList(), onSelect: (val){
                  setState(() {
                    _stackData = stakeModel!.data.elementAt(val);
                    _selectedLabel = '${_stackData?.numberOfMonth??3} Month';
                  });
                },));
              }),

                SizedBox(height: 24,),

              Padding(padding: EdgeInsets.symmetric(horizontal: 24,),
              child: AFSubmitButton(onPressed: (){
                if(validation()){
                  qrCode();
                }
              }, title: 'Pay Now'),),

              /**
              SizedBox(height: 800,
              child: Column(children: [
                Expanded(
                    flex: 28,
                    child: Column(children: [

                      Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: Row(
                          children: [
                            Expanded(
                                flex:30,
                                child: Text("Amount (\$)",style: TextStyle(fontWeight: FontWeight.w500),)),
                            Expanded(
                              flex: 70,
                              child: Container(
                                padding: EdgeInsets.only(left: 8),
                                child: TextField(
                                  controller: controllerAmount,
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


                                  decoration: InputDecoration(
                                    contentPadding: EdgeInsets.only(left: 10),
                                    border: InputBorder.none,
                                    hintText: 'Enter Amount (USDT)'.tr,
                                    hintStyle: TextStyle(color: ColorConstants.white30,fontSize: 15),
                                  ),
                                ),
                                decoration: BoxDecoration(
                                    borderRadius: BorderRadius.circular(8),
                                    border: Border.all(color:Color(0xf020202), width: 1)
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),
                      SizedBox(height: 15,),
                      Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: Row(
                          children: [
                            Expanded(
                                flex: 30,
                                child: Text("Select Stake Period",style: TextStyle(fontWeight: FontWeight.w500),)),
                            Expanded(
                              flex: 70,
                              child: SizedBox(
                                height: 40,
                                child: DropdownButtonFormField(
                                  style: TextStyle(
                                      fontSize: 14.5,
                                      color: Colors.black),
                                  dropdownColor: Colors.white,
                                  decoration: InputDecoration(
                                    contentPadding: const EdgeInsets.only(bottom: 10, left: 15),
                                    filled: true,
                                    fillColor: Colors.white30,
                                    hintStyle: TextStyle(
                                        color: ColorConstants.grey,
                                        fontSize: 13.5),
                                    hintText: "Select Stake Period",
                                    enabledBorder: OutlineInputBorder(
                                        borderRadius: BorderRadius.circular(8),
                                        borderSide:
                                        BorderSide(
                                            width: 1.0,
                                            color: Color(0xf020202))),
                                    border: OutlineInputBorder(
                                        borderRadius: BorderRadius.circular(8),
                                        borderSide:
                                        BorderSide(
                                            width: 1.0,
                                            color: Color(0xf020202))),
                                    focusedBorder: OutlineInputBorder(
                                      borderRadius: BorderRadius.circular(8),
                                      borderSide:
                                      BorderSide(
                                          width: 1.0,
                                          color: Color(0xf020202)),
                                    ),
                                  ),
                                  value: selectedStakeValue,
                                  onChanged: (value){
                                    setState(() {
                                      selectedStakeValue=value;
                                      for(var element in stakeModel!.data){
                                        if(element.packageName==selectedStakeValue){
                                          tblStakePeriodId= element.tblStakePeriodId;
                                        }
                                      }


                                    });
                                  },
                                  items: stakeModel?.data.map((e) => (DropdownMenuItem(
                                    value: e.packageName,
                                    child: Text(e.packageName),
                                  )))
                                      .toList(),),
                              ),
                            )
                          ],
                        ),
                      ),
                      SizedBox(height: 15,),
                      Padding(
                        padding: const EdgeInsets.only(left:28.0,right:28.0),
                        child: SubmitButton(onPressed: (){
                          if(validation()){
                            qrCode();
                          }

                        }, title: "Pay Now"),
                      ),
                    ],)),
                Expanded(
                  flex: 72,
                  child: Stack(children: [
                    depositModel==null?NoRecord():Container(
                      height: double.infinity,
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.only(topLeft: Radius.circular(22),
                            topRight: Radius.circular(22)),
                        color: Colors.white,
                      ),
                      child:Padding(
                        padding: const EdgeInsets.only(top:30.0,left: 12,right: 12),
                        child: SingleChildScrollView(
                          scrollDirection: Axis.vertical,
                          child: Column(
                              mainAxisSize: MainAxisSize.min,
                              children: depositModel!.data.map((e) => Column(children: [
                                Padding(
                                  padding: const EdgeInsets.only(top:10.0),
                                  child: GestureDetector(
                                    onTap: (){

                                    },
                                    child: Container(
                                      height: 101,
                                      color: Colors.white,
                                      child: Row(children: [
                                        Expanded(
                                          flex:10,
                                          child: Padding(
                                            padding: const EdgeInsets.only(left:8.0),
                                            child: Image.asset('assets/expgain/transaction.png',height: 24,width: 24,
                                              color: Color(0xff2d2d2d),),
                                          ),
                                        ),
                                        Expanded(
                                          flex: 50,
                                          child: Column(
                                            children: [
                                              Align(
                                                alignment: Alignment.centerLeft,
                                                child: Padding(
                                                  padding: const EdgeInsets.fromLTRB(8.0,20,0,8),
                                                  child: Text("Buy USDT".toString(),style: TextStyle(
                                                      fontSize: 16, color: Colors.black
                                                  ),),
                                                ),
                                              ),

                                              Align(
                                                alignment: Alignment.centerLeft,
                                                child: Padding(
                                                  padding: const EdgeInsets.fromLTRB(8.0,0,0,8),
                                                  child: Text(e.toaddres,maxLines: 1,style: const TextStyle(
                                                      fontSize: 14,
                                                      color: Color(0xffafaeae)
                                                  ),),
                                                ),
                                              ),
                                              Align(
                                                alignment: Alignment.centerLeft,
                                                child: Padding(
                                                  padding: const EdgeInsets.fromLTRB(8.0,0,0,8),
                                                  child: Text(e.createdDate,textAlign:
                                                  TextAlign.right,style: const TextStyle(
                                                      fontSize: 14,
                                                      color: Color(0xffafaeae)
                                                  ),),
                                                ),
                                              ),

                                            ],),
                                        ),
                                        Expanded(
                                            flex: 40,
                                            child: Padding(
                                              padding: const EdgeInsets.only(right:8.0),
                                              child: Text("+ ${e.amount} USDT",textAlign:
                                              TextAlign.right,style: TextStyle(
                                                  fontSize: 16,
                                                  color: Colors.green,fontWeight: FontWeight.bold
                                              ),),
                                            ))

                                      ],),
                                    ),
                                  ),
                                ),
                                Divider(height: 1,),
                              ],)).toList()),
                        ),
                      ),
                    ),
                    Padding(
                      padding: const EdgeInsets.all(20.0),
                      child: Text("Deposit History",style: TextStyle(fontSize: 18,fontWeight: FontWeight.w600),),
                    )

                  ],),
                )
              ],),)
**/
            ],),

            /**
            child: Column(children: [
              Expanded(
                  flex: 28,
                  child: Column(children: [

                    Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: Row(
                        children: [
                          Expanded(
                              flex:30,
                              child: Text("Amount (\$)",style: TextStyle(fontWeight: FontWeight.w500),)),
                          Expanded(
                            flex: 70,
                            child: Container(
                              padding: EdgeInsets.only(left: 8),
                              child: TextField(
                                controller: controllerAmount,
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


                                decoration: InputDecoration(
                                  contentPadding: EdgeInsets.only(left: 10),
                                  border: InputBorder.none,
                                  hintText: 'Enter Amount (USDT)'.tr,
                                  hintStyle: TextStyle(color: ColorConstants.white30,fontSize: 15),
                                ),
                              ),
                              decoration: BoxDecoration(
                                  borderRadius: BorderRadius.circular(8),
                                  border: Border.all(color:Color(0xf020202), width: 1)
                              ),
                            ),
                          ),
                        ],
                      ),
                    ),
                    SizedBox(height: 15,),
                    Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: Row(
                        children: [
                          Expanded(
                              flex: 30,
                              child: Text("Select Stake Period",style: TextStyle(fontWeight: FontWeight.w500),)),
                          Expanded(
                            flex: 70,
                            child: SizedBox(
                              height: 40,
                              child: DropdownButtonFormField(
                                style: TextStyle(
                                    fontSize: 14.5,
                                    color: Colors.black),
                                dropdownColor: Colors.white,
                                decoration: InputDecoration(
                                  contentPadding: const EdgeInsets.only(bottom: 10, left: 15),
                                  filled: true,
                                  fillColor: Colors.white30,
                                  hintStyle: TextStyle(
                                      color: ColorConstants.grey,
                                      fontSize: 13.5),
                                  hintText: "Select Stake Period",
                                  enabledBorder: OutlineInputBorder(
                                      borderRadius: BorderRadius.circular(8),
                                      borderSide:
                                      BorderSide(
                                          width: 1.0,
                                          color: Color(0xf020202))),
                                  border: OutlineInputBorder(
                                      borderRadius: BorderRadius.circular(8),
                                      borderSide:
                                      BorderSide(
                                          width: 1.0,
                                          color: Color(0xf020202))),
                                  focusedBorder: OutlineInputBorder(
                                    borderRadius: BorderRadius.circular(8),
                                    borderSide:
                                    BorderSide(
                                        width: 1.0,
                                        color: Color(0xf020202)),
                                  ),
                                ),
                                value: selectedStakeValue,
                                onChanged: (value){
                                  setState(() {
                                    selectedStakeValue=value;
                                    for(var element in stakeModel!.data){
                                      if(element.packageName==selectedStakeValue){
                                        tblStakePeriodId= element.tblStakePeriodId;
                                      }
                                    }


                                  });
                                },
                                items: stakeModel?.data.map((e) => (DropdownMenuItem(
                                  value: e.packageName,
                                  child: Text(e.packageName),
                                )))
                                    .toList(),),
                            ),
                          )
                        ],
                      ),
                    ),
                    SizedBox(height: 15,),
                    Padding(
                      padding: const EdgeInsets.only(left:28.0,right:28.0),
                      child: SubmitButton(onPressed: (){
                        if(validation()){
                          qrCode();
                        }

                      }, title: "Pay Now"),
                    ),
                  ],)),
              Expanded(
                flex: 72,
                child: Stack(children: [
                  depositModel==null?NoRecord():Container(
                    height: double.infinity,
                    decoration: BoxDecoration(
                      borderRadius: BorderRadius.only(topLeft: Radius.circular(22),
                          topRight: Radius.circular(22)),
                      color: Colors.white,
                    ),
                    child:Padding(
                      padding: const EdgeInsets.only(top:30.0,left: 12,right: 12),
                      child: SingleChildScrollView(
                        scrollDirection: Axis.vertical,
                        child: Column(
                            mainAxisSize: MainAxisSize.min,
                            children: depositModel!.data.map((e) => Column(children: [
                              Padding(
                                padding: const EdgeInsets.only(top:10.0),
                                child: GestureDetector(
                                  onTap: (){

                                  },
                                  child: Container(
                                    height: 101,
                                    color: Colors.white,
                                    child: Row(children: [
                                      Expanded(
                                        flex:10,
                                        child: Padding(
                                          padding: const EdgeInsets.only(left:8.0),
                                          child: Image.asset('assets/expgain/transaction.png',height: 24,width: 24,
                                            color: Color(0xff2d2d2d),),
                                        ),
                                      ),
                                      Expanded(
                                        flex: 50,
                                        child: Column(
                                          children: [
                                            Align(
                                              alignment: Alignment.centerLeft,
                                              child: Padding(
                                                padding: const EdgeInsets.fromLTRB(8.0,20,0,8),
                                                child: Text("Buy USDT".toString(),style: TextStyle(
                                                    fontSize: 16, color: Colors.black
                                                ),),
                                              ),
                                            ),

                                            Align(
                                              alignment: Alignment.centerLeft,
                                              child: Padding(
                                                padding: const EdgeInsets.fromLTRB(8.0,0,0,8),
                                                child: Text(e.toaddres,maxLines: 1,style: const TextStyle(
                                                    fontSize: 14,
                                                    color: Color(0xffafaeae)
                                                ),),
                                              ),
                                            ),
                                            Align(
                                              alignment: Alignment.centerLeft,
                                              child: Padding(
                                                padding: const EdgeInsets.fromLTRB(8.0,0,0,8),
                                                child: Text(e.createdDate,textAlign:
                                                TextAlign.right,style: const TextStyle(
                                                    fontSize: 14,
                                                    color: Color(0xffafaeae)
                                                ),),
                                              ),
                                            ),

                                          ],),
                                      ),
                                      Expanded(
                                          flex: 40,
                                          child: Padding(
                                            padding: const EdgeInsets.only(right:8.0),
                                            child: Text("+ ${e.amount} USDT",textAlign:
                                            TextAlign.right,style: TextStyle(
                                                fontSize: 16,
                                                color: Colors.green,fontWeight: FontWeight.bold
                                            ),),
                                          ))

                                    ],),
                                  ),
                                ),
                              ),
                              Divider(height: 1,),
                            ],)).toList()),
                      ),
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.all(20.0),
                    child: Text("Deposit History",style: TextStyle(fontSize: 18,fontWeight: FontWeight.w600),),
                  )

                ],),
              )
            ],),
                **/
          ):
          Visibility(child: _qrCodeView(),visible: qrCodeUIVisibility,))

        ],))

    ));
  }

  Widget _confirmTransaction(){
    return Container(
      padding: EdgeInsets.symmetric(horizontal: 16),
      child: Column(children: [
        Text('Have_you_paid'.tr, style: textStyleHeading2(color: Colors.white),),
        SizedBox(height: 8,),
        Text('Confirm_transaction'.tr, style: textStyleLabel(color: Colors.white),),
        SizedBox(height: 16,),

        AFTextBox(label: 'Hash_Key'.tr, hint: 'Enter_Hash_key'.tr,
        textEditingController: editcontrollerHash,),

        // TextFieldIcon(
        //   controller: editcontrollerHash,
        //   hintText: 'Enter_Hash_key'.tr, onChanged: (val){}, labelText: 'Hash_Key'.tr, ),


        SizedBox(height: 16,),
        Padding(padding: EdgeInsets.symmetric(horizontal: 16),
        child: AFSubmitButton(
          onPressed: () {

            if(controllerAmount.text.trim().isEmpty) {
              EasyLoading.showToast('Enter_amount'.tr);
              return;
            }

            if(editcontrollerHash.text.isEmpty) {
              EasyLoading.showToast('Enter_Hash_key'.tr);
              return;
            }

            EasyLoading.show();
            checkPaymentHashKeyAPI(editcontrollerHash.text.trim()).then((value){
              if(value.status){
                int transationId=value.data["block"];
                String amount=value.data['trigger_info']['parameter']['_value'];
                String toAddress=value.data['trigger_info']['parameter']['_to'];
                double amount2=int.parse(amount)/1000000;
                var hashMap={
                  "user_id":tblUserId.toString(),
                  "tbl_stake_period_id":_stackData!.tblStakePeriodId.toString(),
                  "amount":amount2.toInt().toString(),
                  "trasection_id":transationId.toString(),
                  "to_addres":toAddress
                };
                fundDepositAPI(bToken,hashMap).then((value){
                  if(value.status){
                    EasyLoading.dismiss();
                    EasyLoading.showToast(value.message.toString(),
                        toastPosition:EasyLoadingToastPosition.bottom );
                    navPushReplace(context,AffiliateDashBoardView());
                  }else{
                    EasyLoading.dismiss();
                    EasyLoading.showToast(value.message.toString(),
                        toastPosition:EasyLoadingToastPosition.bottom );
                  }
                });
              }else{
                EasyLoading.dismiss();
                EasyLoading.showToast('Incorrect Payment Hash ..'.tr,
                    toastPosition:EasyLoadingToastPosition.bottom );
              }
            });

          }, title: 'Deposit'.tr,),)
      ],),
    );
  }
  
  Widget _qrCodeView() => Column(
    children: [
      Container(
        margin: EdgeInsets.only(top:16,left: 16,right: 16,bottom: 8),
        padding: EdgeInsets.only(top:16,left: 16,right: 16,bottom: 8),
        child: Column(children: [
          LinearProgressIndicator(backgroundColor: ColorConstants.APP_PRIMARY_COLOR,),
          SizedBox(height: 8,),
          Text('Scan or copy the below address to PAY', style: textStyleHeading2(color:
          ColorConstants.TEXT_COLOR.withOpacity(0.7)),),
          SizedBox(height: 16,),

          Text('Scan', style: textStyleHeading(color: ColorConstants.TEXT_COLOR.withOpacity(0.7)),),
          QrImage(
            data: "TYiAJgfZkKZUV1An5RfnD6ad8mftYLc5jg",
            version: QrVersions.auto,
            size: 200.0,
            embeddedImage: AssetImage('assets/expgain/icon_splash.png'),
          ),

          Row(children: [
            Expanded(child: Text('TYiAJgfZkKZUV1An5RfnD6ad8mftYLc5jg', textAlign: TextAlign.center,)),
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
                      text: "TYiAJgfZkKZUV1An5RfnD6ad8mftYLc5jg"
                  ));
                  Fluttertoast.showToast(msg: 'Address copied',
                      backgroundColor: Colors.black);
                }, icon: Icon(Icons.copy,color: Colors.white,), label: Text('Copy',style:
                  TextStyle(color: Colors.white),)),
                Container(width: 1, color: Colors.black,
                  margin: EdgeInsets.all(4),),
                TextButton.icon(onPressed: (){
                  Share.share("TYiAJgfZkKZUV1An5RfnD6ad8mftYLc5jg");
                }, icon: Icon(CupertinoIcons.arrowshape_turn_up_right_fill,color: Colors.white,),
                    label: Text('Share',style:
                    TextStyle(color: Colors.white))),
              ],
            ),

            decoration: BoxDecoration(
                color: Colors.black.withOpacity(0.5),
                borderRadius: BorderRadius.circular(25)
            ),
          ),

        ],),
        decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(8)
        ),
      ),
      Padding(
        padding: const EdgeInsets.only(right: 18.0),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.end,
          children: [
            TextButton.icon(onPressed: (){
              setState(() {
                depositUIVisibility=true;
                qrCodeUIVisibility=false;
              });
            }, icon: Icon(Icons.cancel,color: Colors.black,), label: Text('Cancel',style:
            TextStyle(color: Colors.black),)),
          ],
        ),
      ),
      _confirmTransaction()
    ],
  );

  void qrCode() async{
    setState(() {
      depositUIVisibility=false;
      qrCodeUIVisibility=true;
    });
    Random rn= Random();
    int orderId=rn.nextInt(10000000);
    String amount=controllerAmount.text;
     var hashMap={
       "price_amount":amount,
       "price_currency":"USD",
       "pay_currency":"USDT",
       "ipn_callback_url":"https://nowpayments.io",
       "order_id":"DAR$orderId",
       "order_description":"Deposit Currency"
     };

     try{


     }catch(e){
       Fluttertoast.showToast(msg: e.toString());
     }

  }

  bool validation() {
    if(controllerAmount.text.trim().isEmpty){
      Fluttertoast.showToast(msg: "Enter Amount");
      return false;
    }
    if(_stackData==null){
      Fluttertoast.showToast(msg: "Please Select Stake Period ");
      return false;
    }
    return true;
  }
}
