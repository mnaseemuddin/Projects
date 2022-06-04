

// import 'package:carousel_pro/carousel_pro.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'dart:math' as math;

import 'package:get/get.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:provider/provider.dart';
import 'package:tailor/CommonUI/no_internet_connection.dart';
import 'package:tailor/Screens/AllSketchActivity.dart';
import 'package:tailor/ApiRepositary.dart';
import 'package:tailor/Model/ChooseSketchModel.dart';
import 'package:tailor/SHActivities/scketch_dress.dart';
import 'package:tailor/Support/UISupport.dart';
import 'package:tailor/Support/connectivity_provider.dart';
class ChooseSketchActivity extends StatefulWidget {
  const ChooseSketchActivity({Key? key}) : super(key: key);

  @override
  _ChooseSketchActivityState createState() => _ChooseSketchActivityState();
}

class _ChooseSketchActivityState extends State<ChooseSketchActivity> {

    ChooseSketchModel? _sketchModel;
    int page = 0;

  late int selectedDesignId;
    // bool IsLoading=true;

  @override
  void initState() {
    Provider.of<ConnectivityProvider>(context,listen: false).startMonitoring();
    chooseSketchAPI().then((value){
    setState(() {
      ApiResponse response=value;
      _sketchModel=response.data;
      if(response.status!=true)
        Fluttertoast.showToast(msg: response.message.toString());
      });
    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    Size size=MediaQuery.of(context).size;
    return Consumer<ConnectivityProvider>(builder: (consumerContext,model,child){
      if(model.isOnline!=null){
        return model.isOnline?Scaffold(
          body: Container(
            child: _sketchModel ==null?Center(child: CircularProgressIndicator(strokeWidth: 5.5,
              color: Colors.black,),):Container(
              child: Column(
                children: [
                  Expanded(child: PageView.builder(
                    onPageChanged: (page){

                    },
                    itemBuilder: (context, index){
                      Category category = _sketchModel!.data.elementAt(index);
                      selectedDesignId=category.tblCategoryId;
                      return Container(child: Center(child: GestureDetector(
                        child: Column(
                          mainAxisSize: MainAxisSize.min,
                          crossAxisAlignment: CrossAxisAlignment.center,
                          children: [
                            Image.network(category.categoryImage,height: size.height*.50,width: double.infinity,
                              fit: BoxFit.fitHeight,),
                            Text(category.categoryName)
                          ],),

                        onTap: (){
                          // Get.to(AllSketchesActivity(categoryId: category.tblCategoryId, image: category.categoryImage,));
                          Get.to(SketchDress(categoryId: category.tblCategoryId, image:
                          category.categoryImage,categoryName: category.categoryName,));
                        },

                      ),),);
                    }, itemCount: _sketchModel!.data.length,)),
                  GestureDetector(
                    child: Container(
                      height: 50,
                      width: double.infinity,
                      alignment: Alignment.center,
                      child: Text("Let's Start",style: GoogleFonts.aBeeZee(color: Colors.white,
                          fontSize: 20,
                          fontWeight: FontWeight.bold),),

                      margin: EdgeInsets.symmetric(horizontal: 20),
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(8),
                        color: UISupport.APP_PRIMARY_COLOR,
                      ),),
                    onTap: (){
                      // Get.to(AllSketchesActivity(categoryId: _sketchModel!.data.elementAt(page).tblCategoryId, image: _sketchModel!.data.elementAt(page).categoryImage,));
                      Get.to(SketchDress(categoryId: selectedDesignId/*_sketchModel!.data.elementAt(page).tblCategoryId*/, image: _sketchModel!.data.elementAt(page).categoryImage,categoryName:
                      _sketchModel!.data.first.categoryName,));
                    },
                  ),
                  SizedBox(height: 50,)
                ],),
            ),
          ),
        ):InternetConnection();
      }
      return Container(
        child: Center(
          child: CircularProgressIndicator(),
        ),
      );
    });
    // return Scaffold(
    //   backgroundColor: Colors.white,
    //   body: IsLoading==false?SingleChildScrollView(
    //     child: Column(
    //       mainAxisAlignment: MainAxisAlignment.center,
    //       children: [
    //         Center(
    //           child: Padding(
    //             padding: const EdgeInsets.only(top:60.0,left:0,right:0,bottom: 25),
    //             child: Container(
    //               height: MediaQuery.of(context).size.height*.60,
    //               width: double.infinity,
    //               child: Center(
    //                 child: Carousel(
    //                   autoplay: false,
    //                   defaultImage: Image.asset("drawable/configration3d.png"),
    //                   dotBgColor:Colors.transparent,
    //                   dotColor: Colors.black,
    //                   images: _sketchModel!.data.map((e) =>
    //                       GestureDetector(
    //                         onTap: (){
    //
    //                         },
    //                         child: Container(
    //                             width:double.infinity,child:
    //                         Image.network(e.categoryImage,width:double.infinity,fit: BoxFit.fill,
    //                             errorBuilder: (context,error,stackTrace)=>
    //                                 Center(child: Image.asset("drawable/configration3d.png"),))),
    //                       )).toList(),
    //                 ),
    //               ),
    //             ),
    //           ),
    //         ),
    //       GestureDetector(
    //        onTap: (){
    //         Get.to(AllSketchesActivity());
    //        },
    //         child: Container(
    //           height: MediaQuery.of(context).size.height*.20,
    //           width: double.infinity,
    //
    //           child: Align(
    //             alignment: Alignment.bottomCenter,
    //             child: Padding(
    //               padding: const EdgeInsets.only(top:15.0,left:15,right: 15),
    //               child: Container(
    //                 height: 50,
    //                 decoration: BoxDecoration(
    //                   borderRadius: BorderRadius.circular(8),
    //                   color: UISupport.color,
    //                 ),
    //                 child: Row(
    //                   mainAxisAlignment: MainAxisAlignment.center,
    //                   children: [
    //                    Spacer(),
    //                     Padding(
    //                       padding: const EdgeInsets.only(left:0.0),
    //                       child: Text("Let's Start",style: GoogleFonts.aBeeZee(color: Colors.white,
    //                       fontSize: 20,
    //                           fontWeight: FontWeight.bold),),
    //                     ),
    //                     Spacer(),
    //                      Padding(
    //                       padding: const EdgeInsets.only(right:20.0),
    //                       child: Icon(Icons.arrow_forward_ios,size: 24,color: Colors.white,),
    //                     ),
    //                   ],
    //                 ),
    //               ),
    //             ),
    //           ),
    //         ),
    //       ),
    //       ],
    //     ),
    //   ):Center(child: CircularProgressIndicator(
    //     strokeWidth: 5.5,color: Colors.black,
    //   ),),
    // );
  }


}
