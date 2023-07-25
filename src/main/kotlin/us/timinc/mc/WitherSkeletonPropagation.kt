package us.timinc.mc

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.damage.DamageTypes
import net.minecraft.entity.mob.SkeletonEntity
import net.minecraft.entity.mob.WitherSkeletonEntity

object WitherSkeletonPropagation : ModInitializer {
	override fun onInitialize() {
		ServerLivingEntityEvents.ALLOW_DEATH.register{ deadEntity, damageSource, flt ->
			if (damageSource.typeRegistryEntry.key.get() == DamageTypes.WITHER && deadEntity is SkeletonEntity) {
				var world = deadEntity.world
				deadEntity.remove(Entity.RemovalReason.DISCARDED)
				var newEntity = WitherSkeletonEntity(EntityType.WITHER_SKELETON, world)
				newEntity.setPosition(deadEntity.pos)
				world.spawnEntity(newEntity)
				return@register false
			}
			return@register true
		}
	}
}