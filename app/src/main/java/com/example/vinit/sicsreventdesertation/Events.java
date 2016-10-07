package com.example.vinit.sicsreventdesertation;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class Events extends Fragment implements AdapterView.OnItemClickListener {

    String nme;
    Login login=new Login();
    String URL_IP=login.getURL_IP();
    private ListView listView;
    public String GET_IMAGE_URL;
    TextView txtName;

    public GetAlImages getAlImages;

    public static final String NAME = "NAME";

    public Events() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_events, container, false);

        txtName=(TextView)rootView.findViewById(R.id.textViewURL);
        nme=getArguments().getString("img");
//        nme=getActivity().getIntent().getExtras().getString("img");
//        Log.d("After TXTNAME",txtName.getText().toString());
        GET_IMAGE_URL=URL_IP+"/getAllImage.php?feature_event="+nme;
        Log.d("After URL",GET_IMAGE_URL);

        listView = (ListView)rootView.findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        getURLs();
        return rootView;
    }

    private void getImages(){
        class GetImages extends AsyncTask<Void,Void,Void> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(),"Downloading images...","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(Void v) {
                super.onPostExecute(v);
                loading.dismiss();
                //Toast.makeText(ImageListView.this,"Success",Toast.LENGTH_LONG).show();
                CustomList customList = new CustomList(getActivity(),GetAlImages.name,GetAlImages.bitmaps);
                listView.setAdapter(customList);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    getAlImages.getAllImages();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
        GetImages getImages = new GetImages();
        getImages.execute();
    }

    private void getURLs() {
        class GetURLs extends AsyncTask<String,Void,String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(),"Loading...","Please Wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Log.d("After Post execute", s);
                getAlImages = new GetAlImages(s);
                getImages();
            }

            @Override
            protected String doInBackground(String... strings) {
                BufferedReader bufferedReader = null;
                try {
                    Log.d("After do in back",strings[0]);
                    URL url = new URL(strings[0]);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }
                    Log.d("After while", sb.toString());

                    return sb.toString().trim();

                }catch(Exception e){
                    Log.d("After Exception","Exception");
                    e.printStackTrace();
                    return null;
                }
            }
        }
        GetURLs gu = new GetURLs();
        gu.execute(GET_IMAGE_URL);
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity=getActivity();
        ((HomeNavigation)activity).onSectionAttached(8);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

        Log.d("After passData", NAME+" "+i);

        Intent intent = new Intent(getActivity(), Specification.class);
        intent.putExtra(NAME,i);
        startActivity(intent);
    }


}
