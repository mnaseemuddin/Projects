import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:ionicons/ionicons.dart';
import 'package:trading_apps/api/user_data.dart';
import 'package:trading_apps/custom_ui/popup_dialog.dart';
import 'package:trading_apps/custom_ui/sh_textfield.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/res/constants.dart';
import 'package:trading_apps/res/strings.dart';
import 'package:trading_apps/screens/settings/privacypolicy.dart';
import 'dart:math' as math;

import 'package:trading_apps/utility/common_methods.dart';
import 'package:url_launcher/url_launcher.dart';

class SubmitButton extends StatelessWidget {
  final Function()onPressed;
  final String title;
  const SubmitButton({Key? key, required this.onPressed, required this.title}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    return TextButton(onPressed: onPressed, child: Container(
      width: 3*size.width/4,
      height: 50,
      alignment: Alignment.center,
      child: Text(title, style: TextStyle(color: APP_PRIMARY_COLOR, fontSize: 18),),
      decoration: BoxDecoration(
          color: APP_SECONDARY_COLOR,
          borderRadius: BorderRadius.circular(25)
      ),
    ));
  }
}

class AcceptButton extends StatefulWidget {
  final Function(bool)onAccept;
  const AcceptButton({Key? key, required this.onAccept}) : super(key: key);

  @override
  _AcceptButtonState createState() => _AcceptButtonState();
}

class _AcceptButtonState extends State<AcceptButton> {
  bool _selected = false;
  @override
  Widget build(BuildContext context) {
    return Row(children: [
      TextButton(onPressed: (){
        setState(() {
          _selected = !_selected;
          widget.onAccept(_selected);
        });
      }, child: Icon(_selected?Icons.check_box:Icons.check_box_outline_blank, color: APP_SECONDARY_COLOR,)),
      Expanded(child: GestureDetector(
        onTap:(){
          _launchURL('http://tradingclub.fund/terms_and_conditions');
        //  navPush(context, PrivacyPolicyActivity(tilte: "Terms & Conditions", url: "assets/terms_condition.pdf"));
        },
        child: Wrap(children: [
          Text(MSG_TERM1, style: TextStyle(color: Colors.white),),
          Text(MSG_TERM2, style: TextStyle(fontWeight: FontWeight.bold, color: Colors.white),),
        ],),
      ))
    ],);
  }
}


void _launchURL(_url) async =>
    await canLaunch(_url) ? await launch(_url) : throw 'Could not launch $_url';

class ListSelection extends StatefulWidget {

  final List<String>list;
  final Function(String)onSelct;
  const ListSelection({Key? key, required this.list, required this.onSelct}) : super(key: key);

  @override
  _ListSelectionState createState() => _ListSelectionState();
}

class _ListSelectionState extends State<ListSelection> {

  late String _selectedItem;// = widget.list[0];

  @override
  void initState() {
    _selectedItem = widget.list[0];
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: EdgeInsets.only(top: app_padding / 2, bottom: app_padding / 2),
      padding: EdgeInsets.only(left: 8),
      height: 40,

      child: Column(children: [
        Padding(
          padding: const EdgeInsets.only(top:8.0),
          child: Row(children: [
            Expanded(child: GestureDetector(child: Text('$PAYMENT_TYPE $_selectedItem',
              style: textStyleHeading3(color: Colors.white),))),
            // IconButton(onPressed: _showDialog, icon: Icon(Icons.keyboard_arrow_down_outlined,
            //   color: APP_SECONDARY_COLOR,))
          ],),
        ),
      ],),

      decoration: BoxDecoration(
          color: Colors.white.withOpacity(0.03),
          border: Border(bottom: BorderSide(width: 1, color: APP_SECONDARY_COLOR))
      ),
    );
  }

  _showDialog(){
    Navigator.of(context).push(SelectCategoryDialog(title: SELECT_CURRENCY, list: widget.list, onSelect: (string ) {
      setState(() {
        _selectedItem = string;
        widget.onSelct(_selectedItem);
      });
    }));
  }
}

