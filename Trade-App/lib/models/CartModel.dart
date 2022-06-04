



class CartModel{
  late int tblCartid;
  late int userId;
  late int productId;
  late int quantity;
  late String unitPrice;
  late String totalAmount;
  late String productName;
  late String productDescription;
  late String image;
  late int vendorId;
  late String tdcDetect;
  late bool visibleTdcMessage;
  late int totaltdcdetect;

  CartModel(
      {required this.tblCartid,
        required this.userId,
        required this.productId,
        required this.quantity,
        required this.unitPrice,
        required this.totalAmount,
        required this.productName,
        required this.productDescription,
        required this.image,
      required this.vendorId,
      required this.tdcDetect,
      required this.visibleTdcMessage,required this.totaltdcdetect});


  factory CartModel.fromJson(Map<String ,dynamic>json)=>
      CartModel(tblCartid: json["tbl_cart_id"],userId: json['user_id'], productId: json['product_id'], quantity: json['quantity'],
          unitPrice: json['unit_price'], totalAmount:  json['total_amount'], productName: json['product_name'],
          productDescription: json['product_description'], image: json['image'],
          vendorId: json['vendor_id'],tdcDetect: json['tdc_detect']==null?"0":json['tdc_detect'],visibleTdcMessage: false,
      totaltdcdetect: json['total_tdc_detect']);

}