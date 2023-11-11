import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'wifi_platform_interface.dart';

/// An implementation of [WifiPlatform] that uses method channels.
class MethodChannelWifi extends WifiPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('wifi');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
