// import 'dart:collection';
// import 'dart:math';
// import 'dart:typed_data';
// import 'dart:ui';
//
// import 'package:flutter/material.dart';
// import 'package:flutter/services.dart';
// import 'package:flutter/widgets.dart';
// import 'package:get/get.dart';
// import 'package:google_fonts/google_fonts.dart';
// import 'package:path_provider/path_provider.dart';
// import 'package:screenshot/screenshot.dart';
// import 'package:tailor/Activity/FinalSketchViewActivity.dart';
// import 'package:tailor/ApiRepositary.dart';
// import 'package:tailor/Model/AllSketch.dart';
// import 'package:tailor/Model/BottomSketch.dart';
// import 'package:tailor/Model/model.dart';
// import 'package:tailor/Support/UISupport.dart';
// import 'package:tailor/main.dart';
//
// import 'DashBoardActivity.dart';
//
// class AllSketchesActivity extends StatefulWidget {
//   final String image;
//   final int categoryId;
//   const AllSketchesActivity({Key? key, required this.categoryId, required this.image}) : super(key: key);
//
//   @override
//   _AllSketchesActivityState createState() => _AllSketchesActivityState();
// }
//
// class _AllSketchesActivityState extends State<AllSketchesActivity> {
//
//   ScreenshotController _screenshotController = ScreenshotController();
//
//   late List<AllSketch> allSketchList = [];
//   var IsVisiblityMoreSketches = false;
//   int selectSketch = 100;
//   late List<BottomSketch> bottomSketchNameList = [];
//
//   double minHeight = 90;
//   int rowIndex = 0;
//   int _showIndex = -1;
//   double _fraction = 1.8;
//   String? _fabric;// = 'http://3.108.177.150:8000/files/upload/texture_color/red_texture.png';
//   String? _color;
//   // int _undoIndex = 0;
//   List<ImageTile> _topMenuItems = [
//     ImageTile(id: 0, image: 'drawable/addfile.png', name: 'design'),
//     ImageTile(id: 1, image: 'drawable/addfile.png', name: 'Specs'),
//     ImageTile(id: 2, image: 'drawable/dry-clean.png', name: 'Ease'),
//     ImageTile(id: 3, image: 'drawable/book.png', name: 'Sawing'),
//     ImageTile(id: 4, image: 'drawable/roll.png', name: 'Yardage'),
//   ];
//
//   CategoryWiseAttribute? _categoryAttribute;
//   List<IndexImage> _indexedImage = [];
//
//   Map<int, SubAttributeData> _map = {};
//
//
//   List<PositionItem>_list = [
//     PositionItem(image: 'assets/abackgroung.png', left: 128, top: 0),
//     PositionItem(image: 'assets/neck.png', left: 191, top: 0),
//     PositionItem(image: 'assets/sleeves.png', left: 0, top: 22.8),
//     PositionItem(image: 'assets/pockets.png', left: 152, top: 297),
//   ];
//
//   List<SavedItem> _forward = [];
//   List<SavedItem> _backward = [];
//   Uint8List? _image;
//
//   @override
//   void initState() {
//     super.initState();
//     categoryAttributeAPI(4).then((value){
//       setState(() {
//         if(value.status){
//           _categoryAttribute = value.data;
//           _categoryAttribute!.data.asMap().forEach((index, element) {
//             SubAttributeData e = element.subAttributeData.first;//.elementAt(1);
//             _map[element.zIndex] = e;
//               _indexedImage.add(IndexImage(id: e.tblSubAttributeId, zindex: element.zIndex, top: e.top, left: e.left, width: e.width, height: e.height,image: e.image));
//           });
//
//           Attribute _attribute = _categoryAttribute!.data.first;
//           SubAttributeData subAttr = _attribute.subAttributeData.first;
//           _indexedImage.add(IndexImage(id: _attribute.tblAttributeId, zindex: 0, top: 504/2, left: 125 + 221/2, width: 221, height: 504, image: _attribute.image));
//           _map[0] = SubAttributeData(tblSubAttributeId: _attribute.tblAttributeId, attributeId: subAttr.attributeId, top: 504/2, left: 125 + 221/2, width: 221, height: 504, subAttributeName: subAttr.subAttributeName, image: _attribute.image);
//           _indexedImage.sort((a, b) => a.zindex>b.zindex?1:-1);
//         }
//       });
//     });
//
//   }
//
//   Future<ApiResponse> getJson() async{
//     String str =  await rootBundle.loadString('drawable/aacheck.json');
//     CategoryWiseAttribute model=categoryWiseAttributeFromJson(str);
//     return ApiResponse(status: true, message: data["message"], data: model);
//   }
//
//   // @override
//   // void initState() {
//   //   AllSketch allSketch = new AllSketch(
//   //       IsCheck: false,
//   //       name: "JumpSuits".toString().toUpperCase(),
//   //       imgUrls:
//   //           "https://previews.123rf.com/images/smyrna35/smyrna352002/smyrna35200200162/141610516-knot-t-shirt-women-s-top-fashion-flat-sketch-template.jpg");
//   //   allSketchList.add(allSketch);
//   //
//   //   AllSketch allSketch1 = new AllSketch(
//   //       IsCheck: false,
//   //       name: "Sleeves".toString().toUpperCase(),
//   //       imgUrls:
//   //           "https://previews.123rf.com/images/smyrna35/smyrna352002/smyrna35200200162/141610516-knot-t-shirt-women-s-top-fashion-flat-sketch-template.jpg");
//   //   allSketchList.add(allSketch1);
//   //
//   //   AllSketch allSketch3 = new AllSketch(
//   //       IsCheck: false,
//   //       name: "Necklines".toString().toUpperCase(),
//   //       imgUrls:
//   //           "https://previews.123rf.com/images/smyrna35/smyrna352002/smyrna35200200162/141610516-knot-t-shirt-women-s-top-fashion-flat-sketch-template.jpg");
//   //   allSketchList.add(allSketch3);
//   //
//   //   AllSketch allSketch4 = new AllSketch(
//   //       IsCheck: false,
//   //       name: "Colors".toString().toUpperCase(),
//   //       imgUrls:
//   //           "https://previews.123rf.com/images/smyrna35/smyrna352002/smyrna35200200162/141610516-knot-t-shirt-women-s-top-fashion-flat-sketch-template.jpg");
//   //   allSketchList.add(allSketch4);
//   //
//   //   AllSketch allSketch5 = new AllSketch(
//   //       IsCheck: false,
//   //       name: "Fabric".toString().toUpperCase(),
//   //       imgUrls:
//   //           "https://previews.123rf.com/images/smyrna35/smyrna352002/smyrna35200200162/141610516-knot-t-shirt-women-s-top-fashion-flat-sketch-template.jpg");
//   //   allSketchList.add(allSketch5);
//   //
//   //   BottomSketch bottomSketch =
//   //       new BottomSketch(SketchName: "Woven".toUpperCase(), IsVisiblity: false);
//   //   bottomSketchNameList.add(bottomSketch);
//   //   BottomSketch bottomSketch1 = new BottomSketch(
//   //       SketchName: "Natural Waist Seam".toUpperCase(), IsVisiblity: false);
//   //   bottomSketchNameList.add(bottomSketch1);
//   //   BottomSketch bottomSketch2 = new BottomSketch(
//   //       SketchName: "Shoulder Princess Bodice".toUpperCase(),
//   //       IsVisiblity: false);
//   //   bottomSketchNameList.add(bottomSketch2);
//   //   BottomSketch bottomSketch3 = new BottomSketch(
//   //       SketchName: "Shoulder Princess Bodice".toUpperCase(),
//   //       IsVisiblity: false);
//   //   bottomSketchNameList.add(bottomSketch3);
//   //   BottomSketch bottomSketch4 = new BottomSketch(
//   //       SketchName: "Center Back Seam".toUpperCase(), IsVisiblity: false);
//   //   bottomSketchNameList.add(bottomSketch4);
//   //   BottomSketch bottomSketch5 = new BottomSketch(
//   //       SketchName: "Round Jewal Neak".toUpperCase(), IsVisiblity: false);
//   //   bottomSketchNameList.add(bottomSketch5);
//   //   BottomSketch bottomSketch6 = new BottomSketch(
//   //       SketchName: "Center Back Seam".toUpperCase(), IsVisiblity: false);
//   //   bottomSketchNameList.add(bottomSketch6);
//   //   super.initState();
//   // }
//
//   @override
//   Widget build(BuildContext context) {
//     Size size = MediaQuery.of(context).size;
//
//     return Scaffold(
//       appBar: AppBar(
//         brightness: Brightness.dark,
//         actions: [
//           Container(
//             child: Row(
//               mainAxisAlignment: MainAxisAlignment.center,
//               children: [
//                 _backward.length>0?IconButton(onPressed: (){
//                   if(_backward.length>0){
//                     SavedItem item = _backward.elementAt(0);
//                     _map[item.key] = item.data;
//                     _forward.add(item);
//                     _backward.removeAt(0);
//                     setState(() {});
//                   }
//                 }, icon: Icon(
//                   Icons.undo,
//                   color: Colors.white,
//                   size: 30,
//                 )):SizedBox(),
//                 Padding(
//                   padding: const EdgeInsets.only(left: 15.0, right: 115),
//                   child: _forward.length>0?IconButton(onPressed: (){
//
//                     if(_forward.length>0){
//                       SavedItem item = _forward.elementAt(0);
//                       _map[item.key] = item.data;
//                       _backward.add(item);
//                       _forward.removeAt(0);
//                       setState(() {});
//                     }
//
//                   }, icon: Icon(
//                     Icons.redo,
//                     color: Colors.white,
//                     size: 30,
//                   )):SizedBox(),
//                 ),
//                 GestureDetector(
//                   onTap: () async{
//
//                     final directory = (await getApplicationDocumentsDirectory ()).path; //from path_provide package
//                     String fileName = '${DateTime.now().microsecondsSinceEpoch}';
//                     String path = '$directory';
//
//                     _screenshotController.captureAndSave(
//                       path, fileName: fileName,
//                     ).then((value){
//                       Get.to(FinalSketchViewActivty(imagePath: '$path/$fileName',));
//                     });
//                   },
//                   child: Padding(
//                     padding: const EdgeInsets.all(8.0),
//                     child: Align(
//                         alignment: Alignment.centerRight,
//                         child: Icon(
//                           Icons.check,
//                           color: Colors.white,
//                           size: 32,
//                         )),
//                   ),
//                 ),
//               ],
//             ),
//           ),
//         ],
//         backgroundColor: UISupport.color,
//         leading: Builder(
//           builder: (context) => IconButton(
//             icon: Image.asset(
//               "drawable/close.png",
//               height: 16,
//               width: 20,
//               color: Colors.white,
//             ),
//             onPressed: () => showDialog(
//               context: context,
//               builder: (BuildContext context) {
//                 // return object of type Dialog
//                 return AlertDialog(
//                   content: new Text(
//                       "Are you sure you want to delete all the elements and start the design again?"),
//                   actions: <Widget>[
//                     new FlatButton(
//                       child: new Text("Cancel"),
//                       onPressed: () {
//                         Navigator.of(context).pop();
//                       },
//                     ),
//                     new FlatButton(
//                       child: new Text("OK"),
//                       onPressed: () {
//                         Get.offAll(DashBoard());
//                       },
//                     ),
//                     // usually buttons at the bottom of the dialog
//                   ],
//                 );
//               },
//             ),
//           ),
//         ),
//       ),
//       backgroundColor: Colors.white,
//       body: _categoryAttribute == null? Center(child: CircularProgressIndicator(),)
//       :Builder(builder: (context){
//         _fraction = (_categoryAttribute!.canvasWidth)/(2*size.width/2.5);
//         return Column(children: [
//
//           SizedBox(height: 16,),
//           _topMenuUI(),
//
//           SizedBox(height: 16,),
//
//           Expanded(child: Stack(children: [
//
//             Row(children: [
//               Expanded(child: Container(), flex: 20,),
//               Expanded(child: Container(
//                 padding: EdgeInsets.symmetric(horizontal: 8),
//                 child: Screenshot(child: _createImageUI(), controller: _screenshotController,),), flex: 80,),
//             ],),
//
//             Divider(),
//             // ListView.builder(itemBuilder: (context, index){
//             //   Attribute attribute = _categoryAttribute!.data.elementAt(index);
//             //   List<SubAttributeData> subAttribute = attribute.subAttributeData;
//             //   return Container(height: 136,
//             //     child: Row(children: [
//             //       Expanded(child: ItemSelector(image: attribute.image, name: attribute.attributeName,
//             //         onPress: (){
//             //           setState(() {
//             //             if(index == _showIndex)_showIndex = -1;
//             //             else _showIndex = index;
//             //           });
//             //         },), flex: 25,),
//             //       Expanded(child: _showIndex==index?Container(
//             //         child: ListView.builder(itemBuilder: (context, cIndex){
//             //           SubAttributeData subAttributeData = subAttribute.elementAt(cIndex);
//             //           return ItemSelector(image: subAttributeData.image, name: subAttributeData.subAttributeName,
//             //           onPress: (){
//             //             setState(() {
//             //               _map[attribute.zIndex] = subAttributeData;
//             //               _showIndex = -1;
//             //             });
//             //             print(attribute.zIndex);
//             //           },);
//             //         }, itemCount: subAttribute.length, scrollDirection: Axis.horizontal,),):Container(), flex: 75,),
//             //     ],),);
//             // }, itemCount: _categoryAttribute!.data.length,)
//             _createAttributeList(),
//           ],)),
//
//
//           // Container(height: 72,),
//
//         ],);
//         // return Stack(
//         //   alignment: Alignment.center,
//         //   children: [
//         //   Column(children: [
//         //
//         //     SizedBox(height: 16,),
//         //     _topMenuUI(),
//         //     SizedBox(height: 16,),
//         //
//         //     Expanded(child: Stack(children: [
//         //
//         //       Row(children: [
//         //         Expanded(child: Container(), flex: 25,),
//         //         Expanded(child: Container(
//         //           padding: EdgeInsets.symmetric(horizontal: 8),
//         //           child: _createImageUI(),), flex: 75,),
//         //       ],),
//         //
//         //       // ListView.builder(itemBuilder: (context, index){
//         //       //   Attribute attribute = _categoryAttribute!.data.elementAt(index);
//         //       //   List<SubAttributeData> subAttribute = attribute.subAttributeData;
//         //       //   return Container(height: 136,
//         //       //     child: Row(children: [
//         //       //       Expanded(child: ItemSelector(image: attribute.image, name: attribute.attributeName,
//         //       //         onPress: (){
//         //       //           setState(() {
//         //       //             if(index == _showIndex)_showIndex = -1;
//         //       //             else _showIndex = index;
//         //       //           });
//         //       //         },), flex: 25,),
//         //       //       Expanded(child: _showIndex==index?Container(
//         //       //         child: ListView.builder(itemBuilder: (context, cIndex){
//         //       //           SubAttributeData subAttributeData = subAttribute.elementAt(cIndex);
//         //       //           return ItemSelector(image: subAttributeData.image, name: subAttributeData.subAttributeName,
//         //       //           onPress: (){
//         //       //             setState(() {
//         //       //               _map[attribute.zIndex] = subAttributeData;
//         //       //               _showIndex = -1;
//         //       //             });
//         //       //             print(attribute.zIndex);
//         //       //           },);
//         //       //         }, itemCount: subAttribute.length, scrollDirection: Axis.horizontal,),):Container(), flex: 75,),
//         //       //     ],),);
//         //       // }, itemCount: _categoryAttribute!.data.length,)
//         //       _createAttributeList()
//         //     ],)),
//         //     Container(height: 72,),
//         //   ],),
//         //   // _maskImage('http://azadariapp.com/corneroffice/color_dress.png', 'http://3.108.177.150:8000/files/upload/attribute/dress.png')
//         //   _maskImage('http://3.108.177.150:8000/files/upload/attribute/dress.png', 'http://3.108.177.150:8000/files/upload/attribute/dress.png')
//         // ],);
//       }),
//     );
//   }
//
//   Widget _createAttributeList(){
//     List<Widget> list = [];
//     // int mIndex = 0;
//       _categoryAttribute!.data.asMap().forEach((index, attribute) {
//       List<SubAttributeData> subAttribute = attribute.subAttributeData;
//       list.add(Container(height: 90, child: Row(children: [
//         Expanded(child: ItemSelector(image: attribute.subAttributeData.first.image, name: attribute.attributeName,
//           onPress: (){
//             setState(() {
//               if(index == _showIndex)_showIndex = -1;
//               else _showIndex = index;
//             });
//           },), flex: 20,),
//         Expanded(child: _showIndex==index?Container(
//           child: ListView.builder(itemBuilder: (context, cIndex){
//             SubAttributeData subAttributeData = subAttribute.elementAt(cIndex);
//             return ItemSelector(image: subAttributeData.image, name: subAttributeData.subAttributeName,
//               onPress: (){
//                 setState(() {
//                   _map[attribute.zIndex] = subAttributeData;
//                   _backward.add(SavedItem(index: DateTime.now().millisecond, fabric: _fabric, color: _color, key: attribute.zIndex, data: subAttributeData));
//                   _showIndex = -1;
//                 });
//                 print(attribute.zIndex);
//               },);
//           }, itemCount: subAttribute.length, scrollDirection: Axis.horizontal,),):Container(), flex: 80,),
//       ],),));
//       // mIndex +=1;
//     });
//
//       // Add Textur
//       int textureIndex = _categoryAttribute!.data.length+1;
//
//     SATexture value = _categoryAttribute!.texture.first;
//       list.add(Container(height: 90, child: Row(children: [
//         Expanded(child: ItemSelector(image: value.textureImage, name: value.textureName,onPress: (){
//           setState(() {
//             if(textureIndex == _showIndex)_showIndex = -1;
//             else _showIndex = textureIndex;
//           });
//         }), flex: 20,),
//         Expanded(child: _showIndex==textureIndex?Container(
//           child: ListView.builder(itemBuilder: (context, cIndex){
//             SATexture sTexture = _categoryAttribute!.texture.elementAt(cIndex);
//             return ItemSelector(image: sTexture.textureImage, name: sTexture.textureName,
//               onPress: (){
//                 setState(() {
//                   _fabric = sTexture.textureImage;
//                   _color = null;
//                   _showIndex = -1;
//                 });
//                 // print(attribute.zIndex);
//               },);
//           }, itemCount: _categoryAttribute!.texture.length, scrollDirection: Axis.horizontal,),):Container(), flex: 80,)
//       ],),));
//
//     int colorIndex = _categoryAttribute!.data.length+2;
//
//     // Add Color attribute
//     AttributeColor attributeColor = _categoryAttribute!.attributeColor.first;
//
//     list.add(Container(
//       margin: EdgeInsets.only(left: 6),
//       height: 64, child: Row(children: [
//       GestureDetector(
//         child: Container(
//           margin: EdgeInsets.all(4),
//           width: 64,
//           height: 64,
//           decoration: BoxDecoration(
//               color: _hexToColor(attributeColor.colorCode),
//               borderRadius: BorderRadius.circular(8)
//           ),
//         ),
//         onTap: (){
//           setState(() {
//             if(colorIndex == _showIndex)_showIndex = -1;
//             else _showIndex = colorIndex;
//           });
//         },
//       ),
//       Expanded(child: _showIndex==colorIndex?Container(
//         child: ListView.builder(itemBuilder: (context, cIndex){
//           AttributeColor sColor = _categoryAttribute!.attributeColor.elementAt(cIndex);
//           return ColorSelector(color: sColor.colorCode,
//             onPress: (){
//               setState(() {
//                 // _map[attribute.zIndex] = subAttributeData;
//                 _color = sColor.colorCode;
//                 _fabric = null;
//                 _showIndex = -1;
//               });
//               // print(attribute.zIndex);
//             },);
//         }, itemCount: _categoryAttribute!.attributeColor.length, scrollDirection: Axis.horizontal,),):Container(), flex: 75,)
//     ],),));
//
//     return ListView(children: list,);
//   }
//
//
//   Widget _createImageUI(){
//     List<Widget>list = [];
//     var sortedKeys = _map.keys.toList()..sort();
//     for(int i=0; i<sortedKeys.length; i++){
//       int key = sortedKeys.elementAt(i);
//       SubAttributeData element = _map[key]!;
//       list.add(
//           Positioned(
//           top: (element.top -element.height/2)/_fraction,
//           left: (element.left - element.width/2)/_fraction,
//           child: _fabric==null&&_color==null?Image.network(element.image, width: element.width/_fraction, height: element.height/_fraction,):
//           _maskImage(element.image.replaceAll('.png', '_dn.png'), element.image))
//           // child: Image.network(element.image, width: element.width/_fraction, height: element.height/_fraction,))
//       );
//     }
//
//     return Stack(children: list,);
//   }
//
//   // Widget _createImageUINew(){
//   //   List<Widget>list = [];
//   //   _list.forEach((element) {
//   //     list.add(Positioned(
//   //         top: element.top,
//   //         left: element.left, //right: 0,
//   //         child: Image.asset(element.image)));
//   //   });
//   //   return Stack(children: list,);
//   // }
//   //
//   //
//   Widget _topMenuUI() => Container(
//     height: 72,
//     child: ListView.builder(itemBuilder: (context, index){
//       ImageTile tile = _topMenuItems.elementAt(index);
//       return GestureDetector(
//         child: Container(
//           padding: EdgeInsets.symmetric(horizontal: 16),
//           child: Column(
//             children: [
//               Image.asset(
//                 tile.image,
//                 color: Colors.black,
//                 height: 34,
//                 width: 34,
//               ),
//               Padding(
//                 padding: const EdgeInsets.only(left: 0.0, top: 5),
//                 child: Text("${tile.name}".toUpperCase(),
//                     style: GoogleFonts.aBeeZee(
//                       fontSize: 10,
//                       color: Colors.black,
//                     )),
//               ),
//             ],
//           ),
//         ),
//       );
//     }, itemCount: _topMenuItems.length, scrollDirection: Axis.horizontal,),
//   );
//   //
//   // Widget createLeftSketch() {
//   //   return Align(
//   //     alignment: Alignment.bottomRight,
//   //     child: ListView.builder(
//   //         scrollDirection: Axis.vertical,
//   //         itemCount: allSketchList.length == null ? 0 : allSketchList.length,
//   //         itemBuilder: (BuildContext ctx, int position) {
//   //           if (selectSketch == position) {
//   //             allSketchList[position].IsCheck = true;
//   //           } else {
//   //             allSketchList[position].IsCheck = false;
//   //           }
//   //           return Padding(
//   //             padding: const EdgeInsets.all(4.0),
//   //             child: Row(
//   //               children: [
//   //                 GestureDetector(
//   //                   onTap: () {
//   //                     setState(() {
//   //                       if (allSketchList[position].IsCheck == false) {
//   //                         selectSketch = position;
//   //                         allSketchList[position].IsCheck = true;
//   //                         minHeight = double.infinity;
//   //                       } else {
//   //                         selectSketch = 100;
//   //                         allSketchList[position].IsCheck = false;
//   //                         minHeight = 90;
//   //                       }
//   //                     });
//   //                   },
//   //                   child: Container(
//   //                     width: MediaQuery.of(context).size.width * 0.19,
//   //                     child: Column(
//   //                       children: [
//   //                         Container(
//   //                             decoration: BoxDecoration(
//   //                                 borderRadius: BorderRadius.circular(5),
//   //                                 color: Colors.white,
//   //                                 border: Border.all(
//   //                                     width: 1, color: Color(0xffDCD9DC))),
//   //                             child: Image.network(
//   //                                 allSketchList[position].imgUrls)),
//   //                         Padding(
//   //                           padding: const EdgeInsets.all(4.0),
//   //                           child: Text(
//   //                             allSketchList[position].name,
//   //                             style: GoogleFonts.aBeeZee(
//   //                                 color: Colors.black, fontSize: 12),
//   //                           ),
//   //                         )
//   //                       ],
//   //                     ),
//   //                   ),
//   //                 ),
//   //                 Visibility(
//   //                   visible: allSketchList[position].IsCheck,
//   //                   child: Padding(
//   //                     padding: const EdgeInsets.only(bottom: 20.0, top: 0),
//   //                     child: Container(
//   //                         width: MediaQuery.of(context).size.width * 0.75,
//   //                         height: 90,
//   //                         child: createLeftSketch1()),
//   //                   ),
//   //                 )
//   //               ],
//   //             ),
//   //           );
//   //         }),
//   //   );
//   // }
//   //
//   // Widget createLeftSketch1() {
//   //   return ListView.builder(
//   //       scrollDirection: Axis.horizontal,
//   //       itemCount: allSketchList.length == null ? 0 : allSketchList.length,
//   //       itemBuilder: (BuildContext ctx, int position) {
//   //         return GestureDetector(
//   //           child: Padding(
//   //             padding: const EdgeInsets.only(left: 8.0, bottom: 0),
//   //             child: Container(
//   //               decoration: BoxDecoration(
//   //                   borderRadius: BorderRadius.circular(10),
//   //                   color: Colors.white,
//   //                   border: Border.all(width: 1.2, color: Color(0xffDCDCDC))),
//   //               width: 80,
//   //               child: Image.network(allSketchList[position].imgUrls),
//   //             ),
//   //           ),
//   //         );
//   //       });
//   // }
//   //
//   // Widget createBottomSketchName() {
//   //   return Container(
//   //       margin: EdgeInsets.only(bottom: 0),
//   //       height: double.infinity,
//   //       child: Container(
//   //         child: ListView.builder(
//   //           scrollDirection: Axis.horizontal,
//   //           itemCount: bottomSketchNameList.length == null
//   //               ? 0
//   //               : bottomSketchNameList.length,
//   //           itemBuilder: (BuildContext ctx, int position) {
//   //             if (rowIndex == position) {
//   //               bottomSketchNameList[position].IsVisiblity = true;
//   //             } else {
//   //               bottomSketchNameList[position].IsVisiblity = false;
//   //             }
//   //             return Padding(
//   //               padding: const EdgeInsets.only(top: 8.0, left: 18, right: 8),
//   //               child: GestureDetector(
//   //                 onTap: () {
//   //                   setState(() {
//   //                     rowIndex = position;
//   //                     bottomSketchNameList[position].IsVisiblity = true;
//   //                   });
//   //                 },
//   //                 child: Container(
//   //                   height: 55,
//   //                   child: Column(
//   //                     children: [
//   //                       Text(bottomSketchNameList[position].SketchName,
//   //                           style: GoogleFonts.aBeeZee(
//   //                               color: Colors.black, fontSize: 12)),
//   //                       Visibility(
//   //                         visible: bottomSketchNameList[position].IsVisiblity,
//   //                         child: Text("_ _ _ _",
//   //                             style: GoogleFonts.aBeeZee(
//   //                                 color: Colors.black,
//   //                                 fontSize: 12.5,
//   //                                 fontWeight: FontWeight.bold)),
//   //                       ),
//   //                     ],
//   //                   ),
//   //                 ),
//   //               ),
//   //             );
//   //           },
//   //         ),
//   //       ));
//   // }
//   //
//   // Widget createOptionMenuUI() {
//   //   return Container(
//   //     height: MediaQuery.of(context).size.height * 0.82,
//   //     child: Stack(
//   //       children: [
//   //         Container(
//   //             height: 85,
//   //             width: double.infinity,
//   //             color: Colors.white,
//   //             child: Row(
//   //               children: [
//   //                 Padding(
//   //                   padding: const EdgeInsets.only(top: 8.0, left: 25),
//   //                   child: Column(
//   //                     children: [
//   //                       Image.asset(
//   //                         "drawable/addfile.png",
//   //                         color: Colors.black,
//   //                         height: 34,
//   //                         width: 34,
//   //                       ),
//   //                       Padding(
//   //                         padding: const EdgeInsets.only(left: 0.0, top: 5),
//   //                         child: Text("design".toUpperCase(),
//   //                             style: GoogleFonts.aBeeZee(
//   //                               fontSize: 10,
//   //                               color: Colors.black,
//   //                             )),
//   //                       ),
//   //                     ],
//   //                   ),
//   //                 ),
//   //                 GestureDetector(
//   //                   onTap: () {
//   //                     SpecsAlertDialogUI();
//   //                   },
//   //                   child: Padding(
//   //                     padding: const EdgeInsets.only(top: 8.0, left: 25),
//   //                     child: Column(
//   //                       children: [
//   //                         Icon(
//   //                           Icons.edit,
//   //                           color: Colors.black,
//   //                           size: 34,
//   //                         ),
//   //                         Padding(
//   //                           padding: const EdgeInsets.only(left: 0.0, top: 5),
//   //                           child: Text("Specs".toUpperCase(),
//   //                               style: GoogleFonts.aBeeZee(fontSize: 10)),
//   //                         ),
//   //                       ],
//   //                     ),
//   //                   ),
//   //                 ),
//   //                 GestureDetector(
//   //                   onTap: () {
//   //                     easeAlertDialogUI();
//   //                   },
//   //                   child: Padding(
//   //                     padding: const EdgeInsets.only(top: 8.0, left: 25),
//   //                     child: Column(
//   //                       children: [
//   //                         Image.asset(
//   //                           "drawable/dry-clean.png",
//   //                           height: 33.5,
//   //                           width: 33.5,
//   //                           color: Colors.black,
//   //                         ),
//   //                         Padding(
//   //                           padding: const EdgeInsets.only(left: 0.0, top: 5),
//   //                           child: Text("Ease".toUpperCase(),
//   //                               style: GoogleFonts.aBeeZee(fontSize: 10)),
//   //                         ),
//   //                       ],
//   //                     ),
//   //                   ),
//   //                 ),
//   //                 Padding(
//   //                   padding: const EdgeInsets.only(top: 8.0, left: 25),
//   //                   child: Column(
//   //                     children: [
//   //                       Image.asset(
//   //                         "drawable/book.png",
//   //                         height: 36,
//   //                         width: 36,
//   //                         color: Colors.black,
//   //                       ),
//   //                       Padding(
//   //                         padding: const EdgeInsets.only(left: 0.0, top: 5),
//   //                         child: Text("Sawing".toUpperCase(),
//   //                             style: GoogleFonts.aBeeZee(fontSize: 10)),
//   //                       ),
//   //                     ],
//   //                   ),
//   //                 ),
//   //                 Padding(
//   //                   padding: const EdgeInsets.only(top: 8.0, left: 25),
//   //                   child: Column(
//   //                     children: [
//   //                       Image.asset(
//   //                         "drawable/roll.png",
//   //                         height: 36,
//   //                         width: 36,
//   //                         color: Colors.black,
//   //                       ),
//   //                       Padding(
//   //                         padding: const EdgeInsets.only(left: 0.0, top: 5),
//   //                         child: Text("Yardage".toUpperCase(),
//   //                             style: GoogleFonts.aBeeZee(fontSize: 10)),
//   //                       ),
//   //                     ],
//   //                   ),
//   //                 ),
//   //               ],
//   //             )),
//   //         Padding(
//   //           padding: const EdgeInsets.only(left: 78.0, top: 90),
//   //           child: Container(
//   //             color: Colors.white70,
//   //             height: double.infinity,
//   //             child: InteractiveViewer(
//   //               maxScale: 100.2,
//   //               minScale: 0.2,
//   //               child: Image.network(
//   //                 "https://previews.123rf.com/images/smyrna35/smyrna352002/smyrna35200200162/141610516-knot-t-shirt-women-s-top-fashion-flat-sketch-template.jpg",
//   //               ),
//   //             ),
//   //           ),
//   //         ),
//   //         Padding(
//   //           padding: const EdgeInsets.only(top: 90.0),
//   //           child: Container(width: minHeight, child: createLeftSketch()),
//   //         ),
//   //       ],
//   //     ),
//   //   );
//   // }
//
//   SpecsAlertDialogUI() {
//     showGeneralDialog(
//         context: context,
//         barrierDismissible: true,
//         barrierLabel:
//             MaterialLocalizations.of(context).modalBarrierDismissLabel,
//         pageBuilder: (BuildContext buildContext, Animation animation,
//             Animation secondaryAnimation) {
//           return Padding(
//             padding: const EdgeInsets.all(8.0),
//             child: Center(
//               child: Material(
//                 child: Container(
//                   color: Colors.white,
//                   child: Padding(
//                     padding: const EdgeInsets.all(0.0),
//                     child: Column(
//                       mainAxisSize: MainAxisSize.min,
//                       children: [
//                         Container(
//                           width: double.infinity,
//                           height: 45,
//                           color: Colors.white,
//                           child: Center(
//                               child: Text(
//                             "Specs".toUpperCase(),
//                             style: GoogleFonts.aBeeZee(
//                                 fontSize: 16, color: Colors.black),
//                           )),
//                         ),
//                         Column(
//                           mainAxisSize: MainAxisSize.min,
//                           mainAxisAlignment: MainAxisAlignment.center,
//                           children: [
//                             Padding(
//                               padding: const EdgeInsets.all(8.0),
//                               child: Row(
//                                 children: [
//                                   Padding(
//                                     padding: const EdgeInsets.all(8.0),
//                                     child: Text(
//                                       "Sewing Specs".toUpperCase(),
//                                       style: GoogleFonts.aBeeZee(
//                                           fontWeight: FontWeight.bold,
//                                           fontSize: 12,
//                                           color: Colors.black),
//                                     ),
//                                   ),
//                                   Padding(
//                                     padding: const EdgeInsets.all(8.0),
//                                     child: Text(
//                                       "FitModel Specs".toUpperCase(),
//                                       style: GoogleFonts.aBeeZee(
//                                           fontWeight: FontWeight.bold,
//                                           fontSize: 12,
//                                           color: Colors.black),
//                                     ),
//                                   ),
//                                 ],
//                               ),
//                             ),
//                             Padding(
//                               padding: const EdgeInsets.all(8.0),
//                               child: Row(
//                                 children: [
//                                   Text(
//                                     "Point Of Measure".toUpperCase(),
//                                     style: GoogleFonts.aBeeZee(
//                                         color: Colors.black87,
//                                         fontSize: 12.4,
//                                         fontWeight: FontWeight.bold),
//                                   ),
//                                   Spacer(),
//                                   Text(
//                                     "Measure".toUpperCase(),
//                                     style: GoogleFonts.aBeeZee(
//                                         color: Colors.black87,
//                                         fontSize: 12.4,
//                                         fontWeight: FontWeight.bold),
//                                   ),
//                                 ],
//                               ),
//                             ),
//                             Container(
//                               height: 1,
//                               width: double.infinity,
//                               color: Colors.grey[200],
//                             ),
//                             Padding(
//                               padding: const EdgeInsets.all(8.0),
//                               child: Row(
//                                 children: [
//                                   Text(
//                                     "Front length from HPS to waist",
//                                     style: GoogleFonts.aBeeZee(
//                                         color: Colors.grey[700],
//                                         fontSize: 12.4,
//                                         fontWeight: FontWeight.bold),
//                                   ),
//                                   Spacer(),
//                                   Text(
//                                     "51.99 cm".toUpperCase(),
//                                     style: GoogleFonts.aBeeZee(
//                                         color: Colors.grey[700],
//                                         fontSize: 12.4,
//                                         fontWeight: FontWeight.bold),
//                                   ),
//                                 ],
//                               ),
//                             ),
//                             Padding(
//                               padding: const EdgeInsets.all(8.0),
//                               child: Row(
//                                 children: [
//                                   Text(
//                                     "Across chest",
//                                     style: GoogleFonts.aBeeZee(
//                                         color: Colors.grey[700],
//                                         fontSize: 12.4,
//                                         fontWeight: FontWeight.bold),
//                                   ),
//                                   Spacer(),
//                                   Text(
//                                     "54.41 cm".toUpperCase(),
//                                     style: GoogleFonts.aBeeZee(
//                                         color: Colors.grey[700],
//                                         fontSize: 12.4,
//                                         fontWeight: FontWeight.bold),
//                                   ),
//                                 ],
//                               ),
//                             ),
//                             Padding(
//                               padding: const EdgeInsets.all(8.0),
//                               child: Row(
//                                 children: [
//                                   Text(
//                                     "Center front length to waist",
//                                     style: GoogleFonts.aBeeZee(
//                                         color: Colors.grey[700],
//                                         fontSize: 12.4,
//                                         fontWeight: FontWeight.bold),
//                                   ),
//                                   Spacer(),
//                                   Text(
//                                     "16.8 cm".toUpperCase(),
//                                     style: GoogleFonts.aBeeZee(
//                                         color: Colors.grey[700],
//                                         fontSize: 12.4,
//                                         fontWeight: FontWeight.bold),
//                                   ),
//                                 ],
//                               ),
//                             ),
//                             Container(
//                               height: 60,
//                               child: Padding(
//                                 padding: const EdgeInsets.all(8.0),
//                                 child: Row(
//                                   children: [
//                                     Icon(
//                                       Icons.print,
//                                       size: 30,
//                                       color: Colors.grey,
//                                     ),
//                                     Padding(
//                                       padding:
//                                           const EdgeInsets.only(left: 18.0),
//                                       child: Image.asset(
//                                         "drawable/document.png",
//                                         height: 24,
//                                         color: Colors.black54,
//                                       ),
//                                     ),
//                                     Spacer(),
//                                     Text(
//                                       "Inch".toUpperCase(),
//                                       style: GoogleFonts.aBeeZee(
//                                           color: Colors.black,
//                                           fontWeight: FontWeight.w800,
//                                           fontSize: 12),
//                                     ),
//                                     Padding(padding: EdgeInsets.only(left: 4)),
//                                     Padding(
//                                       padding:
//                                           const EdgeInsets.only(right: 8.0),
//                                       child: Text(
//                                         "Cm".toUpperCase(),
//                                         style: GoogleFonts.aBeeZee(
//                                             color: Colors.black,
//                                             fontWeight: FontWeight.w800,
//                                             fontSize: 12),
//                                       ),
//                                     ),
//                                   ],
//                                 ),
//                               ),
//                             )
//                           ],
//                         ),
//                       ],
//                     ),
//                   ),
//                 ),
//               ),
//             ),
//           );
//         });
//   }
//
//   easeAlertDialogUI() {
//     showGeneralDialog(
//         context: context,
//         barrierDismissible: true,
//         barrierLabel:
//             MaterialLocalizations.of(context).modalBarrierDismissLabel,
//         pageBuilder: (BuildContext buildContext, Animation animation,
//             Animation secondaryAnimation) {
//           return Padding(
//             padding: const EdgeInsets.all(8.0),
//             child: Center(
//               child: Material(
//                 child: Container(
//                   decoration: new BoxDecoration(
//                     color: Colors.white,
//                     borderRadius: BorderRadius.circular(18),
//                   ),
//                   child: Padding(
//                     padding: const EdgeInsets.all(0.0),
//                     child: Column(
//                       mainAxisSize: MainAxisSize.min,
//                       children: [
//                         Container(
//                           width: double.infinity,
//                           height: 45,
//                           color: Colors.white,
//                           child: Center(
//                               child: Text(
//                             "Ease".toUpperCase(),
//                             style: GoogleFonts.aBeeZee(
//                                 fontSize: 16, color: Colors.black),
//                           )),
//                         ),
//                         Column(
//                           mainAxisSize: MainAxisSize.min,
//                           mainAxisAlignment: MainAxisAlignment.center,
//                           children: [
//                             Padding(
//                               padding: const EdgeInsets.all(8.0),
//                               child: Row(
//                                 children: [
//                                   Text(
//                                     "Point Of Measure".toUpperCase(),
//                                     style: GoogleFonts.aBeeZee(
//                                         color: Colors.black87,
//                                         fontSize: 12.4,
//                                         fontWeight: FontWeight.bold),
//                                   ),
//                                   Spacer(),
//                                   Text(
//                                     "Ease".toUpperCase(),
//                                     style: GoogleFonts.aBeeZee(
//                                         color: Colors.black87,
//                                         fontSize: 12.4,
//                                         fontWeight: FontWeight.bold),
//                                   ),
//                                   Padding(padding: EdgeInsets.only(left: 25)),
//                                   Text(
//                                     "PATTERN".toUpperCase(),
//                                     style: GoogleFonts.aBeeZee(
//                                         color: Colors.black87,
//                                         fontSize: 12.4,
//                                         fontWeight: FontWeight.bold),
//                                   ),
//                                 ],
//                               ),
//                             ),
//                             Container(
//                               height: 1,
//                               width: double.infinity,
//                               color: Colors.grey[200],
//                             ),
//                             Padding(
//                               padding: const EdgeInsets.all(8.0),
//                               child: Row(
//                                 children: [
//                                   Text(
//                                     "Paints length",
//                                     style: GoogleFonts.aBeeZee(
//                                         color: Colors.grey[700],
//                                         fontSize: 12.4,
//                                         fontWeight: FontWeight.bold),
//                                   ),
//                                   Spacer(),
//                                   Container(
//                                     margin: EdgeInsets.only(right: 20),
//                                     width: 90,
//                                     height: 40,
//                                     decoration: new BoxDecoration(
//                                         borderRadius: BorderRadius.circular(8),
//                                         border: Border.all(
//                                             width: 1, color: Colors.grey)),
//                                     child: TextFormField(
//                                       textAlign: TextAlign.end,
//                                       decoration: new InputDecoration(
//                                         contentPadding: EdgeInsets.only(
//                                             right: 5, bottom: 10),
//                                         hintText: "0 cm",
//                                         hintStyle: GoogleFonts.aBeeZee(
//                                           color: Colors.black,
//                                         ),
//                                         border: InputBorder.none,
//                                         // prefixIcon: Icon(Icons.lock_open_outlined,color: Color(0xffffffff),),
//                                       ),
//                                     ),
//                                   ),
//                                   Text(
//                                     "51.99 cm",
//                                     style: GoogleFonts.aBeeZee(
//                                         color: Colors.grey[700],
//                                         fontSize: 12.4,
//                                         fontWeight: FontWeight.bold),
//                                   ),
//                                 ],
//                               ),
//                             ),
//                             Padding(
//                               padding: const EdgeInsets.all(8.0),
//                               child: Row(
//                                 children: [
//                                   Text(
//                                     "Bottom opening",
//                                     style: GoogleFonts.aBeeZee(
//                                         color: Colors.grey[700],
//                                         fontSize: 12.4,
//                                         fontWeight: FontWeight.bold),
//                                   ),
//                                   Spacer(),
//                                   Container(
//                                     margin: EdgeInsets.only(right: 20),
//                                     width: 90,
//                                     height: 40,
//                                     decoration: new BoxDecoration(
//                                         borderRadius: BorderRadius.circular(8),
//                                         border: Border.all(
//                                             width: 1, color: Colors.grey)),
//                                     child: TextFormField(
//                                       textAlign: TextAlign.end,
//                                       decoration: new InputDecoration(
//                                         contentPadding: EdgeInsets.only(
//                                             right: 5, bottom: 10),
//                                         hintText: "0 cm",
//                                         hintStyle: GoogleFonts.aBeeZee(
//                                           color: Colors.black,
//                                         ),
//                                         border: InputBorder.none,
//                                         // prefixIcon: Icon(Icons.lock_open_outlined,color: Color(0xffffffff),),
//                                       ),
//                                     ),
//                                   ),
//                                   Text(
//                                     "54.41 cm",
//                                     style: GoogleFonts.aBeeZee(
//                                         color: Colors.grey[700],
//                                         fontSize: 12.4,
//                                         fontWeight: FontWeight.bold),
//                                   ),
//                                 ],
//                               ),
//                             ),
//                             Padding(
//                               padding: const EdgeInsets.all(8.0),
//                               child: Row(
//                                 children: [
//                                   Text(
//                                     "Back rise",
//                                     style: GoogleFonts.aBeeZee(
//                                         color: Colors.grey[700],
//                                         fontSize: 12.4,
//                                         fontWeight: FontWeight.bold),
//                                   ),
//                                   Spacer(),
//                                   Container(
//                                     margin: EdgeInsets.only(right: 20),
//                                     width: 90,
//                                     height: 40,
//                                     decoration: new BoxDecoration(
//                                         borderRadius: BorderRadius.circular(8),
//                                         border: Border.all(
//                                             width: 1, color: Colors.grey)),
//                                     child: TextFormField(
//                                       textAlign: TextAlign.end,
//                                       decoration: new InputDecoration(
//                                         contentPadding: EdgeInsets.only(
//                                             right: 5, bottom: 10),
//                                         hintText: "0 cm",
//                                         hintStyle: GoogleFonts.aBeeZee(
//                                           color: Colors.black,
//                                         ),
//                                         border: InputBorder.none,
//                                         // prefixIcon: Icon(Icons.lock_open_outlined,color: Color(0xffffffff),),
//                                       ),
//                                     ),
//                                   ),
//                                   Text(
//                                     "16.8 cm",
//                                     style: GoogleFonts.aBeeZee(
//                                         color: Colors.grey[700],
//                                         fontSize: 12.4,
//                                         fontWeight: FontWeight.bold),
//                                   ),
//                                 ],
//                               ),
//                             ),
//                             Container(
//                               height: 60,
//                               child: Padding(
//                                 padding: const EdgeInsets.all(8.0),
//                                 child: Row(
//                                   children: [
//                                     Padding(
//                                       padding: const EdgeInsets.only(left: 5.0),
//                                       child: Image.asset(
//                                         "drawable/document.png",
//                                         height: 24,
//                                         color: Colors.black54,
//                                       ),
//                                     ),
//                                     Padding(
//                                       padding: const EdgeInsets.only(left: 8.0),
//                                       child: Text(
//                                         "Download Our Stretch Calculator"
//                                             .toUpperCase(),
//                                         style: GoogleFonts.aBeeZee(
//                                             color: Colors.black,
//                                             fontWeight: FontWeight.w800,
//                                             fontSize: 12),
//                                       ),
//                                     ),
//                                     Spacer(),
//                                     Text(
//                                       "Reset".toUpperCase(),
//                                       style: GoogleFonts.aBeeZee(
//                                           color: Colors.black,
//                                           fontWeight: FontWeight.w800,
//                                           fontSize: 12),
//                                     ),
//                                     Padding(padding: EdgeInsets.only(left: 4)),
//                                   ],
//                                 ),
//                               ),
//                             ),
//                             Container(
//                               height: 60,
//                               child: Row(
//                                 children: [
//                                   Spacer(),
//                                   Container(
//                                     height: 45,
//                                     width: 110,
//                                     decoration: new BoxDecoration(
//                                       color: Colors.black,
//                                       borderRadius: BorderRadius.circular(8),
//                                     ),
//                                     child: Center(
//                                       child: Text(
//                                         'Apply',
//                                         style: GoogleFonts.aBeeZee(
//                                             color: Colors.white),
//                                       ),
//                                     ),
//                                   ),
//                                   Spacer(),
//                                   Text(
//                                     "Inch".toUpperCase(),
//                                     style: GoogleFonts.aBeeZee(
//                                         color: Colors.black,
//                                         fontWeight: FontWeight.w800,
//                                         fontSize: 12),
//                                   ),
//                                   Padding(padding: EdgeInsets.only(left: 4)),
//                                   Padding(
//                                     padding: const EdgeInsets.only(right: 8.0),
//                                     child: Text(
//                                       "Cm".toUpperCase(),
//                                       style: GoogleFonts.aBeeZee(
//                                           color: Colors.black,
//                                           fontWeight: FontWeight.w800,
//                                           fontSize: 12),
//                                     ),
//                                   ),
//                                 ],
//                               ),
//                             )
//                           ],
//                         ),
//                       ],
//                     ),
//                   ),
//                 ),
//               ),
//             ),
//           );
//         });
//   }
//
//   Color _hexToColor(String hex) {
//     assert(RegExp(r'^#([0-9a-fA-F]{6})|([0-9a-fA-F]{8})$').hasMatch(hex),
//     'hex color must be #rrggbb or #rrggbbaa');
//
//     return Color(
//       int.parse(hex.substring(1), radix: 16) +
//           (hex.length == 7 ? 0xff000000 : 0x00000000),
//     );
//   }
//
//   Widget _maskImage(String pathDn, String pathUp){
//     return FutureBuilder(
//         future: _loadAssetImageSize(pathDn),
//         builder: (_, AsyncSnapshot<Size>snapshot){
//           if(!snapshot.hasData)return SizedBox();
//           Size size = snapshot.data!;
//           return Stack(children: [
//             SizedBox(
//               width: size.width/_fraction,
//               height: size.height/_fraction,
//               child: _color==null?MaskedImage(asset: _fabric!, mask: pathDn)
//               :_maskColor(pathUp: pathUp, pathDn: pathDn),
//               // child: Image.asset(pathDn, scale: _scale, color: Colors.red,),
//             ),
//             Image.network(pathUp, scale: _fraction,),
//           ],);
//         });
//   }
//
//   Widget _maskColor({required pathUp, required pathDn}){
//
//     return Stack(children: [
//       Image.network(pathDn, color: _hexToColor(_color!),),
//       Image.network(pathUp),
//     ],);
//   }
//
//   Future<Size> _loadAssetImageSize(String asset) async{
//     Uint8List data = await loadImageByteData(asset);
//     Codec codec = await instantiateImageCodec(data);
//     FrameInfo fi = await codec.getNextFrame();
//     return Size(fi.image.width.toDouble(), fi.image.height.toDouble());
//   }
//
// }
//
// class ItemSelector extends StatelessWidget {
//   final String image;
//   final String name;
//   final Function()? onPress;
//   const ItemSelector({Key? key, required this.image, required this.name, this.onPress}) : super(key: key);
//
//   @override
//   Widget build(BuildContext context) {
//     return GestureDetector(
//       child: Container(
//         padding: EdgeInsets.symmetric(vertical: 4,horizontal: 4),
//         margin: EdgeInsets.symmetric(vertical: 4,horizontal: 4),
//         child: Column(children: [
//           Image.network(image, height: 56,),
//           Expanded(child: Center(child: Text(name, textAlign: TextAlign.center,),))
//         ],),
//         decoration: BoxDecoration(
//           color: Colors.white,
//             border: Border.all(color: Colors.black26, width: 1),
//             borderRadius: BorderRadius.circular(8)
//         ),
//       ),
//       onTap: onPress,
//     );
//   }
// }
//
// class PositionItem{
//   final String image;
//   final double left;
//   final double top;
//
//   PositionItem({required this.image, required this.left, required this.top});
//
//
// }
//
// class ColorSelector extends StatelessWidget {
//   final String color;
//   final Function()? onPress;
//   const ColorSelector({Key? key, required this.color, this.onPress}) : super(key: key);
//
//   @override
//   Widget build(BuildContext context) {
//     return GestureDetector(
//       child: Container(
//         margin: EdgeInsets.all(4),
//         width: 64,
//         height: 64,
//         decoration: BoxDecoration(
//             color: _hexToColor(color),
//             borderRadius: BorderRadius.circular(8)
//             // borderRadius: BorderRadius.circular(38)
//         ),
//       ),
//       onTap: onPress,
//     );
//   }
//
//   Color _hexToColor(String hex) {
//     assert(RegExp(r'^#([0-9a-fA-F]{6})|([0-9a-fA-F]{8})$').hasMatch(hex),
//     'hex color must be #rrggbb or #rrggbbaa');
//
//     return Color(
//       int.parse(hex.substring(1), radix: 16) +
//           (hex.length == 7 ? 0xff000000 : 0x00000000),
//     );
//   }
//
// }
//
// class MaskedImage extends StatelessWidget {
//   final String asset;
//   final String mask;
//
//   MaskedImage({required this.asset, required this.mask});
//
//   @override
//   Widget build(BuildContext context) {
//     return LayoutBuilder(builder: (context, constraints) {
//       return FutureBuilder<List>(
//         future: _createShaderAndImage(asset, mask, constraints.maxWidth, constraints.maxHeight),
//         builder: (context, snapshot) {
//           if (!snapshot.hasData) return const SizedBox.shrink();
//           return ShaderMask(
//             blendMode: BlendMode.dstATop,
//             shaderCallback: (rect) => snapshot.data![0],
//             child: snapshot.data![1],
//           );
//         },
//       );
//     });
//   }
//
//   Future<List> _createShaderAndImage(String asset, String mask, double w, double h) async {
//     return _createNetworkShaderAndImage(asset, mask, w, h);
//     /**
//     ByteData data = await rootBundle.load(asset);
//     ByteData maskData = await rootBundle.load(mask);
//
//
//     Codec codec = await instantiateImageCodec(maskData.buffer.asUint8List(), targetWidth: w.toInt(), targetHeight: h.toInt());
//     FrameInfo fi = await codec.getNextFrame();
//
//     ImageShader shader = ImageShader(fi.image, TileMode.clamp, TileMode.clamp, Matrix4.identity().storage);
//     Image image = Image.memory(data.buffer.asUint8List(), fit: BoxFit.cover, width: w, height: h);
//     return [shader, image];
//         **/
//   }
//
//   Future<List> _createNetworkShaderAndImage(String asset, String mask, double w, double h) async {
//     // ByteData data = await rootBundle.load(asset);
//     // ByteData maskData = await rootBundle.load(mask);
//
//     Uint8List data = await loadImageByteData(asset);
//     Uint8List maskData = await loadImageByteData(mask);
//
//     Codec codec = await instantiateImageCodec(maskData, targetWidth: w.toInt(), targetHeight: h.toInt());
//     FrameInfo fi = await codec.getNextFrame();
//
//     ImageShader shader = ImageShader(fi.image, TileMode.clamp, TileMode.clamp, Matrix4.identity().storage);
//     Image image = Image.memory(data, fit: BoxFit.cover, width: w, height: h);
//     return [shader, image];
//   }
// }
//
// class SavedItem{
//   final int index;
//   final String? fabric;
//   final String? color;
//   final int key;
//   final SubAttributeData data;
//   // final Map<int, SubAttributeData> map;
//
//   SavedItem({required this.index, this.fabric, this.color, required this.key, required this.data, });
// }