package org.priestoffern.vs_miniworlds.forge

import dev.architectury.platform.forge.EventBuses
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import org.priestoffern.vs_miniworlds.VSMiniMod
import org.priestoffern.vs_miniworlds.VSMiniMod.init
import org.priestoffern.vs_miniworlds.VSMiniMod.initClient
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(VSMiniMod.MOD_ID)
class VSAddonTemplateModForge {
    init {
        MOD_BUS.addListener { event: FMLClientSetupEvent? ->
            clientSetup(
                event
            )
        }

        EventBuses.registerModEventBus(VSMiniMod.MOD_ID, MOD_BUS)

        init()
    }

    private fun clientSetup(event: FMLClientSetupEvent?) {
        initClient()
    }

    companion object {
        fun getModBus(): IEventBus = MOD_BUS
    }
}
