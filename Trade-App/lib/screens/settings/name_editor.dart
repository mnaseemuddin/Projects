import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:trading_apps/api/apis.dart';
import 'package:trading_apps/api/user_data.dart';
import 'package:trading_apps/custom_ui/common_widget.dart';
import 'package:trading_apps/custom_ui/progress_dialog.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/res/constants.dart';

class NameEditor extends StatefulWidget {
  const NameEditor({Key? key}) : super(key: key);

  @override
  _NameEditorState createState() => _NameEditorState();
}

class _NameEditorState extends State<NameEditor> {

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
          Text('Edit Name', style: textStyleTitle(color: Colors.white),),
          SizedBox(height: 24,),
          TextFormField(
            textCapitalization: TextCapitalization.sentences,
            inputFormatters: [
              LengthLimitingTextInputFormatter(20),
              FilteringTextInputFormatter.allow(RegExp(
                  r'[A-Z]|[ ]|[a-z]')),
            ],
            controller: _controller,
            style: textStyleHeading2(color: Colors.white),
            decoration: InputDecoration(
              hintText: 'Enter your nick name',
                labelText: 'Name *',
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
                "username": _controller.text.trim(),
                "name": _controller.text.trim()
              };

              _updateProfileAPI(body);
            }else{
              Fluttertoast.showToast(msg: "Please enter your name ",gravity: ToastGravity.BOTTOM);
            }

          }, title: 'Save Name')

        ],),
      )
      :UpdateView(title: 'Name updated', message: 'Your nick name has been changed successfully', onPressed: (){
        Navigator.of(context).pop();
      }),
    );
  }

  _updateProfileAPI(body){

    showDialog(context: context, builder: (context) => ProgressDialog(), barrierDismissible: false).then((value){
      print(value);
    },);

    updateProfileAPI(body).then((value){
      Navigator.of(context).pop();
      if(value.status){
        if(mounted)setState(() {
          _isUpdated = true;
          userModel!.data.first.name = _controller.text.trim();
          setUser(userModel!);
        });
        Fluttertoast.showToast(msg: value.data['message'], backgroundColor: Colors.green.withOpacity(0.5));
      }else{
        Fluttertoast.showToast(msg: value.data['message'], backgroundColor: Colors.red.withOpacity(0.5));
      }
    });
  }
}
