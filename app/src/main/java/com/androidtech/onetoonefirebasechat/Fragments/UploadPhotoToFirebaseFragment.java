package com.androidtech.onetoonefirebasechat.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.androidtech.instar.R;
import com.androidtech.instar.Utils.FirebaseUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by Ahmed Donkl on 11/20/2016.
 */

public class UploadPhotoToFirebaseFragment extends Fragment {
    private static final String TAG = "UploadPhotoToFirebaseFragment";

    public interface TaskCallbacks {
        void onPhotoUploaded(String imageUrl);
    }
    private Context mApplicationContext;
    private UploadPhotoToFirebaseFragment.TaskCallbacks mCallbacks;

    public UploadPhotoToFirebaseFragment() {
        // Required empty public constructor
    }

    public static UploadPhotoToFirebaseFragment newInstance() {
        return new UploadPhotoToFirebaseFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retain this fragment across config changes.
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UploadPhotoToFirebaseFragment.TaskCallbacks) {
            mCallbacks = (UploadPhotoToFirebaseFragment.TaskCallbacks) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement TaskCallbacks");
        }
        mApplicationContext = context.getApplicationContext();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public void uploadPhotoProfile(Uri imageUri) {
        UploadPhotoToFirebaseFragment.UploadPhotoTaskProfile uploadTask =
                new UploadPhotoToFirebaseFragment.UploadPhotoTaskProfile(imageUri);
        uploadTask.execute();
    }

    class UploadPhotoTaskProfile extends AsyncTask<Void, Void, Void> {
        private Uri imageUri;

        public UploadPhotoTaskProfile(Uri imageUri) {
            this.imageUri = imageUri;
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (imageUri == null) {
                return null;
            }

            String fileName = imageUri.getLastPathSegment();

            FirebaseStorage storageRef = FirebaseStorage.getInstance();

            StorageReference photoRef = storageRef.getReferenceFromUrl("gs://" + getString(R.string.google_storage_bucket));
            Long timestamp = System.currentTimeMillis();
            final StorageReference fullSizeRef = photoRef.child(FirebaseUtil.getCurrentUserId()).child("full").child(timestamp.toString()).child(fileName);

            UploadTask fullImageUploadTask = fullSizeRef.putFile(imageUri);
            fullImageUploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    FirebaseCrash.logcat(Log.ERROR, TAG, "Failed to upload pic to storage.");
                    FirebaseCrash.report(exception);
                    //callback results
                    mCallbacks.onPhotoUploaded(null);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    final Uri fullUrl = taskSnapshot.getDownloadUrl();
                    //   Log.d(TAG, fullUrl.toString());

                    //callback results
                    mCallbacks.onPhotoUploaded(fullUrl.toString());
                }
            });

            return null;
        }
    }

}

