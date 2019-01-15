package com.openclassroom.alice.go4lunch.Model;

import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.openclassroom.alice.go4lunch.R;

import java.util.List;
import java.util.Objects;


/**
 * Created by Alice on 12 January 2019.
 */
public class WorkmateHelper {

    public static final String COLLECTION_NAME = "users";
    private static final String DATA_USERNAME = "username";
    private static final String DATA_PLACE_ID_RESTAURANT = "restaurantPlaceId";
    private static final String DATA_RESTAURANT_NAME = "nameOfRestaurant";
    private static final String DATA_RESTAURANT_LIKED = "restaurantLikedPlaceId";
    private static final String TAG = WorkmateHelper.class.getSimpleName();


    // --- COLLECTION REFERENCE ---

    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createUser(String uid, String username, String urlPicture, String mail) {
        Workmate userToCreate = new Workmate(uid, username, mail, urlPicture);
        return WorkmateHelper.getUsersCollection().document(uid).set(userToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getUser(String uid){
        return WorkmateHelper.getUsersCollection().document(uid).get();
    }

    public static Query getAllWorkMates(){
        Log.d(TAG, "getAllWorkMates: ");
        return WorkmateHelper.getUsersCollection()
                .orderBy(DATA_RESTAURANT_NAME)
                .limit(10);
    }

    public static Query getWorkmatesGoingToARestaurant(String placeId){
        return WorkmateHelper.getUsersCollection()
                .whereEqualTo(DATA_PLACE_ID_RESTAURANT, placeId)
                .limit(10);
    }

    // --- UPDATE ---

    public static Task<Void> updateUsername(String username, String uid) {
        return WorkmateHelper.getUsersCollection().document(uid).update(DATA_USERNAME, username);
    }

    public static Task<Void> updateRestaurantID(String uid, String restaurantPlaceId) {
        return WorkmateHelper.getUsersCollection().document(uid).update(DATA_PLACE_ID_RESTAURANT, restaurantPlaceId);
    }

    public static Task<Void> updateNameOfRestaurant(String uid, String nameOfRestaurant) {
        return WorkmateHelper.getUsersCollection().document(uid).update(DATA_RESTAURANT_NAME, nameOfRestaurant);
    }

    public static Task<Void> updateRestaurantLiked(String uid, List<String> restaurantLiked){
        return WorkmateHelper.getUsersCollection().document(uid).update(DATA_RESTAURANT_LIKED, restaurantLiked);
    }


    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return WorkmateHelper.getUsersCollection().document(uid).delete();
    }

}
