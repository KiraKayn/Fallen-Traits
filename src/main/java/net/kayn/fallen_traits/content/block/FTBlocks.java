package net.kayn.fallen_traits.content.block;

import net.kayn.fallen_traits.FallenTraits;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FTBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, FallenTraits.MOD_ID);

    public static final RegistryObject<Block> TRANSCENDENT_BLOCK =
            BLOCKS.register("transcendent_block",
                    () -> new Block(
                            BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK)
                                    .strength(8.0F, 2400.0F)
                    ));

    private FTBlocks() {
    }
}