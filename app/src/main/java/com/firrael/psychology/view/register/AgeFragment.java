package com.firrael.psychology.view.register;

import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import com.firrael.psychology.R;
import com.firrael.psychology.presenter.AgePresenter;
import com.firrael.psychology.view.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;

/**
 * Created by Railag on 07.11.2016.
 */
@RequiresPresenter(AgePresenter.class)
public class AgeFragment extends BaseFragment<AgePresenter> {

    @BindView(R.id.agePicker)
    NumberPicker picker;

    public static AgeFragment newInstance() {

        Bundle args = new Bundle();

        AgeFragment fragment = new AgeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.ageTitle);
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_age;
    }

    @Override
    protected void initView(View v) {
        picker.setMinValue(1900);
        picker.setMaxValue(2017);
        picker.setValue(1990);
    }

    @OnClick(R.id.saveButton)
    public void save() {
        getPresenter().save(String.valueOf(picker.getValue()));
        getMainActivity().toTimeScreen();
    }

}
