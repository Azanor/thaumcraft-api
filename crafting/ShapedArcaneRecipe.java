package thaumcraft.api.crafting;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.oredict.ShapedOreRecipe;
import thaumcraft.api.aspects.AspectList;

public class ShapedArcaneRecipe extends ShapedOreRecipe implements IArcaneRecipe
{
	
	private String research;
	private int vis;
	private AspectList crystals;

	public ShapedArcaneRecipe(ResourceLocation group, String res, int vis, AspectList crystals, Block     result, Object... recipe){ this(group, res, vis, crystals, new ItemStack(result), recipe); }
    public ShapedArcaneRecipe(ResourceLocation group, String res, int vis, AspectList crystals, Item      result, Object... recipe){ this(group, res, vis, crystals, new ItemStack(result), recipe); }
    public ShapedArcaneRecipe(ResourceLocation group, String res, int vis, AspectList crystals, @Nonnull ItemStack result, Object... recipe) { this(group, res, vis, crystals, result, CraftingHelper.parseShaped(recipe)); }
	public ShapedArcaneRecipe(ResourceLocation group, String res, int vis, AspectList crystals, @Nonnull ItemStack result, ShapedPrimer primer) {
		super(group, result, primer);
		this.research = res;
		this.vis = vis;
		this.crystals = crystals;
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1) {
		if (!(var1 instanceof IArcaneWorkbench)) return ItemStack.EMPTY; 
		return super.getCraftingResult(var1);
	}
	
	@Override
	public boolean matches(InventoryCrafting inv, World world) {
		return inv instanceof IArcaneWorkbench && super.matches(inv, world);
	}

	@Override
	public int getVis() {
		return vis;
	}

	@Override
	public String getResearch() {
		return research;
	}

	@Override
	public AspectList getCrystals() {
		return crystals;
	}
	
}
