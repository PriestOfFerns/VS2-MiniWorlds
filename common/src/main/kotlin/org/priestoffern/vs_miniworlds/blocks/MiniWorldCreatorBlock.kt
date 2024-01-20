package org.priestoffern.vs_miniworlds.blocks


import dev.architectury.registry.menu.ExtendedMenuProvider
import dev.architectury.registry.menu.MenuRegistry
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
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
import net.minecraft.world.phys.BlockHitResult
import org.joml.Vector3d
import org.joml.Vector3i
import org.joml.Vector3ic
import org.priestoffern.vs_miniworlds.blockentities.MiniWorldCreatorBlockEntity
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.mod.common.dimensionId
import org.valkyrienskies.mod.common.shipObjectWorld

class MiniWorldCreatorBlock(properties: Properties): BaseEntityBlock(properties) {
    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return MiniWorldCreatorBlockEntity(pos, state)
    }


    override fun onPlace(state: BlockState, level: Level, pos: BlockPos, oldState: BlockState, isMoving: Boolean) {
        super.onPlace(state, level, pos, oldState, isMoving)

        if (level.isClientSide) {
            return
        }
        val Slevel:ServerLevel = level as ServerLevel

        val ship:ServerShip = Slevel.server.shipObjectWorld.createNewShipAtBlock(Vector3i(pos.x,pos.y+1,pos.z), true,0.25,level.dimensionId)
        level.setBlock(BlockPos(ship.transform.positionInWorld.x(),ship.transform.positionInWorld.y(),ship.transform.positionInWorld.z()),
            Blocks.ANDESITE_WALL.defaultBlockState(),0
        )


//        val be: BlockEntity? = level.getBlockEntity(pos)
//        if (be !is MiniWorldCreatorBlockEntity)
//            return;
//        val ME: MiniWorldCreatorBlockEntity = be as MiniWorldCreatorBlockEntity

    }


//    override fun use(state: BlockState, level: Level, pos: BlockPos, player: Player, hand: InteractionHand, hit: BlockHitResult): InteractionResult {
//
//
//    }

    override fun getRenderShape(blockState: BlockState): RenderShape {
        return RenderShape.MODEL
    }

}