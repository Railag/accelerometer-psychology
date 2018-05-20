package com.firrael.psychology.model;

/**
 * Created by Railag on 25.01.2017.
 */

public class Result {
    public String error;
    public String result;

    public boolean invalid() {
        return error != null;
    }
}
