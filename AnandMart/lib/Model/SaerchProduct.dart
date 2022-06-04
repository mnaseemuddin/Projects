




class SearchProduct{
  String id;
  String code;
  String eanCode;
  String productPrice;
  String productPecWeight;
  String color;
  String productSPrice;
  String maxDiscount;
  String productTaxClass;
  String productId;
  String productName;
  String img1;
  bool IsItemAdd;
  bool IsItemAddMinus;

  SearchProduct(
      {required this.id,
        required this.code,
        required this.eanCode,
        required this.productPrice,
        required this.productPecWeight,
        required this.color,
        required this.productSPrice,
        required this.maxDiscount,
        required this.productTaxClass,
        required this.productId,
        required this.productName,
        required this.img1,required this.IsItemAdd,required this.IsItemAddMinus});

  factory SearchProduct.formJson(Map<String, dynamic> json){
    return SearchProduct(id: json["id"], code: json["code"], eanCode: json["ean_code"],
        productPrice: json["product_price"], productPecWeight: json["product_pec_weight"],
        color: json["color"], productSPrice: json["product_s_price"], maxDiscount:
        json["max_discount"], productTaxClass: json["product_tax_class"], productId: json["product_id"],
        productName: json["product_name"], img1:
        "http://anandmart.co.in/mb_beta/assets/uploads/product/"+json["img_1"],IsItemAdd: true,IsItemAddMinus: false);
  }
}