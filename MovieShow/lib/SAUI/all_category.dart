import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:movieshow/Activity/MoreMoviesAndWebSeriesActivity.dart';
import 'package:movieshow/SAModel/home_data_model.dart';
import 'package:movieshow/SAUI/all_item.dart';


class AllCategoryUI extends StatelessWidget {
  final AllCategoryMovie categoryData;
  const AllCategoryUI({Key? key, required this.categoryData}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(child: Column(
      children: [
        Row(children: [
          SizedBox(width: 16,),
          Expanded(child: Text(categoryData.categoryName!=null?
          categoryData.categoryName!:'',style: TextStyle(fontSize: 15,color: Colors.white,
              fontFamily: 'Helvetica Neue',
              fontWeight: FontWeight.bold),)),
          categoryData.allMovies.length>2?TextButton(onPressed: (){

          }, child: GestureDetector(
            onTap: (){
              Get.to(MoreMoviesAndWebSeriesActivty(SubCategoryID: categoryData.subCategoryId.toString()));
            },
            child: Text('More',style: TextStyle(fontSize: 14,color: Colors.white,
                fontFamily: 'Helvetica Neue',
                fontWeight: FontWeight.bold),),
          )):Container()
        ],),
        Container(height: 150,
          child: ListView.builder(itemBuilder: (context, index){
            return AllItem(allMovie: categoryData.allMovies.elementAt(index));
          }, itemCount: categoryData.allMovies.length, scrollDirection: Axis.horizontal,),)
      ],
    ),);
  }
}