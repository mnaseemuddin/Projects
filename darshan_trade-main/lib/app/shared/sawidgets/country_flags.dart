import 'package:flutter/material.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/main.dart';
import 'package:get/get.dart';
import '../shared.dart';



class CountryFlags extends StatefulWidget {
  const CountryFlags({Key? key}) : super(key: key);

  @override
  _CountryFlagsState createState() => _CountryFlagsState();
}

class _CountryFlagsState extends State<CountryFlags> {
  final TextEditingController _controller = TextEditingController();
  String _filterName = '';
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Select_Country'.tr, style: TextStyle(color: ColorConstants.iconTheme),),
      backgroundColor: Colors.transparent, elevation: 0,
        brightness: Brightness.dark,
      iconTheme: IconThemeData(color: ColorConstants.iconTheme),),
      backgroundColor: ColorConstants.containerColor,
      body: Column(children: [
        Container(height: 60,
        padding: EdgeInsets.only(left: 8, right: 8),
        child: TextFormField(
          // keyboardType: TextInputType.number,
          controller: _controller,
          style: textStyleHeading2(color: ColorConstants.iconTheme),
          onChanged: (val){
            setState(() => _filterName=val);
          },
          decoration: InputDecoration(
              labelText: 'search'.tr,
              labelStyle: textStyleHeading(color: ColorConstants.white54),
              filled: true,
              fillColor: ColorConstants.white10,
              border: InputBorder.none,
              suffixIcon: Icon(Icons.search),
              enabledBorder: OutlineInputBorder(borderSide: BorderSide(color: ColorConstants.APP_SECONDARY_COLOR.withOpacity(0.5), width: 1,)),
              focusedBorder: OutlineInputBorder(borderSide: BorderSide(color: ColorConstants.iconTheme, width: 1,))
          ),
        ),),
        Expanded(child: FutureBuilder(
          future: locationListAPI(),
          builder: (BuildContext context, AsyncSnapshot<ApiResponse>snapshot){
            if(!snapshot.hasData)return Center(child: CircularProgressIndicator(color: ColorConstants.APP_SECONDARY_COLOR,),);

            List<CountryModel> list = snapshot.data!.data;
            if(_filterName.isNotEmpty){
              list = list.where((element) => element.name.toLowerCase().contains(_filterName.toLowerCase())).toList();
            }
            return ListView.builder(itemBuilder: (context, index){
              CountryModel country = list.elementAt(index);
              return ListTile(
                title: Text(country.name, style: textStyleHeading(color: ColorConstants.white),),
                trailing: Text('${country.phonecode}'),
                onTap: (){
                  Navigator.of(context).pop({'code': country.name, 'name': country.phonecode});
                },
              );
            }, itemCount: list?.length,);
          },))
      ],)
    );
  }
}
