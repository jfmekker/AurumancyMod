package com.jacobmekker.aurumancy.items;

/**
 * How a wand is used: charging, instant, or on a block.
 */
public enum ItemUsageType {
    INSTANT,    // Instant right-click event
    CHARGED,    // Charged usage (hold right-click)
    BLOCK,      // Instant right-click on a block
    PASSIVE,    // Item's effect is not actively triggered
}
