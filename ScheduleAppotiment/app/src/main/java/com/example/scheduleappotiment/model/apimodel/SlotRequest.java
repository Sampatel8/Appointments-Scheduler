package com.example.scheduleappotiment.model.apimodel;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "contactId",
        "appDate"
})
public class SlotRequest {

    @JsonProperty("contactId")
    private String contactId;

    @JsonProperty("appDate")
    private String appDate;

    @JsonIgnore
    private Map<java.lang.String, Object> additionalProperties = new HashMap<String, Object>();

    public SlotRequest() {
    }

    @JsonProperty("contactId")
    public String getContactId() {
        return contactId;
    }

    @JsonProperty("contactId")
    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    @JsonProperty("appDate")
    public String getAppDate() {
        return appDate;
    }

    @JsonProperty("appDate")
    public void setAppDate(String appDate) {
        this.appDate = appDate;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(java.lang.String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
