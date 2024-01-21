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

    @JvmField var MINI_WORLD_CREATOR = BLOCKS.register("mini_world_creator") { MiniWorldCreatorBlock(BlockBehaviour.Properties.of(Material.METAL).strength(2.0f))}


    fun register() {
        BLOCKS.register()

    }

    fun registerItems(items: DeferredRegister<Item>) {
        BLOCKS.forEach {
            items.register(it.id) { BlockItem(it.get(), Item.Properties().tab(VSMiniItems.TAB)) }
        }
    }


}