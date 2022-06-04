package com.lgt.fxtradingleague;


public class Config {
    // alpha  alpha vantage : 3MAUM7XA8EH8ISCU
    public static String BASEURL = "http://dreamcricketers.com/myrest/user/";
    public static String image_base_url = "http://dreamcricketers.com/";
    public static String baseUrl2 = "http://dreamcricketers.com/";
    // public static String BASEURL = "http://allrounders.biz/perfect11/myrest/user/";

    // public static String baseUrl2="http://allrounders.biz/";
    public static String Authentication = "";

    public static String IMAGEBASEURL = baseUrl2 + "uploads/";
    public static String ProfileIMAGEBASEURL = image_base_url + "/uploads/user/";

    public static String TEAMFLAGIMAGE = "";
    public static String PLAYERIMAGE = IMAGEBASEURL + "player/";

    public static String BANNERIMAGE = IMAGEBASEURL + "offers/";

    public static String LEADERBOARDPLAYERIMAGE = IMAGEBASEURL + "leaderboard/";

    public static String TERMSANDCONDITIONSURL = baseUrl2 + "website/termsandconditions";
    public static String FANTASYPOINTSYSTEMURL = baseUrl2 + "website/pointsystem";
    public static String HOWTOPLAYURL = baseUrl2 + "website/how_to_play";
    public static String ABOUTUSURL = baseUrl2 + "website/aboutUs";
    public static String HELPDESKURL = baseUrl2 + "website/contactus";

    public static String SIGNUP = BASEURL + "user_registration";
    public static String LOGIN = BASEURL + "login";
    public static String VERIFYOTP = BASEURL + "user_number_verify";
    public static String check_coinaddress = BASEURL + "check_coinaddress";
    public static String RESENDOTP = BASEURL + "resend_otp";

    public static String FORGOTPASSWORD = BASEURL + "forget_password";
    public static String VERIFYFORGOTPASSWORD = BASEURL + "varify_forgot_password";

    public static String UPDATENEWPASSWORD = BASEURL + "update_password";
    public static String CHANGEPASSWORD = BASEURL + "change_password";

    // public static String HOMEFIXTURES = BASEURL + "match_record";
    // public static String MYFIXTURES = BASEURL + "mymatch_record";
    public static String CONTESTLIST = BASEURL + "contest_list";
    public static String WINNINGINFOLIST = BASEURL + "winning_info";
    public static String PLAYERLIST = BASEURL + "team_list";
    public static String VIEWPROFILE = BASEURL + "view_profile";
    public static String EDITPROFILE = BASEURL + "edit_profile";
    public static String check_ioaddress = BASEURL + "check_ioaddress";
    // http://dreamcricketers.com/myrest/user/check_ioaddress?addresses=3GMsgUjoFThfcvnxSboEswfn1XJ64jyMSQ&user_id=3

    public static String PLAYERINFO = BASEURL + "Player_information";

    public static String SAVETEAM = BASEURL + "save_team";
    public static String MYTEAMLIST = BASEURL + "my_team_list";

    public static String JOINCONTEST = BASEURL + "join_contest";

    public static String MYJOINCONTESTLIST = BASEURL + "my_join_contest_list";

    public static String MYFIXTURELEADERBOARD = BASEURL + "joined_contest";
    public static String MYFIXTURELEADERBOARDTEAM = BASEURL + "my_joined_team_list";


    /// MyLive Contest List
    public static String MYJOINLIVECONTESTLIST = BASEURL + "my_join_contest_list_live";

    //Live Leaderboard
    public static String MYLIVELEADERBOARD = BASEURL + "joined_contest";

    //Live Leaderboard Team Preview
    public static String MYLIVELEADERBOARDTEAM = BASEURL + "my_joined_team_list";

    /// MyResult Contest List
    public static String MYJOINRESULTCONTESTLIST = BASEURL + "my_join_contest_list_live";

