import 'dart:async';
import 'dart:convert';
import 'dart:math';
import 'dart:typed_data';
import 'dart:ui' as ui;
import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:get/get_navigation/src/root/get_material_app.dart';
import 'package:provider/provider.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/res/strings.dart';
import 'package:trading_apps/screens/splash.dart';
import 'package:trading_apps/utility/connectivityprovider.dart';
import 'package:wakelock/wakelock.dart';
import 'package:firebase_auth/firebase_auth.dart';

void main() async{
  SystemChrome.setSystemUIOverlayStyle(SystemUiOverlayStyle.light);
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp();
  SystemChrome.setPreferredOrientations([
    DeviceOrientation.portraitDown,
    DeviceOrientation.portraitUp,
  ]);
  runApp(MyApp());
}
class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    Wakelock.enable();
    return MultiProvider(providers: [
      ChangeNotifierProvider(create: (context)=>ConnectivityProvider())
    ],child:  GetMaterialApp(
      title: APP_NAME,
      theme: ThemeData(
          primarySwatch: appColor,
          backgroundColor: APP_PRIMARY_COLOR,
          hintColor: Colors.white38
      ),
      debugShowCheckedModeBanner: false,
      home: Splash(),
    ),);
  }
}
// class MyHomePage extends StatefulWidget {
//   MyHomePage({Key? key, required this.title}) : super(key: key);
//
//
//
//   final String title;
//
//   @override
//   _MyHomePageState createState() => _MyHomePageState();
// }
//
// class _MyHomePageState extends State<MyHomePage> {
//
//   // final List<double> axisY =[
//   //   10, 60, 30, 30, 50, 90, 20, 100, 20, 150, 35, 100, 50, 30, 50, 100, 10, 30,
//   //   10, 60, 30, 30, 50, 90, 20, 100, 20, 150, 35, 100, 50, 30, 50, 100, 10, 30,
//   //   10, 60, 30, 30, 50, 90, 20, 100, 20, 150, 35, 100, 50, 30, 50, 100, 10, 30,
//   //   10, 60, 30, 30, 50, 90, 20, 100, 20, 150, 35, 100, 50, 30, 50, 100, 10, 30,
//   //   10, 60, 30, 30, 50, 90, 20, 100, 20, 150, 35, 100, 50, 30, 50, 100, 10, 30,
//   //   10, 60, 30, 30, 50, 90, 20, 100, 20, 150, 35, 100, 50, 30, 50, 100, 10, 30,
//   //   10, 60, 30, 30, 50, 90, 20, 100, 20, 150, 35, 100, 50, 30, 50, 100, 10, 30,
//   //   10, 60, 30, 30, 50, 90, 20, 100, 20, 150, 35, 100, 50, 30, 50, 100, 10, 30,
//   //   10, 60, 30, 30, 50, 90, 20, 100, 20, 150, 35, 100, 50, 30, 50, 100, 10, 30,
//   //   10, 60, 30, 30, 50, 90, 20, 100, 20, 150, 35, 100, 50, 30, 50, 100, 10, 30,
//   //   10, 60, 30, 30, 50, 90, 20, 100, 20, 150, 35, 100, 50, 30, 50, 100, 10, 30,
//   //   10, 60, 30, 30, 50, 90, 20, 100, 20, 150, 35, 100, 50, 30, 50, 100, 10, 30,
//   //   10, 60, 30, 30, 50, 90, 20, 100, 20, 150, 35, 100, 50, 30, 50, 100, 10, 30,
//   //   10, 60, 30, 30, 50, 90, 20, 100, 20, 150, 35, 100, 50, 30, 50, 100, 10, 30,
//   //   10, 60, 30, 30, 50, 90, 20, 100, 20, 150, 35, 100, 50, 30, 50, 100, 10, 30,
//   //   10, 60, 30, 30, 50, 90, 20, 100, 20, 150, 35, 100, 50, 30, 50, 100, 10, 30,
//   //   10, 60, 30, 30, 50, 90, 20, 100, 20, 150, 35, 100, 50, 30, 50, 100, 10, 30,
//   //   10, 60, 30, 30, 50, 90, 20, 100, 20, 150, 35, 100, 50, 30, 50, 100, 10, 30,
//   //   10, 60, 30, 30, 50, 90, 20, 100, 20, 150, 35, 100, 50, 30, 50, 100, 10, 30,
//   // ];
//
//   final List<double> axisY =[
//     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
//     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
//     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
//     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
//     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
//     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
//     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
//     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
//     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
//     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
//     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
//   ];
//
//   final List<double> axisX =[60, 360, 660];
//
//   @override
//   void initState() {
//     int count = 0;
//     double lastvalue = 0;
//     Timer.periodic(Duration(milliseconds: 100), (timer) {
//       double val = Random().nextDouble()*200;
//       setState(() {
//         count++;
//         if(count==5){
//           count = 0;
//           lastvalue = val;
//         }
//
//         axisY.add(lastvalue+Random().nextDouble()*30);
//         axisY.removeAt(0);
//         axisX.asMap().forEach((index, element) {
//           if(element-1<=0){
//             axisX[0] = 300;
//             axisX[1] = 600;
//             axisX[2] = 900;
//             return;
//           }
//           axisX[index] = element-1;
//         });
//       });
//     }, );
//     super.initState();
//   }
//
//   @override
//   Widget build(BuildContext context) {
//     Size size = MediaQuery.of(context).size;
//     return Scaffold(
//       appBar: AppBar(
//         title: Text(widget.title),
//       ),
//       backgroundColor: const Color(0xFF182331),
//       body: Container(
//         child: Container(
//           height: size.height,
//           width: size.width,
//           child: CustomPaint(
//
//           painter: MainGraph(axisY: axisY, axisX: axisX),
//
//         ),),
//       )
//     );
//   }
//
//
// }


