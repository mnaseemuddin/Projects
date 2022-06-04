
import 'dart:async';
import 'dart:io';
import 'dart:typed_data';
import 'dart:ui';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:http/http.dart' as http;
import 'package:google_fonts/google_fonts.dart';
import 'package:path_provider/path_provider.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:provider/provider.dart';
import 'package:screenshot/screenshot.dart';
import 'package:tailor/CommonUI/no_internet_connection.dart';
import 'package:tailor/Screens/DashBoardActivity.dart';
import 'package:tailor/Screens/FinalSketchViewActivity.dart';
import 'package:tailor/ApiRepositary.dart';
import 'package:tailor/Model/AllSketch.dart';
import 'package:tailor/Model/BottomSketch.dart';
import 'package:tailor/Model/model.dart';
import 'package:tailor/Support/ProgressDialogManagers.dart';
import 'package:tailor/Support/SharedPreferencesManager.dart';
import 'package:tailor/Support/UISupport.dart';
import 'package:tailor/Support/connectivity_provider.dart';

class SketchDress extends StatefulWidget {
  final String image;
  final int categoryId;
  final String categoryName;

  const SketchDress(
      {Key? key,
      required this.categoryId,
      required this.image,
      required this.categoryName})
      : super(key: key);

  @override
  _SketchDressState createState() => _SketchDressState();
}

class _SketchDressState extends State<SketchDress> {
  ScreenshotController _screenshotController = ScreenshotController();

  late List<AllSketch> allSketchList = [];

  double minHeight = 90;
  int rowIndex = 0;
  int _showIndex = -1;
  double _fraction = 1.8;
  String?
      _fabric; // = 'http://3.108.177.150:8000/files/upload/texture_color/red_texture.png';
  String? _color;
  AttributeColor? _attributeColor;
  SATexture? _saTexture;
  List<FinalSavedItem> finalSavedList = [];
  List<String> selectedAttributeList = [];
  List<ImageTile> _topMenuItems = [
    ImageTile(id: 0, image: 'drawable/addfile.png', name: 'design'),
    ImageTile(id: 1, image: 'drawable/addfile.png', name: 'Specs'),
    ImageTile(id: 2, image: 'drawable/dry-clean.png', name: 'Ease'),
   // ImageTile(id: 4, image: 'assets/saved.png', name: 'Save'),
    ImageTile(id: 5, image: 'assets/file.png', name: "Download")
    // ImageTile(id: 3, image: 'drawable/book.png', name: 'Sawing'),
   // ImageTile(id: 3, image: 'drawable/roll.png', name: 'Yardage'),
  ];

  CategoryWiseAttribute? _categoryAttribute;
  List<IndexImage> _indexedImage = [];

  Map<int, SubAttributeData> _map = {};

  List<SavedItem> _forward = [];
  List<SavedItem> _backward = [];
  List<String> yardageList = ["88 cm", "108 cm", "110 cm", "112 cm", "115 cm"];
  late Map<String, String> kyValList;
  Directory? _directory;
  String? usermesurment;
  String? yardage;
  String selectedimgUrl = '', selectedName = '';

  File? file;

  Uint8List? image;

  @override
  void initState() {
    super.initState();
    Provider.of<ConnectivityProvider>(context,listen: false).startMonitoring();
    categoryAttributeAPI(widget.categoryId).then((value) {
      setState(() {
        if (value.status) {
          _categoryAttribute = value.data;
          _categoryAttribute!.data.asMap().forEach((index, element) {
            SubAttributeData e =
                element.subAttributeData.first; //.elementAt(1);
            _map[element.zIndex] = e;
            _indexedImage.add(IndexImage(
                id: e.tblSubAttributeId,
                zindex: element.zIndex,
                top: e.top,
                left: e.left,
                width: e.width,
                height: e.height,
                image: e.image));
          });

          Attribute _attribute = _categoryAttribute!.data.first;
          SubAttributeData subAttr = _attribute.subAttributeData.first;
          _indexedImage.add(IndexImage(
              id: _attribute.tblAttributeId,
              zindex: 0,
              top: 504 / 2,
              left: 125 + 221 / 2,
              width: 221,
              height: 504,
              image: _attribute.image));
          _map[0] = SubAttributeData(
              tblSubAttributeId: _attribute.tblAttributeId,
              attributeId: subAttr.attributeId,
              top: 504 / 2,
              left: 125 + 221 / 2,
              width: 221,
              height: 504,
              subAttributeName: subAttr.subAttributeName,
              image: _attribute.image);
          _indexedImage.sort((a, b) => a.zindex > b.zindex ? 1 : -1);
        }
      });
    });
    SharePreferenceManager.instance.getEaseData("Ease").then((value) {
      setState(() {
        usermesurment = value;
      });
    });
    requestPermissions();
  }

