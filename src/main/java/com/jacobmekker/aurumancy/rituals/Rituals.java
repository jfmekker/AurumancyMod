package com.jacobmekker.aurumancy.rituals;

import com.jacobmekker.aurumancy.Aurumancy;
import com.jacobmekker.aurumancy.wands.Wands;

import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.block.RedstoneOreBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static net.minecraft.block.Blocks.*;

public class Rituals {
    public static final Ritual GOLD_ABSORB =
            Ritual.BuildValidRitual(-81,
                    null,
                    Collections.singletonList(
                            new Component(GOLD_BLOCK, new Vec3i(0, 0, 0))
                    )
            );

    public static final Ritual SPEEDY_LAPIS =
            Ritual.BuildValidRitual(99,
                    new RitualActionPotionEffect(new EffectInstance(Effects.SPEED, 6000)),
                    Arrays.asList(
                            new Component(LAPIS_BLOCK, new Vec3i(0,0,0)),
                            new Component(ROSE_BUSH, new Vec3i(1,0,1)),
                            new Component(ROSE_BUSH, new Vec3i(1,0,-1)),
                            new Component(ROSE_BUSH, new Vec3i(-1,0,1)),
                            new Component(ROSE_BUSH, new Vec3i(-1,0,-1))
                    )
            );

    public static final Ritual NETHER_TELEPORT =
            Ritual.BuildValidRitual(24,
                    new RitualAction() {
                        @Override
                        public void doAction(World world, BlockPos pos, PlayerEntity player) {
                            if (world.isRemote) return;
                            Thread t = new Thread(() -> {
                                world.setBlockState(pos, NETHER_PORTAL.getDefaultState());
                                world.setBlockState(pos.add(0,1,0), NETHER_PORTAL.getDefaultState());
                                try { this.wait(5000); }
                                catch (Exception e) { Aurumancy.LOGGER.error(e.getMessage()); }
                                if (world.getBlockState(pos).getBlock().equals(NETHER_PORTAL)) {
                                    world.destroyBlock(pos, false);
                                    world.destroyBlock(pos.add(0,1,0), false);
                                }
                            });
                            t.start();
                        }
                    },
                    Arrays.asList(
                            new Component(NETHERRACK, new Vec3i(0,0,0)),
                            new Component(SAND, new Vec3i(1,0,1)),
                            new Component(SAND, new Vec3i(1,0,-1)),
                            new Component(SAND, new Vec3i(-1,0,1)),
                            new Component(SAND, new Vec3i(-1,0,-1))
                    ));

