package com.openclassroom.alice.go4lunch.Model.ResultOfRequest;

import org.junit.Test;

import java.util.Calendar;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Created by Alice on 08 January 2019.
 */
public class OpeningHoursTest {
    @Test
    public void compareHoursTestFalse(){
        OpeningHours openingHours = new OpeningHours();
        boolean bool=openingHours.compareHours("1500", 12, 30);
        assertFalse(bool);
    }

    @Test
    public void compareHoursTestTrue(){
        OpeningHours openingHours = new OpeningHours();
        assertTrue(openingHours.compareHours("1500", 17, 30));
    }

    @Test
    public void compareHoursTestTrueWithMinutes(){
        OpeningHours openingHours = new OpeningHours();
        assertTrue(openingHours.compareHours("1700", 17, 30));
    }

    @Test
    public void compareHoursTestFalseWithMinutes(){
        OpeningHours openingHours = new OpeningHours();
        assertFalse(openingHours.compareHours("1800", 17, 30));
    }

}