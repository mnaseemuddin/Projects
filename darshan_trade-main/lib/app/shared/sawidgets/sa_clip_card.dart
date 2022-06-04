import 'dart:ui' as ui;

import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:google_fonts/google_fonts.dart';

class SAClipCard extends StatelessWidget {
  final String balance,package;
  const SAClipCard({Key? key, required this.balance,required this.package}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    Size size  = MediaQuery.of(context).size;
    return Container(
      width: 200,
      height: 180,
      // alignment: Alignment.centerLeft,
      child: CustomPaint(

        child: Row(children: [
          SizedBox(width: 16,),
          Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text('Current Package \nAmount \$$package',maxLines: 3,style: GoogleFonts.rajdhani(
                  fontSize: 15, fontWeight: FontWeight.w700, color: Colors.white
              ),),
              SizedBox(height: 16,),
              Text('\$$balance', style: GoogleFonts.rajdhani(
                  fontSize: 24, fontWeight: FontWeight.w700, color: Colors.white
              ),),

              SizedBox(height: 16,),

              Text('Total Balance', style: GoogleFonts.rajdhani(
                  fontSize: 16, fontWeight: FontWeight.w700, color: Colors.white
              ),)

            ],)
        ],),
        painter: CircleCanvas(Color(0xFFFF5555)),
      ),
      decoration: BoxDecoration(
        // boxShadow: [BoxShadow(color: Color(0x2FFF5555), offset: Offset(4, 4), blurRadius: 6)]
      ),
    );
  }
}


class CircleCanvas extends CustomPainter{
  Color color;

  CircleCanvas(this.color);

  @override
  void paint(Canvas canvas, Size size) {
    // TODO: implement paint
    final Paint paint =  Paint()
      ..shader = ui.Gradient.linear(Offset(0, 10), Offset(size.width, size.height), [
        Color(0xFFFD7F7F),
        Color(0xFFFF5555),
      ])
      ..style = PaintingStyle.fill;

    double perWidth = size.width/100;
    double perHeight = size.height/100;

    var path = Path();
    path.moveTo(10*perWidth,  0*perHeight);
    path.quadraticBezierTo(0, 0,    0*perWidth, 20*perWidth);
    path.lineTo(0*perWidth,   20*perHeight);
    path.lineTo(0*perWidth,   90*perHeight);

    path.quadraticBezierTo(0, 100*perHeight,    20*perWidth, 100*perHeight);
    path.lineTo(0*perWidth,   100*perHeight);

    path.lineTo(60*perWidth, 100*perHeight);

    path.quadraticBezierTo(80*perWidth, 100*perHeight, 100*perWidth, 0*perHeight);

    path.lineTo(100*perWidth, 0*perHeight);

    // path.quadraticBezierTo(100*perWidth, 0*perHeight, 100*perWidth, 0*perHeight);

    path.lineTo(20*perWidth,  0*perHeight);

    canvas.drawPath(path, paint);

    // canvas.drawCircle(Offset(80*perWidth, 0*perHeight), 5, paint..color=Colors.red);
    // canvas.drawCircle(Offset(100*perWidth, 0*perHeight), 5, paint..color=Colors.orange);
  }

  @override
  bool shouldRepaint(CircleCanvas oldDelegate) {
    // TODO: implement shouldRepaint
    return true;
  }
}