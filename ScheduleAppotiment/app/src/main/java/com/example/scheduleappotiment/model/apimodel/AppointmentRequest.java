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
        "Description__c",
        "From_Contact__c",
        "Name",
        "Slot__c",
        "Status__c",
        "To_Contact__c",
        "Id"
})

public class AppointmentRequest {


    @JsonProperty("Description__c")
    private String descriptionC;
    @JsonProperty("From_Contact__c")
    private String fromContactC;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Slot__c")
    private String slotC;
    @JsonProperty("Status__c")
    private String statusC;
    @JsonProperty("To_Contact__c")
    private String toContactC;
    @JsonProperty("Id")
    private String Id;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public AppointmentRequest() {
    }

    @JsonProperty("Description__c")
    public String getDescriptionC() {
        return descriptionC;
    }

    @JsonProperty("Description__c")
    public void setDescriptionC(String descriptionC) {
        this.descriptionC = descriptionC;
    }

    @JsonProperty("From_Contact__c")
    public String getFromContactC() {
        return fromContactC;
    }

    @JsonProperty("From_Contact__c")
    public void setFromContactC(String fromContactC) {
        this.fromContactC = fromContactC;
    }

    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }


    @JsonProperty("Slot__c")
    public String getSlotC() {
        return slotC;
    }

    @JsonProperty("Slot__c")
    public void setSlotC(String slotC) {
        this.slotC = slotC;
    }

    @JsonProperty("Status__c")
    public String getStatusC() {
        return statusC;
    }

    @JsonProperty("Status__c")
    public void setStatusC(String statusC) {
        this.statusC = statusC;
    }

    @JsonProperty("To_Contact__c")
    public String getToContactC() {
        return toContactC;
    }

    @JsonProperty("To_Contact__c")
    public void setToContactC(String toContactC) {
        this.toContactC = toContactC;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @JsonProperty("Id")
    public String getId() {
        return Id;
    }

    @JsonProperty("Id")
    public void setId(String id) {
        Id = id;
    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "appointment"
    })
    public static class Request {

        @JsonProperty("appointment")
        private AppointmentRequest appointment;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public Request() {
        }

        @JsonProperty("appointment")
        public AppointmentRequest getAppointment() {
            return appointment;
        }

        @JsonProperty("appointment")
        public void setAppointment(AppointmentRequest appointment) {
            this.appointment = appointment;
        }

        @JsonAnyGetter
        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        @JsonAnySetter
        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

    }

}
