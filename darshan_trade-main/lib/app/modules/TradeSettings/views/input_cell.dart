import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:royal_q/app/shared/shared.dart';

class InputCell extends StatelessWidget {
  final TextEditingController controller;
  final Function(String)?onChanged;
  final Function(String)?onContChanged;
  final bool isInt;
  final TextAlign align;
  final int minVal;
  final bool isOnComplete;
  const InputCell({Key? key, required this.controller, this.onChanged,
    this.isInt=false, this.align=TextAlign.right, this.minVal=0, this.isOnComplete=false, this.onContChanged}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    return Expanded(child: TextFormField(
      controller: controller,
      onChanged: (val){
        if(onContChanged!=null)
        onContChanged!(val);

        if(!isOnComplete){
          if(onChanged !=null){
            String value = controller.text !='.'&&controller.text.isNotEmpty?controller.text:'$minVal';
            if(isInt){
              int temp = int.parse(value);
              onChanged!(temp<minVal?'$minVal':value);
            }else{
              double temp = double.parse(value);
              onChanged!(temp<minVal?'$minVal':value);
            }
          }
        }
      },
      onEditingComplete: (){
        String value = controller.text !='.'&&controller.text.isNotEmpty?controller.text:'$minVal';
        if(onChanged !=null){
          if(isInt){
            int temp = int.parse(value);
            String r_val = temp<minVal?'$minVal':value;
            onChanged!(r_val);
            controller.text = r_val;
          }else{
            double temp = double.parse(value);
            String r_val = temp<minVal?'$minVal':value;
            onChanged!(r_val);
            controller.text = r_val;
          }
        }
        AppFocus.unfocus(context);
      },
      style: textStyleLabel(),
      textAlign: align,
      cursorColor: ColorConstants.APP_SECONDARY_COLOR,
      inputFormatters: isInt?[FilteringTextInputFormatter.digitsOnly]:[
        FilteringTextInputFormatter.allow(RegExp(r'^(\d+)?\.?\d{0,2}')),
      ],
      // keyboardType: TextInputType.numberWithOptions(decimal: false),
      keyboardType: TextInputType.numberWithOptions(
        decimal: true,
        signed: false,
      ),
      decoration: InputDecoration(
          border: InputBorder.none
      ),
    ));
  }
}
