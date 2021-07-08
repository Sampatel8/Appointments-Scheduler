package com.example.scheduleappotiment.model.apimodel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "contact"
})
public class Contact {
    @JsonProperty("contact")
    private NewContact contact;

    @JsonProperty("contact")
    public NewContact getContact() {
        return contact;
    }

    @JsonProperty("contact")
    public void setContact(NewContact contact) {
        this.contact = contact;
    }
}
