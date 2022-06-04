import 'dart:convert';
import 'dart:io';
import 'dart:math';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:get/get.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:intl/intl.dart';
import 'package:provider/provider.dart';
import 'package:tailor/CommonUI/no_internet_connection.dart';
import 'package:tailor/Screens/Activity.dart';
import 'package:tailor/Screens/CartActivity.dart';
import 'package:tailor/Screens/OrderCoinfirmedActivity.dart';
import 'package:tailor/ApiRepositary.dart';
import 'package:tailor/CommonUI/CommonSqureTextField.dart';
import 'package:tailor/Model/CategoryAttribute.dart';
import 'package:tailor/Support/ProgressDialogManagers.dart';
import 'package:tailor/Support/SharedPreferencesManager.dart';
import 'package:tailor/Support/UISupport.dart';
import 'package:http/http.dart' as http;
import 'package:tailor/Support/connectivity_provider.dart';

import 'DashBoardActivity.dart';

class FinalSketchViewActivty extends StatefulWidget {
  final String imagePath;
  final String? categoryId;
  final Map<int, SubAttributeData> map;
  final int tblAttributeTextureId;
  final String ColorCode;
  final String categoryName;

  const FinalSketchViewActivty(
      {Key? key,
      required this.imagePath,
      required this.map,
      required this.tblAttributeTextureId,
      required this.ColorCode,
      required this.categoryId,required this.categoryName})
      : super(key: key);

  @override
  _FinalSketchViewActivtyState createState() => _FinalSketchViewActivtyState();
}

class _FinalSketchViewActivtyState extends State<FinalSketchViewActivty> {

  String? selectedAttribute;
  String? usermesurment;
  List<String> addKeyPairId=[];
  String? yardage;



  @override
  void initState() {
    Provider.of<ConnectivityProvider>(context,listen: false).startMonitoring();
    SharePreferenceManager.instance.getEaseData("Ease").then((value){
      setState(() {
        usermesurment = value.replaceAll("{", "").replaceAll("}", "");
        SharePreferenceManager.instance.getYardageData("Yardage").then((value){
          yardage=value.replaceAll("{", "").replaceAll("}", "");
        });
      });
    });

    widget.map.forEach((key, value) {
      addKeyPairId.add('${"SubAttribute ID : " + value.tblSubAttributeId.toString() +
          ", Attribute ID : " + value.attributeId.toString()+", Fabric "+
          widget.tblAttributeTextureId.toString()+" Color :"+widget.ColorCode}');
    });
    addKeyPairId.removeLast();
    selectedAttribute=addKeyPairId.toString().replaceAll("[", "").replaceAll("]", "");
    SharePreferenceManager.instance.setSelectedAttribute("selectedAttribute", selectedAttribute.toString());
    SharePreferenceManager.instance.setCategoryName("CategoryName", widget.categoryName);
    SharePreferenceManager.instance.setCategoryId("CategoryId",widget.categoryId.toString());
    SharePreferenceManager.instance.setImagePath("ImagePath", widget.imagePath);
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    return Consumer<ConnectivityProvider>(builder: (cContext,model,child){
      if(model.isOnline!=null){
        return model.isOnline?Scaffold(
          resizeToAvoidBottomInset: false,
          backgroundColor: Colors.white,
          appBar: AppBar(
            brightness: Brightness.dark,
            leading: Builder(
                builder: (context) => IconButton(
                  icon: Image.asset(
                    "drawable/close.png",
                    height: 16,
                    width: 20,
                    color: Colors.white,
                  ),
                  onPressed: () => Navigator.of(context).pop(),
                )),
            actions: [
              // Padding(
              //     padding: const EdgeInsets.only(left:8.0,right: 12),
              //     child: IconButton(
              //       icon: Image.asset(
              //         'assets/saved.png',
              //         color: Colors.white,width: 20,height: 26,
              //       ),
              //       onPressed: () {
              //         _saveDesignDialog();
              //       },
              //     )),
            ],
            backgroundColor: UISupport.APP_PRIMARY_COLOR,
          ),
          body: Column(
            children: [
              Padding(
                padding: const EdgeInsets.only(left: 8.0, top: 4),
                child: Container(
                  color: Colors.white70,
                  width: double.infinity,
                  height: MediaQuery.of(context).size.height*.65,
                  child: InteractiveViewer(
                    maxScale: 100.2,
                    minScale: 1.2,
                    child: Padding(
                      padding: const EdgeInsets.only(top: 40.0),
                      child: Center(
                        child: Image.file(
                          File(widget.imagePath),
                          width: size.width,
                          fit: BoxFit.scaleDown,
                        ),
                      ),
                    ),
                  ),
                ),
              ),
              Container(
                color: Colors.white,
                height:  MediaQuery.of(context).size.height*.10,
                child: Padding(
                  padding: const EdgeInsets.only(
                      top: 10.0, left: 20, right: 15, bottom: 10),
                  child: GestureDetector(
                    onTap: () {
                      _saveDesignDialog();
                      //  _placeOrderDialog();
                    },
                    child: Container(
                      decoration: BoxDecoration(
                        color: UISupport.APP_PRIMARY_GREEN,
                        borderRadius: BorderRadius.circular(8),
                      ),
                      child: Center(
                        child: Padding(
                          padding: const EdgeInsets.only(left: 10.0),
                          child: Text(
                            "Save Design",
                            style: GoogleFonts.aBeeZee(
                                color: Colors.white, fontSize: 15,fontWeight: FontWeight.w600),
                          ),
                        ),
                      ),
                    ),
                  ),
                ),
              ),
              Container(
                color: Colors.white,
                height:  MediaQuery.of(context).size.height*.10,
                child: Padding(
                  padding: const EdgeInsets.only(
                      top: 10.0, left: 20, right: 15, bottom: 10),
                  child: GestureDetector(
                    onTap: () {
                      SharePreferenceManager.instance.setImageUrl("ImgUrl","");
                      Get.to(MyAddressActivity(FType: "Checkout",OrderType: 'FinalViewScreen',));
                      //  _placeOrderDialog();
                    },
                    child: Container(
                      decoration: BoxDecoration(
                        color: UISupport.APP_PRIMARY_COLOR,
                        borderRadius: BorderRadius.circular(8),
                      ),
                      child: Center(
                        child: Padding(
                          padding: const EdgeInsets.only(left: 10.0),
                          child: Text(
                            "Proceed To Checkout",
                            style: GoogleFonts.aBeeZee(
                                color: Colors.white, fontSize: 15,fontWeight: FontWeight.w600),
                          ),
                        ),
                      ),
                    ),
                  ),
                ),
              )
            ],
          ),
        ):InternetConnection();
      }
      return Container(
        child: Center(
          child: CircularProgressIndicator(),
        ),
      );
    });
  }

