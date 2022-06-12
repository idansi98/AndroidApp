package com.example.androidapp.api;

import com.example.androidapp.classes.ServerContact;
import com.example.androidapp.classes.ServerMessage;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MessageServiceApi {
    @GET("contacts/{id}/messages")
    Call<List<ServerMessage>> getAllMessages(@Path("id") String contactId);

    @POST("contacts/{id}/messages")
    Call<Void> createServerContact(@Path("id") String contactId, @Body ServerContact serverContact);

    @DELETE("contacts/{id}/messages/{id2}")
    Call<Void> deleteServerContact(@Path("id") String contactId, @Path("id2") String messageId);
}
