package com.example.scheduleappotiment.model.apimodel;

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
        "User_Id__c"
})
public class NewContact {

    @JsonProperty("Id")
    private String id;
    @JsonProperty("FirstName")
    private String firstName;
    @JsonProperty("LastName")
    private String lastName;
    @JsonProperty("User_Id__c")
    private String User_Id__c;
    @JsonProperty("Department")
    private String Department;

    @JsonProperty("Email")
    private String Email;

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

    @JsonProperty("Department")
    public String getDepartment() {
        return Department;
    }

    @JsonProperty("Department")
    public void setDepartment(String department) {
        Department = department;
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
}
