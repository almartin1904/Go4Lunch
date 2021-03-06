package com.openclassroom.alice.go4lunch.Controller.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.openclassroom.alice.go4lunch.Controller.ResetAndShowNotificationReceiver;
import com.openclassroom.alice.go4lunch.Model.ViewPagerAdapter;
import com.openclassroom.alice.go4lunch.Model.Workmate;
import com.openclassroom.alice.go4lunch.Model.WorkmateHelper;
import com.openclassroom.alice.go4lunch.R;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;

import static com.openclassroom.alice.go4lunch.Constantes.CARD_DETAILS;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.main_activity_coordinator_layout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.activity_main_drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.activity_main_nav_view) NavigationView mNavigationView;
    @BindView(R.id.activity_main_toolbar) Toolbar mToolbar;


    private static final int RC_SIGN_IN = 123;
    private TextView mTextViewMail;
    private TextView mTextViewName;
    private ImageView mImageViewProfile;
    private int[] tabIcons = {
            R.drawable.map_black_24x24,
            R.drawable.view_list_black_24x24,
            R.drawable.people_black_24x24
    };
    private static final String TAG = "MainActivityName";

    @Override
    public int getFragmentLayout() { return R.layout.activity_main; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isCurrentUserLogged()){
            this.startSignInActivity();
        }

        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.configureViewPagerAndTabs();
        this.configureResetRestaurantAtMidday();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 4 - Handle SignIn Activity response on activity result
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUIWhenCreating();
    }

    private void startSignInActivity(){
        Log.d(TAG, "startSignInActivity: ");
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Arrays.asList( new AuthUI.IdpConfig.GoogleBuilder().build(),// SUPPORT GOOGLE
                                            new AuthUI.IdpConfig.FacebookBuilder().build())) //SUPPORT FACEBOOK
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.ic_logo_auth)
                        .build(),
                RC_SIGN_IN);
    }

    private void signOutUserFromFirebase(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted());
    }

    // --------------------
    // UI
    // --------------------

    // 2 - Show Snack Bar with a message
    private void showSnackBar(CoordinatorLayout coordinatorLayout, String message){
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                this.createUserInFirestore();
                showSnackBar(this.coordinatorLayout, getString(R.string.connection_succeed));
            } else { // ERRORS
                if (response == null) {
                    showSnackBar(this.coordinatorLayout, getString(R.string.error_authentication_canceled));
                } else if (Objects.requireNonNull(response.getError()).getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackBar(this.coordinatorLayout, getString(R.string.error_no_internet));
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackBar(this.coordinatorLayout, getString(R.string.error_unknown_error));
                }
            }
        }
    }

    private void updateUIWhenCreating(){
        if (this.getCurrentUser() != null){

            //Get picture URL from Firebase
            if (this.getCurrentUser().getPhotoUrl() != null) {
                Glide.with(this)
                        .load(this.getCurrentUser().getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(mImageViewProfile);
            }

            //Get email & username from Firebase
            String email = TextUtils.isEmpty(this.getCurrentUser().getEmail()) ? getString(R.string.info_no_email_found) : this.getCurrentUser().getEmail();
            String username = TextUtils.isEmpty(this.getCurrentUser().getDisplayName()) ? getString(R.string.info_no_username_found) : this.getCurrentUser().getDisplayName();

            //Update views with data
            this.mTextViewName.setText(username);
            this.mTextViewMail.setText(email);

            WorkmateHelper.getUser(this.getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Workmate currentWorkmate = documentSnapshot.toObject(Workmate.class);
                    String username = TextUtils.isEmpty(currentWorkmate != null ? currentWorkmate.getName() : null) ? getString(R.string.info_no_username_found) : currentWorkmate != null ? currentWorkmate.getName() : null;
                    Log.d(TAG, "onSuccess: "+username);
                    mTextViewName.setText(username);
                }
            });
        }
    }

    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(){
        return new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startSignInActivity();
            }
        };
    }

    //-------------------
    // NAVIGATION
    //-------------------

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {

        // 4 - Handle Navigation Item Click
        int id = item.getItemId();

        switch (id){
            case R.id.activity_main_drawer_lunch :
                WorkmateHelper.getUser(Objects.requireNonNull(this.getCurrentUser()).getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Workmate currentWorkmate = documentSnapshot.toObject(Workmate.class);
                        String placeId = TextUtils.isEmpty(currentWorkmate != null ? currentWorkmate.getRestaurantPlaceId() : null) ? "" : currentWorkmate != null ? currentWorkmate.getRestaurantPlaceId() : null;
                        if (placeId != null && placeId.equals("")) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_restaurant_picked_navdrawer), Toast.LENGTH_SHORT).show();
                        } else {
                            Intent restaurantCardActivity = new Intent(MainActivity.this, RestaurantCardActivity.class);
                            restaurantCardActivity.putExtra(CARD_DETAILS, placeId);
                            startActivity(restaurantCardActivity);
                        }

                    }
                });

                break;
            case R.id.activity_main_drawer_setting:
                Intent settingsActivity = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsActivity);
                break;
            case R.id.activity_main_drawer_logout:
                this.signOutUserFromFirebase();
                break;
            default:
                break;
        }

        this.mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }


    // ---------------------
    // CONFIGURATION
    // ---------------------

    // 1 - Configure Toolbar
    private void configureToolBar(){
        setSupportActionBar(mToolbar);
    }

    // 2 - Configure Drawer Layout
    private void configureDrawerLayout(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // 3 - Configure NavigationView
    private void configureNavigationView(){
        mNavigationView.setNavigationItemSelectedListener(this);
        View header=mNavigationView.getHeaderView(0);
        this.mTextViewMail = header.findViewById(R.id.nav_header_mail_txt);
        this.mTextViewName = header.findViewById(R.id.nav_header_name_txt);
        this.mImageViewProfile = header.findViewById(R.id.nav_header_profile_img);
    }

    private void configureViewPagerAndTabs(){
        ViewPager pager = findViewById(R.id.activity_main_viewpager);
        pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), this));
        TabLayout tabs= findViewById(R.id.activity_main_tabs);
        tabs.setupWithViewPager(pager);
        tabs.setTabMode(TabLayout.MODE_FIXED);
        setupTabIcons(tabs);

    }

    private void setupTabIcons(TabLayout tabLayout) {
        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(tabIcons[0]);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(tabIcons[1]);
        Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(tabIcons[2]);
    }

    private void configureResetRestaurantAtMidday() {
        Calendar midnightCalendar = Calendar.getInstance();
        midnightCalendar.set(Calendar.HOUR_OF_DAY, 12);
        midnightCalendar.set(Calendar.MINUTE, 0);
        midnightCalendar.set(Calendar.SECOND, 0);


        boolean alarmUp = (PendingIntent.getBroadcast(MainActivity.this, 20,
                new Intent(MainActivity.this, ResetAndShowNotificationReceiver.class),
                PendingIntent.FLAG_CANCEL_CURRENT) != null);
        if (!alarmUp) {
            Log.d("NotificationTest", "configureResetRestaurantAtMidday: set alarm");
            AlarmManager alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent alarmIntent = new Intent(this, ResetAndShowNotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 20, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, midnightCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        } else {
            Log.d("NotificationTest", "configureResetRestaurantAtMidday: alarm already set");
        }
    }

    // --------------------
    // REST REQUEST
    // --------------------

    // 1 - Http request that create user in firestore
    private void createUserInFirestore(){

        if (this.getCurrentUser() != null){

            String urlPicture = (this.getCurrentUser().getPhotoUrl() != null) ? this.getCurrentUser().getPhotoUrl().toString() : null;
            String username = this.getCurrentUser().getDisplayName();
            String uid = this.getCurrentUser().getUid();
            String mail = this.getCurrentUser().getEmail();

            WorkmateHelper.createUser(uid, username, urlPicture, mail).addOnFailureListener(this.onFailureListener());
        }
    }
}
