import 'dart:async';

import 'package:flutter/material.dart';
import 'package:royal_q/main.dart';

import '../shared.dart';

class SATabview extends StatefulWidget {
  final List<String>titles;
  final int selected;
  final StreamController<int> onPageChange;
  final Function(int)onSelect;
  final Color selectedColor;
  final Color textColor;
  final Color unselectTextColor;
  final bool expand;
  final bool isButton;
  final bool isScroll;
  const SATabview({Key? key, required this.titles, this.selected=0, required this.onPageChange, required this.onSelect,
    this.selectedColor=ColorConstants.APP_SECONDARY_COLOR, this.textColor= ColorConstants.white, this.expand=false, this.isButton=false, this.unselectTextColor=ColorConstants.white54, this.isScroll=false}) : super(key: key);

  @override
  _SATabviewState createState() => _SATabviewState();
}

class _SATabviewState extends State<SATabview> {
  int _selected = 0;
  @override
  void initState() {
    _selected = widget.selected;

    widget.onPageChange.stream.listen((event) {
      if(mounted) {
        setState(() {
        _selected = event;
      });
      }
    });
    super.initState();
  }

  @override
  void dispose() {
    // widget.onPageChange.close();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return _createItemList();
  }

  Widget _createItemList(){
    List<Widget>list = [];
    widget.titles.asMap().forEach((key, value) {
      list.add(GestureDetector(
        child: _tabItem(title: value, isSelect: key==_selected),
        onTap: (){
          widget.onSelect(key);
        },
      ));
    });

    return widget.isScroll?ListView(
      children: list, scrollDirection: Axis.horizontal,):Row(
      mainAxisSize: widget.expand?MainAxisSize.max:MainAxisSize.min,
      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
      crossAxisAlignment: CrossAxisAlignment.start,
      children: list,);
    // return ListView(
    //   children: list, scrollDirection: Axis.horizontal,);
  }
  Widget _tabItem({title, isSelect=0}) => Container(
    margin: EdgeInsets.only(right: 8, bottom: 4),
    padding: EdgeInsets.symmetric(horizontal: 8),
    alignment: Alignment.center,
    // child: Text(title, style: textStyleLabel(fontSize: isSelect?16.0:14.0,color: isSelect?widget.textColor: isPlatformIOS?Colors.black54:Colors.white54),),
    child: widget.isButton?Text(title,overflow: TextOverflow.ellipsis, style: textStyleLabel(fontSize: isSelect?16.0:14.0,
        color: ColorConstants.black),)
    :Text(title,overflow: TextOverflow.ellipsis, style: textStyleLabel(fontSize: isSelect?16.0:14.0,color:
    isSelect?widget.textColor: widget.unselectTextColor),),
    decoration: BoxDecoration(
        gradient: null,
        border: widget.isButton?Border.all(width: 1, color: ColorConstants.black)
        :Border(bottom: BorderSide(width: 2, color: isSelect?widget.selectedColor:Colors.transparent)),
        borderRadius: widget.isButton?BorderRadius.circular(8):null
        // border: Border(bottom: BorderSide(width: 2, color: isSelect?widget.selectedColor:Colors.transparent))
    ),
  );
}
