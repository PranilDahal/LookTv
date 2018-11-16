package looktv.project.cs4540.looktv.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import looktv.project.cs4540.looktv.models.SearchResultModel;

public class NetworkUtils {
    //http://api.tvmaze.com/search/shows?q=girls

    public static final String BASE_URL = "http://api.tvmaze.com/search/shows";

    public static final String SHOWS = "q";

    public static URL buildURL(String showName) {

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(SHOWS, showName)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }

        } finally {
            urlConnection.disconnect();
        }

    }

    public static ArrayList<SearchResultModel> parseShows(String jsonData) {

        ArrayList<SearchResultModel> listOfShows = new ArrayList<SearchResultModel>();

        try {
            JSONArray shows = new JSONArray(jsonData);

            for (int i = 0; i < 10; i++) {

                try {
                    JSONObject curr = shows.getJSONObject(i);
                    Log.d("PARSING MAIN OBJECT", curr.toString());
                    JSONObject showObj = curr.getJSONObject("show");
                    Log.d("\tPARSING SHOW OBJ", curr.toString());
                    JSONObject ratingsObj = showObj.getJSONObject("rating");
                    Log.d("\tPARSING RATINGS OBJ", ratingsObj.toString());
                    JSONObject imagesObj = showObj.getJSONObject("image");
                    Log.d("\tPARSING IMAGE OBJ", imagesObj.toString());

                    String showId = showObj.getString("id");
                    String showName = showObj.getString("name");
                    String avgRatings = ratingsObj.getString("average");
                    String imageUrl = imagesObj.getString("original");
                    String website = showObj.getString("officialSite");
                    String status = showObj.getString("status");
                    String premeried = showObj.getString("premiered");
                    String language = showObj.getString("language");

                    listOfShows.add(new SearchResultModel(showId, showName, avgRatings, imageUrl,website,
                            language, premeried, status));
                } catch (JSONException e){
                    Log.d("ERROR WHILE PARSING", shows.getJSONObject(i).toString());
                    continue;
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listOfShows;
    }

}