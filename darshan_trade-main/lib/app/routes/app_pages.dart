import 'package:get/get.dart';
import 'package:royal_q/app/data/user_data.dart';

import 'package:royal_q/app/modules/TradeSettings/bindings/trade_settings_binding.dart';
import 'package:royal_q/app/modules/TradeSettings/get_booster/get_booster_binding.dart';
import 'package:royal_q/app/modules/TradeSettings/get_booster/get_booster_view.dart';
import 'package:royal_q/app/modules/TradeSettings/views/currency_detail_view.dart';
import 'package:royal_q/app/modules/auth/forgotPassword/bindings/forgot_password_binding.dart';
import 'package:royal_q/app/modules/auth/forgotPassword/views/forgot_password_view.dart';
import 'package:royal_q/app/modules/auth/login/bindings/login_binding.dart';
import 'package:royal_q/app/modules/auth/login/views/login_view.dart';
import 'package:royal_q/app/modules/auth/registration/bindings/registration_binding.dart';
import 'package:royal_q/app/modules/auth/registration/views/registration_view.dart';
import 'package:royal_q/app/modules/dashboard/bindings/dashboard_binding.dart';
import 'package:royal_q/app/modules/dashboard/tabs/home/billing_detail/billing_detail_binding.dart';
import 'package:royal_q/app/modules/dashboard/tabs/home/billing_detail/billing_detail_by_coin.dart';
import 'package:royal_q/app/modules/dashboard/tabs/home/billing_detail/billing_detail_view.dart';
import 'package:royal_q/app/modules/dashboard/tabs/home/community/community_binding.dart';
import 'package:royal_q/app/modules/dashboard/tabs/home/community/community_view.dart';
import 'package:royal_q/app/modules/dashboard/tabs/home/leaderboard/leaderboard_binding.dart';
import 'package:royal_q/app/modules/dashboard/tabs/home/leaderboard/leaderboard_view.dart';
import 'package:royal_q/app/modules/dashboard/tabs/home/revenue_details/revenue_details_binding.dart';
import 'package:royal_q/app/modules/dashboard/tabs/home/revenue_details/revenue_details_view.dart';
import 'package:royal_q/app/modules/dashboard/tabs/mine/RewardDetails/bindings/reward_details_binding.dart';
import 'package:royal_q/app/modules/dashboard/tabs/mine/RewardDetails/views/reward_details_view.dart';
import 'package:royal_q/app/modules/dashboard/tabs/mine/Team/bindings/team_binding.dart';
import 'package:royal_q/app/modules/dashboard/tabs/mine/Team/views/team_dashboard.dart';
import 'package:royal_q/app/modules/dashboard/tabs/mine/member_center/member_center_binding.dart';
import 'package:royal_q/app/modules/dashboard/tabs/mine/member_center/member_center_view.dart';
import 'package:royal_q/app/modules/dashboard/tabs/mine/reward_detail_report/reward_detail_report_binding.dart';
import 'package:royal_q/app/modules/dashboard/tabs/mine/reward_detail_report/reward_detail_report_view.dart';
import 'package:royal_q/app/modules/dashboard/tabs/mine/security_center/security_binding.dart';
import 'package:royal_q/app/modules/dashboard/tabs/mine/security_center/security_center_view.dart';
import 'package:royal_q/app/modules/dashboard/tabs/mine/withdrawal_history/withdrawal_history_binding.dart';
import 'package:royal_q/app/modules/dashboard/tabs/mine/withdrawal_history/withdrawal_history_view.dart';
import 'package:royal_q/app/modules/dashboard/views/dashboard_view.dart';
import 'package:royal_q/app/modules/personalInfo/bindings/personal_info_binding.dart';
import 'package:royal_q/app/modules/personalInfo/views/personal_info_view.dart';
import 'package:royal_q/app/modules/splash/bindings/splash_binding.dart';
import 'package:royal_q/app/modules/splash/views/splash_view.dart';

import '../modules/dashboard/bindings/ftrade_setting_binding.dart';
import '../modules/dashboard/tabs/future/future_currency_detail_view.dart';


part 'app_routes.dart';

class AppPages {
  AppPages._();

  static const INITIAL = Routes.SPLASH;

  static final routes = [
    GetPage(
      name: _Paths.SPLASH,
      page: () => SplashView(),
      binding: SplashBinding(),
    ),
    GetPage(
      name: _Paths.DASHBOARD,
      page: () => DashboardView(),
      binding: DashboardBinding(),
    ),
    GetPage(
      name: _Paths.LOGIN,
      page: () => LoginView(),
      binding: LoginBinding(),
    ),
    GetPage(
      name: _Paths.REGISTRATION,
      page: () => RegistrationView(),
      binding: RegistrationBinding(),
    ),
    GetPage(
      name: _Paths.PERSONAL_INFO,
      page: () => PersonalInfoView(),
      binding: PersonalInfoBinding(),
    ),
    GetPage(
      name: _Paths.FUTURETRADE_SETTINGS,
      // page: () => TradeSettingsView(),
      page: () => FutureCurrencyDetailView(),
      binding: FTradeSettingsBinding(),
    ),

    GetPage(
      name: _Paths.FORGOT_PASSWORD,
      page: () => ForgotPasswordView(),
      binding: ForgotPasswordBinding(),
    ),
    GetPage(
      name: _Paths.TRADE_SETTINGS,
      // page: () => TradeSettingsView(),
      page: () => CurrencyDetailView(exchangeType: exchangeValue,),
      binding: TradeSettingsBinding(),
    ),
    GetPage(
      name: _Paths.TEAM,
      page: () => TeamDashboard(),
      binding: TeamBinding(),
    ),
    GetPage(
      name: _Paths.REWARD_DETAILS,
      page: () => RewardDetailsView(),
      binding: RewardDetailsBinding(),
    ),
    GetPage(
      name: Routes.REVENUE_DETAILS,
      page:()=> RevenueDetailsView(),
      binding: RevenueDetailsBinding(),
    ),
    GetPage(
      name: Routes.SECURITY_CENTER,
      page:()=> SecurityCenterView(),
      binding: SecurityBinding(),
    ),
    GetPage(
      name: Routes.MEMBER_CENTER,
      page:()=> MemberCenterView(),
      binding: MemberCenterBinding(),
    ),

    GetPage(
      name: Routes.BILLING_DETAIL,
      page:()=> BillingDetailView(),
      binding: BillingDetailBinding(),
    ),



    GetPage(
      name: Routes.LEADERBOARD,
      page:()=> LeaderboardView(),
      binding: LeaderboardBinding(),
    ),

    GetPage(
      name: Routes.COMMUNITY,
      page:()=> CommunityView(),
      binding: CommunityBinding(),
    ),

    GetPage(
      name: Routes.REWARD_DETAIL_REPORT,
      page:()=> RewardDetailReportView(),
      binding: RewardDetailReportBinding(),
    ),
    GetPage(
      name: Routes.GET_BOOSTER,
      page:()=> GetBoosterView(),
      binding: GetBoosterBinding(),
    ),
    GetPage(
      name: Routes.WITHDRAWAL_HISTORY,
      page:()=> WithdrawalHistoryView(),
      binding: WithdrawalHistoryBinding(),
    ),
  ];
}
