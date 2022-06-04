


import 'dart:convert';

import 'package:edge_alerts/edge_alerts.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:get/get.dart';
import 'package:http/http.dart' as http;
import 'package:fluttertoast/fluttertoast.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:trading_apps/api/apis.dart';
import 'package:trading_apps/api/user_data.dart';
import 'package:trading_apps/custom_ui/common_widget.dart';
import 'package:trading_apps/custom_ui/progress_dialog.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/screens/Shopping/MyAddressActivity.dart';
import 'package:trading_apps/utility/common_methods.dart';

class AddNewAddressActivity extends StatefulWidget {
  final String CType,AddressID,Name,EmailID,Address,Mobile,City,Country,State,Zipcode;

  const AddNewAddressActivity({Key? key,required this.CType,required this.AddressID,required this.Name,
    required this.EmailID,
  required this.Address,required this.Mobile,required this.City,required this.Country,
  required this.State,required this.Zipcode}) : super(key: key);

  @override
  _AddNewAddressActivityState createState() => _AddNewAddressActivityState();
}

class _AddNewAddressActivityState extends State<AddNewAddressActivity> {

  var editcontrollerName=TextEditingController();
  var editcontrollerAddress=TextEditingController();
  var editcontrollerMobileNo=TextEditingController();
  var editcontrollerCity=TextEditingController();
  var editcontrollerCountry=TextEditingController();
  var editcontrollerState=TextEditingController();
  var editcontrollerZipcode=TextEditingController();

  var _key=GlobalKey<FormState>();
  Color Hselected=APP_SECONDARY_COLOR;
  Color HtvColor=Color(0xffFFFFFF);


  Color Oselected=Color(0xfffae9c5);
  Color Otvselected=Color(0xff151414);


  Color Sselected=Color(0xfffae9c5);
  Color Stvselected=Color(0xff151414);

  Color Othselected=Color(0xfffae9c5);
  Color Othtvselected=Color(0xff151414);
  String AddressType="Home";



  @override
  void initState() {
    if(widget.CType=="Edit"){
      editcontrollerName.text=widget.Name;
      editcontrollerAddress.text=widget.Address;
      editcontrollerMobileNo.text=widget.Mobile;
      editcontrollerCity.text=widget.City;
      editcontrollerCountry.text=widget.Country;
      editcontrollerState.text=widget.State;
      editcontrollerZipcode.text=widget.Zipcode;
    }
    super.initState();
  }

