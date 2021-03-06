package com.jacobmekker.aurumancy;

import com.jacobmekker.aurumancy.blocks.AurumancyBlocks;
import com.jacobmekker.aurumancy.data.AurumancySavedData;
import com.jacobmekker.aurumancy.networking.ModPacketHandler;
import com.jacobmekker.aurumancy.items.AurumancyItems;

import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.stream.Collectors;

@Mod(Aurumancy.MODID)
public class Aurumancy
{
    public static final String MODID = "aurumancy";

    public static final ItemGroup ITEM_GROUP = new AurumancyItemGroup("Aurumancy");

    public static final Logger LOGGER = LogManager.getLogger();

    public Aurumancy() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        AurumancyItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        AurumancyBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        AurumancyBlocks.BLOCK_ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        AurumancyBlocks.TILE_ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("Aurumancy pre-init.");

        ModPacketHandler.registerMessages();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        // InterModComms.sendTo(Aurumancy.MODID, "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("Aurumancy: server starting.");

        ServerWorld w = event.getServer().getWorld(DimensionType.OVERWORLD);
        AurumancySavedData t = w.getSavedData().getOrCreate(
                AurumancySavedData::new, AurumancySavedData.DATA_NAME);
    }

    @SubscribeEvent
    public void onServerEnding(FMLServerStoppingEvent event) {
        // do something when the server ends
        LOGGER.info("Aurumancy: server ending.");

        ServerWorld w = event.getServer().getWorld(DimensionType.OVERWORLD);
        AurumancySavedData t = new AurumancySavedData();
        w.getSavedData().set(t);
        t.markDirty();
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("Aurumancy Register Block");
        }
    }
}
