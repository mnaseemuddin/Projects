import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:get/get.dart';
import 'package:get_storage/get_storage.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/routes/app_pages.dart';
import 'package:royal_q/app/shared/constants/colors.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:royal_q/main.dart';
import '../controllers/personal_info_controller.dart';
import 'package:image_picker/image_picker.dart';


class PersonalInfoView extends GetView<PersonalInfoController> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Personal_Info'.tr, style: TextStyle(color: ColorConstants.iconTheme),),
        centerTitle: true,
        elevation: 0,
        brightness: Brightness.dark,
        backgroundColor: Colors.transparent,
        iconTheme: IconThemeData(color: ColorConstants.iconTheme),
      ),
      body: Obx(() => ListView(children: [
        SizedBox(height: 24,),
        ItemCell(title: 'Nickname'.tr, child: Row(children: [Text(controller.mUsersInfo.value!.name, style: TextStyle(color: ColorConstants.white54),), SizedBox(width: 4,),Icon(Icons.chevron_right, color: ColorConstants.white54)],),
          onPress: () => controller.changeName(context),),
        ItemCell(title: 'UUID'.tr, child: Row(children: [Text(controller.mUsersInfo.value!.invitaionCode, style: TextStyle(color: ColorConstants.white54),), SizedBox(width: 4,),Icon(Icons.copy, color: ColorConstants.white54)], ), onPress: (){
          Clipboard.setData(ClipboardData(text: controller.mUsersInfo.value!.invitaionCode)).then((value) => EasyLoading.showToast('Copied'));
        },),

        ItemCell(title: 'Rank'.tr, child: Text(controller.mUsersInfo.value!.ranks, style: TextStyle(color: ColorConstants.white54),)),
        ItemCell(title: 'Sponsor'.tr, child: Text(controller.mUsersInfo.value!.referralcode, style: TextStyle(color: ColorConstants.white54),)),

        ItemCell(title: 'Avatar'.tr, child: CircleAvatar(
          backgroundColor: ColorConstants.white,
          child:                     ClipRRect(
            borderRadius: BorderRadius.circular(32),
            child: Container(
              height: 64,
              width: 64,
              child: ClipRRect(
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
            ),
          ),
          // child: Image.network(controller.mUsersInfo.value!.pic.contains('http')?controller.mUsersInfo.value!.pic
          //     :'https://github.com/zmqgithub/profile_app/blob/master/assets/profile_img.jpeg?raw=true'),
          // child: controller.mUsersInfo.value!.pic.length>0?Image.network(controller.mUsersInfo.value!.pic)
          // :Image.asset('assets/images/icon_xg.png'),
        ), onPress: controller.updateDp,),

        ItemCell(title: 'Email'.tr, child: Row(children: [Text(controller.mUsersInfo.value!.email, style: TextStyle(color: ColorConstants.white54),)],), onPress: (){
          // controller.changeEmail(context);
        },),
        ItemCell(title: 'Location'.tr, child: Text(controller.mUsersInfo.value!.countryname, style: TextStyle(color: ColorConstants.white54),)),

        ItemCell(title: 'Change_password'.tr, child: Row(children: [Text('', style: TextStyle(color: ColorConstants.white54),), SizedBox(width: 4,),Icon(Icons.chevron_right, color: ColorConstants.white54)],), onPress: (){
          controller.changePassword(context, true);
        },),

        ItemCell(title: 'Change transaction'.tr, child: Row(children: [Text('', style: TextStyle(color: ColorConstants.white54),), SizedBox(width: 4,),Icon(Icons.chevron_right, color: ColorConstants.white54)],), onPress: (){
          controller.changePassword(context, false);
        },),
        // ItemCell(title: 'Modify_email'.tr, child: Row(children: [Text('', style: TextStyle(color: ColorConstants.white54),), SizedBox(width: 4,),Icon(Icons.chevron_right, color: ColorConstants.white54)],)),

        SizedBox(height: 40,),
        Padding(padding: EdgeInsets.symmetric(horizontal: 24),
          child: SubmitButton(title: 'Logout'.tr, onPressed: () async {
            await GetStorage().write('user_info', null);
            await GetStorage().write("Hedge", false);
            clearAPICache();
            Get.offAllNamed(Routes.LOGIN);
          },),)
      ],)),
    );
  }
}



