import 'package:get/get.dart';
// enum VALIDATE {USER, EMAIL, PASSWORD, PHONE, ADDRESS, DOB, CIVIL, OTP}
class AppTextFieldController extends GetxController{

  String value = ''.obs();
  bool isValid = false.obs();
  bool obscureText = false.obs();

  // bool validate(valid) {
  //   switch (valid) {
  //     case VALIDATE.USER:
  //       return validateUser(value);
  //     case VALIDATE.EMAIL:
  //       return validEmail(value);
  //     case VALIDATE.DOB:
  //       return validDOB(value);
  //     case VALIDATE.PASSWORD:
  //       return validPassword(value);
  //     case VALIDATE.OTP:
  //       return validOTP(value);
  //     case VALIDATE.CIVIL:
  //       return validCivilNumber(value);
  //     case VALIDATE.PHONE:
  //       return validPhone(value);
  //     default:
  //       return false;
  //   }
  // }


//   bool validateUser(String data){
//     return data.length > 3;
//   }
// //
//   bool validEmail(emailAddress){
//     bool emailValid = RegExp(r"^[a-zA-Z0-9.a-zA-Z0-9.#$%&'*+-/=?^_`{|}~]+@[a-zA-Z0-9]+\.[a-zA-Z]+").hasMatch(emailAddress);
//     return emailValid;
//   }
//
//   bool validDOB(String dob){
//     bool isValid = dob.length > 0;
//     return isValid;
//   }
//
//   bool validCivilNumber(String data){
//     return data.length > 6;
//   }
//
//   bool valid(String data){
//     return data.length > 3;
//   }
//
//   bool validContact(String data){
//     bool isValid = RegExp(r"^[6-9]\d{9}$").hasMatch(data);
//     return isValid;
//   }
//
//   bool validPassword(String data){
//     return data.length > 4;
//   }
//
//   bool validOTP(String data){
//     return data.length > 5;
//   }
//
//   bool validPhone(String data){
//     bool isValid = RegExp(r"^[6-9]\d{9}$").hasMatch(data);
//     return isValid;
//   }
}