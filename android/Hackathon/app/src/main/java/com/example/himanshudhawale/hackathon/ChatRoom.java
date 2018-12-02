package com.example.himanshudhawale.hackathon;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class ChatRoom extends AppCompatActivity {


    TextView titleName;
    EditText messageToSend;
    ListView listViewChats;
    User userValue;
    ImageView imageViewhome, imageViewsend, imageAdd;

    FirebaseAuth mAuth;
    String token, user_id, id, title;
    ArrayAdapter<Messages> adapter;
    ArrayList<Messages> messageList = new ArrayList<>();
    String getUrl;


    DatabaseReference myRef, myRef2;
    FirebaseUser currentUser;
    FirebaseDatabase database;
    StorageReference storageRef;
    Bitmap bitmapToSend = null;

    boolean flag = false, newFlag = false;
    int image = 0;
    private static final int REQUEST_IMAGE_SELECTOR = 1;

     String eventID=null;
     String eventName=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);


        titleName = findViewById(R.id.textViewtitleId);
        messageToSend = findViewById(R.id.editTextMessageId);
        listViewChats = findViewById(R.id.listViewIdchat);
        imageViewhome = findViewById(R.id.imageViewHome);
        imageViewsend = findViewById(R.id.imageViewSend);
        imageAdd = findViewById(R.id.imageAdd);




        currentUser = FirebaseAuth.getInstance().getCurrentUser();



        eventID=getIntent().getExtras().getString(MyAdapter.EVENTKEY);
        eventName=getIntent().getExtras().getString(MyAdapter.EVENTNAME);

        this.setTitle(eventName);



        //        Log.d("demoMy", eventID);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users").child(currentUser.getUid());

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userValue = dataSnapshot.getValue(User.class);
                Log.d("demoM", "Value is: " + userValue);

                String name = userValue.first + " " + userValue.last;
                titleName.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("demo", "Failed to read value.", error.toException());
            }


        });

        myRef2 = database.getReference().child("events").child(eventID).child("chats");




        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Messages> getMessageList = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    {
                        Messages message = child.getValue(Messages.class);
                        getMessageList.add(message);
                    }
                    messageList = getMessageList;

                    adapter = new MessageAdapter(ChatRoom.this, R.layout.chat_item, messageList, currentUser.getUid(),eventID);
                    listViewChats.setAdapter(adapter);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //adapter.notifyDataSetChanged();


        imageViewsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageAdd.setImageResource(R.drawable.addimage);


                Messages message = new Messages();

                if (bitmapToSend != null) {


                    uploadImage(bitmapToSend);


                } else {

                    message.url = "abc";
                    String mess = messageToSend.getText().toString();
                    message.message = mess;
                    Date date = new Date();
                    message.timeStamp = String.valueOf(date);
                    message.userID = currentUser.getUid();
                    message.userName = userValue.first;

                    String key = myRef2.push().getKey();
                    message.messageID = key;


                    //Make The bitmap null again
                    bitmapToSend = null;

                    myRef2.child(key).setValue(message);

                    messageList.add(message);

                }





                //adapter.add(message);
                //adapter.notifyDataSetChanged();


                //String s=myRef2.push().setValue(message).getKey;


            }
        });

        if (newFlag) {
            newFlag = false;
            flag = false;
        }


        imageAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                image = 1;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_SELECTOR);


            }

        });


        imageViewhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatRoom.this, "Successfully Logged out", Toast.LENGTH_SHORT).show();

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ChatRoom.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });


    }


    public void getMessages() {
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Messages> getMessageList = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    {
                        Messages message = child.getValue(Messages.class);
                        getMessageList.add(message);
                    }
                    messageList = getMessageList;
                    adapter = new MessageAdapter(ChatRoom.this, R.layout.chat_item, messageList, currentUser.getUid(),eventID);
                    listViewChats.setAdapter(adapter);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void uploadImage(Bitmap bitmap) {
        final StorageReference ref = FirebaseStorage.getInstance().getReference().child(UUID.randomUUID() + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();

        final UploadTask uploadTask = ref.putBytes(data);



/*
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...

            }
        });
        */


        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    getUrl = downloadUri.toString();

                    Messages message=new Messages();
                    message.url=downloadUri.toString();
                    String mess = messageToSend.getText().toString();
                    message.message = mess;
                    Date date = new Date();
                    message.timeStamp = String.valueOf(date);
                    message.userID = currentUser.getUid();
                    message.userName = userValue.first;

                    String key = myRef2.push().getKey();
                    message.messageID = key;


                    //Make The bitmap null again
                    bitmapToSend = null;

                    myRef2.child(key).setValue(message);

                    messageList.add(message);
                } else {
                    // Handle failures
                    // ...
                }
            }
        });


        // return getUrl;

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_SELECTOR && resultCode == RESULT_OK && data != null && data.getData() != null && image == 1) {
            image = 0;
            Uri uri = data.getData();
            try {
                bitmapToSend = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                flag = true;
                //Drawable d = new BitmapDrawable(getResources(), bitmap);
                imageAdd.setImageBitmap(bitmapToSend);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




}