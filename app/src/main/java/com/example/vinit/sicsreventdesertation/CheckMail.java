package com.example.vinit.sicsreventdesertation;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CheckMail extends AppCompatActivity {

    TextView txtEmailMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_mail);
        setTitle("Change Password");

        Intent i=getIntent();
        String email=i.getStringExtra("email");

        txtEmailMsg=(TextView)findViewById(R.id.txtCheckMail_MailSent);
        txtEmailMsg.setText("We've sent an email to "+email +" Please check your mail.");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    Intent i = new Intent(CheckMail.this, Login.class);
                    i.setFlags(i.FLAG_ACTIVITY_NEW_TASK | i.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
            }
        }, 5000);
    }
}
