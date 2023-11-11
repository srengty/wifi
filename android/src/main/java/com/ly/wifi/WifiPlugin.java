package com.ly.wifi;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;

/** WifiPlugin */
public class WifiPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private final WifiDelegate delegate;
  private final FlutterPluginBinding binding;
  private Activity activity = null;
  private ActivityPluginBinding activityPluginBinding = null;

  public WifiPlugin(FlutterPluginBinding binding, WifiDelegate delegate) {
    this.binding = binding;
    this.delegate = delegate;
  }

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "wifi");
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);

  }
  private void doAttach(@NonNull FlutterPluginBinding binding){
    final MethodChannel channel = new MethodChannel(binding.getBinaryMessenger(), "plugins.ly.com/wifi");
    WifiManager wifiManager = (WifiManager) binding.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    final WifiDelegate delegate = new WifiDelegate(activity, wifiManager);
    if(activityPluginBinding!=null) activityPluginBinding.addRequestPermissionsResultListener(delegate);

    // support Android O,listen network disconnect event
    // https://stackoverflow.com/questions/50462987/android-o-wifimanager-enablenetwork-cannot-work
    IntentFilter filter = new IntentFilter();
    filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
    filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    binding.getApplicationContext().registerReceiver(delegate.networkReceiver,filter);

    channel.setMethodCallHandler(new WifiPlugin(binding, delegate));
  }

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    activity = binding.getActivity();
    activityPluginBinding = binding;
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
    activity = null;
    activityPluginBinding = null;
  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
    activity = binding.getActivity();
    activityPluginBinding = binding;
  }

  @Override
  public void onDetachedFromActivity() {
    activity = null;
    activityPluginBinding = null;
  }
}
