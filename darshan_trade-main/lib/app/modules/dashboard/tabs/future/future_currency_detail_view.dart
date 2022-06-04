


import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/src/widgets/framework.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:get/get.dart';
import 'package:royal_q/app/modules/dashboard/tabs/future/ftrade_setting_view.dart';
import 'package:royal_q/app/modules/dashboard/tabs/future/ftrade_hisotory_view.dart';
import 'package:royal_q/app/routes/app_pages.dart';
import 'package:royal_q/app/shared/shared.dart';
import '../../../../data/user_data.dart';
import '../../../TradeSettings/views/trade_settings_view.dart';
import '../controllers/ftrade_setting_controller.dart';
import '../home/transaction_record.dart';
import 'asset_mode_view.dart';

class FutureCurrencyDetailView extends GetView<FTradeSettingsController>{





  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: const Color(0xFFF87E02),
        title: Text(controller.model.value.symbol,style: textStyleHeading(color: Colors.white),),
        actions: [
          TextButton(onPressed: () => Get.to(FTransactionRecord(symbol: controller.model.value.symbol,
            exchangeType: 1,)), child: Text('Transaction_record'.tr,
            style: textStyleLabel(color: ColorConstants.whiteRev),)),
        ],
      ),
      body:  Column(children: [
        Expanded(child: ListView(children: [

          headerOptionUI(context),

          _bodyContainer(child: Obx(() => Column(children: [

           Obx(() =>  SizedBox(
             height: 40,
             child: Row(
               mainAxisAlignment:MainAxisAlignment.center,
               children: [
                 Padding(
                   padding: const EdgeInsets.fromLTRB(15.0,8,0,0),
                   child: Text(controller.model.value.symbol,style: TextStyle(
                       fontSize: 17,
                       fontWeight: FontWeight.w700,color: Colors.black),),
                 ),

                 Padding(
                   padding: const EdgeInsets.fromLTRB(15.0,8,0,0),
                   child: Text("${controller.tabName1} ${controller.leveageValue.value}",style: textStyleHeading(
                       fontSize: 13.0,color: Colors.black.withOpacity(0.4)),),
                 ),
                 Spacer(),

               ],),
           ),),
            Padding(
              padding: const EdgeInsets.only(left:15.0,top: 8,right: 10),
              child: Row(
                children: [
                  Text("PNL (USDT)",style: textStyleLabel(color: Colors.grey[700]),),
                  Spacer(),
                  Text("ROE",style: textStyleLabel(color: Colors.grey[700]),),
                ],
              ),
            ),

            Padding(
              padding: const EdgeInsets.only(left:15.0,top: 5,right: 10),
              child: Row(
                children: [
                  Text(controller.PNLUSDT.value.toStringAsFixed(3),style: TextStyle(color: Colors.red,fontSize:18.0,
                      fontWeight: FontWeight.bold),),
                  Spacer(),
                  Text("${controller.ROE.value.toString()}%",style: TextStyle(color: Colors.red,fontSize:18.0,
                      fontWeight: FontWeight.bold),),
                ],
              ),
            ),
            minSpacing(8, 0),
            Padding(
              padding: const EdgeInsets.only(left:15.0,top: 8,right: 10),
              child: Row(
                children: [
                  Expanded(
                      flex:40,
                      child: Text("Size (USDT)",style: textStyleLabel(color: Colors.grey[700]),)),

                  Expanded(
                      flex:40,
                      child: Text("Margin (USDT)",style: textStyleLabel(color: Colors.grey[700]),)),

                  Expanded(
                      flex: 20,
                      child: Align(
                          alignment: Alignment.centerRight,
                          child: Text("Risk ",style: textStyleLabel(color: Colors.grey[700]),))),
                ],
              ),
            ),
            Padding(
              padding: const EdgeInsets.only(left:18.0,top: 2,right: 10),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.start,
                children: [
                  Expanded(
                    flex: 40,
                    child: Text(controller.uSDTSize.value.toStringAsFixed(2),style: TextStyle(color: Colors.black,fontWeight: FontWeight.w500,
                        fontSize: 16),),
                  ),

                  Expanded(
                    flex: 40,
                    child: Padding(
                      padding: const EdgeInsets.only(left:0.0),
                      child: Text(controller.uSDTMargin.value,style: TextStyle(color: Colors.black,fontWeight: FontWeight.w500,
                          fontSize: 16),),
                    ),
                  ),

                  Expanded(
                    flex: 20,
                    child: Align(
                      alignment: Alignment.centerRight,
                      child: Text("${controller.uSDTRisk.toString()}% ",style: TextStyle(color: Colors.black,fontWeight: FontWeight.w500,
                          fontSize: 16),),
                    ),
                  ),
                ],
              ),
            ),

            minSpacing(10, 0),
            Padding(
              padding: const EdgeInsets.only(left:15.0,top: 8,right: 10),
              child: Row(
                children: [
                  Expanded(
                      flex:40,
                      child: Text("Entry Price (USDT)",style: textStyleLabel(color: Colors.grey[700]),)),

                  Expanded(
                      flex:40,
                      child: Text("Mark Price (USDT)",style: textStyleLabel(color: Colors.grey[700]),)),

                  Expanded(
                      flex: 20,
                      child: Align(
                          alignment: Alignment.centerRight,
                          child: Text("Liq.Price ",style: textStyleLabel(color: Colors.grey[700]),))),
                ],
              ),
            ),
            Padding(
              padding: const EdgeInsets.only(left:18.0,top: 2,right: 10),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.start,
                children: [
                  Expanded(
                    flex: 40,
                    child: Text(controller.enrtyPrice.value.toString(),style: TextStyle(color: Colors.black,fontWeight: FontWeight.w500,
                        fontSize: 16),),
                  ),

                  Expanded(
                    flex: 40,
                    child: Padding(
                      padding: const EdgeInsets.only(left:0.0),
                      child: Text(controller.marketPrice.value.toStringAsFixed(4),style: TextStyle(color: Colors.black,fontWeight: FontWeight.w500,
                          fontSize: 16),),
                    ),
                  ),

                  Expanded(
                    flex: 20,
                    child: Align(
                      alignment: Alignment.centerRight,
                      child: Text("${controller.liqPrice.value.toStringAsFixed(4)}% ",style: TextStyle(color: Colors.black,fontWeight: FontWeight.w500,
                          fontSize: 16),),
                    ),
                  ),
                ],
              ),
            ),
            minSpacing(15, 0),

            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                minSpacing(0, 15),
                Expanded(
                  flex: 1,
                  child: Container(
                    decoration: BoxDecoration(borderRadius: BorderRadius.circular(4),
                        color: Colors.grey[300]),
                    child: Center(child: Text('Stop Position',style:
                    textStyleSubTitle(fontSize: 15.0),)),height: 40,width: 130,
                  ),
                ),
                minSpacing(0, 15),

              ],),
            minSpacing(10,0),
          ],))),
          minSpacing(10, 0),
          _bodyContainer(
            child: Padding(
              padding: const EdgeInsets.only(top:10.0,bottom: 10,left: 8),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text('Operation_reminder'.tr, style: textStyleHeading2(),),
                  SizedBox(height: 8,),
                  Text('eEzyTradeMessage'.trParams({'name': '$APP_NAME'}), style: textStyleLabel(color: ColorConstants.white54),),
                ],),
            ),
          ),
          _bodyContainer(child: Padding(
            padding: const EdgeInsets.only(top:10.0,bottom: 10,left: 8),
            child: GestureDetector(
              child: Row(children: [
                Expanded(child: Padding(
                  padding: const EdgeInsets.only(left:8.0),
                  child: Text('First_preset_buy_in_price'.tr, style: textStyleLabel(),),
                )),
                Obx(() => Text('${controller.tradeResponse.value.firstBuy}', style: textStyleLabel(),)),
                Icon(Icons.chevron_right, color: ColorConstants.white,)
              ],),

              onTap: (){
                controller.onChangeTermValue();
                Get.to(FTradeSettingsView());
              },
            ),
          )),
          _bodyContainer(child: Padding(
            padding: const EdgeInsets.all(8.0),
            child: Table(
              border: TableBorder(verticalInside: BorderSide(width: 1, color: Colors.black12),
                horizontalInside: BorderSide(width: 1, color: Colors.black12),
              ),

              children: [
                TableRow(children: [
                  Obx(() => _cellContainer(title: 'First_Buy_in_amount'.tr, value: controller.tradeResponse.value.firstBuy, image: 'assets/icons/ic_quota.png'),),
                  Obx(() => _cellContainer(title: 'Margin_call_limit'.tr,
                      value: controller.tradeResponse.value.maginCallLimit,
                      image: 'assets/icons/ic_order_max.png', isLeft: false)),
                ]),

                TableRow(children: [
                  Obx(() => _cellContainer(title: 'Take_profit_ratio'.tr, value: '${controller.tradeResponse.value.wholePRatio}%', image: 'assets/icons/ic_take_profit_rate.png')),
                  Obx(() => _cellContainer(title: 'Earnings_callback'.tr, value: '${controller.tradeResponse.value.wholePCallback}%', image: 'assets/icons/ic_take_profit_back.png', isLeft: false)),
                ]),

                TableRow(children: [
                  Obx(() => GestureDetector(
                    onTap: (){
                      print(controller.tradeResponse.value.marginList.first.calls);
                    },
                    child: _cellContainer(title: 'Margin_call_drop'.tr, value:
                    '${controller.tradeResponse.value.marginList.isEmpty?"0.0":
                    controller.tradeResponse.value.marginList.first.calls}%',
                        image: 'assets/icons/ic_cover_rate.png'),
                  )),
                  Obx(() => _cellContainer(title: 'Buy_in_callback'.tr, value:
                  '${controller.tradeResponse.value.buyCallback}%', image: 'assets/icons/ic_cover_back.png', isLeft: false)),
                ]),
              ],),
          )),

          minSpacing(10, 0)

        ],)),
        Container(
          height: 60,
          // color: ColorConstants.whiteRev,
          child: Row(children: [
            Expanded(child: GestureDetector(
              child: Container(
                width: double.infinity,
                height: 60,
                alignment: Alignment.center,
                child: Text('Trade_Settings'.tr, style: textStyleHeading2(color: ColorConstants.white),),
              ),
              // onTap: () => Get.toNamed(Routes.TRADE_SETTINGS),
              onTap:(){
                controller.onChangeTermValue();
                Get.to(FTradeSettingsView());
              },
            )),
            Obx(() => Expanded(child: controller.tradeStatus.value=="1"?GestureDetector(
              child: Container(
                height: 60,
                alignment: Alignment.center,
                color: Colors.red,
                child: Text('Pause'.tr, style: textStyleHeading2(color: ColorConstants.whiteRev),),),
              onTap: (){
                controller.exchangeType.value = exchangeValue;
                controller.startTrading(false,context);
              },
            ):GestureDetector(
              child: Container(
                height: 60,
                alignment: Alignment.center,
                color: const Color(0xFFF87E02),
                child: Text('Start'.tr, style: textStyleHeading2(color: ColorConstants.whiteRev),),),
              onTap: (){
                controller.exchangeType.value = exchangeValue;
                controller.startTrading(true,context);
                // _bindSpotTradeAPI(body);
              },
            ))),
          ],),
        )
      ],),
    );
  }


  Widget _cellContainer({required title, required value, required image, isLeft=true}) => Container(
    padding: EdgeInsets.symmetric(vertical: 16),
    child: Row(children: [
      SizedBox(width: isLeft?0:8,),
      Image.asset(image,width: 20,),
      SizedBox(width: 4,),
      Expanded(child: Text(title, style: textStyleLabel(),)),
      Text('$value', style: textStyleLabel(),),
      SizedBox(width: isLeft?8:4,)
    ],),
  );

  Widget _bodyContainer({required child}) => Container(
    child: child,
    margin: EdgeInsets.all(10),
    decoration: BoxDecoration(
      // color: const Color(0xFF102957),
        color: Colors.white.withOpacity(0.6),
        borderRadius: BorderRadius.circular(4)
    ),
  );

  Future xOpenBottomSheetView(BuildContext context) {
    return showModalBottomSheet(
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.vertical(top: Radius.circular(10))
        ),
        context: context, builder: (context){
      return Column(
        mainAxisSize: MainAxisSize.min,
        children: [

          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Spacer(),
              minSpacing(0, 20),
              Text("Adjust Leverage",style: TextStyle(fontSize: 16,fontWeight: FontWeight.w600),),
              Spacer(),
              IconButton(onPressed: (){
                Get.back();
              }, icon: Icon(Icons.cancel))
            ],
          ),

          Padding(
            padding: const EdgeInsets.only(left:25.0,right: 25,top: 15),
            child: Container(
              decoration: BoxDecoration(
                borderRadius: BorderRadius.circular(6),
                color: Colors.grey.withOpacity(0.1)
              ),
              height: 40,
              child: Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  IconButton(onPressed: (){}, icon: Icon(Ionicons.remove,size: 24,)),
                  Spacer(),
                  Text("20x",style: TextStyle(fontSize: 16),),
                  Spacer(),
                  IconButton(onPressed: (){}, icon: Icon(Ionicons.add,size: 24,)),
                ],
              ),
            ),
          ),
          minSpacing(10,0),
          Padding(
            padding: const EdgeInsets.only(left:35.0,right: 40,top: 10),
            child: Text("• Maximum position at current leverage: 5,00,000 USDT",style: TextStyle(fontSize: 15),),
          ),

          Padding(
            padding: const EdgeInsets.only(left:35.0,right: 40,top: 10),
            child: Text("• You can only increase leverage when holding position, please mind the impact to your holding position.",
                style: TextStyle(fontSize: 15)),
          ),
          minSpacing(10, 0),

          Padding(
            padding: const EdgeInsets.fromLTRB(20.0,20,15,20),
            child: SubmitButton(title: "Confirm", onPressed: () {
              Get.back();
            },),
          )


        ],
      );
    });
  }
  Future marginModeBottomSheetView(BuildContext context) {
    return showModalBottomSheet(
        shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.vertical(top: Radius.circular(10))
        ),
        context: context, builder: (context){
      return Column(
        mainAxisSize: MainAxisSize.min,
        children: [

          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Spacer(),
              minSpacing(0, 30),
              Text("Margin Mode",style: TextStyle(fontSize: 16,fontWeight: FontWeight.w700),),
              Spacer(),
              IconButton(onPressed: (){
                Get.back();
              }, icon: Icon(Icons.cancel))
            ],
          ),
           minSpacing(15.0,0.0),
          Obx(() => Row(
            children: [
              minSpacing(0, 20),
              Expanded(
                child: GestureDetector(
                  onTap: (){
                    controller.tabName.value="Cross";
                  },
                  child: Container(
                    height: 45,
                    decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(6),
                        color: Colors.grey.withOpacity(0.1),
                        border: Border.all(color: controller.tabName.value=="Cross"?
                        Colors.deepOrange:Colors.white10,width: 1)
                    ),
                    child: Center(child: Text("Cross",style: textStyleLabel(color: Colors.black),)),
                  ),
                ),
              ),
              minSpacing(0, 10),
              Expanded(
                child: GestureDetector(
                  onTap: (){
                    controller.tabName.value="Isolated";
                  },
                  child: Container(
                    height: 45,
                    decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(6),
                        color: Colors.grey.withOpacity(0.1),
                        border: Border.all(color: controller.tabName.value=="Cross"?
                        Colors.white10:Colors.deepOrange,width: 1)
                    ),
                    child: Center(child: Text("Isolated",style: textStyleLabel(color: Colors.black),)),
                  ),
                ),
              ), minSpacing(0, 20),
            ],
          ),),
          minSpacing(20, 0),
          Padding(
            padding: const EdgeInsets.fromLTRB(20.0,10,15,10),
            child: Text("Switching of margin mode only applies to the selected contract."),
          ),

          Padding(
            padding: const EdgeInsets.fromLTRB(20.0,20,15,20),
            child: SubmitButton(title: "Confirm", onPressed: () {
              controller.tabName1.value=controller.tabName.value;
              controller.saveMarginMode();
            },),
          )

        ],
      );
    });
  }

  Widget minSpacing(double height,double width)=>SizedBox(height: height,width: width,);

  leverageSpinnerUIView()=>Expanded(
    flex: 20,
    child: SizedBox(
      height: 36,
      child: DropdownButtonFormField(
        value: controller.leveageValue.value,
        decoration: InputDecoration(
            enabledBorder: OutlineInputBorder(
                borderRadius: BorderRadius.circular(8),
                borderSide: BorderSide(
                    width: 1.0,
                    color:Color(0xffece7e7)
                )
            ),
            border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(8),
                borderSide: BorderSide(
                    width: 1.0,
                    color: Color(0xffece7e7)
                )),
            errorBorder: OutlineInputBorder(
                borderRadius: BorderRadius.circular(8),
                borderSide: BorderSide(
                    width: 1.0,
                    color: Color(0xffece7e7)
                )),
            focusedBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(8),
              borderSide: BorderSide(
                  width: 1,
                  color: Color(0xffece7e7)
              ),
            ),
            contentPadding: const EdgeInsets.only(top: 20, left: 10),
            hintText: "2X",hintStyle: TextStyle(fontSize: 12),
            filled: true,
            fillColor: Colors.white.withOpacity(0.4)),
        items: controller.leveage.map((e){
          return DropdownMenuItem(
              value: e,child: Text(e,style: TextStyle(fontSize: 14),));
        }).toList(), onChanged: (String? value) {
          controller.leveageValue.value=value!;
          controller.saveLeverageMode();
      },),
    ),
  );

  shortLongSpinnerUIView()=> Expanded(
    flex: 22,
    child: SizedBox(
      height: 36,width: 95,
      child: DropdownButtonFormField(
        value: controller.termValue.value,
        decoration: InputDecoration(
            enabledBorder: OutlineInputBorder(
                borderRadius: BorderRadius.circular(8),
                borderSide: BorderSide(
                    width: 1.0,
                    color:Color(0xffece7e7)
                )
            ),
            border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(8),
                borderSide: BorderSide(
                    width: 1.0,
                    color: Color(0xffece7e7)
                )),
            errorBorder: OutlineInputBorder(
                borderRadius: BorderRadius.circular(8),
                borderSide: BorderSide(
                    width: 1.0,
                    color: Color(0xffece7e7)
                )),
            focusedBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(8),
              borderSide: BorderSide(
                  width: 1,
                  color: Color(0xffece7e7)
              ),
            ),
            contentPadding: const EdgeInsets.only(top: 20, left: 10),
            hintText: "Short",hintStyle: TextStyle(fontSize: 12),
            filled: true,
            fillColor: Colors.white.withOpacity(0.4)),
        items: controller.items.map((e){
          return DropdownMenuItem(
              value: e,child: Text(e,style: TextStyle(fontSize: 14),));
        }).toList(), onChanged: (String? value) {
          controller.termValue.value=value!;
      },),
    ),
  );

  headerOptionUI(BuildContext ctx) =>Padding(
    padding: const EdgeInsets.all(12.0),
    child: Row(
      children: [
        Expanded(
            flex: 32,
            child: Obx(() => GestureDetector(
              onTap: (){
                marginModeBottomSheetView(ctx);
              },
              child: Container(
                  decoration: BoxDecoration(
                      color: Colors.white.withOpacity(0.4),
                      borderRadius: BorderRadius.circular(8)
                  ),
                  height: 35,width: 110,
                  child: Center(child: Row(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      SizedBox(width: 15,),
                      Text(controller.tabName1.value,style: textStyleLabel(),),
                      SizedBox(width: 10,),
                      Icon(Icons.arrow_drop_down,size: 25,)
                    ],
                  ))),
            ),)),
        SizedBox(width: 11,),
        /*Expanded(
            flex: 22,
            child: Obx(() => GestureDetector(
              onTap: (){
                levarageModeBottomSheetView(ctx);
              },
              child: Container(
                  decoration: BoxDecoration(
                      color: Colors.white.withOpacity(0.4),
                      borderRadius: BorderRadius.circular(8)
                  ),
                  height: 35,width: 110,
                  child: Center(child: Row(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      SizedBox(width: 15,),
                      Text(controller.leveageValue.value,style: textStyleLabel(),),
                      SizedBox(width: 10,),
                      Icon(Icons.arrow_drop_down,size: 25,)
                    ],
                  ))),
            ),)),*/
        leverageSpinnerUIView(),
        minSpacing(0, 11),
        shortLongSpinnerUIView(),
        minSpacing(0, 8),
        GestureDetector(
          onTap: (){
            Get.to(AssetModeView());
          },
          child: Container(
            width: 30,
            height: 36,
            decoration: BoxDecoration(
              color:Colors.white.withOpacity(0.4),
              borderRadius: BorderRadius.circular(8),
            ),
            child: Center(child: Text("S")),
          ),
        ),
        Expanded(
            flex: 10,
            child: Container(
              height: 40,
            ))
      ],),
  );

   levarageModeBottomSheetView(BuildContext ctx) {
     Get.bottomSheet(
         Container(
           decoration: BoxDecoration(
               borderRadius: BorderRadius.vertical(top: Radius.circular(10)),
             color: Color(0xffece7e7),

           ),
           child: Column(
             mainAxisSize: MainAxisSize.min,
             children: [
               Row(
                 mainAxisAlignment: MainAxisAlignment.center,
                 children: [
                   Spacer(),
                   minSpacing(0, 30),
                   Text("Adjust Leverage",style: TextStyle(fontSize: 16,fontWeight: FontWeight.w700),),
                   Spacer(),
                   IconButton(onPressed: (){
                     Get.back();
                   }, icon: Icon(Icons.cancel))
                 ],
               ),
               minSpacing(15.0,0.0),
               Padding(
                 padding: const EdgeInsets.only(left:20.0,right: 20),
                 child: Container(
                   width: double.infinity,
                   height: 50,
                   decoration: BoxDecoration(
                     borderRadius: BorderRadius.circular(6),
                     color: Colors.grey[300],
                   ),
                   child: Row(
                     mainAxisAlignment: MainAxisAlignment.center,
                     children: [

                       IconButton(onPressed: (){
                        int leverage=int.parse(controller.leveageValue.value);
                        int lev=leverage++;
                        print(lev);
                        controller.onSetLeaverageValue();
                       }, icon: Icon(Icons.remove,color: Colors.black,size: 16,)),
                       Expanded(
                         flex: 70,
                         child: TextField(
                           controller: controller.editleverageController,
                           textAlign: TextAlign.center,
                           style: TextStyle(color: Colors.black),
                           decoration:InputDecoration(
                               border:OutlineInputBorder(
                                   borderSide: BorderSide.none
                               )
                           ),
                         ),
                       ),
                       IconButton(onPressed: (){}, icon: Icon(Icons.add,color: Colors.black,size: 16,)),
                     ],),
                 ),
               ),
               minSpacing(20, 0),
               Padding(
                 padding: const EdgeInsets.fromLTRB(20.0,10,15,10),
                 child: Text("Switching of margin mode only applies to the selected contract."),
               ),

               Padding(
                 padding: const EdgeInsets.fromLTRB(20.0,20,15,20),
                 child: SubmitButton(title: "Confirm", onPressed: () {
                   controller.tabName1.value=controller.tabName.value;
                   controller.saveMarginMode();
                 },),
               )
             ],
           ),
         )
     );
    /*return showModalBottomSheet(
      isScrollControlled: true,
        shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.vertical(top: Radius.circular(10))
        ),
        context: ctx, builder: (context){
      return Obx(() => );
    });*/
  }
}