



import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import '../../Utils/routes.dart';
import '../../apis/apirepositary.dart';
import '../PlayStore/appsdetailsactivity.dart';

class AppsActivity extends StatefulWidget {
  const AppsActivity({Key? key}) : super(key: key);

  @override
  _AppsActivityState createState() => _AppsActivityState();
}

class _AppsActivityState extends State<AppsActivity> {

  List? data,data1;
  @override
  void initState() {
    super.initState();
    getAllPlayStoreApps();
  }
  Future<String> getAllPlayStoreApps() async {
    final jsonResponse=await http.post(Uri.parse(allPlayStoreAppsUrl));
    setState(() {
      data=json.decode(jsonResponse.body);
    });
    return 'Success';
  }
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xfff1f3f5),
      appBar: PreferredSize(preferredSize: const Size.fromHeight(80),
        child: Padding(
          padding: const EdgeInsets.only(top:50.0,left: 20,bottom: 15),
          child: Row(
            children: const [
              Text('Apps',style: TextStyle(fontSize: 17.5,color: Colors.black87,
                  fontWeight: FontWeight.w500),),
            ],),
        ),
        // backgroundColor: Color(0xfff1f3f5),
      ),
      body: ListView(
        children: [
          searchBarUI(),
          topImgSliderUI(),
          topCategoryUI(),
          Padding(
            padding: const EdgeInsets.fromLTRB(20, 20, 20, 0),
            child: Row(children: [
              Text('Top rated apps',style: TextStyle(fontSize: 15,
                  color: Colors.black)),
              Spacer(),
              Text('More',style: TextStyle(fontSize: 13),),
              Icon(Icons.arrow_forward_ios_outlined,size: 13,)
            ],),
          ),
          topRatedAppsUI(),

          topCollectionSliderUI(),

          youMayLikeAppsUI(),

          appsForKidsAppsUI(),

          moreCoolAppsAppsUI(),

        mostPopularAppsUI(),

         categoryUI()
        ],
      ),
    );
  }

  searchBarUI() => Container(
    margin: const EdgeInsets.fromLTRB(15,10, 15, 10),
    height: 40,
    decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(20),
        color: Colors.white
    ),
    child: Row(
      children: [
        Align(
          alignment: Alignment.centerLeft,
          child: Padding(
            padding: const EdgeInsets.fromLTRB(15 , 12, 0, 12),
            child: Image.asset('assets/loupe.png',height: 15,width: 15,),
          ),
        ),
        const Padding(
          padding: EdgeInsets.only(left:18.0),
          child: Text('PUBG',style: TextStyle(fontSize: 13.5,color: Colors.black87),),
        )
      ],
    ),
  );

  topImgSliderUI() =>SingleChildScrollView(
    scrollDirection: Axis.horizontal,
    child: Padding(
      padding: const EdgeInsets.all(20.0),
      child: Row(
        children: [
          Container(
            width: MediaQuery.of(context).size.width*.90,
            height: 300,
            decoration: BoxDecoration(
                borderRadius: BorderRadius.circular(15),
                color: Colors.white
            ),
            child: Column(children: [
              Container(
                  width: double.infinity,
                  height: 220,
                  child: Padding(
                    padding: const EdgeInsets.all(1.0),
                    child: ClipRRect(
                        borderRadius: BorderRadius.only(topLeft: Radius.circular(15),
                            topRight: Radius.circular(15)),
                        child: Image.asset('assets/chngri.jpg',fit: BoxFit.fitHeight,)),
                  )),

              SizedBox(
                height: 70,
                child: Row(
                  children: [
                    Row(
                      children: [
                        Container(
                          width: MediaQuery.of(context).size.width*.17,
                          child: Padding(
                            padding: const EdgeInsets.all(4.0),
                            child: Container(
                              height: 60,width: 62,
                              decoration: BoxDecoration(
                                  borderRadius: BorderRadius.circular(12),border:
                              Border.all(width: 1,color: Color(0xffe3dfdf))
                              ),
                              child: Padding(
                                padding: const EdgeInsets.all(10.0),
                                child: Image.asset('assets/chingrilogo.png',fit: BoxFit.fill,),
                              ),
                            ),
                          ),
                        ),

                        SizedBox(
                          width: MediaQuery.of(context).size.width*.50,
                          child: Column(
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              Align(
                                alignment: Alignment.centerLeft,
                                child: Padding(
                                  padding: const EdgeInsets.only(top:4.0),
                                  child: RichText(
                                    overflow:TextOverflow.ellipsis,
                                    text: const TextSpan(text: 'Chingari - Original Indian Short Video App',style: TextStyle(
                                        color: Colors.black,fontWeight: FontWeight.w400
                                    )),),
                                ),
                              ),

                              Align(
                                alignment: Alignment.centerLeft,
                                child: Padding(
                                  padding: const EdgeInsets.only(top:4.0),
                                  child: RichText(
                                    overflow:TextOverflow.ellipsis,
                                    text: const TextSpan(text: 'Chingari Bharat ka No1 Short Video App',style: TextStyle(
                                        color: Colors.black,fontWeight: FontWeight.w400
                                    )),),
                                ),
                              )
                            ],),
                        ),
                        Padding(
                          padding: const EdgeInsets.only(right:8.0),
                          child: Container(
                            width: 80,
                            height: 35,
                            decoration: BoxDecoration(
                                borderRadius: BorderRadius.circular(15),
                                color: Color(0xfff1f1f1)
                            ),
                            child: Center(
                              child: Text('Install'.toUpperCase(),style: const TextStyle(color:
                              Color(0xff0a58f5)),),
                            ),
                          ),
                        )

                      ],
                    )
                  ],
                ),
              )

            ],),
          ),
          Padding(
            padding: const EdgeInsets.only(left:10.0),
            child:  Container(
              width: MediaQuery.of(context).size.width*.90,
              height: 300,
              decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(15),
                  color: Colors.white
              ),
              child: Column(children: [
                Container(
                    width: double.infinity,
                    height: 220,
                    child: Padding(
                      padding: const EdgeInsets.all(1.0),
                      child: ClipRRect(
                          borderRadius: const BorderRadius.only(topLeft: Radius.circular(15),
                              topRight: Radius.circular(15)),
                          child: Image.asset('assets/chngri.jpg',fit: BoxFit.fitHeight,)),
                    )),

                Container(
                  height: 70,
                  child: Row(
                    children: [
                      Row(
                        children: [
                          SizedBox(
                            width: MediaQuery.of(context).size.width*.17,
                            child: Padding(
                              padding: const EdgeInsets.all(4.0),
                              child: Container(
                                height: 60,width: 62,
                                decoration: BoxDecoration(
                                    borderRadius: BorderRadius.circular(12),border:
                                Border.all(width: 1,color: Color(0xffe3dfdf))
                                ),
                                child: Padding(
                                  padding: const EdgeInsets.all(10.0),
                                  child: Image.asset('assets/chingrilogo.png',fit: BoxFit.fill,),
                                ),
                              ),
                            ),
                          ),

                          SizedBox(
                            width: MediaQuery.of(context).size.width*.50,
                            child: Column(
                              mainAxisAlignment: MainAxisAlignment.center,
                              children: [
                                Align(
                                  alignment: Alignment.centerLeft,
                                  child: Padding(
                                    padding: const EdgeInsets.only(top:4.0),
                                    child: RichText(
                                      overflow:TextOverflow.ellipsis,
                                      text: const TextSpan(text: 'Chingari - Original Indian Short Video App',style: TextStyle(
                                          color: Colors.black,fontWeight: FontWeight.w400
                                      )),),
                                  ),
                                ),

                                Align(
                                  alignment: Alignment.centerLeft,
                                  child: Padding(
                                    padding: const EdgeInsets.only(top:4.0),
                                    child: RichText(
                                      overflow:TextOverflow.ellipsis,
                                      text: const TextSpan(text: 'Chingari Bharat ka No1 Short Video App',style: TextStyle(
                                          color: Colors.black,fontWeight: FontWeight.w400
                                      )),),
                                  ),
                                )
                              ],),
                          ),
                          Padding(
                            padding: const EdgeInsets.only(right:8.0),
                            child: Container(
                              width: 80,
                              height: 35,
                              decoration: BoxDecoration(
                                  borderRadius: BorderRadius.circular(15),
                                  color: Color(0xfff1f1f1)
                              ),
                              child: Center(
                                child: Text('Install'.toUpperCase(),style: const TextStyle(color:
                                Color(0xff0a58f5)),),
                              ),
                            ),
                          )

                        ],
                      )
                    ],
                  ),
                )

              ],),
            ),
          ),
        ],
      ),
    ),
  );

  topCategoryUI() => SizedBox(
    height: 100,
    child: Padding(
      padding: const EdgeInsets.only(left:20.0,right: 20),
      child: Row(
        children: [
          Expanded(child: Container(
            height: 100,
            decoration: BoxDecoration(
              borderRadius: BorderRadius.circular(10),
              color: const Color(0xfffeab81),
            ),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                const Text('Gift',style: TextStyle(fontWeight: FontWeight.w600,fontSize:18,
                    color: Color(
                        0xfffaf9f8)),),
                Padding(
                  padding: const EdgeInsets.all(15.0),
                  child: Image.asset('assets/gift.png',),
                ),
              ],
            ),
          )),
          Padding(padding: EdgeInsets.all(8)),
          Expanded(child: Container(
            decoration: BoxDecoration(
              borderRadius: BorderRadius.circular(10),
              color: Color(0xff89b8fe),
            ),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Padding(
                  padding: const EdgeInsets.only(left:8.0),
                  child: Text('Quick \nApps',style: TextStyle(fontWeight: FontWeight.w600,fontSize:18,color: Color(
                      0xfffcfcfc)),),
                ),
                Padding(
                  padding: const EdgeInsets.only(right:15.0,top: 15,left:8,bottom: 15),
                  child: Image.asset('assets/play.png',),
                ),
              ],
            ),
          ))
        ],
      ),
    ),
  );

  topRatedAppsUI() =>Padding(
    padding: const EdgeInsets.only(left:18.0),
    child: SizedBox(
      width: double.infinity,
      height: 316,
      child: ListView.builder(
          scrollDirection: Axis.horizontal,
          itemCount: data==null?0: data!.length,
          itemBuilder: (ctx,index){
            data1=data![index]['Category'];
            return Padding(
              padding: const EdgeInsets.only(top:20,bottom: 20,right: 5,left: 3),
              child: Container(
                decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(10),
                    color: Colors.white
                ),
                height: 110,
                width: MediaQuery.of(context).size.width*.90,
                child: featureNewGamesListUI(),
              ),
            );
          }),
    ),
  );

  featureNewGamesListUI()=> Column(
      mainAxisSize: MainAxisSize.min,
      children: data1!.map((item) => InkWell(
        onTap: (){
          navPush(context, AppsDetailsActivity
            (appId: item['id'],appIcon: item['img'],
            appName: item['name'],));
        },
        child: Column(
          children: [
            Padding(
              padding: const EdgeInsets.all(0.0),
              child: Row(
                children: [
                  Container(
                    margin: const EdgeInsets.all(8),
                    height: 70,
                    width: MediaQuery.of(context).size.width *.17,
                    decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(14),
                        color: Colors.white,
                        border: Border.all(
                            width: 1, color: const Color(0xffe3e2e2))),
                    child: ClipRRect(
                        borderRadius: BorderRadius.circular(14),
                        child: Image.network(item['img'],
                          fit: BoxFit.fitHeight,)),
                  ),
                  SizedBox(
                    width: MediaQuery.of(context).size.width * .68,
                    child: Padding(
                      padding: const EdgeInsets.only(top: 14.0),
                      child: Column(
                        children: [
                          Row(
                            children: [
                              SizedBox(
                                width: MediaQuery.of(context)
                                    .size
                                    .width *
                                    .40,
                                child: Column(
                                  children: [
                                    Align(
                                      alignment: Alignment.centerLeft,
                                      child: RichText(
                                          overflow: TextOverflow.ellipsis,
                                          text: TextSpan(
                                              text:
                                              item['name'],
                                              style: const TextStyle(
                                                  color: Colors.black))),
                                    ),
                                    Padding(
                                      padding: const EdgeInsets.only(top:4.0),
                                      child: Align(
                                          alignment: Alignment.centerLeft,
                                          child: Text(item['category'],textAlign: TextAlign.left,)),
                                    ),
                                  ],
                                ),
                              ),
                              const Spacer(),
                              Padding(
                                padding: const EdgeInsets.only(
                                    right: 8.0),
                                child: Container(
                                  width: 80,
                                  height: 34,
                                  decoration: BoxDecoration(
                                      borderRadius:
                                      BorderRadius.circular(15),
                                      color: Color(0xfff1f1f1)),
                                  child: Center(
                                    child: Text(
                                      'Install'.toUpperCase(),
                                      style: const TextStyle(
                                          color: Color(0xff0a58f5)),
                                    ),
                                  ),
                                ),
                              )
                            ],
                          ),

                          Align(
                              alignment: Alignment.centerLeft,
                              child: Padding(
                                padding:
                                const EdgeInsets.only(top: 5.0),
                                child: RichText(
                                    overflow: TextOverflow.ellipsis,
                                    text: TextSpan(
                                        text:
                                        item['description'],
                                        style: const TextStyle(
                                            color: Colors.black))),
                              )),
                          Padding(
                            padding: const EdgeInsets.only(
                                top: 20.0, right: 10),
                            child: Container(
                              width: double.infinity,
                              height: 1,
                              color: Colors.grey[200],
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      )).toList());

  topCollectionSliderUI() =>Column(children: [
    Padding(
      padding: const EdgeInsets.fromLTRB(20, 20, 20, 0),
      child: Row(children: const [
        Text('Top collections',style: TextStyle(fontSize: 15,
            color: Colors.black)),
        Spacer(),
        Text('More',style: TextStyle(fontSize: 13),),
        Icon(Icons.arrow_forward_ios_outlined,size: 13,)
      ],),
    ),
    SingleChildScrollView(
      scrollDirection: Axis.horizontal,
      child: Padding(
        padding: const EdgeInsets.all(20.0),
        child: Row(
          children: [
            Container(
              width: MediaQuery.of(context).size.width*.90,
              height: 300,
              decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(15),
                  color: Colors.white
              ),
              child: Column(children: [
                Container(
                    width: double.infinity,
                    height: 220,
                    child: Padding(
                      padding: const EdgeInsets.all(1.0),
                      child: ClipRRect(
                          borderRadius: BorderRadius.only(topLeft: Radius.circular(15),
                              topRight: Radius.circular(15)),
                          child: Image.asset('assets/chngri.jpg',fit: BoxFit.fitHeight,)),
                    )),

                Container(
                  height: 70,
                  child: Row(
                    children: [
                      Row(
                        children: [
                          Container(
                            width: MediaQuery.of(context).size.width*.17,
                            child: Padding(
                              padding: const EdgeInsets.all(4.0),
                              child: Container(
                                height: 60,width: 62,
                                decoration: BoxDecoration(
                                    borderRadius: BorderRadius.circular(12),border:
                                Border.all(width: 1,color: Color(0xffe3dfdf))
                                ),
                                child: Padding(
                                  padding: const EdgeInsets.all(10.0),
                                  child: Image.asset('assets/chingrilogo.png',fit: BoxFit.fill,),
                                ),
                              ),
                            ),
                          ),

                          Container(
                            width: MediaQuery.of(context).size.width*.50,
                            child: Column(
                              mainAxisAlignment: MainAxisAlignment.center,
                              children: [
                                Align(
                                  alignment: Alignment.centerLeft,
                                  child: Padding(
                                    padding: const EdgeInsets.only(top:4.0),
                                    child: RichText(
                                      overflow:TextOverflow.ellipsis,
                                      text: TextSpan(text: 'Chingari - Original Indian Short Video App',style: TextStyle(
                                          color: Colors.black,fontWeight: FontWeight.w400
                                      )),),
                                  ),
                                ),

                                Align(
                                  alignment: Alignment.centerLeft,
                                  child: Padding(
                                    padding: const EdgeInsets.only(top:4.0),
                                    child: RichText(
                                      overflow:TextOverflow.ellipsis,
                                      text: TextSpan(text: 'Chingari Bharat ka No1 Short Video App',style: TextStyle(
                                          color: Colors.black,fontWeight: FontWeight.w400
                                      )),),
                                  ),
                                )
                              ],),
                          ),
                          Padding(
                            padding: const EdgeInsets.only(right:8.0),
                            child: Container(
                              width: 80,
                              height: 35,
                              decoration: BoxDecoration(
                                  borderRadius: BorderRadius.circular(15),
                                  color: Color(0xfff1f1f1)
                              ),
                              child: Center(
                                child: Text('Install'.toUpperCase(),style: TextStyle(color:
                                Color(0xff0a58f5)),),
                              ),
                            ),
                          )

                        ],
                      )
                    ],
                  ),
                )

              ],),
            ),
            Padding(
              padding: const EdgeInsets.only(left:10.0),
              child:  Container(
                width: MediaQuery.of(context).size.width*.90,
                height: 300,
                decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(15),
                    color: Colors.white
                ),
                child: Column(children: [
                  Container(
                      width: double.infinity,
                      height: 220,
                      child: Padding(
                        padding: const EdgeInsets.all(1.0),
                        child: ClipRRect(
                            borderRadius: BorderRadius.only(topLeft: Radius.circular(15),
                                topRight: Radius.circular(15)),
                            child: Image.asset('assets/chngri.jpg',fit: BoxFit.fitHeight,)),
                      )),

                  Container(
                    height: 70,
                    child: Row(
                      children: [
                        Row(
                          children: [
                            Container(
                              width: MediaQuery.of(context).size.width*.17,
                              child: Padding(
                                padding: const EdgeInsets.all(4.0),
                                child: Container(
                                  height: 60,width: 62,
                                  decoration: BoxDecoration(
                                      borderRadius: BorderRadius.circular(12),border:
                                  Border.all(width: 1,color: Color(0xffe3dfdf))
                                  ),
                                  child: Padding(
                                    padding: const EdgeInsets.all(10.0),
                                    child: Image.asset('assets/chingrilogo.png',fit: BoxFit.fill,),
                                  ),
                                ),
                              ),
                            ),

                            Container(
                              width: MediaQuery.of(context).size.width*.50,
                              child: Column(
                                mainAxisAlignment: MainAxisAlignment.center,
                                children: [
                                  Align(
                                    alignment: Alignment.centerLeft,
                                    child: Padding(
                                      padding: const EdgeInsets.only(top:4.0),
                                      child: RichText(
                                        overflow:TextOverflow.ellipsis,
                                        text: TextSpan(text: 'Chingari - Original Indian Short Video App',style: TextStyle(
                                            color: Colors.black,fontWeight: FontWeight.w400
                                        )),),
                                    ),
                                  ),

                                  Align(
                                    alignment: Alignment.centerLeft,
                                    child: Padding(
                                      padding: const EdgeInsets.only(top:4.0),
                                      child: RichText(
                                        overflow:TextOverflow.ellipsis,
                                        text: TextSpan(text: 'Chingari Bharat ka No1 Short Video App',style: TextStyle(
                                            color: Colors.black,fontWeight: FontWeight.w400
                                        )),),
                                    ),
                                  )
                                ],),
                            ),
                            Padding(
                              padding: const EdgeInsets.only(right:8.0),
                              child: Container(
                                width: 80,
                                height: 35,
                                decoration: BoxDecoration(
                                    borderRadius: BorderRadius.circular(15),
                                    color: Color(0xfff1f1f1)
                                ),
                                child: Center(
                                  child: Text('Install'.toUpperCase(),style: TextStyle(color:
                                  Color(0xff0a58f5)),),
                                ),
                              ),
                            )

                          ],
                        )
                      ],
                    ),
                  )

                ],),
              ),
            ),
          ],
        ),
      ),
    ),
  ],);

  youMayLikeAppsUI() =>Column(children: [
    Padding(
      padding: const EdgeInsets.fromLTRB(20, 20, 20, 0),
      child: Row(children: const [
        Text('You may like',style: TextStyle(fontSize: 15,
            color: Colors.black)),
        Spacer(),
        Text('More',style: TextStyle(fontSize: 13),),
        Icon(Icons.arrow_forward_ios_outlined,size: 13,)
      ],),
    ),

    topRatedAppsUI(),
  ],);

  appsForKidsAppsUI() =>Column(children: [
    Padding(
      padding: const EdgeInsets.fromLTRB(20, 20, 20, 0),
      child: Row(children: const [
        Text('Apps for kids',style: TextStyle(fontSize: 15,
            color: Colors.black)),
        Spacer(),
        Text('More',style: TextStyle(fontSize: 13),),
        Icon(Icons.arrow_forward_ios_outlined,size: 13,)
      ],),
    ),
    Padding(
      padding: const EdgeInsets.all(20.0),
      child: SingleChildScrollView(
        scrollDirection: Axis.horizontal,
        child: Row(
          children: [
            Container(
              width: 90,
              height: 140,
              child: Column(children: [
                Container(
                  height: 70,
                  width: 74,
                  decoration: BoxDecoration(
                    border: Border.all(width: 1,
                        color: Color(0xffdbdada)),
                    borderRadius: BorderRadius.circular(10),
                  ),
                  child: Padding(
                    padding: const EdgeInsets.all(2.0),
                    child: Image.asset('assets/ninjafruit.png',fit: BoxFit.fitWidth,),
                  ),
                ),
                Text('Ninja Fruit',style: TextStyle(fontSize: 13),),
                Padding(
                  padding: const EdgeInsets.only(right:8.0,top: 8),
                  child: Container(
                    width: 80,
                    height: 30,
                    decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(15),
                        color: Color(0xffe5e4e4)
                    ),
                    child: Center(
                      child: Text('Install'.toUpperCase(),style: TextStyle(fontSize: 11,color:
                      Color(0xff0a58f5)),),
                    ),
                  ),
                )
              ],),
            ),
            Container(
              width: 90,
              height: 140,
              child: Column(children: [
                Container(
                  height: 70,
                  width: 74,
                  decoration: BoxDecoration(
                    border: Border.all(width: 1,
                        color: Color(0xffdbdada)),
                    borderRadius: BorderRadius.circular(10),
                  ),
                  child: Padding(
                    padding: const EdgeInsets.all(2.0),
                    child: Image.asset('assets/stgame.png',fit: BoxFit.fitWidth,),
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.only(left:10.0),
                  child: RichText(
                    overflow:TextOverflow.ellipsis,
                    text: TextSpan(text: 'Shooter Assassin',style: TextStyle(
                        fontSize: 13,color: Colors.black
                    )),),
                ),
                Padding(
                  padding: const EdgeInsets.only(right:8.0,top: 8),
                  child: Container(
                    width: 80,
                    height: 30,
                    decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(15),
                        color: Color(0xffe5e4e4)
                    ),
                    child: Center(
                      child: Text('Install'.toUpperCase(),style: TextStyle(fontSize: 11,color:
                      Color(0xff0a58f5)),),
                    ),
                  ),
                )
              ],),
            ),
            Container(
              width: 90,
              height: 140,
              child: Column(children: [
                Container(
                  height: 70,
                  width: 74,
                  decoration: BoxDecoration(
                    border: Border.all(width: 1,
                        color: Color(0xffdbdada)),
                    borderRadius: BorderRadius.circular(10),
                  ),
                  child: Padding(
                    padding: const EdgeInsets.all(2.0),
                    child: Image.asset('assets/ludo.png',fit: BoxFit.fitWidth,),
                  ),
                ),
                Text('Ludo Super',style: TextStyle(fontSize: 13),),
                Padding(
                  padding: const EdgeInsets.only(right:8.0,top: 8),
                  child: Container(
                    width: 80,
                    height: 30,
                    decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(15),
                        color: Color(0xffe5e4e4)
                    ),
                    child: Center(
                      child: Text('Install'.toUpperCase(),style: TextStyle(fontSize: 11,color:
                      Color(0xff0a58f5)),),
                    ),
                  ),
                )
              ],),
            ),
            Container(
              width: 90,
              height: 140,
              child: Column(children: [
                Container(
                  height: 70,
                  width: 74,
                  decoration: BoxDecoration(
                    border: Border.all(width: 1,
                        color: Color(0xffdbdada)),
                    borderRadius: BorderRadius.circular(10),
                  ),
                  child: Padding(
                    padding: const EdgeInsets.all(2.0),
                    child: Image.asset('assets/ninjafruit.png',fit: BoxFit.fitWidth,),
                  ),
                ),
                Text('Ninja Fruit',style: TextStyle(fontSize: 13),),
                Padding(
                  padding: const EdgeInsets.only(right:8.0,top: 8),
                  child: Container(
                    width: 80,
                    height: 30,
                    decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(15),
                        color: Color(0xffe5e4e4)
                    ),
                    child: Center(
                      child: Text('Install'.toUpperCase(),style: TextStyle(fontSize: 11,color:
                      Color(0xff0a58f5)),),
                    ),
                  ),
                )
              ],),
            ),
            Container(
              width: 90,
              height: 140,
              child: Column(children: [
                Container(
                  height: 70,
                  width: 74,
                  decoration: BoxDecoration(
                    border: Border.all(width: 1,
                        color: Color(0xffdbdada)),
                    borderRadius: BorderRadius.circular(10),
                  ),
                  child: Padding(
                    padding: const EdgeInsets.all(2.0),
                    child: Image.asset('assets/stgame.png',fit: BoxFit.fitWidth,),
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.only(left:10.0),
                  child: RichText(
                    overflow:TextOverflow.ellipsis,
                    text: TextSpan(text: 'Shooter Assassin',style: TextStyle(
                        fontSize: 13,color: Colors.black
                    )),),
                ),
                Padding(
                  padding: const EdgeInsets.only(right:8.0,top: 8),
                  child: Container(
                    width: 80,
                    height: 30,
                    decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(15),
                        color: Color(0xffe5e4e4)
                    ),
                    child: Center(
                      child: Text('Install'.toUpperCase(),style: TextStyle(fontSize: 11,color:
                      Color(0xff0a58f5)),),
                    ),
                  ),
                )
              ],),
            ),
            Container(
              width: 90,
              height: 140,
              child: Column(children: [
                Container(
                  height: 70,
                  width: 74,
                  decoration: BoxDecoration(
                    border: Border.all(width: 1,
                        color: Color(0xffdbdada)),
                    borderRadius: BorderRadius.circular(10),
                  ),
                  child: Padding(
                    padding: const EdgeInsets.all(2.0),
                    child: Image.asset('assets/ludo.png',fit: BoxFit.fitWidth,),
                  ),
                ),
                Text('Ludo Super',style: TextStyle(fontSize: 13),),
                Padding(
                  padding: const EdgeInsets.only(right:8.0,top: 8),
                  child: Container(
                    width: 80,
                    height: 30,
                    decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(15),
                        color: Color(0xffe5e4e4)
                    ),
                    child: Center(
                      child: Text('Install'.toUpperCase(),style: TextStyle(fontSize: 11,color:
                      Color(0xff0a58f5)),),
                    ),
                  ),
                )
              ],),
            ),
          ],
        ),
      ),
    ),
  ],);

  moreCoolAppsAppsUI()=>Column(children: [
    Padding(
      padding: const EdgeInsets.fromLTRB(20, 20, 20, 0),
      child: Row(children: const [
        Text('More cool apps',style: TextStyle(fontSize: 15,
            color: Colors.black)),
        Spacer(),
        Text('More',style: TextStyle(fontSize: 13),),
        Icon(Icons.arrow_forward_ios_outlined,size: 13,)
      ],),
    ),

    SingleChildScrollView(
      scrollDirection: Axis.horizontal,
      child: Row(
        children: [
          Padding(
            padding: const EdgeInsets.only(top:20.0,left:20,bottom: 20),
            child: Container(
              width: 150,
              height: 200,
              child: Padding(
                padding: const EdgeInsets.all(2),
                child: Image.asset('assets/mlbb.png',fit: BoxFit.fill,),
              ),
            ),
          ),
          Padding(
            padding: const EdgeInsets.only(top:20.0,left:6,bottom: 20),
            child: Container(
              width: 150,
              height: 200,
              child: Padding(
                padding: const EdgeInsets.all(2),
                child: Image.asset('assets/pubg.png',fit: BoxFit.fill,),
              ),
            ),
          ),
          Padding(
            padding: const EdgeInsets.only(top:20.0,left:6,bottom: 20),
            child: Container(
              width: 150,
              height: 200,
              child: Padding(
                padding: const EdgeInsets.all(2),
                child: Image.asset('assets/pubg.png',fit: BoxFit.fill,),
              ),
            ),
          ),

        ],
      ),
    ),
  ],);

  mostPopularAppsUI()=>Column(children: [
    Padding(
      padding: const EdgeInsets.fromLTRB(20, 20, 20, 0),
      child: Row(children: [
        Text('Most popular',style: TextStyle(fontSize: 15,
            color: Colors.black)),
        Spacer(),
        Text('More',style: TextStyle(fontSize: 13),),
        Icon(Icons.arrow_forward_ios_outlined,size: 13,)
      ],),
    ),

    topRatedAppsUI(),
  ],);

  categoryUI() =>Column(children: [
    Padding(
      padding: const EdgeInsets.fromLTRB(20, 20, 20, 0),
      child: Row(children: [
        Text('Category',style: TextStyle(fontSize: 15,
            color: Colors.black)),
        Spacer(),
        Text('More',style: TextStyle(fontSize: 13),),
        Icon(Icons.arrow_forward_ios_outlined,size: 13,)
      ],),
    ),

    Padding(
      padding: const EdgeInsets.all(20.0),
      child: Container(
        height: 260,
        decoration: BoxDecoration(
            borderRadius: BorderRadius.circular(15),
            color: Colors.white
        ),
        child: Column(children: [
          Padding(
            padding: const EdgeInsets.only(top:10.0,left:12),
            child: Row(
              children: [
                Image.asset('assets/business.png',height: 30,width: 30,),
                Padding(
                  padding: const EdgeInsets.only(left:18.0),
                  child: Text('Business',style: TextStyle(color: Colors.black,fontSize: 16),),
                ),
                Spacer(),
                Padding(
                  padding: const EdgeInsets.only(right:18.0),
                  child: Icon(Icons.arrow_forward_ios_outlined,color: Colors.black54,size: 15,),
                )
              ],
            ),
          ),
          Padding(
            padding: const EdgeInsets.only(left:57.0,top: 8,right:15),
            child: Container(height: 0.8,width: double.infinity,color: Colors.grey[300],),
          ),
          Padding(
            padding: const EdgeInsets.only(top:10.0,left:12),
            child: Row(
              children: [
                Image.asset('assets/car.png',height: 30,width: 30,),
                Padding(
                  padding: const EdgeInsets.only(left:18.0),
                  child: Text('Cars',style: TextStyle(color: Colors.black,fontSize: 16),),
                ),
                Spacer(),
                Padding(
                  padding: const EdgeInsets.only(right:18.0),
                  child: Icon(Icons.arrow_forward_ios_outlined,color: Colors.black54,size: 15,),
                )
              ],
            ),
          ),
          Padding(
            padding: const EdgeInsets.only(left:57.0,top: 8,right:15),
            child: Container(height: 0.8,width: double.infinity,color: Colors.grey[300],),
          ),
          Padding(
            padding: const EdgeInsets.only(top:10.0,left:12),
            child: Row(
              children: [
                Image.asset('assets/graduationcap.png',height: 30,width: 30,),
                Padding(
                  padding: const EdgeInsets.only(left:18.0),
                  child: Text('Education',style: TextStyle(color: Colors.black,fontSize: 16),),
                ),
                Spacer(),
                Padding(
                  padding: const EdgeInsets.only(right:18.0),
                  child: Icon(Icons.arrow_forward_ios_outlined,color: Colors.black54,size: 15,),
                )
              ],
            ),
          ),
          Padding(
            padding: const EdgeInsets.only(left:57.0,top: 8,right:15),
            child: Container(height: 0.8,width: double.infinity,color: Colors.grey[300],),
          ),
          Padding(
            padding: const EdgeInsets.only(top:10.0,left:12),
            child: Row(
              children: [
                Image.asset('assets/theater.png',height: 30,width: 30,),
                Padding(
                  padding: const EdgeInsets.only(left:18.0),
                  child: Text('Entertainment',style: TextStyle(color: Colors.black,fontSize: 16),),
                ),
                Spacer(),
                Padding(
                  padding: const EdgeInsets.only(right:18.0),
                  child: Icon(Icons.arrow_forward_ios_outlined,color: Colors.black54,size: 15,),
                )
              ],
            ),
          ),
          Padding(
            padding: const EdgeInsets.only(left:57.0,top: 8,right:15),
            child: Container(height: 0.8,width: double.infinity,color: Colors.grey[300],),
          ),
          Padding(
            padding: const EdgeInsets.only(top:10.0,left:12),
            child: Row(
              children: [
                Image.asset('assets/stats.png',height: 30,width: 30,),
                const Padding(
                  padding: EdgeInsets.only(left:18.0),
                  child: Text('Finance',style: TextStyle(color: Colors.black,fontSize: 16),),
                ),
                const Spacer(),
                const Padding(
                  padding: EdgeInsets.only(right:18.0),
                  child: Icon(Icons.arrow_forward_ios_outlined,color: Colors.black54,size: 15,),
                )
              ],
            ),
          ),
        ],),
      ),
    )
  ],);
}
