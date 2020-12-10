package com.jacobmekker.aurumancy.items;

import com.jacobmekker.aurumancy.Aurumancy;
import com.jacobmekker.aurumancy.data.BlockProperties;
import com.jacobmekker.aurumancy.networking.ModPacketHandler;
import com.jacobmekker.aurumancy.networking.messages.SummonLightningMessage;

import com.jacobmekker.aurumancy.utils.PlayerEntityHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class AurumancyItems {

    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Aurumancy.MODID);

    public static final RegistryObject<Item> NULL_WAND =
            ITEMS.register("null_wand", () ->
                    new AbstractMagicItem(new Item.Properties(), 0, ItemUsageType.PASSIVE, 20) {
                        @Override
                        protected void onMagicItemUse(PlayerEntity player, Hand hand, BlockPos pos) { }
                    });

    public static final RegistryObject<Item> JUMP_WAND =
            ITEMS.register("jump_wand", () ->
                    new AbstractMagicItem(new Item.Properties(), 1, ItemUsageType.INSTANT, 5) {
                        @Override
                        protected void onMagicItemUse(PlayerEntity player, Hand hand, BlockPos pos) {
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
            ITEMS.register("arrow_wand", () ->
                    new AbstractMagicItem(new Item.Properties(), 2, ItemUsageType.CHARGED, 10) {
                        @Override
                        protected void onMagicItemUse(PlayerEntity player, Hand hand, BlockPos pos) {
                            World world = player.world;
                            ItemStack stack = player.getHeldItem(hand);

                            if (world.isRemote) return;

                            Vec3d eyePos = player.getEyePosition(0);
                            Random rand = new Random();
                            for (int i = 0; i < 10; i += 1) {
                                float velocity = (rand.nextFloat() * 0.75f) + 1.25f;
                                ArrowEntity arrow = new ArrowEntity(world,eyePos.x, eyePos.y, eyePos.z);
                                arrow.setIsCritical(true);
                                arrow.setDamage(6);
                                arrow.shoot(player, player.rotationPitch, player.rotationYaw, 0.0f, velocity, 10.0f);
                                arrow.tick(); // tick once so we don't hit shooter
                                world.addEntity(arrow);
                            }
                        }
                    });

    public static final RegistryObject<Item> STORM_WAND =
            ITEMS.register("storm_wand", () ->
                    new AbstractMagicItem(new Item.Properties(), 5, ItemUsageType.INSTANT, 15) {
                        @Override
                        protected void onMagicItemUse(PlayerEntity player, Hand hand, BlockPos pos) {
                            World world = player.world;

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
                            BlockPos hit = new BlockPos(rayResult.getHitVec());

                            if (rayResult.getType() != RayTraceResult.Type.MISS) {
                                // Tell the server to summon lightning
                                ModPacketHandler.INSTANCE.sendToServer(new SummonLightningMessage(hit.add(1,0,1)));
                            }
                            else {
                                // Refund the mana cost that was deducted on a miss
                                PlayerEntityHelper.AddActualExperienceTotal(player, xpCost);
                            }
                        }
                    });

    public static final RegistryObject<Item> FIREBALL_WAND =
            ITEMS.register("fireball_wand", () ->
                    new AbstractMagicItem(new Item.Properties(), 9, ItemUsageType.CHARGED, 100) {
                        @Override
                        protected void onMagicItemUse(PlayerEntity player, Hand hand, BlockPos pos) {
                            World world = player.world;

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

    public static final RegistryObject<Item> TELEPORT_WAND =
            ITEMS.register("teleport_wand", () ->
                    new AbstractMagicItem(new Item.Properties(), 12, ItemUsageType.CHARGED, 50) {
                        @Override
                        protected void onMagicItemUse(PlayerEntity player, Hand hand, BlockPos pos) {
                            World world = player.world;

                            if (world.isRemote) return;

                            // Get start and look vector for ray-trace
                            Vec3d eyePos = player.getEyePosition(0);
                            Vec3d lookVec = player.getLook(0);
                            int range = 128;

                            // Find first block within range along the ray
                            RayTraceContext rayContext = new RayTraceContext(
                                    eyePos,
                                    eyePos.add(lookVec.scale(range)),
                                    RayTraceContext.BlockMode.COLLIDER,
                                    RayTraceContext.FluidMode.SOURCE_ONLY, player);
                            RayTraceResult rayResult = world.rayTraceBlocks(rayContext);

                            // Move 1 block back towards player
                            Vec3d hit = rayResult.getHitVec().add(lookVec.scale(-1));

                            player.attemptTeleport(hit.x,hit.y,hit.z,false);
                        }
                    });

    public static final RegistryObject<Item> RECALL_WAND =
            ITEMS.register("recall_wand", () ->
                    new RecallWandItem(new Item.Properties(), 15, ItemUsageType.INSTANT, 200));

    public static final RegistryObject<Item> MANA_METER =
            ITEMS.register("mana_meter", () ->
                    new AbstractMagicItem(new Item.Properties(), 0, ItemUsageType.BLOCK, 0) {
                        @Override
                        protected void onMagicItemUse(PlayerEntity player, Hand hand, BlockPos pos) {
                            World world = player.world;
                            if (world.isRemote) return;

                            BlockState block = world.getBlockState(pos);
                            Aurumancy.LOGGER.debug("mana_meter used on " + block.toString());
                            if (block.has(BlockProperties.stored_mana)) {
                                int mana = block.get(BlockProperties.stored_mana);
                                player.sendMessage(new StringTextComponent("Stored mana=" + mana));
                                Aurumancy.LOGGER.info("mana_meter: mana=" + mana);
                            }
                            else {
                                int mana = PlayerEntityHelper.GetActualExperienceTotal(player);
                                player.sendMessage(new StringTextComponent("Your mana=" + mana));
                                Aurumancy.LOGGER.info("mana_meter: mana=" + mana);
                            }
                        }
                    });

}
