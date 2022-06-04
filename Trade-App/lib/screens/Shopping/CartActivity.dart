import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:get/get.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:provider/provider.dart';
import 'package:trading_apps/api/apis.dart';
import 'package:trading_apps/api/user_data.dart';
import 'package:trading_apps/custom_ui/progress_dialog.dart';
import 'package:trading_apps/models/CartModel.dart';
import 'package:http/http.dart' as http;
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/screens/Shopping/ItemDescriptionActivity.dart';
import 'package:trading_apps/screens/Shopping/ShoppingActivity.dart';
import 'package:trading_apps/screens/dashboard.dart';

import 'package:trading_apps/utility/common_methods.dart';
import 'package:trading_apps/utility/connectivityprovider.dart';

import 'AllCategoryItemsActivity.dart';
import 'MyAddressActivity.dart';
import 'OrderReviewActivity.dart';

class CartActivity extends StatefulWidget {

  const CartActivity({Key? key}) : super(key: key);

  @override
  _CartActivityState createState() => _CartActivityState();
}

class _CartActivityState extends State<CartActivity> {

  List<CartModel>cartModelList=[];
  String NoOfCartItems="0";
  int totalAmt=0,tdcAmount=0,mainWallet=0;
  var IsLoading=true;

  var IsVisiblityCartUI=true,IsVisiblityNoRecordsFound=false;



@override
  void initState() {
  Provider.of<ConnectivityProvider>(context,listen: false).startMonitoring();
  cartAPI();
  super.initState();
  }
  @override
  Widget build(BuildContext context) {
    return Consumer<ConnectivityProvider>(builder: (ctx,data,child){
      if(data.isOnline!=null){
        return data.isOnline?Scaffold(
          backgroundColor: Colors.grey[100],
          appBar: AppBar(
            backgroundColor: Colors.black,
            actions: [
              Stack(
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
              )
            ],
            title: Text(
              "Your Cart",
              style: GoogleFonts.aBeeZee(color: Colors.white, fontSize: 14),
            ),
          ),
          body: IsLoading==false?RefreshIndicator(
            onRefresh:  ()=>cartAPI(),
            child: ListView(
              children: [
                Visibility(
                  visible: IsVisiblityCartUI,
                  child: Container(
                    height: MediaQuery.of(context).size.height*.26,
                    color: Colors.black,
                    child: Row(
                      children: [
                        Expanded(child: Column(
                          children: [
                            Padding(
                              padding: const EdgeInsets.only(top:15.0,left: 15),
                              child: Align(
                                alignment: Alignment.centerLeft,
                                child: Text("\$$tdcAmount.00 \nDetect From TDC Wallet",style: GoogleFonts.aBeeZee(color: Colors.white,fontSize: 15,
                                    fontWeight: FontWeight.w500),),
                              ),
                            ),

                            Padding(
                              padding: const EdgeInsets.only(top:15.0,left: 15),
                              child: Align(
                                alignment: Alignment.centerLeft,
                                child: Text("\$$mainWallet.00 \nDetect From Main Wallet",style: GoogleFonts.aBeeZee(
                                    color: Colors.white,fontSize: 15,
                                    fontWeight: FontWeight.w600),),
                              ),
                            ),

                            Padding(
                              padding: const EdgeInsets.only(top:15.0,left: 15),
                              child: Align(
                                alignment: Alignment.centerLeft,
                                child: Row(
                                  children: [
                                    Text("\$"+totalAmt.toString()+".00"+"\n"+"Grand Total",style: GoogleFonts.aBeeZee(color: Colors.white,fontSize: 15,
                                        fontWeight: FontWeight.w600),),
                                    Spacer(),
                                    Align(
                                      alignment: Alignment.centerRight,
                                      child: GestureDetector(
                                        onTap: (){
                                          navPush(context,MyAddressActivity());
                                        },
                                        child: Padding(
                                          padding: const EdgeInsets.only(right:10.0),
                                          child: Container(
                                            width: 130,
                                            height: 50,
                                            decoration: BoxDecoration(
                                              borderRadius: BorderRadius.circular(10),
                                              color: Colors.white,
                                            ),
                                            child: Center(child: Text("Checkout",style: GoogleFonts.aBeeZee(color: Colors.black,fontSize: 16,
                                                fontWeight: FontWeight.w600),)),
                                          ),
                                        ),
                                      ),
                                    )
                                  ],
                                ),
                              ),
                            )
                          ],
                        )),
                      ],
                    ),
                  ),
                ),
                Visibility(
                  visible: IsVisiblityCartUI,
                  child: Container(
                      height: MediaQuery.of(context).size.height * .65,
                      child: ListView.builder(
                          itemCount: cartModelList.length==null?0:cartModelList.length,
                          itemBuilder: (BuildContext ctx,int i){
                            if(cartModelList[i].tdcDetect=="0"){
                              cartModelList[i].visibleTdcMessage=false;
                            }else{
                              cartModelList[i].visibleTdcMessage=true;
                            }
                            return Padding(
                              padding: const EdgeInsets.only(top: 14.0,left: 8,right: 8),
                              child: Stack(
                                children: [
                                  GestureDetector(
                                    onTap: (){
                                      Navigator.of(context).push(new MaterialPageRoute(builder: (_)=>
                                          ItemDescriptionActivity(ItemId: cartModelList[i].productId,
                                            vendorId: 0, tdcDeduct: "",))).then((value){
                                        cartAPI();
                                      });
                                    },
                                    child: Container(
                                      height: 140,
                                      decoration: BoxDecoration(
                                          borderRadius: BorderRadius.circular(4),
                                          boxShadow: [
                                            BoxShadow(
                                                color:  Colors.grey,
                                                offset: Offset(6, 3),
                                                blurRadius: 10
                                            ),
                                          ],
                                          color: Colors.white),
                                      child: Row(
                                        children: [
                                          Container(
                                            height: double.infinity,
                                            width: MediaQuery.of(context).size.width * .25,
                                            child: Padding(
                                              padding: const EdgeInsets.only(top:15.0,left: 5,bottom: 15),
                                              child: Container(
                                                decoration: BoxDecoration(
                                                    color: Colors.white,
                                                    boxShadow: [
                                                      BoxShadow(
                                                        color:  Colors.grey,
                                                        offset: Offset(4, 3),
                                                        blurRadius: 9,
                                                      ),
                                                    ],
                                                    borderRadius: BorderRadius.circular(6)
                                                ),
                                                child: ClipRRect(
                                                  borderRadius: BorderRadius.circular(6),
                                                  child: Image.network(
                                                    cartModelList[i].image,fit: BoxFit.fitHeight,),
                                                ),
                                              ),
                                            ),
                                          ),
                                          Container(
                                            width: MediaQuery.of(context).size.width*.45,
                                            child: Padding(
                                              padding: const EdgeInsets.only(top:10.0),
                                              child: Column(
                                                mainAxisAlignment: MainAxisAlignment.start,
                                                children: [
                                                  Align(
                                                    alignment: Alignment.centerLeft,
                                                    child: Padding(
                                                      padding: const EdgeInsets.only(left:18.0,top: 15),
                                                      child:RichText(
                                                          textAlign: TextAlign.left,
                                                          overflow: TextOverflow.ellipsis,
                                                          text: TextSpan(text:cartModelList[i].productName,style: GoogleFonts.
                                                          aBeeZee(color: Colors.black,
                                                              fontWeight: FontWeight.bold),)
                                                      ),
                                                    ),
                                                  ),
                                                  Padding(
                                                    padding: const EdgeInsets.only(top:5.0,left:15),
                                                    child: Align(
                                                      alignment: Alignment.centerLeft,
                                                      child: RichText(
                                                          textAlign: TextAlign.left,
                                                          overflow: TextOverflow.ellipsis,
                                                          text:TextSpan(
                                                            text:cartModelList[i].productDescription.replaceAll("\n",
                                                                " "),style:
                                                          GoogleFonts.aBeeZee(color: Colors.grey[600]),
                                                          )
                                                      ),
                                                    ),
                                                  ),
                                                  Align(
                                                    alignment: Alignment.centerLeft,
                                                    child: Padding(
                                                      padding: const EdgeInsets.only(left:18.0,top: 5),
                                                      child: Text("\$"+ cartModelList[i].unitPrice+".00",style: GoogleFonts.aBeeZee(color: Colors.black,
                                                          fontWeight: FontWeight.bold),),
                                                    ),
                                                  ),
                                                  Visibility(
                                                    visible: cartModelList[i].visibleTdcMessage,
                                                    child: Align(
                                                      alignment: Alignment.centerLeft,
                                                      child: Padding(
                                                        padding: const EdgeInsets.only(left:18.0,top: 5),
                                                        child: Text('Detect From TDC Wallet \$'+
                                                            cartModelList[i].totaltdcdetect.toString()+'.00',style:
                                                        GoogleFonts.aBeeZee(color: Colors.black,fontSize: 12,fontWeight: FontWeight.bold),),
                                                      ),
                                                    ),
                                                  ),
                                                ],
                                              ),
                                            ),
                                          ),
                                          Container(
                                            height: double.infinity,
                                            width: MediaQuery.of(context).size.width*.25,
                                            child: Align(
                                              alignment: Alignment.centerRight,
                                              child: Column(
                                                children: [
                                                  Padding(
                                                    padding: const EdgeInsets.only(top:20,right:14,bottom: 20),
                                                    child: Align(
                                                        alignment: Alignment.centerRight,
                                                        child: GestureDetector(
                                                          onTap: (){
                                                            showDialog(
                                                              context: context,
                                                              builder: (BuildContext context) {
                                                                // return object of type Dialog
                                                                return AlertDialog(
                                                                  title: Text('Remove Item'),
                                                                  content: new Text("Are you sure you want to delete this item?"),
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
                                                                        Navigator.of(context).pop();
                                                                        deleteCartItemAPI(cartModelList[i].tblCartid);
                                                                      },
                                                                    ),
                                                                    // usually buttons at the bottom of the dialog

                                                                  ],
                                                                );
                                                              },
                                                            );

                                                          },
                                                          child: Container(
                                                            height: 25,width: 25,
                                                            decoration:BoxDecoration(
                                                              color: Colors.white,
                                                             // border: Border.all(width: 1,color: APP_PRIMARY_COLOR),
                                                              borderRadius: BorderRadius.circular(10),
                                                            ),
                                                            child: Padding(
                                                              padding: const EdgeInsets.all(2.0),
                                                              child: Image.asset("assets/images/metalbucket.png",
                                                                height: 20, width: 20,color: Colors.red,),
                                                            ),
                                                          ),
                                                        )),
                                                  ),

                                                  Row(
                                                    children: [
                                                      Padding(
                                                        padding: const EdgeInsets.only(left:5.0,top:8),
                                                        child: GestureDetector(
                                                          onTap: (){
                                                            setState(() {
                                                              if (cartModelList[
                                                              i].quantity == 1) {
                                                                showDialog(
                                                                  context: context,
                                                                  builder: (BuildContext context) {
                                                                    // return object of type Dialog
                                                                    return AlertDialog(
                                                                      title: Text('Remove Item'),
                                                                      content: new Text("Are you sure you want to delete this item?"),
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
                                                                            Navigator.of(context).pop();
                                                                            cartModelList[i].quantity = 0;
                                                                            //  ProgressDialogManagers().IsProgressDialogDismiss(context);
                                                                            updateItemQuantity(cartModelList[i].productId,
                                                                                cartModelList[i].quantity,
                                                                                cartModelList[i].unitPrice);
                                                                          },
                                                                        ),
                                                                        // usually buttons at the bottom of the dialog

                                                                      ],
                                                                    );
                                                                  },
                                                                );
                                                              } else {
                                                                cartModelList[i].quantity--;
                                                                //   ProgressDialogManagers().IsProgressDialogDismiss(context);
                                                                updateItemQuantity(cartModelList[i].productId,
                                                                    cartModelList[i].quantity,
                                                                    cartModelList[i].unitPrice);
                                                              }
                                                            });
                                                          },
                                                          child: Container(
                                                            width: 30,
                                                            height: 30,
                                                            decoration: BoxDecoration(
                                                                borderRadius: BorderRadius.circular(6),
                                                                color: Colors.grey[400]
                                                            ),
                                                            child: Align(
                                                                alignment: Alignment.center,
                                                                child: Image.asset("assets/images/minus.png",
                                                                  height: 9,width:9,color: Colors.white,)),
                                                          ),
                                                        ),
                                                      ),
                                                      Container(
                                                          height: 20,
                                                          width: 25,
                                                          child: Padding(
                                                            padding: const EdgeInsets.only(left:5.0,top:4),
                                                            child: Center(
                                                              child: Text(cartModelList[i].quantity.toString(),
                                                                style: GoogleFonts.aBeeZee(),),
                                                            ),
                                                          )),
                                                      Padding(
                                                        padding: const EdgeInsets.only(left:5.0,top:8),
                                                        child: GestureDetector(
                                                          onTap: (){
                                                            cartModelList[i].quantity++;
                                                            updateItemQuantity(cartModelList[i].productId,
                                                                cartModelList[i].quantity,
                                                                cartModelList[i].unitPrice);
                                                          },
                                                          child: Container(
                                                            width: 30,
                                                            height: 30,
                                                            decoration: BoxDecoration(
                                                              color: Colors.black,
                                                              borderRadius: BorderRadius.circular(6),
                                                            ),
                                                            child: Icon(Icons.add,size: 15,color: Colors.white,),
                                                          ),
                                                        ),
                                                      )
                                                    ],
                                                  ),
                                                ],
                                              ),
                                            ),
                                          )
                                        ],
                                      ),
                                    ),
                                  ),
                                ],
                              ),
                            );
                          })
                  ),
                ),

                Visibility(
                  visible: IsVisiblityNoRecordsFound,
                  child: Container(
                    height: 600,
                    width: double.infinity,
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Image.asset(
                          "assets/images/shoppingbags.png",
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
                              Get.offAll(Dashboard(from: "Shopping",));
                            },
                            child: Container(
                              height: 45,
                              width: double.infinity,
                              decoration: BoxDecoration(
                                borderRadius: BorderRadius.circular(5),
                                border:
                                Border.all(width: 1.2, color: Color(0xff0a0a0a)),
                              ),
                              child: Center(
                                  child: Text(
                                    "Continue Shopping".toUpperCase(),
                                    style: GoogleFonts.aBeeZee(
                                        fontSize: 15,
                                        color: Color(
                                          0xff0a0a0a,
                                        ),
                                        fontWeight: FontWeight.bold),
                                  )),
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                )
              ],
            ),
          ):Center(child: Container(
              padding: EdgeInsets.all(16),
              decoration: BoxDecoration(
                  color: APP_SECONDARY_COLOR,
                  borderRadius: BorderRadius.all(Radius.circular(16))
              ),
              child: CircularProgressIndicator(strokeWidth: 5.2,))),
        ):NoInternetConnection();
      }
      return Container(
        child: Center(
          child: CircularProgressIndicator(),
        ),
      );
    });
  }

  cartAPI()async{

    try{
      final resposne=await http.get(Uri.parse(CartListUrls+userModel!.
      data.first.userId.toString()),headers:headers);
      Map data=json.decode(resposne.body);
      if(data["status"]!=ERROR){
        if(mounted) setState(() {
        totalAmt=data["total"];
        tdcAmount=data['all_tdc_detect'];
        mainWallet=totalAmt-tdcAmount;
        var res=data["data"] as List;
        cartModelList=res.map((e) => CartModel.fromJson(e)).toList();
        if(cartModelList.length>0){
          IsVisiblityNoRecordsFound=false;
          IsVisiblityCartUI=true;
        }else{
          IsVisiblityNoRecordsFound=true;
          IsVisiblityCartUI=false;
        }
      });
      totalCartItemAPI();
      }else{
        Fluttertoast.showToast(msg: data["message"]);
      }
    }catch(e){
      Fluttertoast.showToast(msg: e.toString());
    }

  }

  totalCartItemAPI()async {
    try {
      final response = await http.get(Uri.parse(
          NoOfItemsInCartsUrls + userModel!.data.first.userId.toString()));
      Map data=json.decode(response.body);
      if(mounted)setState(() {
        IsLoading=false;
        if(data["status"]!=ERROR){
          NoOfCartItems=data["quantity"]==null?"0":data["quantity"];
        }else{
          Fluttertoast.showToast(msg: data["message"]);
        }
      });

    }catch(e){
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
      "user_id":userModel!.data.first.userId.toString()
    };

    try {
      final response = await http.post(Uri.parse(UpdateCartUrls), body: map);
      Map data = json.decode(response.body);
      if(mounted)
      setState(() {
        Navigator.of(context).pop();
      //  ProgressDialogManagers().IsProgressDialogDismiss(context);
        if (data["status"] != ERROR) {
          Fluttertoast.showToast(msg: data["message"]);
          cartAPI();
        } else {
          Fluttertoast.showToast(msg: data["message"]);
          cartAPI();
        }
      });

    } catch (e) {
      Fluttertoast.showToast(msg: e.toString());
    }
  }

  void deleteCartItemAPI(int cartId)async {
    showDialog(context: context, builder: (context) => ProgressDialog(),
        barrierDismissible: false).then((value){
      print(value);
    },);
  try{
    final response=await http.get(Uri.parse(DeleteCartItemAPI+cartId.toString()),headers:headers);
    Map data=json.decode(response.body);
    if(mounted)
    setState(() {
      Navigator.of(context).pop();
      if(data["status"]!=ERROR){
        Fluttertoast.showToast(msg: data["message"]);
        cartAPI();
      }else{
        Fluttertoast.showToast(msg: data["message"]);
      }
    });
  }catch(e){
    Fluttertoast.showToast(msg: e.toString());
  }
  }
}


