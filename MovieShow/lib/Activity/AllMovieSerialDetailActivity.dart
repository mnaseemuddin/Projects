
import 'dart:convert';
//import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/route_manager.dart';
import 'package:movieshow/Activity/VideoPlayerActivity.dart';
import 'package:movieshow/ApplicationConfigration/ApiUrls.dart';
import 'package:http/http.dart'as http;
import 'package:movieshow/Support/AlertDialogManager.dart';
import 'package:movieshow/Support/SharePreferenceManager.dart';
import 'package:video_player/video_player.dart';


class AllMovieAndSerialDetails extends StatefulWidget {
  late final String movieId;
      AllMovieAndSerialDetails({Key? key, required this.movieId}) : super(key: key);
  @override
  _AllMovieAndSerialDetailsState createState() => _AllMovieAndSerialDetailsState();
}

class _AllMovieAndSerialDetailsState extends State<AllMovieAndSerialDetails> {
  late Map map;
  var MovieId;
  var UserID;
  late List GridList;
  // ignore: non_constant_identifier_names
  late String MovieName="",thumbnailImage="",Story="",cast='',director="",duration="",resume_time="",
  add_to_fevrit="",trailer='',videoId='',Moviez='';
  // ignore: non_constant_identifier_names
  bool IsFavaurite=false;
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.black,
      body: ListView(
        children: [
          Container(
            margin: EdgeInsets.all(1),
            height: 225,
            child: Image.network(thumbnailImage,fit: BoxFit.fill,
                errorBuilder: (context,error,stackTrace)=>
                    Center(child: Image.asset("drawable/placeholder.png"),)),
          ),
          Padding(
            padding: const EdgeInsets.only(left: 10.0,top: 15),
            child: Text("Movie",style: TextStyle(color: Colors.red,
                fontFamily: 'Helvetica Neue',
                fontWeight: FontWeight.w800,fontSize: 18)),
          ),

 Padding(
            padding: const EdgeInsets.only(left: 10.0,top: 15,right: 10),
            child: Align(
              alignment: Alignment.centerRight,
              child: GestureDetector(
                onTap: (){
                  setState(() {
                     IsFavaurite=true;
                     _addfavorite();
                  });
                },
                child: IsFavaurite==false?Icon(Icons.favorite,size: 27,color: Colors.white,):
                GestureDetector(
                  onTap: (){
                      setState(() {
                     IsFavaurite=false;
                     _removefavorite();
                  });
                  },
                  child: Icon(Icons.favorite,size: 30,color: Colors.red,)))),
          ),

          Padding(
            padding: const EdgeInsets.only(left: 10.0,top: 15),
            child: Text(MovieName,style: TextStyle(color: Colors.white,
                fontFamily: 'Helvetica Neue',
                fontWeight: FontWeight.w800,fontSize: 18)),
          ),


          Padding(
            padding: const EdgeInsets.only(top:18.0,left: 10),
            child: Container(
              height: 110,
              child: Row(
                children: [
                  Expanded(
                      child: Column(
                  children: [
                    GestureDetector(
                      onTap: (){
                        Get.to(AllVideoPlayer(playurl: Moviez));
                      },
                      child: Container(
                        height: 50,
                          width: 50,
                          decoration: BoxDecoration(
                            borderRadius: BorderRadius.circular(106.0),
                            color:  Colors.black,
                            border: Border.all(width: 1.2, color: const Color(0xffe61a1a)),
                          ),
                          child: Icon(Icons.play_arrow,color: Colors.white,size: 40,)),
                    ),
                    Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: Text("Watch Movie",style: TextStyle(fontFamily: 'Helvetica Neue',
                          fontWeight: FontWeight.w800,fontSize: 15,color: Colors.white),),
                    )
                  ],
                )),
                  Expanded(
                      child: Column(
                        children: [
                          GestureDetector(
                            onTap: (){
                              Get.to(AllVideoPlayer(playurl: trailer));
                            },
                            child: Container(
                                height: 50,
                                width: 50,
                                decoration: BoxDecoration(
                                  borderRadius: BorderRadius.circular(106.0),
                                  color:  Colors.black,
                                  border: Border.all(width: 1.2, color: const Color(0xffe61a1a)),
                                ),
                                child: Icon(Icons.play_arrow,color: Colors.white,size: 40,)),
                          ),
                          Padding(
                            padding: const EdgeInsets.all(8.0),
                            child: Text("Play Trailer",style: TextStyle(fontFamily: 'Helvetica Neue',
                                fontWeight: FontWeight.w800,fontSize: 15,color: Colors.white),),
                          )
                        ],
                      )),
                ],
              ),
            ),
          ),


          Padding(
            padding: const EdgeInsets.only(left: 10.0,top: 20),
            child: Text("Movie description",style: TextStyle(color: Colors.white,
                fontFamily: 'Helvetica Neue',
                fontWeight: FontWeight.w800,fontSize: 14)),
          ),


          Padding(
            padding: const EdgeInsets.only(left: 10.0,top: 10),
            child: Text(Story,style: TextStyle(
                color: Colors.white,
                fontFamily: 'Helvetica Neue',
                fontWeight: FontWeight.w600,fontSize: 12.5)),
          ),

          Padding(
            padding: const EdgeInsets.only(left: 10.0,top: 20),
            child: Text("Serial Detail",style: TextStyle(color: Colors.white,
                fontFamily: 'Helvetica Neue',
                fontWeight: FontWeight.w800,fontSize: 14)),
          ),

          Padding(
            padding: const EdgeInsets.only(left: 10.0,top: 20),
            child: Text("Director",style: TextStyle(color: Colors.white,
                fontFamily: 'Helvetica Neue',
                fontWeight: FontWeight.w600,fontSize: 14)),
          ),
          Padding(
            padding: const EdgeInsets.only(left: 10.0,top: 02),
            child: Text(director,style: TextStyle(color: Colors.white,
                fontFamily: 'Helvetica Neue',
                fontWeight: FontWeight.w500,fontSize: 12.5)),
          ),


          Padding(
            padding: const EdgeInsets.only(left: 10.0,top: 20),
            child: Text("Cast",style: TextStyle(color: Colors.white,
                fontFamily: 'Helvetica Neue',
                fontWeight: FontWeight.w600,fontSize: 14)),
          ),
          Padding(
            padding: const EdgeInsets.only(left: 10.0,top: 02),
            child: Text(cast,style: TextStyle(color: Colors.white,
                fontFamily: 'Helvetica Neue',
                fontWeight: FontWeight.w500,fontSize: 12.5)),
          ),

          Padding(
            padding: const EdgeInsets.only(left: 10.0,top: 20),
            child: Text("Duration",style: TextStyle(color: Colors.white,
                fontFamily: 'Helvetica Neue',
                fontWeight: FontWeight.w600,fontSize: 14)),
          ),
          Padding(
            padding: const EdgeInsets.only(left: 10.0,top: 02),
            child: Text(duration,style: TextStyle(color: Colors.white,
                fontFamily: 'Helvetica Neue',
                fontWeight: FontWeight.w500,fontSize: 12.5)),
          ),
         SizedBox(
           height: 40,
         )
        ],
      ),
    );
  }
  _getAllMoviesDetails()async{
     map={
      'id':widget.movieId,
      'user_id':UserID,
     };

    try{
     var ApiUrls=Uri.parse(APiUrls.AllMovieDetails);
     var response=await http.post(ApiUrls,body: map);
     if(response.statusCode==200){
       map=json.decode(response.body);
       setState(() {
         if(map["status"]=="1"){
         GridList=map["data"];
          videoId=GridList[0]["id"];
          MovieName=GridList[0]["Movies Name"];
          thumbnailImage=GridList[0]['thumbnail_image'];
          Story=GridList[0]["story"];
          director=GridList[0]["director"];
          cast=GridList[0]["cast"];
          duration=GridList[0]["duration"];
          resume_time=GridList[0]["resume_time"];
          add_to_fevrit=GridList[0]["add_to_fevrit"];
          Moviez=GridList[0]["movie"];
          if(add_to_fevrit=="Not_added"){
            IsFavaurite=false;
          }else{
            IsFavaurite=true;
          }
          trailer=GridList[0]['trailer'];
         }
       });
     }
    }catch(e){
AlertDialogManager().IsErrorAlertDialog(context, "Status", e.toString(), "");
    }
  }
  @override
  void initState() {
    SharePreferenceManager.instance.getUserID("UserID").then((value){
      setState(() {
        UserID=value;
        _getAllMoviesDetails();
      });
    });
    super.initState();
  }

  void _addfavorite() async{
    map={
     "user_id":UserID,
     "vedio_id":videoId,
     "vedio_type":""
    };

  try{
     var ApiUrls=Uri.parse(APiUrls.Favourite);
     var response=await http.post(ApiUrls,body: map);
     if(response.statusCode==200){
       map=json.decode(response.body);
       setState(() {
         if(map["status"]=="1"){
           Fluttertoast.showToast(msg: map["message"],gravity: ToastGravity.BOTTOM,textColor: Colors.white,
           backgroundColor: Colors.black);
         }else{
            Fluttertoast.showToast(msg: map["message"],gravity: ToastGravity.BOTTOM,textColor: Colors.white,
            backgroundColor: Colors.black);
         }
       });
     }
    }catch(e){
    AlertDialogManager().IsErrorAlertDialog(context, "Status", e.toString(), "");
    }

  }

  void _removefavorite() async{

    map={
      "user_id":UserID,
      "video_id":videoId,
    };

    try{
      var ApiUrls=Uri.parse(APiUrls.removeFavorite);
      var response=await http.post(ApiUrls,body: map);
      if(response.statusCode==200){
        map=json.decode(response.body);
        setState(() {
          if(map["status"]=="1"){
            Fluttertoast.showToast(msg: map["message"],gravity: ToastGravity.BOTTOM,textColor: Colors.white,
                backgroundColor: Colors.black);
          }else{
            Fluttertoast.showToast(msg: map["message"],gravity: ToastGravity.BOTTOM,textColor: Colors.white,
                backgroundColor: Colors.black);
          }
        });
      }
    }catch(e){
      AlertDialogManager().IsErrorAlertDialog(context, "Status", e.toString(), "");
    }
  }
}
