package com.lgt.tailor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
      //  ConnectivityBroadCast
    }
    override fun onBackPressed() {
        finishAffinity()
        super.onBackPressed()
    }
}