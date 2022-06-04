


import 'package:flutter/material.dart';

class TextActivity extends StatefulWidget {
  const TextActivity({Key? key}) : super(key: key);

  @override
  _TextActivityState createState() => _TextActivityState();
}

class _TextActivityState extends State<TextActivity> {
  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    return Scaffold(
      body: Container(
        color: Colors.white,
        height: double.infinity,
        child: ListView(
          children: [
            Container(
              color: Colors.blue,
              height: size.height/2.5,
            ),
            Container(
              color: Colors.black,
              height: size.height/2.5,
            )
          ],
        ),
      ),
    );
  }
}
