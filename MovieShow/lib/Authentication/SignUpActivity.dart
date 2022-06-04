


import 'package:email_validator/email_validator.dart';
import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:movieshow/Activity/DashBoard.dart';
import 'package:movieshow/ApplicationConfigration/ApiUrls.dart';
import 'package:movieshow/Model/Flag.dart';
import 'package:movieshow/Support/AlertDialogManager.dart';

import 'package:movieshow/Support/SharePreferenceManager.dart';
import 'package:movieshow/main.dart';
import 'dart:convert';
import 'package:http/http.dart' as http;

import 'OtpVerificationActivity.dart';
import 'SignInActivity.dart';

class SignUp extends StatefulWidget {
  const SignUp({Key? key}) : super(key: key);

  @override
  _SignUpState createState() => _SignUpState();
}

class _SignUpState extends State<SignUp> {
  TextEditingController editMobileEmail=new TextEditingController();
  TextEditingController editPassword=new TextEditingController();
  TextEditingController editFullName=new TextEditingController();
  // ignore: deprecated_member_use
  late List<Flag>allFlagList=[];
  late List GridList;
  bool IsVisibilityFlagWidget=false;
  late Map map,map1;
  TextEditingController editFlagFilter=new TextEditingController();

  late List<Flag> filterFlagList=List.from(allFlagList);
  var _key=GlobalKey<FormState>();

  String UserId="",Initilized="";
bool isEmail(String input) => EmailValidator.validate(input);
bool isPhone(String input) => RegExp(
  r'^[\+]?[(]?[0-9]{3}[)]?[-\s\.]?[0-9]{3}[-\s\.]?[0-9]{4,6}$'
).hasMatch(input);

