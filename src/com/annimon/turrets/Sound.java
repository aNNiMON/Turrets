package com.annimon.turrets;

import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author aNNiMON
 */
public class Sound {
    
    public static final Sound CLICK = new Sound("/res/click.wav");
    public static final Sound EXPLOSION_1 = new Sound("/res/explosion1.wav");
    public static final Sound EXPLOSION_2 = new Sound("/res/explosion2.wav");
    
    private final Clip soundClip;
    
    public Sound(String resource) {
        soundClip = loadClip(resource);
    }
    
    public void play() {
        soundClip.setFramePosition(0);
        soundClip.start();
    }
    
    private Clip loadClip(String resource) {
        Clip clip = null;
        try {
            final InputStream is = getClass().getResourceAsStream(resource);
            try (AudioInputStream ais = AudioSystem.getAudioInputStream(is)) {
                clip = AudioSystem.getClip();
                clip.open(ais);
            }
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
            ex.printStackTrace();
        }
        return clip;
    }
    
}
