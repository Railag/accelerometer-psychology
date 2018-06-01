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
import com.firrael.psychology.presenter.AttentionVolumeTestPresenter;
import com.firrael.psychology.view.adapter.SignsAdapter2;
import com.firrael.psychology.view.base.BaseFragment;
import com.firrael.psychology.view.results.AttentionVolumeResultsFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.BindViews;
import nucleus.factory.RequiresPresenter;


@RequiresPresenter(AttentionVolumeTestPresenter.class)
public class AttentionVolumeTestFragment extends BaseFragment<AttentionVolumeTestPresenter> implements BluetoothEventListener {

    private final static int[] backgroundIds = {R.drawable.vertical_background, R.drawable.vertical_background, R.drawable.vertical_background, R.drawable.vertical_background, R.drawable.vertical_background,
            R.drawable.vertical_background, R.drawable.vertical_background, R.drawable.vertical_background, R.drawable.vertical_background, R.drawable.vertical_background};

    private final static int MAX_BACKGROUNDS = 1;

    private final static int SIGNS_TYPES = 12;

    private final static int SIGNS_PER_LINE = 5;

    @BindViews({R.id.sign1, R.id.sign2, R.id.sign3, R.id.sign4, R.id.sign5, R.id.sign6, R.id.sign7, R.id.sign8, R.id.sign9, R.id.sign10, R.id.sign11, R.id.sign12})
    ImageView[] signImages;

    @BindView(R.id.attentionBackground)
    RelativeLayout attentionBackground;

    @BindView(R.id.signsGrid)
    RecyclerView signsGrid;

    List<Sign> signsCounter;

    int currentBackground = 0;

    private boolean isSelection;

    private int previousSelection;
    private int currentSignSelection;

    Random random = new Random();

    private Handler handler;

    private long time;
    private ArrayList<Answer> answers = new ArrayList<>();

    private SignsAdapter2 signsAdapter;

    private double resultTime;
    private long winsCount;
    private SignsAdapter2.OnSignClickListener listener;

    public static AttentionVolumeTestFragment newInstance() {

        Bundle args = new Bundle();

        AttentionVolumeTestFragment fragment = new AttentionVolumeTestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.attentionVolumeTestTitle);
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_test_attention_volume;
    }

    @Override
    protected void initView(View v) {
        handler = new Handler();

        signsCounter = Arrays.asList(Sign.values());
        for (Sign sign : signsCounter) {
            sign.setChosen(false);
            sign.setSelected(false);
            sign.setShown(false);
        }

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
        List<Sign> signs = Sign.randomSigns(SIGNS_TYPES);

        for (Sign sign : signs) {
            while (true) {
                int position = random.nextInt(SIGNS_TYPES);
                if (signImages[position].getDrawable() == null) {
                    signImages[position].setImageResource(sign.getDrawableId());
                    break;
                }
            }

            signsCounter.get(signsCounter.indexOf(sign)).setShown(true);
        }
    }

    private void setupBackground() {
        int backgroundNumber = random.nextInt(backgroundIds.length);
        attentionBackground.setBackgroundResource(backgroundIds[backgroundNumber]);
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
    public void onResume() {
        super.onResume();
        if (getMainActivity() != null) {
            getMainActivity().registerBluetoothListener(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getMainActivity() != null) {
            getMainActivity().unregisterBluetoothListener(this);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    private void toFinalSelection() {
        for (ImageView sign : signImages) {
            sign.setVisibility(View.GONE);
        }

        time = System.nanoTime();

        signsCounter.get(0).setSelected(true);

        listener = sign -> {
            sign.setChosen(!sign.isChosen());

            int chosenCounter = 0;
            for (Sign s : signsCounter) {
                if (s.isChosen()) {
                    chosenCounter++;
                }
            }

            if (chosenCounter >= SIGNS_TYPES) {
                toResults();
            } else {
                signsAdapter.notifyDataSetChanged();
            }
        };

        signsAdapter = new SignsAdapter2();
        signsAdapter.setSigns(signsCounter, listener);

        GridLayoutManager manager = new GridLayoutManager(getActivity(), SIGNS_PER_LINE);
        signsGrid.setLayoutManager(manager);

        signsGrid.setAdapter(signsAdapter);

        isSelection = true;
    }

    private void toResults() {
        List<Sign> chosenSigns = new ArrayList<>();
        for (Sign s : signsCounter) {
            if (s.isChosen()) {
                chosenSigns.add(s);
            }
        }

        winsCount = 0;

        for (Sign s : chosenSigns) {
            if (s.isChosen() && s.wasShown()) {
                winsCount++;
                s.setChosen(false);
                s.setShown(false);
            }
        }

        resultTime = Utils.calcTime(time);

        getPresenter().save(resultTime, winsCount);
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
        args.putDouble(AttentionVolumeResultsFragment.TIME, resultTime);
        args.putLong(AttentionVolumeResultsFragment.WINS, winsCount);
        getMainActivity().toAttentionVolumeResults(args);
    }

    public void onError(Throwable throwable) {
        stopLoading();
        throwable.printStackTrace();
    }

    @Override
    public void onLeft() {
        if (isSelection) {
            if (currentSignSelection > 0) {
                currentSignSelection--;
                refreshSelection();
            }
        }
    }

    private void refreshSelection() {
        signsCounter.get(previousSelection).setSelected(false);
        signsCounter.get(currentSignSelection).setSelected(true);

        previousSelection = currentSignSelection;

        if (signsAdapter != null) {
            signsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRight() {
        if (isSelection) {
            if (currentSignSelection < signsCounter.size() - 1) {
                currentSignSelection++;
                refreshSelection();
            }
        }
    }

    @Override
    public void onTop() {
        if (isSelection) {
            if (currentSignSelection >= SIGNS_PER_LINE) {
                currentSignSelection -= SIGNS_PER_LINE;
                refreshSelection();
            }
        }
    }

    @Override
    public void onBottom() {
        if (isSelection) {
            if (currentSignSelection + SIGNS_PER_LINE < signsCounter.size() - 1) {
                currentSignSelection += SIGNS_PER_LINE;
                refreshSelection();
            }
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