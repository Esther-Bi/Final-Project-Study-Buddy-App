package com.example.studybuddy.model.api;

import com.example.studybuddy.objects.Group;
import com.example.studybuddy.objects.Student;
import com.example.studybuddy.objects.Teacher;
import com.example.studybuddy.objects.Class;


import java.util.ArrayList;

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

    @FormUrlEncoded
    @POST("addCourseAndGradeToTeacher")
    Call<ResponseBody> addCourseAndGradeToTeacher(
            @Field("uid") String uid,
            @Field("course") String course,
            @Field("grade") String grade,
            @Field("price") String price
    );

    @FormUrlEncoded
    @POST("deleteCourseAndGradeFromTeacher")
    Call<ResponseBody> deleteCourseAndGradeFromTeacher(
            @Field("uid") String uid,
            @Field("course") String course
    );

    @FormUrlEncoded
    @POST("addDateToTeacher")
    Call<ResponseBody> addDateToTeacher(
            @Field("uid") String uid,
            @Field("date") String date
    );

    @FormUrlEncoded
    @POST("deleteDateFromTeacher")
    Call<ResponseBody> deleteDateFromTeacher(
            @Field("uid") String uid,
            @Field("date") String date
    );

    @FormUrlEncoded
    @POST("deleteClassTeacher")
    Call<ResponseBody> deleteClassTeacher(
            @Field("uid") String uid,
            @Field("name") String name,
            @Field("subject") String subject,
            @Field("date") String date
    );

    @FormUrlEncoded
    @POST("deleteClassStudent")
    Call<ResponseBody> deleteClassStudent(
            @Field("uid") String uid,
            @Field("name") String name,
            @Field("subject") String subject,
            @Field("date") String date
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
            @Query("to") String to,
            @Query("sortId") String sortId
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

    @FormUrlEncoded
    @POST("updateRate")
    Call<ResponseBody> updateRate(
            @Field("uid") String uid,
            @Field("teacherName") String teacherName,
            @Field("date") String date,
            @Field("subject") String subject,
            @Field("rating") Double rating
    );

    @FormUrlEncoded
    @POST("approveYes")
    Call<ResponseBody> approveYes(
            @Field("uid") String uid,
            @Field("teacherName") String teacherName,
            @Field("date") String date,
            @Field("subject") String subject
    );

    @GET("approvePayment")
    Call<String> approvePayment(
            @Query("uid") String uid,
            @Query("teacherName") String teacherName,
            @Query("date") String date,
            @Query("subject") String subject
    );

    @GET("getTeacherCourses")
    Call<ArrayList<String>> getTeacherCourses(
            @Query("uid") String uid
    );

    @GET("getTeacherGrades")
    Call<ArrayList<Integer>> getTeacherGrades(
            @Query("uid") String uid
    );

    @GET("getTeacherDates")
    Call<ArrayList<String>> getTeacherDates(
            @Query("uid") String uid
    );

    @GET("getStudentMobileNumber")
    Call<String> getStudentMobileNumber(
            @Query("uid") String uid,
            @Query("name") String name,
            @Query("subject") String subject,
            @Query("date") String date
    );

    @GET("getClassQuery")
    Call<com.google.firebase.firestore.Query> getClassQuery(
            @Query("uid") String uid
    );

    @GET("getTeacherMobileNumber")
    Call<String> getTeacherMobileNumber(
            @Query("uid") String uid,
            @Query("name") String name,
            @Query("subject") String subject,
            @Query("date") String date
    );

    @GET("getMyGroups")
    Call<ArrayList<Group>> getMyGroups(
            @Query("uid") String uid
    );

    @FormUrlEncoded
    @POST("deleteFromGroup")
    Call<ResponseBody> deleteFromGroup(
            @Field("uid") String uid,
            @Field("groupId") String groupId
    );

    @FormUrlEncoded
    @POST("openNewGroup")
    Call<ResponseBody> openNewGroup(
            @Field("uid") String uid,
            @Field("subject") String subject,
            @Field("degree") String degree,
            @Field("year") String year,
            @Field("day") String day,
            @Field("time") String time,
            @Field("language") String language,
            @Field("min") String min,
            @Field("max") String max,
            @Field("location") String location,
            @Field("link") String link
    );

    @GET("getAllGroups")
    Call<ArrayList<Group>> getAllGroups(
            @Query("uid") String uid
    );

    @GET("getFilteredGroups")
    Call<ArrayList<Group>> getFilteredGroups(
            @Query("uid") String uid,
            @Query("subject") String subject,
            @Query("degree") String degree,
            @Query("year") String year,
            @Query("day") String day,
            @Query("time") String time,
            @Query("language") String language,
            @Query("participants") String participants,
            @Query("location") String location
    );

    @FormUrlEncoded
    @POST("joinGroup")
    Call<ResponseBody> joinGroup(
            @Field("uid") String uid,
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("editGroupTime")
    Call<ResponseBody> editGroupTime(
            @Field("groupId") String groupId,
            @Field("day") String day,
            @Field("time") String time,
            @Field("location") String location
    );


    @GET("payBoxLink")
    Call<String> payBoxLink(
            @Query("uid") String uid,
            @Query("teacherName") String teacherName,
            @Query("subject") String subject,
            @Query("date") String date
    );

    @POST("PastCourses")
    Call<ResponseBody> PastCourses(

    );

    @GET("approvePaymentTeacher")
    Call<String> approvePaymentTeacher(
            @Query("uid") String uid,
            @Query("studentName") String studentName,
            @Query("date") String date,
            @Query("subject") String subject
    );

    @FormUrlEncoded
    @POST("deletePaidClass")
    Call<ResponseBody> deletePaidClass(
            @Field("uid") String uid,
            @Field("studentName") String name,
            @Field("subject") String subject,
            @Field("date") String date
    );

    @GET("getMyClassesStudent")
    Call<ArrayList<Class>> getMyClassesStudent(
            @Query("uid") String uid
    );

    @GET("getMyClasses")
    Call<ArrayList<Class>> getMyClasses(
            @Query("uid") String uid
    );

    @GET("getClassPayments")
    Call<ArrayList<Class>> getClassPayments(
            @Query("uid") String uid
    );

    @GET("getClassPaymentsStudent")
    Call<ArrayList<Class>> getClassPaymentsStudent(
            @Query("uid") String uid
    );

    @GET("getGroupParticipants")
    Call<ArrayList<Student>> getGroupParticipants(
            @Query("uid") String uid,
            @Query("gid") String gid
    );

    @POST("deletePastDates")
    Call<ResponseBody> deletePastDates(

    );
}
