package com.example.himanshudhawale.hackathon;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class DashBoard extends AppCompatActivity {


    ListView listView;
    MyAdapter myAdapter;
    ArrayList<Events> listEvents=new ArrayList<>();
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    FirebaseUser mUser;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);


        listView = findViewById(R.id.listViewID);


        if (isConnected()) {
            mAuth=FirebaseAuth.getInstance();
            mUser=mAuth.getCurrentUser();
            mDatabase=FirebaseDatabase.getInstance();
            mDatabase = FirebaseDatabase.getInstance();


            new MyAsyncTask().execute("http://demo4166466.mockable.io/");

        }

    }


    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }


    private class MyAsyncTask extends AsyncTask<String, Void, ArrayList<Events>> {
        @Override
        protected ArrayList<Events> doInBackground(String... strings) {
            StringBuilder stringBuilder = new StringBuilder();

            String result = null;
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }

                    result = stringBuilder.toString();
                    JSONObject root = new JSONObject(result);
                    JSONArray events = root.getJSONArray("events");
                    for (int i = 0; i < events.length(); i++) {
                        JSONObject newJson = events.getJSONObject(i);
                        Events eventOb = new Events();
                        eventOb.name = newJson.getString("name");
                        eventOb.link = newJson.getString("link");
                        eventOb.descritpionl = newJson.getString("description");


                        myRef = mDatabase.getReference().child("events").push();

                        eventOb.eventID=myRef.getKey();

                       myRef.setValue(eventOb.eventID);
                        listEvents.add(eventOb);


                        Log.d("demoNew", eventOb.toString());

                    }


                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return listEvents;
        }



        @Override
        protected void onPostExecute(ArrayList<Events> myObjects) {
            myAdapter = new MyAdapter(DashBoard.this, R.layout.event_item, listEvents);
            listView.setAdapter(myAdapter);
        }
    }
}