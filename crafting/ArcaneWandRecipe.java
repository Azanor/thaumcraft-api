package thaumcraft.api.crafting;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.WandCap;
import thaumcraft.api.wands.WandRod;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.wands.ItemWandCasting;

public class ArcaneWandRecipe implements IArcaneRecipe
{
	private static final int MAX_CRAFT_GRID_WIDTH = 3;
    private static final int MAX_CRAFT_GRID_HEIGHT = 3;
    private boolean mirrored = true;
    
    public ArcaneWandRecipe() {
//    	super("", new ItemStack(ConfigItems.itemWandCasting), null, 
//    			new Object[]{"  C", " R ", "C  ", 
//			Character.valueOf('C'), WandCap.caps.get("iron").getItem(),
//			Character.valueOf('R'), WandRod.rods.get("wood").getItem()});
    }

	@Override
    public ItemStack getCraftingResult(IInventory inv){ 
		ItemStack out = null;
		String bc = null;
		String br = null;
		int cc=0;
		int cr=0;
		ItemStack cap1 = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 0, 2);
    	ItemStack cap2 = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 2, 0);
    	ItemStack rod = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 1, 1);    	
		
    	if (cap1!=null && cap2!=null && rod!=null && checkItemEquals(cap1,cap2)) {
    		
	    	for (WandCap wc:WandCap.caps.values()) {
	    		if (checkItemEquals(cap1,wc.getItem())) {
	    			bc=wc.getTag();
	    			cc=wc.getCraftCost();
	    			break;
	    		}
	    	}
	    	
	    	for (WandRod wr:WandRod.rods.values()) {
	    		if (checkItemEquals(rod,wr.getItem())) {
	    			br=wr.getTag();
	    			cr=wr.getCraftCost();
	    			break;
	    		}
	    	}
	    	
	    	if (bc!=null && br!=null) {
	    		int cost = cc * cr;
	    		out = new ItemStack(ConfigItems.itemWandCasting,1,cost);
	    		((ItemWandCasting)out.getItem()).setCap(out,WandCap.caps.get(bc));
	    		((ItemWandCasting)out.getItem()).setRod(out,WandRod.rods.get(br));
	    	}
    	
    	}
		
		return out; 
		
	}
	
	@Override		
	public AspectList getAspects(IInventory inv) {
		AspectList al = new AspectList();
		
		int cc=-1;
		int cr=-1;
		ItemStack cap1 = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 0, 2);
    	ItemStack cap2 = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 2, 0);
    	ItemStack rod = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 1, 1);    	
		
    	if (cap1!=null && cap2!=null && rod!=null && checkItemEquals(cap1,cap2)) {
    		
	    	for (WandCap wc:WandCap.caps.values()) {
	    		if (checkItemEquals(cap1,wc.getItem())) {
	    			cc=wc.getCraftCost();
	    			break;
	    		}
	    	}
	    	
	    	for (WandRod wr:WandRod.rods.values()) {
	    		if (checkItemEquals(rod,wr.getItem())) {
	    			cr=wr.getCraftCost();
	    			break;
	    		}
	    	}
	    	
	    	if (cc>=0 && cr>=0) {
	    		int cost = cc * cr;
	    		for (Aspect as: Aspect.getPrimalAspects()) {
	    			al.add(as, cost);
	    		}
	    	}
    	}
		
		return al;
	}

   
    @Override
    public ItemStack getRecipeOutput(){ return null; }

    @Override
    public boolean matches(IInventory inv, World world, EntityPlayer player)
    {
    	ItemStack cap1 = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 0, 2);
    	ItemStack cap2 = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 2, 0);
    	ItemStack rod = ThaumcraftApiHelper.getStackInRowAndColumn(inv, 1, 1);    
    	return checkMatch(cap1,cap2,rod,player);
    }

    private boolean checkMatch(ItemStack cap1, ItemStack cap2, ItemStack rod, EntityPlayer player)
    {
    	boolean bc=false;
		boolean br=false;
		
    	if (cap1!=null && cap2!=null && rod!=null && checkItemEquals(cap1,cap2)) {
    		
	    	for (WandCap wc:WandCap.caps.values()) {
	    		if (checkItemEquals(cap1,wc.getItem()) && 
	    			ThaumcraftApiHelper.isResearchComplete(player.username, "CAP_"+wc.getTag())) {
	    			bc=true;
	    			break;
	    		}
	    	}
	    	
	    	for (WandRod wr:WandRod.rods.values()) {
	    		if (checkItemEquals(rod,wr.getItem()) &&
	    			ThaumcraftApiHelper.isResearchComplete(player.username, "ROD_"+wr.getTag())) {
	    			br=true;
	    			break;
	    		}
	    	}
    	
    	}

        return br && bc;
    }

    private boolean checkItemEquals(ItemStack target, ItemStack input)
    {
        if (input == null && target != null || input != null && target == null)
        {
            return false;
        }
        return (target.itemID == input.itemID && 
        		(!target.hasTagCompound() || ItemStack.areItemStackTagsEqual(target, input)) &&
        		(target.getItemDamage() == OreDictionary.WILDCARD_VALUE|| target.getItemDamage() == input.getItemDamage()));
    }

	@Override
	public int getRecipeSize() {
		return 9;
	}

	@Override
	public AspectList getAspects() {
		return null;
	}

	@Override
	public String getResearch() {
		return "";
	}


}
