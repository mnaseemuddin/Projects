import 'dart:async';
import 'dart:math';

import 'package:carousel_slider/carousel_slider.dart';
import 'package:flutter/material.dart';
import 'package:flutter_custom_clippers/flutter_custom_clippers.dart';
import 'package:get/get.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/models/response/members_response.dart';
import 'package:royal_q/app/shared/constants/colors.dart';
import 'package:royal_q/app/shared/constants/common.dart';
import 'package:royal_q/app/shared/sawidgets/common_widget.dart';
import 'package:royal_q/app/shared/sawidgets/sa_indicator.dart';
import 'package:royal_q/main.dart';
import 'member_center_controller.dart';
import 'package:collection/collection.dart';

class MemberCenterView extends  GetView<MemberCenterController>  {

  final double padding = 24;
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Member_Center'.tr, style: textStyleHeading2(color: ColorConstants.white),),
        iconTheme: IconThemeData(color: ColorConstants.white),
        backgroundColor: ColorConstants.APP_PRIMARY_COLOR,
        elevation: 0,
        centerTitle: true,
      ),
      backgroundColor: ColorConstants.whiteRev,
      body: Container(
        child: ListView(children: [
          Container(height: 200, color: ColorConstants.APP_PRIMARY_COLOR,
          child: Stack(
            alignment: Alignment.bottomCenter,
            children: [
            Obx(() => controller.memberData.value!=null?PageView(

              controller: PageController(
                viewportFraction: 0.9,
                initialPage: 0,
              ),
              allowImplicitScrolling: true,
              onPageChanged: (page){
                controller.listElement.value = controller.membersResponse.elementAt(page).list;
              },
              children: controller.membersResponse.map((element) =>
                  CardView(member: element, isActive: element.start1=='1',)).toList()

              ,scrollDirection: Axis.horizontal,):Center(child: CircularProgressIndicator(),)),

            RotatedBox(quarterTurns: 2, child: ClipPath(
              clipper: BottomBorderClipper(),
              child: Container(
                padding: EdgeInsets.only(top: 8),
                color: ColorConstants.whiteRev,
                width: double.infinity,
                height: 50,
                alignment: Alignment.topCenter,
              ),
            ),),

          ],),),

          Align(alignment: Alignment.center,
            child: Text('VIP_Membership_right'.tr, style: textStyleHeading(color: ColorConstants.black),),),
          SizedBox(height: 16,),

          Obx(() => Column(children: controller.listElement.mapIndexed((index, element) =>
              RewardView(element: element,),).toList(),)),
          SizedBox(height: 16,),
          Container(height: 16, color: ColorConstants.APP_PRIMARY_COLOR,),
          Container(
            padding: EdgeInsets.symmetric(horizontal: 24, vertical: 16),
              color: ColorConstants.APP_PRIMARY_COLOR.withOpacity(0.1),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                Align(alignment: Alignment.center,
                  child: Text('Member_use_rules'.tr, style: textStyleHeading2(color: ColorConstants.black),),),
                SizedBox(height: 16,),
                Text('Direct_push'.tr ,style: textStyleLabel(color: Colors.black54),),
                Text('final_interp'.tr ,style: textStyleLabel(color: Colors.black54),),
                SizedBox(height: 50,)
                ],))
        ],),
      ),
    );
  }
}

