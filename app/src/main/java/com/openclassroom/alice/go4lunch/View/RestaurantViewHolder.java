package com.openclassroom.alice.go4lunch.View;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.openclassroom.alice.go4lunch.Model.ResultOfRequest.Restaurant;
import com.openclassroom.alice.go4lunch.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alice on 08 January 2019.
 */
public class RestaurantViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.fragment_main_item_name) TextView textView;

    public RestaurantViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithGithubUser(Restaurant restaurant){
        this.textView.setText(restaurant.getName());
    }
}
