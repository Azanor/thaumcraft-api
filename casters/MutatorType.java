package thaumcraft.api.casters;

import thaumcraft.api.casters.MutatorStore.EnumMutatorOperand;

public class MutatorType {
		String key;
		EnumMutatorOperand operand;
		float value=0;
		
				
		
		public MutatorType(String key, EnumMutatorOperand operand) {
			this.key = key;
			this.operand = operand;
			if (this.operand==EnumMutatorOperand.MULT) value=1;
		}

		void apply(float input) {
			float ov = value;
			switch(operand) {
				case SUM: value += input; break;
				case MULT: value *= input; break;
			}
		}		
		
		
	}