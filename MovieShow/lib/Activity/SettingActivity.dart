

import 'dart:convert';

import 'package:flutter_phone_direct_caller/flutter_phone_direct_caller.dart';
import 'package:http/http.dart' as http;
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:movieshow/Activity/EditProfileActivity.dart';
import 'package:movieshow/Activity/PrivacyPolicyActivity.dart';
import 'package:movieshow/Activity/TermsConditionActivity.dart';
import 'package:movieshow/ApplicationConfigration/ApiUrls.dart';
import 'package:movieshow/Authentication/SignInActivity.dart';

import 'package:movieshow/Support/AlertDialogManager.dart';
import 'package:movieshow/Support/SharePreferenceManager.dart';
import 'package:url_launcher/url_launcher.dart';
import 'dart:io';

import 'AboutUsActivity.dart';
class Setting extends StatefulWidget {
  const Setting({Key? key}) : super(key: key);

  @override
  _SettingState createState() => _SettingState();
}

class _SettingState extends State<Setting> {
  late Map map,map1;
  late String Name="",MobileNo="",EmailID="",UserImg="",Address="",City="",State="",
      Country="",Pincode="",BounsAmount='',UserID='';


  File? imgFile;
  String imgURL="";


  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.black,
      body: Container(
        height: double.infinity,
        child: ListView(
         children: [
           Center(
             child: Stack(
               children: [
                 Padding(
                   padding: const EdgeInsets.all(8.0),
                   child: Container(
                       height: 120,
                       width: 125,
                       child: imgURL==""?Image.asset("drawable/user_profile.png")
                   :CircleAvatar(
                         radius: 100,
                         child: ClipOval(
                           child: FadeInImage.assetNetwork(placeholder:
                           "drawable/user_profile.png", image: imgURL,
                             height: 120,width: 125,fit: BoxFit.fill,imageErrorBuilder:
                                 (context,error,stackTrace)=>Image.asset("drawable/user_profile.png",
                               height: 120,width: 125,),),
                         ),
                       )),
                 ),
                 Padding(
                   padding: const EdgeInsets.only(left:108.0,top:88),
                   child: GestureDetector(
                     onTap: (){
                       Get.to(EditProfile());
                     },
                     child: Container(
                       height: 30,
                       width: 30,
                       decoration: BoxDecoration(
                         color: Colors.grey[200],
                         borderRadius: BorderRadius.circular(45),
                       ),
                       child: Padding(
                         padding: const EdgeInsets.all(10.0),
                         child: Image.asset("drawable/pencil.png",height:10,width: 10,color:
                         Colors.black,),
                       ),
                     ),
                   ),
                 ),
               ],
             ),
           ),
           Center(
               child:Padding(
                 padding: const EdgeInsets.all(8.0),
                 child: Text(Name,style: TextStyle(color:
                 Colors.white,
                     fontFamily: 'Arial',fontSize: 14,fontWeight: FontWeight.bold)),
               )
           ),
           Center(
               child:Padding(
                 padding: const EdgeInsets.only(left:8.0,right: 8,bottom: 8,top: 2),
                 child: Text(EmailID,style: TextStyle(color:
                 Colors.white,
                     fontFamily: 'Arial',fontSize: 14,fontWeight: FontWeight.bold)),
               )
           ),

           Padding(
             padding: const EdgeInsets.only(top:30.0),
             child: Container(
               height: 50,
               decoration: BoxDecoration(
                   boxShadow: [
                     BoxShadow(
                       color: const Color(0xffffffff),
                       offset: Offset(4, 3),
                       blurRadius: 7,
                     ),
                   ],
                 color: Colors.black,
                 borderRadius: BorderRadius.circular(5),
                   border: Border.all(width: 2.0,color: Colors.grey)
               ),
               child: Padding(
                 padding: const EdgeInsets.all(8.0),
                 child: Row(
                   children: [
                     Expanded(child:
                     Container(
                       height: double.infinity,
                       child: Column(
                         mainAxisAlignment: MainAxisAlignment.center,
                         children: [
                           Row(
                             mainAxisAlignment: MainAxisAlignment.center,
                             children: [
                               Image.asset("drawable/rupees.png",height: 14,width: 14,
                                   color:Colors.white),
                               Padding(
                                 padding: const EdgeInsets.only(left:6.0),
                                 child: Text(BounsAmount,style:TextStyle(
                                     fontFamily: 'Helvetica Neue',fontSize: 13,fontWeight: FontWeight.w600
                                     ,color:Colors.white
                                 )),
                               )
                             ],
                           ),
                           Padding(
                             padding: const EdgeInsets.only(top:2.0),
                             child: Text("Signup Bonus",style:TextStyle(
                                 fontFamily: 'Helvetica Neue',fontSize: 010,fontWeight: FontWeight.w600,
                               color: Colors.white
                             )),
                           )
                         ],
                       ),
                     )),
                     Container(
                       height: double.infinity,
                       width: 1,
                       color: Colors.white,
                     ),
                     Expanded(child:
                     GestureDetector(
                       onTap: (){
                         _callingBottomSheet(context);
                       },
                       child: Container(
                         height: double.infinity,
                         child: Row(
                           mainAxisAlignment: MainAxisAlignment.center,
                           children: [
                             Image.asset("drawable/phonecalls.png",height: 20,width: 20,
                                 color:Colors.white),

                             Padding(
                               padding: const EdgeInsets.only(left:8.0),
                               child: Text("Contact Us",style:TextStyle(
                                   fontFamily: 'Helvetica Neue',fontSize: 13,fontWeight: FontWeight.w600,
                                 color: Colors.white
                               ) ,),
                             )
                           ],
                         ),
                       ),
                     ))
                   ],
                 ),
               ),
             ),
           ),
           Padding(
             padding: const EdgeInsets.only(top:30.0),
             child: Container(
               height: 50,
               decoration: BoxDecoration(
                   color: Colors.black,
                   boxShadow: [
                     BoxShadow(
                       color: const Color(0xffffffff),
                       offset: Offset(4, 3),
                       blurRadius: 7,
                     ),
                   ],
                   borderRadius: BorderRadius.circular(5),
                   border: Border.all(width: 2.0,color: Colors.grey)
               ),
               child:Padding(
                 padding: const EdgeInsets.all(4.0),
                 child: Row(
                   children: [

                     Align(
                         alignment: Alignment.centerLeft,
                         child: Padding(
                           padding: const EdgeInsets.only(left:10.0),
                           child: Image.asset("drawable/premium-quality.png",height: 20,width: 20,fit: BoxFit.fill),
                         )),
                     Align(
                       alignment: Alignment.centerLeft,
                       child: Padding(
                         padding: const EdgeInsets.only(left:58.0),
                         child: Text("Go to be premium  ?",style: TextStyle(color:
                         Colors.white,
                             fontFamily: 'Arial',fontSize: 14,fontWeight: FontWeight.bold)),
                       ),
                     ),
                   ],
                 ),
               ),
             ),
           ),
           Padding(
             padding: const EdgeInsets.only(top:020.0),
             child: GestureDetector(
               onTap: (){
                 Get.to(TermsConditionActivity());
               },
               child: Container(
                 height: 50,
                 decoration: BoxDecoration(
                   color: Colors.black,
                   borderRadius: BorderRadius.circular(10),
                   border: Border.all(width: 2.0,color: Colors.grey),
                   boxShadow: [
                     BoxShadow(
                       color: const Color(0xffffffff),
                       offset: Offset(4, 3),
                       blurRadius: 7,
                     ),
                   ],
                 ),
                 child:Padding(
                   padding: const EdgeInsets.all(4.0),
                   child: Row(
                     children: [
                       Align(
                           alignment: Alignment.centerLeft,
                           child: Padding(
                             padding: const EdgeInsets.only(left:10.0),
                             child: Image.asset("drawable/termsandconditions.png",height: 020,width: 20,fit: BoxFit.fill),
                           )),
                       Align(
                         alignment: Alignment.centerLeft,
                         child: Padding(
                           padding: const EdgeInsets.only(left:58.0),
                           child: Text("Terms Conditions",style: TextStyle(color:
                           Colors.white,
                               fontFamily: 'Arial',fontSize: 14,fontWeight: FontWeight.bold)),
                         ),
                       ),
                     ],
                   ),
                 ),
               ),
             ),
           ),
           Padding(
             padding: const EdgeInsets.only(top:020.0),
             child: GestureDetector(
               onTap: (){
                 Get.to(PrivacyPolicyActivity());
               },
               child: Container(
                 height: 50,
                 decoration: BoxDecoration(
                   color: Colors.black,
                   borderRadius: BorderRadius.circular(10),
                   border: Border.all(width: 2.0,color: Colors.grey),
                   boxShadow: [
                     BoxShadow(
                       color: const Color(0xffffffff),
                       offset: Offset(2,2),
                       blurRadius: 7,
                     ),
                   ],
                 ),
                 child:Padding(
                   padding: const EdgeInsets.all(4.0),
                   child: Row(
                     children: [
                       Align(
                           alignment: Alignment.centerLeft,
                           child: Padding(
                             padding: const EdgeInsets.only(left:10.0),
                             child: Image.asset("drawable/compliant.png",height: 020,
                                 width: 20,fit: BoxFit.fill,color: Colors.white,),
                           )),
                       Align(
                         alignment: Alignment.centerLeft,
                         child: Padding(
                           padding: const EdgeInsets.only(left:58.0),
                           child: Text("Privacy Policy",style: TextStyle(color:
                           Colors.white,
                               fontFamily: 'Arial',fontSize: 14,fontWeight: FontWeight.bold)),
                         ),
                       ),
                     ],
                   ),
                 ),
               ),
             ),
           ),
           Padding(
             padding: const EdgeInsets.only(top:020.0),
             child: GestureDetector(
               onTap: (){
                 Get.to(AboustUsActivity());
               },
               child: Container(
                 height: 50,
                 decoration: BoxDecoration(
                   color: Colors.black,
                   borderRadius: BorderRadius.circular(10),
                   border: Border.all(width: 2.0,color: Colors.grey),
                   boxShadow: [
                     BoxShadow(
                       color: const Color(0xffffffff),
                       offset: Offset(2,2),
                       blurRadius: 7,
                     ),
                   ],
                 ),
                 child:Padding(
                   padding: const EdgeInsets.all(4.0),
                   child: Row(
                     children: [
                       Align(
                           alignment: Alignment.centerLeft,
                           child: Padding(
                             padding: const EdgeInsets.only(left:10.0),
                             child: Image.asset("drawable/information.png",height: 020,width: 20,fit: BoxFit.fill),
                           )),
                       Align(
                         alignment: Alignment.centerLeft,
                         child: Padding(
                           padding: const EdgeInsets.only(left:58.0),
                           child: Text("About Us",style: TextStyle(color:
                           Colors.white,
                               fontFamily: 'Arial',fontSize: 14,fontWeight: FontWeight.bold)),
                         ),
                       ),
                     ],
                   ),
                 ),
               ),
             ),
           ),
           Padding(
             padding: const EdgeInsets.only(top:020.0),
             child: GestureDetector(
               onTap: (){
                 Logout();
               },
               child: Container(
                 height: 50,
                 decoration: BoxDecoration(
                     color: Colors.black,
                     borderRadius: BorderRadius.circular(10),
                   border: Border.all(width: 2.0,color: Colors.grey),
                   boxShadow: [
                     BoxShadow(
                       color: const Color(0xffffffff),
                       offset: Offset(4, 3),
                       blurRadius: 7,
                     ),
                   ],
                 ),
                 child:Padding(
                   padding: const EdgeInsets.all(4.0),
                   child: Row(
                     children: [
                       Align(
                           alignment: Alignment.centerLeft,
                           child: Padding(
                             padding: const EdgeInsets.only(left:10.0),
                             child: Image.asset("drawable/logout.png",height: 020,width: 20,fit: BoxFit.fill),
                           )),
                       Align(
                         alignment: Alignment.centerLeft,
                         child: Padding(
                           padding: const EdgeInsets.only(left:58.0),
                           child: Text("Logout",style: TextStyle(color:
                           Colors.white,
                               fontFamily: 'Arial',fontSize: 14,fontWeight: FontWeight.bold)),
                         ),
                       ),
                     ],
                   ),
                 ),
               ),
             ),
           ),



         // Center(
         //   child: Padding(
         //     padding: const EdgeInsets.only(top:45.0),
         //     child: Text('Setting',style: TextStyle(color:
         //     Colors.white,
         //         fontFamily: 'Helvetica Neue',fontSize: 17.5,fontWeight: FontWeight.bold),),
         //   ),
         // ),
    //      GestureDetector(
    //        onTap: (){
    //          Get.to(EditProfile());
    //        },
    //        child: Row(
    //          children: [
    //            Align(
    //              alignment: Alignment.centerLeft,
    //              child: Padding(
    //                padding: const EdgeInsets.only(left:58.0),
    //                child: Text("Edit Profile",style: TextStyle(color:
    // Colors.white,
    // fontFamily: 'Helvetica Neue',fontSize: 15,fontWeight: FontWeight.bold)),
    //              ),
    //            ),
    //            Expanded(
    //              child: Align(
    //                alignment: Alignment.centerRight,
    //                  child: Padding(
    //                    padding: const EdgeInsets.only(right:65.0),
    //                    child: Image.asset("drawable/pencil.png",height:23,width: 23,color: Colors.white,),
    //                  )),
    //            )
    //          ],
    //        ),
    //      ),

    //      Line(),
//          Row(
//            children: [
//              Align(
//                alignment: Alignment.centerLeft,
//                child: Padding(
//                  padding: const EdgeInsets.only(left:58.0),
//                  child: Text("Signup Bonus",style: TextStyle(color:
//                  Colors.white,
//                      fontFamily: 'Helvetica Neue',fontSize: 15,fontWeight: FontWeight.bold)),
//                ),
//              ),
//              Expanded(
//                child: Row(
//                  mainAxisAlignment: MainAxisAlignment.end,
//                  children: [
//                    Text(BounsAmount,style: TextStyle(color:
//                    Colors.white, fontFamily: 'Helvetica Neue',fontSize: 16.5,
//                        fontWeight: FontWeight.bold)),
//                    Padding(
//                      padding: const EdgeInsets.only(right:65.0,left: 5),
//                      child: Image.asset("drawable/rupees.png",height: 23,width: 23,color: Colors.white,),
//                    ),
//                  ],
//                ),
//              )
//            ],
//          ),
//          Line(),
//          GestureDetector(
// onTap: (){
//   _callingBottomSheet(context);
// },
//            child: Row(
//              children: [
//                Align(
//                  alignment: Alignment.centerLeft,
//                  child: Padding(
//                    padding: const EdgeInsets.only(left:58.0),
//                    child: Text("Contact Us",style: TextStyle(color:
//                    Colors.white,
//                        fontFamily: 'Helvetica Neue',fontSize: 15,fontWeight: FontWeight.bold)),
//                  ),
//                ),
//                Expanded(
//                  child: Align(
//                      alignment: Alignment.centerRight,
//                      child: Padding(
//                        padding: const EdgeInsets.only(right:65.0),
//                        child: Padding(
//                          padding: const EdgeInsets.only(right:0.0),
//                          child: Image.asset("drawable/support.png",height: 27,width: 30,),
//                        ),
//                      )),
//                )
//              ],
//            ),
//          ),
//          Line(),
//          Row(
//            children: [
//              Align(
//                alignment: Alignment.centerLeft,
//                child: Padding(
//                  padding: const EdgeInsets.only(left:58.0),
//                  child: Text("Go to be premium  ?",style: TextStyle(color:
//                  Colors.white,
//                      fontFamily: 'Helvetica Neue',fontSize: 15,fontWeight: FontWeight.bold)),
//                ),
//              ),
//              Expanded(
//                child: Align(
//                    alignment: Alignment.centerRight,
//                    child: Padding(
//                      padding: const EdgeInsets.only(right:65.0),
//                      child: Image.asset("drawable/premium.png",height: 27,width: 30,fit: BoxFit.fill),
//                    )),
//              )
//            ],
//          ),
//          Line(),
//          GestureDetector(
//            onTap: (){
//              Logout();
//            },
//            child: Row(
//              children: [
//                Align(
//                  alignment: Alignment.centerLeft,
//                  child: Padding(
//                    padding: const EdgeInsets.only(left:58.0),
//                    child: Text("Logout ",style: TextStyle(color:
//                    Colors.white, fontFamily: 'Helvetica Neue',fontSize: 15,
//                        fontWeight: FontWeight.bold)),
//                  ),
//                ),
//                Expanded(
//                  child: Align(
//                      alignment: Alignment.centerRight,
//                      child: Padding(
//                        padding: const EdgeInsets.only(right:65.0),
//                        child: Image.asset("drawable/logout.png",height:24,width: 24,fit: BoxFit.cover,),
//                      )),
//                )
//              ],
//            ),
//          ),
//          Line()
         ],
        ),
      ),
    );
  }

  Widget Line() {
    return Padding(
      padding: const EdgeInsets.only(left: 0.0,right: 0,top: 10,bottom: 0),
      child: Container(
        height: 0.90,
        width: double.infinity,
        color: Colors.white,
      ),
    );
  }

  void MyProfileDeatils(String UserID)async{
    map={
      "userid":UserID,
    };
    try{
      // ignore: non_constant_identifier_names
      var ApiUrls=Uri.parse(APiUrls.MyProfile);
      var response=await http.post(ApiUrls,body: map);
      if(response.statusCode==200) {
        map = json.decode(response.body);
        setState(() {
          if(map["status"]=="1"){
             map1 =map['data'];
            Name=map1['name'];
            MobileNo=map1["mobile"];
            EmailID=map1["email"];
            UserImg=map1["user_image"];
           Address=map1["address"];
           City=map1["city"];
           State=map1["state"];
           Country=map1["country"];
            BounsAmount=map1["bonus_amount"];
           Pincode=map1["pincode"];
             imgURL=map1["user_image"];
          }else{
            AlertDialogManager().IsErrorAlertDialog(context, "Status", map["message"], "");
          }
        });
      }
    }catch(e){
      AlertDialogManager().IsErrorAlertDialog(context, "Status", e.toString(), "");
    }
  }
