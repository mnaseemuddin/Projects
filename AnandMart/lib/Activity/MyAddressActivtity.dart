




import 'dart:convert';

import 'package:anandmart/Activity/AddNewAddressActivity.dart';
import 'package:anandmart/Activity/AddressAndDateTimeActivity.dart';
import 'package:anandmart/ApplicationConfigration/ApiUrls.dart';
import 'package:anandmart/Model/MyAddress.dart';
import 'package:anandmart/Support/AlertDialogManager.dart';
import 'package:anandmart/Support/SharePreferenceManager.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:http/http.dart'as http;

class MyAddressActivity extends StatefulWidget {
  final String CType;
  const MyAddressActivity({Key? key,required this.CType}) : super(key: key);

  @override
  _MyAddressActivityState createState() => _MyAddressActivityState();
}

class _MyAddressActivityState extends State<MyAddressActivity> {

  bool IsLoading=true;
  late Map data;
  List <MyAddress>myAddressList=[];
  String UserId="";
  String? _value='';
  late Map map;

  int rowIndex=0;

  @override
  void initState() {
    SharePreferenceManager.instance.getUserID("UserID").then((value){
      setState(() {
        UserId=value;
        getUserAddress(UserId);
      });
    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
         brightness: Brightness.dark,
        actions: [
          Align(
          alignment: Alignment.centerRight,
            child: Container(width: 30,),
          )
        ],
        backgroundColor: Color(0xfff4811e),
        title: Center(child: Text("My Address",style: GoogleFonts.aBeeZee(fontSize: 16,color:Colors.white),)),
      ),
      body: ListView(
        physics: NeverScrollableScrollPhysics(),
        children: [
          GestureDetector(
            onTap: (){
                Get.to(AddNewAddressActivity(CType: "NewAddress",AID: "",
                                    Title: "",FName: "",
                                    Locality: "",City1: "",
                                    MobileNo: "",Address: "",Address1: "",));
            },
            child: Container(
              decoration: BoxDecoration(
                color: const Color(0xffffffff),
                border: Border.all(width: 1.0, color:
                Color(0xfff3efef)),
              ),
              height: MediaQuery.of(context).size.height*0.07,
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
          Container(
              height: MediaQuery.of(context).size.height*0.90,
              child: IsLoading==false?SingleChildScrollView(
                scrollDirection: Axis.vertical,
                child: Column(
                  children: [
                    myAddressUI(),
                    SizedBox(height: 100,)
                  ],
                ),
              ):Center(child: CircularProgressIndicator(strokeWidth: 6.0,color:
          Color(0xfff4811e),))),
        ],
      ),
    );
  }
  getUserAddress(String userId)async{
    Map<String,dynamic>hashmap={
      "uid":userId
    };

    String reqUrls=Uri(queryParameters: hashmap).query;
    final response=await http.get(Uri.parse(ApiUrls.GetUserAddrressAPI+"?"+reqUrls));
    if(response.statusCode==200)
      data=json.decode(response.body);
    setState(() {
       IsLoading=false;
      myAddressList.clear();
      print(data.toString());
      if(data['status']=='success') {
        print(data.toString());
        var res = data["address"] as List;
        myAddressList =
            res.map((json) => MyAddress.formJson(json)).toList();
      
      }
    });
  }

  Widget myAddressUI() {
    return ListView.builder(
      physics: NeverScrollableScrollPhysics(),
            scrollDirection: Axis.vertical,
            shrinkWrap: true,
            itemCount: myAddressList.length == null
                ? 0
                : myAddressList.length,
            itemBuilder: (BuildContext ctx, int i) {
              if(rowIndex==i){
                myAddressList[i].IsCheckMark=true;
              }else{
                 myAddressList[i].IsCheckMark=false;
              }
              return Padding(
                padding: const EdgeInsets.only(
                    top: 15.0, left: 3, right: 3),
                child: GestureDetector(
                  onTap: (){
                    setState(() {
                      if(widget.CType==""){
                        rowIndex=i;
                    String completeAddress=myAddressList[i].locality+" "+myAddressList[i].address+" "+myAddressList[i].city+" "+
                    myAddressList[i].mobileno;
                    Get.to(AddressAndDateTimeActivity(completeAddress: completeAddress,Title:
                    myAddressList[i].title,Locality:myAddressList[i].locality,City:myAddressList[i].city,
                    Address: myAddressList[i].address,AID: myAddressList[i].adId,MobileNo:myAddressList[i].mobileno));
                      }
                    });
                  },
                  child: Container(
                    decoration: BoxDecoration(
                      border: Border.all(
                          width: 1.0, color: const Color(0xffece7e7)),
                      borderRadius: BorderRadius.circular(5),
                      color: const Color(0xffffffff),
                    ),
                    height: 140,
                    child: Row(
                      children: [
                        Container(
                          width: MediaQuery.of(context).size.width*0.60,
                          child: Container(
                            height: double.infinity,
                            width: double.infinity,
                            child: Column(
                              children: [
                                Align(
                                    alignment: Alignment.topLeft,
                                    child: Padding(
                                      padding: const EdgeInsets.only(left:8.0,top:10,bottom: 8),
                                      child: Text(myAddressList[i].title.toUpperCase(),style:
                                      GoogleFonts.aBeeZee(fontSize: 14.6,color: Colors.black),),
                                    )),

                                Align(
                                    alignment: Alignment.topLeft,
                                    child: Padding(
                                      padding: const EdgeInsets.only(left:8.0),
                                      child: Text(myAddressList[i].fname,maxLines:1,overflow:
                                        TextOverflow.ellipsis,style:
                                      GoogleFonts.aBeeZee(fontSize: 13,color: Colors.grey[600]),),
                                    )),

                                Align(
                                    alignment: Alignment.topLeft,
                                    child: Padding(
                                      padding: const EdgeInsets.only(left:8.0,top: 5),
                                      child: Text(myAddressList[i].address+" "+myAddressList[i].city,maxLines: 2,
                                        overflow: TextOverflow.ellipsis,style:
                                      GoogleFonts.aBeeZee(fontSize: 13,color: Colors.grey[600]),),
                                    )),

                                Align(
                                    alignment: Alignment.topLeft,
                                    child: Padding(
                                      padding: const EdgeInsets.only(left:8.0,top: 5),
                                      child: Text(myAddressList[i].locality,maxLines:1,overflow: TextOverflow.ellipsis,style:
                                      GoogleFonts.aBeeZee(fontSize: 13,color: Colors.grey[600]),),
                                    )),

                                Padding(
                                  padding: const EdgeInsets.only(left:8.0,top: 5),
                                  child: Row(
                                    children: [
                                      Text("Contact : ",style:
                                      GoogleFonts.aBeeZee(fontSize: 13,color: Colors.grey[600]),),
                                      Align(
                                          alignment: Alignment.topLeft,
                                          child: Text(myAddressList[i].mobileno,maxLines: 1,overflow: TextOverflow.ellipsis,style:
                                          GoogleFonts.aBeeZee(fontSize: 13,color: Colors.grey[600]),)),
                                    ],
                                  ),
                                )

                              ],
                            ),
                          ),
                        ),
                        Container(
                          width: MediaQuery.of(context).size.width*0.37,
                          child: Column(
                              crossAxisAlignment: CrossAxisAlignment.end,
                                children: [
                              PopupMenuButton(
                              elevation: 40,
                              enabled: true,
                              onSelected: (value) {
                                setState(() {
                                  _value = value as String?;
                                  if(value=="edit"){
                                    print(myAddressList[i].address);
                                      Get.to(AddNewAddressActivity(CType: "Editable",AID: myAddressList[i].adId,
                                    Title: myAddressList[i].title,FName: myAddressList[i].fname,
                                    Locality: myAddressList[i].locality,City1: myAddressList[i].city,
                                    MobileNo: myAddressList[i].mobileno,Address: myAddressList[i].locality,Address1:
                                        myAddressList[i].address,));
                                  }else if(value=="delete"){
                                       setState(() {
                                          IsLoading=true;
                                      _deleteAddress(myAddressList[i].adId);
                                       });
                                  }
                                });
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

                                  //       child: Image.asset("drawable/menu.png",height: 18,width:
                                  // 18,color: Colors.black,)),

                                  Spacer(),
                                  Visibility(
                                    visible: myAddressList[i].IsCheckMark,
                                    child: Align(
                                      alignment: Alignment.bottomRight,
                                      child: Padding(
                                        padding: const EdgeInsets.all(15.0),
                                        child: Container(
                                          height: 40,
                                          width: 40,
                                          decoration: BoxDecoration(
                                            border: Border.all(
                                                width: 1.0, color: const Color(0xffece7e7)),
                                            borderRadius: BorderRadius.circular(25),
                                            color: const Color(0xfff4811e),
                                          ),
                                          child: Icon(Icons.check,size: 25,color:Colors.white),
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

  void _deleteAddress(String id) async{

  map={
   "uid":UserId,
   "aid":id
   };

  try{

   var ApiUrls1=Uri.parse(ApiUrls.DeleteAddressAPI);
   var response=await http.post(ApiUrls1,body: map);
   if(response.statusCode==200){
     map=json.decode(response.body);

   setState(() {
     if(map["status"]=="success"){
        getUserAddress(UserId);
     }else{
       Fluttertoast.showToast(msg: map["status"],gravity: ToastGravity.BOTTOM,textColor:
        Colors.black,fontSize: 12);
         getUserAddress(UserId);
     }
   });


   }

  }catch(e){
    AlertDialogManager().IsErrorAlertDialog(context, "Status", e.toString(),"");
  }

 }
}
