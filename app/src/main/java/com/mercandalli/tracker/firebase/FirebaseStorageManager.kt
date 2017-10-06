package com.mercandalli.tracker.firebase

import java.lang.Exception

interface FirebaseStorageManager {

    fun putString(
            pathParentFolders: List<String>,
            filenameWithExt: String,
            content: String,
            listener: OnPutStringListener)

    fun getString(
            pathParentFolders: List<String>,
            filenameWithExt: String,
            listener: OnGetStringListener)

    interface OnPutStringListener {
        fun onPutStringSucceeded()
        fun onPutStringFailed(e: Exception)
    }

    interface OnGetStringListener {
        fun onGetStringSucceeded(content: String)
        fun onGetStringFailed(e: Exception)
    }
}