import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:trading_apps/api/apis.dart';
import 'package:trading_apps/custom_ui/common_widget.dart';
import 'package:trading_apps/models/support_model.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/res/constants.dart';
import 'package:url_launcher/url_launcher.dart';

class ClientSupport extends StatefulWidget {
  const ClientSupport({Key? key}) : super(key: key);

  @override
  _ClientSupportState createState() => _ClientSupportState();
}

class _ClientSupportState extends State<ClientSupport> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Client Support'),
      elevation: 0,
      backgroundColor: Colors.white12,),
      backgroundColor: APP_PRIMARY_COLOR,
      body: Container(
        padding: EdgeInsets.all(16),
        child: FutureBuilder(
            future: supportInfoAPI(),
            builder: (BuildContext context, AsyncSnapshot<ApiResponse>snapshot){
          if(!snapshot.hasData)return Center(child: CircularProgressIndicator(),);
          if(!snapshot.data!.status)return Center(child: Text('Something went wrong', style: textStyleLabel(color: Colors.white),),);
          SupportModel model = snapshot.data!.data;
          return Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
            // _listView(model.contactNo),

            Text('E-mail', style: textStyleLabel(color: Colors.white),),
            TextButton(onPressed: (){

              String? encodeQueryParameters(Map<String, String> params) {
                return params.entries
                    .map((e) => '${Uri.encodeComponent(e.key)}=${Uri.encodeComponent(e.value)}')
                    .join('&');
              }

              final Uri emailLaunchUri = Uri(
                scheme: 'mailto',
                path: model.email,
                query: encodeQueryParameters(<String, String>{
                  'subject': 'Support email'
                }),
              );

              _launchURL(emailLaunchUri.toString());

            }, child: Text(model.email, style: textStyleHeading(color: Colors.white),)),

              SizedBox(height: 40,),

              Divider(color: APP_SECONDARY_COLOR.withOpacity(0.2),),
              Padding(padding: EdgeInsets.only(top: 8, bottom: 8),
              child: Text('Contact via social network', style: textStyleLabel(color: Colors.white),),),
              Divider(color: APP_SECONDARY_COLOR.withOpacity(0.2),),
              SizedBox(height: 24,),
              Row(
                mainAxisSize: MainAxisSize.min,
                children: [
                  IconsButton(icon: FontAwesomeIcons.facebookF, onSelect: () {
                    _launchURL(model.facebook);
                  },),
                  SizedBox(width: 8,),
                  IconsButton(icon: FontAwesomeIcons.instagram, onSelect: () {
                    _launchURL(model.instagram);
                  }, ),

                  SizedBox(width: 8,),
                  IconsButton(icon: FontAwesomeIcons.telegram, onSelect: () {
                    _launchURL(model.telegram);
                  }, ),

                  SizedBox(width: 8,),
                  IconsButton(icon: FontAwesomeIcons.twitter, onSelect: () {
                    _launchURL(model.twitter);
                  }, ),
                ],)
          ],);
        }),

      ),
    );
  }

  Widget _listView(List<ContactNo> contactNo){
    List<Widget>list = [];
    contactNo.forEach((element) {
      list.add(Text(element.locationName, style: textStyleLabel(color: Colors.white),));
        list.add(TextButton(onPressed: (){
          String contact = 'tel:${element.contactNumber}';
          _launchURL(contact);
        }, child: Text(element.contactNumber, style: textStyleHeading(color: Colors.white),)));
        list.add(SizedBox(height: 40,));
    });

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: list,);
  }

  void _launchURL(_url) async =>
      await canLaunch(_url) ? await launch(_url) : throw 'Could not launch $_url';
}
