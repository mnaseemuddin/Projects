import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_barcode_scanner/flutter_barcode_scanner.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:ionicons/ionicons.dart';
import 'package:trading_apps/api/apis.dart';
import 'package:trading_apps/api/user_data.dart';
import 'package:trading_apps/custom_ui/common_widget.dart';
import 'package:trading_apps/custom_ui/progress_dialog.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/res/constants.dart';

class WithdrawalEditor extends StatefulWidget {
  const WithdrawalEditor({Key? key}) : super(key: key);

  @override
  _NameEditorState createState() => _NameEditorState();
}

class _NameEditorState extends State<WithdrawalEditor> {

  TextEditingController _controller = TextEditingController();
  bool _isUpdated = false;


  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(elevation: 0,),
      backgroundColor: APP_PRIMARY_COLOR,
      body: !_isUpdated?Container(
        padding: EdgeInsets.all(16),
        child: ListView(children: [
          Text('Withdrawal Address', style: textStyleTitle(color: Colors.white),),
          SizedBox(height: 24,),
          TextFormField(
            textCapitalization: TextCapitalization.sentences,
            controller: _controller,
            style: textStyleHeading2(color: Colors.white),
            decoration: InputDecoration(
              suffixIcon: IconButton(icon: Icon(Ionicons.scan_sharp, color: APP_SECONDARY_COLOR,), onPressed: () async {
                String barcodeScanRes = await FlutterBarcodeScanner.scanBarcode(
                    '#ff6666', 'Cancel', true, ScanMode.QR);
                String code = barcodeScanRes.substring(barcodeScanRes.indexOf(':')+1);
                _controller.text = code;
              }),
                hintText: 'Enter Your TDC Withdrawal Address',
                labelText: 'Withdrawal Address *',
                labelStyle: textStyleHeading(color: Colors.white54),
                filled: true,
                fillColor: Colors.white10,
                border: InputBorder.none,
                enabledBorder: OutlineInputBorder(borderSide: BorderSide(color: APP_SECONDARY_COLOR.withOpacity(0.5), width: 1,)),
                focusedBorder: OutlineInputBorder(borderSide: BorderSide(color: Colors.white, width: 1,))
            ),
          ),
          SizedBox(height: 40,),

          SubmitButton(onPressed: (){

            if(_controller.text.trim().isNotEmpty){
              Map body = {
                "user_id": userModel!.data.first.userId,
                "payment_address": _controller.text.trim()
              };

              _savewithdrawalAddressAPI(body);
            }else{
              Fluttertoast.showToast(msg: "Please enter your name ",gravity: ToastGravity.BOTTOM);
            }

          }, title: 'Save Withdrawal Address')

        ],),
      )
          :UpdateView(title: 'Address saved', message: 'Your TDC Withdrawal Address has been saved successfully', onPressed: (){
        Navigator.of(context).pop();
      }),
    );
  }

  _savewithdrawalAddressAPI(body){

    showDialog(context: context, builder: (context) => ProgressDialog(), barrierDismissible: false).then((value){
      print(value);
    },);

    saveWithdrawAddressAPI(body).then((value){
      Navigator.of(context).pop();
      if(value.status){
        print(value.data.toString());
        if(mounted)setState(() {
          _isUpdated = true;
          userModel!.data.first.withdrawaddress = _controller.text.trim();
          setWithdrawAddress(userModel!);
        });
        Fluttertoast.showToast(msg: value.data['message'], backgroundColor: Colors.green.withOpacity(0.5));
      }else{
        Fluttertoast.showToast(msg: value.data['message'], backgroundColor: Colors.red.withOpacity(0.5));
      }
    });
  }
}
