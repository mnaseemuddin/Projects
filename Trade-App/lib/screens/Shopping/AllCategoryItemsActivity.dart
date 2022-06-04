import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:provider/provider.dart';
import 'package:trading_apps/api/apis.dart';
import 'package:trading_apps/api/user_data.dart';
import 'package:trading_apps/custom_ui/progress_dialog.dart';
import 'package:trading_apps/models/AllItemsModel.dart';
import 'package:trading_apps/models/HotDealsModel.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/screens/Shopping/ItemDescriptionActivity.dart';
import 'package:trading_apps/utility/common_methods.dart';
import 'package:http/http.dart' as http;
import 'package:trading_apps/utility/connectivityprovider.dart';

import 'CartActivity.dart';

class AllCategoryItemsActivity extends StatefulWidget {
  final int ItemId;
  final String Type, AppBarName;

  const AllCategoryItemsActivity(
      {Key? key,
      required this.ItemId,
      required this.Type,
      required this.AppBarName})
      : super(key: key);

  @override
  _AllCategoryItemsActivityState createState() =>
      _AllCategoryItemsActivityState();
}

class _AllCategoryItemsActivityState extends State<AllCategoryItemsActivity> {
  List<AllItemsModel> allItemsList = [];

  String NoOfCartItems = "0";
  var IsLoading = true;
  late ProgressDialog pr;

