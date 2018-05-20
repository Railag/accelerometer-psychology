package com.firrael.psychology.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firrael.psychology.R;
import com.firrael.psychology.model.StatisticsResult;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Railag on 04.05.2017.
 */

public class StressResultsAdapter extends RecyclerView.Adapter<StressResultsAdapter.ViewHolder> {

    private List<StatisticsResult.StressResults> allResults = new ArrayList<>();

    public void setAllResults(List<StatisticsResult.StressResults> allResults) {
        this.allResults = allResults;
        notifyDataSetChanged();
    }

    @Override
    public StressResultsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_stress_result, parent, false);
        return new StressResultsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StressResultsAdapter.ViewHolder holder, int position) {
        StatisticsResult.StressResults results = allResults.get(position);

        List<Entry> lineEntries = new ArrayList<>();

        for (int i = 0; i < results.times.size(); i++) {
            lineEntries.add(new Entry(i, results.times.get(i).floatValue()));
        }

        LineDataSet dataSet = new LineDataSet(lineEntries, "Время");

        LineData lineData = new LineData(dataSet);
        holder.chart1.setData(lineData);
        holder.chart1.invalidate();

        holder.chart1.getDescription().setEnabled(false);

        holder.missesCount.setText(String.valueOf(results.misses));
    }

    @Override
    public int getItemCount() {
        return allResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.chart1)
        LineChart chart1;

        @BindView(R.id.missesCount)
        TextView missesCount;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
