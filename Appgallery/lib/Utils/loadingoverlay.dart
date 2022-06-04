

import 'package:flutter/material.dart';

class LoadingOverlay extends StatelessWidget {
  LoadingOverlay({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Dialog(
      backgroundColor: Colors.transparent,
      elevation: 0,
      child: Row(children: <Widget>[
        Expanded(
            flex: 45,
            child: Container()),
        Container(
          width: 50,
          height: 50,
          padding: const EdgeInsets.all(16),
          child: const CircularProgressIndicator(strokeWidth: 3.5,color: Colors.blue,),
          decoration: const BoxDecoration(
              color: Colors.white,
              borderRadius: BorderRadius.all(Radius.circular(16))
          ),
        ),
        Expanded(
            flex: 45,
            child: Container()),
      ],),);
  }
}


