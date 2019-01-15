package com.openclassroom.alice.go4lunch.Controller.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.openclassroom.alice.go4lunch.BuildConfig;
import com.openclassroom.alice.go4lunch.Model.ResultOfRequest.PlaceDetailResult;
import com.openclassroom.alice.go4lunch.Model.Workmate;
import com.openclassroom.alice.go4lunch.Model.WorkmateHelper;
import com.openclassroom.alice.go4lunch.R;
import com.openclassroom.alice.go4lunch.Utils.PlacesAPIStreams;
import com.openclassroom.alice.go4lunch.View.WorkmateAdapter;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

import static com.openclassroom.alice.go4lunch.Constantes.ACTIVITY_RESTAURANT_CARD;
import static com.openclassroom.alice.go4lunch.Constantes.CARD_DETAILS;


public class RestaurantCardActivity extends BaseActivity implements WorkmateAdapter.Listener{

    private String mWebsite;
    private String mPhoneNumber;
    private boolean isMyLunch=false;
    private boolean isLiked=false;
    private static final String TAG = RestaurantCardActivity.class.getSimpleName();
    private String mPlaceId;
    private WorkmateAdapter mWorkmateAdapter;
    private Disposable mDisposable;

    @BindView(R.id.activity_restaurant_card_restaurant_name_txt) TextView nameTxt;
    @BindView(R.id.activity_restaurant_card_restaurant_address_txt) TextView addressTxt;
    @BindView(R.id.activity_restaurant_card_imageView) ImageView photoImg;
    @BindView(R.id.activity_restaurant_card_check_btn) FloatingActionButton checkBtn;
    @BindView(R.id.activity_restaurant_card_recyclerview) RecyclerView mRecyclerView;
    @BindView(R.id.activity_restaurant_card_like_btn) Button mLikeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i=getIntent();
        mPlaceId = (String) i.getSerializableExtra(CARD_DETAILS);

        this.executeHttpRequest(mPlaceId);
        this.updateUiWhenCreating();
        this.configureRecyclerView();
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_restaurant_card;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    //----------------
    // Configuration
    //----------------

