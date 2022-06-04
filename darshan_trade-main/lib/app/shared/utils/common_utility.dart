import 'dart:math';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:intl/intl.dart';
import 'package:royal_q/main.dart';

Map<String, dynamic>_imageCache = {};

String printDuration(Duration duration) {
  String twoDigits(int n) => n.toString().padLeft(2, "0");
  String twoDigitMinutes = twoDigits(duration.inMinutes.remainder(60));
  String twoDigitSeconds = twoDigits(duration.inSeconds.remainder(60));
  if(duration.inHours>0) {
    return "${twoDigits(duration.inHours)}:$twoDigitMinutes:$twoDigitSeconds";
  } else {
    return "$twoDigitMinutes:$twoDigitSeconds";
  }
}

Widget loadImage(String path, Size size){
  if(_imageCache.containsKey(path)){
    return _imageCache[path];
  }


 Widget widget = FutureBuilder(
     future: _buildImage(path, size),
     builder: (context, AsyncSnapshot<Image>snapshot){
       if (snapshot.connectionState == ConnectionState.done) {
         return snapshot.data!;
       } else {
         return CircularProgressIndicator();
       }
     });
 _imageCache[path] = widget;
 return widget;
}

Future<Image> _buildImage(image, size) async {
  // String path = 'assets/images/icon/${image.replaceAll('USDT', '').toLowerCase()}@2x.png';
  return rootBundle.load(image).then((value) {
    return Image.memory(value.buffer.asUint8List(), width: size.width,);
  }).catchError((_) {
    return Image.asset(
      'assets/expgain/icon_splash.png',
      height: size.width,
    );
  });
}

int generateOTP() {
  var rnd = new Random();
  var next = rnd.nextDouble() * 1000000;
  while (next < 100000) {
    next *= 10;
  }
  print(next.toInt());
  return next.toInt();
}

String formatDate(var date) {
  DateTime time = DateTime.fromMillisecondsSinceEpoch(date is int?date:int.parse(date));
  String df = DateFormat('ddMMMM, yyyy, hh:mm:ss').format(time);
  return df;
}

Map emailBody({appName, email, subject, OTP}) =>  {

  'Email': email,
  'Subject': "Verification code From $appName",
  'Body': ''' <div style="font-family: Helvetica,Arial,sans-serif;min-width:1000px;overflow:auto;line-height:2">
  <div style="margin:50px auto;width:70%;padding:20px 0">
    <div style="border-bottom:1px solid #eee">
      <a href="" style="font-size:1.4em;color: #00466a;text-decoration:none;font-weight:600">$appName</a>
    </div>
    <p style="font-size:1.1em">Hi,</p>
    <p>Thank you for choosing $appName. Use the following OTP $subject. OTP is valid for 2 minutes</p>
    <h2 style="background: #00466a;margin: 0 auto;width: max-content;padding: 0 10px;color: #fff;border-radius: 4px;">$OTP</h2>
    <p style="font-size:0.9em;">Regards,<br />$appName</p>
    <hr style="border:none;border-top:1px solid #eee" />
  </div>
</div> '''
};