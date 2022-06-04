import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:qr_flutter/qr_flutter.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:royal_q/main.dart';
import 'package:share/share.dart';
import 'package:get/get.dart';

class InviteFriend extends StatefulWidget {
  const InviteFriend({Key? key}) : super(key: key);
  
  @override
  _InviteFriendState createState() => _InviteFriendState();
}

class _InviteFriendState extends State<InviteFriend> {

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    return Stack(children: [
      Container(color: ColorConstants.white,),
      Padding(padding: EdgeInsets.only(top: 50), child: Align(alignment: Alignment.topCenter, child: Image.asset('assets/expgain/inv_img.png', ),),),

      Container(color: ColorConstants.white54,),

      Scaffold(
        appBar: AppBar(
          backgroundColor: Colors.transparent,
          elevation: 0,
          brightness: Brightness.dark,
          title: Text('Invite_Friends'.tr, style: textStyleHeading2(color: ColorConstants.whiteRev),),
          iconTheme: IconThemeData(color: ColorConstants.whiteRev),
        ),
        backgroundColor: Colors.transparent,
        body: Stack(
          alignment: Alignment.center,
          children: [
            Container(child: Center(child: Opacity(opacity: 0.5, child: Image.asset('assets/images/dashboard_watermark.png', width: size.width*1.5,),),),),
            Center(child: Column(
              // mainAxisSize: MainAxisSize.min,
              mainAxisAlignment: MainAxisAlignment.end,
              children: [
                Text('Darshan.Cloud', style: textStyleHeading(color: ColorConstants.whiteRev),),
                SizedBox(height: 8,),
                Text('A Partner of Your Happiness', style: textStyleTitle(color: ColorConstants.whiteRev), textAlign: TextAlign.center,),
                SizedBox(height: 8,),

                QrImage(
                  data: '$BASE/register?invitation=${userInfo!.invitaionCode}',
                  version: QrVersions.auto,
                  size: 200.0,
                ),
                // Image.asset('assets/images/qr_code.png', width: 2.5*size.width/4,),

                SACellContainer(child: GestureDetector(
                  child: Row(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Text('Invitation code:'),
                      SizedBox(width: 8,),
                      Text(userInfo!.invitaionCode),
                      SizedBox(width: 8,),
                      Icon(Ionicons.share)
                    ],),
                  onTap: (){
                    Share.share('$BASE/register?invitation=${userInfo!.invitaionCode}');
                  },
                )),
                Flexible(
                  child: Padding(
                    padding: const EdgeInsets.only(left:20.0,right: 20,top: 10),
                    child: Text('$BASE/register?invitation=${userInfo!.invitaionCode}',
                      textAlign: TextAlign.center,
                      style: textStyleLabel(color: ColorConstants.whiteRev,fontSize: 16),),
                  ),
                ),
                ElevatedButton(
                    onPressed: (){
                      Clipboard.setData(ClipboardData(text: '$BASE/register?invitation=${userInfo!.invitaionCode}'));
                      EasyLoading.showToast('Copied');
                    }, child: Text('Copy recommended URL')),
                SizedBox(height: size.height/12,),
              ],),)
          ],),
      )
    ],);
  }
}

