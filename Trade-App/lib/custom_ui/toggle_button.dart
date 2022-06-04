import 'package:flutter/material.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/res/strings.dart';


class ToggleButton extends StatefulWidget {
  final bool selected;
  final Function(bool)onSelect;
  const ToggleButton({Key? key, required this.onSelect, this.selected=true}) : super(key: key);

  @override
  _ToggleButtonState createState() => _ToggleButtonState();
}

class _ToggleButtonState extends State<ToggleButton> {

  bool _selected = true;

  @override
  void initState() {
    setState(() => _selected = widget.selected);
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Center(child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          SelectionButton(text: REGISTRATION, cornerLeft: true, selected: !_selected, onSelect: (val){
            setState(() => _selected = false);
            widget.onSelect(false);
          },),
          SelectionButton(text: LOGIN,cornerLeft: false, selected: _selected, onSelect: (val){
            setState(() => _selected = true);
            widget.onSelect(true);
          },),

        ],),),
    );
  }
}


class SelectionButton extends StatelessWidget {
  final bool selected;
  final bool cornerLeft;
  final String text;
  final double width;
  final Function(bool)onSelect;
  const SelectionButton({Key? key, this.selected=false, this.cornerLeft=true, required this.text, required this.onSelect, this.width=120}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(onTap: () => onSelect(!selected), child: Container(
      width: width,
      alignment: Alignment.center,
      padding: EdgeInsets.only(left: cornerLeft?16:8, right: cornerLeft?8:16, bottom: 8, top: 8),
      child: Text(text, style: TextStyle(
          color: selected?APP_PRIMARY_COLOR:APP_SECONDARY_COLOR
      ),),

      decoration: BoxDecoration(
          color: selected?APP_SECONDARY_COLOR:Colors.transparent,
          borderRadius: BorderRadius.only(
            bottomLeft: Radius.circular(cornerLeft?100:0),
            topLeft: Radius.circular(cornerLeft?100:0),
            bottomRight: Radius.circular(cornerLeft?0:100),
            topRight: Radius.circular(cornerLeft?0:100),
          ),
          border: Border.all(color: APP_SECONDARY_COLOR, width: 1)
      ),
    ));
  }
}