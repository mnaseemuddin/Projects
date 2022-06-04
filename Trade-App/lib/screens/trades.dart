import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:trading_apps/custom_ui/common_widget.dart';
import 'package:trading_apps/data_streamer.dart';
import 'package:trading_apps/models/common_model.dart';
import 'package:trading_apps/models/graph_model.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/res/constants.dart';
import 'package:trading_apps/res/strings.dart';
import 'package:trading_apps/screens/trades/active_trades.dart';
import 'package:trading_apps/screens/trades/closed_trades.dart';

class Trades extends StatefulWidget {
  const Trades({Key? key}) : super(key: key);

  @override
  _TradesState createState() => _TradesState();
}

class _TradesState extends State<Trades> {

  int _page = 0;
  PageController _controller = PageController();
  // List<Trade>_trade = [];

  @override
  void initState() {
    SystemChrome.setSystemUIOverlayStyle(SystemUiOverlayStyle.light);
    // _trade = trade.where((element) => element.isActive).toList();
    super.initState();
  }
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(TRADES),
      elevation: 0,),
      backgroundColor: APP_PRIMARY_COLOR,
      body: Container(
        padding: EdgeInsets.only(left: 16, right: 16),
        child: Column(children: [
          Container(height: 40,
            child: Row(children: [
              Expanded(
                  child: GestureDetector(child: _createItem(_page==0, ACTIVE), onTap: (){
                setState(() {
                  _page = 0;
                  _controller.jumpToPage(_page);
                });
              },)),
              Expanded(child: GestureDetector(child: _createItem(_page==1, CLOSED), onTap: (){
                setState(() {
                  _page = 1;
                  _controller.jumpToPage(_page);
                });
              },)),
            ],),
          ),

          Expanded(child: PageView.builder(
            controller: _controller,
            allowImplicitScrolling: false,
          //  physics: NeverScrollableScrollPhysics(),
            onPageChanged: (page){
              setState(() {
                _page = page;
              });
            },
            itemBuilder: (context, index){
            return _page==0?ActiveTrades():
            ClosedTrades();
          }, itemCount: 2,))
        ],),
      ),
    );
  }

  Widget _createItem(bool selected, String title){
    return Container(
      child: Text(title, style: textStyleLabel(color: selected?APP_SECONDARY_COLOR:Colors.white),),
      alignment: Alignment.centerLeft,
      decoration: BoxDecoration(
        border: Border(bottom: BorderSide(color: selected?APP_SECONDARY_COLOR:Colors.white, width: 2))
      ),
    );
  }
}





