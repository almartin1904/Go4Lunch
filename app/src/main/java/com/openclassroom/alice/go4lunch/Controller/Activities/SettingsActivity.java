package com.openclassroom.alice.go4lunch.Controller.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.openclassroom.alice.go4lunch.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    /*private void deleteUserFromFirebase(){
        if (this.getCurrentUser() != null) {

            //4 - We also delete user from firestore storage
            UserHelper.deleteUser(this.getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener());

            ...
        }
    }*/
}
