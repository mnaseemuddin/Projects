import 'dart:async';
import 'dart:convert';
import 'package:chart_sparkline/chart_sparkline.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:http/http.dart' as http;
import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:skillcoin/api/user_data.dart';
import 'package:skillcoin/customui/button.dart';
import 'package:skillcoin/customui/container.dart';
import 'package:skillcoin/customui/progressdialog.dart';
import 'package:skillcoin/customui/textview.dart';
import 'package:skillcoin/model/maeketcurrencymodel.dart';
import 'package:skillcoin/model/profiledetailsmodel.dart';
import 'package:skillcoin/res/color.dart';
import 'package:skillcoin/res/routes.dart';
import 'package:skillcoin/screens/auth/login.dart';
import 'package:skillcoin/screens/dashboard.dart';
import 'package:skillcoin/screens/profile.dart';
import 'package:skillcoin/screens/setting.dart';
import 'package:skillcoin/screens/utils/loadingoverlay.dart';
import 'package:skillcoin/screens/withdraw.dart';
import '../api/apirepositary.dart';
import '../res/constants.dart';
import 'deposit_currency.dart';

class MarketPageActivity extends StatefulWidget {
  const MarketPageActivity({Key? key}) : super(key: key);

  @override
  _MarketPageActivityState createState() => _MarketPageActivityState();
}

