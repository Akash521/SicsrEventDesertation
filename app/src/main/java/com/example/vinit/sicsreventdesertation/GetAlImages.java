package com.example.vinit.sicsreventdesertation;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class GetAlImages {

    Login login=new Login();
    String URL_IP=login.getURL_IP();
    public static String[] imageURLs;
    public static Bitmap[] bitmaps;
    public static String[] name;

    public static final String JSON_ARRAY="result";
    public static final String IMAGE_URL = "url";
    private String json;
    private JSONArray urls;

    public GetAlImages(String json){

        this.json = json;
        Log.d("After JSON",json);
        try {
            JSONObject jsonObject = new JSONObject(json);
            Log.d("After Inside getAll Images","Inside");
            urls = jsonObject.getJSONArray(JSON_ARRAY);

            Log.d("After Length", String.valueOf(urls.length()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Bitmap getImage(JSONObject jo){
        URL url = null;
        Bitmap image = null;
        try {
            url = new URL(URL_IP+"/upload/"+jo.getString(IMAGE_URL));
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void getAllImages() throws JSONException {
        Log.d("After getAllImages size", String.valueOf(urls.length()));
        bitmaps = new Bitmap[urls.length()];
        name = new String[urls.length()];
        imageURLs = new String[urls.length()];

        //Log.d("After getAllImages size", String.valueOf(urls.length()));
        for(int i=0;i<urls.length();i++){
            imageURLs[i] = urls.getJSONObject(i).getString(IMAGE_URL);
            name[i] = urls.getJSONObject(i).getString("name");
            JSONObject jsonObject = urls.getJSONObject(i);
            bitmaps[i]=getImage(jsonObject);
        }
    }
}