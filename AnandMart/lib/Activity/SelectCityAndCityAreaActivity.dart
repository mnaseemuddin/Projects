


import 'dart:convert';

import 'package:anandmart/Activity/DashBoard.dart';
import 'package:anandmart/ApplicationConfigration/ApiUrls.dart';
import 'package:anandmart/Model/CityAera.dart';
import 'package:anandmart/Support/SharePreferenceManager.dart';
import 'package:get/get.dart';
import 'package:http/http.dart'as http;
import 'package:anandmart/Model/City.dart';
import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:progress_dialog/progress_dialog.dart';

class SelctCityAndCityArea extends StatefulWidget {
  const SelctCityAndCityArea({Key? key}) : super(key: key);

  @override
  _SelctCityAndCityAreaState createState() => _SelctCityAndCityAreaState();
}

class _SelctCityAndCityAreaState extends State<SelctCityAndCityArea> {

  TextEditingController editFlagFilter=new TextEditingController();
  bool IsVisibilityCityWidget=false;

 late List<City> filterCityList=[];
  List<City> cityList = [];
  late Map data;
  List<CityAera> cityAreaList=[];
  late List gridList;

  String cityName="Select City",cityAreas="Select City Area";

  bool IsVisiblityErrorCity=false;

  late ProgressDialog pr;

  // Map<String, String> headers = {
  //   'Content-Type': 'application/json',
  //   'Accept': 'application/json'
  // };

  @override
  Widget build(BuildContext context) {

    pr=new ProgressDialog(context,isDismissible: true);
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

    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
         brightness: Brightness.dark,
        backgroundColor: Color(0xfff4811e),
        title: Center(child: Text('Select City And City Area',style: GoogleFonts.aBeeZee(fontSize: 15,color: Colors.white),)),
      ),
      body: ListView(
        children: [

          Padding(
            padding: const EdgeInsets.only(top:8.0,right: 15,left: 15),
            child: GestureDetector(
              onTap: (){
                pr.show();
              getCity();
              },
              child: Container(
                height: 45,
                decoration: BoxDecoration(
                    color: Colors.white,
                    borderRadius: BorderRadius.circular(10),
                    border: Border.all(width: 1,color: Colors.orange)
                ),
                child: Row(
                  children: [
                    Spacer(),
                    Center(child: Text(cityName,style: GoogleFonts.aBeeZee(fontSize: 15,
                        color: Colors.black87),)),
                    Spacer(),
                    Visibility(
                      visible: IsVisiblityErrorCity,
                      child: Padding(
                        padding: const EdgeInsets.only(right:8.0),
                        child: Icon(Icons.error,size: 18,color: Colors.red,),
                      ),
                    )
                  ],
                ),
              ),
            ),
          ),
          Padding(
            padding: const EdgeInsets.only(top:10.0,right: 15,left: 15),
            child: Container(
              height: 45,
              decoration: BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.circular(10),
                  border: Border.all(width: 1,color: Colors.orange)
              ),
              child: Center(child: Text(cityAreas,style: GoogleFonts.aBeeZee(fontSize: 15,
                  color: Colors.black87),)),
            ),
          ),
          Padding(
            padding: const EdgeInsets.only(top:28.0,left: 15,right: 15),
            child: GestureDetector(
              onTap: (){
                setState(() {
                  if(cityName!="Select City") {
                    SharePreferenceManager.instance.setCityAreas("CityArea",
                       cityAreas);
                    SharePreferenceManager.instance.setCity("City",cityName);
                    Get.offAll(DashBoard());
                  }else{
                    IsVisiblityErrorCity=true;
                  }
                });
            },
              child: Container(
                height: 45,
                width: double.infinity,
                decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(6.0),
                  color: const Color(0xfff4811e),
                  border: Border.all(width: 1.0, color: const Color(0xfff4811e)),
                ),
                child: Center(
                  child: Text(
                    'Submit',
                    style: GoogleFonts.aBeeZee(
                      fontWeight: FontWeight.w600,
                      fontSize: 16,
                      color: const Color(0xfff7f7f7),
                    ),
                    textAlign: TextAlign.left,
                  ),
                ),
              ),
            ),
          ),
        ],
      )
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
                                      height: 30,
                                      color: Colors.grey[100],
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


}
