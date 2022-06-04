import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:royal_q/app/shared/widgets/affliate_bg.dart';
import 'package:royal_q/app_controller.dart';

class OtpPage extends StatefulWidget {
  final Widget pinPut;
  final String email;
  OtpPage(this.pinPut, this.email);

  @override
  State<OtpPage> createState() => _OtpPageState();

  @override
  String toStringShort() => pinPut.toStringShort();
}

class _OtpPageState extends State<OtpPage> with AutomaticKeepAliveClientMixin {

  final appController = Get.find<AppController>();

  @override
  Widget build(BuildContext context) {
    super.build(context);
    return AFBg(child: Scaffold(
      // appBar: AppBar(
      //   backgroundColor: Colors.transparent,
      //   elevation: 0,
      //   actions: [IconButton(onPressed: () => Get.back(), icon: Icon(Icons.cancel, color: Colors.white,))],
      // ),
      backgroundColor: Colors.transparent,
      body: SafeArea(child: SingleChildScrollView(
        padding: EdgeInsets.fromLTRB(24, 0, 24, 24),
        child: Column(
          children: [
            Row(children: [

              IconButton(onPressed: () => Get.back(), icon: Icon(Icons.cancel, color: Colors.white,)),
              Spacer(),
            ],),
            SizedBox(height: 40,),
            OtpHeader(email: widget.email),
            widget.pinPut,
            const SizedBox(height: 44),
            Text(
              'Didn’t receive code?',
              style: GoogleFonts.poppins(
                fontSize: 16,
                color: Colors.white,
              ),
            ),
            GestureDetector(
              child: Text(
                'Resend',
                style: GoogleFonts.poppins(
                  fontSize: 16,
                  decoration: TextDecoration.underline,
                  color: Color(0xFFFF5555),
                ),
              ),
              onTap: (){
                appController.sendOTP(message: 'for login', email: widget.email);
              },
            ),
            // appController.sendOTP(message: 'for login', email: email.value!.data);
          ],
        ),
      )),
    ));
  }

  @override
  bool get wantKeepAlive => true;
}

class OtpHeader extends StatelessWidget {
  final String email;

  const OtpHeader({Key? key, required this.email}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisSize: MainAxisSize.min,
      children: [
        Text(
          'Verification',
          style: GoogleFonts.poppins(
            fontSize: 24,
            fontWeight: FontWeight.w700,
            color: Color(0xFFFF5555),
          ),
        ),
        const SizedBox(height: 24),
        Text(
          'Enter the code sent to the mail',
          style: GoogleFonts.poppins(
            fontSize: 16,
            color: Colors.white,
          ),
        ),
        const SizedBox(height: 16),
        Text(
          email,
          style: GoogleFonts.poppins(
            fontSize: 16,
            color: Colors.white,
          ),
        ),
        const SizedBox(height: 64)
      ],
    );
  }
}