class CardView extends GetView<MemberCenterController> {
  final MembersResponse member;
  final bool isActive;
  const CardView({Key? key, required this.member, this.isActive=false,}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    return Stack(children: [
      Container(
        margin: EdgeInsets.all(8),
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(16),
          image: DecorationImage(image: AssetImage('assets/expgain/member_robot.jpg'), fit: BoxFit.fitWidth)
        ),
      ),
      Container(
        margin: EdgeInsets.all(8),
        decoration: BoxDecoration(
            borderRadius: BorderRadius.circular(16),
            color: Color(int.parse('0xFF${member.color}')).withOpacity(0.9),
        ),
      ),

      SACellContainer(
          color: Colors.transparent,
          margin: EdgeInsets.all(8),
          child: Column(children: [
            Row(children: [
              ClipRRect(
                borderRadius: BorderRadius.circular(32),
                child: Container(
                  height: 64,
                  width: 64,
                  child: userInfo!.pic.isNotEmpty?Image(
                    image: NetworkImage('$BASE/DP/${userInfo!.pic}'),
                    fit: BoxFit.fill,
                  ):Image(
                    image: AssetImage('assets/expgain/icon.png'),
                    fit: BoxFit.fill,
                  ),
                ),
              ),
              SizedBox(width: 8,),
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Obx(() => Text(controller.memberData.value?.username??'', style: textStyleHeading2(),)),
                  SizedBox(height: 4,),
                  isActive?Obx(() => Text(controller.memberData.value?.activationtime??'', style: textStyleLabel(),)):SizedBox(),
                  isActive?Obx(() => Text('Remaining ${controller.memberData.value?.remaindays??'365'} Days', style: textStyleLabel(),)):SizedBox(),
                  // Text('Turn_on_automatic_renewal'.tr, style: textStyleCaption(color: ColorConstants.APP_SECONDARY_COLOR),),
                ],),
              Expanded(child: SizedBox()),
              isActive?GestureDetector(
                child: Container(
                  alignment: Alignment.center,
                  padding: EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                  child: Text('Renew'.tr, style: textStyleLabel(color: ColorConstants.whiteRev),),
                  decoration: BoxDecoration(
                      color: ColorConstants.white54,
                      borderRadius: BorderRadius.circular(20),
                      border: Border.all(width: 1, color: Colors.white)
                  ),
                ),
                onTap: (){
                  controller.showActivationDialog(context, ActivationDialog());
                },
              ):SizedBox()
            ],),
            SizedBox(height: 16,),
            Row(children: [
              SizedBox(width: 32,),
              Text('Star'+member.start1),
              SizedBox(width: 8,),
              Expanded(child: Divider(color: ColorConstants.white,)),
              SizedBox(width: 8,),
              Text('Star'+member.start2),
            ],),
            Padding(padding: EdgeInsets.symmetric(horizontal: 16),
              child: Align(alignment: Alignment.center, child: Text(member.cond, style: textStyleCaption(fontSize: 11),),),)

          ],))

    ],);
  }
}

class RewardView extends StatelessWidget {
  final ListElement element;
  const RewardView({Key? key, required this.element,}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Row(children: [
      SizedBox(width: 16,),
      SACellContainer(
        margin: EdgeInsets.all(8),
        color: Color(0xFFF6E6CB),
          padding: EdgeInsets.all(8),
          child: Image.asset('assets/icons/${element.icon}', width: 36, color: Color(0xFF966C3B),)
      ),
      Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(element.heading, style: textStyleHeading2(color: ColorConstants.black),),
          SizedBox(height: 4,),
          Text(element.content, style: textStyleLabel(color: ColorConstants.black),)
        ],)
    ],);
  }
}

class BottomBorderClipper extends CustomClipper<Path> {

  @override
  Path getClip(Size size) {
    Path path = Path();
    path.lineTo(0, size.height);
    path.arcToPoint(Offset(size.width, size.height),
        radius: Radius.elliptical(30, 5));
    path.lineTo(size.width, 0);
    return path;
  }

  @override
  bool shouldReclip(covariant CustomClipper<Path> oldClipper) {
    // TODO: implement shouldReclip
    return true;
  }
}

class ActivationDialog extends GetView<MemberCenterController> {

  const ActivationDialog({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Dialog(
      backgroundColor: ColorConstants.whiteRev,
      child: Container(
        padding: EdgeInsets.symmetric(horizontal: 16, vertical: 16),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            SizedBox(height: 16,),
            Text('Total: 50 USDT', style: textStyleHeading2(color: ColorConstants.black),),
            SizedBox(height: 16,),
            Row(children: [
              Obx(() => controller.isAccepted.value?IconButton(onPressed: (){
                controller.isAccepted.value = false;
              }, icon: Icon(Icons.check_box, color: ColorConstants.black,))
                  :IconButton(onPressed: (){
                controller.isAccepted.value = true;
              }, icon: Icon(Icons.check_box_outline_blank, color: ColorConstants.black,))),
              // Expanded(child: Text('I have agreed to ${isExpertGain?'Expertgain':'Eezy Trader'} User Activation Service Agreement.', style: textStyleLabel(color: ColorConstants.black),)),
              Expanded(child: Text('ageed_to'.trParams({'name': 'Darshan'}), style: textStyleLabel(color: ColorConstants.black),)),
            ],),

            Divider(color: ColorConstants.black,),
            Row(children: [
              Expanded(child: TextButton(onPressed: (){
                Get.back();
              }, child: Text('Cancel'.tr, style: textStyleLabel(color: ColorConstants.black),))),
              Container(width: 0.5, height: 40, color: ColorConstants.black,),
              Expanded(child: TextButton(onPressed: (){
                controller.renewUser();
              }, child: Text('Renew'.tr, style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),))),
            ],)

          ],),
      ),
    );
  }
}
