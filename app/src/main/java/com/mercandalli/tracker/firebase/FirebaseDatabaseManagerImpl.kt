package com.mercandalli.tracker.firebase

import com.google.firebase.database.*

class FirebaseDatabaseManagerImpl constructor(
        firebaseDatabase: FirebaseDatabase) : FirebaseDatabaseManager {

    private val mainReference: DatabaseReference = firebaseDatabase.reference

    override fun getObject(
            pathParentKeys: List<String>,
            listener: FirebaseDatabaseManager.OnGetObjectListener?) {
        val reference = getReference(pathParentKeys)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(Any::class.java)
                if (value == null) {
                    listener?.onGetObjectFailed(IllegalStateException("Data null"))
                    return
                }
                listener?.onGetObjectSucceeded(value)
            }

            override fun onCancelled(error: DatabaseError) {
                listener?.onGetObjectFailed(IllegalStateException(error.message))
            }
        })
    }

    override fun putObject(
            pathParentKeys: List<String>,
            content: Any,
            listener: FirebaseDatabaseManager.OnPutObjectListener?) {
        val reference = getReference(pathParentKeys)
        reference.setValue(
                content) { e, r ->
            if (e == null && r != null) {
                listener?.onPutObjectSucceeded()
            } else {
                listener?.onPutObjectFailed(IllegalStateException(if (e == null) {
                    "Error"
                } else {
                    e.message
                }))
            }
        }
    }

    override fun getObjects(
            pathParentKeys: List<String>,
            listener: FirebaseDatabaseManager.OnGetObjectsListener?) {
        val reference = getReference(pathParentKeys)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listener?.onGetObjectsSucceeded(dataSnapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                listener?.onGetObjectsFailed(IllegalStateException(error.message))
            }
        })
    }

    private fun getReference(pathParentFolders: List<String>): DatabaseReference {
        var currentReference = mainReference
        pathParentFolders.forEach { child ->
            run {
                currentReference = currentReference.child(child)
            }
        }
        return currentReference
    }
}
