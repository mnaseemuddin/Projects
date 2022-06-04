import 'package:flutter/material.dart';
import 'package:trading_apps/api/apis.dart';
import 'package:trading_apps/models/ques_ans_model.dart';
import 'package:trading_apps/models/user_guide_model.dart';
import 'package:trading_apps/res/colors.dart';
import 'package:trading_apps/res/constants.dart';
import 'package:trading_apps/screens/faq/answer_dialog.dart';
import 'package:trading_apps/utility/common_methods.dart';

class QuestionAnswers extends StatefulWidget {
  final int id;
  final String category;
  const QuestionAnswers({Key? key, required this.id, required this.category}) : super(key: key);

  @override
  _QuestionAnswersState createState() => _QuestionAnswersState();
}

class _QuestionAnswersState extends State<QuestionAnswers> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(title: Text('Help Center'),
    elevation: 0,
    backgroundColor: Colors.white12,),
    backgroundColor: APP_PRIMARY_COLOR,
    body: Container(
    margin: EdgeInsets.all(8),
      child: FutureBuilder(
          future: questionAnswersAPI(widget.id),
          builder: (BuildContext context, AsyncSnapshot<ApiResponse>snapshot){
            if(!snapshot.hasData)return Center(child: CircularProgressIndicator(),);
            if(!snapshot.data!.status) return Center(child: Text('No data found', style: textStyleLabel(),),);
            QuesAnsModel model = snapshot.data!.data;
            return ListView.builder(itemBuilder: (context, index){
              return ListTile(
                leading: Icon(Icons.view_headline, color: APP_SECONDARY_COLOR,),
                trailing: Icon(Icons.chevron_right, color: APP_SECONDARY_COLOR,),
                title: Text(model.data.elementAt(index).question, style: textStyleHeading2(color: Colors.white),),
                onTap: (){
                  // navPush(context, AnswerDialog(title: 'title', list: ['list'], onSelect: (val){}));
                  Navigator.of(context).push(AnswerDialog(title: widget.category,
                    question: model.data.elementAt(index).question, answer: model.data.elementAt(index).answer));
                },
              );
            },itemCount: model.data.length,);
          }),
    ));
  }
}
