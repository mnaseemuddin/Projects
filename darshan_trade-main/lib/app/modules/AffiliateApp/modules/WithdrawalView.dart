
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:royal_q/app/shared/sawidgets/sa_appbar.dart';
import 'package:royal_q/app/shared/sawidgets/sawidgets.dart';
import 'package:royal_q/app/shared/widgets/affliate_bg.dart';
import 'package:royal_q/app/shared/widgets/app_textbox.dart';

import '../../../shared/constants/colors.dart';
import '../../../shared/constants/common.dart';

class WithdrawalView extends StatefulWidget {
  const WithdrawalView({Key? key}) : super(key: key);

  @override
  _WithdrawalViewState createState() => _WithdrawalViewState();
}

class _WithdrawalViewState extends State<WithdrawalView> {

  var controllerAddress=TextEditingController();

  @override
  void initState() {
    controllerAddress.text="0XASHKKJKLXLL4525LKJJ";
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return AFBg(child: Scaffold(
      backgroundColor: Colors.transparent,
      // appBar: AppBar(
      //   title: Text('Withdrawal', style: textStyleHeading2(color: ColorConstants.white),),
      //   iconTheme: IconThemeData(color: ColorConstants.white),
      //   elevation: 0,
      //   // brightness: Brightness.dark,
      //   systemOverlayStyle: SystemUiOverlayStyle(
      //     systemNavigationBarColor: ColorConstants.APP_PRIMARY_COLOR, // Navigation bar
      //     statusBarColor: ColorConstants.APP_PRIMARY_COLOR, // Status bar
      //   ),
      //   backgroundColor: Colors.transparent,
      //   actions: [
      //     // IconButton(onPressed: (){
      //     //   setState(() => _isVisible = !_isVisible);
      //     // }, icon: Icon(AntDesign.filter))
      //   ],
      // ),
      body: SafeArea(child: Column(children: [
        SHAppbar(title: 'WITHDRAW'),
        SizedBox(height: 16,),
        Container(
          height: 60,
          color: Color(0xFFFF5555),
          child: Padding(
            padding: const EdgeInsets.all(8.0),
            child: Row(
              children: [
                Text("Available Balance",style: textStyleTitle(color: Colors.white,fontSize: 15.0),),Spacer(),
                Text("110.00 ",style: GoogleFonts.rajdhani(
                    fontSize: 22, fontWeight: FontWeight.w700,
                    color: Colors.white),),
                Text("USDT",style: textStyleLabel(fontSize: 10,color: Colors.white),)
              ],
            ),
          ),
        ),


        SizedBox(height: 24,),
        
        AFTextBox(label: 'Amount', hint: 'Enter Amount', isNumber: true,),
        AFTextBox(label: 'Withdrawal Address', hint: 'Enter Amount', textEditingController: controllerAddress,),

        /**
        Padding(
          padding: const EdgeInsets.all(8.0),
          child: Row(
            children: [
              Expanded(
                  flex:30,
                  child: Text("Amount (\$)",style: TextStyle(fontWeight: FontWeight.w500),)),
              Expanded(
                flex: 70,
                child: Container(
                  padding: EdgeInsets.only(left: 8),
                  child: TextField(
                    style: textStyleHeading2(color: ColorConstants.white),

                    keyboardType: TextInputType.numberWithOptions(decimal: true),
                    inputFormatters: [
                      FilteringTextInputFormatter.allow(RegExp(r"[0-9.]")),
                      TextInputFormatter.withFunction((oldValue, newValue) {
                        try {
                          final text = newValue.text;
                          if (text.isNotEmpty) double.parse(text);
                          return newValue;
                        } catch (e) {
                          print(e.toString());
                        }
                        return oldValue;
                      }),
                    ],


                    decoration: InputDecoration(
                      border: InputBorder.none,
                      hintText: 'Enter Amount',
                      hintStyle: TextStyle(color: ColorConstants.white30,fontSize: 15),
                    ),
                  ),
                  decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(8),
                      border: Border.all(color:Color(0xf020202), width: 1)
                  ),
                ),
              ),
            ],
          ),
        ),

        Padding(
          padding: const EdgeInsets.all(8.0),
          child: Row(
            children: [
              Expanded(
                  flex:30,
                  child: Text("Withdrawal Address (\$)",style: TextStyle(fontWeight: FontWeight.w500),)),
              Expanded(
                flex: 70,
                child: Container(
                  padding: EdgeInsets.only(left: 8),
                  child: TextField(

                    controller: controllerAddress,
                    enabled: false,
                    style: textStyleHeading2(color: ColorConstants.white),
                    keyboardType: TextInputType.numberWithOptions(decimal: true),
                    inputFormatters: [
                      FilteringTextInputFormatter.allow(RegExp(r"[0-9.]")),
                      TextInputFormatter.withFunction((oldValue, newValue) {
                        try {
                          final text = newValue.text;
                          if (text.isNotEmpty) double.parse(text);
                          return newValue;
                        } catch (e) {
                          print(e.toString());
                        }
                        return oldValue;
                      }),
                    ],


                    decoration: InputDecoration(
                      border: InputBorder.none,
                      hintText: 'Enter Amount',
                      hintStyle: TextStyle(color: ColorConstants.white30,fontSize: 15),
                    ),
                  ),
                  decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(8),
                      border: Border.all(color:Color(0xf020202), width: 1)
                  ),
                ),
              ),
            ],
          ),
        ),
**/
        Padding(
          padding: const EdgeInsets.all(24.0),
          child: AFSubmitButton(onPressed: (){}, title: "Withdrawal"),
        ),




      ],)),
    ));
  }
}
