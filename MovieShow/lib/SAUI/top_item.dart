import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:movieshow/Activity/AllMovieSerialDetailActivity.dart';
import 'package:movieshow/SAModel/home_data_model.dart';

class ListItemTop extends StatelessWidget {
  final TopMoviesDatum topMoviesData;
  const ListItemTop({Key? key, required this.topMoviesData}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      child: Container(
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
                    height: 108,
                    child: FadeInImage.assetNetwork(placeholder: "drawable/placeholder.png",
                      image: topMoviesData.thumbnailImage!,
                      imageErrorBuilder: (context,error,stackTrace)=>
                          Center(child: Image.asset("drawable/placeholder.png"),),)
                ),
                Container(
                    margin: EdgeInsets.only(top:1),
                    // height: 29,
                    decoration: BoxDecoration(
                      color: Colors.white,
                      borderRadius: BorderRadius.only(
                          bottomLeft: Radius.circular(5),bottomRight: Radius.circular(5)),
                    ),
                    width: double.infinity,
                    child: Center(child: FittedBox(child: Text(
                      topMoviesData.title,style: TextStyle(fontSize: 13),textAlign: TextAlign.center,)
                    ))),
              ],
            ),
          ),
        ),
      ),
      onTap: (){
        Get.to(AllMovieAndSerialDetails(movieId: topMoviesData.tblMovieId!));
        // print('Get.to(AllMovieAndSerialDetails(movieId:topMoviesList[position].tblMovieid))');
      },
    );
  }
}