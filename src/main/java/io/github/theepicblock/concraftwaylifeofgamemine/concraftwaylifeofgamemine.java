package io.github.theepicblock.concraftwaylifeofgamemine;

import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.event.ChunkComponentCallback;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class concraftwaylifeofgamemine implements ModInitializer {
    public static final ConwayGameOfBlock CONWAY_GAME_OF_BLOCK = new ConwayGameOfBlock(FabricBlockSettings.of(Material.GLASS));
    public static final String MOD_ID = "concraftwaylifeofgamemine";
    public static final ComponentType<ChunkLifeUpdateHolder> UPDATEHOLDER = ComponentRegistry.INSTANCE.registerIfAbsent(new Identifier(MOD_ID, "update_holder"), ChunkLifeUpdateHolder.class)

    @Override
    public void onInitialize() {
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "conway_game_of_block"), CONWAY_GAME_OF_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "conway_game_of_block"), new BlockItem(CONWAY_GAME_OF_BLOCK, new Item.Settings().group(ItemGroup.REDSTONE)));
    }

    static {
        ChunkComponentCallback.EVENT.register((chunk, components) -> components.put(UPDATEHOLDER, new ChunkLifeUpdateHolder()));
    }
}
