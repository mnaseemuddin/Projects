import 'dart:convert';
import 'dart:io';
import 'dart:math';
import 'package:http/http.dart' as http;
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:intl/intl.dart';
import 'package:provider/provider.dart';
import 'package:tailor/CommonUI/no_internet_connection.dart';
import 'package:tailor/Screens/CartActivity.dart';
import 'package:tailor/Screens/MyAddressActivity.dart';
import 'package:tailor/Screens/MyOrderActivity.dart';
import 'package:tailor/Screens/MyProfileActivity.dart';
import 'package:tailor/Screens/OrderCoinfirmedActivity.dart';
import 'package:tailor/Auth/SignIn.dart';
import 'package:tailor/Model/ProfileModel.dart';
import 'package:tailor/Model/SSketch.dart';
import 'package:tailor/SHActivities/scketch_dress.dart';
import 'package:tailor/Support/ProgressDialogManagers.dart';
import 'package:tailor/Support/SharedPreferencesManager.dart';
import 'package:tailor/Support/UISupport.dart';
import 'package:tailor/Support/connectivity_provider.dart';
import 'package:tailor/Support/routes.dart';
import '../ApiRepositary.dart';

import 'ChooseSketchesActivity.dart';

class DashBoard extends StatefulWidget {
  const DashBoard({Key? key}) : super(key: key);

  @override
  _DashBoardState createState() => _DashBoardState();
}

class _DashBoardState extends State<DashBoard> {
  SSketch? _sketch;

  ProfileModel? _profileModel;

  @override
  void initState() {
    Provider.of<ConnectivityProvider>(context,listen: false).startMonitoring();
    getSaveSketchAPI(userModel!.data.first.UserId).then((value) {
      showDialog(
          context: context,
          builder: (context) =>
              ProgressDialogManager(),
          barrierDismissible: false)
          .then((value) {
        print(value);
      });
      setState(() {
        if (value.status) {
          _sketch = value.data;
          navDialogDismiss(context);
        }else{
          _sketch=null;
          navDialogDismiss(context);
        }
      });
    });

    getUserdetails(userModel!.data.first.UserId).then((value) {
      setState(() {
        if (value.status) {
          _profileModel = value.data;
        }else{
          _profileModel=null;
        }
      });
    });

    super.initState();
  }



