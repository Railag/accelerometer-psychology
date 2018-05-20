package com.firrael.psychology.view.results;

import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firrael.psychology.R;
import com.firrael.psychology.Utils;
import com.firrael.psychology.model.Answer;
import com.firrael.psychology.view.base.SimpleFragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Railag on 19.03.2017.
 */

public class FocusingResultsFragment extends SimpleFragment implements ResultScreen {

    public final static String LINES = "lines";
    public final static String ERRORS = "errors";
    public final static String RESULTS = "results";

    public static FocusingResultsFragment newInstance(Bundle args) {

        FocusingResultsFragment fragment = new FocusingResultsFragment();
        fragment.setHasOptionsMenu(true);
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.linesCount)
    TextView linesCount;

    @BindView(R.id.errorsCount)
    TextView errorsCount;

    @BindView(R.id.chart1)
    LineChart chart1;

    @BindView(R.id.chart2)
    BarChart chart2;

    @Override
    protected String getTitle() {
        return getString(R.string.resultsTitle);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.results, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
            default:
                save();
        }

        return super.onOptionsItemSelected(item);
    }

    private void save() {
        PrintAttributes attributes = new PrintAttributes.Builder()
                .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
                .setMediaSize(PrintAttributes.MediaSize.ISO_A2.asLandscape())
                .setResolution(new PrintAttributes.Resolution("Focusing results", "Focusing results", 300, 300))
                .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                .build();

        PrintedPdfDocument document = new PrintedPdfDocument(getActivity(),
                attributes);

        PdfDocument.Page page = document.startPage(0);

        View content = getView();
        content.draw(page.getCanvas());

        document.finishPage(page);
// add more pages

// write the document content
        if (!canWriteOnExternalStorage() || !Utils.canWrite(getActivity())) {
            // TODO
            return;
        }

        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/psychology");
        dir.mkdir();

        File newFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/psychology/focusing_results.pdf");

        String name = newFile.getAbsolutePath();

        try {
            OutputStream stream =
                    new FileOutputStream(newFile, true);
            document.writeTo(stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        document.close();

        Toast.makeText(getActivity(), "Результаты сохранены в  " + name, Toast.LENGTH_SHORT).show();
    }

    public static boolean canWriteOnExternalStorage() {
        // get the state of your external storage
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // if storage is mounted return true
            return true;
        }
        return false;
    }

    @Override
    protected int getViewId() {
        return R.layout.results_focusing_layout;
    }

    @Override
    protected void initView(View v) {
        Bundle args = getArguments();

        Utils.verifyStoragePermissions(getActivity());

        if (args != null) {
            if (args.containsKey(LINES)) {
                linesCount.setText(String.valueOf(args.getInt(LINES)));
            }

            if (args.containsKey(ERRORS)) {
                errorsCount.setText(String.valueOf(args.getInt(ERRORS)));
            }

            if (args.containsKey(RESULTS)) {
                ArrayList<Answer> results = args.getParcelableArrayList(RESULTS);

                List<Entry> lineEntries = new ArrayList<>();
                List<BarEntry> barEntries = new ArrayList<>();

                for (Answer result : results) {
                    lineEntries.add(new Entry(result.getNumber(), (float) result.getTime()));
                    barEntries.add(new BarEntry(result.getNumber(), result.getErrorValue()));
                }

                LineDataSet dataSet = new LineDataSet(lineEntries, "Время");
                //dataSet.setColor(...);
                //dataSet.setValueTextColor(...);

                LineData lineData = new LineData(dataSet);
                chart1.setData(lineData);
                chart1.invalidate();

                chart1.getDescription().setEnabled(false);


                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                BarDataSet barDataSet = new BarDataSet(barEntries, "Количество неверных при ошибке");

                BarData barData = new BarData(barDataSet);
                chart2.setData(barData);
                chart2.invalidate();

                chart2.getDescription().setEnabled(false);

            }


        }
    }
}
