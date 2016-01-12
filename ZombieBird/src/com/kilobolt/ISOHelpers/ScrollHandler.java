package com.kilobolt.ISOHelpers;

import java.util.ArrayList;
import java.util.List;

public class ScrollHandler<T> {

	private int pointer;
	private int amountToShow;

	private List<T> objects;

	public ScrollHandler() {
		pointer = 0;
		amountToShow = 0;
		objects = new ArrayList<T>();
	}

	public ScrollHandler(int amountToShow) {
		super();
		setAmountToShow(amountToShow);
	}

	public void down() {
		down(1);
	}

	public void down(int a) {
		move(-Math.abs(a));
	}

	public void up() {
		up(1);
	}

	public void up(int a) {
		move(a);
	}

	public void move(int a) {
		if (a < 0) {
			if (a + pointer < 0) {
				pointer = 0;
			} else {
				pointer = a + pointer;
			}
		} else {
			if (a + pointer > objects.size() - 1) {
				if (objects.size() > 0) {
					pointer = objects.size() - 1;
				} else {
					pointer = 0;
				}
			} else {
				pointer = a + pointer;
			}
		}
	}

	public void addObject(T obj) {
		objects.add(obj);
	}

	public T getObject() {
		return objects.get(pointer);
	}

	public void removeObject(int pos) {
		objects.remove(pos);
		move(0);
	}

	public void removeObject(String obj) {
		objects.remove(obj);
		move(0);
	}

	public void setObject(List<T> newList) {
		if (newList != null) {
			if (newList.size() < pointer + 1) {
				if (newList.size() > 0) {
					pointer = newList.size() - 1;
				} else {
					pointer = 0;
				}
			}
			objects = newList;
		}
	}

	public void setAmountToShow(int max) {
		if (max >= 0) {
			this.amountToShow = max;
		}
	}

	public int getAmountToShow() {
		return this.amountToShow;
	}

	public List<T> getObjects() {
		List<T> back = new ArrayList<T>();

		for (int i = pointer; i < amountToShow + pointer; i++) {
			if (i < objects.size() && i >= 0) {
				back.add(objects.get(i));
			}
		}

		return back;
	}

}
