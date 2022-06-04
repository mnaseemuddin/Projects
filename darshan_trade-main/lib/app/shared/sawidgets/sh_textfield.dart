import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:royal_q/app/shared/sawidgets/controllers/app_text_controller.dart';
import 'package:royal_q/main.dart';

import '../shared.dart';

enum VALIDATE {USER, EMAIL, PASSWORD, PHONE, ADDRESS, DOB, CIVIL, OTP, PNUMBER}

// class AppTextField extends GetView<AppTextFieldController> {
//   final TextEditingController? mController;
//   final String text;
//   final String hintText;
//   final String labelText;
//   final String? valText;
//   final TextInputAction? action;
//   final Function(FieldData)?onChanged;
//   final VALIDATE validate;
//   final bool obscureText;
//   final IconData icon;
//   final List<String>?items;
//   final bool hasList;
//   final TextInputType textInputType;
//   const AppTextField({Key? key, this.action, this.onChanged, this.hintText='', this.validate=VALIDATE.USER, this.obscureText=false,
//     this.icon=Icons.check, this.items, this.mController, this.hasList = false, this.text='', this.textInputType = TextInputType.text, this.valText, this.labelText='', }) : super(key: key);
//
//
//
//   @override
//   Widget build(BuildContext context) {
//     return  Column(children: [
//       Container(
//         child: TextFormField(
//           style: TextStyle(color: isPlatformIOS?Colors.black:Colors.white),
//           cursorColor: ColorConstants.iconTheme,
//           keyboardType: textInputType,
//           Controller: mController,
//           onChanged: (val) {
//             Controller.value = val;
//             Controller.isValid = Controller.validate(validate);
//
//             if(onChanged!=null)
//             onChanged!(FieldData(data: val, valid: Controller.isValid));
//             // if(mounted)
//             //   setState(() => _value = val);
//           },
//           obscureText: obscureText,
//           textInputAction: action,
//           onEditingComplete: () => FocusScope.of(context).nextFocus(),
//           decoration: InputDecoration(
//
//               labelText: labelText,
//               hintText: hintText,
//               border: InputBorder.none,
//               labelStyle: TextStyle(color: isPlatformIOS?Colors.black87:Colors.white70),
//           hintStyle: TextStyle(color: isPlatformIOS?Colors.black38:Colors.white38, fontSize: 12)),
//         ),
//       ),
//       !Controller.isValid&&Controller.value.length>0&&valText!=null?Row(
//         children: [
//           Icon(Icons.warning_amber_outlined, size: 16, color: Colors.red,),
//           Text('$valText', style: textStyleLabel(color: Colors.red),)
//         ],):Container(),
//       // Align(alignment: Alignment.centerLeft, child: TextButton.icon(onPressed: (){}, icon: Icon(Icons.warning_amber_outlined), label: Text('Name required')),)
//
//     ],);
//   }
// }


// const double app_padding = 16;

class AppTextField extends StatefulWidget {
  final TextEditingController? controller;
  final String text;
  final String hintText;
  final String labelText;
  final String? valText;
  final TextInputAction? action;
  final Function(FieldData)?onChanged;
  final VALIDATE validate;
  final bool obscureText;
  final IconData icon;
  final List<String>?items;
  final bool hasList;
  final TextInputType textInputType;
  const AppTextField({Key? key, this.action, this.onChanged, this.hintText='', this.validate=VALIDATE.USER, this.obscureText=false,
    this.icon=Icons.check, this.items, this.controller, this.hasList = false, this.text='', this.textInputType = TextInputType.text, this.valText, this.labelText='', }) : super(key: key);

  @override
  _AppTextFieldState createState() => _AppTextFieldState();
}


class _AppTextFieldState extends State<AppTextField> {
  TextEditingController _controller = TextEditingController();
  String _value = '';
  bool _isValid = false;
  bool _obscureText = false;
  @override
  void initState() {
    super.initState();
    _obscureText = widget.obscureText;
    if (widget.controller != null) {
      _controller = widget.controller??_controller;
      _controller.addListener(() {
        _value = _controller.text;
        _isValid = _validate(widget.validate);
        if(mounted) {
          setState(() {
          print('_value: $_value, _isValid: $_isValid');
        });
        }
      });
    } else {
      _controller.text = widget.text;
      if(widget.onChanged!=null) {
        widget.onChanged!(FieldData(data: widget.text, valid: _isValid));
      }
    }
    _obscureText = widget.obscureText;
  }

