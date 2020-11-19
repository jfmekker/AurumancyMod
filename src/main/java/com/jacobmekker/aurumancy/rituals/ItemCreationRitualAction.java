package com.jacobmekker.aurumancy.rituals;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemCreationRitualAction extends RitualAction {

    private Item created_item;

    public ItemCreationRitualAction(Item item) {
        this.created_item = item;
    }

    @Override
    public void doAction(World world, BlockPos pos, PlayerEntity player) {
        Vec3d spawn = new Vec3d(pos.getX(),pos.getY(),pos.getZ());
        spawn = spawn.add(0.5,1.25,0.5);
        world.addEntity(new ItemEntity(world, spawn.x, spawn.y, spawn.z,
                new ItemStack(this.created_item)));
    }
}
