import 'dart:math';
import 'package:flutter/material.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:royal_q/app/shared/shared.dart';


class Dynamic extends StatefulWidget {
  const Dynamic({Key? key}) : super(key: key);

  @override
  _DynamicState createState() => _DynamicState();
}

class _DynamicState extends State<Dynamic> {

  final List<Image>_images = [
    // Todo eanable for Eezy Trader
    // Image.asset('assets/images/slide1.jpg',fit: BoxFit.fitWidth),
    // Image.asset('assets/images/slide2.jpg',fit: BoxFit.fitWidth),
    Image.asset('assets/images/slide03.jpg', fit: BoxFit.fitWidth),
    Image.asset('assets/images/slide04.jpg', fit: BoxFit.fitWidth),
    Image.asset('assets/images/slide05.jpg', fit: BoxFit.fitWidth),];

  @override
  Widget build(BuildContext context) {
    return NoRecord();
    // return Container(
    //   padding: EdgeInsets.symmetric(horizontal: 16),
    //   child: ListView.builder(itemBuilder: (context, index){
    //     return SACellContainer(
    //         child: Column(
    //           crossAxisAlignment: CrossAxisAlignment.start,
    //           children: [
    //           Row(
    //         crossAxisAlignment: CrossAxisAlignment.start,
    //         mainAxisAlignment: MainAxisAlignment.start,
    //         children: [
    //             ClipRRect(
    //           borderRadius: BorderRadius.circular(24),
    //           child: Container(
    //             height: 48,
    //             width: 48,
    //             child: Image(
    //               image: NetworkImage(
    //                 Random().nextInt(100)%2==0?
    //                 'https://pyxis.nymag.com/v1/imgs/3ae/a5b/e1a1c69441d44c72a86e1120d71f297423-04-mac-miller-2.rvertical.w570.jpg'
    //                     :'https://static.wikia.nocookie.net/mrbean/images/4/4b/Mr_beans_holiday_ver2.jpg/revision/latest?cb=20181130033425',
    //               ),
    //               fit: BoxFit.fill,
    //             ),
    //           ),
    //         ),
    //             SizedBox(width: 8,),
    //             Column(
    //           crossAxisAlignment: CrossAxisAlignment.start,
    //           children: [
    //               Text('Captain QQ', style: textStyleHeading2(),),
    //               SizedBox(height: 8,),
    //               Text('2021-08-25', style: textStyleLabel(),),
    //         ],),
    //             Expanded(child: Container()),
    //             Container(
    //           child: Row(children: [
    //             Icon(MaterialCommunityIcons.diamond_stone, size: 14, color: ColorConstants.APP_SECONDARY_COLOR,),
    //             Text('Essence', style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),)
    //           ],),
    //           padding: EdgeInsets.all(2),
    //           decoration: BoxDecoration(
    //             borderRadius: BorderRadius.circular(4),
    //             border: Border.all(width: 1, color: ColorConstants.APP_SECONDARY_COLOR)
    //           ),
    //         ),
    //             Container(
    //             padding: EdgeInsets.only(left: 8),
    //             child: Icon(Ionicons.chevron_down, color: Colors.white54,),),
    //
    //       ],),
    //           SizedBox(height: 16,),
    //           Padding(padding: EdgeInsets.only(left: 56),
    //           child: Column(
    //           children: [
    //             Text('Questions, Sharing, Help & support ------------------', style: textStyleLabel(fontSize: 16),),
    //             SizedBox(height: 16,),
    //             // Image.asset('assets/images/bg_banner1_en.png', width: double.infinity,fit: BoxFit.fitWidth,),
    //             _images.elementAt(Random().nextInt(_images.length)),
    //             Row(
    //               mainAxisAlignment: MainAxisAlignment.spaceEvenly,
    //               children: [
    //               TextButton.icon(onPressed: (){}, icon: Icon(MaterialCommunityIcons.share_all_outline, color: Colors.white,), label: Text('Share', style: textStyleLabel(fontSize: 14),)),
    //               TextButton.icon(onPressed: (){}, icon: Icon(Fontisto.commenting, color: Colors.white,), label: Text('53', style: textStyleLabel(fontSize: 14),)),
    //               TextButton.icon(onPressed: (){}, icon: Icon(Ionicons.ios_heart_outline, color: Colors.white,), label: Text('39', style: textStyleLabel(fontSize: 14),)),
    //             ],)
    //
    //       ],),)
    //
    //     ],));
    //   }),
    // );
  }
}
