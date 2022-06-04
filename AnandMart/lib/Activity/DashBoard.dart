

import 'dart:convert';
import 'package:anandmart/Model/City.dart';
import 'package:anandmart/Model/CityAera.dart';
import 'package:flutter/services.dart';
import 'package:progress_dialog/progress_dialog.dart';
import 'package:share/share.dart';
import 'dart:io';
import 'package:anandmart/Activity/AboutUsActivity.dart';
import 'package:anandmart/Activity/AllCategoriesActivity.dart';
import 'package:anandmart/Activity/AllProductDescriptionActivity.dart';
import 'package:anandmart/Activity/CartActivity.dart';
import 'package:anandmart/Activity/CategoryActivity.dart';
import 'package:anandmart/Activity/MyOrderHistoryActivity.dart';
import 'package:anandmart/Login.dart';
import 'package:anandmart/Model/AllMainCategories.dart';
import 'package:anandmart/Model/FlashSaleCategory.dart';
import 'package:anandmart/Model/SaerchProduct.dart';
import 'package:anandmart/Support/AlertDialogManager.dart';
import 'package:anandmart/Support/SharePreferenceManager.dart';
import 'package:flutter/cupertino.dart';
import 'package:get/get.dart';
import 'package:http/http.dart'as http;
import 'package:anandmart/ApplicationConfigration/ApiUrls.dart';
import 'package:anandmart/Model/SliderImage.dart';
import 'package:carousel_pro/carousel_pro.dart';
import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

import 'MyAddressActivtity.dart';

class DashBoard extends StatefulWidget {
  const DashBoard({Key? key}) : super(key: key);

  @override
  _DashBoardState createState() => _DashBoardState();
}

class _DashBoardState extends State<DashBoard> with WidgetsBindingObserver{

  List<SliderImages> imgList=[];
  late Map data;
  late List gridList;
  late List<AllMainCategories>allMainCategories=[];
  late List<FlashSaleCategory>flashSaleCategoryList=[];
  TextEditingController editSearch=new TextEditingController();
  bool IsVisibilityAllMainCategoryUI=true;
  bool IsVisibilitySearchBarUI=false;
  late List<SearchProduct> searchProductList=[];
  bool IsLoading=true,IsVisiblityAddMinusItemsWidget=false,IsVisiblityAddItemsWidget=true;
  int ItemQty=0;
 late Map map;
  List<City> cityList = [];
  List<CityAera> cityAreaList=[];
  int totolCartItems=0;
String deviceId="",UserId="",cityName="",cityAreas="",walletAmt="0";
  late ProgressDialog pr;
 
  

