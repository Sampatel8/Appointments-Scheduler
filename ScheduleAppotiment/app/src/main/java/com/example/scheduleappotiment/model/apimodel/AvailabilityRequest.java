package com.example.scheduleappotiment.model.apimodel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "avails"
})
public class AvailabilityRequest {

    public AvailabilityRequest() {
    }

    @JsonProperty("avails")
    private List<Availability> mlist;

    @JsonProperty("avails")
    public List<Availability> getMlist() {
        return mlist;
    }

    @JsonProperty("avails")
    public void setMlist(List<Availability> mlist) {
        this.mlist = mlist;
    }
}
