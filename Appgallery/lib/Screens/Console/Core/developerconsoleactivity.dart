

import 'dart:convert';
import 'dart:io';
import 'package:appgallery/CommonUI/apptextview.dart';
import 'package:appgallery/CommonUI/commonwidget.dart';
import 'package:appgallery/CommonUI/nodata.dart';
import 'package:appgallery/Model/allconsoleappsmodel.dart';
import 'package:appgallery/Model/usermodel.dart';
import 'package:appgallery/Resources/color.dart';
import 'package:appgallery/Resources/constants.dart';
import 'package:appgallery/Screens/BNavigationFolder/bottommenuactivity.dart';
import 'package:appgallery/Utils/dailog.dart';
import 'package:appgallery/Utils/loadingoverlay.dart';
import 'package:appgallery/Utils/routes.dart';
import 'package:appgallery/apis/userdata.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:http/http.dart'as http;
import '../../../apis/apirepositary.dart';
import '../Auth/clogin.dart';
import '../Profile/ceditprofile.dart';
import '../Profile/changepassword.dart';
import 'cappsdetailsactivity.dart';

class DeveloperConsoleActivity extends StatefulWidget {
  const DeveloperConsoleActivity({Key? key}) : super(key: key);

  @override
  _DeveloperConsoleActivityState createState() => _DeveloperConsoleActivityState();
}

class _DeveloperConsoleActivityState extends State<DeveloperConsoleActivity> {

  // List<ListTitleWithIconModel>UnpublishAppsList=[
  //   ListTitleWithIconModel(img: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR1IhMhtWQvwhA_2f6vS3RiBY34HxwWzbTcLw&usqp=CAU',
  //       name: 'English News -All Latest News',status: 'Unpublished'),
  //   ListTitleWithIconModel(img: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTk6sBl3rg-YQMWfpnQX9z3WVvUaFCkRKbJtg&usqp=CAU',
  //       name: 'Online Casino|Slots & Ultimate Games Guide', status: 'Unpublished'),
  //
  //   ListTitleWithIconModel(img: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTk6sBl3rg-YQMWfpnQX9z3WVvUaFCkRKbJtg&usqp=CAU',
  //       name: 'Online Casino|Slots & Ultimate Games Guide', status: 'Unpublished')
  // ];
  //
  //
  // List<ListTitleWithIconModel>PublishAppsList=[
  //   ListTitleWithIconModel(img: 'assets/logo.png',
  //       name: 'PlayBox -Watching Movies & Web Series ',status: 'Published'),
  //   ListTitleWithIconModel(img: 'assets/icon.png', name: 'Anand Mart-Grocery App',
  //       status: 'Published'),
  //
  //   ListTitleWithIconModel(img: 'assets/icon.png', name: 'Anand Mart-Grocery App',
  //       status: 'Published')
  // ];

  String selected='Publish Apps';
  String? userId,token;
  var url="http://3.111.16.227:8010/files/upload/user_image/";
  var photoUrl='';
  String name='',emailAddress='';
  AllConsoleAppsModel? appsModel;
  @override
  void initState() {
   getUser().then((value){
     name=value!.name;
     emailAddress=value.email;
     getAllApps(value!.data,value!.token);
     getProfileDetails(value!.email, value!.token);
   });
    super.initState();

  }


