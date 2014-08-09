package com.example.test.hackathonday1;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

/**
 * Created by pragar on 7/30/2014.
 */
public class SingleListItemAdapter extends ArrayAdapter<SingleListItem> {

    private List<SingleListItem> objects;

    public SingleListItemAdapter(Context context, List<SingleListItem> objects) {
        super(context, android.R.layout.simple_list_item_2, objects);
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if ( convertView == null )
        {
            LayoutInflater li = LayoutInflater.from(getContext());
            convertView = li.inflate(android.R.layout.simple_list_item_2, null);

        }

        TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);
        TextView text2 = (TextView) convertView.findViewById(android.R.id.text2);

        text1.setText(objects.get(position).Title);
        text2.setText(objects.get(position).getTime());

        return convertView;
    }
}