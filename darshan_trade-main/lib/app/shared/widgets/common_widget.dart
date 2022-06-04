import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
// import 'package:fluttertoast/fluttertoast.dart';
import 'package:royal_q/app/shared/shared.dart';

class CommonWidget {
  static AppBar appBar(

      BuildContext context, String title, IconData? backIcon, Color color,
      {void Function()? callback}) {
    return AppBar(
      leading: backIcon == null
          ? null
          : IconButton(
              icon: Icon(backIcon, color: color),
              onPressed: () {
                if (callback != null) {
                  callback();
                } else {
                  Navigator.pop(context);
                }
              },
            ),
      centerTitle: true,
      title: Text(
        title,
        style: TextStyle(color: color, fontFamily: 'Rubik'),
      ),
      backgroundColor: Colors.transparent,
      elevation: 0.0,
    );
  }

  static SizedBox rowHeight({double height = 30}) {
    return SizedBox(height: height);
  }

  static SizedBox rowWidth({double width = 30}) {
    return SizedBox(width: width);
  }

  static void toast(String error) async {
    await EasyLoading.showToast(error);
        // toastLength: Toast.LENGTH_SHORT,
        // gravity: ToastGravity.BOTTOM,
        // timeInSecForIosWeb: 3,
        // backgroundColor: Colors.white,
        // textColor: Colors.black,
        // fontSize: 16.0
    // );
  }
}

class ItemView extends StatelessWidget {
  final String title;
  final Widget child;
  final EdgeInsets padding;
  const ItemView({Key? key, required this.title, required this.child, this.padding=const EdgeInsets.all(0)}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 60,
      child: Row(children: [
        Expanded(child: Text(title, style: textStyleHeading(color: ColorConstants.white),),flex: 30,),
        Container(width: 0.5, color: ColorConstants.APP_SECONDARY_COLOR.withOpacity(0.2), margin: EdgeInsets.all(4),),
        Expanded(child: child,flex: 70,),
      ],),
      decoration: BoxDecoration(
          border: Border(bottom: BorderSide(color: ColorConstants.APP_SECONDARY_COLOR.withOpacity(0.1), width: 1))
      ),
    );
  }
}