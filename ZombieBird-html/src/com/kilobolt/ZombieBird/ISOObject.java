package com.kilobolt.ZombieBird;

/*
 * ISOObject
 * Desc: Frame ( parent pbject ) for all other objects
 * represented in an isometric environment.
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Graphics;
import java.lang.Math.*;

public abstract class ISOObject {
	// Abstract class ( cannot be instantiated )
	// Holds all base values/variables/methods
	// For objects represented in an isometric world.

	private static int objects = 0;
	// amount of ISOObjects that exists altogether.
	// ( +1 is added whenever an ISOObject is created
	// ( a child of ISOObject to be more exact ).
	protected int x; // x position
	protected int y; // y position
	protected int z; // z position ( lowest position (floor) of object )
	protected int h; // height ( z+h = highest position (roof) of object )

	public int internalHOff;
	public int internalVOff;

	// protected int depth;
	/*
	 * Only used as a method ( much much simpler ) Depth = ( x + y + z );
	 * IMPORTANT! for drawing objects to the screen in the correct order.
	 */
	protected Image sprite; // sprite: visual representation of the object
	protected String spriteDest;
	protected String spriteName = "spr_defaultBlock.png"; // Default block
															// graphics
	protected String name; // Name of the object ( f.ex: tree, table2, block16 )
	protected String desc; // Description of the object. ex: "A small flower"

	private int Tile_W = (int) (32 * (Math.pow(2.0, AppletStarter.zoom - 1)));
	private int Tile_H = (int) (16 * (Math.pow(2.0, AppletStarter.zoom - 1)));

	// Object Eigentschaften
	protected static boolean solid = false;

	// Constructor
	public ISOObject(boolean solid) {
		objects++;
		// adds 1 to the 'objects' instance counter
		// whenever a new subclass of ISOObject is created.
		this.solid = solid;
	}

	// Methods
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public int getH() {
		return h;
	}

	public int getDepth() {
		return (x + y + z);
	}

	public Image getSprite() {
		return sprite;
	}

	public String getSpriteDest() {
		return spriteDest;
	}

	public String getSpriteName() {
		return spriteName;
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}

	public void setSprite(Image spr) {
		this.sprite = spr;
		reload();
	}

	public void reload() {
		if (sprite == null)
			return;
	}

	public void reloadSprite() {
		Tile_W = (int) (32 * (Math.pow(2.0, AppletStarter.zoom - 1)));
		Tile_H = (int) (16 * (Math.pow(2.0, AppletStarter.zoom - 1)));
		if (spriteName == null)
			return;
		System.out.println("Load Sprite: " + spriteName);
		this.loadSprite(spriteName);
	}

	public void setInternalOff() {
		if (sprite == null)
			return;

		internalHOff = sprite.getWidth(null) / 2;
		internalVOff = sprite.getHeight(null);
	}

	public void setInternalOff(int h, int v) {
		internalHOff = (int) (h * (Math.pow(2.0, AppletStarter.zoom - 1)));
		internalVOff = (int) (v * (Math.pow(2.0, AppletStarter.zoom - 1)));
	}

	public void setXYZ(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public boolean classExists(String className) {
		return true;
		// Checks if className exists ( if we can use it )
		// Because we need to check if we can access the sprite
		// loading class to load our sprites/images
		/*
		 * try { Class.forName(className); return true; // className exists }
		 * catch (ClassNotFoundException e) {
		 * System.out.println("ERROR: Can't find SpriteBank class"); return
		 * false; // className not found // package.className also works }
		 */
	}

	public boolean loadSprite(String sprName) {
		// Loads the sprite through the SpriteBank class
		// returns true if successful.

		if (classExists("SpriteBank") == false) {
			// Unable to find/access the "SpriteBank" class.
			System.out.println("Can't load sprite, SpriteBank"
					+ " class doesn't exist");
			return false;
		} else {
			// SpriteBank class found
			sprite = spriteBank.getSprite(sprName);
			reload();
			if (sprite == null) {
				// Error occured
				System.out.println("ERROR: Couldn't find sprite + " + sprName);
				return false;
			}
			return true;
		}
	}

	// method to convert position into isometric position
	private int isoX() {
		// convert x into isometric x
		int isoX;
		isoX = (int) ((x * Tile_W / 2) - (y * Tile_W / 2));

		return isoX;
	}

	private int isoY() {
		// convert y into isometric y
		int isoY;
		isoY = (int) ((x * Tile_H / 2) + (y * Tile_H / 2));

		return isoY;
	}

	//
	// paint methods
	//
	public void paint(Graphics g) {
		// Paints the objects corresponding sprite
		if (sprite == null)
			return; // no sprite = do nothing.

		g.drawImage(sprite, isoX() - internalHOff, isoY() - internalVOff
				- this.z, null);
	}

	public static int off = 0;

	public void paintOff(Graphics g, int hOff, int vOff) {
		// paint image with an offset
		if (sprite == null)
			return;

		g.drawImage(sprite, hOff + isoX() - internalHOff, vOff + isoY()
				- internalVOff - this.z + off, null);

		if (HUD.showNames) {
			if (this.name != null) {

				int textwidth = (int) (AppletStarter.font.getStringBounds(name,
						AppletStarter.frc).getWidth());
				int textheight = (int) (AppletStarter.font.getStringBounds(
						name, AppletStarter.frc).getHeight());
				int xAbstand = 10;
				int yAbstand = 5;

				//Langes ausprobieren, nicht mehr ändern...
				
				
				
				g.setColor(Color.WHITE);
				
				g.setColor(new Color(1f, 1f, 1f, .6f));
				
				g.fillRect(
						hOff + isoX() - internalHOff
								+ (sprite.getWidth(null) / 2) - textwidth / 2
								- xAbstand, vOff + isoY() - internalVOff
								- this.z + off - textheight / 4 - textheight
								/ 2 - yAbstand, textwidth + 2 * xAbstand,
						textheight / 2 + 2 * yAbstand);

				g.setColor(Color.BLACK);
				
				g.setColor(new Color(0f, 0f, 0f, .6f));
				
				g.drawString(name,
						hOff + isoX() - internalHOff
								+ (sprite.getWidth(null) / 2) - textwidth / 2,
						vOff + isoY() - internalVOff - this.z + off
								- textheight / 4);
			}
		}

	}

	public void paintOnScreen(Graphics g, Screen screen) {

		paintOff(g, -screen.x() + screen.hOff(), -screen.y() + screen.vOff());
	}

	public int getObjects() {
		return objects;
	}
}