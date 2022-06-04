



import 'dart:io';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:syncfusion_flutter_pdfviewer/pdfviewer.dart';
import 'package:webview_flutter/webview_flutter.dart';

class PrivacyPolicyActivity extends StatefulWidget {

   final String tilte,url;


  const PrivacyPolicyActivity({Key? key,required this.tilte,required this.url}) : super(key: key);

  @override
  _PrivacyPolicyActivityState createState() => _PrivacyPolicyActivityState();

}

class _PrivacyPolicyActivityState extends State<PrivacyPolicyActivity> {

 String Url="http://tradingclub.fund/privacy_policy";
  var _key=UniqueKey();
 final GlobalKey<SfPdfViewerState> _pdfViewerKey = GlobalKey();

 

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.black,
        title: Text(widget.tilte,style: TextStyle(fontSize:16,),
      )),
      body: SfPdfViewer.asset(widget.url,key: _pdfViewerKey,)
    );
  }
}
