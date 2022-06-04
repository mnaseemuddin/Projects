



import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:webview_flutter/webview_flutter.dart';

class TermsConditionActivity extends StatefulWidget {
  const TermsConditionActivity({Key? key}) : super(key: key);

  @override
  _TermsConditionActivityState createState() => _TermsConditionActivityState();
}

class _TermsConditionActivityState extends State<TermsConditionActivity> {

  var _key=UniqueKey();
  @override
  Widget build(BuildContext context) {
    return Scaffold(

      appBar: AppBar(
        backgroundColor: Colors.black,
        title: Text("Terms Conditions",),
      ),
      body: Container(
        height: double.infinity,
        child: WebView(
          initialUrl: "http://show.movie/Terms_Conditions.php",
          key: _key,
          javascriptMode: JavascriptMode.unrestricted,
        ),
      ),
    );
  }
}
