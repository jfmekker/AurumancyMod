package com.example.aurumancy.wands;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Wands {

    public static final DeferredRegister<Item> WAND_ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, "aurumancy");

    public static final RegistryObject<Item> RITUAL_WAND = WAND_ITEMS.register("ritual_wand", () -> new RitualWand(new Item.Properties().group(ItemGroup.COMBAT)));

    public static final RegistryObject<Item> JUMP_WAND = WAND_ITEMS.register("jump_wand", () -> new JumpWand(new Item.Properties().group(ItemGroup.COMBAT)));

    public static final RegistryObject<Item> ARROW_WAND = WAND_ITEMS.register("arrow_wand", () -> new ArrowWand(new Item.Properties().group(ItemGroup.COMBAT)));

}
