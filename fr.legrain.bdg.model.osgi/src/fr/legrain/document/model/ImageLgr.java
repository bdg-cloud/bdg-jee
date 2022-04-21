package fr.legrain.document.model;

import java.io.Serializable;

public class ImageLgr implements Serializable{
	 
    private String displayName; 
    private String name;
     
    public ImageLgr() {}
 
    public ImageLgr(  String name,String displayName) {
        this.displayName = displayName;
        this.name = name;
    }
 

 
    public String getDisplayName() {
        return displayName;
    }
 
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
     
    @Override
    public String toString() {
        return name;
    }
}
