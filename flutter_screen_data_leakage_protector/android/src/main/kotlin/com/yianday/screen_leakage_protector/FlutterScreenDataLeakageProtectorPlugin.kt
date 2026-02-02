package com.yianday.screen_leakage_protector

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.yianday.screen_leakage_protector.receiver.ISystemDialogListener
import com.yianday.screen_leakage_protector.receiver.SystemDialogReasonEnum
import com.yianday.screen_leakage_protector.receiver.SystemDialogReceiver
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** FlutterScreenDataLeakageProtectorPlugin */
class FlutterScreenDataLeakageProtectorPlugin :
        FlutterPlugin, MethodCallHandler, ActivityAware, Application.ActivityLifecycleCallbacks {
    // The MethodChannel that will the communication between Flutter and native Android
    //
    // This local reference serves to register the plugin with the Flutter Engine and unregister it
    // when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private var activity: Activity? = null

    // For monitoring system dialog events (like HOME key, recent app) BroadcastReceiver.
    private var systemDialogReceiver: SystemDialogReceiver? = null

    // Layer used to cover the screen with black when the application enters the background
    private var securityOverlay: View? = null

    private var overlayImageName: String? = null

    // Whether the covered layer is currently visible.
    private var isOverlayVisible = false

    private val windowFocusChangeListener =
            ViewTreeObserver.OnWindowFocusChangeListener { hasFocus -> if (hasFocus) hideOverlay() }

    override fun onMethodCall(call: MethodCall, result: Result) {
        if (call.method == "applyDataLeakageWithConfig") {
            overlayImageName = call.argument<String>("overlayImage")
            // Reset overlay to allow recreation with new config
            securityOverlay?.let {
                (it.parent as? ViewGroup)?.removeView(it)
                securityOverlay = null
            }
            result.success(null)
        } else {
            result.notImplemented()
        }
    }

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel =
                MethodChannel(
                        flutterPluginBinding.binaryMessenger,
                        "flutter_screen_data_leakage_protector"
                )
        channel.setMethodCallHandler(this)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        val currentActivity = binding.activity
        activity = currentActivity

        currentActivity.application?.registerActivityLifecycleCallbacks(this)
        currentActivity.window?.decorView?.filterTouchesWhenObscured = true
        currentActivity.window?.decorView?.viewTreeObserver?.addOnWindowFocusChangeListener(
                windowFocusChangeListener
        )

        registerSystemDialogReceiver()
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        onAttachedToActivity(binding)
    }

    override fun onDetachedFromActivity() {
        val currentActivity = activity ?: return

        unregisterSystemDialogReceiver()

        currentActivity.window?.decorView?.viewTreeObserver?.removeOnWindowFocusChangeListener(
                windowFocusChangeListener
        )

        currentActivity.application?.unregisterActivityLifecycleCallbacks(this)

        // Clean up overlay and remove from view hierarchy to avoid references to destroyed activity
        securityOverlay?.let {
            (it.parent as? ViewGroup)?.removeView(it)
            securityOverlay = null
        }

        activity = null
    }

    override fun onDetachedFromActivityForConfigChanges() {
        onDetachedFromActivity()
    }

    override fun onActivityResumed(activity: Activity) {
        if (activity == this.activity) {
            hideOverlay()
        }
    }

    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityDestroyed(activity: Activity) {}

    private fun showOverlay() {
        val currentActivity = activity ?: return
        if (securityOverlay == null) {
            val context: Context = currentActivity
            securityOverlay = if (overlayImageName != null) {
                val resId = context.resources.getIdentifier(overlayImageName, "drawable", context.packageName)
                if (resId != 0) {
                    android.widget.ImageView(context).apply {
                        setImageResource(resId)
                        scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
                    }
                } else {
                    View(context).apply { setBackgroundColor(Color.BLACK) }
                }
            } else {
                View(context).apply { setBackgroundColor(Color.BLACK) }
            }.apply {
                layoutParams =
                        ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                        )
                isClickable = true
                isFocusable = true
                setOnClickListener { visibility = View.GONE }
            }
            (currentActivity.window.decorView as ViewGroup).addView(securityOverlay)
        }
        securityOverlay?.visibility = View.VISIBLE
        isOverlayVisible = true
    }

    private fun hideOverlay() {
        securityOverlay?.visibility = View.GONE
        isOverlayVisible = false
    }

    private fun registerSystemDialogReceiver() {
        val currentActivity = activity ?: return
        systemDialogReceiver =
                SystemDialogReceiver(
                        object : ISystemDialogListener {
                            override fun onSystemDialogEvent(reason: SystemDialogReasonEnum) {
                                when (reason) {
                                    SystemDialogReasonEnum.HOME -> showOverlay()
                                    SystemDialogReasonEnum.RECENT ->
                                            if (isOverlayVisible) hideOverlay() else showOverlay()
                                    SystemDialogReasonEnum.UNKNOWN -> Unit
                                }
                            }
                        }
                )

        val filter = IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            currentActivity.registerReceiver(
                    systemDialogReceiver,
                    filter,
                    Context.RECEIVER_NOT_EXPORTED
            )
        } else {
            @Suppress("UnspecifiedRegisterReceiverFlag")
            currentActivity.registerReceiver(systemDialogReceiver, filter)
        }
    }

    private fun unregisterSystemDialogReceiver() {
        activity?.unregisterReceiver(systemDialogReceiver)
        systemDialogReceiver = null
    }
}
