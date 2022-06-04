import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:get/get.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/data/user_data.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/modules/dashboard/controllers/dashboard_controller.dart';
import 'package:royal_q/app/shared/shared.dart';

class Transfer extends StatefulWidget {
  final double balance;
  const Transfer({Key? key, required this.balance}) : super(key: key);

  @override
  _TransferState createState() => _TransferState();
}

class _TransferState extends State<Transfer> {

  DashboardController controller = Get.find<DashboardController>();

  final TextEditingController _useridController = TextEditingController();
  final TextEditingController _amountController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();

  FieldData _userid    = FieldData(data: '', valid: false);
  FieldData _amount    = FieldData(data: '', valid: false);
  FieldData _passw     = FieldData(data: '', valid: false);

  bool _isDataValid = false;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Transfer'.tr, style: textStyleHeading2(color: ColorConstants.white),),
        iconTheme: IconThemeData(color: ColorConstants.white),
        centerTitle: true,
        systemOverlayStyle: SystemUiOverlayStyle(
          systemNavigationBarColor: ColorConstants.APP_PRIMARY_COLOR, // Navigation bar
          statusBarColor: ColorConstants.APP_PRIMARY_COLOR, // Status bar
        ),
      elevation: 0, backgroundColor: Colors.transparent,),

      body: Container(
        padding: EdgeInsets.symmetric(horizontal: 16),
        child: ListView(children: [
          SACellContainer(child: Column(mainAxisSize: MainAxisSize.min, children: [

        Container(
          padding: EdgeInsets.all(8),
          alignment: Alignment.bottomLeft,
          child: Row(children: [
            Text('Balance:'.tr, style: textStyleHeading2(),),
            Text('${widget.balance}', style: textStyleHeading(color: ColorConstants.APP_SECONDARY_COLOR),),
            Text(' USDT', style: textStyleHeading3(color: ColorConstants.APP_SECONDARY_COLOR),),
          ],),
        ),

            SizedBox(height: 8,),
            TextFieldIcon( hintText: 'enter userid', labelText: 'User ID', validate: VALIDATE.USER, onChanged: (val){
              setState(() {
                _userid = val;
                _isDataValid = _userid.valid&&_amount.valid;
              });
            }, controller: _useridController,),
            TextFieldIcon( hintText: 'Enter_amount_USDT', labelText: 'Amount'.tr, validate: VALIDATE.PNUMBER, onChanged: (val){
              setState(() {
                _amount = val;
                _isDataValid = _userid.valid&&_amount.valid;
              });
            }, textInputType: TextInputType.numberWithOptions(
              decimal: true,
              signed: false
            ),controller: _amountController,),

            TextFieldIcon(
              suffix: SuffixWidget(primary: Icon(Icons.remove_red_eye_outlined, color: ColorConstants.white54,),
                  secondary: Icon(Icons.remove_red_eye, color: ColorConstants.APP_SECONDARY_COLOR,)),
              hintText: 'Transaction_password'.tr, labelText: 'Transaction_password'.tr,  onChanged: (val){
              setState(() {
                _passw = val;
                _isDataValid = _userid.valid&&_amount.valid&&_passw.valid;
              });
            }, controller: _passwordController, obscureText: true,),


            // TextFieldIcon(suffix: SuffixWidget(primary: Icon(Icons.remove_red_eye_outlined, color: ColorConstants.white54,),
            //     secondary: Icon(Icons.remove_red_eye, color: ColorConstants.APP_SECONDARY_COLOR,)),
            //   labelText: PASSWORD, hintText: 'Enter_login_password'.tr, validate: VALIDATE.PASSWORD,
            //   obscureText: true, onChanged: (val){
            //     controller.isEmailValid(val);
            //   }, controller: controller.controllerPassword,)


            SizedBox(height: 16,),
            SubmitButton(onPressed: (){
              if(double.parse(_amountController.text.trim())>widget.balance){
                EasyLoading.showToast('Insufficient_balance'.tr);
                return;
              }
              AppFocus.unfocus(context);

              controller.generateOTPDialog(context, ' to transfer amount ${_amountController.text}', (){
                _fundTransferAPI({
                  "fromuserid": userInfo!.id,
                  "Tousername": _useridController.text.trim(),
                  "amount": double.parse(_amountController.text.trim()),
                  'Transpassword': _passwordController.text.trim()
                });
              });

              // _fundTransferAPI({
              //   "fromuserid": userInfo!.id,
              //   "Tousername": _useridController.text.trim(),
              //   "amount": double.parse(_amountController.text.trim()),
              //   'Transpassword': _passwordController.text.trim()
              // });
            }, title: 'Transfer now', isActive: _isDataValid,)
          ],))
        ],),
      ),
    );
  }



  _fundTransferAPI(Map body) async{
    EasyLoading.show();
    ApiResponse response = await fundTransferAPI(body);
    if(response.status) {
      if (mounted) {
        setState(() {});
      }
    }
    EasyLoading.showToast(response.data['message']);
    EasyLoading.dismiss();
    controller.getFundBalance();
    Get.back();

  }
}
