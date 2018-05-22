package com.firrael.psychology.presenter;

import android.os.Bundle;
import android.util.Log;

import com.firrael.psychology.App;
import com.firrael.psychology.RConnectorService;
import com.firrael.psychology.model.User;
import com.firrael.psychology.view.BluetoothSetupFragment;

import java.util.ArrayList;

import icepick.State;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.firrael.psychology.Requests.REQUEST_RESULTS_ACCELEROMETER;

/**
 * Created by railag on 26.02.2018.
 */

public class AccelerometerTestPresenter extends BasePresenter<BluetoothSetupFragment> {
    private final static String TAG = BluetoothSetupFragment.class.getSimpleName();

    @State
    long userId;

    @State
    ArrayList<Double> x;

    @State
    ArrayList<Double> y;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        RConnectorService service = App.restService();

        restartableLatestCache(REQUEST_RESULTS_ACCELEROMETER,
                () -> service.sendAccelerometerResults(userId, x, y)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                BluetoothSetupFragment::onSuccess,
                BluetoothSetupFragment::onError);
    }

    public void save(ArrayList<Double> x, ArrayList<Double> y) {
        this.userId = User.get(App.getMainActivity()).getId();
        this.x = x;
        this.y = y;

        Log.i(TAG, "Upload:" + x);

        start(REQUEST_RESULTS_ACCELEROMETER);
    }
}