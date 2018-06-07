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
import com.firrael.psychology.view.adapter.StabilityResultsAdapter;
import com.firrael.psychology.view.adapter.StressResultsAdapter;
import com.firrael.psychology.view.base.BaseFragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import nucleus.factory.RequiresPresenter;

@RequiresPresenter(StatisticsPresenter.class)
public class StatisticsFragment extends BaseFragment<StatisticsPresenter> {

    @BindView(R.id.stabilityResultsList)
    RecyclerView stabilityResultsList;
    @BindView(R.id.stressResultsList)
    RecyclerView stressResultsList;
    @BindView(R.id.stabilityChart2)
    BarChart stabilityChart2;
    @BindView(R.id.stabilityChart3)
    BarChart stabilityChart3;
    @BindView(R.id.stability2Chart)
    BarChart stability2Chart;
    @BindView(R.id.stability2Chart2)
    BarChart stability2Chart2;
    @BindView(R.id.ramChart)
    BarChart ramChart;
    @BindView(R.id.ram2Chart)
    BarChart ram2Chart;
    @BindView(R.id.ram2Chart2)
    BarChart ram2Chart2;
    @BindView(R.id.stabilitySection)
    LinearLayout stabilitySection;
    @BindView(R.id.stressSection)
    LinearLayout stressSection;
    @BindView(R.id.stabilitySection2)
    LinearLayout stabilitySection2;
    @BindView(R.id.stabilitySection3)
    LinearLayout stabilitySection3;
    @BindView(R.id.stability2Section)
    LinearLayout stability2Section;
    @BindView(R.id.stability2Section2)
    LinearLayout stability2Section2;
    @BindView(R.id.ramSection)
    LinearLayout ramSection;
    @BindView(R.id.ram2Section)
    LinearLayout ram2Section;
    @BindView(R.id.ram2Section2)
    LinearLayout ram2Section2;
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

        LinearLayoutManager stabilityManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        stabilityResultsList.setLayoutManager(stabilityManager);

        LinearLayoutManager stressManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        stressResultsList.setLayoutManager(stressManager);

        fetchData();

