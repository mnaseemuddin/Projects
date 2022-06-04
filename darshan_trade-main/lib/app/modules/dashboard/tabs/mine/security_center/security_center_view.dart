import 'package:flutter/material.dart';

import 'package:get/get.dart';
import 'package:get_storage/get_storage.dart';
import 'package:royal_q/app/routes/app_pages.dart';
import 'package:royal_q/app/shared/shared.dart';

import 'security_center_controller.dart';

class SecurityCenterView extends GetView<SecurityCenterController> {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Security_Center'.tr, style: textStyleHeading2(color: ColorConstants.white),),
        iconTheme: IconThemeData(color: ColorConstants.white),
        centerTitle: true,
        elevation: 0,
        brightness: Brightness.dark,
        backgroundColor: Colors.transparent,
      ),
      body: Container(child: ListView(children: [
        ItemCell(title: 'Change_password'.tr, child: Row(children: [Text('', style: TextStyle(color: ColorConstants.white54),), SizedBox(width: 4,),Icon(Icons.chevron_right, color: ColorConstants.white54)],)),
        ItemCell(title: 'Modify_email'.tr, child: Row(children: [Text('', style: TextStyle(color: ColorConstants.white54),), SizedBox(width: 4,),Icon(Icons.chevron_right, color: ColorConstants.white54)],)),
        ItemCell(title: 'Change_transaction_psw'.tr, child: Row(children: [Text('', style: TextStyle(color: ColorConstants.white54),), SizedBox(width: 4,),Icon(Icons.chevron_right, color: ColorConstants.white54)],)),
        ItemCell(title: 'Reset_transaction_psw'.tr, child: Row(children: [Text('', style: TextStyle(color: ColorConstants.white54),), SizedBox(width: 4,),Icon(Icons.chevron_right, color: ColorConstants.white54)],)),
        // ItemCell(title: 'Transaction_password'.tr, child: Row(children: [Text('Not yet set', style: TextStyle(color: ColorConstants.white54),), SizedBox(width: 4,),Icon(Icons.chevron_right, color: ColorConstants.white54)],)),
        // ItemCell(title: 'Google_verification_code'.tr, child: Row(children: [Text('Not yet set', style: TextStyle(color: ColorConstants.white54),), SizedBox(width: 4,),Icon(Icons.chevron_right, color: ColorConstants.white54)],)),

        SizedBox(height: 40,),
        Padding(padding: EdgeInsets.symmetric(horizontal: 24),
          child: SubmitButton(title: 'Logout'.tr, onPressed: () async {
            await GetStorage().write('user_info', null);
            Get.offAllNamed(Routes.LOGIN);
          },),)

      ],),),
    );
  }
}
