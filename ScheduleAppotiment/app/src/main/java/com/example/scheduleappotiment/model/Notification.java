package com.example.scheduleappotiment.model;

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
        "Id",
        "Appointment__c",
        "Appointment__r",
        "Tag__c",
})
public class Notification {

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Appointment__r")
    private NotificationAppointment appointment;

    @JsonProperty("Appointment__c")
    private String appointment_c;

    @JsonProperty("Tag__c")
    private String tag;

    public Notification() {
    }

    @JsonProperty("Id")
    public String getId() {
        return id;
    }

    @JsonProperty("Id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("Appointment__r")
    public NotificationAppointment getAppointment() {
        return appointment;
    }

    @JsonProperty("Appointment__r")
    public void setAppointment(NotificationAppointment appointment) {
        this.appointment = appointment;
    }

    @JsonProperty("Appointment__c")
    public String getAppointment_c() {
        return appointment_c;
    }

    @JsonProperty("Appointment__c")
    public void setAppointment_c(String appointment_c) {
        this.appointment_c = appointment_c;
    }


    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @JsonProperty("Tag__c")
    public String getTag() {
        return tag;
    }

    @JsonProperty("Tag__c")
    public void setTag(String tag) {
        this.tag = tag;
    }


    @JsonPropertyOrder({
            "Id",
            "Status__c",
            "Start_Time__c",
            "End_Time__c",
            "Date__c",
            "Name",
            "Description__c",
            "To_Contact_Name__c",
            "From_Contact_Name__c"
    })
    public class NotificationAppointment {

        @JsonProperty("Id")
        private String id;
        @JsonProperty("Status__c")
        private String statusC;
        @JsonProperty("Start_Time__c")
        private String startTimeC;
        @JsonProperty("End_Time__c")
        private String endTimeC;
        @JsonProperty("Date__c")
        private String dateC;
        @JsonProperty("Name")
        private String name;
        @JsonProperty("Description__c")
        private String descriptionC;
        @JsonProperty("To_Contact_Name__c")
        private String toContactNameC;
        @JsonProperty("From_Contact_Name__c")
        private String fromContactNameC;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        @JsonProperty("Id")
        public String getId() {
            return id;
        }

        @JsonProperty("Id")
        public void setId(String id) {
            this.id = id;
        }

        @JsonProperty("Status__c")
        public String getStatusC() {
            return statusC;
        }

        @JsonProperty("Status__c")
        public void setStatusC(String statusC) {
            this.statusC = statusC;
        }

        @JsonProperty("Start_Time__c")
        public String getStartTimeC() {
            return startTimeC;
        }

        @JsonProperty("Start_Time__c")
        public void setStartTimeC(String startTimeC) {
            this.startTimeC = startTimeC;
        }

        @JsonProperty("End_Time__c")
        public String getEndTimeC() {
            return endTimeC;
        }

        @JsonProperty("End_Time__c")
        public void setEndTimeC(String endTimeC) {
            this.endTimeC = endTimeC;
        }

        @JsonProperty("Date__c")
        public String getDateC() {
            return dateC;
        }

        @JsonProperty("Date__c")
        public void setDateC(String dateC) {
            this.dateC = dateC;
        }

        @JsonProperty("Name")
        public String getName() {
            return name;
        }

        @JsonProperty("Name")
        public void setName(String name) {
            this.name = name;
        }

        @JsonProperty("Description__c")
        public String getDescriptionC() {
            return descriptionC;
        }

        @JsonProperty("Description__c")
        public void setDescriptionC(String descriptionC) {
            this.descriptionC = descriptionC;
        }

        @JsonProperty("To_Contact_Name__c")
        public String getToContactNameC() {
            return toContactNameC;
        }

        @JsonProperty("To_Contact_Name__c")
        public void setToContactNameC(String toContactNameC) {
            this.toContactNameC = toContactNameC;
        }

        @JsonProperty("From_Contact_Name__c")
        public String getFromContactNameC() {
            return fromContactNameC;
        }

        @JsonProperty("From_Contact_Name__c")
        public void setFromContactNameC(String fromContactNameC) {
            this.fromContactNameC = fromContactNameC;
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
