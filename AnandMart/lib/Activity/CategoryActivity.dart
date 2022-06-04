




import 'dart:convert';

import 'package:anandmart/Activity/AllCategoriesActivity.dart';
import 'package:anandmart/ApplicationConfigration/ApiUrls.dart';
import 'package:anandmart/Model/AllMainCategories.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:http/http.dart'as http;

class CategoryActivity extends StatefulWidget {
  const CategoryActivity({Key? key}) : super(key: key);

  @override
  _CategoryActivityState createState() => _CategoryActivityState();
}

class _CategoryActivityState extends State<CategoryActivity> {

 late Map data;
  List<AllMainCategories> allMainCategories=[];
bool IsLoading=true;

  @override
  void initState() {
    getAllMainCategories();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
         brightness: Brightness.dark,
        backgroundColor: Color(0xfff4811e),
        title: Text("Category",style: GoogleFonts.aBeeZee(fontSize: 16,color:Colors.white),),
      ),
      body: IsLoading==false?ListView(
        children: [
          Padding(
            padding: const EdgeInsets.only(left:15.0,right: 8,top: 8,bottom: 8),
            child: Text('Shop By Category >',style:
            GoogleFonts.aBeeZee(fontSize: 14,color: Colors.black),),
          ),
          MainCategoriesUI(),
        ],
      ):Center(child: CircularProgressIndicator(strokeWidth: 6.0,color: Color(0xfff4811e),)),
    );
  }
  getAllMainCategories() async{
    final response=await http.get(Uri.parse(ApiUrls.AllMainCategoriesAPI));
    data=json.decode(response.body);
    setState(() {
      IsLoading=false;
      if(data['response']=='success') {
        var res = data["data"] as List;
        allMainCategories =res.map((json) => AllMainCategories.formJson(json)).toList();
      }
    });
  }
 Widget MainCategoriesUI() {
   return Padding(
     padding: const EdgeInsets.all(8.0),
     child: Container(
       height: MediaQuery.of(context).size.height*0.90,
       child: GridView.builder(
           shrinkWrap: true,
           itemCount: allMainCategories.length==null?0:allMainCategories.length,
           gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
             mainAxisSpacing: 10,
             crossAxisCount: 3,
           ), itemBuilder: (context,i){
         // movieIdList.clear();
         // movieIdList.add(allMoreMovieList[i].MovieID);
         return GestureDetector(
           onTap: (){
             Get.to(AllCategoriesActivity(CategoryName: allMainCategories[i].MainCategoryName,CategoryID:
             allMainCategories[i].MainCategoryID,));
           },
           child: Container(
             width: double.infinity,
             height: 200,
             child: Wrap(
               children: [
                 Padding(
                   padding: const EdgeInsets.all(8.0),
                   child: Container(
                       height: 97,
                       child: new Image.network(allMainCategories[i].MainCategoryImageUrl,
                         height: double.infinity,
                         errorBuilder: (context,error,stackTrace)=>
                             Center(child: Image.asset("drawable/placeholder.png"),),
                         fit: BoxFit.fill,)
                   ),
                 ),
                 Padding(
                   padding: const EdgeInsets.all(8.0),
                   child: Center(
                     child: Text(allMainCategories[i].
                     MainCategoryName,textAlign: TextAlign.center,style: GoogleFonts.aBeeZee(
                         color: Colors.black,fontSize: 10
                     ),),
                   ),
                 ),
               ],
             ),
           ),
         );
       }),
     ),
   );
 }
}
