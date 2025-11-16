package com.hacker.finalapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;

public class UserPreferencesManager {
    private static UserPreferencesManager instance;
    private SharedPreferences prefs;
    private Context context;

    private static final String PREFS_NAME = "hacker_prefs";
    private static final String KEY_SOUND_ENABLED = "sound_enabled";
    private static final String KEY_VIBRATION_ENABLED = "vibration_enabled";
    private static final String KEY_ANIMATIONS_ENABLED = "animations_enabled";

    private UserPreferencesManager(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        initializeDefaults();
    }

    public static synchronized UserPreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserPreferencesManager(context);
        }
        return instance;
    }

    private void initializeDefaults() {
        if (!prefs.contains(KEY_SOUND_ENABLED)) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(KEY_SOUND_ENABLED, true);
            editor.putBoolean(KEY_VIBRATION_ENABLED, true);
            editor.putBoolean(KEY_ANIMATIONS_ENABLED, true);
            editor.apply();
        }
    }

    public boolean isSoundEnabled() {
        return prefs.getBoolean(KEY_SOUND_ENABLED, true);
    }

    public void setSoundEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_SOUND_ENABLED, enabled).apply();
    }

    public boolean isVibrationEnabled() {
        return prefs.getBoolean(KEY_VIBRATION_ENABLED, true);
    }

    public void setVibrationEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_VIBRATION_ENABLED, enabled).apply();
    }

    public boolean areAnimationsEnabled() {
        return prefs.getBoolean(KEY_ANIMATIONS_ENABLED, true);
    }

    public void setAnimationsEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_ANIMATIONS_ENABLED, enabled).apply();
    }

    public void triggerVibration() {
        if (isVibrationEnabled()) {
            try {
                Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator != null && vibrator.hasVibrator()) {
                    vibrator.vibrate(50);
                }
            } catch (Exception e) {
                // Ignorar errores de vibración
            }
        }
    }
}
