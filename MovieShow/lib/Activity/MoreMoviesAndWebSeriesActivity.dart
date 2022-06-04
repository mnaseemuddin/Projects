
import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:movieshow/ApplicationConfigration/ApiUrls.dart';
import 'package:movieshow/Model/AllMoreMovies.dart';
import 'package:http/http.dart'as http;

import 'AllMovieSerialDetailActivity.dart';
class MoreMoviesAndWebSeriesActivty extends StatefulWidget {

 final String SubCategoryID;
   MoreMoviesAndWebSeriesActivty({Key? key,required this.SubCategoryID}) : super(key: key);

  @override
  _MoreMoviesAndWebSeriesActivtyState createState() => _MoreMoviesAndWebSeriesActivtyState();
}

class _MoreMoviesAndWebSeriesActivtyState extends State<MoreMoviesAndWebSeriesActivty> {
  late List<AllMoreMovies> allMoreMovieList=[];
  late Map map,map1;
  late List<String> movieIdList=[];
  late bool IsProgressDialog=false;
  late List gridList;
 late String movieID="";

 @override
  void initState() {
   allMoreMovieList.clear();
   _getMoreMovies(movieID);
   super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.black,
      body:
     // FutureBuilder(
     //   future: _getMoreMovies(),
       // builder: (BuildContext context, AsyncSnapshot<dynamic> snapshot) {
        // if(snapshot.connectionState==ConnectionState.waiting){
        //   return Center(
        //     child: CircularProgressIndicator(strokeWidth: 6.0,),
        //   );
        // }else if(snapshot.connectionState==ConnectionState.done){
         // return
        Column(
          children: [
            Expanded(
              child: NotificationListener<ScrollNotification>(
                    onNotification: (ScrollNotification scrollInfo){
                      if(!IsProgressDialog&&scrollInfo.metrics.pixels==
                      scrollInfo.metrics.maxScrollExtent){
                        setState(() {
                          IsProgressDialog=true;
                         movieID= movieIdList.toString().replaceAll("[", "").replaceAll("]", "");
                          _getMoreMovies(movieID);
                        });
                      }
                      return false;
                    },
                    child: myMoreMovies()),
            ),
            Container(
              height: IsProgressDialog ? 50.0 : 0,
              color: Colors.transparent,
              child: Center(
                child: new CircularProgressIndicator(strokeWidth: 6.0,),
              ),
            ),
          ],
        )
        //}
      //  return CircularProgressIndicator(strokeWidth: 6.0,);
    //  },

      );

  }
  void _getMoreMovies(String movieID)async{

    map={
      "sub_category_id":widget.SubCategoryID,
      "last_id":movieID
    };

    var ApiUrls=Uri.parse(APiUrls.AllMoreMovies);
    var response=await http.post(ApiUrls,body: map);
    if(response.statusCode==200){
      var resdata=json.decode(response.body);
       setState(() {
         if(resdata["status"]=="1"){
        gridList=resdata["data"]as List;
        for(int i=0;i<gridList.length;i++){
          AllMoreMovies allMoreMovies=new AllMoreMovies(
              MovieName: gridList[i]["movie_name"], MovieID: gridList[i]["movie_id"],
              thumbnilImgae: gridList[i]["image"]);
          allMoreMovieList.add(allMoreMovies);
          IsProgressDialog=false;
        }
         }else{
       IsProgressDialog=false;
      }
      });
      
    }
 //   return allMoreMovieList;
  }
  // _getMoreMoviesMoreLoading(String movieID)async{
  //
  //   map1={
  //     "sub_category_id":widget.SubCategoryID,
  //     "last_id":movieID
  //   };
  //
  //   var ApiUrls=Uri.parse(APiUrls.AllMoreMovies);
  //   var response=await http.post(ApiUrls,body: map1);
  //   if(response.statusCode==200){
  //     var resdata=json.decode(response.body);
  //   setState(() {
  //     gridList =resdata["data"];
  //     for(int i=0;i<gridList.length;i++){
  //       AllMoreMovies allMoreMovies=new AllMoreMovies(
  //           MovieName: gridList[i]["movie_name"], MovieID: gridList[i]["movie_id"],
  //           thumbnilImgae: gridList[i]["image"]);
  //       allMoreMovieList.add(allMoreMovies);
  //     }
  //     IsProgressDialog=false;
  //   });
  //   }
  // }

  Widget myMoreMovies() {
    return GridView.builder(
        itemCount: allMoreMovieList.length==null?0:allMoreMovieList.length,
        gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
          crossAxisCount: 2,
    ), itemBuilder: (context,i){
      movieIdList.clear();
      movieIdList.add(allMoreMovieList[i].MovieID);
      return GestureDetector(
        onTap: (){
        Get.to(AllMovieAndSerialDetails(movieId: allMoreMovieList[i].MovieID));
        },
        child: Padding(
          padding: const EdgeInsets.all(7.0),
          child: Container(
            width: 220,
            decoration: BoxDecoration(
              borderRadius: BorderRadius.circular(5.0),
              border: Border.all(width: 1.0, color: Colors.black),
            ),
            child: Column(
              children: [
                Container(
                    height: 110,
                    child: new Image.network(allMoreMovieList[i].thumbnilImgae,height: double.infinity,
                      errorBuilder: (context,error,stackTrace)=>
                          Center(child: Image.asset("drawable/placeholder.png"),),
                    fit: BoxFit.cover,)
                ),
                Container(
                  margin: EdgeInsets.only(top:1),
                    height: 40,
                    decoration: BoxDecoration(
                      color: Colors.white,
                      borderRadius: BorderRadius.only(
                          bottomLeft: Radius.circular(5),bottomRight: Radius.circular(5)),
                    ),
                    width: double.infinity,
                    child: Center(child: Text(allMoreMovieList[i].MovieName,textAlign: TextAlign.center,))),
              ],
            ),
          ),
        ),
      );
    });
  }
}

