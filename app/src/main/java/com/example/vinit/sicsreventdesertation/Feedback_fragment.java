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
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class Feedback_fragment extends Fragment {

    Login login=new Login();
    String URL_IP=login.getURL_IP();

    TextView txtFeedback_Mail;
    EditText etMessage;
    Button btnSubmit;
    UserSessionManager session;
    String emailid,result=null;

    public Feedback_fragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feedback_fragment, container, false);

        etMessage=(EditText)rootView.findViewById(R.id.etFeedback_Info);
        txtFeedback_Mail=(TextView)rootView.findViewById(R.id.txtFeedback_MailId);
        String text = "Or mail us at <font color=#0000ff>eventTracker@sicsr.ac.in</font>";
        txtFeedback_Mail.setText(Html.fromHtml(text));

        session = new UserSessionManager(getActivity().getApplicationContext());


        if(session.checkLogin())
            getActivity().finish();

        HashMap<String, String> user = session.getUserDetails();
        emailid = user.get(UserSessionManager.KEY_EMAIL);


        btnSubmit=(Button)rootView.findViewById(R.id.btnFeedback_Submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NetCheck().execute();
            }
        });
        return rootView;
    }

    private class NetCheck extends AsyncTask implements AsyncResponse {
        private Boolean val=false;

        @Override
        protected Object doInBackground(Object[] params) {
            ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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
                task.delegate=this;
                task.execute();
            }
            else
            {
                AlertDialog dlgAlert  = new AlertDialog.Builder(getActivity()).create();
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
        public void processFinish(String output) {

            Toast.makeText(getActivity(), output, Toast.LENGTH_SHORT).show();
        }
    }

    private class ProceedData extends AsyncTask<String,String,String>{

        JSONObject json;
        String message;
        public AsyncResponse delegate = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            json = new JSONObject();
            message = etMessage.getText().toString().replaceAll(" ","***");

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                result=null;
                json.put("emailid", emailid);
                json.put("message", message);
                String output = json.toString();
                URL url = new URL(URL_IP+"/EventFeedback.php?feedback="+output);

                URLConnection conn =url.openConnection();
//                conn.setRequestMethod("POST");
//                conn.setDoInput(true);
//                conn.setDoOutput(true);

                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line;
                while ((line = rd.readLine()) != null) {
                    Log.d("After line",line);
                    JSONObject obj = new JSONObject(line);
                    result = obj.get("res").toString();
                    Log.d("After While", result);
                }
            } catch (Exception e) {
                Log.d("After Inside Exception", e.toString());
                e.printStackTrace();
            }
            return result;
        }


        @Override
        protected void onPostExecute(String res) {
            delegate.processFinish(res);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity=getActivity();
        ((HomeNavigation)activity).onSectionAttached(6);
    }
}

