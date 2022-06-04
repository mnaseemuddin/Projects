import 'dart:async';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter/services.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:get/get.dart';
import 'package:royal_q/api/apis.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/modules/dashboard/controllers/dashboard_controller.dart';
import 'package:royal_q/app/modules/dashboard/tabs/controllers/currency_controller.dart';
import 'package:royal_q/app/modules/dashboard/tabs/home/trading_cell.dart';
import 'package:royal_q/app/modules/dashboard/tabs/mine/items/assets_detail.dart';
import 'package:royal_q/app/modules/howto/how_to_use.dart';
import 'package:royal_q/app/routes/app_pages.dart';
import 'package:royal_q/app/shared/sawidgets/sh_webview.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:royal_q/main.dart';
import 'package:share/share.dart';
import 'package:sticky_headers/sticky_headers.dart';
import 'package:transparent_image/transparent_image.dart';
import 'package:url_launcher/url_launcher.dart';
import '../tabs.dart';

class Home extends StatefulWidget {
  const Home({Key? key}) : super(key: key);

  @override
  _HomeState createState() => _HomeState();
}

class _HomeState extends State<Home> with SingleTickerProviderStateMixin {

  final DashboardController controller = Get.find<DashboardController>();

  final StreamController<int> _onPageChange = StreamController<int>.broadcast();

  final PageController _pageController = PageController();

  Size? _size;

  final CurrencyController _currencyController = Get.find<CurrencyController>();

  @override
  void initState() {
    controller.getXpertgainBalance();
    controller.updateUserInfo();
    controller.loadBanners();
    super.initState();
  }


  final List<ImageText> _menuItem = [
   ImageText(id: 0, image: 'assets/icons/ic_api_bind.png', text: 'API Binding'),
   // ImageText(id: 1, image: 'assets/icons/ic_profit_record.png', text: 'Community'),
    ImageText(id: 7, image: 'assets/icons/ic_record.png', text: 'Trade Record'),
    ImageText(id: 2, image: 'assets/icons/ic_billing.png', text: 'Billing Detail'),
  //  ImageText(id: 5, image: 'assets/icons/ic_leaderboard.png', text: 'Leaderboard'),

    ImageText(id: 3, image: 'assets/icons/ic_invite.png', text: 'Invite Friend'),
   // ImageText(id: 4, image: 'assets/icons/ic_usdt_fund.png', text: 'USDT Fund'),

  ];

  final List<ImageText> _menuItem1 = [
  //  ImageText(id: 5, image: 'assets/icons/ic_leaderboard.png', text: 'Leaderboard'),
   // ImageText(id: 6, image: 'assets/icons/ic_user_guid.png', text: 'User Guide'),
   /* ImageText(id: 7, image: 'assets/icons/ic_record.png', text: 'Trade Record'),
    ImageText(id: 3, image: 'assets/icons/ic_invite.png', text: 'Invite Friend'),*/
  ];

