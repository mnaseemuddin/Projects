import 'package:appgallery/CommonUI/commonwidget.dart';
import 'package:appgallery/CommonUI/apptextview.dart';
import 'package:appgallery/Resources/color.dart';
import 'package:appgallery/Resources/constants.dart';
import 'package:appgallery/Utils/routes.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:get/get.dart';
import 'package:google_fonts/google_fonts.dart';
import '../../../Model/allconsoleappsmodel.dart';
import 'allreviewactivity.dart';
import 'ceditappsdetailsactivity.dart';
import 'creleasesversionhistory.dart';

class CAppsDetailsactivity extends StatefulWidget {
  final String img,name,status,releases,createAt,appIcon,featureGraphic,appId,
  appShortDescription,appFullDescription;
  List<ScreenShot> screenShotsList=[];
  List<ApkInformation>apkInfoList=[];
  List<Review>reviewList=[];

   CAppsDetailsactivity(
      {Key? key, required this.img, required this.name,
        required this.status, required this.releases,required this.createAt,
      required this.screenShotsList,required this.appIcon,required this.featureGraphic,
      required this.appId,required this.appShortDescription,required this.appFullDescription,
      required this.apkInfoList,required this.reviewList})
      : super(key: key);

  @override
  _CAppsDetailsactivityState createState() => _CAppsDetailsactivityState();
}

class _CAppsDetailsactivityState extends State<CAppsDetailsactivity> {

  var createdAtDate,dateAndTime,time;