  @override
  void initState() {
    WidgetsBinding.instance!.addObserver(this);
    SharePreferenceManager.instance.getUserID("UserID").then((value){
      SharePreferenceManager.instance.getDeviceID("DeviceID").then((value){
        SharePreferenceManager.instance.getCity("City").then((value){
          SharePreferenceManager.instance.getCityAreas("CityArea").then((value){
            setState(() {
              cityAreas=value
;            });
          });
          setState(() {
            cityName=value;
          });
        });
        setState(() {
          deviceId=value;
          getCartItems(UserId, deviceId);
          getMyWallet(UserId);
        });
      });
      setState(() {
        UserId=value;
      });
    });

    getSliderImage();
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
    return WillPopScope(
      onWillPop: (){
        setState(() {
          if(IsVisibilitySearchBarUI==true){
            IsVisibilitySearchBarUI=false;
            IsVisibilityAllMainCategoryUI=true;
          }else{
            showDialog(
              context: context,
              builder: (BuildContext context) {
                // return object of type Dialog
                return AlertDialog(
                  title: Text("Exit ?",style: GoogleFonts.aBeeZee(fontSize: 16),),
                  content: new Text("Are you sure you want to exit?"
                  ,style: GoogleFonts.aBeeZee(),),
                  actions: <Widget>[
                    new TextButton(
                      child: new Text("CANCEL",style: GoogleFonts.aBeeZee(color: Colors.red,
                        fontSize: 14.899999618530273,),),
                      onPressed: () {
                        Navigator.of(context).pop();
                      },
                    ),

                    new TextButton(
                      child: new Text("OK",style: GoogleFonts.aBeeZee(color: Colors.red,
                        fontSize: 14.899999618530273,),),
                      onPressed: () {
                        exit(0);
                      },
                    ),
                    // usually buttons at the bottom of the dialog

                  ],
                );
              },
            );
          }
        });
        return Future.value(false);
      },
      child: Scaffold(
        appBar: PreferredSize(
          preferredSize: Size(double.infinity, 60),
          child: AppBar(
        brightness: Brightness.dark,
            backgroundColor: Color(0xfff4811e),
            title: GestureDetector(
              onTap: (){
                setState(() {
                  pr.show();
                  getCity();
                });
              },
              child: Column(
                children: [
                  Row(
                    children: [
                      Align(
                          alignment: Alignment.topLeft,
                          child: Text(cityName,style: GoogleFonts.aBeeZee(fontSize: 13,color: Colors.white))),
                      Icon(Icons.keyboard_arrow_down_outlined,size: 20,color: Colors.white,)
                    ],
                  ),
              Align(
                alignment: Alignment.topLeft,
               child: Text(cityAreas,style: GoogleFonts.aBeeZee(fontSize: 13,color: Colors.white),)
              )],
              ),
            ),
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
        ),
        drawer: _createLeftMenu(),
        body: RefreshIndicator(
          onRefresh: () =>
            getCartItems(UserId, deviceId),
          child: Wrap(
            children: [
              Container(
                height: MediaQuery.of(context).size.height*0.08,
                color: Color(0xfff4811e),
                child: Padding(
                  padding: const EdgeInsets.only(left: 12.0,right: 12,top: 12,bottom: 12),
                  child: Container(
                    decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(4.0),
                      color: const Color(0xffffffff),
                      border: Border.all(width: 1.0, color: const Color(0xffffffff)),
                    ),
                    child: Padding(
                      padding: const EdgeInsets.only(left:0.0,right: 20),
                      child: TextField(
                        keyboardType: TextInputType.text,
                        textInputAction: TextInputAction.search,
                        style: GoogleFonts.aBeeZee(color: Colors.black,fontSize: 15.5),
                        onSubmitted: (value){
                          setState(() {
                            IsLoading=true;
                            getSearchzBarProducts(value);
                          });
                        },
                        decoration: new InputDecoration(
                          prefixIcon: Padding(
                            padding: const EdgeInsets.only(top:0),
                            child: Icon(Icons.search,size: 17.8,color: Colors.black),
                          ),
                          hintText: "Search for product",hintStyle: GoogleFonts.aBeeZee(
                            color: Colors.black,),
                          border: InputBorder.none,
                          // prefixIcon: Icon(Icons.lock_open_outlined,color: Color(0xffffffff),),
                        ),
                      ),
                    ),
                  ),
                ),
              ),
             Visibility(
                visible: IsVisibilityAllMainCategoryUI,
                child: Container(
                  height: MediaQuery.of(context).size.height*0.90,
                  child:  IsLoading==false?ListView(
                    children: [
                      CarouselImageSlider(),
                      Padding(
                        padding: const EdgeInsets.only(left:8.0,right: 8.0,top:8),
                        child: Text('Flash Sale>',style:
                        GoogleFonts.aBeeZee(fontSize: 14,color: Colors.black),),
                      ),
                      Container(
                          height:200,child: flashSaleCategoryUI()),
                      Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: Text('Shop By Category >',style:
                        GoogleFonts.aBeeZee(fontSize: 14,color: Colors.black),),
                      ),
                      mainCategoriesUI(),
                    ],
                  ):
                  Center(child: CircularProgressIndicator(strokeWidth: 6.0,color: Color(0xfff4811e),)),
                ),
              ),
              Visibility(
                  visible: IsVisibilitySearchBarUI,
                  child: SearchBarUI(),
                ),
  ]
      ),
        )
            ),
    );
  }
  getSliderImage() async{

    final response=await http.get(Uri.parse(ApiUrls.SliderAPI));
    data=json.decode(response.body);
    if(data['response']=='success'){
      setState(() {
        gridList=data['data'];
        for(int i=0;i<gridList.length;i++) {
          SliderImages sliderImages =new SliderImages(SliderID: gridList[i]["id"],
              Name: gridList[i]["title"],
              ImageURL: gridList[i]["banner_url"]+gridList[i]["image"]);
          imgList.add(sliderImages);
        }
        getAllMainCategories();
      });
    }
  }
  Widget SearchBarUI() {
    return Container(
      height: MediaQuery.of(context).size.height*0.75,
      child: ListView.builder(
        scrollDirection: Axis.vertical,
          shrinkWrap: true,
          itemCount: searchProductList.length == null ? 0 : searchProductList.length,
          itemBuilder: (BuildContext ctx, int i) {
            double currentAmt=double.parse(searchProductList[i].productPrice);
                double previusAmt=double.parse(searchProductList[i].productSPrice);
                double DiscountAmt=currentAmt-previusAmt;
                double finalAmt=DiscountAmt/currentAmt;
                double actuallyDisconunt=finalAmt*100;
                var Amt=actuallyDisconunt.toString().split(".");
            return GestureDetector(
              onTap: (){
               Get.to(AllProductDescriptionActivity(ProductName: searchProductList[i].productName,
               ProductID: searchProductList[i].productId,));
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
                  width: MediaQuery.of(context).size.width*0.100,
                  height: 140,
                  child: Stack(
                    children: [
                      Visibility(
                        visible: searchProductList[i].IsItemAdd,
                        child: GestureDetector(
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
                      Visibility(
                        visible: searchProductList[i].IsItemAddMinus,
                        child: Align(
                          alignment: Alignment.bottomRight,
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
                                  Align(
                                    alignment: Alignment.center,
                                    child: Container(
                                      width: 54,
                                      child: Text(ItemQty.toString(),textAlign: TextAlign.center,style: GoogleFonts.aBeeZee(
                                          fontSize: 11,color: Colors.black,
                                      ),),
                                    ),
                                  ),
                                  Align(
                                    alignment: Alignment.centerRight,
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
                                ],
                              ),
                            ),
                          ),
                        ),
                      ),

                      Row(
                        children: [
                          Container(
                              width: MediaQuery.of(context).size.width*0.25,
                              height: 95,
                              decoration: BoxDecoration(
                                image: DecorationImage(
                                    image: NetworkImage(searchProductList[i].img1),
                                    fit: BoxFit.fill),
                              ),
                          ),
                          Align(
                            alignment: Alignment.topLeft,
                            child: Container(
                              width: MediaQuery.of(context).size.width*0.70,
                              child: Padding(
                                padding: const EdgeInsets.only(left: 8.0, top: 0),
                                child: Column(
                                  children: [
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
                                                text: TextSpan(text:searchProductList[i].productSPrice,style: GoogleFonts.aBeeZee(
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
                                                          text: TextSpan(text:searchProductList[i].productPrice,style: GoogleFonts.aBeeZee(
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
                                                height: 30,
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
                                        padding: const EdgeInsets.only(left:10.0,top: 0),
                                        child: Text(searchProductList[i].productName,
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
          }),
    );
  }
  Widget _createLeftMenu(){
    return Drawer(
      child: ListView(
        children: [
          GestureDetector(
            onTap: (){
             setState(() {
               if(IsVisibilitySearchBarUI==true){
                 IsVisibilityAllMainCategoryUI=true;
                 IsVisibilitySearchBarUI=false;
                 Navigator.of(context).pop();
               }else{
                 Navigator.of(context).pop();
               }
             });
            },
            child: ListTile(
              title: Text("Home",style: GoogleFonts.aBeeZee(fontSize: 15,color:
              Colors.black87),),
              leading: Image.asset("drawable/home.png",height: 24,width: 24,),
            ),
          ),
          ListTile(
            title: Text("My Wallet",style: GoogleFonts.aBeeZee(fontSize: 15,color:
            Colors.black87),),
            leading: Image.asset("drawable/wallet.png",height: 24,width: 24,),
              trailing: Container(
                width: 60,
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.end,
                  children: [
                    Image.asset("drawable/rupee.png",height: 10,width: 10,),
                    Text(walletAmt,style: TextStyle(fontSize: 15,color:
                    Colors.black87),),
                  ],
                ),
              )
          ),
          GestureDetector(
            onTap: (){
              Get.to(CategoryActivity());
            },
            child: ListTile(
              title: Text("Category",style: GoogleFonts.aBeeZee(fontSize: 15,color:
              Colors.black87),),
              leading: Image.asset("drawable/category_images.png",height: 24,width: 24,),
            ),
          ),
          GestureDetector(
            onTap: (){
              Get.to(MyOrderActivity());
            },
            child: ListTile(
              title: Text("My Order",style: GoogleFonts.aBeeZee(fontSize: 15,color:
              Colors.black87,fontWeight: FontWeight.w500),),
              leading: Image.asset("drawable/order.png",height: 24,width: 24,),
            ),
          ),
          GestureDetector(
            onTap: (){
              Get.to(CartActivity());
            },
            child: ListTile(
              title: Row(
                children: [
                  Text("My Cart",style: GoogleFonts.aBeeZee(fontSize: 15,color:
                  Colors.black87,fontWeight: FontWeight.w500),),

                ],
              ),
              leading: Image.asset("drawable/order.png",height: 24,width: 24,),
              trailing: Text(totolCartItems.toString(),style: GoogleFonts.aBeeZee(fontSize: 15,color:
              Colors.black87,fontWeight: FontWeight.w500),)
            ),
          ),
          GestureDetector(
            onTap: (){
              Get.to(MyAddressActivity(CType: "Home",));
            },
            child: ListTile(
              title: Text("My Address",style: GoogleFonts.aBeeZee(fontSize: 15,color:
              Colors.black87,fontWeight: FontWeight.w500),),
              leading: Image.asset("drawable/loc.png",height: 24,width: 24,),
            ),
          ),
          GestureDetector(
            onTap: (){
              SharePreferenceManager.instance.getRefferalCode("refer_code").then((value){
                String refferalCode=value;
                String content="Download Anand Mart app at https://play.google.com/store/apps/details?id="
                    "com.app.anandmartgroceryapp"+" and Enter this "+refferalCode+" Referral code to get money";
                Share.share(content);
              });  
            },
            child: ListTile(
              title: Text("Refer",style: GoogleFonts.aBeeZee(fontSize: 15,color:
              Colors.black87,fontWeight: FontWeight.w500),),
              leading: Image.asset("drawable/reff.png",height: 24,width: 24,),
            ),
          ),
          GestureDetector(
            onTap: (){
              Get.to(AboutUsActivity(title: "About Us", Urls:"https://anandmart.co.in/about_us.html"));
            },
            child: ListTile(
              title: Text("About Us",style: GoogleFonts.aBeeZee(fontSize: 15,color:
              Colors.black87,fontWeight: FontWeight.w500),),
              leading: Image.asset("drawable/aboutttt.png",height: 24,width: 24,),
            ),
          ),
          GestureDetector(
            onTap: (){
              Get.to(AboutUsActivity(title: "Policy",Urls: "https://anandmart.co.in/policy.html",));
            },
            child: ListTile(
              title: Text("Help & Care",style: GoogleFonts.aBeeZee(fontSize: 15,color:
              Colors.black87,fontWeight: FontWeight.w500),),
              leading: Image.asset("drawable/poli.png",height: 24,width: 24,),
            ),
          ),
          GestureDetector(
            onTap: (){
              showDialog(
              context: context,
              builder: (BuildContext context) {
                // return object of type Dialog
                return AlertDialog(
                  title: Text("Sure Logout",style: GoogleFonts.aBeeZee(fontSize: 16),),
                  content: new Text("Do You Want To Logout ?"
                  ,style: GoogleFonts.aBeeZee(),),
                  actions: <Widget>[
                    new TextButton(
                      child: new Text("No",style: GoogleFonts.aBeeZee(color: Colors.red,
                        fontSize: 14.899999618530273,),),
                      onPressed: () {
                        Navigator.of(context).pop();
                      },
                    ),

                    new TextButton(
                      child: new Text("Yes",style: GoogleFonts.aBeeZee(color: Colors.red,
                        fontSize: 14.899999618530273,),),
                      onPressed: () {
                         SharePreferenceManager.instance.IsSharePreferenceRemove();
                          Get.offAll(Login());
                      },
                    ),
                    // usually buttons at the bottom of the dialog

                  ],
                );
              },
            );
             
            },
            child: ListTile(
              title: Text("Logout",style: GoogleFonts.aBeeZee(fontSize: 15,color:
              Colors.black87,fontWeight: FontWeight.w500),),
              leading: Image.asset("drawable/noti.png",height: 24,width: 24,),
            ),
          ),
        ],
      ),
    );
  }
  getAllMainCategories() async{
    final response=await http.get(Uri.parse(ApiUrls.AllMainCategoriesAPI));
    data=json.decode(response.body);
    setState(() {
    if(data['response']=='success') {
      var res = data["data"] as List;
      allMainCategories =res.map((json) => AllMainCategories.formJson(json)).toList();
      getFlashSaleCategories();
     }
      });
    }
  getFlashSaleCategories() async{
    final response=await http.get(Uri.parse(ApiUrls.FlashSaleApi));
    data=json.decode(response.body);
    print("flash Sale "+data.toString());
    setState(() {
      IsLoading=false;
     // if(data['response']=='success') {
        var res = data["data"] as List;
        flashSaleCategoryList =res.map((json) =>FlashSaleCategory.fromJson(json)).toList();
    });
  }

  Widget mainCategoriesUI() {
    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: Container(
        height: MediaQuery.of(context).size.height*0.90,
        child: GridView.builder(
          shrinkWrap: false,
            physics: NeverScrollableScrollPhysics(),
            itemCount: allMainCategories.length==null?0:allMainCategories.length,
            gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
              mainAxisSpacing: 10,
              crossAxisCount: 3,
            ), itemBuilder: (context,i){



          return GestureDetector(
            onTap: (){
               Get.to(AllCategoriesActivity(CategoryName: allMainCategories[i].MainCategoryName,CategoryID:
                 allMainCategories[i].MainCategoryID,));
            },
            child: Container(
              width: double.infinity,
              height: 200,
              child: Wrap(
                children: [
                  Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Container(
                        height: 97,
                        child: FadeInImage.assetNetwork(
                                        placeholder: "drawable/logo.png",
                                        image: allMainCategories[i].MainCategoryImageUrl,
                                        imageErrorBuilder: (context,error,stackTrace)=>
                                            Center(child: Image.asset("drawable/logo.png"),)
                                    ),
                        // child: new Image.network(allMainCategories[i].MainCategoryImageUrl,
                        //   height: double.infinity,
                        //   errorBuilder: (context,error,stackTrace)=>
                        //       Center(child: Image.asset("drawable/logo.png"),),
                        //   fit: BoxFit.fill,)
                    ),
                  ),
                   Padding(
                     padding: const EdgeInsets.all(8.0),
                     child: Center(
                       child: Text(allMainCategories[i].
                       MainCategoryName,textAlign: TextAlign.center,style: GoogleFonts.aBeeZee(
                         color: Colors.black,fontSize: 10
                       ),),
                     ),
                   ),
                ],
              ),
            ),
          );
        }),
      ),
    );
  }

 Widget CarouselImageSlider() {
    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: Container(
        height: MediaQuery.of(context).size.height*0.26,
        child: Carousel(
          defaultImage: Image.asset("drawable/logo.png"),
          dotBgColor:Colors.transparent,
          images: imgList.map((e) =>
              GestureDetector(
                onTap: (){
                  // Get.to(AllMovieAndSerialDetails(movieId: e.MovieID,));
                },
                child: Container(
                    width:double.infinity,child:
                Image.network(e.ImageURL,width:double.infinity,fit: BoxFit.fill,
                    errorBuilder: (context,error,stackTrace)=>
                        Center(child: Image.asset("drawable/logo.png"),))),
              )).toList(),
        ),
      ),
    );
 }

  Widget flashSaleCategoryUI() {
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
                            Navigator.push(context, MaterialPageRoute(builder: (context)=>
                           AllProductDescriptionActivity(ProductName: e.productName,
                             ProductID: e.pid) ));
                          },
                          child: Container(
                              height: 110,
                              child: Stack(
                                children: [
                                  Container(
                                  height: 110,
                                  child: Center(
                                    child: FadeInImage.assetNetwork(
                                        placeholder: "drawable/logo.png",
                                        image: e.pimage,
                                        imageErrorBuilder: (context,error,stackTrace)=>
                                            Center(child: Image.asset("drawable/logo.png"),)
                                    ),
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
                                          child: Text(actuallyPerDiscountAmt[0].toString()+"%"+" OFF",
                                            style: GoogleFonts.aBeeZee(
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
                                  child: Image.asset("drawable/rupee.png",
                                  height: 10,width: 10,)),
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

                            Spacer(),

                            Padding(
                              padding: const EdgeInsets.only(right:8.0),
                              child: Stack(
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
                                            text: TextSpan(text:e.pPrice.contains(",")?pPrice[0]
                                                :e.pPrice,style: GoogleFonts.aBeeZee(
                                                color:Colors.black,fontSize: 10
                                            ))
                                        ),
                                      ),
                                    ],
                                  )
                                ],
                              ),
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

  getSearchzBarProducts(String product) async{
    Map<String,dynamic>hashmap={
      "search_key":product
    };

    String reqUrls=Uri(queryParameters: hashmap).query;
    final response=await http.get(Uri.parse(ApiUrls.SearchBarAPI+"?"+reqUrls));
    if(response.statusCode==200)
    data=json.decode(response.body);
      setState(() {
        IsLoading=false;
         if(data['status']=='success') {
        var res = data["products"] as List;
        searchProductList =
            res.map((json) => SearchProduct.formJson(json)).toList();
        IsLoading=false;
        if(searchProductList.length>0){
          IsVisibilityAllMainCategoryUI=false;
          IsVisibilitySearchBarUI=true;
        }else{
          IsVisibilityAllMainCategoryUI=false;
        }
        }
      });
  }


   _alertDialogforCity(){
    return showDialog(
        context: context,
        barrierDismissible: true,
      builder: (context){
          return AlertDialog(
            content: Padding(
              padding: const EdgeInsets.all(8.0),
              child: Column(
                children: [
                  Container(
                    height: 50,
                    width: double.infinity,
                    color: Colors.white,
                    child: Center(child: Text("Select City",style: GoogleFonts.aBeeZee(fontSize: 14.5,color: Colors.black),)),
                  ),

                  Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: Container(
                        height: 45,
                        decoration: BoxDecoration(
                          borderRadius: BorderRadius.circular(4.0),
                          color: const Color(0xffffffff),
                          border: Border.all(width: 1.0, color: const Color(0xffffffff)),
                        ),
                        child: Padding(
                          padding: const EdgeInsets.only(left:0.0,right: 20),
                          child: TextField(
                            keyboardType: TextInputType.text,
                            // controller: editOTP1,
                            textInputAction: TextInputAction.search,
                            // controller: editPassword,
                            style: GoogleFonts.aBeeZee(color: Colors.black,fontSize: 14.5),
                            // onSubmitted: (value){
                            //   setState(() {
                            //
                            //   });
                            // },
                            decoration: new InputDecoration(
                              prefixIcon: Padding(
                                padding: const EdgeInsets.only(top:4.0),
                                child: Icon(Icons.search,size: 20,color: Colors.black),
                              ),
                              hintText: "Search here...",hintStyle: GoogleFonts.aBeeZee(
                                color: Colors.black),
                              border: InputBorder.none,
                              // prefixIcon: Icon(Icons.lock_open_outlined,color: Color(0xffffffff),),
                            ),
                          ),
                        ),
                      )
                  ),
                ],
              ),
            ),
          );
      }
        );
  }
 Future<Null> getCartItems(String userId, String deviceId) async {
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
          totolCartItems=0;
          IsLoading = false;
          if (map["status"] == "success") {
            totolCartItems = map["totalitems"];
          }
        });
    } catch (e) {
      AlertDialogManager()
          .IsErrorAlertDialog(context, "Status", e.toString(), "");
    }
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
                    padding: const EdgeInsets.all(0.0),
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

  void getMyWallet(String userId) async{


    try{
      final response=await http.get(Uri.parse(ApiUrls.GetMyWalletAPI+userId));
      if(response.statusCode==200){
        data=json.decode(response.body);
        if(data["status"]=="success"){
          walletAmt=data["wallet_amt"];
          SharePreferenceManager.instance.setWalletMinimumDeduction("walletdetection",data["wallet_detection"]);
        }
      }
    }catch(e){
      AlertDialogManager().IsErrorAlertDialog(context, "Status",e.toString(), "");
    }


  }
}



