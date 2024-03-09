package com.github.pamugk.polyhymniamusicplayer.utils

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

fun Bitmap.convertToByteArray(): ByteArray = ByteArrayOutputStream().apply {
    compress(Bitmap.CompressFormat.JPEG, 100, this)
}.toByteArray()