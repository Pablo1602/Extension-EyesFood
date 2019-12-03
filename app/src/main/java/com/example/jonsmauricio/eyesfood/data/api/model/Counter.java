package com.example.jonsmauricio.eyesfood.data.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by JonásMauricio on 16-11-2017.
 */

public class Counter {
    @SerializedName("COUNT")
    private int count;

    public Counter(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}


