



class OrderDetailsModel{

  int tblOrderProduct;
  String orderNumber;
  String productName;
  int productId;
  String unitPrice;
  int quantity;
  String totalAmount;
  String productImage;
  int total_tdc_detect;
  bool visiblityTdcAmount;

  OrderDetailsModel(
      {required this.tblOrderProduct,
        required this.orderNumber,
        required this.productName,
        required this.productId,
        required this.unitPrice,
        required this.quantity,
        required this.totalAmount,
        required this.productImage,
      required this.total_tdc_detect,required this.visiblityTdcAmount});

  factory OrderDetailsModel.fromJson(Map<String,dynamic>json){
    return OrderDetailsModel(tblOrderProduct: json["tbl_order_product"], orderNumber: json["order_number"],
        productName: json["product_name"], productId: json["product_id"], unitPrice: json["unit_price"],
        quantity: json["quantity"],
        totalAmount: json["total_amount"], productImage: json["product_image"],
    total_tdc_detect: json['total_tdc_detect']==null?0:json['total_tdc_detect'],visiblityTdcAmount: false);
  }

}