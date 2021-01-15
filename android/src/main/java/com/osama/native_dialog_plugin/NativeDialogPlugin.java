package com.osama.native_dialog_plugin;
import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.WindowManager;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.PluginRegistry.Registrar;


/** NativeDialogPlugin */
public class NativeDialogPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private static final String CHANNEL_NAME = "native_dialog_plugin_channel";
  private BinaryMessenger messenger;
  private Activity activity;
  private Context context;
  private AlertDialog alertDialog;
  Result pluginResult;

  
  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
    onAttachedToEngine(binding.getApplicationContext(), binding.getBinaryMessenger(), null);
  }

  public static void registerWith(Registrar registrar) {
    NativeDialogPlugin plugin = new NativeDialogPlugin();
    plugin.onAttachedToEngine(registrar.context(), registrar.messenger(), registrar.activity());
  }

  private void onAttachedToEngine(Context applicationContext, BinaryMessenger binaryMessenger, Activity activity) {
    context = applicationContext;
    messenger = binaryMessenger;
    if (activity != null) {
      this.activity = activity;
    }
    channel = new MethodChannel(binaryMessenger, CHANNEL_NAME);
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    pluginResult = result;
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } 
    else if(call.method.equals("showConfirmationDialog"))
    {
      AlertDialog.Builder myDialogBox = new AlertDialog.Builder(context);
      String title = call.argument("title");
      String message = call.argument("message");
      String yesAction = call.argument("yesAction");
      String noAction = call.argument("noAction");
      boolean cancellable = call.argument("cancellable");
      boolean cancelOnTouceOutside = call.argument("cancelOnTouchOutside");
      boolean onTopOfEverything = call.argument("onTopOfEverything");
      myDialogBox.setTitle(title);
      myDialogBox.setMessage(message);
      myDialogBox.setCancelable(cancellable);
      myDialogBox.setPositiveButton(yesAction, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
          dialog.cancel();
          pluginResult.success(true);
        }
      });
      if(noAction != null) {
        myDialogBox.setNegativeButton(noAction, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
            pluginResult.success(false);
          }
        });
      }
      alertDialog = myDialogBox.create();
      if(onTopOfEverything)
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
      alertDialog.show();
    } 
    else if(call.method.equals("dismissDialog")) {
      if(alertDialog != null)
      {
        alertDialog.dismiss();
      }
    }
    else {
      pluginResult.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}
