import 'dart:math';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
// import 'package:flutter_barcode_scanner/flutter_barcode_scanner.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
// import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:get_storage/get_storage.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/shared/sawidgets/progress_dialog.dart';
import 'package:royal_q/app/shared/sawidgets/sa_count_down.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:royal_q/app/shared/utils/common_utility.dart';
import 'package:royal_q/main.dart';


class BindController extends StatefulWidget {
  final int index;
  final String title;
  const BindController({Key? key, required this.title, required this.index}) : super(key: key);

  @override
  _BindControllerState createState() => _BindControllerState();
}

class _BindControllerState extends State<BindController> {
  bool      _isValidData   = false;
  bool      _isAccept      = false;

  FieldData _apiKey      = FieldData(data: '', valid: false);
  FieldData _apiSecret   = FieldData(data: '', valid: false);
  FieldData _apiVerCode  = FieldData(data: '', valid: false);
  final TextEditingController _controllerApiKey      = TextEditingController();
  final TextEditingController _controllerApiSecret   = TextEditingController();

  int OTP = 0;
  bool _otpValidated = false;
  bool _isBinded = false;
  final box = GetStorage();

  final List<AcceptText>texts = [
    AcceptText(text: 'I_have_read'.tr),
    AcceptText(text: 'the_risk_notice_carefully'.tr, isBtn: true),
  ];

   String _ip = '157.119.217.164';

  @override
  void initState() {
    super.initState();
    // _ip = box.read('ip')??_generateRandomIp();
    getApiSecretKeyAPI({
      'id': userInfo!.id,
      'exchanetype': widget.index
    }).then((value){
      if(value.status){
        setState(() {
          ApiKeyResponse data = value.data;
          _apiKey = FieldData(data: data.apikey, valid: data.apikey !=null);
          _apiSecret = FieldData(data: data.secretkey, valid: data.secretkey !=null);
          _controllerApiKey.text = _apiKey.data;
          _controllerApiSecret.text = _apiSecret.data;
          _isBinded = _apiKey.data.isNotEmpty;
          _ip = data.ips;
        });
      }

    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title, style: textStyleHeading2(color: ColorConstants.white),),
        iconTheme: IconThemeData(color: ColorConstants.white),
        elevation: 0,
        // brightness: Brightness.dark,
        backgroundColor: Colors.transparent,
      ),
      body: Container(
        padding: EdgeInsets.symmetric(horizontal: 16),
        child: ListView(children: [
          SizedBox(height: 8,),
          SACellContainer(child: Column(children: [
            GestureDetector(child: Row(children: [
              Expanded(child: Text('Notification'.tr, style: textStyleHeading2(),)),
              Text('See_the_instructions'.tr, style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),)
            ],), onTap: (){},),
            SizedBox(height: 16,),
            Text('confirm_API_permissions'.tr,
              style: textStyleLabel(),),
            SizedBox(height: 8,),
            Text('enter_correct_API'.tr,
              style: textStyleLabel(),)
          ],)),

          SizedBox(height: 8,),

          SACellContainer(child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
            Text('IP_group_binding'.tr, style: textStyleHeading2(),),
            SizedBox(height: 16,),
            Text('For_security'.trParams({'title': widget.title}),
              style: textStyleLabel(),),
            SizedBox(height: 8,),
            Container(
              child: Row(children: [
                SizedBox(width: 8,),
                Expanded(child: Text(_ip, style: textStyleLabel(),),),
                TextButton(onPressed: (){
                  Clipboard.setData(ClipboardData(text: _ip));
                  EasyLoading.showToast('Ip_copied_to_clipboard');
                }, child: Text('Copy'.tr, style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),))
              ],),
              decoration: textBg,
            ),
            // Text('2. Please enter the correct API, please do not enter special character',
            //   style: textStyleLabel(),)
          ],)),

