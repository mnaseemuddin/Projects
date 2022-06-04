package com.lgt.fxtradingleague.Extra;

public interface ExtraData {

    // String BASE_URL = "http://rtmprime.com/ptl11/api/";
    String BASE_URL = "http://ptl11.in/api/";
    String WEB_URL = "http://ptl11.in/";
    // {"message":"The currency pair 'BTCUSD' was not recognised or supported","supportedPairs":["AUDUSD","EURGBP","EURUSD","GBPUSD","NZDUSD","USDAED","USDAFN","USDALL","USDAMD","USDANG","USDAOA","USDARS","USDATS","USDAUD","USDAWG","USDAZM","USDAZN","USDBAM","USDBBD","USDBDT","USDBEF","USDBGN","USDBHD","USDBIF","USDBMD","USDBND","USDBOB","USDBRL","USDBSD","USDBTN","USDBWP","USDBYN","USDBYR","USDBZD","USDCAD","USDCDF","USDCHF","USDCLP","USDCNH","USDCNY","USDCOP","USDCRC","USDCUC","USDCUP","USDCVE","USDCYP","USDCZK","USDDEM","USDDJF","USDDKK","USDDOP","USDDZD","USDEEK","USDEGP","USDERN","USDESP","USDETB","USDEUR","USDFIM","USDFJD","USDFKP","USDFRF","USDGBP","USDGEL","USDGGP","USDGHC","USDGHS","USDGIP","USDGMD","USDGNF","USDGRD","USDGTQ","USDGYD","USDHKD","USDHNL","USDHRK","USDHTG",USDHUF,USDIDR,USDIEP,USDILS,USDIMP,USDINR,USDIQD,USDIRR,USDISK,USDITL,USDJEP,USDJMD,USDJOD,"USDJPY","USDKES","USDKGS","USDKHR","USDKMF","USDKPW","USDKRW","USDKWD","USDKYD","USDKZT","USDLAK","USDLBP","USDLKR","USDLRD","USDLSL","USDLTL","USDLUF","USDLVL","USDLYD","USDMAD","USDMDL","USDMGA","USDMGF","USDMKD","USDMMK","USDMNT","USDMOP","USDMRO","USDMRU","USDMTL","USDMUR","USDMVR","USDMWK","USDMXN","USDMYR","USDMZM","USDMZN","USDNAD","USDNGN","USDNIO","USDNLG","USDNOK","USDNPR","USDNZD","USDOMR","USDPAB","USDPEN","USDPGK","USDPHP","USDPKR","USDPLN","USDPTE","USDPYG","USDQAR","USDROL","USDRON","USDRSD","USDRUB","USDRWF","USDSAR","USDSBD","USDSCR","USDSDD","USDSDG","USDSEK","USDSGD","USDSHP","USDSIT","USDSKK","USDSLL","USDSOS","USDSPL","USDSRD","USDSRG","USDSTD","USDSTN","USDSVC","USDSYP","USDSZL","USDTHB","USDTJS","USDTMM","USDTMT","USDTND","USDTOP","USDTRL","USDTRY","USDTTD","USDTVD","USDTWD","USDTZS","USDUAH","USDUGX","USDUSD","USDUYU","USDUZS","USDVAL","USDVEB","USDVEF","USDVES","USDVND","USDVUV","USDWST","USDXAF","USDXAG","USDXAU","USDXBT","USDXCD","USDXDR","USDXOF","USDXPD","USDXPF","USDXPT","USDYER","USDZAR","USDZMK","USDZMW","USDZWD"],"code":1002}
    String Url = "https://www.freeforexapi.com/api/live?pairs=USDWST,USDXAF,USDXAG,USDXAU,USDXBT,USDXCD,USDXDR,USDXOF,USDXPD,USDXPF,USDXPT,USDYER,USDZAR,USDZMK,USDZMW,USDZWD,USDHUF,USDIDR,USDIEP,USDILS,USDIMP,USDINR,USDIQD,USDIRR,USDISK,USDITL,USDJEP,USDJMD,USDJOD";
    String SELL_AND_BUY_UPDATE = BASE_URL+"update_sell_buy_player_api.php";
    String SELECTED_PLAYER_API = BASE_URL+"selected_player_in_team_api.php";
    String CURRENCY_LIST_API = BASE_URL+"currency_list_api.php";
    String VIEW_TEAM_LIST = BASE_URL+"view_team_api.php";
    String JOIN_CONTEST_API = BASE_URL+"join_contest_api.php";
    String CONTEST_LIST_API = BASE_URL+"contest_list.php";
    String CREATE_TEAM_API = BASE_URL+"creat_team_api.php";
    String HOME_SLIDER_API = BASE_URL+"home_slider_api.php";
    String LOGIN_API = BASE_URL+"login.php";
    String PROFILE_API = BASE_URL+"my_profile.php";
    String EDIT_PROFILE_API = BASE_URL+"edit_profile.php";
    String UPDATE_PROFILE_IMAGE_API = BASE_URL+"profile_image.php";
    String RESET_PASSWORD_API = BASE_URL+"reset_password.php";
    String UPLOAD_DOCUMENTS_API = BASE_URL+"upload_document_api.php";
    String SIGN_UP_API = BASE_URL+"signup_api.php";
    String WITHDRAW_AMOUNT_API = BASE_URL+"withdraw_amount_api.php";
    String AMOUNT_WITHDRAW_LIST_API = BASE_URL+"amount_withdraw_list_api.php";
    // display stock book by user
    String JOIN_CONTEST_LIST_API = BASE_URL+"join_contest_list_api.php";
    String CONTEST_JOIN_TEAM_LIST_API = BASE_URL+"contest_join_team_list_api.php";

