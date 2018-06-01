package com.firrael.psychology.view.tests;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firrael.psychology.App;
import com.firrael.psychology.BluetoothEventListener;
import com.firrael.psychology.R;
import com.firrael.psychology.Utils;
import com.firrael.psychology.model.Answer;
import com.firrael.psychology.model.Difficulty;
import com.firrael.psychology.model.Result;
import com.firrael.psychology.presenter.AttentionStabilityTestPresenter;
import com.firrael.psychology.view.base.BaseFragment;
import com.firrael.psychology.view.results.AttentionStabilityResultsFragment;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;


@RequiresPresenter(AttentionStabilityTestPresenter.class)
public class AttentionStabilityTestFragment extends BaseFragment<AttentionStabilityTestPresenter> implements BluetoothEventListener {

    private final static int MAX_NUMBER = 10;

    private Handler handler;

    private int wins;
    private long errors;
    private long misses;

    @BindView(R.id.testBackground)
    View testBackground;

    @BindView(R.id.number)
    TextView number;

    @BindView(R.id.background)
    View background;

    private Random random = new Random();

    private int currentNum = -1;

    private int progressTime = 0;

    private boolean active;

    private long time;

    private ArrayList<Answer> answers = new ArrayList<>();

    public static AttentionStabilityTestFragment newInstance() {

        Bundle args = new Bundle();

        AttentionStabilityTestFragment fragment = new AttentionStabilityTestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.attentionStabilityTitle);
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_test_first;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        handler = new Handler();

        Random randomTime = new Random();

        Difficulty diff = App.diff(getActivity());

        for (int i = 0; i < 100 * diff.getLevel(); i++) {
            if (i < 10) { // first 10 numbers
                progressTime += randomTime.nextInt(800);
                handler.postDelayed(() -> {
                    int num = generateRandomNumber();
                    number.setText(String.valueOf(num));
                }, progressTime);
            } else if (i % 10 == 0) { // first square
                progressTime += 800;
                handler.postDelayed(this::showRedBackground, progressTime);
            } else if (i == 99 * diff.getLevel()) {
                progressTime += 250;
                handler.postDelayed(this::toNextTest, progressTime);
            } else if (i % 10 == 1) { // number between
                progressTime += 700;
                handler.postDelayed(() -> {
                    currentNum = generateRandomNumber(); // number before second red square
                    number.setText(String.valueOf(currentNum));
                    showNumber();
                    time = System.nanoTime();
                    active = true;
                }, progressTime);
            } else if (i % 10 == 2) { // second square
                progressTime += 300;
                handler.postDelayed(this::showRedBackground, progressTime);
            } else if (i % 10 == 3) { // hide second square
                progressTime += 700;
                handler.postDelayed(this::showNumber, progressTime);
            } else if (i % 10 == 4) {
                progressTime += 50;
                handler.postDelayed(() -> {
                    int num = generateRandomNumber();
                    number.setText(String.valueOf(num));
                }, progressTime);
            } else {
                progressTime += randomTime.nextInt(800);
                handler.postDelayed(() -> {
                    int num = generateRandomNumber();
                    number.setText(String.valueOf(num));
                    if (active) {
                        misses++;
                        active = false;
                    }
                }, progressTime);
            }
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @OnClick(R.id.leftButton) // четное
    public void leftClick() {
        click(false);
    }

    @OnClick(R.id.rightButton) // нечетное
    public void rightClick() {
        click(true);
    }

    public void click(boolean right) {

        if (!active)
            return;

        double resultTime = Utils.calcTime(time);
        Answer answer = new Answer();
        answer.setTime(resultTime);

        boolean evenNumber = currentNum % 2 == 0; // четное

        if (right) {
            if (evenNumber) {
                errors++;
                answer.setErrorValue(1);
            } else {
                wins++;
                answer.setErrorValue(0);
            }
        } else {
            if (!evenNumber) {
                errors++;
                answer.setErrorValue(1);
            } else {
                wins++;
                answer.setErrorValue(0);
            }
        }

        answer.setNumber(answers.size());
        answers.add(answer);

        active = false;

        //    Toast.makeText(getActivity(), "Wins = " + wins + ", Fails = " + errors, Toast.LENGTH_SHORT).show();
    }

    private int generateRandomNumber() {
        return random.nextInt(MAX_NUMBER);
    }

    private void showRedBackground() {
        background.setVisibility(View.VISIBLE);
    }

    private void showNumber() {
        background.setVisibility(View.GONE);
    }

    private void toNextTest() {
        ArrayList<Double> times = new ArrayList<>();

        for (Answer a : answers) {
            times.add(a.getTime());
        }

        startLoading();
        getPresenter().save(times, errors, misses);
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
        args.putParcelableArrayList(AttentionStabilityResultsFragment.RESULTS, answers);
        getMainActivity().toAttentionStabilityResults(args);
    }

    public void onError(Throwable throwable) {
        stopLoading();
        throwable.printStackTrace();
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


    @Override
    public void onLeft() {
        testBackground.setBackgroundColor(getResources().getColor(R.color.greyReaction));
        click(false);
    }

    @Override
    public void onRight() {
        testBackground.setBackgroundColor(getResources().getColor(R.color.greyReaction));
        click(true);
    }

    @Override
    public void onTop() {
    }

    @Override
    public void onBottom() {
    }

    @Override
    public void onTopLeft() {
    }

    @Override
    public void onTopRight() {
    }

    @Override
    public void onBottomLeft() {
    }

    @Override
    public void onBottomRight() {
    }

    @Override
    public void onCenter() {
        testBackground.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
    }
}