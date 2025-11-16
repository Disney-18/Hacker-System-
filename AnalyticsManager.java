package com.hacker.finalapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;

public class AnalyticsManager {
    private static AnalyticsManager instance;
    private Context context;
    private static final String TAG = "HackerAnalytics";

    public static final String EVENT_LEVEL_START = "level_start";
    public static final String EVENT_LEVEL_COMPLETE = "level_complete";
    public static final String EVENT_POWERUP_USED = "powerup_used";
    public static final String EVENT_MODULE_OPEN = "module_open";
    public static final String EVENT_APP_OPEN = "app_open";

    private AnalyticsManager(Context context) {
        this.context = context.getApplicationContext();
    }

    public static synchronized AnalyticsManager getInstance(Context context) {
        if (instance == null) {
            instance = new AnalyticsManager(context);
        }
        return instance;
    }

    public void logEvent(String eventName) {
        logEvent(eventName, new HashMap<String, String>());
    }

    public void logEvent(String eventName, Map<String, String> parameters) {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("ANALYTICS: ").append(eventName);

        if (!parameters.isEmpty()) {
            logMessage.append(" | ");
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                logMessage.append(entry.getKey()).append("=").append(entry.getValue()).append(" ");
            }
        }

        Log.i(TAG, logMessage.toString());
        saveEventOffline(eventName, parameters);
    }

    public void logLevelStart(int level, String mode) {
        Map<String, String> params = new HashMap<>();
        params.put("level", String.valueOf(level));
        params.put("mode", mode);
        logEvent(EVENT_LEVEL_START, params);
    }

    public void logLevelComplete(int level, int score, int timeLeft, int combo) {
        Map<String, String> params = new HashMap<>();
        params.put("level", String.valueOf(level));
        params.put("score", String.valueOf(score));
        params.put("time_left", String.valueOf(timeLeft));
        params.put("combo", String.valueOf(combo));
        logEvent(EVENT_LEVEL_COMPLETE, params);
    }

    public void logPowerUpUsed(String powerUpType, int level) {
        Map<String, String> params = new HashMap<>();
        params.put("powerup_type", powerUpType);
        params.put("level", String.valueOf(level));
        logEvent(EVENT_POWERUP_USED, params);
    }

    public void logModuleOpened(String moduleName) {
        Map<String, String> params = new HashMap<>();
        params.put("module", moduleName);
        logEvent(EVENT_MODULE_OPEN, params);
    }

    private void saveEventOffline(String eventName, Map<String, String> parameters) {
        SharedPreferences prefs = context.getSharedPreferences("game_analytics", Context.MODE_PRIVATE);
        int eventCount = prefs.getInt(eventName + "_count", 0);
        eventCount++;

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(eventName + "_count", eventCount);
        editor.apply();
    }
}
