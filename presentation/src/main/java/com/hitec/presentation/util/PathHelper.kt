package com.hitec.presentation.util

import android.os.Environment
import android.util.Log
import java.io.File

object PathHelper {
    private const val TAG = "PathHelper"

    fun isExistDir(path: String): File {
        val dir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            path
        )
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.v(TAG, "Failed to create directory: " + dir.path)
            }
        }
        return dir
    }
}