@override
  void initState() {
    SharePreferenceManager.instance.getUserID("UserID").then((value){
      setState(() {
        UserID=value;
       MyProfileDeatils(UserID);
      });
    });
    super.initState();
  }

  // ignore: non_constant_identifier_names
  void Logout() {

    showDialog(
      context: context,
      builder: (BuildContext context) {
        // return object of type Dialog
        return AlertDialog(
    
          content: new Text("Do you want to logout?"),
          actions: <Widget>[
new FlatButton(
              child: new Text("CANCEL",style: TextStyle(color: Colors.red, fontFamily: 'Helvetica Neue',
                                    fontSize: 14.899999618530273,),),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),

            new FlatButton(
              child: new Text("OK",style: TextStyle(color: Colors.red, fontFamily: 'Helvetica Neue',
                                    fontSize: 14.899999618530273,),),
              onPressed: () {
                SharePreferenceManager.instance.IsSharePreferenceRemove();
                   Get.offAll(SignIn());
              },
            ),
            // usually buttons at the bottom of the dialog
            
          ],
        );
      },
    );
    
  }
  void _callingBottomSheet(context){
    showModalBottomSheet(
        context: context,
        builder: (BuildContext bc){
          return Container(
            height: 150,
            color: Colors.transparent,
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
              Text("Please contact us on :",style: TextStyle(color:
              Colors.black,fontWeight: FontWeight.w500),),

                GestureDetector(
                    onTap: ()=>
                      _makePhoneCall("18602665035",true),
                  child: Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Icon(Icons.call,color: Colors.black,),
                        Padding(
                          padding: const EdgeInsets.only(left: 8.0),
                          child: Text("18602665035",style: TextStyle(color:
                          Colors.black,fontWeight: FontWeight.w500),),
                        ),
                      ],
                    ),
                  ),
                ),

                Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: Text("Email : glanceproduction@gmail.com",style: TextStyle(color:
                  Colors.black,fontWeight: FontWeight.w500),),
                ),
              ],
            ),
          );
        }
    );
  }
  // _makingPhoneCall() async {
  //   const url = 'tel:18602665035';
  //   if (await canLaunch(url)) {
  //     await launch(url);
  //   } else {
  //     throw 'Could not launch $url';
  //   }
  // }
  Future<void> _makePhoneCall(String contact, bool direct) async {
    if (direct == true) {
      bool? res = await FlutterPhoneDirectCaller.callNumber(contact);
    } else {
      String telScheme = 'tel:$contact';
      if (await canLaunch(telScheme)) {
        await launch(telScheme);
      } else {
        throw 'Could not launch $telScheme';
      }
    }
  }
  // void myProfileDeatils(String UserID)async{
  //   map={
  //     "userid":UserID,
  //   };
  //   try{
  //     // ignore: non_constant_identifier_names
  //     var ApiUrls=Uri.parse(APiUrls.MyProfile);
  //     var response=await http.post(ApiUrls,body: map);
  //     if(response.statusCode==200) {
  //       map = json.decode(response.body);
  //       setState(() {
  //         if(map["status"]=="1"){
  //           map1 =map['data'];
  //           imgURL=map1["user_image"];
  //         }else{
  //           AlertDialogManager().IsErrorAlertDialog(context, "Status", map["message"], "");
  //         }
  //       });
  //     }
  //   }catch(e){
  //     AlertDialogManager().IsErrorAlertDialog(context, "Status", e.toString(), "");
  //   }
  // }
}
