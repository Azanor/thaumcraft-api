package thaumcraft.api.crafting;

public interface IStabilizable {
	public void addStability(int multiples);
	public EnumStability getStability();
	
	public static enum EnumStability {
		VERY_STABLE, STABLE, UNSTABLE, VERY_UNSTABLE 
	}
}
