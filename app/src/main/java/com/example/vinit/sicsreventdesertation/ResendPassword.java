package com.example.vinit.sicsreventdesertation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;

public class ResendPassword extends AppCompatActivity {

    RadioButton rbEmail;
    Button btnContinue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resendpassword);
        setTitle("Change Password");

        rbEmail=(RadioButton)findViewById(R.id.rbResendPassword_EmailId);

        Intent i=getIntent();
        final String emailid=i.getStringExtra("email");

        rbEmail.setText(emailid.toString());


        btnContinue=(Button)findViewById(R.id.btnResendPassword_Continue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rbEmail.isChecked())
                    new NetCheck().execute();
                else
                    Toast.makeText(getApplicationContext(),"Please select email to confirm",Toast.LENGTH_SHORT).show();

            }
        });
    }

    private class NetCheck extends AsyncTask{
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
                task.execute();
            }
            else
            {
                AlertDialog dlgAlert  = new AlertDialog.Builder(ResendPassword.this).create();
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

    private class ProceedData
    {
        public void execute() {
            Intent i=new Intent(ResendPassword.this,CheckMail.class);
            i.setFlags(i.FLAG_ACTIVITY_NEW_TASK | i.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("email",rbEmail.getText().toString());
            startActivity(i);
        }
    }
}