  void getProfileDetails(String emailID,String token){

      Map<String,dynamic>body= {
        "email_address": emailID,
        "token": token
      };
      getProfileAPI(body).then((value){
        if(value.status){
          setState(() {
            photoUrl= value!.data["user_image"];
          });
        }else{
          Fluttertoast.showToast(msg: value!.message.toString());
        }
      });

  }
  
  
  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: (){
        _onBackPressed();
        return Future.value(false);
      },
      child: Scaffold(
        drawer: navigationDrawerUI(),
        backgroundColor: appBarColor,
        appBar: AppBar(
          iconTheme: const IconThemeData(color: Colors.black),
          title: Text('Play Console',style: textStyleTitle(color: Colors.black,fontSize: 16.0)),
          backgroundColor: const Color(0xfff1f3f5),
        ),
        body: appsModel==null&&photoUrl==""?LoadingOverlay():RefreshIndicator(
                  strokeWidth: 3,
                  backgroundColor: appWhiteColor,
                  color: appBlackColor,
                  onRefresh: ()=>getAllApps(consoleUserModel!.data,consoleUserModel!.token),
                  child:  appsModel!=null?allAppsUI():
                  NoData(title: "Your apps list is empty ."),
                )


      ),
    );
  }

  Widget navigationDrawerUI(){
    return Drawer(
      backgroundColor: appBarColor,
      child: Column(children:  [

        Align(
          alignment: Alignment.centerLeft,
          child: GestureDetector(
            onTap: (){
              navPush(context, const EditProfileActivity());
            },
            child: Padding(
              padding: const EdgeInsets.only(left:20.0,top: 60),
              child: ClipRRect(
                borderRadius: BorderRadius.circular(50),
                child: Image.network(photoUrl,height: 70),
              ),
            ),
          ),
        ),

        Align(
            alignment: Alignment.centerLeft,
            child: Padding(
              padding: const EdgeInsets.only(left:15.0,top: 15),
              child: Text(name,style: GoogleFonts.aBeeZee(fontSize: 17,fontWeight:
                  FontWeight.bold),),
            )),
        Align(
            alignment: Alignment.centerLeft,
            child: Padding(
              padding: const EdgeInsets.only(left:15.0,top: 5),
              child: Text(emailAddress,style: GoogleFonts.aBeeZee(fontSize: 13),),
            )),
        const Padding(
          padding: EdgeInsets.only(top:15.0),
          child: Divider(height: 1,),
        ),

        Padding(
          padding: const EdgeInsets.fromLTRB(0.0,20,0,0),
          child: ListTile(
            leading: Image.asset(assetsPlaystoreImg,height: 25,width: 25,color: Colors.black.withOpacity(0.6),),
            title:  Text('Go To App Store',style: GoogleFonts.aBeeZee(fontSize: 16),),onTap:(){
            navPush(context,const BottomMenuActivity());
          },),
        ),


        Padding(
          padding: const EdgeInsets.fromLTRB(0.0,0,0,0),
          child: ListTile(
            leading: Image.asset(assetsChangePasswordImg,height: 25,width: 25,color:
            Colors.black.withOpacity(0.6),),
            title:  Text('Change Password',style: GoogleFonts.aBeeZee(fontSize: 16),),onTap:(){
              navPush(context,const ChangePasswordPage());
            },),
        ),

        ListTile(leading:  Icon(Icons.logout,size: 25,color:
        Colors.black.withOpacity(0.6),),
          title:  Text('Logout',style: GoogleFonts.aBeeZee(fontSize: 16),),onTap:(){
          setUser(null);
          Get.offAll(const CLoginActivity());
        },),
      ],),
    );
  }




  void _onBackPressed() {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        // return object of type Dialog
        return AlertDialog(
          backgroundColor: const Color(0xff262424),
          title: Text("Exit?",style: textStyleTitle(
              color: Colors.white,fontSize: 15.0)),
          content: new Text("Are you sure you want to exit?",style: textStyleTitle(
              color: Colors.white,fontSize: 15.0)),
          actions: <Widget>[
            new FlatButton(
              child: new Text(
                "CANCEL",
                style: TextStyle(
                  color: Colors.red,
                  fontFamily: 'Helvetica Neue',
                  fontSize: 14.899999618530273,
                ),
              ),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),

            new FlatButton(
              child: new Text(
                "OK",
                style: TextStyle(
                  color: Colors.red,
                  fontFamily: 'Helvetica Neue',
                  fontSize: 14.899999618530273,
                ),
              ),
              onPressed: () {
                exit(0);
              },
            ),
            // usually buttons at the bottom of the dialog
          ],
        );
      },
    );
  }

  Widget allAppsUI() {
    return ListView.builder(
      itemCount: appsModel!.data.length??0,
      itemBuilder: (ctx,index){
        Datum apps=appsModel!.data.elementAt(index);
        String appStatus='';
        if(apps.appStatus==0){
          appStatus="Unpublished";
        }else{
          appStatus="published";
        }
        return Padding(
          padding: const EdgeInsets.fromLTRB(10, 5,10,5),
          child: GestureDetector(
            onTap: (){
              Get.to(CAppsDetailsactivity(img:apps.appIcon, name: apps.appName,
                  status:appStatus, releases: apps.apkInformation.last.versionName, createAt: apps.createdDate,
                  screenShotsList: apps.screenShot, appIcon: apps.appIcon,
                  featureGraphic: apps.appGraphic, appId: apps.id,
                  appShortDescription: apps.appShortDescription,
                  appFullDescription: apps.appFullDescription,apkInfoList: apps.apkInformation,
              reviewList: apps.review,));
            },
            child: ContainerBg(
                width: double.infinity,
                child: Row(children: [
                  Padding(
                    padding: padding8,
                    child: ClipRRect(
                        borderRadius: BorderRadius.circular(10),
                        child: FadeInImage.assetNetwork(placeholder:
                        'assets/loader.gif',
                            image: apps.appIcon,height: 85,width: 85,)),
                  ),

                  Flexible(
                    child: Column(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        Padding(
                          padding: const EdgeInsets.fromLTRB(10,0, 10, 0),
                          child: Align(
                              alignment: Alignment.centerLeft,
                              child: Text(apps.appName,textAlign: TextAlign.left,style: textStyleTitle1(color:appBlackColor,
                                  fontSize: 19.0),)),
                        ),

                        Align(
                            alignment: Alignment.centerLeft,
                            child: Padding(
                              padding: const EdgeInsets.fromLTRB(10,8, 10, 0),
                              child: Text(appStatus.toUpperCase(),style: textStyleTitle(
                                  color:Colors.grey[700],fontSize: 16.0),),
                            )),
                      ],
                    ),
                  )
                ],), height: 130, circular:
            6.0, color: appWhiteColor),
          ),
        );
      },
    );
  }

   getAllApps(String userID,String token) async{

    Map<String,dynamic> body={
      'app_id':userID,
      'token':token
    };
    getAllConsoleAppsAPI(body).then((value){
      if(value.status){
        setState(() {
          appsModel=value.data;
        });
      }else{
        toast(value.message.toString());
      }
    });
  }




}


//curl --location --request GET 'https://api.nowpayments.io/v1/estimate?amount=3999.5000&currency_from=usd&currency_to=btc' \
// --header 'x-api-key: <your_api_key>'