    // KEYS
    String KEYS_TEAM_ID = "KEYS_TEAM_ID";
    String KEYS_CONTEST_ID = "KEYS_CONTEST_ID";
    String KEYS_CONTEST_TYPE = "contest_type";
    String KEYS_USER_ID = "KEYS_USER_ID";
    String KEY_ENTRY_FEE = "KEY_ENTRY_FEE";
    String KEY_CURRENCY_TYPE = "currency_type";
    String KEY_JOIN_MATCH = "KEY_JOIN_MATCH";
    String KEY_JOIN_ID = "KEY_JOIN_ID";
    String KEY_ENTRY_FEES = "EntryFee";

    // main event Key

    String KEY_WORLD_LEAGUE = "World League";
    String KEY_INDI_LEAGUE = "Indie League";
    String KEY_CRYPTO_LEAGUE = "Crypto League";

    // Fragment title
    String KEY_CHART_TITLE = "KEY_CHART_TITLE";

    // world league Keys

    // world league contest data api
    String WORLD_COMPANY_LIST_API = BASE_URL+"world_company_list_api.php";
    String WORLD_TEAM_LEAGUE_API = BASE_URL+"create_world_league_team_api.php";
    String WORLD_TEAM_SELECTED_PLAYER_API = BASE_URL+"selected_player_in_world_team_api.php";
    String UPDATE_WORLD_TEAM_BUY_OR_SALE_API = BASE_URL+"update_sell_buy_player_in_world_league_team_api.php";
    String WORLD_LEAGUE_CONTEST_LIST_API = BASE_URL+"worlds_contest_list_api.php";
    String FINAL_JOIN_WORLD_LEAGUE_CONTEST_API = BASE_URL+"join_world_league_contest_api.php";
    // view result...
    String JOIN_WORLD_CONTEST_LEAGUE_API = BASE_URL+"world_join_contest_list_api.php";
    String TEAM_JOIN_WORLD_CONTEST_LEAGUE_API = BASE_URL+"world_join_contest_team_list_api.php";
    String VIEW_WORLD_TEAM_DISPLAY_API = BASE_URL+"view_world_team_api.php";

    // web view links
    String TERM_AND_CONDITION_LINK = WEB_URL+"term_and_condition.html";
    String PTL_POINT_SYSTEM_LINK = WEB_URL+"fantasy_point_system.html";
    String PTL_DISCLAIMER_LINK = WEB_URL+"disclaimer.html";

    // Indi league Keys
    String INDI_CONTEST_LIST_API = BASE_URL+"indian_contest_list_api.php";
    String INDI_COMPANY_RATE_LIST_API = BASE_URL+"indian_company_rate_list_api.php";
    String CREATE_INDI_COMPANY_JOIN_TEAM_API = BASE_URL+"create_indian_league_team_api.php";
    String SELECTED_PLAYER_INDIAN_LEAGUE_TEAM_API = BASE_URL+"selected_player_in_indian_league_team_api.php";
    String UPDATE_SELL_PLAYER_INDIAN_LEAGUE_TEAM_API = BASE_URL+"update_sell_buy_players_in_indian_league_api.php";
    String JOIN_INDIAN_LEAGUE_CONTEST_TEAM_API = BASE_URL+"join_indian_league_contest_api.php";
    String INDIAN_JOIN_CONTEST_LIST_API = BASE_URL+"indian_join_contest_list_api.php";
    String INDIAN_JOIN_CONTEST_JOIN_TEAM_LIST_API = BASE_URL+"indian_contest_join_team_list_api.php";
    String INDIAN_TEAM_VIEW_LIST_API = BASE_URL+"view_indian_team_api.php";

    // add Payment API Rozor Pay

    String Add_Amount_API_API = BASE_URL+"add_amount_api.php";

    // Rozor Pay Key Id
    String PAYMENT_KEY_ID = "rzp_live_DdPd3uvoW24GP8";
    String key_secret  = "EfsarNNYFDrE6Ff6X9WZjO7A";


}
