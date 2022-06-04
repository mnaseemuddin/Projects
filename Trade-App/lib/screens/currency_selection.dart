
import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:trading_apps/api/apis.dart';
import 'package:trading_apps/api/user_data.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/res/constants.dart';
import 'package:trading_apps/utility/common_methods.dart';
import 'package:trading_apps/utility/connectivityprovider.dart';

import 'dashboard.dart';

class CurrencyDialog extends StatefulWidget {
  final List<String>currencies;
  const CurrencyDialog({Key? key, required this.currencies}) : super(key: key);

  @override
  _CurrencyDialogState createState() => _CurrencyDialogState();
}

class _CurrencyDialogState extends State<CurrencyDialog> {

  // List _currencies = ['xrp','trx','eth','zil','bat','ltc','qkc','dash','fun','gnt','iost','nuls','omg','poe','req','snt','storm','sub','theta','zrx','bchold','eos','icx','poly','fet','celr','matic','ada','rvn','xmr','atom','bnb','wrx','ftt','stmx','usdt','btc','ncash','npxs','cs','dent','stq','zco','hot','ocn','noah','banca','bch','btt','yfi','uni','link','sxp','xlm','xem','zec','busd','yfii','doge','dot','vet','easy','crv','ren','enj','mana','hbar','uma','chr','paxg','1inch','etc','uft','dock','fil','win','tko','push','avax','luna','xvg','sc','dgb','cvc','cake','ez','bzrx','ftm','hnt','ark','ctsi','kmd','coti','iotx','shib','rlc','trb','reef','icp','ont','ckb','pnt','xvs','dcr','mdx','pha','rune','ogn','mir','data','ksm','nkn','bal','dnt','keep','tusd','bchsv','pax','usdc','xtz','tfuel','algo','qtum','iota','waves','lsk','steem','loom','rep','fuel','blz','nano','btg','bts','ardr','strat','nas','chz','kava','ankr','one','erd','hive','band','jst','tomo','gxs','lrc','neo','storj','comp','grs','snx','mkr','ava','srm','egld','cos','nbs','aion','sand','cream','adx','aave','near','oxt','dusk','wtc','strax','inj','mtl','front','glm','sushi','firo','dexe','grt','bcha','bnt','rsr','pundix','vib','gto','xmgd','xoom','xstyx','xbck','xpro','xyit','xtbe','xvols','xcbt','xnis','xbstf','xfact','xolo','xstr','xpayi','xmint','xjidt'];
  // List _currencies = ['xrp', 'trx', 'eth', 'zil', 'bat', 'ltc', 'dash', 'fun', 'gnt', 'iost', 'nuls', 'omg', 'poe', 'req', 'snt', 'storm', 'sub', 'theta', 'zrx', 'eos', 'icx', 'poly', 'matic', 'ada', 'rvn', 'xmr', 'atom', 'bnb', 'usdt', 'btc', 'ncash', 'npxs', 'cs', 'dent', 'stq', 'hot', 'bch', 'btt', 'yfi', 'uni', 'link', 'xlm', 'xem', 'zec', 'doge', 'dot', 'vet', 'crv', 'ren', 'enj', 'mana', 'uma', 'paxg', 'etc', 'dock', 'fil', 'xvg', 'sc', 'dgb', 'cvc', 'ark', 'kmd', 'iotx', 'rlc', 'icp', 'ont', 'dcr', 'data', 'ksm', 'nkn', 'bal', 'dnt', 'tusd', 'pax', 'usdc', 'xtz', 'algo', 'qtum', 'waves', 'lsk', 'steem', 'loom', 'rep', 'fuel', 'blz', 'nano', 'btg', 'bts', 'ardr', 'strat', 'nas', 'ankr', 'one', 'band', 'tomo', 'gxs', 'lrc', 'neo', 'storj', 'comp', 'grs', 'snx', 'mkr', 'aion', 'sand', 'adx', 'aave', 'oxt', 'wtc', 'mtl', 'sushi', 'grt', 'bnt', 'vib', 'gto'];
  // List _currencies = [
  //   'btc',
  //   'eth',
  //   'bnb',
  //   'ada',
  //   'xrp',
  //   'bch',
  //   'doge',
  //   'dot',
  //   'uni',
  //   'ltc'];
  List _tempList = [];

  @override
  void initState() {
    Provider.of<ConnectivityProvider>(context,listen: false).startMonitoring();
    widget.currencies.sort((a, b) => a.compareTo(b));
    _tempList = widget.currencies;
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Consumer<ConnectivityProvider>(builder: (ctx,data,child){
      if(data.isOnline!=null){
        return data.isOnline?Scaffold(
          appBar: AppBar(title: Text('Select currency'),),
          backgroundColor: APP_PRIMARY_COLOR,
          body: Column(children: [
            Container(
              height: 50,
              margin: EdgeInsets.all(8),
              padding: EdgeInsets.all(8),
              child: TextFormField(
                style: TextStyle(color: Colors.white),
                onChanged: (val){
                  setState(() {
                    _tempList = widget.currencies.where((element) =>
                        element.toString().contains(val)).toList();
                  });
                },

                decoration: InputDecoration(
                  border: InputBorder.none,
                  hintText: 'Search',
                  suffixIcon: Icon(Icons.search),
                  labelStyle: TextStyle(color: Colors.white),
                  prefixStyle: TextStyle(color: Colors.white),
                ),
              ),
              decoration: BoxDecoration(
                  color: Colors.white10,
                  border: Border(bottom: BorderSide(color: APP_SECONDARY_COLOR))
              ),
            ),
            Expanded(child: ListView.builder(itemBuilder: (context, index){
              return ListTile(
                
                leading: Image.asset('assets/images/icon/${_tempList.elementAt(index)}@2x.png', width: 36,),
                title: Text(_tempList.elementAt(index).toString().toUpperCase(), style: textStyleHeading2(color: Colors.white),),
                onTap: (){
                  // baseMarket = _currencies.elementAt(index);
                  // setCurrency(baseMarket);
                  Navigator.of(context).pop({'val':_tempList.elementAt(index)});
                },
              );
            }, itemCount: _tempList.length,))
          ],),

        ):NoInternetConnection();
      }
      return Container(
        child: Center(
          child: CircularProgressIndicator(),
        ),
      );
    });
  }
}
