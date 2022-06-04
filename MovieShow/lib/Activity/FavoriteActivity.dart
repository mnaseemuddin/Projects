import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:get/get_core/src/get_main.dart';
import 'package:movieshow/Activity/AllMovieSerialDetailActivity.dart';
import 'package:movieshow/Activity/DashBoard.dart';
import 'package:movieshow/Activity/HomeActivity.dart';
import 'package:movieshow/ApplicationConfigration/ApiUrls.dart';
import 'package:movieshow/Model/AllFavorite.dart';
import 'package:http/http.dart' as http;
import 'package:movieshow/Support/AlertDialogManager.dart';
import 'package:movieshow/Support/SharePreferenceManager.dart';

class FavoriteActivity extends StatefulWidget {
  const FavoriteActivity({Key? key}) : super(key: key);

  @override
  _FavoriteActivityState createState() => _FavoriteActivityState();
}

class _FavoriteActivityState extends State<FavoriteActivity> {
  late Map map;
  late String UserID = "";
 late List<Allfavorite> allfavoriteList = [];

 late List gridList;
  @override
  void initState() {
    SharePreferenceManager.instance.getUserID("UserID").then((value) {
    setState(() {
      UserID = value;
     // _getFavorite1();
    });  
    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.black,
      body: FutureBuilder(
        future: _getFavorite(),
        builder: (context, AsyncSnapshot snapshot) {
          if (snapshot.hasError) {
            return Container(
              child: Center(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text(
                      'Your Favourite Is Empty',
                      style: TextStyle(
                        fontFamily: 'Helvetica Neue',
                        fontSize: 18,
                        color: const Color(0xffffffff),
                        fontWeight: FontWeight.w700,
                      ),
                    ),
                    GestureDetector(
                      onTap: (){
                      Get.offAll(DashBoard());
                      },
                      child: Padding(
                        padding: const EdgeInsets.only(top:8.0,left: 50,right: 50),
                        child: Container(
                          width: double.infinity,
                          height: 50,
                          decoration: BoxDecoration(
                            borderRadius: BorderRadius.circular(6.0),
                            color: const Color(0xffe61a1a),
                            border: Border.all(
                                width: 1.0, color: const Color(0xff707070)),
                          ),
                          child: Center(
                            child: Text(
                              'Browse Now',
                              style: TextStyle(
                                fontFamily: 'Helvetica Neue',
                                fontSize: 15,
                                color: const Color(0xffffffff),
                                fontWeight: FontWeight.w700,
                              ),
                              textAlign: TextAlign.center,
                            ),
                          ),
                        ),
                      ),
                    )
                  ],
                ),
              ),
            );
          } else if (snapshot.connectionState == ConnectionState.waiting) {
            return Center(child: CircularProgressIndicator(
              strokeWidth: 7.4,
            ));
          } else if (snapshot.connectionState == ConnectionState.done) {
            return Container(
                child: _MyFavorite(snapshot.data));
          }
          return CircularProgressIndicator(strokeWidth: 7.4,);
        },
      ),
    );
  }

  Future<List<Allfavorite>> _getFavorite() async {
   
    map = {"user_id": UserID};

      var ApiUrls = Uri.parse(APiUrls.GetFavorite);
      var response = await http.post(ApiUrls, body: map);
      if (response.statusCode == 200) {
        var resdata = json.decode(response.body);
        // gridList=resdata["data"];
        // for(int i=0;i<gridList.length;i++){
        //   Allfavorite allfavorite=new Allfavorite(MovieName: gridList[i]["movie_name"],
        //       VideoID: gridList[i]["movie_id"], thumbnilImage: gridList[i]["image"]);
        //   allfavoriteList.add(allfavorite);
        // }
        var res = resdata["data"] as List;
        allfavoriteList =
            res.map<Allfavorite>((json) => Allfavorite.fromJSon(json)).toList();
      }

    return allfavoriteList;
  }

  Widget _MyFavorite(List<Allfavorite> allfavoriteList) {
    return ListView.builder(
        itemCount: allfavoriteList.length == null ? 0 : allfavoriteList.length,
        itemBuilder: (BuildContext ctx, int i) {
          return GestureDetector(
            onTap: (){
             Get.to(AllMovieAndSerialDetails(movieId: allfavoriteList[i].VideoID));
            },
            child: Container(
              width: MediaQuery.of(context).size.width*0.100,
              height: 140,
              child: Row(
                children: [
                  Container(
                      width: MediaQuery.of(context).size.width*0.35,
                      height: 95,
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(5.0),
                        image: DecorationImage(
                            image: NetworkImage(allfavoriteList[i].thumbnilImage),
                            fit: BoxFit.fill),
                        border: Border.all(
                            width: 1.0, color: const Color(0xff1f3349)),
                      ),
                      child: Align(
                          alignment: Alignment.bottomLeft,
                          child: Container(
                              height: 50,
                              width: 60,
                              child: Image.asset("drawable/playicon.png")))),
                  Container(
                     width: MediaQuery.of(context).size.width*0.45,
                    child: Column(
                      children: [
                        Align(
                          alignment: Alignment.topLeft,
                          child: Padding(
                            padding: const EdgeInsets.only(left: 8.0, top: 26),
                            child: Text(allfavoriteList[i].MovieName,
                                style: TextStyle(
                                    color: Colors.white,
                                    fontFamily: 'Helvetica Neue',
                                    fontSize: 14.5,
                                    fontWeight: FontWeight.bold)),
                          ),
                        ),
                        Align(
                          alignment: Alignment.topLeft,
                          child: Padding(
                            padding: const EdgeInsets.only(left: 8.0, top: 5),
                            child: Text("Director "+allfavoriteList[i].Director,
                                style: TextStyle(
                                    color: Colors.white,
                                    fontSize: 12.5,
                                    fontWeight: FontWeight.bold)),
                          ),
                        ),
                        Align(
                          alignment: Alignment.topLeft,
                          child: Padding(
                            padding: const EdgeInsets.only(left: 8.0, top: 4),
                            child: Text("Duration "+allfavoriteList[i].Duration+" minutes",
                                style: TextStyle(
                                    color: Colors.white,
                                    fontSize: 13.5,
                                    fontWeight: FontWeight.bold)),
                          ),
                        ),
                      ],
                    ),
                  ),
                  Container(
                    width: MediaQuery.of(context).size.width*0.20,
                    child: GestureDetector(
                      onTap: (){
                        _removefavorite(allfavoriteList[i].VideoID);
                      },
                      child: Align(
                        alignment: Alignment.topRight,
                        child: Padding(
                          padding:
                              const EdgeInsets.only(left: 0.0, top: 25,right: 15),
                          child: Icon(
                            Icons.delete,
                            color: Colors.white,
                            size: 24,
                          ),
                        ),
                      ),
                    ),
                  )
                ],
              ),
            ),
          );
        });
  }
  void _removefavorite(String videoId) async{

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
