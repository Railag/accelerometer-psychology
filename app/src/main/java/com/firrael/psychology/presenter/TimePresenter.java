package com.firrael.psychology.presenter;

import android.os.Bundle;

import com.firrael.psychology.App;
import com.firrael.psychology.RConnectorService;
import com.firrael.psychology.model.User;
import com.firrael.psychology.view.register.TimeFragment;

import icepick.State;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.firrael.psychology.Requests.REQUEST_CREATE_ACCOUNT;

/**
 * Created by Railag on 07.11.2016.
 */

public class TimePresenter extends BasePresenter<TimeFragment> {

    @State
    String login;

    @State
    String password;

    @State
    String email;

    @State
    int age;

    @State
    int time;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        RConnectorService service = App.restService();

        restartableLatestCache(REQUEST_CREATE_ACCOUNT,
                () -> service.createAccount(login, password, email, age, time)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                TimeFragment::onSuccess,
                TimeFragment::onError);
    }

    public void save(String time) {
        try {
            int timeNumber = Integer.valueOf(time);
            this.time = timeNumber;
            User.get(App.getMainActivity()).setTime(timeNumber);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void request(String login, String password, String email, int age, int time) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.age = age;
        this.time = time;
        start(REQUEST_CREATE_ACCOUNT);
    }
}
