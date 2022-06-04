




import 'dart:convert';
import 'package:anandmart/Activity/AllProductDescriptionActivity.dart';
import 'package:anandmart/Model/AllSubCategoryItems.dart';
import 'package:anandmart/Model/FlashSaleCategory.dart';
import 'package:anandmart/Support/AlertDialogManager.dart';
import 'package:anandmart/Support/SharePreferenceManager.dart';
import 'package:get/get.dart';
import 'package:http/http.dart'as http;
import 'package:anandmart/ApplicationConfigration/ApiUrls.dart';
import 'package:anandmart/Model/SubCategory.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

import 'AllSubCategoryItemsActivity.dart';
import 'CartActivity.dart';

class AllCategoriesActivity extends StatefulWidget {
  final String CategoryName,CategoryID;
  const AllCategoriesActivity({Key? key,required this.CategoryName,
    required this.CategoryID}) : super(key: key);

  @override
  _AllCategoriesActivityState createState() => _AllCategoriesActivityState();
}

class _AllCategoriesActivityState extends State<AllCategoriesActivity> {

  late List<SubCategory> allSubCategoryList=[];
  late Map data;
  late Map map;
  late List<FlashSaleCategory>flashSaleCategoryList=[];
  int totolCartItems=0;

  String deviceId="",UserId="";

