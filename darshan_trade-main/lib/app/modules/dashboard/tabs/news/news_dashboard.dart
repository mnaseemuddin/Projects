import 'dart:math';
import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter_vector_icons/flutter_vector_icons.dart';
import 'package:get/get.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/shared/shared.dart';
import 'package:royal_q/main.dart';
import 'package:transparent_image/transparent_image.dart';
import '../../dashboard.dart';


class NewsDashboard extends StatefulWidget {
  const NewsDashboard({Key? key}) : super(key: key);

  @override
  _NewsDashboardState createState() => _NewsDashboardState();
}

class _NewsDashboardState extends State<NewsDashboard> {

  List<NewsResponse>? _listNews;

  @override
  void initState() {
    super.initState();
    newsAPI().then((value){
      if(mounted){
        setState(() {
          if(value.status){
            _listNews = value.data;
          }
        });
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar:  AppBar(
        backgroundColor: Colors.transparent,
        title: Text('News',style: TextStyle(color: ColorConstants.iconTheme),),
        centerTitle: true,
        elevation: 0,

        // brightness: Brightness.dark,
        iconTheme: IconThemeData(color: ColorConstants.iconTheme),
      ),
      body: Container(
        padding: EdgeInsets.symmetric(horizontal: 8),
        child: _listNews==null?NoRecord()
          : ListView.builder(itemBuilder: (context, index){
        NewsResponse response = _listNews!.elementAt(index);
        return SACellContainer(child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            TextButton.icon(onPressed: (){}, icon: Icon(_listNews!.elementAt(index).heading=='System Information'?Icons.system_update_alt_outlined:Icons.list_alt, color: ColorConstants.APP_SECONDARY_COLOR,), label: Text(_listNews!.elementAt(index).heading, style: textStyleHeading2(),)),
            Divider(),
            SizedBox(height: 8,),
            Text(response.news)
          ],
        ));
      }, itemCount: _listNews?.length,),),
    );
  }

}
