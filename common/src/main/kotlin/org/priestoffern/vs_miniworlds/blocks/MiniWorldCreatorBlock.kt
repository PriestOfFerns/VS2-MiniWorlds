package org.priestoffern.vs_miniworlds.blocks


import dev.architectury.registry.menu.ExtendedMenuProvider
import dev.architectury.registry.menu.MenuRegistry
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.BlockHitResult
import org.joml.Vector3d
import org.joml.Vector3dc
import org.joml.Vector3i
import org.joml.Vector3ic
import org.joml.primitives.AABBdc
import org.joml.primitives.AABBic
import org.priestoffern.vs_miniworlds.blockentities.MiniWorldCreatorBlockEntity
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.api.ships.Ship
import org.valkyrienskies.core.apigame.ShipTeleportData
import org.valkyrienskies.core.impl.game.ShipTeleportDataImpl
import org.valkyrienskies.mod.common.dimensionId
import org.valkyrienskies.mod.common.shipObjectWorld
import java.util.*

class MiniWorldCreatorBlock(properties: Properties): BaseEntityBlock(properties) {
    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return MiniWorldCreatorBlockEntity(pos, state)
    }


    override fun onPlace(state: BlockState, level: Level, pos: BlockPos, oldState: BlockState, isMoving: Boolean) {
        super.onPlace(state, level, pos, oldState, isMoving)
        val be: BlockEntity? = level.getBlockEntity(pos)


        if (level.isClientSide || be !is MiniWorldCreatorBlockEntity) return
        val me: MiniWorldCreatorBlockEntity = be as MiniWorldCreatorBlockEntity
        val Slevel:ServerLevel = level as ServerLevel

        val ship:ServerShip = Slevel.server.shipObjectWorld.createNewShipAtBlock(Vector3i(pos.x,pos.y+1,pos.z), false,0.25,level.dimensionId)
        ship.isStatic = true
        me.assign(ship)



        level.setBlock(
            BlockPos(
                ship.transform.positionInShip.x(),
                ship.transform.positionInShip.y(),
                ship.transform.positionInShip.z(),
            ),
            Blocks.POLISHED_ANDESITE.defaultBlockState(), 0
        )
        for (x in 0..3) {
            for (y in 0..3) {
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
        Slevel.server.shipObjectWorld.teleportShip(ship, ShipTeleportDataImpl(newPos = Vector3d(pos.x+0.5,pos.y+1.125,pos.z+0.5)))

    }

//    override fun use(state: BlockState, level: Level, pos: BlockPos, player: Player, hand: InteractionHand, hit: BlockHitResult): InteractionResult {
//
//    }

    override fun getRenderShape(blockState: BlockState): RenderShape {
        return RenderShape.MODEL
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