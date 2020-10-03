package com.jacobmekker.aurumancy.wands;

/**
 * How a wand is used: charging, instant, or on a block.
 */
public enum WandUsageType {
    INSTANT,    // Instant right-click event
    CHARGED,    // Charged usage (hold right-click)
    BLOCK       // Instant right-click on a block
}