class _MarketPageActivityState extends State<MarketPageActivity> {
  bool isLogin = false;
  double loginAndWalletExpandedHeight = 200, marketExpandedHeight = 600;
  MarketLiveRateModel? marketLive;
  List _currencies = [];
  List<MarketLiveRateModel> currenciesList = [];
  double prevAmt = 0.0, upDown = 0.00;
  int duration = 5;
  double walletAmt = 0.0;
  ProfileDetailsModel? details;
  int quarterTurns = 0;
  List<double> listMaketUpDown = [280800.0,280600.0,280400.0,280500.0,280190.0,280100.0,
    280800.0,280600.0,280400.0,280500.0,280190.0,280100.0,280800.0,280600.0,280400.0,280500.0,280190.0,280100.0];
  @override
  void initState() {
    super.initState();

    Timer.periodic(const Duration(seconds: 5), (timer) {
      getLiveRateAPI();
    });

    getUser().then((value) {
      setState(() {
        if (value != null) {
          isLogin = true;
          _getWalletAPI();
        }
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
    return Scaffold(
      backgroundColor: PRIMARY_APP_COLOR,
      body: SafeArea(
        top: true,
        child: ListView(
          children: [
            isLogin
                ? Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        GestureDetector(
                          onTap: () {
                            navPush(context, const Setting());
                          },
                          child: Padding(
                            padding: const EdgeInsets.all(8.0),
                            child: Image.asset(
                              imgProfile,
                              height: 27,
                              width: 27,
                            ),
                          ),
                        ),
                        const Spacer(),
                      ],
                    ),
                  )
                : Container(),

            SizedBox(
              height: isLogin ? 130 : 300,
              child: isLogin
                  ? balanceAndDepositUI()
                  : Column(
                      children: [
                        Padding(
                          padding: const EdgeInsets.all(15.0),
                          child: Center(
                            child: ContainerBgRCircle(
                              width: 110,
                              height: 110,
                              color: const Color(0xffedecec),
                              circular: 60,
                              child: Padding(
                                padding: const EdgeInsets.all(20),
                                child: Image.asset(mobileVerfication),
                              ),
                            ),
                          ),
                        ),
                        const NormalHeadingText(
                            title: "Welcome To SkillCoin",
                            fontSize: 25,
                            color: PRIMARYBLACKCOLOR),
                        const NormalHeadingText(
                            title: "Join the largest crypto exchange",
                            fontSize: 14,
                            color: PRIMARYLIGHTGREYCOLOR),
                        Padding(
                          padding: const EdgeInsets.all(8.0),
                          child: SubmitButton(
                              colors: 0xfffbd536,
                              circular: 55.0,
                              width: double.infinity,
                              onPressed: () {
                                navPush(context, const Login());
                              },
                              name: "Sign Up / Log In",
                              textColor: PRIMARYBLACKCOLOR),
                        ),
                      ],
                    ),
            ),

            currenciesList.isEmpty
                ? const Center(
                    child: LoadingOverlay())
                : Stack(
                    children: [
                      Padding(
                        padding: const EdgeInsets.only(top: 65.0),
                        child: MarketContainerBg(
                          circular: 22,
                          width: double.infinity,
                          color: PRIMARYWHITECOLOR,
                          child: ListView.builder(
                              physics: const NeverScrollableScrollPhysics(),
                              shrinkWrap: true,
                              scrollDirection: Axis.vertical,
                              itemCount: currenciesList.length ?? 0,
                              itemBuilder: (ctx, i) {
                                MarketLiveRateModel currenciesRate =
                                    currenciesList.elementAt(i);

                                return Padding(
                                  padding: const EdgeInsets.only(top: 12.0),
                                  child: SizedBox(
                                    height: 52,
                                    child: Row(
                                      mainAxisAlignment:
                                          MainAxisAlignment.center,
                                      children: [
                                        Expanded(
                                            flex: 18,
                                            child: Padding(
                                              padding:
                                                  const EdgeInsets.all(6.0),
                                              child: Image.network(
                                                  currenciesRate.image),
                                            )),
                                        Expanded(
                                          flex: 20,
                                          child: Column(
                                            children: [
                                              const SizedBox(
                                                height: 5,
                                              ),
                                              Align(
                                                alignment: Alignment.centerLeft,
                                                child: Text(
                                                  currenciesRate.symbol
                                                      .toUpperCase(),
                                                  style: GoogleFonts.notoSans(
                                                    color: PRIMARYBLACKCOLOR,
                                                    fontSize: 17,
                                                  ),
                                                ),
                                              ),
                                              const SizedBox(
                                                height: 3,
                                              ),
                                              Align(
                                                alignment: Alignment.centerLeft,
                                                child: Text(currenciesRate.name,
                                                    maxLines: 1,
                                                    style: const TextStyle(
                                                      color:
                                                          PRIMARYLIGHTGREYCOLOR,
                                                      fontSize: 13,
                                                    )),
                                              ),
                                            ],
                                          ),
                                        ),
                                        Expanded(
                                            flex: 20,
                                          /* child: SizedBox(
                                             height: 20,
                                             child: Sparkline(
                                               data: listMaketUpDown,
                                             ),
                                           ),*/
                                            child: Padding(
                                              padding: const EdgeInsets.only(
                                                  left: 3.0,
                                                  right: 3,
                                                  bottom: 8),
                                              child: RotatedBox(
                                                quarterTurns: currenciesRate
                                                        .priceChangePercentage24H
                                                        .toString()
                                                        .contains("-")
                                                    ? 0
                                                    : 2,
                                                child: Image.asset(
                                                  imgGraohDown,
                                                  fit: BoxFit.contain,
                                                  color: currenciesRate
                                                          .priceChangePercentage24H
                                                          .toString()
                                                          .contains("-")
                                                      ? Colors.red
                                                      : Colors.green,
                                                ),
                                              ),
                                            )
                                        ),
                                        Expanded(
                                            flex: 30,
                                            child: Padding(
                                              padding: const EdgeInsets.only(
                                                  right: 8.0),
                                              child: Column(
                                                children: [
                                                  const SizedBox(
                                                    height: 8,
                                                  ),
                                                  Align(
                                                    alignment:
                                                        Alignment.centerRight,
                                                    child: NormalHeadingText(
                                                      title:
                                                          '\u{20B9}${currenciesRate.currentPrice.toDouble()}',
                                                      color: PRIMARYBLACKCOLOR,
                                                      fontSize: 16,
                                                    ),
                                                  ),
                                                  const SizedBox(
                                                    height: 3,
                                                  ),
                                                  Align(
                                                    alignment:
                                                        Alignment.centerRight,
                                                    child: Row(
                                                      mainAxisAlignment:
                                                          MainAxisAlignment.end,
                                                      children: [
                                                        NormalHeadingText(
                                                          title: currenciesRate
                                                                  .priceChangePercentage24H
                                                                  .toStringAsFixed(
                                                                      2) +
                                                              "%",
                                                          color: currenciesRate
                                                                  .priceChangePercentage24H
                                                                  .toString()
                                                                  .contains("-")
                                                              ? Colors.red
                                                              : Colors.green,
                                                          fontSize: 13,
                                                        ),
                                                        const Align(
                                                          alignment: Alignment
                                                              .centerRight,
                                                          child: Padding(
                                                            padding:
                                                                EdgeInsets.only(
                                                                    top: 4.0),
                                                            child:
                                                                BoldHeadingText(
                                                              title: " 24h",
                                                              color:
                                                                  Colors.grey,
                                                              fontSize: 10,
                                                            ),
                                                          ),
                                                        )
                                                      ],
                                                    ),
                                                  ),
                                                ],
                                              ),
                                            ))
                                      ],
                                    ),
                                  ),
                                );
                              }),
                        ),
                      ),
                      MarketContainerBg(
                          width: double.infinity,
                          color: PRIMARYWHITECOLOR,
                          height: 80,
                          circular: 22,
                          child: const Padding(
                            padding: EdgeInsets.all(24.0),
                            child: Text("Markets",
                                style: TextStyle(
                                    fontWeight: FontWeight.w800, fontSize: 20)),
                          )),
                    ],
                  )
          ],
        ),
      ),
    );
  }

  balanceAndDepositUI() {
    return Column(
      children: [
        const Align(
            alignment: Alignment.centerLeft,
            child: Padding(
              padding: EdgeInsets.only(left: 12.0, top: 0),
              child: GoogleFontHeadingText(
                  title: "Total Balance",
                  fontSize: 14,
                  color: PRIMARYBLACKCOLOR),
            )),
        Align(
            alignment: Alignment.centerLeft,
            child: Row(
              children: [
                Padding(
                  padding: const EdgeInsets.only(left: 12.0),
                  child: ClipRRect(
                      borderRadius: BorderRadius.circular(20),
                      child: Image.asset(
                        'assets/skl.png',
                        height: 20,
                        width: 20,
                      )),
                ),
                Padding(
                  padding: const EdgeInsets.only(left: 5.0, top: 5),
                  child: GoogleFontHeadingText(
                      title: walletAmt.toString(),
                      fontSize: 25,
                      color: PRIMARYBLACKCOLOR),
                ),
              ],
            )),
        Row(
          children: [
            Expanded(
              child: Padding(
                padding: const EdgeInsets.fromLTRB(15.0, 8, 15, 0),
                child: SizedBox(
                  height: 44,
                  child: ElevatedButton.icon(
                      style: ButtonStyle(
                          backgroundColor: MaterialStateProperty.all<Color>(
                              Color(0xffebecee)),
                          foregroundColor: MaterialStateProperty.all<Color>(
                              Color(0xffebecee)),
                          shape:
                              MaterialStateProperty.all<RoundedRectangleBorder>(
                                  RoundedRectangleBorder(
                                      borderRadius: BorderRadius.circular(30),
                                      side: const BorderSide(
                                          color: Color(0xffebecee))))),
                      onPressed: () {
                        navPush(context, const DepositCurrency());
                      },
                      label: Text(
                        'Deposit'.toUpperCase(),
                        style: TextStyle(color: PRIMARYBLACKCOLOR),
                      ),
                      icon: const Icon(
                        Icons.arrow_circle_up_outlined,
                        color: PRIMARYBLACKCOLOR,
                      )),
                ),
              ),
            ),
            Expanded(
              child: Padding(
                padding: const EdgeInsets.fromLTRB(15.0, 8, 15, 0),
                child: SizedBox(
                  height: 44,
                  child: ElevatedButton.icon(
                      style: ButtonStyle(
                          backgroundColor: MaterialStateProperty.all<Color>(
                              Color(0xfffbd536)),
                          foregroundColor: MaterialStateProperty.all<Color>(
                              Color(0xfffbd536)),
                          shape:
                              MaterialStateProperty.all<RoundedRectangleBorder>(
                                  RoundedRectangleBorder(
                                      borderRadius: BorderRadius.circular(30),
                                      side: const BorderSide(
                                          color: Color(0xfffbd536))))),
                      onPressed: () {
                        navPush(context, const Withdraw());
                      },
                      label: Text(
                        'Withdraw'.toUpperCase(),
                        style: TextStyle(color: PRIMARYBLACKCOLOR),
                      ),
                      icon: const Icon(
                        Icons.arrow_circle_down,
                        color: PRIMARYBLACKCOLOR,
                      )),
                ),
              ),
            )
          ],
        )
      ],
    );
  }

  void getLiveRateAPI() async {
    try {
      final response = await http.get(Uri.parse(currencyLiveRateURL),
          headers: currencyHeaders);
      if (response.statusCode == 200) {
        _currencies = json.decode(response.body);
        if(mounted) {
          setState(() {
          currenciesList.clear();
          for (var element in _currencies) {
            currenciesList.add(MarketLiveRateModel.fromJson(element));
          }
        });
        }
      } else {
        Fluttertoast.showToast(msg: "Something is Error");
      }
    } catch (e) {
      Fluttertoast.showToast(msg: e.toString());
    }
  }

  void _getWalletAPI() {
    Map<dynamic, String> body = {
      "email": userModel!.data.first.email,
      "token": userModel!.data.first.token
    };
    walletBalanceAPI(body).then((value) {
      if (value.status) {
        setState(() {
          details = value.data;
          walletAmt = details!.data.first.walletAmount;
        });
      }
    });
  }
}
