package thaumcraft.api.casters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import thaumcraft.api.casters.IFocusPartMedium.EnumFocusCastMethod;
import thaumcraft.api.casters.MutatorStore.EnumMutatorOperand;

public class FocusCore {

	public IFocusPartMedium medium;
	public IFocusPartModifier[] mediumModifiers;
	public FocusEffect[] effects;	
	public float cost;	
	public LinkedHashMap<String,IFocusPart> partsRaw; 
	public ArrayList<String> ignoredParts = new ArrayList<>(); 
	
	public int uniqueId=0;
	
	public FocusCore(IFocusPartMedium medium, IFocusPartModifier[] mediumModifiers, FocusEffect[] effects) {
		this.medium = medium;
		this.mediumModifiers = mediumModifiers;
		this.effects = effects;
	}
	
	public FocusCore() {
		medium = FocusHelper.TOUCH;
		FocusEffect fe = new FocusEffect();
		fe.effect = FocusHelper.FIRE;
		effects = new FocusEffect[] {fe};
		generate(false);
	}
	
	public FocusCore(NBTTagCompound nbt) {
		this.deserialize(nbt);
	}
	
	public void ignorePart(IFocusPart part) {
		ignoredParts.add(part.getKey());		
	}
	
	public boolean isPartIgnored(IFocusPart part) {
		return ignoredParts.contains(part.getKey());
	}

	public EnumFocusCastMethod getFinalCastMethod() {
		if (mediumModifiers!=null)
			for (IFocusPart part:mediumModifiers) {
				if (part==FocusHelper.CHARGE) return EnumFocusCastMethod.CHARGE; 
			}
		return this.medium.getCastMethod();
	}
	
	public int getFinalChargeTime() {
		int t = this.medium.getChargeTime();
		if (mediumModifiers!=null)
			for (IFocusPart part:mediumModifiers) {
				if (part==FocusHelper.CHARGE) {
					t *= 10;
				}
				if (part==FocusHelper.QUICKEN) {
					t /= 2;
				}
			}
		return t;
	}
	
	public void generate() {
		generate(false);
	}
	
	private void generate(boolean foundMutators) {
		this.partsRaw = new LinkedHashMap<>();
		if (medium==null) return;
		
		partsRaw.put(medium.getKey(), medium);
		
		cost=0;
				
		if (mediumModifiers!=null) {
			Arrays.sort(mediumModifiers, new ModifierSorter());		
			for (IFocusPartModifier mediumMod : mediumModifiers) {
				partsRaw.put(mediumMod.getKey(), mediumMod);
			}
		}		
				
		for (FocusEffect focusEffect:effects) {
			
			
			if (!foundMutators) {
				focusEffect.mutators = new MutatorStore();
				medium.applyBaseMutators(focusEffect.mutators);
				if (mediumModifiers!=null) {
					for (IFocusPartModifier mediumMod : mediumModifiers) {
						mediumMod.applyBaseMutators(focusEffect.mutators);
					}
				}				
				focusEffect.effect.applyBaseMutators(focusEffect.mutators);			
				if (focusEffect.modifiers!=null) {
					Arrays.sort(focusEffect.modifiers, new ModifierSorter());
					for (IFocusPartModifier effMod : focusEffect.modifiers) {
						effMod.applyBaseMutators(focusEffect.mutators);
					}
				}			
			}
			
			partsRaw.put(focusEffect.effect.getKey(), focusEffect.effect);	
			
			if (focusEffect.modifiers!=null)
				for (IFocusPartModifier effMod : focusEffect.modifiers) {
					partsRaw.put(effMod.getKey(), effMod);	
				}
			
			cost += focusEffect.effect.getBaseCost() * focusEffect.mutators.getValue(focusEffect.mutators.COST);
		}
				
	}

	public NBTTagCompound serialize() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("medium", medium.getKey());
		nbt.setInteger("id", uniqueId);
		
		if (mediumModifiers!=null && mediumModifiers.length>0) {
			String s="";
			for (IFocusPart p:mediumModifiers) {
				s+="~"+p.getKey();
			}
			s=s.replaceFirst("~", "");
			nbt.setString("mediumMods", s);
		}				
		
		NBTTagList efflist = new NBTTagList();
		for (FocusEffect fe:effects) {
			NBTTagCompound gt = new NBTTagCompound();
			gt.setString("effect", fe.effect.getKey());
			if (fe.modifiers!=null && fe.modifiers.length>0) {
				String s="";
				for (IFocusPart p:fe.modifiers) {
					s+="~"+p.getKey();
				}
				s=s.replaceFirst("~", "");
				gt.setString("mods", s);
			}
			
			NBTTagList mutlist = new NBTTagList();
			for (String key:fe.mutators.mutators.keySet()) {
				NBTTagCompound mu = new NBTTagCompound();
				mu.setString("name", key);
				mu.setString("type", fe.mutators.mutators.get(key).operand.name());
				mu.setFloat("val", fe.mutators.getValue(key));
				mutlist.appendTag(mu);
			}	
			gt.setTag("mutators", mutlist);		
			efflist.appendTag(gt);
		}				
		nbt.setTag("effects", efflist);		
		
