
import 'dart:convert';
import 'package:anandmart/Activity/AllProductDescriptionActivity.dart';
import 'package:anandmart/ApplicationConfigration/ApiUrls.dart';
import 'package:anandmart/Model/AllSubCategoryItems.dart';
import 'package:anandmart/Support/AlertDialogManager.dart';
import 'package:anandmart/Support/SharePreferenceManager.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:http/http.dart'as http;
import 'package:progress_dialog/progress_dialog.dart';

import 'CartActivity.dart';
import 'TextActivity.dart';

class AllSubCategoryItemActivity extends StatefulWidget {
  final String CategoryID,CategoryName;
  const AllSubCategoryItemActivity({Key? key,required this.CategoryID
  ,required this.CategoryName}) : super(key: key);

  @override
  _AllSubCategoryItemActivityState createState() => _AllSubCategoryItemActivityState();
}

class _AllSubCategoryItemActivityState extends State<AllSubCategoryItemActivity> {
  List<AllSubCategoryItems>allSubCategoryItems1=[];
  late Map data,map;
  late String UserID='',deviceID;
  int totolCartItems=0;
  bool IsLoading=true;
  int NoOfQty=0;

  String sortDece="";

  late ProgressDialog pr;


  @override
  void initState() {
    SharePreferenceManager.instance.getUserID("UserID").then((value){
      SharePreferenceManager.instance.getDeviceID("DeviceID").then((value){
        setState(() {
          deviceID=value;
          getCartItems(UserID,deviceID);
        });
      });
      setState(() {
        UserID=value;
        _getallSubCategoryItems(UserID);
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
        title: Text(widget.CategoryName,style: GoogleFonts.aBeeZee(
            fontSize: 15.5,color: Colors.white
        )),
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
      ),
      body:   IsLoading==false?

      Container(
        height: double.infinity,
        child: ListView(
          physics: NeverScrollableScrollPhysics(),
          children: [
            RefreshIndicator(
               onRefresh: () =>getCartItems(UserID,deviceID),
              child: Container(
                color: Colors.white,
                  height: MediaQuery.of(context).size.height*0.78,
                  child: AllSubCategoryItemsUI()
              ),
            ),
            GestureDetector(
              onTap: (){
               sortAlertDialog();
              },
              child: Container(
                color: Colors.white,
                height: MediaQuery.of(context).size.height*0.10,
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Image.asset("drawable/sort.png",height: 10,width: 10,color: Colors.black,),
                    Padding(
                      padding: const EdgeInsets.only(left:5.0),
                      child: Text("SORT",style: GoogleFonts.aBeeZee(fontSize: 14,color: Colors.black),),
                    )
                  ],
                ),
              ),
            )
          ],
        ),
      ):Center(child: CircularProgressIndicator(
          strokeWidth: 6.0,color: Color(0xfff4811e)
      ))
    );
  }

_getallSubCategoryItems(String userId) async {
    Map<String,dynamic>hashmap={
      "catid":widget.CategoryID,
      "user_id":userId
    };
    print(userId);
    String reqUrls=Uri(queryParameters: hashmap).query;
    final response=await http.get(Uri.parse(ApiUrls.AllSubCategoryItemsAPI+"?"+reqUrls));
    data=json.decode(response.body);
    print(data.toString());
    setState(() {
      allSubCategoryItems1.clear();
      IsLoading=false;
      if(data['status']=='success') {
        var res = data["data"] as List;
        allSubCategoryItems1 =
            res.map((json) => AllSubCategoryItems.formJson(json)).toList();
      }
    });
  }

