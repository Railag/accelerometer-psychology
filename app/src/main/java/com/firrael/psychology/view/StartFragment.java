package com.firrael.psychology.view;

import android.os.Bundle;
import android.view.View;

import com.firrael.psychology.R;
import com.firrael.psychology.view.base.SimpleFragment;

import butterknife.OnClick;

/**
 * Created by Railag on 31.05.2016.
 */

public class StartFragment extends SimpleFragment {

    public static StartFragment newInstance() {

        Bundle args = new Bundle();

        StartFragment fragment = new StartFragment();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.login);
    }

    @Override
    protected void initView(View v) {
        getMainActivity().transparentStatusBar();
        getMainActivity().hideToolbar();
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_start;
    }

    @OnClick(R.id.loginButton)
    public void login() {
        getMainActivity().toLogin();
    }

    @OnClick(R.id.createAccountButton)
    public void createAccount() {
        getMainActivity().toNameScreen();
    }
}