package ru.technosopher.attendancelogapp.data.source;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseImageSource {
    public static final String FIREBASE_AVATAR_PREFIX = "images/avatar_";

    private static FirebaseImageSource INSTANCE;

    public final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    public final StorageReference storageRef = firebaseStorage.getReference();


    public static synchronized FirebaseImageSource getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new FirebaseImageSource();
        }
        return INSTANCE;
    }

    // TODO: Implement this class

    private FirebaseImageSource() {}

//    public Uri getImage(String imageUrl) {
//        StorageReference imageRef = storageRef.child(imageUrl);
//
//    }


//    public String uploadImage(@NonNull String id, @NonNull Uri image) {
//
////        StorageReference imageRef = storageRef.child(FIREBASE_AVATAR_PREFIX + id + ".png");
////
////        UploadTask uploadTask = imageRef.putFile(image);
////        uploadTask.addOnSuccessListener(
////
////        ).addOnFailureListener(
////
////        );
//    }
}