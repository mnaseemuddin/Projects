package com.lgt.tailor

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugins.GeneratedPluginRegistrant

class MainActivity: FlutterActivity()
// ,ConnectivityBroadCast.ConnectivityReceiverListener{
//    private var snackBar: Snackbar? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        GeneratedPluginRegistrant.registerWith(FlutterEngine(this))
//    //    registerReceiver(ConnectivityBroadCast(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
//
//
//    //        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
////        connectivityManager?.let {
////            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
////                it.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
////                    override fun onAvailable(network: Network) {
////                        val activityChangeIntent = Intent(
////                                this@MainActivity,
////                                MainActivity::class.java
////                        )
////                        startActivity(activityChangeIntent)
////                    }
////                    override fun onLost(network: Network) {
////                        val activityChangeIntent = Intent(
////                                this@MainActivity,
////                                MainActivity2::class.java
////                        )
////                        startActivity(activityChangeIntent)
////                    }
////                })
////            }
////        }
//
//
//    }
//
//    override fun onNetworkConnectionChanged(isConnected: Boolean) {
//        showNetworkMessage(isConnected)
//    }
//    override fun onResume() {
//        super.onResume()
//        ConnectivityBroadCast.connectivityReceiverListener = this
//    }
//    private fun showNetworkMessage(isConnected: Boolean) {
//        if (!isConnected) {
//            val activityChangeIntent = Intent(
//                this@MainActivity,
//                MainActivity2::class.java
//            )
//            startActivity(activityChangeIntent)
//        } else {
//            val activityChangeIntent = Intent(
//                this@MainActivity,
//                MainActivity::class.java
//            )
//            startActivity(activityChangeIntent)
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
//        super.onCreate(savedInstanceState, persistentState)
//    }
//}
