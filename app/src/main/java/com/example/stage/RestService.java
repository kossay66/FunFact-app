package com.example.stage;

import java.util.ArrayList;
import java.util.ResourceBundle;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestService {
    @POST("like/add")
    Call <ResponseBody> addFact(@Body Fact fact);
    @GET("like/getAllByUserID/{userID}")
    Call <ArrayList<Fact>> getAllByUserID(@Path("userID") String userID);

}
