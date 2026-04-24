package com.example.civildefence.models;

import com.google.gson.annotations.SerializedName;

public class User {
    private int id;
    private String name;
    private String email;
    private String role;

    @SerializedName("contact_info")
    private String contactInfo;

    @SerializedName("created_at")
    private String createdAt;

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getContactInfo() { return contactInfo; }
    public String getCreatedAt() { return createdAt; }
}