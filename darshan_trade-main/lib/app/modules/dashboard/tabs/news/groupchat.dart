import 'package:flutter/material.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:get/get.dart';

class GroupChat extends StatefulWidget {
  const GroupChat({Key? key}) : super(key: key);

  @override
  _GroupChatState createState() => _GroupChatState();
}

class _GroupChatState extends State<GroupChat> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Group_Chat'.tr, style: textStyleHeading2(color: ColorConstants.white),),
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