  @override
  void initState() {
    SharePreferenceManager.instance.getUserID("UserID").then((value){
      SharePreferenceManager.instance.getDeviceID("DeviceID").then((value){
        setState(() {
          deviceId=value;
          getCartItems(UserId, deviceId);
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
    return Scaffold(
      appBar: AppBar(
         brightness: Brightness.dark,
        title: Text(widget.CategoryName,style: GoogleFonts.aBeeZee(
          fontSize: 15.5,color: Colors.white
        ),),
        actions: [
          GestureDetector(
            onTap: (){
              Get.to(CartActivity());
            },
            child: Stack(
              children: [
                Align(
                  alignment: Alignment.centerRight,
                  child: Padding(
                    padding: const EdgeInsets.only(right: 20.0),
                    child: Image.asset(
                      "drawable/cart.png",
                      height: 30,
                      width: 30,
                    ),
                  ),
                ),
                Align(
                  alignment: Alignment.topRight,
                  child: Padding(
                    padding: const EdgeInsets.only(top: 7.6, left: 14),
                    child: Container(
                      height: 20,
                      width: 20,
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(10),
                        color: const Color(0xffffffff),
                      ),
                      child: Center(
                          child: Text(
                            totolCartItems.toString(),
                            style: GoogleFonts.aBeeZee(
                                fontSize: 10, color: Colors.black),
                          )),
                    ),
                  ),
                )
              ],
            ),
          )
        ],
        backgroundColor: Color(0xfff4811e),
      ),
      body: RefreshIndicator(
        onRefresh: () =>getCartItems(UserId, deviceId),
        child: FutureBuilder(
          future: _getSubCategory(),
          builder: (BuildContext context, AsyncSnapshot snapshot) {
            if(snapshot.hasError){
              return Center(child: CircularProgressIndicator(strokeWidth: 6.0,color: Color(0xfff4811e)));
            }else if(snapshot.connectionState==ConnectionState.done){
              return _getSubCategoryUI(snapshot.data);
            }
          return Center(child: CircularProgressIndicator(strokeWidth: 6.0,color: Color(0xfff4811e)));
        },

        ),
      ),
    );
  }
  Future<List<SubCategory>> _getSubCategory() async {
    Map<String,dynamic>hashmap={
      "catid":widget.CategoryID
    };

    String reqUrls=Uri(queryParameters: hashmap).query;
    final response=await http.get(Uri.parse(ApiUrls.AllCatogoriesApi+"?"+reqUrls));
      data=json.decode(response.body);
      if(data['status']=='success') {
        var res = data["data"] as List;
        allSubCategoryList =
            res.map((json) => SubCategory.formJson(json)).toList();
      }
    return allSubCategoryList;
  }

  Widget _getSubCategoryUI(List<SubCategory>allSubCategoryList1) {
    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: Container(
        width: double.infinity,
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(4),
          border: Border.all(width: 1.0,
              color: Color(0xffece7e7)),),
        child: ListView(
          shrinkWrap: true,
          children: [
            GridView.builder(
                shrinkWrap: true,
                physics: NeverScrollableScrollPhysics(),
                itemCount: allSubCategoryList1.length==null?0:allSubCategoryList1.length,
                gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                    mainAxisExtent: 151.9,
                    crossAxisCount: 3,
                    childAspectRatio:1/1
                ), itemBuilder: (context,i){

              return GestureDetector(
                onTap: (){
                  print(allSubCategoryList1[i].catId);
                  Get.to(AllSubCategoryItemActivity(CategoryID: allSubCategoryList1[i].id,
                  CategoryName: allSubCategoryList1[i].name,));
                },
                child: Container(
                  child: SingleChildScrollView(
                  physics: NeverScrollableScrollPhysics(),
                    child: Column(
                      children: [
                        Container(
                          height: 145,
                          child: Stack(
                            children: [
                              ListView(
                                physics: NeverScrollableScrollPhysics(),
                                children: [
                                  Padding(
                                    padding: const EdgeInsets.all(8.0),
                                    child: Container(
                                        height: 86,
                                        child: new Image.network(allSubCategoryList1[i].image,
                                          height: double.infinity,
                                          errorBuilder: (context,error,stackTrace)=>
                                              Center(child: Image.asset("drawable/logo.png"),),
                                          fit: BoxFit.fill,)
                                    ),
                                  ),
                                  Padding(
                                    padding: const EdgeInsets.all(8.0),
                                    child: Center(
                                      child: Text(allSubCategoryList1[i].
                                      name,textAlign: TextAlign.center,style: GoogleFonts.aBeeZee(
                                          color: Colors.black,fontSize: 12.4
                                      ),),
                                    ),
                                  ),
                                ],
                              ),
                              Align(
                                alignment: Alignment.centerRight,
                                child: Container(
                                  height:double.infinity,
                                  color: Colors.grey,
                                  width: 1,
                                ),
                              )

                            ],
                          ),
                        ),
                        Container(
                          margin: EdgeInsets.only(top: 6),
                          height: 1.0,
                          color: Colors.grey,
                          width: double.infinity,
                        )
                      ],
                    ),
                  ),
                ),
              );
            }),
            Padding(
              padding: const EdgeInsets.only(left:8.0,right: 8.0,top:8),
              child: Text('Flash Sale>',style:
              GoogleFonts.aBeeZee(fontSize: 14,color: Colors.black),),
            ),
            Container(
                height:200,child: FlashSaleCategoryUI()),
          ],
        ),
      ),
    );
  }
  getCartItems(String userId, String deviceId) async {
    map = {"uid": userId,
      "device_type": "Mobile",
      "mid": deviceId
    };
    try {
      var Apiurls = Uri.parse(ApiUrls.GetUserCartItemsAPI);
      var response = await http.post(Apiurls, body: map);
      if(response.statusCode==200)
        map = json.decode(response.body);
      setState(() {
        if (map["status"] == "success") {
          totolCartItems = map["totalitems"];
        }
        getFlashSaleCategories();
      });
    } catch (e) {
      AlertDialogManager()
          .IsErrorAlertDialog(context, "Status", e.toString(), "");
    }
  }
  Widget FlashSaleCategoryUI() {
    ListView myZListView=
    new ListView.builder(
        itemCount: flashSaleCategoryList.length.compareTo(0),
        scrollDirection: Axis.horizontal,
        itemBuilder: (BuildContext ctx,int pos ){
          return Container(
            height: 160,
            child: Row(
              children: flashSaleCategoryList.map((e){
                var actuallyPerDiscountAmt;
                var pPrice=e.pPrice.split(",");
                var sPrice=e.sPrice.split(",");
                if(e.pPrice.contains(",")){
                  double currentAmt=double.parse(pPrice[0]);
                  double previousAmt=double.parse(sPrice[0]);
                  double discountAmt=currentAmt-previousAmt;
                  double finalAmt=discountAmt/currentAmt;
                  double actuallyDiscount=finalAmt*100;
                  actuallyPerDiscountAmt=actuallyDiscount.toString().split(".");
                }else{
                  double currentAmt=double.parse(e.pPrice);
                  double previousAmt=double.parse(e.sPrice);
                  double discountAmt=currentAmt-previousAmt;
                  double finalAmt=discountAmt/currentAmt;
                  double actuallyDiscount=finalAmt*100;
                  actuallyPerDiscountAmt=actuallyDiscount.toString().split(".");
                }
                return  Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: Container(
                    width: 124,
                    height: 160,
                    decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(5.0),
                      color: Colors.white,
                      border: Border.all(width: 0.1, color: Colors.grey),
                      boxShadow: [
                        BoxShadow(
                          color: const Color(0x29E3DDDD),
                          blurRadius: 1,
                        ),
                      ],
                    ),
                    child: Column(
                      children: [
                        GestureDetector(
                          onTap: (){
                            Get.to(AllProductDescriptionActivity(ProductName: e.productName,
                              ProductID: e.pid,));
                          },
                          child: Container(
                              height: 110,
                              child: Stack(
                                children: [
                                  Container(
                                      height: 110,
                                      child: FadeInImage.assetNetwork(
                                          placeholder: "drawable/logo.png",
                                          image: e.pimage,
                                          imageErrorBuilder: (context,error,stackTrace)=>
                                              Center(child: Image.asset("drawable/logo.png"),)
                                      )
                                  ),Align(
                                    alignment: Alignment.topRight,
                                    child:Padding(
                                      padding: const EdgeInsets.all(8.0),
                                      child: Container(
                                        decoration: BoxDecoration(
                                          color: Color(0xfff4811e),
                                          borderRadius: BorderRadius.circular(
                                              3.5),
                                        ),
                                        height: 17,
                                        width: 48,
                                        child: Center(
                                          child: Text(actuallyPerDiscountAmt[0].toString()+"%"+" OFF",style: GoogleFonts.aBeeZee(
                                            fontSize: 8.5,color:Colors.white,
                                          ),),
                                        ),
                                      ),
                                    ),
                                  )
                                ],
                              )
                          ),
                        ),
                        Center(
                            child: Padding(
                              padding: const EdgeInsets.only(left:8.0,top:8),
                              child: RichText(
                                  textAlign: TextAlign.center,
                                  overflow: TextOverflow.ellipsis,
                                  text: TextSpan(text:e.productName,style: GoogleFonts.aBeeZee(
                                      color:Colors.black,fontSize: 10
                                  ))
                              ),
                            )),

                        Row(
                          mainAxisAlignment: MainAxisAlignment.start,
                          children: [
                            Padding(
                                padding: const EdgeInsets.only(top:10.0,left: 8),
                                child: Image.asset("drawable/rupee.png",height: 10,width: 10,)),
                            Padding(
                              padding: const EdgeInsets.only(top:10.0,left:0),
                              child: RichText(
                                  textAlign: TextAlign.center,
                                  overflow: TextOverflow.ellipsis,
                                  text: TextSpan(text:e.sPrice.contains(",")?sPrice[0]:
                                  e.sPrice,style: GoogleFonts.aBeeZee(
                                      color:Colors.black,fontSize: 10
                                  ))
                              ),
                            ),

                            //

                            Stack(
                              children: [
                                Align(
                                  alignment: Alignment.centerRight,
                                  child: Padding(
                                    padding: const EdgeInsets.only(top:15.0,left: 10),
                                    child: Container(height: 1,width: 50,color: Colors.grey,),
                                  ),
                                ),
                                Row(
                                  mainAxisAlignment: MainAxisAlignment.end,
                                  children: [
                                    Padding(
                                        padding: const EdgeInsets.only(top:10.0,left: 14),
                                        child: Image.asset("drawable/rupee.png",height: 10,width: 10,)),
                                    Padding(
                                      padding: const EdgeInsets.only(top:10.0,left:0),
                                      child: RichText(
                                          textAlign: TextAlign.center,
                                          overflow: TextOverflow.ellipsis,
                                          text: TextSpan(text:e.pPrice.contains(",")?sPrice[0]:
                                          e.pPrice,style: GoogleFonts.aBeeZee(
                                              color:Colors.black,fontSize: 10
                                          ))
                                      ),
                                    ),
                                  ],
                                )
                              ],
                            ),
                          ],
                        )
                      ],
                    ),
                  ),
                );
              }).toList(),
            ),
          );
        });
    return myZListView;
  }
  getFlashSaleCategories() async{
    final response=await http.get(Uri.parse(ApiUrls.FlashSaleApi));
    data=json.decode(response.body);
    setState(() {
      // if(data['response']=='success') {
      var res = data["data"] as List;
      flashSaleCategoryList =res.map((json) =>FlashSaleCategory.fromJson(json)).toList();
    });
  }
}



