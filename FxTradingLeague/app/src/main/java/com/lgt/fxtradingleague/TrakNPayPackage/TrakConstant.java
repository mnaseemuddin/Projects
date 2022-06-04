package com.lgt.fxtradingleague.TrakNPayPackage;

import com.lgt.fxtradingleague.Config;

public class TrakConstant {
    //API_KEY is given by the Payment Gateway. Please Copy Paste Here.
    //public static final String PG_API_KEY = "81dde990-ec12-4840-8d32-3d4fb63b1453";
    // public static final String PG_API_KEY = "6c9da2f7-45cc-4742-8fdf-c3812171f4f4";
    public static final String PG_API_KEY = "4c8efba7-747e-4633-b157-4174e8456e2b";
    //URL to Accept Payment Response After Payment. This needs to be done at the client's web server.
    public static final String PG_RETURN_URL = Config.VERIFYHASH;

    //Enter the Mode of Payment Here . Allowed Values are "LIVE" or "TEST".
    public static final String PG_MODE = "LIVE";

    //PG_CURRENCY is given by the Payment Gateway. Only "INR" Allowed.
    // public static final String PG_CURRENCY = "USD";
    public static final String PG_CURRENCY = "INR";

    //PG_COUNTRY is given by the Payment Gateway. Only "IND" Allowed.
    public static final String PG_COUNTRY = "IND";

    public static String PG_ORDER_ID = "";
    public static final String PG_DESCRIPTION = "test";
    public static final String PG_ADD_1 = "NA";
    public static final String PG_ADD_2 = "NA";
    public static final String PG_UDF1 = "udf1";
    public static final String PG_UDF2 = "udf2";
    public static final String PG_UDF3 = "udf3";
    public static final String PG_UDF4 = "udf4";
    public static final String PG_UDF5 = "udf5";

}
