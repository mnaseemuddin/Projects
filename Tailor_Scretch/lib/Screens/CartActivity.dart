

import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:get/get.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:tailor/Screens/MyAddressActivity.dart';
import 'package:tailor/Support/UISupport.dart';

class CartActivity extends StatefulWidget {
  const CartActivity({ Key? key }) : super(key: key);

  @override
  _CartActivityState createState() => _CartActivityState();
}

class _CartActivityState extends State<CartActivity> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        brightness: Brightness.dark,
        backgroundColor:UISupport.APP_PRIMARY_COLOR,
        title: Text("Cart",style: GoogleFonts.aBeeZee(color:Colors.white),),
      ),
      body: ListView(
        physics: NeverScrollableScrollPhysics(),
        children: [
          Container(
            margin: EdgeInsets.only(top:8,left:8,right: 8),
            height: MediaQuery.of(context).size.height*0.60,
            decoration: BoxDecoration(
             color: Colors.grey[100],
              boxShadow: [
                    BoxShadow(
                      color:  Colors.grey,
                      offset: Offset(4, 3),
                      blurRadius: 9,
                    ),
                  ],
              borderRadius: BorderRadius.only(topLeft: Radius.circular(10),topRight: 
              Radius.circular(10))
            ),
            child: Padding(
              padding: const EdgeInsets.only(top:2.5,left:2.5,right: 2.5),
              child: Container(
                decoration: BoxDecoration(
              color: Colors.white,
              boxShadow: [
                    BoxShadow(
                      color:  Colors.grey,
                      offset: Offset(4, 3),
                      blurRadius: 5,
                    ),
                  ],
              borderRadius: BorderRadius.only(topLeft: Radius.circular(10),topRight: 
              Radius.circular(10))
            ),
               
                child: Column(
                  children: [
          Container(
                width: double.infinity,
                height: 150,
                child: Row(
                  children: [
                    Container(
                      margin: EdgeInsets.all(15),
                      width: MediaQuery.of(context).size.width*0.25,
                      height: 105,
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(10),
                        color: Colors.grey[200]
                      ),
                      child: Padding(
                           padding: const EdgeInsets.all(4.0),
                           child: Image.network("https://thumbs.dreamstime.com/b/vector-illustration-women-s-skinny-pants-jeans-sketch-185489899.jpg",
                           fit: BoxFit.fill,),
                         ),
                    ),
                   Container(
                     width: MediaQuery.of(context).size.width*0.60,
                     child: Column(
                       children: [
                         Padding(
                           padding: const EdgeInsets.only(top:40.0,left: 10),
                           child: Align(
                             alignment: Alignment.topLeft,
                             child: Text("Mens Pants",style: GoogleFonts.aBeeZee(fontSize: 16,color:Colors.black87,
                             fontWeight: FontWeight.w800),),
                           ),
                         ),
                         Padding(
                           padding: const EdgeInsets.only(left:8.0,top: 20),
                           child: Row(
                             children: [
                               Image.asset("drawable/dollar-symbol.png",height: 15,width: 15,color: 
                               Colors.orange,),
                               Padding(
                               padding: const EdgeInsets.only(top:0.0,left: 5),
                               child: Align(
                                 alignment: Alignment.topLeft,
                                 child: Text("210.00",style: GoogleFonts.aBeeZee(fontSize: 16,color:Colors.orange,
                                 fontWeight: FontWeight.bold),),
                               ),
                               ),
                               Spacer(),
                               Container(
                                 width: 101,
                                 height: 35,
                                 decoration: BoxDecoration(
                                   color: Colors.white,
                                   borderRadius: BorderRadius.circular(15),
                                   border: Border.all(width: 1,color: Color(0xffdedede))
                                 ),
                                 child: Row(
                                   children: [
                                    Padding(
                                      padding: const EdgeInsets.all(8.0),
                                      child: Icon(Icons.add,size: 15,),
                                    ),
                                    Spacer(),
                                    Text("1",style: GoogleFonts.aBeeZee(),),
                                    Spacer(),
                                    Padding(
                                      padding: const EdgeInsets.only(left:8.0,bottom: 10,right: 8),
                                      child: Icon(Icons.minimize,size: 15,),
                                    )
                                   ],
                                 ),
                               )
                             ],
                           ),
                         ),
                       ],
                     ),
                   )
                  ],
                ),
          ),
          Container(height: 1.2,color: Colors.grey[200],),
          Container(
                width: double.infinity,
                height: 150,
                child: Row(
                  children: [
                    Container(
                      margin: EdgeInsets.all(15),
                      width: MediaQuery.of(context).size.width*0.25,
                      height: 105,
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(10),
                        color: Colors.grey[200]
                      ),
                      child: Padding(
                           padding: const EdgeInsets.all(4.0),
                           child: Image.network("https://previews.123rf.com/images/smyrna35/smyrna352002/smyrna35200200162/141610516-knot-t-shirt-women-s-top-fashion-flat-sketch-template.jpg",
                           fit: BoxFit.fill,),
                         ),
                    ),
                   Container(
                     width: MediaQuery.of(context).size.width*0.60,
                     child: Column(
                       children: [
                         Padding(
                           padding: const EdgeInsets.only(top:40.0,left: 10),
                           child: Align(
                             alignment: Alignment.topLeft,
                             child: Text("Mens T-Shirt",style: GoogleFonts.aBeeZee(fontSize: 16,color:Colors.black87,
                             fontWeight: FontWeight.w800),),
                           ),
                         ),
                         Padding(
                           padding: const EdgeInsets.only(left:8.0,top: 20),
                           child: Row(
                             children: [
                               Image.asset("drawable/dollar-symbol.png",height: 15,width: 15,color: Colors.orange,),
                               Padding(
                               padding: const EdgeInsets.only(top:0.0,left: 5),
                               child: Align(
                                 alignment: Alignment.topLeft,
                                 child: Text("110.00",style: GoogleFonts.aBeeZee(fontSize: 16,color:Colors.orange,
                                 fontWeight: FontWeight.bold),),
                               ),
                               ),
                               Spacer(),
                               Container(
                                 width: 101,
                                 height: 35,
                                 decoration: BoxDecoration(
                                   color: Colors.white,
                                   borderRadius: BorderRadius.circular(15),
                                   border: Border.all(width: 1,color: Color(0xffdedede))
                                 ),
                                 child: Row(
                                   children: [
                                    Padding(
                                      padding: const EdgeInsets.all(8.0),
                                      child: Icon(Icons.add,size: 15,),
                                    ),
                                    Spacer(),
                                    Text("1",style: GoogleFonts.aBeeZee(),),
                                    Spacer(),
                                    Padding(
                                      padding: const EdgeInsets.only(left:8.0,bottom: 10,right: 8),
                                      child: Icon(Icons.minimize,size: 15,),
                                    )
                                   ],
                                 ),
                               )
                             ],
                           ),
                         ),
                       ],
                     ),
                   )
                  ],
                ),
          ),
        
                  ],
                ),
              ),
            ),
          ),
          Container(
            decoration: BoxDecoration(
              color: Colors.white,
               boxShadow: [
                          BoxShadow(
                            color: const Color(0xffdedede),
                            offset: Offset(4, 1),
                            blurRadius: 6,
                          ),
                        ],
              borderRadius: BorderRadius.only(topLeft: Radius.circular(30),
              topRight: Radius.circular(30))
            ),
            height: MediaQuery.of(context).size.height*0.30,
            child: SingleChildScrollView(
              scrollDirection: Axis.vertical,
              child: Column(
                mainAxisAlignment: MainAxisAlignment.start,
                children: [
                  Row(children: [
                    Align(
                    alignment: Alignment.centerLeft,
                    child: Padding(
                      padding: const EdgeInsets.only(top:30.0,left: 20),
                      child: Text('Cart Total',style: GoogleFonts.aBeeZee(fontSize:15),),
                    )),Spacer(),Padding(
                      padding: const EdgeInsets.only(top:30.0,left: 0),
                      child: Image.asset("drawable/dollar-symbol.png",height: 15,width: 15,color:
                               Colors.orange,),
                    ),Padding(
                      padding: const EdgeInsets.only(top:30.0,left: 0,right: 20),
                      child: Text('110.00',style: GoogleFonts.aBeeZee(fontSize:15,
                      fontWeight: FontWeight.bold,
                      color:Colors.orangeAccent),),
                    )
                  ],),

                  Row(children: [
                    Align(
                    alignment: Alignment.centerLeft,
                    child: Padding(
                      padding: const EdgeInsets.only(top:18.0,left: 20),
                      child: Text('Shipping Charge',style: GoogleFonts.aBeeZee(fontSize:15),),
                    )),Spacer(),Padding(
                      padding: const EdgeInsets.only(top:18.0,left: 0),
                      child: Image.asset("drawable/dollar-symbol.png",height: 15,width: 15,color:Colors.orangeAccent,),
                    ),Padding(
                      padding: const EdgeInsets.only(top:18.0,left: 0,right: 20),
                      child: Text('10.00',style: GoogleFonts.aBeeZee(fontSize:15,
                      fontWeight: FontWeight.bold,
                      color:Colors.orangeAccent),),
                    )
                  ],),
                  Padding(
                    padding: const EdgeInsets.only(top:18.0),
                    child: Container(height:1,color:Colors.grey[100]),
                  ),
                  Row(children: [
                    Align(
                    alignment: Alignment.centerLeft,
                    child: Padding(
                      padding: const EdgeInsets.only(top:18.0,left: 20),
                      child: Text('SubTotal',style: GoogleFonts.aBeeZee(fontSize:15,color:Colors.black,
                      fontWeight: FontWeight.bold ),),
                    )),Spacer(),Padding(
                      padding: const EdgeInsets.only(top:18.0,left: 0),
                      child: Image.asset("drawable/dollar-symbol.png",height: 15,width: 15,color:Colors.orangeAccent,),
                    ),Padding(
                      padding: const EdgeInsets.only(top:18.0,left: 0,right: 20),
                      child: Text('220.00',style: GoogleFonts.aBeeZee(fontSize:15,color:Colors.orangeAccent,fontWeight: FontWeight.bold),),
                    )
                  ],),

                  GestureDetector(
                    onTap:(){
                    //Get.to(MyAddressActivity());
                    },
                    child: Container(
                      margin: EdgeInsets.only(left:40,right: 40,top: 20),
                      height: 45,
                      width: double.infinity,
                      decoration: BoxDecoration(
                         color: Color(0xffF57F1E),
                        borderRadius: BorderRadius.circular(20)
                      ),
                      child: Center(child: Text("Continue",style: GoogleFonts.aBeeZee(fontSize:16,color:Colors.white),)),
                    ),
                  )
                ],
              ),
            ),)
         ],
      ),
    );
  }
}

  //  Padding(
  //            padding: const EdgeInsets.all(8.0),
  //            child: Container(
  //              height: 100,
  //              width: 90,
  //              decoration: BoxDecoration(
  //                color: Colors.white,
  //                borderRadius: BorderRadius.circular(10),
  //                 boxShadow: [
  //                         BoxShadow(
  //                           color: const Color(0xffdedede),
  //                           offset: Offset(4, 1),
  //                           blurRadius: 6,
  //                         ),
  //                       ],
  //              ),
  //              child: 
  //            ),
  //          ),