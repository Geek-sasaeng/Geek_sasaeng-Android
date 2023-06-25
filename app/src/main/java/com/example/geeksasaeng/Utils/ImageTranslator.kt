package com.example.geeksasaeng.Utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

// MEMO: 코드 분석하고 주석 달아주기!
object ImageTranslator {
    fun optimizeBitmap(context: Context, uri: Uri): String? {
        try {
            val storage = context.cacheDir
            val fileName = String.format("%s.%s", UUID.randomUUID(), "jpg")

            val tempFile = File(storage, fileName)
            tempFile.createNewFile()

            val fos = FileOutputStream(tempFile)

            decodeBitmapFromUri(uri, context)?.apply {
                compress(Bitmap.CompressFormat.JPEG, 50, fos)
                recycle()
            } ?: throw NullPointerException()

            fos.flush()
            fos.close()

            return tempFile.absolutePath
        } catch (e: Exception) {
            Log.e("이미지 생성 실패", "${e.message}")
        }

        return null
    }

    fun decodeBitmapFromUri(uri: Uri, context: Context): Bitmap? {
        val input = BufferedInputStream(context.contentResolver.openInputStream(uri))
        input.mark(input.available()) // 입력 스트림의 특정 위치를 기억

        var bitmap: Bitmap?
        BitmapFactory.Options().run {
            inJustDecodeBounds = true
            bitmap = BitmapFactory.decodeStream(input, null, this)
            input.reset()
            inJustDecodeBounds = false
            bitmap = BitmapFactory.decodeStream(input, null, this)
        }

        input.close()

        return bitmap
    }
}