package com.jacobmekker.aurumancy.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeItem;

public class RecallWandItem extends AbstractMagicItem implements IForgeItem {
    /**
     * Construct an AbstractWand. Although abstract, will not actually need to override or define any methods.
     *
     * @param properties General Item properties object.
     * @param cost       Cost to use the wand in xp. Can be positive, zero, or negative.
     * @param use        How is the wand used? On a block, charged, or instant?
     */
    public RecallWandItem(Properties properties, int cost, ItemUsageType use, int cooldown) {
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

    public void unsetRecallPos(ItemStack stack) {
        stack.getOrCreateTag().remove("recall_position");
    }

    @Override
    protected void onMagicItemUse(PlayerEntity player, Hand hand, BlockPos pos) {
        World world = player.world;

        if (world.isRemote) return;

        ItemStack stack = player.getHeldItem(hand);

        if (hasRecallPos(stack)) {
            if (player.isCrouching()) {
                unsetRecallPos(stack);
                player.sendMessage(new StringTextComponent("Recall position unset."));
            }
            else {
                BlockPos recall_pos = getRecallPos(stack);
                player.setVelocity(0,0,0);
                player.fallDistance = 0;
                player.attemptTeleport(
                        recall_pos.getX() + 0.5,
                        recall_pos.getY(),
                        recall_pos.getZ() + 0.5,
                        false);
            }
        } else {
            setRecallPos(stack, pos.add(0,1,0));
            player.sendMessage(new StringTextComponent("Recall position set to: " + pos.toString()));
        }
    }
}
