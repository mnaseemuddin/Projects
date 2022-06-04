import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:movieshow/SAApi/SA_API.dart';
import 'package:movieshow/SAModel/tabitem_model.dart';
import 'package:movieshow/SAUI/SA_Content.dart';


class SAHome extends StatefulWidget {
  const SAHome({Key? key}) : super(key: key);

  @override
  _SAHomeState createState() => _SAHomeState();
}

class _SAHomeState extends State<SAHome> with TickerProviderStateMixin{
  TabItemModel? _tabItemModel;
  // TabController? _tabController;
  @override
  void initState() {
    getTabItems().then((value){
      if(value.status){
        setState(() {
          _tabItemModel = value.data;
          // _tabController = TabController(vsync: this, length: _tabItemModel!.data.length);
        });
      }
    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(child: _tabItemModel!=null?_createMainView():Center(child: CircularProgressIndicator(strokeWidth: 7.4,),));
  }

  Widget _createMainView() => DefaultTabController(length: _tabItemModel!.data.length, child: Scaffold(
    appBar: TabBar(
      isScrollable: true,
      indicatorColor: Colors.red,
      tabs: List<Widget>.generate(
          _tabItemModel!.data.length, (index){
        return Padding(
          padding: const EdgeInsets.all(8.0),
          child: Text(_tabItemModel!.data[index].categoryName,style: TextStyle(color: Colors.white,
              fontFamily: 'Helvetica Neue',
              fontWeight: FontWeight.w800,fontSize: 15)),
        );
      }),
    ),
    backgroundColor: Colors.black,
    body: TabBarView(
        children: List<Widget>.generate(_tabItemModel!.data.length, (int index){
          return SAContent(tabData: _tabItemModel!.data.elementAt(index),);
        })),
  ));
}
