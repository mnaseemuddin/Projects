import 'dart:convert';
import 'dart:io';
import 'dart:ui';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_native_image/flutter_native_image.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:image_picker/image_picker.dart';
import 'package:intl/intl.dart';
import 'package:ionicons/ionicons.dart';
import 'package:trading_apps/api/apis.dart';
import 'package:trading_apps/api/user_data.dart';
import 'package:trading_apps/custom_ui/progress_dialog.dart';
import 'package:trading_apps/models/common_model.dart';
import 'package:trading_apps/models/wallet_model.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/res/constants.dart';
import 'package:trading_apps/res/strings.dart';
import 'package:trading_apps/screens/settings/privacypolicy.dart';
import 'package:trading_apps/screens/settings/settings.dart';
import 'package:trading_apps/screens/trades/deposit_currency.dart';
import 'package:trading_apps/screens/trades/transactions.dart';
import 'package:trading_apps/screens/trades/withdrawl.dart';
import 'package:trading_apps/utility/common_methods.dart';
import 'package:url_launcher/url_launcher.dart';

import 'settings/wallet_transaction.dart';

class Profile extends StatefulWidget {
  const Profile({Key? key}) : super(key: key);

  @override
  _ProfileState createState() => _ProfileState();
}

class _ProfileState extends State<Profile> {
  bool _isLoading = true;
  double _amountOpacity = 1;
  @override
  void initState() {
    SystemChrome.setSystemUIOverlayStyle(SystemUiOverlayStyle.light);
    userDetailsAPI(userModel!.data.first.userId).then((value){
      print(value.data.toString());
      if(mounted)
      setState(() => _isLoading = false);
    });
    super.initState();
  }
  
  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    return Scaffold(
      appBar: AppBar(
        elevation: 0,
        backgroundColor: Colors.transparent,
      ),
      backgroundColor: APP_PRIMARY_COLOR,
      body: Container(
        padding: EdgeInsets.only(left: 24, right: 24),
        child: ListView(children: [
          
          GestureDetector(child: Container(width: size.width/2.5, height: size.width/2.5,
            decoration: BoxDecoration(
                shape: BoxShape.circle,
                border: Border.all(color: APP_SECONDARY_COLOR, width: 1),
                image: DecorationImage(image: NetworkImage(userModel!.data.first.userImage),
                    fit: BoxFit.contain)
            ),
            // child: Padding(
            //   padding: const EdgeInsets.all(15.0),
            //   child: Image.network(userModel!.data.first.userImage,color:Colors.white,fit: BoxFit.fill,),
            // ),
          ), onTap: (){
            ImagePicker.platform.pickImage(source: ImageSource.gallery).then((value){

              File file = File(value!.path);
              final bytes=file.readAsBytesSync().lengthInBytes;
              final kb = bytes / 1024;
              if(kb>1000){
                showDialog(context: context, builder: (context) => ProgressDialog(), barrierDismissible: false).then((value){
                  print(value);
                },);
                compressImageFile(file);
              }else{
                showDialog(context: context, builder: (context) => ProgressDialog(), barrierDismissible: false).then((value){
                  print(value);
                },);
                upload(imageFile: file, loc: 'profiles').then((response){
                  response.stream.transform(utf8.decoder).listen((value) {
                    print('object => $value');
                    userDetailsAPI(userModel!.data.first.userId).then((value){
                      if(mounted)setState(() {
                        Fluttertoast.showToast(msg:  value.message.toString());
                      });
                    });
                  });
                  Navigator.of(context).pop();
                });
              }
            });
          },),

          SizedBox(height: 16,),

          GestureDetector(
            onTap: (){
              navPush(context,TDCWalletScreens());
            },
            child: Container(
              margin: EdgeInsets.all(4),
              height: 60,
              decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(8),
                  border: Border.all(color: APP_SECONDARY_COLOR, width: 1)
              ),
            child: Row(children: [
         Padding(
             padding: const EdgeInsets.only(left:6.0),
             child: IconButton(onPressed: (){}, icon:
             Icon(Ionicons.wallet_outline,color: Colors.white,)),
         ),
              Padding(
                padding: const EdgeInsets.only(left:17.0),
                child: Text('TDC Wallet',style: textStyleHeading3(color: Colors.white),),
              ),Spacer(),
               FutureBuilder(
                future: userWalletAPI(userModel!.data.first.userId),
                builder: (context, AsyncSnapshot<ApiResponse>snapshot){
                if(!snapshot.hasData)return Padding(
                  padding: const EdgeInsets.only(right:15.0),
                  child: Text('--:--', style: textStyleHeading2(color: Colors.white),),
                );
                ApiResponse? resp = snapshot.data;
                if(!resp!.status)return Padding(
                   padding: const EdgeInsets.only(right:15.0),
                  child: Text('--:--', style: textStyleHeading2(color: Colors.white),),
                );
                WalletModel model = resp?.data;
                walletAmount = double.parse(model.data.first.tdcWallet);
                return AnimatedOpacity(opacity: _amountOpacity, duration: Duration(microseconds: 500),
                child: Padding(
                   padding: const EdgeInsets.only(right:15.0),
                  child: Text('\$'+walletAmount.toStringAsFixed(2), style: textStyleHeading2(color: Colors.white)),
                ),);
            })

            ],),),
          ),

          Column(children: PROFILE_ITEMS.map((e) => Container(
            margin: EdgeInsets.all(4),
            child: ListTile(title: Text(e.title, style: textStyleHeading3(color: Colors.white),),
            leading: Icon(e.icon, color: Colors.white,),
            trailing: Icon(Icons.navigate_next, color: APP_SECONDARY_COLOR,),
            onTap: (){
                _onItemSelected(e);
            },),
            decoration: BoxDecoration(
              borderRadius: BorderRadius.circular(8),
              border: Border.all(color: APP_SECONDARY_COLOR, width: 1)
            ),
          )).toList(),),

          _isLoading?CircularProgressIndicator():Container(height: 0,)
        ],),
      ),
    );
  }

  Future<void> compressImageFile(File file) async{
    File compressedFile = await FlutterNativeImage.compressImage(file.path,
      quality: 30,);

    upload(imageFile: compressedFile, loc: 'profiles').then((response){
      response.stream.transform(utf8.decoder).listen((value) {
        userDetailsAPI(userModel!.data.first.userId).then((value){
          if(mounted)setState(() {
            Fluttertoast.showToast(msg:value.message!.toString());
          });
        });
      });
      Navigator.of(context).pop();
    });
  }

  _onItemSelected(ListItem item){
    switch(item.title){
      case 'Wallet Transaction':
        navPush(context, WalletTransaction());
        break;
      case 'Deposit':
        navPush(context, DepositCurrency());
        break;
      case 'Withdraw':
        navPush(context, Withdrawal());
        break;
      case 'Transactions':
        navPush(context, Transaction());
        break;
       case 'Privacy Policy':
         _launchURL('http://tradingclub.fund/privacy_policy');
       // navPush(context, PrivacyPolicyActivity(tilte: 'Privacy Policy',url: "assets/privacy_policy.pdf",));
        break;
        case 'Settings':
          navPush(context, Settings());
        break;
      default:
        break;

    }
  }
  void _launchURL(_url) async =>
      await canLaunch(_url) ? await launch(_url) : throw 'Could not launch $_url';
}
