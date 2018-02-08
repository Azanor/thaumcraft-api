package thaumcraft.api.casters;

import java.util.HashMap;

public class MutatorStore {	

	public HashMap<String,MutatorType> mutators = new HashMap<>();
	
	public MutatorStore() {
		registerMutator(POWER);
		registerMutator(COST);
		registerMutator(TIME);
	}
	
	// Standardised mutators that can be used. You can add more for your own use.
	public final MutatorType POWER = new MutatorType("power",MutatorStore.EnumMutatorOperand.MULT);
	public final MutatorType TIME = new MutatorType("time",MutatorStore.EnumMutatorOperand.MULT);
	public final MutatorType COST = new MutatorType("cost",MutatorStore.EnumMutatorOperand.MULT);
	public final MutatorType FORTUNE = new MutatorType("fortune",MutatorStore.EnumMutatorOperand.SUM);
	public final MutatorType SILKTOUCH = new MutatorType("silk",MutatorStore.EnumMutatorOperand.SUM);		
	
	public void registerMutator(MutatorType mutator) {
		if (!mutators.containsKey(mutator.key))
			mutators.put(mutator.key, mutator);
		else
			this.modifyMutator(mutator, mutator.value);
	}
	
	public void modifyMutator(MutatorType mutator, float value) {
		modifyMutator(mutator.key,value);
	}

	public void modifyMutator(String key, float value) {
		if (mutators.containsKey(key)) {			
			MutatorType mutator = mutators.get(key);
			mutator.apply(value);			
			mutators.put(key, mutator);
		} 
	}
	
	public float getValue(MutatorType mutator) {
		return getValue(mutator.key);
	}
	
	public float getValue(String key) {
		if (mutators.containsKey(key)) {			
			return mutators.get(key).value;
		} 
		return 0;
	}
	
	public void merge(MutatorStore store) {
		for (MutatorType mutator:store.mutators.values()) {
			if (!mutators.containsKey(mutator.key)) {
				this.registerMutator(mutator);
			}
			this.modifyMutator(mutator, mutator.value);
//			System.out.println(mutator.key+ ": "+this.getValue(mutator)+" "+store.getValue(mutator));
		}
	}
	
	
	public enum EnumMutatorOperand {
		SUM,	
		MULT;		
	}
	
}