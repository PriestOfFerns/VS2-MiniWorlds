package org.priestoffern.vs_miniworlds.blockentities

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import org.joml.Vector3d
import org.joml.Vector3dc
import org.joml.Vector3i
import org.priestoffern.vs_miniworlds.VSMiniBlockEntities
import org.priestoffern.vs_miniworlds.blocks.MiniWorldCreatorBlock
import org.priestoffern.vs_miniworlds.util.makeManagedConstraint
import org.valkyrienskies.core.api.ships.LoadedShip
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.api.ships.Ship
import org.valkyrienskies.core.apigame.constraints.VSAttachmentConstraint
import org.valkyrienskies.core.apigame.constraints.VSConstraint
import org.valkyrienskies.core.impl.game.ships.*
import org.valkyrienskies.core.impl.hooks.VSEvents
import org.valkyrienskies.mod.common.dimensionId
import org.valkyrienskies.mod.common.getShipManagingPos
import org.valkyrienskies.mod.common.shipObjectWorld
import org.valkyrienskies.physics_api.ConstraintId
import kotlin.math.roundToInt

class MiniWorldCreatorBlockEntity (pos: BlockPos, state: BlockState): BlockEntity(VSMiniBlockEntities.MINI_WORLD_CREATOR.get(),pos, state) {
    var connectedShip: Ship? = null
    fun createMiniworld() {
        val pos: BlockPos = this.blockPos
        val block: MiniWorldCreatorBlock = this.blockState.block as MiniWorldCreatorBlock
        val tier: Double = block.Tier;
        val level:ServerLevel = this.level as ServerLevel




        val ship: ServerShip = level.server.shipObjectWorld.createNewShipAtBlock(Vector3i(pos.x,pos.y+1,pos.z), false,1/tier,
            level.dimensionId)
        connectedShip=ship



        level.setBlock(
            BlockPos(
                ship.transform.positionInShip.x(),
                ship.transform.positionInShip.y(),
                ship.transform.positionInShip.z(),
            ),
            Blocks.POLISHED_ANDESITE.defaultBlockState(), 0
        )

        createConstraint(ship, tier, pos, level)






        for (x in 0..(tier-1).toInt()) {
            for (y in 0..(tier-1).toInt()) {
                level.setBlock(
                    BlockPos(
                        ship.transform.positionInShip.x() + x,
                        ship.transform.positionInShip.y(),
                        ship.transform.positionInShip.z() + y,
                    ),
                    Blocks.POLISHED_ANDESITE.defaultBlockState(), 0
                )
            }
        }
    }

    fun createConstraint(ship: ServerShip, tier: Double, pos:BlockPos, level:ServerLevel) {
        val shipCenterPos = BlockPos(
            (ship.transform.positionInShip.x() - 0.5).roundToInt(),
            (ship.transform.positionInShip.y() - 0.5).roundToInt(),
            (ship.transform.positionInShip.z() - 0.5).roundToInt()
        )

        val attachmentOffset0: Vector3dc = Vector3d(0.0, 0.5, 0.0)
        val attachmentOffset1: Vector3dc = Vector3d(0.0, -0.5, 0.0)

        val attachmentLocalPos0: Vector3dc = Vector3d(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5).add(attachmentOffset0)
        val attachmentLocalPos1: Vector3dc =
            Vector3d(shipCenterPos.x + tier/2, shipCenterPos.y + 0.5, shipCenterPos.z + tier/2).add(attachmentOffset1)


        val shipThisIsIn = level.getShipManagingPos(pos)

        if (shipThisIsIn != null) {
            // Put the new ship where the old ship is
            val newPos = shipThisIsIn.transform.shipToWorld.transformPosition(attachmentLocalPos0, Vector3d())
            newPos.sub(shipThisIsIn.transform.shipToWorldRotation.transform(attachmentOffset1, Vector3d()))
            val newTransform = ShipTransformImpl(
                newPos,
                ship.transform.positionInShip,
                shipThisIsIn.transform.shipToWorldRotation, // Copy source ship rotation
                ship.transform.shipToWorldScaling
            )
            // Update the ship transform
            (ship as ShipDataCommon).transform =
            newTransform
        } else {
            val newPos = Vector3d(attachmentLocalPos0)
            newPos.sub(attachmentOffset1)
            val newTransform = ShipTransformImpl(
                newPos,
                ship.transform.positionInShip,
                ship.transform.shipToWorldRotation,
                ship.transform.shipToWorldScaling
            )
            // Update the ship transform

            (ship as ShipDataCommon)
                .transform =
                newTransform
        }


        val shipId0 = shipThisIsIn?.id ?: level.shipObjectWorld.dimensionToGroundBodyIdImmutable[level.dimensionId]!!
        val shipId1 = ship.id




        val attachmentCompliance = 1e-10
        val attachmentMaxForce = 1e10
        val attachmentFixedDistance = 0.0
        val attachmentConstraint = VSAttachmentConstraint(
            shipId0, shipId1, attachmentCompliance, attachmentLocalPos0, attachmentLocalPos1,
            attachmentMaxForce, attachmentFixedDistance
        )
        level.makeManagedConstraint(attachmentConstraint)

    }
}