package com.example.data.util

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class UploadUtil {

    companion object {
        fun uploadToFirebase(context: Context, file: File) {
            try {
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
                }).addOnSuccessListener({ taskSnapshot ->
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    val deleted = file.delete()
                    Log.d("App", "Success")
                    //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                })

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        fun uploadToFirebase(context: Context, uri: Uri) {
            uploadToFirebase(context, File(uri.))
        }
    }
}