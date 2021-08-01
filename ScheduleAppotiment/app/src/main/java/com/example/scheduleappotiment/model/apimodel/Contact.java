package com.example.scheduleappotiment.model.apimodel;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.BoringLayout;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Id",
        "FirstName",
        "LastName",
        "Email",
        "Department",
        "User_Id__c",
        "Birthdate",
        "Gender__c"
})
public class Contact implements Parcelable {

    @JsonProperty("Id")
    private String id;
    @JsonProperty("FirstName")
    private String firstName;
    @JsonProperty("LastName")
    private String lastName;
    @JsonProperty("User_Id__c")
    private String User_Id__c;

    @JsonProperty("Email")
    private String Email;

    @JsonProperty("Department")
    private String Department;

    @JsonProperty("Is_PC__c")
    private Boolean Is_PC;

    @JsonProperty("Birthdate")
    private String Dob;

    @JsonProperty("Gender__c")
    private String Gender;

    public Contact() {
    }

    public Contact(String id) {
        this.id = id;
    }

    protected Contact(Parcel in) {
        id = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        User_Id__c = in.readString();
        Email = in.readString();
        Department = in.readString();
        byte tmpIs_PC = in.readByte();
        Is_PC = tmpIs_PC == 0 ? null : tmpIs_PC == 1;
        Dob=in.readString();
        Gender=in.readString();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    @JsonProperty("Is_PC__c")
    public Boolean getIs_PC() {
        return Is_PC;
    }

    @JsonProperty("Is_PC__c")
    public void setIs_PC(Boolean is_PC) {
        Is_PC = is_PC;
    }

    @JsonIgnore
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("Id")
    public String getId() {
        return id;
    }

    @JsonProperty("Id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("FirstName")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("FirstName")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty("LastName")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("LastName")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @JsonProperty("User_Id__c")
    public String getUser_Id__c() {
        return User_Id__c;
    }

    @JsonProperty("User_Id__c")
    public void setUser_Id__c(String user_Id__c) {
        User_Id__c = user_Id__c;
    }

    @JsonProperty("Email")
    public String getEmail() {
        return Email;
    }

    @JsonProperty("Email")
    public void setEmail(String email) {
        Email = email;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    @JsonProperty("Department")
    public String getDepartment() {
        return Department;
    }

    @JsonProperty("Department")
    public void setDepartment(String department) {
        Department = department;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(User_Id__c);
        dest.writeString(Email);
        dest.writeString(Department);
        dest.writeByte((byte) (Is_PC == null ? 0 : Is_PC ? 1 : 2));
        dest.writeString(Dob);
        dest.writeString(Gender);
    }

    @JsonProperty("Birthdate")
    public String getDob() {
        return Dob;
    }

    @JsonProperty("Birthdate")
    public void setDob(String dob) {
        Dob = dob;
    }

    @JsonProperty("Gender__c")
    public String getGender() {
        return Gender;
    }

    @JsonProperty("Gender__c")
    public void setGender(String gender) {
        Gender = gender;
    }
}
