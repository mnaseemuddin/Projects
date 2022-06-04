
import 'package:flutter/material.dart';
import 'package:skillcoin/customui/button.dart';
import 'package:skillcoin/customui/container.dart';
import 'package:url_launcher/url_launcher.dart';

import '../api/apirepositary.dart';
import '../customui/textview.dart';
import '../model/liveratemodel.dart';
import '../model/tdccurrencymodel.dart';
import '../res/color.dart';

class DetailsPage extends StatefulWidget {

  final int sKLCoins;
  String address,timeDate,paymentType,hash;

   DetailsPage({Key? key,required this.sKLCoins,required this.address,
     required this.timeDate,required this.paymentType,required this.hash}) : super(key: key);

  @override
  _DetailsPageState createState() => _DetailsPageState();
}

class _DetailsPageState extends State<DetailsPage> {

  double _currencyValue=0.0;
  String symbol="";
  Color? colors;

  @override
  void initState() {
   setState(() {
     if(widget.paymentType=="withdrawal"||widget.paymentType=="sell"){
       symbol="-";
       colors=Colors.red;
     }else{
       symbol="+";
       colors=Colors.green;
     }
   });
    _getLiveTDCAPI();
    super.initState();
  }

  _getLiveTDCAPI(){
    liveSKLCoinRateAPI().then((value){
      if(value.status){
        LiveRateModel model=value.data;
        if(mounted) {
          setState(() => _currencyValue =model.data.liveRate.toDouble());
        }
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: PRIMARY_APP_COLOR,
      appBar: AppBar(

        iconTheme: const IconThemeData(color: Color(0xff9c9b9b)),
        title: const GoogleFontHeadingText(title: 'Transaction Details',fontSize: 14,color: PRIMARYBLACKCOLOR,),
      ),
      body: Column(
        children:  [

          Align(
             alignment: Alignment.center,
             child: Padding(
               padding: EdgeInsets.only(top:8.0,bottom: 2),
               child: Text("$symbol ${widget.sKLCoins.toDouble()} SKL",
                   style: TextStyle(fontSize: 24, color: colors,fontWeight: FontWeight.w800),),
             )),

           Align(
              alignment: Alignment.center,
              child: NormalHeadingText(title: "= USDT ${widget.sKLCoins*_currencyValue}",
                  fontSize: 13, color: PRIMARYLIGHTGREYCOLOR)),

          Padding(
            padding: const EdgeInsets.all(15.0),
            child: ContainerBgRCircle(width: double.infinity, height: 130, color: PRIMARYWHITECOLOR,
                circular: 15, child: Column(children:   [

                  Padding(
                    padding: const EdgeInsets.fromLTRB(10.0,10,10,0),
                    child: Row(
                      children:  [
                        NormalHeadingText(title: "Date", fontSize: 15, color: PRIMARYBLACKCOLOR),
                        Spacer(),
                        NormalHeadingText(title: widget.timeDate, fontSize: 15,
                            color: PRIMARYLIGHTGREYCOLOR),
                      ],
                    ),
                  ),
                  const Padding(
                    padding: EdgeInsets.only(left:10.0,right: 10),
                    child: Divider(),
                  ),
                  Padding(
                    padding: const EdgeInsets.fromLTRB(10.0,10,10,0),
                    child: Row(
                      children: const [
                        NormalHeadingText(title: "Status", fontSize: 15, color: PRIMARYBLACKCOLOR),
                        Spacer(),
                        NormalHeadingText(title: "Completed", fontSize: 15,
                            color: PRIMARYLIGHTGREYCOLOR),
                      ],
                    ),
                  ),
                  const Padding(
                    padding: EdgeInsets.only(left:10.0,right: 10),
                    child: Divider(),
                  ),
                  Padding(
                    padding: const EdgeInsets.fromLTRB(10.0,10,10,0),
                    child: Row(
                      children:  [
                        const Expanded(
                            flex:1,
                            child: NormalHeadingText(title: "Recipient", fontSize: 15,
                                color: PRIMARYBLACKCOLOR)),
                        Expanded(
                          flex:1,
                          child: Text(widget.address,overflow: TextOverflow.ellipsis,style: TextStyle(fontSize: 15,
                              color: PRIMARYLIGHTGREYCOLOR),maxLines: 1,),
                        ),
                      ],
                    ),
                  ),
                  
                ],)),
          ),


          Padding(
            padding: const EdgeInsets.all(15.0),
            child: ContainerBgRCircle(width: double.infinity, height: 40, color: PRIMARYWHITECOLOR,
                circular: 15, child: Column(children:   [

                  Padding(
                    padding: const EdgeInsets.fromLTRB(10.0,10,10,0),
                    child: Row(
                      children: const [
                        NormalHeadingText(title: "Network fees", fontSize: 15, color: PRIMARYBLACKCOLOR),
                        Spacer(),
                        NormalHeadingText(title: "0.0000SKL(USDT${0.0})", fontSize: 15,
                            color: PRIMARYLIGHTGREYCOLOR),
                      ],
                    ),
                  ),

                ],)),
          ),

          SubmitButton(onPressed: (){
            if(widget.paymentType=="buy"||widget.paymentType=="sell") {
              _launchURL("https://tronscan.org/#/transaction/${widget.hash}");
            }else{
              _launchURL("https://bscscan.com/address/${widget.hash}");
            }
          }, name: "View Details", colors: 0xfffbd536,
              textColor: PRIMARYWHITECOLOR, width: double.infinity, circular: 15)

      ],),
    );
  }

  void _launchURL(_url) async =>
      await canLaunch(_url) ? await launch(_url) : throw 'Could not launch $_url';

}


