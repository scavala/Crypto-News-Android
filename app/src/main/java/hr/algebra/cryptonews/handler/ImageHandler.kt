package hr.algebra.cryptonews.handler

import android.content.Context
import android.util.Log
import hr.algebra.cryptonews.factory.createGetHttpUrlConnection
import java.io.File
import java.net.HttpURLConnection
import java.nio.file.Files
import java.nio.file.Paths

fun downloadImageAndStore(context: Context, url: String, fileName: String): String? {

    val extension = url.substring(url.lastIndexOf("."))
    val file: File = createFile(context, fileName, extension)
    try {
        val con: HttpURLConnection =
            createGetHttpUrlConnection(if (url.startsWith("http")) url else "https://$url")
        Files.copy(con.inputStream, Paths.get(file.toURI()))
        return file.absolutePath
    } catch (e: Exception) {
        Log.e("Download failed", e.message, e)
    }
    return null
}


fun createFile(context: Context, fileName: String, extension: String): File {

    val dir = context.applicationContext.getExternalFilesDir(null)
    val file = File(dir, File.separator + fileName + extension)
    if (file.exists()) {
        file.delete()
    }
    return file
}
