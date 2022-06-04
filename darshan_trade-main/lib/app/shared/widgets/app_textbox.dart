import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:get/get.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:royal_q/app/modules/AffiliateApp/model/StakeResponse.dart';

class AFTextBoxController extends GetxController{
  final isSelected = false.obs;
}

class AFTextBox extends GetView<AFTextBoxController> {
  final String label;
  final Function(String)?onChanged;
  final Function()?onEditingComplete;
  final TextEditingController? textEditingController;
  final bool obscureText;
  final Widget? prefix;
  final Widget? suffix;
  final Widget? suffixSec;
  final bool isNumber;
  final String? hint;
  final bool enable;
  const AFTextBox({Key? key, required this.label, this.onChanged, this.onEditingComplete, this.textEditingController, this.obscureText=false, this.prefix, this.suffix, this.isNumber=false, this.hint, this.enable=true, this.suffixSec, }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    Get.isRegistered<AFTextBoxController>()?Get.find<AFTextBoxController>():Get.put(AFTextBoxController());
    return Container(
      margin: EdgeInsets.symmetric(horizontal: 8, vertical: 8),
      padding: EdgeInsets.symmetric(horizontal: 8, vertical: 0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          SizedBox(height: 8,),
          Padding(padding: EdgeInsets.symmetric(horizontal: 8, vertical: 0),
            child: Text(label, style: GoogleFonts.roboto(
                color: Colors.white70, fontSize: 18, fontWeight: FontWeight.w400
            ),),),

          Divider(color: Colors.white24,),

          Container(
            // padding: EdgeInsets.only(left: 16, right: 4, top: suffix!=null?16:0),
            padding: EdgeInsets.only(left: 16, right: 4, top: suffix!=null?0:0),
            height: 50,
            width: double.infinity,
            alignment: Alignment.center,
            child: Obx(() => TextFormField(
              controller: textEditingController,
              onChanged: onChanged,
              onEditingComplete: onEditingComplete,
              obscureText: controller.isSelected.value?false:obscureText,
              style: TextStyle(color: Colors.white),
              cursorColor: Colors.white,
              enabled: enable,
              keyboardType: isNumber?TextInputType.numberWithOptions(decimal: true):null,
              inputFormatters: isNumber?[
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
              ]:null,

              decoration: InputDecoration(
                  border: InputBorder.none,
                  hintText: hint??label,
                  prefix: prefix,
                  suffix: Obx(() => controller.isSelected.value?GestureDetector(
                    child: suffix??SizedBox(),
                    onTap: () => controller.isSelected.value=false,
                  ):GestureDetector(
                    child: suffixSec??SizedBox(),
                    onTap: () => controller.isSelected.value=true,
                  )),

                  hintStyle: GoogleFonts.roboto(
                      fontSize: 12, color: Colors.white24
                  )
              ),
            )),

            decoration: BoxDecoration(
                borderRadius: BorderRadius.circular(8),
                // border: Border.all(color: Colors.black, width: 1)
            ),
          )

        ],),

      decoration: BoxDecoration(gradient: LinearGradient(colors: [
        Color(0xFF292F33),
        Color(0xFF232D35),
      ]),
        borderRadius: BorderRadius.circular(8),
        border: Border.all(color: Colors.white12),
        boxShadow: [BoxShadow(color: Colors.black45, offset: Offset(4, 4), blurRadius: 6)]
      ),

    );
  }
}



class AFDropBox extends StatelessWidget {
  final String label;
  final String? hint;
  final Function() onTap;
  final StakeResponse stakeModel;
  const AFDropBox({Key? key, required this.label, this.hint, required this.stakeModel, required this.onTap, }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: EdgeInsets.symmetric(horizontal: 24, vertical: 8),
      padding: EdgeInsets.symmetric(horizontal: 8, vertical: 0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          SizedBox(height: 8,),
          Padding(padding: EdgeInsets.symmetric(horizontal: 8, vertical: 0),
            child: Text(label, style: GoogleFonts.roboto(
                color: Colors.white70, fontSize: 18, fontWeight: FontWeight.w400
            ),),),

          Divider(color: Colors.white24,),

          GestureDetector(child: Container(
            padding: EdgeInsets.only(left: 16, right: 4, top: 0),
            height: 50,
            width: double.infinity,
            alignment: Alignment.center,
            child: Row(children: [
              Text(hint??'', style: GoogleFonts.roboto(
                  color: Colors.white70, fontSize: 18, fontWeight: FontWeight.w400
              )),
              Spacer(),
              IconButton(onPressed: null, icon: Icon(Icons.arrow_forward_ios, color: Colors.white,))
            ],),

            decoration: BoxDecoration(
              borderRadius: BorderRadius.circular(8),
            ),
          ), onTap: onTap,)

        ],),

      decoration: BoxDecoration(gradient: LinearGradient(colors: [
        Color(0xFF292F33),
        Color(0xFF232D35),
      ]),
        borderRadius: BorderRadius.circular(8),
        border: Border.all(color: Colors.white12),
        boxShadow: [BoxShadow(color: Colors.black45, offset: Offset(4, 4), blurRadius: 6)]
      ),

    );
  }
}