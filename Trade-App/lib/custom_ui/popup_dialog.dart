import 'package:flutter/material.dart';

class SelectCategoryDialog extends ModalRoute<void> {

  final String title;
  final List<String>list;
  final Function(String)onSelect;
  SelectCategoryDialog({required this.title, required this.list, required this.onSelect,});

  @override
  Duration get transitionDuration => Duration(milliseconds: 500);

  @override
  bool get opaque => false;

  @override
  bool get barrierDismissible => false;

  @override
  Color get barrierColor => Colors.black.withOpacity(0.5);

  @override
  String? get barrierLabel => null;

  @override
  bool get maintainState => true;

  @override
  Widget buildPage(
      BuildContext context,
      Animation<double> animation,
      Animation<double> secondaryAnimation,

      ) {
    // This makes sure that text and other content follows the material style
    return Material(
      type: MaterialType.transparency,
      // make sure that the overlay content is not cut off
      child: _buildOverlayContent(context),

    );
  }

  Widget _buildOverlayContent(BuildContext context) {
    return Container(
      child: Column(
        children: <Widget>[
          Expanded(child: Container()),
          Container(
            width: double.infinity,
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
              
              Container(
                child: Row(
                  children: [
                    IconButton(icon: Icon(Icons.chevron_left, size: 40,), onPressed: (){
                      Navigator.pop(context);
                    }),
                    Expanded(child: Title(color: Colors.black, child: Text(title, style: TextStyle(fontSize: 20),
                      textAlign: TextAlign.center,))),
                    SizedBox(width: 36,)

                  ],
                ),
              ),
              Divider(),
              Padding(padding: EdgeInsets.all(16),
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: list.map((e) => GestureDetector(
                  child: Container(
                    width: double.infinity,
                    padding: EdgeInsets.all(16),
                    child: Text(e),
                  ),
                  onTap: (){
                    onSelect(e);
                    Navigator.of(context).pop();
                  },
                )).toList(),
              ),
                )
            ],),

            decoration: BoxDecoration(
              color: Colors.white,
              borderRadius: BorderRadius.only(
                topLeft: Radius.circular(20),
                topRight: Radius.circular(20),
              )
            ),
          )
        ],
      ),
    );
  }

  Widget _rowAlign({head, value}) => Row(
    children: [
      Expanded(child: Text(head, style: TextStyle(color: Colors.grey, fontSize: 14.0),)),
      Text(value, style: TextStyle(color: Colors.black, fontSize: 14.0),)
    ],
  );

  @override
  Widget buildTransitions(
      BuildContext context, Animation<double> animation, Animation<double> secondaryAnimation, Widget child) {
    // You can add your own animations for the overlay content
    // return FadeTransition(
    //   opacity: animation,
    //   child: ScaleTransition(
    //     scale: animation,
    //     child: child,
    //   ),
    // );
    return SlideTransition(
      position: Tween<Offset>(
        begin: const Offset(0, 1),
        end: Offset.zero,
      ).animate(animation),
      child: child,
    
    );
  }
}
