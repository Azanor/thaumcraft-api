package thaumcraft.api.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

/**
 * 
 * @author Azanor
 * 
 * Equipped or held items that extend this class will be able to perform most functions that 
 * goggles of revealing can.
 *
 */

public interface IGoggles {
	
	/*
	 * If this method returns true things like block essentia contents will be shown.
	 */
	public boolean showIngamePopups(ItemStack itemstack, EntityLivingBase player);

}
