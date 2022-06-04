import 'dart:async';
import 'dart:typed_data';
import 'dart:ui' as ui;

import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:trading_apps/data_streamer.dart';
import 'package:trading_apps/models/common_model.dart';
import 'package:trading_apps/models/graph_model.dart';
import 'package:trading_apps/res/colors.dart';
import 'dart:math' as math;

import 'package:trading_apps/res/constants.dart';



class MainGraph extends CustomPainter{

  final int widthFactor;
  final Offset onOffsetChange;
  final int difference;
  final int bidValue;
  final Function(Trade)onCmpleted;
   MainGraph({this.widthFactor=4,
    required this.onOffsetChange, this.difference=60000, this.bidValue=1,
     required this.onCmpleted, });



  @override
  void paint(Canvas canvas, Size size) {

    int mSize = axisY.length;
    List<Offset> points     = List<Offset>.generate(mSize, (index) => Offset.zero);
    List<Offset> pointsCon1 = List<Offset>.generate(mSize, (index) => Offset.zero);
    List<Offset> pointsCon2 = List<Offset>.generate(mSize, (index) => Offset.zero);

    if(axisY.length>1){
      axisY.sort((a, b)=> a.at.compareTo(b.at));
      double width = size.width;
      double height = 9*size.height/10;

      GraphModel first = axisY.last;



      double maxVal = first.ticker.high>first.ticker.last?first.ticker.high:first.ticker.last;
      double minVal = first.ticker.low<first.ticker.last?first.ticker.low:first.ticker.last;


      double maxMinDiff = (first.ticker.high - first.ticker.low)/200;

      mMaxVal = mMaxVal<(first.ticker.last + maxMinDiff)?(first.ticker.last + maxMinDiff):mMaxVal;
      mMinVal = mMinVal>(first.ticker.last - maxMinDiff)?(first.ticker.last - maxMinDiff):mMinVal;

      maxVal = mMaxVal;
      minVal = mMinVal;

      // graphMax = graphMax<maxVal?maxVal:graphMax;
      // graphMin = graphMin>minVal?minVal:graphMin;
      graphMax = mMaxVal;
      graphMin = mMinVal;

      maxVal = graphMax;
      minVal = graphMin;

      print('Paint minVal => $graphMin, graphMax => $graphMax');
      double diff = maxVal - minVal;

      double yFactor = (height)/(diff>0?diff:height);
      double baseY = height;
      double oneSec = width/(durationFactor*widthFactor);
      double startPoint = 7*width/8;

      int timestamp = DateTime.now().millisecondsSinceEpoch;

      for(int i=0; i<mSize; i++){
        GraphModel model = axisY.elementAt(i);
        double xV = startPoint -((timestamp - model.at).abs())*oneSec;
        double y = model.ticker.last;
        double val = y - minVal;
        double vY = baseY - val*yFactor;
        points[i] = Offset(xV, vY);
      }

      points[0]=Offset(points[0].dx, baseY);
      points[mSize-1] = Offset(startPoint, baseY);

      for (int i = 1; i < mSize; i++) {
        pointsCon1[i] = Offset((points[i].dx + points[i - 1].dx) / 2, points[i - 1].dy );
        pointsCon2[i] = Offset((points[i].dx + points[i - 1].dx) / 2, points[i].dy );
      }

      Path mTestCubicPath = new Path();
      mTestCubicPath.moveTo(points[0].dx, points[0].dy);

      Path linePath = new Path();
      linePath.moveTo(0, points[0].dy);
      linePath.lineTo(points[0].dx, points[0].dy);

      for (int i = 1; i < mSize; i++) {
        mTestCubicPath.cubicTo(pointsCon1[i].dx, pointsCon1[i].dy, pointsCon2[i].dx, pointsCon2[i].dy, points[i].dx, points[i].dy);
        if(i<mSize-1)
          linePath.cubicTo(pointsCon1[i].dx, pointsCon1[i].dy, pointsCon2[i].dx, pointsCon2[i].dy, points[i].dx, points[i].dy);
      }

      Rect rect = new Rect.fromLTWH(0, 0, width, height);
      final Gradient gradient = LinearGradient(
          begin: Alignment.center, end: Alignment.bottomCenter,
          colors: [APP_SECONDARY_COLOR, Colors.black]);


      Paint mLinePaint = new Paint();
      mLinePaint.style = PaintingStyle.fill;
      mLinePaint.color = Colors.blue.withOpacity(0.15);
      mLinePaint..shader = gradient.createShader(rect);

      Paint mLinePaint1 = new Paint();
      mLinePaint1.strokeWidth = 0.8;
      mLinePaint1.style = PaintingStyle.stroke;
      mLinePaint1.color = APP_SECONDARY_COLOR;

      _drawLines(canvas, size, points, minVal, maxVal, yFactor);

      canvas.drawPath(mTestCubicPath, mLinePaint);
      canvas.drawPath(linePath, mLinePaint1);


      Paint linePaint = Paint()..color=Colors.white;
      canvas.drawCircle(points[mSize-2], 4, linePaint);

      canvas.drawLine(Offset(0, points[mSize-2].dy), Offset(points[mSize-2].dx, points[mSize-2].dy), linePaint..strokeWidth=0.2);
      canvas.drawLine(Offset(points[mSize-2].dx, points[mSize-2].dy), Offset(width, points[mSize-2].dy), linePaint..strokeWidth=1.5);

      RRect rRect = RRect.fromRectAndRadius(Rect.fromLTWH(width-76 - onOffsetChange.dx,points[mSize-2].dy -10, 80, 20), Radius.circular(10));
      canvas.drawRRect(rRect, linePaint);
      double lastValue = axisY.last.ticker.last;

      TextPainter mTextPainter = TextPainter(
          text: TextSpan(
            text: '${lastValue.toStringAsFixed(lastValue>100?3:5)}',
            style: TextStyle(
              color: Colors.black,
              fontSize: 12,
            ),
          ),
          textDirection: TextDirection.ltr,
          textAlign: TextAlign.center
      );
      mTextPainter.layout(
        minWidth: 0,
        maxWidth: 90,
      );
      mTextPainter.paint(canvas, Offset(width-60 -onOffsetChange.dx, points[mSize-2].dy-8));

      trade.forEach((element) {
        if(element.isActive&&baseMarket==element.currency){
          GraphModel model = element.model;

          double y = model.ticker.last;
          double val = y - minVal;
          double dy = baseY - val*yFactor;
          double tx = startPoint + (model.at - timestamp)*oneSec;
          _drawTrades(canvas, size, element, Offset(tx, dy),
              element.type == UpDownType.UP);
        }
      });
    }

  }

