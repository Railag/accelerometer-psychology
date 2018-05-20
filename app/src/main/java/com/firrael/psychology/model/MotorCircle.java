package com.firrael.psychology.model;

import com.firrael.psychology.R;

import java.util.Random;

/**
 * Created by Railag on 19.04.2017.
 */

public enum MotorCircle {
    GREY,
    YELLOW,
    RED,
    GREEN;

    public int getDrawableId() {
        switch (this) {
            case GREY:
                return R.drawable.complex_button_grey;
            case YELLOW:
                return R.drawable.complex_button_yellow;
            case RED:
                return R.drawable.complex_button_red;
            case GREEN:
                return R.drawable.complex_button_green;
            default:
                return R.drawable.complex_button_grey;
        }
    }

    public static MotorCircle random() {
        return randomEnum(MotorCircle.class);
    }

    private static final Random random = new Random();

    private static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }
}
