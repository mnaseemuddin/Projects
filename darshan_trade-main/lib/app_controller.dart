import 'dart:math';

import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:get/get.dart';

import 'api/apis.dart';

class AppController extends GetxController{

  final OTP = 0.obs;
  void sendOTP({required String message, required String email}) async{
    OTP.value = generateOTP();
    // Map body = emailBody(appName: 'JackBOT', email: 'aslam8604645@gmail.com', OTP: OTP.value, subject: message);
    Map body = emailBody(appName: 'Darshan Trader', email: email, OTP: OTP.value, subject: message);
    // Map body = emailBody(appName: 'JackBOT', email: 'zulfiqar108@gmail.com', OTP: OTP.value, subject: message);
    EasyLoading.show();
    ApiResponse resp = await sendEmailOTPAPI(body);
    EasyLoading.dismiss();
    EasyLoading.showToast('An Email send on you email $email');
    printLog('response => ${resp.data}');
    refresh();
  }

  int generateOTP() {
    var rnd = new Random();
    var next = rnd.nextDouble() * 1000000;
    while (next < 100000) {
      next *= 10;
    }
    printLog(next.toInt());
    return next.toInt();
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

  void printLog(log){
    print(log);
  }
}
