package com.example.vinit.sicsreventdesertation;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class Home extends Fragment {

    Login login=new Login();
    String URL_IP=login.getURL_IP();

    ImageView img0,img1,img2;
    TextView txtHeader;
    private String imagesJSON;

    private static final String JSON_ARRAY ="result";
    String STATIC_URL=URL_IP+"/upload/";
    String IMAGES_URL=URL_IP+"/EventsFeatured.php";
    private static final String IMAGE_URL = "url";

    private JSONArray arrayImages= null;
    ArrayList<String> arrImage;
    Bitmap image = null;
    private int TRACK = 0;

    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView= inflater.inflate(R.layout.fragment_home, container, false);
        img0=(ImageView)rootView.findViewById(R.id.imgHome_FeaturedEvent);
        img1=(ImageView)rootView.findViewById(R.id.imgHome_Nowhappening);
        img2=(ImageView)rootView.findViewById(R.id.imgHome_Upcoming);

        NetCheck nc=new NetCheck();
        nc.execute();

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment mFragment = new Events();
                Bundle args = new Bundle();
                args.putString("img", "NowHappening");
                mFragment.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(getId(), mFragment).commit();
            }
        });


        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        Fragment mFragment = new Events();
                        Bundle args = new Bundle();
                        args.putString("img", "Upcoming");
                        mFragment.setArguments(args);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(getId(), mFragment).commit();
                    }
                });
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity=getActivity();
        ((HomeNavigation)activity).onSectionAttached(0);
    }

    private class NetCheck extends AsyncTask {
        private Boolean val = false;

        @Override
        protected Object doInBackground(Object[] params) {
            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        val = true;
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (val) {
                getAllImages();
            } else {
                AlertDialog dlgAlert = new AlertDialog.Builder(getActivity()).create();
                dlgAlert.setMessage("Please Check your network connection ..");
                dlgAlert.setTitle("No Network Connection");
                dlgAlert.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dlgAlert.setCancelable(true);
                dlgAlert.show();
            }
        }


        private void getAllImages() {
            class GetAllImages extends AsyncTask<String, Void, String> {
                //            ProgressDialog loading;
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
//                loading = ProgressDialog.show(getActivity(), "Fetching Data","Please Wait...",true,true);
                }

                @Override
                protected String doInBackground(String... params) {
                    String uri = params[0];
                    BufferedReader bufferedReader = null;
                    try {
                        URL url = new URL(uri);
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        StringBuilder sb = new StringBuilder();

                        bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                        String json;
                        while ((json = bufferedReader.readLine()) != null) {
                            sb.append(json + "\n");
                        }

                        return sb.toString().trim();

                    } catch (Exception e) {
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(String s) {
                    //super.onPostExecute(s);
//                loading.dismiss();
//                Log.d("After String builder", s);
                    imagesJSON = s;
                    showImage();
                }
            }
            GetAllImages gai = new GetAllImages();
            gai.execute(IMAGES_URL);
        }

        private void getImage(String urlToImage) {
            class GetImage extends AsyncTask<String, Void, ArrayList<Bitmap>> {
                //            ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
//                loading = ProgressDialog.show(MainActivity.this,"Downloading Image...","Please wait...",true,true);
                }
                @Override
                protected ArrayList<Bitmap> doInBackground(String... params) {
                    ArrayList<Bitmap> image= new ArrayList<Bitmap>();
                    URL url = null;

                    arrImage=callReplace(params[0].toString());

                    if (arrImage.get(0).toString().equals("")) {
                        //list.setAdapter(null);
                    } else {
                        for(int i=0;i<arrImage.size();i++)
                            try {
                                url = new URL(STATIC_URL + arrImage.get(i).toString());
//                                Log.d("After URL",url.toString());
                                image.add(BitmapFactory.decodeStream(url.openConnection().getInputStream()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                    }
                    return image;
                }

                @Override
                protected void onPostExecute(ArrayList<Bitmap> bitmap) {
                    super.onPostExecute(bitmap);
                    img0.setImageBitmap(bitmap.get(0));
                    img1.setImageBitmap(bitmap.get(1));
                    img2.setImageBitmap(bitmap.get(2));
                }
            }
            GetImage gi = new GetImage();
            gi.execute(urlToImage);
        }

        private void showImage() {
            try {
//                Log.d("After show Images","SHow images");
                JSONObject jsonObject = new JSONObject(imagesJSON);
                //JSONObject jsonObject = arrayImages.getJSONObject(TRACK);
//                Log.d("After json object",jsonObject.getString(IMAGE_URL));
                getImage(jsonObject.getString(IMAGE_URL));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private ArrayList<String> callReplace(String res) {
        res=res.replaceAll("\\[", "");
        res=res.replaceAll("\\]", "");
        res=res.replaceAll("\"", "");
        ArrayList<String> arr=new ArrayList<String>(Arrays.asList(res.split(",")));
        return arr;
    }
}
