import 'package:flutter_test/flutter_test.dart';
import 'package:wifi/wifi.dart';
import 'package:wifi/wifi_platform_interface.dart';
import 'package:wifi/wifi_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockWifiPlatform
    with MockPlatformInterfaceMixin
    implements WifiPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final WifiPlatform initialPlatform = WifiPlatform.instance;

  test('$MethodChannelWifi is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelWifi>());
  });

  test('getPlatformVersion', () async {
    Wifi wifiPlugin = Wifi();
    MockWifiPlatform fakePlatform = MockWifiPlatform();
    WifiPlatform.instance = fakePlatform;

    expect(await wifiPlugin.getPlatformVersion(), '42');
  });
}
