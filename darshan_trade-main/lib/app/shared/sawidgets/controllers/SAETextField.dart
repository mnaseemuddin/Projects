





import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import '../../constants/common.dart';


class SAETextField extends StatelessWidget {
  final String hintText;
  TextEditingController controller;
   SAETextField({Key? key, required this.hintText,required this.controller}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(left:30.0,right: 30),
      child: TextFormField(
        controller: controller,
        style: const TextStyle(fontSize: 14.5),
        textInputAction: TextInputAction.next,
        /*inputFormatters: [
          LengthLimitingTextInputFormatter(35),
          FilteringTextInputFormatter.deny(RegExp('[ ]')),
        ],*/
        decoration: InputDecoration(
            enabledBorder: OutlineInputBorder(
                borderRadius: BorderRadius.circular(10),
                borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
            ),
            border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(10),
                borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
            ),
            errorBorder: OutlineInputBorder(
                borderRadius: BorderRadius.circular(10),
                borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
            ),
            focusedBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(10),
              borderSide: const BorderSide(width: 1,
                  color: Color(0xffe9e9ea)),
            ),
            contentPadding: const EdgeInsets.only(top: 20,left: 10),
            hintText: hintText,

            hintStyle: TextStyle(color: Colors.grey[600],fontSize: 13.5),
            filled: true,
            fillColor: Colors.grey[200]
        ),
      ),
    );
  }
}



class WithdrawTextField extends StatelessWidget {
  final String hintText,validationMsg;
  final TextEditingController? controller;
  const WithdrawTextField({Key? key, required this.hintText,
    required this.controller, required this.validationMsg}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(top:8.0,left: 18,right: 20),
      child: TextFormField(
        style: const TextStyle(fontSize: 14.5),
        controller: controller,
        validator: (v) {
          if (v!.trim().isEmpty) {
            return validationMsg;
          }
          return null;
        },
        textInputAction: TextInputAction.next,
        enabled: false,
        /*inputFormatters: [
          LengthLimitingTextInputFormatter(35),
          FilteringTextInputFormatter.deny(RegExp('[ ]')),
        ],*/
        decoration: InputDecoration(

            enabledBorder: OutlineInputBorder(
                borderRadius: BorderRadius.circular(10),
                borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
            ),
            border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(10),
                borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
            ),
            errorBorder: OutlineInputBorder(
                borderRadius: BorderRadius.circular(10),
                borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
            ),
            focusedBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(10),
              borderSide: const BorderSide(width: 1,
                  color: Color(0xffe9e9ea)),
            ),
            contentPadding: const EdgeInsets.only(top: 20,left: 15,right: 20), hintText: hintText,

            hintStyle: textStyleHeading3(color: Colors.grey[600]),
            filled: true,
            fillColor: Colors.grey[200]
        ),
      ),
    );
  }
}



class AmountTextField extends StatelessWidget {
  final String hintText,validationMsg;
  final TextEditingController? controller;
  const AmountTextField({Key? key, required this.hintText,
    required this.controller, required this.validationMsg}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(top:8.0,left: 18,right: 20),
      child: TextFormField(
        style: const TextStyle(fontSize: 14.5),
        controller: controller,
        onChanged: (text){
        },
        validator: (v) {
          if (v!.trim().isEmpty) {
            return validationMsg;
          }
          return null;
        },
        textInputAction: TextInputAction.next,
        keyboardType: TextInputType.number,
        /*inputFormatters: [
          LengthLimitingTextInputFormatter(35),
          FilteringTextInputFormatter.deny(RegExp('[ ]')),
        ],*/
        decoration: InputDecoration(
            enabledBorder: OutlineInputBorder(
                borderRadius: BorderRadius.circular(10),
                borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
            ),
            border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(10),
                borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
            ),
            errorBorder: OutlineInputBorder(
                borderRadius: BorderRadius.circular(10),
                borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
            ),
            focusedBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(10),
              borderSide: const BorderSide(width: 1,
                  color: Color(0xffe9e9ea)),
            ),
            contentPadding: const EdgeInsets.only(top: 20,left: 15,right: 5),
            hintText: hintText,
            hintStyle: textStyleHeading3(color: Colors.grey[600]),
            filled: true,
            suffixText: "SHIB",
            fillColor: Colors.grey[200]
        ),
      ),
    );
  }
}




class labelNameTextField extends StatelessWidget {
  final String prefixIcon;
  final String hintText,labelText,validationMsg;
  final TextEditingController? controller;

  bool enabled=true;
  labelNameTextField({Key? key,required this.prefixIcon, required this.hintText,
    required this.controller, required this.validationMsg,required this.labelText
    ,required this.enabled}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return TextFormField(
      style: const TextStyle(fontSize: 14.5),
      controller: controller,
      validator: (v) {
        if (v!.trim().isEmpty) {
          return validationMsg;
        }
        return null;
      },
      autofocus: true,
      textInputAction: TextInputAction.next,
      inputFormatters: [
      ],
      decoration: InputDecoration(
          enabled: enabled,
          focusedBorder: OutlineInputBorder(
            borderSide: BorderSide(color: Color(0xffafaeae)),
            borderRadius: BorderRadius.circular(9.0),
          ),
          border: OutlineInputBorder(
              borderRadius: BorderRadius.circular(12),
              borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
          ),
          errorBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(12),
              borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
          ),
          enabledBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(12),
              borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
          ),
          contentPadding: const EdgeInsets.only(top: 20,left: 15),
          hintText: hintText,
          labelText: labelText,
          labelStyle: TextStyle(color: Colors.grey[600],fontSize: 13.5),
          hintStyle: TextStyle(color: Colors.grey[600],fontSize: 13.5),
          filled: false,
          fillColor: Colors.white
      ),
    );
  }
}