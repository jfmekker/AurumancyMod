package com.jacobmekker.aurumancy.blocks.tileentities;

import com.jacobmekker.aurumancy.Aurumancy;
import com.jacobmekker.aurumancy.blocks.AurumancyBlocks;
import com.jacobmekker.aurumancy.utils.PlayerEntityHelper;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.GameType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ScryingCubeTileEntity extends TileEntity implements ITickableTileEntity {

    public static int SCRY_MAX_TIME = 20 * 60; // 60 seconds
    public static int SCRY_MANA_COST = 18;

    private int scry_ticks = 0;

    private PlayerEntity player;
    private UUID player_id;
    private BlockPos return_pos;
    private GameType prev_mode;

    @SubscribeEvent
    public static void registerTE(RegistryEvent.Register<TileEntityType<?>> evt) {
        evt.getRegistry().register(AurumancyBlocks.SCRYING_CUBE_TILE_ENTITY_TYPE.get());
    }

    public ScryingCubeTileEntity() {
        super(AurumancyBlocks.SCRYING_CUBE_TILE_ENTITY_TYPE.get());
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        scry_ticks = compound.getInt("scry_ticks");
        if (compound.contains("return_pos_x")) return_pos = new BlockPos(
                compound.getInt("return_pos_x"),
                compound.getInt("return_pos_y"),
                compound.getInt("return_pos_z")
        );
        prev_mode = GameType.getByID(compound.getInt("prev_mode"));
        player_id = compound.getUniqueId("player_id");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt("scry_ticks", scry_ticks);
        if (prev_mode == null) prev_mode = GameType.NOT_SET;
        compound.putInt("prev_mode", prev_mode.getID());
        if (return_pos != null) {
            compound.putInt("return_pos_x", return_pos.getX());
            compound.putInt("return_pos_y", return_pos.getY());
            compound.putInt("return_pos_z", return_pos.getZ());
        }
        if (player_id != null) compound.putUniqueId("player_id", player_id);
        return compound;
    }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;

        if (player_id != null) {
            // Only tick if player is in the world
            if (player == null) player = world.getPlayerByUuid(player_id);
            if (player == null) return;

            scry_ticks += 1;
            if (scry_ticks >= SCRY_MAX_TIME) {
                Aurumancy.LOGGER.trace("Scrying Cube time done.");
                stopScry();
            }
        }
    }

    public void startScry(PlayerEntity user) {
        if (world == null || world.isRemote) return;

        if (player != null || player_id != null) {
            user.sendMessage(new StringTextComponent("Scrying cube already in use."));
            return;
        }
        else if (PlayerEntityHelper.GetActualExperienceTotal(user) < SCRY_MANA_COST) {
            user.sendMessage(new StringTextComponent("Not enough mana to scry."));
            return;
        }
        PlayerEntityHelper.AddActualExperienceTotal(user, SCRY_MANA_COST);

        player = user;
        player_id = user.getUniqueID();
        return_pos = user.getPosition();
        prev_mode = ((ServerPlayerEntity)user).interactionManager.getGameType();

        Aurumancy.LOGGER.trace("Starting scry.");
        player.setGameType(GameType.SPECTATOR);

        markDirty();
    }

    public void stopScry() {
        if (player == null || player_id == null) return;

        player.fallDistance = 0; // make sure we don't take random fall damage

        if (world != null && player.dimension == world.dimension.getType())
            player.setPositionAndUpdate(return_pos.getX(), return_pos.getY(), return_pos.getZ());

        if (prev_mode != GameType.NOT_SET) player.setGameType(prev_mode);
        else Aurumancy.LOGGER.error("Scry Cube could not restore gamemode.");

        player = null;
        player_id = null;
        return_pos = null;
        prev_mode = GameType.NOT_SET;

        scry_ticks = 0;
        markDirty();
    }

    @Override
    public void remove() {
        stopScry();
        Aurumancy.LOGGER.trace("Scrying Cube TileEntity removed, stopping scry.");
        super.remove();
    }
}
