import 'package:flutter/material.dart';

import 'package:get/get.dart';
import 'package:royal_q/app/shared/shared.dart';

import 'controllers/about.controller.dart';

class AboutScreen extends GetView<AboutController> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('About Screen', style: textStyleHeading2(color: ColorConstants.white),),
        iconTheme: IconThemeData(color: ColorConstants.white),
        centerTitle: true,
        brightness: Brightness.dark,
      ),
      body: Center(
        child: Text(
          'AboutScreen is working',
          style: TextStyle(fontSize: 20),
        ),
      ),
    );
  }
}
