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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

interface AsyncCheckMail{
    void ckMail(String res);
}

public class Signup extends AppCompatActivity{

    Login login=new Login();
    String URL_IP=login.getURL_IP();

        }

        @Override
        protected void onPostExecute(String res) {
            try {
                registration(res);
            }catch (Exception e){
                Log.d("After Exception", e.toString());
            }
        }
    }




    public void registration(String res) {
        //Log.d("After Inside regis",res);
        Toast.makeText(getApplicationContext(),res,Toast.LENGTH_SHORT).show();
        etFullname.setText("");
        etEmail.setText("");
        etContact.setText("");
        etPassword.setText("");
        Intent i =new Intent(Signup.this,Login.class);
        i.setFlags(i.FLAG_ACTIVITY_NEW_TASK | i.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    private boolean isEmailValid(String email)
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isContactValid(String contact) {
        return android.util.Patterns.PHONE.matcher(contact).matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

}
