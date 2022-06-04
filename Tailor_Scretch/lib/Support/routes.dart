


import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

navPush(context,screen)=>Navigator.of(context).push(MaterialPageRoute(builder: (context)=>
screen));


navPushOnRefresh(context,screen)=>Navigator.of(context).push(new MaterialPageRoute(builder:
    (_) => screen)).then((value) =>{
  value?Function:null
});

navPushReplace(context, screen) => Navigator.of(context).pushReplacement(MaterialPageRoute(builder:
    (context) => screen));


navDialogDismiss(context)=> Navigator.of(context).pop();