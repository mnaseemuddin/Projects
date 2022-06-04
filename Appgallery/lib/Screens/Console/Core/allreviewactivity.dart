
import 'package:appgallery/CommonUI/apptextview.dart';
import 'package:appgallery/Model/allconsoleappsmodel.dart';
import 'package:flutter/material.dart';
import 'package:flutter_rating_bar/flutter_rating_bar.dart';
import 'package:get/get.dart';
import 'package:google_fonts/google_fonts.dart';

import '../../../Resources/color.dart';

class AllReviewActivity extends StatefulWidget {
  final String appName;
   List<Review>reviewList=[];
   AllReviewActivity({Key? key,required this.appName,
    required this.reviewList}) : super(key: key);

  @override
  _AllReviewActivityState createState() => _AllReviewActivityState();
}

class _AllReviewActivityState extends State<AllReviewActivity> {


  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: appGreyColor,
      appBar: AppBar(
        iconTheme: const IconThemeData(
          color: Colors.black, //change your color here
        ),
        backgroundColor: appGreyColor,
        title: Column(
          children: [
            Align(
              alignment: Alignment.centerLeft,
              child: Text(
                'Reviews',
                style: GoogleFonts.aBeeZee(fontSize: 16.0, color: appBlackColor),
              ),
            ),
            Align(
                alignment: Alignment.centerLeft,
                child: HeadingText(title: widget.appName,fontSize: 13,color: appBlackColor,)),
          ],
        ),
      ),
      body: ListView.builder(
          itemCount: widget.reviewList.length??0,
          itemBuilder: (ctx,index){
            Review review=widget.reviewList.elementAt(index);
        return Padding(
          padding: const EdgeInsets.only(top:8.0),
          child: Container(height: 95,color: appWhiteColor,
          child: Column(
            children: [
              Padding(
                padding: const EdgeInsets.only(left: 15.0, top: 8, bottom: 5),
                child: Row(
                  children: [
                    SizedBox(
                      width: MediaQuery.of(context).size.width *.12,
                      child: ClipRRect(
                          borderRadius: BorderRadius.circular(28),
                          child: Image.network(
                            review.userImage,
                            height: 47,
                            width: 47,
                            fit: BoxFit.fill,
                          )),
                    ),
                    SizedBox(
                      width: MediaQuery.of(context).size.width * .77,
                      child: Padding(
                        padding: const EdgeInsets.only(top:8.0,left: 10),
                        child: Column(
                          children: [
                            Row(
                              children: [
                                HeadingText(title:review.userName.capitalizeFirst.toString()),
                                const Spacer(),
                                HeadingText(title: review.dateTime)
                              ],
                            ),
                            Padding(
                              padding: const EdgeInsets.only(top:5.0,left: 0),
                              child: Align(
                                alignment: Alignment.centerLeft,
                                child: RatingBar.builder(
                                  unratedColor: Colors.grey[400],
                                  initialRating: review.ratings.toDouble(),
                                  itemSize: 16,
                                  minRating: 1,
                                  direction: Axis.horizontal,
                                  allowHalfRating: true,
                                  itemCount: 5,
                                  itemBuilder: (context, _) => const Icon(
                                    Icons.star_rounded,
                                    color: Colors.black54,
                                  ),
                                  onRatingUpdate: (rating) {
                                    print(rating);
                                  },
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),
                    ),
                  ],
                ),
              ),
              Align(
                  alignment: Alignment.centerLeft,
                  child: Padding(
                    padding: const EdgeInsets.only(top: 4.0,left: 24),
                    child: Text(review.review.capitalizeFirst.toString(),
                      maxLines: 1,overflow: TextOverflow.ellipsis,
                    ),
                  ))
            ],
          ),),
        );
      }),
    );
  }
}
