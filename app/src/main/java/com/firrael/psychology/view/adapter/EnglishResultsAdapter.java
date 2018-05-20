package com.firrael.psychology.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
 * Created by railag on 14.02.2018.
 */

public class EnglishResultsAdapter extends RecyclerView.Adapter<EnglishResultsAdapter.ViewHolder> {

    private List<StatisticsResult.EnglishResults> allResults = new ArrayList<>();

    public void setAllResults(List<StatisticsResult.EnglishResults> allResults) {
        this.allResults = allResults;
        notifyDataSetChanged();
    }

    @Override
    public EnglishResultsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_english_result, parent, false);
        return new EnglishResultsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EnglishResultsAdapter.ViewHolder holder, int position) {
        StatisticsResult.EnglishResults results = allResults.get(position);

        List<Entry> lineEntries = new ArrayList<>();

        for (int i = 0; i < results.times.size(); i++) {
            lineEntries.add(new Entry(i, results.times.get(i).floatValue()));
        }

        LineDataSet dataSet = new LineDataSet(lineEntries, "Время");
        //dataSet.setColor(...);
        //dataSet.setValueTextColor(...);

        LineData lineData = new LineData(dataSet);
        holder.chart1.setData(lineData);
        holder.chart1.invalidate();

        holder.chart1.getDescription().setEnabled(false);

        List<String> words = results.words;
        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            builder.append(word);
            builder.append(" | ");
        }

        holder.wordsList.setText(builder.toString());

    }

    @Override
    public int getItemCount() {
        return allResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.chart1)
        LineChart chart1;

        @BindView(R.id.wordsList)
        TextView wordsList;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
