package com.firrael.psychology.view.register;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.firrael.psychology.R;
import com.firrael.psychology.Utils;
import com.firrael.psychology.presenter.RegisterPresenter;
import com.firrael.psychology.view.base.BaseFragment;

import butterknife.BindView;
import nucleus.factory.RequiresPresenter;

/**
 * Created by Railag on 07.11.2016.
 */
@RequiresPresenter(RegisterPresenter.class)
public class RegisterFragment extends BaseFragment<RegisterPresenter> {

    @BindView(R.id.emailEdit)
    EditText emailEdit;
    @BindView(R.id.emailLayout)
    TextInputLayout emailLayout;
    @BindView(R.id.passwordEdit)
    EditText passwordEdit;
    @BindView(R.id.passwordLayout)
    TextInputLayout passwordLayout;
    @BindView(R.id.passwordConfirmEdit)
    EditText passwordConfirmEdit;
    @BindView(R.id.passwordConfirmLayout)
    TextInputLayout passwordConfirmLayout;

    public static RegisterFragment newInstance() {

        Bundle args = new Bundle();

        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.registerTitle);
    }

    @Override
    protected int getViewId() {
        return R.layout.fragment_register;
    }

    @Override
    protected void initView(View v) {
        getMainActivity().blueTop();

        Utils.showKeyboard(getActivity(), emailEdit);

        passwordConfirmEdit.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {

                emailLayout.setError(null);
                passwordLayout.setError(null);
                passwordConfirmLayout.setError(null);

                if (TextUtils.isEmpty(emailEdit.getText().toString())) {
                    emailLayout.setError(getString(R.string.emptyError));
                    return false;
                }

                if (TextUtils.isEmpty(passwordEdit.getText().toString())) {
                    passwordLayout.setError(getString(R.string.emptyError));
                    return false;
                }

                if (TextUtils.isEmpty(passwordConfirmEdit.getText().toString())) {
                    passwordConfirmLayout.setError(getString(R.string.emptyError));
                    return false;
                }

                if (!passwordEdit.getText().toString().equals(passwordConfirmEdit.getText().toString())) {
                    passwordConfirmLayout.setError(getString(R.string.confirmPassError));
                    return false;
                }

                getPresenter().save(emailEdit.getText().toString(), passwordEdit.getText().toString());
                getMainActivity().toAgeScreen();

                return true;
            } else {
                return false;
            }
        });
    }
}
