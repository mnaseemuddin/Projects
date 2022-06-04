import 'dart:convert';
import 'package:anandmart/Activity/MyAddressActivtity.dart';
import 'package:anandmart/ApplicationConfigration/ApiUrls.dart';
import 'package:anandmart/Model/City.dart';
import 'package:anandmart/Model/CityAera.dart';
import 'package:anandmart/Support/AlertDialogManager.dart';
import 'package:anandmart/Support/SharePreferenceManager.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:http/http.dart' as http;
import 'package:progress_dialog/progress_dialog.dart';

class AddNewAddressActivity extends StatefulWidget {
  final CType,AID,Title,FName,Locality,City1,MobileNo,Address,Address1;
  const AddNewAddressActivity({Key? key,required this.CType,required this.AID,
  required this.Title,required this.FName,required this.Locality,
  required this.City1,required this.MobileNo,required this.Address,required this.Address1}) : super(key: key);


// "uid":UserId,
//       "title":_selected,
//       "fname":editName.text,
//       "locality":cityAreas,
//       "city":cityName,
//       "mobileno":editMobileNo.text,
//       "address":editAddress.text

  @override
  _AddNewAddressActivityState createState() => _AddNewAddressActivityState();
}

class _AddNewAddressActivityState extends State<AddNewAddressActivity> {
  var addressTypeList = [
    "Please select address type",
    "Home",
    "Office",
    "Shop",
    "Others"
  ];
  String _selected = "Please select address type";
  List<City> cityList = [];
  late Map data;
  List<CityAera> cityAreaList=[];
  String cityName='';
  String cityAreas="";
  var _key=GlobalKey<FormState>();
  TextEditingController editName=new TextEditingController();
  TextEditingController editAddress=new TextEditingController();
  TextEditingController editMobileNo=new TextEditingController();
  String UserId="";

  late ProgressDialog pr;


