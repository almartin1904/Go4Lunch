package com.openclassroom.alice.go4lunch.View;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.openclassroom.alice.go4lunch.Model.Workmate;
import com.openclassroom.alice.go4lunch.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alice on 12 January 2019.
 */
public class WorkmateViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.fragment_workmate_item_picture) ImageView mProfilePicture;
    @BindView(R.id.fragment_workmate_item_txt) TextView mRestaurantTxt;
    private static final String TAG = WorkmateViewHolder.class.getSimpleName();
    private Context mContext;

    public WorkmateViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mContext=context;
    }

    public void updateWithWorkmate(Workmate workmate, RequestManager glide){

        String temp;
        if (workmate.getRestaurantPlaceId().equals("")){
            temp=workmate.getFirstName()+mContext.getResources().getString(R.string.no_restaurant_picked);
        } else {
            temp=workmate.getFirstName()+mContext.getResources().getString(R.string.restaurant_picked)+workmate.getNameOfRestaurant();
        }

        mRestaurantTxt.setText(temp);

        glide.load(workmate.getUrlPicture())
                .apply(RequestOptions.circleCropTransform())
                .into(mProfilePicture);
    }
}