		NBTTagList iglist = new NBTTagList();
		for (String fe:ignoredParts) {
			NBTTagCompound gt = new NBTTagCompound();
			gt.setString("key", fe);
			iglist.appendTag(gt);
		}
		nbt.setTag("ignored", iglist);		
		
		return nbt;
	}
	
	public void deserialize(NBTTagCompound nbt) {	
		
		IFocusPart mp = FocusHelper.getFocusPart(nbt.getString("medium"));
		if (mp==null) return;
		this.uniqueId = nbt.getInteger("id");
		this.medium = (IFocusPartMedium) mp;
		String s = nbt.getString("mediumMods");
		String[] ss = s.split("~");
		if (ss.length>0) {			
			ArrayList<IFocusPartModifier> li = new ArrayList<>();
			for (int a=0;a<ss.length;a++) {
				try {
					IFocusPartModifier p=(IFocusPartModifier) FocusHelper.getFocusPart(ss[a]);
					if (p!=null) {
						li.add(p);
					}
				} catch (Exception e) {
				}
			}			
			this.mediumModifiers = li.toArray(new IFocusPartModifier[li.size()]);
		}

		boolean foundMutators=false;
		
		NBTTagList efflist = nbt.getTagList("effects", (byte)10);	
		ArrayList<FocusEffect> fes = new ArrayList<>();
		for (int x=0;x<efflist.tagCount();x++) {
			NBTTagCompound nbtdata = (NBTTagCompound) efflist.getCompoundTagAt(x);
			FocusEffect fe = new FocusEffect();
			fe.effect=(IFocusPartEffect) FocusHelper.getFocusPart(nbtdata.getString("effect"));
			String mods = nbtdata.getString("mods");
			if (!mods.isEmpty()) {
				String[] modlist=mods.split("~");
				ArrayList<IFocusPartModifier> li = new ArrayList<>();
				for (int a=0;a<modlist.length;a++) {
					try {
						IFocusPartModifier p=(IFocusPartModifier) FocusHelper.getFocusPart(modlist[a]);
						if (p!=null) {
							li.add(p);
						}
					} catch (Exception e) {
					}
				}			
				fe.modifiers = li.toArray(new IFocusPartModifier[li.size()]);
			}
			
			NBTTagList mutlist = nbtdata.getTagList("mutators", (byte)10);	
			for (int m=0;m<mutlist.tagCount();m++) {
				foundMutators=true;
				
				NBTTagCompound mutnbtdata = (NBTTagCompound) mutlist.getCompoundTagAt(m);
				MutatorType mut = null;
				if (mutnbtdata.getString("type").equals("SUM"))
					mut = new MutatorType(mutnbtdata.getString("name"),EnumMutatorOperand.SUM);
				else 
					mut = new MutatorType(mutnbtdata.getString("name"),EnumMutatorOperand.MULT);
				mut.apply(mutnbtdata.getFloat("val"));
				fe.mutators.registerMutator(mut);
			}
			
			fes.add(fe);
		}
		
		NBTTagList iglist = nbt.getTagList("ignored", (byte)10);
		for (int x=0;x<iglist.tagCount();x++) {
			NBTTagCompound nbtdata = (NBTTagCompound) iglist.getCompoundTagAt(x);
			ignoredParts.add(nbtdata.getString("key"));
		}
		
		this.effects = fes.toArray(new FocusEffect[fes.size()]);
		this.generate(foundMutators);
	}
		
	public String getSortingHelper() {
		String s = "";
		try {
			s = medium.getKey();
			if (mediumModifiers!=null) for (IFocusPartModifier pm:this.mediumModifiers) s += pm.getKey();
			for (FocusEffect ef:effects) {
				s += ef.effect.getKey();
				if (ef.modifiers!=null) for (IFocusPartModifier pm:ef.modifiers) s += pm.getKey();
			}
		} catch (Exception e) { }
		return s;
	}
	
	public static class FocusEffect {
		public IFocusPartEffect effect;
		public IFocusPartModifier[] modifiers;
		public MutatorStore mutators = new MutatorStore();
	}
		
	
	
	public class ModifierSorter implements Comparator<IFocusPartModifier>
    {
		@Override
        public int compare(IFocusPartModifier mod1, IFocusPartModifier mod2)
        {
            byte d0 = mod1.getApplicationOrder();
            byte d1 = mod2.getApplicationOrder();
            return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
        }
    }

	public FocusCore copy() {
		FocusCore nc = new FocusCore(this.serialize());
		return nc;
	}
}
