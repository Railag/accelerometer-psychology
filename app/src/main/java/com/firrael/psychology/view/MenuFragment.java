package com.firrael.psychology.view;

import android.os.Bundle;
import android.view.View;

import com.firrael.psychology.R;
import com.firrael.psychology.view.base.SimpleFragment;

import butterknife.OnClick;

/**
 * Created by Railag on 21.03.2017.
 */

public class MenuFragment extends SimpleFragment {

    public static MenuFragment newInstance() {

        Bundle args = new Bundle();

        MenuFragment fragment = new MenuFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.app_name);
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_menu;
    }

    @Override
    protected void initView(View v) {
        getMainActivity().blueTop();
        getMainActivity().toggleArrow(false);
        getMainActivity().toggleExit(true);
    }


    @OnClick({R.id.testsButton, R.id.statisticsButton, R.id.profileButton, R.id.settingsButton})
    public void onViewClicked(View view) {
        getMainActivity().toggleArrow(true);
        getMainActivity().toggleExit(false);

        switch (view.getId()) {
            case R.id.testsButton:
                getMainActivity().toTests();
                break;
            case R.id.statisticsButton:
                getMainActivity().toStatistics();
                break;
            case R.id.profileButton:
                getMainActivity().toInfo();
                break;
            case R.id.settingsButton:
                getMainActivity().toSettings();
                break;
        }
    }
}
