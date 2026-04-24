package com.example.civildefence.api;

import com.example.civildefence.models.*;
import retrofit2.Call;
import retrofit2.http.*;
import java.util.List;
import java.util.Map;

public interface ApiService {

    // ============ AUTHENTICATION ============
    @POST(NetworkUtils.LOGIN)
    Call<TokenResponse> login(@Body LoginRequest request);

    @POST(NetworkUtils.REGISTER)
    Call<User> register(@Body Map<String, Object> userData);

    // ============ INCIDENTS ============
    @POST(NetworkUtils.INCIDENTS)
    Call<Incident> createIncident(
            @Header("Authorization") String token,
            @Body IncidentCreate incident
    );

    @GET(NetworkUtils.INCIDENTS)
    Call<List<Incident>> getIncidents(
            @Header("Authorization") String token,
            @Query("skip") int skip,
            @Query("limit") int limit
    );

    @GET(NetworkUtils.INCIDENTS + "/{id}")
    Call<Incident> getIncidentById(
            @Header("Authorization") String token,
            @Path("id") int incidentId
    );

    @PATCH(NetworkUtils.INCIDENTS + "/{id}/status")
    Call<Incident> updateIncidentStatus(
            @Header("Authorization") String token,
            @Path("id") int incidentId,
            @Body Map<String, String> statusUpdate
    );

    @POST(NetworkUtils.INCIDENTS + "/{id}/assign-team")
    Call<Map<String, Object>> assignTeam(
            @Header("Authorization") String token,
            @Path("id") int incidentId,
            @Query("team_id") int teamId
    );

    @GET(NetworkUtils.AVAILABLE_TEAMS)
    Call<Map<String, Object>> getAvailableTeams(
            @Header("Authorization") String token
    );

    @GET(NetworkUtils.INCIDENTS + "/date-range")
    Call<List<Incident>> getIncidentsByDateRange(
            @Header("Authorization") String token,
            @Query("start_date") String startDate,
            @Query("end_date") String endDate
    );

    // ============ MEDIA ============
    @Multipart
    @POST(NetworkUtils.MEDIA_UPLOAD)
    Call<Map<String, Object>> uploadMedia(
            @Header("Authorization") String token,
            @Part("incident_id") int incidentId,
            @Part("file_type") String fileType,
            @Part okhttp3.MultipartBody.Part file
    );

    @GET(NetworkUtils.API_PREFIX + "/media/incident/{incident_id}")
    Call<List<Map<String, Object>>> getIncidentMedia(
            @Header("Authorization") String token,
            @Path("incident_id") int incidentId
    );

    // ============ RESOURCES ============
    @POST(NetworkUtils.RESOURCES)
    Call<Resource> createResource(
            @Header("Authorization") String token,
            @Body Map<String, Object> resourceData
    );

    @GET(NetworkUtils.RESOURCES)
    Call<List<Resource>> getResources(
            @Header("Authorization") String token,
            @Query("skip") int skip,
            @Query("limit") int limit
    );

    @PATCH(NetworkUtils.RESOURCES + "/{id}/fuel")
    Call<Resource> updateFuelUsage(
            @Header("Authorization") String token,
            @Path("id") int resourceId,
            @Body Map<String, Double> fuelData
    );

    // ============ REPORTS ============
    @GET(NetworkUtils.REPORTS_DAILY)
    Call<DailyReport> getDailyReport(
            @Header("Authorization") String token,
            @Query("date") String date
    );

    @GET(NetworkUtils.REPORTS_PDF)
    Call<okhttp3.ResponseBody> exportPdfReport(
            @Header("Authorization") String token,
            @Query("start_date") String startDate,
            @Query("end_date") String endDate
    );

    // ============ NOTIFICATIONS ============
    @GET(NetworkUtils.NOTIFICATIONS)
    Call<List<Map<String, Object>>> getNotifications(
            @Header("Authorization") String token
    );

    @PATCH(NetworkUtils.NOTIFICATIONS + "/{id}/read")
    Call<Map<String, Object>> markNotificationRead(
            @Header("Authorization") String token,
            @Path("id") int notificationId
    );

    // ============ PROFILE ============
    @GET(NetworkUtils.API_PREFIX + "/auth/verify-token")
    Call<Map<String, Object>> verifyToken(
            @Header("Authorization") String token
    );
}