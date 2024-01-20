package org.priestoffern.vs_miniworlds.blockentities

import dev.architectury.registry.menu.ExtendedMenuProvider
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
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
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.api.ships.Ship
import org.valkyrienskies.mod.common.shipObjectWorld

class MiniWorldCreatorBlockEntity (pos: BlockPos, state: BlockState): BlockEntity(VSMiniBlockEntities.MINI_WORLD_CREATOR.get(),pos, state) {
    var Chile: Ship? = null


    override fun saveAdditional(tag: CompoundTag) {
        super.saveAdditional(tag)
        if (Chile!=null) tag.putLong("connectedship", (Chile as Ship).id)
        else tag.putLong("connectedship", -1)


    }

    override fun load(tag: CompoundTag) {
        super.load(tag)
        var shipID: Long = tag.getLong("connectedship")
        if (shipID!=-1L) Chile=this.level.shipObjectWorld.allShips.getById(shipID)
    }

    fun assign(ship: Ship) {
        Chile=ship
    }


}