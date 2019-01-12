package com.openclassroom.alice.go4lunch.Controller.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.openclassroom.alice.go4lunch.Model.ResultOfRequest.DistanceResult;
import com.openclassroom.alice.go4lunch.Model.ResultOfRequest.PlaceDetailResult;
import com.openclassroom.alice.go4lunch.R;
import com.openclassroom.alice.go4lunch.Utils.PlacesAPIStreams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

import static com.openclassroom.alice.go4lunch.Constantes.CARD_DETAILS;


public class RestaurantCardActivity extends AppCompatActivity {

    private Disposable mDisposable;
    private String mWebsite;
    private String mPhoneNumber;
    private boolean isMyLunch=false;
    private static final String TAG = RestaurantCardActivity.class.getSimpleName();

    @BindView(R.id.restaurant_name_txt) TextView nameTxt;
    @BindView(R.id.restaurant_address_txt) TextView addressTxt;
    @BindView(R.id.restaurant_imageView) ImageView photoImg;
    @BindView(R.id.check_btn) FloatingActionButton checkBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_card);

        ButterKnife.bind(this);
        Intent i=getIntent();
        String placeId = (String) i.getSerializableExtra(CARD_DETAILS);

        executeHttpRequest(placeId);
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
                break;
        }
    }
}
