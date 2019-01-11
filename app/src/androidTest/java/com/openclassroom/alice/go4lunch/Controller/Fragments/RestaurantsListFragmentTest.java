package com.openclassroom.alice.go4lunch.Controller.Fragments;

import com.openclassroom.alice.go4lunch.Model.ResultOfRequest.OpeningHours;
import com.openclassroom.alice.go4lunch.Model.ResultOfRequest.PlaceDetailResult;
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
        Observable<PlaceDetailResult> observableDetail = PlacesAPIStreams.streamFetchDetailPlace("ChIJK2CVUV7VwkcRedYLyCXbmhs");
        TestObserver<PlaceDetailResult> testObserver = new TestObserver<>();
        observableDetail.subscribeWith(testObserver)
                .assertNoErrors()
                .assertNoTimeout()
                .awaitTerminalEvent();

        PlaceDetailResult DetailsFetched = testObserver.values().get(0);
        OpeningHours openingHours=DetailsFetched.getResult().getOpeningHours();
        assertEquals("Open until 23.30pm", openingHours.getOpenNowString(0, 15, 0));
    }

}