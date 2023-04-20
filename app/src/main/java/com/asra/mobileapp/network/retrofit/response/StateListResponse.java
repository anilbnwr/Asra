package com.asra.mobileapp.network.retrofit.response;

import com.asra.mobileapp.model.State;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StateListResponse extends ETResponse {

    @SerializedName("result")
    @Expose
    public List<State> stateList;


}
