package com.example.stage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    List<String> data;
    SwipeFlingAdapterView flingAdapterView;
    RequestQueue requestQueue;
    FloatingActionButton open, liked, logout;
    Boolean clicked = false;
    private Animation rotateOpen, rotateClose, fromButtom, toButtom;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flingAdapterView = findViewById(R.id.swipe);
        requestQueue = Volley.newRequestQueue(this);
        SessionManagment sessionManagment = new SessionManagment(MainActivity.this);


        rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim);
        fromButtom = AnimationUtils.loadAnimation(this, R.anim.from_buttom_anim);
        toButtom = AnimationUtils.loadAnimation(this, R.anim.to_buttom_anim);
        open = findViewById(R.id.open);
        liked = findViewById(R.id.likedbtn);
        logout = findViewById(R.id.logoutbtn);

        open.setOnClickListener(v ->
            onOpenCliked()
        );
        liked.setOnClickListener(v -> {
            Toast.makeText(this, "like pressed", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this,LikedActivity.class));
        });
        logout.setOnClickListener(v -> {
            Toast.makeText(this, "loged out", Toast.LENGTH_SHORT).show();
            logOut(sessionManagment);
        });


        data = new ArrayList<>();
        data.add("welcome");


        getData();

        arrayAdapter = new ArrayAdapter<>(MainActivity.this, R.layout.item, R.id.data, data);
        flingAdapterView.setAdapter(arrayAdapter);
        flingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                data.remove(0);

                Log.i("data", String.valueOf(data.size()));

//                if (data.size() < 2) {
//                    getData();
//
//                }
                arrayAdapter.notifyDataSetChanged();


            }

            @Override
            public void onLeftCardExit(Object o) {
                Toast.makeText(MainActivity.this, "dislike", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onRightCardExit(Object o) {
                Toast.makeText(MainActivity.this, "like", Toast.LENGTH_SHORT).show();
                liked(sessionManagment);

            }

            @Override
            public void onAdapterAboutToEmpty(int i) {
                if (i < 5) {
                    getData();

                }
                arrayAdapter.notifyDataSetChanged();

                //kjjjnjnjn
            }

            @Override
            public void onScroll(float v) {
                // comment explaining why the method is empty

            }
        });


    }

    private void liked(SessionManagment sessionManagment) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+getString(R.string.ip)+":8080/Likes/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RestService restService = retrofit.create(RestService.class);
        restService.addFact(new Fact(data.get(0), sessionManagment.getSession())).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.i("response", response.toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("error", t.getMessage());
            }
        });


    }

    private void onOpenCliked() {
        setVisibility(clicked);
        setAnimation(clicked);
        clicked = !clicked;
    }

    private void setAnimation(Boolean clicked) {
        if (clicked) {
            liked.startAnimation(fromButtom);
            logout.startAnimation(fromButtom);
            open.startAnimation(rotateOpen);
        } else {
            liked.startAnimation(toButtom);
            logout.startAnimation(toButtom);
            open.startAnimation(rotateClose);
        }
    }

    private void setVisibility(Boolean clicked) {
        if (!clicked) {
            liked.setVisibility(View.VISIBLE);
            logout.setVisibility(View.VISIBLE);
        } else {
            liked.setVisibility(View.INVISIBLE);
            logout.setVisibility(View.INVISIBLE);
        }

    }

    public void logOut(SessionManagment sessionManagment) {


        sessionManagment.removeSession();
        moveToLogin();

    }

    private void moveToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private String getData() {
        String url = "https://api.api-ninjas.com/v1/facts?limit=30";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, this::formatData, Throwable::printStackTrace) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap header = new HashMap<>();
                header.put("content-type", "application/json");
                header.put("X-Api-Key", "NztfSzejzJ/lCndyIqZIzw==g4tJmAaby3d3E5eR");
                return header;
            }
        };


        requestQueue.add(stringRequest);
        return "";
    }

    private void formatData(String response) {
        String temp = "";
        for (int i = 0; i < response.length(); i++) {
            if (response.charAt(i) == '{') {
                temp = "";

            } else if (response.charAt(i) == '}') {
                Log.i("temp", temp);
                data.add(temp);
            } else {
                temp = temp + response.charAt(i);
            }
        }


    }
}