  @override
  Widget build(BuildContext context) {
    return Column(children: [
      Container(
        child: TextFormField(
          style: TextStyle(color:  ColorConstants.white),
          cursorColor: ColorConstants.iconTheme,
          keyboardType: widget.textInputType,
          controller: _controller,
          onChanged: (val) {
            _value = val;
            _isValid = _validate(widget.validate);
            print('${widget.validate} val: $_isValid');
            if(widget.onChanged!=null) {
              widget.onChanged!(FieldData(data: val, valid: _isValid));
            }
            if(mounted) {
              setState(() => _value = val);
            }
          },
          obscureText: widget.obscureText,
          textInputAction: widget.action,
          onEditingComplete: () => FocusScope.of(context).nextFocus(),
          decoration: InputDecoration(

              labelText: widget.labelText,
              hintText: widget.hintText,
              border: InputBorder.none,
              labelStyle: TextStyle(color:  ColorConstants.white70),
          hintStyle: TextStyle(color:  ColorConstants.white38, fontSize: 12)),
        ),
      ),
      !_isValid&&_value.isNotEmpty&&widget.valText!=null?Row(
        children: [
          Icon(Icons.warning_amber_outlined, size: 16, color: Colors.red,),
          Text('${widget.valText}', style: textStyleLabel(color: Colors.red),)
        ],):Container(),
      // Align(alignment: Alignment.centerLeft, child: TextButton.icon(onPressed: (){}, icon: Icon(Icons.warning_amber_outlined), label: Text('Name required')),)

    ],);
  }

  bool _validate(valid) {
    switch (valid) {
      case VALIDATE.USER:
        return validateUser(_value);
      case VALIDATE.EMAIL:
        return validEmail(_value);
      case VALIDATE.DOB:
        return validDOB(_value);
      case VALIDATE.PASSWORD:
        return validPassword(_value);
      case VALIDATE.OTP:
        return validOTP(_value);
      case VALIDATE.CIVIL:
        return validCivilNumber(_value);
      case VALIDATE.PHONE:
        return validPhone(_value);
        case VALIDATE.PNUMBER:
        return validNumber(_value);
      default:
        return false;
    }
  }

  bool validateUser(String data){
    return data.length > 3;
  }
//
  bool validEmail(emailAddress){
    bool emailValid = RegExp(r"^[a-zA-Z0-9.a-zA-Z0-9.#$%&'*+-/=?^_`{|}~]+@[a-zA-Z0-9]+\.[a-zA-Z]+").hasMatch(emailAddress);
    return emailValid;
  }

  bool validDOB(String dob){
    bool isValid = dob.isNotEmpty;
    return isValid;
  }

  bool validCivilNumber(String data){
    return data.length > 6;
  }

  bool valid(String data){
    return data.length > 3;
  }

  bool validContact(String data){
    bool isValid = RegExp(r"^[6-9]\d{9}$").hasMatch(data);
    return isValid;
  }

  bool validPassword(String data){
    return data.length > 4;
  }

  bool validOTP(String data){
    return data.length > 5;
  }

  bool validPhone(String data){
    bool isValid = RegExp(r"^[6-9]\d{9}$").hasMatch(data);
    return isValid;
  }

  _showAlertDialog(List<String>items, onTap) {
    showDialog(context: context, builder: (context) =>
        AlertDialog(
          title: Text("Show Dialog"),
          content: SingleChildScrollView(child: Column(
              mainAxisSize: MainAxisSize.min,
              children: List<Widget>.generate(items.length, (index) =>
                  ListTile(
                      title: Text(items.elementAt(index)), onTap: () {
                    Navigator.of(context).pop();
                    onTap(items.elementAt(index));
                  }),
              )),),),
    );
  }

  bool validNumber(String value) {
    try{
      return double.parse(value)>0;
    }catch(e){
      return false;
    }
  }
}


class FieldData{
  final String data;
  final bool valid;
  FieldData({required this.data, required this.valid});
}
