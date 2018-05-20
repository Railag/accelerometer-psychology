
package com.firrael.psychology.model.english;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RhymePatterns {

    @SerializedName("all")
    @Expose
    private String all;

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
    }

}
