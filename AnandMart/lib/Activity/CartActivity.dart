import 'dart:convert';

import 'package:anandmart/Activity/DashBoard.dart';
import 'package:anandmart/Activity/MyAddressActivtity.dart';
import 'package:anandmart/ApplicationConfigration/ApiUrls.dart';
import 'package:anandmart/Model/Cart.dart';
import 'package:anandmart/Support/AlertDialogManager.dart';
import 'package:anandmart/Support/SharePreferenceManager.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:http/http.dart' as http;
import 'package:progress_dialog/progress_dialog.dart';

import 'AllProductDescriptionActivity.dart';

class CartActivity extends StatefulWidget {
  const CartActivity({Key? key}) : super(key: key);

  @override
  _CartActivityState createState() => _CartActivityState();
}

class _CartActivityState extends State<CartActivity> {
  int totolCartItems = 0;
  late List<Cart> cartItemsList = [];
  late String deviceID = "", UserId = "";
  late Map map;
  bool IsLoading = true;
  bool IsVisiblityCartUI = false, IsVisiblityNoRecords = false;
  String subTotalAmt = "", discountAmt = "";
  double BugTotalAmt = 0.0;

  late ProgressDialog pr;

  @override
  void initState() {
    SharePreferenceManager.instance.getDeviceID("DeviceID").then((value) {
      setState(() {
        deviceID = value;
      });
    });
    SharePreferenceManager.instance.getUserID("UserID").then((value) {
      setState(() {
        UserId = value;
        getCartItems(UserId, deviceID);
      });
    });

    super.initState();
  }

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
        appBar: AppBar(
           brightness: Brightness.dark,
          backgroundColor: Color(0xfff4811e),
          actions: [
            Stack(
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
            )
          ],
        ),
        body: IsLoading == false
            ? CartUI()
            : Center(
                child: CircularProgressIndicator(
                strokeWidth: 6.0,
                color: Color(0xfff4811e),
              )));
  }

  Widget CartUI() {
    return IsVisiblityNoRecords == false
        ? Visibility(
            visible: IsVisiblityCartUI,
            child: ListView(
              children: [
                Container(
                  height: MediaQuery.of(context).size.height * 0.78,
                  child: SingleChildScrollView(
                    child: Column(
                      children: [
                        ListView.builder(
                            physics: NeverScrollableScrollPhysics(),
                            scrollDirection: Axis.vertical,
                            shrinkWrap: true,
                            itemCount: cartItemsList.length == null
                                ? 0
                                : cartItemsList.length,
                            itemBuilder: (BuildContext ctx, int i) {
                              return Padding(
                                padding: const EdgeInsets.only(
                                    top: 15.0, left: 10, right: 10),
                                child: Container(
                                  decoration: BoxDecoration(
                                    border: Border.all(
                                        width: 1.0,
                                        color: const Color(0xffece7e7)),
                                    borderRadius: BorderRadius.circular(10),
                                    color: const Color(0xffffffff),
                                  ),
                                  width:
                                      MediaQuery.of(context).size.width * 0.89,
                                  height: 140,
                                  child: Row(
                                    children: [
                                      Container(
                                        width:
                                            MediaQuery.of(context).size.width *
                                                0.25,
                                        height: double.infinity,
                                        child:  Column(
                                          children: [
                                            Align(
                                              alignment: Alignment.topLeft,
                                              child: GestureDetector(
                                                onTap: (){
                                                  setState(() {
                                                    pr.show();
                                                       _deleteCartItems(UserId,deviceID,cartItemsList[i].wieghtid);
                                                  });
                                               
                                                },
                                                child: Icon(Icons.cancel,size: 25,))),
                                                Padding(
                                                  padding: const EdgeInsets.only(right:18.0),
                                                  child: Container(
                                                     height: 90,
                                                    child: FadeInImage.assetNetwork(
                                        placeholder: "drawable/logo.png",
                                        image: cartItemsList[i].pimage,
                                        imageErrorBuilder: (context,error,stackTrace)=>
                                            Center(child: Image.asset("drawable/logo.png"),)
                                    ),
                                                  ),
                                                ),
                                          ],
                                        ),
                                      ),
                                      Container(
                                        width:
                                            MediaQuery.of(context).size.width *
                                                0.64,
                                        child: Column(
                                          children: [
                                            Padding(
                                              padding: const EdgeInsets.only(
                                                  left: 0.0, top: 15),
                                              child: Row(
                                                children: [
                                                  Padding(
                                                      padding:
                                                          const EdgeInsets.only(
                                                              top: 10.0,
                                                              left: 12),
                                                      child: Image.asset(
                                                        "drawable/rupee.png",
                                                        height: 10,
                                                        width: 10,
                                                      )),
                                                  Padding(
                                                    padding:
                                                        const EdgeInsets.only(
                                                            top: 10.0, left: 0),
                                                    child: RichText(
                                                        textAlign:
                                                            TextAlign.center,
                                                        overflow: TextOverflow
                                                            .ellipsis,
                                                        text: TextSpan(
                                                            text:
                                                                cartItemsList[i]
                                                                    .pSubtotal,
                                                            style: GoogleFonts
                                                                .aBeeZee(
                                                                    color: Colors
                                                                        .black,
                                                                    fontSize:
                                                                        14))),
                                                  ),
                                                  Padding(
                                                    padding:
                                                        const EdgeInsets.only(
                                                            left: 8.0),
                                                    child: Stack(
                                                      children: [
                                                        Align(
                                                          alignment: Alignment
                                                              .centerRight,
                                                          child: Padding(
                                                            padding:
                                                                const EdgeInsets
                                                                        .only(
                                                                    top: 18.0,
                                                                    left: 10),
                                                            child: Container(
                                                              height: 1,
                                                              width: 64,
                                                              color:
                                                                  Colors.grey,
                                                            ),
                                                          ),
                                                        ),
                                                        Row(
                                                          mainAxisAlignment:
                                                              MainAxisAlignment
                                                                  .end,
                                                          children: [
                                                            Padding(
                                                                padding:
                                                                    const EdgeInsets
                                                                            .only(
                                                                        top:
                                                                            10.0,
                                                                        left:
                                                                            14),
                                                                child:
                                                                    Image.asset(
                                                                  "drawable/rupee.png",
                                                                  height: 10,
                                                                  width: 10,
                                                                )),
                                                            Padding(
                                                              padding:
                                                                  const EdgeInsets
                                                                          .only(
                                                                      top: 10.0,
                                                                      left: 0),
                                                              child: RichText(
                                                                  textAlign:
                                                                      TextAlign
                                                                          .center,
                                                                  overflow:
                                                                      TextOverflow
                                                                          .ellipsis,
                                                                  text: TextSpan(
                                                                      text: cartItemsList[
                                                                              i]
                                                                          .aprice,
                                                                      style: GoogleFonts.aBeeZee(
                                                                          color: Colors
                                                                              .black,
                                                                          fontSize:
                                                                              14))),
                                                            ),
                                                          ],
                                                        )
                                                      ],
                                                    ),
                                                  ),
                                                ],
                                              ),
                                            ),
                                            Align(
                                              alignment: Alignment.topLeft,
                                              child: Padding(
                                                padding: const EdgeInsets.only(
                                                    left: 12.0, top: 10),
                                                child: Text(
                                                    cartItemsList[i].pname,
                                                    style: GoogleFonts.aBeeZee(
                                                      color: Colors.black,
                                                      fontSize: 12.5,
                                                    )),
                                              ),
                                            ),
                                            Padding(
                                              padding:
                                                  const EdgeInsets.all(8.0),
                                              child: Row(
                                                crossAxisAlignment:
                                                    CrossAxisAlignment.center,
                                                children: [
                                                  Align(
                                                    alignment:
                                                        Alignment.bottomLeft,
                                                    child: Padding(
                                                      padding:
                                                          const EdgeInsets.only(
                                                              left: 0.0,
                                                              top: 8),
                                                      child: Text(
                                                          cartItemsList[i]
                                                              .weight,
                                                          style: GoogleFonts
                                                              .aBeeZee(
                                                            color: Colors.black,
                                                            fontSize: 12.5,
                                                          )),
                                                    ),
                                                  ),
                                                  Spacer(),
                                                  Visibility(
                                                    // visible: searchProductList[i].IsItemAddMinus,
                                                    child: Align(
                                                      alignment:
                                                          Alignment.bottomRight,
                                                      child: Padding(
                                                        padding:
                                                            const EdgeInsets
                                                                    .only(
                                                                left: 8.0,
                                                                top: 8),
                                                        child: Container(
                                                          height: 30,
                                                          width: 105,
                                                          decoration:
                                                              BoxDecoration(
                                                            color: Colors.white,
                                                            borderRadius:
                                                                BorderRadius
                                                                    .circular(
                                                                        4),
                                                          ),
                                                          child: Row(
                                                            children: [
                                                              Align(
                                                                alignment:
                                                                    Alignment
                                                                        .topRight,
                                                                child:
                                                                    GestureDetector(
                                                                      onTap: (){
                                                                        setState(() {
                                                                          if(cartItemsList[i].finalQty==1){
                                                                            cartItemsList[i].finalQty=0;
                                                                            // allSubCategoryItems1[i].IsVisibilityAddItem=true;
                                                                            // allSubCategoryItems1[i].IsVisibilityAddMinus=false;
                                                                            pr.show();
                                                                            updateItemQuantity(cartItemsList[i].wieghtid,
                                                                                cartItemsList[i].finalQty);
                                                                          }else{
                                                                            cartItemsList[i].finalQty--;
                                                                            // allSubCategoryItems1[i].IsVisibilityAddItem=false;
                                                                            // allSubCategoryItems1[i].IsVisibilityAddMinus=true;
                                                                           pr.show();
                                                                            updateItemQuantity(cartItemsList[i].wieghtid,
                                                                                cartItemsList[i].finalQty);
                                                                          }
                                                                        });
                                                                      },
                                                                      child: Container(
                                                                  decoration:
                                                                        BoxDecoration(
                                                                      color: Color(
                                                                          0xfff4811e),
                                                                      borderRadius:
                                                                          BorderRadius
                                                                              .circular(4),
                                                                  ),
                                                                  height: double
                                                                        .infinity,
                                                                  width: 24,
                                                                  child: Center(
                                                                        child: Image
                                                                            .asset(
                                                                      "drawable/minus.png",
                                                                      width: 10,
                                                                      color: Colors
                                                                          .white,
                                                                  )),
                                                                ),
                                                                    ),
                                                              ),
                                                              Align(
                                                                alignment:
                                                                    Alignment
                                                                        .center,
                                                                child:
                                                                    Container(
                                                                  width: 54,
                                                                  child: Text(
                                                                    cartItemsList[
                                                                            i]
                                                                        .finalQty
                                                                        .toString(),
                                                                    textAlign:
                                                                        TextAlign
                                                                            .center,
                                                                    style: GoogleFonts
                                                                        .aBeeZee(
                                                                      fontSize:
                                                                          11,
                                                                      color: Colors
                                                                          .black,
                                                                    ),
                                                                  ),
                                                                ),
                                                              ),
                                                              Align(
                                                                alignment: Alignment
                                                                    .centerRight,
                                                                child:
                                                                    GestureDetector(
                                                                      onTap: (){
                                                                        setState(() {
                                                                          cartItemsList[i].finalQty++;
                                                                          // allSubCategoryItems1[i].IsVisibilityAddItem=false;
                                                                          // allSubCategoryItems1[i].IsVisibilityAddMinus=true;
                                                                           pr.show();
                                                                          updateItemQuantity(cartItemsList[i].wieghtid,
                                                                              cartItemsList[i].finalQty);
                                                                        });
                                                                      },
                                                                      child: Container(
                                                                  decoration:
                                                                        BoxDecoration(
                                                                      color: Color(
                                                                          0xfff4811e),
                                                                      borderRadius:
                                                                          BorderRadius
                                                                              .circular(4),
                                                                  ),
                                                                  height: double
                                                                        .infinity,
                                                                  width: 24,
                                                                  child: Center(
                                                                        child:
                                                                            Icon(
                                                                      Icons.add,
                                                                      size: 10,
                                                                      color: Colors
                                                                          .white,
                                                                  )),
                                                                ),
                                                                    ),
                                                              ),
                                                            ],
                                                          ),
                                                        ),
                                                      ),
                                                    ),
                                                  ),
                                                ],
                                              ),
                                            ),
                                          ],
                                        ),
                                      ),
                                    ],
                                  ),
                                ),
                              );
                            }),
                        //Price Detail UI
                        Padding(
                          padding: const EdgeInsets.only(top: 15.0, bottom: 02),
                          child: Container(
                            height: 162,
                            color: Colors.black,
                            child: Column(
                              children: [
                                Padding(
                                  padding: const EdgeInsets.all(8.0),
                                  child: Align(
                                      alignment: Alignment.topLeft,
                                      child: Text(
                                        'Price  Detail',
                                        style: GoogleFonts.aBeeZee(
                                            fontSize: 12, color: Colors.white),
                                      )),
                                ),
                                Padding(
                                  padding: const EdgeInsets.only(
                                      left: 8.0, right: 8.0),
                                  child: Container(
                                    height: 1,
                                    color: Colors.white,
                                    width: double.infinity,
                                  ),
                                ),
                                Padding(
                                  padding: const EdgeInsets.all(8.0),
                                  child: Row(
                                    children: [
                                      Text(
                                        "Bag's Total",
                                        style: GoogleFonts.aBeeZee(
                                            fontSize: 12, color: Colors.white),
                                      ),
                                      Spacer(),
                                      Padding(
                                          padding: const EdgeInsets.only(
                                              top: 0, left: 8),
                                          child: Image.asset(
                                            "drawable/rupee.png",
                                            height: 10,
                                            width: 10,
                                            color: Colors.white,
                                          )),
                                      Text(
                                        BugTotalAmt.toString(),
                                        style: GoogleFonts.aBeeZee(
                                            fontSize: 12, color: Colors.white),
                                      ),
                                    ],
                                  ),
                                ),
                                Padding(
                                  padding: const EdgeInsets.all(8.0),
                                  child: Row(
                                    children: [
                                      Text(
                                        "Discount",
                                        style: GoogleFonts.aBeeZee(
                                            fontSize: 12, color: Colors.white),
                                      ),
                                      Spacer(),
                                      Padding(
                                          padding: const EdgeInsets.only(
                                              top: 0, left: 8),
                                          child: Image.asset(
                                            "drawable/rupee.png",
                                            height: 10,
                                            width: 10,
                                            color: Colors.white,
                                          )),
                                      Text(
                                        discountAmt,
                                        style: GoogleFonts.aBeeZee(
                                            fontSize: 12, color: Colors.white),
                                      ),
                                    ],
                                  ),
                                ),
                                Padding(
                                  padding: const EdgeInsets.all(8.0),
                                  child: Row(
                                    children: [
                                      Text(
                                        "Sub-Total",
                                        style: GoogleFonts.aBeeZee(
                                            fontSize: 12, color: Colors.white),
                                      ),
                                      Spacer(),
                                      Padding(
                                          padding: const EdgeInsets.only(
                                              top: 0, left: 8),
                                          child: Image.asset(
                                            "drawable/rupee.png",
                                            height: 10,
                                            width: 10,
                                            color: Colors.white,
                                          )),
                                      Text(
                                        subTotalAmt,
                                        style: GoogleFonts.aBeeZee(
                                            fontSize: 12, color: Colors.white),
                                      ),
                                    ],
                                  ),
                                ),
                                Padding(
                                  padding: const EdgeInsets.only(
                                      left: 8.0, right: 8.0),
                                  child: Container(
                                    height: 1,
                                    color: Colors.white,
                                    width: double.infinity,
                                  ),
                                ),
                                Padding(
                                  padding: const EdgeInsets.all(8.0),
                                  child: Row(
                                    children: [
                                      Text(
                                        "Total Payable",
                                        style: GoogleFonts.aBeeZee(
                                            fontSize: 12, color: Colors.white),
                                      ),
                                      Spacer(),
                                      Padding(
                                          padding: const EdgeInsets.only(
                                              top: 0, left: 8),
                                          child: Image.asset(
                                            "drawable/rupee.png",
                                            height: 10,
                                            width: 10,
                                            color: Colors.white,
                                          )),
                                      Text(
                                        subTotalAmt,
                                        style: GoogleFonts.aBeeZee(
                                            fontSize: 12, color: Colors.white),
                                      ),
                                    ],
                                  ),
                                ),
                              ],
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.only(top:8.0),
                  child: SizedBox(
                    height: MediaQuery.of(context).size.height * 0.05,
                    width: double.infinity,
                    child: ElevatedButton(
                      onPressed: () {
                        Get.to(MyAddressActivity(CType: "",));
                      },
                      style: ElevatedButton.styleFrom(onPrimary: Color(0xfff4811e),elevation: 5),
                      child: Align(
                          alignment: Alignment.center,
                          child: Text('Continue',
                              style: GoogleFonts.aBeeZee(
                                  color: Colors.white, fontSize: 16))),
                    ),
                  ),
                )
              ],
            ),
          )
        : Container(
            width: double.infinity,
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Image.asset(
                  "drawable/shoppingbag.png",
                  height: 90,
                  width: 100,
                  color: Colors.grey[600],
                ),
                Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: Text(
                    "Nothing in the cart",
                    style:
                        GoogleFonts.aBeeZee(fontSize: 24, color: Colors.black),
                  ),
                ),
                Padding(
                  padding:
                      const EdgeInsets.only(top: 10.0, left: 38, right: 38),
                  child: GestureDetector(
                    onTap: () {
                      Get.off(DashBoard());
                    },
                    child: Container(
                      height: 45,
                      width: double.infinity,
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(5),
                        border:
                            Border.all(width: 1.2, color: Color(0xfff4811e)),
                      ),
                      child: Center(
                          child: Text(
                        "Continue Shopping".toUpperCase(),
                        style: GoogleFonts.aBeeZee(
                            fontSize: 15,
                            color: Color(
                              0xffff7703,
                            ),
                            fontWeight: FontWeight.bold),
                      )),
                    ),
                  ),
                ),
              ],
            ),
          );
  }

  getCartItems(String userId, String deviceId) async {
    map = {"uid": userId, "device_type": "Mobile", "mid": deviceId};

    try {
      var Apiurls = Uri.parse(ApiUrls.GetUserCartItemsAPI);
      var response = await http.post(Apiurls, body: map);
      if (response.statusCode == 200) {
        map = json.decode(response.body);
        setState(() {
          if(pr.isShowing()){
            pr.hide();
          }
          IsLoading = false;
          if (map["status"] == "success") {
            totolCartItems = map["totalitems"];
            subTotalAmt = map['cart_subtotal'];
            discountAmt = map["discount_amt"];
            BugTotalAmt = double.parse(subTotalAmt) + double.parse(discountAmt);
            var res = map["products"] as List;
            cartItemsList = res.map((e) => Cart.formJson(e)).toList();
            if (cartItemsList.length > 0) {
              IsVisiblityCartUI = true;
              IsVisiblityNoRecords = false;
            } else {
              IsVisiblityNoRecords = true;
              IsVisiblityCartUI = false;
            }
          }
        });
      }
    } catch (e) {
      AlertDialogManager()
          .IsErrorAlertDialog(context, "Status", e.toString(), "");
    }
  }
  updateItemQuantity(int WeightId,int Qty)async{

    map={
      "weightid":WeightId.toString(),
      "qnty":Qty.toString(),
      "uid":UserId,
      "device_type":"Mobile",
      "store_id":"0",
      "mid":deviceID
    };

    try{
      var Apiurls=Uri.parse(ApiUrls.UpdateItemsQty);
      var response=await http.post(Apiurls,body: map);
      if(response.statusCode==200) {
        map = json.decode(response.body);
        setState(() {
          if (map["status"] == "updated") {
            totolCartItems = map["totalitems"];
            showMessageByToast("Item QTY Updated Successfully .");
            getCartItems(UserId, deviceID);
          }else{
            totolCartItems = map["totalitems"];
            showMessageByToast("Item Deleted Successfully .");
            getCartItems(UserId, deviceID);
          }
        });
      }
    }catch(e){
      AlertDialogManager().IsErrorAlertDialog(context,"Status", e.toString(), "");
    }

  }
  void showMessageByToast(String msg) {
    Fluttertoast.showToast(
        msg: msg,
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.BOTTOM,
        backgroundColor: Colors.grey[200],
        textColor: Colors.black,
        fontSize: 12.0
    );

  }

  void _deleteCartItems(String userId, String deviceID, int wieghtid) async{

  map={
      "weightid":wieghtid.toString(),
      "uid":UserId,
      "mid":deviceID
    };

  try{
      var Apiurls=Uri.parse(ApiUrls.DeleteCartItems);
      var response=await http.post(Apiurls,body: map);
      if(response.statusCode==200) {
        map = json.decode(response.body);
        setState(() {
           pr.hide();
          if (map["status"] == "deleted") {
            showMessageByToast("Item Deleted Successfully .");
            getCartItems(UserId, deviceID);
          }else{
            showMessageByToast("Item Deleted Successfully .");
            getCartItems(UserId, deviceID);
            
          }
        });
      }
    }catch(e){
      AlertDialogManager().IsErrorAlertDialog(context,"Status", e.toString(), "");
    }

  }

  
}
