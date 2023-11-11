
import 'wifi_platform_interface.dart';

class Wifi {
  Future<String?> getPlatformVersion() {
    return WifiPlatform.instance.getPlatformVersion();
  }
}
