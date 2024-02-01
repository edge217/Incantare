package com.edgeburnmedia.incantare.incantare;

import com.edgeburnmedia.incantare.incantare.enchantment.ExperienceProficiencyEnchantment;
import com.edgeburnmedia.incantare.incantare.enchantment.GroundingEnchantment;
import com.edgeburnmedia.incantare.incantare.enchantment.ShatterproofEnchantment;
import com.mojang.logging.LogUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Incantare.MODID)
public class Incantare {

	// Define mod id in a common place for everything to reference
	public static final String MODID = "incantare";
	// Directly reference a slf4j logger
	private static final Logger LOGGER = LogUtils.getLogger();
	// Create a Deferred Register to hold Blocks which will all be registered under the "Incantare" namespace
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(
		ForgeRegistries.BLOCKS, MODID);
	// Create a Deferred Register to hold Items which will all be registered under the "Incantare" namespace
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(
		ForgeRegistries.ITEMS, MODID);

	public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(
		ForgeRegistries.ENCHANTMENTS, MODID);

	public static final RegistryObject<Enchantment> SHATTERPROOF_ENCHANTMENT = ENCHANTMENTS.register("shatterproof",
		ShatterproofEnchantment::new);
	public static final RegistryObject<Enchantment> EXPERIENCE_PROFICIENCY_ENCHANTMENT = ENCHANTMENTS.register("experience_proficiency", ExperienceProficiencyEnchantment::new);
	public static final RegistryObject<Enchantment> GROUNDING_ENCHANTMENT = ENCHANTMENTS.register("grounding", GroundingEnchantment::new);

	public Incantare() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		// Register the commonSetup method for modloading
		modEventBus.addListener(this::commonSetup);

		ENCHANTMENTS.register(modEventBus);

		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void commonSetup(final FMLCommonSetupEvent event) {

		// Some common setup code
		LOGGER.info("HELLO FROM COMMON SETUP");
		LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
	}

	// You can use SubscribeEvent and let the Event Bus discover methods to call
	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event) {
		// Do something when the server starts
		LOGGER.info("HELLO from server starting");
	}
}
