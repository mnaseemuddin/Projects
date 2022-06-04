
import 'package:appgallery/CommonUI/apptextfield.dart';
import 'package:appgallery/CommonUI/apptextview.dart';
import 'package:appgallery/Resources/color.dart';
import 'package:appgallery/Utils/dailog.dart';
import 'package:appgallery/Utils/loadingoverlay.dart';
import 'package:appgallery/Utils/routes.dart';
import 'package:appgallery/apis/apirepositary.dart';
import 'package:appgallery/apis/userdata.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_icons/flutter_icons.dart';
import 'package:flutter_rating_bar/flutter_rating_bar.dart';

import '../../CommonUI/progressdialog.dart';

class PostReviewActivity extends StatefulWidget {
 final String appName,appIcon,appID;
  const PostReviewActivity({Key? key,required this.appName,required this.appIcon,
  required this.appID}) : super(key: key);

  @override
  _PostReviewActivityState createState() => _PostReviewActivityState();
}

class _PostReviewActivityState extends State<PostReviewActivity> {
  
  var photoUrl='',name='';
  double rating=0.0;
  var controller=TextEditingController();
  
  @override
  void initState() {
    super.initState();
    getPlayStoreProfileDetails();
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
      appBar: AppBar(
        // ignore: prefer_const_constructors
        leading: IconButton(
          icon: const Icon(Icons.clear,size: 25,color: appBlackColor,), onPressed: () {
          navReturn(context);
        },
        ),
        title: Row(children: [

          Expanded(
              flex: 10,
              child: Image.network(widget.appIcon,height: 30,width: 30,)),
          Expanded(
            flex: 70,
            child: Padding(
              padding: const EdgeInsets.only(left: 8.0),
              child: Column(
                children: [
                  Align(
                    alignment: Alignment.centerLeft,
                    child: Padding(
                      padding: const EdgeInsets.only(left:0.0),
                      child: HeadingText(title: widget.appName,color: appBlackColor,fontSize: 17,),
                    ),
                  ),
                  const SizedBox(height: 2,),
                  Align(
                      alignment: Alignment.centerLeft,
                      child: HeadingText(title: "Rate this app",color: appBlackColor,fontSize: 12,)),
                ],
              ),
            ),
          ),
          Expanded(
            flex: 15,
            child: TextButton(onPressed: () {

              if(rating==0.0){
                return DialogUtils.instance.edgeAlerts(context,"Please select the rating stars .");
              }
              if(controller.text.isEmpty){
                return DialogUtils.instance.edgeAlerts(context,"Please Enter your Review description .");
              }
              submitReview();

            }, child: const Text("Post",style: TextStyle(fontWeight: FontWeight.w500,
            fontSize: 15),),),
          )
        ],),
      ),
      body: photoUrl==""?LoadingOverlay():Column(children: [
        Row(
          children: [
            Padding(
              padding: const EdgeInsets.fromLTRB(10.0,15,0,0),
              child: ClipRRect(
                  borderRadius: BorderRadius.circular(80),
                  child: Image.network(photoUrl,height: 45,width: 45,)),
            ),

            Expanded(
              child: Column(
                children: [
                  Align(
                    alignment: Alignment.centerLeft,
                    child: Padding(
                      padding: const EdgeInsets.fromLTRB(20.0,15,0,0),
                      child: HeadingText(title: name),
                    ),
                  ),

                  Align(
                    alignment: Alignment.centerLeft,
                    child: Padding(
                      padding: const EdgeInsets.fromLTRB(20.0,5,0,0),
                      child: HeadingText(title: 'Reviews are public and include your account.',
                        color: Colors.black.withOpacity(0.6),),
                    ),
                  ),
                ],
              ),
            )
            
          ],
        ),
        const SizedBox(height: 30,),
        Padding(
          padding: const EdgeInsets.fromLTRB(0.0,0,0,0),
          child: RatingBar.builder(
            unratedColor: Colors.grey[400],
            itemSize: 47,
            direction: Axis.horizontal,
            itemCount: 5,
            initialRating: rating,
            itemBuilder: (context,_) =>  Icon(
              Icons.star_rounded,
              color: Colors.green[500],
            ), onRatingUpdate: (double value) {
              setState(() {
                rating=value;
              });
          },
          ),
        ),
        descriptionField()
      ],),
    );
  }

  void getProfile(String email, String token) {
    Map<String,dynamic>body= {
      "email_address": email,
      "token": token
    };
    getProfileAPI(body).then((value){
      if(value.status){
        setState(() {
          photoUrl= value!.data["user_image"];
          name=value!.data["name"];
        });
      }else{
        toast(value.message.toString());
      }
    });
  }

  descriptionField() {
    return Padding(
      padding: const EdgeInsets.only(left: 10.0,right: 10,top: 15),
      child: SizedBox(
        height: 140,
        child: TextFormField(
          style: const TextStyle(fontSize: 14.5),
          controller: controller,

          textInputAction: TextInputAction.next,
          inputFormatters: [
            LengthLimitingTextInputFormatter(500),
          ],
          maxLength: 500,
          decoration: InputDecoration(
              border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(10),
                  borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
              ),
              errorBorder: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(10),
                  borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
              ),
              enabledBorder: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(10),
                  borderSide: const BorderSide(width: 1.0,color: Color(0xffece7e7))
              ),
              focusedBorder: OutlineInputBorder(
                borderRadius: BorderRadius.circular(10),
                borderSide: const BorderSide(width: 1,
                    color: Color(0xff84a2dc)),
              ),

              contentPadding: const EdgeInsets.only(top: 20,left: 15),
              hintText: "Describe your experience",
              hintStyle: TextStyle(color: Colors.grey[600],fontSize: 13.5),
              filled: false,
              fillColor: Colors.white
          ),
        ),
      ),
    );
  }

  void submitReview() {
    showDialog(context: context, builder: (context) => ProgressDialog(),
        barrierDismissible: false).then((value){
      print(value);
    },);
    var imgPath=photoUrl.split("/");
    Map<dynamic,String>body={
      "app_id":widget.appID,
      "ratings":rating.toString(),
      "review":controller.text,
      "user_name":name,
      "user_image":imgPath.last.toString()
    };
    submitReviewAPI(body).then((value){
      if(value.status){
        setState(() {
          toast(value.message);
          navReturn(context);
          navReturn(context);
          controller.clear();
          rating=0.0;
        });
      }else{
        toast(value.message.toString());
        navReturn(context);
      }
    });

  }
}
