


import 'dart:convert';
import 'package:anandmart/Activity/CartActivity.dart';
import 'package:anandmart/Model/FlashSaleCategory.dart';
import 'package:anandmart/Model/MultipleProductModel.dart';
import 'package:anandmart/Support/SharePreferenceManager.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:http/http.dart'as http;
import 'package:anandmart/ApplicationConfigration/ApiUrls.dart';
import 'package:anandmart/Support/AlertDialogManager.dart';
import 'package:carousel_pro/carousel_pro.dart';
import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:progress_dialog/progress_dialog.dart';

class AllProductDescriptionActivity extends StatefulWidget {
  final String ProductName,ProductID;
  const AllProductDescriptionActivity({Key? key,required this.ProductName,
    required this.ProductID}) : super(key: key);

  @override
  _AllProductDescriptionActivityState createState() => _AllProductDescriptionActivityState();
}

class _AllProductDescriptionActivityState extends State<AllProductDescriptionActivity> {

  List<String> imgList=[];
  late Map map;
  String ProductName="",OfferPrice="",ActualltyPrice="",NoOfQty="",ProductDescrib="",
      WeightId="";
  int totolCartItems=0;
  late List gridList;
  bool IsLoading=true;
  int NoOfQty1=0;
  bool IsVisiblityAddMinusItemsWidget=false,IsVisiblityAddItemsWidget=true,IsVisiblityRelatedItemsUI=false;
  late String UserId,deviceID;

  List<FlashSaleCategory> flashRelatedCategoryList=[];

  late ProgressDialog pr;

  List<MultipleProductModel> multiplePrtList=[];

  int rowINdex=0;

  String DiscountAmt1='';




