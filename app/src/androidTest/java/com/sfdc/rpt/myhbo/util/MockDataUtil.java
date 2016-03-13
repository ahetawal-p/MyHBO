package com.sfdc.rpt.myhbo.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utility for serving mock json data to the various tests
 * Created by ahetawal on 3/12/16.
 */
public class MockDataUtil {

    /**
     * Mock Success movie search response
     *
     */
    public static JSONObject getMockSuccessReponse() throws JSONException {
        JSONObject wrapperObj = new JSONObject();
        wrapperObj.put("Response", true);
        wrapperObj.put("totalResults", 3);

        JSONArray resultsArray = new JSONArray();
        JSONObject childObj1 = new JSONObject();
        childObj1.put("Title", "Test Pilot");
        childObj1.put("Year", "1938");
        childObj1.put("imdbID", "tt0030848");
        childObj1.put("Poster", "http://nodomain.com/1");
        resultsArray.put(childObj1);

        JSONObject childObj2 = new JSONObject();
        childObj2.put("Title", "Test");
        childObj2.put("Year", "2014");
        childObj2.put("imdbID", "tt4131188");
        childObj2.put("Poster", "http://nodomain.com/2");
        resultsArray.put(childObj2);

        JSONObject childObj3 = new JSONObject();
        childObj3.put("Title", "3 Day Test");
        childObj3.put("Year", "2012");
        childObj3.put("imdbID", "tt2184095");
        childObj3.put("Poster", "http://nodomain.com/3");
        resultsArray.put(childObj3);

        wrapperObj.put("Search", resultsArray);
        return wrapperObj;

    }

    /**
     * Mock Failed response
     *
     */
    public static JSONObject getMockNoResultsResponse() throws JSONException {
        JSONObject wrapperObj = new JSONObject();
        wrapperObj.put("Response", false);
        wrapperObj.put("Error", "Movie not found!");
        return wrapperObj;

    }

    /**
     * Mock movie Detail response
     */
    public static JSONObject getMockDetailSuccessReponse() throws JSONException {
        JSONObject detailObj = new JSONObject();
        detailObj.put("Response", true);
        detailObj.put("Title", "Rabbit Test");
        detailObj.put("Genre", "Comedy");
        detailObj.put("Year", "1978");
        detailObj.put("imdbID", "tt0078133");
        detailObj.put("Poster", "http://ia.media-imdb.com/images/M/MV5BMTI1MDEwNDI5Ml5BMl5BanBnXkFtZTYwNTQ1Mjg5._V1_SX300.jpg");
        detailObj.put("Director", "Joan Rivers");
        detailObj.put("Writer", "Test Writer");
        detailObj.put("Actors", "Actor1, Actor2");
        detailObj.put("Plot", "This is a detailed plot for the movie");

        return detailObj;
    }
}
