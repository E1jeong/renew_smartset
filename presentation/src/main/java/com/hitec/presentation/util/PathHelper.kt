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

    fun deleteDir(path: String) {
        val dir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            path
        )
        if (dir.exists()) {
            if (dir.isDirectory) {
                dir.deleteRecursively() // delete directory and all content
            } else {
                Log.v(TAG, "Path exists but is not a directory: " + dir.path)
            }
        }
    }
}