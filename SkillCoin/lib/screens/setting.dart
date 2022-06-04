


import 'package:flutter/material.dart';
import 'package:skillcoin/api/user_data.dart';
import 'package:get/get.dart';
import 'package:skillcoin/customui/button.dart';
import 'package:skillcoin/res/color.dart';
import 'package:skillcoin/res/routes.dart';
import 'package:skillcoin/screens/changepassword.dart';
import 'package:skillcoin/screens/profile.dart';
import '../api/apirepositary.dart';
import '../customui/container.dart';
import '../model/profiledetailsmodel.dart';
import '../res/constants.dart';
import 'dashboard.dart';

class Setting extends StatefulWidget {
  const Setting({Key? key}) : super(key: key);

  @override
  _SettingState createState() => _SettingState();
}

class _SettingState extends State<Setting> {

  var imgFile;

  ProfileDetailsModel? profile;
  String? photoUrl,email;
  bool isLoading=true;

  @override
  void initState() {
    _getWalletAPI();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: PRIMARY_APP_COLOR,
      body: SafeArea(
        top: true,
        child: isLoading?const Center(
          child: CircularProgressIndicator(strokeWidth: 5,
            color: PRIMARYBLACKCOLOR,),
        ): Column(children: [
          Align(
            alignment: Alignment.centerLeft,
            child: IconButton(onPressed: (){
              navPushOnBackPressed(context);
            }, icon: const Icon(Icons.arrow_back_ios_outlined,size: 20,color:Colors.black,)),
          ),

          profileHeadingUI(),

          const ListTile(leading: Icon(Icons.help_center),title: Text("Help Support"),
            trailing: Icon(Icons.navigate_next, color: PRIMARYBLACKCOLOR,),),

          const ListTile(leading: Icon(Icons.share),title: Text("Share App"),
            trailing: Icon(Icons.navigate_next, color: PRIMARYBLACKCOLOR,),),

          const ListTile(leading: Icon(Icons.privacy_tip),title: Text("Privacy Policy"),
            trailing: Icon(Icons.navigate_next, color: PRIMARYBLACKCOLOR,),),

          ListTile(onTap:(){
          navPush(context,const ChangePasswordPage());
          },leading: const Icon(Icons.lock),title: const Text("Change Password"),
            trailing: const Icon(Icons.navigate_next, color: PRIMARYBLACKCOLOR,),),
          const Spacer(),
          Padding(
            padding: const EdgeInsets.only(bottom: 30.0,left: 15,right: 15),
            child: SubmitButton(onPressed: (){
              setUser(null);
              navPush(context,const DashBoard());
            }, name: "Logout", colors: 0xffebecee,
                textColor: Colors.black, width: double.infinity, circular: 6),
          )

        ],),
      ),
    );
  }

  profileHeadingUI() {
    return Padding(
      padding: const EdgeInsets.only(top:0.0),
      child: GestureDetector(
        onTap: (){
          navPush(context,const MyProfile());
        },
        child: SizedBox(height: 100,
          child: Row(children: [

            SizedBox(
              width: MediaQuery.of(context).size.width*.20,
              child: Padding(
                padding: const EdgeInsets.all(8.0),
                child: ClipRRect(
                    borderRadius: BorderRadius.circular(60),
                    child: photoUrl!.isEmpty?Image.asset(
                      imgProfile,
                      fit: BoxFit.fitHeight,):Image.network(photoUrl!,fit: BoxFit.fitHeight,)),
              ),
            ),
            const Padding(
              padding: EdgeInsets.only(left:5.0),
            ),
            SizedBox(
              width: MediaQuery.of(context).size.width*.30,
              child: Text(userModel!.data.first.name.toString(),
                overflow: TextOverflow.ellipsis,maxLines: 1,
                style: const TextStyle(fontSize: 17,fontWeight: FontWeight.w500),),
            ),
            Padding(
              padding: const EdgeInsets.only(left:5.0),
              child: Icon(Icons.arrow_forward_ios_rounded,size: 17,color: Colors.black.withOpacity(0.4),),
            ),
            SizedBox(
              width: MediaQuery.of(context).size.width*.09,
              child: const Text("",maxLines: 1,
                style: TextStyle(fontSize: 15),),
            ),
            const Spacer(),
            SizedBox(
              width: MediaQuery.of(context).size.width*.30,
              child: Container(
                height: 35,
                decoration: BoxDecoration(
                    color: Colors.green.withOpacity(0.3),
                    borderRadius: const BorderRadius.only(topLeft: Radius.circular(15),
                        bottomLeft: Radius.circular(15))
                ),
                child: Padding(
                  padding: const EdgeInsets.only(right:8.0),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.end,
                    children: [
                      Image.asset("assets/verified.png",height: 25,width: 25,),
                      SizedBox(width: 10,),
                      Text("Verified",
                        style: TextStyle(fontSize: 15,color: Colors.green),),
                    ],
                  ),
                ),
              ),
            ),

          ],),
        ),
      ),
    );
  }
  void _getWalletAPI() {

    Map<dynamic,String>body={
      "email":userModel!.data.first.email,
      "token":userModel!.data.first.token
    };
    walletBalanceAPI(body).then((value){
      if(value.status){
        setState(() {
          profile=value.data;
          email=profile!.data.first.email;
          photoUrl=profile!.data.first.resultsImage;
          isLoading=false;
        });
      }
    });
  }
}
