




class AllMoreMovies{
  late String MovieName;
  late String MovieID;
  late String thumbnilImgae;


  AllMoreMovies({required this.MovieName,required this.MovieID,required this.thumbnilImgae});

  factory AllMoreMovies.fromJson(Map<String,dynamic>json){
    return AllMoreMovies(MovieName: json["movie_name"], MovieID: json["movie_id"], thumbnilImgae: json["image"]);
  }

}