import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'flutter_screen_data_leakage_protector_method_channel.dart';

abstract class FlutterScreenDataLeakageProtectorPlatform
    extends PlatformInterface {
  /// Constructs a FlutterScreenDataLeakageProtectorPlatform.
  FlutterScreenDataLeakageProtectorPlatform() : super(token: _token);

  static final Object _token = Object();

  static FlutterScreenDataLeakageProtectorPlatform _instance =
      MethodChannelFlutterScreenDataLeakageProtector();

  /// The default instance of [FlutterScreenDataLeakageProtectorPlatform] to use.
  ///
  /// Defaults to [MethodChannelFlutterScreenDataLeakageProtector].
  static FlutterScreenDataLeakageProtectorPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [FlutterScreenDataLeakageProtectorPlatform] when
  /// they register themselves.
  static set instance(FlutterScreenDataLeakageProtectorPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }
}
