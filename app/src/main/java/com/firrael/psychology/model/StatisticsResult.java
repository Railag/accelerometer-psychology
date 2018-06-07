package com.firrael.psychology.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Railag on 04.05.2017.
 */

public class StatisticsResult extends Result {

    @SerializedName("reaction_results")
    public List<ReactionResults> reactionResults;
    @SerializedName("complex_results")
    public List<ComplexResults> complexResults;
    @SerializedName("volume_results")
    public List<VolumeResults> volumeResults;
    @SerializedName("focusing_results")
    public List<FocusingResults> focusingResults;
    @SerializedName("stability_results")
    public List<StabilityResults> stabilityResults;
    @SerializedName("stress_results")
    public List<StressResults> stressResults;
    @SerializedName("english_results")
    public List<EnglishResults> englishResults;
    @SerializedName("ram_volume_results")
    public List<RAMResults1> ramResults1;
    @SerializedName("attention_volume_results")
    public List<RAMResults2> ramResults2;

    public static class ReactionResults implements Comparable<ReactionResults> {
        @SerializedName("id")
        public int id;
        @SerializedName("times")
        public List<Double> times;

        @Override
        public int compareTo(@NonNull ReactionResults reactionResults) {
            return Integer.valueOf(id).compareTo(reactionResults.id);
        }
    }

    public static class ComplexResults implements Comparable<ComplexResults> {
        @SerializedName("id")
        public int id;
        @SerializedName("wins")
        public int wins;
        @SerializedName("fails")
        public int fails;
        @SerializedName("misses")
        public int misses;

        @Override
        public int compareTo(@NonNull ComplexResults complexResults) {
            return Integer.valueOf(id).compareTo(complexResults.id);
        }
    }

    public static class VolumeResults implements Comparable<VolumeResults> {
        @SerializedName("id")
        public int id;
        @SerializedName("wins")
        public int wins;
        @SerializedName("fails")
        public int fails;
        @SerializedName("misses")
        public int misses;

        @Override
        public int compareTo(@NonNull VolumeResults volumeResults) {
            return Integer.valueOf(id).compareTo(volumeResults.id);
        }
    }

    public static class FocusingResults implements Comparable<FocusingResults> {
        @SerializedName("id")
        public int id;
        @SerializedName("times")
        public List<Double> times;
        @SerializedName("error_values")
        public List<Integer> errorValues;

        @Override
        public int compareTo(@NonNull FocusingResults focusingResults) {
            return Integer.valueOf(id).compareTo(focusingResults.id);
        }
    }

    public static class StabilityResults implements Comparable<StabilityResults> {
        @SerializedName("id")
        public int id;
        @SerializedName("times")
        public List<Double> times;
        @SerializedName("errors_value")
        public int errorsValue;
        @SerializedName("misses")
        public int misses;

        @Override
        public int compareTo(@NonNull StabilityResults stabilityResults) {
            return Integer.valueOf(id).compareTo(stabilityResults.id);
        }
    }

    public static class StressResults implements Comparable<StressResults> {
        @SerializedName("id")
        public int id;
        @SerializedName("misses")
        public int misses;
        @SerializedName("times")
        public List<Double> times;

        @Override
        public int compareTo(@NonNull StressResults stressResults) {
            return Integer.valueOf(id).compareTo(stressResults.id);
        }
    }

    public static class EnglishResults implements Comparable<EnglishResults> {
        @SerializedName("id")
        public int id;
        @SerializedName("errors_value")
        public int errorsValue;
        @SerializedName("times")
        public List<Double> times;
        @SerializedName("words")
        public List<String> words;

        @Override
        public int compareTo(@NonNull EnglishResults englishResults) {
            return Integer.valueOf(id).compareTo(englishResults.id);
        }
    }

    public static class RAMResults1 implements Comparable<RAMResults1> {
        @SerializedName("id")
        public int id;
        @SerializedName("time")
        public double time;
        @SerializedName("wins")
        public long wins;

        @Override
        public int compareTo(@NonNull RAMResults1 ramResults) {
            return Integer.valueOf(id).compareTo(ramResults.id);
        }
    }

    public static class RAMResults2 implements Comparable<RAMResults2> {
        @SerializedName("id")
        public int id;
        @SerializedName("time")
        public double time;
        @SerializedName("wins")
        public long wins;

        @Override
        public int compareTo(@NonNull RAMResults2 ramResults) {
            return Integer.valueOf(id).compareTo(ramResults.id);
        }
    }
}