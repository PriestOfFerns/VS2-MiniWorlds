package org.priestoffern.vs_miniworlds.blockentities

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import org.priestoffern.vs_miniworlds.VSMiniBlockEntities
import org.valkyrienskies.core.api.ships.Ship
import org.valkyrienskies.mod.common.shipObjectWorld

class MiniWorldCreatorBlockEntity (pos: BlockPos, state: BlockState): BlockEntity(VSMiniBlockEntities.MINI_WORLD_CREATOR.get(),pos, state) {
    var connectedShip: Ship? = null
    var shipID: Long = -1L;

    override fun saveAdditional(tag: CompoundTag) {
        super.saveAdditional(tag)

        if (shipID!=-1L) connectedShip=this.level.shipObjectWorld.allShips.getById(shipID)
        if (connectedShip!=null) { // This is probably a stupid way to do it, but I can't put this in load, because load happens before the ships load in
            tag.putLong("connectedship", (connectedShip as Ship).id)
            shipID = (connectedShip as Ship).id
        }
        else tag.putLong("connectedship", -1)


    }

    override fun load(tag: CompoundTag) {
        super.load(tag)
        shipID = tag.getLong("connectedship")

    }

    fun assign(ship: Ship) {
        connectedShip=ship
    }


}