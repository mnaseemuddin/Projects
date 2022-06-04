import 'package:flutter/material.dart';

import '../shared.dart';
import 'common_widget.dart';

class SAToggleButton extends StatefulWidget {
  final bool isExpand;
  final List<String>list;
  const SAToggleButton({Key? key, required this.list, required this.isExpand}) : super(key: key);

  @override
  _SAToggleButtonState createState() => _SAToggleButtonState();
}

class _SAToggleButtonState extends State<SAToggleButton> {

  late List<bool>_statusOrder;// = List<bool>.generate(3, (index) =>false);
  List<String>_list = [];

  @override
  void initState() {
    _list = widget.list;
    _statusOrder = List<bool>.generate(widget.list.length, (index) =>false);
    super.initState();
  }
  @override
  Widget build(BuildContext context) {


    _list = widget.isExpand?widget.list:widget.list.sublist(0,8);

    return Wrap(children: _createItem(),);
  }

  List<Widget> _createItem(){
    List<Widget>list = [];
    _list.asMap().forEach((index, e){
      list.add(GestureDetector(child: FilterItem(title: e, isSelected: _statusOrder.elementAt(index),),
      onTap: (){
        setState(() {
          _statusOrder = List<bool>.generate(_list.length, (index) =>false);
          _statusOrder[index] = !_statusOrder[index];
        });
      },));
    });
    return list;
  }
}

class FilterItem extends StatelessWidget {
  final String title;
  final bool isSelected;
  const FilterItem({Key? key, required this.title, this.isSelected=false}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;

    return Container(
      width: size.width/4.8,
      alignment: Alignment.center,
      padding: EdgeInsets.symmetric(horizontal: 8, vertical: 8),
      margin: EdgeInsets.only(right: 8,top: 8),
      child: FittedBox(child: Text(title, style: textStyleLabel(color: isSelected?ColorConstants.APP_PRIMARY_COLOR:ColorConstants.white),),),
      decoration: BoxDecoration(
          color:  isSelected?const Color(0xFFF7C154):ColorConstants.white10,
          borderRadius: BorderRadius.circular(4)
      )
    );
  }
}
