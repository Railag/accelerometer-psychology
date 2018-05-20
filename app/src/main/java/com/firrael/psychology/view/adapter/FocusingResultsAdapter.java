package com.firrael.psychology.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firrael.psychology.R;
import com.firrael.psychology.model.StatisticsResult;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
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

public class FocusingResultsAdapter extends RecyclerView.Adapter<FocusingResultsAdapter.ViewHolder> {

    private List<StatisticsResult.FocusingResults> allResults = new ArrayList<>();

    public void setAllResults(List<StatisticsResult.FocusingResults> allResults) {
        this.allResults = allResults;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_focusing_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StatisticsResult.FocusingResults results = allResults.get(position);

        List<Entry> lineEntries = new ArrayList<>();
        List<BarEntry> barEntries = new ArrayList<>();

        for (int i = 0; i < results.times.size(); i++) {
            lineEntries.add(new Entry(i, results.times.get(i).floatValue()));
            barEntries.add(new BarEntry(i, results.errorValues.get(i)));
        }

        LineDataSet dataSet = new LineDataSet(lineEntries, "Время");
        //dataSet.setColor(...);
        //dataSet.setValueTextColor(...);

        LineData lineData = new LineData(dataSet);
        holder.chart1.setData(lineData);
        holder.chart1.invalidate();

        holder.chart1.getDescription().setEnabled(false);


        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Количество неверных при ошибке");

        BarData barData = new BarData(barDataSet);
        holder.chart2.setData(barData);
        holder.chart2.invalidate();

        holder.chart2.getDescription().setEnabled(false);

    }

    @Override
    public int getItemCount() {
        return allResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.chart1)
        LineChart chart1;

        @BindView(R.id.chart2)
        BarChart chart2;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