class IconsButton extends StatelessWidget {
  final IconData icon;
  final Function()onSelect;
  const IconsButton({Key? key, required this.icon, required this.onSelect}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      child: Container(width: 50, height: 50,
        padding: EdgeInsets.all(8),
        child: Icon(icon, color: APP_SECONDARY_COLOR,),
        decoration: BoxDecoration(
            shape: BoxShape.circle,
            border: Border.all(color: APP_SECONDARY_COLOR, width: 1)
        ),
      ),
      onTap: onSelect,
    );
  }
}

class BottomNavItem extends StatelessWidget {
  final IconData icon;
  final IconData iconMain;
  final String title;
  final bool selected;
  final Function()onSelected;
  const BottomNavItem({Key? key, this.selected=false, required this.icon, required this.iconMain, required this.title, required this.onSelected}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(child: Column(
      mainAxisSize: MainAxisSize.min,
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        Icon(selected?iconMain:icon, color: selected?APP_SECONDARY_COLOR:Colors.white54,),
        Text(title, style: textStyleLabel(color: selected?APP_SECONDARY_COLOR:Colors.white54),)
      ],),onTap: onSelected,);
  }
}

class BorderContainer extends StatelessWidget {
  final Widget child;
  final Color color;
  const BorderContainer({Key? key, required this.child, this.color=Colors.transparent}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(

      child: child,
      decoration: BoxDecoration(
          color: color,
          borderRadius: BorderRadius.circular(4),
          border: Border.all(color: APP_SECONDARY_COLOR, width: 1)
      ),
    );
  }
}

class ContainerBg extends StatelessWidget {
  final Widget child;
  final double? width;
  final double? height;
  const ContainerBg({Key? key, required this.child, this.width, this.height}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      width: width,
      height: height,
      padding: EdgeInsets.all(4),
      margin: EdgeInsets.all(1),
      child: child,
      alignment: Alignment.center,
      decoration: BoxDecoration(
          color: Colors.white10,
          borderRadius: BorderRadius.circular(4)
      ),
    );
  }
}

class InrDcrButton extends StatefulWidget {
  final int time;
  final Function(int)onPressed;
  final String unit;//T for time, D for Dollar
  const InrDcrButton({Key? key, required this.time, required this.onPressed, this.unit='T'}) : super(key: key);

  @override
  _InrDcrButtonState createState() => _InrDcrButtonState();
}

class _InrDcrButtonState extends State<InrDcrButton> {
  int _time = 1;
  String _unit = 'min';
  @override
  void initState() {
    _time = widget.time;
    _unit = widget.unit;
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.only(top: 8, bottom: 8, left: 16, right: 16),
      child: Row(children: [
        Expanded(child: GestureDetector(onTap: () {
          setState(() {
            if (_time > 1 && widget.unit == 'T') {
              if (_time <= 10) {
                _time -= 1;
              } else if (_time > 10 && _time <= 30) {
                _time -= 5;
              } else if (_time > 30 && _time <= 60) {
                _time -= 30;
              } else if (_time > 60 && _time <= 1440) {
                _time -= 60;
              }else if(_time > 1440){
                _time -= 1440;
              }
            }else if(_time>1 && widget.unit=='\$'){
              if (_time <= 10) {
                _time -= 1;
              } else if (_time > 10 && _time <= 50) {
                _time -= 5;
              } else if (_time > 50 && _time <= 100) {
                _time -= 10;
              } else if (_time > 100 && _time <=1000) {
                _time -= 100;
              }else if(_time > 1000){
                _time -=1000;
              }
            }
          });

          if(widget.unit == 'T'){
            setDuration(_time);
          }else{
            setAmount(_time);
          }

          widget.onPressed(_time);

        }, child: Icon(Ionicons.remove, color: APP_SECONDARY_COLOR,)), flex: 25,),
        Expanded(child: Text(_calculateData(_time), textAlign: TextAlign.center,
          style: textStyleHeading(),), flex: 50,),
        Expanded(child: GestureDetector(onTap: (){
          setState(() {
            if (widget.unit == 'T') {
              if (_time < 10) {
                _time += 1;
              } else if (_time >= 10 && _time < 30) {
                _time += 5;
              } else if (_time >= 30 && _time < 60) {
                _time += 30;
              } else if (_time >= 60 && _time < 1440) {
                _time += 60;
              }else if (_time >= 1440) {
                _time += 1440;
              }
            }else if(widget.unit=='\$'){
              if (_time < 10) {
                _time += 1;
              } else if (_time >= 10 && _time < 50) {
                _time += 5;
              } else if (_time >= 50 && _time < 100) {
                _time += 10;
              } else if (_time >= 100 && _time <1000) {
                _time += 100;
              }else if(_time >= 1000){
                _time +=1000;
              }
            }
          });

          if(widget.unit == 'T'){
            setDuration(_time);
          }else{
            setAmount(_time);
          }

          widget.onPressed(_time);
        }, child: Icon(Icons.add, color: APP_SECONDARY_COLOR,)), flex: 25,),
      ],),
      decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(4),
          border: Border.all(color: APP_SECONDARY_COLOR, width: 1)
      ),
    );
  }

  String _calculateData(int time){


    if(widget.unit=='T'){
      double val1 = _time/60;
      int val2 = val1.toInt();
      if(val2>0){
        double day = val2/24;
        int day1 = day.toInt();
        if(day1>0){
          print('day');
          _unit = '\$';
          return '$day1 day';
        }else{
          print('hours');
          _unit = 'hrs';
          return '$val2 $_unit';
        }
      }else {
        print('min');
        _unit = 'min';
        return '$_time $_unit';
      }

    }else{
      return '\$ $time';
    }

  }
}

