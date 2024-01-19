package org.priestoffern.vs_miniworlds.blockentities

import dev.architectury.registry.menu.ExtendedMenuProvider
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextComponent
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import org.priestoffern.vs_miniworlds.VSMiniBlockEntities
import org.priestoffern.vs_miniworlds.VSMiniBlocks

class MiniWorldCreatorBlockEntity (pos: BlockPos, state: BlockState): BlockEntity(VSMiniBlockEntities.MINI_WORLD_CREATOR.get(),pos, state) {



//    override fun saveAdditional(tag: CompoundTag) {
//        super.saveAdditional(tag)
//        tag.put("inventory", inventory.serializeNBT())
//    }
//
//    override fun load(tag: CompoundTag) {
//        super.load(tag)
//        inventory.deserializeNBT(tag.getCompound("inventory"))
//    }
//    override fun saveExtraData(buf: FriendlyByteBuf?) {
//        buf!!.writeBlockPos(blockPos)
//    }

    fun onPlace(state: BlockState, level: Level, pos: BlockPos, oldState: BlockState, isMoving: Boolean) {

    }
}