import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/shared/shared.dart';

class SHCircularAvatar extends StatelessWidget {
  final double radius;
  final String url;
  const SHCircularAvatar({Key? key, required this.radius, required this.url})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      child: CircleAvatar(
        backgroundColor: ColorConstants.black87,
        radius: radius,
        backgroundImage: CachedNetworkImageProvider(ApiConstants.imageUrl+url.replaceAll('%2B', '').replaceAll('%', '')),
      ),
      decoration: BoxDecoration(
        boxShadow: [
          BoxShadow(color: ColorConstants.black, blurRadius: 15, offset: Offset(0, 0))
        ],
        borderRadius: BorderRadius.all(Radius.circular(radius)),
      ),
    );
  }
}

// class SHCircularAvater extends StatefulWidget {
//
//   final double radius;
//   final Color color;
//   final String url;
//   const SHCircularAvater({Key? key,this.url, this.radius, this.color}) : super(key: key);
//
//   @override
//   _SHCircularAvaterState createState() => _SHCircularAvaterState();
// }
//
// class _SHCircularAvaterState extends State<SHCircularAvater> {
//
//
//   @override
//   Widget build(BuildContext context) {
//     return FutureBuilder(
//         future: downloadImage('$BASE_URL_IMAGE${widget.url}'),
//         builder: (context, snapshot){
//
//           if(snapshot.hasData){
//             return Container(child: CircleAvatar(backgroundColor: Colors.black87, radius: widget.radius,
//               backgroundImage: FileImage(snapshot.data,),),
//               decoration: BoxDecoration(boxShadow: [BoxShadow(color: Colors.black, blurRadius: 15, offset: Offset(0, 0))],
//                 borderRadius: BorderRadius.all(Radius.circular(widget.radius)),
//               ),);
//           }else {
//             return CircleAvatar(backgroundColor: Colors.black87, radius: widget.radius,
//               backgroundImage: new AssetImage('assets/images/bg_img.jpg'),);
//           }
//         });
//   }
// }
