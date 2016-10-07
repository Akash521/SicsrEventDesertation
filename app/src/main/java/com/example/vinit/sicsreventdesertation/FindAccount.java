package com.example.vinit.sicsreventdesertation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

/**
 * Created by Vinit on 22-Jul-16.
 */
public class FindAccount extends AppCompatActivity {

    Login login=new Login();
    String URL_IP=login.getURL_IP();

    AutoCompleteTextView etFindEmail;
    Button btnSearch;
    String emailid=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findaccount);
        setTitle("Change Password");

        etFindEmail=(AutoCompleteTextView)findViewById(R.id.etSearchEmail);

        btnSearch=(Button)findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    attemptSearch();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void attemptSearch() throws ExecutionException, InterruptedException {
        etFindEmail.setError(null);

        String email = etFindEmail.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(email)) {
            etFindEmail.setError("Please enter email address");
            focusView = etFindEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            etFindEmail.setError(getString(R.string.error_invalid_email));
            focusView = etFindEmail;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            new NetCheck().execute();
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private class NetCheck extends AsyncTask implements AsyncCheckMail {
        private Boolean val=false;

        @Override
        protected Object doInBackground(Object[] params) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected())
            {
                try
                {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200)
                    {
                        val=true;
                    }
                } catch (Exception e1)
                {
                    e1.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(val)
            {
                ProceedData task=new ProceedData();
                task.op=this;
                task.execute();
            }
            else
            {
                AlertDialog dlgAlert  = new AlertDialog.Builder(FindAccount.this).create();
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

        @Override
        public void ckMail(String res) {
            //Log.d("After ckmail",res);
            if(res=="null") {
                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }
            else {
                Intent i=new Intent(FindAccount.this,ResendPassword.class);
                Log.d("After Intent",res);
                i.setFlags(i.FLAG_ACTIVITY_NEW_TASK | i.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("email",res);
                startActivity(i);
                //Toast.makeText(getApplicationContext(), "Email already exists..Please Login", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class ProceedData extends AsyncTask<String,String,String>{

        JSONObject json;
        String emailSearch;
        public AsyncCheckMail op = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            json = new JSONObject();
            emailSearch = etFindEmail.getText().toString();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                emailid = null;
                json.put("email", emailSearch);
                String output = json.toString();
                //Log.d("After URL_IP",URL_IP);
                URL url = new URL(URL_IP+"/EventCheckEMail.php?login=" + output);
                URLConnection conn = url.openConnection();
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line;
                while ((line = rd.readLine()) != null) {
                    //Log.d("After Before line",line);
                    if(!(line.trim().toString()=="")){
                        //JSONObject obj=new JSONObject();
                        //  Log.d("After Line",line);
                        JSONObject obj = new JSONObject(line);
                        emailid = obj.get("email").toString();
                        //Log.d("After check mail",checkemail);
                    }
                }
            } catch (Exception e) {
                Log.d("After exce process data", e.toString());
                e.printStackTrace();
            }
            return emailid;
        }

        @Override
        protected void onPostExecute(String res) {
            op.ckMail(res);
        }
    }

}
