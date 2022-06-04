import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:royal_q/app/shared/shared.dart';

import 'test_controller.dart';

class TestView extends  GetView<TestController>  {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('test', style: textStyleHeading2(color: ColorConstants.white),),
        iconTheme: IconThemeData(color: ColorConstants.white),
      ), 
      body: Container(),
    );
  }
}
  