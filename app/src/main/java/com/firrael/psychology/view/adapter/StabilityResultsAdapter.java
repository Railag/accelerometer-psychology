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

public class StabilityResultsAdapter extends RecyclerView.Adapter<StabilityResultsAdapter.ViewHolder> {

    private List<StatisticsResult.StabilityResults> allResults = new ArrayList<>();

    public void setAllResults(List<StatisticsResult.StabilityResults> allResults) {
        this.allResults = allResults;
        notifyDataSetChanged();
    }

    @Override
    public StabilityResultsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_stability_result, parent, false);
        return new StabilityResultsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StabilityResultsAdapter.ViewHolder holder, int position) {
        StatisticsResult.StabilityResults results = allResults.get(position);

        List<Entry> lineEntries = new ArrayList<>();

        for (int i = 0; i < results.times.size(); i++) {
            lineEntries.add(new Entry(i, results.times.get(i).floatValue()));
        }

        LineDataSet dataSet = new LineDataSet(lineEntries, "Время");

        LineData lineData = new LineData(dataSet);
        holder.chart1.setData(lineData);
        holder.chart1.invalidate();

        holder.chart1.getDescription().setEnabled(false);

        holder.errorsCount.setText(String.valueOf(results.errorsValue));
        holder.missesCount.setText(String.valueOf(results.misses));
    }

    @Override
    public int getItemCount() {
        return allResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.chart1)
        LineChart chart1;

        @BindView(R.id.errorsCount)
        TextView errorsCount;

        @BindView(R.id.missesCount)
        TextView missesCount;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
