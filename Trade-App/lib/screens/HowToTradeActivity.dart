import 'package:flutter/material.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/utility/common_methods.dart';

import 'dashboard.dart';

class HowToTrade extends StatefulWidget {
  const HowToTrade({Key? key}) : super(key: key);

  @override
  _HowToTradeState createState() => _HowToTradeState();
}

class _HowToTradeState extends State<HowToTrade> {
  PageController controller = PageController();
  List<Widget> widgetList = <Widget>[
    Container(
      child: Column(
        children: [
          Spacer(),
          Center(
            child: Image.asset(
              'assets/graduationhat.png',
              height: 200,
              width: 250,
              color:APP_SECONDARY_COLOR,
            ),
          ),
          Spacer(),
          Padding(
            padding: const EdgeInsets.all(25.0),
            child: Text(
              "Welcome to the online trading platform! We'll help you take the first steps.",
              style: TextStyle(color: Colors.white, fontSize: 16),
              textAlign: TextAlign.center,
            ),
          ),
          ElevatedButton(onPressed: () {}, child: Text(''),)
          // Padding(
          //   padding: const EdgeInsets.only(left:20.0,right: 20,bottom: 20),
          //   child: GestureDetector(
          //     onTap: (){
          //
          //     },
          //     child: Container(
          //       height: 45,
          //       width: double.infinity,
          //       decoration: BoxDecoration(
          //         color: Colors.blue,
          //         borderRadius: BorderRadius.circular(8)
          //       ),
          //       child: Center(child: Text('Start Training',style: TextStyle(fontSize: 15,color: Colors.white),)),
          //     ),
          //   ),
          // )
          // Chip(
          //   backgroundColor: Colors.blue,
          //   label: Text('Start Training'),elevation: 20,)
        ],
      ),
    ),
    Container(
      child: Center(child: Text('Trade Club')),
    )
  ];
  int currentPageIndex = 1;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        backgroundColor: APP_PRIMARY_COLOR,
        appBar: AppBar(
          backgroundColor: APP_PRIMARY_COLOR,
          leading: Container(),
          actions: [
            Row(
              children: [
                Padding(
                  padding: const EdgeInsets.fromLTRB(0, 0, 20, 0),
                  child: Text(
                    '$currentPageIndex/4',
                    style: TextStyle(color: Colors.white),
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.fromLTRB(0, 0, 20, 0),
                  child: GestureDetector(
                    onTap: (){
                      _bottomSheetfinishTraing();
                    },
                    child: Image.asset(
                      'assets/images/cancel.png',
                      color: Colors.white,
                      height: 17,
                      width: 17,
                    ),
                  ),
                )
              ],
            )
          ],
        ),
        body: PageView.builder(
          controller: controller,
          itemCount: 8,
          itemBuilder: (context, index) {
            print('Selected Index ==> $index');
            switch (index) {
              case 0:
                return Container(
                  child: Column(
                    children: [
                      Spacer(),
                      Center(
                        child: Image.asset(
                          'assets/graduationhat.png',
                          height: 200,
                          width: 250,
                          color: APP_SECONDARY_COLOR,
                        ),
                      ),
                      Spacer(),
                      Padding(
                        padding: const EdgeInsets.all(25.0),
                        child: Text(
                          "Welcome to the online trading platform! We'll help you take the first steps.",
                          style: TextStyle(color: Colors.white, fontSize: 16),
                          textAlign: TextAlign.center,
                        ),
                      ),

                      Padding(
                        padding: const EdgeInsets.only(left:20.0,right: 20,bottom: 20),
                        child: GestureDetector(
                          onTap: (){
                         setState(() {
                           controller.jumpToPage(1);
                           currentPageIndex++;
                         });
                          },
                          child: Container(
                            height: 45,
                            width: double.infinity,
                            decoration: BoxDecoration(
                              color: APP_SECONDARY_COLOR,
                              borderRadius: BorderRadius.circular(8)
                            ),
                            child: Center(child: Text('Start Training',style: TextStyle(fontSize: 15,color: Colors.white),)),
                          ),
                        ),
                      )
                    ],
                  ),
                );
              case 1:
                 return Container(
                   child: Column(
                     children: [
                       Padding(
                         padding: const EdgeInsets.only(top:15.0),
                         child: Image.asset('assets/tradeChart.jpeg',height: MediaQuery.of(context).size.height*.60,fit: BoxFit.fitWidth,),
                       ),Spacer(),
                       Padding(
                         padding: const EdgeInsets.all(20.0),
                         child: Text("The chart shows how the price of an asset changes If the line on the chart is going down, it means the price is falling. If it's going up, the price is rising. ",
                         style: TextStyle(color: Colors.white,fontSize: 15),textAlign: TextAlign.center,),
                       ),
                       Padding(
                         padding: const EdgeInsets.only(left:20.0,right: 20,bottom: 20),
                         child: GestureDetector(
                           onTap: (){
                             setState(() {
                               controller.jumpToPage(2);
                               currentPageIndex++;
                             });
                           },
                           child: Container(
                             height: 45,
                             width: double.infinity,
                             decoration: BoxDecoration(
                                 color: APP_SECONDARY_COLOR,
                                 borderRadius: BorderRadius.circular(8)
                             ),
                             child: Center(child: Text('Next',style: TextStyle(fontSize: 15,color: Colors.white),)),
                           ),
                         ),
                       )

                     ],
                   ),
                 );
              case 2:
                return Container(
                  child: Column(
                    children: [
                      Padding(
                        padding: const EdgeInsets.only(top:15.0),
                        child: Image.asset('assets/tradeChart2.jpeg',height: MediaQuery.of(context).size.height*.60,fit: BoxFit.fitWidth,),
                      ),Spacer(),
                      Padding(
                        padding: const EdgeInsets.all(20.0),
                        child: Text("Traders make forcasts on how the price will change in the near future. Such a forecast is called a 'trade'.",
                          style: TextStyle(color: Colors.white,fontSize: 15),textAlign: TextAlign.center,),
                      ),
                      Padding(
                        padding: const EdgeInsets.only(left:20.0,right: 20,bottom: 20),
                        child: GestureDetector(
                          onTap: (){
                            setState(() {
                              controller.jumpToPage(3);
                              currentPageIndex++;
                            });
                          },
                          child: Container(
                            height: 45,
                            width: double.infinity,
                            decoration: BoxDecoration(
                                color: APP_SECONDARY_COLOR,
                                borderRadius: BorderRadius.circular(8)
                            ),
                            child: Center(child: Text('Next',style: TextStyle(fontSize: 15,color: Colors.white),)),
                          ),
                        ),
                      )

                    ],
                  ),
                );
              case 3:
                return Container(
                  child: Column(
                    children: [
                      Padding(
                        padding: const EdgeInsets.only(top:15.0),
                        child: Image.asset('assets/tradeChart.jpeg',height: MediaQuery.of(context).size.height*.60,fit: BoxFit.fitWidth,),
                      ),Spacer(),
                      Padding(
                        padding: const EdgeInsets.all(20.0),
                        child: Text("Trades of fixed duration that offer a fixed profit are known as fixed Time Trades or FTT.",
                          style: TextStyle(color: Colors.white,fontSize: 15),textAlign: TextAlign.center,),
                      ),
                      Padding(
                        padding: const EdgeInsets.only(left:20.0,right: 20,bottom: 20),
                        child: GestureDetector(
                          onTap: (){
                            setState(() {
                              navPush(context, Dashboard(from: '',));
                            });
                          },
                          child: Container(
                            height: 45,
                            width: double.infinity,
                            decoration: BoxDecoration(
                                color: APP_SECONDARY_COLOR,
                                borderRadius: BorderRadius.circular(8)
                            ),
                            child: Center(child: Text('Complete Training',style: TextStyle(fontSize: 15,color: Colors.white),)),
                          ),
                        ),
                      )

                    ],
                  ),
                );
              default:
                return Container();
            }
          },
          physics: NeverScrollableScrollPhysics(),
          onPageChanged: (num) {
            currentPageIndex = num;
          },
        ));
  }

  _bottomSheetfinishTraing() {
    return showModalBottomSheet(context: context, builder: (context){
         return Container(
           color: APP_PRIMARY_COLOR,
           child: Column(
             mainAxisSize: MainAxisSize.min,
             children: [
             Container(
               color: APP_PRIMARY_COLOR,
               child: Column(
                 children: [
                   Padding(
                     padding: const EdgeInsets.all(15.0),
                     child: Text('Do you want to finish training?',style: TextStyle(fontSize: 15,color: Colors.white,
                     fontWeight: FontWeight.w400),),
                   ),

                   Padding(
                     padding: const EdgeInsets.only(left:25.0,right:25,top: 5),
                     child: Text('You can resume your training later in the FAQ section. ',style: TextStyle(fontSize: 15,color: Colors.white,
                         fontWeight: FontWeight.w400),textAlign: TextAlign.center,),
                   ),

                   Padding(
                     padding: const EdgeInsets.only(top:20.0,bottom: 30),
                     child: Row(
                       crossAxisAlignment: CrossAxisAlignment.center,
                       children: [
                       Expanded(
                         child: GestureDetector(
                           onTap: (){
                             Navigator.of(context).pop();
                           },
                           child: Padding(
                             padding: const EdgeInsets.only(left:18.0,right: 12),
                             child: Container(height: 45,
                             decoration: BoxDecoration(
                                 borderRadius: BorderRadius.circular(6),
                               color: Color(0xFF3E3C3C),
                             ),

                             child: Center(child: Text('Cancel',style: TextStyle(color:
                             Colors.white,fontSize: 15),)),),
                           ),
                         ),
                       ),
                         Expanded(
                           child: GestureDetector(
                             onTap:(){navPush(context, Dashboard(from: '',));
                             },
                             child: Padding(
                               padding: const EdgeInsets.only(left:18.0,right: 18),
                               child: Container(height: 45,
                                decoration: BoxDecoration(
                                  color: Color(0xFFD42727),
                                  borderRadius: BorderRadius.circular(6)
                                ),
                                 child: Center(child: Text('Finish',style: TextStyle(color:
                                 Colors.white,fontSize: 15))),),
                             ),
                           ),
                         )
                     ],),
                   )

                 ],
               ),
             ),

           ],),
         );
    });
  }
}

// PageView(
// controller: controller,
// children: widgetList,
// physics: NeverScrollableScrollPhysics(),
// onPageChanged: (num){
// currentPageIndex=num;
// },
// )
