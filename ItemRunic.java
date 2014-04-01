package thaumcraft.api;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class ItemRunic extends Item implements IRunicArmor  {
	
	int charge;
	
	public ItemRunic (int charge)
    {
        super();
        this.charge = charge;
    }
			
	@Override
	public int getRunicCharge(ItemStack itemstack, EntityLivingBase player) {
		return charge;
	}
	
	@Override
	public boolean canAcceptRunicAugments(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}

	@Override
	public void addInformation(ItemStack stack,EntityPlayer player, List list, boolean par4) {
		list.add(EnumChatFormatting.GOLD+StatCollector.translateToLocal("item.runic.charge")+" +"+getRunicCharge(stack,player));
		EnumRunicAugment[] augs = getRunicAugments(stack,player);
		if (augs!=null) 
			for (EnumRunicAugment augment:augs) {
				list.add(EnumChatFormatting.BLUE+StatCollector.translateToLocal("item.runic."+augment));
			}
	}

	@Override
	public EnumRunicAugment[] getRunicAugments(ItemStack itemstack,	EntityLivingBase player) {
		return null;
	}	

}
