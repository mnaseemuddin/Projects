import 'dart:async';
import 'dart:convert';
import 'dart:ui';
import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:get/get.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:http/http.dart' as http;
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/modules/AffiliateApp/model/AllTradeResponse.dart';
import 'package:royal_q/app/modules/AffiliateApp/model/DepositTransactionHistoryResponse.dart';
import 'package:royal_q/app/modules/AffiliateApp/modules/AffiliateDespoistView.dart';
import 'package:royal_q/app/modules/AffiliateApp/modules/AffiliateProfileDetailView.dart';
import 'package:royal_q/app/modules/TradeSettings/views/currency_detail_view.dart';
import 'package:royal_q/app/shared/constants/colors.dart';
import 'package:royal_q/app/shared/sawidgets/sa_clip_card.dart';
import 'package:royal_q/app/shared/sawidgets/sawidgets.dart';
import 'package:transparent_image/transparent_image.dart';

import '../../../../api/apis.dart';
import '../../../models/response/order_transaction_response.dart';
import '../../../models/response/totalbilling_resposne.dart';
import '../../../shared/constants/common.dart';
import '../../../shared/utils/navigator_helper.dart';
import '../auth/AffiliateLoginView.dart';
import 'WithdrawalView.dart';

class AffiliateHomeView extends StatefulWidget {
  final Function(int)movePage;
  const AffiliateHomeView({Key? key, required this.movePage}) : super(key: key);

  @override
  _AffiliateHomeViewState createState() => _AffiliateHomeViewState();
}

class _AffiliateHomeViewState extends State<AffiliateHomeView> {
  bool isLogin = true;
  double loginAndWalletExpandedHeight = 200, marketExpandedHeight = 600;
  //MarketLiveRateModel? marketLive;
  List _currencies = [];
  //List<MarketLiveRateModel> currenciesList = [];
  double prevAmt = 0.0, upDown = 0.00;
  int duration = 5;
  double walletAmt = 0.00;
 // ProfileDetailsModel? details;
  int quarterTurns = 0;
  List<AffiliateAllOrderTransacionResponse> _list=[];
  List<double> listMarketUpDown = [280800.0,280600.0,280400.0,280500.0,280190.0,280100.0,
    280800.0,280600.0,280400.0,280500.0,280190.0,280100.0,280800.0,280600.0,280400.0,280500.0,280190.0,280100.0];
  var totalBillingResponse=RxList<TotalBillingResponse>();
  String currencyPhotoUrl="https://cryptologos.cc/logos/tether-usdt-logo.png";
  DepositTransationResponse? depositModel;
  int pageCount=1;

  bool IsProgressDialog=false;

  String userName = '';
  String package="0.0";

  Size? _size;

  List banners=[
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQcOwSLtWZF0ZHXrZDpbT3ozjH2tgta2dn9iQ&usqp=CAU",
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQcOwSLtWZF0ZHXrZDpbT3ozjH2tgta2dn9iQ&usqp=CAU",
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQcOwSLtWZF0ZHXrZDpbT3ozjH2tgta2dn9iQ&usqp=CAU"
  ];

  @override
  void initState() {
    getUser().then((value){
      userName = value?.data.first.fullName??'';
      getTransationHistoryAPI(value!.data.first.tblUserId.toString(),
          value!.data.first.jwtToken.toString()).then((value){
        if(value.status){
          setState(() {
           depositModel=value.data;
           walletAmt=double.parse(depositModel!.totalRoiIncome);
          });
        }
      });
      getAllOrderTransactionAPI(pageCount.toString()).then((value){
        if(mounted){
          setState(() {
            _list = value.data;
            // _list!.sort((a, b) => b.transactTime>a.transactTime?1:-1);
          });
        }
      });
    });

    super.initState();
  }

