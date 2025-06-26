package br.com.devtest.mercadolivre.utils

import android.util.Log
import br.com.devtest.mercadolivre.BuildConfig

/**
 * Utility object for logging messages in the application.
 * It checks if the build is in debug mode before logging.
 */
object AppLog {
    fun d(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message)
        }
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message, throwable)
        }
    }

    fun i(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, message)
        }
    }

    fun v(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, message)
        }
    }
}

object LogTags {
    const val NETWORK = "NETWORK"
}