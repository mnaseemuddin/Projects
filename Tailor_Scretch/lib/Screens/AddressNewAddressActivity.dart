
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:provider/provider.dart';

import 'package:tailor/Screens/Activity.dart';
import 'package:tailor/ApiRepositary.dart';
import 'package:tailor/Support/ProgressDialogManagers.dart';
import 'package:tailor/Support/SharedPreferencesManager.dart';
import 'package:tailor/Support/UISupport.dart';
import 'package:tailor/Support/connectivity_provider.dart';
import 'package:tailor/Support/routes.dart';

import '../CommonUI/no_internet_connection.dart';

class AddNewAddressActivity extends StatefulWidget {
  final String FType,Type,AID,Name,MobileNo,State,City,ZipCode,
      Address,ADType,OrderType,apptitle;
  const AddNewAddressActivity({ Key? key,required this.FType,required this.Type,required this.AID,required this.Name,required this.MobileNo,
  required this.State,required this.City,required this.ZipCode,required this.Address,
    required this.ADType,required this.OrderType,required this.apptitle}) : super(key: key);

  @override
  _AddNewAddressActivityState createState() => _AddNewAddressActivityState();
}

class _AddNewAddressActivityState extends State<AddNewAddressActivity> {

  var addressTypeList = [
    "Please select address type",
    "Home",
    "Office",
    "Shop",
    "Others"
  ];
  String _selected = "Please select address type";

  var editAddressController=new TextEditingController();
  var editNameController=new TextEditingController();
  var editMobileController=new TextEditingController();
  var editStateController=new TextEditingController();
  var editCityController=new TextEditingController();
  var editZipCodeController=new TextEditingController();
  bool isValid(String data) => RegExp(r"^[6-9]\d{9}$").hasMatch(data);
  var _key=new GlobalKey<FormState>();

  @override
  void initState() {
    Provider.of<ConnectivityProvider>(context,listen: false).startMonitoring();
    if(widget.Type=="Editable"){
      editAddressController.text=widget.Address;
      editCityController.text=widget.City;
      editNameController.text=widget.Name;
      editStateController.text=widget.State;
      editZipCodeController.text=widget.ZipCode;
      editMobileController.text=widget.MobileNo;
    }
    super.initState();
  }

  void toastMessage(String msg){
    Fluttertoast.showToast(msg: msg,
      backgroundColor: Colors.black,textColor: Colors.white,gravity: ToastGravity.BOTTOM,
    );
  }

