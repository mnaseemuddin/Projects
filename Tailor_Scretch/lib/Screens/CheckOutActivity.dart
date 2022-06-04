
import 'dart:convert';
import 'dart:io';
import 'dart:math';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:http/http.dart'as http;
import 'package:google_fonts/google_fonts.dart';
import 'package:intl/intl.dart';
import 'package:provider/provider.dart';
import 'package:tailor/CommonUI/no_internet_connection.dart';
import 'package:tailor/Screens/Activity.dart';
import 'package:tailor/Support/ProgressDialogManagers.dart';
import 'package:tailor/Support/SharedPreferencesManager.dart';
import 'package:tailor/Support/UISupport.dart';
import 'package:tailor/Support/connectivity_provider.dart';

import '../ApiRepositary.dart';
import 'OrderCoinfirmedActivity.dart';

class CheckOutActivity extends StatefulWidget {
 final String addressType;
 final  String name;
 final  String mobile;
 final String address;
 final String city;
 final String state;
 final int zipcode;
 final String OrderType;
 final String FType;

  const CheckOutActivity({Key? key,required this.addressType,required this.name,required this.mobile,required this.address,required this.city,
  required this.state,required this.zipcode,required this.OrderType,required this.FType}) : super(key: key);

  @override
  _CheckOutActivityState createState() => _CheckOutActivityState();
}

class _CheckOutActivityState extends State<CheckOutActivity> {

  bool Ischecked=false;
  String ratiopaymentMnethod="COD";
  var IsVisibilityApplyCoupon=false;
  var IsLoading=true;
  String? selectedAttribute;
  String? usermesurment;

  String? categoryName;

  String? categoryId;

  String? imagePath;

  String? imgUrl;

  String? designId;

  String? yardage;

