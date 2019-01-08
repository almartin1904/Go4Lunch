package com.openclassroom.alice.go4lunch.Controller.Fragments;

import com.openclassroom.alice.go4lunch.Model.ResultOfRequest.RequestResult;
import com.openclassroom.alice.go4lunch.Utils.PlacesAPIStreams;

import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.*;

/**
 * Created by Alice on 08 January 2019.
 */
public class RestaurantsListFragmentTest {
    @Test
    public void fetchArticleTopStories() throws Exception {
        Observable<RequestResult> observableUsers = PlacesAPIStreams.streamFetchRestaurants();
        TestObserver<RequestResult> testObserver = new TestObserver<>();
        observableUsers.subscribeWith(testObserver)
                .assertNoErrors()
                .assertNoTimeout()
                .awaitTerminalEvent();

        RequestResult restaurantsFetched = testObserver.values().get(0);
        assertEquals("Cruise Bar, Restaurant & Events", restaurantsFetched.getResults().get(0).getName());
    }

}