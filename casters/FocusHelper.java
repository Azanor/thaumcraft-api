package thaumcraft.api.casters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import thaumcraft.api.casters.IFocusPart.EnumPartAttribute;

public class FocusHelper {
	
	// mediums
	public static IFocusPartMedium TOUCH;
	public static IFocusPartMedium BOLT;
	public static IFocusPartMedium PROJECTILE;
	public static IFocusPartMedium PLAN;
	
	// effects
	public static IFocusPartEffect FIRE;
	public static IFocusPartEffect FROST;
	public static IFocusPartEffect MAGIC;
	public static IFocusPartEffect CURSE;
	public static IFocusPartEffect BREAK;
	public static IFocusPartEffect RIFT;
	public static IFocusPartEffect EXCHANGE;
	
	// modifiers
	public static IFocusPartModifier FRUGAL;
	public static IFocusPartModifier POTENCY;
	public static IFocusPartModifier LINGERING;
	public static IFocusPartModifier SCATTER;
	public static IFocusPartModifier CHAIN;
	public static IFocusPartModifier SILKTOUCH;
	public static IFocusPartModifier FORTUNE;
	public static IFocusPartModifier CHARGE;
	public static IFocusPartModifier BURST;
	public static IFocusPartModifier QUICKEN;	
	
	public static HashMap<String,IFocusPart> focusParts = new HashMap<>();
	
	
	/**
	 * Registers a focus part for use	
	 * @param part the part to register
	 * @param connections what other parts this part can connect to. By default all 'effects' & 'mediums' can connect so this does not need to be listed
	 * @return
	 */
	public static boolean registerFocusPart(IFocusPart part) {
		if (focusParts.containsKey(part.getKey())) return false;
		focusParts.put(part.getKey(), part);
		return true;
	}
	
	public static IFocusPart getFocusPart(String key) {
		return focusParts.get(key);
	}
	
	public static boolean canPartsConnect(IFocusPart part1,IFocusPart part2) {
		if (part1==null || part2==null) return false;
		if (part1.getType()==part2.getType()) return false;
		
		if (!part1.canConnectTo(part2) || !part2.canConnectTo(part1)) return false;
		
		EnumPartAttribute[] atr1 = part1.getAttributes();
		EnumPartAttribute[] atr2 = part2.getAttributes();
		
		if (atr1!=null && atr2!=null) {
			boolean b=false;			
			List<EnumPartAttribute> l1 = Arrays.asList(atr1);
			List<EnumPartAttribute> l2 = Arrays.asList(atr2);				
			for (EnumPartAttribute atr : atr1) {
				if (atr.shouldCheckAgainst(part2.getType())) b = l2.contains(atr);
			} 					
			for (EnumPartAttribute atr : atr2) {
				if (atr.shouldCheckAgainst(part1.getType())) b = l1.contains(atr);
			} 		
			return b;
		}
		
		return true;
	}
}
