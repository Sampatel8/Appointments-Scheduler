package com.example.scheduleappotiment.model.apimodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

public class Availability implements Parcelable {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "Day_Of_Week__c",
            "Start_Time__c",
            "End_Time__c",
            "PC_Id__c",
            "Id"
    })
    @JsonProperty("Day_Of_Week__c")
    private String dayOfWeek;
    @JsonProperty("Start_Time__c")
    private String startTime;
    @JsonProperty("End_Time__c")
    private String endTime;
    @JsonProperty("PC_Id__c")
    private String contactId;
    @JsonProperty("Id")
    private String id;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    protected Availability(Parcel in) {
        dayOfWeek = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        contactId = in.readString();
        id = in.readString();
    }

    public Availability() {
    }

    public static final Creator<Availability> CREATOR = new Creator<Availability>() {
        @Override
        public Availability createFromParcel(Parcel in) {
            return new Availability(in);
        }

        @Override
        public Availability[] newArray(int size) {
            return new Availability[size];
        }
    };

    @JsonProperty("Day_Of_Week__c")
    public String getDayOfWeek() {
        return dayOfWeek;
    }

    @JsonProperty("Day_Of_Week__c")
    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @JsonProperty("Start_Time__c")
    public String getStartTime() {
        return startTime;
    }

    @JsonProperty("Start_Time__c")
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @JsonProperty("End_Time__c")
    public String getEndTime() {
        return endTime;
    }

    @JsonProperty("End_Time__c")
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @JsonProperty("PC_Id__c")
    public String getContactId() {
        return contactId;
    }

    @JsonProperty("PC_Id__c")
    public void setContactId(String pCIdC) {
        this.contactId = pCIdC;
    }

    @JsonProperty("Id")
    public String getId() {
        return id;
    }

    @JsonProperty("Id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dayOfWeek);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(contactId);
        dest.writeString(id);
    }
}
