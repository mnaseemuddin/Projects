import 'dart:io';
import 'package:edge_alerts/edge_alerts.dart';
import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:http/http.dart' as http;
import 'package:flutter/material.dart';
import 'dart:io';
import 'dart:async';
import 'package:extended_image/extended_image.dart';
import 'package:path/path.dart';
import 'package:async/async.dart';
import 'package:http/http.dart' as http;
import 'package:trading_apps/api/user_data.dart';
import 'package:trading_apps/res/colors.dart';


navPush(context, screen) => Navigator.of(context).push(MaterialPageRoute(builder: (context) => screen));
navPushOnRefresh(context,screen)=>Navigator.of(context).push(new MaterialPageRoute(builder:
    (_) => screen)).then((value){
      value?Function:null;
});
navPushReplace(context, screen) => Navigator.of(context).pushReplacement(MaterialPageRoute(builder:
 (context) => screen));

keyboardDismissed(context)=> FocusScope.of(context).unfocus();

Future<http.StreamedResponse> upload({required File imageFile, String loc='logo'})async{
  var stream= new http.ByteStream(DelegatingStream.typed(imageFile.openRead()));
  var length= await imageFile.length();
  var uploadURL = 'http://3.108.210.239:5000/api/users_image';
  var uri = Uri.parse(uploadURL);
  var request = new http.MultipartRequest("POST", uri);
  var imageUri = '${DateTime.now().millisecondsSinceEpoch}'+imageFile.path.substring(imageFile.path.lastIndexOf("."));
  var multipartFile = new http.MultipartFile("user_image", stream, length, filename: basename(imageUri));
  request.fields['user_image'] = imageUri;
  request.fields['user_id'] = '${userModel!.data.first.userId}';
  request.files.add(multipartFile);
  var response = await request.send();
  if(response.statusCode==201){
    print("Image Uploaded");
  }else{
    print("Upload Failed");
  }
  return response;
}

DateTime getLocalTimeStamp(String timeData) {
  // Map<String, int> months = {"January": 1, "February": 2, "March": 3, "April": 4, "May": 5, "June": 6, "July": 7, "August": 8, "September": 9, "October": 10, "November": 11, "December": 12};
  Map<String, int> months = {"Jan": 1, "Feb": 2, "Mar": 3, "Apr": 4, "May": 5, "Jun": 6, "Jul": 7, "Aug": 8, "Sep": 9, "Oct": 10, "Nov": 11, "Dec": 12};
  List<String> splittedTime = timeData.split(" ").toList();
  String monthString = months[splittedTime[1]].toString().length < 10
      ? "0"+months[splittedTime[1]].toString()
      : months[splittedTime[1]].toString();
  String cleanedDate = splittedTime[3]+"-"+monthString+"-"+splittedTime[2]+"T"+splittedTime[4];
  DateTime parsedDate = DateTime.parse(cleanedDate);

  return parsedDate;
}

class DialogUtils {
static DialogUtils _instance = DialogUtils._();

DialogUtils._();

static DialogUtils get instance => _instance;

void edgeAlerts(BuildContext context,String description){
  edgeAlert(context, title: 'Uh oh!', description: description.toString(),duration: 4,
      gravity: Gravity.bottom,icon: Icons.error,backgroundColor: APP_SECONDARY_COLOR);
  }
}

class NoData extends StatelessWidget {
  const NoData({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          Container(
            padding: EdgeInsets.all(8),
            child: Icon(FontAwesomeIcons.mehBlank, size:50,color: APP_PRIMARY_COLOR,),
            decoration: BoxDecoration(
                color: Colors.white10,
                shape: BoxShape.circle
            ),
          ),
          SizedBox(height: 24,),
          Text('Your list of address is empty', style: TextStyle(color: Colors.black,fontWeight: FontWeight.w600,fontSize: 15),),
          //Text('Go back to the chart if you want to open trade', style: TextStyle(color: Colors.white54)),
        ],
      ),
    );
  }
}





class NoInternetConnection extends StatefulWidget {
  const NoInternetConnection({Key? key}) : super(key: key);

  @override
  _NoInternetConnectionState createState() => _NoInternetConnectionState();
}

class _NoInternetConnectionState extends State<NoInternetConnection> {
  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: (){
        showDialog(
          context: context,
          builder: (BuildContext context) {
            // return object of type Dialog
            return AlertDialog(
              title: Text("Exit?"),
              content: new Text("Are you sure you want to exit?"),
              actions: <Widget>[
                new TextButton(
                  child: new Text(
                    "CANCEL",
                    style: TextStyle(
                      color: Colors.red,
                      fontFamily: 'Helvetica Neue',
                      fontSize: 14.899999618530273,
                    ),
                  ),
                  onPressed: () {
                    Navigator.of(context).pop();
                  },
                ),

                new TextButton(
                  child: new Text(
                    "OK",
                    style: TextStyle(
                      color: Colors.red,
                      fontFamily: 'Helvetica Neue',
                      fontSize: 14.899999618530273,
                    ),
                  ),
                  onPressed: () {
                    exit(0);
                  },
                ),
                // usually buttons at the bottom of the dialog
              ],
            );
          },
        );
        return Future.value(false);
      },
      child: Scaffold(
        body: Container(
         // margin: EdgeInsets.all(10),
         // padding: EdgeInsets.all(20),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              Center(
                child: Container(
                  height: 200,
                  width: 200,
                  margin: EdgeInsets.fromLTRB(25 ,20, 20, 25),
                  decoration: BoxDecoration(
                    image: DecorationImage(
                      image: AssetImage("assets/wifi.png"),
                      fit: BoxFit.fill,
                    ),
                  ),
                ),
              ),
              Text(
                "Whoops",
                textAlign: TextAlign.center,
                style: TextStyle(
                  fontSize: 20,
                  fontWeight: FontWeight.bold,
                ),
              ),
              Padding(
                padding: EdgeInsets.all(15),
                child: Text(
                  "Slow or no internet connection \n Please check your internet setting",
                  textAlign: TextAlign.center,
                  style: TextStyle(
                    fontSize: 16,
                  ),
                ),
              ),
              ElevatedButton(onPressed: (){

              }, child: Text('Try again'))
            ],
          ),
        ),
      ),
    );
  }
}




// Future<String> scanQrCode() async {
//   String qrCode = await MajaScan.startScan(
//       title: "QRcode scanner",
//       barColor: Colors.red,
//       titleColor: Colors.green,
//       qRCornerColor: Colors.blue,
//       qRScannerColor: Colors.deepPurple,
//       flashlightEnable: true,
//       scanAreaScale: 0.7 /// value 0.0 to 1.0
//   );
//
//   return qrCode;
// }