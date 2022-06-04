


import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:provider/provider.dart';
import 'package:tailor/CommonUI/no_internet_connection.dart';
import 'package:tailor/Screens/Activity.dart';
import 'package:tailor/Screens/AddressNewAddressActivity.dart';
import 'package:tailor/Screens/CheckOutActivity.dart';
import 'package:tailor/CommonUI/common_widget.dart';
import 'package:tailor/Model/MyAddress.dart';
import 'package:tailor/Support/SharedPreferencesManager.dart';
import 'package:tailor/Support/UISupport.dart';
import 'package:http/http.dart' as http;
import 'package:tailor/Support/connectivity_provider.dart';
import '../ApiRepositary.dart';

class MyAddressActivity extends StatefulWidget {
  final String FType,OrderType;
  const MyAddressActivity({ Key? key,required this.FType,required this.OrderType}) : super(key: key);

  @override
  _MyAddressActivityState createState() => _MyAddressActivityState();
}

class _MyAddressActivityState extends State<MyAddressActivity> with WidgetsBindingObserver{
 
  var IsLoading=false;
  int rowIndex=0;
  MyAddress? _myAddress;

@override
  void initState() {
  Provider.of<ConnectivityProvider>(context,listen: false).startMonitoring();
     getAddressAPI(userModel!.data.first.UserId).then((value){
      setState(() {
        if(value.status){
        _myAddress=value.data;
        }else{
          _myAddress=null;
          Fluttertoast.showToast(msg: "No Address Found");
        }
      });
     });
    super.initState();
  }



