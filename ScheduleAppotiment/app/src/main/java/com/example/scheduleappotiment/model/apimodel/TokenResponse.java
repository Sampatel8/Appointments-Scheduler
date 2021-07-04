package com.example.scheduleappotiment.model.apimodel;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "access_token",
        "instance_url",
        "id",
        "token_type",
        "issued_at",
        "signature"
})
public class TokenResponse {

        @JsonProperty("access_token")
        private String accessToken;
        @JsonProperty("instance_url")
        private String instanceUrl;
        @JsonProperty("id")
        private String id;
        @JsonProperty("token_type")
        private String tokenType;
        @JsonProperty("issued_at")
        private String issuedAt;
        @JsonProperty("signature")
        private String signature;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        @JsonProperty("access_token")
        public String getAccessToken() {
            return accessToken;
        }

        @JsonProperty("access_token")
        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        @JsonProperty("instance_url")
        public String getInstanceUrl() {
            return instanceUrl;
        }

        @JsonProperty("instance_url")
        public void setInstanceUrl(String instanceUrl) {
            this.instanceUrl = instanceUrl;
        }

        @JsonProperty("id")
        public String getId() {
            return id;
        }

        @JsonProperty("id")
        public void setId(String id) {
            this.id = id;
        }

        @JsonProperty("token_type")
        public String getTokenType() {
            return tokenType;
        }

        @JsonProperty("token_type")
        public void setTokenType(String tokenType) {
            this.tokenType = tokenType;
        }

        @JsonProperty("issued_at")
        public String getIssuedAt() {
            return issuedAt;
        }

        @JsonProperty("issued_at")
        public void setIssuedAt(String issuedAt) {
            this.issuedAt = issuedAt;
        }

        @JsonProperty("signature")
        public String getSignature() {
            return signature;
        }

        @JsonProperty("signature")
        public void setSignature(String signature) {
            this.signature = signature;
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
