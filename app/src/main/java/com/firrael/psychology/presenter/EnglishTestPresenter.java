package com.firrael.psychology.presenter;

import android.os.Bundle;

import com.firrael.psychology.App;
import com.firrael.psychology.RConnectorService;
import com.firrael.psychology.model.User;
import com.firrael.psychology.view.tests.EnglishTestFragment;

import java.util.ArrayList;

import icepick.State;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.firrael.psychology.Requests.REQUEST_RESULTS_ENGLISH;

/**
 * Created by railag on 13.02.2018.
 */

public class EnglishTestPresenter extends BasePresenter<EnglishTestFragment> {

    @State
    long userId;

    @State
    ArrayList<String> words;

    @State
    ArrayList<Double> times;

    @State
    long errors;


    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        RConnectorService service = App.restService();

        restartableLatestCache(REQUEST_RESULTS_ENGLISH,
                () -> service.sendEnglishResults(userId, times, words, errors)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                EnglishTestFragment::onSuccess,
                EnglishTestFragment::onError);
    }

    public void save(ArrayList<Double> times, long errors, ArrayList<String> words) {
        this.userId = User.get(App.getMainActivity()).getId();
        this.times = times;
        this.errors = errors;
        this.words = words;

        start(REQUEST_RESULTS_ENGLISH);
    }
}