  _drawLines(Canvas canvas, Size size, List<Offset> points, double min, double max, double yFactor){

    double width = size.width;
    double height = size.height;

    double startPoint = 7*width/8;

    double oneSec = width/(durationFactor*widthFactor);
    _createBaseLine(canvas, size);

    Color color = APP_SECONDARY_COLOR.withOpacity(0.3);
    Paint paintLine = new Paint()..color=color;

    double diff = max - min;
    double diffFactor = diff/10;

    for(int i=0; i<10; i++){
      double val = min+(10 - i)*diffFactor;

      final textPainter = TextPainter(
        text: TextSpan(
          text: val.toStringAsFixed(val>100?3:5),
          style: TextStyle(
            color: Colors.white54,
            fontSize: 12,
          ),
        ),
        textDirection: TextDirection.rtl,
      );
      textPainter.layout(
        minWidth: 0,
        maxWidth: width,
      );
      final xCenter = (width - textPainter.width-4);
      final yCenter = i*height/10-textPainter.height/2;
      final offset = Offset(xCenter - onOffsetChange.dx, yCenter);
      canvas.drawLine(Offset(0, i*height/10), Offset(width- textPainter.width-6, i*height/10), paintLine);
      textPainter.paint(canvas, offset);
    }

    // Dotted line draw
    Paint paint = new Paint();
    paint..shader = _shader()
      ..style = PaintingStyle.fill
      ..strokeWidth = 0.5;
    canvas.drawLine(Offset(startPoint, 0), Offset(startPoint, height), paint);

    canvas.drawLine(Offset(points[points.length-2].dx + difference*oneSec, 0),
        Offset(points[points.length-2].dx + difference*oneSec, height),
        Paint()..color=Colors.white54..strokeWidth=0.5);
  }

