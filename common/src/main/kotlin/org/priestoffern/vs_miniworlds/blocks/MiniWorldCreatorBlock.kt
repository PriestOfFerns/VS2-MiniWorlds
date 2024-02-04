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


        if (level.isClientSide() || be !is MiniWorldCreatorBlockEntity) return

        val me: MiniWorldCreatorBlockEntity = be
        val serverLevel:ServerLevel = level as ServerLevel

        me.createMiniworld()


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
            me.onRemove()

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