  @override
  void initState() {
    SharePreferenceManager.instance.getUserID("UserID").then((value){
      SharePreferenceManager.instance.getDeviceID("DeviceID").then((value){
        setState(() {
          deviceID=value;
          getCartItems(UserId,deviceID);
            getProductDetails(widget.ProductID);
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
    pr=new ProgressDialog(context,isDismissible: true);
    pr.style(
        message: 'Please wait...',
        borderRadius: 10.0,
        backgroundColor: Colors.white,
        progressWidget: Center(
          child: Padding(
            padding: const EdgeInsets.all(8.0),
            child: CircularProgressIndicator(strokeWidth: 5.0,color: Colors.black,),
          ),
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
      backgroundColor: Colors.grey[200],
      appBar: AppBar(
         brightness: Brightness.dark,
        backgroundColor: Color(0xfff4811e),
        title: Text(widget.ProductName,style: GoogleFonts.aBeeZee(fontSize: 15.5,color: Colors.white),),
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
      body: IsLoading==false?ListView(
        children: [
          CarouselImageSlider(),
          Container(
            height: 0.50,color: Colors.grey,),
          ProductDetailsUI(),
          Container(height: 0.50,color: Colors.grey,),
          ProductDetailsUI1(),
           finalSideProductDetails(),
           Visibility(
             visible: IsVisiblityRelatedItemsUI,
             child: Column(
               mainAxisAlignment: MainAxisAlignment.start,
               mainAxisSize: MainAxisSize.min,
               children: [
                 Container(
                   color: Colors.white,
                   width: double.infinity,
                   child: Padding(
                     padding: const EdgeInsets.all(8.0),
                     child: Text("Related Products",style: GoogleFonts.aBeeZee(fontSize: 15,color: Colors.black),),
                   ),
                 ),
                 Container(
               color: Colors.white,
               height: 200,
               child: RelatedCategoryItemsUI())
               ],
             ),
           ),
           
        ],
      ):Center(child: CircularProgressIndicator(strokeWidth: 6.0,color: Color(0xfff4811e),)),
    );
  }
  Widget CarouselImageSlider() {
    return Padding(
      padding: const EdgeInsets.all(0.0),
      child: Container(
        color: Colors.white,
        height: MediaQuery.of(context).size.height*0.36,
        child: Carousel(
          defaultImage: Image.asset("drawable/logo.png"),
          dotBgColor:Colors.transparent,
          images: imgList.map((e) =>
              Container(
                  width:double.infinity,child:
              Image.network(e,width:double.infinity,
                  errorBuilder: (context,error,stackTrace)=>
                      Center(child: Image.asset("drawable/logo.png"),)))).toList(),
        ),
      ),
    );
  }

 Widget ProductDetailsUI() {
    return Container(
      height: MediaQuery.of(context).size.height*0.20,
      color: Colors.white,
      child: Column(
        children: [
         Padding(
           padding: const EdgeInsets.only(left:12.0,top: 15),
           child: Align(
             alignment: Alignment.topLeft,
             child: Container(height: 25,
             width: 70,
        decoration: BoxDecoration(
              borderRadius: BorderRadius.circular(4),
              color: Color(0xfff4811e),
        ),
             child: Center(child: Text(DiscountAmt1+"%"+" OFF",style: GoogleFonts.aBeeZee(fontSize: 12,color: Colors.white),)),),
           ),
         ),

          Align(
              alignment: Alignment.topLeft,
              child: Padding(
                padding: const EdgeInsets.only(left:12.0,top: 10),
                child: Text(ProductName,style: GoogleFonts.aBeeZee(fontSize: 13,color: Colors.black),),
              )),

          Padding(
            padding: const EdgeInsets.only(top:10.0),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.start,
              children: [
                Padding(
                    padding: const EdgeInsets.only(top:10.0,left: 12),
                    child: Image.asset("drawable/rupee.png",height: 10,width: 10,)),
                Padding(
                  padding: const EdgeInsets.only(top:10.0,left:0),
                  child: RichText(
                      textAlign: TextAlign.center,
                      overflow: TextOverflow.ellipsis,
                      text: TextSpan(text:OfferPrice,style: GoogleFonts.aBeeZee(
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
                                text: TextSpan(text:ActualltyPrice,style: GoogleFonts.aBeeZee(
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
                Padding(
                  padding: const EdgeInsets.only(top:6.7),
                  child: Stack(
                    children: [
                      //Add To CArt
                      Visibility(
                        visible: IsVisiblityAddItemsWidget,
                        child: GestureDetector(
                          onTap: (){
                            setState(() {
                             NoOfQty1++;
                             IsVisiblityAddItemsWidget=false;
                             IsVisiblityAddMinusItemsWidget=true;
                             pr.show();
                             ItemsaddToCart();
                            });
                          },
                          child: Align(
                            alignment: Alignment.centerRight,
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
                        visible: IsVisiblityAddMinusItemsWidget,
                        child: Align(
                          alignment: Alignment.centerRight,
                          child: Padding(
                            padding: const EdgeInsets.all(8.0),
                            child: Container(
                              height: 30,
                              width: 105,
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
                                          if(NoOfQty1==1){
                                            NoOfQty1=0;
                                            IsVisiblityAddItemsWidget=true;
                                            IsVisiblityAddMinusItemsWidget=false;
                                            pr.show();
                                            updateItemQuantity();
                                          }else{
                                            NoOfQty1--;
                                            IsVisiblityAddItemsWidget=false;
                                            IsVisiblityAddMinusItemsWidget=true;
                                             pr.show();
                                            updateItemQuantity();
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
                                      width: 54,
                                      child: Text(NoOfQty1.toString(),textAlign: TextAlign.center,style: GoogleFonts.aBeeZee(
                                        fontSize: 11,color: Colors.black,
                                      ),),
                                    ),
                                  ),
                                  Align(
                                    alignment: Alignment.centerRight,
                                    child: GestureDetector(
                                      onTap: (){
                                        setState(() {
                                          NoOfQty1++;
                                          IsVisiblityAddItemsWidget=false;
                                          IsVisiblityAddMinusItemsWidget=true;
                                           pr.show();
                                          updateItemQuantity();
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
                    ],
                  ),
                ),

              ],
            ),
          )

        ],
      ),
    );
 }

 Widget ProductDetailsUI1() {
    return Container(
      color: Colors.white,
      height: MediaQuery.of(context).size.height*0.14,
      child: Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
        Padding(
          padding: const EdgeInsets.only(left:12.0,top: 15),
          child: Text("Unit",style: GoogleFonts.aBeeZee(fontSize: 16,color: Colors.black),),
        ),
          Container(
            color: Colors.white,
            height:50,
            child: ListView.builder(
                scrollDirection: Axis.horizontal,
                itemCount: multiplePrtList.length==null?0:multiplePrtList.length,
                itemBuilder: (context,i){

                  if(rowINdex==i){
                    multiplePrtList[i].tvColor=Color(0xffffffff);
                    multiplePrtList[i].color=Color(0xfff4811e);
                    multiplePrtList[i].colorCorners=Color(0xfff4811e);
                    OfferPrice=multiplePrtList[i].sPrice;
                    ActualltyPrice=multiplePrtList[i].pPrice;
                    WeightId=multiplePrtList[i].WeightId;
                    double currentAmt=double.parse(multiplePrtList[i].pPrice);
                    double previusAmt=double.parse(multiplePrtList[i].sPrice);
                    double DiscountAmt=currentAmt-previusAmt;
                    double finalAmt=DiscountAmt/currentAmt;
                    double actuallyDisconunt=finalAmt*100;
                   var DisArr=actuallyDisconunt.toString().split(".");
                    DiscountAmt1=DisArr[0];
                  }else{
                    multiplePrtList[i].tvColor=Color(0xfff4811e);
                    multiplePrtList[i].color=Color(0xffffffff);
                    multiplePrtList[i].colorCorners=Color(0xfff4811e);
                  }
                  return Padding(
                    padding: const EdgeInsets.only(left:11.0,top: 15),
                    child: Align(
                      alignment: Alignment.topLeft,
                      child: GestureDetector(
                        onTap: (){
                          setState(() {
                            rowINdex=i;
                            OfferPrice=multiplePrtList[i].sPrice;
                            ActualltyPrice=multiplePrtList[i].pPrice;
                            WeightId=multiplePrtList[i].WeightId;
                            double currentAmt=double.parse(multiplePrtList[i].pPrice);
                            double previusAmt=double.parse(multiplePrtList[i].sPrice);
                            double DiscountAmt=currentAmt-previusAmt;
                            double finalAmt=DiscountAmt/currentAmt;
                            double actuallyDisconunt=finalAmt*100;
                            var DisArr=actuallyDisconunt.toString().split(".");
                            DiscountAmt1=DisArr[0];
                          });

                        },
                        child: Container(height: 35,
                          width: 70,
                          decoration: BoxDecoration(
                            borderRadius: BorderRadius.circular(4),
                            border: Border.all(width: 1,color: multiplePrtList[i].colorCorners),
                            color: multiplePrtList[i].color,
                          ),
                          child: Center(child: Text(multiplePrtList[i].Weight,style:
                          GoogleFonts.aBeeZee(fontSize: 14,color: multiplePrtList[i].tvColor),)),),
                      ),
                    ),
                  );
                }),
          )
        ],
      ),
    );
  }

  Widget finalSideProductDetails() {
    return Padding(
      padding: const EdgeInsets.only(top:8.0),
      child: Container(
        color: Colors.white,
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Padding(
              padding: const EdgeInsets.only(left:12.0,top:10),
              child: Text("Description",style: GoogleFonts.aBeeZee(fontSize: 13,color: Colors.black),),
            ),
            Padding(
              padding: const EdgeInsets.only(left:12.0,top:10),
              child: Text(ProductDescrib,style: GoogleFonts.aBeeZee(fontSize: 13,color: Colors.black),),
            ),
            SizedBox(height: 20,)
          ],
        ),
      ),
    );
  }
  getProductDetails(String ProductID)async{

    map={
      "pid":ProductID
    };

    try{
      var Apiurls=Uri.parse(ApiUrls.AllProductDescription);
      var response=await http.post(Apiurls,body: map);
      if(response.statusCode==200){
        map=json.decode(response.body);
        setState(() {
          multiplePrtList.clear();
          if(map["status"]=="success"){
            ProductName=map["pname"];
        //   var OfferPriceArr =map["s_price"].toString().split(",");
           // OfferPrice=OfferPriceArr[0];
          //  var ActualltyPriceArr=map["p_price"].toString().split(",");
          //  ActualltyPrice=ActualltyPriceArr[0];
            var weightArr=map["p_weight"].toString().split(",");
             var pPriceArr= map["p_price"].toString().split(",");
           var sPriceArr= map["s_price"].toString().split(",");
           var weightIdArr=map["weight_id"].toString().split(",");
            for(int j=0;j<weightArr.length;j++){
              MultipleProductModel productModel=new MultipleProductModel(pPrice: pPriceArr[j],
                  sPrice: sPriceArr[j], WeightId: weightIdArr[j],
                  Weight:weightArr[j],color: Color(0xffffffff),colorCorners: Color(0xfff4811e),
                  tvColor: Color(0xfff4811e),DiscountAmt: "");
              multiplePrtList.add(productModel);
            }
            ProductDescrib=map["product_s_desc"];
            gridList=map["pimage"];
        //    WeightId=map["weight_id"];
            imgList.clear();
            for(int i=0;i<gridList.length;i++){
              imgList.add(gridList[i]["image"]);
            }
            IsLoading=false;
            if(map["category_id1"]!=null)
            getRelatedCategoriesItems(map["category_id1"]);

          }
        });
      }
    }catch(e){
      AlertDialogManager().IsErrorAlertDialog(context, "Status",e.toString(), "");
    }
  }

  updateItemQuantity()async{

    map={
      "weightid":WeightId,
      "qnty":NoOfQty1.toString(),
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

  void ItemsaddToCart() async{
    map={
      "weightid":WeightId,
      "qnty":NoOfQty1.toString(),
      "uid":UserId,
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
          setState(() {
             pr.hide();
             showMessageByToast("Already Added .");
          });
         
        }
      }
    }catch(e){
      AlertDialogManager().IsErrorAlertDialog(context,"Status", e.toString(), "");
    }
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
      });
    } catch (e) {
      AlertDialogManager()
          .IsErrorAlertDialog(context, "Status", e.toString(), "");
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
  getRelatedCategoriesItems(String categoryId) async{
    final response=await http.get(Uri.parse(ApiUrls.RelatedProductAPI+categoryId));
    map=json.decode(response.body);
    setState(() {
        var res = map["data"] as List;
        flashRelatedCategoryList =res.map((json) =>FlashSaleCategory.fromJson(json)).toList();
        if(flashRelatedCategoryList.length>0){
          IsVisiblityRelatedItemsUI=true;
        }else{
          IsVisiblityRelatedItemsUI=false;
        }
    });
  }

  Widget RelatedCategoryItemsUI() {
    ListView myZListView=
    new ListView.builder(
        itemCount: flashRelatedCategoryList.length.compareTo(0),
        scrollDirection: Axis.horizontal,
        itemBuilder: (BuildContext ctx,int pos ){
          return Container(
            height: 160,
            child: Row(
              children: flashRelatedCategoryList.map((e){
                var pPriceArray=e.pPrice.split(",");
                var sPriceArray=e.sPrice.split(",");
                String pPrice=pPriceArray[0];
                String sPrice=sPriceArray[0];
                double currentAmt=double.parse(pPrice);
                double previusAmt=double.parse(sPrice);
                double DiscountAmt=currentAmt-previusAmt;
                double finalAmt=DiscountAmt/currentAmt;
                double actuallyDisconunt=finalAmt*100;
                var Amt=actuallyDisconunt.toString().split(".");
                print(e.qnt);
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
                            // Get.to(AllProductDescriptionActivity(ProductName: e.productName,
                            // ProductID: e.pid,));

                            setState(() {
                              IsLoading=true;
                              getProductDetails(e.pid);
                            });
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
                                          child: Text(Amt[0].toString()+"%"+" OFF",style: GoogleFonts.aBeeZee(
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
                                  text: TextSpan(text:sPrice,style: GoogleFonts.aBeeZee(
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
                                          text: TextSpan(text:pPrice,style: GoogleFonts.aBeeZee(
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
}



//map = {_InternalLinkedHashMap} size = 19
//  0 = {map entry} "parentcategory" -> "Cleaning & Household"
//  1 = {map entry} "category_name" -> "Cleaners"
//  2 = {map entry} "category_id1" -> "540"
//  3 = {map entry} "pimage" -> [_GrowableList]
//  4 = {map entry} "pname" -> "Harpic Bathroom Cleaner Floral 200 Ml"
//  5 = {map entry} "product_s_desc" -> ""
//  6 = {map entry} "product_type" -> "Weight"
//  7 = {map entry} "p_weight" -> "200ML,6 PCS ,6 PCS "
//  8 = {map entry} "redem_price" -> "0,0,0"
//  9 = {map entry} "s_price" -> "43.00,245.00,245.00"
//  10 = {map entry} "p_price" -> "44.00,264.00,264.00"
//  11 = {map entry} "weight_id" -> "2595,3365,3366"
//  12 = {map entry} "discount" -> "0"
//  13 = {map entry} "discount_type" -> "0"
//  14 = {map entry} "stock_type" -> "unlimited"
//  15 = {map entry} "stock_value" -> "1"
//  16 = {map entry} "vendor_name" -> " "
//  17 = {map entry} "status" -> "success"
//  18 = {map entry} "error" -> false
