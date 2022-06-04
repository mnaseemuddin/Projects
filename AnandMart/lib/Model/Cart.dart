



class Cart{
  String pname;
  String pimage;
  String weight;
  int wieghtid;
  String price;
  String redemPrice;
  String aprice;
  String qnty;
  String pSubtotal;
  int finalQty;

  Cart(
      {required this.pname,
        required this.pimage,
        required this.weight,
        required this.wieghtid,
        required this.price,
        required this.redemPrice,
        required this.aprice,
        required this.qnty,
        required this.pSubtotal,required this.finalQty});

  factory Cart.formJson(Map<String,dynamic>json){
    return Cart(pname: json['pname'], pimage: json['pimage'], weight: json['weight'], wieghtid: json['wieghtid'],
        price: json['price'], redemPrice: json['redem_price'], aprice: json['aprice'], qnty: json['qnty'],
        pSubtotal: json['p_subtotal'],finalQty: json["qnty1"]);
  }


}