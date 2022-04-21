package fr.legrain.servicewebexterne.service.divers;

import com.opencsv.bean.ColumnPositionMappingStrategy;

public class CustomCSVMappingStrategy<T> extends ColumnPositionMappingStrategy<T> {
	
	private String[] head = new String[] {};

    @Override
    public String[] generateHeader() {
        return getHead();
    }
    
	public String[] getHead() {
		return head;
	}

	public void setHead(String[] head) {
		this.head = head;
	}
}