  @override
  Widget build(BuildContext context) {
     _size = MediaQuery.of(context).size;
    return Scaffold(
      backgroundColor: Colors.transparent,

      body: SafeArea(
        top: true,
        child: Column(
          children: [

            Row(children: [
              SizedBox(width: 16,),
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                Text(userName, style: GoogleFonts.rajdhani(
                    fontSize: 16, fontWeight: FontWeight.w700, color: Colors.white
                ),),
                SizedBox(),
                Text('Welcome Back', style: GoogleFonts.rajdhani(
                    fontSize: 14, fontWeight: FontWeight.w400, color: Colors.white
                ),),

              ],),
              Spacer(),
              GestureDetector(
                child: Container(
                  // child: Image.network(userInfo!.pic),
                  child: Image.asset('assets/expgain/profile.png', width: 40,),
                  padding: EdgeInsets.all(8),
                  decoration: BoxDecoration(
                    color: Color(0xFF3F484C),
                    shape: BoxShape.circle,
                  ),
                ),
                onTap: (){
                  widget.movePage(3);
                },
              ),
              SizedBox(width: 8,)
            ],),

            SizedBox(height: 8,),

            Row(children: [
              SizedBox(width: 16,),
              SAClipCard(balance: depositModel?.totalRoiIncome??'0.00',
                package:depositModel?.data==null?"0.00":depositModel!.data.first.amount.toString(),),
              SizedBox(width: 14,),

              Expanded(child: Column(children: [
                GestureDetector(
                  child: Container(
                    padding: EdgeInsets.all(8),
                    height: 60,
                    width: double.infinity,
                    alignment: Alignment.center,
                    child: Row(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        Icon(Icons.login, color: Colors.white,),
                        SizedBox(width: 20,),
                        Text('DEPOSIT', style: GoogleFonts.rajdhani(
                            fontSize: 16, fontWeight: FontWeight.w700, color: Colors.white
                        ),),
                      ],),
                    decoration: BoxDecoration(
                        gradient: LinearGradient(colors: [
                          Color(0xFF014101),
                          Color(0xFF00FF00),
                        ]),
                        borderRadius: BorderRadius.only(
                          topLeft: Radius.circular(30),
                        )
                    ),
                  ),
                  onTap: (){
                    Get.to(() => AffDepositView());
                  },
                ),

                SizedBox(height: 16,),

                GestureDetector(
                  child: Container(
                    padding: EdgeInsets.all(8),
                    height: 60,
                    width: double.infinity,
                    alignment: Alignment.center,
                    child: Row(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        Icon(Icons.app_registration, color: Colors.white,),
                        SizedBox(width: 6,),
                        Text('WITHDRAW', style: GoogleFonts.rajdhani(
                            fontSize: 16, fontWeight: FontWeight.w700, color: Colors.white
                        ),),
                      ],),
                    decoration: BoxDecoration(
                        gradient: LinearGradient(colors: [
                          Color(0xFFFF0000),
                          Color(0xFFFF5454),
                        ]),

                        borderRadius: BorderRadius.only(
                          bottomLeft: Radius.circular(30),
                        )
                    ),
                  ),

                  onTap: (){
                    Get.to(() => WithdrawalView());
                  },
                )

              ],))


            ],),

        //     Container(
        //       padding: EdgeInsets.symmetric(vertical: 16),
        //       decoration: BoxDecoration(color: Colors.yellow[800],
        //         image: DecorationImage(image: AssetImage('assets/expgain/background_gradient.png'), fit: BoxFit.fill)
        //       ),
        //
        //       child: Column(children: [
        //         balanceAndDepositUI(),
        //         SizedBox(height: 16,),
        //         Padding(padding: EdgeInsets.symmetric(horizontal: size.width/8),
        //           child: Row(children: [
        //             Expanded(child: GestureDetector(
        //               child: Container(height: 50,
        //                 padding: EdgeInsets.symmetric(vertical: 8),
        //                 alignment: Alignment.center,
        //                 child: Row(
        //                   mainAxisSize: MainAxisSize.min,
        //                   children: [
        //                     Icon(Icons.arrow_circle_down_outlined, color: Colors.white,),
        //                     SizedBox(width: 8,),
        //                     Text('Deposit', style: GoogleFonts.rajdhani(
        //                         fontSize: 18, fontWeight: FontWeight.w500, color: Colors.white
        //                     ))
        //                   ],),
        //                 decoration: BoxDecoration(
        //                     color: Colors.green,
        //                     borderRadius: BorderRadius.only(
        //                       topLeft: Radius.circular(25),
        //                       bottomLeft: Radius.circular(25),
        //                     )
        //                 ),),
        //               onTap: (){
        //                 navPush(context, const AffDepositView());
        //               },
        //             ),),
        // //
        //             Expanded(child: Container(height: 50,
        //               padding: EdgeInsets.symmetric(vertical: 8),
        //               alignment: Alignment.center,
        //               child: Row(
        //                 mainAxisSize: MainAxisSize.min,
        //                 children: [
        //
        //                   Text('Withdrawal', style: GoogleFonts.rajdhani(
        //                       fontSize: 18, fontWeight: FontWeight.w500, color: Colors.white
        //                   ),),
        //                   SizedBox(width: 8,),
        //                   Icon(Icons.arrow_circle_up_outlined, color: Colors.white,),
        //
        //                 ],),
        //               decoration: BoxDecoration(
        //                   color: Colors.red,
        //                   borderRadius: BorderRadius.only(
        //                     topRight: Radius.circular(25),
        //                     bottomRight: Radius.circular(25),
        //                   )
        //               ),))
        //
        //           ],),),
        //       ],),
        //     ),

         // Expanded(
         //   // flex: 12,
         //   child: balanceAndDepositUI(),),

            Expanded(
              // flex: 75,
              child: Stack(children: [
                _list.isEmpty?AFNoRecord(): Container(
                  child: Padding(
                    padding: const EdgeInsets.only(top:24.0),
                    child: NotificationListener<ScrollNotification>(
                        onNotification: (ScrollNotification scrollInfo){
                          if(!IsProgressDialog&&scrollInfo.metrics.pixels==
                              scrollInfo.metrics.maxScrollExtent){
                            setState(() {
                              IsProgressDialog=true;
                              pageCount+=25;
                              EasyLoading.show();
                              getAllOrderTransactionAPI(pageCount.toString()).then((value){
                                if(mounted){
                                  setState(() {
                                    EasyLoading.dismiss();
                                    _list = value.data;
                                    // _list!.sort((a, b) => b.transactTime>a.transactTime?1:-1);
                                  });
                                }
                              });
                            });
                          }
                          return false;
                        },
                        child: allTradeUI()),
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.only(top:1.0,bottom: 0,left: 8),
                  child: Text("History",style: TextStyle(fontSize: 18,fontWeight: FontWeight.w600, color: Colors.white),),
                )

              ],),
            )
          ],
        ),
      ),
    );
  }


  Widget allTradeUI(){
    return Container(
      padding: EdgeInsets.symmetric(horizontal: 4),
      decoration: BoxDecoration(
          borderRadius: BorderRadius.only(topLeft: Radius.circular(8),topRight: Radius.circular(8))
      ),

      child: ListView.builder(itemBuilder: (context, index){
        AffiliateAllOrderTransacionResponse model = _list!.elementAt(index);
        return CustomCell(model: model,);
      }, itemCount: _list!.length,),
    );
  }


  Widget _pageTotalBilling() => ListView.builder(itemBuilder: (context, index){

    TotalBillingResponse model = totalBillingResponse.elementAt(index);
    String title = model.symbol.replaceAll('USDT', '');
    return SACellContainer(
        margin: EdgeInsets.symmetric(vertical: 2, horizontal: 4),
        child: Row(children: [
          Expanded(child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text('$title/USDT', style: textStyleHeading2(color: ColorConstants.APP_SECONDARY_COLOR),),
              Text('Platform: ${model.platform}')
            ],)), Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(children: [
                Text('Profit: ${model.profit}', style: textStyleLabel(),),
                Text(' USDT', style: textStyleLabel(),),
              ],),
              SizedBox(height: 4,),
              // Text(model.transdate, style: textStyleFootNote(),)
            ],)
        ],));
  }, itemCount: totalBillingResponse.length,);

  balanceAndDepositUI() {
    //  --------- Balance and profile ---------

      return Padding(padding: EdgeInsets.only(top: 8, left: 20, right: 20),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.end,
          children: [
            Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [

                Text('\$${walletAmt.toStringAsFixed(5)}', style: GoogleFonts.rajdhani(
                    fontSize: 22, fontWeight: FontWeight.w700, color: Colors.white
                ),),
                Text('Total Balance', style: GoogleFonts.rajdhani(
                    fontSize: 14, fontWeight: FontWeight.w600, color: Colors.white
                )),
              ],),
            Spacer(),
            Column(
              crossAxisAlignment: CrossAxisAlignment.end,
              children: [
                Container(width: 40, height: 40,
                  decoration: BoxDecoration(
                      border: Border.all(color: Colors.white, width: 1),
                      borderRadius: BorderRadius.circular(8),
                      image: DecorationImage(image: AssetImage('assets/expgain/profile.png'), fit: BoxFit.fill)
                      // image: DecorationImage(image: NetworkImage('$BASE/DP/${userInfo!.pic}'))
                  ),
                ),
                SizedBox(height: 4,),
                Text('Demo', style: GoogleFonts.rajdhani(
                    fontSize: 12, fontWeight: FontWeight.w600, color: Colors.white
                )),
              ],),
          ],),);

    return Column(
      children: [

        const Align(
            alignment: Alignment.centerLeft,
            child: Padding(
              padding: EdgeInsets.only(left: 24.0, top: 0),
              child: Text("Total Balance",style: TextStyle( fontSize: 16,
                  color: Colors.white),
                 ),
            )),
        SizedBox(height: 4,),
        Padding(
          padding: EdgeInsets.only(left: 24.0, top: 5,bottom: 5),
          child: Align(
              alignment: Alignment.centerLeft,
              child: Row(
                children: [
                  ClipRRect(
                      borderRadius: BorderRadius.circular(20),
                      child: Image.network(
                        currencyPhotoUrl,
                        height: 25,
                        width: 25,
                      )),
                  SizedBox(width: 4,),
                  Text(
                    walletAmt.toStringAsFixed(5),style: TextStyle( fontSize: 20,
                      color: Colors.white),
                     ),
                  SizedBox(width: 5,),
                  Text(
                    " USDT",style: TextStyle(fontSize: 14,
                      color: Colors.white,fontWeight: FontWeight.w500),
                  ),
                ],
              )),
        ),
        Row(
          children: [
            Expanded(
              child: Padding(
                padding: const EdgeInsets.fromLTRB(15.0, 8, 15, 0),
                child: SizedBox(
                  height: 44,
                  child: ElevatedButton.icon(
                      style: ButtonStyle(
                          backgroundColor: MaterialStateProperty.all<Color>(
                              Color(0xffebecee)),
                          foregroundColor: MaterialStateProperty.all<Color>(
                              Color(0xffebecee)),
                          shape:
                          MaterialStateProperty.all<RoundedRectangleBorder>(
                              RoundedRectangleBorder(
                                  borderRadius: BorderRadius.circular(30),
                                  side: const BorderSide(
                                      color: Color(0xffebecee))))),
                      onPressed: () {
                        navPush(context, const AffDepositView());
                      },
                      label: Text(
                        'Deposit'.toUpperCase(),
                        style: TextStyle(color: Colors.black),
                      ),
                      icon: const Icon(
                        Icons.arrow_circle_up_outlined,
                        color: Colors.black,
                      )),
                ),
              ),
            ),
            Expanded(
              child: Padding(
                padding: const EdgeInsets.fromLTRB(15.0, 8, 15, 0),
                child: SizedBox(
                  height: 44,
                  child: ElevatedButton.icon(
                      style: ButtonStyle(
                          backgroundColor: MaterialStateProperty.all<Color>(
                              Color(0xfffbd536)),
                          foregroundColor: MaterialStateProperty.all<Color>(
                              Color(0xfffbd536)),
                          shape:
                          MaterialStateProperty.all<RoundedRectangleBorder>(
                              RoundedRectangleBorder(
                                  borderRadius: BorderRadius.circular(30),
                                  side: const BorderSide(
                                      color: Color(0xfffbd536))))),
                      onPressed: () {
                       navPush(context, WithdrawalView());
                      },
                      label: Text(
                        'Withdraw'.toUpperCase(),
                        style: TextStyle(color: Colors.black),
                      ),
                      icon: const Icon(
                        Icons.arrow_circle_down,
                        color: Colors.black,
                      )),
                ),
              ),
            )
          ],
        )
      ],
    );
  }

}