    public static final Ritual JUMP_WAND_CREATE =
            Ritual.BuildValidRitual(100,
                    new RitualAction() {
                        @Override
                        public void doAction(World world, BlockPos pos, PlayerEntity player) {
                            world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(),
                                    new ItemStack(Wands.JUMP_WAND.get())));
                        }
                    },
                    Arrays.asList(
                            new Component(HONEY_BLOCK, new Vec3i(0,0,0)),
                            new Component(OAK_LOG, new Vec3i(1,0,0)),
                            new Component(OAK_LOG, new Vec3i(-1,0,0)),
                            new Component(OAK_LOG, new Vec3i(0,0,1)),
                            new Component(OAK_LOG, new Vec3i(0,0,-1)),
                            new Component(SLIME_BLOCK, new Vec3i(1,0,1)),
                            new Component(SLIME_BLOCK, new Vec3i(1,0,-1)),
                            new Component(SLIME_BLOCK, new Vec3i(-1,0,1)),
                            new Component(SLIME_BLOCK, new Vec3i(-1,0,-1))
                    ));

    public static final Ritual FIREBALL_WAND_CREATE = null;
            /*Ritual.BuildValidRitual(new Block[][]{
                            {LAPIS_BLOCK, TNT,        null,       TNT,        LAPIS_BLOCK},
                            {TNT,         null,       GOLD_BLOCK, null,       TNT},
                            {null,        GOLD_BLOCK, GLOWSTONE,  GOLD_BLOCK, null},
                            {TNT,         null,       GOLD_BLOCK, null,       TNT},
                            {LAPIS_BLOCK, TNT,        null,       TNT,        LAPIS_BLOCK}},
                    405,
                    new RitualAction() {
                        @Override
                        public void doAction(World world, BlockPos pos, PlayerEntity player) {
                            world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(),
                                    new ItemStack(Wands.FIREBALL_WAND.get())));
                        }
                    });*/

    public static final Ritual CHUNK_ORE_GATHER = null;
            /*Ritual.BuildValidRitual(new Block[][]{
                                {IRON_BLOCK, null,   GLASS,      null,       GLASS,      null,   IRON_BLOCK},
                                {null,       null,   null,       HOPPER,     null,       null,   null},
                                {GLASS,      null,   GLASS_PANE, FURNACE,    GLASS_PANE, null,   GLASS},
                                {null,       HOPPER, FURNACE,    COAL_BLOCK, FURNACE,    HOPPER, null},
                                {GLASS,      null,   GLASS_PANE, FURNACE,    GLASS_PANE, null,   GLASS},
                                {null,       null,   null,       HOPPER,     null,       null,   null},
                                {IRON_BLOCK, null,   GLASS,      null,       GLASS,      null,   IRON_BLOCK},
                            },
                    81,
                    new RitualAction() {
                        @Override
                        public void doAction(World world, BlockPos pos, PlayerEntity player) {
                            List<ItemStack> drops = new ArrayList<>();

                            int size = 7;
                            for (int i = pos.getX() - size; i < pos.getX() + size; i++) {
                                for (int k = pos.getZ() - size; k < pos.getZ() + size; k++) {
                                    for (int j = pos.getY(); j < world.getHeight(Heightmap.Type.WORLD_SURFACE,i,k); j++) {
                                        BlockPos curr = new BlockPos(i,j,k);
                                        if (world.getBlockState(curr).getBlock() instanceof OreBlock
                                            || world.getBlockState(curr).getBlock() instanceof RedstoneOreBlock) {

                                            // Grab and destroy block
                                            Block drop = world.getBlockState(new BlockPos(i,j,k)).getBlock();
                                            world.destroyBlock(curr, false);

                                            // Add to existing Item stacks first
                                            boolean added = false;
                                            for (ItemStack s : drops) {
                                                if (s.getItem() == drop.asItem() && s.getCount() < s.getMaxStackSize()) {
                                                    s.setCount(s.getCount() + 1);
                                                    added = true;
                                                    break;
                                                }
                                            }
                                            if (!added) drops.add(new ItemStack(drop));
                                        }
                                    }
                                }
                            }

                            for (ItemStack i : drops) {
                                world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), i));
                            }
                        }
                    });*/

    /**
     * Sorted list of all rituals to check against. Sorted by size so big rituals get checked first.
     */
    public static final List<Ritual> RITUAL_SORTED_LIST = new ArrayList<>();

    /**
     * Add all the rituals to the list, then sort it.
     */
    public static void initSortedRitualList() {
        if (RITUAL_SORTED_LIST.size() > 0) return;

        RITUAL_SORTED_LIST.add(GOLD_ABSORB);
        RITUAL_SORTED_LIST.add(SPEEDY_LAPIS);
        RITUAL_SORTED_LIST.add(NETHER_TELEPORT);
        RITUAL_SORTED_LIST.add(JUMP_WAND_CREATE);
        RITUAL_SORTED_LIST.add(FIREBALL_WAND_CREATE);
        RITUAL_SORTED_LIST.add(CHUNK_ORE_GATHER);

        // Remove nulls
        for (int i = 0; i < RITUAL_SORTED_LIST.size(); i++) {
            if (RITUAL_SORTED_LIST.get(i) == null) {
                Aurumancy.LOGGER.error("Found null in Ritual list.");
                RITUAL_SORTED_LIST.remove(i);
                i -= 1;
            }
            else Aurumancy.LOGGER.debug("Ritual #" + i + " size=" + RITUAL_SORTED_LIST.get(i).getSize());
        }

        // Put in descending order by size
        RITUAL_SORTED_LIST.sort(Ritual::compareTo);
        Collections.reverse(RITUAL_SORTED_LIST);
    }

}
