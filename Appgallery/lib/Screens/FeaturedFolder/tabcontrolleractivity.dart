
import 'package:appgallery/Screens/BNavigationFolder/featuredactivity.dart';
import 'package:appgallery/Screens/FeaturedFolder/appsrankingactivity.dart';
import 'package:flutter/material.dart';

import 'gamingrankingactivity.dart';

class TabControllerActivity extends StatefulWidget {
  const TabControllerActivity({Key? key}) : super(key: key);

  @override
  _TabControllerActivityState createState() => _TabControllerActivityState();
}

class _TabControllerActivityState extends State<TabControllerActivity> {
  @override
  Widget build(BuildContext context) {
    return DefaultTabController(
      initialIndex: 0,
      length: 3,
      child: Scaffold(
        backgroundColor: Color(0xfff1f3f5),
        appBar: PreferredSize(
          preferredSize: Size.fromHeight(50),
          child: Padding(
            padding: const EdgeInsets.only(top:40.0,right: 0),
            child: TabBar(
              labelPadding: EdgeInsets.fromLTRB(0, 0, 0, 0),
              indicatorWeight: 0.1,
              indicatorColor: Color(0xfff1f3f5),
              unselectedLabelColor: Colors.grey[600],
              labelColor: Colors.black87,
              labelStyle: const TextStyle(fontSize: 16,color: Colors.black54,
                  fontWeight: FontWeight.w400),
              tabs: const [
                Tab(text: 'Featured'),
                Tab(text: 'Games Ranking'),
                Tab(text: 'Apps Ranking'),
              ],),
          ),

        ),
          body: const TabBarView(
            children: [
              FeaturedActivity(), GamingRankingActivity(), AppsRankingActivity()
            ],
          )
      ),
    );
  }
}

