import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:trading_apps/models/common_model.dart';
import 'package:trading_apps/models/graph_model.dart';
import 'package:trading_apps/res/colors.dart';

const margin16  = EdgeInsets.all(16);
const marginT16  = EdgeInsets.only(top: 16);
const margin8   = EdgeInsets.all(8);
const padding16 = EdgeInsets.all(16);
const padding8  = EdgeInsets.all(8);

const double sizeTitle = 30;
const double sizeSubTitle = 20;
const double sizeHeading = 18;
const double sizeHeading2 = 16;
const double sizeHeading3 = 14;
const double sizeBody = 11;
const double sizeCaption = 10;
const double sizeHeaderFooter = 12;
const double sizeFootNote = 11;
const double sizeLabel = 12;

GoogleFonts googleFonts=GoogleFonts.aBeeZee(fontSize: 16,color: Colors.black) as GoogleFonts;
TextStyle textStyleTitle({color=APP_SECONDARY_COLOR, fontSize=sizeTitle})     
        => TextStyle(fontSize: fontSize,    color: color, fontWeight: FontWeight.bold);
TextStyle textStyleSubTitle({color=APP_SECONDARY_COLOR, fontSize=sizeSubTitle})     
  => TextStyle(fontSize: fontSize, color: color);
TextStyle textStyleHeading({color=APP_SECONDARY_COLOR, fontSize=sizeHeading})       
 => TextStyle(fontSize: fontSize,  color: color, fontWeight: FontWeight.bold);
TextStyle textStyleHeading2({color=APP_SECONDARY_COLOR})     
  => TextStyle(fontSize: sizeHeading2, color: color, fontWeight: FontWeight.bold);
TextStyle textStyleHeading3({color=APP_SECONDARY_COLOR})      
 => TextStyle(fontSize: sizeHeading3, color: color);
TextStyle textStyleBody({color=APP_SECONDARY_COLOR})         
  => TextStyle(fontSize: sizeBody,     color: color);
TextStyle textStyleCaption({color=APP_SECONDARY_COLOR})       
 => TextStyle(fontSize: sizeCaption,  color: color);
TextStyle textStyleHeaderFooter({color=APP_SECONDARY_COLOR}) 
  => TextStyle(fontSize: sizeCaption,  color: color);
TextStyle textStyleFootNote({color=APP_SECONDARY_COLOR})     
  => TextStyle(fontSize: sizeCaption,  color: color);
TextStyle textStyleLabel({color=APP_SECONDARY_COLOR})        
  => TextStyle(fontSize: sizeLabel,    color: color);
int mainDuration = 5;//minutes
int durationFactor = mainDuration*60*1000;
int timeDurationFactor = mainDuration*60;

double walletAmount = 0;
int selectAmount = 1;

double graphMin = 0;
double graphMax = 0.1;

double mMaxVal = double.minPositive;
double mMinVal = double.maxFinite;

// List<GraphModel>axisY = [];
// List<Trade>trade = [];
String baseMarket = 'btc';
bool isMarketChanged = true;

updateTimeDuration(duration){
  mainDuration = 10;
  durationFactor = mainDuration*60*1000;
  timeDurationFactor = mainDuration*60;
}