//#    - assets/images/icon/btc_icon.png
// #    - assets/images/icon/doge@2x.png
// #    - assets/images/minus.png
// #    - assets/images/icon/edo@2x.png
// #    - assets/images/icon/matic@2x.png
// #    - assets/images/icon/polis@2x.png
// #    - assets/images/icon/data@2x.png
// #    - assets/images/icon/poe@2x.png
// #    - assets/images/icon/start@2x.png
// #    - assets/images/icon/rdn@2x.png
// #    - assets/images/icon/sberbank@2x.png
// #    - assets/images/icon/ubq@2x.png
// #    - assets/images/icon/nmc@2x.png
// #    - assets/images/icon/payx@2x.png
// #    - assets/images/icon/bab@2x.png
// #    - assets/images/icon/dtr@2x.png
// #    - assets/images/icon/dcn@2x.png
// #    - assets/images/icon/pink@2x.png
// #    - assets/images/icon/zcl@2x.png
// #    - assets/images/icon/one@2x.png
// #    - assets/images/icon/trx@2x.png
// #    - assets/images/icon/miota@2x.png
// #    - assets/images/icon/amp@2x.png
// #    - assets/images/icon/grin@2x.png
// #    - assets/images/icon/algo@2x.png
// #    - assets/images/icon/krb@2x.png
// #    - assets/images/icon/0xbtc@2x.png
// #    - assets/images/icon/html@2x.png
// #    - assets/images/icon/zec@2x.png
// #    - assets/images/icon/vib@2x.png
// #    - assets/images/icon/qlc@2x.png
// #    - assets/images/icon/lbc@2x.png
// #    - assets/images/icon/rpx@2x.png
// #    - assets/images/icon/mana@2x.png
// #    - assets/images/icon/aeon@2x.png
// #    - assets/images/icon/eos@2x.png
// #    - assets/images/icon/mod@2x.png
// #    - assets/images/icon/xas@2x.png
// #    - assets/images/icon/qtum@2x.png
// #    - assets/images/icon/emc@2x.png
// #    - assets/images/icon/ast@2x.png
// #    - assets/images/icon/xdn@2x.png
// #    - assets/images/icon/husd@2x.png
// #    - assets/images/icon/wax@2x.png
// #    - assets/images/icon/powr@2x.png
// #    - assets/images/icon/add@2x.png
// #    - assets/images/icon/ht@2x.png
// #    - assets/images/icon/hush@2x.png
// #    - assets/images/icon/kmd@2x.png
// #    - assets/images/icon/ela@2x.png
// #    - assets/images/icon/xvc@2x.png
// #    - assets/images/icon/xrp@2x.png
// #    - assets/images/icon/vrc@2x.png
// #    - assets/images/icon/cred@2x.png
// #    - assets/images/icon/sky@2x.png
// #    - assets/images/icon/atm@2x.png
// #    - assets/images/icon/pivx@2x.png
// #    - assets/images/icon/tpay@2x.png
// #    - assets/images/icon/clam@2x.png
// #    - assets/images/icon/arn@2x.png
// #    - assets/images/icon/bix@2x.png
// #    - assets/images/icon/cix@2x.png
// #    - assets/images/icon/wabi@2x.png
// #    - assets/images/icon/hight@2x.png
// #    - assets/images/icon/xby@2x.png
// #    - assets/images/icon/mln@2x.png
// #    - assets/images/icon/cny@2x.png
// #    - assets/images/icon/tgch@2x.png
// #    - assets/images/icon/tnt@2x.png
// #    - assets/images/icon/neu@2x.png
// #    - assets/images/icon/spacehbit@2x.png
// #    - assets/images/icon/2give@2x.png
// #    - assets/images/icon/ppc@2x.png
// #    - assets/images/icon/xbc@2x.png
// #    - assets/images/icon/bela@2x.png
// #    - assets/images/icon/pre@2x.png
// #    - assets/images/icon/tnb@2x.png
// #    - assets/images/icon/icp@2x.png
// #    - assets/images/icon/neo@2x.png
// #    - assets/images/icon/steem@2x.png
// #    - assets/images/icon/jnt@2x.png
// #    - assets/images/icon/gold@2x.png
// #    - assets/images/icon/tks@2x.png
// #    - assets/images/icon/gas@2x.png
// #    - assets/images/icon/eng@2x.png
// #    - assets/images/icon/repv2@2x.png
// #    - assets/images/icon/blk@2x.png
// #    - assets/images/icon/shift@2x.png
// #    - assets/images/icon/theta@2x.png
// #    - assets/images/icon/call@2x.png
// #    - assets/images/icon/music@2x.png
// #    - assets/images/icon/usdc@2x.png
// #    - assets/images/icon/wan@2x.png
// #    - assets/images/icon/ant@2x.png
// #    - assets/images/icon/lpt@2x.png
// #    - assets/images/icon/etp@2x.png
// #    - assets/images/icon/ncash@2x.png
// #    - assets/images/icon/fldc@2x.png
// #    - assets/images/icon/stq@2x.png
// #    - assets/images/icon/eur@2x.png
// #    - assets/images/icon/generic@2x.png
// #    - assets/images/icon/nkn@2x.png
// #    - assets/images/icon/bal@2x.png
// #    - assets/images/icon/ren@2x.png
// #    - assets/images/icon/ntbc@2x.png
// #    - assets/images/icon/ignis@2x.png
// #    - assets/images/icon/kcs@2x.png
// #    - assets/images/icon/btx@2x.png
// #    - assets/images/icon/bcd@2x.png
// #    - assets/images/icon/tern@2x.png
// #    - assets/images/icon/mco@2x.png
// #    - assets/images/icon/bat@2x.png
// #    - assets/images/icon/eth@2x.png
// #    - assets/images/icon/dth@2x.png
// #    - assets/images/icon/crw@2x.png
// #    - assets/images/icon/npxs@2x.png
// #    - assets/images/icon/msr@2x.png
// #    - assets/images/icon/sai@2x.png
// #    - assets/images/icon/bch@2x.png
// #    - assets/images/icon/ilk@2x.png
// #    - assets/images/icon/trtl@2x.png
// #    - assets/images/icon/gzr@2x.png
// #    - assets/images/icon/btt@2x.png
// #    - assets/images/icon/drop@2x.png
// #    - assets/images/icon/sc@2x.png
// #    - assets/images/icon/dew@2x.png
// #    - assets/images/icon/fjc@2x.png
// #    - assets/images/icon/tau@2x.png
// #    - assets/images/icon/evx@2x.png
// #    - assets/images/icon/ong@2x.png
// #    - assets/images/icon/apex@2x.png
// #    - assets/images/icon/atom@2x.png
// #    - assets/images/icon/cmm@2x.png
// #    - assets/images/icon/xuc@2x.png
// #    - assets/images/icon/iotx@2x.png
// #    - assets/images/icon/slr@2x.png
// #    - assets/images/icon/sol@2x.png
// #    - assets/images/icon/grt@2x.png
// #    - assets/images/icon/mnx@2x.png
// #    - assets/images/icon/ok@2x.png
// #    - assets/images/icon/xpr@2x.png
// #    - assets/images/icon/ric@2x.png
// #    - assets/images/icon/ngc@2x.png
// #    - assets/images/icon/sngls@2x.png
// #    - assets/images/icon/moac@2x.png
// #    - assets/images/icon/oax@2x.png
// #    - assets/images/icon/fsn@2x.png
// #    - assets/images/icon/game@2x.png
// #    - assets/images/icon/ftc@2x.png
// #    - assets/images/icon/zil@2x.png
// #    - assets/images/icon/bze@2x.png
// #    - assets/images/icon/cloak@2x.png
// #    - assets/images/icon/gto@2x.png
// #    - assets/images/icon/pasl@2x.png
// #    - assets/images/icon/drgn@2x.png
// #    - assets/images/icon/chips@2x.png
// #    - assets/images/icon/icx@2x.png
// #    - assets/images/icon/sushi@2x.png
// #    - assets/images/icon/storm@2x.png
// #    - assets/images/icon/gnt@2x.png
// #    - assets/images/icon/med@2x.png
// #    - assets/images/icon/ten@2x.png
// #    - assets/images/icon/hns@2x.png
// #    - assets/images/icon/srn@2x.png
// #    - assets/images/icon/ins@2x.png
// #    - assets/images/icon/dat@2x.png
// #    - assets/images/icon/rdd@2x.png
// #    - assets/images/icon/nlg@2x.png
// #    - assets/images/icon/amb@2x.png
// #    - assets/images/icon/flux@2x.png
// #    - assets/images/icon/usd@2x.png
// #    - assets/images/icon/ink@2x.png
// #    - assets/images/icon/ltc@2x.png
// #    - assets/images/icon/smart@2x.png
// #    - assets/images/icon/gmr@2x.png
// #    - assets/images/icon/chat@2x.png
// #    - assets/images/icon/poly@2x.png
// #    - assets/images/icon/rep@2x.png
// #    - assets/images/icon/nebl@2x.png
// #    - assets/images/icon/band@2x.png
// #    - assets/images/icon/tel@2x.png
// #    - assets/images/icon/fair@2x.png
// #    - assets/images/icon/mft@2x.png
// #    - assets/images/icon/qash@2x.png
// #    - assets/images/icon/ctr@2x.png
// #    - assets/images/icon/maid@2x.png
// #    - assets/images/icon/aion@2x.png
// #    - assets/images/icon/bcn@2x.png
// #    - assets/images/icon/nxs@2x.png
// #    - assets/images/icon/meetone@2x.png
// #    - assets/images/icon/dcr@2x.png
// #    - assets/images/icon/etn@2x.png
// #    - assets/images/icon/xmr@2x.png
// #    - assets/images/icon/omg@2x.png
// #    - assets/images/icon/vtho@2x.png
// #    - assets/images/icon/ethos@2x.png
// #    - assets/images/icon/rads@2x.png
// #    - assets/images/icon/burst@2x.png
// #    - assets/images/icon/edg@2x.png
// #    - assets/images/icon/yoyow@2x.png
// #    - assets/images/icon/btdx@2x.png
// #    - assets/images/icon/utk@2x.png
// #    - assets/images/icon/lend@2x.png
// #    - assets/images/icon/dash@2x.png
// #    - assets/images/icon/wings@2x.png
// #    - assets/images/icon/poa@2x.png
// #    - assets/images/icon/booty@2x.png
// #    - assets/images/icon/veri@2x.png
// #    - assets/images/icon/lkk@2x.png
// #    - assets/images/icon/pax@2x.png
// #    - assets/images/icon/part@2x.png
// #    - assets/images/icon/icn@2x.png
// #    - assets/images/icon/bos@2x.png
// #    - assets/images/icon/nexo@2x.png
// #    - assets/images/icon/neos@2x.png
// #    - assets/images/icon/mnz@2x.png
// #    - assets/images/icon/safe@2x.png
// #    - assets/images/icon/adx@2x.png
// #    - assets/images/icon/sumo@2x.png
// #    - assets/images/icon/nav@2x.png
// #    - assets/images/icon/agrs@2x.png
// #    - assets/images/icon/xvg@2x.png
// #    - assets/images/icon/snx@2x.png
// #    - assets/images/icon/crpt@2x.png
// #    - assets/images/icon/$pac@2x.png
// #    - assets/images/icon/snt@2x.png
// #    - assets/images/icon/spank@2x.png
// #    - assets/images/icon/vrsc@2x.png
// #    - assets/images/icon/wpr@2x.png
// #    - assets/images/icon/kin@2x.png
// #    - assets/images/icon/arnx@2x.png
// #    - assets/images/icon/bnt@2x.png
// #    - assets/images/icon/knc@2x.png
// #    - assets/images/icon/d@2x.png
// #    - assets/images/icon/qsp@2x.png
// #    - assets/images/icon/grs@2x.png
// #    - assets/images/icon/xsg@2x.png
// #    - assets/images/icon/tix@2x.png
// #    - assets/images/icon/xp@2x.png
// #    - assets/images/icon/dlt@2x.png
// #    - assets/images/icon/elix@2x.png
// #    - assets/images/icon/ndz@2x.png
// #    - assets/images/icon/cnx@2x.png
// #    - assets/images/icon/gvt@2x.png
// #    - assets/images/icon/skl@2x.png
// #    - assets/images/icon/equa@2x.png
// #    - assets/images/icon/emb@2x.png
// #    - assets/images/icon/xpm@2x.png
// #    - assets/images/icon/bcio@2x.png
// #    - assets/images/icon/usdt@2x.png
// #    - assets/images/icon/gusd@2x.png
// #    - assets/images/icon/xem@2x.png
// #    - assets/images/icon/block@2x.png
// #    - assets/images/icon/fct@2x.png
// #    - assets/images/icon/cob@2x.png
// #    - assets/images/icon/nas@2x.png
// #    - assets/images/icon/xpa@2x.png
// #    - assets/images/icon/ox@2x.png
// #    - assets/images/icon/itc@2x.png
// #    - assets/images/icon/tzc@2x.png
// #    - assets/images/icon/btcd@2x.png
// #    - assets/images/icon/zen@2x.png
// #    - assets/images/icon/dgd@2x.png
// #    - assets/images/icon/storj@2x.png
// #    - assets/images/icon/rise@2x.png
// #    - assets/images/icon/qiwi@2x.png
// #    - assets/images/icon/ksm@2x.png
// #    - assets/images/icon/leo@2x.png
// #    - assets/images/icon/bsv@2x.png
// #    - assets/images/icon/vivo@2x.png
// #    - assets/images/icon/mda@2x.png
// #    - assets/images/icon/blcn@2x.png
// #    - assets/images/icon/btch@2x.png
// #    - assets/images/icon/flo@2x.png
// #    - assets/images/icon/btc@2x.png
// #    - assets/images/icon/xlm@2x.png
// #    - assets/images/icon/rcn@2x.png
// #    - assets/images/icon/omni@2x.png
// #    - assets/images/icon/ctxc@2x.png
// #    - assets/images/icon/xmcc@2x.png
// #    - assets/images/icon/mth@2x.png
// #    - assets/images/icon/btcp@2x.png
// #    - assets/images/icon/xmo@2x.png
// #    - assets/images/icon/rvn@2x.png
// #    - assets/images/icon/ardr@2x.png
// #    - assets/images/icon/ion@2x.png
// #    - assets/images/icon/bq@2x.png
// #    - assets/images/icon/cenz@2x.png
// #    - assets/images/icon/xmy@2x.png
// #    - assets/images/icon/dai@2x.png
// #    - assets/images/icon/via@2x.png
// #    - assets/images/icon/cs@2x.png
// #    - assets/images/icon/hot@2x.png
// #    - assets/images/icon/wbtc@2x.png
// #    - assets/images/icon/zel@2x.png
// #    - assets/images/icon/crv@2x.png
// #    - assets/images/icon/safemoon@2x.png
// #    - assets/images/icon/oxt@2x.png
// #    - assets/images/icon/nxt@2x.png
// #    - assets/images/icon/bay@2x.png
// #    - assets/images/icon/waves@2x.png
// #    - assets/images/icon/eql@2x.png
// #    - assets/images/icon/btm@2x.png
// #    - assets/images/icon/dbc@2x.png
// #    - assets/images/icon/lsk@2x.png
// #    - assets/images/icon/rap@2x.png
// #    - assets/images/icon/gbyte@2x.png
// #    - assets/images/icon/tusd@2x.png
// #    - assets/images/icon/ankr@2x.png
// #    - assets/images/icon/eca@2x.png
// #    - assets/images/icon/sphtx@2x.png
// #    - assets/images/icon/link@2x.png
// #    - assets/images/icon/gup@2x.png
// #    - assets/images/icon/stox@2x.png
// #    - assets/images/icon/enj@2x.png
// #    - assets/images/icon/vtc@2x.png
// #    - assets/images/icon/yfi@2x.png
// #    - assets/images/icon/wtc@2x.png
// #    - assets/images/icon/tnc@2x.png
// #    - assets/images/icon/ppt@2x.png
// #    - assets/images/icon/gbx@2x.png
// #    - assets/images/icon/coqui@2x.png
// #    - assets/images/icon/taas@2x.png
// #    - assets/images/icon/loom@2x.png
// #    - assets/images/icon/ryo@2x.png
// #    - assets/images/icon/ary@2x.png
// #    - assets/images/icon/bnb@2x.png
// #    - assets/images/icon/eop@2x.png
// #    - assets/images/icon/comp@2x.png
// #    - assets/images/icon/huc@2x.png
// #    - assets/images/icon/r@2x.png
// #    - assets/images/icon/rlc@2x.png
// #    - assets/images/icon/deez@2x.png
// #    - assets/images/icon/gxs@2x.png
// #    - assets/images/icon/cdt@2x.png
// #    - assets/images/icon/cc@2x.png
// #    - assets/images/icon/bcpt@2x.png
// #    - assets/images/icon/fil@2x.png
// #    - assets/images/icon/bsd@2x.png
// #    - assets/images/icon/stx@2x.png
// #    - assets/images/icon/exmo@2x.png
// #    - assets/images/icon/btcz@2x.png
// #    - assets/images/icon/mds@2x.png
// #    - assets/images/icon/oot@2x.png
// #    - assets/images/icon/ampl@2x.png
// #    - assets/images/icon/glxt@2x.png
// #    - assets/images/icon/sand@2x.png
// #    - assets/images/icon/nio@2x.png
// #    - assets/images/icon/iop@2x.png
// #    - assets/images/icon/pungo@2x.png
// #    - assets/images/icon/dgb@2x.png
// #    - assets/images/icon/salt@2x.png
// #    - assets/images/icon/mona@2x.png
// #    - assets/images/icon/elec@2x.png
// #    - assets/images/icon/klown@2x.png
// #    - assets/images/icon/rub@2x.png
// #    - assets/images/icon/pura@2x.png
// #    - assets/images/icon/sub@2x.png
// #    - assets/images/icon/xin@2x.png
// #    - assets/images/icon/dta@2x.png
// #    - assets/images/icon/zrx@2x.png
// #    - assets/images/icon/jpy@2x.png
// #    - assets/images/icon/aeur@2x.png
// #    - assets/images/icon/bdl@2x.png
// #    - assets/images/icon/prl@2x.png
// #    - assets/images/icon/qrl@2x.png
// #    - assets/images/icon/iq@2x.png
// #    - assets/images/icon/dot@2x.png
// #    - assets/images/icon/gbp@2x.png
// #    - assets/images/icon/wicc@2x.png
// #    - assets/images/icon/emc2@2x.png
// #    - assets/images/icon/ppp@2x.png
// #    - assets/images/icon/mzc@2x.png
// #    - assets/images/icon/blz@2x.png
// #    - assets/images/icon/cmt@2x.png
// #    - assets/images/icon/elf@2x.png
// #    - assets/images/icon/ost@2x.png
// #    - assets/images/icon/gsc@2x.png
// #    - assets/images/icon/rhoc@2x.png
// #    - assets/images/icon/pirl@2x.png
// #    - assets/images/icon/sls@2x.png
// #    - assets/images/icon/uma@2x.png
// #    - assets/images/icon/xcp@2x.png
// #    - assets/images/icon/nuls@2x.png
// #    - assets/images/icon/iost@2x.png
// #    - assets/images/icon/sin@2x.png
// #    - assets/images/icon/trig@2x.png
// #    - assets/images/icon/tkn@2x.png
// #    - assets/images/icon/exp@2x.png
// #    - assets/images/icon/sys@2x.png
// #    - assets/images/icon/unity@2x.png
// #    - assets/images/icon/abt@2x.png
// #    - assets/images/icon/sib@2x.png
// #    - assets/images/icon/vet@2x.png
// #    - assets/images/icon/hpb@2x.png
// #    - assets/images/icon/agi@2x.png
// #    - assets/images/icon/wgr@2x.png
// #    - assets/images/icon/bnty@2x.png
// #    - assets/images/icon/x@2x.png
// #    - assets/images/icon/ada@2x.png
// #    - assets/images/icon/dnt@2x.png
// #    - assets/images/icon/strat@2x.png
// #    - assets/images/icon/colx@2x.png
// #    - assets/images/icon/fun@2x.png
// #    - assets/images/icon/xtz@2x.png
// #    - assets/images/icon/grc@2x.png
// #    - assets/images/icon/appc@2x.png
// #    - assets/images/icon/uni@2x.png
// #    - assets/images/icon/mith@2x.png
// #    - assets/images/icon/pasc@2x.png
// #    - assets/images/icon/cnd@2x.png
// #    - assets/images/icon/mkr@2x.png
// #    - assets/images/icon/soc@2x.png
// #    - assets/images/icon/nlc2@2x.png
// #    - assets/images/icon/zilla@2x.png
// #    - assets/images/icon/ella@2x.png
// #    - assets/images/icon/aywa@2x.png
// #    - assets/images/icon/arg@2x.png
// #    - assets/images/icon/pay@2x.png
// #    - assets/images/icon/eon@2x.png
// #    - assets/images/icon/snm@2x.png
// #    - assets/images/icon/xbp@2x.png
// #    - assets/images/icon/ark@2x.png
// #    - assets/images/icon/zest@2x.png
// #    - assets/images/icon/aave@2x.png
// #    - assets/images/icon/hsr@2x.png
// #    - assets/images/icon/act@2x.png
// #    - assets/images/icon/lrc@2x.png
// #    - assets/images/icon/gno@2x.png
// #    - assets/images/icon/hodl@2x.png
// #    - assets/images/icon/nano@2x.png
// #    - assets/images/icon/plr@2x.png
// #    - assets/images/icon/lun@2x.png
// #    - assets/images/icon/tomo@2x.png
// #    - assets/images/icon/ont@2x.png
// #    - assets/images/icon/bpt@2x.png
// #    - assets/images/icon/btg@2x.png
// #    - assets/images/icon/gin@2x.png
// #    - assets/images/icon/tbx@2x.png
// #    - assets/images/icon/fuel@2x.png
// #    - assets/images/icon/brd@2x.png
// #    - assets/images/icon/mtl@2x.png
// #    - assets/images/icon/sbd@2x.png
// #    - assets/images/icon/xmg@2x.png
// #    - assets/images/icon/stak@2x.png
// #    - assets/images/icon/bcc@2x.png
// #    - assets/images/icon/beam@2x.png
// #    - assets/images/icon/nmr@2x.png
// #    - assets/images/icon/mcap@2x.png
// #    - assets/images/icon/cdn@2x.png
// #    - assets/images/icon/max@2x.png
// #    - assets/images/icon/paxg@2x.png
// #    - assets/images/icon/etc@2x.png
// #    - assets/images/icon/xzc@2x.png
// #    - assets/images/icon/auto@2x.png
// #    - assets/images/icon/chsb@2x.png
// #    - assets/images/icon/req@2x.png
// #    - assets/images/icon/dock@2x.png
// #    - assets/images/icon/chain@2x.png
// #    - assets/images/icon/ebst@2x.png
// #    - assets/images/icon/ae@2x.png
// #    - assets/images/icon/audr@2x.png
// #    - assets/images/icon/edoge@2x.png
// #    - assets/images/icon/san@2x.png
// #    - assets/images/icon/bcbc@2x.png
// #    - assets/images/icon/entrp@2x.png
// #    - assets/images/icon/cvc@2x.png
// #    - assets/images/icon/dent@2x.png
// #    - assets/images/icon/bts@2x.png
// #    - assets/images/icon/bco@2x.png
// #    - assets/images/icon/actn@2x.png
// #    - assets/images/icon/vibe@2x.png
// #    - assets/images/icon/pot@2x.png
// #    - assets/images/icon/tdc@2x.png
// #
// #    - assets/images/icon/tron@2x.png
//
// #    - assets/images/shirts.jpeg
// #    - assets/images/smartphone.png
// #    - assets/images/loudspeaker.png
// #    - assets/images/television.png
// #    - assets/images/pc.png
// #    - assets/images/accessories.png
// #    - assets/images/hotdealsspeak.jpeg
// #    - assets/images/hotdealsphones.jpeg
// #    - assets/images/categorycars.jpg
// #    - assets/images/categorycars1.jpg
// #    - assets/images/categorycars4.jpg
// #    - assets/images/categorycars3.jpg
// #    - assets/images/itemscars.jpg
// #    - assets/images/itemvilla.jpg
// #    - assets/images/itemvilla1.jpg
// #    - assets/images/metalbucket.png
// #    - assets/images/bag.png
// #    -
// #    -
