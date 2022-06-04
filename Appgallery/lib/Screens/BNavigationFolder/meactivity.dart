



import 'package:appgallery/Screens/PlayStore/Profile/profile.dart';
import 'package:appgallery/Utils/loadingoverlay.dart';
import 'package:flutter/material.dart';

import '../../Utils/routes.dart';
import '../../apis/apirepositary.dart';
import '../../apis/userdata.dart';

class MeActivity extends StatefulWidget {
  const MeActivity({Key? key}) : super(key: key);

  @override
  _MeActivityState createState() => _MeActivityState();
}

class _MeActivityState extends State<MeActivity> {


  var photoUrl="",name="";


  @override
  void initState() {
    getUserLoginDetails().then((value){
      getPlayStoreProfileDetails();
    });
    super.initState();
  }




  void getPlayStoreProfileDetails() {

    Map<dynamic,String>body={
      "email":userLoginModel!.email,
      "token":userLoginModel!.token
    };
    getPlayStoreProfileDetailsAPI(body).then((value){
      if(value.status){
        setState(() {
          name=value.data["name"];
          photoUrl=value.data["user_image"];
        });
      }else{
        toast(value.message);
      }
    });
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
              Text('Me',style: TextStyle(fontSize: 17.5,color: Colors.black87,
                  fontWeight: FontWeight.w500),),
            ],),
        ),
        // backgroundColor: Color(0xfff1f3f5),
      ),
      body: photoUrl==""?LoadingOverlay():ListView(children: [
        Container(
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
        ),

        Padding(
          padding: const EdgeInsets.only(top:18.0,left: 15),
          child: GestureDetector(
            onTap: (){
              navPush(context,const MyProfile());
            },
            child: Container(
              color: Color(0xfff1f3f5),
              height: 70,
              child: Row(children: [
                ClipRRect(
                    borderRadius: BorderRadius.circular(50),
                    child: Image.network(photoUrl,height: 65,width: 65,)),
                const SizedBox(width: 10,),
                Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                     Align(
                        alignment: Alignment.centerLeft,
                        child: Text(name,style: TextStyle(color: Colors.black,fontSize: 16))),
                    // Align(
                    //     alignment: Alignment.centerLeft,
                    //     child: Padding(
                    //       padding: const EdgeInsets.only(top:2.0),
                    //       child: Text('Get exclusive benefits',style: TextStyle(color: Colors.grey[800],fontSize:
                    //       13),),
                    //     ))
                  ],),
                const Spacer(),
                const Padding(
                  padding: EdgeInsets.only(right:18.0),
                  child: Icon(Icons.arrow_forward_ios_outlined,color: Colors.black54,size: 15,),
                )
              ],),
            ),
          ),
        ),
        Padding(
          padding: const EdgeInsets.only(top:18.0),
          child: Container(
            height: 5,
            color: Colors.grey[300],
          ),
        ),

          Padding(
            padding: const EdgeInsets.only(top:10.0,left:12),
            child: Row(
              children: [
                Image.asset('assets/gift2.png',height: 30,width: 30,color:
                Colors.grey[700],),
                Padding(
                  padding: const EdgeInsets.only(left:18.0),
                  child: Text('Gift',style: TextStyle(color: Colors.black,fontSize: 16),),
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
            padding: const EdgeInsets.only(left:57.0,top: 15,right:15),
            child: Container(height: 0.8,width: double.infinity,color: Colors.grey[300],),
          ),
          Padding(
            padding: const EdgeInsets.only(top:15.0,left:12),
            child: Row(
              children: [
                Image.asset('assets/medal1.png',height: 30,width: 30,
                    color: Colors.grey[700]),
                Padding(
                  padding: const EdgeInsets.only(left:18.0),
                  child: Text('Rewards',style: TextStyle(color: Colors.black,fontSize: 16),),
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
            padding: const EdgeInsets.only(left:57.0,top: 15,right:15),
            child: Container(height: 0.8,width: double.infinity,color: Colors.grey[300],),
          ),
          Padding(
            padding: const EdgeInsets.only(top:15.0,left:12),
            child: Row(
              children: [
                Image.asset('assets/message.png',height: 30,width: 30,
                    color: Colors.grey[700]),
                Padding(
                  padding: const EdgeInsets.only(left:18.0),
                  child: Text('Comments',style: TextStyle(color: Colors.black,fontSize: 16),),
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
            padding: const EdgeInsets.only(left:57.0,top: 15,right:15),
            child: Container(height: 0.8,width: double.infinity,color: Colors.grey[300],),
          ),
          Padding(
            padding: const EdgeInsets.only(top:15.0,left:12),
            child: Row(
              children: [
                Image.asset('assets/voucher.png',height: 30,width: 30,
                    color: Colors.grey[700]),
                Padding(
                  padding: const EdgeInsets.only(left:18.0),
                  child: Text('Coupons',style: TextStyle(color: Colors.black,fontSize: 16),),
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
            padding: const EdgeInsets.only(left:57.0,top: 15,right:15),
            child: Container(height: 0.8,width: double.infinity,color: Colors.grey[300],),
          ),
          Padding(
            padding: const EdgeInsets.only(top:15.0,left:12),
            child: Row(
              children: [
                Image.asset('assets/calendar.png',height: 30,width: 30,
                    color: Colors.grey[700]),
                Padding(
                  padding: const EdgeInsets.only(left:18.0),
                  child: Text('Pre-orders',style: TextStyle(color: Colors.black,fontSize: 16),),
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
            padding: const EdgeInsets.only(left:57.0,top: 15,right:15),
            child: Container(height: 0.8,width: double.infinity,color: Colors.grey[300],),
          ),
          Padding(
            padding: const EdgeInsets.only(top:15.0,left:12),
            child: Row(
              children: [
                Image.asset('assets/cart.png',height: 30,width: 30,
                    color: Colors.grey[700]),
                Padding(
                  padding: const EdgeInsets.only(left:18.0),
                  child: Text('Purchase history',style: TextStyle(color: Colors.black,fontSize: 16),),
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
            padding: const EdgeInsets.only(left:57.0,top: 15,right:15),
            child: Container(height: 0.8,width: double.infinity,color: Colors.grey[300],),
          ),
          Padding(
            padding: const EdgeInsets.only(top:15.0,left:12),
            child: Row(
              children: [
                Image.asset('assets/bell.png',height: 30,width: 30,
                    color: Colors.grey[700]),
                Padding(
                  padding: const EdgeInsets.only(left:18.0),
                  child: Text('Comment notifications',style: TextStyle(color: Colors.black,fontSize: 16),),
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
            padding: const EdgeInsets.only(top:18.0),
            child: Container(
              height: 5,
              color: Colors.grey[300],
            ),
          ),
          Padding(
            padding: const EdgeInsets.only(top:15.0,left:12),
            child: Row(
              children: [
                Image.asset('assets/question.png',height: 30,width: 30,
                    color: Colors.grey[700]),
                Padding(
                  padding: const EdgeInsets.only(left:18.0),
                  child: Text('Help',style: TextStyle(color: Colors.black,fontSize: 16),),
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
            padding: const EdgeInsets.only(left:57.0,top: 15,right:15),
            child: Container(height: 0.8,width: double.infinity,color: Colors.grey[300],),
          ),
          Padding(
            padding: const EdgeInsets.only(top:15.0,left:12),
            child: Row(
              children: [
                Image.asset('assets/settings.png',height: 30,width: 30,
                    color: Colors.grey[700]),
                Padding(
                  padding: const EdgeInsets.only(left:18.0),
                  child: Text('Setting',style: TextStyle(color: Colors.black,fontSize: 16),),
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
            padding: const EdgeInsets.only(left:57.0,top: 15,right:15),
            child: Container(height: 0.8,width: double.infinity,color: Colors.grey[300],),
          ),
          Padding(
            padding: const EdgeInsets.only(top:15.0,left:12),
            child: Row(
              children: [
                Image.asset('assets/notes.png',height: 30,width: 30,
                    color: Colors.grey[700]),
                Padding(
                  padding: const EdgeInsets.only(left:18.0),
                  child: Text('Problem and Suggestions',style: TextStyle(color: Colors.black,fontSize: 16),),
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
            padding: const EdgeInsets.only(left:57.0,top: 15,right:15),
            child: Container(height: 0.8,width: double.infinity,color: Colors.grey[300],),
          ),
          Padding(
            padding: const EdgeInsets.only(top:15.0,left:12),
            child: Row(
              children: [
                Image.asset('assets/about.png',height: 30,width: 30,
                    color: Colors.grey[700]),
                Padding(
                  padding: const EdgeInsets.only(left:18.0),
                  child: Text('About',style: TextStyle(color: Colors.black,fontSize: 16),),
                ),
                Spacer(),
                Padding(
                  padding: const EdgeInsets.only(right:18.0),
                  child: Icon(Icons.arrow_forward_ios_outlined,color: Colors.black54,size: 15,),
                )
              ],
            ),
          ),

        SizedBox(height: 20,)
        ],),
    );
  }
}


