
import 'package:appgallery/CommonUI/commonwidget.dart';
import 'package:appgallery/Model/allconsoleappsmodel.dart';
import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

import '../../../CommonUI/apptextview.dart';
import '../../../Resources/color.dart';
import '../../../Resources/constants.dart';


class ReleasesVersionHistory extends StatefulWidget {

   final String appName;
   List<ApkInformation>apkInfoList=[];

   ReleasesVersionHistory({Key? key,required this.appName,
     required this.apkInfoList}) : super(key:key);

  @override
  _ReleasesVersionHistoryState createState() => _ReleasesVersionHistoryState();
}

class _ReleasesVersionHistoryState extends State<ReleasesVersionHistory> {


 bool visibleDeactivatedUI=false;
 String lastUpdate="",versionCode="";
  @override
  void initState() {
    super.initState();
    setState(() {

      if(widget.apkInfoList.length>1){
        ApkInformation apkInfo= widget.apkInfoList.elementAt(widget.apkInfoList.length-2);
        lastUpdate=apkInfo.lastUpdate;
        versionCode=apkInfo.versionCode;
        visibleDeactivatedUI=true;
      }else{
        visibleDeactivatedUI=false;
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: appGreyColor,
      appBar: AppBar(
        iconTheme: const IconThemeData(
          color: Colors.black, //change your color here
        ),
        backgroundColor: appGreyColor,
        title: Column(
          children: [

            Align(
              alignment: Alignment.centerLeft,
              child: HeadingText(
                  title:widget.apkInfoList.last.versionName,
                  fontSize: 16.0, color: appBlackColor),
            ),

            Align(
              alignment: Alignment.centerLeft,
              child: Padding(
                padding: const EdgeInsets.only(top:3.0),
                child: HeadingText(
                  title:widget.appName,
                 fontSize: 14.0, color: Colors.black54),
              ),
            ),

          ],
        ),
      ),
      body: Column(children: [
        productionUI(),
        rollOutHistoryUI(),
        apkAddedUI(),
        Visibility(
          visible: visibleDeactivatedUI,
          child: apkdeactivatedUI(),
        ),
      ],),
    );
  }

  productionUI() {
    return Padding(
      padding: const EdgeInsets.all(12.0),
      child: ContainerBg(
        height: 88,
        color: appWhiteColor,
        width: double.infinity,
        circular: 5,
        child: Padding(
          padding: const EdgeInsets.only(left: 15.0, top: 15, bottom: 15),
          child: GestureDetector(
            onTap: (){
            },
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Expanded(
                  flex: 12,
                  child: Container(
                      height: 50,
                      decoration: BoxDecoration(
                        color: Colors.green,
                        borderRadius: BorderRadius.circular(17),
                      ),
                      child: Padding(
                        padding: const EdgeInsets.all(10.0),
                        child: Image.asset(
                          assetsPlaystoreImg,
                          height: 10,
                          width: 10,
                          color: appWhiteColor,
                        ),
                      )),
                ),
                Expanded(
                  flex: 80,
                  child: Padding(
                    padding: const EdgeInsets.fromLTRB(20.0,8,8,8),
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Row(
                          children: [
                            HeadingText(
                              title: 'Production',
                              fontSize: 15,
                            ),
                            const Spacer(),
                            HeadingText(
                              title: 'Full roll-out',
                              fontSize: 14.5,
                              color: Colors.grey[700],
                            )
                          ],
                        ),
                        Padding(
                          padding: const EdgeInsets.only(top:4.0),
                          child: Align(
                              alignment: Alignment.centerLeft,
                              child: Row(
                                children: [
                                  HeadingText(
                                    fontSize: 13,
                                    title: '1 APK',
                                    maxLines: 1,
                                    color: Colors.grey[700],
                                  ),
                                  const Spacer(),
                                  HeadingText(
                                    fontSize: 13,
                                    title:widget.apkInfoList.last.lastUpdate,
                                    maxLines: 1,
                                    color: Colors.grey[700],
                                  ),
                                ],
                              )),
                        )
                      ],
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  rollOutHistoryUI() {
    return Padding(
      padding: const EdgeInsets.fromLTRB(12.0,5,12,0),
      child: ContainerBg(child: Column(children: [
        Align(
          alignment: Alignment.centerLeft,
          child: Padding(
            padding: const EdgeInsets.fromLTRB(8.0,20,8,8),
            child: HeadingText1(title: "Roll-out history"),
          ),
        ),

        Row(children: [

          Expanded(
              child: Padding(
                padding: const EdgeInsets.all(8.0),
                child: HeadingText1(title: "Update"),
              )),
          Expanded(
              child: Padding(
                padding: const EdgeInsets.all(8.0),
                child: HeadingText1(title: "Status"),
              ))

        ],),
        Row(children: [

          Expanded(
            child: Padding(
              padding: const EdgeInsets.only(left:8.0),
              child: HeadingText(
                fontSize: 13,
                title:widget.apkInfoList.last.lastUpdate,
                maxLines: 1,
                color: Colors.grey[700],
              ),
            ),
          ),

          Expanded(
              child: Padding(
                padding: const EdgeInsets.all(8.0),
                child: HeadingText1(title: "Roll-out started at 100.00%",
                  color: Colors.grey[700],),
              ))

        ],)


      ],), height: 125, circular: 8,
          color: appWhiteColor, width: double.infinity),
    );
  }
  apkAddedUI() {
    return Padding(
      padding: const EdgeInsets.fromLTRB(12.0,5,12,0),
      child: ContainerBg(child: Column(children: [
        Align(
          alignment: Alignment.centerLeft,
          child: Padding(
            padding: const EdgeInsets.fromLTRB(8.0,20,8,8),
            child: HeadingText1(title: "APK added (1)"),
          ),
        ),

        Row(children: [

          Expanded(
              child: Padding(
                padding: const EdgeInsets.all(8.0),
                child: HeadingText1(title: "Version code"),
              )),
          Expanded(
              child: Padding(
                padding: const EdgeInsets.all(8.0),
                child: HeadingText1(title: "Uploaded"),
              ))

        ],),
        Row(children: [

          Expanded(
              child: Padding(
                padding: const EdgeInsets.all(8.0),
                child: HeadingText1(title: widget.apkInfoList.last.versionCode,
                  color: Colors.grey[700],),
              )),
          Expanded(
            child: Padding(
              padding: const EdgeInsets.only(left:8.0),
              child: HeadingText(
                fontSize: 13,
                title:widget.apkInfoList.last.lastUpdate,
                maxLines: 1,
                color: Colors.grey[700],
              ),
            ),
          ),

        ],)


      ],), height: 125, circular: 8,
          color: appWhiteColor, width: double.infinity),
    );
  }
  apkdeactivatedUI() {
    return Padding(
      padding: const EdgeInsets.fromLTRB(12.0,5,12,0),
      child: ContainerBg(child: Column(children: [
        Align(
          alignment: Alignment.centerLeft,
          child: Padding(
            padding: const EdgeInsets.fromLTRB(8.0,20,8,8),
            child: HeadingText1(title: "APK deactivated (1)"),
          ),
        ),

        Row(children: [

          Expanded(
              child: Padding(
                padding: const EdgeInsets.all(8.0),
                child: HeadingText1(title: "Version code"),
              )),
          Expanded(
              child: Padding(
                padding: const EdgeInsets.all(8.0),
                child: HeadingText1(title: "Uploaded"),
              ))

        ],),
        Row(children: [

          Expanded(
              child: Padding(
                padding: const EdgeInsets.all(8.0),
                child: HeadingText1(title: versionCode.toString(),
                  color: Colors.grey[700],),
              )),
          Expanded(
            child: Padding(
              padding: const EdgeInsets.only(left:8.0),
              child: HeadingText(
                fontSize: 13,
                title:lastUpdate,
                maxLines: 1,
                color: Colors.grey[700],
              ),
            ),
          ),

        ],)


      ],), height: 125, circular: 8,
          color: appWhiteColor, width: double.infinity),
    );
  }

}
