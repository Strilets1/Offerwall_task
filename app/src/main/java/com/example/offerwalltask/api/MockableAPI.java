package com.example.offerwalltask.api;

import com.example.offerwalltask.model.ObjectListModel;
import com.example.offerwalltask.model.RecordListModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MockableAPI {

    @GET("trending")
    Call<List<RecordListModel>> getTrending();

    @GET("object/{id}")
    Call<ObjectListModel> getObject(@Path("id") String id);
}
