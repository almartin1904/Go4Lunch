package com.openclassroom.alice.go4lunch.View;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.openclassroom.alice.go4lunch.Model.ResultOfRequest.Restaurant;
import com.openclassroom.alice.go4lunch.R;
import com.openclassroom.alice.go4lunch.Utils.PlacesAPIStreams;

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
            this.mScheduleTxt.setText(restaurant.getOpeningHours().getOpenNowString());
            this.mDistanceTxt.setText(String.valueOf(restaurant.getDistance()));
            if (restaurant.getPhotos()!=null) {
                glide.load(getPhotoURL(restaurant.getPhotos().get(0).getPhotoReference())).apply(RequestOptions.centerCropTransform()).into(mProfileImg);
            }
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
        } catch (Exception e){
            Log.e(TAG, "updateWithRestaurants: " + restaurant.getName() + " : "+ e.getMessage());
        }

    }

    private String getPhotoURL(String photoReference){
        return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+ photoReference + "&key=AIzaSyB0bbKRXlGkEbvEFjxXyACgyAJrZLGS42w";
    }
}