  @override
  void initState() {
    Provider.of<ConnectivityProvider>(context,listen: false).startMonitoring();
    if (widget.Type == "Trending") {
      getAllTrendingItemAPI();
    } else if (widget.Type == "HotDeals") {
      getAllHotDealsItemAPI();
    } else {
      AllItemByIDAPI();
    }
    totalCartItemAPI();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Consumer<ConnectivityProvider>(builder: (ctx,data,child){
      if(data.isOnline!=null){
        return data.isOnline?Scaffold(
          backgroundColor: APP_PRIMARY_COLOR,
          appBar: AppBar(
            actions: [
              GestureDetector(
                onTap: () {
                  // navPush(context, CartActivity());
                  Navigator.of(context)
                      .push(new MaterialPageRoute(builder: (_) => CartActivity()))
                      .then((value) {
                    if (widget.Type == "Trending") {
                      getAllTrendingItemAPI();
                    } else if (widget.Type == "HotDeals") {
                      getAllHotDealsItemAPI();
                    } else {
                      AllItemByIDAPI();
                    }
                    totalCartItemAPI();
                  });
                },
                child: Stack(
                  children: [
                    Container(
                      child: Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: Icon(
                          FontAwesomeIcons.shoppingCart,
                          color: Color(0xfffffefe),
                        ),
                      ),
                    ),
                    Padding(
                      padding: const EdgeInsets.only(left: 18.0),
                      child: Container(
                        height: 20,
                        width: 20,
                        decoration: BoxDecoration(
                          color: Colors.white,
                          border: Border.all(width: 1,color: APP_PRIMARY_COLOR),
                          borderRadius: BorderRadius.circular(10),
                        ),
                        child: Center(
                            child: Text(
                              NoOfCartItems,
                              style: GoogleFonts.aBeeZee(
                                  color: Colors.black, fontSize: 10),
                            )),
                      ),
                    ),
                  ],
                ),
              )
            ],
            backgroundColor: Colors.black,
            title: Text(
              widget.AppBarName,
              style: GoogleFonts.aBeeZee(color: Colors.white, fontSize: 16),
            ),
          ),
          body: IsLoading == false
              ? ListView.builder(
              itemCount: allItemsList.length == null ? 0 : allItemsList.length,
              itemBuilder: (BuildContext ctx, int pos) {
                if (allItemsList[pos].finalQty > 0) {
                  allItemsList[pos].IsVisibilityAddItem = false;
                  allItemsList[pos].IsVisibilityAddMinus = true;
                } else {
                  allItemsList[pos].IsVisibilityAddItem = true;
                  allItemsList[pos].IsVisibilityAddMinus = false;
                }
                return Padding(
                  padding:
                  const EdgeInsets.only(top: 14.0, right: 15, left: 15),
                  child: GestureDetector(
                    onTap: () {
                      Navigator.of(context)
                          .push(new MaterialPageRoute(
                          builder: (_) => ItemDescriptionActivity(
                            ItemId: allItemsList[pos].ProductsId,
                            vendorId: 0,
                            tdcDeduct: "",
                          )))
                          .then((value) {
                        if (widget.Type == "Trending") {
                          getAllTrendingItemAPI();
                        } else if (widget.Type == "HotDeals") {
                          getAllHotDealsItemAPI();
                        } else {
                          AllItemByIDAPI();
                        }
                        totalCartItemAPI();
                      });
                    },
                    child: Container(
                      color: APP_PRIMARY_COLOR,
                      height: 170,
                      child: Stack(
                        children: [
                          Container(
                              decoration: BoxDecoration(
                                  color: Colors.white,
                                  borderRadius: BorderRadius.circular(6),
                                  border: Border.all(
                                      width: 1, color: Colors.white)),
                              margin: EdgeInsets.only(left: 14, top: 30),
                              width: double.infinity,
                              child: Padding(
                                padding:
                                const EdgeInsets.only(top: 18.0, left: 10),
                                child: Container(
                                  width: double.infinity,
                                  height: 120,
                                  child: Container(
                                    color: Colors.white,
                                    child: Row(
                                      children: [
                                        Container(
                                          color: Colors.white,
                                          width: MediaQuery.of(context)
                                              .size
                                              .width *
                                              .28,
                                        ),
                                        Container(
                                          width: MediaQuery.of(context)
                                              .size
                                              .width *
                                              .56,
                                          child: Column(
                                            children: [
                                              Align(
                                                alignment: Alignment.topLeft,
                                                child: Text(
                                                  "\$ " +
                                                      allItemsList[pos]
                                                          .ProductPrice +
                                                      ".00",
                                                  textAlign: TextAlign.left,
                                                  style: GoogleFonts.aBeeZee(
                                                      color: Colors.black,
                                                      fontSize: 16,
                                                      fontWeight:
                                                      FontWeight.w800),
                                                ),
                                              ),
                                              Align(
                                                alignment: Alignment.topLeft,
                                                child: RichText(
                                                    textAlign: TextAlign.left,
                                                    overflow:
                                                    TextOverflow.ellipsis,
                                                    text: TextSpan(
                                                      text: allItemsList[pos]
                                                          .ProductName,
                                                      style:
                                                      GoogleFonts.aBeeZee(
                                                          color:
                                                          Colors.black,
                                                          fontSize: 15,
                                                          fontWeight:
                                                          FontWeight
                                                              .w800),
                                                    )),
                                              ),
                                              Align(
                                                alignment: Alignment.topLeft,
                                                child: RichText(
                                                    textAlign: TextAlign.left,
                                                    overflow:
                                                    TextOverflow.ellipsis,
                                                    text: TextSpan(
                                                      text: allItemsList[pos]
                                                          .ProductDescription
                                                          .replaceAll(
                                                          "\n", " "),
                                                      style:
                                                      GoogleFonts.aBeeZee(
                                                          color: Colors
                                                              .grey[600],
                                                          fontSize: 15,
                                                          fontWeight:
                                                          FontWeight
                                                              .w100),
                                                    )),
                                              ),
                                              Row(
                                                children: [
                                                  Spacer(),
                                                  Visibility(
                                                    visible: allItemsList[pos]
                                                        .IsVisibilityAddItem,
                                                    child: Padding(
                                                      padding:
                                                      const EdgeInsets.only(
                                                          top: 10,
                                                          right: 10),
                                                      child: GestureDetector(
                                                        onTap: () {
                                                          setState(() {
                                                            allItemsList[pos]
                                                                .finalQty++;
                                                            allItemsList[pos]
                                                                .IsVisibilityAddItem =
                                                            false;
                                                            allItemsList[pos]
                                                                .IsVisibilityAddMinus =
                                                            true;
                                                            ItemsAddToCartAPI(
                                                                allItemsList[pos] .ProductsId,
                                                                allItemsList[pos].finalQty,
                                                                allItemsList[pos].ProductPrice,
                                                                allItemsList[pos].vendorId.toString(),
                                                                allItemsList[pos].Tdcdetect);
                                                          });
                                                        },
                                                        child: Container(
                                                          width: 90,
                                                          height: 35,
                                                          decoration:
                                                          BoxDecoration(
                                                            color: Colors.black,
                                                            borderRadius:
                                                            BorderRadius
                                                                .circular(
                                                                6),
                                                          ),
                                                          child: Row(
                                                            mainAxisAlignment:
                                                            MainAxisAlignment
                                                                .center,
                                                            children: [
                                                              Icon(
                                                                Icons.add,
                                                                size: 20,
                                                                color: Colors
                                                                    .white,
                                                              ),
                                                              Padding(
                                                                padding:
                                                                const EdgeInsets
                                                                    .only(
                                                                    left:
                                                                    8.0),
                                                                child: Text(
                                                                  "Add",
                                                                  style: GoogleFonts.aBeeZee(
                                                                      color: Colors
                                                                          .white,
                                                                      fontSize:
                                                                      14),
                                                                ),
                                                              )
                                                            ],
                                                          ),
                                                        ),
                                                      ),
                                                    ),
                                                  ),
                                                  Visibility(
                                                    visible: allItemsList[pos]
                                                        .IsVisibilityAddMinus,
                                                    child: Padding(
                                                      padding:
                                                      const EdgeInsets.only(
                                                          top: 10,
                                                          right: 10),
                                                      child: Row(
                                                        children: [
                                                          GestureDetector(
                                                            onTap: () {
                                                              setState(() {
                                                                if (allItemsList[
                                                                pos]
                                                                    .finalQty ==
                                                                    1) {
                                                                  allItemsList[
                                                                  pos]
                                                                      .finalQty = 0;
                                                                  updateItemQuantity(
                                                                      allItemsList[
                                                                      pos]
                                                                          .ProductsId,
                                                                      allItemsList[
                                                                      pos]
                                                                          .finalQty,
                                                                      allItemsList[
                                                                      pos]
                                                                          .ProductPrice);
                                                                } else {
                                                                  allItemsList[
                                                                  pos]
                                                                      .finalQty--;
                                                                  updateItemQuantity(
                                                                      allItemsList[
                                                                      pos]
                                                                          .ProductsId,
                                                                      allItemsList[
                                                                      pos]
                                                                          .finalQty,
                                                                      allItemsList[
                                                                      pos]
                                                                          .ProductPrice);
                                                                }
                                                              });
                                                            },
                                                            child: Container(
                                                              width: 30,
                                                              decoration:
                                                              BoxDecoration(
                                                                borderRadius:
                                                                BorderRadius
                                                                    .circular(
                                                                    6),
                                                                color: Colors
                                                                    .black,
                                                              ),
                                                              height: 30,
                                                              child: Padding(
                                                                padding:
                                                                const EdgeInsets
                                                                    .all(
                                                                    10.0),
                                                                child:
                                                                Image.asset(
                                                                  "assets/images/minus.png",
                                                                  height: 15,
                                                                  width: 15,
                                                                  color: Colors
                                                                      .white,
                                                                ),
                                                              ),
                                                            ),
                                                          ),
                                                          Container(
                                                            width: 35,
                                                            height: 30,
                                                            child: Center(
                                                              child: Text(
                                                                allItemsList[
                                                                pos]
                                                                    .finalQty
                                                                    .toString(),
                                                                style: GoogleFonts
                                                                    .aBeeZee(
                                                                    color: Colors
                                                                        .black,
                                                                    fontSize:
                                                                    15),
                                                              ),
                                                            ),
                                                          ),
                                                          GestureDetector(
                                                            onTap: () {
                                                              setState(() {
                                                                allItemsList[
                                                                pos]
                                                                    .finalQty++;
                                                                updateItemQuantity(
                                                                    allItemsList[
                                                                    pos]
                                                                        .ProductsId,
                                                                    allItemsList[
                                                                    pos]
                                                                        .finalQty,
                                                                    allItemsList[
                                                                    pos]
                                                                        .ProductPrice);
                                                              });
                                                            },
                                                            child: Container(
                                                              width: 30,
                                                              decoration:
                                                              BoxDecoration(
                                                                borderRadius:
                                                                BorderRadius
                                                                    .circular(
                                                                    6),
                                                                color: Colors
                                                                    .black,
                                                              ),
                                                              height: 30,
                                                              child: Padding(
                                                                  padding:
                                                                  const EdgeInsets
                                                                      .all(
                                                                      8.0),
                                                                  child: Icon(
                                                                    Icons.add,
                                                                    color: Colors
                                                                        .white,
                                                                    size: 15,
                                                                  )),
                                                            ),
                                                          )
                                                        ],
                                                      ),
                                                    ),
                                                  )
                                                ],
                                              ),
                                            ],
                                          ),
                                        ),
                                      ],
                                    ),
                                  ),
                                ),
                              )),
                          Container(
                            width: 110,
                            height: 110,
                            decoration: BoxDecoration(
                                color: Colors.white,
                                borderRadius: BorderRadius.circular(6),
                                border:
                                Border.all(width: 1, color: Colors.white)),
                            child: ClipRRect(
                              borderRadius: BorderRadius.circular(6),
                              child: Image.network(
                                allItemsList[pos].Image,
                                fit: BoxFit.fitHeight,
                              ),
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                );
              })
              : Center(
              child: Container(
                  padding: EdgeInsets.all(16),
                  decoration: BoxDecoration(
                      color: APP_SECONDARY_COLOR,
                      borderRadius: BorderRadius.all(Radius.circular(16))),
                  child: CircularProgressIndicator(strokeWidth: 5.2))),
        ):NoInternetConnection();
      }
      return Container(
        child: Center(
          child: CircularProgressIndicator(),
        ),
      );
    });
  }

  AllItemByIDAPI() async {
    print(widget.ItemId.toString());
    try {
      final response = await http.get(
          Uri.parse(AllItemUrls +
              widget.ItemId.toString() +
              "/" +
              userModel!.data.first.userId.toString()),
          headers: headers);
      Map data = json.decode(response.body);
      if (data["status"] != ERROR) {
        allItemsList.clear();
        var res = data["data"] as List;
        allItemsList = res.map((e) => AllItemsModel.fromJson(e)).toList();
      }
    } catch (e) {
      Fluttertoast.showToast(msg: e.toString());
    }
  }

  void ItemsAddToCartAPI(int productsId, int finalQty, String price,String vendor,String tdcDeduct) async {
    showDialog(
            context: context,
            builder: (context) => ProgressDialog(),
            barrierDismissible: false)
        .then(
      (value) {
        print(value);
      },
    );
    Map<String, String> map = {
      "user_id": userModel!.data.first.userId.toString(),
      "product_id": productsId.toString(),
      "quantity": finalQty.toString(),
      "unit_price": price,
       'vendor_id':vendor,
       'tdc_detect':tdcDeduct
    };
//manojbisht842@gmail.com
    try {
      final response = await http.post(Uri.parse(AddToCartUrls), body: map);
      Map data = json.decode(response.body);
      if (mounted)
        setState(() {
          Navigator.of(context).pop();
          if (data["status"] != ERROR) {
            Fluttertoast.showToast(msg: "Added to Cart Successfully .");
            totalCartItemAPI();
          } else {
            Fluttertoast.showToast(msg: data["message"]);
          }
        });
    } catch (e) {
      Fluttertoast.showToast(msg: e.toString());
    }
  }

  void updateItemQuantity(
      int tblCartId, int finalQty, String productPrice) async {
    showDialog(
            context: context,
            builder: (context) => ProgressDialog(),
            barrierDismissible: false)
        .then(
      (value) {
        print(value);
      },
    );
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

  totalCartItemAPI() async {
    try {
      final response = await http.get(Uri.parse(
          NoOfItemsInCartsUrls + userModel!.data.first.userId.toString()));
      Map data = json.decode(response.body);
      if (mounted)
        setState(() {
          IsLoading = false;
          if (data["status"] != ERROR) {
            NoOfCartItems = data["quantity"] == null
                ? "0"
                : NoOfCartItems = data["quantity"];
            totalCartItemAPI();
          } else {
            Fluttertoast.showToast(msg: data["message"]);
          }
        });
    } catch (e) {
      Fluttertoast.showToast(msg: e.toString());
    }
  }

  getAllHotDealsItemAPI() async {
    try {
      final response = await http.get(
          Uri.parse(
              SeeAllHotDealsItemsUrl + userModel!.data.first.userId.toString()),
          headers: headers);
      Map data = json.decode(response.body);
      if (data["status"] != ERROR) {
        allItemsList.clear();
        var res = data["hot_deal_data"] as List;
        allItemsList = res.map((e) => AllItemsModel.fromJson(e)).toList();
      } else {
        Fluttertoast.showToast(msg: data["message"]);
      }
    } catch (e) {
      Fluttertoast.showToast(msg: e.toString());
    }
  }

  getAllTrendingItemAPI() async {
    try {
      final response = await http.get(
          Uri.parse(
              SeeAllTrendingItemUrl + userModel!.data.first.userId.toString()),
          headers: headers);
      Map data = json.decode(response.body);
      if (data["status"] != ERROR) {
        allItemsList.clear();
        var res = data["trending_data"] as List;
        allItemsList = res.map((e) => AllItemsModel.fromJson(e)).toList();
      } else {
        Fluttertoast.showToast(msg: data["message"]);
      }
    } catch (e) {
      Fluttertoast.showToast(msg: e.toString());
    }
  }
}
