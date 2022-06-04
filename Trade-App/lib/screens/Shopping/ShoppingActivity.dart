import 'dart:async';
import 'dart:convert';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:http/http.dart' as http;
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:provider/provider.dart';
import 'package:trading_apps/api/apis.dart';
import 'package:trading_apps/api/user_data.dart';
import 'package:trading_apps/models/HotDealsModel.dart';
import 'package:trading_apps/models/AllCategoryModel.dart';
import 'package:trading_apps/models/SliderImageModel.dart';
import 'package:trading_apps/models/TopCategoryModel.dart';
import 'package:trading_apps/models/TrendingModel.dart';
import 'package:trading_apps/models/topBrandsModel.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/res/constants.dart';
import 'package:trading_apps/screens/Shopping/ItemDescriptionActivity.dart';
import 'package:trading_apps/utility/common_methods.dart';
import 'package:trading_apps/utility/connectivityprovider.dart';
import 'AllCategoryItemsActivity.dart';
import 'CartActivity.dart';
import 'OrderHistoryActivity.dart';

class ShoppingActivity extends StatefulWidget {
  const ShoppingActivity({Key? key}) : super(key: key);

  @override
  _ShoppingActivityState createState() => _ShoppingActivityState();
}

class _ShoppingActivityState extends State<ShoppingActivity> {
  int index = 1000;
  List<SliderImageModel> imgSliderList = [];
  List<TopBrandsModel> topBrandsModelList = [];
  List<Category1> categoryList1 = [];

  //List<HotDealsModel>hotDealsList=[];
  List<TopCategoryModel> topCategoryModelList = [];
  List<CategoryModel> categoryModelList = [];
  List<SubCategoryModel> subCategoryModelList = [];
  List<SubCategoryModel> subCategoryModelList1 = [];
  String noOfCartItems = "0";
  var isLoading = true;

  //List<TradingModel> trendingModelList=[];
  HotDealModel? hotDealModel;

  TrendingDealModel? trendingModel;

  @override
  void initState() {
    Provider.of<ConnectivityProvider>(context, listen: false).startMonitoring();
    sliderAPI();
    super.initState();
  }

