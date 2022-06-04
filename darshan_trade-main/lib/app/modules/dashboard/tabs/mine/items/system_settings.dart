import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:get/get.dart';
import 'package:get_storage/get_storage.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/routes/app_pages.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:royal_q/main.dart';
import 'package:url_launcher/url_launcher.dart';

class SystemSettings extends StatefulWidget {
  const SystemSettings({Key? key}) : super(key: key);

  @override
  _SystemSettingsState createState() => _SystemSettingsState();
}

class _SystemSettingsState extends State<SystemSettings> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('System_Settings'.tr, style: textStyleHeading2(color: ColorConstants.white),),
        iconTheme: IconThemeData(color: ColorConstants.white),
        brightness: Brightness.dark,
      elevation: 0, backgroundColor: Colors.transparent, centerTitle: true,),
      body: Container(child: ListView(children: [
        ItemCell(title: 'Firmware_version'.tr, child: Row(children: [Text('V1.14.5', style: TextStyle(color: ColorConstants.white54),), SizedBox(width: 4,),Icon(Icons.chevron_right, color: ColorConstants.white54)],)),
        ItemCell(title: 'Valuation_Method'.tr, child: Row(children: [Text('USD', style: TextStyle(color: ColorConstants.white54),), SizedBox(width: 4,),Icon(Icons.chevron_right, color: ColorConstants.white54)],)),
        ItemCell(title: 'Language'.tr, child: Row(children: [Text('English'.tr, style: TextStyle(color: ColorConstants.white54),), SizedBox(width: 4,),Icon(Icons.chevron_right, color: ColorConstants.white54)],), onPress: (){
          // showDialog(context: context, builder: (context) =>
          //     Dialog(child:Column(
          //         mainAxisSize: MainAxisSize.min,
          //         children: [
          //           SizedBox(height: 8,),
          //           Text('Select_language'.tr, style: textStyleHeading2(),),
          //           ListTile(title: Text('English'), onTap: (){
          //             var locale = Locale('en', 'US');
          //             Get.updateLocale(locale);
          //             Get.back();
          //           },),
          //           ListTile(title: Text('German'), onTap: (){
          //             var locale = Locale('de', 'DE');
          //             Get.updateLocale(locale);
          //             Get.back();
          //           },),
          //         ],
          //     ),)
          // );
          /**
           Get.dialog(
              Container(
              color: Colors.white,
              child: Center(child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
              Text('Select language'),
              ListTile(title: Text('English'),),
              ListTile(title: Text('German'),),
              ],),),
              ));
           **/
        },),
        // ItemCell(title: isExpertGain?'About Xpertgain':'About Eezy Trader',
        ItemCell(title: 'About_name'.trParams({'name': '$APP_NAME'}),
            child: Row(children: [Text('', style: TextStyle(color: ColorConstants.white54),), SizedBox(width: 4,),Icon(Icons.chevron_right, color: ColorConstants.white54)],)
        , onPress: (){
              launch('http://jackbot.cloud/#about');
          },),

        SizedBox(height: 40,),
        Padding(padding: EdgeInsets.symmetric(horizontal: 24),
        child: SubmitButton(title: 'Logout'.tr, onPressed: () async {
          await GetStorage().write('user_info', null);
          clearAPICache();
          Get.offAllNamed(Routes.LOGIN);
        },),)

      ],),),
    );
  }
}
