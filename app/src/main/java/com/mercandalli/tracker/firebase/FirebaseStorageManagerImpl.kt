package com.mercandalli.tracker.firebase

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

internal class FirebaseStorageManagerImpl constructor(
        firebaseStorage: FirebaseStorage) : FirebaseStorageManager {

    companion object {
        private val ONE_MEGABYTE = (1024 * 1024).toLong()
    }

    private val mainReference: StorageReference = firebaseStorage.reference

    override fun getString(
            pathParentFolders: List<String>,
            filenameWithExt: String,
            listener: FirebaseStorageManager.OnGetStringListener) {
        val reference = getReference(pathParentFolders, filenameWithExt)
        reference.getBytes(ONE_MEGABYTE)
                .addOnFailureListener({ e ->
                    listener.onGetStringFailed(e)
                })
                .addOnSuccessListener({ byteArray ->
                    val json = String(byteArray)
                    listener.onGetStringSucceeded(json)
                })
    }

    override fun putString(
            pathParentFolders: List<String>,
            filenameWithExt: String,
            content: String,
            listener: FirebaseStorageManager.OnPutStringListener) {
        val reference = getReference(pathParentFolders, filenameWithExt)
        val uploadTask = reference.putBytes(content.toByteArray())
        uploadTask
                .addOnFailureListener({ e ->
                    listener.onPutStringFailed(e)
                })
                .addOnSuccessListener({ _ ->
                    listener.onPutStringSucceeded()
                })
    }

    private fun getReference(pathParentFolders: List<String>, filenameWithExt: String): StorageReference {
        var currentReference = mainReference
        pathParentFolders.forEach { child ->
            run {
                currentReference = currentReference.child(child)
            }
        }
        return currentReference.child(filenameWithExt)
    }
}