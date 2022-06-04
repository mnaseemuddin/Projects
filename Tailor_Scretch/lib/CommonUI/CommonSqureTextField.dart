


import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:tailor/Support/UISupport.dart';

enum VALIDATE {USER, EMAIL, PASSWORD, PHONE, ADDRESS, DOB, CIVIL}
const double app_padding = 16;

class CommonSqureTextField extends StatefulWidget {


 final TextEditingController? controller;
 final String? text;
 final String? labelText;
 final String? hintText;
 final String? valText;
 final TextInputAction? action;
 final Function (FieldData)? onChange;
 final bool IsEnable;
 final int limitCharlength;
 final int maxLength;


  const CommonSqureTextField({Key? key,this.text,this.labelText,this.hintText,this.valText,
  this.action,this.controller,this.onChange, required this.IsEnable,required this.limitCharlength
  ,required this.maxLength}) : super(key: key);

  @override
  _CommonSqureTextFieldState createState() => _CommonSqureTextFieldState();
}

class _CommonSqureTextFieldState extends State<CommonSqureTextField> {


  @override
  Widget build(BuildContext context) {
    return Padding(
        padding: const EdgeInsets.all(8.0),
        child: TextFormField(
          enabled: widget.IsEnable,
          style: TextStyle(color:UISupport.textColor),
          autofocus: true,
          inputFormatters: [
            FilteringTextInputFormatter.allow(RegExp(r'[A-Z]|[ ]|[a-z]')),
            LengthLimitingTextInputFormatter(widget.limitCharlength),
          ],
          textCapitalization: TextCapitalization.sentences,
          textInputAction: TextInputAction.next,
          cursorColor: UISupport.textColor,
          controller: widget.controller,
          decoration: InputDecoration(
            labelText: widget.labelText,labelStyle: GoogleFonts.aBeeZee(color: Colors.grey[600]),
            border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(8),borderSide: BorderSide(width: 1,color:
            Color(0xffdedede))
            ),
            focusedBorder: OutlineInputBorder(
              borderSide: BorderSide(width:0.70,color: Color(0xffdedede)),
              borderRadius: BorderRadius.circular(10.0),
            ),
            hintText: widget.hintText,
          ),
        ),
      );
  }
}

class FieldData{
  final String data;
  final bool valid;
  FieldData({required this.data, required this.valid});
}


class PhoneNumberTextField extends StatelessWidget {

  final TextEditingController? controller;
  final String? text;
  final String? labelText;
  final String? hintText;
  final String? valText;
  final TextInputAction? action;
  final Function (FieldData)? onChange;
  final bool IsEnable;
  final int Inputdigits;
  const PhoneNumberTextField({Key? key,this.text,this.labelText,this.hintText,this.valText,
    this.action,this.controller,this.onChange, required this.IsEnable,required this.Inputdigits}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: TextFormField(
        enabled: IsEnable,
        style: TextStyle(color:UISupport.textColor),
        autofocus: true,
        textInputAction: TextInputAction.next,
        cursorColor: UISupport.textColor,
        controller: controller,
        keyboardType: TextInputType.number,
        inputFormatters: [
          LengthLimitingTextInputFormatter(Inputdigits),
          FilteringTextInputFormatter.digitsOnly
        ],
        decoration: InputDecoration(
          labelText: labelText,labelStyle: GoogleFonts.aBeeZee(color: Colors.grey[600]),
          border: OutlineInputBorder(
              borderRadius: BorderRadius.circular(8),borderSide: BorderSide(width: 1,color:
          Color(0xffdedede))
          ),
          focusedBorder: OutlineInputBorder(
            borderSide: BorderSide(width:0.70,color: Color(0xffdedede)),
            borderRadius: BorderRadius.circular(10.0),
          ),
          hintText: hintText,
        ),
      ),
    );
  }
}



class AddressTextField extends StatelessWidget {
  final TextEditingController? controller;
  final String? text;
  final String? labelText;
  final String? hintText;
  final String? valText;
  final TextInputAction? action;
  final Function (FieldData)? onChange;
  final bool IsEnable;
  final int limitCharlength;
  final int maxLength;
  const AddressTextField({Key? key,this.text,this.labelText,this.hintText,this.valText,
    this.action,this.controller,this.onChange, required this.IsEnable,required this.limitCharlength,
  required this.maxLength}) : super(key: key);


  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: TextFormField(
        enabled: IsEnable,
        style: TextStyle(color:UISupport.textColor),
        autofocus: true,
        inputFormatters: [
          LengthLimitingTextInputFormatter(limitCharlength),
        ],
        textCapitalization: TextCapitalization.sentences,
        textInputAction: TextInputAction.next,
        cursorColor: UISupport.textColor,
        controller: controller,
        maxLength: maxLength,
        decoration: InputDecoration(
          labelText: labelText,labelStyle: GoogleFonts.aBeeZee(color: Colors.grey[600]),
          border: OutlineInputBorder(
              borderRadius: BorderRadius.circular(8),borderSide: BorderSide(width: 1,color:
          Color(0xffdedede))
          ),
          focusedBorder: OutlineInputBorder(
            borderSide: BorderSide(width:0.70,color: Color(0xffdedede)),
            borderRadius: BorderRadius.circular(10.0),
          ),
          hintText: hintText,
        ),
      ),
    );
  }
}