  @override
  void initState() {
     dateAndTime=widget.createAt.split(",");
     createdAtDate=dateAndTime[1].split(" ");
     time=dateAndTime[2].split(" ");
    super.initState();
  }
  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    return Scaffold(
      backgroundColor: const Color(0xfff1f3f5),
      body: SafeArea(

        child: ListView(
          children: [
            Stack(
              children: [
                Image.network(
                  widget.img,
                  height: size.height * .30,
                  width: double.infinity,
                  fit: BoxFit.fitWidth,
                ),
                Padding(
                  padding: const EdgeInsets.only(top:8.0,left: 6),
                  child: Row(
                    children: [
                      GestureDetector(
                        onTap: (){
                          navReturn(context);
                        },
                        child: const ContainerBgWithRadius(
                          height: 26,
                          width: 26,
                          circular: 50,
                          color: appBlackColor,
                          child: Center(
                            child: Icon(Icons.arrow_back_ios_new,
                                size: 15, color: Colors.white),
                          )
                        ),
                      ),
                      const Spacer(),
                      Padding(
                        padding: const EdgeInsets.only(top: 0.0, left: 5, right: 8),
                        child: GestureDetector(
                          onTap: () {
                            navPush(context,EditAppsDetailsActivity(
                              appId: widget.appId,
                              appName: widget.name,screenShotsList: widget.screenShotsList,
                            appIcon: widget.appIcon, featureGraphic: widget.featureGraphic,
                              appFullDescription: widget.appFullDescription,
                              appShortDescription: widget.appShortDescription,
                            ));
                          },
                          child: ContainerBgWithRadius(
                              height: 28,
                              width: 28,
                              circular: 50,
                              color: appBlackColor,
                              child: Padding(
                                padding: const EdgeInsets.all(8.0),
                                child: Image.asset(
                                  assetsWriteImg,
                                  height: 35,
                                  width: 35,
                                  color: appWhiteColor,
                                ),
                              )),
                        ),
                      ),
                    ],
                  ),
                )
              ],
            ),
            Container(
              height: size.height * .08,
              color: Colors.grey[200],
              child: Row(
                children: [
                  Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: ClipRRect(
                        borderRadius: BorderRadius.circular(8),
                        child: Image.network(widget.img)),
                  ),
                  Flexible(
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Align(
                            alignment: Alignment.centerLeft,
                            child: Text(
                              widget.name,
                              maxLines: 1,
                              overflow: TextOverflow.ellipsis,
                              style: textStyleTitle1(
                                  color: Colors.black, fontSize: 18.0),
                            )),
                        Align(
                            alignment: Alignment.centerLeft,
                            child: Padding(
                              padding: const EdgeInsets.only(top: 2.0),
                              child: Text(widget.status.toUpperCase(),
                                  style: textStyleTitle(
                                      color: appBlackColor, fontSize: 14.0)),
                            )),
                      ],
                    ),
                  ),
                ],
              ),
            ),
            userAcquisitionUI(),
            uninstallDevicesUI(size),
            crashesChartUI(),
            latestReviewsUI(),
            activeReleasesUI(),
            const SizedBox(height: 10,)
          ],
        ),
      ),
    );
  }

  Widget userAcquisitionUI() {
    return Padding(
      padding: padding8,
      child: ContainerBg(
          child: Column(
            children: [
              Padding(
                padding: const EdgeInsets.all(12.0),
                child: Align(
                    alignment: Alignment.centerLeft,
                    child: Text(
                      'User acquisition',
                      style: textStyleTitle(
                          fontSize: 16.0, color: appBlackColor),
                    )),
              ),
              Padding(
                padding: paddingT14,
                child: Image.asset(
                  assetsGraphImg,
                  height: 65,
                  width: 65,
                  color: Colors.grey[600],
                ),
              ),
              Padding(
                padding: const EdgeInsets.all(8.0),
                child: Text(
                  'No data in the last 7 days',
                  style: TextStyle(fontSize: 14, color: Colors.grey[700]),
                ),
              ),
              const Padding(
                padding: EdgeInsets.only(top: 40),
                child: Divider(
                  height: 1.5,
                ),
              ),
              Padding(
                padding: const EdgeInsets.all(12.0),
                child: Align(
                  alignment: Alignment.centerLeft,
                  child: Text(
                    'View Details'.toUpperCase(),
                    style:
                        GoogleFonts.aBeeZee(fontSize: 15.0, color: Colors.blue),
                  ),
                ),
              )
            ],
          ),
          height: 260,
          circular: 2,
          width: double.infinity,
          color: appWhiteColor),
    );
  }

  Widget uninstallDevicesUI(Size size) {
    return Padding(
      padding: const EdgeInsets.only(left: 8.0, top: 0, right: 6, bottom: 6),
      child: SizedBox(
        height: 110,
        child: Row(
          children: [
            ContainerBg(
                child: Column(
                  children: [
                    Padding(
                      padding: const EdgeInsets.all(12.0),
                      child: Align(
                          alignment: Alignment.centerLeft,
                          child: Text(
                            'Uninstalls',
                            style: GoogleFonts.aBeeZee(
                                fontSize: 14.0, color: appBlackColor),
                          )),
                    ),
                    Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: Align(
                          alignment: Alignment.centerLeft,
                          child: Text(
                            '0',
                            style: textStyleTitle(
                                fontSize: 18.0, color: Colors.blue),
                          )),
                    ),
                    Padding(
                      padding: const EdgeInsets.only(top: 4.0, left: 8),
                      child: Align(
                          alignment: Alignment.centerLeft,
                          child: Text(
                            'Last 7 days',
                            style: textStyleTitle(
                                fontSize: 15.0, color: Colors.grey[600]),
                          )),
                    )
                  ],
                ),
                height: 110,
                circular: 5,
                color: appWhiteColor,
                width: size.width * .46),
            const Padding(padding: padding8),
            ContainerBg(
                child: Column(
                  children: [
                    Padding(
                      padding: const EdgeInsets.all(12.0),
                      child: Align(
                          alignment: Alignment.centerLeft,
                          child: Text(
                            'Devices',
                            style: GoogleFonts.aBeeZee(
                                fontSize: 14.0, color: appBlackColor),
                          )),
                    ),
                    Padding(
                      padding: EdgeInsets.all(10),
                      child: Image.asset(
                        assetsGraphImg,
                        height: 40,
                        width: 45,
                        color: Colors.grey[600],
                      ),
                    ),
                  ],
                ),
                height: 110,
                circular: 5,
                color: appWhiteColor,
                width: size.width * .46)
          ],
        ),
      ),
    );
  }

  Widget crashesChartUI() {
    return Padding(
      padding: padding8,
      child: ContainerBg(
          child: Column(
            children: [
              Padding(
                padding: const EdgeInsets.all(12.0),
                child: Align(
                    alignment: Alignment.centerLeft,
                    child: Text(
                      'Crashes',
                      style: textStyleTitle(
                          fontSize: 16.0, color: appBlackColor),
                    )),
              ),
              Padding(
                padding: paddingT14,
                child: Image.asset(
                  assetsGraphImg,
                  height: 65,
                  width: 65,
                  color: Colors.grey[600],
                ),
              ),
              Padding(
                padding: const EdgeInsets.all(8.0),
                child: Text(
                  'No Crashes in the last 72 hours',
                  style: TextStyle(fontSize: 14, color: Colors.grey[700]),
                ),
              ),
              const Padding(
                padding: EdgeInsets.only(top: 40),
                child: Divider(
                  height: 5.5,
                ),
              ),
              Padding(
                padding: const EdgeInsets.all(12.0),
                child: Align(
                  alignment: Alignment.centerLeft,
                  child: Text(
                    'View Details'.toUpperCase(),
                    style:
                        GoogleFonts.aBeeZee(fontSize: 15.0, color: Colors.blue),
                  ),
                ),
              )
            ],
          ),
          height: 260,
          circular: 2,
          width: double.infinity,
          color: appWhiteColor),
    );
  }

  Widget latestReviewsUI() {
    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: Container(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Padding(
              padding: const EdgeInsets.only(
                  top: 16.0, left: 8, right: 8, bottom: 8),
              child: Align(
                  alignment: Alignment.centerLeft,
                  child: HeadingText(
                    title: 'Latest reviews',
                    fontSize: 15.0,
                    color: appBlackColor,
                  )),
            ),
            ListView.builder(
              shrinkWrap: true,
              physics: const NeverScrollableScrollPhysics(),
                itemCount: widget.reviewList.length>2?2:widget.reviewList.length,
                itemBuilder: (ctx,i){
                  Review review=widget.reviewList.elementAt(i);
              return Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Padding(
                    padding: const EdgeInsets.only(left: 15.0, top: 8, bottom: 5),
                    child: Row(
                      children: [
                        SizedBox(
                          width: MediaQuery.of(context).size.width *.12,
                          child: ClipRRect(
                              borderRadius: BorderRadius.circular(28),
                              child: Image.network(
                                review.userImage,
                                height: 47,
                                width: 47,
                                fit: BoxFit.fill,
                              )),
                        ),
                        SizedBox(
                          width: MediaQuery.of(context).size.width * .77,
                          child: Padding(
                            padding: const EdgeInsets.all(8.0),
                            child: Column(
                              children: [
                                Row(
                                  children: [
                                    HeadingText(title:review.userName.capitalizeFirst.toString()),
                                    const Spacer(),
                                    HeadingText(title: review.dateTime)
                                  ],
                                ),
                                 Align(
                                    alignment: Alignment.centerLeft,
                                    child: Padding(
                                      padding: const EdgeInsets.only(top: 4.0),
                                      child: Text(review.review.capitalizeFirst.toString(),
                                        maxLines: 1,overflow: TextOverflow.ellipsis,
                                      ),
                                    ))
                              ],
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                  Divider(
                    height: 5,
                    color: Colors.grey[600],
                  ),
                ],
              );
            }),

            Padding(
              padding: const EdgeInsets.only(left:12.0),
              child: GestureDetector(
                onTap: (){
                  navPush(context, AllReviewActivity(appName: widget.name,reviewList: widget.reviewList,));
                },
                child: SizedBox(
                  height: 40,
                  child: Align(
                    alignment: Alignment.centerLeft,
                    child: HeadingText(
                      title: 'View More Reviews'.toUpperCase(),
                      fontSize: 14.0,
                      color: Colors.blue,
                    ),
                  ),
                ),
              ),
            )
          ],
        ),
         decoration: BoxDecoration(
         borderRadius: BorderRadius.circular(5),
         color: appWhiteColor,
),
        width: double.infinity,
      ),
    );
  }

  Widget activeReleasesUI() {
    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: ContainerBg(
        child: Column(
          children: [
            Padding(
              padding: const EdgeInsets.only(
                  top: 16.0, left: 8, right: 8, bottom: 8),
              child: Align(
                  alignment: Alignment.centerLeft,
                  child: HeadingText(
                    title: 'Active releases',
                    fontSize: 15,
                  )),
            ),
            Padding(
              padding: const EdgeInsets.only(left: 15.0, top: 15, bottom: 15),
              child: GestureDetector(
                onTap: (){
                  navPush(context, ReleasesVersionHistory(
                      appName: widget.name,apkInfoList: widget.apkInfoList,));
                },
                child: Row(
                  children: [
                    SizedBox(
                      width: MediaQuery.of(context).size.width * .12,
                      height: 45,
                      child: Container(
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
                    SizedBox(
                      width: MediaQuery.of(context).size.width * .77,
                      child: Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: Column(
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
                                        title: widget.apkInfoList.last.versionName,
                                        maxLines: 1,
                                      ),
                                      const Spacer(),
                                      HeadingText(
                                        fontSize: 13,
                                        title:createdAtDate[2]+" "+createdAtDate[1]+" (${time[2]} "
                                            "${time[3]})",
                                        maxLines: 1,
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
            Divider(
              height: 5,
              color: Colors.grey[600],
            ),
            // Padding(
            //   padding: const EdgeInsets.all(12.0),
            //   child: Align(
            //     alignment: Alignment.centerLeft,
            //     child: Text(
            //       'Manage releases'.toUpperCase(),
            //       style:
            //           GoogleFonts.aBeeZee(fontSize: 14.0, color: Colors.blue),
            //     ),
            //   ),
            // )
          ],
        ),
        height: 135,
        color: appWhiteColor,
        width: double.infinity,
        circular: 5,
      ),
    );
  }
}
