



import 'package:chewie/chewie.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:video_player/video_player.dart';

class AllVideoPlayer extends StatefulWidget {
 late final String playurl;
   AllVideoPlayer({Key? key, required this.playurl}) : super(key: key);

  @override
  _VideoPlayerState createState() => _VideoPlayerState();
}

class _VideoPlayerState extends State<AllVideoPlayer> {

  late VideoPlayerController _controller;
 // late ChewieController _chewieController;
  // ignore: unused_field
 // late Future<void> _initializeVideoPlayerFuture;
double _aspectRatio = 16 / 9;
 late TargetPlatform _platform;
 late ChewieController _chewieController;
@override
  void dispose() {
     _controller.dispose();
    super.dispose();
  }


@override
  void initState() {
    _controller=VideoPlayerController.network(widget.playurl);
    _controller.setLooping(true);
    _chewieController = ChewieController(
      allowedScreenSleep: false,
      allowFullScreen: true,
      deviceOrientationsAfterFullScreen: [
        DeviceOrientation.landscapeRight,
        DeviceOrientation.landscapeLeft,
        DeviceOrientation.portraitUp,
        DeviceOrientation.portraitDown,
      ],
      videoPlayerController: _controller,
      aspectRatio: _aspectRatio,
      autoInitialize: true,
      autoPlay: true,
      showControls: true,
    );
    _chewieController.addListener(() {
      if (_chewieController.isFullScreen) {
        SystemChrome.setPreferredOrientations([
          DeviceOrientation.landscapeRight,
          DeviceOrientation.landscapeLeft,
        ]);
      } else {
         SystemChrome.setPreferredOrientations([
          DeviceOrientation.portraitUp,
          DeviceOrientation.portraitDown,
        ]);
      }
    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.black,
        body:Container(child: Chewie(controller: _chewieController))
    );
  }

}




