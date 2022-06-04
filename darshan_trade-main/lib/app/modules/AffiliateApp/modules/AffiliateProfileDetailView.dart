



import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:get_storage/get_storage.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:royal_q/api/apis.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/modules/AffiliateApp/auth/AffiliateLoginView.dart';
import 'package:royal_q/app/modules/AffiliateApp/model/AffiliateLoginResponse.dart';
import 'package:royal_q/app/modules/AffiliateApp/model/AffiliateProfileDetailResponse.dart';
import 'package:royal_q/app/modules/AffiliateApp/modules/AffilateDashBoardView.dart';
import 'package:royal_q/app/shared/sawidgets/sawidgets.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:royal_q/app/shared/widgets/affliate_bg.dart';
import 'package:royal_q/app/shared/widgets/app_textbox.dart';

import '../../../shared/constants/colors.dart';
import '../../../shared/constants/common.dart';

class AffilateProfileView extends StatefulWidget {
  const AffilateProfileView({Key? key}) : super(key: key);

  @override
  _AffilateProfileViewState createState() => _AffilateProfileViewState();
}

class _AffilateProfileViewState extends State<AffilateProfileView> {

  var imgFile;
  var editControllerName=TextEditingController();
  var editControllerEmailId=TextEditingController();
  var editControllerMobileNo=TextEditingController();
  var editControllerTRCAddress=TextEditingController();
  AffiliateLoginResponse? userInfo1;

  AffiliateProfileDetailResponse? profileModel;

  var editControllerReferralCode=TextEditingController();

  var editControllerSponsorID=TextEditingController();

  var tblUserId,token;

  String refferalCode="";




