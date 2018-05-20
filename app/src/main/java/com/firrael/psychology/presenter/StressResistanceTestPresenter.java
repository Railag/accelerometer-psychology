package com.firrael.psychology.presenter;

import android.os.Bundle;

import com.firrael.psychology.App;
import com.firrael.psychology.RConnectorService;
import com.firrael.psychology.model.User;
import com.firrael.psychology.view.tests.StressResistanceTestFragment;

import java.util.ArrayList;

import icepick.State;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.firrael.psychology.Requests.REQUEST_RESULTS_STRESS;

/**
 * Created by Railag on 27.04.2017.
 */

public class StressResistanceTestPresenter extends BasePresenter<StressResistanceTestFragment> {

    @State
    long userId;

    @State
    ArrayList<Double> times;

    @State
    long misses;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        RConnectorService service = App.restService();

        restartableLatestCache(REQUEST_RESULTS_STRESS,
                () -> service.sendStressResults(userId, times, misses)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                StressResistanceTestFragment::onSuccess,
                StressResistanceTestFragment::onError);
    }

    public void save(ArrayList<Double> times, long misses) {
        this.userId = User.get(App.getMainActivity()).getId();
        this.times = times;
        this.misses = misses;

        start(REQUEST_RESULTS_STRESS);
    }
}
