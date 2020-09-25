package com.example.aurumancy.blocks;

import com.example.aurumancy.Aurumancy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AurumancyBlocks {

    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, "aurumancy");
    public static final DeferredRegister<Item> BLOCK_ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, "aurumancy");

    public static final RegistryObject<Block> COLOR_CHANGE_BLOCK =
            BLOCKS.register("color_change_block",
                    () -> new ColorChangeBlock(Block.Properties.create(Material.ROCK).jumpFactor(1f)));
    public static final RegistryObject<Item> COLOR_CHANGE_BLOCK_ITEM =
            BLOCK_ITEMS.register("color_change_block",
                    () -> new BlockItem(COLOR_CHANGE_BLOCK.get(), new Item.Properties().group(Aurumancy.ITEM_GROUP)));
}
