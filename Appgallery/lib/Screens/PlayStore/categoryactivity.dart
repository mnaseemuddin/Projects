




import 'package:flutter/material.dart';

class CategoryActivity extends StatefulWidget {
  const CategoryActivity({Key? key}) : super(key: key);

  @override
  _CategoryActivityState createState() => _CategoryActivityState();
}

class _CategoryActivityState extends State<CategoryActivity> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xfff1f3f5),
      appBar: PreferredSize(preferredSize: const Size.fromHeight(80),
        child: Padding(
          padding: const EdgeInsets.only(top:50.0,left: 20,bottom: 15),
          child: Row(
            children: [
              Padding(
                padding: const EdgeInsets.only(right:8.0),
                child: GestureDetector(
                    onTap: (){
                      Navigator.pop(context,false);
                    },
                    child: Icon(Icons.arrow_back_ios)),
              ),
              const Padding(
                padding: EdgeInsets.only(left:8.0),
                child: Text('Category',style: TextStyle(fontSize: 17.5,color: Colors.black87,
                    fontWeight: FontWeight.w500),),
              ),
              const Spacer(),Padding(
                padding: const EdgeInsets.only(right:12.0),
                child: Image.asset('assets/loupe.png',height: 15,width: 15,),
              ),
            ],),
        ),
        // backgroundColor: Color(0xfff1f3f5),
      ),
      body: ListView(children: [
        const Padding(
          padding: EdgeInsets.only(left:14.0,top: 10),
          child: Text('All Categorries'),
        ),
        Padding(
          padding: const EdgeInsets.all(20.0),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Container(
                decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(15),
                    color: Colors.white
                ),
                child: Column(children: [
                  Padding(
                    padding: const EdgeInsets.only(top:10.0,left:12),
                    child: Row(
                      children: [
                        Image.asset('assets/business.png',height: 30,width: 30,),
                        Padding(
                          padding: const EdgeInsets.only(left:18.0),
                          child: Text('Business',style: TextStyle(color: Colors.black,fontSize: 16),),
                        ),
                        Spacer(),
                        Padding(
                          padding: const EdgeInsets.only(right:18.0),
                          child: Icon(Icons.arrow_forward_ios_outlined,color: Colors.black54,size: 15,),
                        )
                      ],
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(left:57.0,top: 8,right:15),
                    child: Container(height: 0.8,width: double.infinity,color: Colors.grey[300],),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(top:10.0,left:12),
                    child: Row(
                      children: [
                        Image.asset('assets/car.png',height: 30,width: 30,),
                        Padding(
                          padding: const EdgeInsets.only(left:18.0),
                          child: Text('Cars',style: TextStyle(color: Colors.black,fontSize: 16),),
                        ),
                        Spacer(),
                        Padding(
                          padding: const EdgeInsets.only(right:18.0),
                          child: Icon(Icons.arrow_forward_ios_outlined,color: Colors.black54,size: 15,),
                        )
                      ],
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(left:57.0,top: 8,right:15),
                    child: Container(height: 0.8,width: double.infinity,color: Colors.grey[300],),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(top:10.0,left:12),
                    child: Row(
                      children: [
                        Image.asset('assets/graduationcap.png',height: 30,width: 30,),
                        const Padding(
                          padding: EdgeInsets.only(left:18.0),
                          child: Text('Education',style: TextStyle(color: Colors.black,fontSize: 16),),
                        ),
                        const Spacer(),
                        const Padding(
                          padding: EdgeInsets.only(right:18.0),
                          child: Icon(Icons.arrow_forward_ios_outlined,color: Colors.black54,size: 15,),
                        )
                      ],
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(left:57.0,top: 8,right:15),
                    child: Container(height: 0.8,width: double.infinity,color: Colors.grey[300],),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(top:10.0,left:12),
                    child: Row(
                      children: [
                        Image.asset('assets/theater.png',height: 30,width: 30,),
                        const Padding(
                          padding: EdgeInsets.only(left:18.0),
                          child: Text('Entertainment',style: TextStyle(color: Colors.black,fontSize: 16),),
                        ),
                        const Spacer(),
                        const Padding(
                          padding: EdgeInsets.only(right:18.0),
                          child: Icon(Icons.arrow_forward_ios_outlined,color: Colors.black54,size: 15,),
                        )
                      ],
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(left:57.0,top: 8,right:15),
                    child: Container(height: 0.8,width: double.infinity,color: Colors.grey[300],),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(top:10.0,left:12),
                    child: Row(
                      children: [
                        Image.asset('assets/stats.png',height: 30,width: 30,),
                        Padding(
                          padding: const EdgeInsets.only(left:18.0),
                          child: Text('Finance',style: TextStyle(color: Colors.black,fontSize: 16),),
                        ),
                        Spacer(),
                        Padding(
                          padding: const EdgeInsets.only(right:18.0),
                          child: Icon(Icons.arrow_forward_ios_outlined,color: Colors.black54,size: 15,),
                        )
                      ],
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(left:57.0,top: 8,right:15),
                    child: Container(height: 0.8,width: double.infinity,color: Colors.grey[300],),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(top:10.0,left:12),
                    child: Row(
                      children: [
                        Image.asset('assets/target.png',height: 30,width: 30,),
                        Padding(
                          padding: const EdgeInsets.only(left:18.0),
                          child: Text('Action',style: TextStyle(color: Colors.black,fontSize: 16),),
                        ),
                        Spacer(),
                        Padding(
                          padding: const EdgeInsets.only(right:18.0),
                          child: Icon(Icons.arrow_forward_ios_outlined,color: Colors.black54,size: 15,),
                        )
                      ],
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(left:57.0,top: 8,right:15),
                    child: Container(height: 0.8,width: double.infinity,color: Colors.grey[300],),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(top:10.0,left:12),
                    child: Row(
                      children: [
                        Image.asset('assets/cardgame.png',height: 30,width: 30,),
                        Padding(
                          padding: const EdgeInsets.only(left:18.0),
                          child: Text('Card & board',style: TextStyle(color: Colors.black,fontSize: 16),),
                        ),
                        Spacer(),
                        Padding(
                          padding: const EdgeInsets.only(right:18.0),
                          child: Icon(Icons.arrow_forward_ios_outlined,color: Colors.black54,size: 15,),
                        )
                      ],
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(left:57.0,top: 8,right:15),
                    child: Container(height: 0.8,width: double.infinity,color: Colors.grey[300],),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(top:10.0,left:12),
                    child: Row(
                      children: [
                        Image.asset('assets/jigsaw.png',height: 30,width: 30,),
                        Padding(
                          padding: const EdgeInsets.only(left:18.0),
                          child: Text('Puzzle & casual',style: TextStyle(color: Colors.black,fontSize: 16),),
                        ),
                        Spacer(),
                        Padding(
                          padding: const EdgeInsets.only(right:18.0),
                          child: Icon(Icons.arrow_forward_ios_outlined,color: Colors.black54,size: 15,),
                        )
                      ],
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(left:57.0,top: 8,right:15),
                    child: Container(height: 0.8,width: double.infinity,color: Colors.grey[300],),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(top:10.0,left:12),
                    child: Row(
                      children: [
                        Image.asset('assets/roleplayinggame.png',height: 30,width: 30,),
                        Padding(
                          padding: const EdgeInsets.only(left:18.0),
                          child: Text('Role-playing',style: TextStyle(color: Colors.black,fontSize: 16),),
                        ),
                        Spacer(),
                        Padding(
                          padding: const EdgeInsets.only(right:18.0),
                          child: Icon(Icons.arrow_forward_ios_outlined,color: Colors.black54,size: 15,),
                        )
                      ],
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(left:57.0,top: 8,right:15),
                    child: Container(height: 0.8,width: double.infinity,color: Colors.grey[300],),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(top:10.0,left:12),
                    child: Row(
                      children: [
                        Image.asset('assets/sportsgame.png',height: 30,width: 30,),
                        Padding(
                          padding: const EdgeInsets.only(left:18.0),
                          child: Text('Sports games',style: TextStyle(color: Colors.black,fontSize: 16),),
                        ),
                        Spacer(),
                        Padding(
                          padding: const EdgeInsets.only(right:18.0),
                          child: Icon(Icons.arrow_forward_ios_outlined,color: Colors.black54,size: 15,),
                        )
                      ],
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(left:57.0,top: 8,right:15),
                    child: Container(height: 0.8,width: double.infinity,color: Colors.grey[300],),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(top:10.0,left:12),
                    child: Row(
                      children: [
                        Image.asset('assets/digitalstrategy.png',height: 30,width: 30,),
                        Padding(
                          padding: const EdgeInsets.only(left:18.0),
                          child: Text('Strategy',style: TextStyle(color: Colors.black,fontSize: 16),),
                        ),
                        Spacer(),
                        Padding(
                          padding: const EdgeInsets.only(right:18.0),
                          child: Icon(Icons.arrow_forward_ios_outlined,color: Colors.black54,size: 15,),
                        )
                      ],
                    ),
                  ),
                  SizedBox(height: 10,)
                ],),
              )

          ],),
        )
      ],),
    );
  }
}
