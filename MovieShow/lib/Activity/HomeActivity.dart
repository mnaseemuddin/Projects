
import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:carousel_pro/carousel_pro.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:movieshow/Activity/AllMovieSerialDetailActivity.dart';
import 'package:movieshow/Activity/MoreMoviesAndWebSeriesActivity.dart';
import 'package:movieshow/ApplicationConfigration/ApiUrls.dart';
import 'package:movieshow/Model/AllMovies.dart';
import 'package:movieshow/Model/Heading.dart';
import 'package:movieshow/Model/SliderImages.dart';
import 'package:movieshow/Model/TabItems.dart';
import 'package:movieshow/Model/TopMovies.dart';
import 'package:movieshow/Support/AlertDialogManager.dart';
import 'package:movieshow/Support/SharePreferenceManager.dart';

class Home extends StatefulWidget {
  const Home({Key? key}) : super(key: key);

  @override
  _HomeState createState() => _HomeState();
}

class _HomeState extends State<Home> with TickerProviderStateMixin{

  final List<SliderImages> imgList = [];
 // List<String> tabList=["Home","PlayBox Originals","Movies","Chat Show"];
  late Map map,map1;
  late List GridList,GridList1,GridList2,GridList3;
  late List<TabItems>tabList=[];
  String UserID="",MenuID="1";

  bool IsVisibiltyImageViewdefault=true,IsVisibiltyImageViewwithURL=false;

  List<TopMovies> topMoviesList=[];

  String FirstHeading="",SubCategoryiD="";


  List<Heading> allheadingList=[];

  List<AllMovies> AllMovieList=[];

  List<AllMovies> filterMovieList=[];
  var IsVisibilityTopMovies=true;
  bool IsLoading=false;

  late bool IsDialog=true;
 // late TabController _tabController;

