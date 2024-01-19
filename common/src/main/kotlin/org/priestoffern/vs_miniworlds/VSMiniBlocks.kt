package org.priestoffern.vs_miniworlds

import dev.architectury.registry.registries.DeferredRegister
import net.minecraft.core.Registry
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.Material

object  VSMiniBlocks {
    private val BLOCKS = DeferredRegister.create(VSMiniMod.MOD_ID, Registry.BLOCK_REGISTRY)
    private val ITEMS = DeferredRegister.create(VSMiniMod.MOD_ID, Registry.ITEM_REGISTRY)

    @JvmField var BALLISTIC_ACCELERATOR = BLOCKS.register("ballistic_accelerator") {Block(BlockBehaviour.Properties.of(Material.METAL).strength(2.0f))}


    fun register() {
        BLOCKS.register()

        registerItems(ITEMS)
        ITEMS.register()
    }

    fun registerItems(items: DeferredRegister<Item>) {
        BLOCKS.forEach {
            items.register(it.id) { BlockItem(it.get(), Item.Properties().tab(CreativeModeTab.TAB_COMBAT)) }
        }
    }
}