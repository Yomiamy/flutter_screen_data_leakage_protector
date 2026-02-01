package com.yianday.screen_leakage_protector.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

interface ISystemDialogListener {
    fun onSystemDialogEvent(reason: SystemDialogReasonEnum)
}

/** 監聽System Dialog的 BroadcastReceiver 用於偵測HOME和RecentApp按鈕的點擊 */
class SystemDialogReceiver(private val listener: ISystemDialogListener) : BroadcastReceiver() {

    companion object {
        private const val EXTRA_REASON = "reason"
        private const val REASON_HOME_KEY = "homekey"
        private const val REASON_RECENT_APPS = "recentapps"
        private const val DEBOUNCE_INTERVAL_MS = 100L
    }

    private var lastRecentTriggerTime = 0L

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != Intent.ACTION_CLOSE_SYSTEM_DIALOGS) return

        val reason = parseReason(intent.getStringExtra(EXTRA_REASON)) ?: return
        listener.onSystemDialogEvent(reason)
    }

    private fun parseReason(reason: String?): SystemDialogReasonEnum? {
        return when (reason) {
            REASON_HOME_KEY -> SystemDialogReasonEnum.HOME
            REASON_RECENT_APPS -> if (isDebounced()) SystemDialogReasonEnum.RECENT else null
            else -> SystemDialogReasonEnum.UNKNOWN
        }
    }

    private fun isDebounced(): Boolean {
        val currentTime = System.currentTimeMillis()
        return if (currentTime - lastRecentTriggerTime >= DEBOUNCE_INTERVAL_MS) {
            lastRecentTriggerTime = currentTime
            true
        } else {
            false
        }
    }
}
