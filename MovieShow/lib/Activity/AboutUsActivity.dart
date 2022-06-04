



import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:webview_flutter/webview_flutter.dart';

class AboustUsActivity extends StatefulWidget {
  const AboustUsActivity({Key? key}) : super(key: key);

  @override
  _AboustUsActivityState createState() => _AboustUsActivityState();
}

class _AboustUsActivityState extends State<AboustUsActivity> {

  var _key=UniqueKey();
  @override
  Widget build(BuildContext context) {
    return Scaffold(

      appBar: AppBar(
        backgroundColor: Colors.black,
        title: Text("About Us",),
      ),
      body: Container(
        height: double.infinity,
        child: WebView(
          initialUrl: "http://show.movie/about_us.php",
          key: _key,
          javascriptMode: JavascriptMode.unrestricted,
        ),
      ),
    );
  }
}
