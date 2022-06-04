



class Allfavorite{

  late String MovieName;
  late String VideoID;
  late String thumbnilImage;
  late String Duration;
  late String Director;
  Allfavorite({required this.MovieName,required this.VideoID,required this.thumbnilImage,
    required this.Duration,required this.Director});



  factory Allfavorite.fromJSon(Map<String,dynamic>json){
   return Allfavorite(MovieName: json["movie_name"],
       VideoID: json["movie_id"], thumbnilImage: json["image"],Duration: json["duration"],
       Director: json["director"]);
   }
}