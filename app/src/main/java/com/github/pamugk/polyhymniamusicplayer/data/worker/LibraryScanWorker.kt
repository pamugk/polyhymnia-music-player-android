package com.github.pamugk.polyhymniamusicplayer.data.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class LibraryScanWorker(appContext: Context, workerParams: WorkerParameters):
    CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        // TODO: media files indexing
        Log.i("LibraryScanWorker", "Working work")
        return Result.success()
    }
}