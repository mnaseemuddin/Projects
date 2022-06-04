


import 'package:flutter/material.dart';
import 'package:trading_apps/api/apis.dart';
import 'package:trading_apps/custom_ui/common_widget.dart';
import 'package:trading_apps/models/user_guide_model.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/res/constants.dart';
import 'package:trading_apps/screens/faq/question_answers.dart';
import 'package:trading_apps/utility/common_methods.dart';

class HelpCenter extends StatelessWidget {
  const HelpCenter({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(title: Text('Help Center'),
    elevation: 0,
    backgroundColor: Colors.white12,),
    backgroundColor: APP_PRIMARY_COLOR,
    body: Container(
      margin: EdgeInsets.all(8),
      child: FutureBuilder(
          future: userGuideAPI(),
          builder: (BuildContext context, AsyncSnapshot<ApiResponse>snapshot){
        if(!snapshot.hasData)return Center(child: CircularProgressIndicator(),);
        if(!snapshot.data!.status) return Center(child: Text('Something went wrong'),);
        UserGuideModel model = snapshot.data!.data;
        return ListView.builder(itemBuilder: (context, index){
          return Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
            Padding(padding: EdgeInsets.only(left: 8, top: 20),
            child: Text(model.userGuidedata.elementAt(index).catname, style: textStyleHeading(color: Colors.white),),),
            GridView.count(crossAxisCount: 2,
              children: _createChildren(context, model.userGuidedata.elementAt(index)),
              shrinkWrap : true,
              physics: NeverScrollableScrollPhysics(),
            )
          ],);
        },itemCount: model.userGuidedata.length,);
      })
    ));
  }

  List<Widget> _createChildren(BuildContext context, UserGuidedatum data){
    List<Widget> list = [];

    data.data.forEach((element) {
      list.add(GestureDetector(
        child: SettingItemBg(
            margin: EdgeInsets.all(8),
            padding: EdgeInsets.all(12),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Image.network(element.icon, width: 36, color: APP_SECONDARY_COLOR,),
                Expanded(child: Container()),
                Text(element.name, style: textStyleLabel(color: Colors.white),)
              ],)),
        onTap: (){
          navPush(context, QuestionAnswers(id: element.tblTutorialCategoryId, category: element.name,));
        },
      ));
    });
    return list;
  }
}
