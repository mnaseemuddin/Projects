import 'package:flutter/material.dart';

class AFBg extends StatelessWidget {
  final Widget child;
  const AFBg({Key? key, required this.child}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    return Container(
      child: child,
      width: size.width, height: size.height,
      decoration: BoxDecoration(
        gradient: LinearGradient(colors: [Color(0xFF222C35),Color(0xFF0A1319), ])
        //   gradient: LinearGradient(colors: [Color(0xFF3A714C),Color(0xFF000000), ]),
        // image: DecorationImage(image: AssetImage('assets/expgain/background_gradient.png',),fit: BoxFit.fill)
      ),
    );
  }
}
