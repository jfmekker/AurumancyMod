package com.example.aurumancy.wands;

import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Wands {

    public static final DeferredRegister<Item> WAND_ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, "aurumancy");

    public static final RegistryObject<Item> NULL_WAND =
            WAND_ITEMS.register("null_wand", () ->
                    new AbstractWandItem(new Item.Properties().group(ItemGroup.COMBAT), 0) {
                        @Override
                        protected void rightClickUsage(World world, PlayerEntity player, Hand hand) { }

                        @Override
                        protected void blockUsage(ItemUseContext context) { }
                    });

    public static final RegistryObject<Item> RITUAL_WAND =
            WAND_ITEMS.register("ritual_wand", () ->
                    new RitualWandItem(new Item.Properties().group(ItemGroup.COMBAT)));

    public static final RegistryObject<Item> JUMP_WAND =
            WAND_ITEMS.register("jump_wand", () ->
                    new AbstractWandItem(new Item.Properties().group(ItemGroup.COMBAT), 1) {
                        @Override
                        protected void rightClickUsage(World world, PlayerEntity player, Hand hand) {
                            if (player.isAirBorne) {
                                if (player.getMotion().y < 0)
                                    player.setVelocity(player.getMotion().x, 0.75, player.getMotion().z);
                                else
                                    player.addVelocity(player.getMotion().x, 0.5, player.getMotion().z);
                            }
                            else {
                                player.addVelocity(0,1.5,0);
                            }
                            player.fallDistance = 0;
                        }

                        @Override
                        protected void blockUsage(ItemUseContext context) { }
                    });

    public static final RegistryObject<Item> ARROW_WAND =
            WAND_ITEMS.register("arrow_wand", () ->
                    new AbstractWandItem(new Item.Properties().group(ItemGroup.COMBAT), 0) {
                        @Override
                        protected void rightClickUsage(World world, PlayerEntity player, Hand hand) {
                            Vec3d eyePos = player.getEyePosition(0);
                            float yaw = player.getYaw(0);
                            float pitch = player.getPitch(0);

                            float f = -MathHelper.sin(yaw * ((float)Math.PI / 180F)) * MathHelper.cos(pitch * ((float)Math.PI / 180F)) * 2;
                            float f1 = -MathHelper.sin(pitch * ((float)Math.PI / 180F)) * 2;
                            float f2 = MathHelper.cos(yaw * ((float)Math.PI / 180F)) * MathHelper.cos(pitch * ((float)Math.PI / 180F)) * 2;

                            ArrowEntity arrow = new ArrowEntity(world,eyePos.x + f, eyePos.y + f1, eyePos.z + f2);
                            world.addEntity(arrow);
                            arrow.shoot(player, pitch, yaw, 0, 1, 0.5f);
                        }

                        @Override
                        protected void blockUsage(ItemUseContext context) { }
                    });

    public static final RegistryObject<Item> STORM_WAND =
            WAND_ITEMS.register("storm_wand", () ->
                    new AbstractWandItem(new Item.Properties().group(ItemGroup.COMBAT), 5) {
                        @Override
                        protected void rightClickUsage(World world, PlayerEntity player, Hand hand) {
                            Vec3d eyePos = player.getEyePosition(0);
                            float yaw = player.getYaw(0);
                            float pitch = player.getPitch(0);
                            int range = 100;

                            float f = -MathHelper.sin(yaw * ((float)Math.PI / 180F)) * MathHelper.cos(pitch * ((float)Math.PI / 180F)) * range;
                            float f1 = -MathHelper.sin(pitch * ((float)Math.PI / 180F)) * range;
                            float f2 = MathHelper.cos(yaw * ((float)Math.PI / 180F)) * MathHelper.cos(pitch * ((float)Math.PI / 180F)) * range;

                            RayTraceContext rayContext = new RayTraceContext(
                                    eyePos,
                                    new Vec3d(eyePos.x + f, eyePos.y + f1, eyePos.z + f2),
                                    RayTraceContext.BlockMode.COLLIDER,
                                    RayTraceContext.FluidMode.SOURCE_ONLY, player);
                            RayTraceResult rayResult = world.rayTraceBlocks(rayContext);
                            Vec3d hit = rayResult.getHitVec();
                            LOGGER.debug("Type: " + rayResult.getType());
                            LOGGER.debug("Target: " + hit.toString());

                            if (rayResult.getType() != RayTraceResult.Type.MISS) {
                                world.addEntity(new LightningBoltEntity(world,hit.x,hit.y,hit.z,false));
                            }
                            else {
                                // refund the mana cost on a miss
                                player.giveExperiencePoints(xpCost);
                            }
                        }

                        @Override
                        protected void blockUsage(ItemUseContext context) { }
                    });

}
