
import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

class SubmitButton extends StatelessWidget {
  final Function() onPressed;
  final double width,circular;
  final String name;
  final int colors;
  final Color textColor;
  const SubmitButton({Key? key,required this.onPressed,required this.name,
  required this.colors,required this.textColor,required this.width,required this.circular}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(10.0, 15, 10, 0),
      child: SizedBox(
        height: 50,
        width: width,
        child: ElevatedButton(
            onPressed: onPressed,
            style: ButtonStyle(
                backgroundColor:MaterialStateProperty.all<Color>(Color(colors)),
                foregroundColor: MaterialStateProperty.all<Color>(Color(colors)),
                shape: MaterialStateProperty.all<RoundedRectangleBorder>(
                    RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(circular),
                        side: BorderSide(color: Color(colors))))),
            child: Center(
              child: Text(
                name.toUpperCase(),textAlign: TextAlign.center,style:
              GoogleFonts.adamina(color: textColor,fontSize: 12,
                  fontWeight: FontWeight.w600),
              ),
            )),
      ),
    );
  }
}
