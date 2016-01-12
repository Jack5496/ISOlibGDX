package com.kilobolt.GameWorld;

import com.kilobolt.Screens.CameraObject.DIRECTION;

/*
 * Block
 * Desc; The general Block class
 * functions as walls, ground, blocks, obstacles
 *
 * Basically anything you can stand on.
 */

public class Block {

	public BlockType type;

	private ISOColumn col;

	private float deltaX;
	private float deltaY;

	// float xOff = 0;
	public float xP = 0;
	public float xPMax = 100; // percentage

	// float yOff = 0;
	public float yP = 0;
	public float yPMax = 100; // percentage
	
	public int amount = 1;
	
	// private float rotation;
	// private int width;
	// private float height;

	private float normalSpeed = 2f;
	private float speed = normalSpeed;

	private boolean nextReached = true;

	public Block(BlockType type, ISOColumn col) {
		this.col = col;
		this.type = type;
		setVelocityZero();
	}
	
	public Block(String type, ISOColumn col){
		this.col = col;
		this.type = BlockType.fromString(type);
	}
	
	public String toString(){
		return type.toString();
	}

	public void setVelocityZero() {
		deltaX = 0;
		deltaY = 0;
	}
	
	protected void setSpeed(float speed){
		this.speed = speed;
	}
	
	protected float getSpeed(){
		return speed;
	}
	
	protected void resetToNormalSpeed(){
		this.speed = this.normalSpeed;
	}
	
	protected float getNormalSpeed(){
		return normalSpeed;
	}

	private void moveUR() {
		deltaX = xPMax * speed;
		deltaY = yPMax * speed;
		setNextReached(false);
	}

	private void moveUL() {
		deltaX = -xPMax * speed;
		deltaY = yPMax * speed;
		setNextReached(false);
	}

	private void moveOR() {
		deltaX = xPMax * speed;
		deltaY = -yPMax * speed;
		setNextReached(false);
	}

	private void moveOL() {
		deltaX = -xPMax * speed;
		deltaY = -yPMax * speed;
		setNextReached(false);
	}

	/*
	 * public boolean move(DIRECTION dir) { if (getNextReached()) { switch (dir)
	 * { case UL: moveUL(); return true; case OL: moveOL(); return true; case
	 * OR: moveOR(); return true; case UR: moveUR(); return true; } } return
	 * false; }
	 */

	public boolean allowedToMove(DIRECTION dir) {
		return true;
	}
	
	

	public boolean move(DIRECTION dir) {
		if (getNextReached()) {
			if (allowedToMove(dir)) {	
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
			} else {
				return false;
			}
		}
		return true;
	}

	public float getXPercentage() {
		return (xP / xPMax);
	}

	public float getYPercentage() {
		return (yP / yPMax);
	}

	float time = 0;

	public void update(float delta) {
		if (getNextReached()) {
			xP = 0;
			yP = 0;
		} else {
			xP -= deltaX * delta;
			yP -= deltaY * delta;
		}

		if (xP >= 50*amount) {
			if (yP <= -50*amount) {
				moveBlockToOffset(0, 1*amount);
				yP = 0;
				xP = 0;
				setNextReached(true);
			}
			if (yP >= 50*amount) {
				moveBlockToOffset(-1*amount, 0);
				yP = 0;
				xP = 0;
				setNextReached(true);
			}
		}
		if (xP <= -50*amount) {
			if (yP <= -50*amount) {
				moveBlockToOffset(1*amount, 0);
				yP = 0;
				xP = 0;
				setNextReached(true);
			}
			if (yP >= 50*amount) {
				moveBlockToOffset(0, -1*amount);
				yP = 0;
				xP = 0;
				setNextReached(true);
			}
		}
	}

	private void setNextReached(boolean b) {
		this.nextReached = b;
	}

	protected void moveBlockToOffset(int x, int y) {
		changeBlockIntoColoumn(col.getX() + x, col.getY() + y);
	}
	
	protected ISOColumn getColumnFromOffset(int x, int y){
		return getColumn(col.getX() + x, col.getY() + y);
	}
	
	protected ISOColumn getColumn(int x, int y){
		return col.getSurface().getColoumn(x, y);
	}

	private void changeBlockIntoColoumn(int x, int y) {
		col.deleteBlock(this);
		this.col = getColumn(x,y);
		col.addBlock(this);
	}

	public boolean getNextReached() {
		return this.nextReached;
	}

	public float getX() {
		return col.getX();
	}

	public float getY() {
		return col.getY();
	}

	public BlockType getBlockType() {
		return type;
	}

	public void setBlockType(BlockType type) {
		this.type = type;
	}

	public ISOColumn getColumn() {
		return this.col;
	}

	public void setColumn(ISOColumn col) {
		this.col = col;
	}

}