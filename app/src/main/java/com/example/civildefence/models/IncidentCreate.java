package com.example.civildefence.models;

public class IncidentCreate {
    private String category;
    private String priority;
    private double latitude;
    private double longitude;
    private String description;

    public IncidentCreate(String category, String priority, double latitude,
                          double longitude, String description) {
        this.category = category;
        this.priority = priority;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
    }
}