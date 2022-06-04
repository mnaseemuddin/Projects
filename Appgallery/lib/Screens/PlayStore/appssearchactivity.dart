


import 'package:appgallery/Controllers/allappssearchcontroller.dart';
import 'package:appgallery/Model/mostappsearchedmodel.dart';
import 'package:appgallery/Resources/constants.dart';
import 'package:flutter/material.dart';
import 'package:flutter_icons/flutter_icons.dart';
import 'package:get/get.dart';

class AppsSearchActivity extends StatefulWidget {
  const AppsSearchActivity({Key? key}) : super(key: key);

  @override
  _AppsSearchActivityState createState() => _AppsSearchActivityState();
}

class _AppsSearchActivityState extends State<AppsSearchActivity> {

  final AllAppsSearchController _controller=Get.put(AllAppsSearchController());


  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: PreferredSize(preferredSize: const Size.fromHeight(80),
        child: mostAppsSearchBarUI(),
        // backgroundColor: Color(0xfff1f3f5),
      ),
      body: Column(children: [
        mostSearchedHeadingUI(),
        mostSearchedUI(),
        topSearchAppsHeadingUI(),
       topSearchedAppsUI()
       //  GridView(
       //    gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
       //        crossAxisCount: 5,
       //        crossAxisSpacing: 1,
       //        mainAxisSpacing: 1,
       //        mainAxisExtent: 60
       //    ),
       //    shrinkWrap: true,
       // children: List.generate(12, (index){
       //   return Padding(padding: EdgeInsets.all(10),
       //   child: Padding(
       //     padding: const EdgeInsets.only(top:0.0,right: 0),
       //     child: Container(
       //       decoration: BoxDecoration(
       //         borderRadius:BorderRadius.circular(10),
       //         color: Colors.grey[200]
       //       ),
       //       child: Center(child: Text('VPN')),
       //     ),
       //   ),);
       // }),
       //  )
        
      ],),
    );
  }

Widget mostAppsSearchBarUI() {
    return Padding(
      padding: const EdgeInsets.only(top:50.0,left: 20,bottom: 15),
      child: Row(
        children: [
          Padding(
            padding: const EdgeInsets.only(right:8.0),
            child: GestureDetector(
                onTap: (){
                  Navigator.pop(context,false);
                },
                child: const Icon(Icons.arrow_back_ios_outlined)),
          ),
          Container(
            width: MediaQuery.of(context).size.width*.84,
            height: 45,
            decoration: BoxDecoration(
                borderRadius: BorderRadius.circular(15),
                color: Colors.white
            ),
            child: Row(children: [
              Padding(
                padding: const EdgeInsets.only(left:10.0,top: 4),
                child: Image.asset(assetsSearchImg,height: 15,width: 15,),
              ),
              Padding(
                padding: const EdgeInsets.only(left:10.0),
                child: SizedBox(
                  width: 220,height: 45,
                  child: TextFormField(
                    decoration: const InputDecoration(
                        contentPadding: EdgeInsets.fromLTRB(10, 0, 0, 5),
                        border: InputBorder.none,
                        hintText: 'PUBG'
                    ),
                  ),
                ),
              ),
              Container(
                margin: const EdgeInsets.fromLTRB(0, 0, 8, 0),
                height: 20,
                width: 1,
                color: Colors.grey[300],
              ),Padding(
                padding: const EdgeInsets.only(right:14.0),
                child: Text('Search'.toUpperCase(),style: const TextStyle(color:
                Color(0xff0a58f5),fontSize: 13),),
              )
            ],),
          ),

        ],),
    );
}

 Widget mostSearchedHeadingUI() {
    return Padding(
      padding: const EdgeInsets.fromLTRB(20, 20, 20, 0),
      child: Row(children: const [
        Text('Most searched',style: TextStyle(fontSize: 15,
            color: Colors.black)),
        Spacer(),
        Text('More',style: TextStyle(fontSize: 13),),
        Icon(Icons.arrow_forward_ios_outlined,size: 13,)
      ],),
    );
  }

 Widget mostSearchedUI() {
    return Padding(
      padding: const EdgeInsets.only(left:15.0,top: 10),
      child: SizedBox(
        height: 130,
        child: ListView.builder(
            shrinkWrap: true,
            scrollDirection: Axis.horizontal,
            itemCount: _controller.mostAppsSearchedList.length??0,
            itemBuilder: (ctx,index){
              MostAppSearchedModel mostApps=_controller.mostAppsSearchedList.elementAt(index);
              return Column(
                children: [
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
                      child: Image.asset(mostApps.img,fit: BoxFit.fitWidth,),
                    ),
                  ),
                  Text(mostApps.name,style: const TextStyle(fontSize: 13),),
                  Padding(
                    padding: const EdgeInsets.only(right:8.0,top: 8),
                    child: Container(
                      width: 80,
                      height: 30,
                      decoration: BoxDecoration(
                          borderRadius: BorderRadius.circular(15),
                          color: const Color(0xffe5e4e4)
                      ),
                      child: Center(
                        child: Text('Install'.toUpperCase(),style: const TextStyle(fontSize: 11,color:
                        Color(0xff0a58f5)),),
                      ),
                    ),
                  )
                ],);
            }),
      ),
    );
 }

 Widget topSearchAppsHeadingUI() {
    return Padding(
      padding: const EdgeInsets.only(left:15.0,top: 10),
      child: Row(children: [
        const Text('Top searches',style: TextStyle(fontSize: 15,color: Colors.black)),
        const Spacer(),Padding(
          padding: const EdgeInsets.only(right:8.0),
          child: Text('Refresh'.toUpperCase(),style: const TextStyle(fontSize: 15,color: Color(0xff0a58f5))),
        )
      ],),
    );
 }

 Widget topSearchedAppsUI() {
    return Wrap(
      direction: Axis.horizontal,
      children: _controller.topSearchList.map((e) => Padding(padding: EdgeInsets.all(10),
        child: Padding(
          padding: const EdgeInsets.only(top:0.0,right: 0),
          child: Row(
            mainAxisSize: MainAxisSize.min,
            children: [
              Container(
                height: 37,
                decoration: BoxDecoration(
                    borderRadius:BorderRadius.circular(10),
                    color: Colors.grey[200]
                ),
                child:Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: Center(child: Text(e.toString())),
                ),
              ),
            ],
          ),
        ),)).toList(),
    );
 }
}
