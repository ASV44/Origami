package com.koshka.origami.utils;

import android.content.Context;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class UploadUtil {

    public static void uploadToFirebase(Context context, final File file) {
//        try {
//            String path = Strings.getUploadPath(file.getParent())
//
//            Log.d("App", "Upload to firebase: " + path);
//
//            InputStream inputStream = new FileInputStream(file);
//            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
//            StorageReference riversRef = storageRef.child(path + file.getName());
//            UploadTask uploadTask = riversRef.putStream(inputStream);
//
//            // Register observers to listen for when the download is done or if it fails
//            uploadTask.addOnFailureListener(exception -> {
//                // Handle unsuccessful uploads
//                exception.printStackTrace();
//                Log.d("App", "Error" + exception.getMessage());
//            }).addOnSuccessListener(taskSnapshot -> {
//                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                boolean deleted = file.delete();
//                Log.d("App", "Success");
//                //Uri downloadUrl = taskSnapshot.getDownloadUrl();
//            });
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
