import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:native_dialog_plugin/native_dialog_plugin.dart';

void main() {
  const MethodChannel channel = MethodChannel('native_dialog_plugin');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await NativeDialogPlugin.platformVersion, '42');
  });
}
