package com.example.scheduleappotiment.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.scheduleappotiment.model.apimodel.Contact;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Name",
        "Id",
        "Status__c",
        "To_Contact__r",
        "From_Contact__r",
        "Description__c",
        "Slot__r"
})
public class Appointment implements Parcelable {

    @JsonProperty("Name")
    private String title;
    @JsonProperty("Description__c")
    private String description;

    private long dateTime;

    @JsonProperty("Id")
    private String id;

    private int status;

    @JsonProperty("Status__c")
    private String serverStatus;

    @JsonProperty("From_Contact__r")
    private Contact fromContact;
    @JsonProperty("To_Contact__r")
    private Contact toContact;

    @JsonProperty("Slot__r")
    private TimeSlotModel slot;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    protected Appointment(Parcel in) {
        title = in.readString();
        description = in.readString();
        dateTime = in.readLong();
        id = in.readString();
        status = in.readInt();
        serverStatus = in.readString();
        slot = in.readParcelable(TimeSlotModel.class.getClassLoader());
        fromContact=in.readParcelable(Contact.class.getClassLoader());
        toContact=in.readParcelable(Contact.class.getClassLoader());
    }

    public static final Creator<Appointment> CREATOR = new Creator<Appointment>() {
        @Override
        public Appointment createFromParcel(Parcel in) {
            return new Appointment(in);
        }

        @Override
        public Appointment[] newArray(int size) {
            return new Appointment[size];
        }
    };

    @Override
    public String toString() {
        return "Appointment{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dateTime=" + dateTime +
                ", id='" + id + '\'' +
                ", status=" + status +
                ", serverStatus='" + serverStatus + '\'' +
                ", fromContact='" + fromContact + '\'' +
                ", toContact='" + toContact + '\'' +
                ", slot=" + slot +
                '}';
    }

    public Appointment() {
    }

    public Appointment(String title, String description, long dateTime, String id, int status, Contact fromContact, Contact toContact, TimeSlotModel slot) {
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
        this.id = id;
        this.status=status;
        this.fromContact = fromContact;
        this.toContact = toContact;
        this.slot = slot;
    }

    public Appointment(String title,long dateTime,String id,int status,TimeSlotModel slot){
        this(title,null,dateTime,id,status, null, null, slot);
    }

    @JsonProperty("Name")
    public String getTitle() {
        return title;
    }

    @JsonProperty("Name")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("Description__c")
    public String getDescription() {
        return description;
    }

    @JsonProperty("Description__c")
    public void setDescription(String description) {
        this.description = description;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    @JsonProperty("Id")
    public String getId() {
        return id;
    }

    @JsonProperty("Id")
    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @JsonProperty("From_Contact__r")
    public Contact getFromContact() {
        return fromContact;
    }

    @JsonProperty("From_Contact__r")
    public void setFromContact(Contact fromContact) {
        this.fromContact = fromContact;
    }

    @JsonProperty("To_Contact__r")
    public Contact getToContact() {
        return toContact;
    }

    @JsonProperty("To_Contact__r")
    public void setToContact(Contact toContact) {
        this.toContact = toContact;
    }

    @JsonProperty("Slot__r")
    public TimeSlotModel getSlot() {
        return slot;
    }

    @JsonProperty("Slot__r")
    public void setSlot(TimeSlotModel slot) {
        this.slot = slot;
    }

    @JsonProperty("Status__c")
    public String getServerStatus() {
        return serverStatus;
    }

    @JsonProperty("Status__c")
    public void setServerStatus(String serverStatus) {
        this.serverStatus = serverStatus;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(java.lang.String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeLong(dateTime);
        dest.writeString(id);
        dest.writeInt(status);
        dest.writeString(serverStatus);
        dest.writeParcelable(slot, flags);
        dest.writeParcelable(fromContact,flags);
        dest.writeParcelable(toContact,flags);
    }
}
