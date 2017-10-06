package com.mercandalli.tracker.firebase

import com.google.firebase.database.*

class FirebaseDatabaseManagerImpl constructor(
        firebaseDatabase: FirebaseDatabase) : FirebaseDatabaseManager {

    private val mainReference: DatabaseReference = firebaseDatabase.reference

    override fun getObject(
            pathParent: List<String>,
            key: String,
            listener: FirebaseDatabaseManager.OnGetObjectListener) {
        val reference = getReference(pathParent, key)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(Any::class.java)
                if (value == null) {
                    listener.onGetObjectFailed(IllegalStateException("Data null"))
                    return
                }
                listener.onGetObjectSucceeded(value)
            }

            override fun onCancelled(error: DatabaseError) {
                listener.onGetObjectFailed(IllegalStateException(error.message))
            }
        })
    }

    override fun putObject(
            pathParent: List<String>,
            key: String,
            content: Any,
            listener: FirebaseDatabaseManager.OnPutObjectListener) {
        val reference = getReference(pathParent, key)
        reference.setValue(content)
    }

    override fun getObjects(
            pathParent: List<String>,
            listener: FirebaseDatabaseManager.OnGetObjectsListener) {
        val reference = getReference(pathParent)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val result = ArrayList<Any>()
                dataSnapshot.children.filterNotNullTo(result)
                listener.onGetObjectsSucceeded(result)
            }

            override fun onCancelled(error: DatabaseError) {
                listener.onGetObjectsFailed(IllegalStateException(error.message))
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

    private fun getReference(pathParentFolders: List<String>, filenameWithExt: String): DatabaseReference {
        return getReference(pathParentFolders).child(filenameWithExt)
    }
}
