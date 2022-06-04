import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:trading_apps/models/common_model.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/res/constants.dart';


enum VALIDATE {USER, EMAIL, PASSWORD, PHONE, ADDRESS, DOB, CIVIL}
const double app_padding = 16;

class AppTextField extends StatefulWidget {
  final TextEditingController? controller;
  final String text;
  final String labelText;
  final String hintText;
  final String? valText;
  final TextInputAction? action;
  final Function(FieldData)?onChanged;
  final VALIDATE validate;
  final bool obscureText;
  final IconData icon;
  final List<String>?items;
  final bool hasList;
  final TextInputType textInputType;
  const AppTextField({Key? key, this.action, this.onChanged, this.labelText='',this.hintText='', this.validate=VALIDATE.USER, this.obscureText=false,
    this.icon=Icons.check, this.items, this.controller, this.hasList = false, this.text='', this.textInputType = TextInputType.text, this.valText, }) : super(key: key);

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
        if(mounted)
        setState(() {
          print('_value: $_value, _isValid: $_isValid');
        });
      });
    } else {
      _controller.text = widget.text;
      if(widget.onChanged!=null)
      widget.onChanged!(FieldData(data: widget.text, valid: _isValid));
    }
    _obscureText = widget.obscureText;
  }

  @override
  Widget build(BuildContext context) {
    return Column(children: [
      Container(
        margin: EdgeInsets.only(top: app_padding / 2, bottom: app_padding / 2),
        padding: EdgeInsets.only(left: 8),
        child: TextFormField(
          autofocus: true,
    inputFormatters: [
      LengthLimitingTextInputFormatter(38),
      FilteringTextInputFormatter.deny(RegExp('[ ]')),
    ],
          style: TextStyle(color: Colors.white),
          cursorColor: Colors.white,
          keyboardType: widget.textInputType,
          textInputAction: TextInputAction.next,
          controller: _controller,
          onChanged: (val) {
            _value = val;
            _isValid = _validate(widget.validate);
            print('${widget.validate} val: $_isValid');
            if(widget.onChanged!=null)
            widget.onChanged!(FieldData(data: val, valid: _isValid));
            // widget.onChanged!(val, _isValid);
            if(mounted)
              setState(() => _value = val);
          },
          obscureText: _obscureText,
         // textInputAction: widget.action,
          onTap: widget.hasList ? () {
            FocusScope.of(context).unfocus();
            if(widget.items!=null)
            _showAlertDialog(widget.items!, (data) {
              _value = data;
              _isValid = _validate(widget.validate);
              _controller.text = data;
              if(widget.onChanged!=null)
              widget.onChanged!(FieldData(data: data, valid: _isValid));
              // widget.onChanged!(data, _isValid);
              print('print Item $data _isValid $_isValid');
              if(mounted)
                setState(() => _value = data);
            });
          } : widget.obscureText?(){setState(() => _obscureText =!_obscureText);}:null,
          onEditingComplete: () => FocusScope.of(context).nextFocus(),
          decoration: InputDecoration(
            hintText: widget.hintText,
              labelText: widget.labelText,
              suffixIcon: IconButton(icon: Icon(
                widget.icon, color: _obscureText?Colors.white10: _isValid&&!_obscureText
                  ? APP_SECONDARY_COLOR : Colors.white10,),
                onPressed: widget.items != null ? () {
                  if(widget.items!=null)
                  _showAlertDialog(widget.items!, (data) {
                    _value = data;
                    _isValid = _validate(widget.validate);
                    _controller.text = data;
                    print('print Item $data _isValid $_isValid');
                    if(mounted)
                      setState(() => _value = data);
                  });
                } : null,),
              border: InputBorder.none,
              labelStyle: TextStyle(color: Colors.white70)),
        ),
        decoration: BoxDecoration(
            // border: Border.all(width: 1, color: APP_SECONDARY_COLOR)
          // borderRadius: BorderRadius.only(topLeft: Radius.circular(8), topRight: Radius.circular(8)),
          color: Colors.white.withOpacity(0.03),
            border: Border(bottom: BorderSide(width: 1, color: APP_SECONDARY_COLOR))
        ),
      ),
      !_isValid&&_value.length>0&&widget.valText!=null?Row(
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
      case VALIDATE.CIVIL:
        return validCivilNumber(_value);
      case VALIDATE.PHONE:
        return validPhone(_value);
      default:
        return false;
    }
  }

  bool validateUser(String data){
    return data.length > 3;
  }

  bool validEmail(emailAddress){
    bool emailValid = RegExp(r"^[a-zA-Z0-9.a-zA-Z0-9.#$%&'*+-/=?^_`{|}~]+@[a-zA-Z0-9]+\.[a-zA-Z]+").hasMatch(emailAddress);
    return emailValid;
  }

  bool validDOB(String dob){
    bool isValid = dob.length > 0;
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
    return data.length > 6;
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
}



class AppTextField1 extends StatefulWidget {
  final TextEditingController? controller;
  final String text;
  final String labelText;
  final String hintText;
  final String? valText;
  final TextInputAction? action;
  final Function(FieldData)?onChanged;
  final VALIDATE validate;
  final bool obscureText;
  final IconData icon;
  final List<String>?items;
  final bool hasList;
  final TextInputType textInputType;
  const AppTextField1({Key? key, this.action, this.onChanged, this.labelText='',this.hintText='', this.validate=VALIDATE.USER, this.obscureText=false,
    this.icon=Icons.check, this.items, this.controller, this.hasList = false, this.text='', this.textInputType = TextInputType.text, this.valText, }) : super(key: key);

  @override
  _AppTextFieldState1 createState() => _AppTextFieldState1();
}

class _AppTextFieldState1 extends State<AppTextField1> {
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
        if(mounted)
          setState(() {
            print('_value: $_value, _isValid: $_isValid');
          });
      });
    } else {
      _controller.text = widget.text;
      if(widget.onChanged!=null)
        widget.onChanged!(FieldData(data: widget.text, valid: _isValid));
    }
    _obscureText = widget.obscureText;

  }

  @override
  Widget build(BuildContext context) {
    return Column(children: [
      Container(
        margin: EdgeInsets.only(top: app_padding / 2, bottom: app_padding / 2),
        padding: EdgeInsets.only(left: 8),
        child: TextFormField(
          inputFormatters: [
            LengthLimitingTextInputFormatter(38),
            FilteringTextInputFormatter.deny(RegExp('[ ]')),
          ],
          style: TextStyle(color: Colors.white),
          cursorColor: Colors.white,
          keyboardType: widget.textInputType,
          textInputAction: TextInputAction.done,
          controller: _controller,
          onChanged: (val) {
            _value = val;
            _isValid = _validate(widget.validate);
            print('${widget.validate} val: $_isValid');
            if(widget.onChanged!=null)
              widget.onChanged!(FieldData(data: val, valid: _isValid));
            // widget.onChanged!(val, _isValid);
            if(mounted)
              setState(() => _value = val);
          },
          obscureText: _obscureText,
          // textInputAction: widget.action,
          // onTap: widget.hasList ? () {
          //   FocusScope.of(context).unfocus();
          //   if(widget.items!=null)
          //     _showAlertDialog(widget.items!, (data) {
          //       _value = data;
          //       _isValid = _validate(widget.validate);
          //       _controller.text = data;
          //       if(widget.onChanged!=null)
          //         widget.onChanged!(FieldData(data: data, valid: _isValid));
          //       // widget.onChanged!(data, _isValid);
          //       print('print Item $data _isValid $_isValid');
          //       if(mounted)
          //         setState(() => _value = data);
          //     });
          // } : widget.obscureText?(){setState((){
          //   _obscureText =!_obscureText;
          //   print(_obscureText);
          // });}:null,
          onEditingComplete: () => FocusScope.of(context).nextFocus(),
          decoration: InputDecoration(
              hintText: widget.hintText,
              labelText: widget.labelText,
              // suffixIcon: IconButton(icon: Icon(
              //   widget.icon, color: _obscureText?Colors.white10: _isValid&&!_obscureText
              //     ? APP_SECONDARY_COLOR : Colors.white10,),
              //   onPressed: widget.items != null ? () {
              //     // if(widget.items!=null)
              //     //   _showAlertDialog(widget.items!, (data) {
              //     //     _value = data;
              //     //     _isValid = _validate(widget.validate);
              //     //     _controller.text = data;
              //     //     print('print Item $data _isValid $_isValid');
              //     //     if(mounted)
              //     //       setState(() => _value = data);
              //     //   });
              //
              //   } : null,),
              suffixIcon: IconButton(onPressed: (){
                setState(() {
                  if(_obscureText)
                    _obscureText=false;
                  else
                    _obscureText=true;
                });
              }, icon: Icon(_obscureText?Icons.visibility:Icons.visibility_off,
                color: _obscureText?Colors.grey[600]:APP_SECONDARY_COLOR,)),
              border: InputBorder.none,
              labelStyle: TextStyle(color: Colors.white70)),
        ),
        decoration: BoxDecoration(
          // border: Border.all(width: 1, color: APP_SECONDARY_COLOR)
          // borderRadius: BorderRadius.only(topLeft: Radius.circular(8), topRight: Radius.circular(8)),
            color: Colors.white.withOpacity(0.03),
            border: Border(bottom: BorderSide(width: 1, color: APP_SECONDARY_COLOR))
        ),
      ),
      !_isValid&&_value.length>0&&widget.valText!=null?Row(
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
      case VALIDATE.CIVIL:
        return validCivilNumber(_value);
      case VALIDATE.PHONE:
        return validPhone(_value);
      default:
        return false;
    }
  }

  bool validateUser(String data){
    return data.length > 3;
  }

  bool validEmail(emailAddress){
    bool emailValid = RegExp(r"^[a-zA-Z0-9.a-zA-Z0-9.#$%&'*+-/=?^_`{|}~]+@[a-zA-Z0-9]+\.[a-zA-Z]+").hasMatch(emailAddress);
    return emailValid;
  }

  bool validDOB(String dob){
    bool isValid = dob.length > 0;
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
    return data.length >= 6;
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
}