  @override
  Widget build(BuildContext context) {


    return Scaffold(
        backgroundColor: const Color(0xff000000),
        body:Stack(
          children: [
            ListView(
              children: [
                Container(
                  height: MediaQuery.of(context).size.height*0.87,
                  child: Stack(
                    children: <Widget>[
                      Padding(
                        padding: const EdgeInsets.only(top:100.0),
                        child: Container(
                          height: 200,
                          width: double.infinity,
                          decoration: BoxDecoration(
                          ),
                          child: Center(child: Padding(
                            padding: const EdgeInsets.all(40.0),
                            child: Image.asset('drawable/logo.png'),
                          )),
                        ),
                      ),
                      Form(
                        key:_key,
                        child: Padding(
                          padding: const EdgeInsets.only(top:258.0),
                          child: Column(
                            children: [
                              Padding(
                                padding: const EdgeInsets.only(right:20.0,left: 20,top: 25),
                                child: TextFormField(

 validator: (v){
                                    if(v!.isEmpty){
                                      return "Please Enter Full Name .";
                                    }
                                    return null;
                                  },

                                  controller: editFullName,
                                  style: TextStyle(color: Colors.white),
                                  decoration: new InputDecoration(
                                      errorStyle: TextStyle(fontSize: 14.5,color: Colors.redAccent,fontWeight: FontWeight.w600),
                                      focusColor: Colors.white,
                                       border: OutlineInputBorder(
                                        borderRadius: BorderRadius.circular(5.0),
                                       borderSide: BorderSide(color: Color(0xff1f3349))
                                      ),
 filled: true,
                                      fillColor: Color(0xff1f3349),
                                      focusedBorder: OutlineInputBorder(
                        borderSide: BorderSide(color: Color(0xff1f3349)),
                        borderRadius: BorderRadius.circular(10.0),
                    ),
                                    prefixIcon: Icon(Icons.person_outline,color: Color(0xffffffff),),
                                    hintText: "Enter Name",
                                    hintStyle: TextStyle(
                                      fontFamily: 'Helvetica Neue',
                                      fontSize: 15,
                                      color: Color(0xffffffff),
                                      fontWeight: FontWeight.w500,
                                    ),
                                  ),
                                ),
                              ),

                              Padding(
                                padding: const EdgeInsets.only(right:20.0,left: 20,top: 15),
                                child: TextFormField(
                                   validator: (v){
                                    if(v!.isEmpty){
                                      return "Please Enter Mobile Number or EmailId .";
                                    }
                                    return null;
                                  },
                                  controller: editMobileEmail,
                                  style: TextStyle(color: Colors.white),
                                  decoration: new InputDecoration(
                                     errorStyle: TextStyle(fontSize: 14.5,color: Colors.redAccent,fontWeight: FontWeight.w600),
                                      focusColor: Colors.white,

                                       border: OutlineInputBorder(
                                        borderRadius: BorderRadius.circular(5.0),
                                       borderSide: BorderSide(color: Color(0xff1f3349))
                                      ),
 filled: true,
                                      fillColor: Color(0xff1f3349),
                                      focusedBorder: OutlineInputBorder(
                        borderSide: BorderSide(color: Color(0xff1f3349)),
                        borderRadius: BorderRadius.circular(10.0),
                    ),
                                    prefixIcon: Icon(Icons.phone_android_rounded,color: Color(0xffffffff),),
                                    hintText: "Mobile or Email",
                                    hintStyle: TextStyle(
                                      fontFamily: 'Helvetica Neue',
                                      fontSize: 15,
                                      color: Color(0xffffffff),
                                      fontWeight: FontWeight.w500,
                                    ),
                                  ),
                                ),
                              ),

                              Padding(
                                padding: const EdgeInsets.only(right:20.0,left: 20,top: 15),
                                child: TextFormField(
                                   validator: (v){
                                    if(v!.isEmpty){
                                      return "Please Enter Password .";
                                    }
                                    return null;
                                  },
                                  controller: editPassword,
                                  obscureText: true,
                                  style: TextStyle(color: Colors.white),
                                  decoration: new InputDecoration(
                                    hintText: "Enter Password",
                                     errorStyle: TextStyle(fontSize: 14.5,color: Colors.redAccent,fontWeight: FontWeight.w600),
                                      focusColor: Colors.white,

                                       border: OutlineInputBorder(
                                        borderRadius: BorderRadius.circular(5.0),
                                       borderSide: BorderSide(color: Color(0xff1f3349))
                                      ),
                                     filled: true,
                                      fillColor: Color(0xff1f3349),
                                      focusedBorder: OutlineInputBorder(
                        borderSide: BorderSide(color: Color(0xff1f3349)),
                        borderRadius: BorderRadius.circular(10.0),
                    ),
                                    prefixIcon: Icon(Icons.lock_open_outlined,color: Color(0xffffffff),),
                                    hintStyle: TextStyle(
                                      fontFamily: 'Helvetica Neue',
                                      fontSize: 15,
                                      color: const Color(0xffffffff),
                                      fontWeight: FontWeight.w500,
                                    ),
                                  ),
                                ),
                              ),
                              Padding(
                                padding: const EdgeInsets.only(right:20.0,left: 20,top: 35),
                                child: GestureDetector(
                                  onTap: (){
                                    setState(() {
                                      if(_key.currentState!.validate()){
                                        IsVisibilityFlagWidget=false;
                                  
                                   if(isEmail(editMobileEmail.text)||isPhone(editMobileEmail.text)){
                                     if(isEmail(editMobileEmail.text)==true){
                                       _signUp();
                                     }else if(isPhone(editMobileEmail.text)==true){
                                       _signUpUseOTP();
                                     }
                                      }else{
                                        AlertDialogManager().IsErrorAlertDialog(context, "",
                                        "Please Enter valid Mobile Number or EmailId.", "");
                                      }
                                    }
                                    });
                                  },
                                  child: Container(
                                    width: double.infinity,
                                    height: 50,
                                    decoration: BoxDecoration(
                                      borderRadius: BorderRadius.circular(6.0),
                                      color: const Color(0xffe61a1a),
                                      border: Border.all(width: 1.0, color: const Color(0xff707070)),
                                    ),
                                    child:  Center(
                                      child: Text(
                                        'Register',
                                        style: TextStyle(
                                          fontFamily: 'Helvetica Neue',
                                          fontSize: 15,
                                          color: const Color(0xffffffff),
                                          fontWeight: FontWeight.w700,
                                        ),
                                        textAlign: TextAlign.center,
                                      ),
                                    ),
                                  ),
                                ),
                              ),
                              
                            ],
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
                Container(
                  height: MediaQuery.of(context).size.height*0.13,
                 child: Padding(
                   padding: const EdgeInsets.only(bottom: 28.0),
                   child: Row(
                     mainAxisAlignment: MainAxisAlignment.center,
                     children: [
                       Text(
                         'Already have an account ? ',
                         style: TextStyle(
                           fontFamily: 'Helvetica Neue',
                           fontSize: 11.899999618530273,
                           color: const Color(0xffffffff),
                           fontWeight: FontWeight.w700,
                         ),
                         textAlign: TextAlign.right,
                       ),
                       Padding(
                         padding: const EdgeInsets.only(left:4.0),
                         child: GestureDetector(
                           onTap: (){
                             Get.to(SignIn());
                           },
                           child: Text(
                             'Login',
                             style: TextStyle(
                               fontFamily: 'Helvetica Neue',
                               fontSize: 11.899999618530273,
                               color: const Color(0xffe41e18),
                               fontWeight: FontWeight.w700,
                             ),
                             textAlign: TextAlign.right,
                           ),
                         ),
                       ),
                     ],
                   ),
                 ),
                )
              ],
            ),
            Visibility(
                        visible: IsVisibilityFlagWidget,
                        child: DraggableScrollableSheet(
                      initialChildSize: 0.80,
                      minChildSize: 0.0,
                      maxChildSize: 0.9,
                          builder: (BuildContext context, ScrollController scrollController) {
                            return Container(
                              color: Colors.black,
                              child: ListView(
                                children: [
                                  Container(
                                    height:52,
                                    child: Center(
                                      child: Padding(
                                        padding: const EdgeInsets.only(top: 0.0),
                                        child: TextField(
                                          textAlign: TextAlign.center,
                                        controller: editFlagFilter,
                                        onChanged: onItemChange,
                                        style: TextStyle(color: Colors.white),
                                          decoration: new InputDecoration(
                                            border: OutlineInputBorder(
                                                borderRadius: BorderRadius.circular(5.0),
                                                borderSide: BorderSide(color: Color(0xff1f3349))
                                            ),
                                            focusedBorder: OutlineInputBorder(
                                              borderSide: BorderSide(color: Color(0xff1f3349)),
                                              borderRadius: BorderRadius.circular(10.0),
                                            ),
filled: true,
                                            fillColor: Color(0xff1f3349),
                                            errorStyle: TextStyle(fontSize: 14.5,color: Colors.redAccent,fontWeight: FontWeight.w600),
                                            focusColor: Colors.white,
                                            prefixIcon: Icon(Icons.search_rounded,color: Color(0xffffffff),size:
                                              21,),
                                            hintText: "Search Country",
                                            hintStyle: TextStyle(
                                              fontFamily: 'Helvetica Neue',
                                              fontSize: 15,
                                              color: Color(0xffffffff),
                                              fontWeight: FontWeight.w500,
                                            ),
                                          ),
                                        // decoration: new InputDecoration(
                                        // hintTextDirection: TextDirection.ltr,
                                        // hintText: "Search Country",
                                        // border: InputBorder.none,
                                        // prefixIcon: Icon(Icons.search_rounded,color: Color(0xffffffff),size: 24,),
                                        // hintStyle: TextStyle(
                                        //   fontFamily: 'Helvetica Neue',
                                        //   fontSize: 15,
                                        //   color: const Color(0xffffffff),
                                        //   fontWeight: FontWeight.w500,
                                        // ),
                                        // ),
                                ),
                                      ),
                                    ),
                                  ),
                                  Container(
                                    height: MediaQuery.of(context).size.height*0.94,
                                     color: Colors.black,
                                    child: ListView(
                                      children: filterFlagList.map((e){
                                        return GestureDetector(
                                                      onTap: () {
                                                        Get.to(OTPVerfication(Initilized:Initilized,
                                                            UserID:UserId,MobileNo: editMobileEmail.text, ISDCode:e.ISDCode));
                                                      //  _signUpUseOTP(e.ISDCode);
                                                      },
                                                      child: Container(
                                                        height: 65,
                                                        child: Row(
                                                          mainAxisAlignment: MainAxisAlignment
                                                              .start,
                                                          children: [
                                                            Image.network(e.CountryFlag,
                                                              height: 130, width: 110,),
                                                            Flexible(
                                                              child: Padding(
                                                                padding: const EdgeInsets
                                                                    .only(left: 0.0),
                                                                child: Text(e.CountryName,
                                                                  style: TextStyle(
                                                                      color: Colors
                                                                          .white),),
                                                              ),
                                                            )
                                                          ],
                                                        ),
                                                      ),
                                                    );
                                      }).toList(),
                                    ),
                                    // child: ListView.builder(
                                    //     controller:scrollController,
                                    //     // ignore: unnecessary_null_comparison
                                    //     itemCount: filterFlagList.length==null?0:filterFlagList.length,
                                    //     itemBuilder: (BuildContext context,int i){
                                    //      // if(editFlagFilter.text.isEmpty) {
                                    //         return GestureDetector(
                                    //           onTap: () {
                                    //             Get.to(OTPVerfication());
                                    //           },
                                    //           child: Container(
                                    //             height: 80,
                                    //             child: Row(
                                    //               mainAxisAlignment: MainAxisAlignment
                                    //                   .start,
                                    //               children: [
                                    //                 Image.network(allFlagList[i]
                                    //                     .CountryFlag,
                                    //                   height: 130, width: 140,),
                                    //                 Padding(
                                    //                   padding: const EdgeInsets
                                    //                       .only(left: 0.0),
                                    //                   child: Text(allFlagList[i]
                                    //                       .CountryName,
                                    //                     style: TextStyle(
                                    //                         color: Colors
                                    //                             .white),),
                                    //                 )
                                    //               ],
                                    //             ),
                                    //           ),
                                    //         );
                                    //       // }else{
                                    //       //   for(Flag flag in allFlagList){
                                    //       //     if(flag.CountryName==editFlagFilter.text){
                                    //       //       filterFlagList.add(flag);
                                    //       //     }
                                    //       //   }
                                    //       //   return GestureDetector(
                                    //       //     onTap: () {
                                    //       //       Get.to(OTPVerfication());
                                    //       //     },
                                    //       //     child: Container(
                                    //       //       height: 80,
                                    //       //       child: Row(
                                    //       //         mainAxisAlignment: MainAxisAlignment
                                    //       //             .start,
                                    //       //         children: [
                                    //       //           Image.network(filterFlagList[i]
                                    //       //               .CountryFlag,
                                    //       //             height: 130, width: 140,),
                                    //       //           Padding(
                                    //       //             padding: const EdgeInsets
                                    //       //                 .only(left: 0.0),
                                    //       //             child: Text(filterFlagList[i]
                                    //       //                 .CountryName,
                                    //       //               style: TextStyle(
                                    //       //                   color: Colors
                                    //       //                       .white),),
                                    //       //           )
                                    //       //         ],
                                    //       //       ),
                                    //       //     ),
                                    //       //   );
                                    //       // }
                                    //     }),
                                  ),
                                ],
                              ),
                            );
                          },

                        ),
                      )
          ],
        )
    );
  }

  void allCountryFlag()async{
    try{
      // ignore: non_constant_identifier_names
      var ApiUrls=Uri.parse(APiUrls.Flag);
      var response=await http.get(ApiUrls);
      if(response.statusCode==200) {
        map = json.decode(response.body);
        setState(() {
          if(map["status"]=="1"){
            GridList =map['data'];
            for(int i=0;i<GridList.length;i++){
              Flag flag=new Flag(CountryName: GridList[i]['name'],
                  ISDCode: GridList[i]['countries_isd_code'],
                  ISOCode: GridList[i]['countries_iso_code'],
                  CountryFlag: GridList[i]['flag']);
              allFlagList.add(flag);
              if(allFlagList.length>0){
                IsVisibilityFlagWidget=true;
              }else{
                IsVisibilityFlagWidget=false;
              }
            }
          }else{
            AlertDialogManager().IsErrorAlertDialog(context, "Status", map["message"], "");
          }
        });
      }
    }catch(e){
      AlertDialogManager().IsErrorAlertDialog(context, "Status", e.toString(), "");
    }
  }


  void onItemChange(String value) {
   setState(() {
     filterFlagList=allFlagList.where((string) =>
         string.CountryName.toLowerCase().contains(value.toLowerCase())).toList();
   });
  }

  void ShowMsg(String msg) {
    Fluttertoast.showToast(
      msg: msg,
      toastLength: Toast.LENGTH_SHORT,
      gravity: ToastGravity.BOTTOM,
    );
  }
  _signUp() async {
     map={
      'name':editFullName.text,
      "email_or_mobile":editMobileEmail.text,
      "password":editPassword.text,
    };
  //  8935005528
  // {"message":"Registered Successfully.","status":"1","user_id":"290","data":
  // {"name":"ABCD","email":"8935005528","mobile":"8935005528","password":"123456","bonus_amount":"50"}}
    try{
      // ignore: non_constant_identifier_names
      var ApiUrls=Uri.parse(APiUrls.SignUp);
      var response=await http.post(ApiUrls,body: map);
      if(response.statusCode==200) {
        map = json.decode(response.body);
        if(map["status"]=="1"){
           map1 =map['data'];
          SharePreferenceManager.instance.setSharePreferenceInitialized("Initilized", "Exits");
          SharePreferenceManager.instance.setUserID("UserID",map["user_id"]);
          Get.offAll(DashBoard());
        }else{
          AlertDialogManager().IsErrorAlertDialog(context, "Status", map["message"], "");
        }
      }
    }catch(e){
      AlertDialogManager().IsErrorAlertDialog(context, "Status", e.toString(), "");
    }
  }
  _signUpUseOTP() async {
    map={
      'name':editFullName.text,
      "email_or_mobile":editMobileEmail.text,
      "password":editPassword.text,
    };
    //  8935005528
    // {"message":"Registered Successfully.","status":"1","user_id":"290","data":
    // {"name":"ABCD","email":"8935005528","mobile":"8935005528","password":"123456","bonus_amount":"50"}}
    try{
      // ignore: non_constant_identifier_names
      var ApiUrls=Uri.parse(APiUrls.SignUp);
      var response=await http.post(ApiUrls,body: map);
      if(response.statusCode==200) {
        map = json.decode(response.body);
        setState(() {
          if(map["status"]=="1"){
            map1 =map['data'];
            UserId=map["user_id"];
            Initilized="Exits";
            allCountryFlag();
          }else{
            AlertDialogManager().IsErrorAlertDialog(context, "Status", map["message"], "");
          }
        });
      }
    }catch(e){
      AlertDialogManager().IsErrorAlertDialog(context, "Status", e.toString(), "");
    }
  }
}
const String _svg_7vkce0 =
    '<svg viewBox="15.6 9.0 25.0 25.0" ><path transform="translate(15.0, 8.44)" d="M 25.5625 13.13856506347656 C 25.5625 6.191303253173828 19.96774291992188 0.5625 13.0625 0.5625 C 6.157258033752441 0.5625 0.5625 6.191303253173828 0.5625 13.13856506347656 C 0.5625 19.41544151306152 5.133568286895752 24.6182804107666 11.109375 25.5625 L 11.109375 16.77396011352539 L 7.933971405029297 16.77396011352539 L 7.933971405029297 13.13856506347656 L 11.109375 13.13856506347656 L 11.109375 10.36777400970459 C 11.109375 7.216151237487793 12.97429370880127 5.475278854370117 15.83064460754395 5.475278854370117 C 17.19858932495117 5.475278854370117 18.62903213500977 5.720714569091797 18.62903213500977 5.720714569091797 L 18.62903213500977 8.814021110534668 L 17.05241966247559 8.814021110534668 C 15.5 8.814021110534668 15.015625 9.783595085144043 15.015625 10.77801704406738 L 15.015625 13.13856506347656 L 18.48235893249512 13.13856506347656 L 17.92792510986328 16.77396011352539 L 15.015625 16.77396011352539 L 15.015625 25.5625 C 20.99143218994141 24.6182804107666 25.5625 19.41544151306152 25.5625 13.13856506347656 Z" fill="#f7f7f7" stroke="none" stroke-width="1" stroke-miterlimit="4" stroke-linecap="butt" /></svg>';

const String _svg_cdmsf6 =
    '<svg viewBox="7.0 11.6 18.0 18.0" ><path transform="translate(7.0, 11.0)" d="M 18 9.772982597351074 C 18 14.90806484222412 14.42581748962402 18.5625 9.147540092468262 18.5625 C 4.08688497543335 18.5625 0 14.54153156280518 0 9.5625 C 0 4.583467483520508 4.08688497543335 0.5625 9.147540092468262 0.5625 C 11.61147499084473 0.5625 13.68442535400391 1.451612830162048 15.28155612945557 2.917741775512695 L 12.79180240631104 5.272983551025391 C 9.534835815429688 2.181048393249512 3.478278398513794 4.503629207611084 3.478278398513794 9.5625 C 3.478278398513794 12.70161247253418 6.02704906463623 15.24556255340576 9.147540092468262 15.24556255340576 C 12.76967144012451 15.24556255340576 14.12704849243164 12.69072437286377 14.34098243713379 11.36612796783447 L 9.147540092468262 11.36612796783447 L 9.147540092468262 8.270563125610352 L 17.85614585876465 8.270563125610352 C 17.94098281860352 8.731451034545898 18 9.174193382263184 18 9.772982597351074 Z" fill="#f7f7f7" stroke="none" stroke-width="1" stroke-miterlimit="4" stroke-linecap="butt" /></svg>';
