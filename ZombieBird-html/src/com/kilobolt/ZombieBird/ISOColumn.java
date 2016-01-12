package com.kilobolt.ZombieBird;

/*
 * ISOColumn
 * Desc: An object holding a sorted ArrayList (sorted by z value)
 * of ISOObjects in a particular position (x, y) on an isometric map.
 */

/*
 * ISORoom
 * Desc: Essentially a data type.
 * has a full list of all ISOObjects in a specific room/world.
 * The objects themselves have information about their depth
 * and position etc.
 */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.awt.Graphics;

import com.kilobolt.GameWorld.Block;

public class ISOColumn {

	ArrayList<ISOObject> arrayList;

	private final int x, y, isoX, isoY;

	// Constructor
	public ISOColumn(int x, int y) {
		this.x = x;
		this.y = y;

		arrayList = new ArrayList<ISOObject>();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean samePos(ISOColumn col) {
		if (col.getX() == this.x) {
			if (col.getY() == this.y) {
				return true;
			}
		}
		return false;
	}

	// Inner class
	class zComparator implements Comparator<ISOObject> {
		// A Comparator class to compare objects z values
		public int compare(ISOObject obj1, ISOObject obj2) {
			// compare z value
			if (obj1.getZ() == obj2.getZ())
				return -1;
			if (obj1.getZ() < obj2.getZ())
				return -1;
			else
				return 1;
		}
	}

	// Fun /Interactive methods
	public ISOObject getObj(int pos) {
		pos = pos % arrayList.size();
		return arrayList.get(pos);
	}
	
	// Methods
	public void add(ISOObject obj) {
		if (arrayList.size() <= 0) {
			// If there are no other objects in the list ( no need to sort )
			arrayList.add(obj);
		}
		// adds an object to the array list at its correct position
		// ( depends on z value )
		Comparator<ISOObject> c = new zComparator();
		int index;
		index = Collections.binarySearch(arrayList, obj, c);
		if (index < 0)
			index = -(index + 1);
		// index is a negative value of the position it would have taken
		// if it doesn't fit between any two other objects
		// more info in javadocs about Collections.binarySearch()
		arrayList.add(index, obj);
	}

	// Add blocks on top
	public void addBlock(String sprName, boolean solid) {
		int z = 0;
		if (!arrayList.isEmpty()) {
			ISOObject obj = arrayList.get(arrayList.size() - 1);
			z = obj.getZ() + obj.getH();
		}
		Block b = new Block(spriteBank, x, y, z, sprName, solid);
		add(b);
	}
	
	public void addBlock(String sprName, boolean solid, int h, int offset) {
		int z = 0;
		if (!arrayList.isEmpty()) {
			ISOObject obj = arrayList.get(arrayList.size() - 1);
			z = obj.getZ() + obj.getH();
		}
		Block b = new Block(spriteBank, x, y, z, sprName, solid);
		b.setInternalOff(16, offset);
		b.h=h;
		add(b);
	}
	
	
	

	// Add default block on top
	public void addBlock(boolean solid) {
		int z = 0;
		if (!arrayList.isEmpty()) {
			ISOObject obj = arrayList.get(arrayList.size() - 1);
			z = obj.getZ() + obj.getH();
		}
		Block b = new Block(spriteBank, x, y, z, solid);
		add(b);
	}

	// Add blocks on top at pos
	public void addFloor(String sprName, boolean solid) {
		int z = 0;
		if (!arrayList.isEmpty()) {
			ISOObject obj = arrayList.get(arrayList.size() - 1);
			z = obj.getZ() + obj.getH();
			System.out.println("addFloor: " + z + " getZ: " + obj.getZ()
					+ " getH: " + obj.getH());
		}
		ISOFloor f = new ISOFloor(spriteBank, x, y, z, sprName, solid);
		add(f);
	}

	public void addFloor(String sprName, int height, boolean solid) {
		height = height - 5;
		int relheight = 8 + (height * 8 - 1);
		ISOFloor f = new ISOFloor(spriteBank, x, y, relheight, sprName,
				solid);
		add(f);
	}
	
	public ISOFloor addAndGetFloor(String sprName, int voff, boolean solid) {
		int z = 0;
		if (!arrayList.isEmpty()) {
			ISOObject obj = arrayList.get(arrayList.size() - 1);
			z = obj.getZ() + obj.getH();
			System.out.println("addFloor: " + z + " getZ: " + obj.getZ()
					+ " getH: " + obj.getH());
		}
		ISOFloor f = new ISOFloor(spriteBank, x, y, z, sprName, solid);
		f.setInternalOff(f.hoff, voff);
		add(f);
		return f;
	}

	public void reloadSprites() {

		for (ISOObject obj : arrayList) {
			if (obj.getClass() == Block.class) {
				Block b = (Block) obj;
				b.spriteBank = AppletStarter.getCanvas().surf.spriteBank;
				b.loadSprite(b.spriteName);
			}
			
			//HIER MUSS NOCH WAS GEMACHT WERDEN
			
			
			if (obj.getClass() == ISOFloor.class) {
				ISOFloor b = (ISOFloor) obj;
				b.spriteBank = AppletStarter.getCanvas().surf.spriteBank;
				b.loadSprite(b.spriteName);
				if (obj.getClass() == ISOFloor.class) {
					System.out.println("Spieler");
//					b.h = (int) (4 * (Math.pow(2.0, AppletStarter.zoom - 1)));
				}
			}
		}
	}

	public void removeFloors() {
		List<ISOObject> removing = new ArrayList<ISOObject>();
		for (ISOObject obj : arrayList) {
			if (obj instanceof ISOFloor) {
				removing.add(obj);
			}
		}
		for (ISOObject obj : removing) {
			arrayList.remove(obj);
		}
	}
	
	public void removeObject(ISOObject object){
		List<ISOObject> removing = new ArrayList<ISOObject>();
		for (ISOObject obj : arrayList) {
			if (obj == object) {
				removing.add(obj);
			}
		}
		for (ISOObject obj : removing) {
			arrayList.remove(obj);
		}
	}

	// Removet block on top
	public void removeBlock() {
		if (!arrayList.isEmpty()) {
			arrayList.remove(arrayList.size() - 1);
		}
	}

	public void sort() {
		// sort the whole column in z value order ( lowest first )
		if (arrayList.size() <= 1) {
			// An empty list, or a list consisting of 1 value
			// is already sorted :)
			// Do nothing
			return;
		}
		// Sorts the list with the help of Collections.sort()
		// and the zComparator innner class
		Comparator<ISOObject> c = new zComparator();
		Collections.sort(arrayList, c);
	}

	// Paint the ISOObjects of this Column
	public void paintAll(Graphics g) {
		paintAll(g, 0, 0);
	}

	public void paintAll(Graphics g, Screen screen) {
		// paint the column relatively to the screen.
		// screen.hOff();
		// screen.vOff()
//		 paintAll(g, -(screen.x()/32)*32 + screen.hOff() - (screen.x()%32),
//		 -(screen.y()/16)*16 + screen.vOff() - (screen.y()%16));
//		 paintAll(g, -screen.x() + screen.hOff(), -screen.y() + screen.vOff()
//		 );

		 
		Iterator<ISOObject> itr = arrayList.iterator();

		ISOObject objRef = null;
		while (itr.hasNext()) {
			objRef = (ISOObject) itr.next();
			objRef.paintOnScreen(g, screen);
		}
		
	}

	public void paintWrap(Graphics g, Screen screen, int i, int j, int nx,
			int ny, int tw, int th, int width, int height) {
		int hOff = -screen.x() + screen.hOff();
		int vOff = -screen.y() + screen.vOff();

		// Wrap it
		hOff += (nx - this.isoX);
		vOff += (ny - this.isoY);

		paintAll(g, hOff, vOff);
	}

	public void paintAll(Graphics g, int hOff, int vOff) {
		// Invokes all objects paint() method.
		// in correct order. ( Lowest first )
		Iterator<ISOObject> itr = arrayList.iterator();

		while (itr.hasNext()) {
			// Bottoms up, lowest z value object painted first
			ISOObject objRef = (ISOObject) itr.next();
			objRef.paintOff(g, hOff, vOff);
		}
	}

	public void paintRange(Graphics g, int zBegin, int zEnd) {
		// Invokes the paint methods of objects
		// with a z value between zBegin and zEnd.
		// TODO ( this code is unoptimized )
		Iterator<ISOObject> itr = arrayList.iterator();
		while (itr.hasNext()) {
			// Bottoms up, lowest z value object painted first
			ISOObject objRef = (ISOObject) itr.next();
			// Check if the objects z value lands between zBegin and zEnd
			if (objRef.getZ() > zBegin && objRef.getZ() < zEnd) {
				objRef.paint(g);
			}
		}
	}

	public void move(int z) {
		for (ISOObject obj : arrayList) {
			obj.z += z;
		}
	}

	public void set(int z) {
		for (ISOObject obj : arrayList) {
			obj.z = z;
		}
	}

	public void clearToTop(int z) {
		ISOObject top = arrayList.get(arrayList.size() - 1);
		int i = 2;
		while (top instanceof ISOFloor) {
			top = arrayList.get(arrayList.size() - i);
			i++;
		}
		arrayList.clear();
		arrayList.add(top);
		top.z = z;
	}

	public void pile(int z) {
		int prevH = 0;
		for (ISOObject obj : arrayList) {
			obj.z = (z + prevH);
			prevH += obj.getH();
		}
	}

	public String printPos() {
		return "X:" + this.x + " | Y:" + this.y;
	}

	public void clear() {
		// empty all blocks from column
		arrayList.clear();
	}

}