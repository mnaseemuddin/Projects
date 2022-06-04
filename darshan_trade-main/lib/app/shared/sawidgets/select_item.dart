import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:royal_q/app/shared/sawidgets/sa_appbar.dart';
import 'package:royal_q/app/shared/widgets/affliate_bg.dart';
import 'package:royal_q/main.dart';
import '../shared.dart';
import 'common_widget.dart';
import 'country_flags.dart';

class SelectItem extends StatefulWidget {
  final Function(String)onChangeLocation;
  const SelectItem({Key? key, required this.onChangeLocation}) : super(key: key);

  @override
  _SelectItemState createState() => _SelectItemState();
}

class _SelectItemState extends State<SelectItem> {
  String? _location;
  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      child: Container(
        height: 60,
        padding: EdgeInsets.all(8),
        decoration: textBg,
        child: Row(children: [
          Expanded(child: Text(_location??'Select location',
            style: textStyleLabel(color:  ColorConstants.white, fontSize: 14.0),)),
          Icon(Icons.arrow_drop_down, color: _location !=null?ColorConstants.APP_SECONDARY_COLOR:ColorConstants.white54,)
        ],),
      ),
      onTap: () async{
        var code = await navPush(context, CountryFlags());
        widget.onChangeLocation(code['code']);
       setState(() =>  _location = code['code']);
      },
    );
  }

}

class AFSelectItem extends StatefulWidget {
  final Function(String, String)onChangeLocation;
  const AFSelectItem({Key? key, required this.onChangeLocation}) : super(key: key);

  @override
  _AFSelectItemState createState() => _AFSelectItemState();
}

class _AFSelectItemState extends State<AFSelectItem> {
  String? _location;
  String? _locationCode;
  @override
  Widget build(BuildContext context) {
    return AFBg(child: GestureDetector(
      child: Container(
        margin: EdgeInsets.symmetric(horizontal: 8, vertical: 4),
        height: 50,
        padding: EdgeInsets.all(8),
        child: Row(children: [
          Expanded(child: Text(_location??'Select location',
            style: textStyleLabel(color:  Colors.white, fontSize: 14.0),)),
          Icon(Icons.arrow_drop_down, color: Colors.white,)
        ],),

        decoration: BoxDecoration(
            gradient: LinearGradient(colors: [
              Color(0xFF292F33),
              Color(0xFF232D35),
            ]),
            borderRadius: BorderRadius.circular(8),
            border: Border.all(color: Colors.white24, width: 1)
        ),
      ),
      onTap: () async{
        var code = await navPush(context, CountryFlags());
        widget.onChangeLocation(code['code'], '${code['name']}');
        setState((){
          _location     = code['code'];
          _locationCode = '${code['name']}';
        });
      },
    ));
  }

}



class DropDownSelect extends StatelessWidget {
  final String title;
  final List<DropItem> list;
  final Function(int)onSelect;
  const DropDownSelect({Key? key, required this.title, required this.list, required this.onSelect}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return AFBg(child: Scaffold(
      backgroundColor: Colors.transparent,
      body: SafeArea(child: Column(children: [
        SHAppbar(title: title),
        SizedBox(height: 16,),
        Expanded(child: ListView.builder(itemBuilder: (context, index){
          DropItem item = list.elementAt(index);
           return GestureDetector(
             child: Container(
               margin: EdgeInsets.symmetric(horizontal: 24, vertical: 4),
               padding: EdgeInsets.symmetric(horizontal: 8, vertical: 8),
               child: Row(children: [
                 SizedBox(width: 8,),
                 Text(item.title, style: GoogleFonts.rajdhani(
                     fontSize: 18, fontWeight: FontWeight.w500, color: Colors.white
                 ),),
                 Spacer(),
                 Icon(Icons.arrow_forward_ios, color: Color(0xFFFF5555),)
               ],),
               decoration: BoxDecoration(gradient: LinearGradient(colors: [
                 Color(0xFF292F33),
                 Color(0xFF232D35),
               ]),
                   borderRadius: BorderRadius.circular(8),
                   border: Border.all(color: Colors.white12),
                   boxShadow: [BoxShadow(color: Colors.black45, offset: Offset(4, 4), blurRadius: 6)]
               ),

             ),
             onTap: (){
               onSelect(item.index);
               Get.back();
             },
           );
        }, itemCount: list.length,))
      ],)),
    ));
  }
}



class DropItem{
  final int index;
  final String title;

  DropItem({required this.index, required this.title});
}
