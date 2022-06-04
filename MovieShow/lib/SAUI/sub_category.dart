import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:movieshow/Activity/MoreMoviesAndWebSeriesActivity.dart';
import 'package:movieshow/SAModel/category_model.dart';
import 'package:movieshow/SAUI/all_item.dart';


class CategoryUI extends StatelessWidget {
  final CategoryData categoryData;
  const CategoryUI({Key? key, required this.categoryData}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(child: Column(
      children: [
        Row(children: [
          SizedBox(width: 16,),
          Expanded(child: Text(categoryData.categoryName!=null?categoryData.categoryName!:'',style: TextStyle(fontSize: 15,color: Colors.white,
              fontFamily: 'Helvetica Neue',
              fontWeight: FontWeight.bold),)),
          categoryData.movieData.length>2?TextButton(onPressed: (){

             //Get.to(MoreMoviesAndWebSeriesActivty(SubCategoryID: allheadingList[i].SubCategoryID));
           // print('Get.to(MoreMoviesAndWebSeriesActivty(SubCategoryID: allheadingList[i].SubCategoryID))');
             Get.to(MoreMoviesAndWebSeriesActivty(SubCategoryID: categoryData.categoryId));
            }, child: Text('More',style: TextStyle(fontSize: 14,color: Colors.white,
              fontFamily: 'Helvetica Neue',
              fontWeight: FontWeight.bold),)):Container()
        ],),
        Container(height: 150,
          child: ListView.builder(itemBuilder: (context, index){
            return AllItem(allMovie: categoryData.movieData.elementAt(index));
          }, itemCount: categoryData.movieData.length, scrollDirection: Axis.horizontal,),)
      ],
    ),);
  }
}