  @override
  void initState() {
    getUSerDetail();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return AFBg(child: Scaffold(
        backgroundColor: Colors.transparent,
        appBar: AppBar(
          leading: Container(),
          title: Text('Profile', style: textStyleHeading2(color: Colors.white),),
          iconTheme: IconThemeData(color: Colors.white),
          elevation: 0,
          backgroundColor: Colors.transparent,
          actions: [
            IconButton(onPressed: (){
              showDialog(context: context, builder: (context) =>Dialog(
                child: Container(
                  padding: EdgeInsets.symmetric(horizontal: 16),
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      SizedBox(height: 8,),
                      Text('Logout', style: GoogleFonts.rajdhani(
                          fontSize: 22, fontWeight: FontWeight.w700, color: Colors.red
                      ),),
                      Divider(),
                      Text('Are you sure want to exit?', style: GoogleFonts.rajdhani(
                        fontSize: 18, fontWeight: FontWeight.w400,
                      ),),

                      Divider(),

                      Row(children: [
                        Expanded(child: SizedBox()),
                        TextButton(onPressed: () => Get.back(), child: Text('Cancel', style: TextStyle(
                            color: Colors.red
                        ),)),
                        SizedBox(width: 8,),

                        TextButton(onPressed: (){

                          setUser(null).then((value) => Get.off(AffiliateLoginView()));

                        }, child: Text('Logout', style: TextStyle(

                        ),))

                      ],)

                    ],),),
              ));
            }, icon: Icon(Icons.logout,size: 20,))
            // IconButton(onPressed: (){
            //   setState(() => _isVisible = !_isVisible);
            // }, icon: Icon(AntDesign.filter))
          ],
        ),
        body:ListView(children: [

          // SizedBox(height: 15,),

          Row(children: [
            SizedBox(width: 24,),
            Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text('${editControllerName.text}', style: GoogleFonts.rajdhani(
                    fontSize: 16, fontWeight: FontWeight.w700, color: Colors.white
                ),),
                SizedBox(),
              ],),
            Spacer(),
            Container(
              // child: Image.network(userInfo!.pic),
              child: Image.asset('assets/expgain/profile.png', width: 40,),
              padding: EdgeInsets.all(8),
              decoration: BoxDecoration(
                color: Color(0xFF3F484C),
                shape: BoxShape.circle,

              ),
            ),
            SizedBox(width: 24,)
          ],),

          AFTextBox(label: 'Full Name',textEditingController: editControllerName, ),
          AFTextBox(label: 'Email',textEditingController: editControllerEmailId, enable: false,),
          AFTextBox(label: 'Mobile Number',textEditingController: editControllerMobileNo, enable: false,),
          AFTextBox(label: 'Sponsor ID',textEditingController: editControllerSponsorID, enable: false,),
          AFTextBox(label: 'Referral Code',textEditingController: editControllerReferralCode,
          suffix: IconButton(onPressed: (){
            Clipboard.setData(ClipboardData(
                text: refferalCode
            ));
            Fluttertoast.showToast(msg: 'Address copied',
                backgroundColor: Colors.black);
          }, icon: Icon(Icons.copy))),

          AFTextBox(label: 'USDT/TRX20 Address', textEditingController: editControllerTRCAddress,),

          Padding(
            padding: const EdgeInsets.all(20.0),
            child: AFSubmitButton(onPressed: (){
              AppFocus.unfocus(context);
              var hashMap={
                "full_name":editControllerName.text,
                "mobile":editControllerMobileNo.text,
                "wallet_address":editControllerTRCAddress.text,
                "tbl_user_id":tblUserId.toString()
              };
              saveProfileDetailAPI(hashMap,token).then((value){
                if(value.status){
                  Fluttertoast.showToast(msg: value.message.toString());
                  navPush(context, AffiliateDashBoardView());
                }else{
                  Fluttertoast.showToast(msg: value.message.toString());
                }
              });
            }, title: "Update"),
          )
        ],)





    ));
  }


  void getStorageDataValue() async{
    getUser().then((value){
     setState(() {
       editControllerName.text=value!.data.first.fullName.toString();
       editControllerEmailId.text=value!.data.first.email;
       editControllerMobileNo.text=value!.data.first.mobile;
       userInfo1=value;
     });
    });
  }

  void getUSerDetail() {
    getUser().then((value){
      getProfileDetailAPI(value!.data.first.tblUserId.toString(),
          value!.data.first.jwtToken).then((res){
            setState(() {
              tblUserId=value.data.first.tblUserId;
              token=value.data.first.jwtToken;
            profileModel=res.data;
            editControllerName.text=profileModel!.data.first.fullName;
            editControllerEmailId.text=profileModel!.data.first.email;
            editControllerMobileNo.text=profileModel!.data.first.mobile;
            editControllerTRCAddress.text=profileModel!.data.first.walletAddress;
            refferalCode=profileModel!.data.first.referalCode;
            editControllerReferralCode.text = profileModel!.data.first.referalCode;
            editControllerSponsorID.text=profileModel!.data.first.usedReferalCode;
            });
      });
    });
  }
}



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






class LabelNameTextField extends StatelessWidget {

  final String hintText,labelText;
  bool enabled=true;
  TextEditingController? controller;

  LabelNameTextField({Key? key, required this.hintText, required this.labelText,
    required this.enabled,this.controller}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return TextFormField(
      controller: controller,
      style: const TextStyle(fontSize: 14.5),
      autofocus: true,
      textInputAction: TextInputAction.next,
      inputFormatters: [
      ],
      decoration: InputDecoration(
          enabled: enabled,
          focusedBorder: OutlineInputBorder(
            borderSide: BorderSide(color: Color(0xf020202)),
            borderRadius: BorderRadius.circular(9.0),
          ),
          border: OutlineInputBorder(
              borderRadius: BorderRadius.circular(12),
              borderSide: const BorderSide(width: 1.0,color: Color(0xf020202))
          ),
          errorBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(12),
              borderSide: const BorderSide(width: 1.0,color:Color(0xf020202))
          ),
          enabledBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(12),
              borderSide: const BorderSide(width: 1.0,color: Color(0xf020202))
          ),
          contentPadding: const EdgeInsets.only(top: 20,left: 15),
          hintText: hintText,
          labelText: labelText,
          labelStyle: TextStyle(color: Colors.grey[600],fontSize: 13.5),
          hintStyle: TextStyle(color: Colors.grey[600],fontSize: 13.5),
          filled: false,
          fillColor: Colors.white
      ),
    );
  }
}