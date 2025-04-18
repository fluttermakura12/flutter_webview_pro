package com.wzm.webviewflutter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;

public class WebViewFlutterPlugin implements FlutterPlugin, PluginRegistry.ActivityResultListener, PluginRegistry.RequestPermissionsResultListener, ActivityAware {
  private static final String TAG = "WebViewFlutterPlugin";
  private FlutterCookieManager flutterCookieManager;
  public static Activity activity;
  private WebViewFactory factory;

  public WebViewFlutterPlugin() {}

  @Override
  public void onAttachedToEngine(FlutterPluginBinding binding) {
    BinaryMessenger messenger = binding.getBinaryMessenger();
    factory = new WebViewFactory(messenger, null);
    binding
        .getPlatformViewRegistry()
        .registerViewFactory("plugins.flutter.io/webview", factory);
    flutterCookieManager = new FlutterCookieManager(messenger);
  }

  @Override
  public void onDetachedFromEngine(FlutterPluginBinding binding) {
    if (flutterCookieManager == null) return;
    activity = null;
    flutterCookieManager.dispose();
    flutterCookieManager = null;
  }

  @Override
  public void onAttachedToActivity(ActivityPluginBinding binding) {
    Log.v(TAG,"onAttachedToActivity");
    activity = binding.getActivity();
    binding.addActivityResultListener(this);
    binding.addRequestPermissionsResultListener(this);
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
    Log.v(TAG,"onDetachedFromActivityForConfigChanges");
  }

  @Override
  public void onReattachedToActivityForConfigChanges(ActivityPluginBinding binding) {
    Log.v(TAG,"onReattachedToActivityForConfigChanges");
    activity = binding.getActivity();
    binding.addActivityResultListener(this);
    binding.addRequestPermissionsResultListener(this);
  }

  @Override
  public void onDetachedFromActivity() {
    Log.v(TAG,"onDetachedFromActivity");
    activity = null;
  }

  @Override
  public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
    Log.v(TAG,"onActivityResult");
    if (factory != null && factory.getFlutterWebView() != null) {
      return factory.getFlutterWebView().activityResult(requestCode, resultCode, data);
    }
    return false;
  }

  @Override
  public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    Log.v(TAG,"onRequestPermissionsResult");
    if (factory != null && factory.getFlutterWebView() != null) {
      return factory.getFlutterWebView().requestPermissionsResult(requestCode, permissions, grantResults);
    }
    return false;
  }
}
