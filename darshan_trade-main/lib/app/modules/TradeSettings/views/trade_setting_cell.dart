import 'package:flutter/material.dart';
import 'package:royal_q/app/shared/constants/common.dart';
import 'package:royal_q/app/shared/shared.dart';

class TradeSettingCell extends StatelessWidget {
  final String title;
  final Widget? subView;
  final Widget? trailing;
  final Function()? onPressed;
  final bool isDivider;
  const TradeSettingCell({Key? key, required this.title, this.subView, this.trailing, this.onPressed, this.isDivider=true}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      child: Container(
        // padding: EdgeInsets.symmetric(vertical: 8),

        child: Column(children: [
          Row(children: [
            Expanded(child: Text(title, style: textStyleLabel(),)),
            subView??SizedBox(),
            SizedBox(width: 8,),
            trailing??SizedBox()
          ],),
          isDivider?Divider(color: ColorConstants.white,):SizedBox()
        ],),
      ),
      onTap: onPressed,
    );
  }
}
