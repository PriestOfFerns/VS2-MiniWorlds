package org.priestoffern.vs_miniworlds

import dev.architectury.registry.registries.DeferredRegister
import net.minecraft.core.Registry
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.Material
import org.priestoffern.vs_miniworlds.blocks.MiniWorldCreatorBlock

object  VSMiniBlocks {
    private val BLOCKS = DeferredRegister.create(VSMiniMod.MOD_ID, Registry.BLOCK_REGISTRY)

    @JvmField var MINI_WORLD_CREATOR = BLOCKS.register("mini_world_creator") { MiniWorldCreatorBlock(BlockBehaviour.Properties.of(Material.METAL).strength(2.0f),4.0)}
    @JvmField var MINI_WORLD_CREATOR_2 = BLOCKS.register("mini_world_creator_2") { MiniWorldCreatorBlock(BlockBehaviour.Properties.of(Material.METAL).strength(2.0f),16.0)}
    @JvmField var MINI_WORLD_CREATOR_3 = BLOCKS.register("mini_world_creator_3") { MiniWorldCreatorBlock(BlockBehaviour.Properties.of(Material.METAL).strength(2.0f),32.0)} // DO NOT set tier to >= 64. It WILL make the game run at 0 FPS whenever you're looking at it.


    fun register() {
        BLOCKS.register()

    }

    fun registerItems(items: DeferredRegister<Item>) {
        BLOCKS.forEach {
            items.register(it.id) { BlockItem(it.get(), Item.Properties().tab(VSMiniItems.TAB)) }
        }
    }


}