          SACellContainer(
              padding: EdgeInsets.only(left: 16),
              child: Column(children: [
            Row(children: [
              Text('API Key', style: textStyleHeading2(),),
              SizedBox(width: 8,),
              Expanded(child: TextFormField(
                controller: _controllerApiKey,
                onChanged: (val){
                  _apiKey = FieldData(data: val, valid: val.length>10);
                  _checkDataValidity();
                },
                style: textStyleLabel(),
                decoration: InputDecoration(
                  hintText: 'Please_enter_API_key'.tr,
                  border: InputBorder.none,
                  hintStyle: textStyleLabel(color: ColorConstants.white38),
                  suffixIcon: IconButton(onPressed: ()async{
                  },
                      icon: Icon(Ionicons.ios_scan_outline, color: ColorConstants.APP_SECONDARY_COLOR,))
                ),
              ))

            ],),
                Row(children: [
                  Text('Secret_Key'.tr, style: textStyleHeading2(),),
                  SizedBox(width: 8,),
                  Expanded(child: TextFormField(
                    controller: _controllerApiSecret,
                    onChanged: (val){
                    _apiSecret = FieldData(data: val, valid: val.length>10);
                    _checkDataValidity();
                    },
                    style: textStyleLabel(),
                    decoration: InputDecoration(
                        hintText: 'Please_enter_Secret_Key'.tr,
                        border: InputBorder.none,
                        hintStyle: textStyleLabel(color: ColorConstants.white38),
                        // suffixIcon: IconButton(onPressed: (){},
                        //     icon: Icon(Ionicons.ios_scan_outline, color: APP_SECONDARY_COLOR,))
                    ),
                  ))

                ],),

                Divider(color: ColorConstants.white10,),

                Row(children: [
                  Text('Verification_code'.tr, style: textStyleHeading2(),),
                  SizedBox(width: 8,),
                  Expanded(child: TextFormField(
                    onChanged: (val){
                      setState(() {
                        _otpValidated = '$OTP' == val;
                        _apiVerCode = FieldData(data: val, valid: _otpValidated);
                        _checkDataValidity();
                      });
                    },
                    style: textStyleLabel(),
                    decoration: InputDecoration(
                      hintText: 'Please_enter_verification'.tr,
                      border: InputBorder.none,
                      hintStyle: textStyleLabel(color: ColorConstants.white38),
                      suffixIcon: _otpValidated?Icon(Icons.check, color: ColorConstants.APP_SECONDARY_COLOR,):(OTP==0?TextButton(onPressed: (){
                          setState(() {
                            OTP = generateOTP();
                          });

                          // Map body = {
                          //   'Email': userInfo?.email,
                          //   'Subject': "verification_code_From".trParams({'name': 'Jackbot'}),
                          //   'Body': 'verification_otp'.trParams({'otp': '$OTP'})
                          // };

                          Map body = emailBody(appName: 'Darshan', email:
                          userInfo?.email, OTP: OTP, subject: ' to complete bind API');

                          _sendEmailOTPAPI(body);


                      }, child: Text('Send'.tr, style: textStyleLabel(color: ColorConstants.APP_SECONDARY_COLOR),))
                          :SACountDown(onComplete: (){
                            setState(() {
                              OTP = 0;
                            });
                      },))
                    ),
                  ))

                ],),
          ],)),

          AcceptTerms(accept: (val){
            _isAccept = val;
            _checkDataValidity();
          },texts: texts,),

          SizedBox(height: 16,),
          SubmitButton(title: _isBinded?'Update':'Bind'.tr, isActive: _isValidData, onPressed: (){
            Map body = {
              'id': userInfo?.id,
              'apikey': _apiKey.data,
              'secretkey': _apiSecret.data,
              'exchanetype': widget.index
            };
            _bindApiSecretKeyAPI(body);
            // Get.snackbar('API', 'api binding will be completed ', );
            // Get.defaultDialog(title: 'Under development', content: Text('....'),
            // confirm: TextButton(onPressed: (){
            //   Get.back();
            // }, child: Text('Okay')));
          }),
        ],),
      ),
    );
  }

  _checkDataValidity(){
    bool isValid = _apiKey.valid&&_apiSecret.valid&&_apiVerCode.valid&&_isAccept;
    // bool isValid = _apiKey.valid&&_apiSecret.valid&&_isAccept;
    if(_isValidData!=isValid) {
      setState(() {
      _isValidData = isValid;
    });
    }
  }

  _bindApiSecretKeyAPI(Map body){
    showDialog(context: context, builder: (context) => ProgressDialog(), barrierDismissible: false).then((value){
      print(value);
    },);

    bindApiSecretKeyAPI(body).then((value){
      Navigator.of(context).pop();
      if(value.status){
        if(mounted){
          setState(() {
            EasyLoading.showToast('API_Binded'.tr);
              Get.back();
          });
        }
      }else{
        EasyLoading.showToast(value.data['message']);
      }
    });
  }

  _sendEmailOTPAPI(body){
    EasyLoading.showToast('OTP_requested'.tr);

    EasyLoading.show();


    sendEmailOTPAPI(body).then((value){
      if(value.status){
        EasyLoading.showToast('OTP_sent_on'.trParams({'email': '${userInfo?.email}'}));
      }else{
        EasyLoading.showToast('${value.data['message']}');
      }
    });
    EasyLoading.dismiss();
  }

  String _generateRandomIp(){
    var ip0 = '${(Random().nextInt(255) + 1)}.${(Random().nextInt(255))}.${(Random().nextInt(255))}.${Random().nextInt(255)}';
    var ip1 = '${(Random().nextInt(255) + 1)}.${(Random().nextInt(255))}.${(Random().nextInt(255))}.${Random().nextInt(255)}';
    var ip2 = '${(Random().nextInt(255) + 1)}.${(Random().nextInt(255))}.${(Random().nextInt(255))}.${Random().nextInt(255)}';
    var IP = '$ip0 $ip1 $ip2';
    box.write('ip', IP);
    return IP;
  }
}
