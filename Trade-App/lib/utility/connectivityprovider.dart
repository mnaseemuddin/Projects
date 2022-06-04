


import 'dart:io';
import 'package:connectivity/connectivity.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:provider/provider.dart';

class ConnectivityProvider extends ChangeNotifier{
  Connectivity connectivity=new Connectivity();
  late bool _isOnline;
  bool get isOnline=>_isOnline;

  startMonitoring()async{
    await initConnectivity();
    connectivity.onConnectivityChanged.listen((ConnectivityResult result) async{
      if(result==ConnectivityResult.none){
        _isOnline=false;
        notifyListeners();
      }else{
        await _updateConnectionStatus().then((bool isConnected){
          _isOnline=isConnected;
          notifyListeners();
        });
      }
    });
  }

  Future<void>initConnectivity() async{
    try{
      var status=await connectivity.checkConnectivity();
      if(status==ConnectivityResult.none){
        _isOnline=false;
        notifyListeners();
      }else{
        _isOnline=true;
        notifyListeners();
      }
    }on PlatformException catch(e){
      print("PlatformException: " + e.toString());
    }
  }

  Future<bool>_updateConnectionStatus() async{
    late bool isConnected;
    try{
      final List<InternetAddress>result=await InternetAddress.lookup('google.com');
      if(result.isNotEmpty&&result[0].rawAddress.isNotEmpty){
        isConnected=true;
      }
    }on SocketException catch(_){
      isConnected=false;
    }
    return isConnected;
  }
}