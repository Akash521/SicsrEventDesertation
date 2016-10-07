package com.example.vinit.sicsreventdesertation;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.usage.UsageEvents;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Belal on 7/22/2015.
 */
public class CustomList extends ArrayAdapter<String> {

    private String[] name;
    private Bitmap[] bitmaps;
    private Activity context;

    String ename;

    public String[] getName() {
        return name;
    }

    public void setName(String[] name) {
        this.name = name;
    }
    public CustomList(Activity context, String[] name, Bitmap[] bitmaps) {
        super(context, R.layout.image_list_view, name);
        this.context = context;
        this.name= name;
        this.bitmaps= bitmaps;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.image_list_view, null, true);
        final TextView textViewURL = (TextView) listViewItem.findViewById(R.id.textViewURL);
        ImageView image = (ImageView) listViewItem.findViewById(R.id.imageDownloaded);

        textViewURL.setText(name[position]);
        image.setImageBitmap(bitmaps[position]);

        ename=textViewURL.getText().toString();
        return  listViewItem;
    }


}