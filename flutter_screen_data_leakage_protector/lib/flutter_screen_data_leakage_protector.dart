
import 'flutter_screen_data_leakage_protector_platform_interface.dart';

class FlutterScreenDataLeakageProtector {
  Future<String?> getPlatformVersion() {
    return FlutterScreenDataLeakageProtectorPlatform.instance.getPlatformVersion();
  }
}
