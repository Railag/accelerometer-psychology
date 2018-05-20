package com.firrael.psychology.model;

/**
 * Created by Railag on 07.11.2016.
 */

public enum Level {

    USUAL, IMPROVED, PROFESSIONAL;

    public Level calculate(int age, int hours) {
        int result = age * hours; // TODO replace
        if (result >= PROFESSIONAL_VALUE) {
            return PROFESSIONAL;
        } else if (result >= IMPROVED_VALUE) {
            return IMPROVED;
        }

        return USUAL;
    }

    private final static int USUAL_VALUE = 25;
    private final static int IMPROVED_VALUE = 45;
    private final static int PROFESSIONAL_VALUE = 70;
    }
