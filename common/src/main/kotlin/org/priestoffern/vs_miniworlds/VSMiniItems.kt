package org.priestoffern.vs_miniworlds

import dev.architectury.registry.CreativeTabRegistry
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import dev.architectury.registry.registries.DeferredRegister


object VSMiniItems {
    val ITEMS = DeferredRegister.create(VSMiniMod.MOD_ID, Registry.ITEM_REGISTRY)
    val TAB: CreativeModeTab = CreativeTabRegistry.create(
        ResourceLocation(
            VSMiniMod.MOD_ID,
            "vsminiworlds_tab"
        )
    ) {ItemStack(VSMiniBlocks.MINI_WORLD_CREATOR.get())}

    fun register() {
        VSMiniBlocks.registerItems(ITEMS)
        ITEMS.register()
    }

    private infix fun Item.byName(name: String) = ITEMS.register(name) { this }
}