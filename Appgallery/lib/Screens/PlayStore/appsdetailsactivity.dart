

import 'dart:async';
import 'dart:math' as math;
import 'package:appgallery/CommonUI/apptextview.dart';
import 'package:appgallery/Controllers/allappsdetailscontroller.dart';
import 'package:appgallery/Model/appsdetailmodel.dart';
import 'package:appgallery/Model/mostappsearchedmodel.dart';
import 'package:appgallery/Resources/color.dart';
import 'package:appgallery/Screens/PlayStore/postreviewactivity.dart';
import 'package:appgallery/Utils/dailog.dart';
import 'package:appgallery/Utils/loadingoverlay.dart';
import 'package:appgallery/Utils/routes.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_rating_bar/flutter_rating_bar.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';

import '../../apis/apirepositary.dart';

class AppsDetailsActivity extends StatefulWidget {
  final String appId,appIcon,appName;

  const AppsDetailsActivity({required this.appId,required this.appIcon,
    required this.appName,Key? key}) : super(key: key);

  @override
  _AppsDetailsActivityState createState() => _AppsDetailsActivityState();
}

class _AppsDetailsActivityState extends State<AppsDetailsActivity> {

  bool isAppBarExpanded=false;
  final _scrollController = ScrollController();
  final controller=Get.put(AllAppsDetailsController());

  AppsDetailModel? apps;


