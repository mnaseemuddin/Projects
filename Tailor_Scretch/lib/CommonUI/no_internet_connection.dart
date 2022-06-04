


import 'dart:io';

import 'package:flutter/material.dart';
class InternetConnection extends StatefulWidget {
  const InternetConnection({Key? key}) : super(key: key);

  @override
  _InternetConnectionState createState() => _InternetConnectionState();
}

class _InternetConnectionState extends State<InternetConnection> {
  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: (){
                      showDialog(
                context: context,
                builder: (BuildContext context) {
                  // return object of type Dialog
                  return AlertDialog(
                    title: Text("Exit?"),
                    content: new Text("Are you sure you want to exit?"),
                    actions: <Widget>[
                      new TextButton(
                        child: new Text(
                          "CANCEL",
                          style: TextStyle(
                            color: Colors.red,
                            fontFamily: 'Helvetica Neue',
                            fontSize: 14.899999618530273,
                          ),
                        ),
                        onPressed: () {
                          Navigator.of(context).pop();
                        },
                      ),

                      new TextButton(
                        child: new Text(
                          "OK",
                          style: TextStyle(
                            color: Colors.red,
                            fontFamily: 'Helvetica Neue',
                            fontSize: 14.899999618530273,
                          ),
                        ),
                        onPressed: () {
                          exit(0);
                        },
                      ),
                      // usually buttons at the bottom of the dialog
                    ],
                  );
                },
              );
              return Future.value(false);
      },
      child: Scaffold(
        body: Container(
          margin: EdgeInsets.all(10),
          padding: EdgeInsets.all(20),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              Container(
                height: 200,
                width: 200,
                margin: EdgeInsets.fromLTRB(0, 0, 0, 25),
                decoration: BoxDecoration(
                  image: DecorationImage(
                    image: AssetImage("assets/nointernet.png"),
                    fit: BoxFit.fill,
                  ),
                ),
              ),
              Text(
                "No Internet Connection",
                style: TextStyle(
                  fontSize: 20,
                  fontWeight: FontWeight.bold,
                ),
              ),
              Padding(
                padding: EdgeInsets.all(15),
                child: Text(
                  "You are not connected to the internet. Make sure Wi-Fi is on, Airplane Mode is Off and try again.",
                  style: TextStyle(
                    fontSize: 16,
                  ),
                ),
              )
            ],
          ),
        ),
      ),
    );
  }
}