  sliderAPI() async {
    try {
      final response = await http.get(Uri.parse(SliderUrl), headers: headers);
      Map data = json.decode(response.body);
      List imgModelList = data["data"];
      if (!mounted) return;
      setState(() {
        imgSliderList.clear();
        for (int i = 0; i < imgModelList.length; i++) {
          SliderImageModel slider = new SliderImageModel(
              tblSliderId: imgModelList[i]["tbl_slider_id"],
              sliderImage: imgModelList[i]["slider_image"]);
          imgSliderList.add(slider);
        }
        topCategoryAPI();
      });
    } catch (e) {
      DialogUtils.instance.edgeAlerts(context, e.toString());
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: APP_PRIMARY_COLOR,
      appBar: AppBar(
        backgroundColor: Colors.black,
        actions: [
          GestureDetector(
            onTap: () {
              // navPush(context,CartActivity());
              Navigator.of(context)
                  .push(new MaterialPageRoute(builder: (_) => CartActivity()))
                  .then((value) {
                sliderAPI();
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
                  padding: const EdgeInsets.only(left: 18.0,right: 5),
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
                      noOfCartItems,
                      style: GoogleFonts.aBeeZee(
                          color: Colors.black, fontSize: 10),
                    )),
                  ),
                ),
              ],
            ),
          )
        ],
      ),
      drawer: navigationDrawerUI(),
      body: isLoading == false
          ? RefreshIndicator(
              onRefresh: () => sliderAPI(),
              child: ListView(
                children: [
                  carouselImageSlider(context, imgSliderList),
                  hotDealModel == null
                      ? Center(
                          child: CircularProgressIndicator(
                          strokeWidth: 5.5,
                          color: APP_PRIMARY_COLOR,
                        ))
                      : trendingModel == null
                          ? Center(
                              child: CircularProgressIndicator(
                              strokeWidth: 5.5,
                              color: APP_PRIMARY_COLOR,
                            ))
                          : otherFunctionUI(),
                ],
              ),
            )
          : Center(
              child: Container(
                  padding: EdgeInsets.all(16),
                  decoration: BoxDecoration(
                      color: APP_SECONDARY_COLOR,
                      borderRadius: BorderRadius.all(Radius.circular(16))),
                  child: CircularProgressIndicator(strokeWidth: 5.2))),
    );
  }

  Widget navigationDrawerUI() {
    return Drawer(
      child: Container(
        color: APP_PRIMARY_COLOR,
        child: ListView(
          children: [
            // Padding(
            //   padding: const EdgeInsets.all(8.0),
            //   child: GestureDetector(
            //       onTap: ()=>Navigator.of(context).pop(),
            //       child: Row(children: [Spacer(),Icon(Icons.cancel,size: 30,color: Colors.white,)],)),
            // ),
            Padding(
              padding: const EdgeInsets.only(top: 8.0),
              child: Image.asset(
                'assets/images/tc_logo.png',
                height: 110,
              ),
            ),
            Padding(
              padding: const EdgeInsets.only(left: 8.0, right: 8, top: 15),
              child: ListView.builder(
                  shrinkWrap: true,
                  // ignore: unnecessary_null_comparison
                  itemCount: categoryModelList.length ?? 0,
                  itemBuilder: (context, pos) {
                    return Column(
                      children: [
                        GestureDetector(
                          onTap: () {
                            Navigator.of(context)
                                .push(new MaterialPageRoute(
                                    builder: (_) => AllCategoryItemsActivity(
                                          ItemId:
                                              categoryModelList[pos].categoryId,
                                          Type: "",
                                          AppBarName: "",
                                        )))
                                .then((value) {
                              sliderAPI();
                            });
                          },
                          child: Container(
                            height: 42,
                            child: Row(
                              children: [
                                Padding(
                                  padding: const EdgeInsets.all(8.0),
                                  child: Text(
                                    categoryModelList[pos].categoryName,
                                    style: GoogleFonts.aBeeZee(
                                        fontSize: 16, color: Colors.white),
                                  ),
                                ),
                                Spacer(),
                                //  Padding(
                                //    padding: const EdgeInsets.only(right:8.0),
                                //    child: Icon(Icons.keyboard_arrow_down_rounded,color: Colors.black,size: 16,),
                                //  ),
                              ],
                            ),
                          ),
                        ),
                        // Visibility(
                        //   visible:
                        //       categoryModelList[pos].IsSubCategoryVisibility,
                        //   child: ListView.builder(
                        //       shrinkWrap: true,
                        //       physics: ClampingScrollPhysics(),
                        //       itemCount: subCategoryModelList1.length ?? 0,
                        //       itemBuilder: (context, pos) {
                        //         return Padding(
                        //           padding: const EdgeInsets.only(
                        //               left: 14.0, top: 8, bottom: 8),
                        //           child: Text(
                        //             subCategoryModelList1[pos].subCategoryName,
                        //             style: GoogleFonts.aBeeZee(
                        //                 fontSize: 16, color: Colors.white),
                        //           ),
                        //         );
                        //       }),
                        // ),
                      ],
                    );
                  }),
            ),
            GestureDetector(
              onTap: () {
                navPush(context, OrderHiostoryActivity());
              },
              child: ListTile(
                title: Text(
                  "Order History",
                  style: GoogleFonts.aBeeZee(fontSize: 15, color: Colors.white),
                ),
                //  leading: Image.asset("drawable/home.png",height: 24,width: 24,),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget otherFunctionUI() {
    return Container(
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          //Top Categories Interface
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: Align(
                alignment: Alignment.centerLeft,
                child: Row(
                  children: [
                    Text(
                      "Top Categories",
                      style: GoogleFonts.aBeeZee(
                          color: Colors.white,
                          fontSize: 15.0,
                          fontWeight: FontWeight.w600),
                    ),
                    Spacer(),
                  ],
                )),
          ),
         topCategoriesUI(),
          //End

          //Hot Deals Interface
          hotDealsHeading(),
          hotDealsUI(),
          //End

          //Trending Interface
          trendingHeading(),
          trendingUI()
          //End
        ],
      ),
    );
  }
  //All Shopping API
    topCategoryAPI() async {
      try {
        final response =
        await http.get(Uri.parse(HomeCategoryUrls), headers: headers);
        Map data = json.decode(response.body);
        if (data["status"] != ERROR) {
          var res = data["data"] as List;
          topCategoryModelList =
              res.map((e) => TopCategoryModel.fromJson(e)).toList();
          allCategoryAPI();
        } else {
          Fluttertoast.showToast(msg: data["message"]);
        }
      } catch (e) {
        Fluttertoast.showToast(msg: e.toString());
      }
    }
    allCategoryAPI() async {
      try {
        final resposne =
        await http.get(Uri.parse(AllCategoryUrls), headers: headers);
        Map data = json.decode(resposne.body);
        if (data["status"] != ERROR) {
          if (!mounted) return;
          setState(() {
            categoryModelList.clear();
            //subCategoryModelList.clear();
            List catList = data["data"];
            for (int i = 0; i < catList.length; i++) {
              CategoryModel categoryModel = new CategoryModel(
                  categoryId: catList[i]["tbl_category_id"],
                  categoryName: catList[i]["category_name"],
                  IsSubCategoryVisibility: false);
              categoryModelList.add(categoryModel);
              // List subCatList=catList[i]["Sub_category"];
              // for(int j=0;j<subCatList.length;j++){
              //   SubCategoryModel subCategoryModel=new SubCategoryModel(
              //       subCateoryId: subCatList[j]["tbl_sub_category_id"],
              //       subCategoryName: subCatList[j]["subcategory_name"],
              //       categoryId: subCatList[j]["tbl_category_id"],
              //   IsSubCategoryVisibility: true);
              //   subCategoryModelList.add(subCategoryModel);
              //  // print(categoryModelList[i].categoryName.toString()+"  "+subCategoryModelList[j].subCategoryName.toString());
              // }
            }
            hotDealAndTrendingMethod();
          });
        } else {
          Fluttertoast.showToast(msg: data["message"]);
        }
      } catch (e) {
        Fluttertoast.showToast(msg: e.toString());
      }
    }
    totalCartItemAPI() async {
      try {
        final response = await http.get(Uri.parse(
            NoOfItemsInCartsUrls + userModel!.data.first.userId.toString()));
        Map data = json.decode(response.body);
        if (!mounted) return;
        setState(() {
          isLoading = false;
          if (data["status"] != ERROR) {
            noOfCartItems =
            data["quantity"] == null ? "0" : noOfCartItems = data["quantity"];
          } else {
            noOfCartItems = "0";
            Fluttertoast.showToast(msg: data["message"]);
          }
        });
      } catch (e) {
        Fluttertoast.showToast(msg: e.toString());
      }
    }
    //End

  void hotDealAndTrendingMethod() {
    hotDealsAPI(userModel!.data.first.userId).then((value) {
      if (!mounted) return;
      setState(() {
        if (value.status) {
          hotDealModel = value.data;
          //   print(hotDealModel!.data.first.productDescription);
        } else {
          DialogUtils.instance.edgeAlerts(context, value.message.toString());
        }
      });
    });
    trendingAPI(userModel!.data.first.userId).then((value) {
      if (!mounted) return;
      setState(() {
        if (value.status) {
          trendingModel = value.data;
        } else {
          DialogUtils.instance.edgeAlerts(context, value.message.toString());
        }
      });
    });
    totalCartItemAPI();
  }

  Widget topCategoriesUI(){
    return  Container(
      margin: EdgeInsets.only(left: 10),
      height: 175,
      child: ListView.separated(
          scrollDirection: Axis.horizontal,
          separatorBuilder: (ctx,_)=>SizedBox(width: 15,),
          itemCount: topCategoryModelList.length ?? 0,
          itemBuilder: (ctx,i){
            return GestureDetector(
              onTap: (){
                Navigator.of(context)
                    .push(new MaterialPageRoute(
                    builder: (_) => AllCategoryItemsActivity(
                      ItemId: topCategoryModelList[i]
                          .tblCategoryId, Type: "", AppBarName: "",
                    )))
                    .then((value) {
                  sliderAPI();
                });
              },
              child: Container(
                  decoration: BoxDecoration(
                      color: APP_PRIMARY_COLOR,
                      boxShadow: [
                        BoxShadow(
                          color: Colors.black12,
                          offset: Offset(2, 1),
                          blurRadius: 2,
                        ),
                      ],
                      borderRadius: BorderRadius.circular(5),
                      border:
                      Border.all(width: 1, color: Colors.black12)),
                  width: 130,
                  child: Column(
                    children: [
                      ClipRRect(
                        borderRadius: BorderRadius.circular(4),
                        child: Image.network(
                          topCategoryModelList[i].categoryImage,
                          fit: BoxFit.fitWidth,height: 140,width: 160,
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: Text(topCategoryModelList[i].categoryName,style:
                        TextStyle(color: Colors.white),),
                      ),
                    ],
                  )),
            );
          }),
    );
    // return Container(
    //   color: APP_PRIMARY_COLOR,
    //   child: Padding(
    //     padding: const EdgeInsets.only(top: 8.0),
    //     child: ListView.builder(
    //         scrollDirection: Axis.horizontal,
    //         itemCount: topCategoryModelList.length ?? 0,
    //         itemBuilder: (BuildContext ctx, int index) {
    //           return Padding(
    //             padding: const EdgeInsets.only(left: 20.0, bottom: 1),
    //             child: GestureDetector(
    //               onTap: () {
    //                 Navigator.of(context)
    //                     .push(new MaterialPageRoute(
    //                     builder: (_) => AllCategoryItemsActivity(
    //                       ItemId: topCategoryModelList[index]
    //                           .tblCategoryId,
    //                       Type: "",
    //                       AppBarName: "",
    //                     )))
    //                     .then((value) {
    //                   sliderAPI();
    //                 });
    //               },
    //               child: Container(
    //                 width: 120,
    //                 decoration: BoxDecoration(
    //                     color: APP_PRIMARY_COLOR,
    //                     boxShadow: [
    //                       BoxShadow(
    //                         color: Colors.black12,
    //                         offset: Offset(2, 1),
    //                         blurRadius: 2,
    //                       ),
    //                     ],
    //                     borderRadius: BorderRadius.circular(5),
    //                     border:
    //                     Border.all(width: 1, color: Colors.black12)),
    //                 child: Column(
    //                   children: [
    //                     Container(
    //                       width: double.infinity,
    //                       height: 113,
    //                       child: Padding(
    //                         padding: const EdgeInsets.all(0.0),
    //                         child: ClipRRect(
    //                           borderRadius: BorderRadius.circular(4),
    //                           child: Image.network(
    //                             topCategoryModelList[index].categoryImage,
    //                             fit: BoxFit.fill,
    //                           ),
    //                         ),
    //                       ),
    //                     ),
    //                     Padding(
    //                       padding: const EdgeInsets.all(8.0),
    //                       child: Center(
    //                         child: Text(
    //                           topCategoryModelList[index].categoryName,
    //                           textAlign: TextAlign.center,
    //                           style: GoogleFonts.aBeeZee(
    //                               color: Colors.white,
    //                               fontWeight: FontWeight.w800,
    //                               fontSize: 14),
    //                         ),
    //                       ),
    //                     )
    //                   ],
    //                 ),
    //               ),
    //             ),
    //           );
    //         }),
    //   ),
    // );
  }

  Widget hotDealsHeading(){
    return  Padding(
      padding: const EdgeInsets.all(8.0),
      child: Align(
          alignment: Alignment.centerLeft,
          child: Row(
            children: [
              Text(
                "Hot Deals",
                style: GoogleFonts.aBeeZee(
                    color: Colors.white,
                    fontSize: 15.0,
                    fontWeight: FontWeight.w600),
              ),
              Spacer(),
              GestureDetector(
                onTap: () {
                  // navPush(context,AllCategoryItemsActivity(ItemId: 0,
                  //   Type: "HotDeals",AppBarName: "Hot Deals",));

                  Navigator.of(context)
                      .push(new MaterialPageRoute(
                      builder: (_) => AllCategoryItemsActivity(
                        ItemId: 0,
                        Type: "HotDeals",
                        AppBarName: "Hot Deals",
                      )))
                      .then((value) {
                    sliderAPI();
                  });
                },
                child: Text(
                  "See all >",
                  style: GoogleFonts.aBeeZee(
                      color: Colors.white,
                      fontSize: 13.4,
                      fontWeight: FontWeight.w400),
                ),
              ),
            ],
          )),
    );
  }

  Widget hotDealsUI(){
    return Container(
      height: 200,
      child: Container(
        margin: EdgeInsets.only(left: 10),
        height: 175,
        child: ListView.separated(
            scrollDirection: Axis.horizontal,
            separatorBuilder: (ctx,_)=>SizedBox(width: 15,),
            itemCount: hotDealModel!.data.length ?? 0,
            itemBuilder: (ctx,i){
              HotDealDatum model = hotDealModel!.data.elementAt(i);
              return GestureDetector(
                onTap: (){
                  Navigator.of(context)
                      .push(new MaterialPageRoute(
                      builder: (_) => ItemDescriptionActivity(
                        ItemId: model.tblProductsId,
                        vendorId: model.vendorId,
                        tdcDeduct: model.tdcDetect,
                      )))
                      .then((value) {
                    sliderAPI();
                  });
                },
                child: Container(
                    decoration: BoxDecoration(
                        color: APP_PRIMARY_COLOR,
                        boxShadow: [
                          BoxShadow(
                            color: Colors.black12,
                            offset: Offset(2, 1),
                            blurRadius: 2,
                          ),
                        ],
                        borderRadius: BorderRadius.circular(5),
                        border:
                        Border.all(width: 1, color: Colors.black12)),
                    width: 130,
                    child: Column(
                      children: [
                        ClipRRect(
                          borderRadius: BorderRadius.circular(4),
                          child: Image.network(
                            model.image,
                            fit: BoxFit.fitHeight,height: 114,width: 115,
                          ),
                        ),
                        Padding(
                          padding: const EdgeInsets.only(top:8.0,left: 8),
                          child: Text(model.productName.capitalize.toString(),maxLines:1,style:
                          TextStyle(color: Colors.white),),
                        ),


                        Padding(
                          padding: const EdgeInsets.only(top:8.0,left: 8),
                          child: Text(model.productDescription.capitalize.toString(),maxLines:1,style:
                          TextStyle(color: Colors.white),),
                        ),
                        Padding(
                          padding: const EdgeInsets.only(top:8.0,left: 8),
                          child: Align(
                            alignment: Alignment.centerLeft,
                            child: Text('\$'+model.productPrice.capitalize.toString()+'.00',maxLines:1,style:
                            TextStyle(color: Colors.white),textAlign: TextAlign.left,),
                          ),
                        ),
                      ],
                    )),
              );
            }),
      ),

    );
    // return Container(
    //   color: APP_PRIMARY_COLOR,
    //   height: MediaQuery.of(context).size.height * .28,
    //   child: Padding(
    //     padding: const EdgeInsets.only(top: 8.0),
    //     child: ListView.builder(
    //         scrollDirection: Axis.horizontal,
    //         itemCount: hotDealModel!.data.length ?? 0,
    //         itemBuilder: (BuildContext ctx, int index) {
    //           HotDealDatum model = hotDealModel!.data.elementAt(index);
    //           return Padding(
    //             padding: const EdgeInsets.only(left: 15.0, bottom: 1),
    //             child: GestureDetector(
    //               onTap: () {
    //                 // navPush(context, ItemDescriptionActivity(ItemId:
    //                 // hotDealsList[index].tblProductsId));
    //                 Navigator.of(context)
    //                     .push(new MaterialPageRoute(
    //                     builder: (_) => ItemDescriptionActivity(
    //                       ItemId: model.tblProductsId,
    //                       vendorId: model.vendorId,
    //                       tdcDeduct: model.tdcDetect,
    //                     )))
    //                     .then((value) {
    //                   sliderAPI();
    //                 });
    //               },
    //               child: Container(
    //                 width: 120,
    //                 decoration: BoxDecoration(
    //                     color: APP_PRIMARY_COLOR,
    //                     boxShadow: [
    //                       BoxShadow(
    //                         color: Colors.black12,
    //                         offset: Offset(2, 1),
    //                         blurRadius: 2,
    //                       ),
    //                     ],
    //                     borderRadius: BorderRadius.circular(6),
    //                     border:
    //                     Border.all(width: 1, color: Colors.black12)),
    //                 child: Column(
    //                   children: [
    //                     Container(
    //                       width: double.infinity,
    //                       height: 114,
    //                       decoration: BoxDecoration(
    //                           color: Colors.white,
    //                           borderRadius: BorderRadius.only(
    //                               topLeft: Radius.circular(6),
    //                               topRight: Radius.circular(6)),
    //                           border: Border.all(
    //                               width: 1, color: Color(0xff20243F))),
    //                       child: ClipRRect(
    //                         borderRadius: BorderRadius.only(
    //                             topLeft: Radius.circular(6),
    //                             topRight: Radius.circular(6)),
    //                         child: Image.network(
    //                           model.image,
    //                           fit: BoxFit.fitHeight,
    //                         ),
    //                       ),
    //                     ),
    //                     Padding(
    //                       padding: const EdgeInsets.only(top: 8.0),
    //                       child: Align(
    //                           alignment: Alignment.centerLeft,
    //                           child: RichText(
    //                             overflow: TextOverflow.ellipsis,
    //                             text: TextSpan(
    //                                 text: model.productName,
    //                                 style: GoogleFonts.aBeeZee(
    //                                     color: Colors.white,
    //                                     fontWeight: FontWeight.w600,
    //                                     fontSize: 13)),
    //                           )),
    //                     ),
    //                     Padding(
    //                       padding: const EdgeInsets.only(top: 4.0),
    //                       child: RichText(
    //                           textAlign: TextAlign.left,
    //                           overflow: TextOverflow.ellipsis,
    //                           text: TextSpan(
    //                             text: model.productDescription
    //                                 .replaceAll("\n", " "),
    //                             style: GoogleFonts.aBeeZee(
    //                                 color: Colors.grey[400],
    //                                 fontWeight: FontWeight.w500,
    //                                 fontSize: 12.5),
    //                           )),
    //                     ),
    //                     Padding(
    //                       padding:
    //                       const EdgeInsets.only(top: 4.0, left: 1),
    //                       child: Align(
    //                         alignment: Alignment.centerLeft,
    //                         child: Text(
    //                           "\$" + model.productPrice + ".00",
    //                           textAlign: TextAlign.left,
    //                           style: GoogleFonts.aBeeZee(
    //                               color: Colors.white,
    //                               fontWeight: FontWeight.w600,
    //                               fontSize: 13.8),
    //                         ),
    //                       ),
    //                     )
    //                   ],
    //                 ),
    //               ),
    //             ),
    //           );
    //         }),
    //   ),
    // );
  }

  Widget trendingHeading(){
    return  Padding(
      padding: const EdgeInsets.all(8.0),
      child: Align(
          alignment: Alignment.centerLeft,
          child: Row(
            children: [
              Text(
                "Trending",
                style: GoogleFonts.aBeeZee(
                    color: Colors.white,
                    fontSize: 15.0,
                    fontWeight: FontWeight.w600),
              ),
              Spacer(),
              GestureDetector(
                onTap: () {
                  // navPush(context,AllCategoryItemsActivity(ItemId:0,
                  //   AppBarName: "Trending",Type: "Trending",));
                  Navigator.of(context)
                      .push(new MaterialPageRoute(
                      builder: (_) => AllCategoryItemsActivity(
                        ItemId: 0,
                        AppBarName: "Trending",
                        Type: "Trending",
                      )))
                      .then((value) {
                    sliderAPI();
                  });
                },
                child: Text(
                  "See all >",
                  style: GoogleFonts.aBeeZee(
                      color: Colors.white,
                      fontSize: 13.4,
                      fontWeight: FontWeight.w400),
                ),
              ),
            ],
          )),
    );
  }

  Widget trendingUI(){
    return  Container(
      height: 200,
      child: ListView.separated(
          scrollDirection: Axis.horizontal,
          separatorBuilder: (ctx,_)=>SizedBox(width: 15,),
          itemCount: trendingModel!.data.length ?? 0,
          itemBuilder: (ctx,i){
            TrendingDatum trendingDatum =
            trendingModel!.data.elementAt(i);
            return GestureDetector(
              onTap: (){
                Navigator.of(context)
                    .push(new MaterialPageRoute(
                    builder: (_) => ItemDescriptionActivity(
                      ItemId: trendingDatum.tblProductsId,
                      vendorId: trendingDatum.vendorId,
                      tdcDeduct: trendingDatum.tdcDetect,
                    )))
                    .then((value) {
                  sliderAPI();
                });
              },
              child: Container(
                  decoration: BoxDecoration(
                      color: APP_PRIMARY_COLOR,
                      boxShadow: [
                        BoxShadow(
                          color: Colors.black12,
                          offset: Offset(2, 1),
                          blurRadius: 2,
                        ),
                      ],
                      borderRadius: BorderRadius.circular(5),
                      border:
                      Border.all(width: 1, color: Colors.black12)),
                  width: 130,
                  child: Column(
                    children: [
                      ClipRRect(
                        borderRadius: BorderRadius.circular(4),
                        child: Image.network(
                          trendingDatum.image,
                          fit: BoxFit.fitHeight,height: 114,width: 115,
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.only(top:8.0,left: 8),
                        child: Text(trendingDatum.productName.capitalize.toString(),maxLines:1,style:
                        TextStyle(color: Colors.white),),
                      ),


                      Padding(
                        padding: const EdgeInsets.only(top:8.0,left: 8),
                        child: Text(trendingDatum.productDescription.capitalize.toString(),maxLines:1,style:
                        TextStyle(color: Colors.white),),
                      ),
                      Padding(
                        padding: const EdgeInsets.only(top:8.0,left: 8),
                        child: Align(
                          alignment: Alignment.centerLeft,
                          child: Text('\$'+trendingDatum.productPrice.capitalize.toString()+'.00',maxLines:1,style:
                          TextStyle(color: Colors.white),textAlign: TextAlign.left,),
                        ),
                      ),
                    ],
                  )),
            );
          }),
    );
  }

  Widget carouselImageSlider(
      BuildContext context, List<SliderImageModel> imgSliderList) {
    List<Widget> list = [];
    imgSliderList.forEach((element) {
      print(imgSliderList.length);
      list.add(Image.network(element.sliderImage, fit: BoxFit.cover));
    });
    return CarouselUI(
        width: double.infinity,
        height: MediaQuery.of(context).size.height * 0.24,
        list: list);
  }

}

class CarouselUI extends StatefulWidget {
  final double width;
  final double height;
  final List<Widget> list;
  final int duration;

  const CarouselUI(
      {Key? key,
      required this.width,
      required this.height,
      required this.list,
      this.duration = 2})
      : super(key: key);

  @override
  _CarouselUIState createState() => _CarouselUIState();
}

class _CarouselUIState extends State<CarouselUI> {
  PageController _controller = PageController();
  Timer? _timer;
  int _counter = 0;
  double _opacity = 1;

  @override
  void initState() {
    _timer = Timer.periodic(Duration(seconds: widget.duration), (timer) {
      setState(() {
        _counter = _counter < widget.list.length - 1 ? _counter + 1 : 0;
        if (_counter == 0) {
          _controller.jumpToPage(_counter);
          _opacity = 0;
          Timer(Duration(milliseconds: 10), () => _opacity = 1);
        } else {
          _controller.animateToPage(_counter,
              duration: Duration(microseconds: 1000), curve: Curves.easeIn);
        }
      });
    });
    super.initState();
  }

  @override
  void dispose() {
    if (_timer != null) _timer!.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      height: widget.height,
      width: widget.width,
      child: Stack(
        alignment: Alignment.bottomCenter,
        children: [
          PageView.builder(
            controller: _controller,
            onPageChanged: (page) {
              setState(() {
                _counter = page;
              });
            },
            itemBuilder: (ctx, i) {
              return widget.list.elementAt(i);
            },
            itemCount: widget.list.length,
          ),
          createIndicator()
        ],
      ),
    );
  }

  Widget createIndicator() {
    List<Widget> list = [];
    widget.list.asMap().forEach((index, element) {
      double radius = index == _counter ? 10 : 8;
      list.add(Container(
        margin: EdgeInsets.all(4),
        width: radius,
        height: radius,
        decoration: BoxDecoration(color: Colors.white, shape: BoxShape.circle),
      ));
    });
    return Row(mainAxisSize: MainAxisSize.min, children: list);
  }
}


//
          // Container(
          //   color: APP_PRIMARY_COLOR,
          //   height: MediaQuery.of(context).size.height * .28,
          //   child: Padding(
          //     padding: const EdgeInsets.only(top: 8.0),
          //     child: ListView.builder(
          //         scrollDirection: Axis.horizontal,
          //         itemCount: trendingModel!.data.length ?? 0,
          //         itemBuilder: (BuildContext ctx, int index) {
          //           TrendingDatum trendingDatum =
          //               trendingModel!.data.elementAt(index);
          //           return Padding(
          //             padding: const EdgeInsets.only(left: 15.0, bottom: 1),
          //             child: GestureDetector(
          //               onTap: () {
          //                 Navigator.of(context)
          //                     .push(new MaterialPageRoute(
          //                         builder: (_) => ItemDescriptionActivity(
          //                               ItemId: trendingDatum.tblProductsId,
          //                               vendorId: trendingDatum.vendorId,
          //                               tdcDeduct: trendingDatum.tdcDetect,
          //                             )))
          //                     .then((value) {
          //                   sliderAPI();
          //                 });
          //               },
          //               child: Container(
          //                 width: 120,
          //                 decoration: BoxDecoration(
          //                     color: APP_PRIMARY_COLOR,
          //                     boxShadow: [
          //                       BoxShadow(
          //                         color: Colors.black12,
          //                         offset: Offset(2, 1),
          //                         blurRadius: 2,
          //                       ),
          //                     ],
          //                     borderRadius: BorderRadius.circular(6),
          //                     border:
          //                         Border.all(width: 1, color: Colors.black12)),
          //                 child: Column(
          //                   children: [
          //                     Container(
          //                       width: double.infinity,
          //                       height: 114,
          //                       decoration: BoxDecoration(
          //                           color: Colors.white,
          //                           borderRadius: BorderRadius.only(
          //                               topLeft: Radius.circular(6),
          //                               topRight: Radius.circular(6)),
          //                           border: Border.all(
          //                               width: 1, color: Color(0xff20243F))),
          //                       child: ClipRRect(
          //                         borderRadius: BorderRadius.only(
          //                             topLeft: Radius.circular(6),
          //                             topRight: Radius.circular(6)),
          //                         child: Image.network(
          //                           trendingDatum.image,
          //                           fit: BoxFit.fitHeight,
          //                         ),
          //                       ),
          //                     ),
          //                     Padding(
          //                       padding: const EdgeInsets.only(top: 8.0),
          //                       child: Align(
          //                         alignment: Alignment.centerLeft,
          //                         child: RichText(
          //                           overflow: TextOverflow.ellipsis,
          //                           text: TextSpan(
          //                             text: trendingDatum.productName,
          //                             style: GoogleFonts.aBeeZee(
          //                                 color: Colors.white,
          //                                 fontWeight: FontWeight.w600,
          //                                 fontSize: 13),
          //                           ),
          //                         ),
          //                       ),
          //                     ),
          //                     Padding(
          //                       padding: const EdgeInsets.only(top: 4.0),
          //                       child: RichText(
          //                           textAlign: TextAlign.left,
          //                           overflow: TextOverflow.ellipsis,
          //                           text: TextSpan(
          //                             text: trendingDatum.productDescription
          //                                 .replaceAll("\n", " "),
          //                             style: GoogleFonts.aBeeZee(
          //                                 color: Colors.grey[400],
          //                                 fontWeight: FontWeight.w500,
          //                                 fontSize: 12.5),
          //                           )),
          //                     ),
          //                     Padding(
          //                       padding:
          //                           const EdgeInsets.only(top: 4.0, left: 1),
          //                       child: Align(
          //                         alignment: Alignment.centerLeft,
          //                         child: Text(
          //                           "\$" + trendingDatum.productPrice + ".00",
          //                           textAlign: TextAlign.left,
          //                           style: GoogleFonts.aBeeZee(
          //                               color: Colors.white,
          //                               fontWeight: FontWeight.w600,
          //                               fontSize: 13.8),
          //                         ),
          //                       ),
          //                     )
          //                   ],
          //                 ),
          //               ),
          //             ),
          //           );
          //         }),
          //   ),
          // ),
          // SizedBox(
          //   height: 10,
          // )