package com.androidtech.onetoonefirebasechat.Utils;

/**
 * Created by Ahmed Donkl on 11/13/2016.
 */


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtil {
    public static String profileImages = "profileImages";

    public static DatabaseReference getBaseRef() {
//         FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        return FirebaseDatabase.getInstance().getReference();
    }

    public static String getCurrentUserId() {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            return user.getUid();
        }
        return null;
    }

    public static FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static DatabaseReference getUsersRef() {
        return getBaseRef().child("users");
    }

    public static DatabaseReference getOnlineUsersRef() {
        return getBaseRef().child("online");
    }

    public static DatabaseReference getGeoFireRef() {
        return getBaseRef().child("geo_fire");
    }

    public static DatabaseReference getChatRoomsRef() {
        return getBaseRef().child("chat_rooms");
    }

    public static DatabaseReference getUserInbox() {
        return getUsersRef().child(getCurrentUserId()).child("inbox");
    }

    public static DatabaseReference getCurrentUserRef() {
        String uid = getCurrentUserId();
        if (uid != null) {
            return getBaseRef().child("users").child(getCurrentUserId());
        }
        return null;
    }
}