  @override
  void initState() {
    //parseJson();

   SharePreferenceManager.instance.getUserID("UserID").then((value){
     UserID=value;
     topMoviesList.clear();
     allTabItems();

   });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return DefaultTabController(
      initialIndex: 0,
      length: tabList.length,
      child:
      Scaffold(
        backgroundColor: Colors.black,
        appBar: PreferredSize(
          preferredSize: Size(double.infinity, 49),
          child: AppBar(
            backgroundColor:Colors.black,
            leading: Container(),
            bottom: TabBar(
            onTap: (i){
            //  print(tabList[i].CategoryName);
              _onTap(tabList[i].CategoryID);
            },
              isScrollable: true,
              indicatorColor: Colors.red,
              tabs: List<Widget>.generate(
                tabList.length, (index){
                return Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: Text(tabList[index].CategoryName,style: TextStyle(color: Colors.white,
                      fontFamily: 'Helvetica Neue',
                      fontWeight: FontWeight.w800,fontSize: 15)),
                );
              }),
            ),
          ),
        ),
          body: TabBarView(
            // controller: _tabController,
            children:List<Widget>.generate(tabList.length, (int index){
              return  IsDialog==false?Center(child: CircularProgressIndicator(
                strokeWidth: 6.0,
              )):ListView(
                children: [
                  Container(
                    height: MediaQuery.of(context).size.height*0.24,
                    child: Carousel(
                      defaultImage: Image.asset("drawable/placeholder.png",width: double.infinity,fit: BoxFit.fill,),
                      dotBgColor:Colors.transparent,
                      images: imgList.map((e) =>
                          GestureDetector(
                            onTap: (){
                              Get.to(AllMovieAndSerialDetails(movieId: e.MovieID,));
                            },
                            child: Container(
                                width:double.infinity,child:
                            Image.network(e.ImageURL,width:double.infinity,fit: BoxFit.cover,
                                errorBuilder: (context,error,stackTrace)=>
                                    Center(child: Image.asset("drawable/placeholder.png"),))),
                          )).toList(),
                    ),
                  ),
                  //heading
                  Visibility(
                    visible:IsVisibilityTopMovies,
                    child: Container(
                      height: MediaQuery.of(context).size.height*0.08,
                      child: Row(
                        children: [
                          Padding(
                            padding: const EdgeInsets.only(left: 14.0,top: 28),
                            child: Row(children: [
                              Text(FirstHeading,style: TextStyle(fontSize: 15,color: Colors.white,
                                  fontFamily: 'Helvetica Neue',
                                  fontWeight: FontWeight.bold),)
                            ],),
                          ),
                          Expanded(
                            child: GestureDetector(
                              onTap: (){
                                Get.to(MoreMoviesAndWebSeriesActivty(SubCategoryID:
                                SubCategoryiD));
                              },
                              child: Align(
                                alignment: Alignment.centerRight,
                                child: Padding(
                                  padding: const EdgeInsets.only(left: 14.0,top: 28,right: 20),
                                  child: Text('More',style: TextStyle(fontSize: 14,color: Colors.white,
                                      fontFamily: 'Helvetica Neue',
                                      fontWeight: FontWeight.bold),),
                                ),
                              ),
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                  //Content
                  Visibility(
                    visible: IsVisibilityTopMovies,
                    child: Container(
                        height: MediaQuery.of(context).size.height*0.22,
                        child: ListView.builder(
                            itemCount: topMoviesList.length==null?0:topMoviesList.length,
                            scrollDirection: Axis.horizontal,
                            itemBuilder: (BuildContext ctx,int position){
                              return Row(
                                children: [
                                  GestureDetector(
                                    onTap: (){
                                      Get.to(AllMovieAndSerialDetails(movieId:topMoviesList[position].tblMovieid));
                                    },
                                    child: Padding(
                                      padding: const EdgeInsets.all(8.0),
                                      child: Container(
                                        width: 195,
                                        height: 160,
                                        decoration: BoxDecoration(
                                          borderRadius: BorderRadius.circular(5.0),
                                          border: Border.all(width: 1.0, color: Colors.black),
                                        ),
                                        child: Column(
                                          children: [
                                            Container(
                                                height: 110,
                                                child: FadeInImage.assetNetwork(placeholder: "drawable/placeholder.png",
                                                  image: topMoviesList[position].thumbnil,
                                                  imageErrorBuilder: (context,error,stackTrace)=>
                                                      Center(child: Image.asset("drawable/placeholder.png"),),)
                                            ),
                                            Container(
                                                margin: EdgeInsets.only(top:1),
                                                height: 29,
                                                decoration: BoxDecoration(
                                                  color: Colors.white,
                                                  borderRadius: BorderRadius.only(
                                                      bottomLeft: Radius.circular(5),bottomRight: Radius.circular(5)),
                                                ),
                                                width: double.infinity,
                                                child: Center(child: Text(
                                                  topMoviesList[position].title,style: TextStyle(fontSize: 13),textAlign: TextAlign.center,))),
                                          ],
                                        ),
                                      ),
                                    ),
                                  ),
                                  // Padding(
                                  //   padding: const EdgeInsets.all(8.0),
                                  //   child: Container(
                                  //     width: 220,
                                  //     height: 160,
                                  //     decoration: BoxDecoration(
                                  //       borderRadius: BorderRadius.circular(5.0),
                                  //       image: DecorationImage(
                                  //           image: AssetImage("drawable/TvSerial3.jpeg"),
                                  //           fit: BoxFit.fill
                                  //       ),
                                  //       border: Border.all(width: 1.0, color: const Color(0xff1f3349)),
                                  //     ),
                                  //     child: Align(
                                  //         alignment: Alignment.bottomCenter,
                                  //         child: Container(
                                  //             height: 30,
                                  //             decoration: BoxDecoration(
                                  //               color: Colors.white,
                                  //               borderRadius: BorderRadius.only(bottomLeft: Radius.circular(5),bottomRight: Radius.circular(5)),
                                  //             ),
                                  //             width: double.infinity,
                                  //             child: Center(child: Text('Bawara Dil')))),
                                  //   ),
                                  // )
                                ],
                              );
                            })
                    ),
                  ),
                  //heading
                  ListView.builder(
                      shrinkWrap: true,
                      physics: NeverScrollableScrollPhysics(),
                      // ignore: unnecessary_null_comparison
                      itemCount: allheadingList.length==null?0:allheadingList.length,
                      itemBuilder: (BuildContext ctx,int i){
                        filterMovieList.clear();
                        for(AllMovies allMovies in AllMovieList){
                          if(allheadingList[i].HeadingName==allMovies.CategoryName){
                            filterMovieList.add(allMovies);
                          }
                        }
                        return  Column(
                          children: [
                            Row(
                              children: [
                                Padding(
                                  padding: const EdgeInsets.only(left: 14.0,top: 0),
                                  child: Row(children: [
                                    Text(allheadingList[i].HeadingName,style:
                                    TextStyle(fontSize: 15,color: Colors.white,
                                        fontFamily: 'Helvetica Neue',
                                        fontWeight: FontWeight.bold),)
                                  ],),
                                ),
                                Expanded(
                                  child: Align(
                                    alignment: Alignment.centerRight,
                                    child: Padding(
                                      padding: const EdgeInsets.only(left: 14.0,top: 10,right: 20),
                                      child: filterMovieList.length>2?
                                      GestureDetector(
                                        onTap: (){
                                          Get.to(MoreMoviesAndWebSeriesActivty(SubCategoryID:
                                          allheadingList[i].SubCategoryID));
                                        },
                                        child: Text('More',style: TextStyle(fontSize: 15,color: Colors.white,
                                            fontFamily: 'Helvetica Neue',
                                            fontWeight: FontWeight.bold),),
                                      ):
                                      Text('',style: TextStyle(fontSize: 16,color: Colors.white,
                                          fontFamily: 'Helvetica Neue',
                                          fontWeight: FontWeight.bold),),
                                    ),
                                  ),
                                ),
                              ],
                            ),
                            Container(
                              height: 170,
                              child: _allCategoryListView(),
                            )

                          ],
                        );
                      }),
                  //Content
                ],
              );
            }),
          ))
    //  })
    );
  }

// Widget HomePageOptionLayout() {
//     return ListView(
//       children: [
//         Container(
//           height: 200,
//           child: Image.asset("drawable/tvSerial.jpeg",fit: BoxFit.fill,),
//         ),
//         //heading
//         Row(
//           children: [
//             Padding(
//               padding: const EdgeInsets.only(left: 14.0,top: 28),
//               child: Row(children: [
//                 Text('PlayBox Original',style: TextStyle(fontSize: 16,color: Colors.white,
//                     fontFamily: 'Helvetica Neue',
//                     fontWeight: FontWeight.bold),)
//               ],),
//             ),
//             Expanded(
//               child: Align(
//                 alignment: Alignment.centerRight,
//                 child: Padding(
//                   padding: const EdgeInsets.only(left: 14.0,top: 28,right: 40),
//                   child: Text('More',style: TextStyle(fontSize: 16,color: Colors.white,
//                       fontFamily: 'Helvetica Neue',
//                       fontWeight: FontWeight.bold),),
//                 ),
//               ),
//             ),
//           ],
//         ),
//         //Content
//         Container(
//           height: 160,
//           child: SingleChildScrollView(
//             scrollDirection: Axis.horizontal,
//             child: Row(
//               children: [
//                 GestureDetector(
//                   onTap: (){
//                     Get.to(AllMovieAndSerialDetails());
//                   },
//                   child: Padding(
//                     padding: const EdgeInsets.all(8.0),
//                     child: Container(
//                       width: 220,
//                       height: 160,
//                       decoration: BoxDecoration(
//                         borderRadius: BorderRadius.circular(5.0),
//                         image: DecorationImage(
//                             image: AssetImage("drawable/TvSerial2.jpeg"),
//                             fit: BoxFit.fill
//                         ),
//                         border: Border.all(width: 1.0, color: const Color(0xff1f3349)),
//                       ),
//                       child: Align(
//                           alignment: Alignment.bottomCenter,
//                           child: Container(
//                               height: 30,
//                               decoration: BoxDecoration(
//                                 color: Colors.white,
//                                 borderRadius: BorderRadius.only(bottomLeft: Radius.circular(5),bottomRight: Radius.circular(5)),
//                               ),
//                               width: double.infinity,
//                               child: Center(child: Text('Kundal Bhagya')))),
//                     ),
//                   ),
//                 ),
//                 Padding(
//                   padding: const EdgeInsets.all(8.0),
//                   child: Container(
//                     width: 220,
//                     height: 160,
//                     decoration: BoxDecoration(
//                       borderRadius: BorderRadius.circular(5.0),
//                       image: DecorationImage(
//                           image: AssetImage("drawable/TvSerial3.jpeg"),
//                           fit: BoxFit.fill
//                       ),
//                       border: Border.all(width: 1.0, color: const Color(0xff1f3349)),
//                     ),
//                     child: Align(
//                         alignment: Alignment.bottomCenter,
//                         child: Container(
//                             height: 30,
//                             decoration: BoxDecoration(
//                               color: Colors.white,
//                               borderRadius: BorderRadius.only(bottomLeft: Radius.circular(5),bottomRight: Radius.circular(5)),
//                             ),
//                             width: double.infinity,
//                             child: Center(child: Text('Bawara Dil')))),
//                   ),
//                 )
//               ],
//             ),
//           ),
//         ),
//         //heading
//         Row(
//           children: [
//             Padding(
//               padding: const EdgeInsets.only(left: 14.0,top: 28),
//               child: Row(children: [
//                 Text('Drama',style: TextStyle(fontSize: 16,color: Colors.white,
//                     fontFamily: 'Helvetica Neue',
//                     fontWeight: FontWeight.bold),)
//               ],),
//             ),
//             Expanded(
//               child: Align(
//                 alignment: Alignment.centerRight,
//                 child: Padding(
//                   padding: const EdgeInsets.only(left: 14.0,top: 28,right: 40),
//                   child: Text('',style: TextStyle(fontSize: 16,color: Colors.white,
//                       fontFamily: 'Helvetica Neue',
//                       fontWeight: FontWeight.bold),),
//                 ),
//               ),
//             ),
//           ],
//         ),
//         //Content
//         Container(
//           height: 160,
//           child: SingleChildScrollView(
//             scrollDirection: Axis.horizontal,
//             child: Row(
//               children: [
//                 Padding(
//                   padding: const EdgeInsets.all(8.0),
//                   child: Container(
//                     width: 220,
//                     height: 160,
//                     decoration: BoxDecoration(
//                       borderRadius: BorderRadius.circular(5.0),
//                       image: DecorationImage(
//                           image: AssetImage("drawable/TvSerial3.jpeg"),
//                           fit: BoxFit.fill
//                       ),
//                       border: Border.all(width: 1.0, color: const Color(0xff1f3349)),
//                     ),
//                     child: Align(
//                         alignment: Alignment.bottomCenter,
//                         child: Container(
//                             height: 30,
//                             decoration: BoxDecoration(
//                               color: Colors.white,
//                               borderRadius: BorderRadius.only(bottomLeft: Radius.circular(5),bottomRight: Radius.circular(5)),
//                             ),
//                             width: double.infinity,
//                             child: Center(child: Text('Bawara Dil')))),
//                   ),
//                 )
//               ],
//             ),
//           ),
//         ),
//       ],
//     );
// }
//
//  Widget PlayBoxOriginalOptionLayout() {
//     return ListView(
//       children: [
//         Container(
//           height: 210,
//           child: CarouselSlider(
//            options: CarouselOptions(
//              autoPlay: true,
//              enlargeCenterPage: true,
//            ), items: imgList.map((e) =>
//               Image.network(e,width: double.infinity)).toList(),
//           ),
//         ),
//         //heading
//         Row(
//           children: [
//             Padding(
//               padding: const EdgeInsets.only(left: 14.0,top: 28),
//               child: Row(children: [
//                 Text('PlayBox Original',style: TextStyle(fontSize: 16,color: Colors.white,
//                     fontFamily: 'Helvetica Neue',
//                     fontWeight: FontWeight.bold),)
//               ],),
//             ),
//             Expanded(
//               child: Align(
//                 alignment: Alignment.centerRight,
//                 child: Padding(
//                   padding: const EdgeInsets.only(left: 14.0,top: 28,right: 40),
//                   child: Text('More',style: TextStyle(fontSize: 16,color: Colors.white,
//                       fontFamily: 'Helvetica Neue',
//                       fontWeight: FontWeight.bold),),
//                 ),
//               ),
//             ),
//           ],
//         ),
//         //Content
//         Container(
//           height: 160,
//           child: SingleChildScrollView(
//             scrollDirection: Axis.horizontal,
//             child: Row(
//               children: [
//                 GestureDetector(
//    onTap: (){
//      Get.to(AllMovieAndSerialDetails());
//    },
//                   child: Padding(
//                     padding: const EdgeInsets.all(8.0),
//                     child: Container(
//                       width: 220,
//                       height: 160,
//                       decoration: BoxDecoration(
//                         borderRadius: BorderRadius.circular(5.0),
//                         image: DecorationImage(
//                           image: AssetImage("drawable/TvSerial2.jpeg"),
//                           fit: BoxFit.fill
//                         ),
//                         border: Border.all(width: 1.0, color: const Color(0xff1f3349)),
//                       ),
//                         child: Align(
//                             alignment: Alignment.bottomCenter,
//                             child: Container(
//                                 height: 30,
//    decoration: BoxDecoration(
//      color: Colors.white,
//    borderRadius: BorderRadius.only(bottomLeft: Radius.circular(5),bottomRight: Radius.circular(5)),
//    ),
//                                 width: double.infinity,
//                                 child: Center(child: Text('Kundal Bhagya')))),
//                     ),
//                   ),
//                 ),
//                 Padding(
//                   padding: const EdgeInsets.all(8.0),
//                   child: Container(
//                     width: 220,
//                     height: 160,
//                     decoration: BoxDecoration(
//                       borderRadius: BorderRadius.circular(5.0),
//                       image: DecorationImage(
//                           image: AssetImage("drawable/TvSerial3.jpeg"),
//                           fit: BoxFit.fill
//                       ),
//                       border: Border.all(width: 1.0, color: const Color(0xff1f3349)),
//                     ),
//                     child: Align(
//                         alignment: Alignment.bottomCenter,
//                         child: Container(
//                             height: 30,
//                             decoration: BoxDecoration(
//                               color: Colors.white,
//                               borderRadius: BorderRadius.only(bottomLeft: Radius.circular(5),bottomRight: Radius.circular(5)),
//                             ),
//                             width: double.infinity,
//                             child: Center(child: Text('Bawara Dil')))),
//                   ),
//                 )
//               ],
//             ),
//           ),
//         ),
//         //heading
//         Row(
//           children: [
//             Padding(
//               padding: const EdgeInsets.only(left: 14.0,top: 28),
//               child: Row(children: [
//                 Text('Drama',style: TextStyle(fontSize: 16,color: Colors.white,
//                     fontFamily: 'Helvetica Neue',
//                     fontWeight: FontWeight.bold),)
//               ],),
//             ),
//             Expanded(
//               child: Align(
//                 alignment: Alignment.centerRight,
//                 child: Padding(
//                   padding: const EdgeInsets.only(left: 14.0,top: 28,right: 40),
//                   child: Text('',style: TextStyle(fontSize: 16,color: Colors.white,
//                       fontFamily: 'Helvetica Neue',
//                       fontWeight: FontWeight.bold),),
//                 ),
//               ),
//             ),
//           ],
//         ),
//         //Content
//         Container(
//           height: 160,
//           child: SingleChildScrollView(
//             scrollDirection: Axis.horizontal,
//             child: Row(
//               children: [
//                 Padding(
//                   padding: const EdgeInsets.all(8.0),
//                   child: Container(
//                     width: 220,
//                     height: 160,
//                     decoration: BoxDecoration(
//                       borderRadius: BorderRadius.circular(5.0),
//                       image: DecorationImage(
//                           image: AssetImage("drawable/TvSerial3.jpeg"),
//                           fit: BoxFit.fill
//                       ),
//                       border: Border.all(width: 1.0, color: const Color(0xff1f3349)),
//                     ),
//                     child: Align(
//                         alignment: Alignment.bottomCenter,
//                         child: Container(
//                             height: 30,
//                             decoration: BoxDecoration(
//                               color: Colors.white,
//                               borderRadius: BorderRadius.only(bottomLeft: Radius.circular(5),bottomRight: Radius.circular(5)),
//                             ),
//                             width: double.infinity,
//                             child: Center(child: Text('Bawara Dil')))),
//                   ),
//                 )
//               ],
//             ),
//           ),
//         ),
//       ],
//     );
//  }
//
//  Widget MoviesOptionLayout() {
//     return Center(
//         child: Text(
//           'Movies',
//           style: TextStyle(fontSize: 32,color: Colors.white),
//         ));
//  }
//
//  Widget ChatShowOptionLayout() {
//     return Center(
//         child: Text(
//           'Chat Show',
//           style: TextStyle(fontSize: 32,color: Colors.white),
//         ));
//  }
  void allTabItems()async{

      // ignore: non_constant_identifier_names
      var ApiUrls=Uri.parse(APiUrls.TabItems);
      var response=await http.get(ApiUrls);
      if(response.statusCode==200) {
         map= json.decode(response.body);
        // map = resdata["data"] as List;
        // tabList=res.map<TabItems>((json) => TabItems.fromJson(json)).toList();
        setState(() {
          if(map["status"]=="1"){
            GridList =map['data'];
            for(int i=0;i<GridList.length;i++){
              TabItems tabItems=new TabItems(
                CategoryID: GridList[i]['Category_id'],
                CategoryName: GridList[i]["Category_name"]
              );
              tabList.add(tabItems);
              // _tabController = TabController(vsync: this, length: tabList.length);
              // _tabController.addListener(_handleTabSelection);
            }
            allSliderImage(UserID);
          }else{
            AlertDialogManager().IsErrorAlertDialog(context, "Status", map["message"], "");
          }
        });
      }
  }

  void allSliderImage(String UserID)async{
    map={
      "user_id":UserID,
      "menu_id":MenuID
    };
    print('${APiUrls.CompleteHomeAPI} => $map');
    try{
      // ignore: non_constant_identifier_names
      var ApiUrls=Uri.parse(APiUrls.CompleteHomeAPI);
      var response=await http.post(ApiUrls,body: map);
      if(response.statusCode==200) {
        map = json.decode(response.body);
        setState(() {
                       imgList.clear();
                       topMoviesList.clear();
                       AllMovieList.clear();
                       filterMovieList.clear();
                       allheadingList.clear();
          if(map["status"]=="1"){
            GridList =map['slider_data'];
            for(int i=0;i<GridList.length;i++){
              SliderImages sliderImages=new SliderImages(
                  SliderID: GridList[i]['tbl_slider_id'],
                  Name: GridList[i]["name"],
                ImageURL: GridList[i]['image'],
                MovieID: GridList[i]["movie_id"]
              );
              imgList.add(sliderImages);
              if(imgList.length>0){
                IsVisibiltyImageViewdefault=false;
                IsVisibiltyImageViewwithURL=true;
              }else{
                IsVisibiltyImageViewdefault=true;
                IsVisibiltyImageViewwithURL=false;
              }
            }

            GridList1=map['top_movies_data'];
            FirstHeading=GridList1[0]["title"];
            SubCategoryiD=GridList1[0]["sub_category_id"];
           for(int i=1;i<GridList1.length;i++){
             TopMovies topMovies=new TopMovies(title: GridList1[i]["title"], 
             tblMovieid: GridList1[i]["tbl_movie_id"], thumbnil: GridList1[i]["thumbnail_image"]);
             topMoviesList.add(topMovies);
             if(topMoviesList.length>0){
               IsVisibilityTopMovies=true;
             }else{
                IsVisibilityTopMovies=false;
             }
           }
            GridList2=map['all_category_movies'];
             for(int i=1;i<GridList2.length;i++){
               Heading heading=new Heading(HeadingName: GridList2[i]['category_name'],
               SubCategoryID: GridList2[i]["sub_category_id"]
               );

               allheadingList.add(heading);
              GridList3=GridList2[i]["all_movies"];
              for(int j=0;j<GridList3.length;j++){
                AllMovies allMovies=new AllMovies(tblMovieId: GridList3[j]["tbl_movie"], 
                CategoryName: GridList3[j]["category_name"],
                 MovieName: GridList3[j]["movie_name"], thumbnailImage: GridList3[j]["thumbnail_image"]);
                 AllMovieList.add(allMovies);
              }
             }
          }else{
            AlertDialogManager().IsErrorAlertDialog(context, "Status", map["message"], "");
          }
        });
     }
    }catch(e){
      AlertDialogManager().IsErrorAlertDialog(context, "Status", e.toString(), "");
    }
  }

 void allSliderImageByOption(String MenuID)async{

    map={
      "menu_id":MenuID
    };
    print('${APiUrls.OtherTabOptionAPI} => $MenuID');
    try{
      // ignore: non_constant_identifier_names
      var ApiUrls=Uri.parse(APiUrls.OtherTabOptionAPI);
      var response=await http.post(ApiUrls,body: map);
      if(response.statusCode==200) {
        map = json.decode(response.body);
        setState(() {
               imgList.clear();
               topMoviesList.clear();
               AllMovieList.clear();
               filterMovieList.clear();
               allheadingList.clear();
          if(map["status"]=="1"){
            GridList =map['slider_data'];
            for(int i=0;i<GridList.length;i++){
              SliderImages sliderImages=new SliderImages(
                  SliderID: GridList[i]['tbl_slider_id'],
                  Name: GridList[i]["name"],
                ImageURL: GridList[i]['image'],
                MovieID: GridList[i]["movie_id"]
              );
              if(topMoviesList.length>0){
               IsVisibilityTopMovies=true;
             }else{
                IsVisibilityTopMovies=false;
             }
              imgList.add(sliderImages);
              if(imgList.length>0){
                IsVisibiltyImageViewdefault=false;
                IsVisibiltyImageViewwithURL=true;
              }else{
                IsVisibiltyImageViewdefault=true;
                IsVisibiltyImageViewwithURL=false;
              }
            }

          //   GridList1=map['top_movies_data'];
          //   FirstHeading=GridList1[0]["title"];
          //  for(int i=1;i<GridList1.length;i++){
          //    TopMovies topMovies=new TopMovies(title: GridList1[i]["title"], 
          //    tblMovieid: GridList1[i]["tbl_movie_id"], thumbnil: GridList1[i]["thumbnail_image"]);
          //    topMoviesList.add(topMovies);
          //  }
            GridList2=map['data'];
             for(int i=0;i<GridList2.length;i++){
               Heading heading=new Heading(HeadingName: GridList2[i]['Category_name']
               ,SubCategoryID: GridList2[i]["Category_id"]);
               allheadingList.add(heading);
              GridList3=GridList2[i]["Movie_data"];
              for(int j=0;j<GridList3.length;j++){
                AllMovies allMovies=new AllMovies(tblMovieId: GridList3[j]["tbl_movie"], 
                CategoryName: GridList3[j]["category_name"],
                 MovieName: GridList3[j]["movie_name"], thumbnailImage: GridList3[j]["thumbnail_image"]);
                 AllMovieList.add(allMovies);
              }
             }
             IsDialog=true;
          }else{
           AlertDialogManager().IsErrorAlertDialog(context, "Status", map["message"], "");
          }
        });
      }
    }catch(e){
     AlertDialogManager().IsErrorAlertDialog(context, "Status", e.toString(), "");
    }
  }

  void _onTap(String i) {
      setState(() {
        if(i=="1"){
          allSliderImage(UserID);
        }else {
          AllMovieList.clear();
          IsDialog = false;
          String st = i;
          print(st);
          allSliderImageByOption(i);
        }
      });


  }

  Widget _allCategoryListView() {
    ListView myZListView=
      new ListView.builder(
          itemCount: filterMovieList.length.compareTo(0),
          scrollDirection: Axis.horizontal,
          itemBuilder: (BuildContext ctx,int pos ){
            return Container(
              height: 160,
              child: Row(
                children: filterMovieList.map((e){
                  return  Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Container(
                      width: 195,
                      height: 165,
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(5.0),
                        border: Border.all(width: 1.0, color: Colors.black),
                      ),
                      child: Column(
                        children: [
                          GestureDetector(
                            onTap: (){
                              Get.to(AllMovieAndSerialDetails(movieId: e.tblMovieId,));
                            },
                            child: Container(
                              height: 110,
                              child: FadeInImage.assetNetwork(
                                placeholder: "drawable/placeholder.png",
                                image: e.thumbnailImage,
                                  imageErrorBuilder: (context,error,stackTrace)=>
                                      Center(child: Image.asset("drawable/placeholder.png"),)
                              )
                            ),
                          ),
                          Container(
                            margin: EdgeInsets.only(top:1),
                              height: 30,
                              decoration: BoxDecoration(
                                color: Colors.white,
                                borderRadius: BorderRadius.only(bottomLeft: Radius.circular(5),bottomRight: Radius.circular(5)),
                              ),
                              width: double.infinity,
                              child: Center(
                                  child: Text(e.MovieName,style:TextStyle(fontSize: 13),textAlign: TextAlign.center,))),
                        ],
                      ),
                    ),
                  );
                }).toList(),
              ),
            );
          });
    return myZListView;
  }
}


