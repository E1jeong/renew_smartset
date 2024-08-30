package com.hitec.presentation.main.device_detail

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import java.io.IOException

object ImageManager {
    private const val TAG = "ImageManager"
    private const val METER_OLD = "meter old"
    private const val METER_NEW = "meter new"
    private const val TERMINAL_OLD = "terminal old"
    private const val TERMINAL_NEW = "terminal new"
    private const val BACKGROUND = "background"
    private const val SURROUNDINGS = "surroundings"
    private const val BEFORE_AS = "before as"
    private const val AFTER_AS = "after as"

    fun parsePhotoTypeCd(photoTypeCd: Int): String {
        return when (photoTypeCd) {
            1 -> METER_OLD
            2 -> METER_NEW
            3 -> TERMINAL_OLD
            4 -> TERMINAL_NEW
            5 -> BACKGROUND
            6 -> SURROUNDINGS
            7 -> BEFORE_AS
            8 -> AFTER_AS
            else -> ""
        }
    }

    fun saveBase64ToImage(
        context: Context,
        base64Str: String,
        fileName: String,
        imagePath: String
    ): Uri? {
        return try {
            val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
            val path = Environment.DIRECTORY_DOCUMENTS + "/$imagePath"

            val collection = MediaStore.Files.getContentUri("external")

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, path)
            }

            val uri: Uri? = context.contentResolver.insert(collection, contentValues)

            uri?.let {
                context.contentResolver.openOutputStream(it).use { outputStream ->
                    outputStream?.write(decodedBytes)
                }
            }

            uri
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}

//    private fun saveBase64ToJpgFileLegacy(
//        base64Str: String,
//        fileName: String
//    ): File? {
//        return try {
//            val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
//
//            // Documents/Public 폴더에 SmartSetRenew라는 하위 폴더를 생성
//            val documentsDir = File(
//                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
//                "SmartSetRenew"
//            )
//
//            // 폴더가 존재하지 않으면 생성
//            if (!documentsDir.exists()) {
//                documentsDir.mkdirs()
//            }
//
//            val file = File(documentsDir, fileName)
//            FileOutputStream(file).use { it.write(decodedBytes) }
//            file
//        } catch (e: IOException) {
//            e.printStackTrace()
//            null
//        }
//    }
