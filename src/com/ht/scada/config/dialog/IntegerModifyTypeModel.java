package com.ht.scada.config.dialog;

public class IntegerModifyTypeModel {
	private int base;// 基值
	private int type = 0;// 0不变， 1递增，-1递减
	private int interval = 1;
	
	private boolean fanwei = false;
	private int start;
	private int end;

	public int getBase() {
		return base;
	}

	public void setBase(int base) {
		this.base = base;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public boolean isFanwei() {
		return fanwei;
	}

	public void setFanwei(boolean fanwei) {
		this.fanwei = fanwei;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}
	
	
}
