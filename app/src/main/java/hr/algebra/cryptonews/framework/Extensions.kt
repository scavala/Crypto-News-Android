package hr.algebra.cryptonews.framework

import android.app.Activity
import android.content.*
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.getSystemService
import androidx.preference.PreferenceManager
import hr.algebra.cryptonews.model.Item
import hr.algebra.cryptonews.provider.CRYPTONEWS_PROVIDER_URI
import java.text.SimpleDateFormat
import java.util.*

fun View.startAnimation(animationId: Int) =
    startAnimation(AnimationUtils.loadAnimation(context, animationId))

inline fun <reified T : Activity> Context.startActivity() =
    startActivity(Intent(this, T::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    })

inline fun <reified T : Activity> Context.startActivity(key: String, value: Int) =
    startActivity(Intent(this, T::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        putExtra(key, value)
    })


inline fun <reified T : BroadcastReceiver> Context.sendBroadcast() =
    sendBroadcast(Intent(this, T::class.java))

fun Context.setBooleanPreference(key: String, value: Boolean) =
    PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(key, value).apply()


fun Context.getBooleanPreference(key: String) =
    PreferenceManager.getDefaultSharedPreferences(this).getBoolean(key, false)

fun Context.isOnline(): Boolean {

    val connectivityManager = getSystemService<ConnectivityManager>()
    connectivityManager?.activeNetwork?.let {
        connectivityManager.getNetworkCapabilities(it)?.let { networkCapabilities ->
            return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        }

    }
    return false
}

fun callDelayed(delay: Long, function: Runnable) {
    Handler(Looper.getMainLooper()).postDelayed(
        function, delay
    )
}


//sqllite without ROOM
fun Context.fetchItems(
    selection: String?,
    selectionArgs: Array<String>?
): MutableList<Item> {

    val items = mutableListOf<Item>()
    val cursor = contentResolver?.query(
        CRYPTONEWS_PROVIDER_URI,
        null,
        selection,
        selectionArgs,
        "favorite DESC"
    )
    while (cursor != null && cursor.moveToNext()) {
        items.add(
            Item(
                cursor.getLong(cursor.getColumnIndexOrThrow(Item::_id.name)),
                cursor.getString(cursor.getColumnIndexOrThrow(Item::title.name)),
                cursor.getString(cursor.getColumnIndexOrThrow(Item::articleUrl.name)),
                cursor.getString(cursor.getColumnIndexOrThrow(Item::description.name)),
                cursor.getString(cursor.getColumnIndexOrThrow(Item::picturePath.name)),
                cursor.getString(cursor.getColumnIndexOrThrow(Item::date.name)),
                cursor.getString(cursor.getColumnIndexOrThrow(Item::provider.name)),
                cursor.getInt(cursor.getColumnIndexOrThrow(Item::favorite.name)) == 1
            )
        )
    }
    cursor?.close()
    return items
}

fun Context.startIntent(ACTION_VIEW: String, URI_DATA_STRING: String) =
    Intent(ACTION_VIEW).apply {
        data = Uri.parse(URI_DATA_STRING)
        startActivity(this)
    }


fun Date.formatDate(): String =
    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(this)

fun parseDate(date: String): Date? =
    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(date)

fun Context.copyToClipboard(label: String, text: String) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip: ClipData = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
}
