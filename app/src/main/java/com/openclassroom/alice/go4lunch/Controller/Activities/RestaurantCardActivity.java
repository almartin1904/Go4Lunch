package com.openclassroom.alice.go4lunch.Controller.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.openclassroom.alice.go4lunch.Model.ResultOfRequest.DistanceResult;
import com.openclassroom.alice.go4lunch.Model.ResultOfRequest.PlaceDetailResult;
import com.openclassroom.alice.go4lunch.Model.Workmate;
import com.openclassroom.alice.go4lunch.Model.WorkmateHelper;
import com.openclassroom.alice.go4lunch.R;
import com.openclassroom.alice.go4lunch.Utils.PlacesAPIStreams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

import static com.openclassroom.alice.go4lunch.Constantes.CARD_DETAILS;


public class RestaurantCardActivity extends BaseActivity {

    private Disposable mDisposable;
    private String mWebsite;
    private String mPhoneNumber;
    private boolean isMyLunch=false;
    private static final String TAG = RestaurantCardActivity.class.getSimpleName();
    private String mPlaceId;

    @BindView(R.id.restaurant_name_txt) TextView nameTxt;
    @BindView(R.id.restaurant_address_txt) TextView addressTxt;
    @BindView(R.id.restaurant_imageView) ImageView photoImg;
    @BindView(R.id.check_btn) FloatingActionButton checkBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i=getIntent();
        mPlaceId = (String) i.getSerializableExtra(CARD_DETAILS);

        this.executeHttpRequest(mPlaceId);
        this.updateUiWhenCreating();
    }

    private void updateUiWhenCreating() {
        WorkmateHelper.getUser(this.getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Workmate currentUser = documentSnapshot.toObject(Workmate.class);
                String registeredPlaceId = TextUtils.isEmpty(currentUser.getRestaurantPlaceId()) ? "" : currentUser.getRestaurantPlaceId();
                if (registeredPlaceId.equals(mPlaceId)){
                    isMyLunch=true;
                    checkBtn.setImageResource(R.drawable.check_coloraccent);
                }
            }
        });
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_restaurant_card;
    }

    private void executeHttpRequest(String placeId) {
        this.mDisposable = PlacesAPIStreams.streamFetchDetailPlace(placeId).subscribeWith(new DisposableObserver<PlaceDetailResult>() {
            @Override
            public void onNext(PlaceDetailResult placeDetailResult) {
                nameTxt.setText(placeDetailResult.getResult().getName());
                addressTxt.setText(placeDetailResult.getResult().getFormatted_address());
                mWebsite=placeDetailResult.getResult().getWebsite();
                mPhoneNumber=placeDetailResult.getResult().getFormattedPhoneNumber();

                try {
                    Glide.with(getApplicationContext())
                            .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+ placeDetailResult.getResult().getPhotos().get(0).getPhotoReference()+ "&key=AIzaSyB0bbKRXlGkEbvEFjxXyACgyAJrZLGS42w")
                            .apply(RequestOptions.centerCropTransform())
                            .into(photoImg);
                } catch (Exception e){
                    Log.e(TAG, "onNext: ", e);
                }
            }

            @Override
            public void onError(Throwable e) { }

            @Override
            public void onComplete() { }
        });
    }

    @OnClick({R.id.call_btn, R.id.like_btn, R.id.website_btn, R.id.check_btn})
    public void onClick(View view){
        switch(view.getId()){
            case R.id.call_btn:
                if (mPhoneNumber!=null){
                    Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+mPhoneNumber));
                    //callIntent.setData(Uri.parse("tel:"+mPhoneNumber));
                    startActivity(callIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "No phone number", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.like_btn:
                break;
            case R.id.website_btn:
                if (mWebsite!=null){
                    if (!mWebsite.startsWith("http://") && !mWebsite.startsWith("https://"))
                        mWebsite = "http://" + mWebsite;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mWebsite));
                    startActivity(browserIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "No website", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.check_btn:
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
