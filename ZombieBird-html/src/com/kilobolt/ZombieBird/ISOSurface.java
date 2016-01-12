package com.kilobolt.ZombieBird;

/*
 * ISOSurface
 * Desc: holds a 2D array of ISOColumns
 * consisting of ISOObjects.
 *
 * the surface defines a complete isometric surface with height.
 * essentially a "room".
 */

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import com.kilobolt.GameWorld.Block;
import com.kilobolt.GameWorld.Block.BlockType;

public class ISOSurface {

	/** Instance variables */
	ISOColumn[][] colArray;
	final int WIDTH;
	final int HEIGHT;
	
	int vOff = 0;
	int hOff = 0;


	/** Constructors */
	ISOSurface(int w, int h) {
		WIDTH = w;
		HEIGHT = h;
		vOff = 0; // vertical Offset
		hOff = 0;// HEIGHT * Tile_W / 2; // horizontal offset
		
		colArray = new ISOColumn[WIDTH][HEIGHT];

		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				colArray[i][j] = null;
			}
		}
	}
	
	public void clearToTop(int z) {
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				colArray[i][j].clearToTop(z);
			}
		}
	}

	public ISOColumn getColumn(int x, int y) {
			x = x%WIDTH;
			y = y%HEIGHT;
		
		return colArray[x][y];
	}

	public ISOColumn[] getColumns() {

//		
//		// ArrayList to store our return array
//		ArrayList<ISOColumn> array = new ArrayList<ISOColumn>(sw * sh);
//
//		for (int i = 0; i < dist; i++) {
//			for (int j = 0; j < dist; j++) {
//				array.add(colArray[hWrap(iStart + i)][vWrap(jStart + j)]);
//			}
//		}
//
//		ISOColumn[] cols = new ISOColumn[array.size()];
//		array.toArray(cols);
//		return cols;
	}

	// Functionality methods
	public void addSurface(int height, Block.BlockType type) {
		// Adds a block in each column at a specified height ( z value )
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				colArray[i][j].add(new Block(i, j, type));
			}
		}
	}

	public void addGrass(int height) {
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
					colArray[i][j].add(new Block(i, j, Block.BlockType.GRASS));
			}
		}
	}

	// Paint methods
	public void paintAll(Graphics g) {
		// Paint all Columns in (more or less) depth order.
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				colArray[i][j].paintAll(g);
			}
		}
	}// paintAll() Method ends

	public void paintBox(Graphics g, int x, int y, int w, int h) {
		// Paint a specific box from the surface
		// shows up as a diamond shape in isometrci view
		for (int i = x; i < w; i++) {
			for (int j = y; j < h; j++) {
				colArray[hWrap(i)][vWrap(j)].paintAll(g);
			}
		}
	}// paintBox() Method ends

	public void paintScreen(Graphics g, Screen scr) {
		// Screen is a class that holds data
		// start position of the screen, and its width and height
		// paint only the isometric tiles within the canvas

		// We need to even out the screen size to fit each isometric tile
		// to avoid small gaps
		int sx = scr.x();
		int sy = scr.y();
		int sw = scr.width();
		int sh = scr.height();
		

		int iStart = isoToI(sx, sy) - 1;
		int jStart = isoToJ(sx, sy);
		int iMax = isoToI(sx + sw, sy + sh) + 1;
		int jMax = isoToJ(sx, sy + sh) + 2;
		int jMin = isoToJ(sx + sw, sy);

		boolean nBump = false, mBump = false;
		int n = 0, nStart = 0, nBuffer = 1;
		int m = 1, mStart = 0, mBuffer = 0;

		// Fix for negative screen values
		if (sx < 0) {
			m = 3;
		}

		for (int i = iStart; i < iMax; i++) {
			for (int j = jStart - n; j < jStart + m; j++) {
				// paint the column
				colArray[hWrap(i)][vWrap(j)].paintAll(g);

			}
			// adjust m and n to keep us within the screen

			// adjust n
			if (!nBump) {
				// we have not yet reached the lowest j point
				// increment n to go even lower next iteration
				n++;
				// Check if we have reached the lowest j point
				if ((jStart - n) == jMin) {
					nBump = true;
					// System.out.println("Bump N");
				}
			} else {
				// we have reached the deepest j and are now going back
				// start decreasing after the buffer is gone
				if (nBuffer > 0) {
					nBuffer--;
				} else {
					// The buffer is gone, start decreasing n each iteration
					n--;
				}
			}

			// adjust m
			if (!mBump) {
				// we have not yet reached the HIGHEST j point
				// increasee m to go even higher next iteration
				m++;
				// Check if we have reached the highest j point
				if ((jStart + m) == jMax) {
					mBump = true;
					// System.out.println("Bump M");
				}
			} else {
				// we have reached the maximum j point
				// and are now moving back.
				// start decreasing m after the buffer is gone
				if (mBuffer > 0) {
					mBuffer--;
				} else {
					// The Buffer is gone
					// decrease m each iteration
					m--;
				}
			}
		} // for loop ends
		
//		paintHUD(g,scr);

	}// paintScreen() Method ends

	/**
	 * This is the most signifcant of the paint methods!!
	 * 
	 * @param g
	 * @param scr
	 *            A Screen is basically just a Dimension object.
	 * @param relative
	 */
	public void paintScreen(Graphics g, Screen scr, boolean relative) {
		// Screen is a class that holds data:
		// start position of the screen, and its width and height
		// paint only the isometric tiles within the screen
		// RELATIVE to the screen and window position!
		// Only the paint method get influenced by this
		// ( it adds horizontal and vertical offsets )

		if (relative == false) {
			paintScreen(g, scr);
			return;
		}

		// We need to even out the screen size to fit each isometric tile
		// to avoid small gaps
		int sx = scr.x();
		int sy = scr.y();
		int sw = scr.width();
		int sh = scr.height();

		int iStart = isoToI(sx, sy) - 4;
		int jStart = isoToJ(sx, sy) - 1;
		int iMax = isoToI(sx + sw, sy + sh) + 1;
		int jMax = isoToJ(sx, sy + sh) + 2;
		int jMin = isoToJ(sx + sw, sy) - 1;

		boolean nBump = false, mBump = false;
		int n = 0, nStart = 0, nBuffer = 6;
		int m = 0, mStart = 0, mBuffer = 6;

		// Fix for negative screen values

		for (int i = iStart; i < iMax; i++) {
			for (int j = jStart - n; j < jStart + m; j++) {
				// paint the column
				// RELATIVELY
				// colArray[ hWrap(i) ][ vWrap(j) ].paintAll(g, scr);
				colArray[hWrap(i)][vWrap(j)].paintWrap(g, scr, i, j,
						isoX(i, j), isoY(i, j), Tile_W, Tile_H, WIDTH, HEIGHT);
			}
			// adjust m and n to keep us within the screen

			// adjust n
			if (!nBump) {
				// we have not yet reached the lowest j point
				// increment n to go even lower next iteration
				n++;
				// Check if we have reached the lowest j point
				if ((jStart - n) == jMin) {
					nBump = true;
					// System.out.println("Bump N");
				}
			} else {
				// we have reached the deepest j and are now going back
				// start decreasing after the buffer is gone
				if (nBuffer > 0) {
					nBuffer--;
				} else {
					// The buffer is gone, start decreasing n each iteration
					n--;
				}
			}

			// adjust m
			if (!mBump) {
				// we have not yet reached the HIGHEST j point
				// increase m to go even higher next iteration
				m++;
				// Check if we have reached the highest j point
				if ((jStart + m) == jMax) {
					mBump = true;
					// System.out.println("Bump M");
				}
			} else {
				// we have reached the maximum j point
				// and are now moving back.
				// start decreasing m after the buffer is gone
				if (mBuffer > 0) {
					mBuffer--;
				} else {
					// The Buffer is gone
					// decrease m each iteration
					m--;
				}
			}
		} // for loop ends

//		paintHUD(g,scr);
		
	}// end of paintScreen(Graphics, Screen, boolean) Method
	
	// methods to convert position into isometric position
	private int isoX(int x, int y) {
		// convert x into isometric x
		int isoX;
		isoX = (int) ((x * Tile_W / 2) - (y * Tile_W / 2));

		return isoX;
	}

	private int isoY(int x, int y) {
		// convert y into isometric y
		int isoY;
		isoY = (int) ((x * Tile_H / 2) + (y * Tile_H / 2));

		return isoY;
	}

}