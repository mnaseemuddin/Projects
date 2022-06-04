




import 'dart:convert';
import 'package:anandmart/Activity/MyAddressActivtity.dart';
import 'package:anandmart/Activity/ReviewYourOrderActivity.dart';
import 'package:anandmart/ApplicationConfigration/ApiUrls.dart';
import 'package:anandmart/Model/TimeSlot.dart';
import 'package:anandmart/Model/TimeSlot1.dart';
import 'package:flutter/material.dart';
import 'package:get/instance_manager.dart';
import 'package:get/get.dart';
import 'package:http/http.dart'as http;
import 'package:google_fonts/google_fonts.dart';

class AddressAndDateTimeActivity extends StatefulWidget {
  final String completeAddress,Title,Locality,City,Address,AID,MobileNo;
  const AddressAndDateTimeActivity({ Key? key,required this.completeAddress,required this.Title,
  required this.Locality,required this.City,required this.Address,required this.AID,required this.MobileNo}) : super(key: key);

  @override
  _AddressAndDateTimeActivityState createState() => _AddressAndDateTimeActivityState();
}

class _AddressAndDateTimeActivityState extends State<AddressAndDateTimeActivity> {
  
  late Map map;
  late List gridList;
  List<TimeSlot>timeSlotList=[];
  List<TimeSlot1> timeSlotList1=[];
  bool selected=false;
  int rowIndex=0,rowIndex1=0;
  late Color Boxcolor,tvColor;
  bool IsLoading=true;
  String lasttimeSlot="",daydateSlot="";



 
  @override
  void initState() {
    getDeliveryTimeSlot();
    super.initState();
  }
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
         brightness: Brightness.dark,
     backgroundColor: Color(0xfff4811e),   
        title: Text("Address and Date & Time",style: GoogleFonts.aBeeZee(fontSize:16,color:Colors.white),),
    ),
    body: IsLoading==false?ListView(children: [
    
    Container(
      height: MediaQuery.of(context).size.height*0.81,
      child: SingleChildScrollView(
      physics: NeverScrollableScrollPhysics(),
        child: Column(
          children: [
             GestureDetector(
         onTap: (){
          Get.to(MyAddressActivity(CType: "",));
         },
         child: Container(
           height: 50,
           color: Colors.black,
           child: Row(
             mainAxisAlignment: MainAxisAlignment.start,
             children: [
          Padding(
            padding: const EdgeInsets.only(left:8.0),
            child: Icon(Icons.location_on,size: 20,color: Colors.white,),
          ),
          Expanded(
            child: Padding(
              padding: const EdgeInsets.only(left:18.0),
              child: RichText(
                textAlign: TextAlign.center,
                                              overflow: TextOverflow.ellipsis,
                text:TextSpan(text: widget.completeAddress,style: GoogleFonts.aBeeZee(fontSize:15,color: Colors.white))),
            ),
          ),
          Padding(
            padding: const EdgeInsets.only(right: 8),
            child: Icon(Icons.edit,size: 20,color: Colors.white,),
          ),

           ],),
         ),
     ),

    Container(height: 40,child: Center(
        child: Text("Choose delivery slot for this address",style:
         GoogleFonts.aBeeZee(fontSize: 14,color: Colors.black),),
    ),color: Colors.grey[200],),
   
     Container(
         height: 60,
         child: ListView.builder(
           scrollDirection: Axis.horizontal,
           itemCount: timeSlotList.length==null?0:timeSlotList.length,
           itemBuilder: (BuildContext ctx,int pos){
             var day=timeSlotList[pos].dayName.split(" ");
             var dayName=day[0].substring(0,3);
             var date=day[1];
             if(rowIndex==pos){
               daydateSlot=dayName+","+date;
               Boxcolor=Color(0xfff4811e);
               tvColor=Colors.white;
             }else{
              Boxcolor=Color(0xffffffff);
              tvColor=Colors.black;
             }
           return Padding(
             padding: const EdgeInsets.only(left:1.0,top:2,right: 2),
             child: GestureDetector(
               onTap: (){
                 setState(() {
                   daydateSlot=dayName+","+date;
                   rowIndex=pos;
                   timeSlotList1.clear();
                   var slot=timeSlotList[pos].slots.split(",");
                   for(int i=0;i<slot.length;i++){
                     TimeSlot1 timeSlot1=new TimeSlot1(ctimeSlot: slot[i], IsSelected: false);
                     timeSlotList1.add(timeSlot1);
                   }               
                 });
               },
               child: Container(
               height: 50,
               width: 96,
               color: Boxcolor,
               child: Padding(
                 padding: const EdgeInsets.all(8.0),
                 child: Column(
                   mainAxisAlignment: MainAxisAlignment.center,
                   children: [
    Text(dayName.toUpperCase(),textAlign: TextAlign.center,style: GoogleFonts.aBeeZee(color:tvColor,fontSize: 15,
    fontWeight: FontWeight.bold),),
Text(date,style: GoogleFonts.aBeeZee(color:tvColor,fontSize: 13),),
         ],),
               ),),
             ),
           );
         }),
     ),
    
  Container(
         height: 200,
         child: ListView.builder(
           scrollDirection: Axis.vertical,
           itemCount: timeSlotList1.length==null?0:timeSlotList1.length,
           itemBuilder: (BuildContext ctx,int pos){
             if(rowIndex1==pos){
               timeSlotList1[pos].IsSelected=true;
               lasttimeSlot=timeSlotList1[pos].ctimeSlot;
             }else{
                timeSlotList1[pos].IsSelected=false;
             }
           return Padding(
             padding: const EdgeInsets.only(left:1.0,top:2,right: 2),
             child: Container(
             height: 50,
             width: 96,
             color: Color(0xffffffff),
             child: Padding(
               padding: const EdgeInsets.all(8.0),
               child: Row(
                 children: [
                   timeSlotList1[pos].IsSelected==false?GestureDetector(
                     onTap: (){
                       setState(() {
                        rowIndex1=pos;
                        lasttimeSlot=timeSlotList1[pos].ctimeSlot;
                       });
                     },
                     child: Container(
                       height: 20,
                       width: 20,
                       decoration: BoxDecoration(
                                borderRadius: BorderRadius.circular(10),
                                color: const Color(0xffffffff),
                                border: Border.all(width: 1,color: Colors.black38)
                              ),
                     ),
                   ):GestureDetector(
                     onTap: (){
                       setState(() {
                       rowIndex1=pos;

                       });
                     },
                     child: Container(
                       height: 20,
                       width: 20,
                       decoration: BoxDecoration(
                                borderRadius: BorderRadius.circular(10),
                                color: const Color(0xfff4811e),
                                border: Border.all(width: 1,color: Colors.white)
                              ),
                              child: Icon(Icons.check,color: Colors.white,size: 10,),
                     ),
                   ),
                 Padding(
                   padding: const EdgeInsets.only(left:8.0),
                   child: Text(timeSlotList1[pos].ctimeSlot,textAlign: TextAlign.center,style: GoogleFonts.aBeeZee(
                     color:Colors.black,
                   fontSize: 15),),
                 ),
         ],),
             ),),
           );
         }),
     ),
    
          ],
        ),
      ),
    ),
    GestureDetector(
      onTap: (){
        Get.to(ReviewYourOrderActivity(Title: widget.Title,Locality: widget.Locality,City: widget.City,Address:
          widget.Address,timeSlot: lasttimeSlot,daydateSlots: daydateSlot,AID: widget.AID,MobileNo: widget.MobileNo,));
      },
      child: Container(
  height: MediaQuery.of(context).size.height*0.06,
  color: Color(0xfff4811e),
  child: Center(child: Text("Proceed to Payment",style: GoogleFonts.aBeeZee(color:Colors.white,
  fontSize: 15,fontWeight: FontWeight.w500))),
),
    )
    ],):Center(child: CircularProgressIndicator(strokeWidth: 6.0,color: Color(0xfff4811e))),);
  }
  getDeliveryTimeSlot()async{
    
    final response=await http.get(Uri.parse(ApiUrls.GetTimeSlotAPI));
    if(response.statusCode==200){
     map=json.decode(response.body);
    setState(() {
      IsLoading=false;
      gridList=map["timeslots"];
      for(int i=0;i<gridList.length;i++){
       TimeSlot timeSlot=new TimeSlot(id: gridList[i]["id"], dayName: gridList[i]["day_name"],
        slots: gridList[i]["slots"]);
       timeSlotList.add(timeSlot);
       if(i==0){
          timeSlotList1.clear();
                 var slot=gridList[i]["slots"].split(",");
                 for(int i=0;i<slot.length;i++){
                   TimeSlot1 timeSlot1=new TimeSlot1(ctimeSlot: slot[i], IsSelected: false);
                   timeSlotList1.add(timeSlot1);
          }
       }
      }
    });
    }

  }
}