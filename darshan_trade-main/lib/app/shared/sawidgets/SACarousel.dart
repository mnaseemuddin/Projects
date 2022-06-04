import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:royal_q/app/shared/shared.dart';

class SACarousel extends StatefulWidget {
  final double width;
  final double height;
  final List<Widget> list;
  final int duration;
  const SACarousel({Key? key, required this.width, required this.height, required this.list, this.duration=2}) : super(key: key);

  @override
  _SACarouselState createState() => _SACarouselState();
}

class _SACarouselState extends State<SACarousel> {
  PageController _controller = PageController();
  Timer? _timer;
  int _counter = 0;
  double _opacity = 1;
  @override
  void initState() {
    _timer = Timer.periodic(Duration(seconds: widget.duration), (timer) {
      setState(() {
        _counter = _counter<widget.list.length-1?_counter+1:0;
        if(_counter==0){
          _controller.jumpToPage(_counter);
          // _opacity = 0.5;
          // Timer(Duration(milliseconds: 10), ()=>_opacity=1);
        }else
        _controller.animateToPage(_counter, duration: Duration(milliseconds: 500), curve: Curves.easeIn).then((value){
          print('object => $_counter');
        });
      });
    });
    super.initState();
  }

  @override
  void dispose() {
    if(_timer!=null)_timer!.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return AnimatedOpacity(opacity: _opacity, duration: Duration(milliseconds: 50),
    child: Container(

      width: widget.width,
      height: widget.height,
      child: Stack(
        alignment: Alignment.bottomCenter,
        children: [
          PageView.builder(
            controller: _controller,
            onPageChanged: (page){
              setState(() {
                _counter = page;
              });
            },
            itemBuilder: (context, index){
              return widget.list.elementAt(index);
            }, itemCount: widget.list.length,),
          _createIndicator()
        ],),
    ),);
  }
  
  Widget _createIndicator(){
    List<Widget> list = [];
    widget.list.asMap().forEach((index, element) { 
      double radius = index==_counter?10:8;
      list.add(Container(
        margin: EdgeInsets.all(4),
        width: radius, height: radius,
        decoration: BoxDecoration(
          color: ColorConstants.white,
          shape: BoxShape.circle
        ),
      ));
    });
    
    return Row(
      mainAxisSize: MainAxisSize.min,
      children: list,);
  }
}
