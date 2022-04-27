package hr.algebra.cryptonews.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import hr.algebra.cryptonews.api.NewsFetcher

class CryptoNewsWorker(context: Context, workerParams: WorkerParameters) : Worker(
    context,
    workerParams
) {
    override fun doWork(): Result {
        NewsFetcher(applicationContext).fetchItems()
        return Result.success()
    }
}