  @override
  void initState() {
    super.initState();
    print(widget.appId);
    playStoreAppsDetails();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xfff1f3f5),
      body: SafeArea(
        top: true,
        child: apps!=null?NestedScrollView(
          controller: _scrollController,
          headerSliverBuilder: (BuildContext context, bool innerBoxIsScrolled) {
            return <Widget>[
              SliverAppBar(
                backgroundColor: const Color(0xfff1f3f5),
                leading: IconButton(
                  splashColor: appRippleColor,
                  color: Colors.black54,
                  icon: const Icon(Icons.arrow_back_ios), onPressed: () {
                  Timer(const Duration(milliseconds: 200),(){
                    Navigator.pop(context);
                  });
                },),
                expandedHeight: 160.0,
                floating: true,
                pinned: true,
                elevation: 10,
                flexibleSpace:MyflexibleAppBar(appName: apps!.data.first.appName,
                  appImg:apps!.data.first.appIcon,),

              ),
            ];
          },
          body: Scaffold(
            backgroundColor:const Color(0xfff1f3f5),
            body: Container(
              decoration: const BoxDecoration(
                color: Color(0xffffffff),
                borderRadius: BorderRadius.only(topRight: Radius.circular(25),
                    topLeft: Radius.circular(25)),
              ),

              child: apps!=null?ListView(children: [
                ratingReviewUI(),
                appsScreenShotUI(),
                const SizedBox(height: 5,),
                appsDescriptionUI(),
                const SizedBox(height: 20,),
                alsoPopularHeadingUI(),
                const SizedBox(height: 5,),
                alsoPopularAppsUI(),
                const Spacer(),
                const SizedBox(height: 60,),
                Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: appsInstallAndShareUI(),
                )



              ],):const Center(child: CircularProgressIndicator(strokeWidth: 5)),
            ),
          ),
        ):
       LoadingOverlay(),
      ),
    );
  }

 Widget ratingReviewUI() {
    return   Row(children: [
       Padding(
        padding: const EdgeInsets.only(top:18.0,left: 15),
        child: Text(apps!.data.first.averageReview.toString(),style: TextStyle(color: Colors.black87,fontSize: 15,)),
      ),
      Padding(
        padding: const EdgeInsets.only(top:18.0,left: 4),
        child: RatingBar.builder(
          unratedColor: Colors.grey[400],
          initialRating: apps!.data.first.averageReview,
          itemSize: 20,
          minRating: 1,
          direction: Axis.horizontal,
          allowHalfRating: true,
          itemCount: 5,
          itemBuilder: (context, _) => const Icon(
            Icons.star_rounded,
            color: Colors.black54,
          ),
          onRatingUpdate: (rating) {
            print(rating);
          },
        ),
      ),
      const Spacer(),
       Padding(
        padding: const EdgeInsets.only(top:20.0),
        child: Text('${apps!.data.first.appInstallationCount}\nInstalls',textAlign: TextAlign.center,
            style:const TextStyle(color: Colors.black54,fontSize: 14,
                fontWeight: FontWeight.w600)),
      ),const Spacer(),
      const Padding(
        padding: EdgeInsets.only(top:20.0,right: 20),
        child: Text('3+\n Rated 3+',textAlign: TextAlign.center,
            style:TextStyle(color: Colors.black54,fontSize: 14,
                fontWeight: FontWeight.w600)),
      )
    ],);
 }
 Widget appsScreenShotUI() {
    return Column(children: [
      Padding(
        padding: const EdgeInsets.only(left:20.0,top: 25,right: 10),
        child: Container(height: 1,color: Colors.grey[200],),
      ),
      SizedBox(
        height: 305,
        child: ListView.builder(
          scrollDirection: Axis.horizontal,
            itemCount: apps!.data.first.screenShot.length??0,
            itemBuilder: (ctx,i){
              ScreenShot shots= apps!.data.first.screenShot.elementAt(i);
          return Padding(
            padding: const EdgeInsets.only(top:8.0,left: 12),
            child: ClipRRect(
                borderRadius: BorderRadius.circular(14),
                child: Image.network(shots.filename,fit: BoxFit.fill,width: 180,)),
          );
        }),
      ),
    ],);
 }
 Widget appsDescriptionUI() {
    return Column(children: [
       Padding(
        padding: const EdgeInsets.only(top:8.0,left: 20),
        child: Align(
            alignment: Alignment.centerLeft,
            child: HeadingText(title: apps!.data.first.appShortDescription)),
      ),

      Padding(
        padding: const EdgeInsets.only(left: 20.0,top: 20),
        child: Row(
          children:  [
            HeadingText(title: 'About'),
            const Spacer(),
            const Icon(Icons.arrow_forward_ios_outlined,size: 15,)
          ],
        ),
      ),

      Padding(
        padding: const EdgeInsets.only(left:20.0,top:5),
        child: Row(
          children:  [
            HeadingText(title:'Free',color: Colors.black.withOpacity(0.6),),
             Padding(
              padding: const EdgeInsets.only(left:15.0),
              child: HeadingText(title: apps!.data.first.apkInformation.last.versionName,color: Colors.black.withOpacity(0.6),),
            ),
          ],
        ),
      ),

       Padding(
        padding: const EdgeInsets.only(left:20.0,top: 15),
        child: HeadingText(title:apps!.data.first.appFullDescription,color:
        Colors.black.withOpacity(0.7),),
      ),
    ],);
 }
 Widget alsoPopularHeadingUI() {
    return  Padding(
      padding: const EdgeInsets.fromLTRB(20, 10, 20, 0),
      child: Row(children: const [
        Text('Also popular',style: TextStyle(fontSize: 15,
            color: Colors.black)),
        Spacer(),
        Text('More',style: TextStyle(fontSize: 13),),
        Icon(Icons.arrow_forward_ios_outlined,size: 13,)
      ],),
    );
 }
  Widget appsInstallAndShareUI() {
    return Container(
      color: const Color(0xffffffff),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Padding(
            padding: const EdgeInsets.only(top:8.0,left: 25),
            child: Icon(Icons.share,color: Colors.grey[800],size: 25,),
          ),
          const Spacer(),
          Padding(
            padding: const EdgeInsets.only(right:0.0,top: 8),
            child: SizedBox(
                height: 35,
                width: 160,
                child: RaisedButton(
                  splashColor: Colors.blueAccent,
                  shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(40)
                  ),
                  onPressed:()=> null,color:
                // ignore: prefer_const_constructors
                const Color(0xff0a58f5),child:  Text('Install'.toUpperCase(),style: TextStyle(color:
                Colors.white),),)),
          ),const Spacer(),
           Padding(
            padding: const EdgeInsets.only(right:18.0,top: 8),
            child: IconButton(icon: const Icon(Icons.add_comment,size: 25,), onPressed: () {
              navPush(context, PostReviewActivity(appIcon: widget.appIcon,appName: widget.appName,
              appID: widget.appId,));
            },),
          )

        ],),
    );
  }

  Widget alsoPopularAppsUI() {
    return
                       SizedBox(
                    height: 140,
                    child: ListView.builder(
                      scrollDirection: Axis.horizontal,
                        itemCount: controller.alsoPopularList.length??0,
                        itemBuilder: (ctx,index){
                          MostAppSearchedModel alsopopulur=controller.alsoPopularList.elementAt(index);
                      return  Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: SizedBox(
                          width: 90,
                          height: 140,
                          child: Column(children: [
                            Container(
                              height: 70,
                              width: 74,
                              decoration: BoxDecoration(
                                border: Border.all(width: 1,
                                    color: const Color(0xffdbdada)),
                                borderRadius: BorderRadius.circular(10),
                              ),
                              child: Padding(
                                padding: const EdgeInsets.all(2.0),
                                child: Image.asset(alsopopulur.img,fit: BoxFit.fitWidth,),
                              ),
                            ),
                            Padding(
                              padding: const EdgeInsets.only(left:10.0),
                              child: Text(
                                alsopopulur.name,overflow:TextOverflow.ellipsis,style: const TextStyle(
                                    fontSize: 13,color: Colors.black
                                ),),
                            ),
                            Padding(
                              padding: const EdgeInsets.only(left: 8,right:0.0,top: 8),
                              child: InkWell(

                                onTap: (){
                                },
                                child: Container(
                                  width: 80,
                                  height: 30,
                                  decoration: BoxDecoration(
                                      borderRadius: BorderRadius.circular(15),
                                      color: const Color(0xffe5e4e4)
                                  ),
                                  child: Center(
                                    child: Text('Install'.toUpperCase(),style: const TextStyle(fontSize: 11,
                                        color: Color(0xff0a58f5)),),
                                  ),
                                ),
                              ),
                            )
                          ],),
                        ),
                      );
                    }),
                  );
               
  }

  void playStoreAppsDetails() {

   final Map<String,dynamic>detail={
     "id":widget.appId
   };

   playStoreAppDetailsAPI(detail).then((value){
    setState(() {
      if(value.status) {
        apps = value.data;
      }else{
        Fluttertoast.showToast(msg: value.message.toString());
      }
    });
   });
  }
}

