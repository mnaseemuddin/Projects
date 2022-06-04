


class MyOrderDetail{


  String pname;
  String pimage;
  String weight;
  String wieghtid;
  String pid;
  String price;
  String qnty;

  MyOrderDetail(
      {required this.pname,
        required this.pimage,
        required this.weight,
        required this.wieghtid,
        required this.pid,
        required this.price,
        required this.qnty});

  factory MyOrderDetail.formJson(Map<String ,dynamic> json){
    return MyOrderDetail(pname: json["pname"], pimage: json["pimage"], weight: json["weight"], wieghtid: json["wieghtid"],
        pid: json["pid"], price: json["price"], qnty: json["qnty"]);
  }

}