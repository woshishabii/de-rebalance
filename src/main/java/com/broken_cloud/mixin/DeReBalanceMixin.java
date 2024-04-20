package com.broken_cloud.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerData;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(VillagerEntity.class)
public abstract class DeReBalanceMixin extends MerchantEntity {
	@Shadow public abstract VillagerData getVillagerData();

	public DeReBalanceMixin(EntityType<? extends MerchantEntity> entityType, World world) {
		super(entityType, world);
	}

	@Redirect(method = "levelUp", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/VillagerEntity;fillRecipes()V"))
	private void fillRecipes(VillagerEntity instance) {
		VillagerData villagerData = this.getVillagerData();
		Int2ObjectMap<TradeOffers.Factory[]> int2ObjectMap2 = TradeOffers.PROFESSION_TO_LEVELED_TRADE.get(villagerData.getProfession());
		if (int2ObjectMap2 == null || int2ObjectMap2.isEmpty()) {
			return;
		}
		TradeOffers.Factory[] factorys = int2ObjectMap2.get(villagerData.getLevel());
		if (factorys == null) {
			return;
		}
		TradeOfferList tradeOfferList = this.getOffers();
		this.fillRecipesFromPool(tradeOfferList, factorys, 2);

		System.out.println("fillRecipes() injected");
	}
}