



import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

import '../../shared/utils/navigator_helper.dart';
import 'modules/AffilateDashBoardView.dart';


class AffiliatePackageView extends StatefulWidget {
  const AffiliatePackageView({Key? key}) : super(key: key);

  @override
  State<AffiliatePackageView> createState() => _AffiliatePackageViewState();
}

class _AffiliatePackageViewState extends State<AffiliatePackageView> {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Stack(children: [
        Container(
            height: double.infinity,
            child: Image.asset('assets/expgain/package.png',height: double.infinity,width: double.infinity,fit: BoxFit.fill,)),
        Padding(
          padding: const EdgeInsets.only(top:35.0,right: 10),
          child: Align(
              alignment: Alignment.topRight,
              child: TextButton(onPressed: () {
                navPush(context,AffiliateDashBoardView());
              }, child: Text("SKIP",style: GoogleFonts.rajdhani(fontSize: 20,color:
              Colors.redAccent,fontWeight: FontWeight.w600)),)),
        )
      ],),
    );
  }
}
