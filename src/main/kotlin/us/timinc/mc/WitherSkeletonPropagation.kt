package us.timinc.mc

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.damage.DamageTypes
import net.minecraft.server.world.ServerWorld

object WitherSkeletonPropagation : ModInitializer {
	override fun onInitialize() {
		ServerLivingEntityEvents.ALLOW_DEATH.register{ deadEntity, damageSource, _ ->
			val world = deadEntity.world
			if (world.isClient) true

			val serverWorld = world as? ServerWorld ?: return@register true
			if (damageSource.typeRegistryEntry.key.get() == DamageTypes.WITHER && deadEntity.type == EntityType.SKELETON) {
				val newEntity = EntityType.WITHER_SKELETON.spawn(serverWorld, deadEntity.blockPos, SpawnReason.CONVERSION)!!
				newEntity.bodyYaw = deadEntity.bodyYaw
				newEntity.headYaw = deadEntity.headYaw
				deadEntity.remove(Entity.RemovalReason.DISCARDED)
				false
			} else true
		}
	}
}