  @override
  Widget build(BuildContext context) {
    Size size=MediaQuery.of(context).size;
    return Consumer<ConnectivityProvider>(
      builder: (consumerContext,model,child){
        if(model.isOnline!=null){
          return model.isOnline?WillPopScope(
            onWillPop: () {
              showDialog(
                context: context,
                builder: (BuildContext context) {
                  // return object of type Dialog
                  return AlertDialog(
                    title: Text("Exit?"),
                    content: new Text("Are you sure you want to exit?"),
                    actions: <Widget>[
                      new TextButton(
                        child: new Text(
                          "CANCEL",
                          style: TextStyle(
                            color: Colors.red,
                            fontFamily: 'Helvetica Neue',
                            fontSize: 14.899999618530273,
                          ),
                        ),
                        onPressed: () {
                          Navigator.of(context).pop();
                        },
                      ),

                      new TextButton(
                        child: new Text(
                          "OK",
                          style: TextStyle(
                            color: Colors.red,
                            fontFamily: 'Helvetica Neue',
                            fontSize: 14.899999618530273,
                          ),
                        ),
                        onPressed: () {
                          exit(0);
                        },
                      ),
                      // usually buttons at the bottom of the dialog
                    ],
                  );
                },
              );
              return Future.value(false);
            },
            child: Scaffold(
              appBar: AppBar(
                title: Text('My Designs'),
                brightness: Brightness.dark,
                backgroundColor: UISupport.APP_PRIMARY_COLOR,
                leading: Builder(
                  builder: (context) => IconButton(
                    icon: Icon(
                      Icons.menu_rounded,
                      color: Colors.white,
                    ),
                    onPressed: () => Scaffold.of(context).openDrawer(),
                  ),
                ),
              ),
              drawer: _profileModel == null
                  ? CircularProgressIndicator()
                  : navigationDrawerUI(),
              body: _sketch == null
                  ? Center(child: CircularProgressIndicator(strokeWidth: 5.5,
                color: UISupport.APP_PRIMARY_COLOR,))
                  : Column(
                children: [
                  _sketch!.data.length==0?Expanded(flex: 80,
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Image.asset(
                          "assets/nodesign.png",
                          height: 200,width: double.infinity,),
                        Padding(
                          padding: const EdgeInsets.only(top:15.0),
                          child: Text('No Design!'),
                        ),
                        Padding(
                          padding: const EdgeInsets.only(left:40.0,right: 40,top: 20),
                          child: Text("Looks like you haven't added any design to your My Design yet."),
                        ),
                      ],),
                  ):Expanded(
                    flex: 80,
                    child: GridView.builder(
                        gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                            mainAxisSpacing: 5,
                            crossAxisSpacing: 4,
                            mainAxisExtent: 320,
                            crossAxisCount: 2),
                        shrinkWrap: true,
                        itemCount: _sketch!.data.length,
                        itemBuilder: (context, index) {
                          SDatum _SSketch = _sketch!.data.elementAt(index);
                          return Padding(
                            padding: const EdgeInsets.all(4.0),
                            child: Container(
                              height: 200,
                              decoration: new BoxDecoration(
                                  color: Colors.white,
                                  borderRadius: BorderRadius.circular(8),
                                  border: Border.all(
                                      width: 1, color: Color(0xffdedede))),
                              child: Column(
                                children: [
                                  GestureDetector(
                                    onTap: () {
                                      saveSktechoptionDialog(
                                          _SSketch.categoryId,
                                          _SSketch.image,
                                          _SSketch.tblUserDesignSaveId,
                                          _SSketch.image,
                                          _SSketch.selectedAttribute,
                                          _SSketch.userMeasurement,
                                          _SSketch.pro_image,
                                          _SSketch.categoryName,
                                          _SSketch.tblUserDesignSaveId,
                                          _SSketch.yardage);
                                    },
                                    child: Padding(
                                      padding: const EdgeInsets.only(top: 8.0),
                                      child: Container(
                                        height: 252,
                                        child: FadeInImage.assetNetwork(
                                          placeholder: 'assets/loading.gif',
                                          image: _SSketch.image,
                                          alignment: FractionalOffset.topCenter,),
                                        // decoration: new BoxDecoration(
                                        //     image: new DecorationImage(
                                        //   image:
                                        //       new NetworkImage(_SSketch.image),
                                        //   fit: BoxFit.fitWidth,
                                        //   scale: 4.0,
                                        //   alignment: FractionalOffset.topCenter,
                                        // )),
                                      ),
                                    ),
                                  ),
                                  Padding(
                                    padding: const EdgeInsets.only(top: 10.0),
                                    child: Text(_SSketch.design_name,overflow: TextOverflow.ellipsis,
                                        maxLines: 1,
                                        style: GoogleFonts.aBeeZee(
                                            fontSize: 15.2,
                                            color: Colors.black)),
                                  )
                                ],
                              ),
                            ),
                          );
                        }),
                  ),
                  Expanded(
                    flex: 8,
                    child: GestureDetector(
                      onTap: () {
                        Get.to(ChooseSketchActivity());
                      },
                      child: Padding(
                        padding: const EdgeInsets.only(
                            left: 18.0, right: 18, bottom: 10, top: 0),
                        child: Container(
                          height: 50,
                          width: double.infinity,
                          decoration: BoxDecoration(
                              borderRadius: BorderRadius.circular(8),
                              color: UISupport.APP_PRIMARY_COLOR),
                          child: Row(
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              Padding(
                                padding: const EdgeInsets.only(left: 15.0),
                                child: Text(
                                  "Design Now",
                                  style: GoogleFonts.actor(
                                    color: Colors.white,
                                    fontSize: 17.5,
                                    fontWeight: FontWeight.w800,
                                  ),
                                ),
                              ),
                              Padding(
                                padding: const EdgeInsets.only(right: 20.0),
                                child: Icon(
                                  Icons.arrow_forward_ios,
                                  size: 20,
                                  color: Colors.white,
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

  Widget navigationDrawerUI() {
    return Drawer(
      child: Container(
        color: Colors.white,
        child: ListView(
          children: [
            Container(
              height: 120,
              color: Colors.white,
              child: Row(
                mainAxisAlignment: MainAxisAlignment.start,
                children: [
                  Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: GestureDetector(
                      onTap: () {
                        Navigator.of(context)
                            .push(new MaterialPageRoute(
                                builder: (_) => MyProfileActivity()))
                            .then((value) => {
                                  getUserdetails(userModel!.data.first.UserId)
                                      .then((value) {
                                    setState(() {
                                      if (value.status) {
                                        _profileModel = value.data;
                                      }else{
                                        _profileModel = null;
                                      }
                                    });
                                  })
                                });
                      },
                      child: Container(
                          height: 70,
                          width: 70,
                          decoration: BoxDecoration(
                              borderRadius: BorderRadius.circular(100),
                              border: Border.all(width: 3, color: Colors.grey)),
                          child: Padding(
                            padding: const EdgeInsets.all(0.0),
                            child: ClipRRect(
                              borderRadius: BorderRadius.circular(100),
                              child: FadeInImage.assetNetwork(
                                image:_profileModel!.data.first.userImage,
                                placeholder: 'assets/loading.gif',
                                height: 90,
                                width: 90,
                                fit: BoxFit.cover,
                              ),
                            ),
                          )),
                    ),
                  ),
                  Flexible(
                    child: Text(
                      _profileModel!.data.first.fName +
                          " " +
                          _profileModel!.data.first.lName,
                      softWrap: false,
                      overflow: TextOverflow.ellipsis,
                      maxLines: 2,
                      style: GoogleFonts.aBeeZee(
                        color: Colors.black,
                        fontSize: 16,
                      ),
                      textAlign: TextAlign.center,
                    ),
                  )
                ],
              ),
            ),

            Padding(
              padding: const EdgeInsets.only(left: 10.0, top: 15, bottom: 5),
              child: GestureDetector(
                onTap: () {
                  Navigator.of(context)
                      .push(new MaterialPageRoute(
                      builder: (_) => MyProfileActivity()))
                      .then((value) => {
                    getUserdetails(userModel!.data.first.UserId)
                        .then((value) {
                      setState(() {
                        if (value.status) {
                          _profileModel = value.data;
                        }else{
                          _profileModel = null;
                        }
                      });
                    })
                  });
                },
                child: Container(
                  height: 40,
                  child: Row(
                    children: [
                      Image.asset(
                        "drawable/profile.png",
                        height: 25,
                        width: 25,
                        color: Colors.black,
                      ),
                      Padding(
                        padding: const EdgeInsets.only(left: 15.0),
                        child: Text(
                          "My Profile",
                          style: GoogleFonts.aBeeZee(
                              color: Colors.black, fontSize: 17),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),

            Padding(
              padding: const EdgeInsets.only(left: 10.0, top: 0, bottom: 5),
              child: GestureDetector(
                onTap: () {
                  Get.to(MyOrderActivity());
                },
                child: Container(
                  height: 40,
                  child: Row(
                    children: [
                      Image.asset(
                        "drawable/shipping.png",
                        height: 25,
                        width: 25,
                        color: Colors.black,
                      ),
                      Padding(
                        padding: const EdgeInsets.only(left: 15.0),
                        child: Text(
                          "My Orders",
                          style: GoogleFonts.aBeeZee(
                              color: Colors.black, fontSize: 17),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),

            Padding(
              padding: const EdgeInsets.only(left: 10.0, top: 0, bottom: 5),
              child: GestureDetector(
                onTap: () {
                  Get.to(MyAddressActivity(
                    FType: "OnDashBoard",
                    OrderType: '',
                  ));
                },
                child: Container(
                  height: 40,
                  child: Row(
                    children: [
                      Image.asset(
                        "drawable/address.png",
                        height: 25,
                        width: 25,
                        color: Colors.black,
                      ),
                      Padding(
                        padding: const EdgeInsets.only(left: 15.0),
                        child: Text(
                          "My Address",
                          style: GoogleFonts.aBeeZee(
                              color: Colors.black, fontSize: 17),
                        ),
                      )
                    ],
                  ),
                ),
              ),
            ),

            Padding(
              padding: const EdgeInsets.only(left: 10.0, top: 0, bottom: 5),
              child: GestureDetector(
                onTap: () {
                  Navigator.of(context).pop();
                },
                child: Container(
                  height: 40,
                  child: Row(
                    children: [
                      Image.asset(
                        "assets/diskette.png",
                        height: 20,
                        width: 25,
                        color: Colors.black,
                        fit: BoxFit.fitHeight,
                      ),
                      Padding(
                        padding: const EdgeInsets.only(left: 15.0),
                        child: Text(
                          "My Designs",
                          style: GoogleFonts.aBeeZee(
                              color: Colors.black, fontSize: 17),
                        ),
                      )
                    ],
                  ),
                ),
              ),
            ),
            Padding(
              padding: const EdgeInsets.only(left: 10.0, top: 0, bottom: 5),
              child: GestureDetector(
                onTap: () {
                  showDialog(
                    context: context,
                    builder: (BuildContext context) {
                      // return object of type Dialog
                      return AlertDialog(
                        content: new Text("Do you want to logout?"),
                        actions: <Widget>[
                          new TextButton(
                            child: new Text(
                              "CANCEL",
                              style: TextStyle(
                                color: Colors.black,
                                fontFamily: 'Helvetica Neue',
                                fontSize: 14.899999618530273,
                              ),
                            ),
                            onPressed: () {
                              Navigator.of(context).pop();
                            },
                          ),

                          new TextButton(
                            child: new Text(
                              "OK",
                              style: TextStyle(
                                color: Colors.black,
                                fontFamily: 'Helvetica Neue',
                                fontSize: 14.899999618530273,
                              ),
                            ),
                            onPressed: () {
                              setEmailId(null)
                                  .then((value) => Get.offAll(SignIn()));
                              ;
                            },
                          ),
                          // usually buttons at the bottom of the dialog
                        ],
                      );
                    },
                  );
                },
                child: Container(
                  height: 40,
                  child: Row(
                    children: [
                      Padding(
                        padding: const EdgeInsets.only(left: 0.0),
                        child: Icon(Icons.logout, size: 25),
                      ),
                      Padding(
                        padding: const EdgeInsets.only(left: 15.0),
                        child: Text(
                          "Logout",
                          style: GoogleFonts.aBeeZee(
                              color: Colors.black, fontSize: 17),
                        ),
                      )
                    ],
                  ),
                ),
              ),
            )
          ],
        ),
      ),
    );
  }

  void saveSktechoptionDialog(
      int categoryId,
      String imgFile,
      int tblUserDesignSaveId,
      String imgUrl,
      String selectedAttribute,
      String usermesurment,
      String imgName,
      String categoryName,int tbleusersavedesignId,String yardage) {
    showGeneralDialog(
      context: context,
      barrierDismissible: false,
      barrierLabel: MaterialLocalizations.of(context).modalBarrierDismissLabel,
      pageBuilder: (BuildContext buildContext, Animation animation,
          Animation secondaryAnimation) {
        // return object of type Dialog
        return Padding(
          padding: const EdgeInsets.all(8.0),
          child: Center(
            child: Material(
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
                      SizedBox(
                        height: 30,
                        child: Row(
                          children: [
                            Spacer(),
                            Padding(
                              padding: const EdgeInsets.only(bottom: 8.0),
                              child: IconButton(
                                  onPressed: () {
                                    Navigator.of(context).pop();
                                  },
                                  icon: Icon(Icons.cancel)),
                            )
                          ],
                        ),
                      ),
                      Stack(
                        children: [
                          Padding(
                              padding: const EdgeInsets.only(
                                  left: 0.0, right: 0, top: 0),
                              // child: Image.network(
                              //   imgUrl,
                              //   height: 400,
                              //   width: double.infinity,
                              //   fit: BoxFit.fitHeight,
                              //   scale: 30,
                              // )),
                          child: FadeInImage.assetNetwork(
                            fit: BoxFit.fitHeight,
                              height: 400,
                            placeholder: 'assets/loading.gif',
                            image: imgUrl, alignment: FractionalOffset.topCenter,),
                          ),
                          Column(
                            mainAxisAlignment: MainAxisAlignment.end,
                            children: [
                              Align(
                                alignment: Alignment.bottomRight,
                                child: Padding(
                                  padding: const EdgeInsets.only(top: 400.0),
                                  child: SizedBox(
                                      width: 220,
                                      height: 46,
                                      child: ElevatedButton.icon(
                                          icon: Image.asset(
                                              'assets/redesign.png',
                                              height: 25,
                                              width: 25),
                                          onPressed: () {
                                            Get.to(SketchDress(
                                              categoryId: categoryId,
                                              image: imgFile,
                                              categoryName: categoryName,
                                            ));
                                          },
                                          style: ElevatedButton.styleFrom(
                                            primary: Colors.grey[200],
                                            onPrimary: Colors.grey[200],
                                          ),
                                          label: Text(
                                            "Re-Design",
                                            style: GoogleFonts.aBeeZee(
                                                fontSize: 14,
                                                color: Colors.black),
                                          ))),
                                ),
                              ),
                              Padding(padding: EdgeInsets.only(top: 05)),
                              Align(
                                alignment: Alignment.bottomRight,
                                child: SizedBox(
                                  width: 220,
                                  height: 46,
                                  child: ElevatedButton.icon(
                                      icon: Image.asset(
                                        'assets/delete.png',
                                        height: 25,
                                        width: 25,
                                        color: Colors.white,
                                      ),
                                      onPressed: () {
                                        deleteConfirmationDialog(tblUserDesignSaveId);
                                      },
                                      style: ElevatedButton.styleFrom(
                                          primary: Colors.red,
                                          onPrimary: Colors.red),
                                      label: Text(
                                        "Delete Design",
                                        style: GoogleFonts.aBeeZee(
                                            fontSize: 14, color: Colors.white),
                                      )),
                                ),
                              ),
                              Padding(padding: EdgeInsets.only(top: 05)),
                              Align(
                                alignment: Alignment.bottomRight,
                                child: SizedBox(
                                  height: 46,
                                  width: 220,
                                  child: ElevatedButton.icon(
                                      icon: Image.asset(
                                        'assets/order.png',
                                        height: 28,
                                        width: 28,
                                        color: Colors.white,
                                      ),
                                      onPressed: () {
                                        SharePreferenceManager.instance
                                            .setSelectedAttribute(
                                                "selectedAttribute",
                                                selectedAttribute.toString());
                                        SharePreferenceManager.instance
                                            .setCategoryName(
                                                "CategoryName", categoryName);
                                        SharePreferenceManager.instance
                                            .setCategoryId("CategoryId",
                                                categoryId.toString());
                                        SharePreferenceManager.instance
                                            .setImagePath("ImagePath", imgName);
                                        SharePreferenceManager.instance
                                            .setImageUrl("ImgUrl", imgUrl);
                                        SharePreferenceManager.instance.setDesignID("DesignId",
                                            tblUserDesignSaveId.toString());
                                        SharePreferenceManager.instance.setEaseData('Ease',usermesurment);
                                         SharePreferenceManager.instance.setYardageData(
                                           "Yardage",yardage);
                                        // Get.to(MyAddressActivity(FType: "Checkout",OrderType: 'onDashBoard',));
                                        Navigator.of(context)
                                            .push(new MaterialPageRoute(
                                                builder: (_) =>
                                                    MyAddressActivity(
                                                      FType: "Checkout",
                                                      OrderType: 'onDashBoard',
                                                    )))
                                            .then((value) {
                                          Navigator.of(context).pop();
                                        });
                                        // var now = new DateTime.now();
                                        // var formatter = new DateFormat('dd-MMM-yyyy HH:mm:ss');
                                        // String formattedDate = formatter.format(now);
                                        // placeOrderAPI(formattedDate, usermesurment!,
                                        //     selectedAttribute,imgName,categoryId);
                                      },
                                      style: ElevatedButton.styleFrom(
                                          primary: Colors.green,
                                          onPrimary: Colors.green),
                                      label: Text(
                                        "Place Order",
                                        style: GoogleFonts.aBeeZee(
                                            fontSize: 14, color: Colors.white),
                                      )),
                                ),
                              )
                            ],
                          )
                        ],
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ),
        );
      },
    );
  }

  void deleteConfirmationDialog(int tblUserDesignSaveId) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        // return object of type Dialog
        return AlertDialog(
          title: Text("Confirm Delete?"),
          content: new Text("Are you sure you want to delete this design?"),
          actions: <Widget>[
            new TextButton(
              child: new Text(
                "CANCEL",
                style: TextStyle(
                  color: Colors.red,
                  fontFamily: 'Helvetica Neue',
                  fontSize: 14.899999618530273,
                ),
              ),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),

            new TextButton(
              child: new Text(
                "OK",
                style: TextStyle(
                  color: Colors.red,
                  fontFamily: 'Helvetica Neue',
                  fontSize: 14.899999618530273,
                ),
              ),
              onPressed: () {
                Navigator.of(context).pop();
                deleteAddressMethod(tblUserDesignSaveId);
              },
            ),
            // usually buttons at the bottom of the dialog
          ],
        );
      },
    );
  }

  void deleteAddressMethod(int tblUserDesignSaveId) async{
   await deleteSketch(
        userModel!.data.first.UserId,
        tblUserDesignSaveId)
        .then((value) {
      if (value.status) {
        Fluttertoast.showToast(
            msg: value.message!,gravity: ToastGravity.CENTER);
        Navigator.of(context).pop();
        initState();
      } else {
        Navigator.of(context).pop();
        Fluttertoast.showToast(
            msg: value.message!,gravity: ToastGravity.CENTER);
      }
    });
  }
}
