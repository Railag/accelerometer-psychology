package com.firrael.psychology.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firrael.psychology.R;
import com.firrael.psychology.model.StatisticsResult;
import com.firrael.psychology.model.User;
import com.firrael.psychology.presenter.StatisticsPresenter;
import com.firrael.psychology.view.adapter.EnglishResultsAdapter;
import com.firrael.psychology.view.adapter.FocusingResultsAdapter;
import com.firrael.psychology.view.adapter.StabilityResultsAdapter;
import com.firrael.psychology.view.adapter.StressResultsAdapter;
import com.firrael.psychology.view.base.BaseFragment;

import java.util.Collections;

import butterknife.BindView;
import nucleus.factory.RequiresPresenter;

/**
 * Created by Railag on 03.05.2017.
 */
@RequiresPresenter(StatisticsPresenter.class)
public class StatisticsFragment extends BaseFragment<StatisticsPresenter> {

    @BindView(R.id.focusingResultsList)
    RecyclerView focusingResultsList;
    @BindView(R.id.stabilityResultsList)
    RecyclerView stabilityResultsList;
    @BindView(R.id.stressResultsList)
    RecyclerView stressResultsList;
    @BindView(R.id.englishResultsList)
    RecyclerView englishResultsList;
    @BindView(R.id.focusingSection)
    LinearLayout focusingSection;
    @BindView(R.id.stabilitySection)
    LinearLayout stabilitySection;
    @BindView(R.id.stressSection)
    LinearLayout stressSection;
    @BindView(R.id.englishSection)
    LinearLayout englishSection;
    @BindView(R.id.emptyText)
    TextView emptyText;
    @BindView(R.id.contentSection)
    LinearLayout contentSection;

    public static StatisticsFragment newInstance() {

        Bundle args = new Bundle();

        StatisticsFragment fragment = new StatisticsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.statistics);
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_statistics;
    }

    @Override
    protected void initView(View v) {
        startLoading();

        LinearLayoutManager focusingManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        focusingResultsList.setLayoutManager(focusingManager);

        LinearLayoutManager stabilityManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        stabilityResultsList.setLayoutManager(stabilityManager);

        LinearLayoutManager stressManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        stressResultsList.setLayoutManager(stressManager);

        LinearLayoutManager englishManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        englishResultsList.setLayoutManager(englishManager);

        fetchData();
    }

    private void fetchData() {
        User user = User.get(getActivity());
        getPresenter().fetch(user.getId());
    }

    private StatisticsResult sortResults(StatisticsResult result) {
        Collections.sort(result.focusingResults);
        Collections.sort(result.stressResults);
        Collections.sort(result.stabilityResults);
        Collections.sort(result.englishResults);
        return result;
    }

    private void fillUi(StatisticsResult result) {
        boolean empty = true;

        FocusingResultsAdapter focusingAdapter = new FocusingResultsAdapter();
        if (result.focusingResults != null && result.focusingResults.size() > 0) {
            focusingSection.setVisibility(View.VISIBLE);
            focusingAdapter.setAllResults(result.focusingResults);
            focusingResultsList.setAdapter(focusingAdapter);
            empty = false;
        } else {
            focusingSection.setVisibility(View.GONE);
        }

        StressResultsAdapter stressAdapter = new StressResultsAdapter();
        if (result.stressResults != null && result.stressResults.size() > 0) {
            stressSection.setVisibility(View.VISIBLE);
            stressAdapter.setAllResults(result.stressResults);
            stressResultsList.setAdapter(stressAdapter);
            empty = false;
        } else {
            stressSection.setVisibility(View.GONE);
        }

        StabilityResultsAdapter stabilityAdapter = new StabilityResultsAdapter();
        if (result.stabilityResults != null && result.stabilityResults.size() > 0) {
            stabilitySection.setVisibility(View.VISIBLE);
            stabilityAdapter.setAllResults(result.stabilityResults);
            stabilityResultsList.setAdapter(stabilityAdapter);
            empty = false;
        } else {
            stabilitySection.setVisibility(View.GONE);
        }

        EnglishResultsAdapter englishAdapter = new EnglishResultsAdapter();
        if (result.englishResults != null && result.englishResults.size() > 0) {
            englishSection.setVisibility(View.VISIBLE);
            englishAdapter.setAllResults(result.englishResults);
            englishResultsList.setAdapter(englishAdapter);
            empty = false;
        } else {
            englishSection.setVisibility(View.GONE);
        }

        if (empty) {
            contentSection.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        } else {
            emptyText.setVisibility(View.GONE);
            contentSection.setVisibility(View.VISIBLE);
        }
    }

    public void onSuccess(StatisticsResult result) {
        stopLoading();

        if (result == null) {
            onError(new IllegalArgumentException());
            return;
        }
        if (result.invalid()) {
            toast(result.error);
            return;
        }

        StatisticsResult sortedResult = sortResults(result);
        fillUi(sortedResult);
    }

    public void onError(Throwable throwable) {
        stopLoading();
        throwable.printStackTrace();
    }
}