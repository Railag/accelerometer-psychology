package com.firrael.psychology.presenter;

import android.os.Bundle;

import com.firrael.psychology.App;
import com.firrael.psychology.RConnectorService;
import com.firrael.psychology.model.User;
import com.firrael.psychology.view.tests.AttentionStabilityTestFragment;

import java.util.ArrayList;

import icepick.State;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.firrael.psychology.Requests.REQUEST_RESULTS_ATTENTION_STABILITY;

/**
 * Created by Railag on 06.03.2017.
 */

public class AttentionStabilityTestPresenter extends BasePresenter<AttentionStabilityTestFragment> {


    @State
    long userId;

    @State
    ArrayList<Double> times;

    @State
    long errors;

    @State
    long misses;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        RConnectorService service = App.restService();

        restartableLatestCache(REQUEST_RESULTS_ATTENTION_STABILITY,
                () -> service.sendAttentionStabilityResults(userId, times, misses, errors)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                AttentionStabilityTestFragment::onSuccess,
                AttentionStabilityTestFragment::onError);
    }

    public void save(ArrayList<Double> times, long errors, long misses) {
        this.userId = User.get(App.getMainActivity()).getId();
        this.times = times;
        this.errors = errors;
        this.misses = misses;

        start(REQUEST_RESULTS_ATTENTION_STABILITY);
    }
}
