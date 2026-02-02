import 'flutter_screen_data_leakage_protector_platform_interface.dart';

class FlutterScreenDataLeakageProtector {
  static Future<void> applyDataLeakageWithConfig({String? overlayImage}) {
    return FlutterScreenDataLeakageProtectorPlatform.instance
        .applyDataLeakageWithConfig(overlayImage: overlayImage);
  }
}
