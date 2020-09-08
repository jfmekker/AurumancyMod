package com.example.aurumancy.rituals;

import com.example.aurumancy.wands.Wands;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.apache.logging.log4j.LogManager;
import java.util.ArrayList;
import java.util.List;

public class Rituals {
    public static final Ritual GOLD_ABSORB =
            Ritual.BuildValidRitual(new Block[][]
                    {{Blocks.GOLD_BLOCK}},
                    -81,
                    null);

    public static final Ritual SPEEDY_LAPIS =
            Ritual.BuildValidRitual(new Block[][] {
                            {Blocks.ROSE_BUSH, null,               Blocks.ROSE_BUSH},
                            {null,             Blocks.LAPIS_BLOCK, null},
                            {Blocks.ROSE_BUSH, null,               Blocks.ROSE_BUSH}},
                    100,
                    new RitualActionPotionEffect(new EffectInstance(Effects.SPEED, 6000)));

    public static final Ritual NETHER_TELEPORT =
            Ritual.BuildValidRitual(new Block[][]{
                            {Blocks.SAND, null,              Blocks.SAND},
                            {null,        Blocks.NETHERRACK, null},
                            {Blocks.SAND, null,              Blocks.SAND}},
                    24,
                    new RitualAction() {
                        @Override
                        public void doAction(World world, BlockPos pos, PlayerEntity player) {
                            Thread t = new Thread(() -> {
                                world.setBlockState(pos, Blocks.NETHER_PORTAL.getDefaultState());
                                world.setBlockState(pos.add(0,1,0), Blocks.NETHER_PORTAL.getDefaultState());
                                try { this.wait(5000); }
                                catch (Exception e) { LogManager.getLogger().error(e.getMessage()); }
                                if (world.getBlockState(pos).getBlock().equals(Blocks.NETHER_PORTAL)) {
                                    world.destroyBlock(pos, false);
                                    world.destroyBlock(pos.add(0,1,0), false);
                                }
                            });
                            t.start();
                        }
                    });

    public static final Ritual JUMP_WAND_CREATE =
            Ritual.BuildValidRitual(new Block[][]{
                            {Blocks.SLIME_BLOCK, Blocks.OAK_LOG,     Blocks.SLIME_BLOCK},
                            {Blocks.OAK_LOG,     Blocks.HONEY_BLOCK, Blocks.OAK_LOG},
                            {Blocks.SLIME_BLOCK, Blocks.OAK_LOG,     Blocks.SLIME_BLOCK}},
                    100,
                    new RitualAction() {
                        @Override
                        public void doAction(World world, BlockPos pos, PlayerEntity player) {
                            world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(),
                                    new ItemStack(Wands.JUMP_WAND.get())));
                        }
                    });

    public static final List<Ritual> RITUAL_SORTED_LIST = new ArrayList<Ritual>();

    public static void initSortedRitualList() {
        if (RITUAL_SORTED_LIST.size() > 0) return;

        RITUAL_SORTED_LIST.add(GOLD_ABSORB);
        RITUAL_SORTED_LIST.add(SPEEDY_LAPIS);
        RITUAL_SORTED_LIST.add(NETHER_TELEPORT);
        RITUAL_SORTED_LIST.add(JUMP_WAND_CREATE);

        RITUAL_SORTED_LIST.sort(Ritual::compareTo);
    }

}