  void _saveDesignDialog() {
    // showGeneralDialog(
    //   context: context,
    //   barrierDismissible: false,
    //   barrierLabel:
    //   MaterialLocalizations.of(context).modalBarrierDismissLabel,
    //   pageBuilder: (BuildContext buildContext, Animation animation,
    //       Animation secondaryAnimation) {
    //     TextEditingController _controllerName=new TextEditingController();
    //     // return object of type Dialog
    //     return ;
    //   },
    // );
    showDialog(context: context, builder: (_)=>SaveDesign(userId: userModel!.data.first.UserId.toString(),
    categoryId: widget.categoryId.toString(),selectedAttribute: selectedAttribute!,usermesurment: usermesurment!,
    categoryName: widget.categoryName,yardage: yardage!,imagePath: widget.imagePath,));
  }
}


class SaveDesign extends StatefulWidget {
   final String userId,categoryId,selectedAttribute,usermesurment,categoryName,yardage,imagePath;
  const SaveDesign({Key? key,required this.userId,required this.categoryId,required this.selectedAttribute,
  required this.usermesurment,required this.categoryName,required this.yardage,required this.imagePath}) : super(key: key);

  @override
  State<SaveDesign> createState() => _SaveDesignState();
}

class _SaveDesignState extends State<SaveDesign> {
   var _controllerName=TextEditingController();

  @override
  Widget build(BuildContext context) {
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
            child: SingleChildScrollView(
                scrollDirection: Axis.vertical,
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: CommonSqureTextField(
                        maxLength: 30,
                        controller: _controllerName,
                        hintText: 'Enter Design Name',labelText: 'Design Name',limitCharlength: 30,
                        IsEnable: true,),
                    ),
                    Padding(
                      padding: const EdgeInsets.only(top:15.0),
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
                                  if(_controllerName.text.trim().isNotEmpty){
                                    showDialog(context: context, builder: (context)=>ProgressDialogManager(),
                                        barrierDismissible: false).then((value){
                                      print(value);
                                    });
                                    saveSketchAPI(_controllerName.text.trim());
                                  }else{
                                    Fluttertoast.showToast(msg: "Please Enter Design Name .");
                                  }
                                },style: ElevatedButton.styleFrom(primary: Colors.green,
                                onPrimary: Colors.green),
                                child: Text("Save Design",style: GoogleFonts.aBeeZee(
                                    fontSize: 16,color:
                                Colors.white),))
                          ],))
                      ],),
                    )
                  ],
                ),
              ),
          ),
        ),
      ),),
    );

  }
   void saveSketchAPI(String designName) async {
     try {
       var ApiUrls = Uri.parse(SaveSketch);
       var request = http.MultipartRequest("POST", ApiUrls);
       request.fields["user_id"] = userModel!.data.first.UserId.toString();
       request.fields["category_id"] = widget.categoryId.toString();
       request.fields["selected_attribute"] = widget.selectedAttribute!;
       request.fields["user_measurement"] = widget.usermesurment!;
       request.fields['category_name']=widget.categoryName;
       request.fields['design_name']=designName;
       request.fields['Yardage']=widget.yardage!;
       var img = await http.MultipartFile.fromPath("file", widget.imagePath);
       request.files.add(img);
       print(request.fields);
       var response = await request.send();
       var responsedata = await response.stream.toBytes();
       var responseString = String.fromCharCodes(responsedata);
       String res = responseString.replaceAll("{", "").replaceAll("}", "");
       List<String> res1 = res.split(",");
       var status = res1[0].split(":");
       var message = res1[1].split(":");
       if (status[1].replaceAll('"', '').replaceAll('"', '').toString() ==
           "success") {
         SharePreferenceManager.instance.IsSharePreferenceEaseDataRemove();
         SharePreferenceManager.instance.IsSharePreferenceYardageDataRemove();
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
         Fluttertoast.showToast(
             msg: message[1]
                 .replaceAll('"', '')
                 .replaceAll('"', '')
                 .toString()
                 .trim());
         Navigator.of(context).pop();
         Get.off(DashBoard());
       } else {
         Navigator.of(context).pop();
         Fluttertoast.showToast(
             msg: message[1]
                 .replaceAll('"', '')
                 .replaceAll('"', '')
                 .toString()
                 .trim());
         Navigator.of(context).pop();
       }
     } catch (error) {
       Navigator.of(context).pop();
       Fluttertoast.showToast(msg: error.toString());
     }
   }
}


