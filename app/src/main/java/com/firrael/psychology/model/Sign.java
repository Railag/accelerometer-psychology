package com.firrael.psychology.model;

import com.firrael.psychology.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by railag on 21.05.2018.
 */
public enum Sign {
    TYPE1,
    TYPE2,
    TYPE3,
    TYPE4,
    TYPE5,
    TYPE6,
    TYPE7,
    TYPE8,
    TYPE9,
    TYPE10,
    TYPE11,
    TYPE12,
    TYPE13,
    TYPE14,
    TYPE15,
    TYPE16,
    TYPE17,
    TYPE18,
    TYPE19,
    TYPE20;

    public int getDrawableId() {
        switch (this) {
            case TYPE1:
                return R.drawable.complex_button_grey;
            case TYPE2:
                return R.drawable.complex_button_yellow;
            case TYPE3:
                return R.drawable.complex_button_red;
            case TYPE4:
                return R.drawable.complex_button_green;
            case TYPE5:
                return R.drawable.complex_button_green;
            case TYPE6:
                return R.drawable.complex_button_green;
            case TYPE7:
                return R.drawable.complex_button_green;
            case TYPE8:
                return R.drawable.complex_button_green;
            case TYPE9:
                return R.drawable.complex_button_green;
            case TYPE10:
                return R.drawable.complex_button_green;
            case TYPE11:
                return R.drawable.complex_button_green;
            case TYPE12:
                return R.drawable.complex_button_green;
            case TYPE13:
                return R.drawable.complex_button_green;
            case TYPE14:
                return R.drawable.complex_button_green;
            case TYPE15:
                return R.drawable.complex_button_green;
            case TYPE16:
                return R.drawable.complex_button_green;
            case TYPE17:
                return R.drawable.complex_button_green;
            case TYPE18:
                return R.drawable.complex_button_green;
            case TYPE19:
                return R.drawable.complex_button_green;
            case TYPE20:
                return R.drawable.complex_button_green;
            default:
                return R.drawable.complex_button_grey;
        }
    }

    private int counter;

    private boolean isSelected;

    public int getCounter() {
        return counter;
    }

    public void increase() {
        counter++;
    }

    public void reset() {
        counter = 0;
    }

    public static List<Sign> randomSigns(int min, int max) {
        int size = random.nextInt((max - min) + 1) + min;

        List<Sign> signs = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Sign sign = randomEnum(Sign.class);
            if (!signs.contains(sign)) {
                signs.add(sign);
            } else {
                i--;
            }
        }
        return signs;
    }

    private static final Random random = new Random();

    private static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

