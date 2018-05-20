
package com.firrael.psychology.model.english;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Frequency {

    @SerializedName("zipf")
    @Expose
    private Float zipf;
    @SerializedName("perMillion")
    @Expose
    private Float perMillion;
    @SerializedName("diversity")
    @Expose
    private Integer diversity;

    public Float getZipf() {
        return zipf;
    }

    public void setZipf(Float zipf) {
        this.zipf = zipf;
    }

    public Float getPerMillion() {
        return perMillion;
    }

    public void setPerMillion(Float perMillion) {
        this.perMillion = perMillion;
    }

    public Integer getDiversity() {
        return diversity;
    }

    public void setDiversity(Integer diversity) {
        this.diversity = diversity;
    }

}
