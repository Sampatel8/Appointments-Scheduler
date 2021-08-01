package com.example.scheduleappotiment.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Id",
        "Start_Time__c",
        "End_Time__c",
        "Date__c"
})
public class TimeSlotModel implements Parcelable {

    private String DayOfWeek;

    @JsonProperty("Date__c")
    private String Date;

    public TimeSlotModel() {
    }

    protected TimeSlotModel(Parcel in) {
        DayOfWeek = in.readString();
        endTimeC = in.readString();
        startTimeC = in.readString();
        id = in.readString();
        Date=in.readString();
    }

    public static final Creator<TimeSlotModel> CREATOR = new Creator<TimeSlotModel>() {
        @Override
        public TimeSlotModel createFromParcel(Parcel in) {
            return new TimeSlotModel(in);
        }

        @Override
        public TimeSlotModel[] newArray(int size) {
            return new TimeSlotModel[size];
        }
    };

    public String getDayOfWeek() {
        return DayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.DayOfWeek = dayOfWeek;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(DayOfWeek);
        dest.writeString(endTimeC);
        dest.writeString(startTimeC);
        dest.writeString(id);
        dest.writeString(Date);
    }

    @JsonProperty("Id")
    private String id;
    @JsonProperty("Start_Time__c")
    private String startTimeC;
    @JsonProperty("End_Time__c")
    private String endTimeC;
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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @JsonProperty("Date__c")
    public String getDate() {
        return Date;
    }

    @JsonProperty("Date__c")
    public void setDate(String date) {
        Date = date;
    }
}