  Widget AllSubCategoryItemsUI(){
    return ListView.builder(
        scrollDirection: Axis.vertical,
        itemCount: allSubCategoryItems1.length == null ? 0 : allSubCategoryItems1.length,
        itemBuilder: (BuildContext ctx, int i) {
           var pPriceArray=allSubCategoryItems1[i].pPrice.split(",");
           var sPriceArray=allSubCategoryItems1[i].sPrice.split(",");
           String pPrice=pPriceArray[0];
           String sPrice=sPriceArray[0];
           var WeightArr=allSubCategoryItems1[i].pWeight.split(",");
           String Weight=WeightArr[0];
          double currentAmt=double.parse(pPrice);
          double previusAmt=double.parse(sPrice);
          double DiscountAmt=currentAmt-previusAmt;
          double finalAmt=DiscountAmt/currentAmt;
          double actuallyDisconunt=finalAmt*100;
          var Amt=actuallyDisconunt.toString().split(".");
          if(allSubCategoryItems1[i].finalQty>0){
            allSubCategoryItems1[i].IsVisibilityAddItem=false;
            allSubCategoryItems1[i].IsVisibilityAddMinus=true;
          }else{
            allSubCategoryItems1[i].IsVisibilityAddItem=true;
            allSubCategoryItems1[i].IsVisibilityAddMinus=false;
          }
          return GestureDetector(
            onTap: (){
               Get.to(AllProductDescriptionActivity(ProductName: allSubCategoryItems1[i].productName,
               ProductID: allSubCategoryItems1[i].pid,));
            },
            child: Padding(
              padding: const EdgeInsets.all(8.0),
              child: Container(
                decoration: BoxDecoration(
                  boxShadow: [
                    BoxShadow(
                      color: const Color(0x29EFEAEA),
                      offset: Offset(5, 8),
                      blurRadius: 6,
                    ),
                  ],
                  color: Colors.white,
                  borderRadius: BorderRadius.circular(5),
                  border: Border.all(width: 1.0, color: const Color(0xffe7dfdf)),),
                width: double.infinity,
                height: 140,
                child: Stack(
                  children: [
                  Align(
                  alignment: Alignment.bottomCenter,
                  child:Padding(
                    padding: const EdgeInsets.only(bottom: 18.0,right: 58),
                    child: Text(Weight,textDirection:
                    TextDirection.ltr,textAlign: TextAlign.left,style: GoogleFonts.aBeeZee(
                        fontSize: 12,color:Colors.black
                      ),),
                  ),),
                    Visibility(
                      visible: allSubCategoryItems1[i].IsVisibilityAddItem,
                      child: GestureDetector(
                        onTap: (){
                          setState(() {
                            allSubCategoryItems1[i].finalQty++;
                            allSubCategoryItems1[i].IsVisibilityAddItem=false;
                            allSubCategoryItems1[i].IsVisibilityAddMinus=true;
                             pr.show();
                            ItemsaddToCart(allSubCategoryItems1[i].weightId,
                                allSubCategoryItems1[i].finalQty);
                          });
                        },
                        child: Align(
                          alignment: Alignment.bottomRight,
                          child: Padding(
                            padding: const EdgeInsets.all(8.0),
                            child: Container(
                              height: 30,
                              width: 65,
                              decoration: BoxDecoration(
                                color: Colors.black,
                                borderRadius: BorderRadius.circular(4),
                                border: Border.all(width: 1.0, color: const Color(0xffe7dfdf)),),
                              child:Row(
                                mainAxisAlignment: MainAxisAlignment.center,
                                children: [
                                  Text("ADD",style: GoogleFonts.aBeeZee(
                                      fontSize: 11,color: Colors.white
                                  ),),
                                  Padding(
                                    padding: const EdgeInsets.only(left:2.0),
                                    child: Icon(Icons.add,size: 13,color: Colors.white,),
                                  )
                                ],
                              ),
                            ),
                          ),
                        ),
                      ),
                    ),
                    //Increase Decrease Items In Cart
                    Visibility(
                      visible: allSubCategoryItems1[i].IsVisibilityAddMinus,
                      child: Align(
                        alignment: Alignment.bottomRight,
                        child: Padding(
                          padding: const EdgeInsets.all(8.0),
                          child: Container(
                            height: 30,
                            width: 90,
                            decoration: BoxDecoration(
                              color: Colors.white,
                              borderRadius: BorderRadius.circular(4),),
                            child:Row(
                              children: [
                                Align(
                                  alignment: Alignment.topRight,
                                  child: GestureDetector(
                                    onTap: (){
                                      setState(() {
                                        if(allSubCategoryItems1[i].finalQty==1){
                                          allSubCategoryItems1[i].finalQty=0;
                                          allSubCategoryItems1[i].IsVisibilityAddItem=true;
                                          allSubCategoryItems1[i].IsVisibilityAddMinus=false;
                                          pr.show();
                                          updateItemQuantity(allSubCategoryItems1[i].weightId,
                                              allSubCategoryItems1[i].finalQty);
                                        }else{
                                          allSubCategoryItems1[i].finalQty--;
                                          allSubCategoryItems1[i].IsVisibilityAddItem=false;
                                          allSubCategoryItems1[i].IsVisibilityAddMinus=true;
                                            pr.show();
                                          updateItemQuantity(allSubCategoryItems1[i].weightId,
                                              allSubCategoryItems1[i].finalQty);
                                        }
                                      });
                                    },
                                    child: Container(
                                      decoration: BoxDecoration(
                                        color: Color(0xfff4811e),
                                        borderRadius: BorderRadius.circular(4),
                                      ),
                                      height: double.infinity,
                                      width: 24,
                                      child: Center(child: Image.asset("drawable/minus.png",
                                        width: 10,color: Colors.white,)),
                                    ),
                                  ),
                                ),
                                Align(
                                  alignment: Alignment.center,
                                  child: Container(
                                    width: 40,
                                    child: Text(allSubCategoryItems1[i].finalQty.toString(),textAlign: TextAlign.center,style: GoogleFonts.aBeeZee(
                                      fontSize: 11,color: Colors.black,
                                    ),),
                                  ),
                                ),
                                Align(
                                  alignment: Alignment.centerRight,
                                  child: GestureDetector(
                                    onTap: (){
                                      setState(() {
                                        allSubCategoryItems1[i].finalQty++;
                                        allSubCategoryItems1[i].IsVisibilityAddItem=false;
                                        allSubCategoryItems1[i].IsVisibilityAddMinus=true;
                                          pr.show();
                                        updateItemQuantity(allSubCategoryItems1[i].weightId,
                                            allSubCategoryItems1[i].finalQty);
                                      });
                                    },
                                    child: Container(
                                      decoration: BoxDecoration(
                                        color: Color(0xfff4811e),
                                        borderRadius: BorderRadius.circular(4),),
                                      height: double.infinity,
                                      width: 24,
                                      child: Center(child: Icon(Icons.add,
                                        size: 10,color: Colors.white,)),
                                    ),
                                  ),
                                ),
                              ],
                            ),
                          ),
                        ),
                      ),
                    ),
                    Row(
                      children: [
                        Container(
                          margin: EdgeInsets.all(2),
                          width: MediaQuery.of(context).size.width*0.24,
                          height: 95,
                          decoration: BoxDecoration(
                            image: DecorationImage(
                                image: NetworkImage(allSubCategoryItems1[i].pimage),
                                fit: BoxFit.fill),
                          ),
                        ),

                        Align(
                          alignment: Alignment.topLeft,
                          child: Container(
                            width: MediaQuery.of(context).size.width*0.69,
                            child: Padding(
                              padding: const EdgeInsets.only(left: 0.0, top: 0),
                              child: Column(
                                children: [
                                  Padding(
                                    padding: const EdgeInsets.only(top:10.0),
                                    child: Row(
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
                                              text: TextSpan(text:sPrice,style: GoogleFonts.aBeeZee(
                                                  color:Colors.black,fontSize: 14
                                              ))
                                          ),
                                        ),
                                        //
                                        Padding(
                                          padding: const EdgeInsets.only(left: 8.0),
                                          child: Stack(
                                            children: [
                                              Align(
                                                alignment: Alignment.centerRight,
                                                child: Padding(
                                                  padding: const EdgeInsets.only(top:18.0,left: 10),
                                                  child: Container(height: 1,width: 64,color: Colors.grey,),
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
                                                        text: TextSpan(text:pPrice,style: GoogleFonts.aBeeZee(
                                                            color:Colors.black,fontSize: 14
                                                        ))
                                                    ),
                                                  ),
                                                ],
                                              )
                                            ],
                                          ),
                                        ),
                                        Spacer(),
                                        Align(
                                          alignment: Alignment.topRight,
                                          child:Padding(
                                            padding: const EdgeInsets.all(8.0),
                                            child: Container(
                                              decoration: BoxDecoration(
                                                color: Color(0xfff4811e),
                                                borderRadius: BorderRadius.circular(
                                                    3.5),
                                              ),
                                              height: 26,
                                              width: 55,
                                              child: Center(
                                                child: Text(Amt[0].toString()+"%"+" OFF",style: GoogleFonts.aBeeZee(
                                                  fontSize: 8.5,color:Colors.white,
                                                ),),
                                              ),
                                            ),
                                          ),
                                        )
                                      ],
                                    ),
                                  ),
                                  Align(
                                    alignment: Alignment.centerLeft,
                                    child: Padding(
                                      padding: const EdgeInsets.only(left:8.0),
                                      child: Text(allSubCategoryItems1[i].productName,
                                          style: GoogleFonts.aBeeZee(
                                            color: Colors.black,
                                            fontSize: 12.5,
                                          )),
                                    ),
                                  ),
                                ],
                              ),
                            ),
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ),
          );
        });
  }

  // void ItemsaddToCart(String WeightID) async{
  //   map={
  //     "weightid":WeightID.toString(),
  //     "qnty":NoOfQty,
  //     "uid":UserID,
  //     "device_type":"Mobile",
  //     "store_id":"0",
  //     "mid":deviceID
  //   };
  // }

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
        _getallSubCategoryItems(userId);
      });
    } catch (e) {
      AlertDialogManager()
          .IsErrorAlertDialog(context, "Status", e.toString(), "");
    }
  }
  updateItemQuantity(String WeightId,int Qty)async{

    map={
      "weightid":WeightId,
      "qnty":Qty.toString(),
      "uid":UserID,
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
          pr.hide();
          if (map["status"] == "updated") {
            totolCartItems = map["totalitems"];
            showMessageByToast("Item QTY Updated Successfully .");
          }else{
            totolCartItems = map["totalitems"];
            showMessageByToast("Item Deleted Successfully .");
          }
        });
      }
    }catch(e){
      AlertDialogManager().IsErrorAlertDialog(context,"Status", e.toString(), "");
    }

  }

  void ItemsaddToCart(String WeightId,int Qty) async{
    map={
      "weightid":WeightId,
      "qnty":Qty.toString(),
      "uid":UserID,
      "device_type":"Mobile",
      "store_id":"0",
      "mid":deviceID
    };

    try{
      var Apiurls=Uri.parse(ApiUrls.ItemsAddToCartAPI);
      var response=await http.post(Apiurls,body: map);
      if(response.statusCode==200) {
        map = json.decode(response.body);
        if (map["status"] == "added") {
          setState(() {
              pr.hide();
            totolCartItems = map["totalitems"];
            showMessageByToast("Item Added Successfully .");
          });
        }else{
          showMessageByToast("Already Added .");
        }
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

   sortAlertDialog() {
    return showDialog(
      context: context,
      builder: (BuildContext context) {
        // return object of type Dialog
        return AlertDialog(
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Container(
                  height: 40,
                  width: double.infinity,
                  child: Center(child: Text("Sort By",style: GoogleFonts.aBeeZee(fontSize: 14,color: Colors.black),))),
              Padding(
                padding: const EdgeInsets.only(top:8.0),
                child: Container(height: 1,
                    width: double.infinity,
                    color:Colors.grey[100]),
              ),
              GestureDetector(
                onTap: (){
                  setState(() {
                    IsLoading=true;
                    sortDece="sort_desc";
                    cencelDialog();
                  });
                },
                child: Container(
                    height: 40,
                    width: double.infinity,
                    child: Center(child: Text("Price High To Low",style: GoogleFonts.aBeeZee(fontSize: 14,color: Colors.black),))),
              ),
              Padding(
                padding: const EdgeInsets.only(top:8.0),
                child: Container(height: 1,color:Colors.grey[100]),
              ),
              GestureDetector(
              onTap: (){
                setState(() {
                  IsLoading=true;
                 sortDece="sort_asc";
                cencelDialog();
                });

              },
                child: Container(
                    height: 40,
                    width: double.infinity,
                    child: Center(child: Text("Price Low To High",style:
                    GoogleFonts.aBeeZee(fontSize: 14,color: Colors.black),))),
              ),

            ],
          ),
        );
      },
    );


  }

  void sortInHighToLowApiMethod(String sortdec) async{

    Map<String,dynamic>hashmap={
      "catid":widget.CategoryID,
      "sort_price":sortdec
    };

    String reqUrls=Uri(queryParameters: hashmap).query;
    final response=await http.get(Uri.parse(ApiUrls.SortInAPI+"?"+reqUrls));
    if(response.statusCode==200){
     data =json.decode(response.body);
     setState(() {
       allSubCategoryItems1.clear();
       IsLoading=false;
       if(data['status']=='success') {
         var res = data["data"] as List;
         allSubCategoryItems1 =
             res.map((json) => AllSubCategoryItems.formJson(json)).toList();
       }
     });
    }
  }

  void cencelDialog() {
    Navigator.of(context).pop();
    sortInHighToLowApiMethod(sortDece);
  }
}