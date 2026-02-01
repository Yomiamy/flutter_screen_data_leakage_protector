import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_screen_data_leakage_protector/flutter_screen_data_leakage_protector.dart';
import 'package:flutter_screen_data_leakage_protector/flutter_screen_data_leakage_protector_platform_interface.dart';
import 'package:flutter_screen_data_leakage_protector/flutter_screen_data_leakage_protector_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockFlutterScreenDataLeakageProtectorPlatform
    with MockPlatformInterfaceMixin
    implements FlutterScreenDataLeakageProtectorPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final FlutterScreenDataLeakageProtectorPlatform initialPlatform = FlutterScreenDataLeakageProtectorPlatform.instance;

  test('$MethodChannelFlutterScreenDataLeakageProtector is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelFlutterScreenDataLeakageProtector>());
  });

  test('getPlatformVersion', () async {
    FlutterScreenDataLeakageProtector flutterScreenDataLeakageProtectorPlugin = FlutterScreenDataLeakageProtector();
    MockFlutterScreenDataLeakageProtectorPlatform fakePlatform = MockFlutterScreenDataLeakageProtectorPlatform();
    FlutterScreenDataLeakageProtectorPlatform.instance = fakePlatform;

    expect(await flutterScreenDataLeakageProtectorPlugin.getPlatformVersion(), '42');
  });
}
