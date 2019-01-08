package com.openclassroom.alice.go4lunch.Controller.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.openclassroom.alice.go4lunch.Model.ResultOfRequest.RequestResult;
import com.openclassroom.alice.go4lunch.Model.ResultOfRequest.Restaurant;
import com.openclassroom.alice.go4lunch.R;
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

    @BindView(R.id.fragment_main_recycler_view) RecyclerView recyclerView;
    Disposable mDisposable;
    private List<Restaurant> mRestaurants;
    private RestaurantAdapter mAdapter;

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
        this.mAdapter = new RestaurantAdapter(this.mRestaurants);
        this.recyclerView.setAdapter(this.mAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        mRestaurants.addAll(restaurants);
        mAdapter.notifyDataSetChanged();
    }


}
