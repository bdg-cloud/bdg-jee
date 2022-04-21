package fr.legrain.lib.data;

import javax.swing.event.EventListenerList;

import org.eclipse.swt.widgets.Control;

/**
 * <p>Title: </p>
 * <p>Description: Etat de l'objet gérant la base de données.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Le Grain SA</p>
 * @author Le Grain SA
 * @version 1.0
 */

public class ModeObjetEcran extends ModeObjet {
	
	private Control focusCourantSWT = null;
	
	public void setMode(EnumModeObjet mode) {
		if (this.mode!=mode){
			this.mode = mode;
			switch (mode) {
			case C_MO_CONSULTATION :
			case C_MO_EDITION :  
			case C_MO_INSERTION :
				setFocusCourantSWT(null);
				break;
			default:
				break;
			}
		}	  
		fireChangementMode(new ChangeModeEvent(this,getMode(),mode));
	}
	
	public void setFocusCourantSWTHorsApplication(Control focusCourantSWT) {
		this.focusCourantSWT = focusCourantSWT;		
	}
	
	public Control getFocusCourantSWT() {
		return focusCourantSWT;
	}

	public void setFocusCourantSWT(Control focusCourantSWT) {
		this.focusCourantSWT = focusCourantSWT;
	}
	
	public void destroy(){		
		super.destroy();
		focusCourantSWT = null;		
	}
	
	public boolean dataSetEnModif() {
		if(getMode()==EnumModeObjet.C_MO_EDITION
				|| getMode()==EnumModeObjet.C_MO_IMPORTATION
				|| getMode()==EnumModeObjet.C_MO_INSERTION)
			return true;
		else
			return false;
	}
}
