package com.firrael.psychology.presenter;

import android.os.Bundle;

import com.firrael.psychology.App;
import com.firrael.psychology.RConnectorService;
import com.firrael.psychology.view.StatisticsFragment;

import icepick.State;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.firrael.psychology.Requests.REQUEST_STATISTICS;
import static com.firrael.psychology.Requests.REQUEST_STATISTICS_GLOBAL;

/**
 * Created by Railag on 03.05.2017.
 */

public class StatisticsPresenter extends BasePresenter<StatisticsFragment> {

    @State
    long userId;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        RConnectorService service = App.restService();

        restartableLatestCache(REQUEST_STATISTICS,
                () -> service.fetchStatistics(userId)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                StatisticsFragment::onSuccess,
                StatisticsFragment::onError);

        restartableLatestCache(REQUEST_STATISTICS_GLOBAL,
                () -> service.fetchStatisticsGlobal()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                StatisticsFragment::onSuccessGlobal,
                StatisticsFragment::onErrorGlobal);

    }

    public void fetch(long userId) {
        this.userId = userId;

        start(REQUEST_STATISTICS);
    }

    public void fetchGlobal() {
        start(REQUEST_STATISTICS_GLOBAL);
    }
}
