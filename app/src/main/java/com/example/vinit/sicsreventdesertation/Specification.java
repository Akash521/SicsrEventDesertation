package com.example.vinit.sicsreventdesertation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


/**
 * A simple {@link Fragment} subclass.
 */
public class Specification extends Activity {


    Login login=new Login();
    String URL_IP=login.getURL_IP();
    TextView txtEventName,descEvent;
    ImageView imgEvent;
    String ename,eveName,eveDesc,eveCat;


    public Specification() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specification);

        txtEventName = (TextView) findViewById(R.id.txtSpec_eventName);
        imgEvent = (ImageView) findViewById(R.id.eventImage);
        descEvent = (TextView) findViewById(R.id.descriptionEvent);

        Intent i =getIntent();
        int id = i.getIntExtra(Events.NAME,0);
        ename = GetAlImages.name[id];
        
        Log.d("After ename", ename);

        new NetCheck().execute();


    }

    private class NetCheck extends AsyncTask {
        private Boolean val = false;

        @Override
        protected Object doInBackground(Object[] params) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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
                ProceedData task = new ProceedData();

                task.execute();
            } else {
                AlertDialog dlgAlert = new AlertDialog.Builder(Specification.this).create();
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

    }

    private class ProceedData extends AsyncTask<String,String,String>{

        JSONObject json;
        String evntName;
        public AsyncResponse delegate = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            json = new JSONObject();
            evntName=ename;
            Log.d("After EventName",evntName);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                eveName=null;
                eveDesc=null;
                eveCat=null;
                json.put("ename", evntName);

                String output = json.toString();
                Log.d("After passData", output);
                URL url = new URL(URL_IP+"/EventSpecification.php?login=" + output);
                URLConnection conn = url.openConnection();
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line;
                while ((line = rd.readLine()) != null) {
                    JSONObject obj = new JSONObject(line);
                    eveName = obj.get("name").toString();
                    eveDesc = obj.get("desc").toString();
                    eveCat = obj.get("cat").toString();
                }
                putData(eveName,eveDesc,eveCat);
            } catch (Exception e) {
                Log.d("After Inside Exception", e.toString());
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String res)
        {
            super.onPostExecute(res);
        }
    }

    private void putData(String eveName, String eveDesc, String eveCat) {
        txtEventName.setText(eveName);
        descEvent.setText(eveDesc+" "+eveCat);
    }

}