    private void configureRecyclerView() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            this.mWorkmateAdapter =
                    new WorkmateAdapter(generateOptionsForAdapter(WorkmateHelper.getWorkmatesGoingToARestaurant(mPlaceId)),
                            Glide.with(this),
                            this,
                            this,
                            ACTIVITY_RESTAURANT_CARD);
            mWorkmateAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    mRecyclerView.smoothScrollToPosition(mWorkmateAdapter.getItemCount());
                }

            });
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(this.mWorkmateAdapter);
    }

    private FirestoreRecyclerOptions<Workmate> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions.Builder<Workmate>()
                .setQuery(query, Workmate.class)
                .setLifecycleOwner(this)
                .build();
    }

    //--------------------------------
    // UI
    //--------------------------------

    private void updateUiWhenCreating() {
        WorkmateHelper.getUser(Objects.requireNonNull(this.getCurrentUser()).getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Workmate currentUser = documentSnapshot.toObject(Workmate.class);
                String registeredPlaceId = TextUtils.isEmpty(currentUser.getRestaurantPlaceId()) ? "" : currentUser.getRestaurantPlaceId();
                if (registeredPlaceId.equals(mPlaceId)){
                    isMyLunch=true;
                    checkBtn.setImageResource(R.drawable.check_coloraccent);
                }
                List<String> likedPlaceId = currentUser.getRestaurantLikedPlaceId();
                if (likedPlaceId.contains(mPlaceId)){
                    isLiked=true;
                    mLikeBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.grade_colorprimary), null, null);
                }
            }
        });
    }

    @Override
    public void onDataChanged() {
        if (mWorkmateAdapter.getItemCount()==0){
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    //-------------------------------
    // HTTP REQUEST
    //-------------------------------

    private void executeHttpRequest(String placeId) {
        mDisposable = PlacesAPIStreams.streamFetchDetailPlace(placeId).subscribeWith(new DisposableObserver<PlaceDetailResult>() {
            @Override
            public void onNext(PlaceDetailResult placeDetailResult) {
                nameTxt.setText(placeDetailResult.getResult().getName());
                addressTxt.setText(placeDetailResult.getResult().getFormatted_address());
                mWebsite = placeDetailResult.getResult().getWebsite();
                mPhoneNumber = placeDetailResult.getResult().getFormattedPhoneNumber();

                try {
                    Glide.with(getApplicationContext())
                            .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + placeDetailResult.getResult().getPhotos().get(0).getPhotoReference() + "&key=" + BuildConfig.GOOGLE_API_KEY)
                            .apply(RequestOptions.centerCropTransform())
                            .into(photoImg);
                } catch (Exception e) {
                    Log.e(TAG, "onNext: ", e);
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

    private void disposeWhenDestroy(){
        if (this.mDisposable != null && !this.mDisposable.isDisposed()) this.mDisposable.dispose();
    }

    //-----------------------------
    // Actions
    //-----------------------------

    @OnClick({R.id.activity_restaurant_card_call_btn, R.id.activity_restaurant_card_like_btn, R.id.activity_restaurant_card_website_btn, R.id.activity_restaurant_card_check_btn})
    public void onClick(View view){
        switch(view.getId()){
            case R.id.activity_restaurant_card_call_btn:
                if (mPhoneNumber!=null){
                    Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+mPhoneNumber));
                    //callIntent.setData(Uri.parse("tel:"+mPhoneNumber));
                    startActivity(callIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "No phone number", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.activity_restaurant_card_like_btn:
                if (isLiked){
                    mLikeBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.grade_black_24x24), null, null);
                } else {
                    mLikeBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.grade_colorprimary), null, null);
                }
                isLiked=!isLiked;
                updateLikedRestaurant(isLiked);
                break;
            case R.id.activity_restaurant_card_website_btn:
                if (mWebsite!=null){
                    if (!mWebsite.startsWith("http://") && !mWebsite.startsWith("https://"))
                        mWebsite = "http://" + mWebsite;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mWebsite));
                    startActivity(browserIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "No website", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.activity_restaurant_card_check_btn:
                if (isMyLunch){
                    isMyLunch=false;
                    checkBtn.setImageResource(R.drawable.check_circle_black_24x24);
                } else {
                    isMyLunch=true;
                    checkBtn.setImageResource(R.drawable.check_coloraccent);
                }
                updateCheckedRestaurant(isMyLunch);
                break;
        }
    }

    private void updateLikedRestaurant(final boolean isLiked) {
        WorkmateHelper.getUser(Objects.requireNonNull(this.getCurrentUser()).getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Workmate currentUser = documentSnapshot.toObject(Workmate.class);
                List<String> likedPlaceId = currentUser.getRestaurantLikedPlaceId();
                if (isLiked){
                    likedPlaceId.add(mPlaceId);
                } else {
                    likedPlaceId.remove(mPlaceId);
                }
                WorkmateHelper.updateRestaurantLiked(getCurrentUser().getUid(), likedPlaceId).addOnFailureListener(onFailureListener());
            }
        });
    }

    private void updateCheckedRestaurant(boolean isMyLunch){
        if (this.getCurrentUser() != null) {
            if (isMyLunch) {
                WorkmateHelper.updateRestaurantID(this.getCurrentUser().getUid(), this.mPlaceId).addOnFailureListener(this.onFailureListener());
                WorkmateHelper.updateNameOfRestaurant(this.getCurrentUser().getUid(), this.nameTxt.getText().toString()).addOnFailureListener(this.onFailureListener());
            } else {
                WorkmateHelper.updateRestaurantID(this.getCurrentUser().getUid(), "").addOnFailureListener(this.onFailureListener());
                WorkmateHelper.updateNameOfRestaurant(this.getCurrentUser().getUid(), "").addOnFailureListener(this.onFailureListener());
            }
        }
    }


}
