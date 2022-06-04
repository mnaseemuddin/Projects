




import 'package:flutter/material.dart';

class AlertDialogManager{
  void IsSuccessAlertDialog(BuildContext ctx,String title,String msg,String OtherNavigator){

    showDialog(
      context: ctx,
      builder: (BuildContext context) {
        // return object of type Dialog
        return AlertDialog(
        
          content: new Text(msg),
          actions: <Widget>[

            new TextButton(
              child: new Text("OK"),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),
            // usually buttons at the bottom of the dialog
            new TextButton(
              child: new Text("Cancel"),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),
          ],
        );
      },
    );
  }

  // ignore: non_constant_identifier_names
  void IsErrorAlertDialog(BuildContext ctx,String title,String msg,String OtherNavigator){

    showDialog(
      context: ctx,
      builder: (BuildContext context) {
        // return object of type Dialog
        return AlertDialog(
          title: new Text(title),
          content: new Text(msg),
          actions: <Widget>[
            // usually buttons at the bottom of the dialog
            new TextButton(
              child: new Text("OK"),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),
          ],
        );
      },
    );
  }

  }
