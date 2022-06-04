

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class AppTextField extends StatelessWidget {
  final String prefixIcon;
  final String hintText,validationMsg;
  final TextEditingController? controller;
  const AppTextField({Key? key,required this.prefixIcon, required this.hintText,
    required this.controller, required this.validationMsg}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    return TextFormField(
      controller: controller,
      validator: (v) {
        if (v!.trim().isEmpty) {
          return validationMsg;
        }
        return null;
      },
      textInputAction: TextInputAction.next,
      // inputFormatters: [
      //   FilteringTextInputFormatter.deny(RegExp('[ ]')),
      // ],
        decoration: InputDecoration(
            errorBorder: OutlineInputBorder(
                borderRadius: BorderRadius.circular(25),
                borderSide: const BorderSide(width: 1,color: Color(0xffece7e7))
            ),
          enabledBorder: OutlineInputBorder(
            borderRadius: BorderRadius.circular(25),
            borderSide: const BorderSide(width: 1,color: Color(0xffece7e7))
          ),
          focusedBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(25),
              borderSide: const BorderSide(width: 1,
                  color: Color(0xff84a2dc)),
          ),
          prefixIcon:Padding(
            padding: const EdgeInsets.all(15.0),
            child: Image.asset(prefixIcon,height: 8,width: 8,color: Colors.blue,),
          ),
          contentPadding: EdgeInsets.only(top: 20,left: 15),
          hintText: hintText,
          hintStyle: TextStyle(color: Colors.grey[600],fontSize: 13.5),
          filled: false,
          fillColor: Colors.white
        ),
      );
  }
}


class EmailTextField extends StatelessWidget {
  final String prefixIcon;
  final String hintText,validationMsg;
  final TextEditingController? controller;
  bool? enabled=true;
   EmailTextField({Key? key,required this.prefixIcon, required this.hintText,
    required this.controller, required this.validationMsg,this.enabled}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    return TextFormField(
      style: const TextStyle(fontSize: 14.5),
      controller: controller,
      enabled: enabled,
      validator: (v) {
        if (v!.trim().isEmpty) {
          return validationMsg;
        }
        return null;
      },
      textInputAction: TextInputAction.next,
      inputFormatters: [
        LengthLimitingTextInputFormatter(35),
        FilteringTextInputFormatter.deny(RegExp('[ ]')),
      ],
      decoration: InputDecoration(
          enabledBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(25),
              borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
          ),
          border: OutlineInputBorder(
              borderRadius: BorderRadius.circular(25),
              borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
          ),
          errorBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(25),
              borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
          ),
          focusedBorder: OutlineInputBorder(
            borderRadius: BorderRadius.circular(25),
            borderSide: const BorderSide(width: 1,
                color: Color(0xff84a2dc)),
          ),
          prefixIcon:Padding(
            padding: const EdgeInsets.all(15.0),
            child: Image.asset(prefixIcon,height: 8,width: 8,color: Colors.blue,),
          ),
          contentPadding: const EdgeInsets.only(top: 20,left: 10),
          hintText: hintText,
          hintStyle: TextStyle(color: Colors.grey[600],fontSize: 13.5),
          filled: false,
          fillColor: Colors.white
      ),
    );
  }
}


class NumberTextField extends StatelessWidget {
  final String prefixIcon;
  final String hintText,validationMsg;
  final TextEditingController? controller;
  int? digits=12;
   NumberTextField({Key? key,required this.prefixIcon, required this.hintText,
    required this.controller,required this.validationMsg}) : super(key: key);
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
      textInputAction: TextInputAction.next,
      keyboardType: TextInputType.number,
      inputFormatters: [
        LengthLimitingTextInputFormatter(12),
        FilteringTextInputFormatter.digitsOnly
      ],
      decoration: InputDecoration(
          errorBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(25),
              borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
          ),
          enabledBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(25),
              borderSide: BorderSide(width: 1.0,color: const Color(0xffece7e7))
          ),
          focusedBorder: OutlineInputBorder(
            borderRadius: BorderRadius.circular(25),
            borderSide: const BorderSide(width: 1,
                color: Color(0xff84a2dc)),
          ),
          border: OutlineInputBorder(
              borderRadius: BorderRadius.circular(25),
              borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
          ),
          prefixIcon:Padding(
            padding: const EdgeInsets.all(15.0),
            child: Image.asset(prefixIcon,height: 8,width: 8,color: Colors.blue,),
          ),
          contentPadding: EdgeInsets.only(top: 20,left: 15),
          hintText: hintText,
          hintStyle: TextStyle(color: Colors.grey[600],fontSize: 13.5),
          filled: false,
          fillColor: Colors.white
      ),
    );
  }
}


