package com.jacobmekker.aurumancy.rituals;

import jdk.internal.jline.internal.Nullable;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class Component {

    public enum Type {
        BLOCK, BLOCK_CLASS, ITEM, ITEM_CLASS
    }

    private Type type;

    private boolean consumed = true;

    private Vec3i offset;

    private Block component_block;
    private Class<? extends Block> component_block_class;
    private Item component_item;
    private Class<? extends Item> component_item_class;

    public Component(Block block, Vec3i offset, boolean consumed) {
        this(block, offset);
        this.consumed = consumed;
    }

    public Component(Block block, Vec3i offset) {
        this.component_block = block;
        this.offset = offset;
        this.type = Type.BLOCK;
    }

    public Component(Item item, Vec3i offset, boolean consumed) {
        this(item, offset);
        this.consumed = consumed;
    }

    public Component(Item item, Vec3i offset) {
        this.component_item = item;
        this.offset = offset;
        this.type = Type.ITEM;
    }

    public Component(Class<?> thing_class, Vec3i offset, boolean consumed) {
        this(thing_class, offset);
        this.consumed = consumed;
    }

    public Component(Class<?> thing_class, Vec3i offset) {
        if (Block.class.isAssignableFrom(thing_class)) {
            this.component_block_class = (Class<? extends Block>) thing_class;
            this.type = Type.BLOCK_CLASS;
        }
        else if (Item.class.isAssignableFrom(thing_class)) {
            this.component_item_class = (Class<? extends Item>) thing_class;
            this.type = Type.BLOCK_CLASS;
        }

        this.offset = offset;
    }

    public Type getType() {
        return type;
    }

    public Vec3i getOffset() {
        return offset;
    }

    public @Nullable Block getBlock() {
        return (type == Type.BLOCK) ? component_block : null;
    }

    public boolean checkComponent(World world, BlockPos center) {
        switch (type) {
            case BLOCK:
                return world.getBlockState(center.add(offset)).getBlock() == component_block;
            case BLOCK_CLASS:
                return component_block_class.isInstance(world.getBlockState(center.add(offset)).getBlock());

            case ITEM:
            case ITEM_CLASS:
                // TODO
                return false;

            default:
                return false;
        }
    }

    public void tryConsume(World world, BlockPos center) {
        if (consumed) {
            switch (type) {
                case BLOCK:
                case BLOCK_CLASS:
                    world.destroyBlock(center.add(offset), false);

                case ITEM:
                case ITEM_CLASS:
                    // TODO
                    break;

                default:
                    break;
            }
        }
    }
}
