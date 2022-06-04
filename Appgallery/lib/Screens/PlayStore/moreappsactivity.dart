



import 'dart:async';
import 'dart:convert';
import 'package:appgallery/Controllers/moreappscontroller.dart';
import 'package:appgallery/Resources/color.dart';
import 'package:appgallery/Resources/constants.dart';
import 'package:appgallery/Utils/loadingoverlay.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:http/http.dart'as http;
import '../../Utils/routes.dart';
import '../../apis/apirepositary.dart';
import 'appsdetailsactivity.dart';

class MoreAppsActivity extends StatefulWidget {
  final String appBarName;
  const MoreAppsActivity({Key? key,required this.appBarName}) : super(key: key);

  @override
  _MoreAppsActivityState createState() => _MoreAppsActivityState();
}

class _MoreAppsActivityState extends State<MoreAppsActivity> {

  MoreAppsController controller=Get.put(MoreAppsController());
  List? data,data1;

  @override
  void initState() {
    loadAllApps();
    super.initState();
  }


  Future<String> loadAllApps() async {
    final jsonResponse=await http.post(Uri.parse(allPlayStoreAppsUrl));
    setState(() {
      data=json.decode(jsonResponse.body);
      print(data);
    });
    return 'Success';
  }
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xfff1f3f5),
      appBar: PreferredSize(preferredSize: const Size.fromHeight(80),
        child: Padding(
          padding: const EdgeInsets.only(top:50.0,left: 10,bottom: 15),
          child: Row(
            children: [
              Padding(
                padding: const EdgeInsets.only(right:8.0),
                child: IconButton(
                    splashColor: appRippleColor,
                    onPressed: (){
                      Timer(const Duration(milliseconds: 200),(){
                        Navigator.pop(context);
                      });
                }, icon: const Icon(Icons.arrow_back_ios)),
              ),
               Padding(
                padding: const EdgeInsets.only(left:8.0),
                child: Text(widget.appBarName,style: const TextStyle(fontSize: 17.5,color: Colors.black87,
                    fontWeight: FontWeight.w500),),
              ),
              const Spacer(),Padding(
                padding: const EdgeInsets.only(right:12.0),
                child: Image.asset('assets/loupe.png',height: 15,width: 15,),
              ),
            ],),
        ),
        // backgroundColor: Color(0xfff1f3f5),
      ),
      body: data==null?LoadingOverlay():ListView.builder(
        scrollDirection: Axis.vertical,
       itemCount: data!.length??0,
        itemBuilder: (ctx,index){
          data1=data![index]['Category'];
          return moreAppsListUI(data1);
        },),
    );
  }
  Widget moreAppsListUI(List? data1) {
    return  Column(children: data1!.map((e) => Padding(
      padding: padding8,
      child: GestureDetector(
        onTap: (){
          navPush(context, AppsDetailsActivity
            (appId: e['id'],appIcon: e['img'],
            appName: e['name'],));
        },
        child: Container(
          color: const Color(0xfff1f3f5),
          height: 99,
          child: Row(children: [
            Container(
              height: 70,
              color: const Color(0xfff1f3f5),
              width: MediaQuery.of(context).size.width*.17,
              child: ClipRRect(
                  borderRadius: BorderRadius.circular(14),
                  child: Image.network(e['img'],fit: BoxFit.fitWidth,)),
            ),
            Padding(
              padding: const EdgeInsets.only(left:8.0),
              child: SizedBox(
                width: MediaQuery.of(context).size.width*.75,
                child: Padding(
                  padding: const EdgeInsets.only(top:8.0),
                  child: Column(
                    children: [
                      Row(
                        children: [
                          SizedBox(
                            width: MediaQuery.of(context).size
                                .width*.40,
                            child: Text(e['name'],
                                overflow:TextOverflow.ellipsis,
                                style: const TextStyle(color: Colors.black)),
                          ),
                          const Spacer(),
                          Padding(
                            padding: const EdgeInsets.only(right:8.0),
                            child: Container(
                              width: 80,
                              height: 35,
                              decoration: BoxDecoration(
                                  borderRadius: BorderRadius.circular(15),
                                  color: const Color(0xffe8e8e8)
                              ),
                              child: Center(
                                child: Text('Install'.toUpperCase(),style: const TextStyle(color:
                                Color(0xff0a58f5)),),
                              ),
                            ),
                          )
                        ],
                      ),
                      Align(
                          alignment: Alignment.topLeft,
                          child: Text(e['category'])),
                      Align(
                          alignment: Alignment.centerLeft,
                          child: Padding(
                            padding: const EdgeInsets.only(top:3.0),
                            child: Text(
                                e['description'],
                                overflow:TextOverflow.ellipsis,style:
                            const TextStyle(color: Colors.black)),
                          )),
                      Padding(
                        padding: const EdgeInsets.only(top:20.0,right:10 ),
                        child: Container(
                          width: double.infinity,
                          height: 1,
                          color: Colors.grey[300],
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ],),
        ),
      ),
    )).toList());
  }
}


