package com.firrael.psychology;

import android.app.Application;
import android.content.Context;

import com.firrael.psychology.model.Difficulty;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    public static final String PREFS = "prefs";
    public static final String DIFFICULTY_KEY = "difficulty";

    private static WeakReference<MainActivity> activityRef;

    private static Retrofit api;
    private static RConnectorService rConnectorService;

    private static Retrofit api() {
        if (api == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);

            OkHttpClient client = httpClient.build();

            Gson gson = new Gson();

            api = new Retrofit.Builder()
                    .baseUrl(RConnectorService.API_ENDPOINT)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }

        return api;
    }

    public static RConnectorService restService() {
        if (rConnectorService == null)
            rConnectorService = createRetrofitService(RConnectorService.class);

        return rConnectorService;
    }

    public static void setMainActivity(MainActivity activity) {
        activityRef = new WeakReference<>(activity);
    }

    public static MainActivity getMainActivity() {
        return activityRef != null ? activityRef.get() : null;
    }

    private static <T> T createRetrofitService(final Class<T> clazz) {
        return api().create(clazz);
    }

    public static Difficulty diff(Context context) {
        int level = Utils.prefs(context).getInt(DIFFICULTY_KEY, Difficulty.MEDIUM.getLevel());
        Difficulty currentDifficulty = Difficulty.create(level);
        return currentDifficulty;
    }

    public static void setDiff(Context context, Difficulty diff) {
        Utils.prefs(context).edit().putInt(DIFFICULTY_KEY, diff.getLevel()).apply();
    }
}
