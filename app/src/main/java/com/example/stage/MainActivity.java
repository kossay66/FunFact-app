package com.example.stage;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    List<String> data;
    SwipeFlingAdapterView flingAdapterView;
    RequestQueue requestQueue;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flingAdapterView = findViewById(R.id.swipe);
        requestQueue = Volley.newRequestQueue(this);

        data = new ArrayList<>();
        data.add("ndjkndk");
        data.add("ndjkndk");
        data.add("ndjkndk");
        data.add("ndjkndk");
        data.add("ndjkndk");
        getData();



        arrayAdapter = new ArrayAdapter<>(MainActivity.this, R.layout.item, R.id.data, data);
        flingAdapterView.setAdapter(arrayAdapter);
        flingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                data.remove(0);

                Log.i("data", String.valueOf(data.size()));
                if (data.size() < 2) {
                    getData();
                    
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object o) {
                Toast.makeText(MainActivity.this, "dislike", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onRightCardExit(Object o) {
                Toast.makeText(MainActivity.this, "like", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAdapterAboutToEmpty(int i) {

            }

            @Override
            public void onScroll(float v) {

            }
        });
        Button like, dislike;
        like = findViewById(R.id.like);
        dislike = findViewById(R.id.dislike);
        like.setOnClickListener(v -> {
            flingAdapterView.getTopCardListener().selectRight();
        });
        dislike.setOnClickListener(v -> {
            flingAdapterView.getTopCardListener().selectLeft();
        });
    }


    private void getData() {
        String url = "https://api.api-ninjas.com/v1/facts?limit=30";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                formatData(response);
                data.add(response);
                //Log.i("api", response);
                response = response.replaceAll("\\[\\{\"fact\":", "");

                response = response.replaceAll("\\}, \\{\"fact\": ", "");
                response = response.replaceAll("\\}\\]", "");

                //Log.i("api", response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap header = new HashMap<>();
                header.put("content-type", "application/json");
                header.put("X-Api-Key", "NztfSzejzJ/lCndyIqZIzw==g4tJmAaby3d3E5eR");
                return header;
            }
        };


        requestQueue.add(stringRequest);
    }

    private void formatData(String response) {
        Boolean start=false;
        String temp="";
        for (int i = 0; i < response.length(); i++) {
            if (response.charAt(i) == '{'){
                temp="";
                start=true;

            } else if (response.charAt(i) == '}') {
                Log.i("temp",temp);
                data.add(temp);
                start=false;
            }else {
                temp=temp+response.charAt(i);
            }
            Log.i("temp",temp);
        }

        /*
        StringBuilder sb = new StringBuilder(response);
        sb.deleteCharAt(0);
        //sb.deleteCharAt(-1);
        String newStr = sb.toString();
        String temp =newStr.substring(sb.indexOf("{\"fact\": "), sb.indexOf("}, "));
        Log.i("string",temp);
        Log.i("string",newStr);*/

    }
}