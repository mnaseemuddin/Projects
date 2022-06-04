package com.lgt.Ace11;

import android.util.Log;

/**
 * Created by telasol1 on 23-Oct-18.
 */

public class Constants {

    public static int SPLASH_TIME_OUT = 2500;

    public static String LOGINTYPE = "Login";
    public static String SIGNUPTYPE = "SignUp";
    public static String VERIFYOTPTYPE = "VerifyOtp";
    public static String RESENDOTPTYPE = "ResendOtp";

    public static String FORGOTPASSWORDTYPE = "ForgotPassword";
    public static String VERIFYFORGOTPASSWORDTYPE = "VerifyForgotPassword";
    public static String UPDATENEWPASSWORDTPYE = "UpdateNewPassword";
    public static String CHANGEPASSWORDTPYE = "ChangePassword";


    public static String FIXTURESHOMETYPE = "FixturesHomeType";
    public static String LIVEHOMETYPE = "LiveHomeType";
    public static String RESULTHOMETYPE = "ResultHomeType";
    public static String MYFIXTURESTYPE = "MYFixturesType";
    public static String MYLIVETYPE = "MYLiveType";
    public static String MYRESULTTYPE = "MYResultType";
    public static String CONTESTLISTTYPE = "ContestListType";
    public static String WINNINGINFOLISTTYPE = "WinningInfoListType";
    public static String PLAYERLISTTYPE = "PlayerListType";
    public static String VIEWPROFILETYPE = "ViewProfieType";
    public static String EDITPROFILETYPE = "EditProfieType";
    public static String BANNERHOMETYPE = "BannerHomeType";


    public static String PLAYERINFOTYPE = "PlayerInfoType";

    public static String SAVETEAMTYPE = "SaveTeamType";
    public static String GETPACKAGELIST = "GetPackageList";
    public static String MYTEAMLISTTYPE = "MyTeamListType";
    public static String JOINCONTESTTYPE = "JoinContestType";
    public static String MYJOINCONTESTLISTTYPE = "MyJoinContestListType";

    public static String MYFIXTURELEADERBORADTYPE = "MyFixtureLeaderboardType";
    public static String MYFIXTURELEADERBORADTEAMTYPE = "MyFixtureLeaderboardTeamType";

    //My Live Contest Type
    public static String MYJOINLIVECONTESTLISTTYPE = "MyJoinLiveContestListType";
    //My Result Contest Type
    public static String MYJOINRESULTCONTESTLISTTYPE = "MyJoinResultLiveContestListType";


    //Live Leaderboardtype
    public static String MYLIVELEADERBORADTYPE = "MyLiveLeaderboardType";

    //Live Leaderboard team Preaview Type
    public static String MYLIVELEADERBORADTEAMTYPE = "MyLiveLeaderboardTeamType";

    public static void commonLog(String msg){

        Log.d("common_log",msg+"");
    }

    //My Account Type
    public static String MYACCOUNTTYPE = "MyAccountType";

    //My Playing History Type
    public static String MYPLAYINGHISTORYTYPE = "MyPlayingHistoryType";

    //Add Amount Type
    public static String ADDAMOUNTTYPE = "AddAmountType";
    public static String CheckAmountType = "CheckAmountType";

    //My Transaction Type
    public static String MYTRANSACTIONTYPE = "MyTransactionType";
    //NotificationType
    public static String NOTIFICATIONTYPE = "NotificationType";

    //Invited Friends List type
    public static String INVITEDFRIENDSLISTTYPE = "InvitedFriendsListType";

    //Withdraw amount user data type
    public static String WITHDRAWAMOUNTUSERDATATYPE = "WithdrawAmountUserDataType";

    //Withdrawl Request type
    public static String SUBMITWITHDRAWLREQUESTTYPE = "SubmitWithdrawlRequestType";

    //Global Ranking Request type
    public static String GLOBALRANKINGTYPE = "GlobalRankingRequestType";

    //Home Screen Banner Imager Request type
    public static String HOMEBANNERTYPE = "HomeBannerType";

    //Upload Document type
    public static String UPLOADDOCUMENTTYPE = "UploadDocumentType";

    //Update App type
    public static String UPDATEAPPTYPE = "UpdateAppType";

    //Create Contest Rank List type
    public static String RANKLISTTYPE = "RankListType";

    //Create own Contest type
    public static String CREATEOWNCONTESTTYPE = "CreateOwnContestType";

    //Create own Contest list type
    public static String CREATEOWNCONTESTLISTTYPE = "CreateOwnContestListType";

    //ForUploadImage
    public static String UpdateProfileImage="UpdateImageProfile";

    //roi withdraw header
    public static String access_token="Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6Ijk3MjQ5ODc0NmFmYzVjMjczZGE0ZGE4YjE1NjMzYjAxOWFlZTYxZTM1YzMxYjI1ZDMyZTQ2MzRjMzJiNmIwNWIwYjQ5Mjc1MTQzOTNhMTdkIn0.eyJhdWQiOiIxIiwianRpIjoiOTcyNDk4NzQ2YWZjNWMyNzNkYTRkYThiMTU2MzNiMDE5YWVlNjFlMzVjMzFiMjVkMzJlNDYzNGMzMmI2YjA1YjBiNDkyNzUxNDM5M2ExN2QiLCJpYXQiOjE1ODE3NjIyNDEsIm5iZiI6MTU4MTc2MjI0MSwiZXhwIjoxNjEzMzg0NjQxLCJzdWIiOiIxMzgiLCJzY29wZXMiOltdfQ.HpKxGFwY-YG8PFXGptYa1XLUI2HK58o7bYKtJ73hZOEXFXZ5ZlgeLI6RX_Bx0LiEEtCHMqDxSMDAe9kvcZQyvljrBzIvbqiyrOJU7ijcaXweWOckyEfZeg2Tj3nlV8SRwWqzr_qUkWO99r0RAB2HoKdq5dnm3HY7Xhtksn7qIZyOpwIz4sYGfBs3ciXVA-Mz4Qabp9sEtKzwYU0Z0WTdy4RCDLrAVCWsx7E8XFXY5CJ6pLpprVgSE6p_95CmHbcOwTMHiiLsWtiuOmx8m6bIvgtdUT-qdBcMYVDUwJQQrRhm0t6FQ-v-t4mz-I2GwqQYX9eRVjx-yqxKyVZxIAIKv3i9q2rDEm1r_YypEoySZnifiHyepr0Z1Q7vYhsZaYBTCrCG8dz7_TyJruFl52ne_CsWnvFl4GUzDjk7hAuPBoGaa_hO8nDsHo2WjS3Mas_Gk-24D835LI8CGmMiH00LGp63odfontW1VI1ek8C7i69eVMCa5Y8olCHrKgySmD-VPDsmFdKc84wfSG8x1xpPhOiozLLvFfo_2WIX6gLDUCGOEZUurxn_bK2uuBtIL6kib99S6Z1QEv96oqPUM6w394xAzns6qHrbbtjfTT5IDRxwi7bLnMd056bN4B7-flXzecVOfjBN_KQWkk5lH_HYUtsuJ-JdDTT5fjweXsTo28A";

}
