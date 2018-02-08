package thaumcraft.api.casters;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import thaumcraft.api.casters.FocusCore.FocusEffect;

public interface IFocusPartEffect extends IFocusPart {
	
	public default float getBaseCost() { return 0; }

	/**
	 * This method is triggered when the effect is first cast by the player. Intended to be used for FX or similar things.
	 * Server Side Only
	 */
	public default void onCast(Entity caster, @Nullable ItemStack casterStack) { } 
	
	/**
	 * This method is triggered when the medium makes contact with a target or block. 
	 * This is only called once even if the effect can target multiple things. 
	 * Mostly useful to create FX at the point of impact or something else you don't want to trigger for all possible targets.
	 * Server Side Only
	 * 
	 */
	public default void onTrigger(Entity caster, @Nullable ItemStack casterStack, FocusEffect effect, RayTraceResult source, RayTraceResult target) {  }
	
	/**
	 * This method is triggered when the effect is applied to an entity or block. 
	 * The effect target is completely independent of whatever the medium hit depending on modifiers and such.
	 * This is where you would inflict damage, modify blocks, etc.
	 * Server Side Only
	 */
	public void applyToTarget(Entity caster, @Nullable ItemStack casterStack, FocusEffect focusEffect, RayTraceResult source, RayTraceResult target);
	
	public default float getDamageForDisplay(FocusEffect effect) { return 0; }
}
