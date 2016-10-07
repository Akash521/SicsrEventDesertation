package com.example.vinit.sicsreventdesertation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;


public class Login extends AppCompatActivity {

    public String URL_IP="http://192.168.1.9:8088";
    private AutoCompleteTextView etLoginEmail;
    EditText etLoginPassword;
    Button btnLogin_Login;
    TextView txtNewUser,txtForgotPassword;
    String usrname=null;

    // User Session Manager Class
    UserSessionManager session;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");

        session = new UserSessionManager(getApplicationContext());

        etLoginEmail=(AutoCompleteTextView)findViewById(R.id.etLoginEmail);
        etLoginPassword = (EditText) findViewById(R.id.etLoginPassword);

        txtForgotPassword=(TextView)findViewById(R.id.txtLoginForgotPassword);
        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(Login.this,FindAccount.class);
                i.setFlags(i.FLAG_ACTIVITY_NEW_TASK | i.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        txtNewUser=(TextView)findViewById(R.id.txtLogin_NewUser);
        txtNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(Login.this,Signup.class);
                i.setFlags(i.FLAG_ACTIVITY_NEW_TASK | i.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });

        btnLogin_Login = (Button) findViewById(R.id.btnLogin_Login);
        btnLogin_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    attemptLogin();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void attemptLogin() throws ExecutionException, InterruptedException {
        etLoginEmail.setError(null);
        etLoginPassword.setError(null);

        String email = etLoginEmail.getText().toString();
        String password = etLoginPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            etLoginPassword.setError("Password is too short");
            focusView = etLoginPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            etLoginEmail.setError("Please enter email address");
            focusView = etLoginEmail;
            cancel = true;
        }else if (!isEmailValid(email)) {
            etLoginEmail.setError(getString(R.string.error_invalid_email));
            focusView = etLoginEmail;
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

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private class NetCheck extends AsyncTask implements AsyncResponse {
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
                task.delegate=this;
                task.execute();
            }
            else
            {
                AlertDialog dlgAlert  = new AlertDialog.Builder(Login.this).create();
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
            Log.d("After Process Finish",output);
            if(output == "null")
                Toast.makeText(getApplicationContext(), "Email Id not registered. Please signup", Toast.LENGTH_SHORT).show();
            else {
                session.createUserLoginSession(output, etLoginEmail.getText().toString());
                Intent i=new Intent(Login.this,HomeNavigation.class);
                i.setFlags(i.FLAG_ACTIVITY_NEW_TASK | i.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
                //Toast.makeText(getApplicationContext(), "Welcome " + output.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class ProceedData extends AsyncTask<String,String,String>{

        JSONObject json;
        String email, pwd;
        public AsyncResponse delegate = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            json = new JSONObject();
            email = etLoginEmail.getText().toString();
            pwd = etLoginPassword.getText().toString();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                usrname=null;
                json.put("email", email);
                json.put("pwd", pwd);
                String output = json.toString();
                URL url = new URL(URL_IP+"/EventLogin.php?login=" + output);
                URLConnection conn = url.openConnection();
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line;
                while ((line = rd.readLine()) != null) {
                    JSONObject obj = new JSONObject(line);
                    usrname = obj.get("name").toString();
                    Log.d("After While", usrname);
                }
            } catch (Exception e) {
                Log.d("After Inside Exception", e.toString());
                e.printStackTrace();
            }
            return usrname;
        }


        @Override
        protected void onPostExecute(String res) {
            delegate.processFinish(res);
        }
    }

    public String getURL_IP() {
        Log.d("After IP",URL_IP);
        return URL_IP;
    }
}



