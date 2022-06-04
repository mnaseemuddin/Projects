import 'package:flutter/material.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:get/get.dart';

class NewsComment extends StatefulWidget {
  const NewsComment({Key? key}) : super(key: key);

  @override
  _NewsCommentState createState() => _NewsCommentState();
}

class _NewsCommentState extends State<NewsComment> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Comment'.tr, style: textStyleHeading2(color: ColorConstants.white),),
        iconTheme: IconThemeData(color: ColorConstants.white),
        brightness: Brightness.dark,
        elevation: 0,
        backgroundColor: Colors.transparent,
      ),
      body: Center(
        child: NoRecord(),
      ),
    );
  }
}
