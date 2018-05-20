package com.firrael.psychology.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Railag on 19.03.2017.
 */

public class Answer implements Parcelable {
    private int number;
    private double time;
    private int errorValue;


    public Answer() {
    }

    protected Answer(Parcel in) {
        number = in.readInt();
        time = in.readDouble();
        errorValue = in.readInt();
    }

    public static final Creator<Answer> CREATOR = new Creator<Answer>() {
        @Override
        public Answer createFromParcel(Parcel in) {
            return new Answer(in);
        }

        @Override
        public Answer[] newArray(int size) {
            return new Answer[size];
        }
    };

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public int getErrorValue() {
        return errorValue;
    }

    public void setErrorValue(int errorValue) {
        this.errorValue = errorValue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(number);
        parcel.writeDouble(time);
        parcel.writeInt(errorValue);
    }
}
