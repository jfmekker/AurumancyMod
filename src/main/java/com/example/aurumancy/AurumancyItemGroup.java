package com.example.aurumancy;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class AurumancyItemGroup extends ItemGroup {

    public AurumancyItemGroup(String label) {
        super(label);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(Items.GOLD_NUGGET);
    }
}
