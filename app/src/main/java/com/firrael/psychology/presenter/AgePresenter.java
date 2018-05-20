package com.firrael.psychology.presenter;

import android.os.Bundle;

import com.firrael.psychology.App;
import com.firrael.psychology.RConnectorService;
import com.firrael.psychology.model.User;
import com.firrael.psychology.view.register.AgeFragment;

import icepick.State;

/**
 * Created by Railag on 07.11.2016.
 */

public class AgePresenter extends BasePresenter<AgeFragment> {

    @State
    int age;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        RConnectorService service = App.restService();

        /*restartableLatestCache(REQUEST_LOGIN,
                () -> service.login(login, password)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                StartFragment::onSuccess,
                StartFragment::onError);*/
    }

    public void save(String age) {
        try {
            int ageNumber = Integer.valueOf(age);
            this.age = ageNumber;
            User.get(App.getMainActivity()).setAge(ageNumber);
            App.getMainActivity().toTimeScreen();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        //    start(REQUEST_LOGIN);
    }
}

