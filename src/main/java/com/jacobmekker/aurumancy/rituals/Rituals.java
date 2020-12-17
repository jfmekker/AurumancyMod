package com.jacobmekker.aurumancy.rituals;

import com.jacobmekker.aurumancy.blocks.AurumancyBlocks;
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
                    new CirclePower(5,0,0)
            );

    public static final Ritual FIREBALL_WAND_CREATE =
            new Ritual(495,
                    new ItemCreationRitualAction(AurumancyItems.FIREBALL_WAND.get()),
                    Arrays.asList(
                            Items.TNT,
                            Items.MAGMA_CREAM,
                            Items.MAGMA_CREAM,
                            Items.MAGMA_CREAM,
                            Items.BLAZE_ROD
                    ),
                    new CirclePower(57, 29, 0)
            );

    public static final Ritual ARROW_WAND_CREATE =
            new Ritual(54,
                    new ItemCreationRitualAction(AurumancyItems.ARROW_WAND.get()),
                    Arrays.asList(
                            Items.ARROW,
                            Items.OAK_LOG,
                            Items.STRING,
                            Items.STRING,
                            Items.STRING
                    ),
                    new CirclePower(21, 0, 0)
            );

    public static final Ritual TELEPORT_WAND_CREATE =
            new Ritual(495,
                    new ItemCreationRitualAction(AurumancyItems.TELEPORT_WAND.get()),
                    Arrays.asList(
                            Items.STICK,
                            Items.ENDER_PEARL,
                            Items.ENDER_PEARL,
                            Items.BLACK_WOOL
                    ),
                    new CirclePower(61, 0, 0)
            );

    public static final Ritual RECALL_WAND_CREATE =
            new Ritual(990,
                    new ItemCreationRitualAction(AurumancyItems.RECALL_WAND.get()),
                    Arrays.asList(
                            AurumancyItems.TELEPORT_WAND.get(),
                            Items.ENDER_EYE,
                            Items.ENDER_EYE,
                            Items.DIAMOND
                    ),
                    new CirclePower(50, 10, 0)
            );

    public static final Ritual STORM_WAND_CREATE =
            new Ritual(256,
                    new ItemCreationRitualAction(AurumancyItems.STORM_WAND.get()),
                    Arrays.asList(
                            Items.STICK,
                            Items.IRON_BARS,
                            Items.IRON_BARS,
                            Items.IRON_BARS,
                            Items.IRON_BARS,
                            Items.PHANTOM_MEMBRANE
                    ),
                    new CirclePower(30, 0, 0)
            );

    public static final Ritual TELEPORT_CIRCLE_CREATE =
            new Ritual(162,
                    new ItemCreationRitualAction(AurumancyBlocks.TELEPORT_CIRCLE_ITEM.get()),
                    Arrays.asList(
                            AurumancyItems.RECALL_WAND.get(),
                            Items.END_STONE_BRICKS,
                            Items.GOLD_BLOCK,
                            Items.INK_SAC
                    ),
                    new CirclePower(50, 40, 1)
            );

    public static final Ritual MANA_FERTILIZER_CREATE =
            new Ritual(27,
                    new ItemCreationRitualAction(AurumancyBlocks.MANA_FERTILIZER_ITEM.get()),
                    Arrays.asList(
                            Items.OAK_LEAVES,
                            Items.GOLD_BLOCK,
                            Items.BONE_BLOCK,
                            Items.BONE_MEAL,
                            Items.BONE_MEAL,
                            Items.BONE_MEAL
                    ),
                    new CirclePower(17, 1, 0)
            );

    public static final Ritual SCRYING_CUBE_CREATE =
            new Ritual(810,
                    new ItemCreationRitualAction(AurumancyBlocks.SCRYING_CUBE_ITEM.get()),
                    Arrays.asList(
                            Items.ENDER_EYE,
                            Items.GLASS
                    ),
                    new CirclePower(50, 50, 8)
            );

    /**
     * List of all rituals to check against.
     */
    public static final List<Ritual> RITUAL_LIST = Arrays.asList(
            GOLD_ABSORB, JUMP_WAND_CREATE, FIREBALL_WAND_CREATE, ARROW_WAND_CREATE,
            TELEPORT_WAND_CREATE, RECALL_WAND_CREATE, STORM_WAND_CREATE,
            TELEPORT_CIRCLE_CREATE, MANA_FERTILIZER_CREATE, SCRYING_CUBE_CREATE
    );

}
