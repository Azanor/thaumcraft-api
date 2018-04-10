package thaumcraft.api.casters;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import thaumcraft.api.aspects.Aspect;

public class FocusMediumRoot extends FocusMedium {
		
	public FocusMediumRoot() {
		super();
	}
	
	Trajectory[] trajectories=null;
	RayTraceResult[] targets=null;
	
	public FocusMediumRoot(Trajectory[] trajectories, RayTraceResult[] targets) {
		super();
		this.trajectories = trajectories;
		this.targets = targets;
	}
	
	@Override
	public String getResearch() {
		return "BASEAUROMANCY";
	}

	@Override
	public String getKey() {
		return "ROOT";
	}
	
	@Override
	public int getComplexity() {
		return 0;
	}
	
	@Override
	public EnumSupplyType[] willSupply() {
		return new EnumSupplyType[] {EnumSupplyType.TARGET, EnumSupplyType.TRAJECTORY};
	}

	@Override
	public RayTraceResult[] supplyTargets() {
		return targets;
	}

	@Override
	public Trajectory[] supplyTrajectories() {
		return trajectories;
	}

	
	public void setupFromCaster (EntityLivingBase e) {
		trajectories = new Trajectory[] { new Trajectory(generateSourceVector(e), e.getLookVec()) };
		targets = new RayTraceResult[] { new RayTraceResult(e) };
	}
	
	private Vec3d generateSourceVector(EntityLivingBase e) {
		Vec3d v = e.getPositionVector();
		
		// Sacrifice looks for accuracy for now.
//		boolean mainhand=true;
//		if (e.getHeldItemMainhand()!=null && e.getHeldItemMainhand().getItem() instanceof ICaster) mainhand=true;
//		else
//		if (e.getHeldItemOffhand()!=null && e.getHeldItemOffhand().getItem() instanceof ICaster) mainhand=false;
//		
//		double posX = -Math.cos(((e.rotationYaw-.5f) / 180F) * 3.141593F) * .2F * (mainhand?1:-1);
//		double posZ = -Math.sin(((e.rotationYaw-.5f) / 180F) * 3.141593F) * .3f * (mainhand?1:-1);
//		
//		Vec3d vl = e.getLookVec();
//		v = v.addVector(posX, e.getEyeHeight() - 0.40000000149011612D, posZ);		
//		v = v.add(vl);
		
		v = v.addVector(0, e.getEyeHeight(), 0);
		
		return v;
	}

	@Override
	public Aspect getAspect() {
		return null;
	}
	
}
