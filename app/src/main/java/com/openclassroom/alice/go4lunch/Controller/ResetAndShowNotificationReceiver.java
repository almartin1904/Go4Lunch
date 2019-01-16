package com.openclassroom.alice.go4lunch.Controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.openclassroom.alice.go4lunch.Model.NotificationRestaurant;
import com.openclassroom.alice.go4lunch.Model.ResultOfRequest.PlaceDetailResult;
import com.openclassroom.alice.go4lunch.Model.ResultOfRequest.RequestResult;
import com.openclassroom.alice.go4lunch.Model.Workmate;
import com.openclassroom.alice.go4lunch.Model.WorkmateHelper;
import com.openclassroom.alice.go4lunch.Utils.PlacesAPIStreams;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by Alice on 15 January 2019.
 */
public class ResetAndShowNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {

        final NotificationRestaurant myNotification = new NotificationRestaurant();
        final String currentUserUid= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        final Disposable[] disposable = new Disposable[1];

        //1 check if current user has selected a restaurant
        WorkmateHelper.getUser(currentUserUid).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                final Workmate currentUser = documentSnapshot.toObject(Workmate.class);
                final String restaurantPickedPlaceId = currentUser.getRestaurantPlaceId();

                //If a restaurant has been picked, get Name, Address and workmates joining
                if (restaurantPickedPlaceId!=null){
                    disposable[0] = PlacesAPIStreams.streamFetchDetailPlace(restaurantPickedPlaceId).subscribeWith(new DisposableObserver<PlaceDetailResult>() {
                        @Override
                        public void onNext(PlaceDetailResult placeDetailResult) {
                            myNotification.setRestaurantAddress(placeDetailResult.getResult().getFormatted_address());
                            myNotification.setRestaurantName(placeDetailResult.getResult().getName());
                            //Get workmates joining
                            WorkmateHelper.getWorkmatesGoingToARestaurant(restaurantPickedPlaceId).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@android.support.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                    if (queryDocumentSnapshots != null) {
                                        if (queryDocumentSnapshots.getDocuments().size()>1){
                                            List<String> workmatesJoining = new ArrayList<>();
                                            for (int i=0; i<queryDocumentSnapshots.getDocuments().size(); i++){
                                                if (!queryDocumentSnapshots.getDocuments().get(i).getData().get("uid").equals(currentUserUid)){
                                                    workmatesJoining.add(queryDocumentSnapshots.getDocuments().get(i).getData().get("firstName").toString());
                                                }
                                            }
                                            myNotification.setWorkmatesJoining(workmatesJoining);
                                        }
                                    }
                                    Intent notificationIntent = new Intent(context, NotificationIntentService.class);
                                    notificationIntent.putExtra("notif", myNotification);
                                    context.startService(notificationIntent);
                                }
                            });
                        }

                        @Override
                        public void onError(Throwable e) { }

                        @Override
                        public void onComplete() { }
                    });


                }
            }
        });


        //4 Reset Restaurant
        WorkmateHelper.updateRestaurantID(FirebaseAuth.getInstance().getCurrentUser().getUid(), "");
        WorkmateHelper.updateNameOfRestaurant(FirebaseAuth.getInstance().getCurrentUser().getUid(), "");

        if (disposable[0] != null && !disposable[0].isDisposed()) disposable[0].dispose();
    }
}
