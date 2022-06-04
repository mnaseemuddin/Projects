


import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:webview_flutter/webview_flutter.dart';


class AboutUsActivity extends StatefulWidget {
  final String title,Urls;
  const AboutUsActivity({Key? key,required this.title,required this.Urls}) : super(key: key);

  @override
  _AboutUsActivityState createState() => _AboutUsActivityState();
}

class _AboutUsActivityState extends State<AboutUsActivity> {
  var _key=UniqueKey();

  @override
  Widget build(BuildContext context) {
    // final Set<JavascriptChannel> jsChannels = [
    //   JavascriptChannel(
    //       name: 'Print',
    //       onMessageReceived: (JavascriptMessage message) {
    //         print(message.message);
    //       }),
    // ].toSet();
    return Scaffold(
    //  url: "https://flutter.io",

      appBar: AppBar(
         brightness: Brightness.dark,
        backgroundColor: Color(0xfff4811e),
        title: Text(widget.title,style: GoogleFonts.aBeeZee(fontSize: 16,color: Color(0xffffffff)),),
      ),
     body: Container(
       height: double.infinity,
       child: WebView(
         initialUrl: widget.Urls,
         key: _key,
         javascriptMode: JavascriptMode.unrestricted,
       ),
     ),
    );
  }
}
