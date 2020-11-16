package com.jacobmekker.aurumancy.rituals;

import com.jacobmekker.aurumancy.wands.Wands;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Rituals {
    public static final Ritual GOLD_ABSORB =
            new Ritual(-81,
                    null,
                    Arrays.asList(Items.GOLD_BLOCK),
                    new CirclePower(1,0,0)
            );

    public static final Ritual JUMP_WAND_CREATE =
            new Ritual(100,
                    new RitualAction() {
                        @Override
                        public void doAction(World world, BlockPos pos, PlayerEntity player) {
                            Vec3d spawn = new Vec3d(pos.getX(),pos.getY(),pos.getZ());
                            spawn = spawn.add(0.5,1.25,0.5);
                            world.addEntity(new ItemEntity(world, spawn.x, spawn.y, spawn.z,
                                    new ItemStack(Wands.JUMP_WAND.get())));
                        }
                    },
                    Arrays.asList(
                            Items.OAK_LOG,
                            Items.HONEY_BLOCK,
                            Items.SLIME_BALL
                            ),
                    new CirclePower(1,0,0)
            );

    public static final Ritual FIREBALL_WAND_CREATE =
            new Ritual(405,
                    new RitualAction() {
                        @Override
                        public void doAction(World world, BlockPos pos, PlayerEntity player) {
                            Vec3d spawn = new Vec3d(pos.getX(),pos.getY(),pos.getZ());
                            spawn = spawn.add(0.5,1.25,0.5);
                            world.addEntity(new ItemEntity(world, spawn.x, spawn.y, spawn.z,
                                    new ItemStack(Wands.FIREBALL_WAND.get())));
                        }
                    },
                    Arrays.asList(
                            Items.TNT,
                            Items.MAGMA_CREAM,
                            Items.MAGMA_CREAM,
                            Items.MAGMA_CREAM,
                            Items.BLAZE_ROD
                    ),
                    new CirclePower(45, 25, 0)
            );

    public static final Ritual ARROW_WAND_CREATE =
            new Ritual(56,
                    new RitualAction() {
                        @Override
                        public void doAction(World world, BlockPos pos, PlayerEntity player) {
                            Vec3d spawn = new Vec3d(pos.getX(),pos.getY(),pos.getZ());
                            spawn = spawn.add(0.5,1.25,0.5);
                            world.addEntity(new ItemEntity(world, spawn.x, spawn.y, spawn.z,
                                    new ItemStack(Wands.ARROW_WAND.get())));
                        }
                    },
                    Arrays.asList(
                            Items.ARROW,
                            Items.OAK_LOG,
                            Items.STRING,
                            Items.STRING,
                            Items.STRING
                    ),
                    new CirclePower(25, 0, 0)
            );

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
     * List of all rituals to check against.
     */
    public static final List<Ritual> RITUAL_LIST = Arrays.asList(
            GOLD_ABSORB, JUMP_WAND_CREATE, FIREBALL_WAND_CREATE, ARROW_WAND_CREATE
    );

}
