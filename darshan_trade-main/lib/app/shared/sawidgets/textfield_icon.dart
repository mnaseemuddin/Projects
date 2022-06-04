import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:royal_q/app/models/common_model.dart';
import 'package:royal_q/app/shared/sawidgets/controllers/text_field_controller.dart';
import 'package:royal_q/app/shared/sawidgets/sawidgets.dart';
import 'package:royal_q/main.dart';


import '../shared.dart';
import 'common_widget.dart';
import 'controllers/app_text_controller.dart';

// class TextFieldIcon extends GetView<TextFieldIconController> {
//   final SuffixWidget? suffix;
//   final String hintText;
//   final String labelText;
//   final TextEditingController? mController;
//   final Function(FieldData)?onChanged;
//   final Function()?onPressed;
//   final VALIDATE validate;
//   final bool obscureText;
//   const TextFieldIcon({Key? key, this.suffix, this.onChanged, this.validate=VALIDATE.USER,
//     this.obscureText=false, this.hintText='', this.mController, this.labelText='', this.onPressed,}) : super(key: key);
//
//
//   @override
//   Widget build(BuildContext context) {
//     return Obx(() => Container(
//         margin: EdgeInsets.only(bottom: 8),
//         padding: EdgeInsets.only(left: 8, right: 8),
//         child: Row(children: [
//           Expanded(child: AppTextField(labelText: labelText, hintText: hintText, validate: validate, onChanged: (val){
//             if(onChanged !=null)
//               onChanged!(val);
//             Controller.isValid = val.valid;
//             // if(_isValid != val.valid){
//             //   if(mounted)setState(() => _isValid=val.valid);
//             // }
//           }, obscureText: obscureText?Controller.showPass:false, mController: mController,)),
//           suffix !=null?GestureDetector(child: Controller.showPass?suffix?.primary:suffix?.secondary, onTap: (){
//             if(onPressed !=null){
//               onPressed!();
//             }
//             Controller.showPass = !Controller.showPass;
//             // if(mounted)setState(() {
//             //   _showPass = !_showPass;
//             //   print('object => $_showPass');
//             // });
//           },)
//               :Icon(Icons.check, color: Controller.isValid?isPlatformIOS?Colors.blue:ColorConstants.APP_SECONDARY_COLOR:isPlatformIOS?Colors.black26:Colors.white10,)
//         ],),
//         decoration: textBg
//     ));
//   }
// }


class TextFieldIcon extends StatefulWidget {

  final SuffixWidget? suffix;
  final String hintText;
  final String labelText;
  final TextEditingController? controller;
  final Function(FieldData)?onChanged;
  final Function()?onPressed;
  final VALIDATE validate;
  final bool obscureText;
  final TextInputType textInputType;
  const TextFieldIcon({Key? key, this.suffix, this.onChanged, this.validate=VALIDATE.USER,
    this.obscureText=false, this.hintText='', this.controller, this.labelText='', this.onPressed, this.textInputType=TextInputType.text,}) : super(key: key);

  @override
  _TextFieldIconState createState() => _TextFieldIconState();
}

class _TextFieldIconState extends State<TextFieldIcon> {
  bool _showPass = true;
  bool _isValid = false;
  @override
  Widget build(BuildContext context) {
    return Container(
      margin: EdgeInsets.only(bottom: 8),
      padding: EdgeInsets.only(left: 8, right: 8),
      child: Row(children: [
        Expanded(child: AppTextField(labelText: widget.labelText, textInputType: widget.textInputType, hintText: widget.hintText, validate: widget.validate, onChanged: (val){
          if(widget.onChanged !=null) {
            widget.onChanged!(val);
          }
          if(_isValid != val.valid){
            if(mounted)setState(() => _isValid=val.valid);
          }
        }, obscureText: widget.obscureText?_showPass:false, controller: widget.controller,)),
        widget.suffix !=null?GestureDetector(child: _showPass?widget.suffix?.primary:widget.suffix?.secondary, onTap: (){
          if(widget.onPressed !=null){
            widget.onPressed!();
          }
          if(mounted) {
            setState(() {
            _showPass = !_showPass;
            print('object => $_showPass');
          });
          }
        },)
            :Icon(Icons.check, color: _isValid? ColorConstants.APP_SECONDARY_COLOR:ColorConstants.white12,)
      ],),
      decoration: textBg
    );
  }
}