    //MyAccount
    public static String MYACCOUNT = BASEURL + "my_account";

    //MyPlaying History
    public static String MYPLAYINGHISTORY = BASEURL + "playing_history";

    //AddMoney
    public static String ADDAMOUNT = BASEURL + "add_amount";

    //MyTransactionList
    public static String MYTRANSACTIONLIST = BASEURL + "my_account_transaction";
    //Notification List
    public static String NOTIFICATIONLIST = BASEURL + "notification";

    //Invited Friends List
    public static String INVITEDFRIENDSLIST = BASEURL + "refer_friend_list";

    //Withdraw Amount User Data If Saved
    public static String WITHDRAWAMOUNTUSERDATA = BASEURL + "user_withdrow_information";

    //Submit Withdrawl Request
    public static String WITHDRAWLREQUEST = BASEURL + "withdrow_amount";

    //Global Ranking Request
    public static String GLOBALRANKINGLIST = BASEURL + "global_rank";

    //HashKeyRequest
    public static String HASHKEYREQUEST = BASEURL + "hashkey";

    //HomeBannerRequest
    public static String HOMEBANNER = BASEURL + "get_offers";

    //UploadDocument
    public static String UPLOADDOUCMENT = BASEURL + "update_documents";
    //UploadImage
    public static String UpdateUserProfileImage = BASEURL + "update_user_profile_image";
    //UpdateApp
    public static String UPDATEAPP = BASEURL + "update_app";

    //RankCreateContest
    public static String CREATECONTESTRANK = BASEURL + "user_contest";

    //CreateOwnContest
    public static String CREATEOWNCONTEST = BASEURL + "user_contestCreate";

    //CreateOwnContestList
    public static String CREATEOWNCONTESTLIST = BASEURL + "user_contestList";

    //TrakNPayPaymentGateway Verify Hash Link
    public static String VERIFYHASH = BASEURL + "verifyHash";
    //roi transfer api
    public static String roi_withdraw = "https://api.pay2all.in/v1/payout/transfer";
    //apk download apk
    // public static String apk_download_url = baseUrl2 + "apk/DreamCricketers.apk";
    // public static String apk_download_url = "https://bit.ly/30Cwl6m";
    // public static String apk_download_url = "http://dreamcricketers.club/APK/DreamCricketers.apk";
    public static String apk_download_url = "https://bit.ly/3lhwXG5";
    // BIT URL  080-47181888

    // non pem ?api_key="+BIT_KEY+"&label="+BIT_LABEL
    public static String BIT_KEY = "f7ae-04fa-f36e-7c5b";
    public static String KITE_KEY = "c545an243q562m7z";
    // public static String BIT_KEY = "da10-f91a-9e86-000b";
    // public static String Bitcoin_key = "64fd-c140-6650-6004";
    public static String Bitcoin_key = "da10-f91a-9e86-000b";
    public static String BIT_LABEL = "fxtradingleague";
    public static String BIT_COIN_URL = "https://block.io/api/v2/get_new_address/";
    public static String ALPHA_VANTAGE = "3MAUM7XA8EH8ISCU";
    public static String SYMBOL = "IBM";
    public static String INTERVAL = "1min";
    public static String TIME_SERIES_DAILY_ADJUSTED = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol="+SYMBOL+"&interval="+INTERVAL+"&outputsize=compact&apikey="+ALPHA_VANTAGE;
    public static String TIME_SERIES_MONTHLY = "https://www.alphavantage.co/query?function=TIME_SERIES_MONTHLY&symbol="+SYMBOL+"&interval="+INTERVAL+"&outputsize=compact&apikey="+ALPHA_VANTAGE;
    public static String TIME_SERIES_MONTHLY_ADJUSTED = "https://www.alphavantage.co/query?function=TIME_SERIES_MONTHLY_ADJUSTED&symbol="+SYMBOL+"&interval="+INTERVAL+"&outputsize=compact&apikey="+ALPHA_VANTAGE;

}