package com.firrael.psychology.view.tests;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.firrael.psychology.BluetoothEventListener;
import com.firrael.psychology.R;
import com.firrael.psychology.Utils;
import com.firrael.psychology.model.Answer;
import com.firrael.psychology.model.Result;
import com.firrael.psychology.model.Sign;
import com.firrael.psychology.presenter.RAMVolumeTestPresenter;
import com.firrael.psychology.view.adapter.SignsAdapter;
import com.firrael.psychology.view.base.BaseFragment;
import com.firrael.psychology.view.results.RAMVolumeResultsFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.BindViews;
import nucleus.factory.RequiresPresenter;

/**
 * Created by railag on 21.05.2018.
 */

@RequiresPresenter(RAMVolumeTestPresenter.class)
public class RAMVolumeTestFragment extends BaseFragment<RAMVolumeTestPresenter> implements BluetoothEventListener {

    private final static int[] backgroundIds = {R.drawable.vertical_background, R.drawable.vertical_background, R.drawable.vertical_background, R.drawable.vertical_background, R.drawable.vertical_background,
            R.drawable.vertical_background, R.drawable.vertical_background, R.drawable.vertical_background, R.drawable.vertical_background, R.drawable.vertical_background};

    private final static int MAX_BACKGROUNDS = 10;

    private final static int MIN_SIGNS = 5;
    private final static int MAX_SIGNS = 9;

    private final static int SIGNS_PER_LINE = 5;

    @BindViews({R.id.sign1, R.id.sign2, R.id.sign3, R.id.sign4, R.id.sign5, R.id.sign6, R.id.sign7, R.id.sign8, R.id.sign9})
    ImageView[] signImages;

    @BindView(R.id.ramBackground)
    RelativeLayout ramBackground;

    @BindView(R.id.signsGrid)
    RecyclerView signsGrid;

    List<Sign> signsCounter = Arrays.asList(Sign.values());

    int currentBackground = 0;

    private int previousSelection;
    private int currentSignSelection;

    Random random = new Random();

    private Handler handler;

    private boolean action = false;

    private long time;
    private ArrayList<Answer> answers = new ArrayList<>();

    private SignsAdapter signsAdapter;

    private double resultTime;
    private long winsCount;
    private SignsAdapter.OnSignClickListener listener;

    public static RAMVolumeTestFragment newInstance() {

        Bundle args = new Bundle();

        RAMVolumeTestFragment fragment = new RAMVolumeTestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.ramVolumeTestTitle);
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_test_ram_volume;
    }

    @Override
    protected void initView(View v) {
        handler = new Handler();

        getMainActivity().registerBluetoothListener(this);

        next();
    }

    private void next() {
        int startTime = 10000; // 10 seconds

        resetSigns();

        setupBackground();
        setupSigns();

        handler.postDelayed(() -> {
            double result = Utils.calcTime(time);
            Answer answer = new Answer();
            answer.setTime(result);

            answer.setErrorValue(1); // error

            answer.setNumber(answers.size());

            answers.add(answer);

            time = System.nanoTime();

            nextBackground();

        }, startTime);
    }

    private void resetSigns() {
        if (signImages != null && signImages.length > 0) {
            for (ImageView image : signImages) {
                image.setImageResource(0);
            }
        }
    }

    private void setupSigns() {
        List<Sign> signs = Sign.randomSigns(MIN_SIGNS, MAX_SIGNS);

        for (Sign sign : signs) {
            while (true) {
                int position = random.nextInt(MAX_SIGNS);
                if (signImages[position].getDrawable() == null) {
                    signImages[position].setImageResource(sign.getDrawableId());
                    break;
                }
            }

            signsCounter.get(signsCounter.indexOf(sign)).increase();
        }
    }

    private void setupBackground() {
        int backgroundNumber = random.nextInt(backgroundIds.length);
        ramBackground.setBackgroundResource(backgroundIds[backgroundNumber]);
    }

    private void nextBackground() {
        currentBackground++;

        if (currentBackground >= MAX_BACKGROUNDS) {
            toFinalSelection();
        } else {
            next();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }

        if (getMainActivity() != null) {
            getMainActivity().unregisterBluetoothListener(this);
        }
    }

    private void toFinalSelection() {
        time = System.nanoTime();

        Sign mostVisibleSign = signsCounter.get(0);
        for (Sign sign : signsCounter) {
            if (sign.getCounter() > mostVisibleSign.getCounter()) {
                mostVisibleSign = sign;
            }
        }

        final int maxCounter = mostVisibleSign.getCounter();

        listener = sign -> {
            boolean isSuccess = sign.getCounter() == maxCounter;

            if (isSuccess) {
                winsCount = 1;
            }

            resultTime = Utils.calcTime(time);

            getPresenter().save(resultTime, winsCount);
        };

        signsAdapter = new SignsAdapter();
        signsAdapter.setSigns(signsCounter, listener);

        GridLayoutManager manager = new GridLayoutManager(getActivity(), SIGNS_PER_LINE);
        signsGrid.setLayoutManager(manager);

        signsGrid.setAdapter(signsAdapter);
    }

    public void onSuccess(Result result) {
        stopLoading();

        if (result == null) {
            onError(new IllegalArgumentException());
            return;
        }
        if (result.invalid()) {
            toast(result.error);
            return;
        }

        Bundle args = new Bundle();
        args.putDouble(RAMVolumeResultsFragment.TIME, resultTime);
        args.putLong(RAMVolumeResultsFragment.WINS, winsCount);
        getMainActivity().toRAMVolumeResults(args);
    }

    public void onError(Throwable throwable) {
        stopLoading();
        throwable.printStackTrace();
    }

    @Override
    public void onLeft() {
        if (currentSignSelection > 0) {
            currentSignSelection--;
            refreshSelection();
        }
    }

    private void refreshSelection() {
        signsCounter.get(previousSelection).setSelected(false);
        signsCounter.get(currentSignSelection).setSelected(true);

        previousSelection = currentSignSelection;

        signsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRight() {
        if (currentSignSelection < signsCounter.size()) {
            currentSignSelection++;
            refreshSelection();
        }
    }

    @Override
    public void onTop() {
        if (currentSignSelection > SIGNS_PER_LINE) {
            currentSignSelection -= SIGNS_PER_LINE;
            refreshSelection();
        }
    }

    @Override
    public void onBottom() {
        if (currentSignSelection + SIGNS_PER_LINE < signsCounter.size()) {
            currentSignSelection += SIGNS_PER_LINE;
            refreshSelection();
        }
    }

    @Override
    public void onTopLeft() {
    }

    @Override
    public void onTopRight() {
        if (listener != null) {
            listener.onSignSelected(signsCounter.get(currentSignSelection));
        }
    }

    @Override
    public void onBottomLeft() {
    }

    @Override
    public void onBottomRight() {
    }

    @Override
    public void onCenter() {
    }
}