  @override
  void initState() {
    SharePreferenceManager.instance.getUserID("UserID").then((value){
      SharePreferenceManager.instance.getCity("City").then((value){
        SharePreferenceManager.instance.getCityAreas("CityArea").then((value){
          setState(() {
            cityAreas=value;          });
        });
        setState(() {
         cityName=value;
        });
      });
      setState(() {
       UserId=value;
      });
    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    pr=new ProgressDialog(context,isDismissible: false);
    pr.style(
        message: 'Please wait...',
        borderRadius: 10.0,
        backgroundColor: Colors.white,
        progressWidget: Padding(
          padding: const EdgeInsets.all(8.0),
          child: CircularProgressIndicator(strokeWidth: 5.0,color: Colors.black,),
        ),
        elevation: 20.0,
        insetAnimCurve: Curves.easeInOut,
        progress: 0.0,
        maxProgress: 100.0,
        progressTextStyle: TextStyle(
            color: Colors.black, fontSize: 13.0, fontWeight: FontWeight.w400),
        messageTextStyle: TextStyle(
            color: Colors.black, fontSize: 19.0, fontWeight: FontWeight.w600)
    );
    if(widget.CType=="Editable"){
    editName.text=widget.FName;editAddress.text=widget.Address1;
    editMobileNo.text= widget.MobileNo;
  }
    return Scaffold(
      appBar: AppBar(
         brightness: Brightness.dark,
        backgroundColor: Color(0xfff4811e),
        title: Center(
            child: Text(
          'Add New Address',
          style: GoogleFonts.aBeeZee(fontSize: 16, color: Colors.white),
        )),
        leading: GestureDetector(
          onTap: () {
            Navigator.of(context).pop();
          },
          child: Container(
            child: Padding(
              padding: const EdgeInsets.all(18.0),
              child: Image.asset(
                "drawable/ic_remove.png",
                height: 10,
                width: 10,
                color: Colors.white,
              ),
            ),
          ),
        ),
        actions: [
          Align(
            alignment: Alignment.centerRight,
            child: GestureDetector(
              onTap: () {
                if(_key.currentState!.validate()){
                  if(_selected!="Please select address type"){
                    pr.show();
                    if(widget.CType=="NewAddress"){
                   saveAddress();
                    }else if(widget.CType=="Editable"){
                      updateAddress();
                    }
                  }else{
                    Fluttertoast.showToast(msg: "Please select address type ",
                        backgroundColor: Colors.white,textColor: Colors.black,gravity: ToastGravity.BOTTOM);
                  }
                }
              },
              child: Padding(
                padding: const EdgeInsets.only(right: 8.0),
                child: Text(
                  "Done",
                  style: GoogleFonts.aBeeZee(fontSize: 16, color: Colors.white),
                ),
              ),
            ),
          )
        ],
      ),
      body: Form(
        key: _key,
        child: ListView(
          children: [
            Padding(
              padding: const EdgeInsets.only(top: 8.0, left: 18, right: 18),
              child: TextFormField(
                style: GoogleFonts.aBeeZee(fontSize: 14),
                controller: editName,
                validator: (v){
                  if(v!.isEmpty){
                    return "Please Enter Name .";
                  }
                  return null;
                },
                textAlign: TextAlign.center,
                decoration: InputDecoration(
                      enabledBorder: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(9),
                        borderSide: BorderSide(
                          color: Color(0xfff4811e),
                          width: 0.70,
                        ),
                      ),
                  contentPadding: EdgeInsets.all(0.8),
                  errorStyle: GoogleFonts.aBeeZee(fontSize: 14.5,color:
                  Colors.redAccent,fontWeight: FontWeight.w600),
                  focusColor: Color(0xfff4811e),
                  border: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(5.0),
                      borderSide: BorderSide(color: Color(0xfff4811e))
                  ),
                  filled: true,
                  fillColor: Color(0xffffffff),
                  focusedBorder: OutlineInputBorder(
                    borderSide: BorderSide(color: Color(0xfff4811e)),
                    borderRadius: BorderRadius.circular(9.0),
                  ),
                      hintText: "Enter name",
                      hintStyle: GoogleFonts.aBeeZee(
                          fontSize: 14, color: Colors.grey[500]),
                ),
                // decoration: InputDecoration(
                //     contentPadding: EdgeInsets.all(0.8),
                //     enabledBorder: OutlineInputBorder(
                //       borderRadius: BorderRadius.circular(9),
                //       borderSide: BorderSide(
                //         color: Color(0xfff4811e),
                //         width: 0.70,
                //       ),
                //     ),
                //     focusedBorder: OutlineInputBorder(
                //       borderRadius: BorderRadius.circular(9),
                //       borderSide: BorderSide(
                //         color: Color(0xfff4811e),
                //         width: 2.0,
                //       ),
                //     ),
                //     hintText: "Enter name",
                //     hintStyle: GoogleFonts.aBeeZee(
                //         fontSize: 14, color: Colors.grey[500])),
              ),
            ),
            Padding(
              padding: const EdgeInsets.only(top: 8.0, left: 18, right: 18),
              child: Container(
                decoration: BoxDecoration(
                    color: Colors.white,
                    border: Border.all(width: 1.0, color: Color(0xfff4811e)),
                    borderRadius: BorderRadius.circular(9)),
                child: Padding(
                  padding: const EdgeInsets.only(left: 8.0),
                  child: DropdownButtonHideUnderline(
                    child: DropdownButton(
                      value: _selected,
                      isExpanded: true,
                      dropdownColor: Colors.white,
                      icon: Image.asset(
                        "drawable/dropr_down.png",
                        height: 30,
                        width: 30,
                      ),
                      items: addressTypeList.map((String e) {
                        return DropdownMenuItem(
                            value: e,
                            child: Text(
                              e,
                              style: GoogleFonts.aBeeZee(
                                  color: Colors.grey[800], fontSize: 16),
                            ));
                      }).toList(),
                      onChanged: (String? newValue) {
                        setState(() {
                          _selected = newValue!;
                        });
                      },
                    ),
                  ),
                ),
              ),
            ),
            Padding(
              padding: const EdgeInsets.only(top: 8.0, left: 18, right: 18),
              child: TextFormField(
                style: GoogleFonts.aBeeZee(fontSize: 13),
                controller: editAddress,
                keyboardType: TextInputType.multiline,
                maxLines: null,
                validator: (v){
                  if(v!.isEmpty){
                    return "Please Enter Address .";
                  }
                  return null;
                },
                textAlign: TextAlign.start,
                decoration: InputDecoration(
                    errorStyle: GoogleFonts.aBeeZee(fontSize: 14.5,color:
                    Colors.redAccent,fontWeight: FontWeight.w600),
                    contentPadding:
                        EdgeInsets.symmetric(vertical: 40, horizontal: 20),
                    enabledBorder: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(9),
                      borderSide: BorderSide(
                        color: Color(0xfff4811e),
                        width: 0.70,
                      ),
                    ),
                    border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(5.0),
                        borderSide: BorderSide(color: Color(0xfff4811e))
                    ),
                    focusedBorder: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(9),
                      borderSide: BorderSide(
                        color: Color(0xfff4811e),
                        width: 2.0,
                      ),
                    ),
                    hintText: "Please enter address",
                    hintStyle: GoogleFonts.aBeeZee(
                        fontSize: 12, color: Colors.grey[600])),
              ),
            ),
            Padding(
              padding: const EdgeInsets.only(top: 10.0, left: 18, right: 18),
              child: TextFormField(
                style: GoogleFonts.aBeeZee(fontSize: 14),
                keyboardType: TextInputType.number,
                inputFormatters: [
                  LengthLimitingTextInputFormatter(10),
                  FilteringTextInputFormatter.digitsOnly
                ],
                controller: editMobileNo,
                validator: (v){
                  if(v!.isEmpty){
                    return "Please Enter Mobile Number .";
                  }
                  return null;
                },
                textAlign: TextAlign.center,
                decoration: InputDecoration(
                    errorStyle: GoogleFonts.aBeeZee(fontSize: 14.5,color:
                    Colors.redAccent,fontWeight: FontWeight.w600),
                    contentPadding: EdgeInsets.all(0.8),
                    enabledBorder: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(9),
                      borderSide: BorderSide(
                        color: Color(0xfff4811e),
                        width: 0.70,
                      ),
                    ),
                    border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(5.0),
                        borderSide: BorderSide(color: Color(0xfff4811e))
                    ),
                    focusedBorder: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(9),
                      borderSide: BorderSide(
                        color: Color(0xfff4811e),
                        width: 2.0,
                      ),
                    ),
                    hintText: "Enter Mobile Number",
                    hintStyle: GoogleFonts.aBeeZee(
                        fontSize: 14, color: Colors.grey[600])),
              ),
            ),
            Padding(
              padding: const EdgeInsets.only(top: 10.0, left: 18, right: 18),
              child: GestureDetector(
                onTap: () {
                  setState(() {
                    pr.show();
                    getCity();
                  });

                },
                child: Container(
                  height: 45,
                  width: double.infinity,
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(5),
                    border: Border.all(width: 1.0, color: Color(0xfff4811e)),
                  ),
                  child: Center(
                      child: Text(
                    cityName,
                    style: GoogleFonts.aBeeZee(
                        fontSize: 15, color: Colors.grey[600]),
                  )),
                ),
              ),
            ),
            Padding(
              padding: const EdgeInsets.only(top: 10.0, left: 18, right: 18),
              child: Container(
                height: 45,
                width: double.infinity,
                decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(5),
                  border: Border.all(width: 1.0, color: Color(0xfff4811e)),
                ),
                child: Center(
                    child: Text(
                  cityAreas,
                  style:
                      GoogleFonts.aBeeZee(fontSize: 15, color: Colors.grey[600]),
                )),
              ),
            )
          ],
        ),
      ),
    );
  }

  cityAlertDialog() {
    showGeneralDialog(
        context: context,
        barrierDismissible: true,
        barrierLabel:
        MaterialLocalizations.of(context).modalBarrierDismissLabel,
        pageBuilder: (BuildContext buildContext, Animation animation,
            Animation secondaryAnimation) {
          return Padding(
            padding: const EdgeInsets.all(8.0),
            child: Center(
              child: Material(
                child: Container(
                   color: Colors.grey[100],
                  child: Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Column(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        Container(
                          width: double.infinity,
                          height: 45,
                          color: Colors.white,
                          child: Center(
                            child: Text(
                              "Select City",
                              style: GoogleFonts.aBeeZee(color: Colors.black),
                            ),
                          ),
                        ),
                        Padding(
                          padding: const EdgeInsets.only(top:8.0,left: 6,right: 6),
                          child: TextFormField(
                            style: GoogleFonts.aBeeZee(fontSize: 12,color: Colors.black),
                            textAlign: TextAlign.left,
                            decoration: InputDecoration(
                                prefixIcon: Icon(
                                  Icons.search,
                                  color: Colors.black,
                                ),
                                contentPadding: EdgeInsets.all(0.2),
                                enabledBorder: OutlineInputBorder(
                                  borderRadius: BorderRadius.circular(7),
                                  borderSide: BorderSide(
                                    color: Color(0xffeae8e6),
                                    width: 0.70,
                                  ),
                                ),
                                filled: true,
                                fillColor: Colors.white,
                                focusedBorder: OutlineInputBorder(
                                  borderRadius: BorderRadius.circular(7),
                                  borderSide: BorderSide(
                                    color: Color(0xffeae9e7),
                                    width: 0.70,
                                  ),
                                ),
                                hintText: "Search here...",
                                hintStyle: GoogleFonts.aBeeZee(
                                    fontSize: 13, color: Colors.grey[600])),
                          ),
                        ),
                        Column(
                          mainAxisSize: MainAxisSize.min,
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            ListView.builder(
                                shrinkWrap: true,
                                scrollDirection: Axis.vertical,
                                itemCount: cityList.length == null ? 0 : cityList.length,
                                itemBuilder: (BuildContext ctx, int i) {
                                  return GestureDetector(
                                    onTap: () {
                                      setState(() {
                                        pr.show();
                                        cityName=cityList[i].name;
                                        getCityArea(cityList[i].tblCityId);
                                      });
                                    },
                                    child: Container(
                                       color: Colors.grey[100],
                                      height: 30,
                                      child: Center(
                                        child: Text(
                                          cityList[i].name,
                                          textAlign: TextAlign.center,
                                          style: GoogleFonts.aBeeZee(
                                              fontSize: 15, color: Colors.black),
                                        ),
                                      ),
                                    ),
                                  );
                                }),
                          ],
                        ),
                      ],
                    ),
                  ),
                ),
              ),
            ),
          );
        });
  }

  getCity() async {
    final response = await http.get(Uri.parse(ApiUrls.SelectCity));
    data = json.decode(response.body);
    if (data['response'] == 'success') {
      setState(() {
        pr.hide();
        var res=data["data"] as List;
        cityList=res.map((e) =>City.formJson(e)).toList();
        cityAlertDialog();
      });
    }
  }


  cityAerasAlertDialog() {
    showGeneralDialog(
        context: context,
        barrierDismissible: true,
        barrierLabel:
        MaterialLocalizations.of(context).modalBarrierDismissLabel,
        pageBuilder: (BuildContext buildContext, Animation animation,
            Animation secondaryAnimation) {
          return Padding(
            padding: const EdgeInsets.all(8.0),
            child: Center(
              child: Material(
                child: Padding(
                  padding: const EdgeInsets.only(top:40.0),
                  child: SingleChildScrollView(
                    child: Column(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        GestureDetector(
                          onTap: (){
                            Navigator.of(context).pop();
                          },
                          child: Container(
                            width: double.infinity,
                            height: 45,
                            color: Colors.white,
                            child: Row(
                              children: [
                                Icon(Icons.arrow_back_ios_outlined,color: Colors.orange,),
                                Padding(
                                  padding: const EdgeInsets.only(left: 8.0),
                                  child: Text(
                                    "Select City",
                                    style: GoogleFonts.aBeeZee(color: Colors.black),
                                  ),
                                ),
                                Padding(
                                  padding: const EdgeInsets.only(left: 28.0),
                                  child: Center(
                                    child: Text(
                                      "Select Locality",
                                      style: GoogleFonts.aBeeZee(color: Colors.black),
                                    ),
                                  ),
                                ),
                              ],
                            ),
                          ),
                        ),
                        Padding(
                          padding: const EdgeInsets.only(top:8.0,left: 8,right: 8),
                          child: Container(
                            color: Colors.grey[200],
                            child: TextFormField(
                              textAlign: TextAlign.left,
                              decoration: InputDecoration(
                                  prefixIcon: Icon(
                                    Icons.search,
                                    color: Colors.black,
                                  ),
                                  contentPadding: EdgeInsets.all(0.8),
                                  enabledBorder: OutlineInputBorder(
                                    borderRadius: BorderRadius.circular(9),
                                    borderSide: BorderSide(
                                      color: Color(0xffeae8e6),
                                      width: 0.70,
                                    ),
                                  ),
                                  filled: true,
                                  fillColor: Colors.white,
                                  focusedBorder: OutlineInputBorder(
                                    borderRadius: BorderRadius.circular(9),
                                    borderSide: BorderSide(
                                      color: Color(0xffeae9e7),
                                      width: 2.0,
                                    ),
                                  ),
                                  hintText: "Search here...",
                                  hintStyle: GoogleFonts.aBeeZee(
                                      fontSize: 13, color: Colors.grey[600])),
                            ),
                          ),
                        ),
                        Container(
                          color: Colors.grey[200],
                          height: MediaQuery.of(context).size.height*0.90,
                          child: ListView.builder(
                              shrinkWrap: true,
                              scrollDirection: Axis.vertical,
                              itemCount: cityAreaList.length == null ? 0 : cityAreaList.length,
                              itemBuilder: (BuildContext ctx, int i) {
                                return Column(
                                  mainAxisSize: MainAxisSize.min,
                                  children: [
                                    GestureDetector(
                                      onTap:(){
                                        setState(() {
                                          cityAreas=cityAreaList[i].areaName;
                                          Navigator.pop(context);
                                        });
                                      },
                                      child: Container(
                                        color: Colors.grey[200],
                                        width: double.infinity,
                                        height:25,
                                        child: Center(
                                          child: Text(
                                            cityAreaList[i].areaName,
                                            textAlign: TextAlign.center,
                                            style: GoogleFonts.aBeeZee(
                                                fontSize: 14.5, color: Colors.black),
                                          ),
                                        ),
                                      ),
                                    ),
                                    Padding(
                                      padding: const EdgeInsets.all(8.0),
                                      child: Container(color: Colors.black,height: 1,),
                                    )
                                  ],
                                );
                              }),
                        ),
                      ],
                    ),
                  ),
                ),
              ),
            ),
          );
        });
  }

  getCityArea(String Id)async{
    Map<String,dynamic>hashmap={
      "tbl_city_id":Id
    };

    String reqUrls=Uri(queryParameters: hashmap).query;
    final response=await http.get(Uri.parse(ApiUrls.GetCityAreasAPI+"?"+reqUrls));
    if(response.statusCode==200)
      data=json.decode(response.body);
    setState(() {
      pr.hide();
      if(data['response']=='success') {
        var res = data["data"] as List;
        cityAreaList =
            res.map((json) => CityAera.formJson(json)).toList();
        if(cityAreaList.length>0)
          Navigator.of(context).pop();
        cityAerasAlertDialog();
        // IsLoading=false;
      }
    });
  }

  saveAddress()async{
    data={
      "uid":UserId,
      "title":_selected,
      "fname":editName.text,
      "locality":cityAreas,
      "city":cityName,
      "mobileno":editMobileNo.text,
      "address":editAddress.text
    };

   try{
     var ApiUrls1=Uri.parse(ApiUrls.SaveAddressAPI);
     var response=await http.post(ApiUrls1,body: data);
     if(response.statusCode==200){
       data=json.decode(response.body);
       pr.hide();
       if(data["status"]=="success"){
        Get.off(MyAddressActivity(CType: "",));
       }else{
         AlertDialogManager().IsErrorAlertDialog(context, "Status",data["status"].toString(),"");
       }
     }

   }catch(e){
     AlertDialogManager().IsErrorAlertDialog(context, "Status",e.toString(),"");
   }

  }


   updateAddress()async{
    data={
      "aid":widget.AID,
      "uid":UserId,
      "title":_selected,
      "fname":editName.text,
      "locality":cityAreas,
      "city":cityName,
      "mobileno":editMobileNo.text,
      "address":editAddress.text
    };

   try{
     var ApiUrls1=Uri.parse(ApiUrls.EditAddressAPI);
     var response=await http.post(ApiUrls1,body: data);
     if(response.statusCode==200){
       data=json.decode(response.body);
       pr.hide();
       if(data["status"]=="success"){
        Get.off(MyAddressActivity(CType: ""));
       }else{
         AlertDialogManager().IsErrorAlertDialog(context, "Status",data["status"].toString(),"");
       }
     }

   }catch(e){
     AlertDialogManager().IsErrorAlertDialog(context, "Status",e.toString(),"");
   }

  }

}
