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
                return R.drawable.sign1;
            case TYPE2:
                return R.drawable.sign2;
            case TYPE3:
                return R.drawable.sign3;
            case TYPE4:
                return R.drawable.sign4;
            case TYPE5:
                return R.drawable.sign5;
            case TYPE6:
                return R.drawable.sign6;
            case TYPE7:
                return R.drawable.sign7;
            case TYPE8:
                return R.drawable.sign8;
            case TYPE9:
                return R.drawable.sign9;
            case TYPE10:
                return R.drawable.sign10;
            case TYPE11:
                return R.drawable.sign11;
            case TYPE12:
                return R.drawable.sign12;
            case TYPE13:
                return R.drawable.sign13;
            case TYPE14:
                return R.drawable.sign14;
            case TYPE15:
                return R.drawable.sign15;
            case TYPE16:
                return R.drawable.sign16;
            case TYPE17:
                return R.drawable.sign17;
            case TYPE18:
                return R.drawable.sign18;
            case TYPE19:
                return R.drawable.sign19;
            case TYPE20:
                return R.drawable.sign20;
            default:
                return R.drawable.sign1;
        }
    }

    private boolean wasShown;
    private boolean selected;
    private boolean chosen;

    public boolean wasShown() {
        return wasShown;
    }

    private int counter;

    public void increase() {
        counter++;
    }

    public int getCounter() {
        return counter;
    }

    public void setShown(boolean shown) {
        wasShown = shown;
    }

    public void reset() {
        wasShown = false;
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

    public static List<Sign> randomSigns(int size) {
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
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isChosen() {
        return chosen;
    }

    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }
}

