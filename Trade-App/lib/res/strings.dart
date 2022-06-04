
import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:trading_apps/models/common_model.dart';

const String APP_NAME        =  'Trading App';
const String LOGIN           =  'Log In';
const String SIGN_UP         =  'Register';
const String REGISTRATION    =  'Registration';
const String EMAIL           =  'E-mail *';
const String PASSWORD        =  'Password *';
const String FORGOT_PASSWORD =  'Forgot password?';

const String MSG_TERM1       =  'By registering yourself you accept the our';
const String MSG_TERM2       =  'Term & Condition agreement.';
const String SELECT_CURRENCY =  'Select currency';
const String PAYMENT_TYPE    =  'Payment type:';
const String TRADES          =  'Trades';

const String ACTIVE          =  'Active';
const String CLOSED          =  'Closed';

const String playStoreIcon   =  'assets/images/playstore-icon.png';
const String tcLogoWhite     =  'assets/images/tc_logo_white.png';

// const String btcIcon     =  'assets/images/icons/btc_icon.png';


const List<ListItem>PROFILE_ITEMS = [
  ListItem(icon: Icons.add, title: 'Wallet Transaction'),
  ListItem(icon: Icons.add, title: 'Deposit'),
  ListItem(icon: Icons.add, title: 'Withdraw'),
  ListItem(icon: Icons.add, title: 'Transactions'),
  // ListItem(icon: Icons.add, title: 'Special Offers'),
  // ListItem(icon: Icons.add, title: 'Statuses'),
   ListItem(icon: Icons.add, title: 'Privacy Policy'),
  ListItem(icon: Icons.add, title: 'Settings'),
 
];

const List<ListItemWithSubTitle>FAQ_ITEMS = [
  ListItemWithSubTitle(1, icon: FontAwesomeIcons.comment, title: 'Client Support', subtitle: 'Ask a specialist'),
  // ListItemWithSubTitle(icon: FontAwesomeIcons.comment, title: 'Personal analyst', subtitle: 'Ask a specialist'),
  // ListItemWithSubTitle(icon: FontAwesomeIcons.headset, title: 'Personal analyst', subtitle: 'Ask a specialist'),
  ListItemWithSubTitle(2, icon: FontAwesomeIcons.headset, title: 'Help Center', subtitle: 'Ask a specialist'),
  ListItemWithSubTitle(3, icon: FontAwesomeIcons.book, title: 'How to trade', subtitle: 'A quick guide'),
];
 List<String> TRADE_CURRENCIES = [
  'btc',
  'eth',
  'bnb',
  'ada',
  'xrp',
  'bch',
  'doge',
  'dot',
  'uni',
  'ltc'];

 List<String> DEPOSITE_CURRENCIES = [
  'btc',
  'eth',
  'bnb',
  'ltc',
  'trx',
  'tdc'];

List<String> WITHDRAW_CURRENCIES = [
  'bnb',
  'tdc'];