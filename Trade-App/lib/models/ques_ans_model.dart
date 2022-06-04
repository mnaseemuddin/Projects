// To parse this JSON data, do
//
//     final quesAnsModel = quesAnsModelFromJson(jsonString);

import 'package:meta/meta.dart';
import 'dart:convert';

QuesAnsModel quesAnsModelFromJson(String str) => QuesAnsModel.fromJson(json.decode(str));

String quesAnsModelToJson(QuesAnsModel data) => json.encode(data.toJson());

class QuesAnsModel {
  QuesAnsModel({
    required this.status,
    required this.message,
    required this.data,
  });

  final String status;
  final String message;
  final List<Question> data;

  factory QuesAnsModel.fromJson(Map<String, dynamic> json) => QuesAnsModel(
    status: json["status"],
    message: json["message"],
    data: List<Question>.from(json["data"].map((x) => Question.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "status": status,
    "message": message,
    "data": List<dynamic>.from(data.map((x) => x.toJson())),
  };
}

class Question {
  Question({
    required this.tblTutorialQustAnsId,
    required this.question,
    required this.answer,
  });

  final int tblTutorialQustAnsId;
  final String question;
  final String answer;

  factory Question.fromJson(Map<String, dynamic> json) => Question(
    tblTutorialQustAnsId: json["tbl_tutorial_qust_ans_id"],
    question: json["question"],
    answer: json["answer"],
  );

  Map<String, dynamic> toJson() => {
    "tbl_tutorial_qust_ans_id": tblTutorialQustAnsId,
    "question": question,
    "answer": answer,
  };
}
