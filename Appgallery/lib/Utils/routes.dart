



import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';

navPush(context,screen)=>Navigator.of(context).push(MaterialPageRoute(builder: (context)=>
screen));


navPushOnRefresh(context,screen)=>Navigator.of(context).push(MaterialPageRoute(builder:
    (_) => screen)).then((value) =>{
value?Function:null
});

navPushReplace(context, screen) => Navigator.of(context).pushReplacement(MaterialPageRoute(builder:
    (context) => screen));

navReturn(context)=>Navigator.pop(context);

keyboardDismissed(context)=> FocusScope.of(context).unfocus();

toast(msg)=>Fluttertoast.showToast(msg: msg);