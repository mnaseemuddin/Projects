import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'package:ionicons/ionicons.dart';
import 'package:trading_apps/api/user_data.dart';
import 'package:trading_apps/custom_ui/common_widget.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/res/constants.dart';
import 'package:trading_apps/screens/settings/name_editor.dart';
import 'package:trading_apps/screens/settings/verify_email.dart';
import 'package:trading_apps/screens/settings/verify_phone.dart';
import 'package:trading_apps/screens/settings/withdraw_address.dart';
import 'package:trading_apps/utility/common_methods.dart';

class PersonalSettings extends StatefulWidget {
  const PersonalSettings({Key? key}) : super(key: key);

  @override
  _PersonalSettingsState createState() => _PersonalSettingsState();
}

class _PersonalSettingsState extends State<PersonalSettings> {


  final GoogleSignIn _googleSignIn = GoogleSignIn();
  late User _user;
  final FirebaseAuth _auth = FirebaseAuth.instance;

  String LoginType='';

  var IsVisiblitySocialGoogle=false,IsVisiblitySocialFacebook=false;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Personal Settings'),
        elevation: 0,
      ),
      backgroundColor: APP_PRIMARY_COLOR,
      body: Container(
          padding: EdgeInsets.all(16),
        child: ListView(children: [
          SizedBox(height: 40,),
          Text('Personal', style: textStyleHeading(color: Colors.white, fontSize: 24.0),),
          SizedBox(height: 8,),

          _Item(title: 'Name', subTitle: userModel!.data.first.name, onTap: () {
            navPush(context, NameEditor()).then((value) => setState(() {}));
          },),

          SizedBox(height: 40,),
          Text('Contacts', style: textStyleHeading(color: Colors.white, fontSize: 24.0),),

          _Item(title: 'Email', subTitle: userModel!.data.first.email, verified: userModel!.data.first.emailVerify==1, onTap: () {
          // navPush(context, VerifyGmail()).then((val) => setState(() {}));
          },),
          _Item(title: 'Phone Number', subTitle: userModel!.data.first.mobile,
            verified: userModel!.data.first.mobileVerify==1, onTap: () {
            navPush(context, ConfirmPhone()).then((val) => setState(() {}));
          },),

          _Item(title: 'TDC Withdrawal Address', subTitle: userModel!.data.first.withdrawaddress=="0"?""
            :userModel!.data.first.withdrawaddress,
            verified: userModel!.data.first.mobileVerify==1, onTap: () {
              navPush(context, WithdrawalEditor()).then((val) => setState(() {}));
            },),

          SizedBox(height: 40,),
        //  Text('Social', style: textStyleHeading(color: Colors.white, fontSize: 24.0),),

         // _SubmitButton(icon: Ionicons.logo_facebook, IsVisiblityConnectStatus:IsVisiblitySocialFacebook,title: 'Connect Facebook',
       //       onTap: (){

      //    }),
        //  _SubmitButton(icon: Ionicons.logo_google,IsVisiblityConnectStatus:
      //    IsVisiblitySocialGoogle, title: 'Connect Google', onTap: (){
       //       signInWithGoogle();
        //  }),
          // SizedBox(
          //   height: 60,
          //   child: Padding(
          //     padding: const EdgeInsets.all(8.0),
          //     child: RaisedButton(
          //       onPressed: ()=>debugPrint('OKAY'),
          //       child: Container(
          //         padding: EdgeInsets.all(16),
          //         margin: EdgeInsets.all(4),
          //         child: Row(children: [
          //           Icon(Ionicons.logo_google, color: APP_SECONDARY_COLOR,),
          //           Expanded(child: Text('Connect Google', style: textStyleHeading(color: Colors.white),
          //             textAlign: TextAlign.center,))
          //         ],),
          //         decoration: BoxDecoration(
          //             color: APP_PRIMARY_COLOR,
          //             borderRadius: BorderRadius.circular(8)
          //         ),
          //       ),
          //     ),
          //   ),
          // )
        ],),
      ),
    );
  }
  Future<User?> signInWithGoogle() async{
    try {
      final GoogleSignInAccount? googleSignInAccount = await _googleSignIn
          .signIn();
      GoogleSignInAuthentication googleSignInAuthentication =
      await googleSignInAccount!.authentication;
      AuthCredential credential = GoogleAuthProvider.credential(
          accessToken: googleSignInAuthentication.accessToken,
          idToken: googleSignInAuthentication.idToken
      );
      await _auth.signInWithCredential(credential);
      UserCredential userCredential=await _auth.signInWithCredential(credential);
      _user=userCredential.user!;
      LoginType="SOCIAL";
      setState(() {
        IsVisiblitySocialGoogle=true;
      });
     // userLoginBySocial(LoginType,_user.email,_user.uid);
    }catch(e){
      debugPrint(e.toString());
    }
  }
}

class _Item extends StatelessWidget {
  final String title;
  final String? subTitle;
  final bool verified;
  final Function()onTap;
  const _Item({Key? key, required this.title, this.subTitle, required this.onTap, this.verified=true}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      child: SettingItemBg(
          height: 55,
          child: Padding(padding: EdgeInsets.all(8),
            child: Row(children: [
              Expanded(child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Text(title, style: textStyleLabel(color: Colors.white54),),
                  subTitle != null?Text(subTitle!, overflow:TextOverflow.ellipsis,style: textStyleHeading2(color: Colors.white),):Container(height: 0,),
                ],
              )),
              verified?Container():Icon(Icons.info_outline, color: APP_SECONDARY_COLOR,)
            ],),)),
      onTap: onTap,
    );
  }
}

class _SubmitButton extends StatefulWidget {
  final IconData icon;
  final String title;
  final Function()onTap;
  final bool IsVisiblityConnectStatus;
  const _SubmitButton({Key? key, required this.icon, required this.title,
    required this.onTap,this.IsVisiblityConnectStatus=false}) : super(key: key);

  @override
  State<_SubmitButton> createState() => _SubmitButtonState();
}

class _SubmitButtonState extends State<_SubmitButton> {

  bool IsVisiblitySocial=false;

  @override
  void initState() {
    super.initState();
    IsVisiblitySocial=widget.IsVisiblityConnectStatus;
  }

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: widget.onTap,
      child: Container(
        padding: EdgeInsets.all(16),
        margin: EdgeInsets.all(4),
        child: Row(children: [
          Icon(widget.icon, color: APP_SECONDARY_COLOR,),
          Expanded(child: Text(widget.title, style: textStyleHeading(color: Colors.white),
            textAlign: TextAlign.center,)),
          Visibility(
              visible: widget.IsVisiblityConnectStatus,
              child: Icon(Icons.check,color: Colors.green,))
        ],),
        decoration: BoxDecoration(
            color: Colors.white10,
            borderRadius: BorderRadius.circular(8)
        ),
      ),
    );
  }
}