  @override
  Widget build(BuildContext context) {

    return Consumer<ConnectivityProvider>(builder: (consumerContext,model,child){
      if(model.isOnline!=null) {
        return model.isOnline ? Scaffold(
          appBar: AppBar(
            brightness: Brightness.dark,
            backgroundColor: UISupport.APP_PRIMARY_COLOR,
            title: Center(
                child: Text(
                  widget.apptitle,
                  style: GoogleFonts.aBeeZee(fontSize: 16, color: Colors.white),
                )),
            // leading: GestureDetector(
            //   onTap: () {
            //     Navigator.of(context).pop();
            //   },
            //   child: Container(
            //     child: Padding(
            //       padding: const EdgeInsets.all(18.0),
            //       // child: Image.asset(
            //       //   "drawable/ic_remove.png",
            //       //   height: 10,
            //       //   width: 10,
            //       //   color: Colors.white,
            //       // ),
            //     ),
            //   ),
            // ),
            actions: [
              Align(
                alignment: Alignment.centerRight,
                child: GestureDetector(
                  onTap: () {
                    if (_key.currentState!.validate()) {
                      if (_selected != "Please select address type") {
                        if (isValid(editMobileController.text)) {
                          if (validateInputLength(
                              editNameController.text.trim())) {
                            if (validateInputLength(
                                editAddressController.text)) {
                              if (validateInputLength(
                                  editCityController.text)) {
                                if (validateInputLength(
                                    editStateController.text)) {
                                  if (validateInputZipCodeLength(
                                      editZipCodeController.text)) {
                                    showDialog(context: context,
                                        builder: (context) =>
                                            ProgressDialogManager(),
                                        barrierDismissible: false).then((
                                        value) {
                                      print(value);
                                    });
                                    if (widget.Type == "NewAddress") {
                                      Map<String, String>mao = {
                                        'user_id': userModel!.data.first.UserId
                                            .toString(),
                                        'name': editNameController.text.trim(),
                                        'mobile': editMobileController.text
                                            .trim(),
                                        'address': editAddressController.text
                                            .trim(),
                                        'city': editCityController.text.trim(),
                                        'state': editStateController.text
                                            .trim(),
                                        'address_type': _selected,
                                        'zipcode': editZipCodeController.text
                                      };
                                      saveAddressAPI(mao).then((value) {
                                        Navigator.of(context).pop();
                                        if (value.status) {
                                          Navigator.of(context).pop();
                                          Fluttertoast.showToast(
                                              msg: value.message!);
                                          //Get.to(MyAddressActivity(FType: widget.FType,OrderType: widget.OrderType));
                                        } else {
                                          Navigator.of(context).pop();
                                          Fluttertoast.showToast(
                                              msg: value.message!);
                                        }
                                      });
                                    } else {
                                      Map<String, String>mao = {
                                        'tbl_user_address_id': widget.AID,
                                        'user_id': userModel!.data.first.UserId
                                            .toString(),
                                        'name': editNameController.text.trim(),
                                        'mobile': editMobileController.text
                                            .trim(),
                                        'address': editAddressController.text
                                            .trim(),
                                        'city': editCityController.text.trim(),
                                        'state': editStateController.text
                                            .trim(),
                                        'address_type': _selected,
                                        'zipcode': editZipCodeController.text
                                      };
                                      updateAddressAPI(mao).then((value) {
                                        Navigator.of(context).pop();
                                        if (value.status) {
                                          Navigator.of(context).pop();
                                          Fluttertoast.showToast(
                                              msg: value.message!);
                                          // Get.to(MyAddressActivity(FType: widget.FType,OrderType: widget.OrderType));
                                        } else {
                                          Navigator.of(context).pop();
                                          Fluttertoast.showToast(
                                              msg: value.message!);
                                        }
                                      });
                                    }
                                  } else {
                                    toastMessage(
                                        "Zipcode length should be greater than 5 digits.");
                                  }
                                } else {
                                  toastMessage(
                                      "State length must be between 3 to 30 characters.");
                                }
                              } else {
                                toastMessage(
                                    "City length must be between 3 to 30 characters.");
                              }
                            } else {
                              toastMessage(
                                  "Address length must be between 3 to 100 characters.");
                            }
                          } else {
                            toastMessage(
                                "Name length must be between 3 to 30 characters.");
                          }
                        } else {
                          Fluttertoast.showToast(
                            msg: "Please enter valid contact number.",
                            backgroundColor: Color(0xff080808),
                            textColor: Colors.red,
                            gravity: ToastGravity.BOTTOM,
                          );
                        }
                      } else {
                        Fluttertoast.showToast(
                          msg: "Please select address type.",
                          backgroundColor: Colors.black,
                          textColor: Colors.white,
                          gravity: ToastGravity.BOTTOM,);
                      }
                    }
                  },
                  child: Padding(
                    padding: const EdgeInsets.only(right: 8.0),
                    child: Text(
                      "Done",
                      style: GoogleFonts.aBeeZee(fontSize: 16, color: Colors
                          .white),
                    ),
                  ),
                ),
              )
            ],
          ),
          body: Form(
            key: _key,
            child: ListView(
              children: [
                Padding(
                  padding: const EdgeInsets.only(top: 8.0, left: 18, right: 18),
                  child: TextFormField(
                    style: GoogleFonts.aBeeZee(fontSize: 14),
                    controller: editNameController,
                    maxLength: 30,
                    textInputAction: TextInputAction.next,
                    inputFormatters: [
                      FilteringTextInputFormatter.allow(RegExp(
                          r'[A-Z]|[ ]|[a-z]')),
                      LengthLimitingTextInputFormatter(30),
                    ],
                    validator: (v) {
                      if (v!.trim()!.isEmpty) {
                        return "Please Enter Name .";
                      }
                      return null;
                    },
                    textAlign: TextAlign.left,
                    textCapitalization: TextCapitalization.sentences,
                    decoration: InputDecoration(
                      enabledBorder: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(9),
                        borderSide: BorderSide(
                          color: Color(0xff000000),
                          width: 0.70,
                        ),
                      ),
                      contentPadding: EdgeInsets.all(08.8),
                      errorStyle: GoogleFonts.aBeeZee(fontSize: 14.5, color:
                      Colors.redAccent, fontWeight: FontWeight.w600),
                      focusColor: Color(0xff000000),
                      border: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(5.0),
                          borderSide: BorderSide(color: Color(0xff000000))
                      ),
                      filled: true,
                      fillColor: Color(0xffffffff),
                      focusedBorder: OutlineInputBorder(
                        borderSide: BorderSide(color: Color(0xff000000)),
                        borderRadius: BorderRadius.circular(9.0),
                      ),
                      hintText: "Enter Name *",
                      hintStyle: GoogleFonts.aBeeZee(
                          fontSize: 14, color: Colors.grey[800]),
                    ),
                    // decoration: InputDecoration(
                    //     contentPadding: EdgeInsets.all(0.8),
                    //     enabledBorder: OutlineInputBorder(
                    //       borderRadius: BorderRadius.circular(9),
                    //       borderSide: BorderSide(
                    //         color: Color(0xfff4811e),
                    //         width: 0.70,
                    //       ),
                    //     ),
                    //     focusedBorder: OutlineInputBorder(
                    //       borderRadius: BorderRadius.circular(9),
                    //       borderSide: BorderSide(
                    //         color: Color(0xfff4811e),
                    //         width: 2.0,
                    //       ),
                    //     ),
                    //     hintText: "Enter name",
                    //     hintStyle: GoogleFonts.aBeeZee(
                    //         fontSize: 14, color: Colors.grey[500])),
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.only(top: 8.0, left: 18, right: 18),
                  child: Container(
                    decoration: BoxDecoration(
                        color: Colors.white,
                        border: Border.all(width: 1.0, color: Color(
                            0xff000000)),
                        borderRadius: BorderRadius.circular(9)),
                    child: Padding(
                      padding: const EdgeInsets.only(left: 8.0),
                      child: DropdownButtonHideUnderline(
                        child: DropdownButton(
                          value: _selected,
                          isExpanded: true,
                          dropdownColor: Colors.white,
                          icon: Icon(Icons.arrow_drop_down),
                          items: addressTypeList.map((String e) {
                            return DropdownMenuItem(
                                value: e,
                                child: Text(
                                  e,
                                  style: GoogleFonts.aBeeZee(
                                      color: Colors.grey[800], fontSize: 16),
                                ));
                          }).toList(),
                          onChanged: (String? newValue) {
                            setState(() {
                              _selected = newValue!;
                            });
                          },
                        ),
                      ),
                    ),
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.only(top: 8.0, left: 18, right: 18),
                  child: TextFormField(
                    style: GoogleFonts.aBeeZee(fontSize: 13),
                    controller: editAddressController,
                    maxLength: 100,
                    textCapitalization: TextCapitalization.sentences,
                    inputFormatters: [
                      LengthLimitingTextInputFormatter(100),
                    ],
                    textInputAction: TextInputAction.next,

                    ///keyboardType: TextInputType.multiline,
                    maxLines: 3,
                    validator: (v) {
                      if (v!.trim()!.isEmpty) {
                        return "Please Enter Address .";
                      }
                      return null;
                    },
                    textAlign: TextAlign.start,
                    decoration: InputDecoration(
                        errorStyle: GoogleFonts.aBeeZee(fontSize: 14.5, color:
                        Colors.redAccent, fontWeight: FontWeight.w600),
                        enabledBorder: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(9),
                          borderSide: BorderSide(
                            color: Color(0xff000000),
                            width: 0.70,
                          ),
                        ),
                        border: OutlineInputBorder(
                            borderRadius: BorderRadius.circular(5.0),
                            borderSide: BorderSide(color: Color(0xff000000))
                        ),
                        focusedBorder: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(9),
                          borderSide: BorderSide(
                            color: Color(0xff000000),
                            width: 2.0,
                          ),
                        ),
                        hintText: "Please Enter Address *",
                        hintStyle: GoogleFonts.aBeeZee(
                            fontSize: 12, color: Colors.grey[600])),
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.only(
                      top: 10.0, left: 18, right: 18),
                  child: TextFormField(
                    style: GoogleFonts.aBeeZee(fontSize: 14),
                    keyboardType: TextInputType.number,
                    textInputAction: TextInputAction.next,
                    maxLength: 10,
                    inputFormatters: [
                      LengthLimitingTextInputFormatter(10),
                      FilteringTextInputFormatter.digitsOnly
                    ],
                    controller: editMobileController,
                    validator: (v) {
                      if (v!.trim()!.isEmpty) {
                        return "Please Enter Mobile Number .";
                      }
                      return null;
                    },
                    textAlign: TextAlign.left,
                    decoration: InputDecoration(
                        errorStyle: GoogleFonts.aBeeZee(fontSize: 14.5, color:
                        Colors.redAccent, fontWeight: FontWeight.w600),
                        contentPadding: EdgeInsets.all(08.8),
                        enabledBorder: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(9),
                          borderSide: BorderSide(
                            color: Color(0xff000000),
                            width: 0.70,
                          ),
                        ),
                        border: OutlineInputBorder(
                            borderRadius: BorderRadius.circular(5.0),
                            borderSide: BorderSide(color: Color(0xff000000))
                        ),
                        focusedBorder: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(9),
                          borderSide: BorderSide(
                            color: Color(0xff000000),
                            width: 2.0,
                          ),
                        ),
                        hintText: "Enter Mobile Number *",
                        hintStyle: GoogleFonts.aBeeZee(
                            fontSize: 14, color: Colors.grey[600])),
                  ),
                ),


                Padding(
                  padding: const EdgeInsets.only(
                      top: 10.0, left: 18, right: 18),
                  child: TextFormField(
                    style: GoogleFonts.aBeeZee(fontSize: 14),
                    keyboardType: TextInputType.text,
                    maxLength: 25,
                    textCapitalization: TextCapitalization.sentences,
                    inputFormatters: [
                      FilteringTextInputFormatter.allow(RegExp(
                          r'[A-Z]|[ ]|[a-z]')),
                      LengthLimitingTextInputFormatter(25),
                    ],
                    textInputAction: TextInputAction.next,
                    controller: editCityController,
                    validator: (v) {
                      if (v!.trim()!.isEmpty) {
                        return "Please Enter City Name .";
                      }
                      return null;
                    },
                    textAlign: TextAlign.left,
                    decoration: InputDecoration(
                        errorStyle: GoogleFonts.aBeeZee(fontSize: 14.5, color:
                        Colors.redAccent, fontWeight: FontWeight.w600),
                        contentPadding: EdgeInsets.all(08.8),
                        enabledBorder: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(9),
                          borderSide: BorderSide(
                            color: Color(0xff000000),
                            width: 0.70,
                          ),
                        ),
                        border: OutlineInputBorder(
                            borderRadius: BorderRadius.circular(5.0),
                            borderSide: BorderSide(color: Color(0xff000000))
                        ),
                        focusedBorder: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(9),
                          borderSide: BorderSide(
                            color: Color(0xff000000),
                            width: 2.0,
                          ),
                        ),
                        hintText: "Enter City Name *",
                        hintStyle: GoogleFonts.aBeeZee(
                            fontSize: 14, color: Colors.grey[600])),
                  ),
                ),

                Padding(
                  padding: const EdgeInsets.only(
                      top: 10.0, left: 18, right: 18),
                  child: TextFormField(
                    style: GoogleFonts.aBeeZee(fontSize: 14),
                    keyboardType: TextInputType.text,
                    maxLength: 25,
                    inputFormatters: [
                      FilteringTextInputFormatter.allow(RegExp(
                          r'[A-Z]|[ ]|[a-z]')),
                      LengthLimitingTextInputFormatter(25),
                    ],
                    controller: editStateController,
                    textCapitalization: TextCapitalization.sentences,
                    textInputAction: TextInputAction.next,
                    validator: (v) {
                      if (v!.trim()!.isEmpty) {
                        return "Please Enter State Name .";
                      }
                      return null;
                    },
                    textAlign: TextAlign.left,
                    decoration: InputDecoration(
                        errorStyle: GoogleFonts.aBeeZee(fontSize: 14.5, color:
                        Colors.redAccent, fontWeight: FontWeight.w600),
                        contentPadding: EdgeInsets.all(08.8),
                        enabledBorder: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(9),
                          borderSide: BorderSide(
                            color: Color(0xff000000),
                            width: 0.70,
                          ),
                        ),
                        border: OutlineInputBorder(
                            borderRadius: BorderRadius.circular(5.0),
                            borderSide: BorderSide(color: Color(0xff000000))
                        ),
                        focusedBorder: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(9),
                          borderSide: BorderSide(
                            color: Color(0xff000000),
                            width: 2.0,
                          ),
                        ),
                        hintText: "Enter State Name *",
                        hintStyle: GoogleFonts.aBeeZee(
                            fontSize: 14, color: Colors.grey[600])),
                  ),
                ),

                Padding(
                  padding: const EdgeInsets.only(
                      top: 10.0, left: 18, right: 18),
                  child: TextFormField(
                    style: GoogleFonts.aBeeZee(fontSize: 14),
                    keyboardType: TextInputType.number,
                    inputFormatters: [
                      LengthLimitingTextInputFormatter(6),
                      FilteringTextInputFormatter.digitsOnly
                    ],
                    controller: editZipCodeController,
                    validator: (v) {
                      if (v!.trim()!.isEmpty) {
                        return "Please Enter Zipcode .";
                      }
                      return null;
                    },
                    textAlign: TextAlign.left,
                    decoration: InputDecoration(
                        errorStyle: GoogleFonts.aBeeZee(fontSize: 14.5, color:
                        Colors.redAccent, fontWeight: FontWeight.w600),
                        contentPadding: EdgeInsets.all(08.8),
                        enabledBorder: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(9),
                          borderSide: BorderSide(
                            color: Color(0xff000000),
                            width: 0.70,
                          ),
                        ),
                        border: OutlineInputBorder(
                            borderRadius: BorderRadius.circular(5.0),
                            borderSide: BorderSide(color: Color(0xff000000))
                        ),
                        focusedBorder: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(9),
                          borderSide: BorderSide(
                            color: Color(0xff000000),
                            width: 2.0,
                          ),
                        ),
                        hintText: "Enter Zipcode *",
                        hintStyle: GoogleFonts.aBeeZee(
                            fontSize: 14, color: Colors.grey[600])),
                  ),
                ),


              ],
            ),
          ),
        ) : InternetConnection();
      }
      return Container(
        child: Center(
          child: CircularProgressIndicator(),
        ),
      );
    });
  }
  bool validateInputLength(String data){
    return data.length > 2;
  }

  bool validateInputZipCodeLength(String data){
    return data.length > 5;
  }
}

