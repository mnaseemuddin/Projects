import 'dart:async';
import 'package:flutter/material.dart';
import 'package:royal_q/app/shared/shared.dart';


class SATabview extends StatefulWidget {
  final List<String>titles;
  final int selected;
  final StreamController<int> onPageChange;
  final Function(int)onSelect;
  final Color selectedColor;
  final Color textColor;
  final bool expand;
  const SATabview({Key? key, required this.titles, this.selected=0, required this.onPageChange, required this.onSelect,
    this.selectedColor=const Color(0xFFFF0000), this.textColor=ColorConstants.white, this.expand=false}) : super(key: key);

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
    widget.onPageChange.close();
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

    return Row(
      mainAxisSize: widget.expand?MainAxisSize.max:MainAxisSize.min,
      mainAxisAlignment: MainAxisAlignment.spaceAround,
      crossAxisAlignment: CrossAxisAlignment.start,
      children: list,);
  }
  Widget _tabItem({title, isSelect=0}) => Container(
    margin: EdgeInsets.only(right: 24),
    alignment: Alignment.center,
    child: Text(title, style: TextStyle(fontSize: isSelect?16.0:14.0,color: isSelect?widget.textColor: ColorConstants.white54),),
    decoration: BoxDecoration(
        border: Border(bottom: BorderSide(width: 2, color: isSelect?widget.selectedColor:Colors.transparent))
    ),
  );
}
