package com.example.scheduleappotiment.model.apimodel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "userId"
})
public class LoginApiModel {

    public LoginApiModel() {
    }

    public LoginApiModel(java.lang.String userId) {
        this.userId = userId;
    }

    @JsonProperty("userId")
    private java.lang.String userId;

    @JsonProperty("userId")
    public java.lang.String getUserId() {
        return userId;
    }

    @JsonProperty("userId")
    public void setUserId(java.lang.String userId) {
        this.userId = userId;
    }

}
