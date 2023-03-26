package com.example.studybuddy.model.api;

import com.example.studybuddy.objects.Student;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface API {

    @FormUrlEncoded
    @POST("updateStudentDetails")
    Call<ResponseBody> updateStudentDetails(
            @Field("uid") String uid,
            @Field("name") String name,
            @Field("year") String year,
            @Field("degree") String degree,
            @Field("gender") String gender,
            @Field("age") String age,
            @Field("phone") String phone
    );

    @GET("getStudentDetails")
    Call<Student> getStudentDetails(
            @Query("uid") String uid
    );


}
