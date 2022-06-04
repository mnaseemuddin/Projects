import 'dart:io';

import 'package:flutter/material.dart';
import 'package:webview_flutter/webview_flutter.dart';

class SHWebView extends StatefulWidget {
  final String url;

  const SHWebView({Key? key, required this.url}) : super(key: key);
  @override
  _SHWebViewState createState() => _SHWebViewState();
}

class _SHWebViewState extends State<SHWebView> {

  bool _isLoading = true;

  @override
  void initState() {
    super.initState();
    if (Platform.isAndroid) WebView.platform = AndroidWebView();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        elevation: 0,
      ),
      body: Stack(children: [
        WebView(

          onPageFinished: (val){
            setState(() {
              _isLoading = false;
            });
          },
          initialUrl: widget.url,
        ),
        _isLoading?Column(children: [
          LinearProgressIndicator(),
          Expanded(child: SizedBox())
        ],):SizedBox(height: 1,)
      ],),
    );
  }
}