class TradeButton extends StatelessWidget {
  final Color color;
  final bool isTradeUp;
  final Function()onPressed;
  const TradeButton({Key? key, required this.color, this.isTradeUp=false, required this.onPressed}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      child: Container(
        width: double.infinity,
        padding: EdgeInsets.all(8),
        // height: 40,
        child: Row(children: [
          Expanded(child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(isTradeUp?'Up':'Down', style: textStyleHeading2(color: Colors.white),),
              Text(isTradeUp?'+90%  Win':'+90%  Win', style: textStyleLabel(color: Colors.white),),
            ],)),
          isTradeUp?Transform.rotate(angle: 45*math.pi/180, child:
          Icon(Icons.arrow_upward, color: Colors.white,),)
              :Transform.rotate(angle: 135*math.pi/180, child:
          Icon(Icons.arrow_upward, color: Colors.white,),)
        ],),
        decoration: BoxDecoration(
            color: color,
            borderRadius: BorderRadius.circular(4)
        ),
      ),
      onTap: onPressed,
    );
  }
}

class NoData extends StatelessWidget {
  const NoData({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          SizedBox(height: 40,),
          Container(
            padding: EdgeInsets.all(16),
            child: Icon(FontAwesomeIcons.mehBlank, color: APP_SECONDARY_COLOR.withOpacity(0.5),),
            decoration: BoxDecoration(
                color: Colors.white10,
                shape: BoxShape.circle
            ),
          ),
          SizedBox(height: 24,),
          Text('Your list of trades is empty', style: TextStyle(color: Colors.white54,fontWeight: FontWeight.w500),),
          Text('Go back to the chart if you want to open trades', style: TextStyle(color: Colors.white54,fontWeight: FontWeight.w500)),
        ],
      ),
    );
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
        Expanded(child: Text('$title', style: textStyleHeading(color: Colors.white),),flex: 30,),
        Container(width: 0.5, color: APP_SECONDARY_COLOR.withOpacity(0.2), margin: EdgeInsets.all(4),),
        Expanded(child: child,flex: 70,),
      ],),
      decoration: BoxDecoration(
          border: Border(bottom: BorderSide(color: APP_SECONDARY_COLOR.withOpacity(0.1), width: 1))
      ),
    );
  }
}

