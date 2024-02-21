package com.example.stage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class LoginActivity extends AppCompatActivity {
    EditText mail, password;
    Button login;
    TextView signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mail = findViewById(R.id.mail);
        password = findViewById(R.id.password);
        login = findViewById(R.id.loginbtn);
        signup = findViewById(R.id.signuptext);
        signup.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignupActivity.class)));
        login.setOnClickListener(v -> {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://"+getString(R.string.ip)+":8080/Users/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            RequestUser requestUser = retrofit.create(RequestUser.class);
            requestUser.getUser(mail.getText().toString(), password.getText().toString()).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    Log.i("res", response.body().userID);
                    login(response.body().userID);

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "invalid user", Toast.LENGTH_SHORT).show();
                    Log.i("error", t.getMessage());


                }
            });
        });


    }


    @Override
    protected void onStart() {
        super.onStart();

        checkSession();

    }

    private void checkSession() {
        SessionManagment sessionManagment = new SessionManagment(LoginActivity.this);
        String userID = sessionManagment.getSession();
        Toast.makeText(this, userID, Toast.LENGTH_SHORT).show();
        if (!userID.equals( "noID")) {
            moveToMainActivity();
        }
    }

    public void login(String userID) {

        SessionManagment sessionManagment = new SessionManagment(LoginActivity.this);
        sessionManagment.saveSession(userID);
        moveToMainActivity();
    }

    private void moveToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    interface RequestUser {
        @GET("user/login/{mail}/{password}")
        Call<User> getUser(@Path("mail") String mail,
                           @Path("password") String password);
    }
}