package com.goat.lotech.ml

import android.os.Handler
import android.os.HandlerThread

internal fun MLMainActivity.startBackgroundThread() {
    backgroundThread = HandlerThread(MLMainActivity.HANDLER_THREAD_NAME)
    backgroundThread.start()
    backgroundHandler = Handler(backgroundThread.looper)
    synchronized(lock) {
        runClassifier = true
    }
}

internal fun MLMainActivity.stopBackgroundThread() {
    try {
        backgroundThread.quit()
        synchronized(lock) {
            runClassifier = false
        }
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
}