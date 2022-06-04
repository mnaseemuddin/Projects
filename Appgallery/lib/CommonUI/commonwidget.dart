
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:google_fonts/google_fonts.dart';

class SubmitButton extends StatelessWidget {

  final Function() onPressed;
  final String buttonName;


  const SubmitButton({Key? key,required this.onPressed,required this.buttonName}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return  Padding(
      padding: const EdgeInsets.fromLTRB(80.0, 15, 80, 0),
      child: SizedBox(
        height: 50,
        width: 190,
        child: ElevatedButton(
            onPressed: onPressed,
            style: ButtonStyle(
                overlayColor: MaterialStateProperty.all<Color>(const
                Color(0xff134072)),
                backgroundColor:MaterialStateProperty.all<Color>(Color(0xff0049A0)),
                foregroundColor: MaterialStateProperty.all<Color>(Color(0xff0049A0)),
                shape: MaterialStateProperty.all<RoundedRectangleBorder>(
                    RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(55.0),
                        side: BorderSide(color: Color(0xff0049A0))))),
            child: Text(
              buttonName.toUpperCase(),style: GoogleFonts.adamina(color: Colors.white,fontSize: 12,
                fontWeight: FontWeight.w600),
            )),
      ),
    );
  }
}

class SaveButton extends StatelessWidget {

  final Function() onPressed;
  final String buttonName;
  final int buttoncolors;
  final Color buttontextcolor;


  const SaveButton({Key? key,required this.onPressed,required this.buttonName,required this.buttoncolors,
  required this.buttontextcolor}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return  Padding(
      padding: const EdgeInsets.fromLTRB(10.0, 15, 10, 0),
      child: SizedBox(
        height: 50,
        width: 150,
        child: ElevatedButton(
            onPressed: onPressed,
            style: ButtonStyle(
                backgroundColor:MaterialStateProperty.all<Color>(Color(buttoncolors)),
                foregroundColor: MaterialStateProperty.all<Color>(Color(buttoncolors)),
                shape: MaterialStateProperty.all<RoundedRectangleBorder>(
                    RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(55.0),
                        side: BorderSide(color: Color(buttoncolors))))),
            child: Center(
              child: Text(
                buttonName.toUpperCase(),textAlign: TextAlign.center,style:
              GoogleFonts.adamina(color: buttontextcolor,fontSize: 12,
                  fontWeight: FontWeight.w600),
              ),
            )),
      ),
    );
  }
}

class ContainerBg extends StatelessWidget {

  final Widget child;
  final double height;
  final double circular;
  final Color color;
  final double width;
  const ContainerBg({Key? key,required this.child,required this.height,required this.circular,
  required this.color,required this.width}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      width: width,
      height: height,
      child: child,
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(circular),
        color: color
      ),
    );
  }
}


class ContainerBgWithRadius extends StatelessWidget {

  final Widget child;
  final double height;
  final double? circular;
  final Color? color;
  final double width;
  const ContainerBgWithRadius({Key? key,required this.child,required this.height,
    required this.width, this.circular, this.color}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      width: width,
      height: height,
      child: child,
      decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(circular!),
          color: color,
          border: Border.all(width: 1.1,color: Color(0xffe1e0e0))
      ),
    );
  }
}


class AppDetailsTextField extends StatelessWidget {

  final String label;
  final double borderWidth,borderRadius;
  TextEditingController controller;
  final int maxLength,inputLength,maxLines;
   AppDetailsTextField({Key? key,required this.label,required this.borderWidth,
  required this.borderRadius,required this.maxLength,required this.controller,
    required this.inputLength,required this.maxLines}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return TextFormField(
      controller: controller,
      maxLength: maxLength,
      textCapitalization: TextCapitalization.sentences,
      textInputAction: TextInputAction.next,
      keyboardType: TextInputType.multiline,
      maxLines: maxLines,
      inputFormatters: [
        LengthLimitingTextInputFormatter(inputLength),
      ],
      decoration: InputDecoration(
          enabledBorder: OutlineInputBorder(
              borderRadius: BorderRadius.circular(borderRadius),
              borderSide: BorderSide(width: borderWidth,color: Color(0xffece7e7))
          ),
          focusedBorder: OutlineInputBorder(
            borderRadius: BorderRadius.circular(borderRadius),
            borderSide: BorderSide(width: borderWidth,
                color: Color(0xff84a2dc)),
          ),

          // prefixIcon:Padding(
          //   padding: const EdgeInsets.all(15.0),
          //   child: Image.asset(prefixIcon,height: 8,width: 8,color: Colors.blue,),
          // ),

          label: Text(label),
          labelStyle: TextStyle(color: Colors.grey[600],fontSize: 15.5),
      ),
    );
  }
}


