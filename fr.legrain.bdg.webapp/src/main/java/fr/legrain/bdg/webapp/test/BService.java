package fr.legrain.bdg.webapp.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
 

public class BService implements Serializable {

	private static final long serialVersionUID = -7442506301755842723L;
	
	private static final List<B> allValues = new ArrayList<B>();
	
	public BService() {
		init();
	}

    public void init() {
    	if(allValues.isEmpty()) {
			allValues.add(new B("aa1", "ab"));
			allValues.add(new B("bb2", "abc"));
			allValues.add(new B("ab3", "abcd"));
    	}
    }

	public List<B> getAllValues() {
		return allValues;
	}
    
}
