package com.jomariabejo.motorph.utility;

import java.util.prefs.Preferences;

/**
 * Utility class for managing user session persistence.
 * Stores and retrieves the last logged-in user's credentials for auto-login functionality.
 */
public class SessionManager {
    
    private static final String PREF_NODE = "com.jomariabejo.motorph";
    private static final String KEY_USERNAME = "last_username";
    private static final String KEY_PASSWORD = "last_password";
    private static final String KEY_USER_ID = "last_user_id";
    private static final String KEY_REMEMBER_ME = "remember_me";
    
    private static Preferences preferences = Preferences.userRoot().node(PREF_NODE);
    
    // Private constructor to prevent instantiation
    private SessionManager() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    /**
     * Save user session information for auto-login
     * 
     * @param username The username to save
     * @param password The password to save (in production, consider encrypting this)
     * @param userId The user ID to save
     * @param rememberMe Whether to remember the session
     */
    public static void saveSession(String username, String password, Integer userId, boolean rememberMe) {
        if (rememberMe) {
            preferences.put(KEY_USERNAME, username);
            preferences.put(KEY_PASSWORD, password);
            preferences.putInt(KEY_USER_ID, userId);
            preferences.putBoolean(KEY_REMEMBER_ME, true);
        } else {
            clearSession();
        }
    }
    
    /**
     * Get the saved username
     * 
     * @return The saved username, or null if not found
     */
    public static String getSavedUsername() {
        return preferences.get(KEY_USERNAME, null);
    }
    
    /**
     * Get the saved password
     * 
     * @return The saved password, or null if not found
     */
    public static String getSavedPassword() {
        return preferences.get(KEY_PASSWORD, null);
    }
    
    /**
     * Get the saved user ID
     * 
     * @return The saved user ID, or -1 if not found
     */
    public static int getSavedUserId() {
        return preferences.getInt(KEY_USER_ID, -1);
    }
    
    /**
     * Check if "Remember Me" was enabled
     * 
     * @return true if remember me is enabled, false otherwise
     */
    public static boolean isRememberMeEnabled() {
        return preferences.getBoolean(KEY_REMEMBER_ME, false);
    }
    
    /**
     * Check if a session exists
     * 
     * @return true if session data exists, false otherwise
     */
    public static boolean hasSession() {
        return isRememberMeEnabled() && 
               getSavedUsername() != null && 
               getSavedPassword() != null &&
               getSavedUserId() != -1;
    }
    
    /**
     * Clear saved session data
     */
    public static void clearSession() {
        preferences.remove(KEY_USERNAME);
        preferences.remove(KEY_PASSWORD);
        preferences.remove(KEY_USER_ID);
        preferences.remove(KEY_REMEMBER_ME);
    }
}

