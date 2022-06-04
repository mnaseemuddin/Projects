

import 'package:flutter/material.dart';

class ContainerBgRCircle extends StatelessWidget {
  final double? height,width,circular;
  final Color color;
 final Widget child;
   const ContainerBgRCircle({Key? key,this.width,this.height,
   required this.color,this.circular,required this.child}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      height: height,
      width: width,
      child: child,
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(circular!),
        color: color,
      ),
    );
  }
}


class MarketContainerBg extends StatelessWidget {
  final double? height,width,circular;
  final Color color;
  final Widget child;
    MarketContainerBg({Key? key,required this.width,
    required this.color,required this.circular,required this.child,this.height}) : super(key: key);


  @override
  Widget build(BuildContext context) {
    return Container(
      height: height,
      width: width,
      child: child,
      decoration: BoxDecoration(
        borderRadius: BorderRadius.only(topLeft: Radius.circular(circular!),
            topRight: Radius.circular(circular!)),
        color: color,
      ),
    );
  }
}