        fetchGlobalData();
    }

    private void fetchData() {
        User user = User.get(getActivity());
        getPresenter().fetch(user.getId());
    }

    private void fetchGlobalData() {
        getPresenter().fetchGlobal();
    }

    private StatisticsResult sortResults(StatisticsResult result) {
        Collections.sort(result.stressResults);
        Collections.sort(result.stabilityResults);
        Collections.sort(result.ramResults1);
        Collections.sort(result.ramResults2);
        return result;
    }

    private void fillUi(StatisticsResult result) {
        boolean empty = true;

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

        if (result.ramResults1 != null && result.ramResults1.size() > 0) {
            ramSection.setVisibility(View.VISIBLE);
            List<StatisticsResult.RAMResults1> ramResultsList = result.ramResults1;
            List<BarEntry> barEntries = new ArrayList<>();

            for (int i = 0; i < ramResultsList.size(); i++) {
                barEntries.add(new BarEntry(i, (float) ramResultsList.get(i).time));
            }

            BarDataSet barDataSet = new BarDataSet(barEntries, "Время выбора знака");

            BarData barData = new BarData(barDataSet);
            ramChart.setData(barData);
            ramChart.invalidate();

            ramChart.getDescription().setEnabled(false);

            empty = false;
        } else {
            ramSection.setVisibility(View.GONE);
        }

        if (result.ramResults2 != null && result.ramResults2.size() > 0) {
            ram2Section.setVisibility(View.VISIBLE);
            List<StatisticsResult.RAMResults2> ramResultsList = result.ramResults2;
            List<BarEntry> barEntries = new ArrayList<>();

            for (int i = 0; i < ramResultsList.size(); i++) {
                barEntries.add(new BarEntry(i, (float) ramResultsList.get(i).time));
            }

            BarDataSet barDataSet = new BarDataSet(barEntries, "Время выбора знаков");

            BarData barData = new BarData(barDataSet);
            ram2Chart.setData(barData);
            ram2Chart.invalidate();

            ram2Chart.getDescription().setEnabled(false);

            empty = false;
        } else {
            ram2Section.setVisibility(View.GONE);
        }

        if (result.ramResults2 != null && result.ramResults2.size() > 0) {
            ram2Section2.setVisibility(View.VISIBLE);
            List<StatisticsResult.RAMResults2> ramResultsList = result.ramResults2;
            List<BarEntry> barEntries = new ArrayList<>();

            for (int i = 0; i < ramResultsList.size(); i++) {
                barEntries.add(new BarEntry(i, (float) ramResultsList.get(i).wins));
            }

            BarDataSet barDataSet = new BarDataSet(barEntries, "Количество правильно выбранных знаков");

            BarData barData = new BarData(barDataSet);
            ram2Chart2.setData(barData);
            ram2Chart2.invalidate();

            ram2Chart2.getDescription().setEnabled(false);

            empty = false;
        } else {
            ram2Section2.setVisibility(View.GONE);
        }

        if (empty) {
            contentSection.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        } else {
            emptyText.setVisibility(View.GONE);
            contentSection.setVisibility(View.VISIBLE);
        }
    }

    private void fillUiGlobal(StatisticsResult result) {
        if (result.stabilityResults != null && result.stabilityResults.size() > 0) {
            stabilitySection2.setVisibility(View.VISIBLE);
            List<StatisticsResult.StabilityResults> stabilityResultsList = result.stabilityResults;
            List<BarEntry> barEntries = new ArrayList<>();

            for (int i = 0; i < stabilityResultsList.size(); i++) {
                barEntries.add(new BarEntry(i, stabilityResultsList.get(i).errorsValue));
            }

            BarDataSet barDataSet = new BarDataSet(barEntries, "Количество неверных нажатий");

            BarData barData = new BarData(barDataSet);
            stabilityChart2.setData(barData);
            stabilityChart2.invalidate();

            stabilityChart2.getDescription().setEnabled(false);

        } else {
            stabilitySection2.setVisibility(View.GONE);
        }

        if (result.stabilityResults != null && result.stabilityResults.size() > 0) {
            stabilitySection3.setVisibility(View.VISIBLE);
            List<StatisticsResult.StabilityResults> stabilityResultsList = result.stabilityResults;
            List<BarEntry> barEntries = new ArrayList<>();

            for (int i = 0; i < stabilityResultsList.size(); i++) {
                barEntries.add(new BarEntry(i, stabilityResultsList.get(i).misses));
            }

            BarDataSet barDataSet = new BarDataSet(barEntries, "Количество пропусков");

            BarData barData = new BarData(barDataSet);
            stabilityChart3.setData(barData);
            stabilityChart3.invalidate();

            stabilityChart3.getDescription().setEnabled(false);
        } else {
            stabilitySection3.setVisibility(View.GONE);
        }

        if (result.stressResults != null && result.stressResults.size() > 0) {
            stability2Section.setVisibility(View.VISIBLE);
            List<StatisticsResult.StressResults> stabilityResultsList = result.stressResults;
            List<BarEntry> barEntries = new ArrayList<>();

            for (int i = 0; i < stabilityResultsList.size(); i++) {
                barEntries.add(new BarEntry(i, stabilityResultsList.get(i).misses));
            }

            BarDataSet barDataSet = new BarDataSet(barEntries, "Количество пропусков");

            BarData barData = new BarData(barDataSet);
            stability2Chart.setData(barData);
            stability2Chart.invalidate();

            stability2Chart.getDescription().setEnabled(false);
        } else {
            stability2Section.setVisibility(View.GONE);
        }

        if (result.stressResults != null && result.stressResults.size() > 0) {
            stability2Section2.setVisibility(View.VISIBLE);
            List<StatisticsResult.StressResults> stabilityResultsList = result.stressResults;
            List<BarEntry> barEntries = new ArrayList<>();

            for (int i = 0; i < stabilityResultsList.size(); i++) {
                for (Double time : stabilityResultsList.get(i).times) {
                    barEntries.add(new BarEntry(i, time.floatValue()));
                }
            }

            BarDataSet barDataSet = new BarDataSet(barEntries, "Время реакции на сигнал");

            BarData barData = new BarData(barDataSet);
            stability2Chart2.setData(barData);
            stability2Chart2.invalidate();

            stability2Chart2.getDescription().setEnabled(false);
        } else {
            stability2Section2.setVisibility(View.GONE);
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

    public void onSuccessGlobal(StatisticsResult result) {
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
        fillUiGlobal(sortedResult);
    }

    public void onErrorGlobal(Throwable throwable) {
        stopLoading();
        throwable.printStackTrace();
    }
}