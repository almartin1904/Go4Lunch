package com.openclassroom.alice.go4lunch.Controller.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.openclassroom.alice.go4lunch.Controller.Activities.MainActivity;
import com.openclassroom.alice.go4lunch.Controller.Activities.RestaurantCardActivity;
import com.openclassroom.alice.go4lunch.Model.ResultOfRequest.RequestResult;
import com.openclassroom.alice.go4lunch.Model.ResultOfRequest.Restaurant;
import com.openclassroom.alice.go4lunch.R;
import com.openclassroom.alice.go4lunch.Utils.ItemClickSupport;
import com.openclassroom.alice.go4lunch.Utils.PlacesAPIStreams;
import com.openclassroom.alice.go4lunch.View.RestaurantAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantsListFragment extends Fragment {

    private static final String CARD_DETAILS = "CARD_DETAILS";
    @BindView(R.id.fragment_main_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.fragment_listview_swipe_container) SwipeRefreshLayout swipeRefreshLayout;

    Disposable mDisposable;
    private List<Restaurant> mRestaurants;
    private RestaurantAdapter mAdapter;
    private static final String TAG = RestaurantsListFragment.class.getSimpleName();

    public RestaurantsListFragment() {
        // Required empty public constructor
    }

    public static RestaurantsListFragment newInstance() {
        return (new RestaurantsListFragment());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurants_list, container, false);
        ButterKnife.bind(this, view);
        this.configureRecyclerView();
        this.executeHTTPRequest();
        this.configureSwipeRefreshLayout();
        this.configureOnClickRecyclerView();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    // -----------------
    // CONFIGURATION
    // -----------------

    private void configureRecyclerView() {
        this.mRestaurants = new ArrayList<>();
        this.mAdapter = new RestaurantAdapter(this.mRestaurants, Glide.with(this));
        this.recyclerView.setAdapter(this.mAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void configureSwipeRefreshLayout(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                executeHTTPRequest();
            }
        });
    }

    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(recyclerView, R.layout.fragment_restaurant_item)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Restaurant restaurant = mAdapter.getRestaurant(position);
                        Intent restaurantCardActivity = new Intent(getActivity(), RestaurantCardActivity.class);
                        restaurantCardActivity.putExtra(CARD_DETAILS, restaurant.getPlaceId());
                        startActivity(restaurantCardActivity);
                    }
                });
    }

    // -------------------
    // HTTP (RxJAVA)
    // -------------------

    private void executeHTTPRequest(){
        this.mDisposable = PlacesAPIStreams.streamFetchRestaurants().subscribeWith(new DisposableObserver<RequestResult>() {
            @Override
            public void onNext(RequestResult requestResult) {
                updateUI(requestResult.getResults());
            }

            @Override
            public void onError(Throwable e) { }

            @Override
            public void onComplete() { }
        });

    }

    private void disposeWhenDestroy(){
        if (this.mDisposable != null && !this.mDisposable.isDisposed()) this.mDisposable.dispose();
    }


    // -------------------
    // UPDATE UI
    // -------------------

    private void updateUI(List<Restaurant> restaurants){
        swipeRefreshLayout.setRefreshing(false);
        mRestaurants.clear();
        mRestaurants.addAll(restaurants);
        mAdapter.notifyDataSetChanged();
    }


}
