package com.jacobmekker.aurumancy.blocks;

import com.jacobmekker.aurumancy.Aurumancy;
import com.jacobmekker.aurumancy.blocks.tileentities.ManaFertilizerTileEntity;
import com.jacobmekker.aurumancy.blocks.tileentities.ScryingCubeTileEntity;
import com.jacobmekker.aurumancy.blocks.tileentities.StarlightCollectorTileEntity;
import com.jacobmekker.aurumancy.blocks.tileentities.TeleportCircleTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AurumancyBlocks {

    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Aurumancy.MODID);
    public static final DeferredRegister<Item> BLOCK_ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Aurumancy.MODID);
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, Aurumancy.MODID);

    public static final RegistryObject<Block> STARLIGHT_COLLECTOR =
            BLOCKS.register("starlight_collector",
                    () -> new StarlightCollectorBlock(Block.Properties.create(Material.GLASS)));
    public static final RegistryObject<Item> STARLIGHT_COLLECTOR_ITEM =
            BLOCK_ITEMS.register("starlight_collector",
                    () -> new BlockItem(STARLIGHT_COLLECTOR.get(), new Item.Properties().group(Aurumancy.ITEM_GROUP)));
    public static final RegistryObject<TileEntityType<?>> STARLIGHT_COLLECTOR_TILE_ENTITY_TYPE =
            TILE_ENTITY_TYPES.register("starlight_collector",
                    () -> TileEntityType.Builder.create(
                            StarlightCollectorTileEntity::new, STARLIGHT_COLLECTOR.get()
                    ).build(null));

    public static final RegistryObject<Block> TELEPORT_CIRCLE =
            BLOCKS.register("teleport_circle",
                    () -> new TeleportCircleBlock(Block.Properties.create(Material.ROCK)));
    public static final RegistryObject<Item> TELEPORT_CIRCLE_ITEM =
            BLOCK_ITEMS.register("teleport_circle",
                    () -> new BlockItem(TELEPORT_CIRCLE.get(), new Item.Properties().group(Aurumancy.ITEM_GROUP)));
    public static final RegistryObject<TileEntityType<?>> TELEPORT_CIRCLE_TILE_ENTITY_TYPE =
            TILE_ENTITY_TYPES.register("teleport_circle",
                    () -> TileEntityType.Builder.create(
                            TeleportCircleTileEntity::new, TELEPORT_CIRCLE.get()
                    ).build(null));

    public static final RegistryObject<Block> RITUAL_CIRCLE =
            BLOCKS.register("ritual_circle",
                    () -> new RitualCircleBlock(Block.Properties.create(Material.ROCK)));
    public static final RegistryObject<Item> RITUAL_CIRCLE_ITEM =
            BLOCK_ITEMS.register("ritual_circle",
                    () -> new BlockItem(RITUAL_CIRCLE.get(), new Item.Properties().group(Aurumancy.ITEM_GROUP)));

    public static final RegistryObject<Block> MANA_FERTILIZER =
            BLOCKS.register("mana_fertilizer",
                    () -> new ManaFertilizerBlock(Block.Properties.create(Material.GLASS)));
    public static final RegistryObject<Item> MANA_FERTILIZER_ITEM =
            BLOCK_ITEMS.register("mana_fertilizer",
                    () -> new BlockItem(MANA_FERTILIZER.get(), new Item.Properties().group(Aurumancy.ITEM_GROUP)));
    public static final RegistryObject<TileEntityType<?>> MANA_FERTILIZER_TILE_ENTITY_TYPE =
            TILE_ENTITY_TYPES.register("mana_fertilizer",
                    () -> TileEntityType.Builder.create(
                            ManaFertilizerTileEntity::new, MANA_FERTILIZER.get()
                    ).build(null));

    public static final RegistryObject<Block> SCRYING_CUBE =
            BLOCKS.register("scry_cube",
                    () -> new ScryingCubeBlock(Block.Properties.create(Material.GLASS)));
    public static final RegistryObject<Item> SCRYING_CUBE_ITEM =
            BLOCK_ITEMS.register("scry_cube",
                    () -> new BlockItem(SCRYING_CUBE.get(), new Item.Properties().group(Aurumancy.ITEM_GROUP)));
    public static final RegistryObject<TileEntityType<?>> SCRYING_CUBE_TILE_ENTITY_TYPE =
            TILE_ENTITY_TYPES.register("scry_cube",
                    () -> TileEntityType.Builder.create(
                            ScryingCubeTileEntity::new, SCRYING_CUBE.get()
                    ).build(null));
}
