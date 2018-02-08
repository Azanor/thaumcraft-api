package thaumcraft.api.casters;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import thaumcraft.api.casters.FocusCore.FocusEffect;

public interface IFocusPartModifier extends IFocusPart {
	
	/**
	 * When modifiers get applied they are applied in a specific order from lowest to highest. 
	 * You should place modifiers that simply modify stats like power and time first, while modifiers
	 * that massively alter what happens like Scattershot should be last. 
	 * @return the order in which the modifier should be applied.
	 */
	public byte getApplicationOrder();
	
	/**
	 * This method will be called for all connected parts at cast time. 
	 * Server Side Only
	 * @param caster
	 * @param casterStack
	 * @param core
	 * @param castVector
	 * @return if true normal cast processing will NOT occur. It will be assumed it will be handled in this method.
	 */
	public default boolean applyAtCast(Entity caster, @Nullable ItemStack casterStack, FocusCore core, RayTraceResult source, Vec3d castVector) { return false; }

	/**
	 * This method will be called for all connected MEDIUMS just before focus effects will be processed.
	 * Server Side Only
	 * @param caster
	 * @param casterStack
	 * @param core
	 * @param target
	 * @param castVector
	 * @return if true normal effect processing will NOT occur. It will be assumed it will be handled in this method.
	 */
	public default boolean applyAtMediumResolution(Entity caster,  @Nullable ItemStack casterStack, FocusCore core, RayTraceResult target, RayTraceResult source, Vec3d castVector) { return false; }
	
	/**
	 * This method will be called for one connected EFFECT when that effect is being processed.  
	 * Server Side Only
	 * @param caster
	 * @param casterStack
	 * @param focusEffect
	 * @param castVector
	 * @return if true normal effect processing will NOT occur. It will be assumed it will be handled in this method.
	 */
	public default  boolean applyAtEffect(Entity caster, @Nullable ItemStack casterStack, FocusEffect focusEffect, RayTraceResult target, RayTraceResult source, Vec3d castVector) { return false; }
	
}
