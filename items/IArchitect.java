package thaumcraft.api.items;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IArchitect {
	
	/*
	 * Items that should display the planning block display like the Shovel of the Earthmover should extend this.
	 * Also works for focus effects that can be linked to the 'Plan' medium.
	 */

	/**
	 * Returns a list of blocks that should be highlighted in world. The starting point is whichever block the player currently has highlighted in the world.
	 */
	public ArrayList<BlockPos> getArchitectBlocks(ItemStack stack, World world, 
			BlockPos pos, EnumFacing side, EntityPlayer player);
	
	/**
	 * which axis should be displayed. 
	 */
	public boolean showAxis(ItemStack stack, World world, EntityPlayer player, EnumFacing side, 
			EnumAxis axis);
	
	public enum EnumAxis {
		X, // east / west
		Y, // up / down
		Z; // north / south
	}
}
