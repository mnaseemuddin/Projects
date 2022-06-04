import 'dart:math';
import 'package:flutter/material.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:royal_q/app/shared/shared.dart';


class Manage extends StatefulWidget {
  const Manage({Key? key}) : super(key: key);

  @override
  _ManageState createState() => _ManageState();
}

class _ManageState extends State<Manage> {
  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.symmetric(horizontal: 16),
      child: SACellContainer(child: Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          TextButton.icon(onPressed: (){},
              icon: Icon(AntDesign.addusergroup, color: ColorConstants.APP_SECONDARY_COLOR,),
              label: Text('I joined', style: textStyleLabel(fontSize: 18),)),

          Expanded(child: ListView.builder(itemBuilder: (context, index){
            return Container(
              margin: EdgeInsets.symmetric(vertical: 4),
              child: Row(children: [
                ClipRRect(
                  borderRadius: BorderRadius.circular(8),
                  child: Container(
                    height: 64,
                    width: 64,
                    child: Image(
                      image: NetworkImage(
                        Random().nextInt(100)%2==0?
                        'https://pyxis.nymag.com/v1/imgs/3ae/a5b/e1a1c69441d44c72a86e1120d71f297423-04-mac-miller-2.rvertical.w570.jpg'
                            :'https://static.wikia.nocookie.net/mrbean/images/4/4b/Mr_beans_holiday_ver2.jpg/revision/latest?cb=20181130033425',
                      ),
                      fit: BoxFit.fill,
                    ),
                  ),
                ),
                SizedBox(width: 8,),
                Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                  Text('Captain QQ', style: textStyleHeading2(),),
                  SizedBox(height: 8,),
                  Text('5 updates', style: textStyleLabel(),),
                ],),
                Expanded(child: Container()),
                Icon(Icons.chevron_right, color: ColorConstants.white54,)
              ],),

              decoration: BoxDecoration(
                  border: Border(bottom: BorderSide(width: 1, color: ColorConstants.white10))
              ),
            );
          }, itemCount: 2,))

      ],)),
    );
  }
}
