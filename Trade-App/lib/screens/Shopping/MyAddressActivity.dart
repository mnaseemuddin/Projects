


import 'dart:convert';

import 'package:get/get.dart';
import 'package:http/http.dart' as http;
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:provider/provider.dart';
import 'package:trading_apps/api/apis.dart';
import 'package:trading_apps/api/user_data.dart';
import 'package:trading_apps/models/MyAddress.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/screens/Shopping/CartActivity.dart';
import 'package:trading_apps/screens/Shopping/OrderReviewActivity.dart';
import 'package:trading_apps/utility/common_methods.dart';
import 'package:trading_apps/utility/connectivityprovider.dart';

import 'AddNewAddressActivity.dart';

class MyAddressActivity extends StatefulWidget {
  const MyAddressActivity({ Key? key }) : super(key: key);

  @override
  _MyAddressActivityState createState() => _MyAddressActivityState();
}

class _MyAddressActivityState extends State<MyAddressActivity> {
  late List <MyAddress>myAddressList=[];

  var IsLoading=true;
  int rowIndex=0;

  @override
  void initState() {
    Provider.of<ConnectivityProvider>(context,listen: false).startMonitoring();
     getmyAddressAPI();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Consumer<ConnectivityProvider>(builder: (ctx,data,child){
      if(data.isOnline!=null){
        return data.isOnline?Scaffold(
          appBar: AppBar(
            brightness: Brightness.dark,
            backgroundColor: Colors.black,
            title: Text("My Address",style: GoogleFonts.aBeeZee(fontSize: 16),),
          ),
          body: ListView(
            children: [
              GestureDetector(
                onTap: (){
                  // navPush(context,AddNewAddressActivity(CType: "NAddress",AddressID: "",Name: "",EmailID: "",
                  //   Address: "",Mobile: "",
                  //   City: "",Country: "",));

                  Navigator.of(context).push(new MaterialPageRoute(builder: (_)=>
                      AddNewAddressActivity(CType: "NAddress",AddressID: "",Name: "",EmailID: "",
                        Address: "",Mobile: "",
                        City: "",Country: "",State: '',Zipcode: "",))).then((value){
                    getmyAddressAPI();
                  });
                },
                child: Container(
                  decoration: BoxDecoration(
                    color: const Color(0xffffffff),
                    border: Border.all(width: 1.0, color:
                    Color(0xfff3efef)),
                  ),
                  height: MediaQuery.of(context).size.height*0.06,
                  child: Row(
                    children: [
                      Align(
                          alignment: Alignment.centerLeft,
                          child: Padding(
                            padding: const EdgeInsets.all(8.0),
                            child: Text("ADD NEW ADDRESS",style: GoogleFonts.aBeeZee(color: Colors.black),),
                          )),
                      Spacer(), Padding(
                        padding: const EdgeInsets.only(right:8.0),
                        child: Icon(Icons.add,size: 18.5,color: Colors.black,),
                      ),
                    ],
                  ),
                ),
              ),
              Expanded(
                flex: 80,
                child: Container(
                  // height: MediaQuery.of(context).size.height*0.90,
                    child: IsLoading==false?SingleChildScrollView(
                      scrollDirection: Axis.vertical,
                      child: Column(
                        children: [
                          myAddressList.isEmpty?Container(
                            height: MediaQuery.of(context).size.height/1.2,
                            child: Align(
                                alignment: Alignment.center,
                                child: NoData()),
                          ):AllMyAddressUI(),
                          SizedBox(height: 100,)
                        ],
                      ),
                    ):Center(child: Container(
                      padding: EdgeInsets.all(16),
                      decoration: BoxDecoration(
                          color: APP_SECONDARY_COLOR,
                          borderRadius: BorderRadius.all(Radius.circular(16))
                      ),
                      child: CircularProgressIndicator(strokeWidth: 6.0),
                    ))),
              ),
            ],
          ),
        ):NoInternetConnection();
      }
      return Container(
        child: Center(
          child: CircularProgressIndicator(),
        ),
      );
    });
  }
  Widget AllMyAddressUI() {
    return ListView.builder(
        physics: NeverScrollableScrollPhysics(),
        scrollDirection: Axis.vertical,
        shrinkWrap: true,
        itemCount: myAddressList.length == null
            ? 0
            : myAddressList.length,
        itemBuilder: (BuildContext ctx, int i) {

          if(myAddressList[i].addressType=="Office"){
            myAddressList[i].imgString="assets/images/briefcase1.png";
          }else if(myAddressList[i].addressType=="Home"){
            myAddressList[i].imgString="assets/images/home1.png";
          }else if(myAddressList[i].addressType=="Shop"){
            myAddressList[i].imgString="assets/images/bag.png";
          }else if(myAddressList[i].addressType=="Other"){
            myAddressList[i].imgString="assets/images/painting.png";
          }
          if(rowIndex==i){
            myAddressList[i].color=Color(0xfffceddd);
            myAddressList[i].colorBoxWidth=Colors.black;
            myAddressList[i].isAddressSelected=true;
          }else{
            myAddressList[i].colorBoxWidth=Color(0xffFFFFFF);
            myAddressList[i].color=Color(0xffffffff);
            myAddressList[i].isAddressSelected=false;
          }
          // if(rowIndex==i){
          //   myAddressList[i].color=Color(0xfffceddd);
          //   myAddressList[i].IsAddressSelected=true;
          // }else{
          // mao = {_InternalLinkedHashMap} size = 8
          // 0 = {map entry} "user_id" -> "26"
          // 1 = {map entry} "address_type" -> "Office"
          // 2 = {map entry} "name" -> "Vikram"
          // 3 = {map entry} "email" -> "manojbisht842@gmail.com"
          // 4 = {map entry} "mobile" -> "9562323232"
          // 5 = {map entry} "address" -> "Hno 782 PalamPur Himachal "
          // 6 = {map entry} "city" -> "Himachal"
          // 7 = {map entry} "country" -> "India"
          //   myAddressList[i].color=Color(0xffffffff);
          //   myAddressList[i].IsAddressSelected=false;
          // }
          return Padding(
            padding: const EdgeInsets.only(
                top: 15.0, left: 8, right: 8),
            child: GestureDetector(
              onTap: (){
                setState(() {
                  rowIndex=i;
                  navPush(context, OrderReviewActivity(
                    AddressId: myAddressList[i].tblUserAddressId.toString(),
                    AddressType: myAddressList[i].addressType,
                     Name: myAddressList[i].name,EmailId: myAddressList[i].email,
                    Address: myAddressList[i].address,MobileNo: myAddressList[i].mobile,
                    City: myAddressList[i].city,Country: myAddressList[i].country,State: myAddressList[i].state,
                      ZipCode: myAddressList[i].zipcode.toString()

                  ));
                });
              },
              child: Container(
                decoration: BoxDecoration(
                  border: Border.all(
                      width: 0.80, color: myAddressList[i].colorBoxWidth),
                  borderRadius: BorderRadius.circular(5),
                  color:  myAddressList[i].color,
                ),
                height: 150,
                child: Row(
                  children: [
                    Container(
                      width: MediaQuery.of(context).size.width*0.60,
                      child: Container(
                        height: double.infinity,
                        width: double.infinity,
                        child: Column(
                          children: [
                            Row(
                              children: [
                                Padding(
                                  padding: const EdgeInsets.only(left:8.0,top:10,
                                      bottom: 8),
                                  child: Image.asset(myAddressList[i].imgString,height:20,
                                    width: 20,color: Colors.black,),
                                ),
                                Align(
                                    alignment: Alignment.topLeft,
                                    child: Padding(
                                      padding: const EdgeInsets.only(left:8.0,top:10,
                                          bottom: 8),
                                      child: Text(myAddressList[i].addressType.toUpperCase(),style:
                                      GoogleFonts.aBeeZee(fontSize: 14.6,color:
                                      Colors.black,fontWeight: FontWeight.bold),),
                                    )),
                              ],
                            ),

                            Align(
                                alignment: Alignment.topLeft,
                                child: Padding(
                                  padding: const EdgeInsets.only(left:8.0),
                                  child: Text(myAddressList[i].name,maxLines: 1, overflow: TextOverflow.ellipsis,style:
                                  GoogleFonts.aBeeZee(fontSize: 14,color: Colors.black),),
                                )),

                            Align(
                                alignment: Alignment.topLeft,
                                child: Padding(
                                  padding: const EdgeInsets.only(left:8.0,top: 4),
                                  child: Text(myAddressList[i].address,maxLines: 2,
                                    overflow: TextOverflow.ellipsis,style:
                                  GoogleFonts.aBeeZee(fontSize: 15,color: Colors.black),),
                                )),

                            Align(
                                alignment: Alignment.topLeft,
                                child: Padding(
                                  padding: const EdgeInsets.only(left:8.0,top: 0),
                                  child: Text(myAddressList[i].state+' '+myAddressList[i].city+' '+myAddressList[i].country+
                                      ' '+myAddressList[i].zipcode.toString(),
                                    maxLines: 1, overflow: TextOverflow.ellipsis,style:
                                    GoogleFonts.aBeeZee(fontSize: 15,color: Colors.black),),
                                )),

                            Padding(
                              padding: const EdgeInsets.only(left:8.0,top: 4),
                              child: Row(
                                children: [
                                  Text("Contact : ",style:
                                  GoogleFonts.aBeeZee(fontSize: 14,color:
                                  Colors.grey[900]),),
                                  Align(
                                      alignment: Alignment.topLeft,
                                      child: Text(myAddressList[i].mobile,style:
                                      GoogleFonts.aBeeZee(fontSize: 14,color: Colors.
                                      grey[900]),)),
                                ],
                              ),
                            ),


                          ],
                        ),
                      ),
                    ),
                    Container(
                      width: MediaQuery.of(context).size.width*0.34,
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.end,
                        children: [
                          PopupMenuButton(
                              elevation: 40,
                              enabled: true,
                              onSelected: (value) {
                                if(value=="delete"){
                                  _deleteAddressUsingDialog(myAddressList[i].tblUserAddressId);
                                }else if(value=="edit"){
                                  Navigator.of(context).push(new MaterialPageRoute(builder: (_)=>
                                      AddNewAddressActivity( CType: "Edit",AddressID: myAddressList[i]
                                          .tblUserAddressId.toString(),Name: myAddressList[i].name,EmailID: myAddressList[i].email,
                                        Address: myAddressList[i].address,Mobile: myAddressList[i].mobile,
                                        City: myAddressList[i].city,Country: myAddressList[i].country,State: myAddressList[i].state,
                                        Zipcode: myAddressList[i].zipcode.toString()))).then((value){
                                    getmyAddressAPI();
                                  });
                                }
                              },
                              itemBuilder: ( context)=>[
                                PopupMenuItem(
                                  child: Container(
                                      width: 140,
                                      child: Text("Edit")),
                                  value: "edit",
                                ),
                                PopupMenuItem(
                                  child: Container(
                                      width: 140,
                                      child: Text("Delete")),
                                  value: "delete",
                                ),
                              ]
                          ),

                          Spacer(),
                          Visibility(
                            visible: myAddressList[i].isAddressSelected,
                            child: Padding(
                              padding: const EdgeInsets.all(15.0),
                              child: Align(
                                alignment: Alignment.bottomRight,
                                child: Container(
                                  height: 40,
                                  width: 40,
                                  decoration: BoxDecoration(
                                    border: Border.all(
                                        width: 1.0, color: const Color(
                                        0xff0a0a0a)),
                                    borderRadius: BorderRadius.circular(25),
                                    color:  Color(0xfffceddd),
                                  ),
                                  child: Icon(Icons.check,size: 25,color:Color(0xff0a0a0a)),
                                ),
                              ),
                            ),
                          )
                        ],
                      ),
                    ),
                  ],
                ),
              ),
            ),
          );
        });
  }

  void getmyAddressAPI() async{

    try{
      final response=await http.get(Uri.parse(GetAddressListAPI+userModel!.data.first.userId.toString()),
          headers: headers);
       Map data=json.decode(response.body);
       if(!mounted)
         return;
       setState(() {
         myAddressList.clear();
         IsLoading=false;
         if(data["status"]!=ERROR){
           var res=data["data"] as List;
           myAddressList=res.map((e) => MyAddress.fromJson(e)).toList();
         }else{
           Fluttertoast.showToast(msg: data["message"]);
         }
       });
    }catch(e){
      Fluttertoast.showToast(msg: e.toString());
    }

}

  void deleteAddressAPI(int tblAdddressId) async{
    try{
      final response=await http.get(Uri.parse(DeleteAddressAPI+tblAdddressId.toString()),headers: headers);
      Map data=json.decode(response.body);
      if(data["status"]!=ERROR){
        Navigator.of(context).pop();
        Fluttertoast.showToast(msg: data["message"]);
        getmyAddressAPI();
      }else{
        Fluttertoast.showToast(msg: data["message"]);
      }
    }catch(e){
      Fluttertoast.showToast(msg: e.toString());
    }
}

  void _deleteAddressUsingDialog(int addressId) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        // return object of type Dialog
        return AlertDialog(
          title: Text("Confirm Delete?"),
          content: new Text("Are you sure you want to delete this address?"),
          actions: <Widget>[
            new TextButton(
              child: new Text("CANCEL",style: TextStyle(color: Colors.red, fontFamily: 'Helvetica Neue',
                fontSize: 14.899999618530273,),),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),

            new TextButton(
              child: new Text("OK",style: TextStyle(color: Colors.red, fontFamily: 'Helvetica Neue',
                fontSize: 14.899999618530273,),),
              onPressed: () {
                deleteAddressAPI(addressId);

              },
            ),
            // usually buttons at the bottom of the dialog

          ],
        );
      },
    );
  }
}