  _drawTrades(Canvas canvas, Size size, Trade trade, Offset offset, bool isUp){

    double width = size.width;
    double height = size.height;
    double oneSec = width/(durationFactor*widthFactor);
    Color color = isUp?Colors.green:Colors.red;
    canvas.drawLine(Offset(offset.dx-size.width/18+70, offset.dy), Offset(offset.dx + (trade.duration)*oneSec+50, offset.dy),
        Paint()..color=color..strokeWidth=0.5..shader=_shader(color: color));

    canvas.drawLine(Offset(offset.dx , offset.dy), Offset(offset.dx + (trade.duration)*oneSec, offset.dy),
        Paint()..color=color..strokeWidth=1);

    Rect rectUp = Rect.fromLTWH(offset.dx-size.width/18, offset.dy-10, 70, 20);
    canvas.drawRRect(RRect.fromRectAndRadius(rectUp, Radius.circular(10)), Paint()..color=color.withOpacity(0.7));

    _baseLabelPainter('\$${trade.bidValue}').paint(canvas, Offset(rectUp.center.dx-20, rectUp.top+2));

    int dx = 4;
    canvas.drawPath(_createPoint(offset, dx), Paint()..color=Colors.white..style=PaintingStyle.fill);
    canvas.drawPath(_createPoint(Offset(offset.dx + (trade.duration)*oneSec, offset.dy), dx),
        Paint()..color=Colors.white..style=PaintingStyle.fill);

  }

  Path _createPoint(Offset offset, int dx){
    Path path = Path();
    path.moveTo(offset.dx - dx,   offset.dy + dx*0);
    path.lineTo(offset.dx - dx*0, offset.dy - dx*1);
    path.lineTo(offset.dx + dx*1, offset.dy - dx*0);
    path.lineTo(offset.dx + dx*0, offset.dy + dx*1);
    path.lineTo(offset.dx - dx*1, offset.dy + dx*0);

    return path;
  }

  Shader _shader({Color color=APP_SECONDARY_COLOR}) =>  LinearGradient(
      colors: [color, Colors.transparent],
      stops: [0.5, 0.5],
      tileMode: TileMode.repeated,
      transform: GradientRotation(3.14 / 4)).createShader(Rect.fromLTWH(0, 0, 5, 5));

  TextPainter _baseLabelPainter(value){
    TextPainter mTextPainter = TextPainter(
        text: TextSpan(
          text: '$value',
          style: TextStyle(
            color: Colors.white,
            fontSize: 12,
          ),
        ),
        textDirection: TextDirection.ltr,
        textAlign: TextAlign.center
    );
    mTextPainter.layout(
      minWidth: 0,
      maxWidth: 80,
    );

    return mTextPainter;

  }

  _createBaseLine(Canvas canvas, Size size){

    double oneSec = size.width/(durationFactor*widthFactor);

    DateTime now = DateTime.now();
    int timestamp = DateTime.now().millisecondsSinceEpoch;//~/1000;


    int mint = (4 - now.minute%5);
    int sec = (60 - now.second%60);
    DateTime nowP5 = now.add(Duration(minutes: mint)).add(Duration(seconds: sec));
    double startPoint = 7*size.width/8;

    Color color = APP_SECONDARY_COLOR.withOpacity(0.3);
    Paint paintLine = new Paint()..color=color;
    int factor = mainDuration~/2;
    for(int i=0; i<50; i++){
      DateTime nowM = nowP5.add(Duration(minutes: -factor*i));
      double tx = startPoint + (nowM.millisecondsSinceEpoch - timestamp)*oneSec;
      canvas.drawLine(Offset(tx, 0), Offset(tx, size.height-15), paintLine);
      _baseLabelPainter('${nowM.hour}:${nowM.minute<10?'0${nowM.minute}':nowM.minute}')
          .paint(canvas, Offset(tx - 15, size.height-15));
    }
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) {
    return true;
  }


}

extension DateTimeExtension on DateTime{

  DateTime roundDown({Duration delta = const Duration(minutes: 5)}){
    return DateTime.fromMillisecondsSinceEpoch(
        this.millisecondsSinceEpoch -
            this.millisecondsSinceEpoch % delta.inMilliseconds
    );
  }

  DateTime roundUp({Duration delta = const Duration(minutes: 5)}){
    return DateTime.fromMillisecondsSinceEpoch(
        this.millisecondsSinceEpoch +
            this.millisecondsSinceEpoch % delta.inMilliseconds
    );
  }
}