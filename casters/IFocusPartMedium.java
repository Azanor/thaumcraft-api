package thaumcraft.api.casters;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import thaumcraft.api.casters.FocusCore.FocusEffect;

public interface IFocusPartMedium extends IFocusPart {

	public default EnumFocusCastMethod getCastMethod() { return EnumFocusCastMethod.DEFAULT; }
	
	enum EnumFocusCastMethod {
		DEFAULT, //Spell is cast instantly and as long as you keep casting
		CHARGE, //Spell is cast when player releases the charge. The longer you charge, the more effective & expensive the spell will be. Range of 50% - 200%
		INSTANT; //Instantly takes effect when cast with no cooldown.
	}
		
	
	/**
	 * Determines the number of ticks that you will need to 'charge' the gauntlet before the spell is cast. 
	 * Only applies to DEFAULT and CHARGE methods 
	 * @return
	 */
	public default int getChargeTime() { return 10; }
	

	/**
	 * Server Side Only
	 * @param caster
	 * @param casterStack
	 * @param core
	 * @param source
	 * @param castVector
	 * @return
	 */
	public boolean onCast(Entity caster, ItemStack casterStack, FocusCore core, RayTraceResult source, Vec3d castVector);
	
	/**
	 * This method will be called for one connected EFFECT when that effect is being processed.  
	 * Server Side Only
	 * @param caster
	 * @param casterStack
	 * @param focusEffect
	 * @param target
	 * @param source
	 * @param castVector
	 * @return if true normal effect processing will NOT occur. It will be assumed it will be handled in this method.
	 */
	public default boolean applyAtEffect(Entity caster, @Nullable ItemStack casterStack, FocusEffect focusEffect, RayTraceResult target, RayTraceResult source, Vec3d castVector) { return false; }
}
