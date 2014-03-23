package com.annimon.turrets;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author aNNiMON
 */
public enum Sound {
    CLICK("/res/click.wav"),
    EXPLOSION_1("/res/explosion1.wav"),
    EXPLOSION_2("/res/explosion2.wav");
    
    private final Clip soundClip;
    
    Sound(String resource) {
        soundClip = loadClip(resource);
    }
    
    public void play() {
        soundClip.setFramePosition(0);
        soundClip.start();
    }
    
    private Clip loadClip(String resource) {
        Clip clip = null;
        try {
            final URL url = getClass().getResource(resource);
            try (AudioInputStream ais = AudioSystem.getAudioInputStream(url)) {
                clip = AudioSystem.getClip();
                clip.open(ais);
            }
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
            ex.printStackTrace();
        }
        return clip;
    }
    
}
