package com.annimon.turrets;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.util.Random;

/**
 * Destructible terrain.
 * @author aNNiMON
 */
public class Terrain implements Constants {

    private static final Color[] TERRAIN_COLOR = {
        new Color(0xFF33CE2B), new Color(0xFF1C7317)
    };
    private static final int SMOOTH_ITERATIONS = 10;
    
    private final int blocksCount;
    private final int blockSize;
    private final int[] blockHeights;
    
    private LinearGradientPaint gradientPaint;

    public Terrain(int blocksCount) {
        this.blocksCount = blocksCount;
        blockSize = WIDTH / blocksCount;
        blockHeights = new int[blocksCount];
    }
    
    public void draw(Graphics2D g) {
        g.setPaint(gradientPaint);
        for (int i = 0; i < blocksCount; i++) {
            final int blockHeight = blockHeights[i];
            g.fillRect(i * blockSize, HEIGHT - blockHeight, blockSize, blockHeight);
        }
    }
    
    public void destroyTerrain(int x) {
        final int position = x / blockSize;
        final int explosionSize = PLAYERS_BLOCK_COUNT / 2;
        if ( (position <= PLAYERS_BLOCK_COUNT) ||
             (position >= blocksCount - PLAYERS_BLOCK_COUNT - 1) ) return;
        
        blockHeights[position] -= explosionSize * 2;
        for (int i = 1; i < explosionSize; i++) {
            blockHeights[position - i] -= explosionSize * 2 - i;
            blockHeights[position + i] -= explosionSize * 2 - i;
        }
        for (int i = position - explosionSize; i < position + explosionSize; i++) {
            if (blockHeights[i] < 0) blockHeights[i] = 0;
        }
    }
    
    public boolean isCollide(int x, int y) {
        final int pos = x / blockSize;
        return (y <= blockHeights[pos]);
    }
    
    public final int getFirstBlockHeight() {
        return blockHeights[0];
    }
    
    public final int getLastBlockHeight() {
        return blockHeights[blocksCount - 1];
    }
    
    public void generate() {
        final int maxHeight = HEIGHT / 2;
        final int stepHeight = maxHeight / 75 * blockSize;
        
        int maxBlockHeight = 0;
        blockHeights[0] = Util.rand(maxHeight);
        for (int i = 1; i < blocksCount; i++) {
            int value = blockHeights[i - 1] + Util.rand(-stepHeight, stepHeight);
            if (value > maxHeight) value = maxHeight;
            else if (value < 0) value = 0;
            blockHeights[i] = value;
            // Detect max block height
            if (maxBlockHeight < value) maxBlockHeight = value;
        }
        // Flatten out left and right blocks for turrets.
        for (int i = 0; i < PLAYERS_BLOCK_COUNT; i++) {
            blockHeights[i] = getFirstBlockHeight();
            blockHeights[blocksCount - i - 1] = getLastBlockHeight();
        }
        // Smooth blocks
        final int halfCount = PLAYERS_BLOCK_COUNT / 2;
        for (int start = 1; start <= SMOOTH_ITERATIONS; start++) {
            for (int i = start + halfCount; i < blocksCount - halfCount; i += 2) {
                blockHeights[i] = (blockHeights[i - 1] + blockHeights[i + 1]) / 2;
            }
        }
        
        gradientPaint = new LinearGradientPaint(
                0, Constants.HEIGHT - maxBlockHeight,
                0, Constants.HEIGHT,
                new float[] {0f, 1f}, TERRAIN_COLOR);
    }
}
