package org.processmining.plugins.pmenum;

import java.util.ArrayList;

public class ModelTransitionIterator {
	private ArrayList<Double[]> modelTransitionList;
	private int index;
	
	public ModelTransitionIterator(ModelTransitionTable table) {
		modelTransitionList = table.getModelTransitionList();
		index = 0;
	}
	
	public boolean hasNext() {
		if(index < modelTransitionList.size() - 1) {
			return true;
		} else {
			return false;
		}
	}
	
	public Double[] getNext() {
		index++;
		return modelTransitionList.get(index);
	}
	
	public boolean hasPrev() {
		if(index > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public Double[] getPrev() {
		index--;
		return modelTransitionList.get(index);
	}
	
	public Double[] getThis() {
		return modelTransitionList.get(index);
	}

}
