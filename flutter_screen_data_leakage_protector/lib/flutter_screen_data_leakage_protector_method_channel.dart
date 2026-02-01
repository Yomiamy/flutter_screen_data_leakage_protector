import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'flutter_screen_data_leakage_protector_platform_interface.dart';

/// An implementation of [FlutterScreenDataLeakageProtectorPlatform] that uses method channels.
class MethodChannelFlutterScreenDataLeakageProtector extends FlutterScreenDataLeakageProtectorPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('flutter_screen_data_leakage_protector');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
