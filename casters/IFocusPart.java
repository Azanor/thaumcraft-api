package thaumcraft.api.casters;

import java.util.Arrays;
import java.util.List;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;

public interface IFocusPart {	
	
	/**
	 * A unique key for this part. It is probably a good idea to include your modid in this.
	 * @return
	 */
	public String getKey();
	
	/**
	 * The research required to unlock this part. 
	 * @return
	 */
	public String getResearch();
	
	/**
	 * Get the aspect associated with this part. Used to determine the crystal required in crafting.
	 * @return
	 */
	public Aspect getAspect();
	
	public default boolean hasAttribute(EnumPartAttribute attribute) {
		return getAttributes()==null || Arrays.asList(getAttributes()).contains(attribute);
	}
	
	/**
	 * 
	 * @return returning null will count as having all attributes
	 */
	public EnumPartAttribute[] getAttributes();
	
	enum EnumPartAttribute {
		RANGED(false,EnumFocusPartType.MEDIUM), 	//Medium only, works at range
		TOUCH(false,EnumFocusPartType.MEDIUM), 		//Medium only, works on something you clicked on
		BLOCKS(false,EnumFocusPartType.MEDIUM,EnumFocusPartType.EFFECT), 	//can apply to blocks
		ENTITIES(false,EnumFocusPartType.MEDIUM,EnumFocusPartType.EFFECT), 	//can apply to entities
		TIMED(false,EnumFocusPartType.MODIFIER), 		//has or modifies duration
		HARVEST(true,EnumFocusPartType.EFFECT) 		//will harvest blocks or entities for resources
		; 
		EnumFocusPartType[] partTypes;
		public boolean required;
		
		private EnumPartAttribute(boolean required, EnumFocusPartType ... partTypes) {
			this.partTypes = partTypes;
			this.required=required;
		}
		
		public boolean shouldCheckAgainst(EnumFocusPartType checkType) {
			for (EnumFocusPartType tt:partTypes) if (checkType==tt) return true;
			return false;
		}
		
	}
	
	public default String getName() {
		return I18n.translateToLocal("focuspart."+getKey()+".name");
	}
	
	public default String getText() {
		return I18n.translateToLocal("focuspart."+getKey()+".text");
	}
	
	public EnumFocusPartType getType();
	
	/**
	 * Location of the icon image file for this part.
	 * @return
	 */
	public ResourceLocation getIcon();
	
	/**
	 * Default color the part gem will display as.
	 * @return
	 */
	public int getGemColor();
	
	/**
	 * Default color the part icon will display as.
	 * @return
	 */
	public int getIconColor();
		
	enum EnumFocusPartType {
		MEDIUM, EFFECT, MODIFIER;
	}

	/**
	 * Special coding to see if one part can connect to each other.
	 * @param part
	 * @return
	 */
	public default boolean canConnectTo(IFocusPart part) { return true; }
	
	
	
	public default boolean hasCustomFX() { return false; }
	public default void drawCustomFX(World world, double posX, double posY, double posZ, double motionX, double motionY, double motionZ) {  }
	
	/**
	 * 
	 * @param mutators
	 */
	public default void applyBaseMutators(MutatorStore mutators) {} 
	
}
