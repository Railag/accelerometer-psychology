package com.firrael.psychology.presenter;

import android.os.Bundle;

import com.firrael.psychology.App;
import com.firrael.psychology.RConnectorService;
import com.firrael.psychology.model.User;
import com.firrael.psychology.view.tests.FocusingTestFragment;

import java.util.ArrayList;

import icepick.State;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.firrael.psychology.Requests.REQUEST_RESULTS_FOCUSING;

/**
 * Created by Railag on 16.03.2017.
 */

public class FocusingTestPresenter extends BasePresenter<FocusingTestFragment> {


    @State
    long userId;

    @State
    ArrayList<Double> times;

    @State
    ArrayList<Long> errors;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        RConnectorService service = App.restService();

        restartableLatestCache(REQUEST_RESULTS_FOCUSING,
                () -> service.sendFocusingResults(userId, times, errors)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                FocusingTestFragment::onSuccess,
                FocusingTestFragment::onError);
    }

    public void save(ArrayList<Double> times, ArrayList<Long> errors) {
        this.userId = User.get(App.getMainActivity()).getId();
        this.times = times;
        this.errors = errors;

        start(REQUEST_RESULTS_FOCUSING);
    }
}
