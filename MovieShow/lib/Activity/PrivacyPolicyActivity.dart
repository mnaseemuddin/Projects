



import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:webview_flutter/webview_flutter.dart';

class PrivacyPolicyActivity extends StatefulWidget {
  const PrivacyPolicyActivity({Key? key}) : super(key: key);

  @override
  _PrivacyPolicyActivityState createState() => _PrivacyPolicyActivityState();
}

class _PrivacyPolicyActivityState extends State<PrivacyPolicyActivity> {

  var _key=UniqueKey();
  @override
  Widget build(BuildContext context) {
    return Scaffold(

      appBar: AppBar(
        backgroundColor: Colors.black,
        title: Text("Privacy Policy",),
      ),
      body: Container(
        height: double.infinity,
        child: WebView(
          initialUrl: "http://show.movie/privacy.php",
          key: _key,
          javascriptMode: JavascriptMode.unrestricted,
        ),
      ),
    );
  }
}
