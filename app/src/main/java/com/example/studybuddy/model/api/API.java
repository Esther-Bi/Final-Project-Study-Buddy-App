package com.example.studybuddy.model.api;

import com.example.studybuddy.objects.Student;
import com.example.studybuddy.objects.Teacher;

import java.util.ArrayList;

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

    @FormUrlEncoded
    @POST("updateTeacherDetails")
    Call<ResponseBody> updateTeacherDetails(
            @Field("uid") String uid,
            @Field("name") String name,
            @Field("year") String year,
            @Field("degree") String degree,
            @Field("gender") String gender,
            @Field("age") String age,
            @Field("phone") String phone,
            @Field("payBox") String payBox
    );

    @FormUrlEncoded
    @POST("newTeacher")
    Call<ResponseBody> newTeacher(
            @Field("uid") String uid,
            @Field("name") String name,
            @Field("year") String year,
            @Field("degree") String degree,
            @Field("gender") String gender,
            @Field("age") String age,
            @Field("phone") String phone,
            @Field("payBox") String payBox
    );

    @GET("getStudentDetails")
    Call<Student> getStudentDetails(
            @Query("uid") String uid
    );

    @GET("getFilteredTeachers")
    Call<ArrayList<Teacher>> getFilteredTeachers(
            @Query("course") String course,
            @Query("date") String date,
            @Query("from") String from,
            @Query("to") String to
    );

    @GET("getAllTeachers")
    Call<ArrayList<Teacher>> getAllTeachers(
    );

    @GET("getTeacherDetails")
    Call<Teacher> getTeacherDetails(
            @Query("uid") String uid
    );

    @FormUrlEncoded
    @POST("bookClass")
    Call<ResponseBody> bookClass(
            @Field("uid") String uid,
            @Field("classes") String[] classes,
            @Field("courseValueFromSpinner") String courseValueFromSpinner,
            @Field("prices") Integer[] prices,
            @Field("dateValueFromSpinner") String dateValueFromSpinner,
            @Field("teacherID") String teacherID
    );


}
