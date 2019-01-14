package com.openclassroom.alice.go4lunch.View;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.openclassroom.alice.go4lunch.Model.ResultOfRequest.DistanceResult;
import com.openclassroom.alice.go4lunch.Model.ResultOfRequest.PlaceDetailResult;
import com.openclassroom.alice.go4lunch.Model.ResultOfRequest.Restaurant;
import com.openclassroom.alice.go4lunch.R;
import com.openclassroom.alice.go4lunch.Utils.PlacesAPIStreams;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by Alice on 08 January 2019.
 */
public class RestaurantViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = RestaurantViewHolder.class.getSimpleName();

    @BindView(R.id.fragment_main_item_name) TextView mNameTxt;
    @BindView(R.id.fragment_main_item_address) TextView mAddressTxt;
    @BindView(R.id.fragment_main_item_schedule) TextView mScheduleTxt;
    @BindView(R.id.fragment_main_item_distance) TextView mDistanceTxt;
    @BindView(R.id.fragment_main_item_star_1) ImageView mStarImg1;
    @BindView(R.id.fragment_main_item_star_2) ImageView mStarImg2;
    @BindView(R.id.fragment_main_item_star_3) ImageView mStarImg3;
    @BindView(R.id.fragment_main_item_image) ImageView mProfileImg;

    Disposable mDisposable;

    public RestaurantViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithRestaurants(Restaurant restaurant, RequestManager glide){

        try {
            this.mNameTxt.setText(restaurant.getName());
            this.mAddressTxt.setText(restaurant.getAddress());

            setOpeningHour(restaurant);
            setDistance("place_id:"+restaurant.getPlaceId());
            setPhoto(restaurant, glide);
            setStars(restaurant);
        } catch (Exception e){
            Log.e(TAG, "updateWithRestaurants: " + restaurant.getName() + " : "+ e.getMessage());
        }

    }

    private void setPhoto(Restaurant restaurant, RequestManager glide) {
        if (restaurant.getPhotos()!=null) {
            glide.load(getPhotoURL(restaurant.getPhotos().get(0).getPhotoReference()))
                    .apply(RequestOptions.centerCropTransform())
                    .into(mProfileImg);
        }
    }


    private void setStars(Restaurant restaurant) {
        if (restaurant.getRatingStars()<2){
            mStarImg3.setVisibility(View.GONE);
        } else {
            mStarImg3.setVisibility(View.VISIBLE);
        }
        if (restaurant.getRatingStars()<1){
            mStarImg2.setVisibility(View.GONE);
        } else {
            mStarImg2.setVisibility(View.VISIBLE);
        }
    }

    private String getPhotoURL(String photoReference){
        return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+ photoReference + "&key=AIzaSyB0bbKRXlGkEbvEFjxXyACgyAJrZLGS42w";
    }

    private void setDistance(String placeID){
        this.mDisposable = PlacesAPIStreams.streamFetchDistance(placeID).subscribeWith(new DisposableObserver<DistanceResult>() {
            @Override
            public void onNext(DistanceResult distanceResult) {
                mDistanceTxt.setText(distanceResult.getRows().get(0).getElements().get(0).getDistance().getText());
            }

            @Override
            public void onError(Throwable e) { }

            @Override
            public void onComplete() { }
        });

    }

    private void setOpeningHour(Restaurant restaurant){
        final int currentDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1;
        final int currentHour = Calendar.getInstance().get(Calendar.HOUR)+12;
        final int currentMinute = Calendar.getInstance().get(Calendar.MINUTE);
        if (restaurant.getOpeningHours()!=null) {
            this.mDisposable = PlacesAPIStreams.streamFetchDetailPlace(restaurant.getPlaceId()).subscribeWith(new DisposableObserver<PlaceDetailResult>() {
                @Override
                public void onNext(PlaceDetailResult placeDetailResult) {
                    if (placeDetailResult.getResult().getOpeningHours().getOpenNowString(currentDayOfWeek, currentHour, currentMinute).equals("Closing Soon")) {
                        mScheduleTxt.setTextColor(Color.RED);
                    } else {
                        mScheduleTxt.setTextColor(Color.BLACK);
                    }
                    mScheduleTxt.setText(placeDetailResult.getResult().getOpeningHours().getOpenNowString(currentDayOfWeek, currentHour, currentMinute));
                }

                @Override
                public void onError(Throwable e) { }

                @Override
                public void onComplete() { }
            });
        } else {
            mScheduleTxt.setText("No information");
        }
    }
}