  @override
  Widget build(BuildContext context) {

    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.black,
        title: Text("Add New Address",style: GoogleFonts.aBeeZee(color: Colors.white,
        fontSize: 15),),
      ),
      body: Form(
        key: _key,
        child: ListView(
          children: [
            Padding(
              padding: const EdgeInsets.only(top: 8.0, left: 18, right: 18),
              child: TextFormField(
                style: GoogleFonts.aBeeZee(fontSize: 14),
                controller: editcontrollerName,
                autofocus: true,
                textCapitalization: TextCapitalization.sentences,
                textInputAction: TextInputAction.next,
                inputFormatters: [
                  LengthLimitingTextInputFormatter(20),
                ],
                validator: (v){
                  if(v!.trim().isEmpty){
                    return "Please Enter Name .";
                  }
                  return null;
                },

                textAlign: TextAlign.left,
                decoration: InputDecoration(
                  enabledBorder: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(9),
                    borderSide: BorderSide(
                      color: Color(0xff000000),
                      width: 0.70,
                    ),
                  ),
                  contentPadding: EdgeInsets.only(left:10),
                  errorStyle: GoogleFonts.aBeeZee(fontSize: 14.5,color:
                  Colors.redAccent,fontWeight: FontWeight.w600),
                  focusColor: Color(0xff000000),
                  border: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(5.0),
                      borderSide: BorderSide(color: Color(0xff000000))
                  ),
                  filled: true,
                  fillColor: Color(0xffffffff),
                  focusedBorder: OutlineInputBorder(
                    borderSide: BorderSide(color: Color(0xff000000)),
                    borderRadius: BorderRadius.circular(9.0),
                  ),
                  hintText: "Enter name",
                  hintStyle: GoogleFonts.aBeeZee(
                      fontSize: 14, color: Colors.grey[800]),
                ),
              ),
            ),

            Padding(
              padding: const EdgeInsets.only(top: 8.0, left: 18, right: 18),
              child: TextFormField(
                style: GoogleFonts.aBeeZee(fontSize: 13),
                controller: editcontrollerAddress,
                textCapitalization: TextCapitalization.sentences,
                inputFormatters: [
                  LengthLimitingTextInputFormatter(100),
                ],
                textInputAction: TextInputAction.newline,
                keyboardType: TextInputType.multiline,
                maxLines: null,
                validator: (v){
                  if(v!.trim().isEmpty){
                    return "Please Enter Address .";
                  }
                  return null;
                },
                textAlign: TextAlign.left,
                decoration: InputDecoration(
                  enabledBorder: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(9),
                    borderSide: BorderSide(
                      color: Color(0xff000000),
                      width: 0.70,
                    ),
                  ),
                  contentPadding:
                  EdgeInsets.only(left:10,top: 40,bottom: 40),
                  errorStyle: GoogleFonts.aBeeZee(fontSize: 14.5,color:
                  Colors.redAccent,fontWeight: FontWeight.w600),
                  focusColor: Color(0xff000000),
                  border: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(5.0),
                      borderSide: BorderSide(color: Color(0xff000000))
                  ),
                  filled: true,
                  fillColor: Color(0xffffffff),
                  focusedBorder: OutlineInputBorder(
                    borderSide: BorderSide(color: Color(0xff000000)),
                    borderRadius: BorderRadius.circular(9.0),
                  ),
                  hintText: "Enter Address",
                  hintStyle: GoogleFonts.aBeeZee(
                      fontSize: 14, color: Colors.grey[800]),
                ),
              ),
            ),
            Padding(
              padding: const EdgeInsets.only(top: 10.0, left: 18, right: 18),
              child: TextFormField(
                style: GoogleFonts.aBeeZee(fontSize: 14),
                keyboardType: TextInputType.number,
                inputFormatters: [
                  LengthLimitingTextInputFormatter(15),
                  FilteringTextInputFormatter.digitsOnly
                ],
                textInputAction: TextInputAction.next,
                controller: editcontrollerMobileNo,
                validator: (v){
                  if(v!.trim().isEmpty){
                    return "Please Enter Mobile Number .";
                  }
                  return null;
                },
                textAlign: TextAlign.left,
                decoration: InputDecoration(
                  enabledBorder: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(9),
                    borderSide: BorderSide(
                      color: Color(0xff000000),
                      width: 0.70,
                    ),
                  ),
                  contentPadding: EdgeInsets.only(left: 10.8),
                  errorStyle: GoogleFonts.aBeeZee(fontSize: 14.5,color:
                  Colors.redAccent,fontWeight: FontWeight.w600),
                  focusColor: Color(0xff000000),
                  border: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(5.0),
                      borderSide: BorderSide(color: Color(0xff000000))
                  ),
                  filled: true,
                  fillColor: Color(0xffffffff),
                  focusedBorder: OutlineInputBorder(
                    borderSide: BorderSide(color: Color(0xff000000)),
                    borderRadius: BorderRadius.circular(9.0),
                  ),
                  hintText: "Enter Mobile Number",
                  hintStyle: GoogleFonts.aBeeZee(
                      fontSize: 14, color: Colors.grey[800]),
                ),

              ),
            ),

            Padding(
              padding: const EdgeInsets.only(top: 10.0, left: 18, right: 18),
              child: TextFormField(
                style: GoogleFonts.aBeeZee(fontSize: 14),
                keyboardType: TextInputType.text,
                inputFormatters: [
                  LengthLimitingTextInputFormatter(30),
                ],
                textInputAction: TextInputAction.next,
                controller: editcontrollerCity,
                textCapitalization: TextCapitalization.sentences,
                validator: (v){
                  if(v!.trim().isEmpty){
                    return "Please Enter City .";
                  }
                  return null;
                },
                textAlign: TextAlign.left,
                decoration: InputDecoration(
                  enabledBorder: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(9),
                    borderSide: BorderSide(
                      color: Color(0xff000000),
                      width: 0.70,
                    ),
                  ),
                  contentPadding: EdgeInsets.only(left:10.8),
                  errorStyle: GoogleFonts.aBeeZee(fontSize: 14.5,color:
                  Colors.redAccent,fontWeight: FontWeight.w600),
                  focusColor: Color(0xff000000),
                  border: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(5.0),
                      borderSide: BorderSide(color: Color(0xff000000))
                  ),
                  filled: true,
                  fillColor: Color(0xffffffff),
                  focusedBorder: OutlineInputBorder(
                    borderSide: BorderSide(color: Color(0xff000000)),
                    borderRadius: BorderRadius.circular(9.0),
                  ),
                  hintText: "Enter City",
                  hintStyle: GoogleFonts.aBeeZee(
                      fontSize: 14, color: Colors.grey[800]),
                ),

              ),
            ),

            Padding(
              padding: const EdgeInsets.only(top: 10.0, left: 18, right: 18),
              child: TextFormField(
                style: GoogleFonts.aBeeZee(fontSize: 14),
                keyboardType: TextInputType.text,
                inputFormatters: [
                  LengthLimitingTextInputFormatter(30),
                ],
                textInputAction: TextInputAction.next,
                controller: editcontrollerState,
                textCapitalization: TextCapitalization.sentences,
                validator: (v){
                  if(v!.trim().isEmpty){
                    return "Please Enter State .";
                  }
                  return null;
                },
                textAlign: TextAlign.left,
                decoration: InputDecoration(
                  enabledBorder: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(9),
                    borderSide: BorderSide(
                      color: Color(0xff000000),
                      width: 0.70,
                    ),
                  ),
                  contentPadding: EdgeInsets.only(left:10.8),
                  errorStyle: GoogleFonts.aBeeZee(fontSize: 14.5,color:
                  Colors.redAccent,fontWeight: FontWeight.w600),
                  focusColor: Color(0xff000000),
                  border: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(5.0),
                      borderSide: BorderSide(color: Color(0xff000000))
                  ),
                  filled: true,
                  fillColor: Color(0xffffffff),
                  focusedBorder: OutlineInputBorder(
                    borderSide: BorderSide(color: Color(0xff000000)),
                    borderRadius: BorderRadius.circular(9.0),
                  ),
                  hintText: "Enter State",
                  hintStyle: GoogleFonts.aBeeZee(
                      fontSize: 14, color: Colors.grey[800]),
                ),

              ),
            ),

            Padding(
              padding: const EdgeInsets.only(top: 10.0, left: 18, right: 18),
              child: TextFormField(
                style: GoogleFonts.aBeeZee(fontSize: 14),
                keyboardType: TextInputType.text,
                textCapitalization: TextCapitalization.sentences,
                inputFormatters: [
                  LengthLimitingTextInputFormatter(30),
                ],
                controller: editcontrollerCountry,
                textInputAction: TextInputAction.next,
                validator: (v){
                  if(v!.trim().isEmpty){
                    return "Please Enter Country .";
                  }
                  return null;
                },
                textAlign: TextAlign.left,
                decoration: InputDecoration(
                  enabledBorder: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(9),
                    borderSide: BorderSide(
                      color: Color(0xff000000),
                      width: 0.70,
                    ),
                  ),
                  contentPadding: EdgeInsets.only(left:10.8),
                  errorStyle: GoogleFonts.aBeeZee(fontSize: 14.5,color:
                  Colors.redAccent,fontWeight: FontWeight.w600),
                  focusColor: Color(0xff000000),
                  border: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(5.0),
                      borderSide: BorderSide(color: Color(0xff000000))
                  ),
                  filled: true,
                  fillColor: Color(0xffffffff),
                  focusedBorder: OutlineInputBorder(
                    borderSide: BorderSide(color: Color(0xff000000)),
                    borderRadius: BorderRadius.circular(9.0),
                  ),
                  hintText: "Enter Country",
                  hintStyle: GoogleFonts.aBeeZee(
                      fontSize: 14, color: Colors.grey[800]),
                ),

              ),
            ),

            //ZipCode
            Padding(
              padding: const EdgeInsets.only(top: 10.0, left: 18, right: 18),
              child: TextFormField(
                style: GoogleFonts.aBeeZee(fontSize: 14),
                keyboardType: TextInputType.phone,
                textCapitalization: TextCapitalization.sentences,
                inputFormatters: [
                  LengthLimitingTextInputFormatter(6),
                ],
                controller: editcontrollerZipcode,
                textInputAction: TextInputAction.done,
                validator: (v){
                  if(v!.trim().isEmpty){
                    return "Please Enter Zipcode .";
                  }
                  return null;
                },
                textAlign: TextAlign.left,
                decoration: InputDecoration(
                  enabledBorder: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(9),
                    borderSide: BorderSide(
                      color: Color(0xff000000),
                      width: 0.70,
                    ),
                  ),
                  contentPadding: EdgeInsets.only(left:10.8),
                  errorStyle: GoogleFonts.aBeeZee(fontSize: 14.5,color:
                  Colors.redAccent,fontWeight: FontWeight.w600),
                  focusColor: Color(0xff000000),
                  border: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(5.0),
                      borderSide: BorderSide(color: Color(0xff000000))
                  ),
                  filled: true,
                  fillColor: Color(0xffffffff),
                  focusedBorder: OutlineInputBorder(
                    borderSide: BorderSide(color: Color(0xff000000)),
                    borderRadius: BorderRadius.circular(9.0),
                  ),
                  hintText: "Enter Zipcode",
                  hintStyle: GoogleFonts.aBeeZee(
                      fontSize: 14, color: Colors.grey[800]),
                ),

              ),
            ),


            Padding(
              padding: const EdgeInsets.all(10.0),
              child: Container(
                height: 45,
                child: Row(children: [
                  Padding(
                    padding: const EdgeInsets.only(left:8.0),
                    child: GestureDetector(
                      onTap: (){
                        setState(() {
                          AddressType="Home";
                          if(AddressType=="Home"){
                            Hselected=APP_SECONDARY_COLOR;
                            HtvColor=Color(0xffffffff);
                            Oselected=Color(0xfffae9c5);
                            Otvselected=Color(0xff151414);
                            Sselected=Color(0xfffae9c5);
                            Stvselected=Color(0xff151414);
                            Othselected=Color(0xfffae9c5);
                            Othtvselected=Color(0xff151414);
                          }
                        });
                      },
                      child: Container(
                        decoration: BoxDecoration(
                          borderRadius: BorderRadius.circular(17),
                          color: Hselected,
                        ),
                        width: MediaQuery.of(context).size.width*.20,
                        height: 40,
                        child: Center(child: Text("Home",style: GoogleFonts.aBeeZee(color: HtvColor),)),
                      ),
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(left:8.0),
                    child: GestureDetector(
                      onTap: (){
                        setState(() {
                          AddressType="Office";
                          if(AddressType=="Office"){
                            Oselected=APP_SECONDARY_COLOR;
                            Otvselected=Color(0xffffffff);
                            Hselected=Color(0xfffae9c5);
                            HtvColor=Color(0xff151414);
                            Sselected=Color(0xfffae9c5);
                            Stvselected=Color(0xff151414);
                            Othselected=Color(0xfffae9c5);
                            Othtvselected=Color(0xff151414);
                          }
                        });
                      },
                      child: Container(
                        decoration: BoxDecoration(
                          borderRadius: BorderRadius.circular(17),
                          color: Oselected,
                        ),
                        width: MediaQuery.of(context).size.width*.20,
                        height: 40,
                        child: Center(child: Text("Office",style: GoogleFonts.aBeeZee(color:Otvselected),)),
                      ),
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(left:8.0),
                    child: GestureDetector(
                      onTap: (){
                        setState(() {
                          AddressType="Shop";
                          if(AddressType=="Shop"){
                            Sselected=APP_SECONDARY_COLOR;
                            Stvselected=Color(0xffffffff);
                            Hselected=Color(0xfffae9c5);
                            HtvColor=Color(0xff151414);
                            Oselected=Color(0xfffae9c5);
                            Otvselected=Color(0xff151414);
                            Othselected=Color(0xfffae9c5);
                            Othtvselected=Color(0xff151414);
                          }
                        });
                      },
                      child: Container(
                        decoration: BoxDecoration(
                          borderRadius: BorderRadius.circular(17),
                          color: Sselected,
                        ),
                        width: MediaQuery.of(context).size.width*.20,
                        height: 40,
                        child: Center(child: Text("Shop",style: GoogleFonts.aBeeZee(color: Stvselected),)),
                      ),
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(left:8.0),
                    child: GestureDetector(
                      onTap: (){
                        setState(() {
                          AddressType="Other";
                          if(AddressType=="Other"){
                            Othselected=APP_SECONDARY_COLOR;
                            Othtvselected=Color(0xffffffff);
                            Sselected=Color(0xfffae9c5);
                            Stvselected=Color(0xff151414);
                            Oselected=Color(0xfffae9c5);
                            Otvselected=Color(0xff151414);
                            Hselected=Color(0xfffae9c5);
                            HtvColor=Color(0xff151414);
                          }
                        });
                      },
                      child: Container(
                        decoration: BoxDecoration(
                          borderRadius: BorderRadius.circular(17),
                          color: Othselected,
                        ),
                        width: MediaQuery.of(context).size.width*.20,
                        height: 40,
                        child: Center(child: Text("Other",style: GoogleFonts.aBeeZee(color: Othtvselected),)),
                      ),
                    ),
                  ),
                ],),
              ),
            ),
            Padding(
              padding: const EdgeInsets.only(top:18.0,left: 40,right: 40),
              child: GestureDetector(
                onTap: (){
                  keyboardDismissed(context);
                  if(_key.currentState!.validate()){
                    if(widget.CType=="NAddress") {
                      saveAddressAPI();
                    }else if(widget.CType=="Edit"){
                      editAddressAPI();
                    }
                  }
                },
                child: Container(
                  height: 50,
                  decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(18),
                      color: APP_SECONDARY_COLOR
                  ),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Icon(Icons.check,size: 30,color: Colors.white,),
                      Padding(
                        padding: const EdgeInsets.only(left:15.0),
                        child: Center(child:
                        Text("Save",style: GoogleFonts.aBeeZee(color:Colors.white),)),
                      ),
                    ],
                  ),
                ),
              ),
            )
          ],
        ),
      ),
    );
  }

  void saveAddressAPI() async{
    showDialog(context: context, builder: (context) => ProgressDialog(),
        barrierDismissible: false).then((value){
      print(value);
    },);
   Map<String,String> mao={
     "user_id":userModel!.data.first.userId.toString(),
     "address_type":AddressType,
     "name":editcontrollerName.text,
     "email":userModel!.data.first.email,
     "mobile":editcontrollerMobileNo.text,
     "address":editcontrollerAddress.text,
    "city":editcontrollerCity.text,
     "country":editcontrollerCountry.text,
     'state':editcontrollerState.text,
     'zipcode':editcontrollerZipcode.text
   };

   try{
     final response=await http.post(Uri.parse(SaveAddressUrls),body: mao);
     Map data=json.decode(response.body);
     if(data["status"]!=ERROR){
       Navigator.of(context).pop();
       Fluttertoast.showToast(msg: data["message"].toString().capitalize.toString());
       Navigator.of(context).pop();
     }else{
       Navigator.of(context).pop();
       _edgeAlert(data["message"]);
      // Fluttertoast.showToast(msg: data["message"]);
     }
   }catch(e){
     Navigator.of(context).pop();
     _edgeAlert(e.toString());
   //  Fluttertoast.showToast(msg: e.toString());
   }
  }

  void editAddressAPI() async{

    showDialog(context: context, builder: (context) => ProgressDialog(),
        barrierDismissible: false).then((value){
      print(value);
    },);

    Map<String,String> mao={
      "user_id":userModel!.data.first.userId.toString(),
      "address_type":AddressType,
      "tbl_user_address_id":widget.AddressID,
      "name":editcontrollerName.text,
      "email":userModel!.data.first.email,
      "mobile":editcontrollerMobileNo.text,
      "address":editcontrollerAddress.text,
      "city":editcontrollerCity.text,
      "country":editcontrollerCountry.text,
      'state':editcontrollerState.text,
      'zipcode':editcontrollerZipcode.text
    };
    print(mao);
    try{
      final response=await http.post(Uri.parse(EditAddressAPI),body: mao);
      Map data=json.decode(response.body);
      if(data["status"]!=ERROR){
        Navigator.of(context).pop();
        Fluttertoast.showToast(msg: data["message"].toString().capitalize.toString());
        Navigator.of(context).pop();
      }else{
        Navigator.of(context).pop();
        _edgeAlert(data["message"]);
        //Fluttertoast.showToast(msg: data["message"]);
      }
    }catch(e){
      Navigator.of(context).pop();
     _edgeAlert(e.toString());
    }
  }
  void _edgeAlert(String description){
    edgeAlert(context, title: 'Uh oh!', description: description.toString(),
        gravity: Gravity.bottom,icon: Icons.error,backgroundColor: APP_SECONDARY_COLOR);
  }
}



