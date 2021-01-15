import 'dart:async';

import 'package:flutter/services.dart';

class NativeDialogPlugin {
  static const MethodChannel _channel =
      const MethodChannel('native_dialog_plugin_channel');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future showDialog(
      {String title,
      String message,
      String yesAction,
      String noAction,
      bool cancellable = true,
      bool cancelOnTouchOutside = true,
      bool onTopOfEverything = false}) async {
    await _channel.invokeMethod('showConfirmationDialog', {
      "title": title,
      "message": message,
      "yesAction": yesAction,
      "noAction": noAction,
      "cancellable": cancellable,
      "cancelOnTouchOutside": cancelOnTouchOutside,
      "onTopOfEverything": onTopOfEverything
    });
  }

  static Future dismissDialog() async {
    await _channel.invokeMethod('dismissDialog');
  }
}
