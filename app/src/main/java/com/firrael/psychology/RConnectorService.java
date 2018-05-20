package com.firrael.psychology;

import com.firrael.psychology.model.Result;
import com.firrael.psychology.model.StatisticsResult;
import com.firrael.psychology.model.UserResult;

import java.util.ArrayList;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by firrael on 25.05.2016.
 */
public interface RConnectorService {
    //String API_ENDPOINT = "http://127.0.0.1:3000";
    //String API_ENDPOINT = "http://10.0.2.2:3000";
    //String API_ENDPOINT = "http://10.0.3.2:3000";
    String API_ENDPOINT = "https://firrael.herokuapp.com";

    @FormUrlEncoded
    @POST("/user/login")
    Observable<UserResult> login(@Field("login") String login, @Field("password") String password);

    @FormUrlEncoded
    @POST("/user/startup_login")
    Observable<UserResult> startupLogin(@Field("login") String login, @Field("token") String token);

    @FormUrlEncoded
    @POST("/user")
    Observable<UserResult> createAccount(@Field("login") String login, @Field("password") String password, @Field("email") String email, @Field("age") int age, @Field("time") int time);

    @FormUrlEncoded
    @POST("/user/fcm_token")
    Observable<Result> sendFCMToken(@Field("user_id") long userId, @Field("fcm_token") String fcmToken);

    @FormUrlEncoded
    @POST("/user/update")
    Observable<UserResult> updateInfo(@Field("user_id") long userId, @Field("email") String email, @Field("age") int age, @Field("time") int time);

    @FormUrlEncoded
    @POST("/user/results_stress")
    Observable<Result> sendStressResults(@Field("user_id") long userId, @Field("times[]") ArrayList<Double> times, @Field("misses") long misses);

    @FormUrlEncoded
    @POST("/user/results_focusing")
    Observable<Result> sendFocusingResults(@Field("user_id") long userId, @Field("times[]") ArrayList<Double> times, @Field("error_values[]") ArrayList<Long> errors);

    @FormUrlEncoded
    @POST("/user/results_stability")
    Observable<Result> sendAttentionStabilityResults(@Field("user_id") long userId, @Field("times[]") ArrayList<Double> times, @Field("misses") long misses, @Field("errors_value") long errors);

    @FormUrlEncoded
    @POST("/user/results_english")
    Observable<Result> sendEnglishResults(@Field("user_id") long userId, @Field("times[]") ArrayList<Double> times, @Field("words[]") ArrayList<String> words, @Field("errors_value") long errors);

    @FormUrlEncoded
    @POST("/user/results_accelerometer")
    Observable<Result> sendAccelerometerResults(@Field("user_id") long userId, @Field("x[]") ArrayList<Double> x,
                                                @Field("y[]") ArrayList<Double> y);

    @FormUrlEncoded
    @POST("/user/statistics")
    Observable<StatisticsResult> fetchStatistics(@Field("user_id") long userId);
}