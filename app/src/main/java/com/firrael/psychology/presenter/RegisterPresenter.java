package com.firrael.psychology.presenter;

import com.firrael.psychology.App;
import com.firrael.psychology.model.User;
import com.firrael.psychology.view.register.RegisterFragment;

import icepick.State;

/**
 * Created by Railag on 07.11.2016.
 */

public class RegisterPresenter extends BasePresenter<RegisterFragment> {

    @State
    String name;

    @State
    String email;

    @State
    String password;

    public void save(String email, String password) {
        this.name = email;
        this.email = email;
        this.password = password;
        User.get(App.getMainActivity()).setLogin(name);
        User.get(App.getMainActivity()).setEmail(email);
        User.get(App.getMainActivity()).setPassword(password);
    }
}