class CustomPaintNotch extends CustomPainter {
  @override
  void paint(Canvas canvas, Size size) {
    var paint = Paint()
      ..color = Colors.green;

    Path path = Path();

    path.moveTo(0, 0);
    path.lineTo(0, size.height);
    path.lineTo(size.width, size.height,);
    path.lineTo(3*size.width/4, 0);
    canvas.drawPath(path, paint);
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) {
    return false;
  }
}

class CustomCell extends StatelessWidget {
  final AffiliateAllOrderTransacionResponse model;
  const CustomCell({Key? key, required this.model}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    String title = model.symbol.toString().replaceAll('USDT', '');
    String tradeStatus=model.tradeStatus;

    return Container(
      margin: EdgeInsets.symmetric(horizontal: 8, vertical: 8),
      child: Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Stack(children: [

          Positioned(
              top: 0, right: 0,
              child: Container(
            height: 56,
            width: 40,
            decoration: BoxDecoration(
                color: Color(0xFF353D48),
                borderRadius: BorderRadius.circular(30)
            ),
          )),

          Container(
            margin: EdgeInsets.only(top: 8, right: 8),
            padding: EdgeInsets.all(8),
            child: FadeInImage.memoryNetwork(
              placeholder: kTransparentImage,
              image: 'https://xpertgain.io/symbol/${model.symbol.toString().replaceAll('USDT', '').toLowerCase()}@2x.png',
              width: 36, height: 36,
              imageErrorBuilder: (context, url, error) => Image.asset('assets/images/icon_xg.png', width: 48,),
            ),
            decoration: BoxDecoration(
                color: Color(0xFF353D48),
                borderRadius: BorderRadius.circular(12)
            ),
          )
        ],),
        SizedBox(width: 8,),
        Expanded(child: Column(children: [
          SizedBox(height: 8,),
            Row(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
              Text(title, style: GoogleFonts.rajdhani(
                  color: Colors.white, fontSize: 22, fontWeight: FontWeight.w700
              ),),
              Expanded(child: SizedBox()),

              Text(model.tradeAmount.toStringAsFixed(4), style: GoogleFonts.rajdhani(
                  color: Colors.white, fontSize: 22, fontWeight: FontWeight.w700
              ),),

                SizedBox(width: 4,),
                Column(children: [
                  Text('USDT', style: GoogleFonts.rajdhani(
                      color: Colors.white,
                      fontSize: 12, fontWeight: FontWeight.w500
                  ),),

                  Text(model.side, style: GoogleFonts.rajdhani(
                      color: model.side.toUpperCase().contains('BUY')?Colors.green[300]:Colors.redAccent, fontSize: 12, fontWeight: FontWeight.w500
                  ),),

                ],)
            ],),

          _cellRow('Exchange', model.platform, ''),
          _cellRow('Quantity', model.qunatity, title),
          _cellRow('Price', model.price, 'USDT'),
          _cellRow('Fee', model.commission, 'USDT'),
          _cellRow('', model.transactTime, ''),
          Divider(color: Colors.white24,)
        ],))
      ],),);

  }

  _cellRow(title, val, String type) => Row(children: [
    Expanded(child: Text(title, style: GoogleFonts.rajdhani(
        color: Colors.white,
        fontSize: 14, fontWeight: FontWeight.w500
    ),),),
    Text('$val', style: GoogleFonts.rajdhani(
        color: Colors.white,
        fontSize: 14, fontWeight: FontWeight.w500
    ),),
    if(type.isNotEmpty)
      Text(type, style: GoogleFonts.rajdhani(
          color: Color(0xFFFF5555),
          fontSize: 14, fontWeight: FontWeight.w500
      ),),
  ],);
}





