import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'wifi_method_channel.dart';

abstract class WifiPlatform extends PlatformInterface {
  /// Constructs a WifiPlatform.
  WifiPlatform() : super(token: _token);

  static final Object _token = Object();

  static WifiPlatform _instance = MethodChannelWifi();

  /// The default instance of [WifiPlatform] to use.
  ///
  /// Defaults to [MethodChannelWifi].
  static WifiPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [WifiPlatform] when
  /// they register themselves.
  static set instance(WifiPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
