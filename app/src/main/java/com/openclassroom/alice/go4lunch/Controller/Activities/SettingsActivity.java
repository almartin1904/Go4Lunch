package com.openclassroom.alice.go4lunch.Controller.Activities;

import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.openclassroom.alice.go4lunch.Model.Workmate;
import com.openclassroom.alice.go4lunch.Model.WorkmateHelper;
import com.openclassroom.alice.go4lunch.R;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingsActivity extends BaseActivity {

    private static final int DELETE_USER_TASK = 20;
    private static final int UPDATE_USERNAME = 30;

    @BindView(R.id.activity_settings_name) TextView mUserNameTxt;
    @BindView(R.id.activity_settings_progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.activity_settings_email) TextView mEmailTxt;
    @BindView(R.id.activity_settings_imageview_profile) ImageView mProfileImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.updateUIWhenCreating();
    }


    @Override
    public int getFragmentLayout() {
        return R.layout.activity_settings;
    }

    @OnClick(R.id.activity_settings_delete_account_btn)
    public void onClickDeleteButton() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.popup_message_confirmation_delete_account)
                .setPositiveButton(R.string.popup_message_choice_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteUserFromFirebase();
                    }
                })
                .setNegativeButton(R.string.popup_message_choice_no, null)
                .show();
    }

    private void deleteUserFromFirebase(){
        if (this.getCurrentUser() != null) {
            WorkmateHelper.deleteUser(this.getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener());
            AuthUI.getInstance()
                    .delete(this)
                    .addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted(DELETE_USER_TASK));
        }
    }

    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(final int origin){
        return new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                switch (origin){
                    case UPDATE_USERNAME:
                        mProgressBar.setVisibility(View.INVISIBLE);
                        break;
                    case DELETE_USER_TASK:
                        finish();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void updateUIWhenCreating(){

        if (this.getCurrentUser() != null){

            if (this.getCurrentUser().getPhotoUrl() != null) {
                Glide.with(this)
                        .load(this.getCurrentUser().getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(mProfileImage);
            }

            String email = TextUtils.isEmpty(this.getCurrentUser().getEmail()) ? getString(R.string.info_no_email_found) : this.getCurrentUser().getEmail();

            this.mEmailTxt.setText(email);

            // 7 - Get additional data from Firestore (isMentor & Username)
            WorkmateHelper.getUser(this.getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Workmate currentWorkmate = documentSnapshot.toObject(Workmate.class);
                    String username = TextUtils.isEmpty(currentWorkmate.getName()) ? getString(R.string.info_no_username_found) : currentWorkmate.getName();
                    mUserNameTxt.setText(username);
                }
            });
        }
    }
}
