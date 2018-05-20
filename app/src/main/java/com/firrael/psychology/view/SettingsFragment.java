package com.firrael.psychology.view;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.firrael.psychology.App;
import com.firrael.psychology.R;
import com.firrael.psychology.model.Difficulty;
import com.firrael.psychology.view.base.SimpleFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Railag on 02.05.2017.
 */

public class SettingsFragment extends SimpleFragment {

    public static SettingsFragment newInstance() {

        Bundle args = new Bundle();

        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.radioEasy)
    RadioButton radioEasy;
    @BindView(R.id.radioMedium)
    RadioButton radioMedium;
    @BindView(R.id.radioHard)
    RadioButton radioHard;

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;


    Difficulty current;

    @Override
    protected String getTitle() {
        return getString(R.string.settings);
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_settings;
    }

    @Override
    protected void initView(View v) {

        current = App.diff(getActivity());
        switch (current) {
            case EASY:
                radioGroup.check(R.id.radioEasy);
                break;
            case MEDIUM:
                radioGroup.check(R.id.radioMedium);
                break;
            case HARD:
                radioGroup.check(R.id.radioHard);
                break;
            default:
                radioGroup.check(R.id.radioMedium);
                break;
        }

        super.initView(v);
    }

    @OnClick(R.id.saveButton)
    void save() {

        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.radioEasy:
                current = Difficulty.EASY;
                break;
            case R.id.radioMedium:
                current = Difficulty.MEDIUM;
                break;
            case R.id.radioHard:
                current = Difficulty.HARD;
                break;
            default:
                break;
        }

        App.setDiff(getActivity(), current);
        getMainActivity().toMenu();
    }
}
