import 'package:flutter/material.dart';
import 'package:royal_q/app/shared/constants/common.dart';
import 'package:get/get.dart';
import 'package:royal_q/app/shared/shared.dart';

class NewsLike extends StatefulWidget {
  const NewsLike({Key? key}) : super(key: key);

  @override
  _NewsLikeState createState() => _NewsLikeState();
}

class _NewsLikeState extends State<NewsLike> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Like'.tr, style: textStyleHeading2(color: ColorConstants.white),),
        iconTheme: IconThemeData(color: ColorConstants.white),
        elevation: 0,
        backgroundColor: Colors.transparent,
        brightness: Brightness.dark,
      ),
      body: Center(
        child: Text('Under_development'.tr, style: textStyleHeading(),),
      ),
    );
  }
}
