package com.annimon.turrets;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

/**
 * Destructible terrain.
 * @author aNNiMON
 */
public class Terrain implements Constants {

    private static final Color TERRAIN_COLOR = new Color(0xFF269920);
    private static final int PLAYERS_BLOCK_COUNT = 24;
    private static final int SMOOTH_ITERATIONS = 10;

    private final int blocksCount;
    private final int blockSize;
    private final int[] blockHeights;

    public Terrain(int blocksCount) {
        this.blocksCount = blocksCount;
        blockSize = WIDTH / blocksCount;
        blockHeights = new int[blocksCount];
    }
    
    public void draw(Graphics g) {
        g.setColor(TERRAIN_COLOR);
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
    
    public void generate(long seed) {
        final Random rnd = new Random(seed);
        final int maxHeight = HEIGHT - HEIGHT / 3;
        final int stepHeight = maxHeight / 100 * blockSize;
        
        blockHeights[0] = rnd.nextInt(maxHeight);
        for (int i = 1; i < blocksCount; i++) {
            blockHeights[i] = blockHeights[i - 1] + rnd.nextInt(2 * stepHeight + 1) - stepHeight;
            if (blockHeights[i] > maxHeight) blockHeights[i] = maxHeight;
            else if (blockHeights[i] < 0) blockHeights[i] = 0;
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
    }
}
