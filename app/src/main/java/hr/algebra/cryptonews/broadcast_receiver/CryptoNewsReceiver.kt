package hr.algebra.cryptonews.broadcast_receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import hr.algebra.cryptonews.HostActivity
import hr.algebra.cryptonews.framework.startActivity

class CryptoNewsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        context.startActivity<HostActivity>()
    }
}