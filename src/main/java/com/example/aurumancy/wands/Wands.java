package com.example.aurumancy.wands;

import com.example.aurumancy.Aurumancy;
import com.example.aurumancy.networking.ModPacketHandler;
import com.example.aurumancy.networking.messages.SummonLightningMessage;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class Wands {

    public static final DeferredRegister<Item> WAND_ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, "aurumancy");

    public static final RegistryObject<Item> NULL_WAND =
            WAND_ITEMS.register("null_wand", () ->
                    new AbstractWandItem(new Item.Properties().group(Aurumancy.ITEM_GROUP), 0, WandUsageType.CHARGED) { });

    public static final RegistryObject<Item> RITUAL_WAND =
            WAND_ITEMS.register("ritual_wand", () ->
                    new RitualWandItem(new Item.Properties().group(Aurumancy.ITEM_GROUP)));

    public static final RegistryObject<Item> JUMP_WAND =
            WAND_ITEMS.register("jump_wand", () ->
                    new AbstractWandItem(new Item.Properties().group(Aurumancy.ITEM_GROUP), 1, WandUsageType.INSTANT) {
                        @Override
                        protected void instantUsage(World world, PlayerEntity player, Hand hand) {
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
                    });

    public static final RegistryObject<Item> ARROW_WAND =
            WAND_ITEMS.register("arrow_wand", () ->
                    new AbstractWandItem(new Item.Properties().group(Aurumancy.ITEM_GROUP), 0, WandUsageType.CHARGED) {
                        @Override
                        protected void chargedUsage(ItemStack stack, World world, PlayerEntity player) {
                            if (world.isRemote) return;

                            Vec3d eyePos = player.getEyePosition(0);
                            Random rand = new Random();
                            for (int i = 0; i < 10; i += 1) {
                                float velocity = (rand.nextFloat() * 0.75f) + 1.25f;
                                ArrowEntity arrow = new ArrowEntity(world,eyePos.x, eyePos.y, eyePos.z);
                                arrow.setIsCritical(true);
                                arrow.shoot(player, player.rotationPitch, player.rotationYaw, 0.0f, velocity, 10.0f);
                                arrow.tick();
                                world.addEntity(arrow);
                            }
                        }
                    });

    public static final RegistryObject<Item> STORM_WAND =
            WAND_ITEMS.register("storm_wand", () ->
                    new AbstractWandItem(new Item.Properties().group(Aurumancy.ITEM_GROUP), 5, WandUsageType.INSTANT) {
                        @Override
                        protected void instantUsage(World world, PlayerEntity player, Hand hand) {
                            // Only do this on client side
                            if (world.isRemote) return;

                            // Get start and look vector for ray-trace
                            Vec3d eyePos = player.getEyePosition(0);
                            Vec3d lookVec = player.getLook(0);
                            int range = 100;

                            // Find first block within range along the ray
                            RayTraceContext rayContext = new RayTraceContext(
                                    eyePos,
                                    eyePos.add(lookVec.scale(range)),
                                    RayTraceContext.BlockMode.COLLIDER,
                                    RayTraceContext.FluidMode.SOURCE_ONLY, player);
                            RayTraceResult rayResult = world.rayTraceBlocks(rayContext);
                            Vec3d hit = rayResult.getHitVec();
                            LOGGER.debug("Type: " + rayResult.getType());
                            LOGGER.debug("Target: " + hit.toString());

                            if (rayResult.getType() != RayTraceResult.Type.MISS) {
                                // Tell the server to summon lightning
                                ModPacketHandler.INSTANCE.sendToServer(new SummonLightningMessage(new BlockPos(hit.x, hit.y, hit.z)));
                            }
                            else {
                                // Refund the mana cost that was deducted on a miss
                                player.giveExperiencePoints(xpCost);
                            }
                        }
                    });

    public static final RegistryObject<Item> FIREBALL_WAND =
            WAND_ITEMS.register("fireball_wand", () ->
                    new AbstractWandItem(new Item.Properties().group(Aurumancy.ITEM_GROUP), 9, WandUsageType.CHARGED) {
                        @Override
                        protected void chargedUsage(ItemStack stack, World world, PlayerEntity player) {
                            if (world.isRemote) return;

                            Vec3d eyePos = player.getEyePosition(0);
                            Vec3d lookVec = player.getLook(0);
                            FireballEntity fireball = new FireballEntity(world,
                                    eyePos.x,eyePos.y,eyePos.z,
                                    lookVec.x,lookVec.y,lookVec.z);
                            fireball.explosionPower = 3;
                            fireball.shootingEntity = player;
                            fireball.tick();
                            world.addEntity(fireball);
                        }
                    });

}
