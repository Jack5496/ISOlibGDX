package com.kilobolt.Screens;

import com.kilobolt.GameWorld.Block;
import com.kilobolt.GameWorld.GameWorld;

public class CameraObject {

	// Actual Position
	private int x;
	private int y;
	
	private Block tracking;

	// Old Position
	private int oldx;
	private int oldy;

	// Movevelocity;
	private float deltaX;
	private float deltaY;

	// GameeWorld
	private GameWorld world;

	// MoveSpeed
	private float speed = 2f;
	
	private float normalSpeed = 2f;


	private boolean nextReached = true;

//	float xOff = 0;
	public float xP = 0;
	public float xPMax = 100; // percentage

//	float yOff = 0;
	public float yP = 0;
	public float yPMax = 100; // percentage

	public enum DIRECTION {
		UL, OL, UR, OR
	}
	
	public void setTrackingBlock(Block b){
		tracking = b;
	}

	public CameraObject(GameWorld world) {
		setToZero();
		updateOldPosition();
		setVelocityZero();
		this.world = world;
	}

	public void setToZero() {
		x = 0;
		y = 0;
	}

	public void setVelocityZero() {
		deltaX = 0;
		deltaY = 0;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void addX(int i) {
		int w = world.getSurface().WIDTH;
		
		x += i;
		int dx = x % w;
		if (dx < 0)
			dx = w + dx;
		x = dx;
	}

	public void addY(int i) {
		int h = world.getSurface().HEIGHT;
		
		y += i;

		int dy = y % h;
		if (dy < 0)
			dy = h + dy;

		y = dy;
	}

	public void setNextReached(boolean bol) {
		this.nextReached = bol;
	}

	public boolean changedPosition() {
		return oldx != getX() || oldy != getY();
	}

	private void setOldPosition(int x, int y) {
		oldx = x;
		oldy = y;
	}

	public void updateOldPosition() {
		setOldPosition(getX(), getY());
	}

	public boolean getNextReached() {
		return this.nextReached;
	}

	public void setSpeed(float s) {
		this.speed = s;
	}

	public float getSpeed() {
		return this.speed;
	}

	private void moveUR() {
		deltaY = yPMax * speed;
		deltaX = xPMax * speed;
		setNextReached(false);
	}

	private void moveUL() {
		deltaY = yPMax * speed;
		deltaX = -xPMax * speed;
		setNextReached(false);
	}

	private void moveOR() {
		deltaY = -yPMax * speed;
		deltaX = xPMax * speed;
		setNextReached(false);
	}

	private void moveOL() {
		deltaY = -yPMax * speed;
		deltaX = -xPMax * speed;
		setNextReached(false);
	}

	public boolean move(DIRECTION dir) {
		if (getNextReached()) {
			switch (dir) {
			case UL:
				moveUL();
				return true;
			case OL:
				moveOL();
				return true;
			case OR:
				moveOR();
				return true;
			case UR:
				moveUR();
				return true;
			}
		}
		return false;
	}
	
	public void doubleSpeed(){
		setSpeed(2*normalSpeed);
	}
	
	public void resetSpeed(){
		setSpeed(normalSpeed);
	}
	
	public void followTracking(){
		xP = tracking.xP;
		yP = tracking.yP;
		x = tracking.getColumn().getX();
		y = tracking.getColumn().getY();
	}

	public void update(float delta) {
		if(tracking!=null){
			followTracking();
			return;
		}
		
		if (getNextReached()) {
			xP = 0;
			yP = 0;
		} else {
			xP -= deltaX * delta;
			yP -= deltaY * delta;
		}

		if (xP >= 50) {
			if (yP <= -50) {
				addY(1);
				yP = 0;
				xP = 0;
			}
			if (yP >= 50) {
				addX(-1);
				yP = 0;
				xP = 0;
			}
		}
		if (xP <= -50) {
			if (yP <= -50) {
				addX(1);
				yP = 0;
				xP = 0;
			}
			if (yP >= 50) {
				addY(-1);
				yP = 0;
				xP = 0;
			}
		}

		if (changedPosition()) {
			updateOldPosition();
			setNextReached(true);
		}
	}

	public float getXPercentage() {
		return (xP / xPMax);
	}

	public float getYPercentage() {
		return (yP / yPMax);
	}

}
