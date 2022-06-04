import 'dart:math';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:get/get.dart';
import 'package:get_storage/get_storage.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/modules/dashboard/controllers/dashboard_controller.dart';
import 'package:royal_q/app/modules/dashboard/tabs/mine/items/about_view.dart';
import 'package:royal_q/app/modules/dashboard/tabs/mine/items/assets_detail.dart';
import 'package:royal_q/app/modules/dashboard/tabs/mine/items/system_settings.dart';
import 'package:royal_q/app/modules/dashboard/tabs/mine/security_center/security_center_view.dart';
import 'package:royal_q/app/routes/app_pages.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:royal_q/main.dart';
import '../tabs.dart';


class MineDashboard extends StatefulWidget {
  const MineDashboard({Key? key}) : super(key: key);

  @override
  _MineDashboardState createState() => _MineDashboardState();
}

class _MineDashboardState extends State<MineDashboard> {
  bool _switchValue=true;
  final DashboardController _controller = Get.find<DashboardController>();
  @override
  void initState() {
    super.initState();
    _controller.getFundBalance();
  }
  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    return Container(
      // color: const Color(0xFFF87E02),
      color: ColorConstants.grey,
      child: SafeArea(child: Container(
        color: ColorConstants.APP_PRIMARY_COLOR,
        // color: ColorConstants.grey,
        child: ListView(children: [
          Container(
            padding: EdgeInsets.only(left: 16, right: 16, top: 16),
            child: Column(children: [
              GestureDetector(child: Row(children: [
                ClipRRect(
                  borderRadius: BorderRadius.circular(32),
                  child: Container(
                    height: 64,
                    width: 64,
                    child: userInfo!.pic.isNotEmpty?Image(
                      image: NetworkImage('$BASE/DP/${userInfo!.pic}'),
                      fit: BoxFit.fill,
                    ):Image(
                      // image: NetworkImage('https://github.com/zmqgithub/profile_app/blob/master/assets/profile_img.jpeg?raw=true'),
                      image: AssetImage('assets/expgain/icon_splash.png'),
                      fit: BoxFit.fill,
                    ),
                  ),
                ),
                SizedBox(width: 8,),
                Expanded(child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text('${userInfo?.name}', style: textStyleHeading(color: ColorConstants.whiteRev),),
                    SizedBox(height: 8,),
                    Text('${userInfo?.email}', style: textStyleLabel(color: ColorConstants.whiteRev),),
                  ],)),
                IconButton(onPressed: null, icon: Icon(Icons.chevron_right,
                  color: ColorConstants.white70Rev, size: 36,))
              ],),
              onTap: () => Get.toNamed(Routes.PERSONAL_INFO)!.then((value){
                setState(() {});
              }),),
              SizedBox(height: 32,),
              Container(

                child: Row(children: [
                  Row(children: [
                    Text('VIP'.tr, style: textStyleHeading(),),
                    SizedBox(width: 8,),
                    Text('to Darshan'.tr, style: textStyleLabel(),),
                  ],),

                  Expanded(child: SizedBox()),

                  userInfo!.ispaid==0?GestureDetector(child: Container(
                    padding: EdgeInsets.symmetric(vertical: 6, horizontal: 16),
                    alignment: Alignment.center,
                    child: Text('Activate'.tr, style: textStyleLabel(color: const Color(0xFFF7C154)),),
                    decoration: BoxDecoration(color: ColorConstants.white,
                      borderRadius: BorderRadius.circular(20),
                    ),
                  ), onTap: (){
                    _confirmActivation();
                  },):
                      Row(children: [
                        Text('Activated'.tr, style: textStyleLabel(),),
                        Icon(Icons.check, color: ColorConstants.blue,)
                      ],)
                ],),

                margin: EdgeInsets.symmetric(horizontal: 8),
                padding: EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                decoration: BoxDecoration(
                    borderRadius: BorderRadius.only(topLeft: Radius.circular(16),
                        topRight: Radius.circular(16)),
                    gradient: LinearGradient(
                        colors: [const Color(0xFFFCCB68),
                      const Color(0xFFF7C154)],
                        begin: Alignment.topRight, end: Alignment.bottomRight)
                ),
              )
            ],),

            decoration: BoxDecoration(
              gradient: LinearGradient(colors: [Color(0xFFFF5400),
                Color(0xFF8b009e),Color(0xff1f0fa1),
                ]),
                color: ColorConstants.APP_SECONDARY_COLOR,
                // gradient: LinearGradient(colors: [const Color(0xFFF87E02), const Color(0xFF1C419F)],
                //     begin: Alignment.topCenter, end: Alignment.bottomCenter)
            ),
          ),

          // MineCell(icon: MaterialCommunityIcons.hexagon_slice_1, title: 'Professional_addition'.tr,
          //   trailing: CupertinoSwitch(
          //     value: _switchValue,
          //     onChanged: (value) {
          //       setState(() {
          //         _switchValue = value;
          //       });
          //     },
          //   ),),
          MineCell(icon: MaterialCommunityIcons.diamond_outline,
            title: 'Member_center'.tr, onPressed: (){
            Get.toNamed(Routes.MEMBER_CENTER);
          },),
          MineCell(icon: Fontisto.money_symbol, title: 'Asset'.tr, onPressed: (){
            Get.to(AssetsDetail());
          }, subview: Row(
            children: [
              Obx(() => Text(_controller.balanceFund.value.toStringAsFixed(3), style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR))),
              Text(' USD', style: textStyleCaption(color: ColorConstants.white)),
            ],)
          // FutureBuilder(
          //     future: getFundBalanceAPI(),
          //     builder: (context, AsyncSnapshot<ApiResponse>snapshot){
          //       if(!snapshot.hasData)return Text(0.000.toStringAsFixed(3), style: textStyleCaption(color: ColorConstants.white));
          //       ApiResponse resp = snapshot.data!;
          //       double _asset = double.parse(resp.data['totalAmount']??'0.0');
          //       return Row(
          //         children: [
          //         Text(_asset.toStringAsFixed(3), style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR)),
          //         Text('USD', style: textStyleCaption(color: ColorConstants.white)),
          //       ],);
          //     }),
          ),
          MineCell(icon: Octicons.link, title: 'API_Binding'.tr, onPressed: () => Get.to(APIBinding()),),
          // MineCell(icon: Ionicons.ios_receipt_outline, title: 'Billing_Details'.tr, onPressed: () => Get.to(() => TransactionRecord(exchangeType: 1,)),),
          MineCell(icon: Ionicons.ios_receipt_outline, title: 'Billing_Details'.tr, onPressed: () =>
              Get.toNamed(Routes.BILLING_DETAIL),),
          MineCell(icon: Ionicons.ios_receipt_outline, title: 'Reward_details_report'.tr, onPressed: () => Get.toNamed(Routes.REWARD_DETAIL_REPORT),),
          MineCell(icon: AntDesign.team, title: 'Team', onPressed: () => Get.toNamed(Routes.TEAM),),
          MineCell(icon: Ionicons.book_outline, title: 'User Guide',onPressed: (){
            EasyLoading.showToast('Coming soon');
          },),
          // MineCell(icon: MaterialCommunityIcons.account_details_outline, title: 'Community'.tr, onPressed: () => Get.toNamed(Routes.REWARD_DETAILS),),
          // MineCell(icon: MaterialCommunityIcons.account_details_outline,
          //   title: 'Community'.tr, onPressed: () => Get.toNamed(Routes.COMMUNITY),),
          // MineCell(icon: Feather.pie_chart, title: 'Circle owner\'s income details', onPressed: (){},),
          // MineCell(icon: Ionicons.sync_circle_outline, title: 'My synchronize strategy', onPressed: (){},),
          MineCell(icon: Feather.share, title: 'Share'.tr, onPressed: () =>
              EasyLoading.showToast('Coming soon')
              //Get.to(InviteFriend()),
          ),
          // MineCell(icon: MaterialCommunityIcons.headset, title: isExpertGain?'Xpertgain feedback':'EeasyTrader feedback', onPressed: (){},),

          // MineCell(icon: MaterialCommunityIcons.security, title: 'Security_center'.tr, onPressed: () => Get.toNamed(Routes.SECURITY_CENTER),),

          MineCell(icon: Ionicons.ios_settings_outline, title: 'System_Settings'.tr,
            onPressed: () => Get.to(SystemSettings()),),
          MineCell(icon: Icons.account_box_outlined, title: 'Contact_Us'.tr,
            onPressed: () => Get.to(AboutUs()),),

        ],),
      )),
    );
  }

  _confirmActivation(){
    showDialog(context: context, builder: (context) => Dialog(
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          SizedBox(height: 16,),
          Row(children: [
            SizedBox(width: 16,),
            Text('Activate')
          ],),
          SizedBox(height: 16,),
          Text('Are you sure want to activate?'),
          SizedBox(width: 16,),
          Row(children: [
            Expanded(child: SizedBox()),
            TextButton(onPressed: () => Get.back(), child: Text('Cancel')),
            TextButton(onPressed: _activationIDAPI, child: Text('Confirm')),
          ],)
        ],),
    ));
  }

  _activationIDAPI(){
    Get.back();
    EasyLoading.show(status: 'Requesting...');
    activationIDAPI().then((value){
      EasyLoading.dismiss();
      ApiResponse response = value;
      EasyLoading.showToast(response.data['message']);
      if(mounted) {
        setState((){
          userInfo!.ispaid = 1;
        });
      }
      // if(value.status){
      //   getProfileAPI(userInfo!.id).then((value){
      //     userInfo = value.data;
      //     if(mounted) {
      //       setState((){
      //         userInfo!.ispaid = 1;
      //         GetStorage().write('user_info', value);
      //       });
      //     }
      //   });
      // }
    });
  }
}


