package com.jacobmekker.aurumancy.rituals;

import com.jacobmekker.aurumancy.items.AurumancyItems;

import net.minecraft.item.Items;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Rituals {
    public static final Ritual GOLD_ABSORB =
            new Ritual(-81,
                    null,
                    Collections.singletonList(Items.GOLD_BLOCK),
                    new CirclePower(1,0,0)
            );

    public static final Ritual JUMP_WAND_CREATE =
            new Ritual(100,
                    new ItemCreationRitualAction(AurumancyItems.JUMP_WAND.get()),
                    Arrays.asList(
                            Items.OAK_LOG,
                            Items.HONEY_BLOCK,
                            Items.SLIME_BALL
                    ),
                    new CirclePower(1,0,0)
            );

    public static final Ritual FIREBALL_WAND_CREATE =
            new Ritual(405,
                    new ItemCreationRitualAction(AurumancyItems.FIREBALL_WAND.get()),
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
                    new ItemCreationRitualAction(AurumancyItems.ARROW_WAND.get()),
                    Arrays.asList(
                            Items.ARROW,
                            Items.OAK_LOG,
                            Items.STRING,
                            Items.STRING,
                            Items.STRING
                    ),
                    new CirclePower(25, 0, 0)
            );

    /**
     * List of all rituals to check against.
     */
    public static final List<Ritual> RITUAL_LIST = Arrays.asList(
            GOLD_ABSORB, JUMP_WAND_CREATE, FIREBALL_WAND_CREATE, ARROW_WAND_CREATE
    );

}
