
import 'dart:io';
import 'package:appgallery/Utils/dailog.dart';
import 'package:appgallery/Utils/routes.dart';
import 'package:http/http.dart'as http;
import 'package:appgallery/CommonUI/commonwidget.dart';
import 'package:appgallery/Resources/color.dart';
import 'package:appgallery/Resources/constants.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_icons/flutter_icons.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:image_picker/image_picker.dart';

import '../../../CommonUI/progressdialog.dart';
import '../../../Model/allconsoleappsmodel.dart';
import '../../../apis/apirepositary.dart';
import 'developerconsoleactivity.dart';


class EditAppsDetailsActivity extends StatefulWidget {
  final String appId,appName,appIcon,featureGraphic,appShortDescription,appFullDescription;
  List<ScreenShot> screenShotsList=[];
   EditAppsDetailsActivity({Key? key,required this.appId,required this.appName,
  required this.screenShotsList,required this.appIcon,required this.featureGraphic,
   required this.appShortDescription,required this.appFullDescription}) : super(key: key);

  @override
  State<EditAppsDetailsActivity> createState() =>
      _EditAppsDetailsActivityState();
}

class _EditAppsDetailsActivityState extends State<EditAppsDetailsActivity> {

 final ImagePicker imagePicker=ImagePicker();
  List<XFile>imageFileList=[];
  PickedFile? imgAppIconfile;
  var controllerAppName=TextEditingController();
  var controllerShortDescription=TextEditingController();
  var controllerFullDescription=TextEditingController();
  PickedFile? imgFeatureIconfile;
  bool _setImg=false;

  var appIconImg,featureIconImg,screenShotImg;

