package com.example.stage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class SignupActivity extends AppCompatActivity {
    EditText username, email, password, confpassword;
    Button signup;
    TextView tologin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confpassword = findViewById(R.id.confpassword);
        signup = findViewById(R.id.button);
        tologin = findViewById(R.id.signuptext);

        tologin.setOnClickListener(v -> startActivity(new Intent(SignupActivity.this, LoginActivity.class)));
        signup.setOnClickListener(v -> {
            if (checkParams()) {
                User user = new User(username.getText().toString(), email.getText().toString(), password.getText().toString());
                retroadd(user);
            }
        });

    }

    private Boolean checkParams() {
        if (username.getText().toString().isEmpty() || email.getText().toString().isEmpty() || password.getText().toString().isEmpty()
                || confpassword.getText().toString().isEmpty()) {
            Toast.makeText(this, "no empty inputs please", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.getText().toString().length() < 6) {
            Toast.makeText(this, "password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!password.getText().toString().equals(confpassword.getText().toString())) {
            Toast.makeText(this, "passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!email.getText().toString().contains("@") || !email.getText().toString().substring(email.getText().toString().indexOf("@")).contains(".")) {
            Toast.makeText(this, "invalid Email", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

    private void retroadd(User user) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://" + getString(R.string.ip) + ":8080/Users/").addConverterFactory(GsonConverterFactory.create()).build();

        RequestSignup requestuser = retrofit.create(RequestSignup.class);
        requestuser.addUser(user).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(SignupActivity.this, "Done !", Toast.LENGTH_SHORT).show();
                Log.i("rree", response.toString());
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i("error ", t.getMessage());
            }
        });

    }

    interface RequestSignup {
        @POST("user/add")
        Call<Void> addUser(@Body User user);
    }
}