package com.app.cryptok.utils;

import android.Manifest;

public class Permissions {

  public static   String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
          Manifest.permission.CAMERA,
          Manifest.permission.RECORD_AUDIO

    };

  public static   String[] storage_permissions = new String[]{
          Manifest.permission.WRITE_EXTERNAL_STORAGE,
          Manifest.permission.READ_EXTERNAL_STORAGE,
          Manifest.permission.CAMERA,


  };
}
