package com.example.test.hackathonday1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

/**
 * Created by pragar on 7/30/2014.
 */
public class InvitesAdapter extends ArrayAdapter<SingleListItem> {

    private List<SingleListItem> objects;

    public InvitesAdapter(Context context, List<SingleListItem> objects) {
        super(context, R.layout.layout_for_invites, objects);
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if ( convertView == null )
        {
            LayoutInflater li = LayoutInflater.from(getContext());
            convertView = li.inflate(R.layout.layout_for_invites, null);

        }


        InviteHolder mholder = new InviteHolder();
        SingleListItem item = objects.get(position);


        mholder.acceptButton = (Button) convertView.findViewById(R.id.accept_button);
        mholder.acceptButton.setClickable(true);

        mholder.declineButton = (Button) convertView.findViewById(R.id.decline_button);
        mholder.declineButton.setClickable(true);

        mholder.title = (TextView)convertView.findViewById(R.id.invitee);
        mholder.time = (TextView)convertView.findViewById(R.id.invite_time);


        mholder.acceptButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "An invitation has been accepted", Toast.LENGTH_LONG).show();
            }
        });

        mholder.declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "An invitation has been declined", Toast.LENGTH_LONG).show();
            }
        });
        mholder.title.setText(item.Title);
        mholder.time.setText(item.getTime());

        convertView.setTag(mholder);



        return convertView;
    }

    public static class InviteHolder {
        SingleListItem singleItem;
        TextView title;
        TextView time;
        Button acceptButton;
        Button declineButton;
    }
}