  @override
  Widget build(BuildContext context) {
    return Consumer<ConnectivityProvider>(builder: (cContext,data,child){
      if(data.isOnline!=null){
        return data.isOnline?Scaffold(
          appBar: AppBar(
            brightness: Brightness.dark,
            backgroundColor: UISupport.APP_PRIMARY_COLOR,
            title: Text("My Address",style: GoogleFonts.aBeeZee(fontSize: 16),),
          ),
          body: ListView(
            children: [
              GestureDetector(
                onTap: (){
                  // Get.to(AddNewAddressActivity(FType: widget.FType,Type: "NewAddress",AID: "",
                  //                       Name: "",
                  //                        City: "",
                  //                       MobileNo: "",Address:"",ADType: "",
                  //                       State: "",ZipCode: "",OrderType: widget.OrderType,));

                  Navigator.of(context).push(new MaterialPageRoute(builder:
                      (_) => AddNewAddressActivity(FType: widget.FType,Type: "NewAddress",AID: "",
                    Name: "",
                    City: "",
                    MobileNo: "",Address:"",ADType: "",
                    State: "",ZipCode: "",OrderType: widget.OrderType,apptitle: "Add New Address",))).then((value){
                    getAddressAPI(userModel!.data.first.UserId).then((value){
                      setState(() {
                        if(value.status){
                          _myAddress=value.data;
                        }else{
                          _myAddress=null;
                          Fluttertoast.showToast(msg: "No Address Found");
                        }
                      });
                    });
                  });
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
                  child: _myAddress==null?Center(
                    child: _myAddress==null?NoData():CircularProgressIndicator(
                      strokeWidth: 5.6,
                    ),
                  ):SingleChildScrollView(
                    scrollDirection: Axis.vertical,
                    child: Column(
                      children: [
                        AllMyAddressUI(),
                        SizedBox(height: 100,)
                      ],
                    ),
                  )),
            ],
          ),
        ):InternetConnection();
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
            itemCount:  _myAddress!.data.length==null?0:_myAddress!.data.length,
            itemBuilder: (BuildContext ctx, int i) {
            Datum _address=_myAddress!.data.elementAt(i);
              if(_address.addressType=="Office"){
                _address.imgString="assets/briefcase1.png";
              }else if(_address.addressType=="Home"){
                  _address.imgString="assets/home1.png";
              }else if(_address.addressType=="Shop"){
                   _address.imgString="assets/bag.png";
              }else if(_address.addressType=="Others"){
                    _address.imgString="assets/painting.png";
              }
              if(i==0){
                 _address.color=Color(0xfffceddd);
                 _address.colorBoxWidth=UISupport.APP_PRIMARY_COLOR;
              }else{
                _address.colorBoxWidth=Color(0xffFFFFFF);
                 _address.color=Color(0xffffffff);
              }
              if(rowIndex==i){
                _address.color=Color(0xfffceddd);
                _address.IsAddressSelected=true;
              }else{
                 _address.color=Color(0xffffffff);
                 _address.IsAddressSelected=false;
              }
              return Padding(
                padding: const EdgeInsets.only(
                    top: 15.0, left: 8, right: 8),
                child: GestureDetector(
                  onTap: (){
                    setState(() {
                       rowIndex=i;
                    });
                    if(widget.FType=="Checkout")
                    // Get.to(CheckOutActivity(address: _address.address,addressType: _address.addressType,name: _address.name,
                    //   mobile: _address.mobile,state: _address.state,city: _address.state,
                    //   zipcode: _address.zipcode,OrderType: widget.OrderType,FType: widget.FType,));
                        Navigator.of(context).push(new
                        MaterialPageRoute(builder: (_) =>
                                CheckOutActivity(address: _address.address,
                                  addressType: _address.addressType,
                                  name: _address.name,
                                  mobile: _address.mobile,
                                  state: _address.state,
                                  city: _address.city,
                                  zipcode: _address.zipcode,
                                  OrderType: widget.OrderType,
                                  FType: widget.FType,))).then((value) {
                          getAddressAPI(userModel!.data.first.UserId).then((
                              value) {
                            setState(() {
                              if (value.status) {
                                _myAddress = value.data;
                              } else {
                                _myAddress = null;
                                Fluttertoast.showToast(msg: "No Address Found");
                              }
                            });
                          });
                        });
                  },
                  child: Container(
                    decoration: BoxDecoration(
                      border: Border.all(
                          width: 0.80, color: _address.colorBoxWidth),
                      borderRadius: BorderRadius.circular(5),
                      color: _address.color,
                    ),
                    height: 170,
                    child: Row(
                      children: [
                        Container(
                          width: MediaQuery.of(context).size.width*0.60,
                          child: Container(
                            height: double.infinity,
                            width: double.infinity,
                            child: SingleChildScrollView(
                              scrollDirection: Axis.vertical,
                              child: Column(
                                children: [
                                  Row(
                                    children: [
                                      Padding(
                                        padding: const EdgeInsets.only(left:8.0,top:10,
                                            bottom: 8),
                                        child: Image.asset(_address.imgString,
                                          height:20,
                                          width: 20,color: UISupport.APP_PRIMARY_COLOR,),
                                      ),
                                      Align(
                                          alignment: Alignment.topLeft,
                                          child: Padding(
                                            padding: const EdgeInsets.only(left:8.0,top:10,
                                                bottom: 8),
                                            child: Text(_address.addressType==null?"":
                                            _address.addressType.toUpperCase(),style:
                                            GoogleFonts.aBeeZee(fontSize: 14.6,color:
                                            Colors.black,fontWeight: FontWeight.bold),),
                                          )),
                                    ],
                                  ),

                                  Align(
                                      alignment: Alignment.topLeft,
                                      child: Padding(
                                        padding: const EdgeInsets.only(left:8.0),
                                        child: Text(_address.name,overflow:TextOverflow.ellipsis,maxLines: 1,style:
                                        GoogleFonts.aBeeZee(fontSize: 14,color: Colors.black),),
                                      )),

                                  Align(
                                      alignment: Alignment.topLeft,
                                      child: Padding(
                                        padding: const EdgeInsets.only(left:8.0,top: 5),
                                        child: Text(_address.address,overflow:
                                        TextOverflow.ellipsis,maxLines: 2,style:
                                        GoogleFonts.aBeeZee(fontSize: 15,
                                            color: Colors.black),),
                                      )),

                                  Align(
                                      alignment: Alignment.topLeft,
                                      child: Padding(
                                        padding: const EdgeInsets.only(left:8.0,top: 3),
                                        child: Text(_address.city+" "+_address.state+" "+
                                            _address.zipcode.toString(),overflow:
                                        TextOverflow.ellipsis,maxLines: 1,style:
                                        GoogleFonts.aBeeZee(fontSize: 15,
                                            color: Colors.black),),
                                      )),
                                  Padding(
                                    padding: const EdgeInsets.only(left:8.0,top: 3),
                                    child: Row(
                                      children: [
                                        Text("Contact : ",style:
                                        GoogleFonts.aBeeZee(fontSize: 14,color:
                                        Colors.grey[900]),),
                                        Align(
                                            alignment: Alignment.topLeft,
                                            child: Text(_address.mobile,style:
                                            GoogleFonts.aBeeZee(fontSize: 14,color: Colors.
                                            grey[900]),)),
                                      ],
                                    ),
                                  )

                                ],
                              ),
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
                                    setState(() {
                                      if(value=="edit"){
                                        Navigator.of(context).push(new MaterialPageRoute(builder:
                                            (_) => AddNewAddressActivity(FType: widget.FType,Type: "Editable",
                                            AID: _address.tblUserAddressId.toString(),
                                            Name: _address.name,
                                            City: _address.city,
                                            MobileNo: _address.mobile,Address:_address.address,ADType: _address.addressType,
                                            State: _address.state,ZipCode: _address.zipcode.toString(),
                                            OrderType: widget.OrderType,apptitle: "Update Address"))).then((value){
                                          getAddressAPI(userModel!.data.first.UserId).then((value){
                                            setState(() {
                                              if(value.status){
                                                _myAddress=value.data;
                                              }else{
                                                _myAddress=null;
                                                Fluttertoast.showToast(msg: "No Address Found");
                                              }
                                            });
                                          });
                                        });

                                      }else if(value=="delete"){
                                        setState(() {
                                          _deleteAddressDialog(_address.tblUserAddressId,userModel!.data.first.UserId);
                                          // deleteAddressAPI(_address.tblUserAddressId,userModel!.data.first.UserId);
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

                              Spacer(),
                              Visibility(
                                visible: _address.IsAddressSelected,
                                child: Padding(
                                  padding: const EdgeInsets.all(15.0),
                                  child: Align(
                                    alignment: Alignment.bottomRight,
                                    child: Container(
                                      height: 40,
                                      width: 40,
                                      decoration: BoxDecoration(
                                        border: Border.all(
                                            width: 1.0, color: const Color(0xffFFA52D)),
                                        borderRadius: BorderRadius.circular(25),
                                        color:  Color(0xfffceddd),
                                      ),
                                      child: Icon(Icons.check,size: 25,color:Color(0xffFFA52D)),
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

void deleteAddressAPI(int tblAdddressId,int userId) async{
    try{

   Map<String,String>mao={
     'tbl_user_address_id':tblAdddressId.toString(),
     'user_id':userId.toString()
   };

      final response=await http.post(Uri.parse(DeleteAddressAPI),body: mao);
      Map data=json.decode(response.body);
      if(data["status"]!=ERROR){
        Fluttertoast.showToast(msg: data["message"]);
        Navigator.of(context).pop();
       getAddressAPI(userModel!.data.first.UserId).then((value){
      setState(() {
        if(value.status){
        _myAddress=value.data;
        }else{
          _myAddress=null;
          Fluttertoast.showToast(msg: "No Address Found");
        }
      });
     });
      }else{
        Fluttertoast.showToast(msg: data["message"]);
      }

    }catch(e){
      Fluttertoast.showToast(msg: e.toString());
    }
}

  void _deleteAddressDialog(int ID,int userId) {
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
                deleteAddressAPI(ID,userId);
              },
            ),
            // usually buttons at the bottom of the dialog

          ],
        );
      },
    );
  }

}

