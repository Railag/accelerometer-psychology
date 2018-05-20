package com.firrael.psychology.view.tests;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.firrael.psychology.App;
import com.firrael.psychology.view.adapter.CirclesAdapter;
import com.firrael.psychology.R;
import com.firrael.psychology.Utils;
import com.firrael.psychology.model.Answer;
import com.firrael.psychology.model.Circle;
import com.firrael.psychology.model.Difficulty;
import com.firrael.psychology.model.Result;
import com.firrael.psychology.presenter.FocusingTestPresenter;
import com.firrael.psychology.view.base.BaseFragment;
import com.firrael.psychology.view.results.FocusingResultsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;

/**
 * Created by Railag on 16.03.2017.
 */

@RequiresPresenter(FocusingTestPresenter.class)
public class FocusingTestFragment extends BaseFragment<FocusingTestPresenter> {

    private Circle baseCircle;

    private final static int LINES_VISIBLE = 11;

    private static int LINES_COUNT = 20;
    private final static int CIRCLES_PER_LINE = 15;

    private Handler handler;

    private int wins;
    private int fails;

    private boolean locked = false;

    @BindView(R.id.circlesGrid)
    RecyclerView circlesGrid;

    @BindView(R.id.baseCircle)
    ImageView baseCircleView;

    CirclesAdapter adapter;

    private int currentLine = 0;

    private long time;

    private ArrayList<Circle> circles = new ArrayList<>();

    private ArrayList<Answer> answers = new ArrayList<>();

    public static FocusingTestFragment newInstance() {

        Bundle args = new Bundle();

        FocusingTestFragment fragment = new FocusingTestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.focusingTestTitle);
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_test_circles;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        handler = new Handler();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initView(View v) {
        Difficulty diff = App.diff(getActivity());
        LINES_COUNT *= diff.getLevel();

        baseCircle = Circle.random();
        baseCircleView.setRotation(Circle.rotation(baseCircle));

        for (int i = 0; i < LINES_VISIBLE; i++) {
            ArrayList<Circle> line = new ArrayList<>();
            for (int j = 0; j < CIRCLES_PER_LINE; j++) {
                line.add(Circle.random());
            }

            int result = Circle.answer(line, baseCircle);
            if (result > 10 || result < 1) {
                i--;
            } else {
                circles.addAll(line);
            }
        }

        adapter = new CirclesAdapter();
        adapter.setCircles(circles);

        GridLayoutManager manager = new GridLayoutManager(getActivity(), CIRCLES_PER_LINE);
        circlesGrid.setLayoutManager(manager);

        circlesGrid.setAdapter(adapter);

        time = System.nanoTime();
    }

    private void replaceCircleLine() {
        ArrayList<Circle> newCircles = new ArrayList<>(circles.subList(CIRCLES_PER_LINE, circles.size()));

        if (LINES_COUNT - currentLine > LINES_VISIBLE) {
            for (int i = 0; i < CIRCLES_PER_LINE; i++) {
                newCircles.add(Circle.random());
            }
        }

        circles = newCircles;
        adapter.setCircles(circles);

        baseCircle = Circle.random();
        baseCircleView.setRotation(Circle.rotation(baseCircle));
    }

    @OnClick(R.id.button1)
    public void click1() {
        click(1);
    }

    @OnClick(R.id.button2)
    public void click2() {
        click(2);
    }

    @OnClick(R.id.button3)
    public void click3() {
        click(3);
    }

    @OnClick(R.id.button4)
    public void click4() {
        click(4);
    }

    @OnClick(R.id.button5)
    public void click5() {
        click(5);
    }

    @OnClick(R.id.button6)
    public void click6() {
        click(6);
    }

    @OnClick(R.id.button7)
    public void click7() {
        click(7);
    }

    @OnClick(R.id.button8)
    public void click8() {
        click(8);
    }

    @OnClick(R.id.button9)
    public void click9() {
        click(9);
    }

    @OnClick(R.id.button10)
    public void click10() {
        click(10);
    }

    public void click(int count) {

        if (locked) {
            return;
        }

        if (currentLine == LINES_COUNT) {
            toNextTest();
            return;
        }

        if (currentLine > LINES_COUNT) {
            return;
        }

        List<Circle> lineCircles = circles.subList(0, CIRCLES_PER_LINE);
        int answer = Circle.answer(lineCircles, baseCircle);

        Answer ans = new Answer();
        ans.setNumber(currentLine);

        if (answer == count) {
            wins++;
        } else {
            fails++;
        }

        ans.setErrorValue(Math.abs(answer - count));
        ans.setTime(Utils.calcTime(time));
        answers.add(ans);

    //    Toast.makeText(getActivity(), "Wins = " + wins + ", Fails = " + fails, Toast.LENGTH_SHORT).show();

        replaceCircleLine();
        time = System.nanoTime();

        currentLine++;
        if (currentLine == LINES_COUNT) {
            toNextTest();
        }
    }

    private void toNextTest() {
        locked = true;
        ArrayList<Double> times = new ArrayList<>();
        ArrayList<Long> errors = new ArrayList<>();

        for (Answer a : answers) {
            times.add(a.getTime());

            errors.add((long) a.getErrorValue());
        }

        startLoading();
        getPresenter().save(times, errors);
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
        args.putParcelableArrayList(FocusingResultsFragment.RESULTS, answers);
        args.putInt(FocusingResultsFragment.LINES, LINES_COUNT);
        args.putInt(FocusingResultsFragment.ERRORS, fails);
        getMainActivity().toFocusingResults(args);
    }

    public void onError(Throwable throwable) {
        stopLoading();
        throwable.printStackTrace();

        locked = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
