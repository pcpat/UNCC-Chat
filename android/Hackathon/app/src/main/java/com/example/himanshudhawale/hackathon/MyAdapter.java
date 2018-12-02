package com.example.himanshudhawale.hackathon;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Browser;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<Events> {
    Context mContext;
    int mResource;
    ArrayList<Events> list;
    FirebaseAuth mAuth;
    FirebaseUser current;
    FirebaseDatabase mDatabase;

    public static String EVENTKEY="Eventkey";
    public static String EVENTNAME="Eventname";


    public MyAdapter(Context context, int resource, ArrayList<Events> list) {

        super(context, resource, list);

        this.mContext=context;
        this.list=list;
        this.mResource = resource;
    }




    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.eventName=convertView.findViewById(R.id.textViewe1);
            viewHolder.descriptionEvent=convertView.findViewById(R.id.textViewe3);
            viewHolder.button=convertView.findViewById(R.id.buttone);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        final Events events= list.get(position);
        viewHolder.eventName.setText(events.name);
        viewHolder.descriptionEvent.setText(events.descritpionl);


        mAuth=FirebaseAuth.getInstance();
        mDatabase=FirebaseDatabase.getInstance();
        current= FirebaseAuth.getInstance().getCurrentUser();



        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatRoom.class);
                intent.putExtra(EVENTKEY, events.eventID);
                intent.putExtra(EVENTNAME, events.name);
                getContext().startActivity(intent);
            }
        });

        viewHolder.eventName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(events.link));

                getContext().startActivity(i);
            }
        });






        return convertView;
    }





    class ViewHolder {
        TextView eventName;
        TextView descriptionEvent;
        Button button;
    }
}
