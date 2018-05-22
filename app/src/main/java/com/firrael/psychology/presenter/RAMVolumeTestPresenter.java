package com.firrael.psychology.presenter;

import android.os.Bundle;

import com.firrael.psychology.App;
import com.firrael.psychology.RConnectorService;
import com.firrael.psychology.model.User;
import com.firrael.psychology.view.tests.RAMVolumeTestFragment;

import icepick.State;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.firrael.psychology.Requests.REQUEST_RESULTS_RAM;

/**
 * Created by railag on 21.05.2018.
 */
public class RAMVolumeTestPresenter extends BasePresenter<RAMVolumeTestFragment> {

    @State
    long userId;

    @State
    Double time;

    @State
    long wins;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        RConnectorService service = App.restService();

        restartableLatestCache(REQUEST_RESULTS_RAM,
                () -> service.sendRAMVolumeResults(userId, time, wins)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                RAMVolumeTestFragment::onSuccess,
                RAMVolumeTestFragment::onError);
    }

    public void save(Double time, long wins) {
        this.userId = User.get(App.getMainActivity()).getId();
        this.time = time;
        this.wins = wins;

        start(REQUEST_RESULTS_RAM);
    }
}