  Future<ApiResponse> getJson() async {
    String str = await rootBundle.loadString('drawable/aacheck.json');
    CategoryWiseAttribute model = categoryWiseAttributeFromJson(str);
    return ApiResponse(status: true, message: data["message"], data: model);
  }

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    return Consumer<ConnectivityProvider>(builder: (cCtx,net,child){
      if(net.isOnline!=null){
        return net.isOnline?WillPopScope(
          onWillPop: () {
            showDialog(
              context: context,
              builder: (BuildContext context) {
                // return object of type Dialog
                return AlertDialog(
                  content: Text(
                      "Are you sure you want to delete all the elements and start the design again?"),
                  actions: <Widget>[
                    TextButton(
                      child: new Text("Cancel"),
                      onPressed: () {
                        Navigator.of(context).pop();
                      },
                    ),
                    TextButton(
                      child: new Text("OK"),
                      onPressed: () {
                        SharePreferenceManager.instance
                            .IsSharePreferenceEaseDataRemove();
                        SharePreferenceManager.instance
                            .IsSharePreferenceYardageDataRemove();
                        SharePreferenceManager.instance
                            .IsSharePreferenceFabricDropDown();
                        SharePreferenceManager.instance
                            .IsSharePreferenceFabricWidthDropDown();
                        SharePreferenceManager.instance
                            .IsSharePreferenceFitDropDown();
                        SharePreferenceManager.instance
                            .IsSharePreferenceInchCmDropDown();
                        SharePreferenceManager.instance
                            .IsSharePreferenceMainFabricCm();
                        SharePreferenceManager.instance
                            .IsSharePreferenceMainFabricInch();
                        SharePreferenceManager.instance
                            .IsSharePreferenceFusibleinterfacingCm();
                        SharePreferenceManager.instance
                            .IsSharePreferenceFusibleinterfacingInch();
                        SharePreferenceManager.instance.IsSharePreferenceFabricWidthInchDropDown();
                        Get.offAll(DashBoard());
                      },
                    ),
                    // usually buttons at the bottom of the dialog
                  ],
                );
              },
            );
            return Future.value(true);
          },
          child: Scaffold(
            appBar: AppBar(
              brightness: Brightness.dark,
              centerTitle: true,
              title: Row(
                mainAxisSize: MainAxisSize.min,
                children: [
                  IconButton(
                      onPressed: (){
                        if(_backward.length>0){
                          setState(() {
                            SavedItem item = _backward.elementAt(_backward.length-2);
                            if(item.data !=null){
                              _map[item.key] = item.data!;
                            }
                            _fabric = item.fabric;
                            _color = item.color;
                            _forward.add(_backward.last);
                            _backward.removeAt(_backward.length-1);
                          });
                        }
                      },
                      icon: Icon(Icons.undo)),
                  IconButton(
                      onPressed: () {
                        if(_forward.length>0){
                          setState(() {

                            SavedItem item = _forward.elementAt(_forward.length-1);
                            if(item.data !=null){
                              _map[item.key] = item.data!;
                            }
                            _fabric = item.fabric;
                            _color = item.color;
                            _backward.add(_forward.last);
                            _forward.removeAt(_forward.length-1);
                          });
                        }
                      },
                      icon: Icon(Icons.redo)),
                ],
              ),
              actions: [
                IconButton(
                    onPressed: () async {
                      final directory = (await getApplicationDocumentsDirectory())
                          .path; //from path_provide package
                      String fileName =
                          '${DateTime.now().microsecondsSinceEpoch.toString() + ".jpg"}';
                      String path = '$directory';
                      _screenshotController.captureAndSave(path, fileName: fileName,)
                          .then((value) {
                        SharePreferenceManager.instance
                            .getEaseData("Ease")
                            .then((value) {
                          setState(() {
                            usermesurment = value;
                            SharePreferenceManager.instance
                                .getYardageData("Yardage")
                                .then((value) {
                              yardage = value;
                              if (usermesurment!.isNotEmpty) {
                                if (_saTexture != null) {
                                  if (_attributeColor != null) {
                                    int tblAttributeTextureId =
                                        _saTexture!.tblAttributeTextureId;
                                    String? colorCode =
                                        _attributeColor!.colorCode;
                                    Get.to(FinalSketchViewActivty(
                                      imagePath: '$path/$fileName',
                                      map: _map,
                                      tblAttributeTextureId:
                                      tblAttributeTextureId,
                                      ColorCode: colorCode,
                                      categoryId: widget.categoryId.toString(),
                                      categoryName: widget.categoryName,
                                    ));
                                  } else {
                                    Fluttertoast.showToast(
                                        msg: "Please choose Color ");
                                  }
                                } else {
                                  Fluttertoast.showToast(
                                      msg: "Please choose Texture ");
                                }

                              } else {
                                Fluttertoast.showToast(
                                    msg: "Please fill all EASE. ");
                              }
                            });
                          });
                        });
                      });
                    },
                    icon: Icon(Icons.check))
              ],
              ///data/user/0/com.lgt.tailor/app_flutter
              backgroundColor: UISupport.APP_PRIMARY_COLOR,
              leading: Builder(
                builder: (context) => IconButton(
                  icon: Image.asset(
                    "drawable/close.png",
                    height: 16,
                    width: 20,
                    color: Colors.white,
                  ),
                  onPressed: () => showDialog(
                    context: context,
                    builder: (BuildContext context) {
                      // return object of type Dialog
                      return AlertDialog(
                        content: Text(
                            "Are you sure you want to delete all the elements and start the design again?"),
                        actions: <Widget>[
                          TextButton(
                            child: new Text("Cancel"),
                            onPressed: () {
                              Navigator.of(context).pop();
                            },
                          ),
                          TextButton(
                            child: new Text("OK"),
                            onPressed: () {
                              SharePreferenceManager.instance
                                  .IsSharePreferenceEaseDataRemove();
                              SharePreferenceManager.instance
                                  .IsSharePreferenceYardageDataRemove();
                              SharePreferenceManager.instance
                                  .IsSharePreferenceFabricDropDown();
                              SharePreferenceManager.instance
                                  .IsSharePreferenceFabricWidthDropDown();
                              SharePreferenceManager.instance
                                  .IsSharePreferenceFitDropDown();
                              SharePreferenceManager.instance
                                  .IsSharePreferenceInchCmDropDown();
                              SharePreferenceManager.instance
                                  .IsSharePreferenceMainFabricCm();
                              SharePreferenceManager.instance
                                  .IsSharePreferenceMainFabricInch();
                              SharePreferenceManager.instance
                                  .IsSharePreferenceFusibleinterfacingCm();
                              SharePreferenceManager.instance
                                  .IsSharePreferenceFusibleinterfacingInch();
                              SharePreferenceManager.instance.IsSharePreferenceFabricWidthInchDropDown();
                              Get.offAll(DashBoard());
                            },
                          ),
                          // usually buttons at the bottom of the dialog
                        ],
                      );
                    },
                  ),
                ),
              ),
            ),
            backgroundColor: Colors.white,
            body: _categoryAttribute == null
                ? Center(
              child: CircularProgressIndicator(
                strokeWidth: 5.5,
                color: Colors.black,
              ),
            )
                : Builder(builder: (context) {
              _fraction =
                  (_categoryAttribute!.canvasWidth) / (2 * size.width / 2.5);
              return Stack(
                children: [
                  Screenshot(
                    child: _createImageUIShot(context),
                    controller: _screenshotController,
                  ),
                  Container(
                    color: Colors.white,
                  ),
                  Column(
                    children: [
                      Padding(
                        padding: EdgeInsets.symmetric(vertical: 16),
                        child: _topMenuUI(),
                      ),
                      Expanded(
                          child: Stack(
                            children: [
                              Row(
                                children: [
                                  Expanded(
                                    child: Container(),
                                    flex: 20,
                                  ),
                                  Expanded(
                                    child: Container(
                                      padding:
                                      EdgeInsets.symmetric(horizontal: 8),
                                      child: _createImageUI(),
                                    ),
                                    flex: 80,
                                  ),
                                ],
                              ),
                              _createAttributeList(),
                            ],
                          )),
                    ],
                  ),
                ],
              );
            }),
          ),
        ):InternetConnection();
      }
      return Container(
        child: Center(
          child: CircularProgressIndicator(),
        ),
      );
    });
  }

  Widget _createImageUIShot(context) {
    List<Widget> list = [];

    double heightCal = 0;
    double widthCal = 0;

    var sortedKeys = _map.keys.toList()..sort();
    for (int i = 0; i < sortedKeys.length; i++) {
      int key = sortedKeys.elementAt(i);
      SubAttributeData element = _map[key]!;

      double top = (element.top - element.height / 2) / _fraction;
      double left = (element.left - element.width / 2) / _fraction;
      double height = element.height / _fraction;
      double width = element.width / _fraction;

      if (top + height > heightCal) heightCal = top + height;
      if (left + width > widthCal) widthCal = left + width;

      list.add(Positioned(
          top: top,
          left: left,
          child: _fabric == null && _color == null
              ? Image.network(
                  element.image,
                  width: width,
                  height: height,
                )
              : _maskImage(
                  element.image.replaceAll('.png', '_dn.png'), element.image)));

      // list.add(Positioned(
      //         top: (element.top -element.height/2)/_fractionShot,
      //         left: (element.left - element.width/2)/_fractionShot,
      //         child: _fabric==null&&_color==null?Image.network(element.image,
      //           width: element.width/_fractionShot, height: element.height/_fractionShot,):
      //         _maskImage(element.image.replaceAll('.png', '_dn.png'), element.image))
      // );
    }
    // return Stack(children: list,);
    return SizedBox(
      width: widthCal + 20,
      height: heightCal + 20,
      child: Padding(
        padding: const EdgeInsets.fromLTRB(8, 15, 8, 4),
        child: Stack(
          children: list,
        ),
      ),
    );
  }

  Widget _createAttributeList() {
    List<Widget> list = [];
    // int mIndex = 0;
    _categoryAttribute!.data.asMap().forEach((index, attribute) {
      List<SubAttributeData> subAttribute = attribute.subAttributeData;
      list.add(Container(
        width: 70,
        height: 90,
        child: Row(
          children: [
            Expanded(
              child: ItemSelector(
                image: attribute.selectedImage == "null"
                    ? attribute.subAttributeData.first.image
                    : attribute.selectedImage,
                name: attribute.selectedName == "null"
                    ? attribute.attributeName
                    : attribute.selectedName,
                onPress: () {
                  setState(() {
                    if (index == _showIndex)
                      _showIndex = -1;
                    else
                      _showIndex = index;
                  });
                },
              ),
              flex: 20,
            ),
            Expanded(
              child: _showIndex == index
                  ? Container(
                      child: ListView.builder(
                        itemBuilder: (context, cIndex) {
                          SubAttributeData subAttributeData =
                              subAttribute.elementAt(cIndex);
                          return ItemSelector(
                            image: subAttributeData.image,
                            name: subAttributeData.subAttributeName,
                            onPress: () {
                              setState(() {
                                _map[attribute.zIndex] = subAttributeData;
                                attribute.selectedImage =
                                    subAttributeData.image;
                                attribute.selectedName =
                                    subAttributeData.subAttributeName;
                                Map<String, dynamic> map =
                                    subAttributeData.toJson();
                                _backward.add(SavedItem(
                                    index: DateTime.now().millisecond,
                                    fabric: _fabric,
                                    color: _color,
                                    key: attribute.zIndex,
                                    data: SubAttributeData.fromJson(map)));
                                _showIndex = -1;
                              });
                              //print(attribute.zIndex);
                            },
                          );
                        },
                        itemCount: subAttribute.length,
                        scrollDirection: Axis.horizontal,
                      ),
                    )
                  : Container(),
              flex: 80,
            ),
          ],
        ),
      ));
      // mIndex +=1;
    });

    // Add Textur
    int textureIndex = _categoryAttribute!.data.length + 1;

    SATexture value = _categoryAttribute!.texture.first;
    list.add(Container(
      width: 70,
      height: 90,
      child: Row(
        children: [
          Expanded(
            child: ItemSelector(
                image: value.selectedtextureImage == "null"
                    ? 'https://cdn-icons-png.flaticon.com/512/222/222239.png'
                    : value.selectedtextureImage,
                name: value.selectedtextureName == "null"
                    ? 'Texture'
                    : value.selectedtextureName,
                onPress: () {
                  setState(() {
                    if (textureIndex == _showIndex)
                      _showIndex = -1;
                    else
                      _showIndex = textureIndex;
                  });
                }),
            flex: 20,
          ),
          Expanded(
            child: _showIndex == textureIndex
                ? Container(
                    child: ListView.builder(
                      itemBuilder: (context, cIndex) {
                        SATexture sTexture =
                            _categoryAttribute!.texture.elementAt(cIndex);
                        return ItemSelector(
                          image: sTexture.textureImage,
                          name: sTexture.textureName,
                          onPress: () {
                            setState(() {
                              _fabric = sTexture.textureImage;
                              _saTexture = sTexture;
                              value.selectedtextureImage =
                                  sTexture.textureImage;
                              value.selectedtextureName = sTexture.textureName;
                              _backward.add(SavedItem(
                                  index: DateTime.now().millisecond,
                                  fabric: sTexture.textureImage,
                                  color: null,
                                  key: -1,
                                  data: null));
                              _showIndex = -1;
                            });
                            // print(attribute.zIndex);
                          },
                        );
                      },
                      itemCount: _categoryAttribute!.texture.length,
                      scrollDirection: Axis.horizontal,
                    ),
                  )
                : Container(),
            flex: 80,
          )
        ],
      ),
    ));

    int colorIndex = _categoryAttribute!.data.length + 2;
    // Add Color attribute
    AttributeColor attributeColor = _categoryAttribute!.attributeColor.first;

    list.add(Container(
      margin: EdgeInsets.only(left: 4),
      height: 85,
      child: Row(
        children: [
          GestureDetector(
            child: Container(
              margin: EdgeInsets.all(4),
              width: 64,
              height: 85,
              decoration: BoxDecoration(
                borderRadius: BorderRadius.circular(8),
                border: Border.all(color: Colors.black26, width: 1),
              ),
              child: Column(
                children: [
                  Padding(
                    padding: const EdgeInsets.only(top: 2.0, left: 2, right: 2),
                    child: Container(
                      width: 64,
                      height: 52,
                      decoration: BoxDecoration(
                          color: _hexToColor(
                              attributeColor.selectedColor == "null"
                                  ?  '#f5f456'
                                  : attributeColor.selectedColor),
                          borderRadius: BorderRadius.circular(8)),
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(top: 2.0),
                    child: Text(
                      attributeColor.selectedColorName == "null"
                          ? 'Color'
                          : attributeColor.selectedColorName,
                      style: GoogleFonts.roboto(fontSize: 10),
                    ),
                  )
                ],
              ),
            ),
            onTap: () {
              setState(() {
                if (colorIndex == _showIndex)
                  _showIndex = -1;
                else
                  _showIndex = colorIndex;
              });
            },
          ),
          Expanded(
            child: _showIndex == colorIndex
                ? Container(
                    child: ListView.builder(
                      itemBuilder: (context, cIndex) {
                        AttributeColor sColor = _categoryAttribute!
                            .attributeColor
                            .elementAt(cIndex);
                        return ColorSelector(
                          color: sColor.colorCode,
                          colorName: sColor.colorName,
                          onPress: () {
                            setState(() {
                              _color = sColor.colorCode;
                              _attributeColor = sColor;
                              attributeColor.selectedColor = sColor.colorCode;
                              attributeColor.selectedColorName =
                                  sColor.colorName;
                              _showIndex = -1;
                              _backward.add(SavedItem(
                                  index: DateTime.now().millisecond,
                                  fabric: null,
                                  color: sColor.colorCode,
                                  key: -1,
                                  data: null));
                            });
                            // print(attribute.zIndex);
                          },
                        );
                      },
                      itemCount: _categoryAttribute!.attributeColor.length,
                      scrollDirection: Axis.horizontal,
                    ),
                  )
                : Container(),
            flex: 75,
          )
        ],
      ),
    ));

    return ListView(
      children: list,
    );
  }

  Widget _createImageUI() {
    List<Widget> list = [];
    var sortedKeys = _map.keys.toList()..sort();
    for (int i = 0; i < sortedKeys.length; i++) {
      int key = sortedKeys.elementAt(i);
      SubAttributeData element = _map[key]!;
      list.add(Positioned(
          top: (element.top - element.height / 2) / _fraction,
          left: (element.left - element.width / 2) / _fraction,
          child: _fabric == null && _color == null
              ? Image.network(
                  element.image,
                  width: element.width / _fraction,
                  height: element.height / _fraction,
                )
              : _maskImage(
                  element.image.replaceAll('.png', '_dn.png'), element.image)));
    }

    return Stack(
      children: list,
    );
  }

  Widget _topMenuUI() => Container(
        height: 72,
        child: ListView.builder(
          itemBuilder: (context, index) {
            ImageTile tile = _topMenuItems.elementAt(index);
            return GestureDetector(
              onTap: () {
                if (_topMenuItems[index].id == 0) {
                  showDialog(
                    context: context,
                    builder: (BuildContext context) {
                      // return object of type Dialog
                      return AlertDialog(
                        content: Text(
                            "Are you sure you want to delete all the elements and start the design again?"),
                        actions: <Widget>[
                          TextButton(
                            child: new Text("Cancel"),
                            onPressed: () {
                              Navigator.of(context).pop();
                            },
                          ),
                          TextButton(
                            child: new Text("OK"),
                            onPressed: () {
                              SharePreferenceManager.instance
                                  .IsSharePreferenceEaseDataRemove();
                              SharePreferenceManager.instance
                                  .IsSharePreferenceYardageDataRemove();
                              Get.offAll(DashBoard());
                            },
                          ),
                          // usually buttons at the bottom of the dialog
                        ],
                      );
                    },
                  );
                } else if (_topMenuItems[index].id == 1) {
                  SpecsAlertDialogUI();
                } else if (_topMenuItems[index].id == 2) {
                  easeAlertDialogUI();
                }  else if (_topMenuItems[index].id == 3) {
                  YardageAlertDialogUI();
                }else if(_topMenuItems[index].id==5) {
                  showDialog(
                      context: context,
                      builder: (context) =>
                          ProgressDialogManager(), barrierDismissible: false)
                      .then((value) {});
                  saveImageFileInStorage();
                }
              },
              child: Container(
                padding: EdgeInsets.symmetric(horizontal: 16),
                child: Column(
                  children: [
                    Image.asset(
                      tile.image,
                      color: Colors.black,
                      height: 33,
                      width: 33,
                    ),
                    Padding(
                      padding: const EdgeInsets.only(left: 0.0, top: 5),
                      child: Text("${tile.name}".toUpperCase(),
                          style: GoogleFonts.aBeeZee(
                            fontSize: 10,
                            color: Colors.black,
                          )),
                    ),
                  ],
                ),
              ),
            );
          },
          itemCount: _topMenuItems.length,
          scrollDirection: Axis.horizontal,
        ),
      );

  Color _hexToColor(String hex) {
    assert(RegExp(r'^#([0-9a-fA-F]{6})|([0-9a-fA-F]{8})$').hasMatch(hex),
        'hex color must be #rrggbb or #rrggbbaa');

    return Color(
      int.parse(hex.substring(1), radix: 16) +
          (hex.length == 7 ? 0xff000000 : 0x00000000),
    );
  }

  Widget _maskImage(String pathDn, String pathUp) {
    return FutureBuilder(
        future: _loadAssetImageSize(pathDn),
        builder: (_, AsyncSnapshot<Size> snapshot) {
          if (!snapshot.hasData) return SizedBox();
          Size size = snapshot.data!;
          return Stack(
            children: [
              SizedBox(
                width: size.width / _fraction,
                height: size.height / _fraction,
                child: _color == null
                    ? MaskedImage(asset: _fabric!, mask: pathDn)
                    : _maskColor(pathUp: pathUp, pathDn: pathDn),
                // child: Image.asset(pathDn, scale: _scale, color: Colors.red,),
              ),
              Image.network(
                pathUp,
                scale: _fraction,
              ),
            ],
          );
        });
  }

  Widget _maskColor({required pathUp, required pathDn}) {
    return Stack(
      children: [
        Image.network(
          pathDn,
          color: _hexToColor(_color!),
        ),
        Image.network(pathUp),
      ],
    );
  }

  Future<Size> _loadAssetImageSize(String asset) async {
    Uint8List data = await loadImageByteData(asset);
    Codec codec = await instantiateImageCodec(data);
    FrameInfo fi = await codec.getNextFrame();
    return Size(fi.image.width.toDouble(), fi.image.height.toDouble());
  }

  SpecsAlertDialogUI() {
    showGeneralDialog(
        context: context,
        barrierDismissible: false,
        barrierLabel:
            MaterialLocalizations.of(context).modalBarrierDismissLabel,
        pageBuilder: (BuildContext buildContext, Animation animation,
            Animation secondaryAnimation) {
          var IsVisiblitySewingSpecs = true;
          var IsVisiblityFitModelSpecs = false;
          String? _selected;
          String? _selectedInchCm = "";
          String? height,
              Bustcircumference,
              Underbustcircumference,
              Hipscircumference,
              Upperarmcircumference,
              Waistcircumference,
              Kneecircumference,
              Neckcircumference,
              Shoulderwidth,
              Wristcircumference,
              Backwidth,
              Thighcircumference,
              Bustheightfromcenterbackneckpoint,
              Frontlengthofcenterbackneckpoint,
              Backlength,
              Bellyprotuberance,
              Buttocks;
          if (_selectedInchCm == "") {
            height = "175 Cm";
            Bustcircumference = "100.5 Cm";
            Underbustcircumference = "83.37 Cm";
            Hipscircumference = "105.28 Cm";
            Upperarmcircumference = "34.87 Cm";
            Waistcircumference = "76.67 Cm";
            Kneecircumference = "37.29 Cm";
            Neckcircumference = "40.45 Cm";
            Shoulderwidth = "11.77 Cm";
            Wristcircumference = "15.79 Cm";
            Backwidth = "36.53 Cm";
            Thighcircumference = "64.98 Cm";
            Bustheightfromcenterbackneckpoint = "38.46 Cm";
            Frontlengthofcenterbackneckpoint = "61.77 Cm";
            Backlength = "42.17 Cm";
            Bellyprotuberance = "A";
            Buttocks = "Very Flat";
          }
          Color colorCm = Colors.orange;
          Color colorInch = Colors.black;
          Color colorSewing = Colors.orange;
          Color colorFitModel = Colors.black;
          if (_selected == null) {
            IsVisiblitySewingSpecs = true;
            IsVisiblityFitModelSpecs = false;
          } else {
            IsVisiblitySewingSpecs = true;
            IsVisiblityFitModelSpecs = false;
          }
          String chooseMesurment = ' Cm';
          double measure = 0;
          return StatefulBuilder(builder: (context, setState) {
            return Padding(
              padding: const EdgeInsets.all(8.0),
              child: Center(
                child: Material(
                  color: Colors.transparent,
                  child: Container(
                    decoration: ShapeDecoration(
                        color: Colors.white,
                        shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(8.0))),
                    child: SingleChildScrollView(
                      scrollDirection: Axis.vertical,
                      child: Column(
                        mainAxisSize: MainAxisSize.min,
                        children: [
                          Padding(
                            padding: const EdgeInsets.only(top: 24.0),
                            child: Container(
                              width: double.infinity,
                              height: 45,
                              color: Colors.white,
                              child: Row(
                                children: [
                                  Spacer(),
                                  Center(
                                      child: Text(
                                    "Specs".toUpperCase(),
                                    style: GoogleFonts.aBeeZee(
                                        fontSize: 16, color: Colors.black),
                                  )),
                                  Spacer(),
                                  Padding(
                                    padding: const EdgeInsets.only(bottom: 3.0),
                                    child: IconButton(
                                        onPressed: () {
                                          Navigator.of(context).pop();
                                        },
                                        icon: Icon(Icons.cancel)),
                                  )
                                ],
                              ),
                            ),
                          ),
                          Padding(
                            padding: const EdgeInsets.all(8.0),
                            child: Row(
                              children: [
                                Padding(
                                  padding: const EdgeInsets.all(8.0),
                                  child: GestureDetector(
                                    onTap: () {
                                      setState(() {
                                        colorFitModel = Colors.black;
                                        colorSewing = Colors.orange;
                                        _selected = null;
                                        IsVisiblitySewingSpecs = true;
                                        IsVisiblityFitModelSpecs = false;
                                      });
                                    },
                                    child: Container(
                                      width: 110,
                                      color: Colors.white,
                                      child: Text(
                                        "Sewing Specs".toUpperCase(),
                                        style: GoogleFonts.aBeeZee(
                                            fontWeight: FontWeight.bold,
                                            fontSize: 12,
                                            color: colorSewing),
                                      ),
                                    ),
                                  ),
                                ),
                                Padding(
                                  padding: const EdgeInsets.all(0.0),
                                  child: GestureDetector(
                                    onTap: () {
                                      setState(() {
                                        colorFitModel = Colors.orange;
                                        colorSewing = Colors.black;
                                        _selected = "FitModel";
                                        IsVisiblitySewingSpecs = false;
                                        IsVisiblityFitModelSpecs = true;
                                        debugPrint(_selected);
                                      });
                                    },
                                    child: Container(
                                      width: 110,
                                      color: Colors.white,
                                      child: Text(
                                        "FitModel Specs".toUpperCase(),
                                        style: GoogleFonts.aBeeZee(
                                            fontWeight: FontWeight.bold,
                                            fontSize: 12,
                                            color: colorFitModel),
                                      ),
                                    ),
                                  ),
                                ),
                              ],
                            ),
                          ),
                          Padding(
                            padding: const EdgeInsets.all(8.0),
                            child: Row(
                              children: [
                                Text(
                                  "Point Of Measure".toUpperCase(),
                                  style: GoogleFonts.aBeeZee(
                                      color: Colors.black87,
                                      fontSize: 12.4,
                                      fontWeight: FontWeight.bold),
                                ),
                                Spacer(),
                                Text(
                                  "Measure".toUpperCase(),
                                  style: GoogleFonts.aBeeZee(
                                      color: Colors.black87,
                                      fontSize: 12.4,
                                      fontWeight: FontWeight.bold),
                                ),
                              ],
                            ),
                          ),
                          Divider(
                            height: 1,
                            color: Colors.grey[200],
                          ),
                          Visibility(
                            visible: IsVisiblitySewingSpecs,
                            child: ListView.builder(
                                physics: NeverScrollableScrollPhysics(),
                                shrinkWrap: true,
                                itemCount:
                                    _categoryAttribute!.specsDatum.length ==
                                            null
                                        ? 0
                                        : _categoryAttribute!.specsDatum.length,
                                itemBuilder: (context, index) {
                                  SpecsDatum specs = _categoryAttribute!
                                      .specsDatum
                                      .elementAt(index);
                                  if (chooseMesurment == " Inch") {
                                    measure =
                                        double.parse(specs.measure) / 2.54;
                                  }
                                  return Padding(
                                    padding: const EdgeInsets.all(6.0),
                                    child: Row(
                                      children: [
                                        Flexible(
                                          child: Align(
                                            alignment: Alignment.centerLeft,
                                            child: Text(
                                              specs.pointOfMeasure.capitalizeFirst.toString(),
                                             textAlign: TextAlign.left,
                                              style: TextStyle(
                                                color: Colors.grey[700],
                                                fontSize: 14.4,
                                              ),
                                            ),
                                          ),
                                        ),
                                        Spacer(),
                                        Flexible(
                                          child: Align(
                                            alignment: Alignment.centerRight,
                                            child: Text(
                                              chooseMesurment == ' Cm'
                                                  ? specs.measure + chooseMesurment
                                                  : measure.toStringAsFixed(2) +
                                                      chooseMesurment,
                                              textAlign: TextAlign.end,
                                              style: GoogleFonts.aBeeZee(
                                                color: Colors.grey[700],
                                                fontSize: 14.4,
                                              ),
                                            ),
                                          ),
                                        ),
                                      ],
                                    ),
                                  );
                                }),
                          ),
                          Visibility(
                              visible: IsVisiblityFitModelSpecs,
                              child: FitModelSpecsUI(
                                  height!,
                                  Bustcircumference!,
                                  Hipscircumference!,
                                  Upperarmcircumference!,
                                  Underbustcircumference!,
                                  Waistcircumference!,
                                  Thighcircumference!,
                                  Kneecircumference!,
                                  Neckcircumference!,
                                  Shoulderwidth!,
                                  Wristcircumference!,
                                  Backwidth!,
                                  Bustheightfromcenterbackneckpoint!,
                                  Frontlengthofcenterbackneckpoint!,
                                  Backlength!,
                                  Bellyprotuberance!,
                                  Buttocks!)),
                          Container(
                            height: 80,
                            child: Padding(
                              padding: const EdgeInsets.all(8.0),
                              child: Row(
                                children: [
                                  Spacer(),
                                  GestureDetector(
                                    onTap: () {
                                      setState(() {
                                        colorCm = Colors.black;
                                        colorInch = Colors.orange;
                                        height = "68.9 Inch";
                                        Bustcircumference = "39.67 Inch";
                                        Underbustcircumference = "32.82 Inch";
                                        Hipscircumference = "41.45 Inch";
                                        Upperarmcircumference = "13.73 Inch";
                                        Waistcircumference = "30.19 Inch";
                                        Kneecircumference = "14.68 Inch";
                                        Neckcircumference = "15.93 Inch";
                                        Shoulderwidth = "4.63 Inch";
                                        Wristcircumference = "6.22 Inch";
                                        Backwidth = "14.38 Inch";
                                        Thighcircumference = "25.58 Inch";
                                        Bustheightfromcenterbackneckpoint =
                                            "15.14 Inch";
                                        Frontlengthofcenterbackneckpoint =
                                            "24.32 Inch";
                                        Backlength = "16.6 Inch";
                                        Bellyprotuberance = "A";
                                        Buttocks = "Very Flat";
                                        chooseMesurment = " Inch";
                                      });
                                    },
                                    child: Container(
                                      color: Colors.white,
                                      height: 60,
                                      width: 60,
                                      child: Align(
                                        alignment: Alignment.centerRight,
                                        child: Text(
                                          "Inch",
                                          style: GoogleFonts.aBeeZee(
                                              color: colorInch,
                                              fontWeight: FontWeight.w800,
                                              fontSize: 12),
                                        ),
                                      ),
                                    ),
                                  ),
                                  Padding(
                                    padding: const EdgeInsets.only(right: 8.0),
                                    child: GestureDetector(
                                      onTap: () {
                                        setState(() {
                                          colorCm = Colors.orange;
                                          colorInch = Colors.black;
                                          height = "175 Cm";
                                          Bustcircumference = "100.5 Cm";
                                          Underbustcircumference = "83.37 Cm";
                                          Hipscircumference = "105.28 Cm";
                                          Upperarmcircumference = "34.87 Cm";
                                          Waistcircumference = "76.67 Cm";
                                          Kneecircumference = "37.29 Cm";
                                          Neckcircumference = "40.45 Cm";
                                          Shoulderwidth = "11.77 Cm";
                                          Wristcircumference = "15.79 Cm";
                                          Backwidth = "36.53 Cm";
                                          Thighcircumference = "64.98 Cm";
                                          Bustheightfromcenterbackneckpoint =
                                              "38.46 Cm";
                                          Frontlengthofcenterbackneckpoint =
                                              "61.77 Cm";
                                          Backlength = "42.17 Cm";
                                          Bellyprotuberance = "A";
                                          Buttocks = "Very Flat";
                                          chooseMesurment = " Cm";
                                        });
                                      },
                                      child: Container(
                                        color: Colors.white,
                                        height: 60,
                                        width: 60,
                                        child: Align(
                                          alignment: Alignment.centerRight,
                                          child: Text(
                                            "Cm",
                                            style: GoogleFonts.aBeeZee(
                                                color: colorCm,
                                                fontWeight: FontWeight.w800,
                                                fontSize: 12),
                                          ),
                                        ),
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
                  ),
                ),
              ),
            );
          });
        });
  }

  easeAlertDialogUI() {
    showDialog(
        context: context,
        builder: (_) => EaseDialogUI(
              categoryWiseAttribute: _categoryAttribute,
            ));
  }

  YardageAlertDialogUI(){
    showDialog(
        context: context,
        builder: (_) => YardageUI());
  }



  Widget FitModelSpecsUI(
      String height,
      String Bustcircumference,
      String Hipscircumference,
      String Upperarmcircumference,
      String Underbustcircumference,
      String Waistcircumference,
      String Thighcircumference,
      String Kneecircumference,
      String Neckcircumference,
      String Shoulderwidth,
      String Wristcircumference,
      String Backwidth,
      String Bustheightfromcenterbackneckpoint,
      String Frontlengthofcenterbackneckpoint,
      String Backlength,
      String Bellyprotuberance,
      String Buttocks) {
    return Column(
      children: [
        FitModelUI(text: "Height", text1: height),
        FitModelUI(text: "Bust circumference", text1: Bustcircumference),
        FitModelUI(
            text: "Under bust circumference", text1: Underbustcircumference),
        FitModelUI(
            text: "Upper arm circumference", text1: Upperarmcircumference),
        FitModelUI(text: "Waist circumference", text1: Waistcircumference),
        FitModelUI(text: "Hips circumference", text1: Hipscircumference),
        FitModelUI(text: "Thigh circumference", text1: Thighcircumference),
        FitModelUI(text: "Knee circumference", text1: Kneecircumference),
        FitModelUI(text: "Neck circumference", text1: Neckcircumference),
        FitModelUI(text: "Shoulder width", text1: Shoulderwidth),
        FitModelUI(text: "Wrist circumference", text1: Wristcircumference),
        FitModelUI(text: "Back width", text1: Backwidth),
        FitModelUI(
            text: "Bust height from center back neck point",
            text1: Bustheightfromcenterbackneckpoint),
        FitModelUI(
            text: "Front length of center back neck point",
            text1: Frontlengthofcenterbackneckpoint),
        FitModelUI(text: "Back length", text1: Backlength),
        FitModelUI(text: "Belly protuberance", text1: Bellyprotuberance),
        FitModelUI(text: "Buttocks", text1: Buttocks),
      ],
    );
  }

  Widget YardageBasicUI(String onSelected, String inchCmType,
      List<String> _spFabricInchList, List<String> _spFabricCmList) {
    return StatefulBuilder(builder: (context, setState) {
      return Column(
        children: [
          Padding(
            padding: const EdgeInsets.only(left: 15.0, top: 10),
            child: Align(
                alignment: Alignment.centerLeft,
                child: Text(
                  'Fabric Width',
                  textAlign: TextAlign.center,
                  style: GoogleFonts.aBeeZee(
                      fontWeight: FontWeight.bold,
                      fontSize: 12,
                      color: Colors.black),
                )),
          ),
          Padding(
            padding: const EdgeInsets.only(
                left: 10.0, bottom: 8, top: 10, right: 10),
            child: Container(
              height: 35,
              decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(10),
                  border: Border.all(color: Colors.grey, width: 1)),
              width: double.infinity,
              child: DropdownButton(
                isExpanded: true,
                underline: Container(
                  decoration: ShapeDecoration(
                    shape: RoundedRectangleBorder(),
                  ),
                ),
                value: onSelected,
                items: inchCmType == "Cm"
                    ? _spFabricCmList.map<DropdownMenuItem<String>>((String e) {
                        print(e);
                        return DropdownMenuItem<String>(
                          value: e,
                          child: Padding(
                            padding: const EdgeInsets.only(left: 8.0),
                            child: Text(
                              e,
                              style:
                                  TextStyle(color: Colors.black, fontSize: 14),
                            ),
                          ),
                        );
                      }).toList()
                    : _spFabricInchList
                        .map<DropdownMenuItem<String>>((String e) {
                        return DropdownMenuItem<String>(
                          value: e,
                          child: Padding(
                            padding: const EdgeInsets.only(left: 8.0),
                            child: Text(
                              e,
                              style:
                                  TextStyle(color: Colors.black, fontSize: 14),
                            ),
                          ),
                        );
                      }).toList(),
                onChanged: (String? value) {
                  setState(() {
                    print(value);
                    onSelected = value!;
                  });
                },
              ),
            ),
          ),
        ],
      );
    });
  }

  Widget YardageAdvanceUI(
      String onSelectedMFabric,
      String inchCmType,
      List<String> _spMainFabricCmList,
      List<String> _spMainFabricInchList,
      List<String> _spFusibleinterfacingCmList,
      String Fusibleinterfacing,
      List<String> _spFusibleinterfacingInchList) {
    return StatefulBuilder(builder: (context, setState) {
      return Column(
        children: [
          Padding(
            padding: const EdgeInsets.only(left: 15.0, top: 10),
            child: Align(
                alignment: Alignment.centerLeft,
                child: Text(
                  'Main Fabric ',
                  textAlign: TextAlign.center,
                  style: GoogleFonts.aBeeZee(
                      fontWeight: FontWeight.bold,
                      fontSize: 12,
                      color: Colors.black),
                )),
          ),
          Padding(
            padding: const EdgeInsets.only(
                left: 10.0, bottom: 8, top: 10, right: 10),
            child: Container(
              height: 35,
              decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(10),
                  border: Border.all(color: Colors.grey, width: 1)),
              width: double.infinity,
              child: DropdownButton(
                isExpanded: true,
                underline: Container(
                  decoration: ShapeDecoration(
                    shape: RoundedRectangleBorder(),
                  ),
                ),
                value: onSelectedMFabric,
                items: inchCmType == "Cm"
                    ? _spMainFabricCmList
                        .map<DropdownMenuItem<String>>((String e) {
                        return DropdownMenuItem<String>(
                          value: e,
                          child: Padding(
                            padding: const EdgeInsets.only(left: 8.0),
                            child: Text(
                              e,
                              style:
                                  TextStyle(color: Colors.black, fontSize: 14),
                            ),
                          ),
                        );
                      }).toList()
                    : _spMainFabricInchList
                        .map<DropdownMenuItem<String>>((String e) {
                        return DropdownMenuItem<String>(
                          value: e,
                          child: Padding(
                            padding: const EdgeInsets.only(left: 8.0),
                            child: Text(
                              e,
                              style:
                                  TextStyle(color: Colors.black, fontSize: 14),
                            ),
                          ),
                        );
                      }).toList(),
                onChanged: (String? value) {
                  setState(() {
                    onSelectedMFabric = value!;
                  });
                },
              ),
            ),
          ),
          //Fusible interfacing
          Padding(
            padding: const EdgeInsets.only(left: 15.0, top: 10),
            child: Align(
                alignment: Alignment.centerLeft,
                child: Text(
                  'Fusible interfacing',
                  textAlign: TextAlign.center,
                  style: GoogleFonts.aBeeZee(
                      fontWeight: FontWeight.bold,
                      fontSize: 12,
                      color: Colors.black),
                )),
          ),
          Padding(
            padding: const EdgeInsets.only(
                left: 10.0, bottom: 8, top: 10, right: 10),
            child: Container(
              height: 35,
              decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(10),
                  border: Border.all(color: Colors.grey, width: 1)),
              width: double.infinity,
              child: DropdownButton(
                isExpanded: true,
                underline: Container(
                  decoration: ShapeDecoration(
                    shape: RoundedRectangleBorder(),
                  ),
                ),
                value: Fusibleinterfacing,
                items: inchCmType == "Cm"
                    ? _spFusibleinterfacingCmList
                        .map<DropdownMenuItem<String>>((String e) {
                        return DropdownMenuItem<String>(
                          value: e,
                          child: Padding(
                            padding: const EdgeInsets.only(left: 8.0),
                            child: Text(
                              e,
                              style:
                                  TextStyle(color: Colors.black, fontSize: 14),
                            ),
                          ),
                        );
                      }).toList()
                    : _spFusibleinterfacingInchList
                        .map<DropdownMenuItem<String>>((String e) {
                        return DropdownMenuItem<String>(
                          value: e,
                          child: Padding(
                            padding: const EdgeInsets.only(left: 8.0),
                            child: Text(
                              e,
                              style:
                                  TextStyle(color: Colors.black, fontSize: 14),
                            ),
                          ),
                        );
                      }).toList(),
                onChanged: (String? value) {
                  setState(() {
                    Fusibleinterfacing = value!;
                  });
                },
              ),
            ),
          ),
        ],
      );
    });
  }

  Future<void> requestPermissions() async{
    final permission=await Permission.storage.isGranted;
    bool isCheck=permission==ServiceStatus.enabled;
    final permissionAllow=await Permission.storage.request();
    if(permissionAllow==PermissionStatus.granted){
      print("Permission Successfully Granted...");
    }else if(permissionAllow==PermissionStatus.denied){
      print("Permission denied Successfully...");
    }
  }

  void saveImageFileInStorage()async{
    await _screenshotController.capture(delay: const Duration(seconds: 2)).then((value)async{
      if(value!=null) {
        //var directory = (await getExternalStorageDirectories(type:
       // StorageDirectory.downloads))!.first;
        Directory generalDownloadDir = Directory('/storage/emulated/0/Download');
        var imgPath = '${generalDownloadDir.path}/${DateTime.now().microsecondsSinceEpoch.toString()}.png';
        File file=new File(imgPath);
        file.writeAsBytes(value);
        Fluttertoast.showToast(msg: 'Design Download Successfully.');
        print(imgPath);
        Navigator.of(context).pop();
      }else{
        Navigator.of(context).pop();
      }
    });
  }
}

class EaseDialogUI extends StatefulWidget {
  final CategoryWiseAttribute? categoryWiseAttribute;

  const EaseDialogUI({Key? key, required this.categoryWiseAttribute})
      : super(key: key);

  @override
  _EaseDialogUIState createState() => _EaseDialogUIState();
}

class _EaseDialogUIState extends State<EaseDialogUI> {
  String? onFit = "Choose Fit";
  String? onFabricType = "Choose Fabric Type";
  Color colorInch = Colors.black;
  Color colorCm = Colors.orange;
  List<String> easeKeysList = [];
  List<String> easeValueList = [];
  late Map<String, String> easekeyValueList;
  String? onInchCm = "Choose Measurement In Inch/Cm";
  ScrollController _scrollController = ScrollController();
  var _key = GlobalKey<FormState>();

  @override
  void initState() {
    SharePreferenceManager.instance
        .getDropDownFabricType("FabricType")
        .then((value) {
      setState(() {
        if (value != null && value != "") onFabricType = value;
      });
    });
    SharePreferenceManager.instance.getDropDownFit("Fit").then((value) {
      setState(() {
        if (value != null && value != "") onFit = value;
      });
    });

    SharePreferenceManager.instance.getDropDownInchOrCm("InchCm").then((value) {
      setState(() {
        if (value != null && value != "") onInchCm = value;
      });
    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(top: 30.0, bottom: 20, left: 5, right: 5),
      child: Center(
        child: Material(
          color: Colors.transparent,
          child: Container(
            decoration: ShapeDecoration(
                color: Colors.white,
                shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(8.0))),
            child: Padding(
              padding: const EdgeInsets.all(8.0),
              child: Scaffold(
                resizeToAvoidBottomInset: true,
                body: SingleChildScrollView(
                  scrollDirection: Axis.vertical,
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Padding(
                        padding: const EdgeInsets.only(
                            top: 5.0, left: 10, right: 10),
                        child: Container(
                          width: double.infinity,
                          height: 45,
                          child: Row(
                            children: [
                              Spacer(),
                              Center(
                                  child: Text(
                                "Ease".toUpperCase(),
                                style: GoogleFonts.aBeeZee(
                                    fontSize: 16, color: Colors.black),
                              )),
                              Spacer(),
                              Padding(
                                padding: const EdgeInsets.only(bottom: 8.0),
                                child: IconButton(
                                    onPressed: () {
                                      Navigator.of(context).pop();
                                    },
                                    icon: Icon(Icons.cancel)),
                              )
                            ],
                          ),
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: Row(
                          children: [
                            Text(
                              "Point Of Measure".toUpperCase(),
                              style: GoogleFonts.aBeeZee(
                                  color: Colors.black87,
                                  fontSize: 12.4,
                                  fontWeight: FontWeight.bold),
                            ),
                            Spacer(),
                            Text(
                              "Ease".toUpperCase(),
                              style: GoogleFonts.aBeeZee(
                                  color: Colors.black87,
                                  fontSize: 12.4,
                                  fontWeight: FontWeight.bold),
                            ),
                          ],
                        ),
                      ),
                      Divider(
                        height: 1,
                        color: Colors.grey[200],
                      ),
                      Padding(
                        padding: const EdgeInsets.only(
                            left: 8.0, bottom: 8, top: 10, right: 10),
                        child: Container(
                          height: 35,
                          decoration: BoxDecoration(
                              borderRadius: BorderRadius.circular(6),
                              border: Border.all(color: Colors.grey, width: 1)),
                          width: double.infinity,
                          child: DropdownButton(
                            isExpanded: true,
                            underline: Container(
                              decoration: ShapeDecoration(
                                shape: RoundedRectangleBorder(),
                              ),
                            ),
                            value: onFabricType,
                            items: <String>[
                              "Choose Fabric Type",
                              "No Stretch Woven",
                              "Low Stretch Woven",
                              "Medium Stretch Woven",
                              "High Stretch Woven",
                            ].map<DropdownMenuItem<String>>((String e) {
                              return DropdownMenuItem<String>(
                                value: e,
                                child: Padding(
                                  padding: const EdgeInsets.only(left: 8.0),
                                  child: Text(
                                    e,
                                    style: TextStyle(
                                        color: Colors.black, fontSize: 15),
                                  ),
                                ),
                              );
                            }).toList(),
                            onChanged: (String? value) {
                              setState(() {
                                onFabricType = value;
                              });
                            },
                          ),
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.only(
                            left: 8.0, bottom: 8, top: 10, right: 10),
                        child: Container(
                          height: 35,
                          decoration: BoxDecoration(
                              borderRadius: BorderRadius.circular(6),
                              border: Border.all(color: Colors.grey, width: 1)),
                          width: double.infinity,
                          child: DropdownButton(
                            isExpanded: true,
                            underline: Container(
                              decoration: ShapeDecoration(
                                shape: RoundedRectangleBorder(),
                              ),
                            ),
                            value: onFit,
                            items: <String>[
                              "Choose Fit",
                              "Very Fitted",
                              "Fitted",
                              "Semi-Fitted",
                            ].map<DropdownMenuItem<String>>((String e) {
                              return DropdownMenuItem<String>(
                                value: e,
                                child: Padding(
                                  padding: const EdgeInsets.only(left: 8.0),
                                  child: Text(
                                    e,
                                    style: TextStyle(
                                        color: Colors.black, fontSize: 15),
                                  ),
                                ),
                              );
                            }).toList(),
                            onChanged: (String? value) {
                              setState(() {
                                onFit = value;
                              });
                            },
                          ),
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.only(
                            left: 8.0, bottom: 8, top: 10, right: 10),
                        child: Container(
                          height: 35,
                          decoration: BoxDecoration(
                              borderRadius: BorderRadius.circular(6),
                              border: Border.all(color: Colors.grey, width: 1)),
                          width: double.infinity,
                          child: DropdownButton(
                            isExpanded: true,
                            underline: Container(
                              decoration: ShapeDecoration(
                                shape: RoundedRectangleBorder(),
                              ),
                            ),
                            value: onInchCm,
                            items: <String>[
                              "Choose Measurement In Inch/Cm",
                              "Inch",
                              "Cm",
                            ].map<DropdownMenuItem<String>>((String e) {
                              return DropdownMenuItem<String>(
                                value: e,
                                child: Padding(
                                  padding: const EdgeInsets.only(left: 8.0),
                                  child: Text(
                                    e,
                                    style: TextStyle(
                                        color: Colors.black, fontSize: 15),
                                  ),
                                ),
                              );
                            }).toList(),
                            onChanged: (String? value) {
                              setState(() {
                                onInchCm = value;
                              });
                            },
                          ),
                        ),
                      ),
                      Form(
                        key: _key,
                        child: ListView.builder(
                            physics: NeverScrollableScrollPhysics(),
                            shrinkWrap: true,
                            itemCount: widget.categoryWiseAttribute!.easeDatum
                                        .length ==
                                    null
                                ? 0
                                : widget
                                    .categoryWiseAttribute!.easeDatum.length,
                            itemBuilder: (context, index) {
                              EaseDatum ease = widget
                                  .categoryWiseAttribute!.easeDatum
                                  .elementAt(index);
                              return Padding(
                                padding: const EdgeInsets.only(
                                    left: 8.0, top: 8, right: 8, bottom: 8),
                                child: SizedBox(
                                  width: double.infinity,
                                  height: 55,
                                  child: TextFormField(
                                    validator: (v) {
                                      print(v.toString());
                                      if (v!.trim().isEmpty) {
                                        return 'Please fill the all ease .';
                                      }else if(v=="0"||v=="00"||v=="000"||v=="0000"){
                                        return 'Please fill all ease greater than 0.';
                                      }
                                      return null;
                                    },
                                    onChanged: (v) {
                                      easeKeysList.add(ease.pointOfMeasure);
                                      easeValueList.add(ease.tvcontroller.text);
                                      easekeyValueList = Map.fromIterables(
                                          easeKeysList, easeValueList);
                                    },
                                    controller: ease.tvcontroller,
                                    keyboardType: TextInputType.number,
                                    textAlign: TextAlign.left,
                                    inputFormatters: [
                                      LengthLimitingTextInputFormatter(4),
                                      FilteringTextInputFormatter.digitsOnly
                                    ],
                                    textInputAction: TextInputAction.next,
                                    decoration: InputDecoration(
                                        errorBorder: OutlineInputBorder(
                                            borderRadius:
                                                BorderRadius.circular(6),
                                            borderSide: BorderSide(
                                                width: 1, color: Colors.red)),
                                        enabledBorder: OutlineInputBorder(
                                            borderRadius:
                                                BorderRadius.circular(6),
                                            borderSide: BorderSide(
                                                width: 1,
                                                color: Colors.black54)),
                                        focusedBorder: OutlineInputBorder(
                                            borderRadius:
                                                BorderRadius.circular(6),
                                            borderSide: BorderSide(
                                                width: 1,
                                                color: Colors.black54)),
                                        contentPadding:
                                            EdgeInsets.only(left: 10, top: 10),
                                        border: OutlineInputBorder(
                                            borderRadius:
                                                BorderRadius.circular(5.0),
                                            borderSide: BorderSide(
                                                color: Colors.black54)),
                                        labelText: ease.pointOfMeasure + " *",
                                        labelStyle: GoogleFonts.aBeeZee(
                                            color: Colors.black, fontSize: 13),
                                        hintText: "0 Inch/Cm",
                                        hintStyle: GoogleFonts.aBeeZee(
                                            color: Colors.black, fontSize: 13)),
                                  ),
                                ),
                              );
                            }),
                      ),
                      Container(
                        height: 60,
                        child: Row(
                          children: [
                            Spacer(),
                            GestureDetector(
                              onTap: () {
                                if (onFabricType != "Choose Fabric Type") {
                                  if (onFit != "Choose Fit") {
                                    if (onInchCm !=
                                        "Choose Measurement In Inch/Cm") {
                                      if (_key.currentState!.validate()) {
                                        SharePreferenceManager.instance
                                            .setDropDownFabricType(
                                                "FabricType", onFabricType!);
                                        SharePreferenceManager.instance
                                            .setDropDownFit("Fit", onFit!);
                                        SharePreferenceManager.instance
                                            .setDropDownInchOrCm(
                                                "InchCm", onInchCm!);
                                        easeKeysList.add("Fabric");
                                        easeValueList.add(onFabricType!);
                                        easeKeysList.add("Fit");
                                        easeValueList.add(onFit!);
                                        easeKeysList.add("Inch/Cm");
                                        easeValueList.add(onInchCm!);
                                        easekeyValueList = Map.fromIterables(
                                            easeKeysList, easeValueList);
                                        SharePreferenceManager.instance
                                            .setEaseData("Ease",
                                                easekeyValueList.toString());
                                        Navigator.of(context).pop();
                                      }
                                    } else {
                                      Fluttertoast.showToast(
                                          msg:
                                              "Please choose measurement in Inch/Cm");
                                    }
                                  } else {
                                    Fluttertoast.showToast(
                                        msg: "Please choose fit");
                                  }
                                } else {
                                  Fluttertoast.showToast(
                                      msg: "Please choose fabric type");
                                }
                              },
                              child: Container(
                                height: 45,
                                width: 110,
                                decoration: new BoxDecoration(
                                  color: Colors.black,
                                  borderRadius: BorderRadius.circular(8),
                                ),
                                child: Center(
                                  child: Text(
                                    'Apply',
                                    style: GoogleFonts.aBeeZee(
                                        color: Colors.white),
                                  ),
                                ),
                              ),
                            ),
                            Spacer()
                          ],
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}

class FitModelUI extends StatelessWidget {
  final String text;
  final String text1;

  const FitModelUI({Key? key, required this.text, required this.text1})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: Row(
        children: [
          Flexible(
            flex: 1,
            child: Align(
              alignment: Alignment.centerLeft,
              child: Text(
                text,
                maxLines: 2,
                overflow: TextOverflow.ellipsis,
                textAlign: TextAlign.left,
                style: TextStyle(
                  color: Colors.grey[700],
                  fontSize: 13.4,
                ),
              ),
            ),
          ),
          Spacer(),
          Flexible(
            flex: 1,
            child: Align(
              alignment: Alignment.centerRight,
              child: Text(
                text1,
                textAlign: TextAlign.end,
                style: TextStyle(
                  color: Colors.grey[700],
                  fontSize: 13.4,
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}


class YardageUI extends StatefulWidget {
  const YardageUI({Key? key}) : super(key: key);

  @override
  _YardageUIState createState() => _YardageUIState();
}

class _YardageUIState extends State<YardageUI> {


  String? InchCmType = "Cm";
  String? onSelected = "Choose Fabric Width";
  String? onSelectedInchCm = "Choose Fabric Width";
  String? onSelectedMFabricCm = "Choose Main Fabric";
  String? onSelectedMFabricInch = "Choose Main Fabric";
  String? FusibleinterfacingCm = "Choose Fusible Interfacing";
  String? FusibleinterfacingInch = "Choose Fusible Interfacing";
  List<String> _spFabricCmList = [
    "Choose Fabric Width",
    "88 Cm",
    "99 Cm",
    "104 Cm",
    "111 Cm",
    "127 Cm",
    "132 Cm",
    "147 Cm",
    "167 Cm",
    "182 Cm",
    "243 Cm",
    "274 Cm"
  ];
  List<String> _spFabricInchList = [
    "Choose Fabric Width",
    "35-36 ",
    "39",
    "41",
    "44-45",
    "50",
    "52-54",
    "58-60",
    "66",
    "72",
    "96",
    "108"
  ];
  //Fabric Main
  List<String> _spMainFabricCmList = [
    "Choose Main Fabric",
    "88 Cm",
    "99 Cm",
    "104 Cm",
    "111 Cm",
    "127 Cm",
    "132 Cm",
    "147 Cm",
    "167 Cm",
    "182 Cm",
    "243 Cm",
    "274 Cm"
  ];
  List<String> _spMainFabricInchList = [
    "Choose Main Fabric",
    "35-36 ",
    "39",
    "41",
    "44-45",
    "50",
    "52-54",
    "58-60",
    "66",
    "72",
    "96",
    "108"
  ];
  //---

  //Fusibleinterfacing
  List<String> _spFusibleinterfacingCmList = [
    "Choose Fusible Interfacing",
    "88 Cm",
    "99 Cm",
    "104 Cm",
    "111 Cm",
    "127 Cm",
    "132 Cm",
    "147 Cm",
    "167 Cm",
    "182 Cm",
    "243 Cm",
    "274 Cm"
  ];

  List<String> _spFusibleinterfacingInchList = [
    "Choose Fusible Interfacing",
    "35-36 ",
    "39",
    "41",
    "44-45",
    "50",
    "52-54",
    "58-60",
    "66",
    "72",
    "96",
    "108"
  ];
  //--
  Color colorCm = Colors.orange;
  Color colorInch = Colors.black;
  Color colorBasic = Colors.orange;
  Color colorAdvanced = Colors.black;
  bool IsVisiblityBasicUI = true,
      IsVisiblityAdvanceUI = false,
      IsVisiblityFBWidthCm = true,
      IsVisiblityFBWidthInch = false,
      IsVisiblityMainFBCm = true,
      IsVisiblityMainFBInch = false,
      IsVisiblityFusibleCm = true,
      IsVisiblityFusibleInch = false;
  String _selectedYardageMode = 'Basic';
  String _selectedYardageIncm = 'Cm';

  @override
  void initState() {
              SharePreferenceManager.instance
                  .getDropDownFabricWidthCm("FabricWidthCm")
                  .then((value) {
                setState(() {
                  if (value != null && value != "") onSelected = value;
                });
              });

              SharePreferenceManager.instance
                  .getDropDownFabricWidthInch("FabricWidthInch")
                  .then((value) {
                setState(() {
                  if (value != null && value != "")
                    onSelectedInchCm = value;
                  print(onSelectedInchCm);
                });
              });

              SharePreferenceManager.instance
                  .getDropDownMainFabricCm("MainFabricCm")
                  .then((value) {
                setState(() {
                  if (value != null && value != "") onSelectedMFabricCm = value;
                });
              });

              SharePreferenceManager.instance
                  .getDropDownMainFabricCm("MainFabricInch")
                  .then((value) {
                setState(() {
                  if (value != null && value != "") onSelectedMFabricInch = value;
                });
              });

              //FusibleinterfacingCm

              SharePreferenceManager.instance
                  .getDropDownMainFabricCm("FusibleinterfacingCm")
                  .then((value) {
                setState(() {
                  if (value != null && value != "") FusibleinterfacingCm = value;
                });
              });

              SharePreferenceManager.instance
                  .getDropDownMainFabricCm("FusibleinterfacingInch")
                  .then((value) {
                setState(() {
                  if (value != null && value != "")
                    FusibleinterfacingInch = value;
                });
              });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Material(
        child: Container(
          decoration: ShapeDecoration(
              color: Colors.white,
              shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(8.0))),
          child: SingleChildScrollView(
            scrollDirection: Axis.vertical,
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: Container(
                    width: double.infinity,
                    height: 45,
                    color: Colors.white,
                    child: Row(
                      children: [
                        Spacer(),
                        Center(
                            child: Text(
                              "Yardage calculator".toUpperCase(),
                              style: GoogleFonts.aBeeZee(
                                  fontSize: 16, color: Colors.black),
                            )),
                        Spacer(),
                        IconButton(
                            onPressed: () {
                              Navigator.of(context).pop();
                            },
                            icon: Icon(Icons.cancel))
                      ],
                    ),
                  ),
                ),
                Column(
                  mainAxisSize: MainAxisSize.min,
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: Row(
                        children: [
                          Padding(
                            padding: const EdgeInsets.all(8.0),
                            child: GestureDetector(
                              onTap: () {
                                setState(() {
                                  colorAdvanced = Colors.black;
                                  colorBasic = Colors.orange;
                                  IsVisiblityAdvanceUI = false;
                                  IsVisiblityBasicUI = true;
                                  _selectedYardageMode = "Basic";
                                });
                              },
                              child: Container(
                                width: 60,
                                height: 20,
                                color: Colors.white,
                                child: Text(
                                  "Basic".toUpperCase(),
                                  style: GoogleFonts.aBeeZee(
                                      fontWeight: FontWeight.bold,
                                      fontSize: 12,
                                      color: colorBasic),
                                ),
                              ),
                            ),
                          ),
                          Padding(
                            padding: const EdgeInsets.all(8.0),
                            child: GestureDetector(
                              onTap: () {
                                setState(() {
                                  colorAdvanced = Colors.orange;
                                  colorBasic = Colors.black;
                                  IsVisiblityAdvanceUI = true;
                                  IsVisiblityBasicUI = false;
                                  _selectedYardageMode = "Advanced";
                                });
                              },
                              child: Container(
                                width: 70,
                                height: 20,
                                child: Text(
                                  "Advanced".toUpperCase(),
                                  style: GoogleFonts.aBeeZee(
                                      fontWeight: FontWeight.bold,
                                      fontSize: 12,
                                      color: colorAdvanced),
                                ),
                              ),
                            ),
                          ),
                        ],
                      ),
                    ),
                    Visibility(
                      visible: IsVisiblityBasicUI,
                      // child: YardageBasicUI(onSelected!,InchCmType!,
                      //     _spFabricInchList,_spFabricCmList)
                      child: Column(
                        children: [
                          Padding(
                            padding: const EdgeInsets.only(
                                left: 15.0, top: 10),
                            child: Align(
                                alignment: Alignment.centerLeft,
                                child: Text(
                                  'Fabric Width',
                                  textAlign: TextAlign.center,
                                  style: GoogleFonts.aBeeZee(
                                      fontWeight: FontWeight.bold,
                                      fontSize: 12,
                                      color: Colors.black),
                                )),
                          ),
                          Visibility(
                            visible: IsVisiblityFBWidthCm,
                            child: Padding(
                              padding: const EdgeInsets.only(
                                  left: 10.0,
                                  bottom: 8,
                                  top: 10,
                                  right: 10),
                              child: Container(
                                height: 35,
                                decoration: BoxDecoration(
                                    borderRadius:
                                    BorderRadius.circular(10),
                                    border: Border.all(
                                        color: Colors.grey,
                                        width: 1)),
                                width: double.infinity,
                                child: DropdownButton(
                                  isExpanded: true,
                                  underline: Container(
                                    decoration: ShapeDecoration(
                                      shape: RoundedRectangleBorder(),
                                    ),
                                  ),
                                  value: onSelected,
                                  items: _spFabricCmList
                                      .map<DropdownMenuItem<String>>(
                                          (String e) {
                                        return DropdownMenuItem<String>(
                                          value: e,
                                          child: Padding(
                                            padding:
                                            const EdgeInsets.only(
                                                left: 8.0),
                                            child: Text(
                                              e,
                                              style: TextStyle(
                                                  color: Colors.black,
                                                  fontSize: 14),
                                            ),
                                          ),
                                        );
                                      }).toList(),
                                  onChanged: (String? value) {
                                    setState(() {
                                      onSelected = value!;
                                    });
                                  },
                                ),
                              ),
                            ),
                          ),
                          Visibility(
                            visible: IsVisiblityFBWidthInch,
                            child: Padding(
                              padding: const EdgeInsets.only(
                                  left: 10.0,
                                  bottom: 8,
                                  top: 10,
                                  right: 10),
                              child: Container(
                                height: 35,
                                decoration: BoxDecoration(
                                    borderRadius:
                                    BorderRadius.circular(10),
                                    border: Border.all(
                                        color: Colors.grey,
                                        width: 1)),
                                width: double.infinity,
                                child: DropdownButton(
                                  isExpanded: true,
                                  underline: Container(
                                    decoration: ShapeDecoration(
                                      shape: RoundedRectangleBorder(),
                                    ),
                                  ),
                                  value: onSelectedInchCm,
                                  items: _spFabricInchList
                                      .map<DropdownMenuItem<String>>(
                                          (String e) {
                                        return DropdownMenuItem<String>(
                                          value: e,
                                          child: Padding(
                                            padding:
                                            const EdgeInsets.only(
                                                left: 8.0),
                                            child: Text(
                                              e,
                                              style: TextStyle(
                                                  color: Colors.black,
                                                  fontSize: 14),
                                            ),
                                          ),
                                        );
                                      }).toList(),
                                  onChanged: (String? value) {
                                    setState(() {
                                      onSelectedInchCm = value!;
                                    });
                                  },
                                ),
                              ),
                            ),
                          ),
                        ],
                      ),
                    ),

                    //  Yardage Calculator Mode

                    Visibility(
                      visible: IsVisiblityAdvanceUI,
                      // child: YardageAdvanceUI(onSelectedMFabric!,InchCmType!,
                      //     _spMainFabricCmList,_spMainFabricInchList,
                      //     _spFusibleinterfacingCmList,Fusibleinterfacing!,
                      //     _spFusibleinterfacingInchList)
                      child: Column(
                        children: [
                          Padding(
                            padding: const EdgeInsets.only(
                                left: 15.0, top: 10),
                            child: Align(
                                alignment: Alignment.centerLeft,
                                child: Text(
                                  'Main Fabric ',
                                  textAlign: TextAlign.center,
                                  style: GoogleFonts.aBeeZee(
                                      fontWeight: FontWeight.bold,
                                      fontSize: 12,
                                      color: Colors.black),
                                )),
                          ),
                          //Main Fabric Cm
                          Visibility(
                            visible: IsVisiblityMainFBCm,
                            child: Padding(
                              padding: const EdgeInsets.only(
                                  left: 10.0,
                                  bottom: 8,
                                  top: 10,
                                  right: 10),
                              child: Container(
                                height: 35,
                                decoration: BoxDecoration(
                                    borderRadius:
                                    BorderRadius.circular(10),
                                    border: Border.all(
                                        color: Colors.grey,
                                        width: 1)),
                                width: double.infinity,
                                child: DropdownButton(
                                  isExpanded: true,
                                  underline: Container(
                                    decoration: ShapeDecoration(
                                      shape: RoundedRectangleBorder(),
                                    ),
                                  ),
                                  value: onSelectedMFabricCm,
                                  items: _spMainFabricCmList
                                      .map<DropdownMenuItem<String>>(
                                          (String e) {
                                        return DropdownMenuItem<String>(
                                          value: e,
                                          child: Padding(
                                            padding:
                                            const EdgeInsets.only(
                                                left: 8.0),
                                            child: Text(
                                              e,
                                              style: TextStyle(
                                                  color: Colors.black,
                                                  fontSize: 14),
                                            ),
                                          ),
                                        );
                                      }).toList(),
                                  onChanged: (String? value) {
                                    setState(() {
                                      onSelectedMFabricCm = value!;
                                    });
                                  },
                                ),
                              ),
                            ),
                          ),
                          //Main Fabric Inch
                          Visibility(
                            visible: IsVisiblityMainFBInch,
                            child: Padding(
                              padding: const EdgeInsets.only(
                                  left: 10.0,
                                  bottom: 8,
                                  top: 10,
                                  right: 10),
                              child: Container(
                                height: 35,
                                decoration: BoxDecoration(
                                    borderRadius:
                                    BorderRadius.circular(10),
                                    border: Border.all(
                                        color: Colors.grey,
                                        width: 1)),
                                width: double.infinity,
                                child: DropdownButton(
                                  isExpanded: true,
                                  underline: Container(
                                    decoration: ShapeDecoration(
                                      shape: RoundedRectangleBorder(),
                                    ),
                                  ),
                                  value: onSelectedMFabricInch,
                                  items: _spMainFabricInchList
                                      .map<DropdownMenuItem<String>>(
                                          (String e) {
                                        return DropdownMenuItem<String>(
                                          value: e,
                                          child: Padding(
                                            padding:
                                            const EdgeInsets.only(
                                                left: 8.0),
                                            child: Text(
                                              e,
                                              style: TextStyle(
                                                  color: Colors.black,
                                                  fontSize: 14),
                                            ),
                                          ),
                                        );
                                      }).toList(),
                                  onChanged: (String? value) {
                                    setState(() {
                                      onSelectedMFabricInch = value!;
                                    });
                                  },
                                ),
                              ),
                            ),
                          ),

                          //Fusible interfacing
                          Padding(
                            padding: const EdgeInsets.only(
                                left: 15.0, top: 10),
                            child: Align(
                                alignment: Alignment.centerLeft,
                                child: Text(
                                  'Fusible interfacing',
                                  textAlign: TextAlign.center,
                                  style: GoogleFonts.aBeeZee(
                                      fontWeight: FontWeight.bold,
                                      fontSize: 12,
                                      color: Colors.black),
                                )),
                          ),

                          //Fusible Cm
                          Visibility(
                            visible: IsVisiblityFusibleCm,
                            child: Padding(
                              padding: const EdgeInsets.only(
                                  left: 10.0,
                                  bottom: 8,
                                  top: 10,
                                  right: 10),
                              child: Container(
                                height: 35,
                                decoration: BoxDecoration(
                                    borderRadius:
                                    BorderRadius.circular(10),
                                    border: Border.all(
                                        color: Colors.grey,
                                        width: 1)),
                                width: double.infinity,
                                child: DropdownButton(
                                  isExpanded: true,
                                  underline: Container(
                                    decoration: ShapeDecoration(
                                      shape: RoundedRectangleBorder(),
                                    ),
                                  ),
                                  value: FusibleinterfacingCm,
                                  items: _spFusibleinterfacingCmList
                                      .map<DropdownMenuItem<String>>(
                                          (String e) {
                                        return DropdownMenuItem<String>(
                                          value: e,
                                          child: Padding(
                                            padding:
                                            const EdgeInsets.only(
                                                left: 8.0),
                                            child: Text(
                                              e,
                                              style: TextStyle(
                                                  color: Colors.black,
                                                  fontSize: 14),
                                            ),
                                          ),
                                        );
                                      }).toList(),
                                  onChanged: (String? value) {
                                    setState(() {
                                      FusibleinterfacingCm = value!;
                                    });
                                  },
                                ),
                              ),
                            ),
                          ),
                          //Fusible Inch
                          Visibility(
                            visible: IsVisiblityFusibleInch,
                            child: Padding(
                              padding: const EdgeInsets.only(
                                  left: 10.0,
                                  bottom: 8,
                                  top: 10,
                                  right: 10),
                              child: Container(
                                height: 35,
                                decoration: BoxDecoration(
                                    borderRadius:
                                    BorderRadius.circular(10),
                                    border: Border.all(
                                        color: Colors.grey,
                                        width: 1)),
                                width: double.infinity,
                                child: DropdownButton(
                                  isExpanded: true,
                                  underline: Container(
                                    decoration: ShapeDecoration(
                                      shape: RoundedRectangleBorder(),
                                    ),
                                  ),
                                  value: FusibleinterfacingInch,
                                  items: _spFusibleinterfacingInchList
                                      .map<DropdownMenuItem<String>>(
                                          (String e) {
                                        return DropdownMenuItem<String>(
                                          value: e,
                                          child: Padding(
                                            padding:
                                            const EdgeInsets.only(
                                                left: 8.0),
                                            child: Text(
                                              e,
                                              style: TextStyle(
                                                  color: Colors.black,
                                                  fontSize: 14),
                                            ),
                                          ),
                                        );
                                      }).toList(),
                                  onChanged: (String? value) {
                                    setState(() {
                                      FusibleinterfacingInch = value!;
                                    });
                                  },
                                ),
                              ),
                            ),
                          ),
                        ],
                      ),
                    ),

                    Container(
                      height: 60,
                      child: Row(
                        children: [
                          Spacer(),
                          GestureDetector(
                            onTap: () {
                              if (_selectedYardageMode == "Basic") {
                                if (onSelected != 'Choose Fabric Width') {
                                  var basicfabric = new Map();
                                  if (_selectedYardageMode == "Basic") {
                                    if (InchCmType == 'Inch') {
                                      basicfabric["FabricWidth"] = onSelectedInchCm;
                                      basicfabric["InchCmType"] = InchCmType;
                                      SharePreferenceManager.instance.setDropDownFabricWidthInch("FabricWidthInch",
                                          onSelectedInchCm!);
                                    } else if (InchCmType == 'Cm') {
                                      basicfabric["FabricWidth"] = onSelected;
                                      basicfabric["InchCmType"] = InchCmType;
                                      SharePreferenceManager.instance.setDropDownFabricWidthCm("FabricWidthCm",
                                          onSelected!);
                                    }
                                  }
                                  SharePreferenceManager.instance.setYardageData("Yardage",
                                      basicfabric.toString());
                                  Navigator.of(context).pop();
                                } else if (onSelectedInchCm != 'Choose Fabric Width') {
                                  var basicfabric = new Map();
                                  if (_selectedYardageMode == "Basic") {
                                    if (InchCmType == 'Inch') {
                                      basicfabric["FabricWidth"] = onSelectedInchCm;
                                      basicfabric["InchCmType"] = InchCmType;
                                      SharePreferenceManager.instance.setDropDownFabricWidthInch("FabricWidthInch",
                                          onSelectedInchCm!);
                                    } else if (InchCmType == 'Cm') {
                                      basicfabric["FabricWidth"] = onSelected;
                                      basicfabric["InchCmType"] = InchCmType;
                                      SharePreferenceManager.instance.setDropDownFabricWidthCm("FabricWidthCm",
                                          onSelected!);
                                    }
                                  }
                                  SharePreferenceManager.instance.setYardageData("Yardage", basicfabric.toString());
                                  Navigator.of(context).pop();
                                } else {
                                  Fluttertoast.showToast(msg: "Please choose fabric width");
                                }
                              } else if (_selectedYardageMode == "Advanced") {
                                if (onSelectedMFabricCm !=
                                    'Choose Main Fabric') {
                                  if (FusibleinterfacingCm !=
                                      'Choose Fusible Interfacing') {
                                    var advancedfabric = new Map();
                                    if (_selectedYardageMode ==
                                        "Advanced") {
                                      if (InchCmType == 'Inch') {
                                        advancedfabric["MainFabric"] = onSelectedMFabricInch;
                                        advancedfabric["Fusibleinterfacing"] = FusibleinterfacingInch;
                                        advancedfabric["InchCmType"] = InchCmType;
                                        SharePreferenceManager.instance.setDropDownFabricWidthInch(
                                            "MainFabricInch", onSelectedMFabricInch!);
                                        SharePreferenceManager.instance.setDropDownFusibleinterfacingCm("FusibleinterfacingInch",
                                            FusibleinterfacingInch!);
                                      } else if (InchCmType == 'Cm') {
                                        advancedfabric["MainFabric"] = onSelectedMFabricCm;
                                        advancedfabric["Fusibleinterfacing"] = FusibleinterfacingCm;
                                        advancedfabric["InchCmType"] = InchCmType;
                                        SharePreferenceManager.instance.setDropDownFabricWidthCm("MainFabricCm",
                                            onSelectedMFabricCm!);
                                        SharePreferenceManager.instance.setDropDownFusibleinterfacingCm(
                                            "FusibleinterfacingCm", FusibleinterfacingCm!);
                                      }
                                      SharePreferenceManager.instance.setYardageData("Yardage", 
                                      advancedfabric.toString());
                                      Navigator.of(context).pop();
                                    }
                                  } else {
                                    Fluttertoast.showToast(
                                        msg:
                                        "Please choose fusible interfacing");
                                  }
                                } else {
                                  Fluttertoast.showToast(
                                      msg:
                                      "Please choose main fabric");
                                }
                              }
                            },
                            child: Container(
                              height: 45,
                              width: 110,
                              decoration: new BoxDecoration(
                                color: Colors.black,
                                borderRadius:
                                BorderRadius.circular(8),
                              ),
                              child: Center(
                                child: Text(
                                  'Submit',
                                  style: GoogleFonts.aBeeZee(
                                      color: Colors.white),
                                ),
                              ),
                            ),
                          ),
                          GestureDetector(
                            onTap: () {
                              setState(() {
                                IsVisiblityFBWidthCm = false;
                                IsVisiblityFBWidthInch = true;
                                IsVisiblityMainFBCm = false;
                                IsVisiblityMainFBInch = true;
                                IsVisiblityFusibleCm = false;
                                IsVisiblityFusibleInch = true;
                                _selectedYardageIncm = "Inch";
                                InchCmType = "Inch";
                                colorCm = Colors.black;
                                colorInch = Colors.orange;
                              });
                            },
                            child: Container(
                              width: 60,
                              height: 60,
                              color: Colors.white,
                              child: Center(
                                child: Align(
                                  alignment: Alignment.centerRight,
                                  child: Text(
                                    "Inch",
                                    style: GoogleFonts.aBeeZee(
                                        color: colorInch,
                                        fontWeight: FontWeight.w800,
                                        fontSize: 12),
                                  ),
                                ),
                              ),
                            ),
                          ),
                          Padding(
                            padding:
                            const EdgeInsets.only(right: 15.0),
                            child: GestureDetector(
                              onTap: () {
                                setState(() {
                                  IsVisiblityFBWidthCm = true;
                                  IsVisiblityFBWidthInch = false;
                                  IsVisiblityMainFBCm = true;
                                  IsVisiblityMainFBInch = false;
                                  IsVisiblityFusibleCm = true;
                                  IsVisiblityFusibleInch = false;
                                  _selectedYardageIncm = "Cm";
                                  InchCmType = "Cm";
                                  colorCm = Colors.orange;
                                  colorInch = Colors.black;

                                });
                              },
                              child: Container(
                                width: 60,
                                height: 60,
                                color: Colors.white,
                                child: Center(
                                  child: Align(
                                    alignment: Alignment.centerRight,
                                    child: Text(
                                      "Cm",
                                      style: GoogleFonts.aBeeZee(
                                          color: colorCm,
                                          fontWeight: FontWeight.w800,
                                          fontSize: 12),
                                    ),
                                  ),
                                ),
                              ),
                            ),
                          ),
                        ],
                      ),
                    )
                  ],
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}





class ItemSelector extends StatelessWidget {
  final String image;
  final String name;
  final Function()? onPress;

  const ItemSelector(
      {Key? key, required this.image, required this.name, this.onPress})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      child: Container(
        padding: EdgeInsets.symmetric(vertical: 4, horizontal: 4),
        margin: EdgeInsets.symmetric(vertical: 4, horizontal: 4),
        child: Column(
          children: [
            Image.network(image,
              fit: BoxFit.fitWidth,
              height: 40,
            ),
            Expanded(
                child: Center(
              child: Padding(
                padding: const EdgeInsets.only(top: 3.0),
                child: Text(
                  name.capitalizeFirst.toString(),
                  textAlign: TextAlign.center,
                  style: GoogleFonts.roboto(fontSize: 10, color: Colors.black),
                ),
              ),
            ))
          ],
        ),
        decoration: BoxDecoration(
            color: Colors.white,
            border: Border.all(color: Colors.black26, width: 1),
            borderRadius: BorderRadius.circular(8)),
      ),
      onTap: onPress,
    );
  }
}

class PositionItem {
  final String image;
  final double left;
  final double top;

  PositionItem({required this.image, required this.left, required this.top});
}

class ColorSelector extends StatelessWidget {
  final String color, colorName;
  final Function()? onPress;

  const ColorSelector(
      {Key? key, required this.color, required this.colorName, this.onPress})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      child: Container(
        margin: EdgeInsets.all(4),
        width: 64,
        height: 64,
        decoration: BoxDecoration(
            borderRadius: BorderRadius.circular(8),
            border: Border.all(color: Colors.black26, width: 1)
            // borderRadius: BorderRadius.circular(38)
            ),
        child: Column(
          children: [
            Container(
              width: 64,
              height: 52,
              decoration: BoxDecoration(
                  color: _hexToColor(color),
                  borderRadius: BorderRadius.circular(8)),
            ),
            Padding(
              padding: const EdgeInsets.only(top: 3.0),
              child: Text(
                colorName,
                style: GoogleFonts.roboto(fontSize: 10),
              ),
            )
          ],
        ),
      ),
      onTap: onPress,
    );
  }

  Color _hexToColor(String hex) {
    assert(RegExp(r'^#([0-9a-fA-F]{6})|([0-9a-fA-F]{8})$').hasMatch(hex),
        'hex color must be #rrggbb or #rrggbbaa');

    return Color(
      int.parse(hex.substring(1), radix: 16) +
          (hex.length == 7 ? 0xff000000 : 0x00000000),
    );
  }
}

class MaskedImage extends StatelessWidget {
  final String asset;
  final String mask;

  MaskedImage({required this.asset, required this.mask});

  @override
  Widget build(BuildContext context) {
    return LayoutBuilder(builder: (context, constraints) {
      return FutureBuilder<List>(
        future: _createShaderAndImage(
            asset, mask, constraints.maxWidth, constraints.maxHeight),
        builder: (context, snapshot) {
          if (!snapshot.hasData) return const SizedBox.shrink();
          return ShaderMask(
            blendMode: BlendMode.dstATop,
            shaderCallback: (rect) => snapshot.data![0],
            child: snapshot.data![1],
          );
        },
      );
    });
  }

  Future<List> _createShaderAndImage(
      String asset, String mask, double w, double h) async {
    return _createNetworkShaderAndImage(asset, mask, w, h);
    /**
        ByteData data = await rootBundle.load(asset);
        ByteData maskData = await rootBundle.load(mask);


        Codec codec = await instantiateImageCodec(maskData.buffer.asUint8List(), targetWidth: w.toInt(), targetHeight: h.toInt());
        FrameInfo fi = await codec.getNextFrame();

        ImageShader shader = ImageShader(fi.image, TileMode.clamp, TileMode.clamp, Matrix4.identity().storage);
        Image image = Image.memory(data.buffer.asUint8List(), fit: BoxFit.cover, width: w, height: h);
        return [shader, image];
     **/
  }

  Future<List> _createNetworkShaderAndImage(
      String asset, String mask, double w, double h) async {
    // ByteData data = await rootBundle.load(asset);
    // ByteData maskData = await rootBundle.load(mask);

    Uint8List data = await loadImageByteData(asset);
    Uint8List maskData = await loadImageByteData(mask);

    Codec codec = await instantiateImageCodec(maskData,
        targetWidth: w.toInt(), targetHeight: h.toInt());
    FrameInfo fi = await codec.getNextFrame();

    ImageShader shader = ImageShader(
        fi.image, TileMode.clamp, TileMode.clamp, Matrix4.identity().storage);
    Image image = Image.memory(data, fit: BoxFit.cover, width: w, height: h);
    return [shader, image];
  }
}

class SavedItem {
  final int index;
  final String? fabric;
  final String? color;
  final int key;
  final SubAttributeData? data;

  SavedItem({
    required this.index,
    this.fabric,
    this.color,
    required this.key,
    required this.data,
  });
}

class FinalSavedItem {
  final int index;
  final String? fabric;
  final String? color;
  final int key;
  final SubAttributeData? data;

  FinalSavedItem({
    required this.index,
    this.fabric,
    this.color,
    required this.key,
    required this.data,
  });
}
