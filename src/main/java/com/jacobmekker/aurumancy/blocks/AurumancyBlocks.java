package com.jacobmekker.aurumancy.blocks;

import com.jacobmekker.aurumancy.Aurumancy;

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

    public static final RegistryObject<Block> COLOR_CHANGE_BLOCK =
            BLOCKS.register("color_change_block",
                    () -> new ColorChangeBlock(Block.Properties.create(Material.ROCK).jumpFactor(1f)));
    public static final RegistryObject<Item> COLOR_CHANGE_BLOCK_ITEM =
            BLOCK_ITEMS.register("color_change_block",
                    () -> new BlockItem(COLOR_CHANGE_BLOCK.get(), new Item.Properties().group(Aurumancy.ITEM_GROUP)));

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
}
