package com.khalid.apicalls.network;

import com.khalid.apicalls.models.CurrentObservation;
import com.khalid.apicalls.models.ResponseUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GetDataService {

    @GET("users")
    Call<CurrentObservation> getAllUsers();

    @GET()
    Call<ResponseUser> getUser(@Url String url);

}
