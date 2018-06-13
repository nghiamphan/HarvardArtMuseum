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

    public byte[] getUrlBytes(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlString);
            }

            int bytesRead = 0;
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

    public String getUrlString(String urlString) throws IOException {
        return new String(getUrlBytes(urlString));
    }

    public List<CultureItem> fetchItems() {

        List<CultureItem> items = new ArrayList<>();

        try {
            String url = Uri.parse("https://api.harvardartmuseums.org/culture")
                    .buildUpon()
                    .appendQueryParameter("apikey", API_KEY)
                    .appendQueryParameter("size", "1000")
                    .build().toString();
            String jsonString = getUrlString(url);
            Log.i(TAG, url);
            Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonObject = new JSONObject(jsonString);
            parseItems(items, jsonObject);
        }
        catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }
        catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }

        return items;
    }

    private void parseItems(List<CultureItem> items, JSONObject jsonObject) throws IOException, JSONException {
        JSONArray recordsJsonArray = jsonObject.getJSONArray("records");

        for (int i = 0; i < recordsJsonArray.length(); i++) {
            JSONObject periodJsonObject = recordsJsonArray.getJSONObject(i);

            CultureItem item = new CultureItem();
            item.setCulture(periodJsonObject.getString("name"));
            Log.i(TAG, periodJsonObject.getString("name"));
            items.add(item);
        }
        Collections.sort(items, new Comparator<CultureItem>() {
            @Override
            public int compare(CultureItem o1, CultureItem o2) {
                return o1.getCulture().compareTo(o2.getCulture());
            }
        });
    }
}
