package thaumcraft.api;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

/**
 * 
 * @author Azanor
 * 
 * Armor or bauble slot items that implement this interface can provide runic shielding.
 * Recharging is handled internally by thaumcraft. 
 *
 */

public interface IRunicArmor {
	
	/**
	 * returns how much charge this item can provide. 
	 */
	public int getRunicCharge(ItemStack itemstack, EntityLivingBase player);
	
	/**
	 * can this item recieve the standard runic augments 
	 */
	public boolean canAcceptRunicAugments(ItemStack itemstack, EntityLivingBase player);
	
	/**
	 * what augments does this item have 
	 */
	public EnumRunicAugment[] getRunicAugments(ItemStack itemstack, EntityLivingBase player);

}