class SettingItem extends StatelessWidget {
  final String title;
  final String subTitle;
  final IconData leadingIcon;
  final Function() onTap;
  const SettingItem({Key? key, required this.title,
    required this.subTitle, required this.leadingIcon, required this.onTap}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      child: SettingItemBg(child: ListTile(title: Text(title, style: textStyleHeading2(color: Colors.white),),
        subtitle: Text(subTitle, style: textStyleLabel(color: Colors.white54),),
        leading: Icon(leadingIcon, color: Colors.white54,),
        trailing: Icon(Icons.chevron_right, color: Colors.white54,),),),
      onTap: onTap,
    );
  }
}

class SettingItemBg extends StatelessWidget {
  final Widget child;
  final double? height;
  final EdgeInsets padding;
  final EdgeInsets margin;
  const SettingItemBg({Key? key, required this.child, this.height,
    this.padding=const EdgeInsets.all(0), this.margin=const EdgeInsets.all(4)}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      height: height,
      child: child,
      padding: padding,
      margin: margin,
      decoration: BoxDecoration(
          color: Colors.white10,
          borderRadius: BorderRadius.circular(8)
      ),
    );
  }
}

class UpdateView extends StatelessWidget {
  final String title;
  final String message;
  final String btnText;
  final Function()onPressed;
  const UpdateView({Key? key, required this.title, required this.message, required this.onPressed, this.btnText='Continue'}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    return Center(child: Column(
      mainAxisSize: MainAxisSize.min,
      children: [

        Image.asset(playStoreIcon, width: size.width/2, height: size.height/10,),
        SizedBox(height: 24,),
        Text(title, style: textStyleHeading(),),
        SizedBox(height: 24,),
        Text(message,
          style: textStyleHeading(color: Colors.white70), textAlign: TextAlign.center,),

        SizedBox(height: 24,),
        SubmitButton(onPressed: onPressed, title: btnText),
        SizedBox(height: 40,)
      ],),);
  }
}




class CommonButton extends StatelessWidget {

  final Function() onPressed;
  final String buttonName;
  final int buttoncolors;
  final Color buttontextcolor;
  final double height;
  final double width;
  final double borderRadius;


  const CommonButton({Key? key,required this.onPressed,required this.buttonName,required this.buttoncolors,
    required this.buttontextcolor,required this.height,required this.width,required this.borderRadius}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return  Padding(
      padding: const EdgeInsets.fromLTRB(10.0, 15, 10, 0),
      child: SizedBox(
        // height: 50,
        // width: 150,
        height: height,
        width: width,
        child: ElevatedButton(
            onPressed: onPressed,
            style: ButtonStyle(
                backgroundColor:MaterialStateProperty.all<Color>(Color(buttoncolors)),
                foregroundColor: MaterialStateProperty.all<Color>(Color(buttoncolors)),
                shape: MaterialStateProperty.all<RoundedRectangleBorder>(
                    RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(borderRadius),
                        side: BorderSide(color: Color(buttoncolors))))),
            child: Center(
              child: Text(
                buttonName.toUpperCase(),textAlign: TextAlign.center,style:
              GoogleFonts.adamina(color: buttontextcolor,fontSize: 12,
                  fontWeight: FontWeight.w600),
              ),
            )),
      ),
    );
  }
}



class CommonSqureTextField extends StatelessWidget {

  final TextEditingController? controller;
  final String? text;
  final String? labelText;
  final String? hintText;
  final String? valText;
  final TextInputAction? action;
  //final Function (FieldData)? onChange;
  final bool IsEnable;
  final int limitCharlength;
  final int maxLength;
  const CommonSqureTextField({Key? key,this.text,this.labelText,this.hintText,this.valText,
    this.action,this.controller, required this.IsEnable,required this.limitCharlength
    ,required this.maxLength}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: TextFormField(
        enabled: IsEnable,
        style: TextStyle(color:APP_PRIMARY_COLOR),
        autofocus: true,
        inputFormatters: [
          FilteringTextInputFormatter.allow(RegExp(r'[A-Z]|[ ]|[a-z]')),
          LengthLimitingTextInputFormatter(limitCharlength),
        ],
        textCapitalization: TextCapitalization.sentences,
        textInputAction: TextInputAction.next,
        cursorColor: APP_PRIMARY_COLOR,
        controller: controller,
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


