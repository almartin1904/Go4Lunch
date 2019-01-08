package com.openclassroom.alice.go4lunch.Controller.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.openclassroom.alice.go4lunch.Model.ResultOfRequest.RequestResult;
import com.openclassroom.alice.go4lunch.R;
import com.openclassroom.alice.go4lunch.Utils.PlacesAPIStreams;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantsListFragment extends Fragment {

    @BindView(R.id.textview) TextView mTextView;
    Disposable mDisposable;

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
        this.executeHTTPRequest();
        return view;

    }

    private void executeHTTPRequest(){
        this.mDisposable = PlacesAPIStreams.streamFetchRestaurants().subscribeWith(new DisposableObserver<RequestResult>() {
            @Override
            public void onNext(RequestResult requestResult) {
                mTextView.setText(requestResult.getResults().get(0).getName());
            }

            @Override
            public void onError(Throwable e) { }

            @Override
            public void onComplete() { }
        });

    }



}
