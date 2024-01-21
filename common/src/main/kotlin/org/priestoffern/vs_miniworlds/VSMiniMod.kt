package org.priestoffern.vs_miniworlds

object VSMiniMod {
    const val MOD_ID = "vs_miniworlds"
    @JvmStatic
    fun init() {
        VSMiniBlocks.register()
        VSMiniItems.register()
        VSMiniBlockEntities.register()

    }

    @JvmStatic
    fun initClient() {
    }
}
