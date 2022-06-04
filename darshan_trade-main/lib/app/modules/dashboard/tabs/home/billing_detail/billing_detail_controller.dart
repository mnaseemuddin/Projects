import 'package:get/get.dart';
import 'package:intl/intl.dart';
import 'package:royal_q/api/api.dart';
import 'package:royal_q/app/models/models.dart';
import 'package:royal_q/app/models/response/allfees_response.dart';
import 'package:royal_q/app/models/response/totalbilling_resposne.dart';

import '../../../../../models/response/TotalFeesAssetResposne.dart';
import '../../../../../models/response/fee_response.dart';

class BillingDetailController extends GetxController {

  final billingResponse = RxList<BillingResponse>();
  final totalBillingResponse=RxList<TotalBillingResponse>();
  final totalprofitResponse = RxList<TotalprofitResponse>();
  final allLossResponse=RxList<TotalBillingResponse>();
  final allFeesResponse=RxList<AllFeesResponse>();
  final allTotalFeesAsset=RxList<TotoalFeesResponse>();
  final totalLossResponse=RxList<TotalprofitResponse>();
  final feesResposne=RxList<FeeResponse>();

  final totalProfit = 0.0.obs;
  final todayProfit = 0.0.obs;
  final totalLoss2=0.0.obs;
  final totalFess=0.0.obs;
  final todayLoss=0.0.obs;

  @override
  void onInit(){
    super.onInit();
    totalBilling();
    billing();
    allFess();
    totalFeesAsset();
  }

  void billing() async{
    ApiResponse response = await billingAPI();
    billingResponse.value = response.data;
     /*  double todayP = 0.0;
    for(var element in billingResponse){
      DateTime now = DateTime.now();
      String formattedDate = DateFormat('MM-dd-yyyy').format(now);
      var dateSplit=element.transDate.split(" ");
      print(dateSplit[0]);
      if(formattedDate==dateSplit[0].trim()){
        todayP+=element.profit;
      }
    }
    todayProfit.value = todayP;*/
    totalprofit();
  }

  void totalprofit() async{
    ApiResponse response = await totalprofitAPI('Totalprofit');
    totalprofitResponse.value = response.data;
    double totalP = 0.0;
    double todayP = 0.0;
    for (var element in totalprofitResponse) {
      totalP += element.profit;
      if(isToday(element.transdate)){
        todayP += element.profit;
      }
    }
    totalProfit.value = totalP;
    todayProfit.value = todayP;
    allLoss();
  }

  bool isToday(time){
    final now = DateTime.now();
    DateFormat dateFormat = DateFormat("MM-dd-yyyy");
    DateTime dateToCheck = dateFormat.parse(time);
    final today = DateTime(now.year, now.month, now.day);
    final aDate = DateTime(dateToCheck.year, dateToCheck.month, dateToCheck.day);
    return aDate == today;
  }

  void allLoss() async{

    ApiResponse response=await totalBillingANdLossAPI("BillingLoss");
    allLossResponse.value=response.data;
    double totalL = 0.0;
    double todayL=0.0;

    for (var element in allLossResponse) {
      double loss=double.parse(element.profit);
      totalL += loss;
      DateTime now = DateTime.now();
      String formattedDate = DateFormat('MM-dd-yyyy').format(now);
      var dateSplit=element.transdate.split(" ");
      print(dateSplit[0]);
      if(formattedDate==dateSplit[0].trim()){
        todayL+=double.parse(element.profit);
      }
    }
    totalLoss2.value = totalL;
    todayLoss.value=todayL;
    totalLoss();
  }

  void totalBilling() async{
    ApiResponse response=await totalBillingANdLossAPI("AllBilling");
    totalBillingResponse.value=response.data;
  }


   void totalLoss()async{
       ApiResponse response = await totalprofitAPI('Totalloss');
    totalLossResponse.value = response.data;
    fees();
   }

   void fees()async{
        ApiResponse response = await feesAPI('AllFee');
       feesResposne.value = response.data;
   }
 
  void allFess() async{
    ApiResponse response=await totalFeesAPI();
    allFeesResponse.value=response.data;
    double totalF = 0.0;
    for (var element in allFeesResponse) {
      double loss=element.commission;
      totalF += loss;
    }
    totalFess.value = totalF;
  }


  void totalFeesAsset()async{
   ApiResponse response=await totalFeesAssetAPI();
   allTotalFeesAsset.value=response.data;
  }

}
