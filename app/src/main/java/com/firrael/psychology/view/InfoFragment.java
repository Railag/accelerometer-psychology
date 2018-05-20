package com.firrael.psychology.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firrael.psychology.R;
import com.firrael.psychology.Utils;
import com.firrael.psychology.model.User;
import com.firrael.psychology.model.UserResult;
import com.firrael.psychology.presenter.InfoPresenter;
import com.firrael.psychology.view.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;

/**
 * Created by Railag on 21.03.2017.
 */

@RequiresPresenter(InfoPresenter.class)
public class InfoFragment extends BaseFragment<InfoPresenter> {

    @BindView(R.id.login)
    LinearLayout login;
    @BindView(R.id.age)
    LinearLayout age;
    @BindView(R.id.time)
    LinearLayout time;
    @BindView(R.id.displayLayout)
    LinearLayout displayLayout;
    @BindView(R.id.loginEdit)
    TextView loginEdit;
    @BindView(R.id.ageEdit)
    EditText ageEdit;
    @BindView(R.id.timeEdit)
    EditText timeEdit;
    @BindView(R.id.editLayout)
    LinearLayout editLayout;
    @BindView(R.id.loginText)
    TextView loginText;
    @BindView(R.id.ageText)
    TextView ageText;
    @BindView(R.id.timeText)
    TextView timeText;
    @BindView(R.id.editButton)
    FloatingActionButton editButton;

    public static InfoFragment newInstance() {

        Bundle args = new Bundle();

        InfoFragment fragment = new InfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.info);
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_info;
    }

    @Override
    protected void initView(View v) {
        getMainActivity().blueTop();

        User user = User.get(getActivity());

        loginText.setText(user.getLogin());
        timeText.setText(String.valueOf(user.getTime()));
        ageText.setText(String.valueOf(user.getAge()));

        loginEdit.setText(user.getLogin());
        timeEdit.setText(String.valueOf(user.getTime()));
        ageEdit.setText(String.valueOf(user.getAge()));
    }


    @OnClick(R.id.editButton)
    public void editMode() {
        displayLayout.setVisibility(View.GONE);
        editLayout.setVisibility(View.VISIBLE);
        editButton.setVisibility(View.GONE);
    }

    @OnClick(R.id.saveButton)
    public void save() {
        String time = timeEdit.getText().toString();
        String age = ageEdit.getText().toString();
        String login = loginEdit.getText().toString();

        Utils.hideKeyboard(getActivity());
        startLoading();

        long userId = User.get(getActivity()).getId();

        getPresenter().save(userId, login, time, age);
    }

    public void onSuccess(UserResult result) {
        stopLoading();
        if (result == null) {
            onError(new IllegalArgumentException());
            return;
        }
        if (result.invalid()) {
            toast(result.error);
            return;
        }

        User.save(result, getActivity());

        getMainActivity().toMenu();
    }

    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }
}
