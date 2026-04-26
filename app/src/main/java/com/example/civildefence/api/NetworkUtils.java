package com.example.civildefence.api;

public class NetworkUtils {
    // Change this URL to the computer IP address when running locally
    // For Android Emulator connecting to host machine: http://10.0.2.2:8000
    // For physical device on same WiFi: http://192.168.x.x:8000
    public static final String BASE_URL = "http://192.168.18.7:8000"; // UPDATED TO LOCAL IP

    public static final String API_PREFIX = "/api/v1";

    // Endpoints
    public static final String LOGIN = API_PREFIX + "/auth/login/json";
    public static final String REGISTER = API_PREFIX + "/auth/register";
    public static final String INCIDENTS = API_PREFIX + "/incidents";
    public static final String MEDIA_UPLOAD = API_PREFIX + "/media/upload";
    public static final String RESOURCES = API_PREFIX + "/resources";
    public static final String REPORTS_DAILY = API_PREFIX + "/reports/daily";
    public static final String REPORTS_PDF = API_PREFIX + "/reports/export/pdf";
    public static final String AVAILABLE_TEAMS = API_PREFIX + "/incidents/available-teams";
    public static final String NOTIFICATIONS = API_PREFIX + "/notifications";
    public static final String INTEGRATIONS_HOSPITAL = API_PREFIX + "/integrations/hospital/admission";
    public static final String INTEGRATIONS_POLICE = API_PREFIX + "/integrations/police/incident-report";
    public static final String INTEGRATIONS_FIRE = API_PREFIX + "/integrations/fire-department/request";
}