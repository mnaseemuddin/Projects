import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:royal_q/app/shared/shared.dart';

class Deposit extends StatefulWidget {
  const Deposit({Key? key}) : super(key: key);

  @override
  _DepositState createState() => _DepositState();
}

class _DepositState extends State<Deposit> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Deposit'.tr, style: textStyleHeading2(color: ColorConstants.white),),
        iconTheme: IconThemeData(color: ColorConstants.white),
        brightness: Brightness.dark,
      elevation: 0, backgroundColor: Colors.transparent,),
    );
  }
}
