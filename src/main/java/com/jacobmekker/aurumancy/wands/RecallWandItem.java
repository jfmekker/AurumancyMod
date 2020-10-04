package com.jacobmekker.aurumancy.wands;

import com.jacobmekker.aurumancy.Aurumancy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeItem;

public class RecallWandItem extends AbstractWandItem implements IForgeItem {
    /**
     * Construct an AbstractWand. Although abstract, will not actually need to override or define any methods.
     *
     * @param properties General Item properties object.
     * @param cost       Cost to use the wand in xp. Can be positive, zero, or negative.
     * @param use        How is the wand used? On a block, charged, or instant?
     */
    public RecallWandItem(Properties properties, int cost, WandUsageType use, int cooldown) {
        super(properties, cost, use, cooldown);
    }

    public boolean hasRecallPos(ItemStack stack) {
        return stack.getOrCreateTag().contains("recall_position");
    }

    public BlockPos getRecallPos(ItemStack stack) {
        int[] coords = stack.getOrCreateTag().getIntArray("recall_position");
        return new BlockPos(coords[0], coords[1], coords[2]);
    }

    public void setRecallPos(ItemStack stack, BlockPos pos) {
        int[] coords = { pos.getX(), pos.getY(), pos.getZ() };
        stack.getOrCreateTag().putIntArray("recall_position", coords);
    }

    @Override
    protected void instantUsage(World world, PlayerEntity player, Hand hand) {
        if (world.isRemote) return;
        super.instantUsage(world, player, hand);

        ItemStack stack = player.getHeldItem(hand);

        if (hasRecallPos(stack)) {
            BlockPos pos = getRecallPos(stack);
            player.setVelocity(0,0,0);
            player.fallDistance = 0;
            player.attemptTeleport(
                    pos.getX() + 0.5,
                    pos.getY(),
                    pos.getZ() + 0.5,
                    false);
        } else {
            setRecallPos(stack, player.getPosition().add(0,1,0));
            player.sendMessage(new StringTextComponent(
                    "Recall position set to: " + player.getPosition().toString()));
        }
    }


}
