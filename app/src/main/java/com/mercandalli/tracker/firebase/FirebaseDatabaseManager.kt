package com.mercandalli.tracker.firebase

import com.google.firebase.database.DataSnapshot
import java.lang.Exception

interface FirebaseDatabaseManager {

    fun getObject(
            pathParentKeys: List<String>,
            listener: OnGetObjectListener?)

    fun putObject(
            pathParentKeys: List<String>,
            content: Any,
            listener: OnPutObjectListener?)

    fun getObjects(
            pathParentKeys: List<String>,
            listener: OnGetObjectsListener?)

    interface OnPutObjectListener {
        fun onPutObjectSucceeded()
        fun onPutObjectFailed(e: Exception)
    }

    interface OnGetObjectListener {
        fun onGetObjectSucceeded(content: Any)
        fun onGetObjectFailed(e: Exception)
    }

    interface OnGetObjectsListener {
        fun onGetObjectsSucceeded(dataSnapshot: DataSnapshot)
        fun onGetObjectsFailed(e: Exception)
    }
}