package com.example.himanshudhawale.hackathon;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class MessageAdapter extends ArrayAdapter<Messages>{
    List<Messages> messagesList;

    //FirebaseDatabase database;
    String userId;
    String eventID;

    DatabaseReference myref;
    public MessageAdapter(@NonNull Context context, int resource, @NonNull List<Messages> objects, String id, String eventID) {
        super(context, resource, objects);
        this.messagesList=objects;
        this.userId=id;
        this.eventID=eventID;
    }



    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

         final Messages message = getItem(position);


        if(convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chat_item, parent, false);

        }


        TextView textViewMsg = convertView.findViewById(R.id.textViewc1);
        TextView textViewName = convertView.findViewById(R.id.textViewc2);
        TextView textViewTime = convertView.findViewById(R.id.textViewc3);
        ImageView imageDelete = convertView.findViewById(R.id.imageViewdelete);

        ImageView imageToSend = convertView.findViewById(R.id.imageView);

        imageToSend.setVisibility(View.GONE);


        if(!("abc".equals(message.url))) {
            imageToSend.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(message.url)
                    .into(imageToSend);


        }



        imageDelete.setVisibility(View.GONE);
        try
        {

            if(userId.equals(message.userID))
            {
                imageDelete.setVisibility(View.VISIBLE);

                imageDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                           // final Messages message = getItem(position);

                            String s=message.messageID;
                            String url=message.url;


                            delete(s, url);

                            messagesList.remove(position);
                            notifyDataSetChanged();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }



                    }
                });
            }
        }catch (Exception e)
        {
            //Log.d
        }



        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

        Date date = new Date();
        try {
            date = simpleDateFormat.parse(messagesList.get(position).timeStamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.HOUR, 0);
        Date updatedDate = gc.getTime();

        String prettyTime = new PrettyTime().format(updatedDate);

        textViewTime.setText(prettyTime);


        textViewMsg.setText(message.message);
        textViewName.setText(message.userName);


            return convertView;

    }



    public void delete(final String id, final String url)
    {
      myref = FirebaseDatabase.getInstance().getReference("events").child(eventID).child("chats").child(userId);
     //   Query applesQuery = my.child("place").orderByChild("nomPlace").equalTo(p.getNomPlace());
//
        myref.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

              if(!("abc".equals(url)))
              {

                  StorageReference mreference=FirebaseStorage.getInstance().getReferenceFromUrl(url);
                  mreference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                      @Override
                      public void onSuccess(Void aVoid) {
                          Toast.makeText(getContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                      }
                  }).addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                          Log.d("demo","Failed");
                      }
                  }).addOnCompleteListener(new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull Task<Void> task) {
                          dataSnapshot.child(id).getRef().removeValue();

                      }
                  });
              }
              else {
                  dataSnapshot.child(id).getRef().removeValue();
              }



          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
      });

    }


}
