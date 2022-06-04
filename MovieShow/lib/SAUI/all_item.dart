import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:movieshow/Activity/AllMovieSerialDetailActivity.dart';
import 'package:movieshow/SAModel/home_data_model.dart';


class AllItem extends StatelessWidget {
  final AllMovie allMovie;
  const AllItem({Key? key, required this.allMovie}) : super(key: key);

  @override
  Widget build(BuildContext context) {

    return GestureDetector(
      child: Container(
        child: Padding(
          padding: const EdgeInsets.all(8.0),
          child: Container(
            width: 195,
            height: 164,
            decoration: BoxDecoration(
              borderRadius: BorderRadius.circular(5.0),
              border: Border.all(width: 1.0, color: Colors.black),
            ),
            child: Column(
              children: [
                Container(
                    width: 195,
                    height: 108,
                    child: FadeInImage.assetNetwork(placeholder: "drawable/placeholder.png",
                      image: allMovie.thumbnailImage!,
                      imageErrorBuilder: (context,error,stackTrace)=>
                          Center(child: Image.asset("drawable/placeholder.png"),),)
                ),
                Container(
                    margin: EdgeInsets.only(top:1),
                    // height: 29,
                    padding: EdgeInsets.all(3),
                    decoration: BoxDecoration(
                      color: Colors.white,
                      borderRadius: BorderRadius.only(
                          bottomLeft: Radius.circular(5),bottomRight: Radius.circular(5)),
                    ),
                    width: double.infinity,
                    child: Center(child: FittedBox(child: Text(
                      allMovie.movieName,style: TextStyle(fontSize: 13),textAlign: TextAlign.center,),))),
              ],
            ),
          ),
        ),
      ),
      onTap: (){
        // Get.to(AllMovieAndSerialDetails(movieId:topMoviesList[position].tblMovieid));
      //  print('Get.to(AllMovieAndSerialDetails(movieId:topMoviesList[position].tblMovieid))');
        Get.to(AllMovieAndSerialDetails(movieId: allMovie.tblMovie!));
      },
    );
  }
}