  @override
  void initState() {
    Provider.of<ConnectivityProvider>(context,listen: false).startMonitoring();
    SharePreferenceManager.instance.getEaseData("Ease").then((value) {
      setState(() {
        usermesurment = value.replaceAll("{", "").replaceAll("}", "");
        SharePreferenceManager.instance.getSelectedAttribute("selectedAttribute").then((value){
            selectedAttribute=value;
            SharePreferenceManager.instance.getCategoryName("CategoryName").then((value){
              categoryName=value;
              SharePreferenceManager.instance.getCategoryId("CategoryId").then((value){
                categoryId=value;
                SharePreferenceManager.instance.getImagePath("ImagePath").then((value){
                  imagePath=value;
                  SharePreferenceManager.instance.getImageUrl("ImgUrl").then((value){
                    imgUrl=value;
                    SharePreferenceManager.instance.getDesignID("DesignId").then((value){
                      designId=value;
                      SharePreferenceManager.instance.getYardageData("Yardage").then((value){
                        yardage=value.replaceAll("{", "").replaceAll("}", "");
                        print(yardage);
                        print(usermesurment);
                      });
                    });
                  });
                });
              });
            });
        });
      });
    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    return Consumer<ConnectivityProvider>(
      builder: (consumerContext,model,child){
        if(model.isOnline!=null){
          return model.isOnline?Scaffold(
            backgroundColor: Colors.white,
            appBar: AppBar(
              brightness: Brightness.dark,
              title: Text("Review Your Order",style: GoogleFonts.aBeeZee(fontSize: 15),),
              backgroundColor: UISupport.APP_PRIMARY_COLOR,
            ),
            body:Container(
              child: Column(
                children: [
                  Container(
                    color: Colors.white70,
                    width: double.infinity,
                    height: size.height*.45,
                    child: InteractiveViewer(
                      maxScale: 100.2,
                      minScale: 1.2,
                      child: Padding(
                        padding: const EdgeInsets.only(top: 40.0),
                        child: imgUrl==""?Image.file(
                          File(imagePath!),
                          width: size.width,
                          fit: BoxFit.scaleDown,
                        ):Image.network(imgUrl!, width: size.width,
                          fit: BoxFit.scaleDown,),
                      ),
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(top:18.0,left: 10,right: 10),
                    child: Container(
                      height: 1,
                      color: Colors.grey[300],
                    ),
                  ),

                  Padding(
                    padding: const EdgeInsets.only(left:0.0,top: 6),
                    child: Container(
                      height: 25,
                      width: double.infinity,
                      child: Padding(
                        padding: const EdgeInsets.only(left:14.0,top: 0),
                        child: Align(
                            alignment: Alignment.centerLeft,
                            child: Text("Shipping To :",style: GoogleFonts.aBeeZee(fontSize: 13,color: Colors.black),)),
                      ),

                    ),
                  ),
                  Container(height: size.height*.20,
                    decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(5),
                      boxShadow: [
                        BoxShadow(
                          color:  Color(0xFCFAFAFF),
                          offset: Offset(4, 3),
                          blurRadius: 7,
                        ),
                      ],
                    ),
                    child: SingleChildScrollView(
                      scrollDirection: Axis.vertical,
                      child: Column(children: [
                        Row(
                          children: [
                            Flexible(
                              flex: 1,
                              child: Padding(
                                padding: const EdgeInsets.only(left:14.0,top:10),
                                child: Text(widget.name,style: GoogleFonts.aBeeZee(fontSize:13,color:Color(0xff000000))),
                              ),
                            ),
                            Spacer(),
                            Flexible(
                              flex: 1,
                              child: GestureDetector(
                                onTap: (){
                                  Navigator.of(context).pop();
                                },
                                child: Padding(
                                  padding: const EdgeInsets.only(left:14.0,top:10,right: 10),
                                  child: Align(
                                    alignment: Alignment.centerRight,
                                    child: Text("Change Address",
                                        textAlign: TextAlign.end,
                                        style: GoogleFonts.aBeeZee(fontSize:13,fontWeight: FontWeight.w600,color:
                                        Color(0xff000000))),
                                  ),
                                ),
                              ),
                            ),
                          ],
                        ),

                        Align(
                          alignment: Alignment.centerLeft,
                          child: Padding(
                            padding: const EdgeInsets.only(left:14.0,top: 8),
                            child: Text(widget.address,style: GoogleFonts.aBeeZee(fontSize:13,color:
                            Color(0xff000000))),
                          ),
                        ),

                        Align(
                          alignment: Alignment.centerLeft,
                          child: Padding(
                            padding: const EdgeInsets.only(left:14.0,top: 8),
                            child: Text(widget.city+" "+widget.state+" "+widget.zipcode.toString(),style: GoogleFonts.aBeeZee(fontSize:13,color:
                            Color(0xff000000))),
                          ),
                        ),

                        Align(
                            alignment: Alignment.centerLeft,
                            child: Padding(
                              padding: const EdgeInsets.only(left:14.0,top: 8),
                              child: Text(widget.mobile,style: GoogleFonts.aBeeZee(fontSize:13,color:
                              Color(0xff000000))),
                            )
                        ),

                      ],),
                    ),)
                  ,
                  Spacer(),
                  Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: GestureDetector(
                      onTap: (){
                        _placeOrderDialog();
                      },
                      child: Container(
                        height: size.height*.08,
                        decoration: BoxDecoration(
                          color: UISupport.APP_PRIMARY_COLOR,
                          borderRadius: BorderRadius.circular(8),
                        ),
                        child: Padding(
                          padding: EdgeInsets.only(top:14,bottom: 16),
                          child: Center(
                            child: Text("Place Order",textAlign: TextAlign.center,style: GoogleFonts.aBeeZee(
                                fontSize: 18,color: Colors.white
                                ,fontWeight: FontWeight.bold),),
                          ),
                        ),
                      ),
                    ),
                  )
                ],),
            ),
          ):InternetConnection();
        }
        return Container(
          child: Center(
            child: CircularProgressIndicator(),
          ),
        );
      },
    );
  }

  void _placeOrderDialog() {
    showGeneralDialog(
      context: context,
      barrierDismissible: false,
      barrierLabel:
      MaterialLocalizations.of(context).modalBarrierDismissLabel,
      pageBuilder: (BuildContext buildContext, Animation animation,
          Animation secondaryAnimation) {
        // return object of type Dialog
        return Padding(
          padding: const EdgeInsets.all(8.0),
          child: Center(child: Material(
            color: Colors.transparent,
            child: Container(
              decoration: ShapeDecoration(
                  color: Colors.white,
                  shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(6.0))),
              child: Padding(
                padding: const EdgeInsets.all(8.0),
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Padding(
                      padding: const EdgeInsets.only(left:15.0,right: 15),
                      child: Text(' Payment confirm after admin review.It will be take sometimes.',textAlign: TextAlign.center,
                        style: GoogleFonts.aBeeZee(
                            fontSize: 15,color: Colors.black
                        ),),
                    ),
                    Padding(
                      padding: const EdgeInsets.only(top:30.0),
                      child: Row(children: [

                        Expanded(child: Row(
                          mainAxisAlignment: MainAxisAlignment.end,
                          children: [
                            GestureDetector(
                              onTap: (){
                                Navigator.of(context).pop();
                              },
                              child: Container(
                                child: Text("No, I'm Cancel It",style: GoogleFonts.aBeeZee(
                                    fontSize: 16,color:
                                Colors.black),),
                              ),
                            ),
                            Padding(padding: EdgeInsets.only(left: 15)),
                            ElevatedButton(
                                onPressed: (){
                                  showDialog(context: context, builder: (context)=>ProgressDialogManager(),
                                      barrierDismissible: false).then((value){
                                    print(value);
                                  });
                                  var now = new DateTime.now();
                                  var formatter = new DateFormat('dd-MMM-yyyy HH:mm:ss');
                                  String formattedDate = formatter.format(now);
                                  if(widget.OrderType=="FinalViewScreen"){
                                    placeOrderAPI(formattedDate, usermesurment!);
                                  }else if(widget.OrderType=="onDashBoard") {
                                    saveDesignplaceOrderAPI(
                                        formattedDate, usermesurment!,
                                        selectedAttribute!,
                                        imagePath!, categoryId!);
                                  }
                                },style: ElevatedButton.styleFrom(primary: Colors.green,
                                onPrimary: Colors.green),
                                child: Text("Place Order",style: GoogleFonts.aBeeZee(
                                    fontSize: 16,color:
                                Colors.white),))
                          ],))
                      ],),
                    )
                  ],
                ),
              ),
            ),
          ),),
        );
      },
    );
  }

  void placeOrderAPI(String formattedDate, String usermesurment) async {
    try {
      int orderNumber = new Random().nextInt(1000000000);
      var ApiUrls = Uri.parse(PlaceorderApi);
      var request = http.MultipartRequest("POST", ApiUrls);
      request.fields["user_id"] = userModel!.data.first.UserId.toString();
      request.fields["order_number"] = "T" + orderNumber.toString();
      request.fields["category_id"] = categoryId.toString();
      request.fields["selected_attribute"] = selectedAttribute!;
      request.fields["user_mesurment"] = usermesurment!;
      request.fields["order_date"] = formattedDate;
      request.fields['Yardage']=yardage!;
      //add for address
      request.fields["address_type"] = widget.addressType;
      request.fields["name"] = widget.name;
      request.fields["mobile"] = widget.mobile;
      request.fields["address"] = widget.address;
      request.fields["city"] = widget.city;
      request.fields["state"] = widget.state;
      request.fields["zipcode"] = widget.zipcode.toString();
      var img = await http.MultipartFile.fromPath("file", imagePath!);
      request.files.add(img);
      print(request.toString());
      var response = await request.send();
      var responsedata = await response.stream.toBytes();
      var responseString = String.fromCharCodes(responsedata);
      String res = responseString.replaceAll("{", "").replaceAll("}", "");
      List<String> res1 = res.split(",");
      var message = res1[0].split(":");
      var orderNo = res1[2].split(":");
      if (message[1].replaceAll('"', '').replaceAll('"', '').toString() ==
          "success") {
        Navigator.of(context).pop();
        SharePreferenceManager.instance.IsSharePreferenceEaseDataRemove();
        SharePreferenceManager.instance.IsSharePreferenceSelectedAttributeDataRemove();
        SharePreferenceManager.instance.IsSharePreferenceCategoryNameDataRemove();
        SharePreferenceManager.instance.IsSharePreferencCategoryIdDataRemove();
        SharePreferenceManager.instance.IsSharePreferenceImagePathDataRemove();
        SharePreferenceManager.instance.IsSharePreferenceImgUrlDataRemove();
        SharePreferenceManager.instance
            .IsSharePreferenceYardageDataRemove();
        SharePreferenceManager.instance
            .IsSharePreferenceFabricDropDown();
        SharePreferenceManager.instance
            .IsSharePreferenceFabricWidthDropDown();
        SharePreferenceManager.instance
            .IsSharePreferenceFitDropDown();
        SharePreferenceManager.instance
            .IsSharePreferenceInchCmDropDown();
        SharePreferenceManager.instance
            .IsSharePreferenceMainFabricCm();
        SharePreferenceManager.instance
            .IsSharePreferenceMainFabricInch();
        SharePreferenceManager.instance
            .IsSharePreferenceFusibleinterfacingCm();
        SharePreferenceManager.instance
            .IsSharePreferenceFusibleinterfacingInch();
        SharePreferenceManager.instance.IsSharePreferenceFabricWidthInchDropDown();
        Get.off(OrderConfirmActivity(
            OrderNo:
            orderNo[1].replaceAll('"', '').replaceAll('"', '').toString()));
      } else {
        Navigator.of(context).pop();
        Fluttertoast.showToast(
            msg: message[1]
                .replaceAll('"', '')
                .replaceAll('"', '')
                .toString()
                .trim());
      }
    } catch (error) {
      Fluttertoast.showToast(msg: error.toString());
      Navigator.of(context).pop();
    }
  }
  void saveDesignplaceOrderAPI(String formattedDate, String usermesurment,String selectedAttribute,
      String imgName,String categoryId) async {
    try {
      Map mao=new Map();
      int orderNumber = new Random().nextInt(1000000000);
      mao["user_id"] = userModel!.data.first.UserId.toString();
      mao["order_number"] = "T" + orderNumber.toString();
      mao["category_id"] = categoryId.toString();
      mao ["selected_attribute"] = selectedAttribute!;
      mao["user_mesurment"] = usermesurment!;
      mao["order_date"] = formattedDate;
        mao['Yardage']=yardage!;
      //add address
      mao["address_type"] = widget.addressType;
      mao["name"] = widget.name;
      mao["mobile"] = widget.mobile;
      mao["address"] = widget.address;
      mao["city"] = widget.city;
      mao["state"] = widget.state;
      mao["zipcode"] = widget.zipcode.toString();
      mao["image"]=imgName;
      mao['tbl_user_design_save_id']=designId;
      var ApiUrls = Uri.parse(SaveDesignConfirmOrder);
      final response =await http.post(ApiUrls,body:mao);
      data=json.decode(response.body);
      if (data['status']== "success") {
        Navigator.of(context).pop();
          SharePreferenceManager.instance.IsSharePreferenceEaseDataRemove();
        SharePreferenceManager.instance.IsSharePreferenceSelectedAttributeDataRemove();
        SharePreferenceManager.instance.IsSharePreferenceCategoryNameDataRemove();
        SharePreferenceManager.instance.IsSharePreferencCategoryIdDataRemove();
        SharePreferenceManager.instance.IsSharePreferenceImagePathDataRemove();
        SharePreferenceManager.instance.IsSharePreferenceImgUrlDataRemove();
        SharePreferenceManager.instance
            .IsSharePreferenceYardageDataRemove();
        SharePreferenceManager.instance
            .IsSharePreferenceFabricDropDown();
        SharePreferenceManager.instance
            .IsSharePreferenceFabricWidthDropDown();
        SharePreferenceManager.instance
            .IsSharePreferenceFitDropDown();
        SharePreferenceManager.instance
            .IsSharePreferenceInchCmDropDown();
        SharePreferenceManager.instance
            .IsSharePreferenceMainFabricCm();
        SharePreferenceManager.instance
            .IsSharePreferenceMainFabricInch();
        SharePreferenceManager.instance
            .IsSharePreferenceFusibleinterfacingCm();
        SharePreferenceManager.instance
            .IsSharePreferenceFusibleinterfacingInch();
        SharePreferenceManager.instance.IsSharePreferenceFabricWidthInchDropDown();
        Get.off(OrderConfirmActivity(
            OrderNo: data['order_number']));
      } else {
        Fluttertoast.showToast(
            msg: data['message'].trim());
      }
    } catch (error) {
      Fluttertoast.showToast(msg: error.toString());
    }
  }
}
