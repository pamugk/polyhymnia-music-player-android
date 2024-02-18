package com.github.pamugk.polyhymniamusicplayer.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class LibraryScanWorker(appContext: Context, workerParams: WorkerParameters):
    CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        // TODO: media files indexing
        return Result.success()
    }
}