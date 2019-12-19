package com.mikroskil.androiddasar.intent.model

import android.net.Uri

data class contact (
    var contactName : String = "",
    var contactPhoneNum : String = "",
    var contactPhoto : Uri = Uri.parse("")
)