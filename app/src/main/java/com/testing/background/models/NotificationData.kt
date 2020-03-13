package com.testing.background.models

import android.media.RingtoneManager
import android.net.Uri

/** notification data model with default values */
data class NotificationData(
        var id: Int = 1,
        var title: String = "",
        var message: String = "",
        var autoCancel: Boolean = false,
        var ringTone: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
)