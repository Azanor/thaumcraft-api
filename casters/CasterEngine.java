package thaumcraft.api.casters;

import java.util.HashMap;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import thaumcraft.api.casters.FocusCore.FocusEffect;

public class CasterEngine {
	
	public static boolean cast(FocusCore core, Entity casterEntity, ItemStack casterStack, RayTraceResult source, Vec3d castVector, MutatorStore castingMutators) {
					
		for (FocusEffect ef:core.effects) {
			ef.mutators.merge(castingMutators);
		}
		
		boolean cancel=false;
		
		if (core.mediumModifiers!=null)
		for (IFocusPartModifier mediumMod : core.mediumModifiers) {
			if (core.isPartIgnored(mediumMod)) continue;
			boolean b = mediumMod.applyAtCast(casterEntity, casterStack, core, source, castVector); 
			if (b) cancel=true;
		}
		
		if (cancel || core.medium.onCast(casterEntity, casterStack, core, source, castVector)) {
			
			for (FocusEffect focusEffect:core.effects) {
				
				if (core.isPartIgnored(focusEffect.effect)) continue;
				
				boolean cancel2=false;				
				if (focusEffect.modifiers!=null)
					for (IFocusPartModifier effectMod : focusEffect.modifiers) {	
						if (core.isPartIgnored(effectMod)) continue;
						boolean b = effectMod.applyAtCast(casterEntity, casterStack, core, source, castVector);
						if (b) cancel2=true;
					}
					
				if (!cancel2)
					focusEffect.effect.onCast(casterEntity, casterStack); 
			}
			
			return true;
		}
		
		return false;
	}

	public static void applyEffectsFromFocus(FocusCore core, RayTraceResult source, RayTraceResult mediumTarget, Entity casterEntity, ItemStack casterStack) {
						
		boolean cancel=false;
		
		if (core.mediumModifiers!=null)
		for (IFocusPartModifier mediumMod : core.mediumModifiers) {
			if (core.isPartIgnored(mediumMod)) continue;			
			boolean b = mediumMod.applyAtMediumResolution(casterEntity, casterStack, core, mediumTarget, source, source.entityHit!=null?source.entityHit.getLook(1):new Vec3d(0,0,0));
			if (b) cancel=true;
		}		

		if (!cancel) {
		
			HashMap<Integer,Integer> hurtMap = new HashMap<Integer,Integer>();
			
			for (FocusEffect focusEffect:core.effects) {	
				if (core.isPartIgnored(focusEffect.effect)) continue;
				boolean cancel2 = core.medium.applyAtEffect(casterEntity, casterStack, focusEffect, mediumTarget, source, source.entityHit!=null?source.entityHit.getLook(1):new Vec3d(0,0,0));
				
				if (!cancel2) {		
					
					focusEffect.effect.onTrigger(casterEntity, casterStack, focusEffect, source, mediumTarget);	
					
					if (focusEffect.modifiers!=null)
					for (IFocusPartModifier effectMod : focusEffect.modifiers) {	
						if (core.isPartIgnored(effectMod)) continue;
						boolean b = effectMod.applyAtEffect(casterEntity, casterStack, focusEffect, mediumTarget, source, source.entityHit!=null?source.entityHit.getLook(1):new Vec3d(0,0,0));
						if (b) cancel2=true;
					}
								
					if (mediumTarget.entityHit!=null) {
						if (mediumTarget.entityHit.hurtResistantTime<=0) {
							hurtMap.put(mediumTarget.entityHit.getEntityId(),0);
						}
						if (hurtMap.containsKey(mediumTarget.entityHit.getEntityId())) {
							hurtMap.put(mediumTarget.entityHit.getEntityId(),mediumTarget.entityHit.hurtResistantTime);
							mediumTarget.entityHit.hurtResistantTime = 0;
						}
					}
					
					focusEffect.effect.applyToTarget(casterEntity, casterStack, focusEffect, source, mediumTarget);
					
	
				}
				
				
			}
			
			for (Integer k:hurtMap.keySet()) {
				Entity e = casterEntity.getEntityWorld().getEntityByID(k);
				if (e!=null)
					e.hurtResistantTime = hurtMap.get(k);
			}
			
		}
		
		
	}
	


}
