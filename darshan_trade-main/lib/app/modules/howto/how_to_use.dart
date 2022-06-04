import 'dart:async';
import 'package:get/get.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:royal_q/app/shared/constants/colors.dart';
import 'package:royal_q/app/shared/constants/common.dart';
import 'package:royal_q/app/shared/sawidgets/sa_indicator.dart';
import 'package:royal_q/main.dart';

class HowToUse extends StatefulWidget {
  const HowToUse({Key? key}) : super(key: key);

  @override
  _HowToUseState createState() => _HowToUseState();
}

class _HowToUseState extends State<HowToUse> {
  final StreamController<int> _onPageChange = StreamController<int>.broadcast();
  int _current = 0;
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.transparent,
        brightness: Brightness.dark,
        elevation: 0,
      ),
      body: Container(
        padding: EdgeInsets.symmetric(horizontal: 16, vertical: 0),
        child: Column(children: [
          Expanded(child: PageView.builder(
            onPageChanged: (page){
              _current = page;
              _onPageChange.add(page);
            },
            itemBuilder: (context, index){
            return _pages();
          }, itemCount: 5,)),
          Container(child: SAIndicator(onPageChange: _onPageChange, len: 5,),),
          Container(child: Row(children: [
            Expanded(child: Container()),
            RoundedButton(title: 'Skip', onPress: (){
              Get.back();
            },),
            // Expanded(child: Container()),
            // RoundedButton(title: 'Next', onPress: (){
            //
            //   _current +=1;
            //   _onPageChange.add(_current);
            // },)
          ],),),
          SizedBox(height: 16,)
        ],),
      ),
    );
  }



  Widget _pages() => ListView(
    children: [
      Image.asset('assets/expgain/icon_splash.png', height: 72,),
      SizedBox(height: 8,),
      Center(child: Text('How to Use', style: textStyleHeading2(),),),
      SizedBox(height: 8,),
      Text('In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document or a typeface without relying on meaningful content. Lorem ipsum may be used as a placeholder before final copy is available.'
          '\nIn publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document or a typeface without relying on meaningful content. Lorem ipsum may be used as a placeholder before final copy is available.',
        style: textStyleHeading3(),)
    ],
  );
}

class RoundedButton extends StatelessWidget {
  final String title;
  final Function()? onPress;
  const RoundedButton({Key? key, required this.title, this.onPress}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      child: Container(
        alignment: Alignment.center,
        padding: EdgeInsets.symmetric(horizontal: 16, vertical: 4),
        child: Text(title, style: textStyleHeading(),),
        decoration: BoxDecoration(
            gradient: ColorConstants.gradient,
            borderRadius: BorderRadius.circular(16),
            border: Border.all(width: 1, color: ColorConstants.white)
        ),
      ),
      onTap: onPress,
    );
  }
}
