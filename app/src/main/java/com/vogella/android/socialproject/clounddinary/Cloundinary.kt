package com.vogella.android.socialproject.clounddinary

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.cloudinary.android.MediaManager
@Composable
fun initCloudinary() {
    val context= LocalContext.current
    val config = mapOf(
        "cloud_name" to "drsaws8pp",
        "api_key" to "692177247654145",
        "api_secret" to "JMeDZZhVPrmQx_TrFWMpeGC9D2A"
    )
    MediaManager.init(context, config)
}