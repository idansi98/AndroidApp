package com.example.androidapp.api;


import com.example.androidapp.classes.ServerContact;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ContactServiceApi {
    @GET("contacts")
    Call<List<ServerContact>> getServerContacts();

    @POST("contacts")
    Call<Void> createServerContact(@Body ServerContact serverContact);

    @DELETE("contacts/{id}")
    Call<Void> deleteServerContact(@Path("id") String id);
}
