package org.priestoffern.vs_miniworlds.blocks


import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import org.joml.Vector3d
import org.joml.Vector3dc
import org.joml.Vector3i
import org.priestoffern.vs_miniworlds.blockentities.MiniWorldCreatorBlockEntity
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.apigame.constraints.VSAttachmentConstraint
import org.valkyrienskies.core.apigame.constraints.VSConstraint
import org.valkyrienskies.core.impl.game.ShipTeleportDataImpl
import org.valkyrienskies.core.impl.game.ships.ShipDataCommon
import org.valkyrienskies.core.impl.game.ships.ShipTransformImpl
import org.valkyrienskies.mod.common.allShips
import org.valkyrienskies.mod.common.dimensionId
import org.valkyrienskies.mod.common.getShipManagingPos
import org.valkyrienskies.mod.common.shipObjectWorld
import kotlin.math.roundToInt

class MiniWorldCreatorBlock(properties: Properties, val Tier: Double): BaseEntityBlock(properties) {
    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return MiniWorldCreatorBlockEntity(pos, state)
    }


    override fun onPlace(state: BlockState, level: Level, pos: BlockPos, oldState: BlockState, isMoving: Boolean) {
        super.onPlace(state, level, pos, oldState, isMoving)

        val be: BlockEntity? = level.getBlockEntity(pos)


        if (level.isClientSide || be !is MiniWorldCreatorBlockEntity) return
        val me: MiniWorldCreatorBlockEntity = be
        val serverLevel:ServerLevel = level as ServerLevel

        val ship:ServerShip = serverLevel.server.shipObjectWorld.createNewShipAtBlock(Vector3i(pos.x,pos.y+1,pos.z), false,1/Tier,level.dimensionId)
        me.assign(ship)



        level.setBlock(
            BlockPos(
                ship.transform.positionInShip.x(),
                ship.transform.positionInShip.y(),
                ship.transform.positionInShip.z(),
            ),
            Blocks.POLISHED_ANDESITE.defaultBlockState(), 0
        )
        for (x in 0..(Tier-1).toInt()) {
            for (y in 0..(Tier-1).toInt()) {
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


        // Gonna be honest. Most of this code is copied directly from VS2 debug hinges


        val shipCenterPos = BlockPos(
            (ship.transform.positionInShip.x() - 0.5).roundToInt(),
            (ship.transform.positionInShip.y() - 0.5).roundToInt(),
            (ship.transform.positionInShip.z() - 0.5).roundToInt()
        )

        val attachmentOffset0: Vector3dc = Vector3d(0.0, 0.5, 0.0)
        val attachmentOffset1: Vector3dc = Vector3d(0.0, -0.5, 0.0)

        val attachmentLocalPos0: Vector3dc = Vector3d(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5).add(attachmentOffset0)
        val attachmentLocalPos1: Vector3dc =
            Vector3d(shipCenterPos.x + 0.5, shipCenterPos.y + 0.5, shipCenterPos.z + 0.5).add(attachmentOffset1)


        val shipThisIsIn = serverLevel.getShipManagingPos(pos)

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
            (ship as ShipDataCommon).transform = newTransform
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
            (ship as ShipDataCommon).transform = newTransform
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

    }

//    override fun use(state: BlockState, level: Level, pos: BlockPos, player: Player, hand: InteractionHand, hit: BlockHitResult): InteractionResult {
//
//    }

    override fun getRenderShape(blockState: BlockState): RenderShape {
        return RenderShape.MODEL
    }

    override fun onRemove(state: BlockState, level: Level, pos: BlockPos, newState: BlockState, isMoving: Boolean) {


        val be: BlockEntity? = level.getBlockEntity(pos)

        if (!level.isClientSide && be is MiniWorldCreatorBlockEntity) {
            val me: MiniWorldCreatorBlockEntity = be
            if (me.connectedShip==null) return
            (level as ServerLevel).server.shipObjectWorld.deleteShip(be.connectedShip as ServerShip)

        }
        super.onRemove(state, level, pos, newState, isMoving) // super method has to be at the end, because otherwise the BlockEntity gets deleted first

    }

    // This causes collision box errors, so I'm just going to shelf this for now
//    override fun tick(state: BlockState, level: ServerLevel, pos: BlockPos, random: Random) {
//        super.tick(state, level, pos, random)
//
//        val be: BlockEntity? = level.getBlockEntity(pos)
//        if (level.isClientSide || be !is MiniWorldCreatorBlockEntity) return
//        val me: MiniWorldCreatorBlockEntity = be as MiniWorldCreatorBlockEntity
//
//        val ship: Ship? = me.connectedShip
//        if (ship == null) return
//        val sAABB: AABBdc = ship.worldAABB
//
//
//        println(sAABB.minX())
//        val check: AABB = AABB(
//            sAABB.minX(),
//            sAABB.minY() ,
//            sAABB.minZ() ,
//            sAABB.maxX(),
//            sAABB.maxY()+2,
//            sAABB.maxZ()
//        )
//
//        for (ent: LivingEntity in level.getEntitiesOfClass(LivingEntity::class.java,check)) {
//            ent.hurt(DamageSource.GENERIC,1f)
//        }
//    }
}