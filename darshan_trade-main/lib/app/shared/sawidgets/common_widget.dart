import 'package:flutter/material.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:royal_q/main.dart';
import 'package:get/get.dart';
import '../shared.dart';


BoxDecoration textBg = BoxDecoration(
    color: ColorConstants.white.withOpacity(0.15),
    borderRadius: BorderRadius.circular(8)
);


BoxDecoration textBgGradient = BoxDecoration(
    gradient: LinearGradient(colors: [
      Color(0xFF292F33),
      Color(0xFF232D35),
    ]),
    border: Border.all(color: Colors.white12),
    borderRadius: BorderRadius.only(
      topLeft: Radius.circular(20),
      topRight: Radius.circular(8),
      bottomLeft: Radius.circular(8),
      bottomRight: Radius.circular(20),
    ),
    // image: DecorationImage(image: AssetImage('assets/expgain/background_gradient.png'), fit: BoxFit.fill)
);



BoxDecoration textBgWhite = BoxDecoration(
    color: ColorConstants.whiteRev,
    borderRadius: BorderRadius.circular(8)
);

Container SACellContainer({required Widget child, EdgeInsets padding=const EdgeInsets.all(16),
EdgeInsets margin=const EdgeInsets.only(bottom: 4, top: 4), Color? color, double? width, double? 
height, AlignmentGeometry? alignment}) => Container(
  width: width,
  height: height,
  padding: padding,
  margin: margin,
  child: child,
  alignment: alignment,
  decoration: BoxDecoration(
      // gradient: ColorConstants.gradient,
      color: color??ColorConstants.whiteRev,//.withOpacity(0.5),
      borderRadius: BorderRadius.circular(8)
  ),
);

Container SACellContainerImage({required Widget child, EdgeInsets padding=const EdgeInsets.all(16),
  EdgeInsets margin=const EdgeInsets.only(bottom: 4, top: 4), Color? color, double? width, double? height, AlignmentGeometry? alignment, image}) => Container(
  width: width,
  height: height,
  padding: padding,
  margin: margin,
  child: child,
  alignment: alignment,
  decoration: BoxDecoration(
    // gradient: ColorConstants.gradient,
    //   color: color??ColorConstants.whiteRev,//.withOpacity(0.5),
      borderRadius: BorderRadius.circular(8),
      image: DecorationImage(image: AssetImage('assets/expgain/member_robot.jpg'), fit: BoxFit.fitWidth)
  ),
);

Container SACellContainerGradient({required Widget child, EdgeInsets padding=const EdgeInsets.all(16),
  EdgeInsets margin=const EdgeInsets.only(bottom: 4, top: 4), Gradient? gradient}) => Container(
  padding: padding,
  margin: margin,
  // height: 130,
  child: child,
  decoration: BoxDecoration(
      gradient: gradient??ColorConstants.gradientRev,
      // color: Colors.white10.withOpacity(0.05),
      borderRadius: BorderRadius.circular(8)
  ),
);

Container NoRecord() => Container(child: Center(
  child: Column(
    mainAxisSize: MainAxisSize.min,
    children: [
    Icon(MaterialCommunityIcons.file_alert_outline, color: ColorConstants.white54, size: 100,),
    // Icon(FontAwesome.file_code_o, color: Colors.white54, size: 100,),
    Container(margin: EdgeInsets.all(4), height: 1, width: 120, color: ColorConstants.white54,),
    Container(margin: EdgeInsets.all(4), height: 1, width: 100, color: ColorConstants.white54,),
    Text('No_Records'.tr, style: textStyleLabel(color: ColorConstants.white54))
  ],),
),);





Container AFNoRecord() => Container(child: Center(
  child: Column(
    mainAxisSize: MainAxisSize.min,
    children: [
    Icon(MaterialCommunityIcons.file_alert_outline, color: Colors.white54, size: 100,),
    // Icon(FontAwesome.file_code_o, color: Colors.white54, size: 100,),
    Container(margin: EdgeInsets.all(4), height: 1, width: 120, color: Colors.white54,),
    Container(margin: EdgeInsets.all(4), height: 1, width: 100, color: Colors.white54,),
    Text('No_Records'.tr, style: textStyleLabel(color: Colors.white54))
  ],),
),);

class ItemCell extends StatelessWidget {
  final String title;
  final Widget child;
  final Function()? onPress;
  const ItemCell({Key? key, required this.title, required this.child, this.onPress}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(child: Container(
      padding: EdgeInsets.symmetric(vertical: 8, horizontal: 16),
      child: Column(children: [
        Row(children: [
          Expanded(child: Text(title, style: TextStyle(color: ColorConstants.white),)),
          child
        ],),
        Divider(color: ColorConstants.white10,)
      ],),
    ), onTap: onPress,);
  }
}