package com.example.data.util

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.data.portability.Consumer
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class UploadUtil {

    companion object {
        fun uploadToFirebase(file: File, onSuccess: (Uri?) -> Unit, onError: (Exception) -> Unit) {
            val path = file.parent

            Log.d("App", "Upload to firebase: $path")

            val inputStream = FileInputStream(file)
            val storageRef = FirebaseStorage.getInstance().getReference()
            val riversRef = storageRef.child(path + file.name)
            val uploadTask = riversRef.putStream(inputStream)

            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener({ exception ->
                // Handle unsuccessful uploads
                exception.printStackTrace()
                Log.d("App", "Error" + exception.message)
                onError(exception)
            }).addOnSuccessListener({ taskSnapshot ->
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                val deleted = file.delete()
                Log.d("App", "Success")
                val downloadUrl = taskSnapshot.downloadUrl
                onSuccess(downloadUrl)
            })
        }
    }
}