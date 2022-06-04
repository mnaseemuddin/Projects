import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:http/http.dart' as http;
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:provider/provider.dart';
import 'package:trading_apps/api/apis.dart';
import 'package:trading_apps/api/user_data.dart';
import 'package:trading_apps/custom_ui/progress_dialog.dart';
import 'package:trading_apps/models/AllCategoryModel.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/utility/common_methods.dart';
import 'package:trading_apps/utility/connectivityprovider.dart';
import 'CartActivity.dart';

class ItemDescriptionActivity extends StatefulWidget {
  final int ItemId,vendorId;

  final String tdcDeduct;

  const ItemDescriptionActivity({Key? key, required this.ItemId,required this.vendorId,required this.tdcDeduct})
      : super(key: key);

  @override
  _ItemDescriptionActivityState createState() =>
      _ItemDescriptionActivityState();
}

class _ItemDescriptionActivityState extends State<ItemDescriptionActivity> {
  late String ProductName = "",
      ProductPrice = "",
      ProductDescription = "",
      ImageUrls = "";
      int tblProductId=0;

  var IsVisibilityMinusAdd = false, IsVisibilityAddItem = false;

  var finalQty = 0;

  String NoOfCartItems="0";

  bool IsLoading=true;

  @override
  void initState() {
    Provider.of<ConnectivityProvider>(context,listen: false).startMonitoring();
    ItemDescriptionAPI();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Consumer<ConnectivityProvider>(builder: (ctx,data,child){
      if(data.isOnline!=null){
        return data.isOnline?WillPopScope(
          onWillPop: (){
            Navigator.pop(context,true);
            return Future.value(false);
          },
          child: Scaffold(
            backgroundColor: APP_PRIMARY_COLOR,
            appBar: AppBar(
              actions: [
                GestureDetector(
                  onTap: (){
                    //navPush(context,CartActivity());
                    Navigator.of(context).push(new MaterialPageRoute(builder: (_)=>
                        CartActivity())).then((value){
                      ItemDescriptionAPI();
                    });
                  },
                  child: Stack(
                    children: [
                      Container(
                        child: Padding(
                          padding: const EdgeInsets.all(8.0),
                          child: Icon(FontAwesomeIcons.shoppingCart,color: Color(
                              0xfffffefe),),
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.only(left:18.0),
                        child: Container(
                          height: 20,
                          width: 20,
                          decoration: BoxDecoration(
                            color: Colors.white,
                            border: Border.all(width: 1,color: APP_PRIMARY_COLOR),
                            borderRadius: BorderRadius.circular(10),
                          ),
                          child: Center(child: Text(NoOfCartItems,style: GoogleFonts.aBeeZee(color: Colors.black,fontSize: 10),)),
                        ),
                      ),
                    ],
                  ),
                )
              ],
              backgroundColor: Colors.black,
              title: Text(
                "Details",
                style: GoogleFonts.aBeeZee(fontSize: 16, color: Colors.white),
              ),
            ),
            body: IsLoading==false?RefreshIndicator(
              onRefresh: ()=>totalCartItemAPI(),
              child: Column(
                children: [
               Expanded(
                   flex: 37,
                   child: Container(
                     width: double.infinity,
                     child: Image.network(
                       ImageUrls,
                       fit: BoxFit.fill,
                     ),
                   )),
                  Expanded(
                      flex: 60,
                      child: Container(
                        decoration: BoxDecoration(
                          color: Colors.white,
                          borderRadius: BorderRadius.only(topRight: Radius.circular(60)),
                        ),
                        child: Column(children: [
                        Expanded(
                          flex: 85,
                            child: Padding(
                              padding: const EdgeInsets.only(bottom: 8.0),
                              child: Container(child: SingleChildScrollView(
                                scrollDirection: Axis.vertical,
                                child: Column(children: [
                                  Align(
                                    alignment: Alignment.topLeft,
                                    child: Padding(
                                      padding: const EdgeInsets.only(left:8.0,top:8,bottom: 8,right: 15),
                                      child: Text(
                                        ProductName,
                                        style: GoogleFonts.aBeeZee(
                                            color: Colors.black,
                                            fontWeight: FontWeight.w600),
                                      ),
                                    ),
                                  ),
                                  Align(
                                    alignment: Alignment.topLeft,
                                    child: Padding(
                                      padding: const EdgeInsets.all(8.0),
                                      child: Text(
                                            '\$'+ProductPrice + ".00",
                                            style: GoogleFonts.aBeeZee(
                                                color: Colors.black,
                                                fontWeight: FontWeight.w700,
                                                fontSize: 16),
                                          ),

                                    ),
                                  ),
                                  Align(
                                    alignment: Alignment.topLeft,
                                    child: Padding(
                                      padding: const EdgeInsets.all(8.0),
                                      child: Text(
                                        ProductDescription,
                                        textAlign: TextAlign.left,
                                        style: GoogleFonts.aBeeZee(
                                            color: Colors.grey[700],
                                            fontWeight: FontWeight.w100),
                                      ),
                                    ),
                                  ),
                                ],),
                              ),),
                            )),
                        Expanded(
                            flex: 18,
                            child: Padding(
                              padding: const EdgeInsets.only(left:10.0,right:0,top: 6),
                              child: Align(
                                alignment: Alignment.centerLeft,
                                child: Container(
                                    width: 130,
                                    height: 40,
                                    decoration: BoxDecoration(
                                      color: Colors.black,
                                      borderRadius: BorderRadius.circular(6),
                                    ),
                                    child: Column(
                                      children: [
                                        Visibility(
                                          visible: IsVisibilityAddItem,
                                          child: GestureDetector(
                                            onTap: () {
                                              setState(() {
                                                finalQty++;
                                                IsVisibilityAddItem = false;
                                                IsVisibilityMinusAdd = true;
                                                ItemsAddToCartAPI(tblProductId,finalQty,ProductPrice);
                                              });
                                            },
                                            child: Container(
                                              height: 35,
                                              decoration: BoxDecoration(
                                                color: Colors.black,
                                                borderRadius: BorderRadius.circular(6),
                                              ),
                                              child: Padding(
                                                padding: const EdgeInsets.only(top:6.0),
                                                child: Row(
                                                  mainAxisAlignment: MainAxisAlignment.center,
                                                  children: [
                                                    Icon(
                                                      Icons.add,
                                                      size: 20,
                                                      color: Colors.white,
                                                    ),
                                                    Padding(
                                                      padding: const EdgeInsets.only(left: 8.0),
                                                      child: Text(
                                                        "Add",
                                                        style: GoogleFonts.aBeeZee(
                                                            color: Colors.white, fontSize: 14),
                                                      ),
                                                    )
                                                  ],
                                                ),
                                              ),
                                            ),
                                          ),
                                        ),
                                        Visibility(
                                          visible: IsVisibilityMinusAdd,
                                          child: Padding(
                                            padding: const EdgeInsets.only(top:5.0),
                                            child: Row(
                                              mainAxisAlignment: MainAxisAlignment.center,
                                              children: [
                                                GestureDetector(
                                                  onTap: () {
                                                    setState(() {
                                                      if (finalQty == 1) {
                                                        finalQty = 0;
                                                        IsVisibilityAddItem = true;
                                                        IsVisibilityMinusAdd=false;
                                                        updateItemQuantity(tblProductId,finalQty,ProductPrice);
                                                      } else {
                                                        finalQty--;
                                                        updateItemQuantity(tblProductId,finalQty,ProductPrice);
                                                      }
                                                    });
                                                  },
                                                  child: Container(
                                                    width: 30,
                                                    decoration: BoxDecoration(
                                                      borderRadius: BorderRadius.circular(6),
                                                      color: Colors.black,
                                                    ),
                                                    height: 30,
                                                    child: Padding(
                                                      padding: const EdgeInsets.all(10.0),
                                                      child: Image.asset(
                                                        "assets/images/minus.png",
                                                        height: 15,
                                                        width: 15,
                                                        color: Colors.white,
                                                      ),
                                                    ),
                                                  ),
                                                ),
                                                Container(
                                                  width: 35,
                                                  height: 30,
                                                  child: Center(
                                                    child: Text(
                                                      finalQty.toString(),
                                                      style: GoogleFonts.aBeeZee(
                                                          color: Colors.white, fontSize: 15),
                                                    ),
                                                  ),
                                                ),
                                                GestureDetector(
                                                  onTap: () {
                                                    setState(() {
                                                      finalQty++;
                                                      updateItemQuantity(tblProductId,finalQty,ProductPrice);
                                                    });
                                                  },
                                                  child: Container(
                                                    width: 30,
                                                    decoration: BoxDecoration(
                                                      borderRadius: BorderRadius.circular(6),
                                                      color: Colors.black,
                                                    ),
                                                    height: 30,
                                                    child: Padding(
                                                        padding: const EdgeInsets.all(8.0),
                                                        child: Icon(
                                                          Icons.add,
                                                          color: Colors.white,
                                                          size: 15,
                                                        )),
                                                  ),
                                                )
                                              ],
                                            ),
                                          ),
                                        )
                                      ],
                                    )
                                ),
                              ),
                            )),
                      ],),
                      )),
                ],
              ),
            ):Center(child: Container(
                padding: EdgeInsets.all(16),
                decoration: BoxDecoration(
                    color: APP_SECONDARY_COLOR,
                    borderRadius: BorderRadius.all(Radius.circular(16))
                ),
                child: CircularProgressIndicator(strokeWidth: 5.2,))),
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

  ItemDescriptionAPI() async {
    try {
      final response = await http.get(
          Uri.parse(ProductDescriptionUrls +userModel!.data.first.userId.toString()+"/"+ widget.ItemId.toString()),
          headers: headers);
      Map data = json.decode(response.body);
      if (data["status"] != ERROR) {
        setState(() {
          List descriptionList = data["data"];
          tblProductId=descriptionList[0]["tbl_products_id"];
          ProductName = descriptionList[0]["product_name"];
          ProductPrice = descriptionList[0]["product_price"];
          ProductDescription = descriptionList[0]["product_description"].toString().replaceAll("\n",
              "");
          print(ProductDescription);
            finalQty=descriptionList[0]["quantity"];
              if(finalQty>0) {
                IsVisibilityAddItem = false;
                IsVisibilityMinusAdd = true;
              } else {
                IsVisibilityAddItem = true;
                IsVisibilityMinusAdd = false;
              }

          ImageUrls = descriptionList[0]["image"];
          totalCartItemAPI();
        });
      } else {
        Fluttertoast.showToast(msg: data["message"]);
      }
    } catch (e) {
      Fluttertoast.showToast(msg: e.toString());
    }
  }

  void updateItemQuantity(
      int tblCartId, int finalQty, String productPrice) async {

    showDialog(context: context, builder: (context) => ProgressDialog(),
        barrierDismissible: false).then((value){
      print(value);
    },);
    Map<String, String> map = {
      "product_id": tblCartId.toString(),
      "quantity": finalQty.toString(),
      "unit_price": productPrice,
      "user_id": userModel!.data.first.userId.toString()
    };

    try {
      final response = await http.post(Uri.parse(UpdateCartUrls), body: map);
      Map data = json.decode(response.body);
      if (mounted)
        setState(() {
          Navigator.of(context).pop();
          if (data["status"] != ERROR) {
            Fluttertoast.showToast(msg: data["message"]);
            totalCartItemAPI();
          } else {
            Fluttertoast.showToast(msg: data["message"]);
          }
        });
    } catch (e) {
      Fluttertoast.showToast(msg: e.toString());
    }
  }
  void ItemsAddToCartAPI(int productsId, int finalQty, String price) async {

    showDialog(context: context, builder: (context) => ProgressDialog(),
        barrierDismissible: false).then((value){
      print(value);
    },);
    Map<String, String> map = {
      "user_id": userModel!.data.first.userId.toString(),
      "product_id": productsId.toString(),
      "quantity": finalQty.toString(),
      "unit_price": price,
      "vendor_id":widget.vendorId.toString(),
      'tdc_detect':widget.tdcDeduct
    };

    try {
      final response = await http.post(Uri.parse(AddToCartUrls), body: map);
      Map data = json.decode(response.body);
      print(data.toString());
      if(mounted)
        setState(() {
          Navigator.of(context).pop();
          if (data["status"] != ERROR) {
            Fluttertoast.showToast(msg: "Added to Cart Successfully ..");
           totalCartItemAPI();
          } else {
            Fluttertoast.showToast(msg: data["message"]);
          }
        });
    } catch (e) {
      Fluttertoast.showToast(msg: e.toString());
    }
  }
  totalCartItemAPI() async {
    try {
      final response = await http.get(Uri.parse(
          NoOfItemsInCartsUrls + userModel!.data.first.userId.toString()));
      Map data = json.decode(response.body);
      if(mounted)
        setState(() {
          IsLoading=false;
          if (data["status"] != ERROR) {
            NoOfCartItems =
            data["quantity"] == null ? "0" : NoOfCartItems = data["quantity"];
          } else {
            Fluttertoast.showToast(msg: data["message"]);
          }
        });
    } catch (e) {
      Fluttertoast.showToast(msg: e.toString());
    }
  }
}


