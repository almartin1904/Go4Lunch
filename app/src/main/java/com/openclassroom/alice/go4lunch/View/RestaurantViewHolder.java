package com.openclassroom.alice.go4lunch.View;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.openclassroom.alice.go4lunch.BuildConfig;
import com.openclassroom.alice.go4lunch.Model.ResultOfRequest.PlaceDetailResult;
import com.openclassroom.alice.go4lunch.Model.ResultOfRequest.Restaurant;
import com.openclassroom.alice.go4lunch.R;
import com.openclassroom.alice.go4lunch.Utils.PlacesAPIStreams;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

import static com.openclassroom.alice.go4lunch.Constantes.CLOSED;
import static com.openclassroom.alice.go4lunch.Constantes.CLOSING_SOON;
import static com.openclassroom.alice.go4lunch.Constantes.OPEN;
import static com.openclassroom.alice.go4lunch.Constantes.OPEN_24_7;

/**
 * Created by Alice on 08 January 2019.
 */
public class RestaurantViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = RestaurantViewHolder.class.getSimpleName();

    @BindView(R.id.fragment_restaurant_item_name) TextView mNameTxt;
    @BindView(R.id.fragment_restaurant_item_address) TextView mAddressTxt;
    @BindView(R.id.fragment_restaurant_item_schedule) TextView mScheduleTxt;
    @BindView(R.id.fragment_restaurant_item_distance) TextView mDistanceTxt;
    @BindView(R.id.fragment_restaurant_item_star_1) ImageView mStarImg1;
    @BindView(R.id.fragment_restaurant_item_star_2) ImageView mStarImg2;
    @BindView(R.id.fragment_restaurant_item_star_3) ImageView mStarImg3;
    @BindView(R.id.fragment_restaurant_item_image) ImageView mProfileImg;

    private Context mContext;

    public RestaurantViewHolder(View itemView, Context context) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mContext =context;
    }

    public void updateWithRestaurants(Restaurant restaurant, RequestManager glide){

        try {
            this.mNameTxt.setText(restaurant.getName());
            this.mAddressTxt.setText(restaurant.getAddress());

            setOpeningHour(restaurant);
            setDistance(restaurant);
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
        return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+ photoReference + "&key="+BuildConfig.GOOGLE_API_KEY;
    }

    private String computeDistance(double deviceLat, double restaurantLat, double deviceLng, double restaurantLng) {

        final int R = 6371; // Radius of earth
        double latDistance = Math.toRadians(restaurantLat - deviceLat);
        double lonDistance = Math.toRadians(restaurantLng - deviceLng);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(deviceLat)) * Math.cos(Math.toRadians(restaurantLat))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = 0.0 - 0.0;
        distance = Math.pow(distance, 2) + Math.pow(height, 2);
        //Rounded
        return Math.round(Math.sqrt(distance)) + "m";
    }

    private void setDistance(Restaurant restaurant){
        mDistanceTxt.setText(computeDistance(50.633258, restaurant.getGeometry().getLocation().getLat(), 3.020537, restaurant.getGeometry().getLocation().getLng()));
    }

    private void setOpeningHour(Restaurant restaurant){
        final int currentDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1;
        final int currentHour = Calendar.getInstance().get(Calendar.HOUR)+12;
        final int currentMinute = Calendar.getInstance().get(Calendar.MINUTE);
        if (restaurant.getOpeningHours()!=null) {
            Disposable disposable = PlacesAPIStreams.streamFetchDetailPlace(restaurant.getPlaceId()).subscribeWith(new DisposableObserver<PlaceDetailResult>() {
                @Override
                public void onNext(PlaceDetailResult placeDetailResult) {
                    if (placeDetailResult.getResult().getOpeningHours().getOpenNowString(currentDayOfWeek, currentHour, currentMinute) == CLOSING_SOON) {
                        mScheduleTxt.setTextColor(Color.RED);
                    } else {
                        mScheduleTxt.setTextColor(Color.BLACK);
                    }
                    String temp;
                    switch (placeDetailResult.getResult().getOpeningHours().getOpenNowString(currentDayOfWeek, currentHour, currentMinute)) {
                        case OPEN_24_7:
                            temp = mContext.getResources().getString(R.string.open24_7);
                            break;
                        case OPEN:
                            temp = mContext.getResources().getString(R.string.open) + placeDetailResult.getResult().getOpeningHours().getHourWithFormat(placeDetailResult.getResult().getOpeningHours().getClosingHour(currentDayOfWeek, currentHour));
                            break;
                        case CLOSING_SOON:
                            temp = mContext.getResources().getString(R.string.closing_soon);
                            break;
                        case CLOSED:
                            temp = mContext.getResources().getString(R.string.closed);
                            break;
                        default:
                            temp = mContext.getResources().getString(R.string.no_hour_information);
                            break;
                    }
                    mScheduleTxt.setText(temp);
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onComplete() {
                }
            });
        } else {
            mScheduleTxt.setText(mContext.getResources().getString(R.string.no_hour_information));
        }
    }
}
