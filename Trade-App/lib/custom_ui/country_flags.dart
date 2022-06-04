import 'package:flutter/material.dart';
import 'package:trading_apps/api/apis.dart';
import 'package:trading_apps/models/country_model.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/res/constants.dart';

class CountryFlags extends StatefulWidget {
  const CountryFlags({Key? key}) : super(key: key);

  @override
  _CountryFlagsState createState() => _CountryFlagsState();
}

class _CountryFlagsState extends State<CountryFlags> {
  TextEditingController _controller = TextEditingController();
  String _filterName = '';
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Select Country'),),
      backgroundColor: APP_PRIMARY_COLOR,
      body: Column(children: [
        Container(height: 60,
        padding: EdgeInsets.only(left: 8, right: 8,top: 8),
        child: TextFormField(
          // keyboardType: TextInputType.number,
          controller: _controller,
          style: textStyleHeading2(color: Colors.white),
          onChanged: (val){
            setState(() => _filterName=val);
          },
          decoration: InputDecoration(
              labelText: 'Search',
              labelStyle: textStyleHeading(color: Colors.white54),
              filled: true,
              fillColor: Colors.white10,
              border: InputBorder.none,
              suffixIcon: Icon(Icons.search),
              enabledBorder: OutlineInputBorder(borderSide: BorderSide(color: APP_SECONDARY_COLOR.withOpacity(0.5), width: 1,)),
              focusedBorder: OutlineInputBorder(borderSide: BorderSide(color: Colors.white, width: 1,))
          ),
        ),),
        Expanded(child: FutureBuilder(
          future: countryFlagAPI(),
          builder: (BuildContext context, AsyncSnapshot<ApiResponse>snapshot){
            if(!snapshot.hasData)return Center(child: CircularProgressIndicator(color: APP_SECONDARY_COLOR,),);

            CountryModel model = snapshot.data!.data as CountryModel;

            List<Country> list = model.data;
            if(_filterName.length>0){
              list = list.where((element) => element.name.toLowerCase().contains(_filterName.toLowerCase())).toList();
            }
            // if(list == null) return Center(child: Text('Something went wrong', style: textStyleLabel(color: Colors.white),),);

            return ListView.builder(itemBuilder: (context, index){
              Country country = list.elementAt(index);
              return ListTile(
                title: Text('${country.name}(${country.countriesIsoCode})', style: textStyleHeading(color: Colors.white),),
                leading: Image.network(country.flag),
                trailing: Container(
                  width: 80,
                  padding: EdgeInsets.all(8),
                  decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(8),
                      color: APP_SECONDARY_COLOR
                  ),
                  child: Text('+${country.countriesIsdCode}', style: textStyleHeading(color: Colors.black),),
                ),
                onTap: (){
                  Navigator.of(context).pop({'code': country.countriesIsdCode});
                },
              );
            }, itemCount: list?.length,);
          },))
      ],)
    );
  }
}