  Widget _createTopMenu(List<ImageText> menuItem){
    List<Widget> list = [];
    for (var element in menuItem) {
      list.add(GestureDetector(
        child: Container(
          width: SizeConfig().screenWidth*0.2,//(_size!.width-64>0?_size!.width-64:0)/(menuItem.length>3?3.9:3.7),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              SizedBox(height: 8,),
              Image.asset(element.image, width: 48,),
              SizedBox(height: 4,),
              Text(element.text, style: textStyleLabel(color: ColorConstants.black,
                  fontSize: SizeConfig().screenWidth*0.035), textAlign: TextAlign.center,)
            ],
          ),
        ),
        onTap: (){
          switch(element.id){
            case 0:
              Get.to(() => APIBinding());
              break;

            case 1:
              Get.toNamed(Routes.COMMUNITY);
              break;

            case 2:
              Get.toNamed(Routes.BILLING_DETAIL);
              break;

            case 3:
              Share.share("https://play.google.com/store/apps/details?id=com.bittgc.darshan");
              break;

            case 4:
              Get.to(() => AssetsDetail());
              break;

            case 5:
              EasyLoading.showToast('Coming soon');
              break;

              case 6:
                EasyLoading.showToast('Coming soon');
              // Get.to(() => SHWebView(url: 'http://jackbot.cloud/Userguide.html '));
              //launch('http://jackbot.cloud/Userguide.html');
              break;

              case 7:
                Get.to(() => TransactionRecord(exchangeType: exchangeValue,));
              break;
          }
        },
      ));
    }

    // list.add(Expanded(child: SizedBox()));

    return Container(
      padding: EdgeInsets.all(8),
      child: Row(
        mainAxisSize: MainAxisSize.max,
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: list,),
    );
  }

  @override
  Widget build(BuildContext context) {
    _size = MediaQuery.of(context).size;
    SystemChrome.setPreferredOrientations([
      DeviceOrientation.portraitUp,
      DeviceOrientation.portraitDown,
    ]);

    return Scaffold(
      appBar: AppBar(
        backgroundColor: ColorConstants.APP_PRIMARY_COLOR,
        title: Row(
          children: [
            Image.asset("assets/expgain/darklogo.png",height: 50,width: 50,),
            Spacer(),
            IconButton(onPressed: (){
              Get.to(NewsDashboard(),);
            }, icon: Icon(AntDesign.bells,color: Colors.black,size: 22,),)
          ],
        ),
      ),
      body: ListView(
        controller: controller.scrollController,
          children: [
            Obx(() => Padding(
              padding: const EdgeInsets.all(8.0),
              child: SACarousel(width: double.infinity, height: _size!.height/4.5,
                duration: 5,
                list: controller.banners.map((element){
                  return ClipRRect(
                    borderRadius: BorderRadius.circular(10),
                    child: FadeInImage.memoryNetwork(
                      placeholder: kTransparentImage,
                      image: element.url,
                      fit: BoxFit.fill,
                      imageErrorBuilder: (context, url, error) =>
                          Image.asset('assets/expgain/darklogo.png', width: 40, height: 40,),
                    ),
                  );
                }).toList(),
              ),
            )),
            SizedBox(height: 8,),
            Container(
              margin: EdgeInsets.symmetric(horizontal: 8),
              // padding: EdgeInsets.only(top: _size!.height/5, left: 8, right: 8, bottom: 8),
              child: Container(child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  _createTopMenu(_menuItem),
                  SizedBox(height: 8,),
                  _createTopMenu(_menuItem1)
                ],),
                decoration: BoxDecoration(
                    gradient: ColorConstants.gradientRev,
                    color: null,
                    borderRadius: BorderRadius.circular(16),
                    boxShadow: [BoxShadow(color: Colors.black12, offset: Offset(4,4))]
                ),
              ),
            ),
            SafeArea(child: StickyHeader(header: Container(
              margin: EdgeInsets.symmetric(horizontal: 15, vertical: 8),
              padding: EdgeInsets.symmetric(horizontal: 23, vertical: 16),
              alignment: Alignment.bottomLeft,

              decoration: BoxDecoration(
                  gradient: ColorConstants.gradientRev,
                  color: null,
                  borderRadius: BorderRadius.circular(8),
                  boxShadow: [BoxShadow(color: Colors.black12, offset: Offset(4,4))]
              ),
              child: Column(children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    _binanceButton(),
                    _huobiButton(),
                    // _gateioButton(),
                  ],),
                SizedBox(height: 8,),
              ],),
            ),
              content:
              /*Padding(
                padding: const EdgeInsets.all(8.0),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Center(
                      child: Image.asset("assets/expgain/norecord.png",height: 60,width: 80,
                        color: Colors.black.withOpacity(0.6),),
                    ),
                    Center(
                      child: Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: Text('No record found',style: TextStyle(fontSize:16,color:
                        Colors.black.withOpacity(0.5)),),
                      ),
                    ),
                 SizedBox(height: 50,)
                  ],
                ),
              )*/
              Obx(() => Column(children: _createCoinList(0,
                  _currencyController.listBinanceHome.value??[]),)),
            ))

          ],
        ),
    );
  }

  List<Widget> _createCoinList(int index, List<UserCurrencyResponse> curList){
    List<Widget>list = [];
    for (var model in curList) {
      list.add(TradingCell(model: model, onPressed: (){

        CurrencyModel currencyModel = CurrencyModel(symbol: model.symbol,
            priceChangePercent: '${model.priceChange}', lastPrice: '${model.price}',
            lastQty: '${model.quantity}', strategyMode: model.strategyMode);

        Get.toNamed(Routes.TRADE_SETTINGS,
            arguments: [{'model': currencyModel, 'exchangeType': exchangeValue}]);
      },));
    }

    curList.isNotEmpty||_currencyController.isListBinanceHome.value?SizedBox():list.add(Container(
      margin: EdgeInsets.symmetric(horizontal: 24),
      height: 1, child: LinearProgressIndicator(color: ColorConstants.APP_SECONDARY_COLOR,),));
    // list.add(Container(height: 100,));
    curList.isEmpty?list.add(Opacity(opacity: 0.5, child: Image.asset('assets/images/dashboard_watermark.png', width: _size!.width*1.5,),),):SizedBox();
    return list;
  }

  Widget _binanceButton() => GestureDetector(
    child: Container(
      padding: EdgeInsets.symmetric(horizontal: 8, vertical: 4),

      child: Row(children: [
      Image.asset('assets/icons/ic_binance.png', width: 36, height: 36,),
      SizedBox(width: 8,),
      Text('Binance', style: textStyleHeading2(color: Color(0xFFF0B90A)),)
    ],), decoration: BoxDecoration(
        // color: exchangeValue==1?ColorConstants.white12:Colors.transparent,
      borderRadius: BorderRadius.circular(30),
      border: Border.all(width: 1, color: exchangeValue==1?Color(0xFFF0B90A): Colors.transparent)
    ),),
    onTap: (){
      // Get.to(() => APIBinding());
      setState(() {
        exchangeValue = 1;
      });
      controller.getXpertgainBalance();
      EasyLoading.showToast('Switched to Binance');
    },
  );


  Widget _huobiButton() => GestureDetector(
    child: Container(
      padding: EdgeInsets.symmetric(horizontal: 8,vertical: 4),
        decoration: BoxDecoration(
            // color: exchangeValue==2?ColorConstants.white12:Colors.transparent,
            borderRadius: BorderRadius.circular(30),
            border: Border.all(width: 1, color: exchangeValue==2?Color(0xFF1D2242): Colors.transparent)
        ),
      child: Row(children: [
      Image.asset('assets/icons/ic_huobi.png', width: 36, height: 36),
      Text('Huo', style: textStyleHeading2(color: Color(0xFF1D2242)),),
      Text('bi', style: textStyleHeading2(color: Color(0xFF2DA9DE)),),
    ],),),
    onTap: (){
      setState(() {
        exchangeValue = 2;
      });
      controller.getXpertgainBalance();
      EasyLoading.showToast('Switched to Huobi');
    },
  );

  Widget _gateioButton() => GestureDetector(
    child: Row(children: [
      Image.asset('assets/icons/ic_gateio.png', width: 36, height: 36,color: Color(0xFFD95B58),),
      SizedBox(width: 8,),
      Text('Gate io', style: textStyleHeading2(color: Color(0xFFD95B58)),)
    ],),
    onTap: (){
      EasyLoading.showToast('Coming soon...');
    },
  );
}

class TradingCoin extends StatelessWidget {
  final String image;
  final String name;
  // final bool isConfig;
  final Widget configure;
  const TradingCoin({Key? key, required this.image, required this.name, required this.configure}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: EdgeInsets.symmetric(horizontal: 16, vertical: 8),
      padding: EdgeInsets.symmetric(vertical: 8, horizontal: 16),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
        children: [
          Container(
            width: 48, height: 48,
            child: ClipRRect(
              borderRadius: BorderRadius.all(Radius.circular(24)),
              child: Image.asset(image),
            ),
          ),
          SizedBox(width: 16,),

          Expanded(child: Container(
            alignment: Alignment.center,
            child: Text(name.replaceAll('USDT', ''), style: textStyleHeading3(color: ColorConstants.black),),
          )),
          configure,
          SizedBox(width: 8,)
        ],),
      decoration: BoxDecoration(
          gradient: ColorConstants.gradientRev,
          borderRadius: BorderRadius.circular(8),
          // border: Border.all(width: 1, color: Colors.white24)
      ),
    );
  }
}

