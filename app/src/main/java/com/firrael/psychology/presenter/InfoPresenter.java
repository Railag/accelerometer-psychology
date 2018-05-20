package com.firrael.psychology.presenter;

import android.os.Bundle;

import com.firrael.psychology.App;
import com.firrael.psychology.RConnectorService;
import com.firrael.psychology.model.User;
import com.firrael.psychology.view.InfoFragment;

import icepick.State;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.firrael.psychology.Requests.REQUEST_UPDATE_INFO;

/**
 * Created by Railag on 21.03.2017.
 */

public class InfoPresenter extends BasePresenter<InfoFragment> {

    @State
    long userId;

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

        restartableLatestCache(REQUEST_UPDATE_INFO,
                () -> service.updateInfo(userId, email, age, time)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                InfoFragment::onSuccess,
                InfoFragment::onError);
    }

    public void save(long userId, String login, String time, String age) {
        this.userId = userId;

        try {
            int ageNumber = Integer.valueOf(age);
            this.age = ageNumber;
            int timeNumber = Integer.valueOf(time);
            this.time = timeNumber;
            this.email = login;

            User user = User.get(App.getMainActivity());
            user.setAge(ageNumber);
            user.setEmail(email);
            user.setTime(timeNumber);
            User.get(App.getMainActivity()).setEmail(email);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        start(REQUEST_UPDATE_INFO);
    }
}