class MyflexibleAppBar extends StatelessWidget {
   String appImg,appName;
     MyflexibleAppBar({Key? key,required this.appImg,required this.appName}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return LayoutBuilder(
      builder: (context, c) {
        final settings = context
            .dependOnInheritedWidgetOfExactType<FlexibleSpaceBarSettings>();
        final deltaExtent = settings!.maxExtent - settings!.minExtent;
        final t =
        (1.0 - (settings.currentExtent - settings.minExtent) / deltaExtent)
            .clamp(0.0, 1.0);
        final fadeStart = math.max(0.0, 1.0 - kToolbarHeight / deltaExtent);
        const fadeEnd = 1.0;
        final opacity = 1.0 - Interval(fadeStart, fadeEnd).transform(t);

        return Stack(
          children: [
            Center(
              child: Opacity(
                  opacity: 1 - opacity,
                  child: Align(
                    alignment: Alignment.centerLeft,
                    child: Padding(
                      padding: const EdgeInsets.only(left:42.0),
                      child: getTitle(
                        appName,
                      ),
                    ),
                  )),
            ),
            Opacity(
              opacity: opacity,
              child: Stack(
                alignment: Alignment.bottomCenter,
                children: [
                  getImage(),
                ],
              ),
            ),
          ],
        );
      },
    );
  }
  Widget getImage() {
    return Container(
      color: const Color(0xfff1f3f5),
      child: Padding(
        padding: const EdgeInsets.fromLTRB(15.0,60,0,10),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Expanded(
              flex: 20,
              child: ClipRRect(
                borderRadius: BorderRadius.circular(10),
                child: Image.network(
                  appImg,
                  fit: BoxFit.cover,
                ),
              ),
            ),
            Expanded(
              flex: 65,
              child: Container(
                color: const Color(0xfff1f3f5),
                child: Padding(
                  padding: const EdgeInsets.fromLTRB(0, 10, 15, 0),
                  child: ListView(
                    children:  [
                      Padding(
                        padding: const EdgeInsets.fromLTRB(15, 0, 15, 0),
                        child: Align(
                          alignment: Alignment.topLeft,
                          child: HeadingTextFontWtW500(title: appName,
                            fontSize: 18,color: Colors.black,
                              ),
                        ),
                      ),
                      Align(
                          alignment: Alignment.centerLeft,
                          child: Padding(
                            padding: EdgeInsets.fromLTRB(15, 5, 15, 0),
                            child: Text('Free',style: TextStyle(color: Colors.black54),),
                          )),

                      Align(
                          alignment: Alignment.centerLeft,
                          child: Padding(
                            padding: EdgeInsets.fromLTRB(15, 4, 15, 0),
                            child: Text('Ads - Manual check',style: TextStyle(color: Colors.black54),),
                          ))
                    ],
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget getTitle(String text) {
    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: Text(
        text, textAlign: TextAlign.left,
        style: const TextStyle(
          color: Colors.black,
          fontSize: 15.0,
        ),
      ),
    );
  }
}
