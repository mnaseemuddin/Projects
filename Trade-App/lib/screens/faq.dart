import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/res/constants.dart';
import 'package:trading_apps/res/strings.dart';
import 'package:trading_apps/screens/HowToTradeActivity.dart';
import 'package:trading_apps/screens/faq/client_support.dart';
import 'package:trading_apps/screens/faq/help_center.dart';
import 'package:trading_apps/utility/common_methods.dart';
import 'package:url_launcher/url_launcher.dart';

class FAQuestion extends StatefulWidget {
  const FAQuestion({Key? key}) : super(key: key);

  @override
  _FAQuestionState createState() => _FAQuestionState();
}

class _FAQuestionState extends State<FAQuestion> {

  @override
  void initState() {
    SystemChrome.setSystemUIOverlayStyle(SystemUiOverlayStyle.light);
    super.initState();
  }

  @override
  void dispose() {
    // TODO: implement dispose
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Answer the questions'),
        elevation: 0,
      ),
      backgroundColor: APP_PRIMARY_COLOR  ,
      body: Container(padding: EdgeInsets.all(16),
        child: ListView(
          children: FAQ_ITEMS.map((e) => Container(
            child: ListTile(title: Text(e.title, style: textStyleHeading2(color: Colors.white),),
            subtitle: Text(e.subtitle, style: textStyleLabel(color: Colors.white),),
              leading: Icon(e.icon, color: Colors.white,),
              trailing: Icon(Icons.navigate_next, color: Colors.white,),
              onTap: (){
                _onPressed(e.id);
              },
            ),

            decoration: BoxDecoration(
                border: Border(bottom: BorderSide(color: APP_SECONDARY_COLOR.withOpacity(0.1), width: 1))
            ),
          )).toList(),
        ),
      ),
    );
  }

  _onPressed(int id){
    switch(id){
      case 1:
        navPush(context, ClientSupport());
        break;
      case 2:
        navPush(context, HelpCenter());
        break;
      case 3:
       navPush(context,HowToTrade());
         //_launchURL('http://tradingclub.fund/privacy_policy');
        break;
    }
  }

  void _launchURL(_url) async =>
      await canLaunch(_url) ? await launch(_url) : throw 'Could not launch $_url';
}
