import 'dart:ui';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:movieshow/Activity/AllMovieSerialDetailActivity.dart';
import 'package:movieshow/Activity/MoreMoviesAndWebSeriesActivity.dart';
import 'package:movieshow/SAApi/SA_API.dart';
import 'package:movieshow/SAModel/category_model.dart';
import 'package:movieshow/SAModel/home_data_model.dart';
import 'package:movieshow/SAModel/tabitem_model.dart';
import 'package:movieshow/SAUI/SACarousel.dart';
import 'package:movieshow/SAUI/all_category.dart';
import 'package:movieshow/SAUI/sub_category.dart';
import 'package:movieshow/SAUI/top_item.dart';


class SAContent extends StatefulWidget {
  final TabData tabData;
  const SAContent({Key? key, required this.tabData}) : super(key: key);

  @override
  _SAContentState createState() => _SAContentState();
}

class _SAContentState extends State<SAContent> {

  HomeDataModel? _homeDataModel;
  CategoryModel? _categoryModel;
  bool _isDataLoaded = false;
  @override
  void initState() {
    widget.tabData.categoryId=='1'?homeDataUpAPI({
      'user_id': '307',
      'menu_id': widget.tabData.categoryId
    }).then((value){
      if(value.status)
      setState(() {
        _homeDataModel = value.data;
        _isDataLoaded = true;
      });
    }):categoryWiseDataUpAPI({
      'menu_id': widget.tabData.categoryId
    }).then((value){
      if(value.status){
        setState(() {
          _categoryModel = value.data;
          _isDataLoaded = true;
        });
      }
    });

    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return _isDataLoaded?(widget.tabData.categoryId=='1'?
    HomePage(homeDataModel: _homeDataModel!)
    :CategoryPages(categoryModel: _categoryModel!)):
    Center(child: CircularProgressIndicator(strokeWidth: 7.4,),);
  }
}

class CategoryPages extends StatelessWidget {
  final CategoryModel categoryModel;
  const CategoryPages({Key? key, required this.categoryModel}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      child: ListView(children: _createCategoryList(context,categoryModel!.data),),
    );
  }

  Widget _createSlider(BuildContext context, List<SliderData> sliderData){
    List<Widget> list = [];
    sliderData.forEach((element) {
      list.add(GestureDetector(
        child: Image.network(element.image, fit: BoxFit.cover,),
        onTap: (){
          Get.to(AllMovieAndSerialDetails(movieId:element.movieId));
        },
      ));
    });
    return SACarousel(width: double.infinity, height: MediaQuery.of(context).size.height*0.24, list:list);
  }

  List<Widget>_createCategoryList(BuildContext context, List<CategoryData> categoryMovies){

    List<Widget> list = [];
    list.add(_createSlider(context, categoryModel.sliderData),);
    list.add(SizedBox(height: 16,));
    categoryMovies.forEach((element) {
      list.add(SizedBox(height: 16,));
      list.add(Container(height: 200, child: CategoryUI(categoryData: element),));
    });
    return list;
  }
}


class HomePage extends StatelessWidget {
  final HomeDataModel homeDataModel;
  const HomePage({Key? key, required this.homeDataModel}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    return Container(
      child: ListView(children: _createCategoryList(context,homeDataModel!.allCategoryMovies),),
    );
  }


  Widget _createSlider(BuildContext context, List<SliderDatum> dataList){
    List<Widget> list = [];
    dataList.forEach((element) {
      list.add(GestureDetector(
        child: Image.network(element.image, fit: BoxFit.cover,),
        onTap: (){
          // Get.to(AllMovieAndSerialDetails(movieId:topMoviesList[position].tblMovieid));
          Get.to(AllMovieAndSerialDetails(movieId:element.movieId));
        },
      ));
    });
    return SACarousel(width: double.infinity, height: MediaQuery.of(context).size.height*0.24, list:list);
  }

  List<Widget>_createCategoryList(BuildContext context, List<AllCategoryMovie> allCategoryMovies){
    allCategoryMovies = allCategoryMovies.where((element) => element.categoryName!=null).toList();
    List<Widget> list = [];
    list.add(_createSlider(context, homeDataModel!.sliderData),);
    list.add(SizedBox(height: 16,));
    list.add(_createTopCategory(homeDataModel!.topMoviesData));
    allCategoryMovies.forEach((element) {
      list.add(SizedBox(height: 16,));
      list.add(Container(height: 200, child: AllCategoryUI(categoryData: element),));
    });
    return list;
  }

  _createTopCategory(List<TopMoviesDatum> topMoviesData){
    List<TopMoviesDatum> topMoviesDataTemp = topMoviesData.where((element) => element.thumbnailImage!=null).toList();
    List<Widget> list = [];
    list.add(Row(children: [
      SizedBox(width: 16,),
      Expanded(child: Text(topMoviesData.first.title,style: TextStyle(fontSize: 15,color: Colors.white,
          fontFamily: 'Helvetica Neue',
          fontWeight: FontWeight.bold),)),
      TextButton(onPressed: (){
        Get.to(MoreMoviesAndWebSeriesActivty(SubCategoryID: topMoviesData.first.subCategoryId!
        ));
      }, child: Text('More',style: TextStyle(fontSize: 14,color: Colors.white,
          fontFamily: 'Helvetica Neue',
          fontWeight: FontWeight.bold),)),
    ],));
    list.add(Expanded(child: ListView.builder(itemBuilder: (context, index){
      return ListItemTop(topMoviesData: topMoviesDataTemp.elementAt(index));
    }, itemCount: topMoviesDataTemp.length, scrollDirection: Axis.horizontal,)));

    return Container(height: 200,
      child: Column(children: list,),);
  }
}








