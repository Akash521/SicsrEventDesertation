package com.example.vinit.sicsreventdesertation;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.EachExceptionsHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Add_Event extends Fragment {

    Login login=new Login();
    String URL_IP=login.getURL_IP();


    //Image Upload
    ImageView ivGallery;
    GalleryPhoto galleryPhoto;
    final int GALLERY_REQUEST=22131;
    private final String TAG=this.getClass().getName();
    String selectedPhoto;

    //DatePicker and TimePicker
    static final int DIALOG_ID1 = 1;
    int hour_x;
    int minute_x;
    int year_x,month_x,day_x;
    static final int DIALOG_ID = 0;

    public EditText eventName,eventDescription,eventDate,eventTime,eventVenue;
    Button btnSave;
    RadioButton rbFree,rbPaid;
    String category,reg_fee;
    EditText eventFee;


    public Add_Event() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView= inflater.inflate(R.layout.fragment_add__event, container, false);

        eventName=(EditText)rootView.findViewById(R.id.etAddEvent_Name);
        eventDescription=(EditText)rootView.findViewById(R.id.etAddEvent_Description);
        eventDate=(EditText)rootView.findViewById(R.id.etAddEvent_Date);
        eventTime=(EditText)rootView.findViewById(R.id.etAddEvent_Time);
        eventVenue=(EditText)rootView.findViewById(R.id.etAddEvent_Venue);
        eventFee=(EditText)rootView.findViewById(R.id.etAddEvent_Paid);

        btnSave=(Button)rootView.findViewById(R.id.btnAddEvent);


        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        //showDialogOnButtonClick();

        final Spinner staticSpinner = (Spinner)rootView.findViewById(R.id.static_spinner);
        List<String> list = new ArrayList<>();
        list.add("Cultural");
        list.add("Tech");
        list.add("Guest Lecture");
        list.add("Sports");
        list.add("Social Service");
        list.add("Miscellaneous");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        staticSpinner.setAdapter(dataAdapter);

        staticSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = staticSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rbFree=(RadioButton)rootView.findViewById(R.id.rbAddEvent_Free);
        rbPaid=(RadioButton)rootView.findViewById(R.id.rbAddEvent_Paid);
        rbFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reg_fee = "Free";
            }
        });

        rbPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reg_fee = eventFee.getText().toString();
            }
        });

        galleryPhoto=new GalleryPhoto(getActivity().getApplicationContext());
        ivGallery=(ImageView)rootView.findViewById(R.id.imgAddEvent);
        ivGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NetCheck().execute();
                //Toast.makeText(getActivity().getApplicationContext(), eventDate.getText() + " " + eventTime.getText() + " " + eventVenue.getText() + " " + category + " " + eventDescription.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    private class NetCheck extends AsyncTask implements AsyncCheckMail {
        private Boolean val=false;

        @Override
        protected Object doInBackground(Object[] params) {
            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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
                Data task=new Data();


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
        public void ckMail(String res) {
            Log.d("After ckmail",res);
            if(res=="null") {
                Toast.makeText(getActivity().getApplicationContext(), res, Toast.LENGTH_SHORT).show();
//                Register task=new Register();
//                task.op= this;
                //               task.execute();
            }
            else {
                Toast.makeText(getActivity().getApplicationContext(), "Email already exists..Please Login", Toast.LENGTH_SHORT).show();
            }
        }
    }

        @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==Activity.RESULT_OK)
        {
            if(requestCode==GALLERY_REQUEST)
            {
                Uri uri=data.getData();
                galleryPhoto.setPhotoUri(uri);
                String photoPath=galleryPhoto.getPath();
                selectedPhoto=photoPath;
                try {
                    Bitmap bitmap= ImageLoader.init().from(photoPath).requestSize(512,512).getBitmap();
                    ivGallery.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getActivity().getApplicationContext(),"Something went wrong while choosing photots",Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private TimePickerDialog.OnTimeSetListener tpickerListner=new TimePickerDialog.OnTimeSetListener()
    {
        @Override
        public void onTimeSet(TimePicker view,int hour,int min)
        {
            hour_x=hour;
            minute_x=min;
            eventTime.setText(hour_x+":"+minute_x);
        }
    };
    private DatePickerDialog.OnDateSetListener dpickerListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            month_x = monthOfYear + 1;
            day_x = dayOfMonth;

            eventDate.setText(year_x + "/" + month_x +"/" +day_x);
        }
    };


    private class Data
    {
        public Data() {
            HashMap<String,String> postData = new HashMap<String, String>();
            Bitmap bitmap = null;
            try {
                bitmap = ImageLoader.init().from(selectedPhoto).requestSize(1024, 1024).getBitmap();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String encodedImage = ImageBase64.encode(bitmap);
            Log.d(TAG, encodedImage);

            postData.put("image", encodedImage);
            postData.put("name",eventName.getText().toString());
            postData.put("desc",eventDescription.getText().toString());
            postData.put("date",eventDate.getText().toString());
            postData.put("time",eventTime.getText().toString());
            postData.put("category",category);
            postData.put("registration",reg_fee);
            postData.put("venue",eventVenue.getText().toString());

            PostResponseAsyncTask task = new PostResponseAsyncTask(getActivity(), postData, new AsyncResponse() {
                @Override
                public void processFinish(String s) {
//                            if (s.contains("upload_success")) {
//                                Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
//                            } else {
                    Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_SHORT).show();
//                            }

                }
            });
            task.execute("http://192.168.1.8:8088/EventAdd.php");
            task.setEachExceptionsHandler(new EachExceptionsHandler() {
                @Override
                public void handleIOException(IOException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Cannot connect to server", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void handleMalformedURLException(MalformedURLException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "URL Error", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void handleProtocolException(ProtocolException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Protocol Error", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void handleUnsupportedEncodingException(UnsupportedEncodingException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Encoding error", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
