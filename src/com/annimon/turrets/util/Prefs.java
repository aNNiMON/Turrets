package com.annimon.turrets.util;

import java.util.prefs.Preferences;

/**
 * Preferences helper class.
 * @author aNNiMON
 */
public final class Prefs {
    
    private static final String SOUND_ENABLED = "sound_enabled";
    private static final String LAST_IP = "last_ip";
    
    private static Prefs instance;
    
    public static synchronized Prefs getInstance() {
        if (instance == null) {
            instance = new Prefs();
        }
        return instance;
    }
    
    private final Preferences prefs;

    private Prefs() {
        prefs = Preferences.userNodeForPackage(Prefs.class);
    }
    
    public boolean soundEnabled() {
        return prefs.getBoolean(SOUND_ENABLED, true);
    }
    
    public void setSoundEnabled(boolean enabled) {
        prefs.putBoolean(SOUND_ENABLED, enabled);
    }
    
    public String lastIp() {
        return prefs.get(LAST_IP, "127.000.000.001");
    }
    
    public void setLastIp(String ip) {
        prefs.put(LAST_IP, ip);
    } 
}