class labelNumberTextField extends StatelessWidget {
  final String prefixIcon;
  final String hintText,labelText,validationMsg;
  final TextEditingController? controller;
  int? digits=12;
  bool enabled;
  labelNumberTextField({Key? key,required this.prefixIcon, required this.hintText,
    required this.controller,required this.validationMsg,required this.labelText,
  required this.enabled}) : super(key: key);
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
      textInputAction: TextInputAction.next,
      keyboardType: TextInputType.number,
      inputFormatters: [
        LengthLimitingTextInputFormatter(12),
        FilteringTextInputFormatter.digitsOnly
      ],
      decoration: InputDecoration(
        enabled: enabled,
          errorBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(12),
              borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
          ),
          enabledBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(12),
              borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
          ),
          focusedBorder: OutlineInputBorder(
            borderRadius: BorderRadius.circular(12),
            borderSide: const BorderSide(width: 1,
                color: Color(0xff84a2dc)),
          ),
          border: OutlineInputBorder(
              borderRadius: BorderRadius.circular(12),
              borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
          ),
          prefixIcon:Padding(
            padding: const EdgeInsets.all(15.0),
            child: Image.asset(prefixIcon,height: 8,width: 8,color: Colors.blue,),
          ),
          contentPadding: const EdgeInsets.only(top: 20,left: 15),
          hintText: hintText,
          labelText: "Mobile Number",
          hintStyle: TextStyle(color: Colors.grey[600],fontSize: 13.5),
          filled: false,
          fillColor: Colors.white
      ),
    );
  }
}


class PasswordTextField extends StatefulWidget {
  final String prefixIcon;
  final String hintText,validationMsg;

  final bool isPassword;

  final TextEditingController? controller;
  const PasswordTextField({Key? key,required this.prefixIcon, required this.hintText,
  required this.isPassword,required this.controller,
    required this.validationMsg}) : super(key: key);

  @override
  State<PasswordTextField> createState() => _PasswordTextFieldState();
}

class _PasswordTextFieldState extends State<PasswordTextField> {

  bool obscureText=true;


  @override
  Widget build(BuildContext context) {
    return TextFormField(
      style: const TextStyle(fontSize: 14.5),
      controller: widget.controller,
      obscureText: widget.isPassword,
      validator: (v) {
        if (v!.trim().isEmpty) {
          return widget.validationMsg;
        }
        return null;
      },
      textInputAction: TextInputAction.next,
      inputFormatters: [
        FilteringTextInputFormatter.deny(RegExp('[ ]')),
      ],
      decoration: InputDecoration(
        suffixIcon:  IconButton(onPressed: (){
          setState(() {
            if(obscureText) {
              obscureText=false;
            } else {
              obscureText=true;
            }
          });
        },icon: Icon(obscureText?Icons.visibility:
        Icons.visibility_off),),
          enabledBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(25),
              borderSide: BorderSide(width: 1,color: const Color(0xffece7e7))
          ),
          focusedBorder: OutlineInputBorder(
            borderRadius: BorderRadius.circular(25),
            borderSide: const BorderSide(width: 1,
                color: Color(0xff84a2dc)),
          ),
          prefixIcon:Padding(
            padding: const EdgeInsets.all(15.0),
            child: Image.asset(widget.prefixIcon,height: 8,width: 8,color: Colors.blue,),
          ),
          contentPadding: const EdgeInsets.only(top: 20,left: 15),
          hintText: widget.hintText,
          hintStyle: TextStyle(color: Colors.grey[600],fontSize: 13.5),
          filled: false,
          fillColor: Colors.white
      ),
    );
  }
}


class NameTextField extends StatelessWidget {
  final String prefixIcon;
  final String hintText,validationMsg;
  final TextEditingController? controller;
  const NameTextField({Key? key,required this.prefixIcon, required this.hintText,
    required this.controller, required this.validationMsg}) : super(key: key);

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
      textInputAction: TextInputAction.next,
      inputFormatters: [
        FilteringTextInputFormatter.allow(RegExp(
            r'[A-Z]|[ ]|[a-z]')),
        LengthLimitingTextInputFormatter(30),
      ],
      decoration: InputDecoration(
          border: OutlineInputBorder(
              borderRadius: BorderRadius.circular(25),
              borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
          ),
          errorBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(25),
              borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
          ),
          enabledBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(25),
              borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
          ),
          focusedBorder: OutlineInputBorder(
            borderRadius: BorderRadius.circular(25),
            borderSide: const BorderSide(width: 1,
                color: Color(0xff84a2dc)),
          ),
          prefixIcon:Padding(
            padding: const EdgeInsets.all(15.0),
            child: Image.asset(prefixIcon,height: 8,width: 8,color: Colors.blue,),
          ),
          contentPadding: const EdgeInsets.only(top: 20,left: 15),
          hintText: hintText,
          hintStyle: TextStyle(color: Colors.grey[600],fontSize: 13.5),
          filled: false,
          fillColor: Colors.white
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
        FilteringTextInputFormatter.allow(RegExp(
            r'[A-Z]|[ ]|[a-z]')),
        LengthLimitingTextInputFormatter(30),
      ],
      decoration: InputDecoration(
        enabled: enabled,
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
          focusedBorder: OutlineInputBorder(
            borderRadius: BorderRadius.circular(12),
            borderSide: const BorderSide(width: 1,
                color: Color(0xff84a2dc)),
          ),
          prefixIcon:Padding(
            padding: const EdgeInsets.all(15.0),
            child: Image.asset(prefixIcon,height: 8,width: 8,color: Colors.blue,),
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
