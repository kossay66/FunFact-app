package com.example.stage;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LikedActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    recyclerAdapter adapter;
    private ArrayList<Fact> facts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked);
        recyclerView = findViewById(R.id.recyclerview);
        SessionManagment sessionManagment = new SessionManagment(LikedActivity.this);

        facts = new ArrayList<>();
        getData(sessionManagment);
        setAdapter();

    }

    private void setAdapter() {
        adapter = new recyclerAdapter(facts);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void getData(SessionManagment sessionManagment) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+getString(R.string.ip)+":8080/Likes/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RestService restService = retrofit.create(RestService.class);
        restService.getAllByUserID(sessionManagment.getSession()).enqueue(new Callback<ArrayList<Fact>>() {
            @Override
            public void onResponse(Call<ArrayList<Fact>> call, Response<ArrayList<Fact>> response) {
                if (response.isSuccessful()) {
                    // Add the data from the response to the existing 'facts' array
                    ArrayList<Fact> newFacts = response.body();
                    if (newFacts != null) {
                        runOnUiThread(() -> {
                            facts.addAll(newFacts);
                            Log.i("res1", facts.toString());
                            adapter.notifyDataSetChanged();
                        });

                    }
                } else {
                    Toast.makeText(LikedActivity.this, sessionManagment.getSession(), Toast.LENGTH_SHORT).show();
                    Log.i("res", response.toString());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Fact>> call, Throwable t) {
                Log.i("error", t.getMessage());
            }
        });
    }
}