  @override
  void initState() {
    setState(() {
      controllerAppName.text=widget.appName;
      controllerFullDescription.text=widget.appFullDescription;
      controllerShortDescription.text=widget.appShortDescription;
      if(widget.screenShotsList.isNotEmpty){
        _setImg=true;
      }
    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: appGreyColor,
      appBar: AppBar(
        iconTheme: const IconThemeData(
          color: Colors.black, //change your color here
        ),
        backgroundColor: appGreyColor,
        title: Text(
          'App Details',
          style: GoogleFonts.aBeeZee(fontSize: 16.0, color: appBlackColor),
        ),
      ),
      body: ListView(
        children: [
           Padding(
            padding: const EdgeInsets.only(top: 12.0, left: 10, right: 10),
            child: AppDetailsTextField(
              controller: controllerAppName,
              label: 'App Name *',
              borderWidth: 1.0,
              borderRadius: 10,
              maxLength: 50,
              inputLength: 50,
              maxLines: 1,
            ),
          ),
           Padding(
            padding: const EdgeInsets.only(top: 0.0, left: 10, right: 10),
            child: AppDetailsTextField(
              controller: controllerShortDescription,
              label: 'Short Description *',
              borderWidth: 1.0,
              borderRadius: 10,
              maxLength: 80,
              inputLength: 80,
              maxLines: 1,
            ),
          ),
           Padding(
            padding: const EdgeInsets.only(top: 0.0, left: 10, right: 10),
            child: AppDetailsTextField(
              controller: controllerFullDescription,
              label: 'Full Description *',
              borderWidth: 1.0,
              borderRadius: 10,
              maxLength: 1000,
              inputLength: 1000,
              maxLines: 8,
            ),
          ),
          const Padding(
            padding: EdgeInsets.only(left: 15.0),
            child: Align(
                alignment: Alignment.centerLeft,
                child: Text(
                  'Graphics',
                  style: TextStyle(fontSize: 17),
                )),
          ),
          const Padding(
            padding: EdgeInsets.only(left: 15.0, top: 15),
            child: Align(
                alignment: Alignment.centerLeft,
                child: Text(
                  'Manage your app icon, feature graphic, and screenshots to promote your app on App Gallery. Review the content guidelines before uploading new graphics. If you add translations for your store listing without localized graphics, we will use the graphics from your default language.',
                  style: TextStyle(fontSize: 13, color: Colors.black45),
                )),
          ),
          Padding(
            padding: const EdgeInsets.only(left: 15.0, top: 15),
            child: Align(
                alignment: Alignment.centerLeft,
                child: Text(
                  'App Icon *',
                  style: GoogleFonts.aBeeZee(
                      fontSize: 16.0, color: appBlackColor),
                )),
          ),
          appSelectIconUI(),
          Padding(
            padding: const EdgeInsets.only(left: 14.0),
            child: Text(
              'Your app icon must be a transparent PNG or JPEG, up to 1 MB, 512 px by 512 px',
              style: textStyleTitle(fontSize: 13.0, color: Colors.grey[600]),
            ),
          ),
          Padding(
            padding: const EdgeInsets.only(left: 15.0, top: 15),
            child: Align(
                alignment: Alignment.centerLeft,
                child: Text(
                  'Feature graphic *',
                  style: GoogleFonts.aBeeZee(
                      fontSize: 16.0, color: appBlackColor),
                )),
          ),
          featureGraphicIconUI(),
          Padding(
            padding: const EdgeInsets.only(left: 14.0),
            child: Text(
              'Your app icon must be a transparent PNG or JPEG, up to 1 MB, 1024 px by 500 px',
              style: textStyleTitle(fontSize: 13.0, color: Colors.grey[600]),
            ),
          ),
          Padding(
            padding: const EdgeInsets.only(left: 15.0, top: 15),
            child: Align(
                alignment: Alignment.centerLeft,
                child: Text(
                  'Phone screenshots *',
                  style: GoogleFonts.aBeeZee(
                      fontSize: 16.0, color: appBlackColor),
                )),
          ),
          phoneScreenshotsUI(),
          Padding(
            padding: const EdgeInsets.only(left: 14.0),
            child: Text(
              'Upload 2-8 phone screenshots. Screenshots must be PNG or JPEG, up to 8 MB each, 16:9 or 9:16 aspect ratio, with each side between 320 px and 3,840 px',
              style: textStyleTitle(fontSize: 13.0, color: Colors.grey[600]),
            ),
          ),

          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              SaveButton(
                buttontextcolor: appBlackColor,
                  buttoncolors: 0xfff4f2f2,
                  onPressed: (){
                  Navigator.pop(context);
              }, buttonName: 'Discard changes'),
              SaveButton(
                buttontextcolor: appWhiteColor,
                  buttoncolors: 0xff0049A0,
                  onPressed: (){
                  saveApplicationInfo();
              }, buttonName: 'Save'),
            ],
          ),
          const SizedBox(height: 40,)

        ],
      ),
    );
  }

  Widget appSelectIconUI() {
    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: GestureDetector(
        onTap: (){
          selectAppIconImages();
        },
        child: ContainerBgWithRadius(
            child: imgAppIconfile==null?_setImg==true?Padding(
              padding: const EdgeInsets.fromLTRB(108.0,8,99,8),
              child: ClipRRect(
                borderRadius: const BorderRadius.all(Radius.circular(14)),
                child: Image.network(widget.appIcon,
                  fit: BoxFit.fitHeight,),
              ),
            ):Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                const Icon(
                  Ionicons.md_photos,
                  size: 60,
                  color: Colors.black45,
                ),
                const Padding(
                  padding: EdgeInsets.all(8.0),
                  child: Text('Choose a PNG or JPEG file here to upload'),
                ),
                Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: Image.asset(
                    'assets/upload.png',
                    width: 20,
                    height: 20,
                  ),
                )
              ],
            ):Padding(
              padding: const EdgeInsets.all(8.0),
              child: ClipRRect(
                borderRadius: const BorderRadius.all(Radius.circular(4)),
                child: Image.file(File(imgAppIconfile!.path),
                  fit: BoxFit.fitHeight,),
              ),
            ),
            height: 250,
            circular: 5,
            color: appWhiteColor,
            width: double.infinity),
      ),
    );
  }

  Widget featureGraphicIconUI() {
    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: ContainerBgWithRadius(
          child: GestureDetector(
            onTap: (){
              selectFeatureImageFile();
            },
            child: imgFeatureIconfile==null?_setImg==true?Padding(
              padding: const EdgeInsets.only(left:80.0,right: 80,top: 4,bottom: 6),
              child: ClipRRect(
                borderRadius: const BorderRadius.all(Radius.circular(10)),
                child: Image.network(widget.featureGraphic,
                  fit: BoxFit.fill,),
              ),
            ):Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                const Icon(
                  Ionicons.md_photos,
                  size: 60,
                  color: Colors.black45,
                ),
                const Padding(
                  padding: EdgeInsets.all(8.0),
                  child: Text('Choose a PNG or JPEG file here to upload'),
                ),
                Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: Image.asset(
                    'assets/upload.png',
                    width: 20,
                    height: 20,
                  ),
                )
              ],
            ):Padding(
              padding: const EdgeInsets.all(8.0),
              child: ClipRRect(
                borderRadius: const BorderRadius.all(Radius.circular(4)),
                child: Image.file(File(imgFeatureIconfile!.path),
                  fit: BoxFit.fitHeight,),
              ),
            ),
          ),
          height: 250,
          circular: 5,
          color: appWhiteColor,
          width: double.infinity),
    );
  }

  Widget phoneScreenshotsUI() {
    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: ContainerBgWithRadius(
          child:imageFileList.isEmpty?widget.screenShotsList.isEmpty?GestureDetector(
            onTap: (){
              selectMultipleImages();
            },
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                const Icon(
                  Ionicons.md_photos,
                  size: 60,
                  color: Colors.black45,
                ),
                const Padding(
                  padding: EdgeInsets.all(8.0),
                  child: Text('Choose a PNG or JPEG file here to upload'),
                ),
                Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: Image.asset(
                    'assets/upload.png',
                    width: 20,
                    height: 20,
                  ),
                )
              ],
            ),
          ):Column(
            mainAxisSize: MainAxisSize.max,
            children: [
              Expanded(
                flex: 60,
                child: Padding(
                  padding: const EdgeInsets.only(left:4.0),
                  child: GridView.builder(
                    physics: const NeverScrollableScrollPhysics(),
                      gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                          crossAxisCount: 4,
                          mainAxisExtent: 125
                      ),itemCount: widget.screenShotsList.length ??0, itemBuilder: (ctx,index){
                    return SizedBox(
                      height: 140,width: 90,
                      child: Stack(
                        children: [
                          const SizedBox(height: 140,width: 90,),
                          Padding(
                            padding: const EdgeInsets.only(top:18.0),
                            child: ContainerBgWithRadius(
                              height: 120,width: 85,circular: 4,
                              child: ClipRRect(
                                  borderRadius: const BorderRadius.all(Radius.circular(4)),
                                  child: Image.network(widget.screenShotsList[index]
                                      .filename, fit: BoxFit.fill,)),
                            ),
                          ),
                          // ElevatedButton.icon(onPressed: (){
                          //
                          // }, icon: const Icon(Icons.cancel,color: Colors.white),
                          //   style: ElevatedButton.styleFrom(
                          //     shape: const CircleBorder(),
                          //   primary: Colors.red,
                          // ),
                          //     label: const Text(''),),
                        ],
                      ),
                    );
                  }),
                ),
              ),
              Expanded(
                flex: 10,
                child: Align(
                    alignment: Alignment.centerRight,
                    child: IconButton(onPressed: (){
                      selectMultipleImages();
                    }, icon: const Icon(Icons.upload_rounded,size: 24,))),
              )
            ],
          ):
          Column(
            children: [
              Expanded(
                flex: 70,
                child: GridView.builder(
                    physics: const NeverScrollableScrollPhysics(),
                    gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                        crossAxisCount: 4,
                      mainAxisExtent: 125
                    ),itemCount: imageFileList.length ??0,
                    itemBuilder: (ctx,index){
                  return Padding(
                    padding: const EdgeInsets.only(left:4.0),
                    child: SizedBox(
                      height: 140,width: 90,
                      child: Stack(
                        children: [
                          const SizedBox(height: 140,width: 90,),
                          Padding(
                            padding: const EdgeInsets.only(top:18.0),
                            child: ContainerBgWithRadius(
                              height: 120,width: 80,circular: 4,
                              child: ClipRRect(
                                  borderRadius: const BorderRadius.all(Radius.circular(4)),
                                  child: Image.file(File(imageFileList[index].path),fit: BoxFit.fill,)),
                            ),
                          ),
                         // ElevatedButton.icon(onPressed: (){
                         //
                         // }, icon: const Icon(Icons.cancel,color: Colors.white),
                         //   style: ElevatedButton.styleFrom(
                         //     shape: const CircleBorder(),
                         //   primary: Colors.red,
                         // ),
                         //     label: const Text(''),),
                          Align(
                            alignment: Alignment.topRight,
                            child: GestureDetector(
                              onTap: (){
                                setState(() {
                                  imageFileList.removeAt(index);
                                });
                              },
                              child: Container(
                                width: 40,
                                height: 38,
                                alignment: Alignment.center,
                                decoration: const BoxDecoration(shape: BoxShape.circle),
                                child:  const Icon(Icons.cancel,color: Colors.red,size: 25,),
                              ),
                            ),
                          )
                        ],
                      ),
                    ),
                  );
                }),
              ),
              Expanded(
                flex: 10,
                child: Align(
                    alignment: Alignment.centerRight,
                    child: IconButton(onPressed: (){
                      selectMultipleImages();
                    }, icon:  Image.asset('assets/photo.png',height: 40,width: 35,))),
              )
            ],
          ),
          height: 305,
          circular: 5,
          color: appWhiteColor,
          width: double.infinity),
    );
  }

  void selectMultipleImages() async{
    final List<XFile>? selectedImages=await imagePicker.pickMultiImage(imageQuality: 60);
    setState(() {
      if(selectedImages!.isNotEmpty){
        imageFileList.addAll(selectedImages);
      }
    });
  }

  void selectAppIconImages() async{
    PickedFile? imgFiles=await ImagePicker.platform.pickImage(source: ImageSource.gallery,
        imageQuality: 60);
    setState(() {
      imgAppIconfile=imgFiles!;
    });
  }

  void selectFeatureImageFile()async{
    PickedFile? imgFiles=await ImagePicker.platform.pickImage(source: ImageSource.gallery,
        imageQuality: 60);
    setState(() {
      imgFeatureIconfile=imgFiles!;
    });
  }

  void saveApplicationInfo() {
    showDialog(context: context, builder: (context) => ProgressDialog(),
        barrierDismissible: false).then((value){
      print(value);
    },);
    if(imgAppIconfile!=null){
      uploadAppInfoAPI(widget.appId,controllerAppName.text, controllerShortDescription.text,
          controllerFullDescription.text, imgAppIconfile, imgFeatureIconfile,imageFileList);
    }else if(imgFeatureIconfile!=null) {
      uploadAppInfoAPI(widget.appId,controllerAppName.text, controllerShortDescription.text,
          controllerFullDescription.text, imgAppIconfile, imgFeatureIconfile,imageFileList);
    }else if(imageFileList.isNotEmpty){
      uploadAppInfoAPI(widget.appId,controllerAppName.text, controllerShortDescription.text,
          controllerFullDescription.text, imgAppIconfile, imgFeatureIconfile,imageFileList);
    } else{
      Map<String,dynamic>body={
        "app_id":widget.appId,
        "app_name":controllerAppName.text,
        "app_short_description":controllerShortDescription.text,
        "app_full_description":controllerFullDescription.text,
      };
      normalUploadAppInfoAPI(body).then((value){
        navReturn(context);
        if(value.status){
          Fluttertoast.showToast(msg: value.message.toString());
          navPush(context, const DeveloperConsoleActivity());
        }else{
          Fluttertoast.showToast(msg: value.message.toString());
        }
      });
    }
  }


 uploadAppInfoAPI(String appID,String appName,String appShortDescription,
     String appFullDescription,PickedFile? imgAppIconFile,PickedFile? imgAppFuatureGraphinc,
     List<XFile> imageFileList) async{
   var ApiUrls=Uri.parse(uploadAppInfoUrl);
   var request=http.MultipartRequest("POST",ApiUrls);
   request.fields["app_id"]=appID;
   request.fields['app_name']=appName;
   request.fields['app_short_description']=appShortDescription;
   request.fields['app_full_description']=appFullDescription;

   if( imgAppIconFile!=null) {
     appIconImg = await http.MultipartFile.fromPath("app_icon",
         imgAppIconFile!.path);
     request.files.add(appIconImg);
   }
   if(imgAppFuatureGraphinc!=null) {
     featureIconImg = await http.MultipartFile.fromPath("app_graphic",
         imgAppFuatureGraphinc!.path);
     request.files.add(featureIconImg);
   }
   if(imageFileList.isNotEmpty){
     for(var element in imageFileList){
       screenShotImg=await http.MultipartFile.fromPath("screen_shot",
           element!.path);
       request.files.add(screenShotImg);
     }
   }
   var response = await request.send();
   if(response.statusCode==201) {
     navReturn(context);
     var responseData = await response.stream.toBytes();
     var responseString = String.fromCharCodes(responseData);
     var res=responseString.replaceAll("{", "").replaceAll("}", "");
     var res1=res.split(",");
     var message=res1[0].split(":");
     Fluttertoast.showToast(msg: message[1].replaceAll('"', '').replaceAll('"', '').toString().trim());
     navPush(context, const DeveloperConsoleActivity());
   }
 }
}
