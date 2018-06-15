package com.nphan.android.harvardartmuseum;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HarvardArtMuseumFetch {

    private static final String TAG = "ArtFetch";
    private static final String API_KEY = "57818250-6aaa-11e8-932e-e1bd830b7046";
    private static final String CULTURE_RESOURCE = "culture";
    private static final String OBJECT_RESOURCE = "object";
    private static final Uri ENDPOINT = Uri
            .parse("https://api.harvardartmuseums.org")
            .buildUpon()
            .appendQueryParameter("apikey", API_KEY)
            .build();

    private byte[] getUrlBytes(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlString);
            }

            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        }
        finally {
            connection.disconnect();
        }
    }

    private String getUrlString(String urlString) throws IOException {
        return new String(getUrlBytes(urlString));
    }

    private String buildUrl(String  cultureId) {
        /*
        Note about Harvard Art Museum API:
        There are around 255 cultures, so giving size = 1000 will return all cultures in one page.
        For objects, the maximum objects returned per page is 100.

         */
        Uri.Builder uriBuilder = ENDPOINT.buildUpon();
        if (cultureId == null) {
            // Display list of all cultures
            uriBuilder
                    .appendPath(CULTURE_RESOURCE)
                    .appendQueryParameter("size", "1000");
        }
        else {
            // Display list of objects of a certain culture
            uriBuilder
                    .appendPath(OBJECT_RESOURCE)
                    .appendQueryParameter(CULTURE_RESOURCE, cultureId)
                    .appendQueryParameter("size", "100");
        }
        return uriBuilder.build().toString();
    }

    public List<CultureItem> fetchCultureItems() {

        List<CultureItem> items = new ArrayList<>();

        try {
            String urlString = buildUrl(null);
            String jsonString = getUrlString(urlString);
            Log.i(TAG, urlString);
            Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonObject = new JSONObject(jsonString);
            parseCultureItems(items, jsonObject);
        }
        catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }
        catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }

        return items;
    }

    private void parseCultureItems(List<CultureItem> items, JSONObject jsonObject) throws JSONException {
        JSONArray recordsJsonArray = jsonObject.getJSONArray("records");

        for (int i = 0; i < recordsJsonArray.length(); i++) {
            JSONObject cultureJsonObject = recordsJsonArray.getJSONObject(i);

            CultureItem item = new CultureItem();
            item.setId(cultureJsonObject.getString("id"));
            item.setCulture(cultureJsonObject.getString("name"));
            Log.i(TAG, cultureJsonObject.getString("name"));
            items.add(item);
        }
        Collections.sort(items, new Comparator<CultureItem>() {
            @Override
            public int compare(CultureItem o1, CultureItem o2) {
                return o1.getCulture().compareTo(o2.getCulture());
            }
        });
    }

    public List<ObjectItem> getObjectItems(String cultureId) {
        List<ObjectItem> items = new ArrayList<>();

        try {
            String urlString = buildUrl(cultureId);
            String jsonString = getUrlString(urlString);
            JSONObject jsonObject = new JSONObject(jsonString);
            parseObjectItems(items, jsonObject);
        }
        catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }
        catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }

        return items;
    }

    private void parseObjectItems(List<ObjectItem> items, JSONObject jsonObject) throws JSONException {
        JSONArray recordsJsonArrray = jsonObject.getJSONArray("records");

        for (int i = 0; i < recordsJsonArrray.length(); i++) {
            JSONObject js = recordsJsonArrray.getJSONObject(i);

            if (!js.has("primaryimageurl") || js.isNull("primaryimageurl")) {
                break;
            }

            ObjectItem item = new ObjectItem();
            item.setTitle(js.getString("title"));
            item.setPrimaryImageUrl(js.getString("primaryimageurl"));
            item.setDated(js.getString("dated"));
            item.setPeriod(js.getString("period"));
            item.setMedium(js.getString("medium"));
            items.add(item);
        }
    }
}
