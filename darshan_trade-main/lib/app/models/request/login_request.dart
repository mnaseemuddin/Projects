import 'dart:convert';

class LoginRequest {
  LoginRequest({
    required this.username,
    required this.Password,
  });

  String username;
  String Password;

  factory LoginRequest.fromRawJson(String str) =>
      LoginRequest.fromJson(json.decode(str));

  String toRawJson() => json.encode(toJson());

  factory LoginRequest.fromJson(Map<String, dynamic> json) => LoginRequest(
        username: json["username"],
        Password: json["Password"],
      );

  Map<String, dynamic> toJson() => {
        "username": username,
        "Password": Password,
      };
}
