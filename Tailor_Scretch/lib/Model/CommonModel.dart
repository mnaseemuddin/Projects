class ImageTile{
  final int id;
  final String image;
  final String name;

  ImageTile({required this.id, required this.image, required this.name});
}

class IndexImage{
  final int id;
  final int zindex;
  final double top;
  final double left;
  final double width;
  final double height;
  final String image;

  IndexImage({required this.id, required this.zindex, required this.top, required this.left, required this.width, required this.height, required this.image});

}