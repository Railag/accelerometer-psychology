package com.firrael.psychology.view.register;

import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import com.firrael.psychology.R;
import com.firrael.psychology.model.User;
import com.firrael.psychology.model.UserResult;
import com.firrael.psychology.presenter.TimePresenter;
import com.firrael.psychology.view.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;

/**
 * Created by Railag on 07.11.2016.
 */
@RequiresPresenter(TimePresenter.class)
public class TimeFragment extends BaseFragment<TimePresenter> {


    @BindView(R.id.timePicker)
    NumberPicker picker;

    public static TimeFragment newInstance() {

        Bundle args = new Bundle();

        TimeFragment fragment = new TimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.timeTitle);
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_time;
    }

    @Override
    protected void initView(View v) {
        picker.setMinValue(0);
        picker.setMaxValue(24);
        picker.setValue(2);
    }

    @OnClick(R.id.saveButton)
    public void save() {
        getPresenter().save(String.valueOf(picker.getValue()));

        startLoading();
        User user = User.get(getActivity());
        getPresenter().request(user.getLogin(), user.getPassword(), user.getEmail(), user.getAge(), user.getTime());
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

    public void onError(Throwable error) {
        error.printStackTrace();
        stopLoading();
        toast(error.getMessage());
    }
}
