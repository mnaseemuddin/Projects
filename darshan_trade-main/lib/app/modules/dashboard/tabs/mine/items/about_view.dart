import 'package:flutter/material.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:get/get.dart';
import 'package:url_launcher/url_launcher.dart';


class AboutUs extends StatelessWidget {
  const AboutUs({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    return Scaffold(
      appBar: AppBar(title: Text('Contact_Us'.tr, style: textStyleHeading2(color: ColorConstants.white),),
        iconTheme: IconThemeData(color: ColorConstants.white),
        brightness: Brightness.dark,
        elevation: 0, backgroundColor: Colors.transparent, centerTitle: true,),
      backgroundColor: Color(0xFFF7F7F7),
      body: Stack(children: [
        // Center(child: Image.asset('assets/expgain/contact_us.png'),),
        // Align(alignment: Alignment.bottomCenter,
        // child: Image.asset('assets/expgain/contact_us.png'),),
        Container(
          padding: EdgeInsets.symmetric(horizontal: 8, vertical: 16),
          child: SACellContainer(
              margin: EdgeInsets.all(16),
              child: Column(
                mainAxisSize: MainAxisSize.min,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Row(children: [
                    Expanded(child: Text('Contact_Us'.tr, style: textStyleHeading(),),),
                    TextButton(onPressed: (){
                 //     launch('https://t.me/jbtechsupport');
                    }, child: Text('Support'.tr, style:
                    textStyleHeading(color: ColorConstants.blue),),)
                  ],),
                  SizedBox(height: 16,),
                  Divider(),
                  Text('Contact_Us_desc'.tr, style: textStyleLabel(),),
                  SizedBox(height: 8,),
                  Text('Email_'.trParams({'email': 'Darshantrade2022@gmail.com'}), style: textStyleLabel(),),
                 // Text('Telegram'.trParams({'tel': '@jbtechsupport'}), style: textStyleLabel(),),
                  SizedBox(height: 16,),
                 // Text('direct_contact'.trParams({'name': '@jbtechsupport'}), style: textStyleLabel(),),

                  // TextButton(onPressed: (){
                  //   launch('https://t.me/Jbtofficial');
                  // }, child: Text('JackBOT Official(Telegram)\nhttps://t.me/Jackbotsuper',
                  //   style: TextStyle(color: ColorConstants.blue),)),

                  // Text('https://t.me/Jackbotsuper', style: textStyleLabel(),),

                ],)),
        )
      ],),
    );
  }
}
