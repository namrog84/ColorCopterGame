package com.newrog.colorcopter.entities;

import aurelienribon.tweenengine.TweenAccessor;

public class EntityAccessor implements TweenAccessor<Entity>{
	public static final int POSITION_XY = 1;
	public static final int SCALE_XY = 2;
	
	@Override
	public int getValues(Entity target, int tweenType, float[] returnValues) {
		switch (tweenType)
		{
			case POSITION_XY:
				returnValues[0] = target.position.x;
				returnValues[1] = target.position.y;
				return 2;

			case SCALE_XY:
				returnValues[0] = target.position.x;
				returnValues[1] = target.position.y;
				return 2;
			default:
				assert false;
				return -1;
		}
	}

	@Override
	public void setValues(Entity target, int tweenType, float[] newValues) {
		switch (tweenType)
		{
			case POSITION_XY:
				target.position.x = newValues[0];
				target.position.y = newValues[1];
				break;
			case SCALE_XY:
				target.position.x = newValues[0];
				target.position.y = newValues[1];
				//target.(newValues[0]);
				//target.setScaleY(newValues[1]);
				break;
			default:
				assert false;
		}
	
	}
	

}
