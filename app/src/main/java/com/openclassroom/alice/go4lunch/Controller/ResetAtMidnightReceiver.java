package com.openclassroom.alice.go4lunch.Controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.openclassroom.alice.go4lunch.Model.WorkmateHelper;

/**
 * Created by Alice on 15 January 2019.
 */
public class ResetAtMidnightReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        WorkmateHelper.updateRestaurantID(FirebaseAuth.getInstance().getCurrentUser().getUid(), "");
        WorkmateHelper.updateNameOfRestaurant(FirebaseAuth.getInstance().getCurrentUser().getUid(), "");
    }
}
